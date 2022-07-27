package io.deeplay.logic.model;

import io.deeplay.core.model.Side;

import java.util.Map;

/**
 * Класс хранит в себе битборд-представление фигур одной стороны(белой/чёрной) доски шахмат.
 * Упрощает взаимодействие со сторонами игры.
 */
public class SideBitboards {
    private final Side side;
    private final long pawns;
    private final long knights;
    private final long bishops;
    private final long rooks;
    private final long queens;
    private final long king;

    public SideBitboards(final Map<Character, Long> piecesBitboard, final Side side) {
        this.side = side;
        String piecesCharacterRepresentation = "pnbrqk";
        if (side == Side.WHITE)
            piecesCharacterRepresentation = piecesCharacterRepresentation.toUpperCase();
        this.pawns = piecesBitboard.get(piecesCharacterRepresentation.charAt(0));
        this.knights = piecesBitboard.get(piecesCharacterRepresentation.charAt(1));
        this.bishops = piecesBitboard.get(piecesCharacterRepresentation.charAt(2));
        this.rooks = piecesBitboard.get(piecesCharacterRepresentation.charAt(3));
        this.queens = piecesBitboard.get(piecesCharacterRepresentation.charAt(4));
        this.king = piecesBitboard.get(piecesCharacterRepresentation.charAt(5));
    }

    public long andAllBitboards() {
        return pawns | knights | bishops | rooks | queens | king;
    }

    public long getPawns() {
        return pawns;
    }

    public long getKnights() {
        return knights;
    }

    public long getBishops() {
        return bishops;
    }

    public long getRooks() {
        return rooks;
    }

    public long getQueens() {
        return queens;
    }

    public long getKing() {
        return king;
    }

    public Side getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "SideBitboards{" +
                "pawns=" + pawns +
                ", knights=" + knights +
                ", bishops=" + bishops +
                ", rooks=" + rooks +
                ", queens=" + queens +
                ", king=" + king +
                ", side=" + side +
                '}';
    }
}
