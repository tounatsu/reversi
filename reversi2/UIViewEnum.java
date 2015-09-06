package reversi2;


public enum UIViewEnum {
    HELLO, LOGIN, SIGNUP, INSTRUCTIONS, MENU, PROFILE, SINGLEPLAYER, 
    MULTIPLAYER, SETTINGS, NULL, WAITING, CONNECT;
    
    @Override
    public String toString(){
        return this.name();
    }
}
