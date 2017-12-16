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
        this.setActionName("Close A Course");

    }

    @Override
    protected void start() {
        this.actorState.addRecord(getActionName());
        if (((DepartmentPrivateState)this.actorState).getCourseList().contains(this.course)){
            TerminateCourse terminate = new TerminateCourse();
            LinkedList<Action> actions = new LinkedList<Action>();
            Promise promise = sendMessage(terminate, this.course, (CoursePrivateState)pool.getPrivateState(course));//todo make sure its not a syncronization problem
            actions.add(terminate);
            then(actions,()->{
                ((DepartmentPrivateState)this.actorState).getCourseList().remove(course);//remove course from the department
                System.out.println("removed course");
                complete(null);
            });
        } else {
            System.out.println("no such course: "+ course);
        }
    }
}
