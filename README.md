# University project | serverside

## Description

Employee Management System (Serverside Part) project prepared for 'IT Projects Prepared in Teams' subject.
The other parts can be found here (could be outdated):

[Clientside part](https://github.com/kalimbet/EA) and a camera module.

[Rating calculation](https://github.com/XOFFF/Develop-a-rating-calculation-algorithm/tree/master).

The The server runs on JRE 11.

Current functionality as of `v 0.07`:

Some functionality may be duplicated for some roles, while some might be missing. This is made as a first step to enabling the role request authorization process and is still WIP.
In the future more functions may be shifted or distributed between the roles.

### Functions to be used in a desktop app with a UI.

- **General (role independent)**
	- Login
	- Input mail to check if the user exists.
- **Admin**
	- Register a new user.
	- Look up users by UserID or email.
	- Get list of all registered users.
	- Remove registered users.
	- Update registered user details.
	- Get list of all groups (lead by Group Leader role).
	- Get info about individual groups.
	- Get group members list for individual groups.
	- Edit group info (group name and description).
- **Project Manager**
	- Get list of all employees (User role = 1) which can be assigned as a Group Leader.
	- Get list of all groups assigned by selected PM.
	- Promote an employee to Group Leader role (automatically creates and binds a group to this person).
	- Demote a Group Leader back to employee role.
- **User (Employee)**
	- Get daily task completion status.
	- Set daily task completion status (can only be set once a day).
	- Get weekly rating list (last 5 days).
	- Get rating for selected date.
	- Get rating for last N days.
- **Group Leader (group management)**
	- Change group name and description.
	- Add new group members.
	- Remove group members.

### File related

- Upload user avatar picture.
- Upload training dataset (zip file) for facial recognition.
	- The server automatically unzips and performs training based on added data.
- Get user avatar pictures.
- Get newest trained ML-file.

### Functionality to be used in a side application used to calculate and update user ratings.

- Get newest trained machine learning file.
- Get roomIDs, their names and room types.
- Get daily question answer status.
- Update or add user rating score (as well as rest\work time).

### Unrelated debug features

- Very basic echo test (Returns input value).

At the moment the server relies on conscience of the user to input valid data into the requests.

## Compilation

1. Open the root folder of this project in your cmd interface or powershell.
2. Execute `lein ring uberjar` command using leiningen.
3. The compiled STANDALONE jar should be created at 'target' folder.

## Preliminary Configuration


The following repository includes an example file called 'example_config.edn'. This file should be placed in the same folder as server jar file.

This file must be renamed to 'config.edn' and the DB configuration details must be specified inside (only MSSQL is supported). Failing to provide necessary configuration or misplacing the file itself will result in failure to connect to the database.

## Launching The Server


After compiling and configuring the server application, it can be launched using java. When launching the server application, "-start" launch parameter must be passed to the server.

Example:

```console
java -jar ring-app-0.0.3-SNAPSHOT-standalone.jar -start
```

On succesful start, the server will be launched and start listening on port 3000. The server automatically checks the connected database and creates all necessary tables if they are not present.

**Warning: If you're upgrading from old versions (especially `0.0.3 -> 0.0.4`), please consider dropping DB tables before upgrading. Failing to comply may result in unexpected behaviour.**

## Example Usage


This section contains example server usage. For a list of all available endpoints refer to the documentation (see docs folder).

The server implements API which relies on POST/GET queries. The server accepts and returns json strings as accepted data type.

Examples using curl in *Windows commandline* prompt.

**Note: every quotation mark within the request string has to be escaped in order to form correct request through *Windows commandline curl*. Another option would be to surround requests in singular brackets. Either way, a correct JSON string has to be sent.**

* New user registration.

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/admin/register-user -d "{\"name\": \"John\", \"surname\": \"Smith\", \"address\": \"128 East Greenview Street West Lafayette\", \"residencecountry\": \"USA\", \"nationality\": \"American\", \"sex\": 1, \"email\": \"abcdef@mail.com\", \"password\": \"123456\", \"phonenumber\": \"+777712345678\", \"birthdaydate\": \"1990-10-15\", \"roleid\": 1}"
```

Accepts JSON string containing following data:  'name', 'surname', 'address', 'residencecountry', 'nationality', 'sex', 'email', 'password', 'phonenumber', 'birthdaydate', 'roleid'.

All fields except phone number are mandatory. Emails must be unique. 'Sex' accepts either 1 or 0 (1 stands for 'Male', 0 stands for 'Female'). Accepted date format is 'MM-DD-YYYY' or 'YYYY-MM-DD'.

* Check if Email is registered in DB

```console
curl http://localhost:3000/api/user/check/email
```

*Email* should be replaced with a valid email.

Returns 'userid', 'name', 'surname', 'email' if such email exists. UserID can be used to get the user avatar later.

* Login and password validation

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/user/login -d "{\"email\": \"abcdef@mail.com\", \"password\": \"123456\"}"
```

Accepts JSON string containing 'email' and 'password'. Returns all userdata (excluding password) from 'User' table if entered credentials are correct.

* Remove registered user by ID

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/admin/remove-user -d "{\"userid\": 1}"
```

Accepts JSON string containing 'userid'.

* Get user by ID or Email

```console
curl http://localhost:3000/api/admin/get-user/parameter
```

*Parameter* should be replaced with a number, corresponding to user's ID or a valid registered email address. Returns all available user data from 'User' tableon success (excluding password).

* Get all users and their data from 'User' table (excluding password)

```console
curl http://localhost:3000/api/admin/get-users
```

Returns all available data for all registered users.

* Simple echo test (Debug feature).

```console
curl http://localhost:3000/echo/something
```

*Something* should be changed. Echoes back text you input there.

## License

Copyright © 2020

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
