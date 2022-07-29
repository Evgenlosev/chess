package io.deeplay.logic.model;

public class CheckData {
    /**
     * Здесь находятся все клетки под атакой противника, без учёта фигуры короля, в виде битборда.
     * Нужно, для того чтобы отсечь множество ходов короля
     */
    public final long allAttacks;
    private final CheckType checkType;
    /**
     * Если checkType = ONE, то в переменной содержится информация о местоположении угрозы королю
     * и клетках которые она атакует в виде битборда
     */
    private final long threatPieceBitboard;
    /**
     * Если checkType = ONE, то в переменной содержится информация о местоположении угрозы королю
     */
    private final long threateningPiecePositionBitboard;

    public CheckData(final CheckType checkType,
                     final long threatPieceBitboard,
                     final long threateningPiecePositionBitboard,
                     final long allAttacks) {
        this.checkType = checkType;
        this.threatPieceBitboard = threatPieceBitboard;
        this.threateningPiecePositionBitboard = threateningPiecePositionBitboard;
        this.allAttacks = allAttacks;
    }

    public CheckType getCheckType() {
        return checkType;
    }

    public long getThreatPieceBitboard() {
        return threatPieceBitboard;
    }

    public long getAllAttacks() {
        return allAttacks;
    }

    public long getThreateningPiecePositionBitboard() {
        return threateningPiecePositionBitboard;
    }

    @Override
    public String toString() {
        return "CheckData{" +
                "checkType=" + checkType +
                ", threatPieceBitboard=" + threatPieceBitboard +
                ", threateningPiecePositionBitboard=" + threateningPiecePositionBitboard +
                ", allAttacks=" + allAttacks +
                '}';
    }
}
