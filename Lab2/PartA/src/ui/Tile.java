package ui;

public class Tile {
    private final int x;
    private final int y;
    private boolean isVisited;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisited() {
        return isVisited;
    }

    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }

    @Override
    public boolean equals(Object o) {
        if(o == null) { return false; }
        if(o.getClass() != this.getClass()) { return false; }

        final Tile that = (Tile) o;
        return (this.x == that.x && this.y == that.y);
    }

    @Override
    public String toString() {
        return (this.x + " " + this.y);
    }
}
