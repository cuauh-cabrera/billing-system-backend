## Billing System API
This is a billing backend application that provides a RESTful API for managing accounts and performing transactions. The API is designed to be scalable, secure, and easy to use.

## Main Functionalities

* Create Account: Create a new account with the given details.
* Retrieve Account: Retrieve an account by ID
* Search Accounts: Search for accounts based on various criteria (name, bill cycle day, last bill date, status).

## Transaction Management

* Perform Transaction: Perform a transaction on an account (charge or credit).

## Features

* Pagination: Search results are paginated for easier navigation.
* Sorting: Search results can be sorted by various fields (name, bill cycle day, last bill date).
* Advanced filtering: Search results can be filtered based on multiple criteria.
* Error Handling: The API returns meaningful error responses for invalid requests or internal errors.

## Documentation

The API documentation is available at http:// localhost:{port}/swagger-ui.html

## Requirements

* Java 21
* Spring Boot 3.3.4
* Maven
* MySQL 8.0 or higher

## Dependencies
* Spring Boot
* Spring Data JPA
* Spring Web
* Spring Boot DevTools
* MySQL Driver
* Lombok

## Project Build
To build the project, you need to have the following tools installed:

*	Java JDK 17 or higher
*	Maven 3.1.1 or higher
*	Git
*   IDE (IntelliJ, Netbeans,Eclipse, VScode)

Clone the project from GitHub using the following commands:

```sh
  $ cd your-local-folder
```
```sh
  $ git clone https://github.com/cuauh-cabrera/billing-system-backend.git
```
```sh
  $ git clone git@github.com:cuauh-cabrera/billing-system-backend.git
```
```sh
  $ mvn clean install
```
### Note: 

This README provides a high-level overview of the project's features and functionalities. For more detailed information, please refer to the API documentation and code comments.