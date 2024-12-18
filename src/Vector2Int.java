
public class Vector2Int {
    int x;
    int y;

    public Vector2Int(int X, int Y) {
        this.x = X;
        this.y = Y;
    }

    public void plus(Vector2Int other) {
        this.x += other.x;
        this.y += other.y;
    }

    public void substract(Vector2Int other) {
        this.x -= other.x;
        this.y -= other.y;
    }

    public void divide(Vector2Int other) {
        this.x /= other.x;
        this.y /= other.y;
    }
}

