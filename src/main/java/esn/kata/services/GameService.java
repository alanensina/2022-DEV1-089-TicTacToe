package esn.kata.services;

import esn.kata.models.Game;
import esn.kata.models.dto.ResponseDTO;
import esn.kata.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<ResponseDTO> listGames() {
        var response = new ResponseDTO();
        response.setGames(repository.findAll());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
