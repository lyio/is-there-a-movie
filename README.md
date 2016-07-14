# is-there-a-movie

GENERAL
=======

**System Properties**

- dbname: "botomo",
- dburl:  "mongodb://localhost:27017" 

**Run the application**

- Start your local Mongo DB instance

- Create some test data (This also creates the database and collection):

Navigate to src/main/scripts and run

  ```shell
  ./import_test_data_to_mongo.sh
  ```

- Run the fat jar from console:

  ```shell
  java -jar -ddbname="botomo" -ddburl="mongodb://localhost:27017" build/libs/botomo-1.0-SNAPSHOT-fat.jar
  ```

API
===
**GET api/v1/books**
 
Response body:
   ```json
  [{
    "id":1,
    "title":"Lorem Ipsum",
    "subtitle":"Lorem Ipsum Sub",
    "author":"Loras Ipsum",
    "year":"2003",
    "ups":2,
    "downs":12
  },
  {
    ...
  }]
  ```

**POST api/v1/books**
 
Request body:
   ```json
  {
    "title":"Lorem Ipsum",
    "subtitle":"Lorem Ipsum Sub",
    "author":"Loras Ipsum",
    "year":"2003",
    "ups":2,
    "downs":12
  }
  ```

  Response:
  ```json
    {
      "id": "123",
      "title":"Lorem Ipsum",
      "subtitle":"Lorem Ipsum Sub",
      "author":"Loras Ipsum",
      "year":"2003",
      "ups":2,
      "downs":12
    }
  ```

**POST api/v1/books/:id/upvote**

Path parameters:
- id: book id

**POST api/v1/books/:id/downvote**

Path parameters:
- id: book id

**GET api/v1/books?search=searchterm**

Query parameter
- searchterm: search term

Response body:
   ```json
  [{
    "id":1,
    "title":"Lorem Ipsum",
    "subtitle":"Lorem Ipsum Sub",
    "author":"Loras Ipsum",
    "year":"2003",
    "ups":2,
    "downs":12
  },
  {
    ...
  }]
  ```
