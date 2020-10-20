package comparators;

import classes.Course;

import java.util.Comparator;

public class DegreeComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course2.getOverlappingCoursesNumber()-course1.getOverlappingCoursesNumber();
    }
}
