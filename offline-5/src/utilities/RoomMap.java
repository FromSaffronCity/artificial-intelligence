package utilities;

import java.util.Random;

public class RoomMap {
    private double[][] ghostLocationProbabilities;
    private int[][] currentGhostLocation;
    private Random random;

    public RoomMap(int dimension) {
        ghostLocationProbabilities = new double[dimension][dimension];
        currentGhostLocation = new int[1][2];
        random = new Random();

        /* initial probability distribution P(X1) */
        for(int i=0; i<ghostLocationProbabilities.length; i++) {
            for(int j=0; j<ghostLocationProbabilities[0].length; j++) {
                ghostLocationProbabilities[i][j] = 1.0/(dimension*dimension);
            }
        }
        /* initial ghost location */
        int temp = random.nextInt(dimension*dimension);
        currentGhostLocation[0][0] = temp/dimension;
        currentGhostLocation[0][1] = temp%dimension;
    }

    public int getDimensionRow() {
        return ghostLocationProbabilities.length;
    }

    public int getDimensionCol() {
        return ghostLocationProbabilities[0].length;
    }

    public double getCellProbability(int x, int y) {
        return ghostLocationProbabilities[x][y];
    }

    public void updateWithTransition() {
        /* update probability distribution P(Xt) with transition probability distribution P(Xt|Xt-1) */
        double[][] temp = new double[ghostLocationProbabilities.length][ghostLocationProbabilities[0].length];
        for(int i=0; i<temp.length; i++) {
            System.arraycopy(ghostLocationProbabilities[i], 0, temp[i], 0, temp[0].length);
        }
        int g_l=ghostLocationProbabilities.length, g0_l=ghostLocationProbabilities[0].length;
        for(int i=0; i<ghostLocationProbabilities.length; i++) {
            for(int j=0; j<ghostLocationProbabilities[0].length; j++) {
                if((i==0 || i==g_l-1) && (j==0 || j==g0_l-1)) {
                    ghostLocationProbabilities[i][j] = 0.02*temp[i][j]+0.008*temp[(i==0? 1: g_l-2)][(j==0? 1: g0_l-2)]+0.32*(temp[(i==0? 1: g_l-2)][j]+temp[i][(j==0? 1: g0_l-2)]);
                } else if((i==0 || i==g_l-1) || (j==0 || j==g0_l-1)) {
                    ghostLocationProbabilities[i][j] = 0.02*temp[i][j];
                    if(i==0 || i==g_l-1) {
                        if(j==1 || j==g0_l-2) {
                            ghostLocationProbabilities[i][j] += 0.48*temp[i][(j==1? 0: g0_l-1)]+0.32*temp[i][(j==1? 2: g0_l-3)]+0.24*temp[(i==0? 1: g_l-2)][j]+0.01*temp[(i==0? 1: g_l-2)][(j==1? 0: g0_l-1)]+0.008*temp[(i==0? 1: g_l-2)][(j==1? 2: g0_l-3)];
                        } else {
                            ghostLocationProbabilities[i][j] += 0.32*(temp[i][j-1]+temp[i][j+1])+0.24*temp[(i==0? 1: g_l-2)][j]+0.008*(temp[(i==0? 1: g_l-2)][j-1]+temp[(i==0? 1: g_l-2)][j+1]);
                        }
                    } else {
                        if(i==1 || i==g_l-2) {
                            ghostLocationProbabilities[i][j] += 0.48*temp[(i==1? 0: g_l-1)][j]+0.32*temp[(i==1? 2: g_l-3)][j]+0.24*temp[i][(j==0? 1: g0_l-2)]+0.01*temp[(i==1? 0: g_l-1)][(j==0? 1: g0_l-2)]+0.008*temp[(i==1? 2: g_l-3)][(j==0? 1: g0_l-2)];
                        } else {
                            ghostLocationProbabilities[i][j] += 0.32*(temp[i-1][j]+temp[i+1][j])+0.24*temp[i][(j==0? 1: g0_l-2)]+0.008*(temp[i-1][(j==0? 1: g0_l-2)]+temp[i+1][(j==0? 1: g0_l-2)]);
                        }
                    }
                } else {
                    ghostLocationProbabilities[i][j] = 0.008*temp[i][j];
                    if((i==1 || i==g_l-2) && (j==1 || j==g0_l-2)) {
                        ghostLocationProbabilities[i][j] += 0.32*(temp[i][(j==1? 0: g0_l-1)]+temp[(i==1? 0: g_l-1)][j])+0.24*(temp[i][(j==1? 2: g0_l-3)]+temp[(i==1? 2: g_l-3)][j])+0.02*temp[(i==1? 0: g_l-1)][(j==1? 0: g0_l-1)]+0.01*(temp[(i==1? 0: g_l-1)][(j==1? 2: g0_l-3)]+temp[(i==1? 2: g_l-3)][(j==1? 0: g0_l-1)])+0.008*temp[(i==1? 2: g_l-3)][(j==1? 2: g0_l-3)];
                    } else if((i==1 || i==g_l-2) || (j==1 || j==g0_l-2)) {
                        if(i==1 || i==g_l-2) {
                            ghostLocationProbabilities[i][j] += 0.32*temp[(i==1? 0: g_l-1)][j]+0.24*(temp[i][j-1]+temp[i][j+1]+temp[(i==1? 2: g_l-3)][j])+0.01*(temp[(i==1? 0: g_l-1)][j-1]+temp[(i==1? 0: g_l-1)][j+1])+0.008*(temp[(i==1? 2: g_l-3)][j-1]+temp[(i==1? 2: g_l-3)][j+1]);
                        } else {
                            ghostLocationProbabilities[i][j] += 0.32*temp[i][(j==1? 0: g0_l-1)]+0.24*(temp[i-1][j]+temp[i+1][j]+temp[i][(j==1? 2: g0_l-3)])+0.01*(temp[i-1][(j==1? 0: g0_l-1)]+temp[i+1][(j==1? 0: g0_l-1)])+0.008*(temp[i-1][(j==1? 2: g0_l-3)]+temp[i+1][(j==1? 2: g0_l-3)]);
                        }
                    } else {
                        ghostLocationProbabilities[i][j] += 0.24*(temp[i-1][j]+temp[i+1][j]+temp[i][j-1]+temp[i][j+1])+0.008*(temp[i-1][j-1]+temp[i-1][j+1]+temp[i+1][j-1]+temp[i+1][j+1]);
                    }
                }
            }
        }

        /* update current ghost location */
        int temp_rand = random.nextInt(100);
        if((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==ghostLocationProbabilities.length-1) && (currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==ghostLocationProbabilities[0].length-1)) {
            if(temp_rand < 48) {
                currentGhostLocation[0][0] = (currentGhostLocation[0][0]==0? 1: g_l-2);
            } else if(temp_rand < 96) {
                currentGhostLocation[0][1] = (currentGhostLocation[0][1]==0? 1: g0_l-2);
            } else if(temp_rand < 98) {
                currentGhostLocation[0][0] = (currentGhostLocation[0][0]==0? 1: g_l-2);
                currentGhostLocation[0][1] = (currentGhostLocation[0][1]==0? 1: g0_l-2);
            }
        } else if((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==ghostLocationProbabilities.length-1) || (currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==ghostLocationProbabilities[0].length-1)) {
            if(temp_rand < 32) {
                currentGhostLocation[0][0] = ((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==g_l-1)? currentGhostLocation[0][0]: currentGhostLocation[0][0]-1);
                currentGhostLocation[0][1] = ((currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==g0_l-1)? currentGhostLocation[0][1]: currentGhostLocation[0][1]-1);
            } else if(temp_rand < 64) {
                currentGhostLocation[0][0] = ((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==g_l-1)? currentGhostLocation[0][0]: currentGhostLocation[0][0]+1);
                currentGhostLocation[0][1] = ((currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==g0_l-1)? currentGhostLocation[0][1]: currentGhostLocation[0][1]+1);
            } else if(temp_rand < 96) {
                currentGhostLocation[0][0] = ((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==g_l-1)? (currentGhostLocation[0][0]==0? 1: g_l-2): currentGhostLocation[0][0]);
                currentGhostLocation[0][1] = ((currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==g0_l-1)? (currentGhostLocation[0][1]==0? 1: g0_l-2): currentGhostLocation[0][1]);
            } else if(temp_rand < 97) {
                currentGhostLocation[0][0] = ((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==g_l-1)? (currentGhostLocation[0][0]==0? 1: g_l-2): currentGhostLocation[0][0]-1);
                currentGhostLocation[0][1] = ((currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==g0_l-1)? (currentGhostLocation[0][1]==0? 1: g0_l-2): currentGhostLocation[0][1]-1);
            } else if(temp_rand < 98) {
                currentGhostLocation[0][0] = ((currentGhostLocation[0][0]==0 || currentGhostLocation[0][0]==g_l-1)? (currentGhostLocation[0][0]==0? 1: g_l-2): currentGhostLocation[0][0]+1);
                currentGhostLocation[0][1] = ((currentGhostLocation[0][1]==0 || currentGhostLocation[0][1]==g0_l-1)? (currentGhostLocation[0][1]==0? 1: g0_l-2): currentGhostLocation[0][1]+1);
            }
        } else {
            if(temp_rand < 24) {
                currentGhostLocation[0][0] = currentGhostLocation[0][0]-1;
            } else if(temp_rand < 48) {
                currentGhostLocation[0][0] = currentGhostLocation[0][0]+1;
            } else if(temp_rand < 72) {
                currentGhostLocation[0][1] = currentGhostLocation[0][1]-1;
            } else if(temp_rand < 96) {
                currentGhostLocation[0][1] = currentGhostLocation[0][1]+1;
            } else {
                temp_rand = random.nextInt(40);
                if(temp_rand < 8) {
                    currentGhostLocation[0][0] = currentGhostLocation[0][0]-1;
                    currentGhostLocation[0][1] = currentGhostLocation[0][1]-1;
                } else if(temp_rand < 16) {
                    currentGhostLocation[0][0] = currentGhostLocation[0][0]-1;
                    currentGhostLocation[0][1] = currentGhostLocation[0][1]+1;
                } else if(temp_rand < 24) {
                    currentGhostLocation[0][0] = currentGhostLocation[0][0]+1;
                    currentGhostLocation[0][1] = currentGhostLocation[0][1]-1;
                } else if(temp_rand < 32) {
                    currentGhostLocation[0][0] = currentGhostLocation[0][0]+1;
                    currentGhostLocation[0][1] = currentGhostLocation[0][1]+1;
                }
            }
        }
    }

    private int getColorAccordingToManhattanDistance(int x1, int y1, int x2, int y2) {
        int manhattanDistance = Math.abs(x1-x2)+Math.abs(y1-y2);
        return (manhattanDistance<3? 0: (manhattanDistance<5? 1: 2));  /* color: 0 = red; 1 = orange; 2 = green; */
    }

    public int updateWithObservation(int x, int y) {
        /* determining sensor color reading */
        int color = getColorAccordingToManhattanDistance(x, y, currentGhostLocation[0][0], currentGhostLocation[0][1]);

        /* update probability distribution P(Xt) with emission probability distribution P(Rij|Xt) */
        double normalizingFactor = 0.0;
        for(int i=0; i<ghostLocationProbabilities.length; i++) {
            for(int j=0; j<ghostLocationProbabilities[0].length; j++) {
                ghostLocationProbabilities[i][j] = (getColorAccordingToManhattanDistance(x, y, i, j)==color? 0.9: 0.05)*ghostLocationProbabilities[i][j];  /* considering noisy output from sensor */
                normalizingFactor += ghostLocationProbabilities[i][j];
            }
        }
        for(int i=0; i<ghostLocationProbabilities.length; i++) {
            for(int j=0; j<ghostLocationProbabilities[0].length; j++) {
                ghostLocationProbabilities[i][j] /= normalizingFactor;
            }
        }
        return color;
    }

    public boolean isGhostHere(int x, int y) {
        return (currentGhostLocation[0][0]==x && currentGhostLocation[0][1]==y);
    }

    public void updateBecauseGhostFound(int x, int y) {
        for(int i=0; i<ghostLocationProbabilities.length; i++) {
            for(int j=0; j<ghostLocationProbabilities[0].length; j++) {
                if(i==x && j==y) {
                    ghostLocationProbabilities[i][j] = 1.0;
                } else {
                    ghostLocationProbabilities[i][j] = 0.0;
                }
            }
        }
    }

    public void updateBecauseMiss(int x, int y) {
        ghostLocationProbabilities[x][y] = 0.0;
        double probabilitySum = 0.0;
        for(double[] ghostLocationProbabilityArray: ghostLocationProbabilities) {
            for(double ghostLocationProbability: ghostLocationProbabilityArray) {
                probabilitySum += ghostLocationProbability;
            }
        }
        for(int i=0; i<ghostLocationProbabilities.length; i++) {
            for(int j=0; j<ghostLocationProbabilities[0].length; j++) {
                ghostLocationProbabilities[i][j] /= probabilitySum;
            }
        }
    }

    @Override
    public String toString() {
        double probabilitySum = 0.0;
        for(double[] ghostLocationProbabilityArray: ghostLocationProbabilities) {
            for(double ghostLocationProbability: ghostLocationProbabilityArray) {
                probabilitySum += ghostLocationProbability;
            }
        }
        return "["+currentGhostLocation[0][0]+", "+currentGhostLocation[0][1]+"]: "+probabilitySum;
    }
}
