package edu.cmu.cs214.Santorini.model;

import edu.cmu.cs214.Santorini.god.God;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * There are two players in one game, and each player controls two workers
 * A player has its own god character and it is unchangeable along the whole game
 */
public class Player {
    private int id;
    private ArrayList<Worker> workers;
    private God god;

    public Player(int id) {
        this.id = id;
        this.workers = new ArrayList<>();
    }

    public Player(int id, ArrayList<Worker> workers, God god) {
        this.id = id;
        this.workers = workers;
        this.god = god;
    }

    /**
     * Create a new worker for player, place them on the board
     * and add to the player's workers list
     */
    public boolean addWorker(Board board, Point point) {
        if (this.workers.size() >= 2 || !board.positionInBoard(point) || board.hasWorker(point)) {
            return false;
        }
        int wid = (this.id == 1) ? 1 : 3;
        wid += this.workers.size();
        Worker worker = new Worker(wid, this);
        worker.setP(point);
        this.workers.add(worker);
        board.addWorker(worker);
        return true;
    }

    public List<Worker> getWorkers() {
        return this.workers;
    }

    public void setGod(God god) {
        this.god = god;
    }

    public God getGod() {
        return god;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PlayerDto toDto() {
        List<WorkerDto> workerDtos = new ArrayList<>();
        for (var worker : workers) {
            workerDtos.add(worker.toDto());
        }
        return new PlayerDto(id, workerDtos, god != null ? god.toDto() : null);
    }

    public Player copy() {
        ArrayList<Worker> copyWorkers = new ArrayList<>();
        for (var worker : workers) {
            copyWorkers.add(worker.copy());
        }
        return new Player(id, copyWorkers, god);
    }

    @Override
    public String toString() {
        StringBuilder workerIds = new StringBuilder();
        workerIds.append('[');
        for (var worker : workers) {
            workerIds.append(worker.getId());
            workerIds.append(",");
        }
        workerIds.append(']');
        return "Player{" +
                "id=" + id +
                ", workers=" + workerIds +
                ", god=" + god +
                '}';
    }
}
