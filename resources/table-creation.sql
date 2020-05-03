-- :name create-user-table-if-not-exists! :!
-- :doc check if the 'users' table exists
IF NOT EXISTS (SELECT * FROM sysobjects WHERE id = object_id(N'[dbo].[User]')
AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
CREATE TABLE [dbo].[User](
	[UserID] [int] IDENTITY(1,1) NOT NULL,
	[Name] [nvarchar](50) NOT NULL,
	[Surname] [nvarchar](50) NOT NULL,
	[Address] [nvarchar](50) NOT NULL,
	[ResidenceCountry] [varchar](58) NOT NULL,
	[Nationality] [varchar](50) NOT NULL,
	[Sex] [bit] NOT NULL,
	[Email] [varchar](50) NOT NULL,
	[Password] [varchar](256) NOT NULL,
	[PhoneNumber] [varchar](50) NULL,
	[BirthdayDate] [date] NOT NULL,
	[RoleID] [int] NOT NULL,
	[RegistrationDate] [datetime] NOT NULL DEFAULT CURRENT_TIMESTAMP,	
 CONSTRAINT [PK_users_v2] PRIMARY KEY CLUSTERED 
(
	[UserID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

-- :name drop-users-table :!
-- :doc Drop users table if exists
DROP TABLE IF EXISTS users