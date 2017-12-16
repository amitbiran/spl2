package bgu.spl.a2.sim.privateStates;

import java.util.LinkedList;
import java.util.List;

import bgu.spl.a2.PrivateState;

/**
 * this class describe course's private state
 */
public class CoursePrivateState extends PrivateState{

	private Integer availableSpots;
	private Integer registered;
	private List<String> regStudents;
	private List<String> prequisites;
	
	/**
 	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 */
	public CoursePrivateState() {
		availableSpots = 0;
		registered = 0;
		regStudents = new LinkedList<String>();
		prequisites = new LinkedList<String>();
	}

	public Integer getAvailableSpots() {
		return availableSpots;
	}

	public Integer getRegistered() {
		return registered;
	}

	public List<String> getRegStudents() {
		return regStudents;
	}

	public List<String> getPrequisites() {
		return prequisites;
	}

	public void setAvailableSpots (Integer availableSpots){
		this.availableSpots = availableSpots;
	}
	public void setRegistered (Integer registered){
		this.registered = registered;
	}
	public void setPrequisites (List prequisites){
		this.prequisites = prequisites;
	}
	public void setRegStudents (List regStudents){
		this.regStudents = regStudents;
	}
	public void register_student (String name) {
		if (availableSpots > 0){
			regStudents.add(name);
			availableSpots--;
			registered++;
		} else {
			System.out.println("Course is Full!!");
		}
	}
	public void remove_student (String name){
		if (regStudents.contains(name)) {
			regStudents.remove(name);
			registered--;
			availableSpots++;
			System.out.println("Sent him home");
		} else
			System.out.println("Me dont know him");
	}
	public void add_prequisite (String prequisite){
		prequisites.add(prequisite);
	}
}
