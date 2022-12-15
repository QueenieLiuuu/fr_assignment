package edu.cmu.cs214.Santorini.god;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * RESTful API related to god
 */
@CrossOrigin
@RestController
public class GodController {
    /**
     * List all gods in game rules
     * Convert to DTO so that strategy will not be serialized
     * @return all godDtos
     */
    @GetMapping("/god")
    public @ResponseBody
    List<GodDto> list() {
        List<GodDto> gods = new ArrayList<>();
        for (God god : GodFactory.createAllGods()) {
            gods.add(god.toDto());
        }
        return gods;
    }
}
