package edu.cmu.cs214.Santorini.state;

public class Buff {
    private int toPlayerId;
    private int lastRound;
    private BuffType type;

    public Buff(int toPlayerId, int lastRound, BuffType type) {
        this.toPlayerId = toPlayerId;
        this.lastRound = lastRound;
        this.type = type;
    }

    public int getToPlayerId() {
        return toPlayerId;
    }

    public void setToPlayerId(int toPlayerId) {
        this.toPlayerId = toPlayerId;
    }

    public int getLastRound() {
        return lastRound;
    }

    public void setLastRound(int lastRound) {
        this.lastRound = lastRound;
    }

    public void setType(BuffType type) {
        this.type = type;
    }

    public BuffType getType() {
        return type;
    }

    public enum BuffType {
        cannotMoveUp,
    }
}