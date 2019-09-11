# account-transfers
A RESTful API for money transfers between accounts.

## Interface

### Create an account

`POST /accounts`

Responds `201 Created` on success, returning a unique account number.

### Get an account's balance

`GET /account/<number>`

Responds `200 OK` and account balance, or `404 Not Found` if the account does not exist.

### Transfer money between accounts

`PUT /account/<number>`

*Arguments*

- `"other_account":int` account number of the transfer destination
- `"value":int` value of money to be transferred

*Response*

- `200 OK` on success (requires that both accounts exist, the source account has sufficient balance and the transfer was successful).
- `404 Not Found` if either account does not exist.
- `409 Conflict` if the source account has insufficient balance for the transfer.
