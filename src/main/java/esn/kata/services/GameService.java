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
import java.util.List;

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

        var game = repository.findById(dto.getId()).orElse(null);
        if (game == null) {
            response.setMessages(Arrays.asList("Game not found!"));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        var error = validateMove(dto, response, game);
        if (error != null) return error;

        var board = updateBoard(dto, game);

        game.setNextPlayer(game.getNextPlayer().equalsIgnoreCase("X") ? "O" : "X");
        var gameIsOver = checkIfGameIsOver(dto, response, game, board);
        if (gameIsOver != null) return gameIsOver;

        repository.save(game);
        response.setGames(Arrays.asList(game));
        response.setMessages(null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<ResponseDTO> checkIfGameIsOver(RequestDTO dto, ResponseDTO response, Game game, List<String> board) {
        if (checkWinner(board, dto.getPlayer())) {
            response.setMessages(Arrays.asList("Game is over, Player: " + dto.getPlayer() + " is the winner."));
            game.setFinished(true);
            repository.save(game);
            response.setGames(Arrays.asList(game));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        if (checkFullBoard(board)) {
            response.setMessages(Arrays.asList("Game is over, there's no winner."));
            game.setFinished(true);
            repository.save(game);
            response.setGames(Arrays.asList(game));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        return null;
    }

    private boolean checkWinner(List<String> positions, String player) {
        return (player.equalsIgnoreCase(positions.get(0)) && player.equalsIgnoreCase(positions.get(1)) && player.equalsIgnoreCase(positions.get(2))) ||
                (player.equalsIgnoreCase(positions.get(3)) && player.equalsIgnoreCase(positions.get(4)) && player.equalsIgnoreCase(positions.get(5))) ||
                (player.equalsIgnoreCase(positions.get(6)) && player.equalsIgnoreCase(positions.get(7)) && player.equalsIgnoreCase(positions.get(8))) ||
                (player.equalsIgnoreCase(positions.get(0)) && player.equalsIgnoreCase(positions.get(4)) && player.equalsIgnoreCase(positions.get(8))) ||
                (player.equalsIgnoreCase(positions.get(2)) && player.equalsIgnoreCase(positions.get(4)) && player.equalsIgnoreCase(positions.get(6))) ||
                (player.equalsIgnoreCase(positions.get(0)) && player.equalsIgnoreCase(positions.get(3)) && player.equalsIgnoreCase(positions.get(6))) ||
                (player.equalsIgnoreCase(positions.get(1)) && player.equalsIgnoreCase(positions.get(4)) && player.equalsIgnoreCase(positions.get(7))) ||
                (player.equalsIgnoreCase(positions.get(2)) && player.equalsIgnoreCase(positions.get(5)) && player.equalsIgnoreCase(positions.get(8)));
    }

    private boolean checkFullBoard(List<String> positions) {
        for (String position : positions) {
            if (position == null) return false;
        }

        return true;
    }

    private List<String> updateBoard(RequestDTO dto, Game game) {
        var updatedPositions = game.getPositions();
        updatedPositions.set(dto.getPosition(), dto.getPlayer());
        game.setPositions(updatedPositions);
        return updatedPositions;
    }

    private ResponseEntity<ResponseDTO> validateMove(RequestDTO dto, ResponseDTO response, Game game) {
        if (!game.getNextPlayer().equalsIgnoreCase(dto.getPlayer())) {
            response.setMessages(Arrays.asList("Player not allowed to play!"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (game.isFinished()) {
            response.setMessages(Arrays.asList("This game is already finished!"));
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        var positionOnBoard = game.getPositions().get(dto.getPosition());
        if (positionOnBoard != null) {
            response.setMessages(Arrays.asList("Invalid move, position is already occupied!"));
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
