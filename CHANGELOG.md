# Change Log

## v0.01 

- Updated the base project template.

<<<<<<< Updated upstream
v0.03a Fixed user registration and login functions. Updated the description.
=======
## v0.02 

- Migration to `hugsql` statements.

## v0.03 

- Implemented basic user registration and login validation.

## v0.03a 

- Fixed user registration and login functions. Updated the description.

## v0.03b 

- Changed some of server response values. Server now returns a JSON response at all times.

## v0.04

- Updated dependencies.
- Switched from `ring.adapter.jetty` to `http-kit` for managing server state.
- Fixed timestamp showing incorrect time when retrieved from DB.
- Changed failed logins to provide `400 Bad Request` response code instead of `200 OK`.
- Changed failed `api/get-user` endpoint to provide `400 Bad Request` response code instead of `200 OK` if user with input ID cannot be found.
- Changed failed registration (due to duplicate username) to provide `409 Conflict` response code instead of `200 OK`.
- Optimized login validation function to use less queries.
>>>>>>> Stashed changes
