# Events Calendar Backend

A REST API created with Spring boot. It allows creating events with labels. Includes JWT authentication. Backend for [Events Calendar](https://github.com/martynakr/events-calendar/)

## Running instructions

## Available Endpoints

-   `POST /auth/register` - allows a new user to create an account, example payload:

```json

```

-   `POST /auth/login`
-   `GET /events`
-   `GET /labels`

## Tests

## Change log

-   **2023** - set up a REST API that allows creating events and labels for events. Added authentication and set up One-To-Many relationship between User and Event. Each user can only get events that belong to them.

-   **12 June 2024** - Adding a relationship between Label and User - each user will now be able to create their own labels and won't be able to view labels that belong to other users.

## Known issues

## Next steps

-   Sending JWT as httpOnly cookie
-
