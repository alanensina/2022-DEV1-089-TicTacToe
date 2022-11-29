package esn.kata.services;

import esn.kata.models.Game;
import esn.kata.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    @Autowired
    private GameRepository repository;

    public ResponseEntity<String> startGame() {
        var game = repository.save(new Game());

        return game.getId() != null ? new ResponseEntity<>("Game started. ID: " + game.getId(), HttpStatus.CREATED) :
                new ResponseEntity<>("Error to start a game.", HttpStatus.BAD_REQUEST);
    }

}
