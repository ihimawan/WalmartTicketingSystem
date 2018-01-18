# Walmart Ticketing System - Restful Web Service

This project is a Spring Boot Restful application that allows users to see the status of a certain venue, obtain and hold the 'best seats' (the definition of 'best seats' will be discussed later in the document), as well as reserve held seats. The assumption is that the user can only interact through the RESTful API's, with little to no interaction with the database. Therefore, the information output from the endpoints shall be made as comprehensive as possible.

In this README.md document, you should be able to find the following information:


1. Starting and Stopping the web service
2. Documentation: API endpoints
3. Assumptions in the Project
4. 'Best Seat' algorithm
5. Things to Improve

## 1. Starting and Stopping the web service

Navigate to the ```WalmartTicketingSystem``` directory and do the execute the file ```ticketingStartup.sh``` in the Terminal/command line. It will run ```mvn clean install``` and you will be able to access the web service on:

```
localhost:8090
```

The database being used is H2, to access the database 
```
http://localhost:8090/h2-console
```

with the following information:

**JDBC url:** ```jdbc:h2:mem:testdb```
**Username:** ```sa```
**Password:** ```[blank]```

To stop the Spring Boot service in the Terminal/Command Line, simply press ```control+C```


## 2. Documentation: API endpoints

This is a RESTful web application. There are a number of API that are made accessible for the following objects:
1. Venue
2. Seat Holds
3. Seat Reservations

### 2.1. Venue

##### A. Finding available venues

This API endpoint is to display to users the id and the name of the available venues in the theater. The available venues are according to the initial SQL insert statements in the ```src/main/resources/data.sql``` file.
```
GET /api/venues
```

##### B. Finding available information about venue

Given the venue id the endpoint provides information on the venue name, available seats, hold seats, and reserved seats. The initial motivation behind the creation of this API endpoint is so that UI (e.g. ReactJS) can consume this API endpoint and visualize the venue. UI might be something that can be done in the future. 

```
GET /api/venues/{venueId}
```
If venueId isn't found, the request will return a ```404```.

##### C. Finding number of available seats given venueId

I am taking the assumption that this is one of the required endpoint as written in the coding challenge document, which is to display **ONLY** the available seats in a given venue as an integer.
```
GET /api/venues/{venueId}/availableSeats
```
If venueId isn't found, the request will return a ```404```.

### 2.2. Seat Holds

##### A. Finding seat holds and information about seat hold

Just like the API endpoint for venues, the following calls could be made:
```
GET /api/seatHolds
GET /api/seatHolds/{seatHoldId}
```

##### B. Creating seat holds

```
POST /api/seatHolds?email={email}&venueId={venueId}&numSeats={numSeats}
```
The following **required** query parameters customizes your request.

| param   | type   | Description   | 
| ------- |------  | ------------- |
| email	  | String | Provide email of the customer. If the email does not exist in the Customer database, a new Customer entry will be created in the database automatically with the email.  |
| venueId | String | Provide existing ID of venue (as can be obtained from ```/api/venues```). If venue does not exist, then an error message will be displayed.  |
| numSeats| String | Provide number of seats wanting to be held. If the number of seats requested to be held exceeds the number of available seats in the venue, then an error message will be displayed.  |

Sample successful seat holding:
```
{
    "status": "SUCCESS",
    "message": "Your seatHoldId=2",
    "link": "http://localhost:8090/api/seatHolds/2"
}
```

Sample failed seat holding:
```
{
    "status": "FAILURE",
    "message": "Unable to get best seats. There are only 16 available while 100 is requested.",
    "link": null
}
```

### 2.3. Seat Reservations

##### A. Finding seat reservations and information about seat reservations

Just like the API endpoint for venues, the following calls could be made:
```
GET /api/seatReserves
GET /api/seatReserves/{seatReservationId}
```

##### B. Creating seat reservations based on seat hold ID

```
POST http://localhost:8090/api/seatReserves?seatHoldId={seatHoldId}&email={email}
```
The following **required** query parameters customizes your request.

| param   | type   | Description   | 
| ------- |------  | ------------- |
| seatHoldId| String | Provide the seatHoldId (as can be seen on ```/api/seatHolds```). If seatHoldId that is enterred does not exist, then an error message will be displayed.  |
| email	  | String | Provide email of the customer. The email must match the email in which the seat hold was made under. Otherwise, an error message will be displayed.  |


Sample successful seat holding:
```
{
    "status": "SUCCESS",
    "message": "Reserved with id=1",
    "link": "http://localhost:8090/api/seatReserves/1"
}
```

Sample failed seat reservation:
```
{
    "status": "FAILURE",
    "message": "other@test.com cannot claim this hold. Expected customer email=test@test.com",
    "link": null
}
```

## Assumptions in the Project

## Best Seat Algorithm

## Things to Improve

* Adding security layer (e.g. Basic Authentication)
* Adding UI, possibly best with ReactJS to leverage the API's
* Adding multiple AI's as threads that holds and reserves seats to simulate real-time interaction

