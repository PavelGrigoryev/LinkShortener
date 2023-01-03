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
* Spring-boot-starter-test
* Lombok
* Mapstruct
* H2 (in memory)
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

## Functionalities

In summary the application can:

***LinkShortenerController [links.http](src/main/resources/links.http)***

* **POST**
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
* **GET**
    * Redirects via a short link to the original
    * http://localhost:8080/?shortLink=/1/noob-club
* **GET**
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
* **GET**
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