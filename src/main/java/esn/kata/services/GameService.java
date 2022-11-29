package esn.kata.services;

import esn.kata.models.Game;
import esn.kata.models.dto.RequestDTO;
import esn.kata.models.dto.ResponseDTO;
import esn.kata.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;

@Service
public class GameService {

    @Autowired
    private GameRepository repository;

    public ResponseEntity<String> startGame() {
        var game = repository.save(new Game());

        return game.getId() != null ? new ResponseEntity<>("Game started. ID: " + game.getId(), HttpStatus.CREATED) :
                new ResponseEntity<>("Error to start a game.", HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<ResponseDTO> findById(Long id) {
        var game = repository.findById(id).orElse(null);
        var response = new ResponseDTO();

        if (game != null) {
            response.setGames(Arrays.asList(game));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        response.setMessages(Arrays.asList("Game not found!"));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ResponseDTO> play(RequestDTO dto) {
        var response = new ResponseDTO();
        validateInputs(dto, response);

        if (!response.getMessages().isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        return null;
    }

    private ResponseDTO validateInputs(RequestDTO dto, ResponseDTO response) {
        var messages = new ArrayList<String>();

        if (dto.getId() == null) {
            messages.add("Id can't be null");
        }

        if (dto.getPlayer() == null) {
            messages.add("Player can't be null");
        }

        if (dto.getPosition() < 0 || dto.getPosition() > 8) {
            messages.add("Position must be greater or equal than zero and lesser or equal than 8.");
        }

        if (messages.isEmpty()) return response;

        response.setMessages(messages);
        return response;
    }

    public ResponseEntity<ResponseDTO> listGames() {
        var response = new ResponseDTO();
        response.setGames(repository.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
