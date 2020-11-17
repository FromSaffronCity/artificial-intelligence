package equipments;

public class GameBoard {
    private boolean hasBlackWon;  // false: white won; true: black won;
    private Checker[] blackCheckers;
    private Checker[] whiteCheckers;
    private Checker[][] gameBoard;

    public GameBoard(int dimension) {
        blackCheckers = new Checker[2*dimension-4];
        whiteCheckers = new Checker[2*dimension-4];
        gameBoard = new Checker[dimension][dimension];

        for(int i=0; i<blackCheckers.length; i++) {
            blackCheckers[i] = new Checker(true, i, (i<blackCheckers.length/2? 0: dimension-1), (i%(blackCheckers.length/2))+1);
        }
        for(int i=0; i<whiteCheckers.length; i++) {
            whiteCheckers[i] = new Checker(false, i, (i%(whiteCheckers.length/2))+1, (i<whiteCheckers.length/2? 0: dimension-1));
        }
        for(int i=0; i<gameBoard.length; i++) {
            for(int j=0; j<gameBoard[0].length; j++) {
                if((i==0 || i==gameBoard.length-1) && (j>0 && j<gameBoard[0].length-1)) {
                    /* for black checkers */
                    gameBoard[i][j] = blackCheckers[(i==0? j-1: i+j-2)];
                } else if((i>0 && i<gameBoard.length-1) && (j==0 || j==gameBoard[0].length-1)) {
                    /* for white checkers */
                    gameBoard[i][j] = whiteCheckers[(j==0? i-1: j+i-2)];
                } else {
                    /* for blank cells */
                    gameBoard[i][j] = null;
                }
            }
        }
    }

    public void setHasBlackWon(boolean hasBlackWon) {
        this.hasBlackWon = hasBlackWon;
    }

    public boolean getHasBlackWon() {
        return hasBlackWon;
    }

    public void updateChecker(boolean isBlack, int checker, int cellX, int cellY) {
        if(isBlack) {
            gameBoard[blackCheckers[checker].getCellX()][blackCheckers[checker].getCellY()] = null;
            blackCheckers[checker].setCellX(cellX);
            blackCheckers[checker].setCellY(cellY);
        } else {
            gameBoard[whiteCheckers[checker].getCellX()][whiteCheckers[checker].getCellY()] = null;
            whiteCheckers[checker].setCellX(cellX);
            whiteCheckers[checker].setCellY(cellY);
        }
    }

    public void updateGameBoard(boolean isBlack, int checker) {
        if(isBlack) {
            gameBoard[blackCheckers[checker].getCellX()][blackCheckers[checker].getCellY()] = blackCheckers[checker];
        } else {
            gameBoard[whiteCheckers[checker].getCellX()][whiteCheckers[checker].getCellY()] = whiteCheckers[checker];
        }
    }

    public int[][] makeMove(boolean isBlack, int checker) {
        int cellX=(isBlack? blackCheckers[checker].getCellX(): whiteCheckers[checker].getCellX()), cellY=(isBlack? blackCheckers[checker].getCellY(): whiteCheckers[checker].getCellY()), totalCheckers, index=0;
        int[][] availableNewPositions = new int[8][2];
        for(int i=0; i<availableNewPositions.length; i++) {
            availableNewPositions[i][0] = availableNewPositions[i][1] = -1;
        }
        /* vertical movements */
        totalCheckers = 0;
        for(int i=0; i<gameBoard.length; i++) {
            if(gameBoard[i][cellY] != null) {
                totalCheckers++;
            }
        }
        /* upward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX-i < 0) {
                break;
            }
            if(gameBoard[cellX-i][cellY] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX-i][cellY].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX-i][cellY].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX-totalCheckers;
                availableNewPositions[index++][1] = cellY;
            }
        }
        /* downward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX+i == gameBoard.length) {
                break;
            }
            if(gameBoard[cellX+i][cellY] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX+i][cellY].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX+i][cellY].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX+totalCheckers;
                availableNewPositions[index++][1] = cellY;
            }
        }
        /* horizontal movements */
        totalCheckers = 0;
        for(int i=0; i<gameBoard[0].length; i++) {
            if(gameBoard[cellX][i] != null) {
                totalCheckers++;
            }
        }
        /* leftward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellY-i < 0) {
                break;
            }
            if(gameBoard[cellX][cellY-i] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX][cellY-i].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX][cellY-i].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX;
                availableNewPositions[index++][1] = cellY-totalCheckers;
            }
        }
        /* rightward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellY+i == gameBoard[0].length) {
                break;
            }
            if(gameBoard[cellX][cellY+i] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX][cellY+i].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX][cellY+i].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX;
                availableNewPositions[index++][1] = cellY+totalCheckers;
            }
        }
        /* positive diagonal movements */
        totalCheckers = 1;
        for(int i=1; i<=gameBoard.length; i++) {
            if(cellX-i<0 || cellY+i==gameBoard[0].length) {
                break;
            }
            if(gameBoard[cellX-i][cellY+i] != null) {
                totalCheckers++;
            }
        }
        for(int i=1; i<=gameBoard.length; i++) {
            if(cellX+i==gameBoard.length || cellY-i<0) {
                break;
            }
            if(gameBoard[cellX+i][cellY-i] != null) {
                totalCheckers++;
            }
        }
        /* up+rightward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX-i<0 || cellY+i==gameBoard[0].length) {
                break;
            }
            if(gameBoard[cellX-i][cellY+i] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX-i][cellY+i].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX-i][cellY+i].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX-totalCheckers;
                availableNewPositions[index++][1] = cellY+totalCheckers;
            }
        }
        /* down+leftward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX+i==gameBoard.length || cellY-i<0) {
                break;
            }
            if(gameBoard[cellX+i][cellY-i] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX+i][cellY-i].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX+i][cellY-i].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX+totalCheckers;
                availableNewPositions[index++][1] = cellY-totalCheckers;
            }
        }
        /* negative diagonal movements */
        totalCheckers = 1;
        for(int i=1; i<=gameBoard.length; i++) {
            if(cellX-i<0 || cellY-i<0) {
                break;
            }
            if(gameBoard[cellX-i][cellY-i] != null) {
                totalCheckers++;
            }
        }
        for(int i=1; i<=gameBoard.length; i++) {
            if(cellX+i==gameBoard.length || cellY+i==gameBoard[0].length) {
                break;
            }
            if(gameBoard[cellX+i][cellY+i] != null) {
                totalCheckers++;
            }
        }
        /* up+leftward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX-i<0 || cellY-i<0) {
                break;
            }
            if(gameBoard[cellX-i][cellY-i] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX-i][cellY-i].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX-i][cellY-i].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX-totalCheckers;
                availableNewPositions[index++][1] = cellY-totalCheckers;
            }
        }
        /* down+rightward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX+i==gameBoard.length || cellY+i==gameBoard[0].length) {
                break;
            }
            if(gameBoard[cellX+i][cellY+i] != null) {
                if(i==totalCheckers && isBlack==gameBoard[cellX+i][cellY+i].isBlack()) {
                    break;
                }
                if(i!=totalCheckers && isBlack!=gameBoard[cellX+i][cellY+i].isBlack()) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX+totalCheckers;
                availableNewPositions[index++][1] = cellY+totalCheckers;
            }
        }
        return availableNewPositions;
    }

    public int whichOpponentCheckerThere(int cellX, int cellY) {
        int opponentChecker = -1;
        if(gameBoard[cellX][cellY] != null) {
            opponentChecker = gameBoard[cellX][cellY].getChecker();
        }
        return opponentChecker;
    }

    private void doDepthFirstSearch(boolean isBlack, int checker, int[] dfsStatus) {
        dfsStatus[checker] = 1;
        int cellX=(isBlack? blackCheckers[checker].getCellX(): whiteCheckers[checker].getCellX()), cellY=(isBlack? blackCheckers[checker].getCellY(): whiteCheckers[checker].getCellY());

        if((cellX-1>-1 && cellY-1>-1) && gameBoard[cellX-1][cellY-1]!=null && isBlack==gameBoard[cellX-1][cellY-1].isBlack() && dfsStatus[gameBoard[cellX-1][cellY-1].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX-1][cellY-1].getChecker(), dfsStatus);
        }
        if(cellX-1>-1 && gameBoard[cellX-1][cellY]!=null && isBlack==gameBoard[cellX-1][cellY].isBlack() && dfsStatus[gameBoard[cellX-1][cellY].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX-1][cellY].getChecker(), dfsStatus);
        }
        if((cellX-1>-1 && cellY+1<gameBoard[0].length) && gameBoard[cellX-1][cellY+1]!=null && isBlack==gameBoard[cellX-1][cellY+1].isBlack() && dfsStatus[gameBoard[cellX-1][cellY+1].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX-1][cellY+1].getChecker(), dfsStatus);
        }
        if(cellY+1<gameBoard[0].length && gameBoard[cellX][cellY+1]!=null && isBlack==gameBoard[cellX][cellY+1].isBlack() && dfsStatus[gameBoard[cellX][cellY+1].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX][cellY+1].getChecker(), dfsStatus);
        }
        if((cellX+1<gameBoard.length && cellY+1<gameBoard[0].length) && gameBoard[cellX+1][cellY+1]!=null && isBlack==gameBoard[cellX+1][cellY+1].isBlack() && dfsStatus[gameBoard[cellX+1][cellY+1].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX+1][cellY+1].getChecker(), dfsStatus);
        }
        if(cellX+1<gameBoard.length && gameBoard[cellX+1][cellY]!=null && isBlack==gameBoard[cellX+1][cellY].isBlack() && dfsStatus[gameBoard[cellX+1][cellY].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX+1][cellY].getChecker(), dfsStatus);
        }
        if((cellX+1<gameBoard.length && cellY-1>-1) && gameBoard[cellX+1][cellY-1]!=null && isBlack==gameBoard[cellX+1][cellY-1].isBlack() && dfsStatus[gameBoard[cellX+1][cellY-1].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX+1][cellY-1].getChecker(), dfsStatus);
        }
        if(cellY-1>-1 && gameBoard[cellX][cellY-1]!=null && isBlack==gameBoard[cellX][cellY-1].isBlack() && dfsStatus[gameBoard[cellX][cellY-1].getChecker()]==0) {
            doDepthFirstSearch(isBlack, gameBoard[cellX][cellY-1].getChecker(), dfsStatus);
        }
        dfsStatus[checker] = 2;
    }

    public boolean hasWon(boolean isBlack) {
        int connectedComponents = 0;
        int[] dfsStatus = new int[blackCheckers.length];
        for(int i=0; i<dfsStatus.length; i++) {
            dfsStatus[i] = 0;  // 0: unvisited; 1: visited; 2: explored;
        }
        if(isBlack) {
            for(int i=0; i<blackCheckers.length; i++) {
                if(blackCheckers[i].getCellX()!=-1 && dfsStatus[i]==0) {
                    connectedComponents++;
                    doDepthFirstSearch(true, i, dfsStatus);
                }
            }
        } else {
            for(int i=0; i<whiteCheckers.length; i++) {
                if(whiteCheckers[i].getCellX()!=-1 && dfsStatus[i]==0) {
                    connectedComponents++;
                    doDepthFirstSearch(false, i, dfsStatus);
                }
            }
        }
        return (connectedComponents == 1);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");
        for(int i=0; i<gameBoard.length; i++) {
            for(int j=0; j<gameBoard[0].length; j++) {
                if(gameBoard[i][j] == null) {
                    stringBuilder.append("-");
                } else {
                    stringBuilder.append(gameBoard[i][j].isBlack()? "B": "W");
                }
                stringBuilder.append((j<gameBoard[0].length-1? " ": ""));
            }
            stringBuilder.append((i<gameBoard.length-1? "\n": ""));
        }
        return stringBuilder.toString();
    }

    private int[][] makeMoveLite(int cellX, int cellY, int[][] gameBoardLite) {
        int totalCheckers, index=0;
        int[][] availableNewPositions = new int[8][2];
        for(int i=0; i<availableNewPositions.length; i++) {
            availableNewPositions[i][0] = availableNewPositions[i][1] = -1;
        }
        /* vertical movements */
        totalCheckers = 0;
        for(int i=0; i<gameBoardLite.length; i++) {
            if(gameBoardLite[i][cellY] != 0) {
                totalCheckers++;
            }
        }
        /* upward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX-i < 0) {
                break;
            }
            if(gameBoardLite[cellX-i][cellY] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX-i][cellY]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX-i][cellY]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX-totalCheckers;
                availableNewPositions[index++][1] = cellY;
            }
        }
        /* downward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX+i == gameBoardLite.length) {
                break;
            }
            if(gameBoardLite[cellX+i][cellY] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX+i][cellY]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX+i][cellY]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX+totalCheckers;
                availableNewPositions[index++][1] = cellY;
            }
        }
        /* horizontal movements */
        totalCheckers = 0;
        for(int i=0; i<gameBoardLite[0].length; i++) {
            if(gameBoardLite[cellX][i] != 0) {
                totalCheckers++;
            }
        }
        /* leftward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellY-i < 0) {
                break;
            }
            if(gameBoardLite[cellX][cellY-i] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX][cellY-i]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX][cellY-i]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX;
                availableNewPositions[index++][1] = cellY-totalCheckers;
            }
        }
        /* rightward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellY+i == gameBoardLite[0].length) {
                break;
            }
            if(gameBoardLite[cellX][cellY+i] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX][cellY+i]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX][cellY+i]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX;
                availableNewPositions[index++][1] = cellY+totalCheckers;
            }
        }
        /* positive diagonal movements */
        totalCheckers = 1;
        for(int i=1; i<=gameBoardLite.length; i++) {
            if(cellX-i<0 || cellY+i==gameBoardLite[0].length) {
                break;
            }
            if(gameBoardLite[cellX-i][cellY+i] != 0) {
                totalCheckers++;
            }
        }
        for(int i=1; i<=gameBoardLite.length; i++) {
            if(cellX+i==gameBoardLite.length || cellY-i<0) {
                break;
            }
            if(gameBoardLite[cellX+i][cellY-i] != 0) {
                totalCheckers++;
            }
        }
        /* up+rightward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX-i<0 || cellY+i==gameBoardLite[0].length) {
                break;
            }
            if(gameBoardLite[cellX-i][cellY+i] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX-i][cellY+i]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX-i][cellY+i]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX-totalCheckers;
                availableNewPositions[index++][1] = cellY+totalCheckers;
            }
        }
        /* down+leftward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX+i==gameBoardLite.length || cellY-i<0) {
                break;
            }
            if(gameBoardLite[cellX+i][cellY-i] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX+i][cellY-i]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX+i][cellY-i]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX+totalCheckers;
                availableNewPositions[index++][1] = cellY-totalCheckers;
            }
        }
        /* negative diagonal movements */
        totalCheckers = 1;
        for(int i=1; i<=gameBoardLite.length; i++) {
            if(cellX-i<0 || cellY-i<0) {
                break;
            }
            if(gameBoardLite[cellX-i][cellY-i] != 0) {
                totalCheckers++;
            }
        }
        for(int i=1; i<=gameBoardLite.length; i++) {
            if(cellX+i==gameBoardLite.length || cellY+i==gameBoardLite[0].length) {
                break;
            }
            if(gameBoardLite[cellX+i][cellY+i] != 0) {
                totalCheckers++;
            }
        }
        /* up+leftward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX-i<0 || cellY-i<0) {
                break;
            }
            if(gameBoardLite[cellX-i][cellY-i] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX-i][cellY-i]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX-i][cellY-i]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX-totalCheckers;
                availableNewPositions[index++][1] = cellY-totalCheckers;
            }
        }
        /* down+rightward movement */
        for(int i=1; i<=totalCheckers; i++) {
            if(cellX+i==gameBoardLite.length || cellY+i==gameBoardLite[0].length) {
                break;
            }
            if(gameBoardLite[cellX+i][cellY+i] != 0) {
                if(i==totalCheckers && gameBoardLite[cellX][cellY]==gameBoardLite[cellX+i][cellY+i]) {
                    break;
                }
                if(i!=totalCheckers && gameBoardLite[cellX][cellY]!=gameBoardLite[cellX+i][cellY+i]) {
                    break;
                }
            }
            if(i == totalCheckers) {
                availableNewPositions[index][0] = cellX+totalCheckers;
                availableNewPositions[index++][1] = cellY+totalCheckers;
            }
        }
        return availableNewPositions;
    }

    private int[][] getAdjacentCheckersPositions(int cellX, int cellY, int[][] gameBoardLite) {
        int index = 0;
        int[][] adjacentCheckersPositions = new int[8][2];
        for(int i=0; i<adjacentCheckersPositions.length; i++) {
            adjacentCheckersPositions[i][0] = adjacentCheckersPositions[i][1] = -1;
        }
        if((cellX-1>-1 && cellY-1>-1) && gameBoardLite[cellX][cellY]==gameBoardLite[cellX-1][cellY-1]) {
            adjacentCheckersPositions[index][0] = cellX-1;
            adjacentCheckersPositions[index++][1] = cellY-1;
        }
        if(cellX-1>-1 && gameBoardLite[cellX][cellY]==gameBoardLite[cellX-1][cellY]) {
            adjacentCheckersPositions[index][0] = cellX-1;
            adjacentCheckersPositions[index++][1] = cellY;
        }
        if((cellX-1>-1 && cellY+1<gameBoardLite[0].length) && gameBoardLite[cellX][cellY]==gameBoardLite[cellX-1][cellY+1]) {
            adjacentCheckersPositions[index][0] = cellX-1;
            adjacentCheckersPositions[index++][1] = cellY+1;
        }
        if(cellY+1<gameBoardLite[0].length && gameBoardLite[cellX][cellY]==gameBoardLite[cellX][cellY+1]) {
            adjacentCheckersPositions[index][0] = cellX;
            adjacentCheckersPositions[index++][1] = cellY+1;
        }
        if((cellX+1<gameBoardLite.length && cellY+1<gameBoardLite[0].length) && gameBoardLite[cellX][cellY]==gameBoardLite[cellX+1][cellY+1]) {
            adjacentCheckersPositions[index][0] = cellX+1;
            adjacentCheckersPositions[index++][1] = cellY+1;
        }
        if(cellX+1<gameBoardLite.length && gameBoardLite[cellX][cellY]==gameBoardLite[cellX+1][cellY]) {
            adjacentCheckersPositions[index][0] = cellX+1;
            adjacentCheckersPositions[index++][1] = cellY;
        }
        if((cellX+1<gameBoardLite.length && cellY-1>-1) && gameBoardLite[cellX][cellY]==gameBoardLite[cellX+1][cellY-1]) {
            adjacentCheckersPositions[index][0] = cellX+1;
            adjacentCheckersPositions[index++][1] = cellY-1;
        }
        if(cellY-1>-1 && gameBoardLite[cellX][cellY]==gameBoardLite[cellX][cellY-1]) {
            adjacentCheckersPositions[index][0] = cellX;
            adjacentCheckersPositions[index][1] = cellY-1;
        }
        return adjacentCheckersPositions;
    }

    private void doDepthFirstSearchLite(int checker, int[][] checkersLite, int[][] gameBoardLite, int[] dfsStatus) {
        dfsStatus[checker] = 1;
        int[][] adjacentCheckersPositions = getAdjacentCheckersPositions(checkersLite[checker][0], checkersLite[checker][1], gameBoardLite);
        for(int i=0, adjacentChecker=-1; i<adjacentCheckersPositions.length; i++) {
            if(adjacentCheckersPositions[i][0] == -1) {
                break;
            }
            for(int j=0; j<checkersLite.length; j++) {
                if(adjacentCheckersPositions[i][0]==checkersLite[j][0] && adjacentCheckersPositions[i][1]==checkersLite[j][1]) {
                    adjacentChecker = j;
                }
            }
            if(dfsStatus[adjacentChecker] == 0) {
                doDepthFirstSearchLite(adjacentChecker, checkersLite, gameBoardLite, dfsStatus);
            }
        }
        dfsStatus[checker] = 2;
    }

    private boolean hasWonLite(int[][] checkersLite, int[][] gameBoardLite) {
        int connectedComponents = 0;
        int[] dfsStatus = new int[checkersLite.length];
        for(int i=0; i<dfsStatus.length; i++) {
            dfsStatus[i] = 0;  // 0: unvisited; 1: visited; 2: explored;
        }
        for(int i=0; i<checkersLite.length; i++) {
            if(checkersLite[i][0]!=-1 && dfsStatus[i]==0) {
                connectedComponents++;
                doDepthFirstSearchLite(i, checkersLite, gameBoardLite, dfsStatus);
            }
        }
        return (connectedComponents == 1);
    }

    private int findPieceSquareTableValue(boolean isBlack, int[][] gameBoardLite) {
        /* the higher the better */
        int pieceSquareTableValue = 0;
        if(gameBoardLite.length == 6) {
            int[][] pieceSquareTable = {{-10,  5,  10,  10,  5,  -10}, {5,  10,  25,  25,  10,  5}, {10,  25,  50,  50,  25,  10}, {10,  25,  50,  50,  25,  10}, {5,  10,  25,  25,  10,  5}, {-10,  5,  10,  10,  5,  -10}};
            for(int i=0; i<gameBoardLite.length; i++) {
                for(int j=0; j<gameBoardLite[0].length; j++) {
                    if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                        pieceSquareTableValue += pieceSquareTable[i][j];
                    }
                }
            }
        } else {
            int[][] pieceSquareTable = {{-80, -25, -20, -20, -20, -20, -25, -80}, {-25,  10,  10,  10,  10,  10,  10,  -25}, {-20,  10,  25,  25,  25,  25,  10,  -20}, {-20,  10,  25,  50,  50,  25,  10,  -20}, {-20,  10,  25,  50,  50,  25,  10,  -20}, {-20,  10,  25,  25,  25,  25,  10,  -20}, {-25,  10,  10,  10,  10,  10,  10,  -25}, {-80, -25, -20, -20, -20, -20, -25, -80}};
            for(int i=0; i<gameBoardLite.length; i++) {
                for(int j=0; j<gameBoardLite[0].length; j++) {
                    if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                        pieceSquareTableValue += pieceSquareTable[i][j];
                    }
                }
            }
        }
        return pieceSquareTableValue;
    }

    private int findAreaValue(boolean isBlack, int[][] gameBoardLite) {
        /* the lower the better */
        int minX=gameBoardLite.length, maxX=-1, minY=gameBoardLite[0].length, maxY=-1;
        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                    minX = (minX>i? i: minX);
                    maxX = (maxX<i? i: maxX);
                    minY = (minY>j? j: minY);
                    maxY = (maxY<j? j: maxY);
                }
            }
        }
        return (maxX-minX)*(maxY-minY);
    }

    private int findMobilityValue(boolean isBlack, int[][] gameBoardLite) {
        /* the higher the better */
        int totalAvailableMovements = 0;
        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                    int[][] temp = makeMoveLite(i, j, gameBoardLite);
                    for(int k=0; k<temp.length; k++) {
                        totalAvailableMovements += (temp[k][0]==-1? 0: 1);
                    }
                }
            }
        }
        return totalAvailableMovements;
    }

    private int findConnectednessValue(boolean isBlack, int[][] gameBoardLite) {
        /* the higher the better */
        int totalConnections = 0;
        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                    int[][] temp = getAdjacentCheckersPositions(i, j, gameBoardLite);
                    for(int k=0; k<temp.length; k++) {
                        totalConnections += (temp[k][0]==-1? 0: 1);
                    }
                }
            }
        }
        return totalConnections;
    }

    private int findQuadValue(boolean isBlack, int[][] gameBoardLite) {
        /* the higher the better */
        int quadCount=0;
        for(int i=0; i<gameBoardLite.length-1; i++) {
            for(int j=0, count=0; j<gameBoardLite[0].length-1; j++, count=0) {
                for(int row=0; row<2; row++) {
                    for(int column=0; column<2; column++) {
                        if(gameBoardLite[i+row][j+column] == (isBlack? -1: 1)) {
                            count++;
                        }
                    }
                }
                if(count==3 || count==4) {
                    quadCount++;
                }
            }
        }
        return quadCount;
    }

    private double findDensityFromDistanceValue(boolean isBlack, int[][] gameBoardLite) {
        /* the lower the better for distance, the higher the better for density */
        double centerX=0, centerY=0, totalDistance=0;
        int count = 0;
        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                    centerX += i;
                    centerY += j;
                    count++;
                }
            }
        }
        centerX /= count;
        centerY /= count;
        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                if(gameBoardLite[i][j] == (isBlack? -1: 1)) {
                    totalDistance += Math.sqrt(Math.pow(i-centerX, 2)+Math.pow(j-centerY, 2));
                }
            }
        }
        return totalDistance/count;
    }

    private double findHeuristicEvaluation(boolean isBlack, int[][] gameBoardLite) {
        /* scope for experimentation: evaluation may differ by changing weights associated with heuristic functions */
        return 1.0*findPieceSquareTableValue(isBlack, gameBoardLite)-1.0*findAreaValue(isBlack, gameBoardLite)+1.0*findMobilityValue(isBlack, gameBoardLite)+1.0*findConnectednessValue(isBlack, gameBoardLite)+1.0*findQuadValue(isBlack, gameBoardLite)-1.0*findDensityFromDistanceValue(isBlack, gameBoardLite);
    }

    private int[][] copyCheckersLite(int[][] checkersLite, int checker, int cellX, int cellY) {
        int[][] copy = new int[checkersLite.length][2];
        for(int i=0; i<copy.length; i++) {
            copy[i][0] = (i==checker? cellX: checkersLite[i][0]);
            copy[i][1] = (i==checker? cellY: checkersLite[i][1]);
        }
        return copy;
    }

    private int[][] copyGameBoardLite(int[][] gameBoardLite, int oldCellX, int oldCellY, int newCellX, int newCellY) {
        int[][] copy = new int[gameBoardLite.length][gameBoardLite[0].length];
        for(int i=0; i<copy.length; i++) {
            for(int j=0; j<copy[0].length; j++) {
                if(i==oldCellX && j==oldCellY) {
                    copy[i][j] = 0;
                } else if(i==newCellX && j==newCellY) {
                    copy[i][j] = gameBoardLite[oldCellX][oldCellY];
                } else {
                    copy[i][j] = gameBoardLite[i][j];
                }
            }
        }
        return copy;
    }

    private int findChecker(int[][] checkersLite, int cellX, int cellY) {
        int checker = -1;
        for(int i=0; i<checkersLite.length; i++) {
            if(checkersLite[i][0]==cellX && checkersLite[i][1]==cellY) {
                checker = i;
                break;
            }
        }
        return checker;
    }

    private int doMiniMaxWithAlphaBetaPruningLite(int[][] blackCheckersLite, int[][] whiteCheckersLite, int[][] gameBoardLite, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if(isMaximizingPlayer) {
            if(hasWonLite(blackCheckersLite, gameBoardLite)) {
                return Integer.MIN_VALUE;
            }
            if(hasWonLite(whiteCheckersLite, gameBoardLite)) {
                return Integer.MAX_VALUE;
            }
        } else {
            if(hasWonLite(whiteCheckersLite, gameBoardLite)) {
                return Integer.MAX_VALUE;
            }
            if(hasWonLite(blackCheckersLite, gameBoardLite)) {
                return Integer.MIN_VALUE;
            }
        }
        if(depth == 0) {
            return (int)(findHeuristicEvaluation(false, gameBoardLite)-findHeuristicEvaluation(true, gameBoardLite));
        }

        if(isMaximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            boolean isPruned = false;
            for(int i=0; i<gameBoardLite.length; i++) {
                for(int j=0; j<gameBoardLite[0].length; j++) {
                    if(gameBoardLite[i][j] == 1) {
                        int[][] availableNewPositions = makeMoveLite(i, j, gameBoardLite);
                        for(int k=0; k<availableNewPositions.length; k++) {
                            if(availableNewPositions[k][0] == -1) {
                                break;
                            }
                            int eval, checkerWhite=findChecker(whiteCheckersLite, i, j);
                            if(gameBoardLite[availableNewPositions[k][0]][availableNewPositions[k][1]] == -1) {
                                int checkerBlack=findChecker(blackCheckersLite, availableNewPositions[k][0], availableNewPositions[k][1]);
                                eval = doMiniMaxWithAlphaBetaPruningLite(copyCheckersLite(blackCheckersLite, checkerBlack, -1, -1), copyCheckersLite(whiteCheckersLite, checkerWhite, availableNewPositions[k][0], availableNewPositions[k][1]), copyGameBoardLite(gameBoardLite, i, j, availableNewPositions[k][0], availableNewPositions[k][1]), depth-1, alpha, beta, false);
                            } else {
                                eval = doMiniMaxWithAlphaBetaPruningLite(blackCheckersLite, copyCheckersLite(whiteCheckersLite, checkerWhite, availableNewPositions[k][0], availableNewPositions[k][1]), copyGameBoardLite(gameBoardLite, i, j, availableNewPositions[k][0], availableNewPositions[k][1]), depth-1, alpha, beta, false);
                            }
                            maxEval = Math.max(maxEval, eval);
                            alpha = Math.max(alpha, eval);
                            if(alpha >= beta) {
                                isPruned = true;
                                break;
                            }
                        }
                        if(isPruned) {
                            break;
                        }
                    }
                }
                if(isPruned) {
                    break;
                }
            }
            return maxEval;
        } else {
            int minEval = Integer.MAX_VALUE;
            boolean isPruned = false;
            for(int i=0; i<gameBoardLite.length; i++) {
                for(int j=0; j<gameBoardLite[0].length; j++) {
                    if(gameBoardLite[i][j] == -1) {
                        int[][] availableNewPositions = makeMoveLite(i, j, gameBoardLite);
                        for(int k=0; k<availableNewPositions.length; k++) {
                            if(availableNewPositions[k][0] == -1) {
                                break;
                            }
                            int eval, checkerBlack=findChecker(blackCheckersLite, i, j);
                            if(gameBoardLite[availableNewPositions[k][0]][availableNewPositions[k][1]] == 1) {
                                int checkerWhite=findChecker(whiteCheckersLite, availableNewPositions[k][0], availableNewPositions[k][1]);
                                eval = doMiniMaxWithAlphaBetaPruningLite(copyCheckersLite(blackCheckersLite, checkerBlack, availableNewPositions[k][0], availableNewPositions[k][1]), copyCheckersLite(whiteCheckersLite, checkerWhite, -1, -1), copyGameBoardLite(gameBoardLite, i, j, availableNewPositions[k][0], availableNewPositions[k][1]), depth-1, alpha, beta, true);
                            } else {
                                eval = doMiniMaxWithAlphaBetaPruningLite(copyCheckersLite(blackCheckersLite, checkerBlack, availableNewPositions[k][0], availableNewPositions[k][1]), whiteCheckersLite, copyGameBoardLite(gameBoardLite, i, j, availableNewPositions[k][0], availableNewPositions[k][1]), depth-1, alpha, beta, true);
                            }
                            minEval = Math.min(minEval, eval);
                            beta = Math.min(beta, eval);
                            if(alpha >= beta) {
                                isPruned = true;
                                break;
                            }
                        }
                        if(isPruned) {
                            break;
                        }
                    }
                }
                if(isPruned) {
                    break;
                }
            }
            return minEval;
        }
    }

    private int[] doMiniMaxWithAlphaBetaPruning(int[][] blackCheckersLite, int[][] whiteCheckersLite, int[][] gameBoardLite, int depth, int alpha, int beta) {
        int[] nextMovePositionDetails = new int[3];  // [0]: checker; [1]: nextCellX; [2]: nextCellY;
        for(int i=0; i<nextMovePositionDetails.length; i++) {
            nextMovePositionDetails[i] = -1;
        }
        int maxEval = Integer.MIN_VALUE;
        boolean isPruned = false;

        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                if(gameBoardLite[i][j] == 1) {
                    int[][] availableNewPositions = makeMoveLite(i, j, gameBoardLite);
                    for(int k=0; k<availableNewPositions.length; k++) {
                        if(availableNewPositions[k][0] == -1) {
                            break;
                        }
                        int eval, checkerWhite=findChecker(whiteCheckersLite, i, j);
                        if(gameBoardLite[availableNewPositions[k][0]][availableNewPositions[k][1]] == -1) {
                            int checkerBlack=findChecker(blackCheckersLite, availableNewPositions[k][0], availableNewPositions[k][1]);
                            eval = doMiniMaxWithAlphaBetaPruningLite(copyCheckersLite(blackCheckersLite, checkerBlack, -1, -1), copyCheckersLite(whiteCheckersLite, checkerWhite, availableNewPositions[k][0], availableNewPositions[k][1]), copyGameBoardLite(gameBoardLite, i, j, availableNewPositions[k][0], availableNewPositions[k][1]), depth-1, alpha, beta, false);
                        } else {
                            eval = doMiniMaxWithAlphaBetaPruningLite(blackCheckersLite, copyCheckersLite(whiteCheckersLite, checkerWhite, availableNewPositions[k][0], availableNewPositions[k][1]), copyGameBoardLite(gameBoardLite, i, j, availableNewPositions[k][0], availableNewPositions[k][1]), depth-1, alpha, beta, false);
                        }
                        if(maxEval < eval) {
                            maxEval = eval;
                            nextMovePositionDetails[0] = checkerWhite;
                            nextMovePositionDetails[1] = availableNewPositions[k][0];
                            nextMovePositionDetails[2] = availableNewPositions[k][1];
                        }
                        alpha = Math.max(alpha, eval);
                        if(alpha >= beta) {
                            isPruned = true;
                            break;
                        }
                    }
                    if(isPruned) {
                        break;
                    }
                }
            }
            if(isPruned) {
                break;
            }
        }
        return nextMovePositionDetails;
    }

    public int[] doAdversarialSearch() {
        /* white is AI and maximizing player(1); black is human and minimizing player(-1); */
        int[][] blackCheckersLite = new int[blackCheckers.length][2];
        int[][] whiteCheckersLite = new int[whiteCheckers.length][2];
        int[][] gameBoardLite = new int[gameBoard.length][gameBoard[0].length];
        for(int i=0; i<blackCheckersLite.length; i++) {
            blackCheckersLite[i][0] = blackCheckers[i].getCellX();
            blackCheckersLite[i][1] = blackCheckers[i].getCellY();
        }
        for(int i=0; i<whiteCheckersLite.length; i++) {
            whiteCheckersLite[i][0] = whiteCheckers[i].getCellX();
            whiteCheckersLite[i][1] = whiteCheckers[i].getCellY();
        }
        for(int i=0; i<gameBoardLite.length; i++) {
            for(int j=0; j<gameBoardLite[0].length; j++) {
                gameBoardLite[i][j] = (gameBoard[i][j]==null? 0: (gameBoard[i][j].isBlack()? -1: 1));
            }
        }
        /* starting adversarial search (miniMax algorithm with alpha-beta pruning with proper heuristics) */
        return doMiniMaxWithAlphaBetaPruning(blackCheckersLite, whiteCheckersLite, gameBoardLite, 4, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
}
