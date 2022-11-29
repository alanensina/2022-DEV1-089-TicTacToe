package esn.kata.controllers;

import esn.kata.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {

    @Autowired
    private GameService service;

    @PostMapping("/start")
    public ResponseEntity<String> start() {
        return service.startGame();
    }
}
