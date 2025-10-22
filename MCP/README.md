# Input API MCP Server

This MCP (Model Content Protocol) server provides access to Input API API functionality through HTTP, HTTPS, and STDIO transport modes.

## Features

- transport mode support (HTTP and STDIO)
- Dynamic configuration through HTTP headers
- Automatic tool generation from API documentation

## Building the Project

1. Ensure you have Go 1.24.6 or later installed
2. Clone the repository
3. Build the project:

```bash
go build -o mcp-server
```

## Running the Server

The server can run in three modes based on the **TRANSPORT** environment variable:

### HTTP Mode

To run in HTTP mode, set the transport environment variable to "http" or "HTTP":

```bash
export TRANSPORT="http"  # or "HTTP" or "HTTPS"
export PORT="8181"       # required
```

Run the server:
```bash
./mcp-server
```

#### Required Environment Variables for HTTP Mode:
- `TRANSPORT`: Set to "HTTP" **(Required)**
- `PORT`: Server port **(Required)**

#### Configuration through HTTP Headers:
In HTTP mode, API configuration is provided via HTTP headers for each request:
- `API_BASE_URL`: **(Required)** Base URL for the API
- `BEARER_TOKEN`: Bearer token for authentication
- `API_KEY`: API key for authentication
- `BASIC_AUTH`: Basic authentication credentials

Cursor mcp.json settings:

{
  "mcpServers": {
    "your-mcp-server-http": {
      "url": "http://<host>:<port>/mcp",
      "headers": {
        "API_BASE_URL": "https://your-api-base-url",
        "BEARER_TOKEN": "your-bearer-token"
      }
    }
  }
}

The server will start on the configured port with the following endpoints:
- `/mcp`: HTTP endpoint for MCP communication (requires API_BASE_URL header)
- `/`: Health check endpoint

**Note**: At least one authentication header (BEARER_TOKEN, API_KEY, or BASIC_AUTH) should be provided unless the API explicitly doesn't require authentication.

### HTTPS Mode

To run in HTTPS mode, set the transport environment variable to "https" or "HTTPS":

```bash
export TRANSPORT="https"  # or "HTTPS"
export PORT="8443"        # required
export CERT_FILE="./certs/cert.pem"  # required
export KEY_FILE="./certs/key.pem"    # required
```

Run the server:
```bash
./mcp-server
```

#### Required Environment Variables for HTTPS Mode:
- `TRANSPORT`: Set to "HTTPS" **(Required)**
- `PORT`: Server port **(Required)**
- `CERT_FILE`: Path to SSL certificate file **(Required)**
- `KEY_FILE`: Path to SSL private key file **(Required)**

#### Configuration through HTTP Headers:
In HTTPS mode, API configuration is provided via HTTP headers for each request:
- `API_BASE_URL`: **(Required)** Base URL for the API
- `BEARER_TOKEN`: Bearer token for authentication
- `API_KEY`: API key for authentication
- `BASIC_AUTH`: Basic authentication credentials

Cursor mcp.json settings:

{
  "mcpServers": {
    "your-mcp-server-https": {
      "url": "https://<host>:<port>/mcp",
      "headers": {
        "API_BASE_URL": "https://your-api-base-url",
        "BEARER_TOKEN": "your-bearer-token"
      }
    }
  }
}

The server will start on the configured port with the following endpoints:
- `/mcp`: HTTPS endpoint for MCP communication (requires API_BASE_URL header)
- `/`: Health check endpoint

**Note**: At least one authentication header (BEARER_TOKEN, API_KEY, or BASIC_AUTH) should be provided unless the API explicitly doesn't require authentication.

```

### STDIO Mode

To run in STDIO mode, either set the transport environment variable to "stdio" or leave it unset (default):

```bash
export TRANSPORT="stdio"  # or leave unset for default
export API_BASE_URL="https://your-api-base-url"
export BEARER_TOKEN="your-bearer-token"
```

Run the server:
```bash
./mcp-server
```

#### Required Environment Variables for STDIO Mode:
- `TRANSPORT`: Set to "stdio" or leave unset (default)
- `API_BASE_URL`: Base URL for the API **(Required)**
- `BEARER_TOKEN`: Bearer token for authentication
- `API_KEY`: API key for authentication  
- `BASIC_AUTH`: Basic authentication credentials

**Note**: At least one authentication environment variable (BEARER_TOKEN, API_KEY, or BASIC_AUTH) should be provided unless the API explicitly doesn't require authentication.

Cursor mcp.json settings:

{
  "mcpServers": {
	"your-mcp-server-stdio": {
		"command": "<path-to-binary>/<mcpserver-binary-name>",
		"env": {
			"API_BASE_URL": "<api-base-url>",
			"BEARER_TOKEN": "<token>"
		}
	}
  }
}

## Environment Variable Case Sensitivity

The server supports both uppercase and lowercase transport environment variables:
- `TRANSPORT` (uppercase) - checked first
- `transport` (lowercase) - fallback if uppercase not set

Valid values: "http", "HTTP", "https", "HTTPS", "stdio", or unset (defaults to STDIO)

## Authentication

### HTTP Mode
Authentication is provided through HTTP headers on each request:
- `BEARER_TOKEN`: Bearer token
- `API_KEY`: API key
- `BASIC_AUTH`: Basic authentication

### STDIO Mode
Authentication is provided through environment variables:
- `BEARER_TOKEN`: Bearer token
- `API_KEY`: API key
- `BASIC_AUTH`: Basic authentication

## Health Check

When running in HTTP mode, you can check server health at the root endpoint (`/`).
Expected response: `{"status":"ok"}`

## Transport Modes Summary

### HTTP Mode (TRANSPORT=http or TRANSPORT=HTTP)
- Uses streamable HTTP server
- Configuration provided via HTTP headers for each request
- Requires API_BASE_URL header for each request
- Endpoint: `/mcp`
- Port configured via PORT environment variable (defaults to 8080)

### HTTPS Mode (TRANSPORT=https or TRANSPORT=HTTPS)
- Uses streamable HTTPS server with SSL/TLS encryption
- Configuration provided via HTTP headers for each request
- Requires API_BASE_URL header for each request
- Endpoint: `/mcp`
- Port configured via PORT environment variable (defaults to 8443)
- **Requires SSL certificate and private key files (CERT_FILE and KEY_FILE)**

### STDIO Mode (TRANSPORT=stdio or unset)
- Uses standard input/output for communication
- Configuration through environment variables only
- Requires API_BASE_URL environment variable
- Suitable for command-line usage

