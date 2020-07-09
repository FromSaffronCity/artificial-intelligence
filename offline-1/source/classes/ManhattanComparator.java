package classes;

import java.util.Comparator;

public class ManhattanComparator implements Comparator<PuzzleBoard> {
    @Override
    public int compare(PuzzleBoard puzzleBoard1, PuzzleBoard puzzleBoard2) {
        return puzzleBoard1.getCostManhattan()-puzzleBoard2.getCostManhattan();
    }
}
