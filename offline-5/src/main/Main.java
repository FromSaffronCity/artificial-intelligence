package main;

import sharedvalues.StateIDs;
import states.*;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Main extends StateBasedGame {
    private static final String NAME = "Catching the Ghost";

    private Main(String name) {
        super(name);
        this.addState(new MainMenu(StateIDs.MAINMENU));
        this.addState(new RoomGrid(StateIDs.ROOMGRID));
    }

    @Override
    public void initStatesList(GameContainer gameContainer) throws SlickException {
        this.getState(StateIDs.MAINMENU).init(gameContainer, this);
        this.getState(StateIDs.ROOMGRID).init(gameContainer, this);
        this.enterState(StateIDs.MAINMENU);
    }

    public static void main(String[] args) {
        AppGameContainer appGameContainer;
        try {
            appGameContainer = new AppGameContainer(new Main(NAME));
            appGameContainer.setDisplayMode(1280, 960, false);
            appGameContainer.setShowFPS(false);
            appGameContainer.start();
        } catch(SlickException e) {
            e.printStackTrace();
        }
    }
}
