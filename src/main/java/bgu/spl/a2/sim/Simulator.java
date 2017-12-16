/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.actions.*;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {
     public static Warehouse warehouse;
	 public static ActorThreadPool actorThreadPool;
	 public static Object json;

	/**
	 * Begin the simulation Should not be called before attachActorThreadPool()
	 */
	public static void start(){

        //get computers
		ArrayList[] JSON = ParseJson();

		 warehouse = new Warehouse(JSON[0].size());
		//todo Parse Computers

		for (Object M: JSON[0]){//JSON[0] - Computers
			LinkedTreeMap computer =(LinkedTreeMap)M;
			Computer pc = new Computer((String)computer.get("Type"));
			pc.setFailSig(Long.parseLong((String)computer.get("Sig Fail")));
			pc.setSuccessSig(Long.parseLong((String)computer.get("Sig Success")));
			warehouse.addPC(pc);
		}
		actorThreadPool.start();
		for (Object M: JSON[1]){//JSON[1] - Phase 1
			LinkedTreeMap action =(LinkedTreeMap)M;
			addAction( action);
			//todo this is good
		}
		synchronized (json){
			try {
				json.wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Object M: JSON[2]){//JSON[2] - Phase 2
			LinkedTreeMap action =(LinkedTreeMap)M;
			addAction( action);
			//todo this is good
		}

		synchronized (json){
			try {
				json.wait(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		for (Object M: JSON[3]){//JSON[3] - Phase 3
			LinkedTreeMap action =(LinkedTreeMap)M;
			addAction( action);
			//todo this is good
		}



		//start the pool


	}

	/**
	 * attach an ActorThreadPool to the Simulator, this ActorThreadPool will be used to run the simulation
	 *
	 * @param myActorThreadPool - the ActorThreadPool which will be used by the simulator
	 */
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool = myActorThreadPool;
	}

	/**
	 * shut down the simulation
	 * returns list of private states
	 */
	public static HashMap<String,PrivateState> end(){
		try {
			actorThreadPool.shutdown();
			return actorThreadPool.getPrivateStates();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;

	}


	public static void main(String [] args){
		//TODO: replace method body with real implementation
		//json read
		String path = "C:\\Users\\bamit\\Desktop\\Personal\\spl2-master\\src\\test\\java\\bgu\\spl\\a2\\test.json";
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(path));
		} catch (FileNotFoundException e) {
			System.out.println("could not read the file " + path);
		}
		Gson gson = new Gson();
		json = gson.fromJson(bufferedReader, Object.class);

		//get number of threads
		Double threads0 = (Double) ((LinkedTreeMap)json).get("threads");
		int threads = (int)threads0.doubleValue();
		attachActorThreadPool(new ActorThreadPool(threads));
		start();


	//	return 0;
	}

	public static ArrayList[] ParseJson () {
		ArrayList[] JSON = new ArrayList[4];
		//get Computers
		JSON[0] = (ArrayList) ((LinkedTreeMap)json).get("Computers");

		//get phases
		JSON[1] = (ArrayList) ((LinkedTreeMap)json).get("Phase 1");
		JSON[2] = (ArrayList) ((LinkedTreeMap)json).get("Phase 2");
		JSON[3] = (ArrayList) ((LinkedTreeMap)json).get("Phase 3");
		return JSON;
	}


	/**
	 * best function in the world!!!!!!!!! checked
	 * @param action
	 */
	public static void addAction ( LinkedTreeMap action){
		String name = ((String)action.get("Action"));
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
				if (!actorThreadPool.getActors().containsKey(department)){
					departmentPS = new DepartmentPrivateState();
					actorThreadPool.submit(null, department, departmentPS);
				} else {
					departmentPS = actorThreadPool.getPrivateState(department);
				}
				OpenCourse openCourse = new OpenCourse(spaces, courseName, preq);
				actorThreadPool.submit(openCourse, department, departmentPS);
			}
			break;
			case "Add Student": {
				String student = ((String) action.get("Student"));
				String department = ((String) action.get("Department"));
				PrivateState departmentPS;
				if (!actorThreadPool.getActors().containsKey(department)){
					departmentPS = new DepartmentPrivateState();
					actorThreadPool.submit(null, department, departmentPS);
				} else {
					departmentPS = actorThreadPool.getPrivateState(department);
				}
				AddStudent addStudent = new AddStudent(student);
				actorThreadPool.submit(addStudent, department, departmentPS);
			}
			break;
			case "Participate In Course":{
				String student = ((String) action.get("Student"));
				String course = ((String) action.get("Course"));
				String grade = ((String) ((ArrayList)action.get("Grade")).get(0));
				if (!actorThreadPool.getActors().containsKey(course)){
					System.out.println(name+" ERROR - no Course: "+course);
					return;
				}
				Praticipate praticipate = new Praticipate(student, grade);
				actorThreadPool.submit(praticipate, course, actorThreadPool.getPrivateState(course));
			}
			break;
			case "Add Spaces":{
				String course = ((String) action.get("Course"));
				if (!actorThreadPool.getActors().containsKey(course)){
					System.out.println(name+" ERROR - no Course: "+course);
					return;
				}
				String Spaces = ((String) action.get("Number"));
				Integer spaces = Integer.parseInt(Spaces);
				addPlaces addPlaces = new addPlaces(spaces);
				actorThreadPool.submit(addPlaces, course, actorThreadPool.getPrivateState(course));
			}
			break;
			case "Register With Preferences":{

			}
			break;
			case "Unregister":{
				String course = ((String) action.get("Course"));
				if (!actorThreadPool.getActors().containsKey(course)){
					System.out.println(name+" ERROR - no Course: "+course);
					return;
				}
				String student = ((String) action.get("Student"));
				Unregister unregister = new Unregister(student);
				actorThreadPool.submit(unregister, course, actorThreadPool.getPrivateState(course));
			}
			break;
			case "Close Course":{
				String department = ((String)action.get("Department"));
				if (!actorThreadPool.getActors().containsKey(department)){
					System.out.println(name+" ERROR - no Department: "+department);
					return;
				}
				String course = ((String)action.get("Course"));
				if (!actorThreadPool.getActors().containsKey(course)){
					System.out.println(name+" ERROR - no Course: "+course);
					return;
				}
				CloseCourse closeCourse = new CloseCourse(course);
				actorThreadPool.submit(closeCourse, department, actorThreadPool.getPrivateState(department));
			}
			break;
			case "Administrative Check":{
				String department = ((String)action.get("Department"));
				if (!actorThreadPool.getActors().containsKey(department)){
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
				actorThreadPool.submit(checkObligations, department, actorThreadPool.getPrivateState(department));
			}
			break;

		}
	}
}
