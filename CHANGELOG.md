# Change Log

## v0.01 

- Updated the template.

## v0.02 

- Migration to `hugsql` statements.
- Split server files.

## v0.03 

- Implemented basic user registration and login validation.

## v0.03a 

- Fixed user registration and login functions. Updated the description.

## v0.03b 

- Changed some of server response values. Server now returns a JSON response at all times.

## v0.04

- Updated dependencies.
- Expanded 'User' table in accordance with project requirements.
- Switched from `ring.adapter.jetty` to `http-kit` for managing server state.
- Optimized login validation function to use less queries.
- In general, all failed requests should now return `400 Bad Request` error.
- Added new GET `/api/user/:email` endpoint to check whether a user with input email exists. To be used in end application.
- Added new functionality to `api/get-user/:params` endpoint. It can now accept either user email or ID.
- Added basic table creation function on startup (mostly for the future).
- Added some verbosity to table creation process. It will tell whether a specific table was found. If the table was not found, it will be created.
- Updated user registration API to match new 'User' table.
- Updated login API to match new 'User' table. `api/login` now requires 'email' and 'password'.
- Updated most SQL queries to match new 'User' table.
- Changed all login related functionalities to use 'email' insted of 'username'.
- Changed failed logins to provide `400 Bad Request` response code instead of `200 OK`.
- Changed failed `api/get-user` endpoint to provide `400 Bad Request` response code instead of `200 OK` if user with input ID cannot be found.
- Changed failed registration (due to duplicate username) to provide `409 Conflict` response code instead of `200 OK`.
- Changed hugsql to use `hugsql-jdbc-next-adapter` by default.
- Fixed timestamp showing incorrect time when retrieved from DB.

## v0.05

- Rewrote code for getting values from database action status `next.jdbc/update-count`.
- Added AES password encryption on registration. Encrypted password is now stored in DB instead of plain text. Very basic for the time being.
	- At no point are passwords decrypted back. Only encrypted hashes are compared.
- Updated inner functions of retrieving users from DB.
- Changed failed `login` message.
- Changed way of generating registration date in DB from `CURRENT_TIMESTAMP` to `GETDATE()`
- Moved `login` endpoint to `/user/login`
- Moved `user` email check endpoint to `/user/check`

## v0.05a

- User authentication and password encryption is now handled by `funcool/buddy` library.
	- Passwords are encrypted using `bcrypt+sha512` algorithm.
- Added basic Token system for user authentication and authorization.
- Added token-based login and logout server endpoints.
