package classes;

import comparators.TimeSlotComparator;

import java.util.ArrayList;
import java.util.Collections;

public class Student {
    private ArrayList<Course> enrolledCourses;

    public Student() {
        enrolledCourses = new ArrayList<>();
    }

    public void addEnrolledCourse(Course course) {
        enrolledCourses.add(course);
        return ;
    }

    public Course[] getEnrolledCourses() {
        Course[] enrolledCourses = new Course[this.enrolledCourses.size()];
        for(int i=0; i<enrolledCourses.length; i++) {
            enrolledCourses[i] = this.enrolledCourses.get(i);
        }
        return enrolledCourses;
    }

    public double calculateTotalPenalty() {
        double total_penalty = 0;
        Collections.sort(enrolledCourses, new TimeSlotComparator());

        for(int i=0; i<enrolledCourses.size()-1; i++) {
            for(int j=i+1; j<enrolledCourses.size(); j++) {
                int gap = enrolledCourses.get(j).getTime_slot()-enrolledCourses.get(i).getTime_slot();
                if(gap < 6) {
                    total_penalty += Math.pow(2, 5-gap);
                }
            }
        }
        return total_penalty;
    }
}
