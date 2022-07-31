package io.deeplay.core.model.bitboard;

import io.deeplay.core.logic.BitUtils;
import io.deeplay.core.logic.newlogic.SimpleBitboardHandler;
import io.deeplay.core.model.Figure;
import io.deeplay.core.model.Side;

import java.util.ArrayList;
import java.util.List;
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

    private final List<PieceBitboard> pieceBitboards;
    private PieceBitboard kingPieceBitboards;

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
        pieceBitboards = new ArrayList<>();
    }

    public void initializePieceBitboards(final ChessBitboard chessBitboard) { // TODO: вставить в каждый соответствующие методы обертки
        int pieceIndex;
        for (long pawn : BitUtils.segregatePositions(pawns)) {
            pieceIndex = Long.numberOfTrailingZeros(pawn);
            pieceBitboards.add(new PieceBitboard(chessBitboard.getOccupied(), side,
                    side == Side.WHITE ? Figure.W_PAWN : Figure.B_PAWN, pieceIndex, pawn,
                    SimpleBitboardHandler.getPawnFunction(side))); // У каждой фигуры своя логика обработки, передаем её
        }
        for (long knight : BitUtils.segregatePositions(knights)) {
            pieceIndex = Long.numberOfTrailingZeros(knight);
            pieceBitboards.add(new PieceBitboard(chessBitboard.getOccupied(), side,
                    side == Side.WHITE ? Figure.W_KNIGHT : Figure.B_KNIGHT, pieceIndex, knight,
                    SimpleBitboardHandler.getKnightMovesBitboard));
        }
        for (long bishop : BitUtils.segregatePositions(bishops)) {
            pieceIndex = Long.numberOfTrailingZeros(bishop);
            pieceBitboards.add(new PieceBitboard(chessBitboard.getOccupied(), side,
                    side == Side.WHITE ? Figure.W_BISHOP : Figure.B_BISHOP, pieceIndex, bishop,
                    SimpleBitboardHandler.getBishopMovesBitboard));
        }
        for (long rook : BitUtils.segregatePositions(rooks)) {
            pieceIndex = Long.numberOfTrailingZeros(rook);
            pieceBitboards.add(new PieceBitboard(chessBitboard.getOccupied(), side,
                    side == Side.WHITE ? Figure.W_ROOK : Figure.B_ROOK, pieceIndex, rook,
                    SimpleBitboardHandler.getRookMovesBitboard));
        }
        for (long queen : BitUtils.segregatePositions(queens)) {
            pieceIndex = Long.numberOfTrailingZeros(queen);
            pieceBitboards.add(new PieceBitboard(chessBitboard.getOccupied(), side,
                    side == Side.WHITE ? Figure.W_QUEEN : Figure.B_QUEEN, pieceIndex, queen,
                    SimpleBitboardHandler.getQueenMovesBitboard));
        }
        if (king == 0L)
            throw new IllegalArgumentException("У стороны " + side + " - не обнаружена фигура короля");
        pieceIndex = Long.numberOfTrailingZeros(king);
        this.kingPieceBitboards = new PieceBitboard(chessBitboard.getOccupied(), side,
                side == Side.WHITE ? Figure.W_KING : Figure.B_KING, pieceIndex, king,
                SimpleBitboardHandler.getKingMovesBitboard);
        // Короля в список всех фигур не сохраняем, т.к. вычисления по всем фигурам будут происходить относительно него
        for (PieceBitboard pieceBitboard : pieceBitboards)
            pieceBitboard.initializeMoves(chessBitboard);
        kingPieceBitboards.initializeMoves(chessBitboard);
    }


    public long orOperationOnAllBitboards() {
        return pawns | knights | bishops | rooks | queens | king; // occupied (занятые клетки)
    }

    public List<PieceBitboard> getPieceBitboards() {
        return pieceBitboards;
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

    public PieceBitboard getKingPieceBitboards() {
        return kingPieceBitboards;
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
