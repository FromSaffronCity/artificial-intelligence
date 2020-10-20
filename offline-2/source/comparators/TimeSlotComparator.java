package comparators;

import classes.Course;

import java.util.Comparator;

public class TimeSlotComparator implements Comparator<Course> {
    @Override
    public int compare(Course course1, Course course2) {
        return course1.getTime_slot()-course2.getTime_slot();
    }
}
