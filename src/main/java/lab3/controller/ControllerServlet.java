package lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

// Точка входа: запрос с x/y/r  AreaCheckServlet, иначе  index.jsp.
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Map<String, String[]> params = req.getParameterMap();
        boolean hasPoint = params.containsKey("x") || params.containsKey("y") || params.containsKey("r");

        if (hasPoint) {
            // у AreaCheckServlet нет URL, вызываем по имени из web.xml
            getServletContext().getNamedDispatcher("AreaCheckServlet").forward(req, resp);
        } else {
            req.getRequestDispatcher("/WEB-INF/jsp/index.jsp").forward(req, resp);
        }
    }
}
