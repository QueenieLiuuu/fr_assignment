package edu.cmu.cs214.Santorini.god;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;

@SpringBootTest
public class GodFactoryTest {
    @Test
    public void testFactory() {
        List<God> allGods = GodFactory.createAllGods();
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Man")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Apollo")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Artemis")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Athena")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Atlas")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Demeter")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Hephaestus")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Hermes")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Minotaur")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Pan")).count());
        assertEquals(1, allGods.stream().filter(g -> g.getName().equals("Prometheus")).count());
        assertEquals(11, allGods.size());
    }
}
