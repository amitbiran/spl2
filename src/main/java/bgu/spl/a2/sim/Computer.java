package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;

public class Computer {

	String computerType;
	long failSig;
	long successSig;
	private  SuspendingMutex txt= new SuspendingMutex(this);


	public Computer(String computerType) {
		this.computerType = computerType;
	}
	
	/**
	 * this method checks if the courses' grades fulfill the conditions
	 * @param courses
	 * 							courses that should be pass
	 * @param coursesGrades
	 * 							courses' grade
	 * @return a signature if couersesGrades grades meet the conditions
	 */
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		long ans = successSig;
		for(String course:courses){
			if(coursesGrades.get(course)==null||coursesGrades.get(course)<56)ans = failSig;
		}
		return ans;
	}
}
