package esn.kata.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import esn.kata.models.Game;
import esn.kata.models.dto.ResponseDTO;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GameRepository repository;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void should_be_able_to_start_a_game() throws Exception {
        var game = new Game(1L, false, "X", getEmptyPositions());
        Mockito.when(repository.save(any(Game.class))).thenReturn(game);
        var response = mockMvc.perform(post("/start")).andExpect(status().isCreated()).andReturn().getResponse();
        Assertions.assertEquals("Game started. ID: 1", response.getContentAsString());
    }

    @Test
    public void should_be_able_to_list_all_games() throws Exception {
        var games = Arrays.asList(
                new Game(1L, true, "X", Arrays.asList("X", "X", "X", null, null, "O", null, null, "O")),
                new Game(2L, false, "O", Arrays.asList("X", null, null, null, null, null, null, null, null)),
                new Game(3L, false, "O", Arrays.asList(null, null, "X", null, null, "O", null, null, "X")),
                new Game(4L, false, "X", Arrays.asList("X", "O", "X", "O", null, null, null, null, null))
        );

        Mockito.when(repository.findAll()).thenReturn(games);

        var jsonResponse = mockMvc.perform(get("/list")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var response = mapper.readValue(jsonResponse, ResponseDTO.class);
        Assertions.assertEquals(4, response.getGames().size());
    }

    @Test
    public void should_be_able_to_find_by_id() throws Exception {
        var game = new Game(1L, true, "X", Arrays.asList("X", "X", "X", null, null, "O", null, null, "O"));
        Mockito.when(repository.findById(1L)).thenReturn(Optional.of(game));

        var jsonResponse = mockMvc.perform(get("/find/1")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var response = mapper.readValue(jsonResponse, ResponseDTO.class);
        Assertions.assertEquals(1, response.getGames().size());
    }

    @Test
    public void should_not_be_able_to_find_by_id_with_invalid_id() throws Exception {
        var jsonResponse = mockMvc.perform(get("/find/22")).andExpect(status().isNotFound()).andReturn().getResponse().getContentAsString();
        var response = mapper.readValue(jsonResponse, ResponseDTO.class);
        Assertions.assertEquals("Game not found!", response.getMessages().get(0));
    }

    private static List<String> getEmptyPositions() {
        return Arrays.asList(null, null, null, null, null, null, null, null, null);
    }
}
