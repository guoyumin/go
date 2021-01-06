package com.yumin.go;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class GoRuleEngine {
    private IBoard board;
    private class ReadOnlyBoard implements IBoard{
        private IBoard board;
        public ReadOnlyBoard (IBoard board) {
            this.board = board;
        }
        @Override
        public int getDimensionX() {
            return board.getDimensionX();
        }
        @Override
        public int getDimensionY() {
            return board.getDimensionY();
        }
        @Override
        public void setStone(Stone stone, int x, int y) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void resetBoard() {
            throw new UnsupportedOperationException();
        }
        @Override
        public Stone getStone(int x, int y) {
            return board.getStone(x, y);
        }
        @Override
        public IBoard deepCopy() {
            return board.deepCopy();
        }
    }
    
    
    public enum GameResult {BLACK, WHITE, DRAW};
    private LinkedList<Move> moveHistory;
    private LinkedList<IBoard> boardHistory;
    public GoRuleEngine(IBoard board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }
        this.board = board;
        this.moveHistory = new LinkedList<>();
        this.boardHistory = new LinkedList<>();
    }

    public Stone getStone(int x, int y) {
        return board.getStone(x, y);
    }
    public IBoard getBoard() {
        return new ReadOnlyBoard(board);
    }
    public boolean placeStone (Stone stone, int x, int y) {
        if (!isLegalMove(stone, x, y)) {
            return false;
        }
        board.setStone(stone, x, y);
        Move lastMove = new Move(stone, x, y);
        moveHistory.add(lastMove);
        reCalculateBoard (lastMove, this.board);
        boardHistory.add(board.deepCopy());
        return true;
    }
    
    private void reCalculateBoard (Move lastMove, IBoard curBoard) {
        Move[] neighburs = findNeighburs(lastMove, curBoard);
        Set<Set<Move>> adjacentBlocks = new HashSet<>();
        for (int i=0; i < neighburs.length; i++) {
            if (neighburs[i] != null && neighburs[i].stone != Stone.EMPTY && neighburs[i].stone != lastMove.stone) {
                boolean isCurrentStoneVisited = false;
                for (Set<Move> block : adjacentBlocks) {
                    if(block.contains(neighburs[i])) {
                        isCurrentStoneVisited = true;
                        break;
                    }
                }

                if (!isCurrentStoneVisited) {
                    Set<Move> stonesInBlock = new HashSet<>();
                    findAdjacentStones(neighburs[i], stonesInBlock, curBoard);
                    adjacentBlocks.add(stonesInBlock);
                }
            }
    
        }

        for (Set<Move> block : adjacentBlocks) {
            int liberties=findLiberty(block, curBoard);
            if (liberties == 0) {
                for (Move m : block) {
                    curBoard.setStone(Stone.EMPTY, m.x, m.y);
                }
            }    
        }
    }

    private Move[] findNeighburs (Move lastMove, IBoard curBoard) {
        ArrayList<Move> adjacentStones = new ArrayList<>();
        
        if (lastMove.x - 1 >=0) {
            adjacentStones.add(
                new Move(curBoard.getStone(lastMove.x-1, lastMove.y),lastMove.x-1, lastMove.y));
        }

        if (lastMove.x+1 < curBoard.getDimensionX()) {
            adjacentStones.add(
                new Move(curBoard.getStone(lastMove.x+1, lastMove.y), lastMove.x +1, lastMove.y));
        }
        if (lastMove.y - 1 >= 0 ) {
            adjacentStones.add(
                new Move(curBoard.getStone(lastMove.x, lastMove.y-1), lastMove.x, lastMove.y-1));
        }
        if (lastMove.y + 1 < curBoard.getDimensionY()) {
            adjacentStones.add(
                new Move(curBoard.getStone(lastMove.x, lastMove.y+1), lastMove.x, lastMove.y + 1));
        }
        return adjacentStones.toArray(new Move[0]);

    }
    private void findAdjacentStones(Move currentVisitingStone, Set<Move> visitedStones, IBoard curBoard) {
        if (visitedStones.contains(currentVisitingStone)) {
            return;
        }
        visitedStones.add(currentVisitingStone);
        Move [] adjacentStones = findNeighburs(currentVisitingStone, curBoard);
        ArrayList<Move> stonesToSearch = new ArrayList<>();
        for (Move m : adjacentStones) {
            if (m.stone == currentVisitingStone.stone && !visitedStones.contains(m)) {
                stonesToSearch.add(m);
            }
        }
        for (Move move : stonesToSearch) {
            findAdjacentStones(move, visitedStones, curBoard);
        }
    }
    private int findLiberty(Set<Move> stones, IBoard curBoard) {
        Set<Move> liberties = new HashSet<>();
        for (Move s : stones) {
            Move[] neighburs = findNeighburs(s, curBoard);
            for (Move neighbur : neighburs) {
                if (neighbur.stone == Stone.EMPTY) {
                    liberties.add(neighbur);
                }
            }
        }
        return liberties.size();
    }

    public boolean isLegalMove (Stone stone, int x, int y) {
        if (board.getStone(x, y) != Stone.EMPTY) {
            return false;
        }
        if (isDuplicatedAsHistory(new Move(stone, x, y))) {
            return false;
        }
        if (!hasNewMoveLiberty(new Move(stone, x, y))) {
            return false;
        }
        return true;
    }

    private boolean hasNewMoveLiberty(Move lastMove) {
        IBoard newBoard = board.deepCopy();
        newBoard.setStone(lastMove.stone, lastMove.x, lastMove.y);
        reCalculateBoard(lastMove, newBoard);
        Set<Move> moveSet = new HashSet<>();
        findAdjacentStones(lastMove, moveSet, newBoard);
        int liberties = findLiberty(moveSet, newBoard);
        if (liberties == 0) {
            return false;
        }
        return true;
    }

    private boolean isDuplicatedAsHistory(Move lastMove) {
        IBoard newBoard = board.deepCopy();
        newBoard.setStone(lastMove.stone, lastMove.x, lastMove.y);
        reCalculateBoard(lastMove, newBoard);
        Iterator<IBoard> iter = boardHistory.iterator();
        while (iter.hasNext()) {
            IBoard historyBoard = iter.next();
            if (newBoard.equals(historyBoard)) {
                return true;
            }
        }
        return false;
    }
    public boolean isEndGame() {
        throw new UnsupportedOperationException();
    }
    public GameResult getResult () {
        throw new UnsupportedOperationException();
    }
}
