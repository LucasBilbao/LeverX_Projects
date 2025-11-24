# TruGame

## LeverX Final Project (24/11/2025)

## Table of Contents

1. [Run Application Locally](#run-application-locally)
2. [Swagger](#swagger)

### Run application locally

- Clone repository

ssh:

```shell
    git clone git@github.com:LucasBilbao/LeverX_Projects.git
```

OR

http:

```shell
    git clone https://github.com/LucasBilbao/LeverX_Projects.git
```

- Checkout final_project_dev branch

```shell
    git checkout final_project_dev
```

- Enter the project folder

```shell
    cd ./TruGame
```

- Copy the .env.example to .env

```shell
    cp .env.example .env
```

- Fill the fields in the new .env file

    - NOTE: Do not use your real password for GMAIL_PASSWORD instead got
      to [Google App Passwords](https://myaccount.google.com/apppasswords) sign in and generate a new password for your
      app.

- Configure .env as the source for the project, can be done easily
  with [IntelliJ IDEA](https://www.jetbrains.com/help/idea/program-arguments-and-environment-variables.html#environment_variables).

- Install [Redis](https://redis.io/docs/latest/operate/oss_and_stack/install/archive/install-redis/) locally and then
  start it. Redis is used for caching in this application.

- Run App.main()

### Swagger

With Swagger, you can review all the API requests that TruGame supports.

When the application is running visit
the [Swagger Documentation](http://localhost:8080/trugame/api/swagger-ui/index.html).

NOTE: Once you try to log in as a user the server will respond with a jwt token which you can use by pressing the
Authorize button on the top or any of the lock icons on the endpoints. You will be greeted by a popup in which you
have to write "_Bearer <your_token>_", instead of _<your_token>_ enter the token that the server responded with on
log in and Authorize. After that you can use the all the endpoints that the User's role allows you to.
