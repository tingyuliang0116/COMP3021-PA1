package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

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
    GameMap thismap;
    List<GameMap> p=new ArrayList<>();
    int width;
    int height;
    Set<Position> destination=new HashSet<>();
    Set<Position> empty=new HashSet<>();
    int undolimit;
    Set<Position> wall=new HashSet<>();
    HashMap<Integer,Position> player=new HashMap<>();
    HashMap<Integer,List<Position>> box=new HashMap<>();

    public GameState(@NotNull GameMap map) {
        // TODO
        for( char c = 'a'; c <= 'z'; ++c){
            player.put((int)(c-'a'),null);
            box.put((int)(c-'a'),null);
        }
        width=map.width;
        height=map.height;
        undolimit=map.undolimit;
        for(Position t:map.destination){
            destination.add(new Position(t.x(),t.y()));
        }
        for(Position t:map.wall){
            wall.add(new Position(t.x(),t.y()));
        }
        for(Position t:map.empty){
            empty.add(new Position(t.x(),t.y()));
        }
        for(int i=0;i<26;i++){
            if(map.player.get(i)!=null){
                player.put(i,new Position(map.player.get(i).x(),map.player.get(i).y()));
            }
            if(map.box.get(i)!=null){
                box.put(i,new ArrayList<>());
                for(int j=0;j<map.box.get(i).size();j++){
                    box.get(i).add(new Position(map.box.get(i).get(j).x(),map.box.get(i).get(j).y()));
                }
            }
        }
        thismap=map;

        p.add(new GameMap(width,height,destination,undolimit,wall,player,box,empty));
    }
    /**
     * Get the current position of the player with the given id.
     *
     * @param id player id.
     * @return the current position of the player.
     */
    public @Nullable Position getPlayerPositionById(int id) {
        // TODO
        return thismap.player.get(id);
    }

    /**
     * Get current positions of all players in the game map.
     *
     * @return a set of positions of all players.
     */
    public @NotNull Set<Position> getAllPlayerPositions() {
        // TODO
        Set<Position> t=new HashSet<>();
        for(int i=0;i<26;i++){
            if((thismap.player.get(i)!=null)){
                t.add(getPlayerPositionById(i));
            }
        }
        return t;
    }
    /**
     * Get the entity that is currently at the given position.
     *
     * @param position the position of the entity.
     * @return the entity object.
     */
    public @Nullable Entity getEntity(@NotNull Position position) {
        // TODO
        if(thismap.wall.contains(position)){
            return new Wall();
        }
        for(int i=0;i<26;i++){
            if(thismap.box.get(i)==null || thismap.player.get(i)==null){
                continue;
            } else if(thismap.box.get(i).contains(position)){
                return new Box(i);
            } else if(thismap.player.get(i).equals(position)){
                return new Player(i);
            }
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
        return thismap.destination;
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
        return Optional.of(thismap.undolimit);
    }

    /**
     * Check whether the game wins or not.
     * The game wins only when all box destinations have been occupied by boxes.
     *
     * @return true is the game wins.
     */
    public boolean isWin() {
// TODO
        Set<Position> tes=getDestinations();

        Set<Position> b=new HashSet<>();
        for(int i=0;i<26;i++){
            if(thismap.box.get(i)!=null){
                for(int j=0;j<thismap.box.get(i).size();j++){
                    b.add(thismap.box.get(i).get(j));
                }
            }
        }
        for(Position l:b){
            if(thismap.destination.contains(l)){
                tes.remove(l);
            }
        }
        if(tes.size()==0){
            return true;
        }
        return false;
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
        thismap.removeEntity(from,fr);
        thismap.putEntity(to,fr);

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
        p.add(thismap);
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
        if(p.size()==2){
            thismap=p.get(0);
        } else{
            thismap=p.get(p.size()-2);
        }
        thismap.undolimit--;
    }

    /**
     * Get the maximum width of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum width.
     */
    public int getMapMaxWidth() {
        // TODO
        return thismap.width;
    }

    /**
     * Get the maximum height of the game map.
     * This should be the same as that in {@link GameMap} class.
     *
     * @return maximum height.
     */
    public int getMapMaxHeight() {
        // TODO
        return thismap.height;
    }
}
