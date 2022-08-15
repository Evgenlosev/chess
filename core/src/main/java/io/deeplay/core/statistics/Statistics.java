package io.deeplay.core.statistics;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.SelfPlay;
import io.deeplay.core.listener.ChessAdapter;
import io.deeplay.core.model.*;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XYChart;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

// TODO: getName у Player (для EvaluationBot - названия оценочной функции, для ботов которые строят дерево игры еще и глубина)

/**
 * Класс собирает статистику об игре двух игроков(через SelfPlay), в рамках партий игр ( >=1 сыгранных игр).
 * А конкретно:
 * Отдельно о каждом игроке собирается информация о том как быстро он совершал ходы(в среднем).
 * Какие комбинации фигур оставались в конце у каждой стороны.
 * Какие виды ходов используются чаще всего.
 */
public class Statistics extends ChessAdapter {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(Statistics.class);
    private final File outputDirectory;
    private final SelfPlay selfPlay;
    private final String firstPlayerName;
    private final String secondPlayerName;
    private final int gamesAmount;
    private final List<GameStatistics> gamesStatistics;
    private int countGames;
    private int currentGameStatistics;

    /**
     * Для того чтобы замерять время потраченное на ход.
     */
    private long previousTimeMark;

    public Statistics(final SelfPlay selfPlay) {
        this.selfPlay = selfPlay;
        this.firstPlayerName =
                selfPlay.getFirstPlayer().getSide().toString() + "-" + selfPlay.getFirstPlayer().getName();
        this.secondPlayerName =
                selfPlay.getSecondPlayer().getSide().toString() + "-" + selfPlay.getSecondPlayer().getName();
        // Создается папка с шаблоном
        // {DD.MM.YYYY;HH-MM-w-PlayerName.getName()vs.b:PlayerName.getName(),Ngamesplayed}
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy;HH-mm");
        String dataAndTime = sdf.format(calendar.getTime());
        final String directoryName = dataAndTime + "-"
                + firstPlayerName + "vs." + secondPlayerName + ","
                + selfPlay.getGamesAmount() + "gamesPlayed";
        this.outputDirectory = new File("out/" + directoryName);
        outputDirectory.mkdir();
        this.gamesAmount = selfPlay.getGamesAmount();
        this.countGames = 0;
        this.gamesStatistics = new ArrayList<>();
        this.currentGameStatistics = -1;
        this.previousTimeMark = System.currentTimeMillis();
    }

    public static <N extends Number, T extends String> void drawLineChartWithStringOnXAxis(
            final Map<String, List<T>> xLineNameElements,
            final Map<String, List<N>> yLineNameElements,
            final String xAxisTitle,
            final String yAxisTitle,
            final String outputPath,
            final String chartTitle) throws IOException {
        CategoryChart chart = new CategoryChart(1300, 800);
        chart.setTitle(chartTitle);
        chart.setXAxisTitle(xAxisTitle);
        chart.setYAxisTitle(yAxisTitle);
        for (String lineName : xLineNameElements.keySet()) {
            chart.addSeries(lineName, xLineNameElements.get(lineName), yLineNameElements.get(lineName));
        }

        BitmapEncoder.saveBitmap(chart, outputPath + "/" + chartTitle, BitmapEncoder.BitmapFormat.JPG);
    }

    public static <T, N extends Number> void drawLineChart(final Map<String, List<T>> xLineNameElements,
                                                           final Map<String, List<N>> yLineNameElements,
                                                           final String xAxisTitle,
                                                           final String yAxisTitle,
                                                           final String outputPath,
                                                           final String chartTitle) throws IOException {
        XYChart chart = new XYChart(900, 500);
        chart.setTitle(chartTitle);
        chart.setXAxisTitle(xAxisTitle);
        chart.setYAxisTitle(yAxisTitle);
        for (String lineName : xLineNameElements.keySet()) {
            chart.addSeries(lineName, xLineNameElements.get(lineName), yLineNameElements.get(lineName));
        }

        BitmapEncoder.saveBitmap(chart, outputPath + "/" + chartTitle, BitmapEncoder.BitmapFormat.JPG);
    }

    private void saveStatistics() {
        writeInFileAverageStatistics();
        writeInFileLeftPiecesSets();
        lineChartOfSpecialMovesOnAverage();
    }

    private void writeInFileAverageStatistics() {
        File averageStatistics = new File(outputDirectory.getPath() + "/averageStatistics.txt");
        // т.к. в SelfPLay первый игрок всегда играет за белые фигуры, пропускаем процесс опознавания стороны
        final long firstPlayerWins =
                gamesStatistics.stream().filter(stats -> stats.getGameStatus() == GameStatus.WHITE_WON).count();
        final long secondPlayerWins =
                gamesStatistics.stream().filter(stats -> stats.getGameStatus() == GameStatus.BLACK_WON).count();
        final long draws =
                gamesStatistics.stream().filter(stats -> stats.getGameStatus() == GameStatus.STALEMATE
                        || stats.getGameStatus() == GameStatus.FIFTY_MOVES_RULE
                        || stats.getGameStatus() == GameStatus.THREEFOLD_REPETITION
                        || stats.getGameStatus() == GameStatus.INSUFFICIENT_MATING_MATERIAL).count();
        final float firstPlayerLongestTimeOnMove =
                gamesStatistics.stream().map(GameStatistics::getFirstPlayerMovesTime)
                        .flatMap(List::stream).max(Float::compare).orElseThrow();
        final float secondPlayerLongestTimeOnMove =
                gamesStatistics.stream().map(GameStatistics::getSecondPlayerMovesTime)
                        .flatMap(List::stream).max(Float::compare).orElseThrow();
        final float firstPlayerShortestTimeOnMove =
                gamesStatistics.stream().map(GameStatistics::getFirstPlayerMovesTime)
                        .flatMap(List::stream).min(Float::compare).orElseThrow();
        final float secondPlayerShortestTimeOnMove =
                gamesStatistics.stream().map(GameStatistics::getSecondPlayerMovesTime)
                        .flatMap(List::stream).min(Float::compare).orElseThrow();
        final double firstPlayerAverageTimeOnMove =
                gamesStatistics.stream().map(GameStatistics::getFirstPlayerMovesTime)
                        .flatMapToDouble(x -> x.stream().mapToDouble(time -> time)).average().orElseThrow();
        final double secondPlayerAverageTimeOnMove =
                gamesStatistics.stream().map(GameStatistics::getSecondPlayerMovesTime)
                        .flatMapToDouble(x -> x.stream().mapToDouble(time -> time)).average().orElseThrow();
        try {
            averageStatistics.createNewFile();
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(averageStatistics), StandardCharsets.UTF_8))) {
                writer.write("Games played: " + gamesAmount + '\n');
                writer.write("Game ends on average after " +
                        gamesStatistics.stream().mapToInt(GameStatistics::getCountTurns).average().orElseThrow()
                        + " turns" + '\n');
                writer.write('\n');
                writer.write(firstPlayerName + ": won " + firstPlayerWins + " times" + '\n');
                writer.write("Longest time on move: " + String.format("%.3f", firstPlayerLongestTimeOnMove)
                        + "; Shortest time on move: " + String.format("%.3f", firstPlayerShortestTimeOnMove) + '\n');
                writer.write("Average time on move: " + String.format("%.3f", firstPlayerAverageTimeOnMove) + '\n');
                writer.write('\n');
                writer.write(secondPlayerName + ": won " + secondPlayerWins + " times" + '\n');
                writer.write("Longest time on move: " + String.format("%.3f", secondPlayerLongestTimeOnMove)
                        + "; Shortest time on move: " + String.format("%.3f", secondPlayerShortestTimeOnMove) + '\n');
                writer.write("Average time on move: " + String.format("%.3f", secondPlayerAverageTimeOnMove) + '\n');
                writer.write('\n');
                writer.write("Draw " + draws + " times.");
            }
        } catch (IOException e) {
            LOGGER.error("Ошибка при выводе в файл: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeInFileLeftPiecesSets() {
        File statistics = new File(outputDirectory.getPath() + "/leftPiecesSetsStatistics.txt");
        Map<List<Figure>, Integer> setPiecesFrequency = new HashMap<>();
        for (GameStatistics gameStatistics : gamesStatistics) {
            final List<Figure> leftPiecesAfterGame = gameStatistics.getPiecesLeftAfterGameEnd();
            setPiecesFrequency.putIfAbsent(leftPiecesAfterGame, 0);
            setPiecesFrequency.put(leftPiecesAfterGame, setPiecesFrequency.get(leftPiecesAfterGame) + 1);
        }
        List<List<Figure>> topSetPiecesAfterEndGame =
                setPiecesFrequency.entrySet().stream().sorted((e1, e2) -> e2.getValue() - e1.getValue())
                        .map(Map.Entry::getKey).collect(Collectors.toList());
        try {
            statistics.createNewFile();
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(statistics), StandardCharsets.UTF_8))) {
                writer.write("Games played: " + gamesAmount + '\n');
                writer.write("Most common sets of pieces after end: " + '\n');
                for (int i = 0; i < topSetPiecesAfterEndGame.size(); i++) {
                    writer.write("№" + (i + 1) + ": ");
                    final StringBuilder setOfPieces = new StringBuilder();
                    for (Figure figure : topSetPiecesAfterEndGame.get(i)) {
                        setOfPieces.append(MapsStorage.FIGURE_TO_SYMBOL.get(figure));
                    }
                    char[] tempArray = setOfPieces.toString().toCharArray();
                    Arrays.sort(tempArray);
                    writer.write(new String(tempArray));
                    writer.write('\n');
                }
                writer.write('\n');
            }
        } catch (IOException e) {
            LOGGER.error("Ошибка при выводе в файл: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void lineChartOfSpecialMovesOnAverage() {
        int countFirstPLayersTotalMovesInAllGames = 0;
        int countSecondPLayersTotalMovesInAllGames = 0;
        Map<MoveType, Integer> firstPlayerCountMoveType = new EnumMap<>(MoveType.class);
        Map<MoveType, Integer> secondPlayerCountMoveType = new EnumMap<>(MoveType.class);
        for (GameStatistics gameStatistics : gamesStatistics) {
            for (MoveType moveType : gameStatistics.getCountFirstPlayerUsedMoves().keySet()) {
                firstPlayerCountMoveType.putIfAbsent(moveType, 0);
                firstPlayerCountMoveType.put(moveType,
                        firstPlayerCountMoveType.get(moveType) +
                                gameStatistics.getCountFirstPlayerUsedMoves().get(moveType));
                countFirstPLayersTotalMovesInAllGames += gameStatistics.getCountFirstPlayerUsedMoves().get(moveType);
            }
            for (MoveType moveType : gameStatistics.getCountSecondPlayerUsedMoves().keySet()) {
                secondPlayerCountMoveType.putIfAbsent(moveType, 0);
                secondPlayerCountMoveType.put(moveType,
                        secondPlayerCountMoveType.get(moveType) +
                                gameStatistics.getCountSecondPlayerUsedMoves().get(moveType));
                countSecondPLayersTotalMovesInAllGames += gameStatistics.getCountSecondPlayerUsedMoves().get(moveType);
            }
        }
        Map<String, Double> firstPlayerAverageMoveType = new HashMap<>();
        Map<String, Double> secondPlayerAverageMoveType = new HashMap<>();
        for (MoveType moveType : firstPlayerCountMoveType.keySet()) {
            firstPlayerAverageMoveType
                    .put(moveType.toString(),
                            firstPlayerCountMoveType.get(moveType) / (countFirstPLayersTotalMovesInAllGames + 0.0));
        }
        for (MoveType moveType : secondPlayerCountMoveType.keySet()) {
            secondPlayerAverageMoveType
                    .put(moveType.toString(),
                            secondPlayerCountMoveType.get(moveType) / (countSecondPLayersTotalMovesInAllGames + 0.0));
        }
        Map<String, List<String>> firstPlayerXLineNameElements = new HashMap<>();
        Map<String, List<Double>> firstPlayerYLineNameElements = new HashMap<>();
        firstPlayerXLineNameElements.put(firstPlayerName, new ArrayList<>(firstPlayerAverageMoveType.keySet()));
        firstPlayerYLineNameElements.put(firstPlayerName, new ArrayList<>(firstPlayerAverageMoveType.values()));

        Map<String, List<String>> secondPlayerXLineNameElements = new HashMap<>();
        Map<String, List<Double>> secondPlayerYLineNameElements = new HashMap<>();
        secondPlayerXLineNameElements.put(secondPlayerName, new ArrayList<>(secondPlayerAverageMoveType.keySet()));
        secondPlayerYLineNameElements.put(secondPlayerName, new ArrayList<>(secondPlayerAverageMoveType.values()));

        try {
            drawLineChartWithStringOnXAxis(firstPlayerXLineNameElements, firstPlayerYLineNameElements,
                    "moveTypes", "usageFrequency",
                    outputDirectory.getPath(), "First-player-move-type-usage-frequency");
            drawLineChartWithStringOnXAxis(secondPlayerXLineNameElements, secondPlayerYLineNameElements,
                    "moveTypes", "usageFrequency",
                    outputDirectory.getPath(), "Second-player-move-type-usage-frequency");
        } catch (IOException e) {
            LOGGER.error("Ошибка при выводе в файл: {}", e.getMessage());
            e.printStackTrace();
        }
    }

    private void lineChartOfPieceTypeFrequencyByGamePhase() {
        // С какой частотой(усредненной) ходили определенными типами фигур в НАЧАЛЕ игры(y - проценты для типа фигуры, x - этапы (ВСЕХ)игр)
        //  (начало - первые 10 ходов) (мидшпиль - оставшиеся) (эндшпиль - последние 10 ходов)
        // С помощью такого графика можно будет проследить АКТИВНОСТЬ использования определенных фигур в разные этапы игры
    }

    private void lineChartOfPieceTypeFrequency() {
        // С какой частотой(НЕ усредненной) ходили определенными типами фигур(y - проценты для типа фигуры, x - ВСЕ сыгранные игры)
        // С помощью такого графика можно будет проследить ПОСТОЯННОСТЬ использования определенных фигур

    }

    @Override
    public void gameStarted() {
        gamesStatistics.add(new GameStatistics());
        currentGameStatistics++;
        countGames++;
    }

    @Override
    public void gameOver(final GameStatus gameStatus) {
        gamesStatistics.get(currentGameStatistics).addGameEndStatistics(selfPlay.getGameInfo());
        if (gamesAmount == countGames) {
            saveStatistics();
        }
    }

    @Override
    public void playerActed(final Side side, final MoveInfo moveInfo) {
        final long lastTimeMark = System.currentTimeMillis();
        if (selfPlay.getFirstPlayer().getSide() == side)
            gamesStatistics.get(currentGameStatistics)
                    .updateStatisticsAfterFirstPlayerMove(moveInfo, ((lastTimeMark - previousTimeMark) / 1000F));
        if (selfPlay.getSecondPlayer().getSide() == side)
            gamesStatistics.get(currentGameStatistics)
                    .updateStatisticsAfterSecondPlayerMove(moveInfo, ((lastTimeMark - previousTimeMark) / 1000F));
        previousTimeMark = lastTimeMark;
    }


}

