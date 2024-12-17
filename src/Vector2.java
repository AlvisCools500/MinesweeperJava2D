
public class Vector2 {
    int x;
    int y;

    public Vector2(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    public void plus(Vector2 other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void substract(Vector2 other) {
        this.x -= other.x;
        this.y -= other.y;
    }
}

