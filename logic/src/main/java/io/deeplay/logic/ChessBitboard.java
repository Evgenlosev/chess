package io.deeplay.logic;

import io.deeplay.core.model.Side;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ChessBitboard {
    private static final int BOARD_WIDTH = 8;
    private static final int BOARD_HEIGHT = 8;
    // Маленькие буквы - фигуры черных, большие - белых
    private static final Set<Character> allPiecesCharacterRepresentation =
            Stream.of('p', 'n', 'b', 'r', 'q', 'k', 'P', 'N', 'B', 'R', 'Q', 'K')
                    .collect(Collectors.toUnmodifiableSet());

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

    // From нужен, чтобы определить сторону, т.к. она не передается
    public ChessBitboard(final String fenNotation, int from) {
        Map<Character, Long> piecesBitboard = new HashMap<>();
        String parseBoard = fenNotation.trim();

        boolean sideNotDetermined = true;
        final int lastIndex = 63;
        // счёт для обращения к строке
        int count = 0;
        // т.к. нужно знать индекс фигуры для битборда, нужно так же считать пустые клетки нотации FEN
        int countCharactersAndSkips = lastIndex - BOARD_WIDTH;
        int backwardPrinting = BOARD_WIDTH - 1;
        int rowCount = 0; // считаем начиная с верхней строки
        char currentChar = parseBoard.charAt(count);
        for (char ch : allPiecesCharacterRepresentation) {
            piecesBitboard.putIfAbsent(ch, 0L);
        }

        int skip;
        while (currentChar != ' ') {
            if (Character.isDigit(currentChar)) {
                skip = currentChar - '0'; // widening casting
                countCharactersAndSkips += skip;
                backwardPrinting -= skip;
            }
            if (allPiecesCharacterRepresentation.contains(currentChar)) {
                piecesBitboard.put(currentChar,
                        piecesBitboard.get(currentChar) | (1L << (lastIndex - (rowCount * BOARD_WIDTH + backwardPrinting))));
            }
            if (Character.isLetter(currentChar) && countCharactersAndSkips == from) {
                this.mySide = Character.isLowerCase(currentChar) ? Side.BLACK : Side.WHITE;
                sideNotDetermined = false;
            }
            count++;
            if (Character.isLetter(currentChar)) {
                backwardPrinting--;
                countCharactersAndSkips++;
            }
            if (currentChar == '/') {
                backwardPrinting = BOARD_WIDTH - 1;
                rowCount++;
                countCharactersAndSkips = lastIndex - rowCount * BOARD_WIDTH - 7;
            }
            currentChar = parseBoard.charAt(count);
        }
        if (sideNotDetermined) {
            // Если сторона не определена, то есть возможность, что дали не верную позицию фигуры
            throw new NullPointerException("Side is not determined!");
            // TODO: До первого пробела извлекаем charAt и считаем '/' а так же количество свободных фигур
            //  если разделителей ('/') будет не 7 штук или sum(свободных клеток + занятых) != 8, то ошибка
            // currentChar > 8
            // исключение, логирование
        }
        // TODO: проверка то что символ в условии совпадения from совпадает с символом в мапе
        //  (берем символ, из мапы, на позиции from и смотрим на бит, если нету, то ошибка)

        // TODO: разжевать оставшуюся часть FEN (в цикле до, нужно сохранить индекс пробела)

        if (Side.otherSide(mySide) == Side.BLACK)
            setOpponentsBitboards(piecesBitboard.entrySet().stream().filter(x -> Character.isLowerCase(x.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        if (Side.otherSide(mySide) == Side.WHITE)
            setOpponentsBitboards(piecesBitboard.entrySet().stream().filter(x -> Character.isUpperCase(x.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));

        if (mySide == Side.BLACK)
            setMyBitboards(piecesBitboard.entrySet().stream().filter(x -> Character.isLowerCase(x.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        if (mySide == Side.WHITE)
            setMyBitboards(piecesBitboard.entrySet().stream().filter(x -> Character.isUpperCase(x.getKey()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
        setCommonBitboards();
    }

    private void setOpponentsBitboards(Map<Character, Long> piecesBitboard) {
        String piecesCharacterRepresentation = "pnbrqk";
        if (Side.otherSide(mySide) == Side.WHITE)
            piecesCharacterRepresentation = piecesCharacterRepresentation.toUpperCase();
        this.opponentPawns = piecesBitboard.get(piecesCharacterRepresentation.charAt(0));
        this.opponentKnights = piecesBitboard.get(piecesCharacterRepresentation.charAt(1));
        this.opponentBishops = piecesBitboard.get(piecesCharacterRepresentation.charAt(2));
        this.opponentRooks = piecesBitboard.get(piecesCharacterRepresentation.charAt(3));
        this.opponentQueens = piecesBitboard.get(piecesCharacterRepresentation.charAt(4));
        this.opponentKing = piecesBitboard.get(piecesCharacterRepresentation.charAt(5));
    }

    private void setMyBitboards(Map<Character, Long> piecesBitboard) {
        String piecesCharacterRepresentation = "pnbrqk";
        if (mySide == Side.WHITE)
            piecesCharacterRepresentation = piecesCharacterRepresentation.toUpperCase();
        this.myPawns = piecesBitboard.get(piecesCharacterRepresentation.charAt(0));
        this.myKnights = piecesBitboard.get(piecesCharacterRepresentation.charAt(1));
        this.myBishops = piecesBitboard.get(piecesCharacterRepresentation.charAt(2));
        this.myRooks = piecesBitboard.get(piecesCharacterRepresentation.charAt(3));
        this.myQueens = piecesBitboard.get(piecesCharacterRepresentation.charAt(4));
        this.myKing = piecesBitboard.get(piecesCharacterRepresentation.charAt(5));
    }

    private void setCommonBitboards() {
        this.opponentPieces = opponentPawns | opponentKnights | opponentBishops | opponentRooks | opponentQueens
                | opponentKing;
        this.myPieces = myPawns | myKnights | myBishops | myRooks | myQueens | myKing;
        this.occupied = opponentPieces | myPieces;
        this.empty = ~occupied;
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

    @Override
    public String toString() {
        return "ChessBitboard{" +
                "mySide=" + mySide +
                ", myPawns=" + myPawns +
                ", myKnights=" + myKnights +
                ", myBishops=" + myBishops +
                ", myRooks=" + myRooks +
                ", myQueens=" + myQueens +
                ", myKing=" + myKing +
                ", opponentPawns=" + opponentPawns +
                ", opponentKnights=" + opponentKnights +
                ", opponentBishops=" + opponentBishops +
                ", opponentRooks=" + opponentRooks +
                ", opponentQueens=" + opponentQueens +
                ", opponentKing=" + opponentKing +
                ", opponentPieces=" + opponentPieces +
                ", myPieces=" + myPieces +
                ", occupied=" + occupied +
                ", empty=" + empty +
                '}';
    }
}
