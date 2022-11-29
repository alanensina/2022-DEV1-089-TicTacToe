package esn.kata.controllers;

import esn.kata.models.Game;
import esn.kata.repositories.GameRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameRepository repository;

    @Test
    public void should_be_able_to_start_a_game() throws Exception {
        var game = new Game(1L, false, "X", getEmptyPositions());
        Mockito.when(repository.save(any(Game.class))).thenReturn(game);
        var response = mockMvc.perform(post("/start")).andExpect(status().isCreated()).andReturn().getResponse();
        Assertions.assertEquals("Game started. ID: 1", response.getContentAsString());
    }

    private static List<String> getEmptyPositions() {
        return Arrays.asList(null, null, null, null, null, null, null, null, null);
    }
}
