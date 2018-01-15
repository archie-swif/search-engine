# search-engine

Reverse index document search with simple ranking*

## Main features:


1. Put documents into the search engine by key.
2. Get document by key.
3. Search on a string of tokens to return keys of all documents that contain **all** tokens in the set.
4. \*Search results **ranking** is done based on word frequency in documents.


## Installation and running

Runnable jar can be downloaded from the [release page](https://github.com/archie-swif/search-engine/releases)

And launched from the command-line

```java -jar search-engine-0.0.1.jar```


---

Also ,search-engine can be launched from Idea IDE or with maven:

```mvn spring-boot:run```

## Usage

Navigate to [localhost:8080](http://localhost:8080) to open a swagger page

Three rest APIs are available to test the search-engine app

![swagger](swagger.png)

## Tests

Unit and integration tests are available in the test directory.
Test coverage is **92% Lines**

![coverage](coverage.png)

## Spent time

* 3 hours to implement and test a primitive reverse index search
* 2 hours to refactor and pretify the code for better modularity
* 2 hours to add Spring Boot and implement REST API
* 3 hours for integration and unit tests

#### Total: **10 hours**

