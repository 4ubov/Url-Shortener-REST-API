# URL-Shortener REST-API

##### This API is designed to implement link shortening functionality.

If you're have a URl like this - someLongUrl.subLinkOnHost.mail.com/index.html

This API can make it to this - localhost:8080/7KX 

If you're have server with domain - short.com It will be looks like - short.com/7KX

someLongUrl.subLinkOnHost.mail.com/index.html  --->  short.com/7KX


# REST API

### Request description: Convert input URL to short format.

#### Request

<details>

  <summary>⚪ POST /api/create-short</summary>
  
  
    Request body: application/json
    Example value:
      {
        "longUrl": "yourLongURL.com"
      }
      
      
#### Response

    Status code: `201`
    Description: Successfully converted!
    Media type: application/json
    Example value:
      {
        "shortUrl": "7LK"
      }
      
      
    Status code: `400`
    Description: Bad Request!
    Media type: application/json
    Example value:
      {
        "statusCode": 400,
        "message": "We'll need a valid URL, like 'yourbrnd.co/niceurl'",
        "timestamp": 1679584728598
      }

</details>

</br>

### Request description: Decode shortUrl to longUrl representation And redirect to longUrl.

#### Request

<details>

<summary>⚪ GET /api/{shortUrl}</summary>


    Request body: application/json
    Example value: shortUrl in PathVariable : /api/7LK
      
#### Response

    Status code: `302`
    Description: Url successfully found! As a result of sending this request, you will be redirected to http://yourLongUrl.com
    Media type: application/http
    Example value:
      "redirect : http://yourLongURL.com"
       <html>
            ...
            ...
            ...
       </html>
       
       
      Status code: `404`
      Description: Url not Found!
      Media type: application/json
      Example value:
        {
          "statusCode": 404,
          "message": "This shortUrl doesn't exist or his duration was expired",
          "timestamp": 1679587416465
        }
        
</details>

</br>

### Schemas:

#### `UrlDTO`

<details>

<summary>⚪ longUrl</summary>

        type: string
        maxLength: 2147483647
        minLength: 3

</details>

<details>

<summary>⚪ shortUrl</summary>

        type: string
        maxLength: 2147483647
        minLength: 1

</details>

<details>

<summary>⚪ expiresDate</summary>

        type: string($date-time)

</details>

</br>

### UrlShortener DataBase

#### PostgreSQL code: <a href="https://github.com/4ubov/Url-Shortener-REST-API/blob/master/src/main/resources/url_shortener_db.sql">Link</a>
![image](https://user-images.githubusercontent.com/46792640/227319259-f43cd17a-f61a-4545-a3b4-dfda5ef132f4.png)


</br>

### Client side on React js, that implements this REST-API - <a href="https://github.com/4ubov/React-Client-URl-Shortener-API">ReactJs-Client</a>
