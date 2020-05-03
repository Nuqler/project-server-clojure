# University project | serverside

## Description
***

Current version provides very basic user registration and login validation functions.

Current functionality:

* Registering new users and adding them to the DB.
* Removing users from the DB.
* Check if input email is registered in the DB. Function to be used in end application.
* Basic login and password validation. The server will send back most available user details on successful validation.
* Getting user data by their ID or Email (Debug feature).
* Getting every available user and their data (Debug feature).
* Very basic echo test (Returns input value). (Debug feature).

At the moment the server relies on conscience of the user to input valid data into the requests.

### Compilation
***

TBA

### Preliminary Configuration
***

The following repository includes an example file called 'example_config.edn'. This file should be placed in the same folder as server jar file.

This file must be renamed to 'config.edn' and the DB configuration details must be specified inside (only MSSQL is supported). Failing to provide necessary configuration or misplacing the file itself will result in failure to connect to the database.

### Launching The Server
***

After compiling and configuring the server application, it can be launched using java. When launching the server application, "-start" launch parameter must be passed to the server.

Example:

```console
java -jar ring-app-0.0.3-SNAPSHOT-standalone.jar -start
```

On succesful start, the server will be launched and start listening on port 3000. The 'users' table should be automatically created if it does not exist.

**Warning: If you're upgrading from old versions (especially `0.0.3 -> 0.0.4`), please consider dropping DB tables before upgrading. Failing to comply may result in unexpected behaviour.**

### Example Usage
***

The server implements API which relies on POST/GET queries. The server accepts and returns json strings as accepted data type.

Examples using curl in *Windows commandline* prompt.

**Note: every quotation mark within the request string has to be escaped in order to form correct request through *Windows commandline curl*. Another option would be to surround requests in singular brackets. Either way, a correct JSON string has to be sent.**

* New user registration.

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/register-user -d "{\"name\": \"John\", \"surname\": \"Smith\", \"address\": \"128 East Greenview Street West Lafayette\", \"residencecountry\": \"USA\", \"nationality\": \"American\", \"sex\": 1, \"email\": \"abcdef@mail.com\", \"password\": \"123456\", \"phonenumber\": \"+777712345678\", \"birthdaydate\": \"1990-10-15\", \"roleid\": 1}"
```

Accepts JSON string containing following data:  'name', 'surname', 'address', 'residencecountry', 'nationality', 'sex', 'email', 'password', 'phonenumber', 'birthdaydate', 'roleid'.

All fields except phone number are mandatory. Emails must be unique. 'Sex' accepts either 1 or 0 (1 stands for 'Male', 0 stands for 'Female'). Accepted date format is 'MM-DD-YYYY' or 'YYYY-MM-DD'.

* Check if Email is registered in DB

```console
curl http://localhost:3000/api/user/email
```

*Email* should be replaced with a valid email.

Returns 'userid', 'name', 'surname', 'email' if such email exists.

* Login and password validation

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/login -d "{\"email\": \"abcdef@mail.com\", \"password\": \"123456\"}"
```

Accepts JSON string containing 'email' and 'password'. Returns all userdata (excluding password) if entered credentials are correct.

* Remove registered user by ID

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/remove-user -d "{\"userid\": 1}"
```

Accepts JSON string containing 'userid'.

* Get user by ID or Email (Debug Feature)

```console
curl http://localhost:3000/api/get-user/parameter
```

*Parameter* should be replaced with a number, corresponding to user's ID or a valid registered email address. Returns all available user data on success (including password).

* Get all users (Debug Feature)

```console
curl http://localhost:3000/api/get-users
```

Returns all available data for all registered users.

* Simple echo test (Debug feature).

```console
curl http://localhost:3000/echo/something
```

*Something* should be changed. Echoes back whatever you input there.

## License
***

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
