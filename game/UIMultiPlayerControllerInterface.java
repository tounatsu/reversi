/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game;

/**
 *
 * @author Jasmine
 */
public interface UIMultiPlayerControllerInterface {
    
    
    
    /** display message
     * 
     * @param g 
     */
    public void message(GameControlCommand g);
    
   /** display message
     * 
     * @param g 
     */
    public void message(String g);
    
    /** intialize opponent info
     * 
     * @param color
     * @param name
     * @param rank 
     */
    public void initOpponent(int color, String name, String rank);

    public void setOppScore(int score);
    
    /** initialize board for new game
     * 
     */
    public void initGame();
  
    /** returns whether is in game
     * 
     * @return 
     */
    public boolean isInGame();
    
    /** returns whether is undo request
     * 
     * @return 
     */
    public boolean isUndoRequest();
    
    /** returns whether is new game request
     * 
     * @return 
     */
    public boolean isNGRequest();
    
    /** returns opponent color
     * 
     * @return 
     */
    public int getOppColor();
    
    /** user/opponent makes a step, draw step.
     * 
     * @param x
     * @param y
     * @param color
     * @return 
     */
    public boolean make_step(int x, int y, int color);
    
    /** game over, clean up panel.
     * 
     */
    public void gameOverCleanup();
    
    /** undoes a player's move
     * 
     * @param color 
     */
    public void undoMove(int color);
    
    /** rejects a player's undo
     * 
     */
    public void cantUndoMove();
    
    /** opponent sends a request
     * 
     */
    public void oppRequestUndo();
    
    /** opponent sends a request
     * 
     */
    public void oppRequestNew();
}
