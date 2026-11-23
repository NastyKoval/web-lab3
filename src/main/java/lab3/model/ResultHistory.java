package lab3.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

// История проверок пользователя. Бин в сессии под ключом "history".
public class ResultHistory implements Serializable {
    public static final String SESSION_KEY = "history";

    // потокобезопасный: запросы одной сессии могут идти параллельно
    private final List<Result> results = new CopyOnWriteArrayList<>();

    public ResultHistory() { }

    public void addAll(List<Result> newResults) {
        results.addAll(newResults);
    }

    public List<Result> getResults() {
        return Collections.unmodifiableList(results);
    }

    // R последней проверки — чтобы отметить его в форме
    public String getLastR() {
        return results.isEmpty() ? "" : results.get(results.size() - 1).getR();
    }
}
