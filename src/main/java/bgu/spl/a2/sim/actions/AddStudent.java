package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;


public class AddStudent extends Action {
    String name;
    long signature;

    public AddStudent( int signature, String name){
        this.signature =signature;
        this.name = name;
    }
    @Override
    protected void start() {
        ((DepartmentPrivateState)actorState).addStudent(name);
        StudentPrivateState student = new StudentPrivateState();
        student.setSignature(signature);
        pool.submit(null,name,student);

    }
}
