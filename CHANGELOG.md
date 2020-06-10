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

## v0.06

- Added other tables in accordance with project requirements.
- Added debug SQL queries to add test data to tables.
- API endpoints are now moved and separated by role functionality.
	- TODO: enable role validation on request.
- Added new roles functionality.
	- **ADMIN**
		- Update user information in `[user]` table.
		- List all available user groups.
		- List all members of selected group.
		- Update group information (name or description.)
		- `get-user` and `get-users` have been reworked to provide information without passwords.
		- Edit group name and description.
	- **USER**
		- Get daily task completion status.
		- Get ratings for last week.
		- Get ratings for last N days.
	- **Group Leader (Group Management)**
		- Automatically creates a new group upon user promotion to a Group Leader role.
		- Get all list with 'employee' role (users that can be added to groups).
		- Add and remove members to a group.
		- See list of all groups and group members.
		- Edit group name and description.
	- **Project Manager**
		- Get the list of all available employees for promotion.
		- Promote users to GL role (automatically creates a group).
		- Demote users back to 'employee' role.
		- Get list of all groups created by selected Project Manager.
- Changed row datatypes in some tables.
	- 'WorkFinished' in Recommendation table is now an `int`.
	- 'WorkFinished' now defaults to '3' as an initial value.
	- Some tables were missing `IDENTITY(1,1)` statement in their primary key rows.
- Changed outputs of some `hugsql` SQL queries.
	- More outputs will be changed and standardized later.
	
# v0.06a

- Fixed some SQL queries returning wrong data.
- Fixed some SQL queries making use of data from wrong tables.
- Fixed wrong arity exceptions on some inner functions.

## v0.07

- Added basic image upload function. To be used to upload user avatars.
- Added basic get-image function to receive user avatars from the server.
- Added automatic creation of directories related to image storing.
- Added automatic administrator role (default user) creation on first server startup.
- Added some functionalities related to rating storage in DB.
	- Added ability to update user's rating score.
	- Added ability to receive rooms, room names and their types from the server.
	- The server will now fill DB with new records on some endpoints.
		- `api/rating/update-rating` will create a new record for user if there is none, or update already existing.
- Added more functionalities to user.
	- Set daily question status. Can be only set once a day.
	- Getting daily question in case it wasn't answered now always returns status '2' and generates a record in DB.
- Added ability to upload a training dataset for facial recognition.
	- The server must receive a dataset in a .zip format.
	- The server will automatically unpack the archive, add new images to already existing dataset and train and produce ML-file by running a python script.
	- The archive is removed after successful training session.
- Added python machine learning script for facial recognition. Special thanks to [Viktor Surzhko](https://github.com/kalimbet) and [Marko Andrushchenko](https://github.com/XOFFF) for the preparation of the script.
- Updated ML-file can now be downloaded from the server by accesing `api/camera/get-ml-file` endpoint.
- Fixed `get-last-week-rating` endpoint not returning any data.
- Migrated documentation from main readme file to docs.
- Fixed some typos in code.
