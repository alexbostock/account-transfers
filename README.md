# account-transfers

A RESTful API for money transfers between accounts.

Build and run using Maven:

```
mvn verify
mvn exec:java
```

## Interface

All arguments must be passed as JSON in the request body.

Where values are returned, they are returned as JSON.

### Create an account

`POST /accounts`

This takes a single argument: `initial_balance`, the account's initial balance.

Responds `201 Created` on success, returning a unique account number.

`{account_number: 12345678}`

### Get an account's balance

`GET /account/<number>`

Responds `200 OK` and account balance, or `404 Not Found` if the account does not exist.

`{balance: 1000}`

### Transfer money between accounts

`PUT /account/<number>`

*Arguments*

- `"other_account"` account number of the transfer destination
- `"value"` value of money to be transferred, as an integer number of pence, cents or similar

*Response*

- `200 OK` on success (requires that both accounts exist, the source account has sufficient balance and the transfer was successful).
- `400 Bad Request` if the request is invalid, which may occur if the source and destination account numbers are not distinct.
- `404 Not Found` if either account does not exist.
- `409 Conflict` if the source account has insufficient balance for the transfer.

## Sample Output

```
$ http POST localhost:4567/accounts initial_balance=1000
HTTP/1.1 201 Created
Content-Type: text/json
Date: Wed, 11 Sep 2019 19:29:25 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

{account_number: 00000000}

$ http POST localhost:4567/accounts initial_balance=1000
HTTP/1.1 201 Created
Content-Type: text/json
Date: Wed, 11 Sep 2019 19:29:36 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

{account_number: 00000001}

$ http GET localhost:4567/account/00000000
HTTP/1.1 200 OK
Content-Type: text/json
Date: Wed, 11 Sep 2019 19:30:08 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

{balance: 1000}

$ http PUT localhost:4567/account/00000000 other_account=00000000 value=500
HTTP/1.1 400 Bad Request
Content-Type: text/plain
Date: Wed, 11 Sep 2019 19:30:29 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

400 Bad Request

$ http PUT localhost:4567/account/00000000 other_account=00000001 value=500
HTTP/1.1 200 OK
Content-Type: text/plain
Date: Wed, 11 Sep 2019 19:30:33 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

200 OK

$ http PUT localhost:4567/account/00000000 other_account=00000001 value=1000
HTTP/1.1 409 Conflict
Content-Type: text/plain
Date: Wed, 11 Sep 2019 19:30:49 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

409 Conflict

$ http GET localhost:4567/account/00000000
HTTP/1.1 200 OK
Content-Type: text/json
Date: Wed, 11 Sep 2019 19:30:55 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

{balance: 500}

$ http GET localhost:4567/account/00000001
HTTP/1.1 200 OK
Content-Type: text/json
Date: Wed, 11 Sep 2019 19:30:57 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

{balance: 1500}

$ http PUT localhost:4567/account/00000001 other_account=00000003 value=500
HTTP/1.1 404 Not Found
Content-Type: text/plain
Date: Wed, 11 Sep 2019 19:31:31 GMT
Server: Jetty(9.3.6.v20151106)
Transfer-Encoding: chunked

404 Not Found

```
