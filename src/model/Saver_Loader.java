package model;

import java.awt.Point;
import java.util.Base64;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

class Saver_Loader {

    private static Preferences p = Preferences.userRoot().node("2048");

    protected static void saveGame(String str) {
	String encodedStr1 = Base64.getEncoder().encodeToString(str.getBytes());
	String encodedStr2 = encodedStr1.concat("\n" + str.hashCode());
	try {
	    p.clear();
	} catch (BackingStoreException e) {
	    e.printStackTrace();
	}
	if (encodedStr2.length() > 8190) return;
	p.put("v1game.sav", encodedStr2);
    }

    protected static String loadGame() {
	String encodedStr1 = p.get("v1game.sav", "");
	try {
	    p.clear();
	} catch (BackingStoreException e) {
	    e.printStackTrace();
	}
	if (encodedStr1 == null || encodedStr1.equals("")) return null;
	int index = encodedStr1.lastIndexOf("\n");
	if (index == -1 || index == 0) return null;
	String encodedStr2 = encodedStr1.substring(0, index);
	int hashValue = Integer.valueOf(encodedStr1.substring(index + 1));
	String decodedStr = new String(Base64.getDecoder().decode(encodedStr2));
	if (hashValue == decodedStr.hashCode()) return decodedStr;
	return null;
    }

    public static void main(String[] args) {
	Board board = new Board();
	board.setSize(4, 4);
	board.setCell(new Point(1, 1), 4);
	board.setCell(new Point(2, 2), 1024);
	board.setCell(new Point(1, 3), 32);
	board.setCell(new Point(3, 0), 2);
	board.setScore(10546);
	board.setHighScore(5461321);
	System.out.println(board);
	saveGame(board.toString());
	Board board2 = new Board();
	board2.stringToBoard(loadGame());
	System.out.println();
	System.out.println(board2);
    }
}