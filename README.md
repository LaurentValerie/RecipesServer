# Recipes Server

## About the Service

The application is just a simple multi-user RESTful service for kitchen recipes sharing.
That allows to perform CRUD operations with recipes and provides Basic HTTP authentication.
It uses a PostgreSQL database to store the data. You can also use any other relational database.
If your database work, you can call some REST endpoints defined in localhost on port 8081. (see below)

## How to Run 

* Clone this repository
* Change the dir to the root of the project
* Edit [application.properties](src/main/resources/application.properties) - set database info (db url, db driver, username, password, dialect)
* If you use a DBMS other than postgreSQL edit [build.gradle](./build.gradle) (need to add jdbc-driver to the dependencies block)

### Build :

./gradlew build
### Run :
./gradlew bootRun

## Allowed endpoints

### User registration

POST /api/register

Accept: application/json
Content-Type: application/json

{
    "email": "email@example.example",
    "password": "password"
}

### User authentification

POST /api/register

To unlock the remaining endpoints, you need to log in using http basic auth. 

How it looks in IntelliJ Idea:

![image](https://user-images.githubusercontent.com/105499106/203878673-e7114983-b5af-4d71-9e24-e8bb45487073.png)

How it looks in Postman :

![image](https://user-images.githubusercontent.com/105499106/203878462-f6974a4a-bf0f-4d08-b567-e47ec1fba4e2.png)

### Create a recipe

POST /api/recipes/

Accept: application/json
Content-Type: application/json

{
"name": "Fresh Mint Tea",
   "category": "beverage",
   "description": "Light, aromatic and refreshing beverage, ...",
   "ingredients": ["boiled water", "honey", "fresh mint leaves"],
   "directions": ["Boil water", "Pour boiling hot water into a mug", "Add fresh mint leaves", "Mix and let the mint leaves seep for 3-5 minutes", "Add honey and mix again"]
}

How it looks in IntelliJ Idea:

![image](https://user-images.githubusercontent.com/105499106/203879130-5995cd99-ee53-44b4-b973-d7a7d4cf4163.png)

How it looks in Postman :

![image](https://user-images.githubusercontent.com/105499106/203879038-a0e00ab6-a15d-4947-afbd-6f9e1ee1e5df.png)
![image](https://user-images.githubusercontent.com/105499106/203879081-0e332e8e-3bce-48fe-90f8-af60294ce91d.png)

<b>All other queries look similar</b>
### Get all recipes sorted by update time

GET /api/recipes/

### Update a recipe

PUT /api/recipes/{id}
Accept: application/json
Content-Type: application/json

{
   "name": "Warming Ginger Tea",
   "category": "beverage",
   "description": "Ginger tea is a warming drink for cool weather, ...",
   "ingredients": ["1 inch ginger root, minced", "1/2 lemon, juiced", "1/2 teaspoon manuka honey"],
   "directions": ["Place all ingredients in a mug and fill with warm water (not too hot so you keep the beneficial honey compounds in tact)", "Steep for 5-10 minutes", "Drink and enjoy"]
}

### Delete a recipe by id

DELETE /api/recipes/{id}

### Retrieve a recipe by id

GET /api/recipes/{id}

### Search for recipes by name

GET /api/recipes/search/?name=Warming Ginder Tea

### Search for recipes by category

GET /api/recipes/search/?category=beverage
