package edu.cmu.cs214.Santorini;

import edu.cmu.cs214.Santorini.game.Context;
import edu.cmu.cs214.Santorini.game.Game;
import edu.cmu.cs214.Santorini.model.Board;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.state.run.BeforeMove;
import edu.cmu.cs214.Santorini.state.run.Win;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class IntegrationTest extends AbstractControllerTest {
    @Autowired
    Game game;

    private void bulkTestGets(List<String> uris) throws Exception {
        MvcResult mvcResult;
        for (String uri : uris) {
            mvcResult = this.mvc.perform(MockMvcRequestBuilders.get(uri)
                    .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
            int status = mvcResult.getResponse().getStatus();
            System.out.println(uri);
            assertEquals(200, status);
        }
    }

    private boolean checkBoardEqual(Board board, int[] array) {
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (board.getCell(i, j).getHeight() != array[i * 5 + j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Test
    public void testInitGameWithoutGod() throws Exception {
        String initUri = "/game/init/";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(initUri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        List<String> uris = new ArrayList<>(List.of(
                "/god/",
                "/game/choose_god/?playerId=1&godName=Man",
                "/game/choose_god/?playerId=2&godName=Man",
                "/game/init_worker/?playerId=1&x=1&y=2",
                "/game/init_worker/?playerId=1&x=2&y=1",
                "/game/init_worker/?playerId=2&x=1&y=1",
                "/game/init_worker/?playerId=2&x=2&y=2"
        ));
        this.bulkTestGets(uris);
        Context context = game.getCurrentContext();
        Board board = context.getBoard();
        assertTrue(this.checkBoardEqual(board,
                new int[]{
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0
                }));
        assertEquals(new Point(1, 2), context.getPlayer1().getWorkers().get(0).getP());
        assertEquals(new Point(2, 1), context.getPlayer1().getWorkers().get(1).getP());
        assertEquals(new Point(1, 1), context.getPlayer2().getWorkers().get(0).getP());
        assertEquals(new Point(2, 2), context.getPlayer2().getWorkers().get(1).getP());
        assertEquals(BeforeMove.class, context.getState().getClass());

        uris = new ArrayList<>(List.of(
                "/game/move/?workerId=1&x=2&y=3",
                "/game/build/?workerId=1&x=1&y=2&buildDome=false",
                "/game/move/?workerId=4&x=1&y=3",
                "/game/build/?workerId=4&x=2&y=2&buildDome=false",
                "/game/move/?workerId=2&x=3&y=1",
                "/game/build/?workerId=2&x=2&y=2&buildDome=false",
                "/game/move/?workerId=3&x=1&y=2",
                "/game/build/?workerId=3&x=2&y=1&buildDome=false",
                "/game/move/?workerId=2&x=2&y=1",
                "/game/build/?workerId=2&x=2&y=2&buildDome=false",
                "/game/move/?workerId=3&x=1&y=1",
                "/game/build/?workerId=3&x=1&y=2&buildDome=false",
                "/game/move/?workerId=2&x=1&y=2",
                "/game/build/?workerId=2&x=2&y=1&buildDome=false",
                "/game/move/?workerId=4&x=2&y=4",
                "/game/build/?workerId=4&x=3&y=3&buildDome=false",
                "/game/move/?workerId=2&x=2&y=2"
        ));
        this.bulkTestGets(uris);
        context = game.getCurrentContext();
        assertEquals(new Point(2, 3), context.getPlayer1().getWorkers().get(0).getP());
        assertEquals(new Point(2, 2), context.getPlayer1().getWorkers().get(1).getP());
        assertEquals(new Point(1, 1), context.getPlayer2().getWorkers().get(0).getP());
        assertEquals(new Point(2, 4), context.getPlayer2().getWorkers().get(1).getP());
        assertTrue(this.checkBoardEqual(context.getBoard(),
                new int[]{
                        0, 0, 0, 0, 0,
                        0, 0, 2, 0, 0,
                        0, 2, 3, 0, 0,
                        0, 0, 0, 1, 0,
                        0, 0, 0, 0, 0
                }));
        assertEquals(Win.class, context.getState().getClass());
    }

    @Test
    public void testInitGameWithGod() throws Exception {
        String initUri = "/game/init/";
        MvcResult mvcResult = this.mvc.perform(MockMvcRequestBuilders.post(initUri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);

        List<String> uris = new ArrayList<>(List.of(
                "/god/",
                "/game/choose_god/?playerId=1&godName=Demeter",
                "/game/choose_god/?playerId=2&godName=Minotaur",
                "/game/init_worker/?playerId=1&x=1&y=1",
                "/game/init_worker/?playerId=1&x=2&y=1",
                "/game/init_worker/?playerId=2&x=2&y=3",
                "/game/init_worker/?playerId=2&x=3&y=3",
                "/game/move/?workerId=2&x=2&y=2",
                "/game/build/?workerId=2&x=1&y=2&buildDome=false",
                "/game/build/?workerId=2&x=2&y=1&buildDome=false",
                "/game/move/?workerId=3&x=2&y=2",
                "/game/build/?workerId=3&x=3&y=2&buildDome=false",
                "/game/move/?workerId=2&x=3&y=2",
                "/game/build/?workerId=2&x=4&y=2&buildDome=false",
                "/game/build/?workerId=2&x=2&y=1&buildDome=false",
                "/game/move/?workerId=4&x=3&y=2",
                "/game/build/?workerId=4&x=4&y=2&buildDome=false",
                "/game/move/?workerId=1&x=1&y=2",
                "/game/build/?workerId=1&x=0&y=2&buildDome=false",
                "/game/build/?workerId=1&x=1&y=1&buildDome=false",
                "/game/move/?workerId=4&x=2&y=1",
                "/game/build/?workerId=4&x=3&y=2&buildDome=false",
                "/game/move/?workerId=1&x=1&y=1",
                "/game/build/?workerId=1&x=0&y=1&buildDome=false",
                "/game/build/?workerId=1&x=0&y=2&buildDome=false",
                "/game/move/?workerId=3&x=1&y=1",
                "/game/build/?workerId=3&x=1&y=2&buildDome=false",
                "/game/move/?workerId=1&x=0&y=1",
                "/game/build/?workerId=1&x=1&y=2&buildDome=false",
                "/game/build/?workerId=1&x=1&y=0&buildDome=false",
                "/game/move/?workerId=3&x=0&y=2",
                "/game/build/?workerId=3&x=1&y=3&buildDome=false",
                "/game/move/?workerId=1&x=1&y=1",
                "/game/build/?workerId=1&x=2&y=2&buildDome=false",
                "/game/build/?workerId=1&x=0&y=1&buildDome=false",
                "/game/move/?workerId=3&x=1&y=2"
        ));
        this.bulkTestGets(uris);
        Context context = game.getCurrentContext();
        assertEquals(new Point(1, 1), context.getPlayer1().getWorkers().get(0).getP());
        assertEquals(new Point(3, 1), context.getPlayer1().getWorkers().get(1).getP());
        assertEquals(new Point(1, 2), context.getPlayer2().getWorkers().get(0).getP());
        assertEquals(new Point(2, 1), context.getPlayer2().getWorkers().get(1).getP());
        assertTrue(this.checkBoardEqual(context.getBoard(),
                new int[]{
                        0, 2, 2, 0, 0,
                        1, 1, 3, 1, 0,
                        0, 2, 1, 0, 0,
                        0, 0, 2, 0, 0,
                        0, 0, 2, 0, 0
                }));
        assertEquals(Win.class, context.getState().getClass());
    }
}
