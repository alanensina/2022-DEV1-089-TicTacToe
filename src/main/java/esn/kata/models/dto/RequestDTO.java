package esn.kata.models.dto;

import java.io.Serializable;

public class RequestDTO implements Serializable {

    private Long id;
    private String player;
    private int position;

    public RequestDTO(Long id, String player, int position) {
        this.id = id;
        this.player = player;
        this.position = position;
    }

    public RequestDTO() { }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
