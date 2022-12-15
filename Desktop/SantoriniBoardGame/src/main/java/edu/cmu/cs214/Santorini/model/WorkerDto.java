package edu.cmu.cs214.Santorini.model;


import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * Worker DTO, used for RESTful API serialization
 */
@JsonAutoDetect
public final class WorkerDto {
    final private int id;
    final private int x;
    final private int y;

    public WorkerDto(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
