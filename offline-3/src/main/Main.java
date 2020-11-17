package main;

import sharedvalues.StateIDs;
import states.*;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends StateBasedGame {
    private static final String GAMENAME = "Lines of Action";

    private Main(String gameName) {
        super(gameName);
        this.addState(new MainMenu(StateIDs.MAINMENU));
        this.addState(new PlayerSelection(StateIDs.PLAYERSELECTION));
        this.addState(new BoardSelection(StateIDs.BOARDSELECTION));
        this.addState(new GamePlay(StateIDs.GAMEPLAY));
        this.addState(new Exit(StateIDs.EXIT));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(StateIDs.MAINMENU).init(gameContainer, this);
        this.getState(StateIDs.PLAYERSELECTION).init(gameContainer, this);
        this.getState(StateIDs.BOARDSELECTION).init(gameContainer, this);
        this.getState(StateIDs.GAMEPLAY).init(gameContainer, this);
        this.getState(StateIDs.EXIT).init(gameContainer, this);
        this.enterState(StateIDs.MAINMENU);
    }

    public static void main(String[] args) {
        AppGameContainer appGameContainer;
        try {
            appGameContainer = new AppGameContainer(new Main(GAMENAME));
            appGameContainer.setDisplayMode(1280, 960, false);
            appGameContainer.setShowFPS(false);
            appGameContainer.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }
}
