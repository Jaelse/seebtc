# Instructions

The infrastructure is in the directory `infra`. It's a docker compose file.
To run this run the below command in `infra` directory:

```shell
docker-compose up -d
```

Once all the containers are running then you can start the BE application.

This project is built with Reactive Programing and use using kafka because it is a good fit for a
system that is required handle high number of transactions. kafka configuration is too basic. It can
be fine-tuned, but I didn't do it because it will require a lot of time. Consumer takes a while to
start consuming, so if you see the consumption hasn't started right away please wait a while.

# List of Endpoints

| Endpoint                          | Description                                                            |
|-----------------------------------|------------------------------------------------------------------------|
| POST /v1/wallets                  | Create a new Wallet with an initial balance                            |
| GET /v1/wallets/{id}              | Get wallet details by id                                               |
| GET /v1/wallets                   | Get list of wallets with query params                                  |
| GET /v1/allWallets                | Get all wallets                                                        |
| PATCH /v1/wallets/{id}            | Update wallet by id                                                    |
| DELETE /v1/wallets/{id}           | Delete wallet by id                                                    |
| POST /v1/wallets/{id}/transfer    | Create new Transaction to a wallet with id                             |
| GET /v1/transactions/{id}         | Get a transaction detail by id                                         |
| GET /v1/transactions              | Get list of transactions with query params                             |
| GET /v1/allTransactions           | Get all transactions                                                   |
| GET /v1/allBalance                | Get all Balance history for all wallets                                |
| GET /v1/balance/{id}              | Get Balance history by id                                              |
| GET /v1/balances                  | Get list of balance with query params                                  |
| GET /v1/wallet/{walletId}/balance | Get balance history of a wallet by id and with start time and end time |

# Run load test

Go to the load_testing directory. Run the following command to start the program.
First arg is the wallet id and the second is the number of senders.

```shell
go run main.go 64339ea06120341bad15586d 1
```

To stop the senders, press `cmd+c` on macbook.
It will take a while to stop if too many senders are there.

