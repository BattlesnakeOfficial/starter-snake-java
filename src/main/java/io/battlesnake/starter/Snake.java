package io.battlesnake.starter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.port;
import static spark.Spark.post;

/**
 * Snake server that deals with requests from the snake engine.
 * Just boiler plate code.  See the readme to get started.
 * It follows the spec here: https://github.com/battlesnakeio/docs/tree/master/apis/snake
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
        post("/start", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/move", HANDLER::process, JSON_MAPPER::writeValueAsString);
        post("/end", HANDLER::process, JSON_MAPPER::writeValueAsString);
    }

    /**
     * Handler class for dealing with the routes set up in the main method.
     */
    public static class Handler {

        /**
         * Generic processor that prints out the request and response from the methods.
         *
         * @param req
         * @param res
         * @return
         */
        public Map process(Request req, Response res) {
            try {
                Map bodyMap = JSON_MAPPER.readValue(req.body(), Map.class);
                String uri = req.uri();
                LOG.info("{} called with: {}", uri, req.body());
                Map snakeResponse = null;
                if (uri.equals("/start")) {
                    snakeResponse = start(bodyMap);
                } else if (uri.equals("/move")) {
                    snakeResponse = move(bodyMap);
                } else if (uri.equals("/end")) {
                    snakeResponse = end(bodyMap);
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
         * /start is called by the engine when a game is first run.
         *
         * @param bodyMap a map containing the JSON sent to this snake. See the spec for details of what this contains.
         * @return a response back to the engine containing the snake setup values.
         */
        public Map<String, String> start(Map bodyMap) {
            Map<String, String> response = new HashMap();
            response.put("color", "#ff00ff");
            return response;
        }

        /**
         * /move is called by the engine for each turn the snake has.
         *
         * @param bodyMap a map containing the JSON sent to this snake. See the spec for details of what this contains.
         * @return a response back to the engine containing snake movement values.
         */
        public Map<String, String> move(Map bodyMap) {
            Map<String, String> response = new HashMap();
            response.put("move", "right");
            return response;
        }

        /**
         * /end is called by the engine when a game is complete.
         *
         * @param bodyMap a map containing the JSON sent to this snake. See the spec for details of what this contains.
         * @return responses back to the engine are ignored.
         */
        public Map<String, String> end(Map bodyMap) {
            Map<String, String> response = new HashMap();
            return response;
        }
    }

}
