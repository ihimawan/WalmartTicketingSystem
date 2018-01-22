# Walmart Ticketing System - Restful Web Service

This project is a Spring Boot Restful application that allows users to see the status of a certain venue, obtain and hold the 'best seats' (the definition of 'best seats' will be discussed later in the document), as well as reserve held seats. The assumption is that the user can only interact through the RESTful API's, with little to no interaction with the database. Therefore, the information output from the endpoints shall be made as comprehensive as possible.

In this README.md document, you should be able to find the following information:


1. Starting and Stopping the web service
2. Documentation: API endpoints
3. Assumptions in the Project
4. 'Best Seat' Algorithm Design
5. Things to Improve

## 1. Starting and Stopping the web service

Navigate to the ```WalmartTicketingSystem``` directory and do the execute the file ```ticketingStartup.sh``` in the Terminal/command line. It will run ```mvn clean install``` to run the test and build, after you see the following lines you will be able to access the web service in ```localhost:8090```

```
2018-01-21 20:31:47.876  INFO 8413 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat started on port(s): 8090 (http)
2018-01-21 20:31:47.882  INFO 8413 --- [           main] c.w.t.WalmartTicketingSystemApplication  : Started WalmartTicketingSystemApplication in 15.365 seconds (JVM running for 15.771)
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
GET http://localhost:8090/api/venues
```

##### B. Finding available information about venue

Given the venue id the endpoint provides information on the venue name, available seats, hold seats, and reserved seats. The initial motivation behind the creation of this API endpoint is so that UI (e.g. ReactJS) can consume this API endpoint and visualize the venue. UI might be something that can be done in the future. 

```
GET http://localhost:8090/api/venues/{venueId}
```
If venueId isn't found, the request will return a ```404```.

##### C. Finding number of available seats given venueId

I am taking the assumption that this is one of the required endpoint as written in the coding challenge document, which is to display **ONLY** the available seats in a given venue as an integer.
```
GET http://localhost:8090/api/venues/{venueId}/availableSeats
```
If venueId isn't found, the request will return a ```404```.

### 2.2. Seat Holds

##### A. Finding seat holds and information about seat hold

Just like the API endpoint for venues, the following calls could be made:
```
GET http://localhost:8090/api/seatHolds
GET http://localhost:8090/api/seatHolds/{seatHoldId}
```

##### B. Creating seat holds

```
POST http://localhost:8090/api/seatHolds?email={email}&venueId={venueId}&numSeats={numSeats}
```
The following **required** query parameters customizes your request. 

| param   | type   | Description   | 
| ------- |------  | ------------- |
| email	  | String | Provide email of the customer. If the email does not exist in the Customer database, a new Customer entry will be created in the database automatically with the email.  |
| venueId | String | Provide existing ID of venue (as can be obtained from ```/api/venues```). If venue does not exist, then an error message will be displayed.  |
| numSeats| int | Provide number of seats wanting to be held (number must be strictly larger than 0). If the number of seats requested to be held exceeds the number of available seats in the venue, then an error message will be displayed.  |

Sample successful seat holding (upon success of request, link to the seat hold will be provided for review):
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
GET http://localhost:8090/api/seatReserves
GET http://localhost:8090/api/seatReserves/{seatReservationId}
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


Sample successful seat holding (upon success of request, link to the seat reservation will be provided for review):
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

## 3. Assumptions in the Project

- The project will run on ```localhost:8090``` and will be using an embedded H2 database.
- The seat layout for each venue is a rectangle with dimensions NxM, and the number of seats is N * M. For example, a seat layout of 5x6 would have a total number of 30 seats, 5 consecutive seats for each 6 rows. 1 seat can only be for 1 person.
- The venues and its seats are initialized with ```src/main/resources/data.sql```
- There are three states to a seat, 'available,' 'held,' and 'reserved.'
- An 'available' seat can become a 'held' seat through a request from API endpoint.
- A 'held' seat can only become an 'available' if the seat has been in 'held' for ```100``` seconds (this setting can be changed under. ```src/main/resources/application.properties``` for ```hold.expirationSeconds```). There cannot be manual intervention to make a 'held' seat back to 'available.'
- A 'held' seat can only become a 'reserved' seat if the seat is reservation is made only through the API endpoint. 
- Once a seat becomes 'reserved' it will stay 'reserved' in the lifetime of the H2 database. 
- Each seat that belongs to seat hold/seat reservation must only be from one venue.
- One customer can make multiple holds and multiple reservations. 

## 4. Best Seat Algorithm Design
The problem itself seems like a known disk-allocation problem. If this weren't an excercise I would research on what the best disk-allocation algorithms are present, but since the task is to come up with one on your own, I've spared the arduous task of researching for the best disk-allocation algorithm. The short description to my best seat algorithm is it is a modification of a contigous allocation. The problem is since we are considering 'people' in this algorithm, if we have venue of 5x5 and there is a request for 6 people, would we put 5 in one row and 1 in the next one? **My idea of a 'best seat' is that not one person should be isolated from the group.** To do that, given a 5x5 layout and 6 seat requests, my 'best seat' algorithm does the following:
- Factorize the number 6, and each factorization will be the potential dimension of the seating. Factorization of 6 results in 6x1, 3x2, and 2x3 (1x6 is ommitted because we don't want people be placed vertically away from the stage). 
- For each dimension, try to fit in the available seats of the venue layout. In this case, if all seats are available, then 3x2 would be the ideal dimension (larger width first). This way, not one person would be left out in the seating. If no dimensions are able to fit, then there are no 'best seats'.


### 4.1 Strengths to the chosen design

- Not one person would be alone, which fits my criteria of a 'best seat finder'.
- Works for requests for X number of seats where if there exists an integer N and M where N * M is the factorization of X and N <= number of columns in venue and M <= number of rows in venue.


### 4.2 Problems to the chosen design

- First problem is with prime numbers; say we have an empty 5x5 layout, and we would like 7 people to be placed. The only available factorization is 7x1, which would not fit, resulting in no 'best seats', which is untrue.
- Second problem, using the same empty 5x5 layout, say we would like to request 24 seats. None of its factorization would result in a dimension that will fit. This problem would have been easily solved with contigous allocation. 

### 4.3 Improving the chosen design

Although the current 'best-seat finder' was implemented, considering the problems of the implementation, I wish that I had more chance to improve it. 
- One thing could be done is, before applying the current best-seat finder, first to do a contiguous allocation. If there exists a person who will be isolated (i.e. person/people separated from where the seat is mostly allocated), then instead use the current best-seat finder. Still, this won't be a foolproof design since the two problems mentioned in 4.2 could still very well persist. 
- Another alternative is to try the current best-seat finder but if it fails (due to either reasons 1 or 2 in section 4.2), come up with an algorithm that can separate the requested seat number in a way that it would fit. For example, in a 5x5 venue with a 24 seat request, the number 24 could be divided into two seat requests of 20 and 4. In which case, the venue will be inserted with a block of 5x4 and 4x1. It could work for the prime number problem as well. For example, 7 seat requests can be devided into 4 and 3 seat requests with 4x1 and 3x1 block. But this will require finding the optimal algorithm to separate a seat request to multiple seat requests in a way that it fits the venue and does not leave any one person isolated. 

### 4.4 Considering other designs
- I was considering an option where a 'seat' is defined merely a 'collection of seats,' analogous to what we call a 'section.' For example, section on coordinate (0,0) can have the capacity of 100 people. But those 100 does not have any location property on them (i.e. a capacity is not identified by any coordinates, it is just 'there' in that section). This would certainly eliminate the need to make a SQL insert statement for every single seat and it would be easier to imagine venues in a large scale. The solution to finding the 'best seat' would also be very straight forward. Meaning that if a user requests for an X number of seats, find the most front and then most left section that whose capacity > X. And substract X from capacity, ergo that would be the new capacity of the section. The X number of people that has just been assigned as section can sit however they want, without the seat being recorded in the database. If it comes a situation where there are no more sections that can hold the X number of seat requests, then that number could be separated among sections. It could be that one person be left out, but nevertheless, it's a solution that could result in no unused spaces. However, as straight-forward as it is, it does not seem to be the purpose of the coding challenge. I have the impression that one seat should be treated as 1 person, and not a capacity.
- I was considering using merely the contiguous allocation algorithm, following the memory model. The benefit of using this algorithm is that there would potentially be no unused seats. However, considering the context of 'people', like mentioned in the previous section, one person could be left out to their own different row. 
 
However, all in all, considering real-world context, what a 'best seat' is subjective to the person. It could be that there is one person who wants to be separated from the party to enjoy the show alone. 


## 5. Things to Improve

* Adding security layer (e.g. Basic Authentication)
* Adding UI, possibly best with ReactJS to leverage the API's
* Adding multiple AI's as threads that holds and reserves seats to simulate real-time interaction
* And of course, the 'best seat' algorithm
