## Test project for Clevertec
### Technologies:
- #### stack:
  <pre>
    'org.apache.commons:commons-lang3:3.12.0'
    'com.itextpdf:itextpdf:5.5.13.3'
    'org.mapstruct:mapstruct:1.5.3.Final'
    'org.apache.logging.log4j:log4j-api:2.20.0'
    'org.apache.logging.log4j:log4j-core:2.20.0'
    'org.postgresql:postgresql:42.6.0'
    'com.fasterxml.jackson.core:jackson-databind:2.15.0-rc2'
    'org.apache.commons:commons-configuration2:2.9.0'
    'org.yaml:snakeyaml:2.0'
    'jakarta.servlet:jakarta.servlet-api:6.0.0'
    'org.projectlombok:lombok:1.18.26'
    'org.projectlombok:lombok:1.18.26'
    'org.mapstruct:mapstruct-processor:1.5.3.Final'
  </pre>

### Configurations
  - datasource:
    - url: jdbc:postgresql://localhost:5432/clevertec
    - username: postgres
    - password: 172143
    - ddl:
      - auto: [create, drop-create]
    - liquibase:
      - changeLog:
      - file: /liquibase/db.changelog.xml
      - schema: market
<pre>
      create - execute changes
      drop-create - drop schema and execute changes
</pre>

## Endpoints:
### Check
  - /Servlet-1.0-SNAPSHOT/receipts/pdf
    - params:
      - itemId
      - card
    - status: 200
    - response: pdf file
### Products:
  - #### GET
      - /Servlet-1.0-SNAPSHOT/products/{id}
        - status: 200
          - response: application/json
            <pre>{
              "id": 1,
              "description": "Loren Ipsum",
              "price": 1.55,
              "quantity": 10,
              "is_discount": true
            }</pre>
      - /Servlet-1.0-SNAPSHOT/products/all
        - status 200
          - response: application/json
            <pre>[
              {
                "id": 1,
                "description": "Loren Ipsum",
                "price": 1.55,
                "quantity": 10,
                "is_discount": true
              },
              {
                "id": 2,
                "description": "Dolor",
                "price": 2.34,
                "quantity": 10,
                "is_discount": false
              }
            ]</pre>
      - /Servlet-1.0-SNAPSHOT/products?page=0&size=4
        - status 200
        - response: application/json
          <pre>{
            "page": 1,
            "size": 1,
            "max_pages": 29,
            "content": [
              {
                "id": 1,
                "description": "Loren Ipsum",
                "price": 1.55,
                "quantity": 10,
                "is_discount": true
              }
            ]
          }</pre>
  - #### POST
    - /Servlet-1.0-SNAPSHOT/products
      - request:
        <pre>{
          "description": "Alcohol 2001",
          "price": 2001,
          "quantity": 9,
          "is_discount": true
        }</pre>
      - response
        - location: /Servlet-1.0-SNAPSHOT/products/{id}
      
  - #### PUT
    - /Servlet-1.0-SNAPSHOT/products/{id}
      - request:
        <pre>{
          "description": "Product",
          "price": 1,
          "quantity": 0,
          "is_discount": true
        }</pre>
      - response
        - location: /Servlet-1.0-SNAPSHOT/products/{id}
  - #### DELETE
    - /Servlet-1.0-SNAPSHOT/products/{id}
      - status: 204
      
### Discount card:
  - #### GET
    - /Servlet-1.0-SNAPSHOT/cards/{id}
      - status: 200
        - response: application/json
        <pre>{
          "id": 1,
          "discount": 1
        }</pre>
    - /Servlet-1.0-SNAPSHOT/cards?page=1&size=2
      - status 200
      - response: application/json
        <pre>{
          "page": 1,
          "size": 2,
          "max_pages": 2,
          "content": [
            {
              "id": 1,
              "discount": 1
            },
            {
              "id": 2,
              "discount": 10
            }
          ]
        }</pre>
  - #### POST
    - /Servlet-1.0-SNAPSHOT/cards
      - request:
        <pre>{
          "discount":100
        }</pre>
      - response
        - location: /Servlet-1.0-SNAPSHOT/cards/{id}
  - #### PUT
    - /Servlet-1.0-SNAPSHOT/cards/{id}
      - request:
        <pre>{
          "discount":100
        }</pre>
      - response
        - location: /Servlet-1.0-SNAPSHOT/products/{id}
  - #### DELETE
    - /Servlet-1.0-SNAPSHOT/cards/{id}
      - status: 204