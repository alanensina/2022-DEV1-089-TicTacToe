package esn.kata.services;

import esn.kata.models.Game;
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

    private static List<String> getEmptyPositions() {
        return Arrays.asList(null, null, null, null, null, null, null, null, null);
    }
}
