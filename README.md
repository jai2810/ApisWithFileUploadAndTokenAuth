Pre-requisite: Postgres should be installed in the system. And then run the sql script usermanagement_db.sql to create the database and the tables.

This project contains api endpoints for user management with an option to upload image for each user profile.

Api end points contains CRUD operations for users. 

Base url: http://localhost:8080/api/user

1. End point to create a user: POST:http://localhost:8080/api/user
2. End point to delete a user: DELETE:http://localhost:8080/api/user
3. End point to create a user: PUT:http://localhost:8080/api/user
4. End point to generate token for a user: POST:http://localhost:8080/api/user/token
5. End point to get details a user: GET:http://localhost:8080/api/user/{user_id}
6. End point to all the users in the system: GET:http://localhost:8080/api/user/all

Api end points to upload/download images.

Base url: http://localhost:8080/api/image

1. End point to upload a image for a user: POST:http://localhost:8080/api/image/upload
2. End point to download a image for a user: GET:http://localhost:8080/api/image/{user_id}
3. End point to download a image for a given filename in the system: GET:http://localhost:8080/api/image/{filename}

For api requests and response structure please visit: http://localhost:8080/swagger-ui/index.html#/
