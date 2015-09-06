package game;

/**
 * The standard commands in a game.
 * 
 * These commands will trigger different messages.
 * 
 * @author Jasmine
 */

public enum GameControlCommand {
    UNDO, HINT, NEW, MUSIC, SAVE, LOAD, HOME, EXIT, INVALID, 
    SAVED, UNSAVEABLE, LOADED, UNLOADABLE, WIN, LOSE, DRAW, 
    COMPUTER_STUCK, USER_STUCK, OPPONENT_STUCK,
    REJECT, AGREE, OPP_REJECT, OPP_AGREE, SURRENDER, OPP_SURRENDER, 
    OPP_QUIT, OPP_REQUEST_NEW, OPP_DISCONNECTED,
    UNKNOWN_BUG;
    
}
