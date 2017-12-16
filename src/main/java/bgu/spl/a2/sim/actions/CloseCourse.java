package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class CloseCourse extends Action {
    String course;

    public CloseCourse (String course){
        this.course = course;
    }

    @Override
    protected void start() {
        if (((DepartmentPrivateState)this.actorState).getCourseList().contains(this.course)){
            TerminateCourse terminate = new TerminateCourse();
            Promise promise = sendMessage(terminate, this.course, (CoursePrivateState)pool.getPrivateState(course));
        } else {
            System.out.println("no such course: "+ course);
        }
    }
}
