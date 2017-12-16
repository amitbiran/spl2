package bgu.spl.a2.sim.actions;

import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import junit.framework.TestCase;

public class CloseCourseTest extends TestCase {


    public static void main(String args[]) throws InterruptedException {

        ActorThreadPool pool = new ActorThreadPool(6);
        pool.start();


        DepartmentPrivateState cs = new DepartmentPrivateState();
        pool.submit(null, "cs", cs);

        String[] prequisites1 = new String[0];
        OpenCourse createDS = new OpenCourse(5, "data structures", prequisites1);
        CoursePrivateState dslState = new CoursePrivateState();



        AddStudent createStudent1 = new AddStudent("amit", 100);
        StudentPrivateState student1State = new StudentPrivateState();



        pool.submit(createDS, "cs", dslState);//add course
        pool.submit(createStudent1, "cs", student1State);// add student

        Praticipate pr0 = new Praticipate("amit", "56");

        Object lock = new Object();
        synchronized (lock){
            lock.wait(1000);
        }

        pool.submit(pr0, "data structures", dslState);



        CloseCourse closeDs = new CloseCourse("data structures");

        pool.submit(closeDs,"cs",cs);

        synchronized (lock){
            lock.wait(150);
        }
        int i = 0;








    }

}