package classes;

import structures.Position;

import java.util.Arrays;

public class PuzzleBoard {
    private int[][] boardConfig;
    private PuzzleBoard parentBoard;
    private int costFromInitialState;
    private int costMisplacementHeuristic;
    private int costManhattanHeuristic;
    private boolean isEqualGoal;
    private static int[][] goalConfig;

    public PuzzleBoard(int[][] boardConfig, PuzzleBoard parentBoard, int costFromInitialState) {
        if(goalConfig == null) {
            goalConfig = new int[4][4];

            for(int row=0; row<4; row++) {
                for(int column=0; column<4; column++) {
                    goalConfig[row][column] = (row*4+(column+1))%16;
                }
            }
        }
        this.boardConfig = boardConfig;
        this.parentBoard = parentBoard;
        this.costFromInitialState = costFromInitialState;
        costMisplacementHeuristic = getMisplacementHeuristic();
        costManhattanHeuristic = getManhattanHeuristic();
        isEqualGoal = true;

        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                if(this.boardConfig[row][column] != goalConfig[row][column]) {
                    isEqualGoal = false;
                    break;
                }
            }

            if(!isEqualGoal) {
                break;
            }
        }
    }

    public static void setGoalConfig(int[][] goalConfig) {
        PuzzleBoard.goalConfig = goalConfig;
        return ;
    }

    private int getMisplacementHeuristic() {
        int count = 0;

        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                if(boardConfig[row][column] != 0) {
                    if(boardConfig[row][column] != goalConfig[row][column]) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private int getManhattanHeuristic() {
        int totalManhattanDistance = 0;
        int[][] temp = new int[15][2];

        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                if(boardConfig[row][column] != 0) {
                    temp[boardConfig[row][column]-1][0] = row;
                    temp[boardConfig[row][column]-1][1] = column;
                }
            }
        }

        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                if(goalConfig[row][column] != 0) {
                    totalManhattanDistance += Math.abs(row-temp[goalConfig[row][column]-1][0])+Math.abs(column-temp[goalConfig[row][column]-1][1]);
                }
            }
        }
        return totalManhattanDistance;
    }

    private int[][] getCopyBoardConfig() {
        int[][] tempBoard = new int[4][4];

        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                tempBoard[row][column] = boardConfig[row][column];
            }
        }
        return tempBoard;
    }

    private void swapTiles(int[][] tempBoard, Position p1, Position p2) {
        int temp = tempBoard[p1.row][p1.column];
        tempBoard[p1.row][p1.column] = tempBoard[p2.row][p2.column];
        tempBoard[p2.row][p2.column] = temp;
        return ;
    }
    
    private void createNewSuccessor(PuzzleBoard[] successors, int index, Position p1, Position p2) {
        int[][] tempBoard = getCopyBoardConfig();
        swapTiles(tempBoard, p1, p2);
        successors[index] = new PuzzleBoard(tempBoard, this, costFromInitialState+1);
        return ;
    }

    public PuzzleBoard getParentBoard() {
        return parentBoard;
    }

    public int getCostFromInitialState() {
        return costFromInitialState;
    }

    public int getCostMisplacement() {
        return costFromInitialState+costMisplacementHeuristic;
    }

    public int getCostManhattan() {
        return costFromInitialState+costManhattanHeuristic;
    }

    public boolean getIsEqualGoal() {
        return isEqualGoal;
    }

    public PuzzleBoard[] getSuccessors() {
        int blankRow=-1, blankColumn=-1, successorCount;

        /* locating blank */
        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                if(boardConfig[row][column] == 0) {
                    blankRow = row;
                    blankColumn = column;
                    break;
                }
            }

            if(blankRow != -1) {
                break;
            }
        }

        /* determining number of successors */
        if((blankRow==0&&(blankColumn==0||blankColumn==3)) || (blankRow==3&&(blankColumn==0||blankColumn==3))) {
            successorCount = 2;  // corner
        } else if(blankRow==0 || blankColumn==0 || blankRow==3 || blankColumn==3) {
            successorCount = 3;  // edge
        } else {
            successorCount = 4;  // otherwise
        }

        /* creating successors */
        PuzzleBoard[] successors = new PuzzleBoard[successorCount];
        int index = 0;

        if(successorCount == 2) {
            Position p1 = new Position(((blankRow-1)==-1? blankRow+1: blankRow-1), blankColumn);
            Position p2 = new Position(blankRow, ((blankColumn-1)==-1? blankColumn+1: blankColumn-1));

            createNewSuccessor(successors, index++, new Position(blankRow, blankColumn), p1);
            createNewSuccessor(successors, index, new Position(blankRow, blankColumn), p2);
        } else if(successorCount == 3) {
            Position p1, p2, p3;

            if(blankRow-1!=-1 && blankRow+1!=4) {
                p1 = new Position(blankRow-1, blankColumn);
                p2 = new Position(blankRow+1, blankColumn);
                p3 = new Position(blankRow, ((blankColumn-1)==-1? blankColumn+1: blankColumn-1));
            } else {
                p1 = new Position(blankRow, blankColumn-1);
                p2 = new Position(blankRow, blankColumn+1);
                p3 = new Position(((blankRow-1)==-1? blankRow+1: blankRow-1), blankColumn);
            }

            createNewSuccessor(successors, index++, new Position(blankRow, blankColumn), p1);
            createNewSuccessor(successors, index++, new Position(blankRow, blankColumn), p2);
            createNewSuccessor(successors, index, new Position(blankRow, blankColumn), p3);
        } else {
            createNewSuccessor(successors, index++, new Position(blankRow, blankColumn), new Position(blankRow-1, blankColumn));
            createNewSuccessor(successors, index++, new Position(blankRow, blankColumn), new Position(blankRow+1, blankColumn));
            createNewSuccessor(successors, index++, new Position(blankRow, blankColumn), new Position(blankRow, blankColumn-1));
            createNewSuccessor(successors, index, new Position(blankRow, blankColumn), new Position(blankRow, blankColumn+1));
        }
        return successors;
    }

    @Override
    public String toString() {
        StringBuilder temp = new StringBuilder();

        for(int row=0; row<4; row++) {
            for (int column = 0; column<4; column++) {
                temp.append(" ").append(boardConfig[row][column]);
            }
            temp.append("\n");
        }
        return temp.toString();
    }

    @Override
    public boolean equals(Object object) {
        if(object == this) {
            return true;  // same object
        }
        if(!(object instanceof PuzzleBoard)) {
            return false;
        }

        PuzzleBoard temp = (PuzzleBoard) object;
        boolean isMatched = true;

        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                if(this.boardConfig[row][column] != temp.boardConfig[row][column]) {
                    isMatched = false;
                    break;
                }
            }

            if(!isMatched) {
                break;
            }
        }
        return isMatched;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(boardConfig);  // NOTICE
    }
}
