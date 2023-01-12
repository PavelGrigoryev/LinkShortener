# LinkShortener application

Cuts the original link to a short one, the short one redirects to the original one.
There are also statistics of the most popular short links.

## Author: [Grigoryev Pavel](https://pavelgrigoryev.github.io/GrigoryevPavel/)

### Technologies that I used on the project:

* Java 17
* Maven
* Spring-boot 2.7.7
* Spring-boot-starter-data-jpa
* Spring-boot-starter-web (Apache Tomcat)
* Spring-boot-starter-security
* JWT(Json Web Token)
* Spring-boot-starter-test
* Spring-security-test
* Lombok
* Mapstruct
* H2 (in memory)
* Swagger
* Docker

### Instructions to run application locally:

1. You must have [Java 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html),
   [Intellij IDEA](https://www.jetbrains.com/idea/download/) installed
2. Run [LinkShortenerApplication.java](src/main/java/by/grigoryev/linkshortener/LinkShortenerApplication.java)
3. Application is ready to work

### Instruction to run application in Docker container:

1. You must have [Docker](https://www.docker.com/) installed
2. Run `mvn package`
3. Run line №2 in [docker-compose.yml](docker-compose.yml)
4. Application is ready to work

### Unit tests

You can run the unit tests for this project, by at the root of the project
executing:

```
mvn test
```

The unit tests will also be run every time the Docker image is rebuilt

## Documentation

To view the API Swagger documentation, start the application and see:

* [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

## Functionalities

In summary the application can:
***
***1. AuthenticationController [auth.http](src/main/resources/auth.http)***
***

* **POST register**
    * Registers a user and issues jwt that valid during 24 hours
    * http://localhost:8080/auth/register
    * Request body example:
  ````
      {
      "firstname": "Pavel",
      "lastname": "Shishkin",
      "email": "Georgiy@mail.com",
      "password": "1234"
      }
  ````
    * Response example:
  ````
      {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJHZW9yZ2l5QG1haWwuY29tIiwiaWF0IjoxNjczNTMxNjg1LCJleHAiOjE2NzM2MTgwODV9.USdnXwFkSRh6f1NT7HbWIKausZE0Jt-QEzaWkWtfCK4",
      "tokenExpiration": "Fri Jan 13 16:54:45 MSK 2023"
      }
  ````

* **POST authenticate**
    * You can get a new token if the previous one has expired entering your email and password
    * http://localhost:8080/auth/authenticate
    * Request body example:
  ````
     {
     "email": "Georgiy@mail.com",
     "password": "1234"
     }
  ````
    * Response example:
  ````
      {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJHZW9yZ2l5QG1haWwuY29tIiwiaWF0IjoxNjczNTMxNjg5LCJleHAiOjE2NzM2MTgwODl9.odSokMxjlMDV8ykkP06vTcdgNZU95DIyPwah1KDi6hI",
      "tokenExpiration": "Fri Jan 13 16:54:49 MSK 2023"
      }
  ````

***
***2. LinkShortenerController [links.http](src/main/resources/links.http)***

In [http-client.private.env.json](src/main/resources/http-client.private.env.json) line №3 you must insert your
jwt token issued to you during registration or authentication

***

* **POST generate**
    * Cuts the original link to a short one
    * http://localhost:8080/generate
    * Request body example:
  ````
      {
      "original": "https://www.noob-club.ru/index.php?frontpage;p=45"
      }
  ````
    * Response example:
  ````
      {
      "link": "/1/noob-club"
      }
  ````
* **GET redirect**
    * Redirects via a short link to the original
    * http://localhost:8080/?shortLink=/1/noob-club
* **GET stat**
    * Processes the GET request and returns statistics of clicks on a specific link
    * http://localhost:8080/stat/?shortLink=/1/noob-club
    * Response example:
  ````
      {
      "link": "/1/noob-club",
      "original": "https://www.noob-club.ru/index.php?frontpage;p=45",
      "rank": 2,
      "count": 0
      }
  ````
* **GET stats**
    * Processes the GET request and returns statistics of requests from sorting by frequency of requests in descending
      order and the possibility of paging display
    * page - page number
    * count - the number of records displayed on the page, the maximum possible value 100 (inclusive)
    * http://localhost:8080/stats/?page=1&count=3
    * Response example:
  ```` 
    [
      {
      "link": "/2/onliner",
      "original": "https://catalog.onliner.by/notebook?page=2",
      "rank": 1,
      "count": 1
      },
      {
      "link": "/1/noob-club",
      "original": "https://www.noob-club.ru/index.php?frontpage;p=45",
      "rank": 2,
      "count": 0
      },
      {
      "link": "/3/habr",
      "original": "https://habr.com/ru/news/t/708070/",
      "rank": 3,
      "count": 0
      }
    ]
  ````