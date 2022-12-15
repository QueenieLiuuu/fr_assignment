package edu.cmu.cs214.Santorini.model;

public enum Direction {
    NW("NW", new int[]{-1, -1}),
    N("N", new int[]{-1, 0}),
    NE("NE", new int[]{-1, 1}),
    E("E", new int[]{0, 1}),
    SE("SE", new int[]{1, 1}),
    S("S", new int[]{1, 0}),
    SW("SW", new int[]{1, -1}),
    W("W", new int[]{0, -1});

    private final String key;
    private final int[] value;

    Direction(String key, int[] value) {
        this.key = key;
        this.value = value;
    }

    public static Direction getDirectionFromKey(String key) {
        for (Direction direction : values()) {
            if (direction.getKey().equals(key)) {
                return direction;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public int[] getValue() {
        return value;
    }
}