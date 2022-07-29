package io.deeplay.core.model;

public class ChessBitboard {

    private final Side mySide;
    private final SideBitboards myBitboards;
    private final SideBitboards opponentBitboards;
    private long enPassantFile = 0L;

    // Получается из комбинации полей выше
    private long opponentPieces;
    private long myPieces;
    private long occupied;
    private long empty;


    public ChessBitboard(final SideBitboards myBitboards, final SideBitboards opponentBitboards) {
        this.myBitboards = myBitboards;
        this.mySide = myBitboards.getSide();
        this.opponentBitboards = opponentBitboards;
        setCommonBitboards();
    }

    private void setCommonBitboards() {
        this.opponentPieces = opponentBitboards.andAllBitboards();
        this.myPieces = myBitboards.andAllBitboards();
        this.occupied = opponentPieces | myPieces;
        this.empty = ~occupied;
    }

    public void setEnPassantFile(final long enPassantFile) {
        this.enPassantFile = enPassantFile;
    }

    public Side getMySide() {
        return mySide;
    }

    public SideBitboards getMyBitboards() {
        return myBitboards;
    }

    public SideBitboards getOpponentBitboards() {
        return opponentBitboards;
    }

    public long getEnPassantFile() {
        return enPassantFile;
    }

    public long getOpponentPieces() {
        return opponentPieces;
    }

    public long getMyPieces() {
        return myPieces;
    }

    public long getOccupied() {
        return occupied;
    }

    public long getEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "ChessBitboard{" +
                "mySide=" + mySide +
                ", myBitboards=" + myBitboards +
                ", opponentBitboards=" + opponentBitboards +
                ", enPassantFile=" + enPassantFile +
                ", opponentPieces=" + opponentPieces +
                ", myPieces=" + myPieces +
                ", occupied=" + occupied +
                ", empty=" + empty +
                '}';
    }
}
