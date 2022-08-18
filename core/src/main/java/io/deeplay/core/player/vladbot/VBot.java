package io.deeplay.core.player.vladbot;

import io.deeplay.core.evaluation.vladevaluations.Evaluation;
import io.deeplay.core.evaluation.vladevaluations.PeSTO;
import io.deeplay.core.model.*;
import io.deeplay.core.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public abstract class VBot extends Player {
    private static final Logger LOGGER = LoggerFactory.getLogger(VBot.class);

    private final Evaluation evaluation;

    public VBot(final Side side) {
        this(side, new PeSTO());
    }

    public VBot(Side side, Evaluation evaluation) {
        super(side);
        this.evaluation = evaluation;
        LOGGER.info("Для {} установлена оценочная функция - {}", this, evaluation);
    }

    public Evaluation getEvaluation() {
        return evaluation;
    }

    @Override
    public abstract MoveInfo getAnswer(GameInfo gameInfo);

    public static List<MoveInfo> sortWithMVVLVA(final GameInfo gameInfo) {
        Comparator<Figure> figureComparator = new VBot.SortByMVVLVA();
        final ArrayList<MoveInfo> moves = new ArrayList<>(gameInfo.getAvailableMoves());
        final List<MoveInfo> sortedMoves = new ArrayList<>();
        for (MoveInfo move : moves) {
            if (MapsStorage.ATTACKS.contains(move.getMoveType())) {
                sortedMoves.add(move);
            }
        }
        moves.removeAll(sortedMoves);
        sortedMoves.sort(Comparator.comparingInt(move -> figureComparator.compare(move.getFigure(),
                gameInfo.getChessBoard()[move.getCellTo().getRow()][move.getCellTo().getColumn()].getFigure())));
        sortedMoves.addAll(moves);
        return sortedMoves;
    }

    @Override
    public abstract String getName();

    protected MoveInfo getGreedyDecision(final List<EvaluatedMove> evaluatedMoves) {
        return evaluatedMoves.stream().max(Comparator.comparingInt(e -> e.score)).orElseThrow().moveInfo;
    }

    protected boolean isMateByBot(final GameStatus gameStatus) {
        return gameStatus ==
                (getSide() == Side.WHITE ? GameStatus.WHITE_WON : GameStatus.BLACK_WON);
    }

    protected boolean isMateByOpponent(final GameStatus gameStatus) {
        return gameStatus ==
                (getSide() == Side.WHITE ? GameStatus.BLACK_WON : GameStatus.WHITE_WON);
    }

    public int evaluate(final GameInfo gameInfo) {
        final int sideCoeff = getSide() == Side.WHITE ? 1 : -1;
        if (MapsStorage.END_GAME_BY_RULES.contains(gameInfo.getGameStatus())) {
            return evaluateByEndGameStatus(gameInfo.getGameStatus());
        }
        return sideCoeff * getEvaluation().evaluateBoard(gameInfo.getBoard());
    }

    protected int evaluateByEndGameStatus(final GameStatus gameStatus) {
        if (isMateByBot(gameStatus)) {
            return Integer.MAX_VALUE;
        }
        if (isMateByOpponent(gameStatus)) {
            return Integer.MIN_VALUE;
        }
        if (gameStatus == GameStatus.STALEMATE
                || gameStatus == GameStatus.INSUFFICIENT_MATING_MATERIAL
                || gameStatus == GameStatus.THREEFOLD_REPETITION
                || gameStatus == GameStatus.FIFTY_MOVES_RULE) {
            // Пат лучше, чем проигрыш, поэтому он ценнее на 1
            return Integer.MIN_VALUE + 1;
        }
        throw new IllegalArgumentException("Impossible end game reason by rules: " + gameStatus);
    }

    /**
     * Позволяет избежать Horizon Effect.
     *
     * @param gameInfo
     * @param alpha
     * @param beta
     * @return оценка хода
     */
    protected int quiesce(final GameInfo gameInfo, int alpha, final int beta) {
        int score = evaluate(gameInfo);
        if (score >= beta)
            return beta;
        if (score > alpha)
            alpha = score;

        final List<MoveInfo> attackMoves =
                gameInfo.getAvailableMoves().stream()
                        .filter(x -> MapsStorage.ATTACKS.contains(x.getMoveType())).collect(Collectors.toList());
        for (MoveInfo attackMove : attackMoves) {
            final GameInfo virtualGameInfo = gameInfo.copy(attackMove);
            score = -quiesce(virtualGameInfo, -beta, -alpha);
            if (score >= beta)
                return beta;
            if (score > alpha)
                alpha = score;
        }
        return alpha;
    }

    protected static final class EvaluatedMove {
        final MoveInfo moveInfo;
        final int score;

        public EvaluatedMove(final MoveInfo moveInfo, final int score) {
            this.moveInfo = moveInfo;
            this.score = score;
        }
    }

    /**
     * Использовать только когда в MoveInfo атакующий ход.
     * Ставит все АТАКУЮЩИЕ ходы в приоритет, тихие ходы можно просто дополнить в конец списка.
     * Comparator для сортировки важности ходов - MVVLVA ("most valuable victim - least valuable attacker" -
     * самая ценная жертва - наименее ценный нападающий).
     * Например, ход в котором пешка срубает ферзя противника должен цениться больше,
     * т.к. потерять пешку в размен будет не так страшно.
     */
    private final static class SortByMVVLVA implements Comparator<Figure> {
        @Override
        public int compare(Figure figure1, Figure figure2) {
            return -(score(figure2) - score(figure1));
        }

        private int score(Figure figure) {
            return Math.abs(figure.getWeight());
        }
    }

}
