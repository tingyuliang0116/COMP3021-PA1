package hk.ust.comp3021.game;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import hk.ust.comp3021.actions.Action;
import hk.ust.comp3021.actions.Exit;
import hk.ust.comp3021.entities.*;
import hk.ust.comp3021.tui.TerminalInputEngine;
import hk.ust.comp3021.tui.TerminalRenderingEngine;
import hk.ust.comp3021.utils.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.ArrayList;
public class test {
    public static void main(String[] args) {
        String mapText = """
            233
            ######
            #A.a@#
            #..a@#
            ###### 
            """;
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
        Set<Position> tes=des;
        System.out.println(tes);
        Position t=new Position(4,1);
        System.out.println(des.contains(t));
        Set<Position> b=new HashSet<>();
        b.add(new Position(4,1));
        b.add(new Position(4,2));
        for(Position l:b){
            if(des.contains(l)){
                System.out.println("this");
                tes.remove(b);
            }
        }
        if(tes.size()==0){
            System.out.println(true);
        }
        System.out.println(false);







    }

}

