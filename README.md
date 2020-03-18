# [![ASERVO Software GmbH](https://aservo.github.io/img/aservo_atlassian_banner.png)](https://www.aservo.com/en/atlassian)ConfAPI for Confluence
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/de.aservo.atlassian/confluence-confapi-plugin/badge.svg)](https://maven-badges.herokuapp.com/maven-central/de.aservo.atlassian/confluence-confapi-plugin)
[![Build Status](https://circleci.com/gh/aservo/confluence-confapi-plugin.svg?style=shield)](https://circleci.com/gh/aservo/confluence-confapi-plugin)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=aservo_confluence-confapi-plugin&metric=coverage)](https://sonarcloud.io/dashboard?id=aservo_confluence-confapi-plugin)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=aservo_confluence-confapi-plugin&metric=alert_status)](https://sonarcloud.io/dashboard?id=aservo_confluence-confapi-plugin)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

REST API for automated Confluence configuration.



**Contact information:**  
Kai Lehmann  
https://www.aservo.com/  
klehmann@aservo.com  

**License:** [Apache 2.0](https://opensource.org/licenses/Apache-2.0)

### /application-links

#### GET
##### Summary:

Retrieves currently configured application links

##### Description:

Upon successful request, creates a list of `ApplicationLinkBean` objects, e.g. 
```
[
  {
    "serverId": "9f2d636e-c842-3388-8a66-17c1b951dd45",
    "appType": "jira",
    "name": "Jira TEST",
    "displayUrl": "http://localhost:2990/jira",
    "rpcUrl": "http://localhost:2990/jira",
    "primary": true
  }
]
```

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | List of application links |
| ![Status 400][status-400] | An error occured while retrieving the application links |

#### POST
##### Summary:

Adds a new application link

##### Description:

Upon successful request, returns a list of all configured `ApplicationLinkBean` object

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | List of all configured application links |
| ![Status 400][status-400] | An error occured while creating the application link |

### /gadgets/external

#### GET
##### Summary:

Retrieves currently configured external gadgets

##### Description:

Upon successful request, returns a list of `String` containing all configured gadget configuration urls

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | Gadget successfully added. Returns the list of configured gadget links |
| ![Status 204][status-204] | Provided gadget link was null or empty. Operation is skipped |
| ![Status 400][status-400] | An error occured while retrieving the gadget links |

#### POST
##### Summary:

Adds a new external gadget link

##### Description:

Upon successful request, returns a list of `String` containing all configured gadget configuration urls

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| test | query |  | No | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | All configured gadget links |
| ![Status 400][status-400] | An error occured while creating the gadget link |

### /permissions/anonymous-access

#### PUT
##### Summary:

Anonymous access

##### Description:

Sets global permissions for anonymous access to public pages and user profiles

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| activateUse | query |  | No | boolean |
| activateViewProfiles | query |  | No | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | access successfully set |
| ![Status 400][status-400] | An error occured while setting the access |

### /license

#### GET
##### Summary:

Retrieves license information

##### Description:

Upon successful request, returns a `LicenseBean` object containing license details, e.g. 
```
{
  "productName": "Confluence",
  "licenseType": "TESTING",
  "organization": "Atlassian",
  "description": "Test license for plugin developers",
  "expiryDate": 1583671644086,
  "numUsers": 25
}
```

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | License details |
| ![Status 400][status-400] | An error occured while retrieving the license infos |

#### POST
##### Summary:

Adds a new license

##### Description:

Upon successful request, returns a `LicenseBean` object containing license details

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | License details for the added license |
| ![Status 400][status-400] | An error occured while setting the new license |

### /mail/smtp

#### GET
##### Summary:

Retrieves the current SMTP mailserver configuration

##### Description:

Returns a `SmtpMailServerBean` object with the configuration of the SMTP mail server, if any server is defined.., e.g. 
```
{
    "name": "Localhost",
    "description": "The localhost SMTP server",
    "from": "confluence@localhost",
    "prefix": "Confluence",
    "protocol": "smtp",
    "host": "localhost",
    "port": 25,
    "tls", false,
    "timeout": 10000,
    "username": "admin",
    "password": "admin"
}
```

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | smtp mailserver configuration |
| ![Status 400][status-400] | An error occured while retrieving the settings |

#### PUT
##### Summary:

Updates the SMTP mailserver configuration

##### Description:

Upon successful request, returns a `SmtpMailServerBean` object containing the updates settings

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | updated settings object |
| ![Status 400][status-400] | An error occured while updating the settings |

### /mail/pop

#### GET
##### Summary:

Retrieves the current POP mailserver configuration

##### Description:

Returns a `PopMailServerBean` object with the configuration of the POP mail server, if any server is defined.., e.g. 
```
{
    "name": "Localhost",
    "description": "The localhost SMTP server",
    "protocol": "pop",
    "host": "localhost",
    "port": 110,
    "timeout": 10000,
    "username": "admin",
    "password": "admin"
}
```

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | pop mailserver configuration |
| ![Status 400][status-400] | An error occured while retrieving the settings |

#### PUT
##### Summary:

Updates the POP mailserver configuration

##### Description:

Upon successful request, returns a `PopMailServerBean` object containing the updates settings

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | updated settings object |
| ![Status 400][status-400] | An error occured while updating the settings |

### /ping

#### GET
##### Summary:

Ping

##### Description:

Simple connectivity check

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | returns pong |

### /settings

#### GET
##### Summary:

Get Confluence application settings

##### Description:

Returns a `SettingsBean` object with general Confluence settings like the base url or the title., e.g. 
```
{
   "baseurl": "http://localhost:1990/confluence",
   "title": "Your Confluence"
}
```

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | settings object |
| ![Status 400][status-400] | An error occured while retrieving the settings |

#### PUT
##### Summary:

Updates Confluence application settings

##### Description:

Upon successful request, returns a `SettingsBean` object containing the updates settings

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | updated settings object |
| ![Status 400][status-400] | An error occured while updating the settings |

### /user-directories

#### GET
##### Summary:

Retrieves user directory information

##### Description:

Upon successful request, returns a list of `UserDirectoryBean` object containing user directory details, e.g. 
```
[
  {
    "active": true,
    "name": "Confluence Internal Directory",
    "type": "INTERNAL",
    "description": "Confluence default internal directory",
    "implClass": "com.atlassian.crowd.directory.InternalDirectory"
  }
]
```

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | user directory details list |
| ![Status 400][status-400] | An error occurred while retrieving the user directory list |

#### POST
##### Summary:

Adds a new user directory

##### Description:

Upon successful request, returns the added `UserDirectoryBean` object, Any existing configurations with the same 'name' property are removed before adding the new configuration

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| test | query |  | No | boolean |

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | user directory added |
| ![Status 400][status-400] | An error occured while setting adding the new user directory |

### /users/password

#### PUT
##### Summary:

Updates the user password

##### Description:

Upon successful request, returns the updated `UserBean` object.

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| username | query |  | No | string |
| password | query |  | No | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | updated user details |
| ![Status 400][status-400] | An error occured while updating the user password |

### /users

#### GET
##### Summary:

Retrieves user information

##### Description:

Upon successful request, returns a `UserBean` object containing user details, e.g. 
```
{
  "username": "admin",
  "fullName": "admin",
  "email": "admin@example.com"
}
```

##### Parameters

| Name | Located in | Description | Required | Schema |
| ---- | ---------- | ----------- | -------- | ---- |
| username | query |  | No | string |

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | user details |
| ![Status 400][status-400] | An error occured while retrieving the user details |

#### PUT
##### Summary:

Updates user details

##### Description:

Upon successful request, returns the updated `UserBean` object. NOTE: Currently only the email address is updated from the provided `UserBean` parameter.

##### Responses

| Code | Description |
| ---- | ----------- |
| ![Status 200][status-200] | updated user details |
| ![Status 400][status-400] | An error occured while updating the user |
