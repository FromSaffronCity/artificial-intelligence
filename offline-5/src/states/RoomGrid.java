package states;

import utilities.RoomMap;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class RoomGrid extends BasicGameState {
    private int states;
    private Input input;
    private RoomMap roomMap;
    private int timeTicks;
    private String hitOrMiss;
    private boolean isCatchDown;
    private boolean isGhostFound;
    private int clickedCellX;
    private int clickedCellY;
    private int clickedCellColor;

    public RoomGrid(int states) {
        this.states = states;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        input = gameContainer.getInput();
        roomMap = new RoomMap(9);
        timeTicks = 0;
        hitOrMiss = "Off";
        isCatchDown = isGhostFound = false;
        clickedCellX = clickedCellY = clickedCellColor = -1;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 48), true));

        /* preparing game buttons and messages */
        g.setColor(Color.white);
        g.fillRect(985, 240, 180, 80);
        if(isCatchDown) {
            g.setColor(Color.lightGray);
        }
        g.fillRect(985, 360, 180, 80);

        g.setColor(Color.black);
        g.drawString("Tick", 1025, 250);
        g.drawString("Catch", 1005, 370);

        g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24), true));
        g.setColor(Color.white);
        g.drawRect(970, 600, 210, 155);
        g.drawString("Time: "+timeTicks, 990, 620);
        g.drawString("Hit/Miss: "+hitOrMiss, 990, 700);

        /* preparing roomgrid */
        g.setColor(Color.white);
        g.fillRect(60, 60, 810, 810);

        /* shading each cell according to its corresponding ghostLocationProbability */
        for(int i=0; i<roomMap.getDimensionRow(); i++) {
            for(int j=0; j<roomMap.getDimensionCol(); j++) {
                if(i==clickedCellX && j==clickedCellY) {
                    g.setColor((clickedCellColor==0? new Color(255, 51, 51): (clickedCellColor==1? new Color(255, 153, 51): new Color(102, 204, 0))));
                    g.fillRect(60+j*90, 60+i*90, 90, 90);
                } else if(roomMap.getCellProbability(i, j)*100>0 && roomMap.getCellProbability(i, j)*100<=1) {
                    g.setColor(new Color(224, 224,224));
                    g.fillRect(60+j*90, 60+i*90, 90, 90);
                } else if(roomMap.getCellProbability(i, j)*100>1 && roomMap.getCellProbability(i, j)*100<=5) {
                    g.setColor(new Color(192, 192,192));
                    g.fillRect(60+j*90, 60+i*90, 90, 90);
                } else if(roomMap.getCellProbability(i, j)*100>5 && roomMap.getCellProbability(i, j)*100<=20) {
                    g.setColor(new Color(160, 160,160));
                    g.fillRect(60+j*90, 60+i*90, 90, 90);
                } else if(roomMap.getCellProbability(i, j)*100>20 && roomMap.getCellProbability(i, j)*100<=50) {
                    g.setColor(new Color(128, 128,128));
                    g.fillRect(60+j*90, 60+i*90, 90, 90);
                } else if(roomMap.getCellProbability(i, j)*100>50) {
                    g.setColor(new Color(64, 64,64));
                    g.fillRect(60+j*90, 60+i*90, 90, 90);
                }
            }
        }

        /* drawing roomgrid lines */
        g.setColor(Color.black);
        for(int y=150; y<870; y+=90) {
            g.drawLine(60, y, 870, y);
        }
        for(int x=150; x<870; x+=90) {
            g.drawLine(x, 60, x, 870);
        }

        /* printing ghostLocationProbability (in percentage) of each cell */
        for(int i=0; i<roomMap.getDimensionRow(); i++) {
            for(int j=0; j<roomMap.getDimensionCol(); j++) {
                g.drawString(String.format("%.2f", roomMap.getCellProbability(i, j)*100), 70+j*90, 90+i*90);
            }
        }

        /* preparing back button */
        if(isGhostFound) {
            g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 48), true));
            g.setColor(Color.white);
            g.fillRect(1120, 860, 140, 80);
            g.setColor(Color.black);
            g.drawString("Exit", 1140, 870);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if((Mouse.getX()>985 && Mouse.getX()<1165) && (Mouse.getY()>640 && Mouse.getY()<720)) {
            if(!isGhostFound && input.isMouseButtonDown(0)) {
                timeTicks++;
                roomMap.updateWithTransition();
                
                hitOrMiss = "Off";
                isCatchDown = false;
                clickedCellX = clickedCellY = clickedCellColor = -1;
            }
        } else if((Mouse.getX()>985 && Mouse.getX()<1165) && (Mouse.getY()>520 && Mouse.getY()<600)) {
            if(!isGhostFound && input.isMouseButtonDown(0)) {
                isCatchDown = !isCatchDown;
                if(isCatchDown) {
                    hitOrMiss = "On";
                } else {
                    hitOrMiss = "Off";
                }
                clickedCellX = clickedCellY = clickedCellColor = -1;
            }
        } else if((Mouse.getX()>60 && Mouse.getX()<870) && (Mouse.getY()>90 && Mouse.getY()<900)) {
            for(int row=0; row<9; row++) {
                for(int col=0; col<9; col++) {
                    if(!isGhostFound && (Mouse.getX()>60+col*90 && Mouse.getX()<60+90+col*90) && (Mouse.getY()>900-90-row*90 && Mouse.getY()<900-row*90) && input.isMouseButtonDown(0)) {
                        if(isCatchDown) {
                            isGhostFound = roomMap.isGhostHere(row, col);
                            if(isGhostFound) {
                                roomMap.updateBecauseGhostFound(row, col);
                                hitOrMiss = "Hit!";
                            } else {
                                roomMap.updateBecauseMiss(row, col);
                                hitOrMiss = "Miss";
                            }
                        } else {
                            clickedCellX = row;
                            clickedCellY = col;
                            clickedCellColor = roomMap.updateWithObservation(row, col);
                        }
                    }
                }
            }
        } else if((Mouse.getX()>1120 && Mouse.getX()<1260) && (Mouse.getY()>20 && Mouse.getY()<100)) {
            if(isGhostFound && input.isMouseButtonDown(0)) {
                gameContainer.exit();
            }
        }
    }

    @Override
    public int getID() {
        return states;
    }
}
