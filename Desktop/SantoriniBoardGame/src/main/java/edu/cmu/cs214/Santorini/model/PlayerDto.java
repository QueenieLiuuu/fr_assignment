package edu.cmu.cs214.Santorini.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import edu.cmu.cs214.Santorini.god.GodDto;

import java.util.List;

/**
 * Player DTO, used for RESTful API serialization
 */
@JsonAutoDetect
public class PlayerDto {
    private int id;
    private List<WorkerDto> workers;
    private GodDto god;

    public PlayerDto(int id, List<WorkerDto> workers, GodDto god) {
        this.id = id;
        this.workers = workers;
        this.god = god;
    }

    public PlayerDto() {
    }

    public int getId() {
        return id;
    }

    public List<WorkerDto> getWorkers() {
        return workers;
    }

    public GodDto getGod() {
        return god;
    }
}
