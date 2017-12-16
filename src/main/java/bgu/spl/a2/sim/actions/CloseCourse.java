package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

import java.util.LinkedList;

public class CloseCourse extends Action {
    String course;

    public CloseCourse (String course){
        this.course = course;
        setActionName("Close A Course");
    }

    @Override
    protected void start() {
        actorState.addRecord(getActionName());
        LinkedList<Action> actions = new LinkedList<Action>();
        if (((DepartmentPrivateState)this.actorState).getCourseList().contains(this.course)){
            TerminateCourse terminate = new TerminateCourse();
            Promise promise = sendMessage(terminate, this.course, (CoursePrivateState)pool.getPrivateState(course));
            actions.add(terminate);
        } else {
            System.out.println("no such course: "+ course);
            return;
        }

        then(actions, ()->{
            ((DepartmentPrivateState)this.actorState).getCourseList().remove(course);
            System.out.println("Removed course: "+ course);
            complete(null);
        });
    }
}
