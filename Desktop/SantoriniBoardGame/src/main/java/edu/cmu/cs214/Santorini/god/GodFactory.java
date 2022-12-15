package edu.cmu.cs214.Santorini.god;

import edu.cmu.cs214.Santorini.god.strategy.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * use static Factory pattern to generate god by GodName (enum)
 */
public class GodFactory {
    /**
     * generate all gods in game rules (for frontend display)
     */
    public static List<God> createAllGods() {
        List<God> gods = new ArrayList<>();
        String[] godNames =
                Arrays.stream(GodName.class.getEnumConstants()).map(Enum::name).toArray(String[]::new);
        for (String name : godNames) {
            gods.add(GodFactory.createGod(GodName.valueOf(name)));
        }
        return gods;
    }

    public static God createGod(GodName name) {
        if (name == GodName.Man) {
            return new God(
                    "Man",
                    "I'm just me.",
                    "God bless me: (Nothing happened)",
                    "https://www.thestampmaker.com/stock_rubber_stamp_images/SSS1_HAPPY_FACE.jpg",
                    new ManStrategy()
            );
        } else if (name == GodName.Pan) {
            return new God(
                    "Pan",
                    "God of the Wild",
                    "Win Condition: You also win if " +
                            "your Worker moves down two or more levels",
                    "https://www.ultraboardgames.com/santorini/gfx/pan.jpg",
                    new PanStrategy()
            );
        } else if (name == GodName.Minotaur) {
            return new God(
                    "Minotaur",
                    "Bull-headed Monster",
                    "Your Move: Your Worker may " +
                            "move into an opponent Worker’s " +
                            "space, if their Worker can be " +
                            "forced one space straight backwards to an " +
                            "unoccupied space at any level.",
                    "https://www.ultraboardgames.com/santorini/gfx/minotaur.jpg",
                    new MinotaurStrategy()
            );
        } else if (name == GodName.Athena) {
            return new God(
                    "Athena",
                    "Goddess of Wisdom",
                    "Opponent's Turn: If one of your Workers moved up on your last turn, opponent Workers cannot move up this turn.",
                    "https://www.ultraboardgames.com/santorini/gfx/athena.jpg",
                    new AthenaStrategy()
            );
        } else if (name == GodName.Apollo) {
            return new God(
                    "Apollo",
                    "God of Music",
                    "Your Move: Your Worker may move into an opponent Worker's space by forcing their Worker to the space yours just vacated.",
                    "https://www.ultraboardgames.com/santorini/gfx/apollo.jpg",
                    new ApolloStrategy()
            );
        } else if (name == GodName.Artemis) {
            return new God(
                    "Artemis",
                    "Goddess of the Hunt",
                    "Your Move: Your Worker may move one additional time, but not back to its initial space.",
                    "https://www.ultraboardgames.com/santorini/gfx/artemis.jpg",
                    new ArtemisStrategy()
            );
        } else if (name == GodName.Atlas) {
            return new God(
                    "Atlas",
                    "Titan Shouldering the Heavens",
                    "Your Build: Your Worker may build a dome at any level.",
                    "https://www.ultraboardgames.com/santorini/gfx/atlas.jpg",
                    new AtlasStrategy()
            );
        } else if (name == GodName.Demeter) {
            return new God(
                    "Demeter",
                    "Goddess of the Harvest",
                    "Your Build: Your Worker may build one additional time, but not on the same space.",
                    "https://www.ultraboardgames.com/santorini/gfx/demeter.jpg",
                    new DemeterStrategy()
            );
        } else if (name == GodName.Hephaestus) {
            return new God(
                    "Hephaestus",
                    "God of Blacksmiths",
                    "Your Build: Your Worker may build one additional block (not dome) on top of your first block.",
                    "https://www.ultraboardgames.com/santorini/gfx/hephaestus.jpg",
                    new HephaestusStrategy()
            );
        } else if (name == GodName.Hermes) {
            return new God(
                    "Hermes",
                    "God of Travel",
                    "Your Turn: If your Workers do not move up or down, they may each move any number of times (even zero), and then either builds.",
                    "https://www.ultraboardgames.com/santorini/gfx/hermes.jpg",
                    new HermesStrategy()
            );
        } else if (name == GodName.Prometheus) {
            return new God(
                    "Prometheus",
                    "Titan Benefactor of Mankind",
                    "Your Turn: If your Worker does not move up, it may build both before and after moving.",
                    "https://www.ultraboardgames.com/santorini/gfx/prometheus.jpg",
                    new PrometheusStrategy()
            );
        }
        return null;
    }
}
