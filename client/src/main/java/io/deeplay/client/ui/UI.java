package io.deeplay.client.ui;

import io.deeplay.core.model.GameInfo;
import io.deeplay.core.model.GameStatus;
import io.deeplay.core.model.MoveInfo;
import io.deeplay.interaction.clientToServer.StartGameRequest;

public interface UI {
    void updateBoard(GameInfo gameInfo);
    MoveInfo getMove(GameInfo gameInfo);
    void gameOver(GameStatus gameStatus);
    StartGameRequest getGameSettings();
    void start(GameInfo gameInfo);
}
