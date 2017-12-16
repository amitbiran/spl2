package bgu.spl.a2;

import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class jsonTest {

    public  static void main(String[] args) throws FileNotFoundException {
        String path = "C:\\Users\\AmiramL\\Documents\\Studies\\Year 3\\SPL\\Assignment2\\a2\\src\\test\\java\\bgu\\spl\\a2\\test.json";
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));

        Gson gson = new Gson();
        Object json = gson.fromJson(bufferedReader, Object.class);
        Double threads0 = (Double) ((LinkedTreeMap)json).get("threads");
        ArrayList computers = (ArrayList) ((LinkedTreeMap)json).get("Computers");
        ArrayList p1 = (ArrayList) ((LinkedTreeMap)json).get("Phase 1");
        ArrayList p2 = (ArrayList) ((LinkedTreeMap)json).get("Phase 2");
        ArrayList p3 = (ArrayList) ((LinkedTreeMap)json).get("Phase 3");
        //todo Parse thread number
        int threads = (int)threads0.doubleValue();
        Warehouse warehouse =new Warehouse(computers.size());
        //todo Parse Computers
        for (Object M: computers){
            LinkedTreeMap computer =(LinkedTreeMap)M;
            Computer pc = new Computer((String)computer.get("Type"));
            pc.setFailSig(Long.parseLong((String)computer.get("Sig Fail")));
            pc.setSuccessSig(Long.parseLong((String)computer.get("Sig Success")));
            warehouse.addPC(pc);
        }
        ActorThreadPool pool = new ActorThreadPool(threads);
        //PriorityQueue<Action> P1 = new PriorityQueue<Action>();
        LinkedTreeMap action =(LinkedTreeMap)p3.get(0);
        pool.submit(null, "Data Bases", new PrivateState(){});
        pool.submit(null, "CS", new PrivateState(){});
        addAction(pool,action);

        /*for (Object M: p1){
            LinkedTreeMap action =(LinkedTreeMap)M;
            addAction(pool, action);
            //todo this is good
        }*/

        //

        int n = 0;
    }

    static class BagOfPrimitives {
        private int value1 = 1;
        private String value2 = "abc";
        private transient int value3 = 3;
        BagOfPrimitives() {
            // no-args constructor
        }
    }

    /**
     * best function in the world!!!!!!!!! checked
     * @param pool
     * @param action
     */
    public static void addAction (ActorThreadPool pool, LinkedTreeMap action){
        String name = ((String)action.get("Action"));
        Warehouse warehouse = new Warehouse(5);// todo bom remove this warehouse!!!!!!badnbadbadbabdabdabd
        switch (name){
            case "Open Course":{
                String department = ((String)action.get("Department"));
                String courseName = ((String)action.get("Course"));
                int spaces = (Integer.parseInt((String)action.get("Space")));
                ArrayList<String> Preq = ((ArrayList<String>)action.get("Prerequisites"));
                String[] preq = new String[Preq.size()];
                for (int i = 0; i < preq.length; i++)
                    preq[i] = Preq.get(i);
                PrivateState departmentPS;
                if (!pool.getActors().containsKey(department)){
                    departmentPS = new DepartmentPrivateState();
                    pool.submit(null, department, departmentPS);
                } else {
                    departmentPS = pool.getPrivateState(department);
                }
                OpenCourse openCourse = new OpenCourse(spaces, courseName, preq);
                pool.submit(openCourse, department, departmentPS);
            }

            break;
            case "Add Student": {
                String student = ((String) action.get("Student"));
                String department = ((String) action.get("Department"));
                PrivateState departmentPS;
                if (!pool.getActors().containsKey(department)){
                    departmentPS = new DepartmentPrivateState();
                    pool.submit(null, department, departmentPS);
                } else {
                    departmentPS = pool.getPrivateState(department);
                }
                AddStudent addStudent = new AddStudent(student);
                pool.submit(addStudent, department, departmentPS);
            }
            break;
            case "Participate In Course":{
                String student = ((String) action.get("Student"));
                String course = ((String) action.get("Course"));
                String grade = ((String) ((ArrayList)action.get("Grade")).get(0));
                if (!pool.getActors().containsKey(course)){
                    System.out.println(name+" ERROR - no Course: "+course);
                    return;
                }
                Praticipate praticipate = new Praticipate(student, grade);
                pool.submit(praticipate, course, pool.getPrivateState(course));
            }
            break;
            case "Add Spaces":{
                String course = ((String) action.get("Course"));
                if (!pool.getActors().containsKey(course)){
                    System.out.println(name+" ERROR - no Course: "+course);
                    return;
                }
                String Spaces = ((String) action.get("Number"));
                Integer spaces = Integer.parseInt(Spaces);
                addPlaces addPlaces = new addPlaces(spaces);
                pool.submit(addPlaces, course, pool.getPrivateState(course));
            }
            break;
            case "Register With Preferences":{

            }
                break;
            case "Unregister":{
                String course = ((String) action.get("Course"));
                if (!pool.getActors().containsKey(course)){
                    System.out.println(name+" ERROR - no Course: "+course);
                    return;
                }
                String student = ((String) action.get("Student"));
                Unregister unregister = new Unregister(student);
                pool.submit(unregister, course, pool.getPrivateState(course));
            }
            break;
            case "Close Course":{
                String department = ((String)action.get("Department"));
                if (!pool.getActors().containsKey(department)){
                    System.out.println(name+" ERROR - no Department: "+department);
                    return;
                }
                String course = ((String)action.get("Course"));
                if (!pool.getActors().containsKey(course)){
                    System.out.println(name+" ERROR - no Course: "+course);
                    return;
                }
                CloseCourse closeCourse = new CloseCourse(course);
                pool.submit(closeCourse, department, pool.getPrivateState(department));
            }
            break;
            case "Administrative Check":{
                String department = ((String)action.get("Department"));
                if (!pool.getActors().containsKey(department)){
                    System.out.println(name+" ERROR - no Department: "+department);
                    return;
                }
                String computer = ((String)action.get("Computer"));
                ArrayList<String> Students = ((ArrayList<String>)action.get("Students"));
                String[] students = new String[Students.size()];
                for (int i = 0; i < students.length; i++)
                    students[i] = Students.get(i);
                ArrayList<String> Courses = ((ArrayList<String>)action.get("Conditions"));
                String[] courses = new String[Courses.size()];
                for (int i = 0; i < courses.length; i++)
                    courses[i] = Courses.get(i);
                CheckObligations checkObligations = new CheckObligations(warehouse, students, courses, computer);
                pool.submit(checkObligations, department, pool.getPrivateState(department));
            }
            break;

        }
    }
}
