package edu.cmu.cs214.Santorini.game;

import edu.cmu.cs214.Santorini.model.Player;
import edu.cmu.cs214.Santorini.model.Point;
import edu.cmu.cs214.Santorini.model.Worker;
import edu.cmu.cs214.Santorini.state.prepare.GodChosen;
import edu.cmu.cs214.Santorini.state.prepare.InitWorker;
import edu.cmu.cs214.Santorini.state.run.RunState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * GameController contains RESTful APIs
 */
@CrossOrigin
@RestController
public class GameController {
    @Autowired
    Game game;

    /**
     * @return all contexts in the game session
     */
    @PostMapping("/game/init/")
    public ContextDto init() {
        game.reset();
        Context context = game.createFirstContext();
        return context.toDto();
    }

    /**
     * player choose god
     *
     * @return raise 400 if god name invalid, raise 404 if playerId invalid,
     * 400 if at wrong state
     */
    @GetMapping("/game/choose_god/")
    public @ResponseBody
    ContextDto chooseGod(@RequestParam int playerId, @RequestParam String godName) {
        System.out.println("choose god, pid=" + playerId + ", gname=" + godName);
        Context currentContext = game.getCurrentContext();
        if (!(currentContext.getState() instanceof GodChosen currentState)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Player player;
        if (1 == playerId) {
            player = currentContext.getPlayer1();
        } else if (2 == playerId) {
            player = currentContext.getPlayer2();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        try {
            var nextContext = currentState.onChooseGod(player, godName);
            game.setCurrentContextId(nextContext.getId());
            game.addContextAndRemoveFollowing(nextContext);
            return nextContext.toDto();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * player set worker
     *
     * @return raise 404 if playerId invalid,
     * 400 if at wrong state or already worker there
     */
    @GetMapping("/game/init_worker/")
    public @ResponseBody
    ContextDto initWorker(@RequestParam int playerId, @RequestParam int x, @RequestParam int y) {
        System.out.println("set worker, pid=" + playerId + ", x=" + x + ", y=" + y);
        Context currentContext = game.getCurrentContext();
        if (!(currentContext.getState() instanceof InitWorker currentState)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Player player;
        if (1 == playerId) {
            player = currentContext.getPlayer1();
        } else if (2 == playerId) {
            player = currentContext.getPlayer2();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        var nextContext = currentState.onInitWorker(
                game.getCurrentContext().getBoard(), player, new Point(x, y));
        if (nextContext == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        game.setCurrentContextId(nextContext.getId());
        game.addContextAndRemoveFollowing(nextContext);
        return nextContext.toDto();
    }

    /**
     * worker move
     *
     * @return raise 400 if failed to move
     */
    @GetMapping("/game/move/")
    public @ResponseBody
    ContextDto move(@RequestParam int workerId, @RequestParam int x, @RequestParam int y) {
        System.out.println("move, wid=" + workerId + ", x=" + x + ", y=" + y);
        Context currentContext = game.getCurrentContext();
        if (!(currentContext.getState() instanceof RunState currentState)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Worker worker = game.getWorkerById(workerId, true);
        if (worker == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var nextContext = currentState.onMove(
                worker, new Point(x, y));
        if (nextContext == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        game.setCurrentContextId(nextContext.getId());
        game.addContextAndRemoveFollowing(nextContext);
        return nextContext.toDto();
    }

    /**
     * worker build
     * buildDome means specifically build dome on any level
     * @return raise 400 if failed to build
     */
    @GetMapping("/game/build/")
    public @ResponseBody
    ContextDto build(@RequestParam int workerId, @RequestParam int x, @RequestParam int y,
                     @RequestParam boolean buildDome) {
        System.out.println("build, wid=" + workerId + ", x=" + x + ", y=" + y);
        Context currentContext = game.getCurrentContext();
        if (!(currentContext.getState() instanceof RunState currentState)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Worker worker = game.getWorkerById(workerId, true);
        if (worker == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var nextContext = currentState.onBuild(
                worker, new Point(x, y), buildDome);
        if (nextContext == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        game.setCurrentContextId(nextContext.getId());
        game.addContextAndRemoveFollowing(nextContext);
        return nextContext.toDto();
    }

    /**
     * worker pass this stage (working with god power)
     * buildDome means specifically pass your turn
     * @return raise 400 if failed to pass
     */
    @GetMapping("/game/pass/")
    public @ResponseBody
    ContextDto build() {
        Context currentContext = game.getCurrentContext();
        if (!(currentContext.getState() instanceof RunState currentState)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        var nextContext = currentState.onPass();
        if (nextContext == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        game.setCurrentContextId(nextContext.getId());
        game.addContextAndRemoveFollowing(nextContext);
        return nextContext.toDto();
    }
}
