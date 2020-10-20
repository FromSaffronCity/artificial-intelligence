package classes;

import java.util.ArrayList;

public class Course {
    private String courseID;
    private int total_enrollment;
    private int time_slot;
    private ArrayList<Course> overlappingCourses;

    private int conflict;
    private int dfsStatus;  // NOTICE: dfsStatus = 0(not explored), 1(in stack), 2(visited)

    public Course(String courseID, int total_enrollment) {
        this.courseID = courseID;
        this.total_enrollment = total_enrollment;
        time_slot = -1;
        overlappingCourses = new ArrayList<>();
    }

    public int getTotal_enrollment() {
        return total_enrollment;
    }

    public void setTime_slot(int time_slot) {
        this.time_slot = time_slot;
        return ;
    }

    public int getTime_slot() {
        return time_slot;
    }

    public void addOverlappingCourse(Course course) {
        overlappingCourses.add(course);
        return ;
    }

    public int getOverlappingCoursesNumber() {
        return overlappingCourses.size();
    }

    public Course[] getOverlappingCourses() {
        Course[] overlappingCourses = new Course[this.overlappingCourses.size()];
        for(int i=0; i<overlappingCourses.length; i++) {
            overlappingCourses[i] = this.overlappingCourses.get(i);
        }
        return overlappingCourses;
    }

    @Override
    public String toString() {
        return courseID+" "+time_slot;
    }

    public void setConflict(int conflict) {
        this.conflict = conflict;
        return ;
    }

    public int getConflict() {
        return conflict;
    }

    public void setDfsStatus(int dfsStatus) {
        this.dfsStatus = dfsStatus;
        return ;
    }

    public int getDfsStatus() {
        return dfsStatus;
    }
}
