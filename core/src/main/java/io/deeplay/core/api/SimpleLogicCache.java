package io.deeplay.core.api;

import io.deeplay.core.model.BoardSituationInfo;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.core.parser.FENParser;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс реализует интерфейс логики на основе SimpleLogic.
 * Сохраняет ситуации(мат, пат и т.д.) на доске по первой(до первого пробела) части нотации fen,
 * при повторении возвращает уже посчитанный до этого результат.
 */
public class SimpleLogicCache implements SimpleLogicAppeal {
    private final Map<String, BoardSituationInfo> boardSituationInfoMap;
    private final SimpleLogicAppeal simpleLogicAppeal;

    public SimpleLogicCache() {
        this.boardSituationInfoMap = new ConcurrentHashMap<>();
        this.simpleLogicAppeal = new SimpleLogic();
    }

    @Override
    public boolean isMate(String fenNotation) {
        return boardSituationInfoMap.computeIfAbsent(FENParser.getPiecePlacement(fenNotation),
                simpleLogicAppeal::getBoardSituationInfo).isMate();

    }

    @Override
    public boolean isStalemate(String fenNotation) {
        return boardSituationInfoMap.computeIfAbsent(FENParser.getPiecePlacement(fenNotation),
                simpleLogicAppeal::getBoardSituationInfo).isStalemate();
    }

    @Override
    public boolean isDrawByPieceShortage(String fenNotation) {
        return boardSituationInfoMap.computeIfAbsent(FENParser.getPiecePlacement(fenNotation),
                simpleLogicAppeal::getBoardSituationInfo).isDrawByPieceShortage();
    }

    @Override
    public Set<MoveInfo> getMoves(String fenNotation) {
        return simpleLogicAppeal.getMoves(fenNotation);
    }

    @Override
    public BoardSituationInfo getBoardSituationInfo(String fenNotation) {
        return boardSituationInfoMap.computeIfAbsent(FENParser.getPiecePlacement(fenNotation),
                simpleLogicAppeal::getBoardSituationInfo);
    }
}
