package io.deeplay.core.player.vladbot;

import ch.qos.logback.classic.Logger;
import io.deeplay.core.evaluation.Evaluation;
import io.deeplay.core.evaluation.PeSTO;
import io.deeplay.core.model.*;
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
        BoardSituationInfo currentMoveBoardSituation;
        // Пат - лучше проигрышного хода (если лучший ход = проигрышному -> берем пат)
        try {
            ChessBoard virtualChessBoard = gameInfo.getBoard().clone(); // TODO: изменить на аналог
            for (MoveInfo moveInfo : allMoves) {
                virtualChessBoard.updateBoard(moveInfo);
                currentScore = sideCoeff * getEvaluation().evaluateBoard(virtualChessBoard);
                if (gameInfo.isMate(virtualChessBoard)) {
                    currentScore = Integer.MAX_VALUE;
                }
                if (gameInfo.isStalemate(virtualChessBoard)
                        || gameInfo.isDrawByPieceShortage(virtualChessBoard)
                        || gameInfo.isThreefoldRepetition(virtualChessBoard)
                        || gameInfo.isMovesWithoutAttackOrPawnMove(virtualChessBoard)) {
                    // Пат лучше, чем проигрыш, поэтому он ценнее на 1
                    currentScore = Integer.MIN_VALUE + 1;
                }
                if (bestScore < currentScore) {
                    bestScore = currentScore;
                    bestMove = moveInfo;
                }
                virtualChessBoard = virtualChessBoard.getPreviousChessBoard(); // TODO: изменить на аналог
            }
        } catch (CloneNotSupportedException e) {
            LOGGER.error("Ошибка при клонировании: " + e.getMessage());
        }
        Objects.requireNonNull(bestMove);
        return bestMove;
    }

}
