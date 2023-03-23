# URL-Shortener REST-API

This API is designed to implement link shortening functionality.

# REST API

## Convert input URL to short format.

### Request

`⚪ POST /api/create-short`

    Request body: application/json
    Example value:
      {
        "longUrl": "yourLongURL.com"
      }
      
### Response

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


## Decode shortUrl to longUrl representation And redirect to longUrl.

### Request

`⚪ GET /api/{shortUrl}`

    Request body: application/json
    Example value: shortUrl in PathVariable : /api/7LK
      
### Response

    Status code: `302`
    Description: Url successfully found!
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

## Schemas:

### UrlDTO

`⚪ longUrl`

        type: string
        maxLength: 2147483647
        minLength: 3


`⚪ shortUrl`

        type: string
        maxLength: 2147483647
        minLength: 1


`⚪ expiresDate`

        type: string($date-time)

