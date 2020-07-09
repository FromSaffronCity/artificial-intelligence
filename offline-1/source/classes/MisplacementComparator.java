package classes;

import java.util.Comparator;

public class MisplacementComparator implements Comparator<PuzzleBoard> {
    @Override
    public int compare(PuzzleBoard puzzleBoard1, PuzzleBoard puzzleBoard2) {
        return puzzleBoard1.getCostMisplacement()-puzzleBoard2.getCostMisplacement();
    }
}
