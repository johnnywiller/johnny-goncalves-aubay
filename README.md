# Reservation system

Before you start read this document and make sure you follow these simple guidelines:

1. Clone this repository to your local environment
2. Create a branch for your solution
3. Commit and push your progress as many times as necessary and as you would normally do for any development.
4. Once you're happy with your solution create a pull request

## Problem

Your goal is to implement a simple reservation system with a basic search and booking functionality.

### Get list of experiences

**Given**

* location of the travel
* date of travel
* number of travelers

**When** you perform the search

**Then** you get the list of:

* experiences matching the location
* total price (for all travelers)

**API**

* Request:
    
    `/api/search?location={location}&date={date}&travelers={travelers}`

* Response example:
    ```
   {
        "search": {
            "location": {location},
            "date": {date},
            "travelers": {travelers}
        },
        "results": {
            "matching": 2,
            "items": [
                {
                    "code": 32324,
                    "price": 234.5
                },
                {
                    "code": 1236,
                    "price": 123.7
                }
            ]
        }        
  }
  ```

### Create a booking for an experience

**Given**

* attraction code
* date of travel
* number of travelers

**When** you create a booking

**Then** you get a booking confirmation:

* status (SUCCESS | FAILURE)
* booking details
* total price for all travelers

**API**

* Request:
    
    `/api/booking`
    
     ```
     {
        "code": 1236,
        "date": "20190512",
        "travelers": 2
     }
     ```

* Response:
    ```
    {
        "status": "SUCCESS",
        "message": "Your booking is confirmed",
        "code": 1236,
        "date": "20190512",
        "travelers": 2,
        "price": 123.7
    }
    ```

## Implementation considerations

We ask you to solve it using the **Java** programming language. You are only allowed to use test libraries (JUnit, TestNG, Hamcrest, ...) and nothing else.
The project skeleton is already provided, with Gradle configured as build tool.

Availability, locations and prices are stored in an [SQLite](https://www.sqlite.org/index.html) database included in this application. It uses [jdbi](http://jdbi.org) as a thin wrapper to more conveniently access the relational database. The database file itself is located in `src/main/resources`

### Tables

All the tables you will need to implement your solution are already available in the SQLite database included in the application.

* Tables `experiences`, `availability` and `prices` have all the data you'll need pre-populated and you should treat them as read-only.
* Table `bookings` is empty and you are free to write to it in order to persist booking data.

**experiences**

| Column             | Type     | Description                                           |
|--------------------|----------|-------------------------------------------------------|
| id                 | INTEGER  | Unique experience identifier                          |
| location           | TEXT     | Name of the city where the experience is located      |

**availability**

| Column             | Type     | Description                                           |
|--------------------|----------|-------------------------------------------------------|
| experience_id      | INTEGER  | FK of experiences.id                                  |
| tickets            | INTEGER  | Number of tickets available. Defaults to zero         |

**prices**

| Column             | Type     | Description                                           |
|--------------------|----------|-------------------------------------------------------|
| experience_id      | INTEGER  | FK of experiences.id                                  |
| price              | REAL     | Base price per ticket                                 |
| currency           | TEXT     | Currency in which the price is set. Defaults to EUR   |

**bookings**

| Column             | Type     | Description                                           |
|--------------------|----------|-------------------------------------------------------|
| experience_id      | INTEGER  | FK of experiences.id                                  |
| booking_date       | TEXT     | Date booked                                           |
| travelers          | INTEGER  | Number of travelers or tickets                        |
| price              | REAL     | Total amount charged                                  |
| currency           | TEXT     | Currency in which the price is set. Defaults to EUR   |

Remember that **automated and self-checking tests** are mandatory. You should provide sufficient evidence that your solution is complete by, at a minimum, indicating that it works correctly against the supplied test data.

Keep the code clean and maintainable!

## What we value most?

The goal is to provide us with a full understanding of your coding style and skills. We’ll pay particular attention to:

* readable and well-structured code
* design and domain modeling
* quality of tests

## Business rules and constraints

* Search only for attractions with availability for the number of travelers
* Each booking decreases availability
* The current date is defined by the server and the timezone should always be UTC/GMT
* There is only one currency (€)
* Ticket price is based on the number of days to the travel date and the base price of the attraction:

| days prior to the travel date | % of the base price |
|-------------------------------|---------------------|
| more than 30 (i.e. >= 31)     | 80%                 |
| 30 - 16                       | 100%                |
| 15 - 3                        | 120%                |
| less that 3 (i.e. <= 2)       | 150%                |

## Examples

* 1 traveler, 21 days to the experience date in Madrid

  experiences:

    * 2118, 165.00 EUR
    * 8169, 205.00 EUR
    * 8911, 180.00 EUR

* 3 travelers, 15 days to the experience date in Lisbon

  experiences:

    * 3282, 406.80 EUR (3 * (120% of 113))
    * 4107, 853.20 EUR (3 * (120% of 237))
    * 4369, 392.40 EUR (3 * (120% of 109))

* 2 travelers, 2 days to the experience date in London

  experiences:

    * 1423, 822.00 EUR (2 * (150% of 274))
    * 2818, 558.00 EUR (2 * (150% of 186))
    * 2974, 333.00 EUR (2 * (150% of 111))

* Oxford

  no experiences available

## Useful gradle commands

From the command line:

* Clean the solution: `./gradlew clean`
* Build the code (without tests): `./gradlew assemble`
* Build with tests: `./gradlew build`
* Clean and build: `./gradlew clean build`
* Run tests: `./gradlew test`

## Server

* Run the server: `./gradlew appRun`

You can also launch the server to debug the application from your IDE and attach the debugger to port `5005`:

* `./gradlew appRunDebug`

Once the server is running you can use your browser or an application like [postman](https://www.getpostman.com) to test the API endpoints:
* The API endpoints are exposed from: `http://localhost:8080/api`, e.g `http://localhost:8080/api/locations` returns a list of locations
* There is already a test health check endpoint at: `http://localhost:8080/api/health/check`
* There is also an empty start page at: `http://localhost:8080`