package hk.ust.comp3021.game;

import hk.ust.comp3021.entities.*;
import hk.ust.comp3021.utils.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.ArrayList;

/**
 * A Sokoban game board.
 * GameBoard consists of information loaded from map data, such as
 * <li>Width and height of the game map</li>
 * <li>Walls in the map</li>
 * <li>Box destinations</li>
 * <li>Initial locations of boxes and player</li>
 * <p/>
 * GameBoard is capable to create many GameState instances, each representing an ongoing game.
 */
public class GameMap {
    int width;
    int height;
    Set<Position> destination;
    int undolimit;
    Set<Position> wall;
    HashMap<Integer,Position> player;
    HashMap<Integer,List<Position>> box;

    /**
     * Create a new GameMap with width, height, set of box destinations and undo limit.
     *
     * @param maxWdth     Width of the game map.
     * @param maxHeight    Height of the game map.
     * @param destinations Set of box destination positions.
     * @param undoLimit    Undo limit.
     *                     Positive numbers specify the maximum number of undo actions.
     *                     0 means undo is not allowed.
     *                     -1 means unlimited. Other negative numbers are not allowed.
     */
    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit) {
        // TODO
        this.width=maxWidth;
        this.height=maxHeight;
        this.destination=destinations;
        this.undolimit=undoLimit;
    }
    public GameMap(int maxWidth, int maxHeight, Set<Position> destinations, int undoLimit,Set<Position> wall, HashMap<Integer,Position> player,HashMap<Integer,List<Position>> box) {
        this.wall=wall;
        this.box=box;
        this.player=player;
        this.width=maxWidth;
        this.height=maxHeight;
        this.destination=destinations;
        this.undolimit=undoLimit;
    }
    /**
     * Parses the map from a string representation.
     * The first line is undo limit.
     * Starting from the second line, the game map is represented as follows,
     * <li># represents a {@link Wall}</li>
     * <li>@ represents a box destination.</li>
     * <li>Any upper-case letter represents a {@link Player}.</li>
     * <li>
     * Any lower-case letter represents a {@link Box} that is only movable by the player with the corresponding upper-case letter.
     * For instance, box "a" can only be moved by player "A" and not movable by player "B".
     * </li>
     * <li>. represents an {@link Empty} position in the map, meaning there is no player or box currently at this position.</li>
     * <p>
     * Notes:
     * <li>
     * There can be at most 26 players.
     * All implementations of classes in the hk.ust.comp3021.game package should support up to 26 players.
     * </li>
     * <li>
     * For simplicity, we assume the given map is bounded with a closed boundary.
     * There is no need to check this point.
     * </li>
     * <li>
     * Example maps can be found in "src/main/resources".
     * </li>
     *
     * @param mapText The string representation.
     * @return The parsed GameMap object.
     * @throws IllegalArgumentException if undo limit is negative but not -1.
     * @throws IllegalArgumentException if there are multiple same upper-case letters, i.e., one player can only exist at one position.
     * @throws IllegalArgumentException if there are no players in the map.
     * @throws IllegalArgumentException if the number of boxes is not equal to the number of box destinations.
     * @throws IllegalArgumentException if there are boxes whose {@link Box#getPlayerId()} do not match any player on the game board,
     *                                  or if there are players that have no corresponding boxes.
     */
    public static GameMap parse(String mapText) {
        // TODO
        String [] gamemap = mapText.split("\\r?\\n");
        Set<Position> des=new HashSet<>();
        Set<Position> wall=new HashSet<>();
        HashMap<Integer,Position> player= new HashMap<>();
        HashMap<Integer,List<Position>> box=new HashMap<>();
        for( char c = 'a'; c <= 'z'; ++c){
            player.put((int)(c-'a'),null);
            box.put((int)(c-'a'),null);
        }
        int w=gamemap[1].length();
        for(int i=1;i<gamemap.length;i++){
            if(gamemap[i].length()>w){
                w=gamemap[i].length();
            }
            for(int j=0;j<gamemap[i].length();j++) {
                if (gamemap[i].charAt(j)=='@'){
                    des.add(new Position(j, i-1));
                }
                if (gamemap[i].charAt(j)=='#') {
                    wall.add(new Position(j, i-1));
                }
                if (Character.isAlphabetic(gamemap[i].charAt(j)) && Character.isUpperCase(gamemap[i].charAt(j))) {
                    if(player.get(i)!=null){
                        throw new IllegalArgumentException();
                    }
                    player.put((int)(gamemap[i].charAt(j)-'A'),new Position(j,i-1));
                }
                if (Character.isAlphabetic(gamemap[i].charAt(j)) && Character.isLowerCase(gamemap[i].charAt(j))) {
                    if(box.get((int)(gamemap[i].charAt(j)-'a'))==null){
                        box.put((int)(gamemap[i].charAt(j)-'a'),new ArrayList<>());
                    }
                    box.get((int)(gamemap[i].charAt(j)-'a')).add(new Position(j,i-1));

                }
            }
        }
        if(Integer.parseInt(gamemap[0])<0 && Integer.parseInt(gamemap[0])!=-1){
            throw new IllegalArgumentException();
        }
        int playernum=0;
        int boxcount=0;
        for(int i=0;i<26;i++){
            if(player.get(i)!=null){
                if(box.get(i)==null){
                    throw new IllegalArgumentException();
                }
                boxcount+=box.get(i).size();
                playernum+=1;
            }
        }
        if(playernum==0){
            throw new IllegalArgumentException();
        }
        if((boxcount!=des.size())){
            throw new IllegalArgumentException();
        }

        return new GameMap(w,gamemap.length-1,des,Integer.parseInt(gamemap[0]),wall,player,box);
    }
    /**
     * Get the entity object at the given position.
     *
     * @param position the position of the entity in the game map.
     * @return Entity object.
     */
    @Nullable
    public Entity getEntity(Position position) {
        // TODO
        if(wall.contains(position)){
            return new Wall();
        }
        for(int i=0;i<26;i++){
            if(box.get(i)==null || player.get(i)==null){
                continue;
            }
            else if(box.get(i).contains(position)){
                return new Box(i);
            }
            else if(player.get(i).equals(position)){
                return new Player(i);
            }
        }
        return new Empty();
    }

    /**
     * Put one entity at the given position in the game map.
     *
     * @param position the position in the game map to put the entity.
     * @param entity   the entity to put into game map.
     */
    public void putEntity(Position position, Entity entity) {
        // TODO`
        if(entity instanceof Box){
            if(box.get(((Box) entity).getPlayerId())==null){
                box.put(((Box) entity).getPlayerId(),new ArrayList<>());
            }
            box.get(((Box) entity).getPlayerId()).add(position);
        }
        if(entity instanceof Wall){
            wall.add(position);
        }
        if(entity instanceof Player){
            player.put(((Player) entity).getId(), position);
        }
    }
    public void removeEntity(Position position, Entity entity) {
        if(entity instanceof Box){
            box.get(((Box) entity).getPlayerId()).remove(position);
        }
        if(entity instanceof Wall){
            wall.remove(position);
        }
        if(entity instanceof Player){
            player.put(((Player) entity).getId(),null);
        }
    }
    /**
     * Get all box destination positions as a set in the game map.
     *
     * @return a set of positions.
     */
    public @NotNull @Unmodifiable Set<Position> getDestinations() {
        // TODO
        return destination;
    }

    /**
     * Get the undo limit of the game map.
     *
     * @return undo limit.
     */
    public Optional<Integer> getUndoLimit() {
        // TODO
        return Optional.of(undolimit);
    }

    /**
     * Get all players' id as a set.
     *
     * @return a set of player id.
     */
    public Set<Integer> getPlayerIds() {
        // TODO
        Set<Integer> re=new HashSet<>();
        for(int i=0;i<26;i++){
            if(player.get(i)!=null){
                re.add(i);
            }
        }
        return re;
    }

    /**
     * Get the maximum width of the game map.
     *
     * @return maximum width.
     */
    public int getMaxWidth() {
        // TODO
        return width;
    }

    /**
     * Get the maximum height of the game map.
     *
     * @return maximum height.
     */
    public int getMaxHeight() {
        // TODO
        return height;
    }

}
