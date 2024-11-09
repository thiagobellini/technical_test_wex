# Application Test WEX

To meet the first requirement, a CRUD was implemented for purchases, with the following possibilities:
* POST - Create a new purchase
* GET - Search for all registered purchases
* GET - Search purchase by Id
* PUT - Update a purchase
* DELETE - Delete a purchase

Taking into account requirements such as:
* Description: must not exceed 50 characters
* Transaction date: must be a valid date format
* Purchase amount: must be a valid positive amount rounded to the nearest cent
* Unique identifier: must uniquely identify the purchase

\
To meet the second requirement, two endpoints were implemented, with the following possibilities:
* GET - Fetch all purchases with the exchange rate
* GET - Search purchase by Id with exchange rate

Taking into account requirements such as:
* When converting between currencies, you do not need an exact date match, but must use a currency conversion rate less than or equal to the purchase date from within the last 6 months.
* If no currency conversion rate is available within 6 months equal to or before the purchase date, an error should be returned stating the purchase cannot be converted to the target currency.
* The converted purchase amount to the target currency should be rounded to two decimal places (i.e., cent).

\
In addition, unit tests and integration tests have been implemented to ensure the effectiveness of the system.

\
To use the system, two alternatives were provided. Swagger, which can be accessed via the link below, and Postman, which can be found in a collection in the postman folder.

[http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

[Postman](postman)

\
\
The application uses Docker. To upload it, simply have Docker installed on your machine, go into the root folder of the project and run the command:

```docker-compose up --build -d```