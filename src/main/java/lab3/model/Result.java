package lab3.model;

import java.io.Serializable;

// Результат одной проверки. Геттеры нужны для EL: ${row.x}.
public class Result implements Serializable {
    private final String x;
    private final String y;
    private final String r;
    private final boolean hit;
    private final String time;
    private final long execMicros;

    public Result(String x, String y, String r, boolean hit, String time, long execMicros) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.time = time;
        this.execMicros = execMicros;
    }

    public String getX() { return x; }
    public String getY() { return y; }
    public String getR() { return r; }
    public boolean isHit() { return hit; }
    public String getTime() { return time; }
    public long getExecMicros() { return execMicros; }
}
