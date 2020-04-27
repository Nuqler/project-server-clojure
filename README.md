# University project | EA serverside

## Description

Serverside part of the 'EA' University Project.

Current version provides very basic user registration and login validation functions.

Current functionality:

* Registering new users and adding them to the DB.
* Removing users from the DB.
* Basic login validation. The server will send back all available user details on success.
* Getting user data by their ID (Debug feature).
* Getting every available user and their data (Debug feature).
* Very basic echo test (Returns input value. Debug feature.).

At the moment the server relies on conscience of the user to input valid data to the requests.

### Preliminary Configuration

The server must have MSSQL instance installed. The following repository includes an example file called 'example_config.edn'. This file should be placed in the same folder as server jar file.

This file must be renamed to 'config.edn' and the DB configuration details must be specified inside (only MSSQL is supported). Failing to provide necessary configuration or misplacing the file itself will result in failure to connect to the database.

### Launching The Server

After compiling and configuring the server application, it can be launched using java. When launching the server application, "-start" launch parameter must be passed to the server.

Example:

> java -jar ring-app-0.0.3-SNAPSHOT-standalone.jar -start

On succesful start, the server will be launched and start listening on port 3000. The 'users' table should be automatically created.

### Example Usage

The server implements API which relies on POST/GET queries. The server accepts and returns json strings as accepted data type.

Examples using curl in *Windows commandline* prompt.

Note: every quotation mark within the request string has to be escaped in order to form correct request through curl.

* New user registration.

> curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/register-user -d "{\"username\": \"John\", \"pass\": \"12345\", \"role\": \"User\", \"description\": \"test record\"}"

Accepts json string containing following data: 'username', 'pass', 'role' and 'description'.

All fields are mandatory. Usernames must be unique.

* Login validation

> curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/login -d "{\"username\": \"John\", \"pass\": \"12345\"}"

Accepts json string containing 'username' and 'pass'.

* Get user by ID

> curl http://localhost:3000/api/get-user/ID

*ID* should be replaced with a number, corresponding to user's ID. Returns all available user data on success.

* Get all users

> curl http://localhost:3000/api/get-users

Returns all available data for all registered users.

* Remove registered user by ID

> curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/remove-user -d "{\"id\": 1}"

Accepts json string containing 'id'.

* Simple echo test.

> curl http://localhost:3000/echo/something

Echoes back whatever you input here.


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
