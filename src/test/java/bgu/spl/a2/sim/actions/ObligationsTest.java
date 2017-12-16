package bgu.spl.a2.sim.actions;


import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ObligationsTest {
    public static void main(String args[]){

        ActorThreadPool pool = new ActorThreadPool(6);
        pool.start();
        int n = 0;

        DepartmentPrivateState cs = new DepartmentPrivateState();
        pool.submit(null, "cs", cs);

        String[] prequisites1 = new String[0];
        OpenCourse createDS = new OpenCourse(5, "data structures", prequisites1);
        CoursePrivateState dslState = new CoursePrivateState();

        OpenCourse createCalculus = new OpenCourse(5, "calculus", prequisites1);
        CoursePrivateState CalState = new CoursePrivateState();

        OpenCourse createSex = new OpenCourse(5, "sexual harassment", prequisites1);
        CoursePrivateState SexState = new CoursePrivateState();

        String[] prequisites = new String[]{"data structures"};
        OpenCourse createSplAction = new OpenCourse(8, "spl", prequisites);
        CoursePrivateState splState = new CoursePrivateState();

        AddStudent createStudent1 = new AddStudent("amit", 0);
        StudentPrivateState student1State = new StudentPrivateState();

        AddStudent createStudent2 = new AddStudent("groot", 0);
        StudentPrivateState student2State = new StudentPrivateState();

        Unregister unregister = new Unregister("amit");
        Unregister unregister2 = new Unregister("amit");

        pool.submit(createDS, "cs", dslState);//add course
        pool.submit(createSplAction, "cs", splState);//add course
        pool.submit(createCalculus, "cs", CalState);
        pool.submit(createSex, "cs", SexState);
        pool.submit(createStudent1, "cs", student1State);// add student
        pool.submit(createStudent2, "cs", student2State);// add student

        Praticipate pr0 = new Praticipate("amit", "56");
        Praticipate pr = new Praticipate("amit", "90");
        Praticipate pr1 = new Praticipate("groot", "56");
        Praticipate pr2= new Praticipate("amit", "56");
        Praticipate pr3 = new Praticipate("amit", "56");

        CloseCourse closeds = new CloseCourse("data structures");

        pool.submit(pr1, "data structures", dslState);
        pool.submit(pr0, "data structures", dslState);
        Object lock = new Object();
        pr0.getResult().subscribe(()->{
            pool.submit(pr, "spl", splState);

        });
        while (!pool.getActors().containsKey("spl")){}
        //pool.submit(unregister, "spl", splState);
        //pool.submit(unregister2, "data structures", splState);
        //while (!((StudentPrivateState)pool.getPrivateState("amit")).getGrades().containsKey("spl")){}
           /* synchronized (lock) {
                try {
                    lock.wait(200);
                    //pool.shutdown();
                } catch (InterruptedException e) {
                    System.out.println("I hate you");
                }
            }*/
        //pool.submit(closeds, "cs" , cs);
        Warehouse warehouse = new Warehouse(3);
        Computer c1 = new Computer("bestPC");
        warehouse.addPC(c1);
        c1.setFailSig(55);
        c1.setSuccessSig(100);
        String[] students = {"amit"};
        String[] courses = {"data structures", "spl"};
        CheckObligations check1 = new CheckObligations(warehouse,students ,courses, "bestPC" );
        pool.submit(pr2, "calculus", CalState);
        pool.submit(check1, "cs", cs);
        pool.submit(pr3, "sexual harassment" , SexState);
        pool.submit(unregister, "spl", splState);



    }
}
