# Documentation

## Example usage

The server implements API which relies on POST/GET queries. The server accepts and returns json strings as accepted data type for most requests.

The following endpoints list will show all available endpoints and how to work with them. They will be written in following form.

### Example #1

`GET /api/user/check/:email` (Lookup for a registered user. Returns Name, Surname and UserID on success.)

The first word of this example request shows the type of request the endpoint expects (GET/POST).

The second part of the example request shows the server api endpoint without the host IP address (i.e. full route could be `localhost:3000/api/user/check/:email`).

The third part `:email` shows the parameter that the server expects to receive. (In case of GET queries it should be changed accordingly. For instance, `localhost:3000/api/user/check/john_smith@mail.com`). Nothing should be passed in the body of the JSON file.

The fourth part in the parenthesis contains basic explanation as to what kind of parameters the server expects from user and what the user will get in response.

Full request example using *curl* in *windows cmd prompt*

```console
curl http://localhost:3000/api/user/check/john_smith@mail.com
```

### Example #2

`POST /api/user/login [email password]` (Returns all data w/o password from 'User' table on success.)

The first word in this example request shows the type of request the endpoint expects (GET/POST).

The second part of the example request shows the server api endpoint without the host IP address (i.e. full route could be `localhost:3000/api/user/login`).

The third part in the brackets shows what kind of parameters must be passed in the body of the JSON file (`email` and `password` in this case.)

The fourth part in the parenthesis contains basic explanation as to what kind of parameters the server expects from user and what the user will get in response.

Full request example using *curl* in *windows cmd prompt*

```console
curl -H "Content-Type: application/json" -X POST http://localhost:3000/api/user/login -d "{\"email\": \"abcdef@mail.com\", \"password\": \"123456\"}"
```

## Endpoints List

### JSON

The following endpoint list works with `application/json` header.

Emails used in registration must be unique. 'Sex' accepts either 1 or 0 (1 stands for 'Male', 0 stands for 'Female'). Accepted date format is 'MM-DD-YYYY' or 'YYYY-MM-DD', make sure to use only one.

All requests will return a JSON object  containing queried data. In case a request does not return any data by design (i.e. `/api/admin/removeUser`), it will return a JSON object with a 'result' key containing information about the status (success/fail). All responses from the server are returned with correct HTML status codes.

RoleID:

- 1 = "Employee"
- 2 = "Administrator"
- 3 = "Project Manager"
- 4 = "Group Leader"
- 5 = "Boss/Company Director" (WIP title)

**User**

1. GET /api/user/check/:email (Lookup for a registered user. Returns Name, Surname and UserID on success. This should be used to see if the user's email is registered in the system.)

2. POST /api/user/login [email password] (Returns all data w/o password from 'User' table on success.)

3. POST /api/user/get-daily-task-status [userid date] (Returns daily task completion status on queried date. 0 = false, 1 = true, 2 = no answer yet. In case there is no record for queried date, it creates a new record in the DB with 'WorkFinished' = 2)

4. POST /api/user/get-last-week-rating [userid] (Returns [UserID, TimeOnRest, TimeOnWork, Rating, Date] for last 5 days for selected UserID)

5. POST /api/user/get-last-N-days-rating [userid number] (Returns [UserID, TimeOnRest, TimeOnWork, Rating, Date] for last N days. Number parameter stands for number of days to query.)

6. POST /api/user/set-daily-question [userid workfinished date] (Update or add 'WorkFinished' status for queried UserID for queried date. Can only be updated once a day or if the status = 2 [WorkFinished status 0 = false, 1 = true, 2 = no answer yet.]). In case there is no record for queried date, it creates a new record in the DB with input WorkFinished.)

7. (BETA-version login/logout features using tokens are not included there as they are not fully implemented yet)

**Group Leader (Group Management)**

1. POST /api/user/group/add-member [userid groupid] (Add a person to the group)

- POST /api/user/group/remove-member [userid groupid] (Remove a preson from the group. Be careful not to remove a group leader from the group!)

- GET /api/user/group/get-employees (Return a list of all employees with RoleID = 1)

- GET /api/user/group/get-group-members/:groupid (Return a list of all employees in queried group)

- POST /api/user/group/edit-group-info [groupid groupname description] (Update group name and description in queried group)

- GET /api/user/group/get-group/:groupid (Get group name and description for queried group)

**Project Manager**

1. GET /api/pm/get-employees (Get list of all employees which can be promoted to Group Leader role)

- GET /api/pm/get-controlled-groups/:projectmanagerid (Get list of all groups created by quereied PM)

- POST /api/pm/promote-to-leader [userid projectmanagerid] (Promotes selected 'UserID' to Group Leader role. Make sure to specify ProjectManagerID as well (this should be a UserID of a user with a role of PM).)

- POST /api/pm/demote-to-employee [userid] (Demote an employee back to 'RoleID = 1'; remove his group and all its members)

**Admin***

1. POST /api/admin/register-user [name surname address residencecountry nationality sex email password phonenumber birthdaydate roleid] (Register a new user based on provided info)

- GET /api/admin/get-user/:parameter (Return all data (w/o pass) from 'User' table for queried user. Parameter could be either UserID or a valid email)

- GET /api/admin/get-users (Return the list of all registered users and their data (w/o pass) from 'User' table)

- POST /api/admin/update-user-data [userid name surname address residencecountry nationality sex email password phonenumber birthdaydate roleid] (Update user data for queried UserID)

- POST /api/admin/remove-user [userid] (Removes user from the DB. At the moment of writing this does not remove images associated with the user.)

- GET /api/admin/get-all-groups (Get list of all created groups)

- GET /api/admin/get-group-members/:groupid (Get all members of a group querid by groupid)

- POST /api/admin/edit-group-info [groupid groupname description] (Edit group name and description for queried groupid)

**Rating Related**

1. POST /api/rating/get-daily-task-status [userid date] (Exactly same as endpoint #3 for 'User' role)

- GET /api/rating/get-rooms (Returns all available RoomID, name, RoomType from 'Room' table.)

- POST /api/rating/update-rating [userid timeonrest timeonwork rating date] (Updates or creates a new record in the DB for queried UsedID for queried Date. The added\updated information is 'timeonrest', 'timeonwork', 'rating' in the 'RatingForDay' table.)

### Multipart form

The following endpoint list works with `multipart/form-data` header.

These endpoints deal with file uploading and downloading.

1. XPOST /file/upload-user-picture [file] (Uploads a picture to the 'uploadedData/userPic' directory. The file parameter in the request must be named 'file'.)

- GET /file/user-picture/:filename (Get a picture from the server. The filename is the same used in uploading. Looks for files in 'uploadedData/userPic' directory.)

- XPOST /file/upload-training-set [file] (The file must be .zip format. Accepts a file, unpacks the training images to the 'uploadedData/dataset' folder and removes the archive. The server then automatically runs python training scrips which creates/updates the 'trainingData.yml' file at 'uploadedData/recognizer'.)

- GET /api/cameras/get-ml-file (Returns newest 'trainingData.yml' file from 'uploadedData/recognizer' folder to be used for facial recognition.)