package edu.asu.stratego.common.game.boardPieces;

import java.util.List;
import java.util.ArrayList;

public class PieceManager {
    private static List<PieceTypeInterface> allPieces = new ArrayList<>();
    static {
        allPieces.add(new Scout());
        allPieces.add(new Miner());
        allPieces.add(new Sergeant());
        allPieces.add(new Lieutenant());
        allPieces.add(new Captain());
        allPieces.add(new Major());
        allPieces.add(new Colonel());
        allPieces.add(new General());
        allPieces.add(new Marshal());
        allPieces.add(new Bomb());
        allPieces.add(new Spy());
        allPieces.add(new Flag());
    }
    public static List<PieceTypeInterface> getAllPieces() {
        return new ArrayList<>(allPieces);
    }
}

