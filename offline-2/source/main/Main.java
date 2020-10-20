package main;

import classes.*;
import comparators.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
    private static int doTimetabling(ArrayList<Course> courses) {
        int total_time_slot = 0;

        for(int i=0; i<courses.size(); i++) {
            Course[] overlappingCourses = courses.get(i).getOverlappingCourses();
            int[] slotOccupied = new int[overlappingCourses.length];
            for(int j=0; j<slotOccupied.length; j++) {
                slotOccupied[j] = overlappingCourses[j].getTime_slot();
            }
            Arrays.sort(slotOccupied);

            int suitable_time_slot = 0;
            for(int j=0; j<slotOccupied.length; j++) {
                if(slotOccupied[j] != -1) {
                    if(suitable_time_slot == slotOccupied[j]) {
                        suitable_time_slot++;
                    }
                    if(suitable_time_slot < slotOccupied[j]) {
                        courses.get(i).setTime_slot(suitable_time_slot);
                    }
                }
            }
            if(courses.get(i).getTime_slot() == -1) {
                if(suitable_time_slot == total_time_slot) {
                    courses.get(i).setTime_slot(total_time_slot++);
                } else {
                    courses.get(i).setTime_slot(suitable_time_slot);
                }
            }
        }
        return total_time_slot;
    }

    private static int timetableByLargestDegree(ArrayList<Course> courses) {
        Collections.sort(courses, new DegreeComparator());
        return doTimetabling(courses);
    }

    private static int timetableByDSaturAlgorithm(ArrayList<Course> courses) {
        /* step: 1 & 2 */
        Collections.sort(courses, new DegreeComparator());
        courses.get(0).setTime_slot(0);

        /* step: 3 & 4 & 5 */
        int total_time_slot = 1;
        for(int i=1; i<courses.size(); i++) {
            HashSet<Integer> temp, selected=null;
            int max_saturation_degree=-1, max_index=-1;

            for(int j=0; j<courses.size(); j++) {
                if(courses.get(j).getTime_slot() == -1) {
                    temp = new HashSet<>();

                    Course[] overlappingCourses = courses.get(j).getOverlappingCourses();
                    for(int k=0; k<overlappingCourses.length; k++) {
                        if(overlappingCourses[k].getTime_slot() != -1) {
                            temp.add(overlappingCourses[k].getTime_slot());
                        }
                    }

                    if(temp.size()>max_saturation_degree || (temp.size()==max_saturation_degree && courses.get(j).getOverlappingCoursesNumber()>courses.get(max_index).getOverlappingCoursesNumber())) {
                        max_saturation_degree = temp.size();
                        max_index = j;
                        selected = temp;
                    }
                }
            }

            max_saturation_degree = 0;  // NOTICE: reusing as suitable_time_slot
            while(courses.get(max_index).getTime_slot() == -1) {
                if(!selected.contains(max_saturation_degree)) {
                    courses.get(max_index).setTime_slot(max_saturation_degree);
                    if(max_saturation_degree == total_time_slot) {
                        total_time_slot++;
                    }
                } else {
                    max_saturation_degree++;
                }
            }
        }
        return total_time_slot;
    }

    private static int timetableByLargestEnrollment(ArrayList<Course> courses) {
        Collections.sort(courses, new EnrollmentComparator());
        return doTimetabling(courses);
    }

    private static int timetableByLargestStudentsWithConflict(ArrayList<Course> courses, ArrayList<Student> students) {
        for(int i=0; i<students.size(); i++) {
            Course[] enrolledCourses = students.get(i).getEnrolledCourses();
            for(int j=0; j<enrolledCourses.length; j++) {
                enrolledCourses[j].setConflict(enrolledCourses[j].getConflict()+(enrolledCourses.length==1? 0: 1));
            }
        }
        Collections.sort(courses, new ConflictComparator());
        return doTimetabling(courses);
    }

    private static int timetableByLargestWeightedDegree(ArrayList<Course> courses, ArrayList<Student> students) {
        for(int i=0; i<students.size(); i++) {
            Course[] enrolledCourses = students.get(i).getEnrolledCourses();
            for(int j=0; j<enrolledCourses.length; j++) {
                enrolledCourses[j].setConflict(enrolledCourses[j].getConflict()+enrolledCourses.length-1);
            }
        }
        Collections.sort(courses, new ConflictComparator());
        return doTimetabling(courses);
    }

    private static int timetableByRandomOrdering(ArrayList<Course> courses) {
        Collections.shuffle(courses);
        return doTimetabling(courses);
    }

    private static double calculateAveragePenalty(ArrayList<Student> students) {
        double average_penalty = 0;
        for(int i=0; i<students.size(); i++) {
            average_penalty += students.get(i).calculateTotalPenalty();
        }
        average_penalty /= students.size();
        return average_penalty;
    }

    private static void doDepthFirstSearch(Course current, int neighbor_time_slot) {
        current.setDfsStatus(1);
        Course[] overlappingCourses = current.getOverlappingCourses();
        for(int i=0; i<overlappingCourses.length; i++) {
            if(overlappingCourses[i].getDfsStatus()==0 && overlappingCourses[i].getTime_slot()==neighbor_time_slot) {
                doDepthFirstSearch(overlappingCourses[i], current.getTime_slot());
            }
        }
        current.setDfsStatus(2);
        return ;
    }

    private static void doKempeChainInterchange(ArrayList<Course> courses, ArrayList<Student> students, Course current, int neighbor_time_slot) {
        /* forming kempe-chain */
        doDepthFirstSearch(current, neighbor_time_slot);

        /* interchanging time slots among kempe-chain nodes */
        double current_penalty = calculateAveragePenalty(students);
        int current_time_slot = current.getTime_slot();

        for(int i=0; i<courses.size(); i++) {
            if(courses.get(i).getDfsStatus()==2) {
                if(courses.get(i).getTime_slot() == current_time_slot) {
                    courses.get(i).setTime_slot(neighbor_time_slot);
                } else {
                    courses.get(i).setTime_slot(current_time_slot);
                }
            }
        }

        /* comparing obtained penalty with current penalty */
        if(current_penalty <= calculateAveragePenalty(students)) {
            /* undoing kempe-chain interchange */
            for(int i=0; i<courses.size(); i++) {
                if(courses.get(i).getDfsStatus()==2) {
                    if(courses.get(i).getTime_slot() == current_time_slot) {
                        courses.get(i).setTime_slot(neighbor_time_slot);
                    } else {
                        courses.get(i).setTime_slot(current_time_slot);
                    }
                }
            }
        }

        for(int i=0; i<courses.size(); i++) {
            if(courses.get(i).getDfsStatus()==2) {
                courses.get(i).setDfsStatus(0);
            }
        }
        return ;
    }

    private static void doPairSwapOperator(ArrayList<Student> students, Course u, Course v) {
        int u_time_slot=u.getTime_slot(), v_time_slot=v.getTime_slot();

        /* checking if pair swapping is possible */
        if(u_time_slot == v_time_slot) {
            return ;
        }

        Course[] overlappingCourses = u.getOverlappingCourses();
        for(int i=0; i<overlappingCourses.length; i++) {
            if(overlappingCourses[i].getTime_slot() == v_time_slot) {
                return ;
            }
        }

        overlappingCourses = v.getOverlappingCourses();
        for(int i=0; i<overlappingCourses.length; i++) {
            if(overlappingCourses[i].getTime_slot() == u_time_slot) {
                return ;
            }
        }

        /* do pair swap */
        double current_penalty = calculateAveragePenalty(students);
        u.setTime_slot(v_time_slot);
        v.setTime_slot(u_time_slot);

        /* comparing obtained penalty with current penalty */
        if(current_penalty <= calculateAveragePenalty(students)) {
            /* undoing pair swap */
            u.setTime_slot(u_time_slot);
            v.setTime_slot(v_time_slot);
        }

        return ;
    }

    private static void tryPenaltyReduction(ArrayList<Course> courses, ArrayList<Student> students, boolean isKempeChainInterchange) {
        Random random = new Random();

        /* iterating for penalty reduction */
        for(int i=0; i<3000; i++) {
            if(isKempeChainInterchange) {
                /* kempe-chain interchange */
                int current = random.nextInt(courses.size());
                Course[] overlappingCourses = courses.get(current).getOverlappingCourses();
                if(overlappingCourses.length != 0) {
                    doKempeChainInterchange(courses, students, courses.get(current), overlappingCourses[random.nextInt(overlappingCourses.length)].getTime_slot());
                }
            } else {
                /* pair swap operator */
                doPairSwapOperator(students, courses.get(random.nextInt(courses.size())), courses.get(random.nextInt(courses.size())));
            }
        }
        return ;
    }

    private static void runSchemeOn(String fileName) throws IOException {
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Student> students = new ArrayList<>();
        Scanner scanner;

        /* extracting input from .crs file */
        scanner = new Scanner(new File("toronto-dataset-input/"+fileName+".crs"));
        while(scanner.hasNextLine()) {
            String[] tempString = scanner.nextLine().split(" ");
            courses.add(new Course(tempString[0], Integer.parseInt(tempString[1])));
        }
        scanner.close();

        /* preparing conflict matrix */
        int[][] conflictMatrix = new int[courses.size()][courses.size()];
        for(int i=0; i<conflictMatrix.length; i++) {
            for(int j=0; j<conflictMatrix[0].length; j++) {
                conflictMatrix[i][j] = 0;
            }
        }

        /* extracting input from .stu file */
        int student_count = 0;
        scanner = new Scanner(new File("toronto-dataset-input/"+fileName+".stu"));
        while(scanner.hasNextLine()) {
            /* preparing enrolled courses array */
            String[] tempString = scanner.nextLine().split(" ");
            int[] tempInteger = new int[tempString.length];
            for(int i=0; i<tempInteger.length; i++) {
                tempInteger[i] = Integer.parseInt(tempString[i]);
            }

            /* adding to enrolledCourses */
            students.add(new Student());
            for(int i=0; i<tempInteger.length; i++) {
                students.get(student_count).addEnrolledCourse(courses.get(tempInteger[i]-1));
            }
            student_count++;

            /* updating conflict matrix */
            for(int i=0; i<tempInteger.length-1; i++) {
                for(int j=i+1; j<tempInteger.length; j++) {
                    if(conflictMatrix[tempInteger[i]-1][tempInteger[j]-1] == 0) {
                        conflictMatrix[tempInteger[i]-1][tempInteger[j]-1] = conflictMatrix[tempInteger[j]-1][tempInteger[i]-1] = 1;
                    }
                }
            }
        }
        scanner.close();

        /* adding to overlappingCourses */
        for(int i=0; i<conflictMatrix.length; i++) {
            for(int j=0; j<conflictMatrix[0].length; j++) {
                if(conflictMatrix[i][j] == 1) {
                    courses.get(i).addOverlappingCourse(courses.get(j));
                }
            }
        }

        /* applying constructive heuristic to yield an examination timetable */
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fileName).append(": ").append(timetableByLargestDegree(courses));
        //stringBuilder.append(fileName).append(": ").append(timetableByDSaturAlgorithm(courses));
        //stringBuilder.append(fileName).append(": ").append(timetableByLargestEnrollment(courses));
        //stringBuilder.append(fileName).append(": ").append(timetableByLargestWeightedDegree(courses, students));
        //stringBuilder.append(fileName).append(": ").append(timetableByRandomOrdering(courses));

        /* calculating average penalty before attempting to reduce it */
        stringBuilder.append(": ").append(calculateAveragePenalty(students));

        /* applying perturbative heuristics to reduce penalty */
        tryPenaltyReduction(courses, students, true);
        tryPenaltyReduction(courses, students, false);

        /* calculating average penalty after reducing it */
        stringBuilder.append(": ").append(calculateAveragePenalty(students));
        System.out.println(stringBuilder.toString());

        /* creating .sol file */
        File outputFile = new File("toronto-dataset-output/"+fileName+".sol");
        if(outputFile.exists()) {
            outputFile.delete();
        }
        outputFile.createNewFile();

        /* writing output to .sol file */
        FileWriter fileWriter = new FileWriter(outputFile);
        Collections.sort(courses, new TimeSlotComparator());
        for(int i=0; i<courses.size(); i++) {
            fileWriter.append(courses.get(i).toString()+"\n");
        }
        fileWriter.close();

        return ;
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Benchmark Data: total time slots: penalty before: penalty after");

        /* running corresponding scheme on each benchmark data */
        runSchemeOn("car-s-91");
        runSchemeOn("car-f-92");
        runSchemeOn("tre-s-92");
        runSchemeOn("yor-f-83");
        runSchemeOn("kfu-s-93");

        return ;
    }
}
