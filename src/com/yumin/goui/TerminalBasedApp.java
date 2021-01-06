package com.yumin.goui;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.yumin.go.*;
import com.yumin.go.GoRuleEngine;

public class TerminalBasedApp {
    private GoRuleEngine engine;
    
    Scanner in  = new Scanner(System.in);

    public void run() {
        System.out.print("Please input dimension of the board: ");
        if (in.hasNextInt()) {
            int dimension = in.nextInt();
            engine = new GoRuleEngine(new Board(dimension, dimension));
        }

        while (true) {
            drawBoard(engine.getBoard());
            System.out.print("Please place a stone (example: B 1A or W 2B): ");
            String input  = in.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            } else {
                if (isEligibleInput(input)) {
                    Move move = parseInput(input);
                    if (move  !=null) {
                        engine.placeStone(move.stone, move.x, move.y);
                    }
                } else {
                    System.out.println("I don't understand the input.");
                }
            }
        } 
    }
    private void drawBoard(IBoard board) {
        int dimensionX = board.getDimensionX();
        int dimensionY = board.getDimensionY();
        for (int y=0; y<dimensionY; y++) {
            for (int x=0;x<dimensionX; x++) {
                Stone stone  = board.getStone(x, y);
                if (stone ==  Stone.BLACK) {
                    System.out.print("B");
                } else if (stone == Stone.WHITE) {
                    System.out.print("W");
                } else if (stone == Stone.EMPTY) {
                    System.out.print(".");
                }
            }
            System.out.println();
        }
    }
    private boolean isEligibleInput(String input) {
        String pattern = "[B|W|b|w|E|e]\\s[\\d{1,2}][A-S|a-s]\\z";
        Pattern r  = Pattern.compile(pattern);
        Matcher m  = r.matcher(input);
        return m.matches();
    }
    private Move parseInput(String input) {
    
        Pattern pattern = Pattern.compile("\\A([B|W|b|w|E|e])\\s([\\d{1,2}])([A-S|a-s])\\z");
        Matcher m = pattern.matcher(input);
        if (!m.matches()) {
            return null;
        }
        String stoneInput = m.group(1);
        String xInput = m.group(3);
        String yInput = m.group(2);
        Stone stone;
        
        if (stoneInput.equalsIgnoreCase("B")) {
            stone = Stone.BLACK;
        } else if (stoneInput.equalsIgnoreCase("W")){
            stone = Stone.WHITE;
        } else if (stoneInput.equalsIgnoreCase("E")) {
            stone = Stone.EMPTY;
        } else {
            return null;
        }
        int y = Integer.parseInt(yInput) - 1;
        int x  = (int)xInput.toUpperCase().charAt(0) - (int) 'A';
        return new Move(stone, x, y);
    }
}
