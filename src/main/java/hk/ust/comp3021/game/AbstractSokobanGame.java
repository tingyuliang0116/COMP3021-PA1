package hk.ust.comp3021.game;

import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.entities.Box;
import hk.ust.comp3021.entities.Player;
import hk.ust.comp3021.entities.Wall;
import hk.ust.comp3021.utils.StringResources;
import org.jetbrains.annotations.NotNull;

/**
 * A base implementation of Sokoban Game.
 */
public abstract class AbstractSokobanGame implements SokobanGame {
    @NotNull
    protected final GameState state;

    protected AbstractSokobanGame(@NotNull GameState gameState) {
        this.state = gameState;
    }

    /**
     * @return True is the game should stop running.
     * For example when the user specified to exit the game or the user won the game.
     */
    protected boolean shouldStop() {
        // TODO
        if(state.isWin()){
            return true;
        }
        return false;

    }

    /**
     * @param action The action received from the user.
     * @return The result of the action.
     */
    protected ActionResult processAction(@NotNull Action action) {
        // TODO
        Position playerposition=state.getPlayerPositionById(action.getInitiator());
        if(action instanceof Move.Down){
            Position t= new Position(playerposition.x(),playerposition.y()+1);
            if(state.getEntity(t) instanceof Wall || state.getEntity(t) instanceof Player){
                return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
            } else if(state.getEntity(t) instanceof Box){
                if(state.thismap.box.get(action.getInitiator()).contains(t)){
                    Position m=new Position(t.x(),t.y()+1);
                    if(state.getEntity(m) instanceof Wall){
                        return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                    } else{
                        state.move(t,m);
                    }
                } else{
                    return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                }
            }
            state.move(playerposition,t);
            state.checkpoint();
            return new ActionResult.Success(action);
        } else if(action instanceof Move.Up){
            Position t= new Position(playerposition.x(),playerposition.y()-1);
            if(state.getEntity(t) instanceof Wall || state.getEntity(t) instanceof Player){
                return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
            } else if(state.getEntity(t) instanceof Box){
                if(state.thismap.box.get(action.getInitiator()).contains(t)){
                    Position m=new Position(t.x(),t.y()-1);
                    if(state.getEntity(m) instanceof Wall){
                        return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                    } else{
                        state.move(t,m);
                    }
                } else{
                    return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                }
            }
            state.move(playerposition,t);
            state.checkpoint();
            return new ActionResult.Success(action);
        } else if(action instanceof Move.Left){
            Position t= new Position(playerposition.x()-1,playerposition.y());
            if(state.getEntity(t) instanceof Wall || state.getEntity(t) instanceof Player){
                return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
            } else if(state.getEntity(t) instanceof Box){
                if(state.thismap.box.get(action.getInitiator()).contains(t)){
                    Position m=new Position(t.x()-1,t.y());
                    if(state.getEntity(m) instanceof Wall){
                        return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                    } else{
                        state.move(t,m);
                    }
                } else{
                    return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                }
            }
            state.move(playerposition,t);
            state.checkpoint();
            return new ActionResult.Success(action);
        } else if(action instanceof Move.Right){
            Position t= new Position(playerposition.x()+1,playerposition.y());
            if(state.getEntity(t) instanceof Wall || state.getEntity(t) instanceof Player){
                return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
            } else if(state.getEntity(t) instanceof Box){
                if(state.thismap.box.get(action.getInitiator()).contains(t)){
                    Position m=new Position(t.x()+1,t.y());
                    if(state.getEntity(m) instanceof Wall){
                        return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                    } else{
                        state.move(t,m);

                    }
                } else{
                    return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
                }
            }
            state.move(playerposition,t);
            state.checkpoint();
            return new ActionResult.Success(action);
        } else {
            return new ActionResult.Failed(action,StringResources.INVALID_INPUT_MESSAGE);
        }
    }
}
