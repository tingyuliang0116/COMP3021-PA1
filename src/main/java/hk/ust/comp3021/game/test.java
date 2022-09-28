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
        Set<Position> playerposition=new HashSet<>();
        Set<Position> BoxId=new HashSet<>();
        for(int i=1;i<gamemap.length;i++){
            for(int j=0;j<gamemap[1].length();j++) {
                if (gamemap[i].charAt(j)=='@'){
                    des.add(new Position(j, i-1));
                }
                if (gamemap[i].charAt(j)=='#') {
                    wall.add(new Position(j, i-1));
                }
                if (gamemap[i].charAt(j)=='A') {
                    playerposition.add(new Position(j, i-1));
                }
                if (gamemap[i].charAt(j)=='a') {
                    BoxId.add(new Position(j, i-1));
                }
            }
        }



    }

}

