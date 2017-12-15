package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class OpenCourse extends Action {
    DepartmentPrivateState department
    int capacity;
    String name;
    String[] preqisites;



    public OpenCourse(DepartmentPrivateState department,int capacity,String name,String[] preqisites){
        this.department = department;
        this.capacity =capacity;
        this.name = name;
        this.preqisites = preqisites;
    }

    @Override
    protected void start() {
        CoursePrivateState course = new CoursePrivateState();
        


    }
}
