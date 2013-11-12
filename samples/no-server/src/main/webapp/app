{
    "basePath": "http://localhost/rest",
    "swaggerVersion": "1.1",
    "apiVersion": "1.0",
    "resourcePath": "/app",
    "apis": [
        {
            "path": "/app",
            "operations": [
                {
                    "httpMethod": "GET",
                    "summary": "List all containers.",
                    "nickname": "getContainers",
                    "responseClass": "Containers",
                    "parameters": [
                        {
                            "name": "include_properties",
                            "description": "Return all properties of the container, if any.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "List the names of the available containers in this storage. Use 'include_properties' to include any properties of the containers."
                },
                {
                    "httpMethod": "POST",
                    "summary": "Create one or more containers.",
                    "nickname": "createContainers",
                    "responseClass": "Containers",
                    "parameters": [
                        {
                            "name": "data",
                            "description": "Array of containers to create.",
                            "allowMultiple": false,
                            "dataType": "Containers",
                            "paramType": "body",
                            "required": true
                        },
                        {
                            "name": "check_exist",
                            "description": "If true, the request fails when the container to create already exists.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data should be a single container definition or an array of container definitions."
                },
                {
                    "httpMethod": "DELETE",
                    "summary": "Delete one or more containers.",
                    "nickname": "deleteContainers",
                    "responseClass": "Containers",
                    "parameters": [
                        {
                            "name": "data",
                            "description": "Array of containers to delete.",
                            "allowMultiple": false,
                            "dataType": "Containers",
                            "paramType": "body",
                            "required": true
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data should be a single container definition or an array of container definitions."
                }
            ],
            "description": "Operations available for File Storage Service."
        },
        {
            "path": "/app/{container}/",
            "operations": [
                {
                    "httpMethod": "GET",
                    "summary": "List the container's properties, including folders and files.",
                    "nickname": "getContainer",
                    "responseClass": "FoldersAndFiles",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container you want to retrieve the contents.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "include_properties",
                            "description": "Return all properties of the container, if any.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "include_folders",
                            "description": "Include folders in the returned listing.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": true
                        },
                        {
                            "name": "include_files",
                            "description": "Include files in the returned listing.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": true
                        },
                        {
                            "name": "full_tree",
                            "description": "List the contents of all sub-folders as well.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "zip",
                            "description": "Return the zipped content of the folder.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Use 'include_properties' to get properties of the container. Use the 'include_folders' and/or 'include_files' to return a listing."
                },
                {
                    "httpMethod": "POST",
                    "summary": "Add folders and/or files to the container.",
                    "nickname": "createContainer",
                    "responseClass": "FoldersAndFiles",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container you want to put the contents.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "url",
                            "description": "The full URL of the file to upload.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "query",
                            "required": false
                        },
                        {
                            "name": "extract",
                            "description": "Extract an uploaded zip file into the container.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "clean",
                            "description": "Option when 'extract' is true, clean the current folder before extracting files and folders.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "check_exist",
                            "description": "If true, the request fails when the file or folder to create already exists.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "data",
                            "description": "Array of folders and/or files.",
                            "allowMultiple": false,
                            "dataType": "FoldersAndFiles",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data as an array of folders and/or files."
                },
                {
                    "httpMethod": "PATCH",
                    "summary": "Update properties of the container.",
                    "nickname": "updateContainerProperties",
                    "responseClass": "Container",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container you want to put the contents.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "data",
                            "description": "An array of container properties.",
                            "allowMultiple": false,
                            "dataType": "Container",
                            "paramType": "body",
                            "required": true
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data as an array of container properties."
                },
                {
                    "httpMethod": "DELETE",
                    "summary": "Delete the container or folders and/or files from the container.",
                    "nickname": "deleteContainer",
                    "responseClass": "FoldersAndFiles",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container you want to delete from.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "data",
                            "description": "An array of folders and/or files to delete from the container.",
                            "allowMultiple": false,
                            "dataType": "FoldersAndFiles",
                            "paramType": "body",
                            "required": true
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Careful, this deletes the requested container and all of its contents, unless there are posted specific folders and/or files."
                }
            ],
            "description": "Operations on containers."
        },
        {
            "path": "/app/{container}/{file_path}",
            "operations": [
                {
                    "httpMethod": "GET",
                    "summary": "Download the file contents and/or its properties.",
                    "nickname": "getFile",
                    "responseClass": "File",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "Name of the container where the file exists.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "file_path",
                            "description": "Path and name of the file to retrieve.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "include_properties",
                            "description": "Return properties of the file.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "content",
                            "description": "Return the content as base64 of the file, only applies when 'include_properties' is true.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "download",
                            "description": "Prompt the user to download the file from the browser.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container, folder, or file does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "By default, the file is streamed to the browser. Use the 'download' parameter to prompt for download.\n             Use the 'include_properties' parameter (optionally add 'content' to include base64 content) to list properties of the file."
                },
                {
                    "httpMethod": "POST",
                    "summary": "Create a new file.",
                    "nickname": "createFile",
                    "responseClass": "File",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "Name of the container where the file exists.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "file_path",
                            "description": "Path and name of the file to create.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "check_exist",
                            "description": "If true, the request fails when the file to create already exists.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false
                        },
                        {
                            "name": "properties",
                            "description": "Properties of the file.",
                            "allowMultiple": false,
                            "dataType": "File",
                            "paramType": "body",
                            "required": false
                        },
                        {
                            "name": "content",
                            "description": "The content of the file.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container or folder does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data should be the contents of the file or an object with file properties."
                },
                {
                    "httpMethod": "PUT",
                    "summary": "Update content of the file.",
                    "nickname": "updateFile",
                    "responseClass": "File",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "Name of the container where the file exists.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "file_path",
                            "description": "Path and name of the file to update.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "content",
                            "description": "The content of the file.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container, folder, or file does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data should be the contents of the file."
                },
                {
                    "httpMethod": "PATCH",
                    "summary": "Update properties of the file.",
                    "nickname": "updateFileProperties",
                    "responseClass": "File",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "Name of the container where the file exists.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "file_path",
                            "description": "Path and name of the file to update.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "properties",
                            "description": "Properties of the file.",
                            "allowMultiple": false,
                            "dataType": "File",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container, folder, or file does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data should be the file properties."
                },
                {
                    "httpMethod": "DELETE",
                    "summary": "Delete the file.",
                    "nickname": "deleteFile",
                    "responseClass": "File",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "Name of the container where the file exists.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "file_path",
                            "description": "Path and name of the file to delete.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container, folder, or file does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Careful, this removes the given file from the storage."
                }
            ],
            "description": "Operations on individual files."
        },
        {
            "path": "/app/{container}/{folder_path}/",
            "operations": [
                {
                    "httpMethod": "GET",
                    "summary": "List the folder's properties, or sub-folders and files.",
                    "nickname": "getFolder",
                    "responseClass": "FoldersAndFiles",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container from which you want to retrieve contents.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "folder_path",
                            "description": "The path of the folder you want to retrieve. This can be a sub-folder, with each level separated by a '/'",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "include_properties",
                            "description": "Return all properties of the folder, if any.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "include_folders",
                            "description": "Include folders in the returned listing.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": true
                        },
                        {
                            "name": "include_files",
                            "description": "Include files in the returned listing.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": true
                        },
                        {
                            "name": "full_tree",
                            "description": "List the contents of all sub-folders as well.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "zip",
                            "description": "Return the zipped content of the folder.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container or folder does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Use with no parameters to get properties of the folder or use the 'include_folders' and/or 'include_files' to return a listing."
                },
                {
                    "httpMethod": "POST",
                    "summary": "Create one or more sub-folders and/or files.",
                    "nickname": "createFolder",
                    "responseClass": "FoldersAndFiles",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container where you want to put the contents.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "folder_path",
                            "description": "The path of the folder where you want to put the contents. This can be a sub-folder, with each level separated by a '/'",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "url",
                            "description": "The full URL of the file to upload.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "query",
                            "required": false
                        },
                        {
                            "name": "extract",
                            "description": "Extract an uploaded zip file into the folder.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "clean",
                            "description": "Option when 'extract' is true, clean the current folder before extracting files and folders.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "check_exist",
                            "description": "If true, the request fails when the file or folder to create already exists.",
                            "allowMultiple": false,
                            "dataType": "boolean",
                            "paramType": "query",
                            "required": false,
                            "defaultValue": false
                        },
                        {
                            "name": "data",
                            "description": "Array of folders and/or files.",
                            "allowMultiple": false,
                            "dataType": "FoldersAndFiles",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data as an array of folders and/or files. Folders are created if they do not exist"
                },
                {
                    "httpMethod": "PATCH",
                    "summary": "Update folder properties.",
                    "nickname": "updateFolderProperties",
                    "responseClass": "Folder",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container where you want to put the contents.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "folder_path",
                            "description": "The path of the folder you want to update. This can be a sub-folder, with each level separated by a '/'",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "data",
                            "description": "Array of folder properties.",
                            "allowMultiple": false,
                            "dataType": "FoldersAndFiles",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container or folder does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Post data as an array of folder properties."
                },
                {
                    "httpMethod": "DELETE",
                    "summary": "Delete one or more sub-folders and/or files.",
                    "nickname": "deleteFolder",
                    "responseClass": "FoldersAndFiles",
                    "parameters": [
                        {
                            "name": "container",
                            "description": "The name of the container where the folder exists.",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "folder_path",
                            "description": "The path of the folder where you want to delete contents. This can be a sub-folder, with each level separated by a '/'",
                            "allowMultiple": false,
                            "dataType": "string",
                            "paramType": "path",
                            "required": true
                        },
                        {
                            "name": "data",
                            "description": "Array of folder and files to delete.",
                            "allowMultiple": false,
                            "dataType": "FoldersAndFiles",
                            "paramType": "body",
                            "required": false
                        }
                    ],
                    "errorResponses": [
                        {
                            "reason": "Bad Request - Request does not have a valid format, all required parameters, etc.",
                            "code": 400
                        },
                        {
                            "reason": "Unauthorized Access - No currently valid session available.",
                            "code": 401
                        },
                        {
                            "reason": "Not Found - Requested container does not exist.",
                            "code": 404
                        },
                        {
                            "reason": "System Error - Specific reason is included in the error message.",
                            "code": 500
                        }
                    ],
                    "notes": "Careful, this deletes the requested folder and all of its contents, unless there are posted specific sub-folders and/or files."
                }
            ],
            "description": "Operations on folders."
        }
    ],
    "models": {
        "Containers": {
            "id": "Containers",
            "properties": {
                "container": {
                    "type": "Array",
                    "description": "An array of containers.",
                    "items": {
                        "$ref": "Container"
                    }
                }
            }
        },
        "Container": {
            "id": "Container",
            "properties": {
                "name": {
                    "type": "string",
                    "description": "Identifier/Name for the container."
                },
                "path": {
                    "type": "string",
                    "description": "Same as name for the container."
                },
                "last_modified": {
                    "type": "string",
                    "description": "A GMT date timestamp of when the container was last modified."
                },
                "_property_": {
                    "type": "string",
                    "description": "Storage type specific properties."
                },
                "metadata": {
                    "type": "Array",
                    "description": "An array of name-value pairs.",
                    "items": {
                        "type": "string"
                    }
                }
            }
        },
        "FoldersAndFiles": {
            "id": "FoldersAndFiles",
            "properties": {
                "name": {
                    "type": "string",
                    "description": "Identifier/Name for the current folder, localized to requested folder resource."
                },
                "path": {
                    "type": "string",
                    "description": "Full path of the folder, from the service including container."
                },
                "container": {
                    "type": "string",
                    "description": "Container for the current folder."
                },
                "last_modified": {
                    "type": "string",
                    "description": "A GMT date timestamp of when the folder was last modified."
                },
                "_property_": {
                    "type": "string",
                    "description": "Storage type specific properties."
                },
                "metadata": {
                    "type": "Array",
                    "description": "An array of name-value pairs.",
                    "items": {
                        "type": "string"
                    }
                },
                "folder": {
                    "type": "Array",
                    "description": "An array of contained folders.",
                    "items": {
                        "$ref": "Folder"
                    }
                },
                "file": {
                    "type": "Array",
                    "description": "An array of contained files.",
                    "items": {
                        "$ref": "File"
                    }
                }
            }
        },
        "Folder": {
            "id": "Folder",
            "properties": {
                "name": {
                    "type": "string",
                    "description": "Identifier/Name for the folder, localized to requested folder resource."
                },
                "path": {
                    "type": "string",
                    "description": "Full path of the folder, from the service including container."
                },
                "last_modified": {
                    "type": "string",
                    "description": "A GMT date timestamp of when the folder was last modified."
                },
                "_property_": {
                    "type": "string",
                    "description": "Storage type specific properties."
                },
                "metadata": {
                    "type": "Array",
                    "description": "An array of name-value pairs.",
                    "items": {
                        "type": "string"
                    }
                }
            }
        },
        "File": {
            "id": "File",
            "properties": {
                "name": {
                    "type": "string",
                    "description": "Identifier/Name for the file, localized to requested folder resource."
                },
                "path": {
                    "type": "string",
                    "description": "Full path of the file, from the service including container."
                },
                "content_type": {
                    "type": "string",
                    "description": "The media type of the content of the file."
                },
                "content_length": {
                    "type": "string",
                    "description": "Size of the file in bytes."
                },
                "last_modified": {
                    "type": "string",
                    "description": "A GMT date timestamp of when the file was last modified."
                },
                "_property_": {
                    "type": "string",
                    "description": "Storage type specific properties."
                },
                "metadata": {
                    "type": "Array",
                    "description": "An array of name-value pairs.",
                    "items": {
                        "type": "string"
                    }
                }
            }
        }
    }
}