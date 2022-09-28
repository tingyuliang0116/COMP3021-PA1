package hk.ust.comp3021.tui;

import hk.ust.comp3021.actions.*;
import hk.ust.comp3021.game.InputEngine;
import hk.ust.comp3021.utils.NotImplementedException;
import hk.ust.comp3021.utils.StringResources;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.Scanner;

/**
 * An input engine that fetches actions from terminal input.
 */
public class  TerminalInputEngine implements InputEngine {

    /**
     * The {@link Scanner} for reading input from the terminal.
     */
    private final Scanner terminalScanner;

    /**
     * @param terminalStream The stream to read terminal inputs.
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    /**
     * Fetch an action from user in terminal to process.
     *
     * @return the user action.
     */
    @Override
    public @NotNull Action fetchAction() {
        // This is an example showing how to read a line from the Scanner class.
        // Feel free to change it if you do not like it.
        final var inputLine = terminalScanner.nextLine();
        // TODO
        if(inputLine.equals("exit")){
            return new Exit(0);
        }
        else if(inputLine.equals("A") || inputLine.equals("H")){
            return new Move.Left(0);
        }
        else if(inputLine.equals("S") || inputLine.equals("J")){
            return new Move.Down(0);
        }
        else if(inputLine.equals("W") || inputLine.equals("K")){
            return new Move.Up(0);
        }
        else if(inputLine.equals("D") || inputLine.equals("L")){
            return new Move.Right(0);
        }
        else if(inputLine.equals("U")){
            return new Undo(0);
        }
        System.out.println(4);
        return new InvalidInput(0, StringResources.INVALID_INPUT_MESSAGE);
    }
}
