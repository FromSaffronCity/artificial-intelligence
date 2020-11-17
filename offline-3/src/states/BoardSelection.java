package states;

import equipments.GameBoard;
import sharedvalues.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class BoardSelection extends BasicGameState {
    private int states;
    private Input input;
    private boolean hasLoadedJustNow;
    private int mouseInputDelayCount;

    public BoardSelection(int states) {
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
        g.drawString("Select Board Option", 400, 240);
        g.fillRect(480, 400, 280, 80);
        g.fillRect(480, 520, 280, 80);
        g.fillRect(20, 860, 160, 80);

        g.setColor(Color.black);
        g.drawString("6x6 Board", 500, 410);
        g.drawString("8x8 Board", 500, 530);
        g.drawString("Back", 40, 870);
        if(!hasLoadedJustNow) {
            hasLoadedJustNow = true;
            mouseInputDelayCount = 0;
        }
        mouseInputDelayCount++;
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if((Mouse.getX()>480 && Mouse.getX()<760) && (Mouse.getY()>480 && Mouse.getY()<560)) {
            if(input.isMouseButtonDown(0) && mouseInputDelayCount>10) {
                GameData.blackCoordinates = new int[8][2];
                GameData.whiteCoordinates = new int[8][2];
                for(int i=0; i<GameData.blackCoordinates.length; i++) {
                    GameData.blackCoordinates[i][0] = 410+(i%4)*120;
                    GameData.blackCoordinates[i][1] = (i<4? 130: 730);
                    GameData.whiteCoordinates[i][0] = (i<4? 290: 890);
                    GameData.whiteCoordinates[i][1] = 250+(i%4)*120;
                }
                GameData.gameBoard = new GameBoard(6);
                hasLoadedJustNow = false;
                stateBasedGame.enterState(StateIDs.GAMEPLAY);
            }
        }
        if((Mouse.getX()>480 && Mouse.getX()<760) && (Mouse.getY()>360 && Mouse.getY()<440)) {
            if(input.isMouseButtonDown(0) && mouseInputDelayCount>10) {
                GameData.blackCoordinates = new int[12][2];
                GameData.whiteCoordinates = new int[12][2];
                for(int i=0; i<GameData.blackCoordinates.length; i++) {
                    GameData.blackCoordinates[i][0] = 375+(i%6)*90;
                    GameData.blackCoordinates[i][1] = (i<6? 125: 755);
                    GameData.whiteCoordinates[i][0] = (i<6? 285: 915);
                    GameData.whiteCoordinates[i][1] = 215+(i%6)*90;
                }
                GameData.gameBoard = new GameBoard(8);
                hasLoadedJustNow = false;
                stateBasedGame.enterState(StateIDs.GAMEPLAY);
            }
        }
        if((Mouse.getX()>20 && Mouse.getX()<180) && (Mouse.getY()>20 && Mouse.getY()<100)) {
            if(input.isMouseButtonDown(0) && mouseInputDelayCount>10) {
                hasLoadedJustNow = false;
                stateBasedGame.enterState(StateIDs.PLAYERSELECTION);
            }
        }
        mouseInputDelayCount++;
    }

    @Override
    public int getID() {
        return states;
    }
}
