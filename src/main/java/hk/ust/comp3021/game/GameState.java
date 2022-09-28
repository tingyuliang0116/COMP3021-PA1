package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import hk.ust.comp3021.utils.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Optional;
import java.util.Set;

/**
 * The state of the Sokoban Game.
 * Each game state represents an ongoing game.
 * As the game goes, the game state changes while players are moving while the original game map stays the unmodified.
 * <b>The game state should not modify the original game map.</b>
 * <p>
 * GameState consists of things changing as the game goes, such as:
 * <li>Current locations of all crates.</li>
 * <li>A move history.</li>
 * <li>Current location of player.</li>
 * <li>Undo quota left.</li>
 */
public class GameState {
    /**
     * Create a running game state from a game map.
     *
     * @param map the game map from which to create this game state.
     */
    Set<Position> destination;
    int undolimit;
    Set<Position> wall;
    Set<Position> playerposition;
    Set<Position> BoxId;
    GameMap initmap;
    GameMap thismap;

    public GameState(@NotNull GameMap map) {

        // TODO
        this.destination=map.destination;
        this.undolimit=map.undolimit;
        this.wall=map.wall;
        this.playerposition=map.playerposition;
        this.BoxId=map.BoxId;
        initmap=map;
        thismap=initmap;
    }

    /**
     * Get the current position of the player with the given id.
     *
     * @param id player id.
     * @return the current position of the player.
     */
    public @Nullable Position getPlayerPositionById(int id) {
        // TODO
        Position[] p = playerposition.toArray(new Position[playerposition.size()]);
        return p[0];
    }

    /**
     * Get current positions of all players in the game map.
     *
     * @return a set of positions of all players.
     */
    public @NotNull Set<Position> getAllPlayerPositions() {
        // TODO
        return playerposition;
    }
    /**
     * Get the entity that is currently at the given position.
     *
     * @param position the position of the entity.
     * @return the entity object.
     */
    public @Nullable Entity getEntity(@NotNull Position position) {
        // TODO
        if(wall.contains(position)){
            return new Wall();
        }
        if(BoxId.contains(position)){
            return new Box(0);
        }
        if(playerposition.contains(position)){
            return new Player(0);
        }
        return new Empty();
    }

    /**
     * Get all box destination positions as a set in the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return a set of positions.
     */
    public @NotNull @Unmodifiable Set<Position> getDestinations() {
        // TODO
        return destination;
    }

    /**
     * Get the undo quota currently left, i.e., the maximum number of undo actions that can be performed from now on.
     * If undo is unlimited,
     *
     * @return the undo quota left (using {@link Optional#of(Object)}) if the game has an undo limit;
     * {@link Optional#empty()} if the game has unlimited undo.
     */
    public Optional<Integer> getUndoQuota() {
        // TODO
        return Optional.of(undolimit);
    }

    /**
     * Check whether the game wins or not.
     * The game wins only when all box destinations have been occupied by boxes.
     *
     * @return true is the game wins.
     */
    public boolean isWin() {
// TODO
        Set<Position> tes=destination;
        for(Position a:BoxId){
            if(tes.contains(a)){
                tes.remove(a);
            }
        }
        if(tes.size()==0){
            return true;
        }
        return false;
    }
    public void putEntity(Position position, Entity entity) {
        // TODO`
        if(entity instanceof Box){
            BoxId.add(position);
        }
        if(entity instanceof Wall){
            wall.add(position);
        }
        if(entity instanceof Player){
            playerposition.add(position);
        }
    }
    public void removeEntity(Position position, Entity entity) {
        // TODO`
        if(entity instanceof Box){
            BoxId.remove(position);
        }
        if(entity instanceof Wall){
            wall.remove(position);
        }
        if(entity instanceof Player){
            playerposition.remove(position);
        }
    }
    /**
     * Move the entity from one position to another.
     * This method assumes the validity of this move is ensured.
     * <b>The validity of the move of the entity in one position to another need not to check.</b>
     *
     * @param from The current position of the entity to move.
     * @param to   The position to move the entity to.
     */
    public void move(Position from, Position to) {
        // TODO
        Entity fr=getEntity(from);
        putEntity(to,fr);
        removeEntity(from,fr);
    }

    /**
     * Record a checkpoint of the game state, including:
     * <li>All current positions of entities in the game map.</li>
     * <li>Current undo quota</li>
     * <p>
     * Checkpoint is used in {@link GameState#undo()}.
     * Every undo actions reverts the game state to the last checkpoint.
     */
    public void checkpoint() {
        // TODO
        destination=thismap.destination;
        undolimit=thismap.undolimit;
        wall=thismap.getWall();
        playerposition=thismap.playerposition;
        BoxId=thismap.getBoxPosition(0);
    }

    /**
     * Revert the game state to the last checkpoint in history.
     * This method assumes there is still undo quota left, and decreases the undo quota by one.
     * <p>
     * If there is no checkpoint recorded, i.e., before moving any box when the game starts,
     * revert to the initial game state.
     */
    public void undo() {
        // TODO
        checkpoint();
    }

    /**
     * Get the maximum width of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum width.
     */
    public int getMapMaxWidth() {
        // TODO
        return thismap.getMaxWidth();
    }

    /**
     * Get the maximum height of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum height.
     */
    public int getMapMaxHeight() {
        // TODO
        return thismap.getMaxHeight();
    }
}
