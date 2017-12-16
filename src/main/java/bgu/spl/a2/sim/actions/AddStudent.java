package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action {
    private String name;
    private long signature = 0;

    public AddStudent (String name){
        setActionName("Add Student");
        this.name = name;
    }
    @Override
    protected void start() {
        actorState.addRecord(getActionName());
        ((DepartmentPrivateState) actorState).addStudent(name);
        StudentPrivateState student = new StudentPrivateState();
        student.setSignature(signature);
        pool.submit(null, name, student);
        System.out.println("Add Student: "+name);
        complete(null);
    }
}
