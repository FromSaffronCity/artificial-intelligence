package comparators;

import classes.Course;

import java.util.Comparator;

public class EnrollmentComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course2.getTotal_enrollment()-course1.getTotal_enrollment();
    }
}
