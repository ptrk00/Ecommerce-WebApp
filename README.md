# Ecommerce App
I made that simple CRUD web application for learning purposes. It uses Spring Boot as a backend, MySQL as database and Thymeleaf for generating html pages.

In order to make it slightly more advanced than typical simple beginner level CRUD I'm planning to consistently add new technologies like Stripe payment, email sending, cache db, elastic search, task scheduler etc.

I mainly focused on the backend, the frontend is minimalistic, and I did not pay much attention to make it more advanced. 

The main intention was to learn about web application development: api programming, database communication etc. not to learn solely Spring.

### Build With
* Spring Boot
    * web
    * data
    * security
    * validation
    * mail
    * test
* Thymeleaf
* MySQL and h2 databases
* Flyway
* Lombok
* AssertJ, JUnit, Mockito
* Bootstrap

### What I have learnt
* Managing project development with Maven, project lifecycle, using custom Maven plugins, working with pom file
* Spring basics, dependency injection concept, spring profiles
* Hibernate basics, entity lifecycle, sorting and pagination  
* How to write tests, difference between unit and integration tests, mocking
* Working with MVC architecture, what are controller, service, repository responsible for
* Using template engines like Thymeleaf, using boostrap to style the page
* Project configuration, working with yaml files
* Organising project structure
* Working with SQL Databases, modelling associations between model classes
* Creating separate configuration for testing environment e.g., setting up different database
* Using database migration tools like Flyway
* User authentication and authorization
* Working with http session
* Validating user input

### Features
* user authentication and authorization
* user can list all products and sort them 
* user can add selected products to cart
* user can make an order that contains products present in the cart
* and more...

### TODO
* set up redis cache db
* create Dto objects  
* use Stripe payments
* enable removing from shopping cart
* prompt user to enable his account via email
* create PUT (PATCH) and DELETE http methods handlers for few model classes
* remove eager loading (Hibernate), use entity Graph 
* find usage for task scheduler and use it in the project
* use elasticsearch
* add "final thoughts" section in `README.md` after finishing project

### License
Distributed under the MIT License. See `LICENSE.md` for more information.