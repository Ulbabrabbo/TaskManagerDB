Task Manager DB with Docker

REQUIREMENTS:

    Java 17
    Maven
    Docker

SETUP:
On the main directory of the project there is a file .env.example. You have to create a file .env using the .example as template.
On the file .env you have to select a password for: MYSQL_ROOT_PASSWORD and MYSQL_PASSWORD

FIRST EXECUTION:

    Open the terminal in the root directory of the project.
    Create the .jar file with maven running:
        "mvn package -DskipTests"
    Create the docker images and containers running:
        "docker compose up --build"

    To stop the application you have to run:
        "docker compose down"
    To start the application you have to run:
        "doker compose up"
    Data will persiste on the MySQL Volume between sessions. If you want to delete the data form the volume you have to run:
        "docker compose down -v"

The app support Swagger for documentation and testing: http://localhost:8080/swagger-ui/index.html


TESTING with Postman
To test the application using Postman, follow these steps after starting the application as described above.

- Setup
1. Download and install [Postman](https://www.postman.com/downloads/).
2. Create a new collection (e.g., "Task Manager API").
3. Set up a collection variable:
   - baseUrl: "http://localhost:8080"
4. Create the following HTTP requests inside the collection.

-- Positive Tests

1. Get all tasks
- Method: GET
- URL: "{{baseUrl}}/api/tasks"
- Expected status: 200 OK

2. Get tasks filtered by status**
- Method: GET
- URL: "{{baseUrl}}/api/tasks?status=TODO"
- Expected status: 200 OK
Valid statuses are "TODO", "IN_PROGRESS" and "DONE".

3. Get task by ID
- Method: GET
- URL: "{{baseUrl}}/api/tasks/1"
- Expected status: 200 OK
Replace "1" with the ID of the task you want to retrieve.

4. Create a new task
- Method: POST
- URL: "{{baseUrl}}/api/tasks"
- Body: raw → JSON
- Expected status: 201 Created

body example:
{
    "title": "Complete Spring Boot project",
    "description": "Implement the Task Manager API",
    "status": "TODO"
}
The title and status are required. The description is optional.

5. Update an existing task
- Method: PUT
- URL: "{{baseUrl}}/api/tasks/1"
- Body: raw → JSON
- Expected status: 200 OK
Replace "1" with the ID of the task you want to update.

6. Delete a task
- Method: DELETE
- URL: "{{baseUrl}}/api/tasks/1"
- Expected status: 204 No Content
Replace "1" with the ID of the task you want to delete.

---
---
---

-- Negative Tests
You can do requests to verify that the API correctly handles possible invalid input and missing resources.
When an error occurs the app returns a ProblemDetail containing info on the error

1. access task that does not exist
- Expected status: 404 Not Found
- info: Task not found


2. Create/update a task with invalid data
example:
{
    "title": "",
    "description": "Invalid task",
    "status": "INVALID"
}
The request is rejected because the title is blank. The response contains validation error details.

- Expected status: 400 Bad Request
- info: Validation failed
- errors: required title.

The request can be also rejected for example if the title exceeds the maximum allowed length of 100 characters or if the description exceeds 200 characters. 


3. Update a task with an invalid ID
- URL: {{baseUrl}}/api/tasks/-1

- Expected status: 400 Bad Request
- info: Validation failed
- errors: id must be greater than 0

Task IDs must be positive integers.
