package io.deeplay;

import io.deeplay.core.model.Side;

// TODO: здесь автоматически определяется сторона по координатам фигуры(которая должна походить),
//  а так же просмотром в строковое представление, если с маленькой буквы - то чёрные
public class ChessBitboard {
    private Side mySide;
    private long myPawns;
    private long myKnights;
    private long myBishops;
    private long myRooks;
    private long myQueens;
    private long myKing;

    private long opponentPawns;
    private long opponentKnights;
    private long opponentBishops;
    private long opponentRooks;
    private long opponentQueens;
    private long opponentKing;

    // Получается из комбинации полей выше
    private long opponentPieces;
    private long myPieces;
    private long occupied;
    private long empty;

    // TODO: конструктор по нотации FEN


    public ChessBitboard(String fen) {

    }

    public Side getMySide() {
        return mySide;
    }

    public void setMySide(Side mySide) {
        this.mySide = mySide;
    }

    public long getMyPawns() {
        return myPawns;
    }

    public void setMyPawns(long myPawns) {
        this.myPawns = myPawns;
    }

    public long getMyKnights() {
        return myKnights;
    }

    public void setMyKnights(long myKnights) {
        this.myKnights = myKnights;
    }

    public long getMyBishops() {
        return myBishops;
    }

    public void setMyBishops(long myBishops) {
        this.myBishops = myBishops;
    }

    public long getMyRooks() {
        return myRooks;
    }

    public void setMyRooks(long myRooks) {
        this.myRooks = myRooks;
    }

    public long getMyQueens() {
        return myQueens;
    }

    public void setMyQueens(long myQueens) {
        this.myQueens = myQueens;
    }

    public long getMyKing() {
        return myKing;
    }

    public void setMyKing(long myKing) {
        this.myKing = myKing;
    }

    public long getOpponentPawns() {
        return opponentPawns;
    }

    public void setOpponentPawns(long opponentPawns) {
        this.opponentPawns = opponentPawns;
    }

    public long getOpponentKnights() {
        return opponentKnights;
    }

    public void setOpponentKnights(long opponentKnights) {
        this.opponentKnights = opponentKnights;
    }

    public long getOpponentBishops() {
        return opponentBishops;
    }

    public void setOpponentBishops(long opponentBishops) {
        this.opponentBishops = opponentBishops;
    }

    public long getOpponentRooks() {
        return opponentRooks;
    }

    public void setOpponentRooks(long opponentRooks) {
        this.opponentRooks = opponentRooks;
    }

    public long getOpponentQueens() {
        return opponentQueens;
    }

    public void setOpponentQueens(long opponentQueens) {
        this.opponentQueens = opponentQueens;
    }

    public long getOpponentKing() {
        return opponentKing;
    }

    public void setOpponentKing(long opponentKing) {
        this.opponentKing = opponentKing;
    }

    public long getOpponentPieces() {
        return opponentPieces;
    }

    public void setOpponentPieces(long opponentPieces) {
        this.opponentPieces = opponentPieces;
    }

    public long getMyPieces() {
        return myPieces;
    }

    public void setMyPieces(long myPieces) {
        this.myPieces = myPieces;
    }

    public long getOccupied() {
        return occupied;
    }

    public void setOccupied(long occupied) {
        this.occupied = occupied;
    }

    public long getEmpty() {
        return empty;
    }

    public void setEmpty(long empty) {
        this.empty = empty;
    }
}
