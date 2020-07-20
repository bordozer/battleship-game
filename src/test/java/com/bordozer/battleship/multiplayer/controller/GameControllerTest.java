package com.bordozer.battleship.multiplayer.controller;

import com.bordozer.battleship.multiplayer.dto.GameDto;
import com.bordozer.battleship.multiplayer.dto.GamePlayerDto;
import com.bordozer.battleship.multiplayer.dto.battle.GameStep;
import com.bordozer.battleship.multiplayer.exception.GameNotFoundException;
import com.bordozer.battleship.multiplayer.exception.IncopatibleGameStatusException;
import com.bordozer.battleship.multiplayer.service.GameService;
import com.bordozer.battleship.multiplayer.service.RequestHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

import static com.bordozer.commons.utils.FileUtils.readSystemResource;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameController.class)
@AutoConfigureMockMvc
class GameControllerTest {

    private static final String PLAYER_ID = "ea287b6a-15d1-424c-986f-612d68f9da02";
    private static final String GAME_ID = "051a4d66-78ee-44cc-9a70-f4ae222c8900";

    private static final String GAMES_URL = "/api/games";
    private static final String CREATE_GAME_URL = "/api/games/create";
    private static final String JOIN_GAME_URL = String.format("/api/games/join/%s", GAME_ID);

    private static final String GET_OPEN_GAMES_EXPECTED_RESPONSE =
            readSystemResource("tests/GameControllerTest/open-games-expected-response.json");
    private static final String CREATE_GAME_REQUEST =
            readSystemResource("tests/GameControllerTest/create-game-request.json");
    private static final String CREATE_GAME_EXPECTED_RESPONSE =
            readSystemResource("tests/GameControllerTest/create-game-expexted-response.json");
    private static final String GAME_ERROR_EXPECTED_RESPONSE =
            readSystemResource("tests/GameControllerTest/game-error.json");

    @MockBean
    private GameService gameService;
    @MockBean
    private RequestHelper requestHelper;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @SneakyThrows
    void shouldGetGames() {
        // given
        final var player1 = com.bordozer.battleship.multiplayer.dto.GamePlayerDto.builder()
                .id("ea287b6a-15d1-424c-986f-612d68f9da02")
                .name("Arthur Herbert")
                .build();
        final var player2 = com.bordozer.battleship.multiplayer.dto.GamePlayerDto.builder()
                .id("2b6d4c74-aae0-4a57-95a3-43f4cb8d9527")
                .name("Sir John Norris")
                .build();
        final var playerGame = GameDto.builder()
                .gameId("051a4d66-78ee-44cc-9a70-f4ae222c8900")
                .player1(player1)
                .player2(player2)
                .created("2020-07-10 19:04:03")
                .gameStep(GameStep.BATTLE)
                .build();
        final var openGame = GameDto.builder()
                .gameId("9567eba4-4bd8-4708-b123-6a7a9ed1029d")
                .player1(player1)
                .player2(null)
                .created("2020-06-09 18:20:45")
                .gameStep(GameStep.WAITING_FOR_OPPONENT)
                .build();
        when(requestHelper.getPlayerId(any(HttpServletRequest.class))).thenReturn(PLAYER_ID);
        when(gameService.getPlayerGames(PLAYER_ID)).thenReturn(Collections.singletonList(playerGame));
        when(gameService.getOpenGames(PLAYER_ID)).thenReturn(Collections.singletonList(openGame));

        // when
        mockMvc.perform(get(GAMES_URL)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(GET_OPEN_GAMES_EXPECTED_RESPONSE, true));
    }

    @Test
    @SneakyThrows
    void shouldCreateGames() {
        // given
        when(requestHelper.getPlayerId(any(HttpServletRequest.class))).thenReturn(PLAYER_ID);

        final GamePlayerDto player = GamePlayerDto.builder()
                .id(PLAYER_ID)
                .name("Arthur Herbert")
                .build();
        final var game = GameDto.builder()
                .gameId(GAME_ID)
                .player1(player)
                .created("2020-07-21 12:15:25")
                .gameStep(GameStep.WAITING_FOR_OPPONENT)
                .build();

        when(gameService.createGame(eq(PLAYER_ID), any())).thenReturn(game);

        // when
        mockMvc.perform(post(CREATE_GAME_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_GAME_REQUEST)
        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(CREATE_GAME_EXPECTED_RESPONSE, true));
    }

    @Test
    @SneakyThrows
    void shouldReturn422IfGameNotFoundWhenPlayerJoinGame() {
        // given
        when(requestHelper.getPlayerId(any(HttpServletRequest.class))).thenReturn(PLAYER_ID);
        doThrow(new GameNotFoundException(GAME_ID)).when(gameService).joinGame(eq(GAME_ID), eq(PLAYER_ID), any());

        // when
        mockMvc.perform(put(JOIN_GAME_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_GAME_REQUEST)
        )
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format(GAME_ERROR_EXPECTED_RESPONSE, GAME_ID), true));
    }

    @Test
    @SneakyThrows
    void shouldReturn417IfGameHasWrongStatusWhenPlayerJoinGame() {
        // given
        when(requestHelper.getPlayerId(any(HttpServletRequest.class))).thenReturn(PLAYER_ID);
        doThrow(new IncopatibleGameStatusException(GAME_ID)).when(gameService).joinGame(eq(GAME_ID), eq(PLAYER_ID), any());

        // when
        mockMvc.perform(put(JOIN_GAME_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("[]")
        )
                .andExpect(status().isExpectationFailed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(String.format(GAME_ERROR_EXPECTED_RESPONSE, GAME_ID), true));
    }
}
