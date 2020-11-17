package states;

import sharedvalues.*;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

public class GamePlay extends BasicGameState {
    private int states;
    private String turn;
    private String message;
    private Input input;
    private int[][] availableNewPositions;
    private int activeChecker;  // -2: game ended; -1: no active checker; >-1: active checker;

    public GamePlay(int states) {
        this.states = states;
    }

    @Override
    public void init(GameContainer gameContainer, StateBasedGame stateBasedGame) throws SlickException {
        turn = "Black";
        message = "Message:";
        input = gameContainer.getInput();
        activeChecker = -1;
    }

    @Override
    public void render(GameContainer gameContainer, StateBasedGame stateBasedGame, Graphics g) throws SlickException {
        g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 24), true));
        g.setColor(Color.white);
        g.drawString("Turn: "+turn, 280, 30);
        g.drawString(message, 280, 900);
        g.fillRect(280, 120, 720, 720);

        /* showing availableNewPositions */
        if(availableNewPositions != null) {
            g.setColor(Color.orange);
            for(int i=0; i<availableNewPositions.length; i++) {
                if(availableNewPositions[i][0] == -1) {
                    break;
                }
                if(GameData.blackCoordinates.length == 8) {
                    g.fillRect(280+availableNewPositions[i][1]*120, 120+availableNewPositions[i][0]*120, 120, 120);
                } else {
                    g.fillRect(280+availableNewPositions[i][1]*90, 120+availableNewPositions[i][0]*90, 90, 90);
                }
            }
            g.setColor(Color.red);
            if(GameData.blackCoordinates.length == 8) {
                if(turn.equalsIgnoreCase("Black")) {
                    g.fillRect(GameData.blackCoordinates[activeChecker][0]-10, GameData.blackCoordinates[activeChecker][1]-10, 120, 120);
                }
                if(turn.equalsIgnoreCase("White")) {
                    g.fillRect(GameData.whiteCoordinates[activeChecker][0]-10, GameData.whiteCoordinates[activeChecker][1]-10, 120, 120);
                }
            } else {
                if(turn.equalsIgnoreCase("Black")) {
                    g.fillRect(GameData.blackCoordinates[activeChecker][0]-5, GameData.blackCoordinates[activeChecker][1]-5, 90, 90);
                }
                if(turn.equalsIgnoreCase("White")) {
                    g.fillRect(GameData.whiteCoordinates[activeChecker][0]-5, GameData.whiteCoordinates[activeChecker][1]-5, 90, 90);
                }
            }
        }
        /* preparing board */
        g.setColor(Color.black);
        if(GameData.blackCoordinates.length == 8) {
            for(int i=240; i<840; i+=120) {
                g.drawLine(280, i, 1000, i);
            }
            for(int i=400; i<1000; i+=120) {
                g.drawLine(i, 120, i, 840);
            }
        } else {
            for(int i=210; i<840; i+=90) {
                g.drawLine(280, i, 1000, i);
            }
            for(int i=370; i<1000; i+=90) {
                g.drawLine(i, 120, i, 840);
            }
        }
        g.setColor(Color.gray);
        if(GameData.blackCoordinates.length == 8) {
            for(int i=170; i<890; i+=120) {
                g.drawString(""+(890-i)/120, 255, i);
                g.drawString(""+(890-i)/120, 1010, i);
            }
            for(int i=330; i<1050; i+=120) {
                g.drawString(""+(char)((i-330)/120+65), i, 85);
                g.drawString(""+(char)((i-330)/120+65), i, 845);
            }
        } else {
            for(int i=150; i<870; i+=90) {
                g.drawString(""+(870-i)/90, 255, i);
                g.drawString(""+(870-i)/90, 1010, i);
            }
            for(int i=320; i<1040; i+=90) {
                g.drawString(""+(char)((i-320)/90+65), i, 85);
                g.drawString(""+(char)((i-320)/90+65), i, 845);
            }
        }

        /* arranging checkers on board */
        g.setColor(Color.darkGray);
        for(int i=0; i<GameData.blackCoordinates.length; i++) {
            if(GameData.blackCoordinates[i][0] == -1) {
                continue;
            }
            if(GameData.blackCoordinates.length == 8) {
                g.fillOval(GameData.blackCoordinates[i][0], GameData.blackCoordinates[i][1], 100, 100);
            } else {
                g.fillOval(GameData.blackCoordinates[i][0], GameData.blackCoordinates[i][1], 80, 80);
            }
        }
        g.setColor(Color.lightGray);
        for(int i=0; i<GameData.blackCoordinates.length; i++) {
            if(GameData.whiteCoordinates[i][0] == -1) {
                continue;
            }
            if(GameData.blackCoordinates.length == 8) {
                g.fillOval(GameData.whiteCoordinates[i][0], GameData.whiteCoordinates[i][1], 100, 100);
            } else {
                g.fillOval(GameData.whiteCoordinates[i][0], GameData.whiteCoordinates[i][1], 80, 80);
            }
        }

        /* creating endgame button */
        if(activeChecker == -2) {
            g.setColor(Color.white);
            g.fillRect(1100, 860, 160, 80);
            g.setColor(Color.black);
            g.setFont(new TrueTypeFont(new java.awt.Font("Helvetica", java.awt.Font.BOLD, 48), true));
            g.drawString("Next", 1120, 870);
        }
    }

    @Override
    public void update(GameContainer gameContainer, StateBasedGame stateBasedGame, int delta) throws SlickException {
        if(activeChecker == -2) {
            if((Mouse.getX()>1100 && Mouse.getX()<1260) && (Mouse.getY()>20 && Mouse.getY()<100)) {
                if(input.isMouseButtonDown(0)) {
                    stateBasedGame.enterState(StateIDs.EXIT);
                }
            }
        }
        if(activeChecker!=-2 && !GameData.isHumanVsHuman && turn.equalsIgnoreCase("White")) {
            int[] nextMovePositionDetails = GameData.gameBoard.doAdversarialSearch();
            GameData.whiteCoordinates[nextMovePositionDetails[0]][0] = 280+nextMovePositionDetails[2]*(GameData.whiteCoordinates.length==8? 120: 90)+(GameData.whiteCoordinates.length==8? 10: 5);
            GameData.whiteCoordinates[nextMovePositionDetails[0]][1] = 120+nextMovePositionDetails[1]*(GameData.whiteCoordinates.length==8? 120: 90)+(GameData.whiteCoordinates.length==8? 10: 5);
            GameData.gameBoard.updateChecker(false, nextMovePositionDetails[0], nextMovePositionDetails[1], nextMovePositionDetails[2]);

            int opponentChecker = GameData.gameBoard.whichOpponentCheckerThere(nextMovePositionDetails[1], nextMovePositionDetails[2]);
            if(opponentChecker != -1) {
                GameData.blackCoordinates[opponentChecker][0] = -1;
                GameData.blackCoordinates[opponentChecker][1] = -1;
                GameData.gameBoard.updateChecker(true, opponentChecker, -1, -1);
            }
            GameData.gameBoard.updateGameBoard(false, nextMovePositionDetails[0]);
            turn = "Black";
            if(GameData.gameBoard.hasWon(turn.equalsIgnoreCase("White"))) {
                GameData.gameBoard.setHasBlackWon(turn.equalsIgnoreCase("White"));
                activeChecker = -2;
                message = "Message: Game Ended";
            } else {
                if(GameData.gameBoard.hasWon(turn.equalsIgnoreCase("Black"))) {
                    GameData.gameBoard.setHasBlackWon(turn.equalsIgnoreCase("Black"));
                    activeChecker = -2;
                    message = "Message: Game Ended";
                }
            }
            availableNewPositions = null;
            activeChecker = (activeChecker==-2? -2: -1);
            return ;  // NOTICE: instead of break;
        }
        if(activeChecker!=-2 && availableNewPositions != null) {
            for(int i=0; i<availableNewPositions.length; i++) {
                if(availableNewPositions[i][0] == -1) {
                    break;
                }
                if((Mouse.getX()>280+availableNewPositions[i][1]*(GameData.blackCoordinates.length==8? 120: 90) && Mouse.getX()<280+(availableNewPositions[i][1]+1)*(GameData.blackCoordinates.length==8? 120: 90)) && (Mouse.getY()<960-(120+availableNewPositions[i][0]*(GameData.blackCoordinates.length==8? 120: 90)) && Mouse.getY()>960-(120+(availableNewPositions[i][0]+1)*(GameData.blackCoordinates.length==8? 120: 90)))) {
                    if(input.isMouseButtonDown(0)) {
                        if(turn.equalsIgnoreCase("Black")) {
                            GameData.blackCoordinates[activeChecker][0] = 280+availableNewPositions[i][1]*(GameData.blackCoordinates.length==8? 120: 90)+(GameData.blackCoordinates.length==8? 10: 5);
                            GameData.blackCoordinates[activeChecker][1] = 120+availableNewPositions[i][0]*(GameData.blackCoordinates.length==8? 120: 90)+(GameData.blackCoordinates.length==8? 10: 5);
                            GameData.gameBoard.updateChecker(true, activeChecker, availableNewPositions[i][0], availableNewPositions[i][1]);

                            int opponentChecker = GameData.gameBoard.whichOpponentCheckerThere(availableNewPositions[i][0], availableNewPositions[i][1]);
                            if(opponentChecker != -1) {
                                GameData.whiteCoordinates[opponentChecker][0] = -1;
                                GameData.whiteCoordinates[opponentChecker][1] = -1;
                                GameData.gameBoard.updateChecker(false, opponentChecker, -1, -1);
                            }
                            GameData.gameBoard.updateGameBoard(true, activeChecker);
                            turn = "White";
                        } else if(turn.equalsIgnoreCase("White")) {
                            GameData.whiteCoordinates[activeChecker][0] = 280+availableNewPositions[i][1]*(GameData.whiteCoordinates.length==8? 120: 90)+(GameData.whiteCoordinates.length==8? 10: 5);
                            GameData.whiteCoordinates[activeChecker][1] = 120+availableNewPositions[i][0]*(GameData.whiteCoordinates.length==8? 120: 90)+(GameData.whiteCoordinates.length==8? 10: 5);
                            GameData.gameBoard.updateChecker(false, activeChecker, availableNewPositions[i][0], availableNewPositions[i][1]);

                            int opponentChecker = GameData.gameBoard.whichOpponentCheckerThere(availableNewPositions[i][0], availableNewPositions[i][1]);
                            if(opponentChecker != -1) {
                                GameData.blackCoordinates[opponentChecker][0] = -1;
                                GameData.blackCoordinates[opponentChecker][1] = -1;
                                GameData.gameBoard.updateChecker(true, opponentChecker, -1, -1);
                            }
                            GameData.gameBoard.updateGameBoard(false, activeChecker);
                            turn = "Black";
                        }

                        if(GameData.gameBoard.hasWon(turn.equalsIgnoreCase("White"))) {
                            GameData.gameBoard.setHasBlackWon(turn.equalsIgnoreCase("White"));
                            activeChecker = -2;
                            message = "Message: Game Ended";
                        } else {
                            if(GameData.gameBoard.hasWon(turn.equalsIgnoreCase("Black"))) {
                                GameData.gameBoard.setHasBlackWon(turn.equalsIgnoreCase("Black"));
                                activeChecker = -2;
                                message = "Message: Game Ended";
                            }
                        }
                        availableNewPositions = null;
                        activeChecker = (activeChecker==-2? -2: -1);
                        return ;  // NOTICE: instead of break;
                    }
                }
            }
        }
        if(activeChecker!=-2 && turn.equalsIgnoreCase("Black")) {
            for(int i=0; i<GameData.blackCoordinates.length; i++) {
                if((Mouse.getX()>GameData.blackCoordinates[i][0] && Mouse.getX()<GameData.blackCoordinates[i][0]+(GameData.blackCoordinates.length==8? 100: 80)) && (Mouse.getY()<960-GameData.blackCoordinates[i][1] && Mouse.getY()>960-GameData.blackCoordinates[i][1]-(GameData.blackCoordinates.length==8? 100: 80))) {
                    if(input.isMouseButtonDown(0)) {
                        availableNewPositions = GameData.gameBoard.makeMove(true, i);
                        activeChecker = i;
                        if(availableNewPositions[0][0] == -1) {
                            availableNewPositions = null;
                            activeChecker = -1;
                            message = "Message: No Valid Move Available";
                        } else {
                            message = "Message:";
                        }
                        break;
                    }
                }
            }
        }
        if(activeChecker!=-2 && GameData.isHumanVsHuman && turn.equalsIgnoreCase("White")) {
            for(int i=0; i<GameData.whiteCoordinates.length; i++) {
                if((Mouse.getX()>GameData.whiteCoordinates[i][0] && Mouse.getX()<GameData.whiteCoordinates[i][0]+(GameData.blackCoordinates.length==8? 100: 80)) && (Mouse.getY()<960-GameData.whiteCoordinates[i][1] && Mouse.getY()>960-GameData.whiteCoordinates[i][1]-(GameData.blackCoordinates.length==8? 100: 80))) {
                    if(input.isMouseButtonDown(0)) {
                        availableNewPositions = GameData.gameBoard.makeMove(false, i);
                        activeChecker = i;
                        if(availableNewPositions[0][0] == -1) {
                            availableNewPositions = null;
                            activeChecker = -1;
                            message = "Message: No Valid Move Available";
                        } else {
                            message = "Message:";
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public int getID() {
        return states;
    }
}
