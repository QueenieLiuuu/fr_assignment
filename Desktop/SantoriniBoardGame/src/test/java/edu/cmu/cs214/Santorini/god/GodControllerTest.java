package edu.cmu.cs214.Santorini.god;

import edu.cmu.cs214.Santorini.AbstractControllerTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class GodControllerTest extends AbstractControllerTest {
    @Test
    public void testListGod() throws Exception {
        String uri = "/god";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        GodDto[] allGods = super.mapFromJson(content, GodDto[].class);
        assertEquals(11, allGods.length);
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Man")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Apollo")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Artemis")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Athena")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Atlas")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Demeter")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Hephaestus")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Hermes")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Minotaur")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Pan")).count());
        assertEquals(1, Arrays.stream(allGods).filter(g -> g.getName().equals("Prometheus")).count());
    }
}
