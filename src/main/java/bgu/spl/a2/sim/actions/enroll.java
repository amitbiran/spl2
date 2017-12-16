package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.HashMap;
import java.util.List;

public class enroll extends Action {
    List<String> prequisites;
    String course;
    Integer grade;

    public enroll(List<String> prequisites,String course,Integer grade){
        super();
        this.prequisites = prequisites;
        this.course = course;
        this.grade = grade;
    }

    @Override
    protected void start() {
        //System.out.println("enroll");
        HashMap<String, Integer>grades = ((StudentPrivateState)this.actorState).getGrades();//get the grade of the student
        //System.out.println("hash");
        boolean  canEnroll = true;
        for(String course:prequisites){
            if(grades.get(course)==null||grades.get(course)<56){
                canEnroll = false;
                System.out.println("cant enroll");
                break;
            }
        }//for
        //System.out.println("for");
        if(canEnroll){
            System.out.println("enrolled");
            grades.put(course,grade);
        }
        complete(canEnroll);
       // System.out.println("enroll end");
    }
}
