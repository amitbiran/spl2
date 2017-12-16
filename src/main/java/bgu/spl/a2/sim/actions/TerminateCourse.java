package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

import java.util.LinkedList;

public class TerminateCourse extends Action {
    public TerminateCourse(){
        setActionName("Terminate Course");
    }

    @Override
    protected void start() {
        ((CoursePrivateState)this.actorState).setAvailableSpots(-1);
        ((CoursePrivateState)this.actorState).setRegistered(0);
        LinkedList<Action> actions = new LinkedList<Action>();
        if (((CoursePrivateState)this.actorState).getRegStudents().size() == 0){
            complete(null);
            return;
        }
        for (String student: ((CoursePrivateState)this.actorState).getRegStudents()){
            Unregister unregister = new Unregister(student);
            sendMessage(unregister, this.actorId, this.actorState);
            actions.add(unregister);
            System.out.println("Removed: "+ student + " because he is stupid");
        }
        this.then(actions, ()->{
            System.out.println("Terminate " + actorId);
            complete(null);
        });

    }
}
