package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;

public class Unregister extends Action {
    public String student;

    public Unregister (String student){
        this.student = student;
    }

    @Override
    protected void start() {
        StudentPrivateState student = (StudentPrivateState)pool.getPrivateState(this.student);
        if (student ==null){
            System.out.println("no such Student: "+ this.student);
            return;
        }
        Action disenroll = new disenroll(this.actorId);
        LinkedList<Action> actions = new LinkedList<Action>();
        actions.add(disenroll);
        Promise p = sendMessage(disenroll, this.student, student);
        then(actions, ()->{
            ((CoursePrivateState)this.actorState).remove_student(this.student);
        });
    }

}
