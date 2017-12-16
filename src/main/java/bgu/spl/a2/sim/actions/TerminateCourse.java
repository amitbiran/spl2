package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class TerminateCourse extends Action {

    @Override
    protected void start() {
        ((CoursePrivateState)this.actorState).setAvailableSpots(0);
        ((CoursePrivateState)this.actorState).setRegistered(0);
        for (String student: ((CoursePrivateState)this.actorState).getRegStudents()){
            Unregister unregister = new Unregister(student);
            pool.submit(unregister, this.actorId, this.actorState);
        }
    }
}
