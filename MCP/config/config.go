package config

import (
	"fmt"
	"os"
)

type APIConfig struct {
	BaseURL     string
	BearerToken string // For OAuth2/Bearer authentication
	APIKey      string // For API key authentication
	BasicAuth   string // For basic authentication
	Port        string // For server port configuration
}

func LoadAPIConfig() (*APIConfig, error) {
	// Check port environment variable (both uppercase and lowercase)
	port := os.Getenv("PORT")
	if port == "" {
		port = os.Getenv("port")
	}
	
	baseURL := os.Getenv("API_BASE_URL")
	
	// Check transport environment variable (both uppercase and lowercase)
	transport := os.Getenv("TRANSPORT")
	if transport == "" {
		transport = os.Getenv("transport")
	}
	
	// For STDIO mode (transport is not "http"/"HTTP"/"https"/"HTTPS"), API_BASE_URL is required from environment
	if transport != "http" && transport != "HTTP" && transport != "https" && transport != "HTTPS" && baseURL == "" {
		return nil, fmt.Errorf("API_BASE_URL environment variable not set")
	}
	
	// For HTTP/HTTPS mode (transport is "http"/"HTTP"/"https"/"HTTPS"), API_BASE_URL comes from headers
	// so we don't require it from environment variables

	return &APIConfig{
		BaseURL:     baseURL,
		BearerToken: os.Getenv("BEARER_TOKEN"),
		APIKey:      os.Getenv("API_KEY"),
		BasicAuth:   os.Getenv("BASIC_AUTH"),
		Port:        port,
	}, nil
}


