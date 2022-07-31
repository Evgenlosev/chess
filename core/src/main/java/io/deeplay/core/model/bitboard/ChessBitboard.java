package io.deeplay.core.model.bitboard;

import io.deeplay.core.model.Side;

/**
 * Класс хранит обобщенную информацию о шахматной доске.
 * Так же отвечает на вопрос "Относительно кого считаем допустимые ходы".
 */
public class ChessBitboard {

    private final Side processingSide;
    private final SideBitboards processingSideBitboards;
    private final SideBitboards opponentSideBitboards;
    private long enPassantFile;

    // Получается из комбинации полей выше
    private long opponentPieces;
    private long processingSidePieces;
    private long occupied;
    private long empty;
    private CheckData processingSideCheckData;

    /**
     * Не коммутативный конструктор.
     * Поменяв порядок параметров местами, логика будет работать относительной другой стороны.
     *
     * @param processingSideBitboards обрабатываемая сторона.
     * @param opponentSideBitboards   сторона, относительно которой будет производиться расчёт допустимых ходов.
     */
    public ChessBitboard(final SideBitboards processingSideBitboards, final SideBitboards opponentSideBitboards) {
        this.processingSideBitboards = processingSideBitboards;
        this.processingSide = processingSideBitboards.getSide();
        this.opponentSideBitboards = opponentSideBitboards;
        this.enPassantFile = 0L;
        setCommonBitboards();
    }

    private void setCommonBitboards() {
        this.opponentPieces = opponentSideBitboards.orOperationOnAllBitboards();
        this.processingSidePieces = processingSideBitboards.orOperationOnAllBitboards();
        this.occupied = opponentPieces | processingSidePieces;
        this.empty = ~occupied;
    }

    public Side getProcessingSide() {
        return processingSide;
    }

    public void setEnPassantFile(final long enPassantFile) {
        this.enPassantFile = enPassantFile;
    }

    public SideBitboards getProcessingSideBitboards() {
        return processingSideBitboards;
    }

    public SideBitboards getOpponentSideBitboards() {
        return opponentSideBitboards;
    }

    public long getProcessingSidePieces() {
        return processingSidePieces;
    }

    public long getEnPassantFile() {
        return enPassantFile;
    }

    public long getOpponentPieces() {
        return opponentPieces;
    }

    public CheckData getProcessingSideCheckData() {
        return processingSideCheckData;
    }

    public long getOccupied() {
        return occupied;
    }

    public long getEmpty() {
        return empty;
    }

    public void setProcessingSideCheckData(final CheckData processingSideCheckData) {
        this.processingSideCheckData = processingSideCheckData;
    }

    @Override
    public String toString() {
        return "ChessBitboard{" +
                "processingSide=" + processingSide +
                ", processingSideBitboards=" + processingSideBitboards +
                ", opponentBitboards=" + opponentSideBitboards +
                ", enPassantFile=" + enPassantFile +
                ", opponentPieces=" + opponentPieces +
                ", processingSidePieces=" + processingSidePieces +
                ", occupied=" + occupied +
                ", empty=" + empty +
                ", processingSideCheckData=" + processingSideCheckData +
                '}';
    }
}
