package esn.kata.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private boolean isFinished;
    @Column
    private String nextPlayer;
    @Column
    private List<String> positions = new ArrayList<>(9);

    public Game() {
        this.nextPlayer = "X";
        this.positions = Arrays.asList(null, null, null, null, null, null, null, null, null);
    }

    public Game(Long id, boolean isFinished, String nextPlayer, List<String> positions) {
        this.id = id;
        this.isFinished = isFinished;
        this.nextPlayer = nextPlayer;
        this.positions = positions;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public String getNextPlayer() {
        return nextPlayer;
    }

    public void setNextPlayer(String nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public List<String> getPositions() {
        return positions;
    }

    public void setPositions(List<String> positions) {
        this.positions = positions;
    }
}
