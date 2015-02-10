

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Handlers {
    
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    public void handleStart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get the request object
        Map<String, Object> requestObject = getRequestObject(req);
        
        // Debug
        System.out.println(String.format("Request Body: %s", JSON_MAPPER.writeValueAsString(requestObject)));
        
        // Dummy Response
        Map<String, Object> responseObject = new HashMap<String, Object>();
        responseObject.put("name", "battlesnake-java");
        responseObject.put("color", "#ff0000");
        responseObject.put("head_url", "http://battlesnake-java.herokuapp.com/");
        responseObject.put("taunt", "battlesnake-java");
        
        // JSON Response
        String responseJson = JSON_MAPPER.writeValueAsString(responseObject);
        resp.getWriter().print(responseJson);
    }
    
    public void handleMove(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Get the request object
        Map<String, Object> requestObject = getRequestObject(req);
        
        // Debug
        System.out.println(String.format("Request Body: %s", JSON_MAPPER.writeValueAsString(requestObject)));
        
        // Dummy Response
        Map<String, Object> responseObject = new HashMap<String, Object>();
        responseObject.put("move", "up");
        responseObject.put("taunt", "going up!");
        
        // JSON Response
        String responseJson = JSON_MAPPER.writeValueAsString(responseObject);
        resp.getWriter().print(responseJson);
    }
    
    public void handleEnd(HttpServletRequest req, HttpServletResponse resp) {
        // No response required
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestObject(HttpServletRequest req) throws IOException {
        Map<String, Object> requestObject = JSON_MAPPER.readValue(req.getReader(), Map.class);
        return requestObject;
    }

}
