package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.LinkedList;
import java.util.List;

public class Praticipate extends Action {
    String student;
    Integer grade;


    public Praticipate (String student, String grade){
        try {
            this.grade = Integer.parseInt(grade);
        } catch (NumberFormatException e) {
            this.grade = null;
        }
        this.student = student;
        setActionName("Participating In Course");
    }
    @Override
    protected void start() {
        actorState.addRecord(getActionName());
        if (((CoursePrivateState)actorState).getAvailableSpots()==0){
            System.out.println("course is full: "+ this.actorId);
            return;
        }
        //System.out.println("course is not full");
        StudentPrivateState student = (StudentPrivateState)pool.getPrivateState(this.student);
        if (student ==null){
            System.out.println("no such Student: "+ this.student);
            return;
        }
        //System.out.println("student exist");

        List<String> prequisites = ((CoursePrivateState) actorState).getPrequisites();
        Action<Boolean> enroll = new enroll(prequisites,this.actorId,grade);
        List<Action<Boolean>> actions = new LinkedList<Action<Boolean>>();
        actions.add(enroll);//list that contains enroll
        Promise<Boolean> result =  sendMessage(enroll,this.student,student);//todo it returns a promise
        then(actions,()->{
            //System.out.println("then participate");
            if (result.get().booleanValue()) {
                //System.out.println("pokiolkjnm");
                if (((CoursePrivateState) actorState).getAvailableSpots() == 0) {
                    Action disenroll = new disenroll(this.actorId);
                    sendMessage(disenroll, this.student, student);
                } else {
                    ((CoursePrivateState) actorState).register_student(this.student);
                    System.out.println("student added");
                    complete(null);
                }
            }
            else System.out.println("student is stupid");
        });
        return;
    }


}
