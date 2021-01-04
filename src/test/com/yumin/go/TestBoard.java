package test.com.yumin.go;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.yumin.go.*;

public class TestBoard {
    @Test
    public void evaluateBoard() {
        Board board = new Board(9,9);
        board.setStone(Stone.BLACK, 1, 1);
        assertEquals(Stone.BLACK, board.getStone(1, 1));
    }
    @Test(expected = IllegalArgumentException.class)
    public void testIllegalInputException() {
        Board board = new Board(9,9);
        board.setStone(Stone.BLACK, 9,9);
    }
    @Test
    public void testBoardEqual(){
        Board board = new Board(9, 9);
        Board equalBoard = new Board(9,9);
        Board nonEqualBoard = new Board(8,8);
        Board nonEqualBoard2 = new Board(9,9);
        assertTrue("bug in Board.equals method.", board.equals(equalBoard));
        assertFalse("bug in Board.equals method, it does not check the dimensions.", board.equals(nonEqualBoard));
        board.setStone(Stone.BLACK, 1, 1);
        equalBoard.setStone(Stone.BLACK, 1, 1);
        assertTrue("bug in Board.equals method. ", board.equals(equalBoard));
        nonEqualBoard2.setStone(Stone.WHITE, 1, 1);
        assertFalse("bug in Board.equals method", board.equals(nonEqualBoard2));
        nonEqualBoard2.setStone(Stone.BLACK, 1, 1);
        nonEqualBoard2.setStone(Stone.WHITE, 2, 2);
        assertFalse("bug in Board.equals method", board.equals(nonEqualBoard2));
    }
    @Test
    public void testDeepCopy () {
        Board board = new Board(9, 9);
        board.setStone(Stone.BLACK, 1, 1);
        Board newBoard = board.deepCopy();
        assertEquals(board.getStone(1, 1), newBoard.getStone(1,1));
        assertTrue("deep copied board does not equal to the original board.", board.equals(newBoard));
        newBoard.setStone(Stone.WHITE, 2, 2);
        assertFalse("deep copied object is the same object as the orignal one", board.equals(newBoard));
        assertEquals(board.getStone(2, 2), Stone.EMPTY);
    }
    
    
}
