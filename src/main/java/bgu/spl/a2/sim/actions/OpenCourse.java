package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;



public class OpenCourse extends Action {
    private int capacity;
    private String name;
    private String[] prequisites;

    public OpenCourse (int capacity , String name, String[] prequisites){
        this.capacity = capacity;
        this.name = name;
        this.prequisites = prequisites;
        setActionName("Open A New Course");

    }


    @Override
    protected void start() {
        actorState.addRecord(getActionName());
        ((DepartmentPrivateState) this.actorState).addCourse(name);
        CoursePrivateState course = new CoursePrivateState();
        course.setAvailableSpots(capacity);
        course.setRegistered(0);
        for (String p :prequisites){
            course.add_prequisite(p);
        }
        System.out.println("Open Course: "+ name);
        pool.submit(null, name, course);

    }
}
