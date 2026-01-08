package main

import (
	"context"
	"log"
	"net"
	"net/http"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/mark3labs/mcp-go/server"
	"github.com/input-api/mcp-server/config"
)

func main() {
	cfg, err := config.LoadAPIConfig()
	if err != nil {
		log.Fatalf("Failed to load config: %v", err)
	}

	// Check transport environment variable (both uppercase and lowercase)
	transport := os.Getenv("TRANSPORT")
	if transport == "" {
		transport = os.Getenv("transport")
	}
	sigChan := make(chan os.Signal, 1)
	signal.Notify(sigChan, syscall.SIGINT, syscall.SIGTERM)

	// HTTP/HTTPS Mode - if transport is "http", "HTTP", "https", or "HTTPS"
	if transport == "http" || transport == "HTTP" || transport == "https" || transport == "HTTPS" {
		port := cfg.Port
		if port == "" {
			log.Fatalf("PORT environment variable is required for HTTP/HTTPS mode. Please set PORT environment variable.")
		}

		// Determine if HTTPS mode and normalize transport
		isHTTPS := transport == "https" || transport == "HTTPS"
		if isHTTPS {
			transport = "HTTPS"
		} else {
			transport = "HTTP"
		}
		
		log.Printf("Running in %s mode on port %s", transport, port)

		mux := http.NewServeMux()
		mux.HandleFunc("/mcp", func(w http.ResponseWriter, r *http.Request) {
			// Read headers for dynamic config
			apiCfg := &config.APIConfig{
				BaseURL:     r.Header.Get("API_BASE_URL"),
				BearerToken: r.Header.Get("BEARER_TOKEN"),
				APIKey:      r.Header.Get("API_KEY"),
				BasicAuth:   r.Header.Get("BASIC_AUTH"),
			}

			if apiCfg.BaseURL == "" {
				http.Error(w, "Missing API_BASE_URL header", http.StatusBadRequest)
				return
			}

			log.Printf("Incoming HTTP request - BaseURL: %s", apiCfg.BaseURL)

			// Create MCP server for this request
			mcpSrv := createMCPServer(apiCfg, transport)
			handler := server.NewStreamableHTTPServer(mcpSrv, server.WithHTTPContextFunc(
				func(ctx context.Context, req *http.Request) context.Context {
					return context.WithValue(ctx, "apiConfig", apiCfg)
				},
			))

			handler.ServeHTTP(w, r)
		})

		mux.HandleFunc("/", func(w http.ResponseWriter, _ *http.Request) {
			w.Header().Set("Content-Type", "application/json")
			w.Write([]byte(`{"status":"ok"}`))
		})

		addr := net.JoinHostPort("0.0.0.0", port)
		httpServer := &http.Server{Addr: addr, Handler: mux}

		go func() {
			// Check if HTTPS mode
			if isHTTPS {
				certFile := os.Getenv("CERT_FILE")
				keyFile := os.Getenv("KEY_FILE")
				
				if certFile == "" || keyFile == "" {
					log.Fatalf("CERT_FILE and KEY_FILE environment variables are required for HTTPS mode")
				}
				
				log.Printf("Starting HTTPS server on %s", addr)
				if err := httpServer.ListenAndServeTLS(certFile, keyFile); err != http.ErrServerClosed {
					log.Fatalf("HTTPS server error: %v", err)
				}
			} else {
				log.Printf("Starting HTTP server on %s", addr)
				if err := httpServer.ListenAndServe(); err != http.ErrServerClosed {
					log.Fatalf("HTTP server error: %v", err)
				}
			}
		}()

		<-sigChan
		log.Println("Shutdown signal received")

		ctx, cancel := context.WithTimeout(context.Background(), 5*time.Second)
		defer cancel()
		if err := httpServer.Shutdown(ctx); err != nil {
			log.Printf("Shutdown error: %v", err)
		} else {
			log.Println("HTTP server shutdown complete")
		}
		return
	}

	// STDIO Mode - default when no transport or transport is "stdio"
	log.Println("Running in STDIO mode")
	mcp := createMCPServer(cfg, "STDIO")
	go func() {
		if err := server.ServeStdio(mcp); err != nil {
			log.Fatalf("STDIO error: %v", err)
		}
	}()
	<-sigChan
	log.Println("Received shutdown signal. Exiting STDIO mode.")
}

func createMCPServer(cfg *config.APIConfig, mode string) *server.MCPServer {
	mcp := server.NewMCPServer("Input API", "1.0.0",
		server.WithToolCapabilities(true),
		server.WithRecovery(),
	)

	tools := GetAll(cfg)
	log.Printf("Loaded %d tools for %s mode", len(tools), mode)

	for _, tool := range tools {
		mcp.AddTool(tool.Definition, tool.Handler)
	}

	return mcp
}