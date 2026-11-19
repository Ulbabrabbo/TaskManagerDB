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

TEST with Bruno: To test the application using Bruno, follow these steps after starting the application as described above:

    Create a new collection in Bruno (Quick Start Guide).
    Create the following HTTP requests:

    Get all tasks: Returns the list of all available tasks.

    Type: HTTP
    Method: GET
    URL: "http://localhost:8080/api/tasks"

    Get tasks filtered by status

    Type: HTTP
    Method: GET
    URL: "http://localhost:8080/api/tasks?status={STATUS}" (Replace "{STATUS}" with a valid status (TODO, DONE or IN_PROGRESS) Example: "http://localhost:8080/api/tasks?status=TODO"

    Get task by ID

    Type: HTTP
    Method: GET
    URL: "http://localhost:8080/api/tasks/{id}" (Replace "{id}" with the ID of the task you want to retrieve) Example: "http://localhost:8080/api/tasks/1"

    Create a new task

    Type: HTTP
    Method: POST
    URL: "http://localhost:8080/api/tasks"
    Body: JSON Example request body (JSON): { "title": "title_text", "description": "description_text", "status": "TODO" } The request body must contain the fields required by the "Task" (Title and Status) Returns the newly created task with HTTP status "201 CREATED".

    Update an existing task

    Type: HTTP
    Method: PUT
    URL: "http://localhost:8080/api/tasks/{id}" (Replace "{id}" with the ID of the task you want to update)
    Body: JSON Example: "http://localhost:8080/api/tasks/1" Example request body (JSON): { "title": "Complete Spring Boot project", "description": "Task updated successfully", "status": "DONE" }

    Delete a task

    Type: HTTP
    Method: DELETE
    URL: "http://localhost:8080/api/tasks/{id}" (Replace "{id}" with the ID of the task you want to delete) Example: "http://localhost:8080/api/tasks/1" Returns HTTP status "204 NO CONTENT" if the operation is successful.

Once the requests have been created, you can execute them individually by clicking the "Send" button.
