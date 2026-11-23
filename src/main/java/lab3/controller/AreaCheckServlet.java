package lab3.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lab3.model.AreaChecker;
import lab3.model.PointValidator;
import lab3.model.Result;
import lab3.model.ResultHistory;
import lab3.model.ValidationException;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// Проверяет точки, сохраняет результаты в сессию, показывает result.jsp.
public class AreaCheckServlet extends HttpServlet {
    private static final DateTimeFormatter TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        long start = System.nanoTime();

        try {
            PointValidator.Query q = PointValidator.validate(req.getParameterMap());
            String now = LocalDateTime.now().format(TIME);

            List<Result> results = new ArrayList<>();
            for (BigDecimal x : q.xs()) {
                boolean hit = AreaChecker.isHit(x, q.y(), q.r());
                long execMicros = (System.nanoTime() - start) / 1000;
                results.add(new Result(x.toPlainString(), q.y().toPlainString(),
                        q.r().toPlainString(), hit, now, execMicros));
            }

            history(req.getSession()).addAll(results);
            req.setAttribute("results", results);
        } catch (ValidationException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            req.setAttribute("errors", e.getErrors());
        }

        req.getRequestDispatcher("/WEB-INF/jsp/result.jsp").forward(req, resp);
    }

    // история из сессии; создаётся при первом обращении
    private static ResultHistory history(HttpSession session) {
        ResultHistory h = (ResultHistory) session.getAttribute(ResultHistory.SESSION_KEY);
        if (h == null) {
            h = new ResultHistory();
            session.setAttribute(ResultHistory.SESSION_KEY, h);
        }
        return h;
    }
}
