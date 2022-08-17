package io.deeplay.core.player.vladbot;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.evaluation.vladevaluations.Evaluation;
import io.deeplay.core.evaluation.vladevaluations.PeSTO;
import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.model.Side;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Set;

/**
 * Бот, который генерирует ход основываясь только на оценочных функциях.
 * Для оценки, по умолчанию, используется Песто.
 */
public class EvaluationBot extends VBot {
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(EvaluationBot.class);
    // TODO: Zobrist Keys должен реализовывать Evaluation и возвращать результат если он уже есть

    public EvaluationBot(final Side side) {
        this(side, new PeSTO());
    }

    public EvaluationBot(final Side side, final Evaluation evaluation) {
        super(side, evaluation);
    }

    @Override
    public MoveInfo getAnswer(final GameInfo gameInfo) {
        return evaluateBestMove(gameInfo);
    }

    private MoveInfo evaluateBestMove(final GameInfo gameInfo) { // Этот бот не увидит, то что через ход оппонента будет ничья
        final int sideCoeff = getSide() == Side.WHITE ? 1 : -1;
        final Set<MoveInfo> allMoves = gameInfo.getAvailableMoves();
        MoveInfo bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        int currentScore;
        for (final MoveInfo moveInfo : allMoves) {
            final GameInfo virtualGameInfo = gameInfo.copy(moveInfo); // копия с ходом
            currentScore = sideCoeff * getEvaluation().evaluateBoard(virtualGameInfo.getBoard());
            if (virtualGameInfo.isMate()) {
                currentScore = Integer.MAX_VALUE;
            }
            if (virtualGameInfo.isStalemate()
                    || virtualGameInfo.isDrawByPieceShortage()
                    || virtualGameInfo.isThreefoldRepetition()
                    || virtualGameInfo.isMovesWithoutAttackOrPawnMove()) {
                // Пат лучше, чем проигрыш, поэтому он ценнее на 1
                currentScore = Integer.MIN_VALUE + 1;
            }
            if (bestScore < currentScore) {
                bestScore = currentScore;
                bestMove = moveInfo;
            }
        }
        Objects.requireNonNull(bestMove);
        return bestMove;
    }

}
