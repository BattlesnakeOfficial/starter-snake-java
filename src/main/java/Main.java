

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

@SuppressWarnings("serial")
public class Main extends HttpServlet {
    
    private static Handlers HANDLERS = new Handlers();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(String.format("Request: GET %s", req.getRequestURI()));
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(String.format("Request: POST %s", req.getRequestURI()));
        if (req.getRequestURI().endsWith("/start")) {
            HANDLERS.handleStart(req, resp);
        } else if (req.getRequestURI().endsWith("/move")) {
            HANDLERS.handleMove(req, resp);
        } else if (req.getRequestURI().endsWith("/end")) {
            HANDLERS.handleEnd(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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
