package com.yumin.go;

public interface IBoard {
    public int getDimensionX();
    public int getDimensionY();
    public void setStone(Stone stone, int x, int y);
    public void resetBoard();
    public Stone getStone(int x, int y);
    public IBoard deepCopy();
}
