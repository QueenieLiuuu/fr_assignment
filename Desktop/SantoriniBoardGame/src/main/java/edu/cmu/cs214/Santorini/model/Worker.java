package edu.cmu.cs214.Santorini.model;

import java.util.Objects;

/**
 * Worker model
 */
public class Worker {
    private int id;  // each worker has a unique Id
    private Point p;  // worker position (x, y)
    private Player player;  // the player they belongs to

    public void setPlayer(Player player) {
        this.player = player;
    }


    public Worker(int id, Player player) {
        this.id = id;
        this.player = player;
    }

    public Worker(int id, Point p, Player player) {
        this.id = id;
        this.p = p;
        this.player = player;
    }

    public Worker(int id) {
        this.id = id;
    }

    public Point getP() {
        return p;
    }

    public void setP(Point p) {
        this.p = p;
    }

    public Player getPlayer() {
        return player;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public WorkerDto toDto() {
        return new WorkerDto(id, p.x(), p.y());
    }

    public Worker copy() {
        return new Worker(id, p, player);
    }

    public static Worker getWorkerById(Player player1, Player player2, int id) {
        assert player1 != null;
        assert player2 != null;
        if (id == 1) {
            return player1.getWorkers().size() > 0 ? player1.getWorkers().get(0) : null;
        } else if (id == 2) {
            return player1.getWorkers().size() > 1 ? player1.getWorkers().get(1) : null;
        } else if (id == 3) {
            return player2.getWorkers().size() > 0 ? player2.getWorkers().get(0) : null;
        } else if (id == 4) {
            return player2.getWorkers().size() > 1 ? player2.getWorkers().get(1) : null;
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", p=" + p +
                ", player=" + player.getId() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return id == worker.id && p.equals(worker.p) && Objects.equals(player, worker.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, p);
    }
}
