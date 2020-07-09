package classes;

import structures.Output;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Hashtable;
import java.util.Stack;

public class Main {
    private static boolean checkReachabilityToDefault(int[] state) {
        int numberOfInversions=0, blankPosition=-1;

        for(int i=0; i<16; i++) {
            if(state[i] == 0) {
                blankPosition = i;
            } else {
                for(int j=i+1; j<16; j++) {
                    if(state[j] != 0) {
                        if(state[i] > state[j]) {
                            numberOfInversions++;
                        }
                    }
                }
            }
        }
        return ((blankPosition/4)%2==0 && numberOfInversions%2==1) || ((blankPosition/4)%2==1 && numberOfInversions%2==0);
    }

    private static boolean checkSolvability(int[] initialState, int[] goalState) {
        return (checkReachabilityToDefault(initialState) == checkReachabilityToDefault(goalState));
    }

    private static Output doAStarSearch(int[] initialState, String heuristicChoice) {
        PriorityQueue<PuzzleBoard> priorityQueue;

        if(heuristicChoice.equalsIgnoreCase("misplacement")) {
            priorityQueue = new PriorityQueue<>(new MisplacementComparator());
        } else if(heuristicChoice.equalsIgnoreCase("manhattan")) {
            priorityQueue = new PriorityQueue<>(new ManhattanComparator());
        } else {
            return null;
        }
        Hashtable<PuzzleBoard, Integer> openList = new Hashtable<>();
        Hashtable<PuzzleBoard, Integer> closedList = new Hashtable<>();

        int[][] initialConfig = new int[4][4];
        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                initialConfig[row][column] = initialState[row*4+column];
            }
        }
        int exploredNodesCount = 0;
        long startTime, endTime;
        boolean isGoalFound = false;

        /* A* search begins */
        startTime = System.nanoTime();
        PuzzleBoard temp = new PuzzleBoard(initialConfig, null, 0);
        priorityQueue.add(temp);  // putting start node on open-list
        if(heuristicChoice.equalsIgnoreCase("misplacement")) {
            openList.put(temp, temp.getCostMisplacement());
        } else {
            openList.put(temp, temp.getCostManhattan());
        }

        while(!priorityQueue.isEmpty()) {
            temp = priorityQueue.poll();
            openList.remove(temp);
            if(heuristicChoice.equalsIgnoreCase("misplacement")) {
                closedList.put(temp, temp.getCostMisplacement());
            } else {
                closedList.put(temp, temp.getCostManhattan());
            }
            exploredNodesCount++;

            PuzzleBoard[] successors = temp.getSuccessors();
            for(int i=0; i<successors.length; i++) {
                if(successors[i].getIsEqualGoal()) {
                    temp = successors[i];
                    isGoalFound = true;
                    exploredNodesCount++;
                    break;
                }
                if(!closedList.containsKey(successors[i])) {
                    if(!openList.containsKey(successors[i])) {
                        priorityQueue.add(successors[i]);
                        if(heuristicChoice.equalsIgnoreCase("misplacement")) {
                            openList.put(successors[i], successors[i].getCostMisplacement());
                        } else {
                            openList.put(successors[i], successors[i].getCostManhattan());
                        }
                    } else {
                        if(heuristicChoice.equalsIgnoreCase("misplacement")) {
                            if(successors[i].getCostMisplacement()<openList.get(successors[i])) {
                                priorityQueue.add(successors[i]);
                                openList.put(successors[i], successors[i].getCostMisplacement());
                            }
                        }
                        if(heuristicChoice.equalsIgnoreCase("manhattan")) {
                            if(successors[i].getCostManhattan()<openList.get(successors[i])) {
                                priorityQueue.add(successors[i]);
                                openList.put(successors[i], successors[i].getCostManhattan());
                            }
                        }
                    }
                }
            }
            if(isGoalFound) {
                break;
            }
        }
        endTime = System.nanoTime();
        return new Output(temp, exploredNodesCount, endTime-startTime);
    }

    private static void printOutput(Output output, BufferedWriter bufferedWriter) throws IOException {
        if(output == null) {
            bufferedWriter.write(">> invalid option provided for heuristic"+"\n");
            return ;
        }

        Stack<PuzzleBoard> stack = new Stack<>();
        PuzzleBoard temp = output.goalBoard;
        while(temp != null) {
            stack.push(temp);
            temp = temp.getParentBoard();
        }

        bufferedWriter.write("path from initial state to goal state:"+"\n\n");
        while(!stack.empty()) {
            bufferedWriter.write(stack.pop()+"\n");
        }
        bufferedWriter.write("total number of swaps: "+output.goalBoard.getCostFromInitialState()+"\n");
        bufferedWriter.write("total time taken (in nanoseconds): "+output.elapsedTime+"\n");
        bufferedWriter.write("total explored nodes (nodes in closedList): "+output.exploredNodesCount+"\n\n");
        return ;
    }

    public static void main(String[] args) throws IOException  {
        File file = new File("io-files\\3_input.txt");
        Scanner scanner = new Scanner(file);
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("io-files\\3_output.txt"));

        int cases = scanner.nextInt()-1;
        if(cases <= 0) {
            bufferedWriter.write(">> insufficient number of cases"+"\n");
            bufferedWriter.close();
            return ;
        }

        int[] initialState = new int[16];
        int[] goalState = new int[16];
        for(int i=0; i<16; i++) {
            goalState[i] = scanner.nextInt();
        }
        int[][] goalConfig = new int[4][4];
        for(int row=0; row<4; row++) {
            for(int column=0; column<4; column++) {
                goalConfig[row][column] = goalState[row*4+column];
            }
        }
        PuzzleBoard.setGoalConfig(goalConfig);

        for(int i=0; i<cases; i++) {
            for(int j=0; j<16; j++) {
                initialState[j] = scanner.nextInt();
            }

            /* task-1: solvability check */
            if(!checkSolvability(initialState, goalState)) {
                /*
                    if any one and only one of the two configurations can not reach to default configuration,
                      then the problem is unsolvable and the case will be skipped.
                */
                bufferedWriter.write(">> unsolvable board configuration provided"+"\n");
                continue;
            }

            /* task-2: A* search to solve the 15-puzzle problem */
            bufferedWriter.write("-- result using Manhattan Heuristic --"+"\n\n");
            printOutput(doAStarSearch(initialState, "manhattan"), bufferedWriter);
            bufferedWriter.write("-- result using Misplacement Heuristic --"+"\n\n");
            printOutput(doAStarSearch(initialState, "misplacement"), bufferedWriter);
        }
        bufferedWriter.close();
        return ;
    }
}
