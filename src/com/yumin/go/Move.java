package com.yumin.go;

public class Move {

    public int x;
    public int y;
    public Stone stone;
    public Move(Stone stone, int x, int y) {
        this.stone = stone;
        this.x = x;
        this.y = y;
    }
    public boolean equals (Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != Move.class) {
            return false;
        }
        Move m = (Move) obj;
        return m.stone == stone && m.x == x && m.y==y;
    }
    public int hashCode() {
        return x + y + stone.hashCode();
    }
    public String toString() {
        return stone.toString() + "," + Integer.toString(x) + "," + Integer.toString(y);
    }  
}
