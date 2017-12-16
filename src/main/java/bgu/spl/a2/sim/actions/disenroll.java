package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;

public class disenroll extends Action {
    String course;

    public disenroll(String course){
        this.course = course;
    }
    @Override
    protected void start() {
        System.out.println("disenroll");
        HashMap<String,Integer> grades = ((StudentPrivateState)this.actorState).getGrades();
        grades.remove(course);
        complete(null);
    }
}
