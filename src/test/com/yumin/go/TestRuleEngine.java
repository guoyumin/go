package test.com.yumin.go;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import com.yumin.go.GoRuleEngine;
import com.yumin.go.*;

public class TestRuleEngine {

    private class Point {
        int x;
        int y;
        public Point(int x, int y) {
            this.x=x;
            this.y=y;
        }
        
    }
    private void placeStones(List<Point> points, GoRuleEngine engine, Stone stone) {
        for (Point point : points) {
            engine.placeStone(stone, point.x, point.y);
        }
    }
    private void assertStones(List<Point> points, GoRuleEngine engine, Stone stone) {
        for (Point point: points) {
            assertEquals(stone, engine.getStone(point.x, point.y));
        }
    }

    @Test
    public void testIsLegalMove() {
        GoRuleEngine engine = new GoRuleEngine(new Board(9,9));
        engine.placeStone(Stone.BLACK, 1, 1);
        assertFalse(engine.isLegalMove(Stone.WHITE, 1, 1));
        assertTrue(engine.isLegalMove(Stone.BLACK, 2, 2));
    }
    @Test
    public void testPlaceStone(){
        GoRuleEngine engine = new GoRuleEngine(new Board(9,9));
        engine.placeStone(Stone.BLACK, 1, 1);
        assertEquals(Stone.BLACK, engine.getStone(1, 1));
    }
    @Test
    public void testRecalculate(){
        GoRuleEngine engine = new GoRuleEngine(new Board(9,9));
        engine.placeStone(Stone.BLACK, 3, 3);
        engine.placeStone(Stone.BLACK, 4, 4);
        engine.placeStone(Stone.BLACK, 2, 4);
        engine.placeStone(Stone.WHITE, 3, 4);
        engine.placeStone(Stone.BLACK, 3, 5);
        assertEquals(engine.getStone(3, 4), Stone.EMPTY);
        assertEquals(engine.getStone(3, 3), Stone.BLACK);
        assertEquals(engine.getStone(2, 4), Stone.BLACK);
        assertEquals(engine.getStone(3, 5), Stone.BLACK);
        assertEquals(engine.getStone(4, 4), Stone.BLACK);

        //角部
        engine=new GoRuleEngine(new Board(9,9));
        engine.placeStone(Stone.BLACK, 0,1);
        engine.placeStone(Stone.WHITE, 0,0);
        engine.placeStone(Stone.BLACK, 1,0);
        assertEquals(engine.getStone(0, 0), Stone.EMPTY);
        assertEquals(engine.getStone(1, 0), Stone.BLACK);
        assertEquals(engine.getStone(0, 1), Stone.BLACK);

        //边
        engine = new GoRuleEngine(new Board(9,9));
        engine.placeStone(Stone.BLACK, 0,1);
        engine.placeStone(Stone.BLACK, 1,2);
        engine.placeStone(Stone.WHITE, 0,2);
        engine.placeStone(Stone.BLACK, 0,3);
        assertEquals(engine.getStone(0, 2), Stone.EMPTY);
        assertEquals(engine.getStone(1, 2), Stone.BLACK);
        assertEquals(engine.getStone(0, 1), Stone.BLACK);
        assertEquals(engine.getStone(0, 3), Stone.BLACK);

        //提多子
        engine = new GoRuleEngine(new Board(9,9));
        List<Point> blacks = new LinkedList<>();
        List<Point> whites = new LinkedList<>();
        blacks.add(new Point(2,2));
        blacks.add(new Point(3,2));
        blacks.add(new Point(1,3));
        blacks.add(new Point(1,4));
        blacks.add(new Point(2,5));
        blacks.add(new Point(3,5));
        blacks.add(new Point(4,3));

        whites.add(new Point(2,3));
        whites.add(new Point(3,3));
        whites.add(new Point(2,4));
        whites.add(new Point(3,4));

        placeStones(blacks, engine, Stone.BLACK);
        placeStones(whites, engine, Stone.WHITE);
        engine.placeStone(Stone.BLACK, 4, 4);

        assertStones(blacks, engine, Stone.BLACK);      
        assertStones(whites, engine, Stone.EMPTY);      

    }

    @Test
    public void testNoLibertyMove() {
        //测试最后一步本身无气但能提子
        GoRuleEngine engine = new GoRuleEngine(new Board(9,9));
        List<Point> blacks = new LinkedList<>();
        List<Point> whites = new LinkedList<>();

        blacks.add(new Point(1,1));
        blacks.add(new Point(2,1));
        blacks.add(new Point(3,1));
        blacks.add(new Point(4,1));
        blacks.add(new Point(5,1));
        blacks.add(new Point(1,2));
        blacks.add(new Point(5,2));
        blacks.add(new Point(1,3));
        blacks.add(new Point(5,3));
        blacks.add(new Point(1,4));
        blacks.add(new Point(5,4));
        blacks.add(new Point(1,5));
        blacks.add(new Point(2,5));
        blacks.add(new Point(3,5));
        blacks.add(new Point(4,5));
        blacks.add(new Point(5,5));

        whites.add(new Point(2,2));
        whites.add(new Point(3,2));
        whites.add(new Point(4,2));
        whites.add(new Point(2,3));
        whites.add(new Point(4,3));
        whites.add(new Point(2,4));
        whites.add(new Point(3,4));
        //whites.add(new Point(4,4));

        placeStones(blacks, engine, Stone.BLACK);
        placeStones(whites, engine, Stone.WHITE);
        assertTrue("Wrongly mark illegal move for no liberty", engine.isLegalMove(Stone.WHITE, 4, 4));
        engine.placeStone(Stone.WHITE, 4, 4);
        assertTrue("wrongly mark illegal move for no liberty", engine.placeStone(Stone.BLACK, 3, 3));

        assertStones(blacks, engine, Stone.BLACK);      
        assertStones(whites, engine, Stone.EMPTY);      
    }

    @Test
    public void testIllegalMoveForKo() {
        GoRuleEngine engine = new GoRuleEngine(new Board(9,9));
        List<Point> blacks = new LinkedList<>();
        List<Point> whites = new LinkedList<>();

        blacks.add(new Point(3,3));
        blacks.add(new Point(2,4));
        blacks.add(new Point(3,5));
        blacks.add(new Point(4,4));
        whites.add(new Point(4,3));
        whites.add(new Point(4,5));
        whites.add(new Point(5,4));
        placeStones(blacks, engine, Stone.BLACK);
        placeStones(whites, engine, Stone.WHITE);
        engine.placeStone(Stone.WHITE, 3, 4);
        assertEquals(Stone.EMPTY, engine.getStone(4, 4));
        assertFalse("Did not detect illegal move for ko", engine.isLegalMove(Stone.BLACK, 4, 4));
        assertEquals(Stone.EMPTY, engine.getStone(4, 4));
    }

    @Test
    public void testIllegalMoveForNoLiberty() {
        GoRuleEngine engine = new GoRuleEngine(new Board(9,9));
        List<Point> blacks = new LinkedList<>();
        List<Point> whites = new LinkedList<>();
        blacks.add(new Point(3,3));
        blacks.add(new Point(2,4));
        blacks.add(new Point(3,5));
        blacks.add(new Point(4,4));
        placeStones(blacks, engine, Stone.BLACK);
        assertFalse("Did not detect illegal move for no liberty", engine.isLegalMove(Stone.WHITE, 3, 4));
    }
}
