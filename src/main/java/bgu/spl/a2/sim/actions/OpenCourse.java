package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class OpenCourse extends Action {
    int capacity;
    String name;
    String[] preqisites;



    public OpenCourse(int capacity,String name,String[] preqisites){
        this.capacity =capacity;
        this.name = name;
        this.preqisites = preqisites;
    }

    @Override
    protected void start() {
        ((DepartmentPrivateState)actorState).addCourse(name);
        CoursePrivateState course = new CoursePrivateState();
        course.setSpots(capacity);
        course.setRegistered(0);
        for(String p : preqisites){
            course.addprequisites(p);
        }
        pool.submit(null,name,course);
    }
}
