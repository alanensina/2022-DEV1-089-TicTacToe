package esn.kata.controllers;

import esn.kata.models.dto.ResponseDTO;
import esn.kata.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/list")
    public ResponseEntity<ResponseDTO> list(){
        return service.listGames();
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<ResponseDTO> find(@PathVariable Long id){
        return service.findById(id);
    }
}
