package states;

import sharedvalues.StateIDs;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class MainMenu extends BasicGameState {
    private int states;
    private Input input;

    public MainMenu(int states) {
        this.states = states;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        input = gameContainer.getInput();
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 48), true));

        g.setColor(Color.white);
        g.drawString("Catching the Ghost", 420, 240);
        g.fillRect(550, 400, 180, 80);
        g.fillRect(550, 520, 180, 80);

        g.setColor(Color.black);
        g.drawString("Play", 590, 410);
        g.drawString("Exit", 590, 530);
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if((Mouse.getX()>550 && Mouse.getX()<730) && (Mouse.getY()>480 && Mouse.getY()<560)) {
            if(input.isMouseButtonDown(0)) {
                stateBasedGame.enterState(StateIDs.ROOMGRID);
            }
        } else if((Mouse.getX()>550 && Mouse.getX()<730) && (Mouse.getY()>360 && Mouse.getY()<440)) {
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
