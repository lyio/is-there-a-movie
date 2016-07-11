# is-there-a-movie

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
    "votes":{
      "up":2,
      "down":12
    }
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
    "votes":{
      "up":2,
      "down":12
    }
  }
  ```

**POST api/v1/books/:id/upvote**

Path parameters:
- id: book id

**POST api/v1/books/:id/downvote**

Path parameters:
- id: book id

**GET api/v1/books?search&searchterm**

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
    "votes":{
      "up":2,
      "down":12
    }
  },
  {
    ...
  }]
  ```
