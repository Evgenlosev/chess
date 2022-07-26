package io.deeplay.logic.parser;

import io.deeplay.core.model.Side;
import io.deeplay.logic.model.SideBitboards;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.deeplay.logic.logic.BitUtils.*;
import static java.util.Map.entry;

public class FENParser {
    private static final int BOARD_SIZE = 8; // 8 x 8
    // Маленькие буквы - фигуры черных, большие - белых
    private static final Set<Character> allPiecesCharacterRepresentation =
            Stream.of('p', 'n', 'b', 'r', 'q', 'k', 'P', 'N', 'B', 'R', 'Q', 'K')
                    .collect(Collectors.toUnmodifiableSet());
    private static final Map<Character, Long> fileCharToBitboardRepresentation =
            Map.ofEntries(
                    entry('a', MASK_FILE_A),
                    entry('b', MASK_FILE_B),
                    entry('c', MASK_FILE_C),
                    entry('d', MASK_FILE_D),
                    entry('e', MASK_FILE_E),
                    entry('f', MASK_FILE_F),
                    entry('g', MASK_FILE_G),
                    entry('h', MASK_FILE_H)
            );

    public static Map<Side, SideBitboards> parseFENToBitboards(final String fen) {
        final String parsePiecePlacementData = splitFEN(fen).get(0);
        // TODO: проверка на то что есть символы "/pnbrqkPNBRQK" + меньше макс. длины fen, логирование, исключения
        // TODO: До первого пробела извлекаем charAt и считаем '/' а так же количество свободных фигур
        //  если разделителей ('/') будет не 7 штук или sum(свободных клеток + занятых) != 8, то ошибка

        final int lastIndex = BOARD_SIZE * BOARD_SIZE - 1;
        // т.к. нужно знать индекс фигуры для битборда, нужно так же считать пустые клетки нотации FEN
        int backwardPrinting = BOARD_SIZE - 1;
        int rowCount = 0; // считаем начиная с верхней строки
        Map<Character, Long> piecesBitboard = new HashMap<>();
        for (char ch : allPiecesCharacterRepresentation) {
            piecesBitboard.putIfAbsent(ch, 0L);
        }

        for (String rank : parsePiecePlacementData.split("/")) {
            for (char currentChar : rank.toCharArray()) {
                if (Character.isDigit(currentChar)) {
                    backwardPrinting -= currentChar - '0'; // widening casting
                }
                if (allPiecesCharacterRepresentation.contains(currentChar)) {
                    piecesBitboard.put(currentChar,
                            piecesBitboard.get(currentChar) | (1L << (lastIndex - (rowCount * BOARD_SIZE + backwardPrinting))));
                }
                if (Character.isLetter(currentChar)) {
                    backwardPrinting--;
                }
            }
            backwardPrinting = BOARD_SIZE - 1;
            rowCount++;
        }
        return Map.ofEntries(
                entry(Side.WHITE, new SideBitboards(piecesBitboard.entrySet().stream()
                        .filter(x -> Character.isUpperCase(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), Side.WHITE)),
                entry(Side.BLACK, new SideBitboards(piecesBitboard.entrySet().stream()
                        .filter(x -> Character.isLowerCase(x.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)), Side.BLACK))
        );
    }

    public static long getEnPassant(final String fen) {
        final String parseEnPassantTargetSquare = splitFEN(fen).get(3);
        return fileCharToBitboardRepresentation.getOrDefault(parseEnPassantTargetSquare.charAt(0), 0L);
    }

    private static List<String> splitFEN(final String fen) {
        final List<String> parseFenNotation = List.of(fen.split(" "));
        if (parseFenNotation.size() != 6)
            throw new NullPointerException("Неверная, либо неполная нотация, количество элементов в нотации не равно 6");
        return parseFenNotation;
    }

}
