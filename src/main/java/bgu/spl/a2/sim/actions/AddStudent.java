package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action {
    private String name;
    private long signature;

    public AddStudent (String name, long signature){
        this.name = name;
        this.signature = signature;
    }
    @Override
    protected void start() {
        ((DepartmentPrivateState) actorState).addStudent(name);
        StudentPrivateState student = new StudentPrivateState();
        student.setSignature(signature);
        pool.submit(null, name, student);
    }
}
