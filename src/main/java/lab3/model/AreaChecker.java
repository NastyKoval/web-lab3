package lab3.model;

import java.math.BigDecimal;

// Попадание точки в область:
// I четверть   — прямоугольник 0 ≤ x ≤ R, 0 ≤ y ≤ R/2
// III четверть — четверть круга радиуса R/2
// IV четверть  — треугольник (0; 0), (R/2; 0), (0; −R)
public final class AreaChecker {
    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private AreaChecker() { }

    public static boolean isHit(BigDecimal x, BigDecimal y, BigDecimal r) {
        BigDecimal halfR = r.divide(TWO);
        boolean right = x.signum() >= 0;
        boolean left = x.signum() <= 0;
        boolean up = y.signum() >= 0;
        boolean down = y.signum() <= 0;

        boolean rectangle = right && up && x.compareTo(r) <= 0 && y.compareTo(halfR) <= 0;
        boolean quarterCircle = left && down
                && x.multiply(x).add(y.multiply(y)).compareTo(halfR.multiply(halfR)) <= 0;
        // гипотенуза: y = 2x − R
        boolean triangle = right && down && y.compareTo(x.multiply(TWO).subtract(r)) >= 0;

        return rectangle || quarterCircle || triangle;
    }
}
