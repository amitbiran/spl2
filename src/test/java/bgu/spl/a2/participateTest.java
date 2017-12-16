package bgu.spl.a2;

import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.OpenCourse;
import bgu.spl.a2.sim.actions.Praticipate;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class participateTest
{
    public static void main(String args[]){

            ActorThreadPool pool = new ActorThreadPool(6);
            pool.start();


            DepartmentPrivateState cs = new DepartmentPrivateState();
            pool.submit(null, "cs", cs);

            String[] prequisites1 = new String[0];
            OpenCourse createDS = new OpenCourse(5, "data structures", prequisites1);
            CoursePrivateState dslState = new CoursePrivateState();

            String[] prequisites = new String[]{"data structures"};
            OpenCourse createSplAction = new OpenCourse(8, "spl", prequisites);
            CoursePrivateState splState = new CoursePrivateState();

            AddStudent createStudent1 = new AddStudent("amit", 100);
            StudentPrivateState student1State = new StudentPrivateState();

            Unregister unregister = new Unregister("amit");
            Unregister unregister2 = new Unregister("amit");

            pool.submit(createDS, "cs", splState);//add course
            pool.submit(createSplAction, "cs", splState);//add course
            pool.submit(createStudent1, "cs", student1State);// add student

            Praticipate pr0 = new Praticipate("amit", "56");
            Praticipate pr = new Praticipate("amit", "90");
            pool.submit(pr0, "data structures", dslState);
            Object lock = new Object();
            //pr0.getResult().subscribe(()->{
            pool.submit(pr, "spl", splState);
            //});
            while (!pool.getActors().containsKey("spl")){}
            pool.submit(unregister, "spl", splState);
            pool.submit(unregister2, "data structures", splState);
            /*synchronized (lock) {
                try {
                    lock.wait(20);
                    pool.shutdown();
                } catch (InterruptedException e) {
                    System.out.println("I hate you");
                }
            }*/




    }
}
