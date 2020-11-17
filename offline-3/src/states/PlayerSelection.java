package states;

import sharedvalues.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class PlayerSelection extends BasicGameState {
    private int states;
    private Input input;
    private boolean hasLoadedJustNow;
    private int mouseInputDelayCount;

    public PlayerSelection(int states) {
        this.states = states;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        input = gameContainer.getInput();
        hasLoadedJustNow = false;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 48), true));
        g.setColor(Color.white);
        g.drawString("Select Player Option", 400, 240);
        g.fillRect(410, 400, 440, 80);
        g.fillRect(410, 520, 440, 80);
        g.fillRect(20, 860, 160, 80);

        g.setColor(Color.black);
        g.drawString("Human vs Human", 430, 410);
        g.drawString("Human vs AI", 480, 530);
        g.drawString("Back", 40, 870);
        if(!hasLoadedJustNow) {
            hasLoadedJustNow = true;
            mouseInputDelayCount = 0;
        }
        mouseInputDelayCount++;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if((Mouse.getX()>410 && Mouse.getX()<850) && (Mouse.getY()>480 && Mouse.getY()<560)) {
            if(input.isMouseButtonDown(0) && mouseInputDelayCount>10) {
                if(!GameData.isHumanVsHuman) {
                    GameData.isHumanVsHuman = true;
                }
                hasLoadedJustNow = false;
                stateBasedGame.enterState(StateIDs.BOARDSELECTION);
            }
        }
        if((Mouse.getX()>410 && Mouse.getX()<850) && (Mouse.getY()>360 && Mouse.getY()<440)) {
            if(input.isMouseButtonDown(0) && mouseInputDelayCount>10) {
                if(GameData.isHumanVsHuman) {
                    GameData.isHumanVsHuman = false;
                }
                hasLoadedJustNow = false;
                stateBasedGame.enterState(StateIDs.BOARDSELECTION);
            }
        }
        if((Mouse.getX()>20 && Mouse.getX()<180) && (Mouse.getY()>20 && Mouse.getY()<100)) {
            if(input.isMouseButtonDown(0) && mouseInputDelayCount>10) {
                if(GameData.isHumanVsHuman) {
                    GameData.isHumanVsHuman = false;
                }
                hasLoadedJustNow = false;
                stateBasedGame.enterState(StateIDs.MAINMENU);
            }
        }
        mouseInputDelayCount++;
    }

    @Override
    public int getID() {
        return states;
    }
}
