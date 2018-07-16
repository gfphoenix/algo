package misc.board;

class Vector2 {
    int x;
    int y;

    Vector2(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Vector2() {
    }

    Vector2 set(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    Vector2 set(Vector2 data) {
        x = data.x;
        y = data.y;
        return this;
    }
    static Vector2 add(Vector2 result, Vector2 op1, Vector2 op2) {
        result.x = op1.x + op2.x;
        result.y = op1.y + op2.y;
        return result;
    }
    Vector2 add(Vector2 v) {
        x += v.x;
        y += v.y;
        return this;
    }

    Vector2 add(int dx, int dy) {
        x += dx;
        y += dy;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if ( o == null || !(o instanceof Vector2) ) return false;
        Vector2 v = (Vector2) o;
        return v.x == x && v.y == y;
    }

    public boolean equals(int x, int y) {
        return this.x == x && this.y == y;
    }

    @Override
    public String toString() {
        return "Vec2("+x+", "+y+")";
    }
}
