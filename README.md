# RepoFinder

## Overview
This application allows users to list GitHub repositories for a given username. The API is built using Java 21 and Spring Boot 3.

RepoFinder provides the following functionalities:

1. **List GitHub Repositories**: Given a GitHub username and an "Accept: application/json" header, the API will return a list of the user's GitHub repositories which are not forks. It includes repository names, owner login, and branch details (name and last commit SHA).

2. **Handle User Not Found**: If a requested GitHub user does not exist, the API responds with a 404 status and a detailed error message.

## Endpoints

### Get User Repositories

**Endpoint**: `/users/{username}/repos`

**Method**: `GET`

**Headers**:
- `Accept: application/json`

**Response**:
- **Status 200 OK**

  Example Response:
  ```json
  [
   {
        "Repository Name": "repofinder",
        "Branches": [
            {
                "Last Commit SHA": "b784323af50c412b12a3c9c6f993705f74a32141",
                "Branch Name": "master"
            }
        ],
        "Owner Login": "CodeCatnip"
    }
  ]
- **Status 404 Not Found**

  Example Response:
  ```json 
  [
    {
         "message": "User not found",
         "status": 404
    }
  ]
