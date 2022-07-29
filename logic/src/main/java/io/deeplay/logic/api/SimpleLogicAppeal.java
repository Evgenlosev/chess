package io.deeplay.logic.api;

import io.deeplay.core.model.MoveInfo;

import java.util.Set;

/**
 * Упрощенный интерфейс обращения к логике
 */
public interface SimpleLogicAppeal {
    /**
     * @param fenNotation нотация Форсайта — Эдвардса
     * @return возвращает true если стороне с текущим ходом - мат
     */
    boolean isMate(final String fenNotation);

    /**
     * @param fenNotation нотация Форсайта — Эдвардса
     * @return возвращает true если одна из сторон не имеет возможности совершить ход
     */
    boolean isStalemate(final String fenNotation);

    /**
     * @param fenNotation нотация Форсайта — Эдвардса
     * @return возвращает все возможные ходы для стороны предстоящим ходом, если игра закончилась list = 0
     */
    Set<MoveInfo> getMoves(final String fenNotation);

}
