package structures;

import classes.PuzzleBoard;

public class Output {
    public PuzzleBoard goalBoard;
    public int exploredNodesCount;
    public long elapsedTime;

    public Output(PuzzleBoard goalBoard, int exploredNodesCount, long elapsedTime) {
        this.goalBoard = goalBoard;
        this.exploredNodesCount = exploredNodesCount;
        this.elapsedTime = elapsedTime;
    }
}
