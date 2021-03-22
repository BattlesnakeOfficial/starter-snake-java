package com.battlesnake.starter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.lang.Integer;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.get;

/**
 * This is a simple Battlesnake server written in Java.
 * 
 * For instructions see
 * https://github.com/BattlesnakeOfficial/starter-snake-java/README.md
 */
public class Snake {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();
    private static final Handler HANDLER = new Handler();
    private static final Logger LOG = LoggerFactory.getLogger(Snake.class);

    /**
     * Main entry point.
     *
     * @param args are ignored.
     */
    public static void main(String[] args) {
        String port = System.getProperty("PORT");
        if (port != null) {
            LOG.info("Found system provided port: {}", port);
        } else {
            LOG.info("Using default port: {}", port);
            port = "8080";
        }
        port(Integer.parseInt(port));
        get("/",  HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/start", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/move", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/end", HANDLER::process, JSON_MAPPER::writeValueAsString);
    }

    /**
     * Handler class for dealing with the routes set up in the main method.
     */
    public static class Handler {
      // closest foot item seen so far ["x", "y", "steps_to_path"] values
      Map<Integer, ArrayList<Integer>> foodPathMap = new HashMap<>(); 
      
        

        /**
         * For the start/end request
         */
        private static final Map<String, String> EMPTY = new HashMap<>();

        /**
         * Generic processor that prints out the request and response from the methods.
         *
         * @param req
         * @param res
         * @return
         */
        public Map<String, String> process(Request req, Response res) {
            try {
                JsonNode parsedRequest = JSON_MAPPER.readTree(req.body());
                String uri = req.uri();
                LOG.info("{} called with: {}", uri, req.body());
                Map<String, String> snakeResponse;
                if (uri.equals("/")) {
                    snakeResponse = index();
                } else if (uri.equals("/start")) {
                    snakeResponse = start(parsedRequest);
                } else if (uri.equals("/move")) {
                    snakeResponse = move(parsedRequest);
                } else if (uri.equals("/end")) {
                    snakeResponse = end(parsedRequest);
                } else {
                    throw new IllegalAccessError("Strange call made to the snake: " + uri);
                }
                LOG.info("Responding with: {}", JSON_MAPPER.writeValueAsString(snakeResponse));
                return snakeResponse;
            } catch (Exception e) {
                LOG.warn("Something went wrong!", e);
                return null;
            }
        }

    
        /**
         * This method is called everytime your Battlesnake is entered into a game.
         * 
         * Use this method to decide how your Battlesnake is going to look on the board.
         *
         * @return a response back to the engine containing the Battlesnake setup
         *         values.
         */
        public Map<String, String> index() {         
            Map<String, String> response = new HashMap<>();
            response.put("apiversion", "1");
            response.put("author", "");           // TODO: Your Battlesnake Username
            response.put("color", "#639d62");     // TODO: Personalize
            response.put("head", "default");  // TODO: Personalize
            response.put("tail", "default");  // TODO: Personalize
            return response;
        }

        /**
         * This method is called everytime your Battlesnake is entered into a game.
         * 
         * Use this method to decide how your Battlesnake is going to look on the board.
         *
         * @param startRequest a JSON data map containing the information about the game
         *                     that is about to be played.
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> start(JsonNode startRequest) {
            LOG.info("START");
            return EMPTY;
        }

        /**
         * This method is called on every turn of a game. It's how your snake decides
         * where to move.
         * 
         * Valid moves are "up", "down", "left", or "right".
         *
         * @param moveRequest a map containing the JSON sent to this snake. Use this
         *                    data to decide your next move.
         * @return a response back to the engine containing Battlesnake movement values.
         */
        public Map<String, String> move(JsonNode moveRequest) {
            try {
                LOG.info("Data: {}", JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(moveRequest));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            String[] possibleMoves = { "up", "down", "left", "right" };

            // get board info
            JsonNode board = moveRequest.get("board");
            int bHeight = board.get("height").asInt();
            int bWidth = board.get("width").asInt();

            JsonNode foodItems = board.get("food");

            // get info about my snake
            JsonNode mySnake = moveRequest.get("you");
            int currX = mySnake.get("head").get("x").asInt();
            int currY = mySnake.get("head").get("y").asInt();

            // get obstractions
            int topBodyX = mySnake.get("body").get(0).get("x").asInt();
            int topBodyY = mySnake.get("body").get(0).get("y").asInt();
            int currHealth = mySnake.get("health").asInt();

            int[] result = findShortestPathToFood(currX, currY, foodItems);
            int foodX = result[0];
            int foodY = result[1];

            // get info about other items on board
            JsonNode snakesOnBoard = board.get("snakes");
            checkPossibleMoves(currX, currY, possibleMoves, snakesOnBoard);
            try {
                LOG.info("POSSIBLE MOVES: {}", JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(possibleMoves));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if(currX > foodX){
              // if no obstruction to the left
              if(currX - 1 >= 0 && possibleMoves[2].equals("left")){
                // if no obstruction, return move
                String move = "left";
                LOG.info("MOVE {}", move);
                Map<String, String> response = new HashMap<>();
                response.put("move", move);
                return response;
              }
            } else if (currX < foodX) {
              // if no obstruction to the right
              if (currX + 1 <= bWidth && possibleMoves[3].equals("right")){
                String move = "right";
                LOG.info("MOVE {}", move);
                Map<String, String> response = new HashMap<>();
                response.put("move", move);
                return response;
              }
            }

            // if couldnt go left or right, either faced an obstruction
            // or currX == foodX so try to move Up/Down
            if(currY > foodY){
              // if no obstruction to the down
              if(currY - 1 >= 0 && possibleMoves[1].equals("down")){
                // if no obstruction, return move
                String move = "down";
                LOG.info("MOVE {}", move);
                Map<String, String> response = new HashMap<>();
                response.put("move", move);
                return response;
              }
            } else if(currY < foodY){
              // if no obstruction to the up
              if(currY + 1 <= bHeight && possibleMoves[0].equals("up")){
                // if no obstruction, return move
                String move = "up";
                LOG.info("MOVE {}", move);
                Map<String, String> response = new HashMap<>();
                response.put("move", move);
                return response;
              }
            }

            for(String s: possibleMoves){
              if(!s.equals("OBSTRUCTION")){
                if(s.equals("up")){
                  if (currY + 1 <= bHeight){
                  LOG.info("OBSTRUCTIONS AT TWO PLACES, USE OTHER: ", s);
                  Map<String, String> response = new HashMap<>();
                  response.put("move", s);
                  return response;
                  }
                } else if (s.equals("down")){
                  if (currY - 1 >= 0){
                  LOG.info("OBSTRUCTIONS AT TWO PLACES, USE OTHER: ", s);
                  Map<String, String> response = new HashMap<>();
                  response.put("move", s);
                  return response;
                  }
                } else if (s.equals("left")){
                  if (currX - 1 >= 0){
                  LOG.info("OBSTRUCTIONS AT TWO PLACES, USE OTHER: ", s);
                  Map<String, String> response = new HashMap<>();
                  response.put("move", s);
                  return response;
                  }
                } else if (s.equals("right")){
                  if (currX + 1 <= bWidth){
                  LOG.info("OBSTRUCTIONS AT TWO PLACES, USE OTHER: ", s);
                  Map<String, String> response = new HashMap<>();
                  response.put("move", s);
                  return response;
                  }
                }
              }
            }

            // Choose a random direction to move in
            int choice = new Random().nextInt(possibleMoves.length);
            String move = possibleMoves[choice];

            LOG.info("PROBLEM HAPPENED WE ARE USING RANDOM MOVE {}", move);

            Map<String, String> response = new HashMap<>();
            response.put("move", move);
            return response;
        }

        public void checkPossibleMoves(int currX, int currY, String[] possibleMoves, JsonNode snakesOnBoard){

          int possibleLeft = currX - 1;
          int possibleUp = currY + 1;
          int possibleRight = currX + 1;
          int possibleDown = currY - 1;

          for(int i = 0; i < snakesOnBoard.size(); i++){
            JsonNode currSnakeBody = snakesOnBoard.get(i).get("body");
            for(int j = 0; j < currSnakeBody.size(); j++){
              int snakeX = currSnakeBody.get(j).get("x").asInt();
              int snakeY = currSnakeBody.get(j).get("y").asInt();
              // checkLeft
              if (snakeX == possibleLeft && snakeY == currY){
                // if snake body is in space of our snakes LEFT
                // then remove LEFT as a possible move
                possibleMoves[2] = "OBSTRUCTION";
              } else if (snakeX == possibleRight && snakeY == currY){
                // if snake body is in space of our snakes RIGHT
                // then remove RIGHT as a possible move
                possibleMoves[3] = "OBSTRUCTION";
              }else if (snakeY == possibleUp && snakeX == currX){
                // if snake body is in space of our snakes UP
                // then remove UP as a possible move
                possibleMoves[0] = "OBSTRUCTION";
              } else if (snakeY == possibleDown && snakeX == currX){
                // if snake body is in space of our snakes DOWN
                // then remove DOWN as a possible move
                possibleMoves[1] = "OBSTRUCTION";
              }
            }
          }
          
        }
        public int[] findShortestPathToFood(int currX, int currY, JsonNode foodItems){
          int[] shortestPathSoFar = new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};

          for(int i = 0; i < foodItems.size(); i++){
            JsonNode currNode = foodItems.get(i);
            int fPrimeX = currNode.get("x").asInt();
            int fPrimeY = currNode.get("y").asInt();
            
            // calc path to food item
            int xSlope = Math.abs(currX - fPrimeX);
            int ySlope = Math.abs(currY - fPrimeY);
            int shortestPath = xSlope + ySlope;
          
            // assign closest food item seen so far
            if(shortestPath < shortestPathSoFar[2]){
             shortestPathSoFar[0] = fPrimeX;
             shortestPathSoFar[1] = fPrimeY;
             shortestPathSoFar[2] = shortestPath;
            }
          }
          return shortestPathSoFar;
        }

        /**
         * This method is called when a game your Battlesnake was in ends.
         * 
         * It is purely for informational purposes, you don't have to make any decisions
         * here.
         *
         * @param endRequest a map containing the JSON sent to this snake. Use this data
         *                   to know which game has ended
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> end(JsonNode endRequest) {

            LOG.info("END");
            return EMPTY;
        }
    }

}
