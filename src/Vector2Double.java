
public class Vector2Double {
    double x;
    double y;

    public Vector2Double(double X, double Y) {
        this.x = X;
        this.y = Y;
    }

    public void plus(Vector2Double other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void substract(Vector2Double other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void divide(Vector2Double other) {
        this.x /= other.x;
        this.y /= other.y;
    }
}
