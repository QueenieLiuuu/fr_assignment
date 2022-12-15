package edu.cmu.cs214.Santorini.game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

/**
 * ContextController contains RESTful APIs
 */
@CrossOrigin
@RestController
public class ContextController {
    @Autowired
    Game game;

    /**
     * @return all contexts in the game session
     */
    @GetMapping("/context")
    public @ResponseBody
    List<ContextDto> list() {
        List<ContextDto> contexts = new ArrayList<>();
        for (var context : game.getContexts()) {
            contexts.add(context.toDto());
        }
        return contexts;
    }

    /**
     * given an id, search the nearest former context.
     *
     * @param id id that ret.id < id
     * @return former nearest contextDto,
     * raise 404 if context not found
     */
    @GetMapping("/context/former/{id}/")
    public @ResponseBody
    ContextDto setFormer(@PathVariable int id) {
        int lo = 0;
        int hi = game.getContexts().size() - 1;
        int mid;
        List<Context> contexts = game.getContexts();
        while (lo < hi) {
            mid = lo + (hi - lo + 1) / 2;
            if (contexts.get(mid).getId() >= id) {
                hi = mid - 1;
            } else {
                lo = mid;
            }
        }
        Context candidate = contexts.get(lo);
        if (candidate.getId() < id) {
            game.setCurrentContextId(candidate.getId());
            System.out.println(game.getContexts().size());
            System.out.println(game.getContexts());
            return candidate.toDto();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, id + " latter not found"
        );
    }

    /**
     * given an id, search the nearest latter context.
     *
     * @param id id that ret.id > id
     * @return latter nearest contextDto,
     * raise 404 if context not found
     */
    @GetMapping("/context/latter/{id}/")
    public @ResponseBody
    ContextDto setLatter(@PathVariable int id) {
        int lo = 0;
        int hi = game.getContexts().size() - 1;
        int mid;
        List<Context> contexts = game.getContexts();
        while (lo < hi) {
            mid = lo + (hi - lo) / 2;
            if (contexts.get(mid).getId() <= id) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        Context candidate = contexts.get(lo);
        if (candidate.getId() > id) {
            game.setCurrentContextId(candidate.getId());
            System.out.println(game.getContexts().size());
            System.out.println(game.getContexts());
            return candidate.toDto();
        }
        throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, id + " former not found"
        );
    }
}
