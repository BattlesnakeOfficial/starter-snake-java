# battlesnake-java

A simple [BattleSnake AI](http://battlesnake.io) written in Java using [HttpServlet](http://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html).

This is based on Heroku's official [Getting Started with Java App](https://github.com/heroku/java-getting-started).

You'll need a working Java 7 development enviroment with [Maven](http://maven.apache.org/) installed.

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

### App Overview

- `BattleSnakeHandler.java` is where the three actions must be implemented: `start`, `move`, `end`

- `Main.java` handles the HttpServlet and request routing, as well as JSON parsing and rendering.

### Running the AI locally

Fork and clone this repo:

```
git clone https://github.com/heroku/java-getting-started.git
cd battlesnake-java
```

Install dependencies (requires [Maven](http://maven.apache.org/)):

```
mvn install
```

Run the server:

```
foreman start web
```

Test the client in your browser: [http://localhost:5000](http://localhost:5000)


### Deploying to Heroku

Click the Deploy to Heroku button at the top or use the command line commands below.

Create a new Heroku app:

```
heroku apps:create APP_NAME
```

Push code to Heroku servers:

```
git push heroku master
```

Open Heroku app in browser:

```
heroku open
```

Or go directly via http://APP_NAME.herokuapp.com

View/stream server logs:

```
heroku logs --tail
```
