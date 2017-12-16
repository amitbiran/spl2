package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;

public class TerminateCourse extends Action {

    public TerminateCourse(){
        this.setActionName("Terminate Course");
    }

    @Override
    protected void start() {
        ((CoursePrivateState)this.actorState).setAvailableSpots(-1);
        ((CoursePrivateState)this.actorState).setRegistered(-1);
        LinkedList<Action> actions = new LinkedList<Action>();
        for (String student: ((CoursePrivateState)this.actorState).getRegStudents()){
            Unregister unregister = new Unregister(student);// unregister already send the student action to delete him
            sendMessage(unregister, this.actorId, this.actorState);
            actions.add(unregister);
        }
        then(actions,()->{
            complete(null);
        });
    }
}
