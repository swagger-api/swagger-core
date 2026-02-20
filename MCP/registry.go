package main

import (
	"github.com/input-api/mcp-server/config"
	"github.com/input-api/mcp-server/models"
	tools_auth "github.com/input-api/mcp-server/tools/auth"
	tools_api "github.com/input-api/mcp-server/tools/api"
	tools_mcp "github.com/input-api/mcp-server/tools/mcp"
	tools_openapi "github.com/input-api/mcp-server/tools/openapi"
	tools_openapi_type_json_yaml "github.com/input-api/mcp-server/tools/openapi_type_json_yaml"
	tools_config "github.com/input-api/mcp-server/tools/config"
	tools_authentication "github.com/input-api/mcp-server/tools/authentication"
)

func GetAll(cfg *config.APIConfig) []models.Tool {
	return []models.Tool{
		tools_auth.CreateHead_basic_authTool(cfg),
		tools_auth.CreateHead_bearer_tokenTool(cfg),
		tools_api.CreateGetTool(cfg),
		tools_mcp.CreateGet_mcpTool(cfg),
		tools_openapi.CreateGet_openapiTool(cfg),
		tools_openapi_type_json_yaml.CreateGet_openapi_typejsonyamlTool(cfg),
		tools_config.CreateHead_api_base_urlTool(cfg),
		tools_authentication.CreateHead_api_keyTool(cfg),
	}
}
