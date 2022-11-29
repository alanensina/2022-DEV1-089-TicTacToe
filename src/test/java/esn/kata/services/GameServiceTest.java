package esn.kata.services;

import esn.kata.models.Game;
import esn.kata.models.dto.RequestDTO;
import esn.kata.repositories.GameRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
public class GameServiceTest {

    @Autowired
    private GameService service;

    @MockBean
    private GameRepository repository;

    @TestConfiguration
    static class GameServiceTestConfiguration {
        @Bean
        public GameService gameService() {
            return new GameService();
        }
    }

    @Test
    public void should_be_able_to_start_a_game() {
        var game = new Game(1L, false, "X", getEmptyPositions());
        Mockito.when(repository.save(any(Game.class))).thenReturn(game);

        var response = service.startGame();
        Assertions.assertEquals(HttpStatus.CREATED.value(), response.getStatusCode().value());
        Assertions.assertEquals("Game started. ID: " + game.getId(), response.getBody());
    }

    @Test
    public void should_not_be_able_to_start_a_game_if_did_not_save() {
        var game = new Game(null, false, "X", getEmptyPositions());
        Mockito.when(repository.save(any(Game.class))).thenReturn(game);

        var response = service.startGame();
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("Error to start a game.", response.getBody());
    }

    @Test
    public void should_be_able_to_list_all_games() {
        var games = Arrays.asList(
                new Game(1L, true, "X", Arrays.asList("X", "X", "X", null, null, "O", null, null, "O")),
                new Game(2L, false, "O", Arrays.asList("X", null, null, null, null, null, null, null, null)),
                new Game(3L, false, "O", Arrays.asList(null, null, "X", null, null, "O", null, null, "X")),
                new Game(4L, false, "X", Arrays.asList("X", "O", "X", "O", null, null, null, null, null))
        );

        Mockito.when(repository.findAll()).thenReturn(games);

        var response = service.listGames();
        var body = response.getBody();
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertEquals(4, body.getGames().size());
        Assertions.assertTrue(body.getGames().get(0).isFinished());
    }

    @Test
    public void should_be_able_to_find_by_id() {
        var game = new Game(1L, true, "X", Arrays.asList("X", "X", "X", null, null, "O", null, null, "O"));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));

        var response = service.findById(1L);
        var games = response.getBody().getGames();

        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertEquals(1, games.size());
        Assertions.assertEquals(1L, games.get(0).getId());
    }

    @Test
    public void should_not_be_able_to_find_by_id_with_id_incorrect() {
        Mockito.when(repository.findById(17L)).thenReturn(Optional.empty());
        var response = service.findById(17L);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        Assertions.assertEquals("Game not found!", message);
    }

    @Test
    public void should_not_be_able_to_make_a_move_with_id_null() {
        var dto = new RequestDTO(null, "X", 0);
        Mockito.when(repository.findById(null)).thenReturn(Optional.empty());
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("Id can't be null", message);
    }

    @Test
    public void should_not_be_able_to_make_a_move_with_player_null() {
        var dto = new RequestDTO(1L, null, 0);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("Player can't be null", message);
    }

    @Test
    public void should_not_be_able_to_make_a_move_with_position_invalid() {
        var dto = new RequestDTO(1L, "X", 10);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.empty());
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("Position must be greater or equal than zero and lesser or equal than 8.", message);
    }

    @Test
    public void should_not_be_able_to_play_if_the_id_is_invalid() {
        Mockito.when(repository.findById(17L)).thenReturn(Optional.empty());
        var dto = new RequestDTO(17L, "X", 0);
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode().value());
        Assertions.assertEquals("Game not found!", message);
    }

    @Test
    public void should_not_be_able_to_play_if_its_not_your_move() {
        var dto = new RequestDTO(1L, "O", 0);
        var game = new Game(1L, false, "X", getEmptyPositions());
        Optional.of(game);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("Player not allowed to play!", message);
    }

    @Test
    public void should_not_be_able_to_play_if_the_game_is_over() {
        var dto = new RequestDTO(1L, "X", 4);
        var game = new Game(1L, true, "X", Arrays.asList("X", "X", "X", "O", null, null, null, "O", null));
        Optional.of(game);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("This game is already finished!", message);
    }

    @Test
    public void should_not_be_able_to_play_if_the_position_is_occupied() {
        var dto = new RequestDTO(1L, "O", 0);
        var game = new Game(1L, false, "O", Arrays.asList("X", null, null, null, null, null, null, null, null));
        Optional.of(game);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));
        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        Assertions.assertEquals("Invalid move, position is already occupied!", message);
    }

    @Test
    public void should_be_able_to_finish_the_game_with_a_winner() {
        var dto = new RequestDTO(1L, "X", 2);
        var game = new Game(1L, false, "X", Arrays.asList("X", "X", null, null, null, null, "O", null, "O"));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));

        var gameToBeSaved = new Game(1L, true, "X", Arrays.asList("X", "X", "X", null, null, null, "O", null, "O"));
        Mockito.when(repository.save(any(Game.class))).thenReturn(gameToBeSaved);

        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertEquals("Game is over, Player: X is the winner.", message);
    }

    @Test
    public void should_be_able_to_finish_the_game_with_a_draw() {
        var dto = new RequestDTO(1L, "X", 6);
        var game = new Game(1L, false, "X", Arrays.asList("X", "O", "O", "O", "X", "X", null, "X", "O"));
        Optional.of(game);
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));

        var gameToBeSaved = new Game(1L, true, "X", Arrays.asList("X", "O", "O", "O", "X", "X", "X", "X", "O"));
        Mockito.when(repository.save(any(Game.class))).thenReturn(gameToBeSaved);

        var response = service.play(dto);
        var message = response.getBody().getMessages().get(0);
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode().value());
        Assertions.assertEquals("Game is over, there's no winner.", message);
    }

    private static List<String> getEmptyPositions() {
        return Arrays.asList(null, null, null, null, null, null, null, null, null);
    }
}
