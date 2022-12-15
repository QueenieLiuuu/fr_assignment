package edu.cmu.cs214.Santorini.game;

import edu.cmu.cs214.Santorini.AbstractControllerTest;
import edu.cmu.cs214.Santorini.state.prepare.GodChosen;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.*;

public class GameControllerTest extends AbstractControllerTest {
    @Autowired
    Game game;

    @Test
    public void testInitGame() throws Exception {
        String uri = "/game/init/";
        game.reset();

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        Context context = game.getCurrentContext();
        assertEquals(GodChosen.class, context.getState().getClass());
        assertEquals(1, game.getContexts().size());
        assertEquals(context.getId(), game.getCurrentContextId().intValue());
        String content = mvcResult.getResponse().getContentAsString();
        ContextDto contextDto = super.mapFromJson(content, ContextDto.class);
    }

}
