package io.deeplay.core.model;

public enum GameStatus {
    // Пока сервер создает сеанс игры, она неактивна
    INACTIVE("Сеанс игры еще не стартовал"),
    // С момента начала игры клиентом, до получения конечных результатов игра является активной
    ACTIVE("Игра активна"),
    // Если с игрой что то произошло не по причине игроков, то игра считается прерваной
    INTERRUPTED("Игра прервана"),
    // Белые поставили мат, либо черные сдались
    WHITE_WON("Белые победили"),
    // Черные поставили мат, либо белые сдались
    BLACK_WON("Черные победили"),
    // Ничья по соглашению, либо по правилам
    DRAW("Ничья");

    private final String message;

    GameStatus(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
