package states;

import sharedvalues.GameData;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class Exit extends BasicGameState {
    private int states;
    private Image image;
    private Input input;

    public Exit(int states) {
        this.states = states;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        image = new Image("res/michael-scott-ed-truck.jpg");
        input = gameContainer.getInput();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 48), true));
        g.setColor(Color.white);
        g.drawString((GameData.gameBoard.getHasBlackWon()? "Black": "White")+" Won!", 520, 120);
        g.drawImage(image, 300, 200);
        g.fillRect(560, 840, 160, 80);

        g.setColor(Color.black);
        g.drawString("Exit", 590, 850);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if((Mouse.getX()>560 && Mouse.getX()<720) && (Mouse.getY()>40 && Mouse.getY()<120)) {
            if(input.isMouseButtonDown(0)) {
                gameContainer.exit();
            }
        }
    }

    @Override
    public int getID() {
        return states;
    }
}
