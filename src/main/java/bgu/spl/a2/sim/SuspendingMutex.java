package bgu.spl.a2.sim;
import bgu.spl.a2.Promise;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 *
 * this class is related to {@link Computer}
 * it indicates if a computer is free or not
 *
 * Note: this class can be implemented without any synchronization.
 * However, using synchronization will be accepted as long as the implementation is blocking free.
 *
 */
public class SuspendingMutex {
	private Computer computer;
	AtomicBoolean isFree = new AtomicBoolean(true);
	Queue<Promise> promises = new PriorityQueue<Promise>();
	/**
	 * Constructor
	 * @param computer
	 */
	public SuspendingMutex(Computer computer){
		this.computer = computer;
	}
	/**
	 * Computer acquisition procedure
	 * Note that this procedure is non-blocking and should return immediatly
	 *
	 * @return a promise for the requested computer
	 */
	public Promise<Computer> down(){
		Promise<Computer> p = new Promise<Computer>();
		if (!isFree.get()){
			promises.add(p);
			return p;
		}
		isFree.set(false);
		p.resolve(computer);
		return p;
	}
	/**
	 * Computer return procedure
	 * releases a computer which becomes available in the warehouse upon completion
	 */
	public void up(){
		for (Promise p: promises)
			p.resolve(this);
		isFree.set(true);
	}

}