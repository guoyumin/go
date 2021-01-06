package com.yumin.go;

import java.util.Arrays;

public class Board implements IBoard{
    private int dimensionX = 0;
    private int dimensionY = 0;
    Stone[][] stones;
    @Override
    public int getDimensionX() {
        return dimensionX;
    }
    @Override
    public int getDimensionY() {
        return dimensionY;
    }
    
    public Board(int x, int y) {
        if (x<=0 || y<=0) {
            throw new IllegalArgumentException("Board dimension must be greater than zero.");
        }
        this.dimensionX = x;
        this.dimensionY = y;
        this.stones = new Stone[x][y];
        resetBoard();
    }
    @Override
    public void setStone(Stone stone, int x, int y) {
        checkInputRange(x,y);
        stones[x][y] = stone;
    }
    @Override
    public void resetBoard() {
        for (int i=0; i<dimensionX; i++) {
            for (int j=0; j<dimensionY; j++) {
                stones[i][j] = Stone.EMPTY;
            }
        }
    }
    @Override
    public Stone getStone(int x, int y) {
        checkInputRange(x,y);
        return stones[x][y];
    }
    
    private void checkInputRange (int x, int y) {
        if (x>=dimensionX || x<0 || y>=dimensionY || y<0) {
            throw new IllegalArgumentException("Cannot place a stone out of board range.");
        }
    }
    @Override
    public boolean equals (Object obj) {
        if (obj ==  null) {
            return false ;
        }
        if (obj.getClass() != Board.class) {
            return false;
        }
        Board newBoard = (Board) obj;
        if (newBoard.dimensionX != dimensionX || newBoard.dimensionY != dimensionY) {
            return false;
        }
        return Arrays.deepEquals(stones, newBoard.stones);
    }
    @Override
    public int hashCode() {
        return Arrays.hashCode(stones);
    }
    @Override
    public IBoard deepCopy() {
        Board newBoard = new Board(dimensionX, dimensionY);
        for (int i=0; i<dimensionX; i++) {
            newBoard.stones[i] = Arrays.copyOf(stones[i], stones[i].length);
        }
        return newBoard;
    }
}
