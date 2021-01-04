package com.yumin.go;

public class Stone {
    private String color;
    public static final Stone BLACK = new Stone("Black");
    public static final Stone WHITE = new Stone("White");
    public static final Stone EMPTY = new Stone("Empty");
    private Stone(String color) {
        this.color = color;
    }
    public String toString() {
        return color;
    }
}
