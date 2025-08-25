package main

import (
	"github.com/input-api/mcp-server/config"
	"github.com/input-api/mcp-server/models"
	tools_openapi "github.com/input-api/mcp-server/tools/openapi"
	tools_openapi_type_json_yaml "github.com/input-api/mcp-server/tools/openapi_type_json_yaml"
)

func GetAll(cfg *config.APIConfig) []models.Tool {
	return []models.Tool{
		tools_openapi.CreateGet_openapiTool(cfg),
		tools_openapi_type_json_yaml.CreateGet_openapi_typejsonyamlTool(cfg),
	}
}
