package lab3.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Проверка параметров x, y, r. Собирает все ошибки сразу.
public final class PointValidator {
    private static final String NUMBER = "-?\\d+(\\.\\d+)?";
    private static final int MAX_LENGTH = 30;
    private static final BigDecimal X_MIN = new BigDecimal("-5");
    private static final BigDecimal X_MAX = new BigDecimal("3");
    private static final BigDecimal Y_MIN = new BigDecimal("-5");
    private static final BigDecimal Y_MAX = new BigDecimal("3");
    private static final Set<String> R_VALUES = Set.of("1", "2", "3", "4", "5");
    private static final int MAX_X_COUNT = 9;

    private PointValidator() { }

    public record Query(List<BigDecimal> xs, BigDecimal y, BigDecimal r) { }

    public static Query validate(Map<String, String[]> params) throws ValidationException {
        Set<String> errors = new LinkedHashSet<>();   // без повторов

        // X: 1–9 значений из [−5; 3] (диапазон, т.к. клик по графику даёт дробный X)
        List<BigDecimal> xs = new ArrayList<>();
        String[] xValues = params.get("x");
        if (xValues == null || xValues.length == 0) {
            errors.add("Выберите хотя бы одно значение X.");
        } else if (xValues.length > MAX_X_COUNT) {
            errors.add("Слишком много значений X.");
        } else {
            for (String s : new LinkedHashSet<>(List.of(xValues))) {
                BigDecimal x = parse(s);
                if (x == null) {
                    errors.add("X должно быть числом.");
                } else if (x.compareTo(X_MIN) < 0 || x.compareTo(X_MAX) > 0) {
                    errors.add("X должно быть в отрезке [-5; 3].");
                } else {
                    xs.add(x);
                }
            }
        }

        // Y: одно число из (−5; 3)
        BigDecimal y = null;
        String ys = single(params, "y");
        if (ys == null) {
            errors.add("Введите одно значение Y.");
        } else if ((y = parse(ys)) == null) {
            errors.add("Y должно быть числом, например 1.5.");
        } else if (y.compareTo(Y_MIN) <= 0 || y.compareTo(Y_MAX) >= 0) {
            errors.add("Y должно быть строго между -5 и 3.");
        }

        // R: одно значение из {1..5}
        String rs = single(params, "r");
        if (rs == null) {
            errors.add("Выберите один радиус R.");
        } else if (!R_VALUES.contains(rs)) {
            errors.add("R должно быть одним из: 1, 2, 3, 4, 5.");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(List.copyOf(errors));
        }
        return new Query(xs, y, new BigDecimal(rs));
    }

    // значение параметра, переданного ровно один раз; иначе null
    private static String single(Map<String, String[]> params, String name) {
        String[] values = params.get(name);
        if (values == null || values.length != 1 || values[0].isBlank()) return null;
        return values[0].trim();
    }

    // строка -> число; null, если не число или слишком длинная
    private static BigDecimal parse(String s) {
        s = s.trim().replace(',', '.');
        if (s.length() > MAX_LENGTH || !s.matches(NUMBER)) return null;
        return new BigDecimal(s);
    }
}
