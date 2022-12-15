package edu.cmu.cs214.Santorini.game;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import edu.cmu.cs214.Santorini.model.PlayerDto;

import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Context DTO, used for RESTful API serialization
 */
@JsonAutoDetect
public class ContextDto {
    private int id;
    private String state;
    private PlayerDto player1;
    private PlayerDto player2;
    private int activePlayerId;
    private int activeWorkerId;
    private Set<String> nextAvailActions;
    private List<List<Integer>> board;
    private ContextDto lastContextDto;

    public ContextDto() {
    }

    public int getActivePlayerId() {
        return activePlayerId;
    }

    public int getActiveWorkerId() {
        return activeWorkerId;
    }

    public PlayerDto getPlayer1() {
        return player1;
    }

    public PlayerDto getPlayer2() {
        return player2;
    }

    public ContextDto(int id, String state, PlayerDto player1, PlayerDto player2,
                      int activePlayerId, int activeWorkerId, List<List<Integer>> board,
                      Set<String> nextAvailActions, ContextDto lastContextDto) {
        this.id = id;
        this.state = state;
        this.player1 = player1;
        this.player2 = player2;
        this.activePlayerId = activePlayerId;
        this.activeWorkerId = activeWorkerId;
        this.board = board;
        this.nextAvailActions = nextAvailActions;
        this.lastContextDto = lastContextDto;
    }

    public int getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public List<List<Integer>> getBoard() {
        return board;
    }

    public Set<String> getNextAvailActions() {
        return nextAvailActions;
    }

    public ContextDto getLastContextDto() {
        return lastContextDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextDto that = (ContextDto) o;
        return id == that.id && activePlayerId == that.activePlayerId && activeWorkerId == that.activeWorkerId && state.equals(that.state) && player1.equals(that.player1) && player2.equals(that.player2) && nextAvailActions.equals(that.nextAvailActions) && board.equals(that.board) && Objects.equals(lastContextDto, that.lastContextDto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, state, player1, player2, activePlayerId, activeWorkerId, nextAvailActions, board, lastContextDto);
    }
}
