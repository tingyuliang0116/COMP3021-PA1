package hk.ust.comp3021.tui;


import hk.ust.comp3021.actions.ActionResult;
import hk.ust.comp3021.actions.Exit;
import hk.ust.comp3021.actions.InvalidInput;
import hk.ust.comp3021.game.*;
import hk.ust.comp3021.utils.NotImplementedException;
import hk.ust.comp3021.utils.StringResources;

import java.util.Optional;

/**
 * A Sokoban game running in the terminal.
 */
public class TerminalSokobanGame extends AbstractSokobanGame {

    private final InputEngine inputEngine;

    private final RenderingEngine renderingEngine;

    /**
     * Create a new instance of TerminalSokobanGame.
     * Terminal-based game only support at most two players, although the hk.ust.comp3021.game package supports up to 26 players.
     * This is only because it is hard to control too many players in a terminal-based game.
     *
     * @param gameState       The game state.
     * @param inputEngine     the terminal input engin.
     * @param renderingEngine the terminal rendering engine.
     * @throws IllegalArgumentException when there are more than two players in the map.
     */
    public TerminalSokobanGame(GameState gameState, TerminalInputEngine inputEngine, TerminalRenderingEngine renderingEngine) {
        super(gameState);
        this.inputEngine=inputEngine;
        this.renderingEngine = renderingEngine;
        // TODO
        // Check the number of players
        if(this.state.getAllPlayerPositions().size()>2){
            throw new IllegalArgumentException();
        }
    }

    @Override
    public void run() {
        // TODO
        renderingEngine.render(state);
        while(!this.state.isWin()){
            var t=inputEngine.fetchAction();
            renderingEngine.render(this.state);
            if(t instanceof Exit){
                renderingEngine.message(StringResources.GAME_EXIT_MESSAGE);
                break;
            }
            if(t instanceof InvalidInput){
                renderingEngine.message(StringResources.INVALID_INPUT_MESSAGE);
            }
            if(state.getUndoQuota()==Optional.of(0)){
                renderingEngine.message(StringResources.UNDO_QUOTA_RUN_OUT);
            }

        }
        renderingEngine.message(StringResources.WIN_MESSAGE);
    }
}
