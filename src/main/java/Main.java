

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import com.fasterxml.jackson.databind.ObjectMapper;

@SuppressWarnings("serial")
public class Main extends HttpServlet {
    
    private static ObjectMapper JSON_MAPPER = new ObjectMapper();
    
    private static BattleSnakeHandlers HANDLERS = new BattleSnakeHandlers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(String.format("Request: GET %s", req.getRequestURI()));
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String uri = req.getRequestURI();
        Map<String, Object> requestObject = getRequestObject(req);
        System.out.println(String.format("Request: POST %s %s", uri, requestObject));
        
        Object responseObject;
        if (uri.endsWith("/start")) {
            responseObject = HANDLERS.handleStart(requestObject);
        } else if (uri.endsWith("/move")) {
            responseObject = HANDLERS.handleMove(requestObject);
        } else if (uri.endsWith("/end")) {
            responseObject = HANDLERS.handleEnd(requestObject);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        String responseJson = (responseObject != null) ? JSON_MAPPER.writeValueAsString(responseObject) : "{}";
        resp.getWriter().print(responseJson);
    }
    
    @SuppressWarnings("unchecked")
    private Map<String, Object> getRequestObject(HttpServletRequest req) throws IOException {
        Map<String, Object> requestObject = JSON_MAPPER.readValue(req.getReader(), Map.class);
        return requestObject;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new Main()), "/*");
        server.start();
        server.join();
    }
}
