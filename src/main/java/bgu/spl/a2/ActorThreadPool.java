package bgu.spl.a2;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * represents an actor thread pool - to understand what this class does please
 * refer to your assignment.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class ActorThreadPool {

	/**
	 * creates a {@link ActorThreadPool} which has nthreads. Note, threads
	 * should not get started until calling to the {@link #start()} method.
	 *
	 * Implementors note: you may not add other constructors to this class nor
	 * you allowed to add any other parameter to this constructor - changing
	 * this may cause automatic tests to fail..
	 *
	 * @param nthreads
	 *            the number of threads that should be started by this thread
	 *            pool
	 */
	private BlockingQueue<Thread> nthreads;
	private ConcurrentHashMap<String,Queue> actors = new ConcurrentHashMap<String,Queue>();
	private ConcurrentHashMap<String,PrivateState> privateStates = new ConcurrentHashMap<String,PrivateState>();
	private ConcurrentHashMap<String,AtomicBoolean> locks = new ConcurrentHashMap<String,AtomicBoolean>();
	private VersionMonitor version =new VersionMonitor();
	private AtomicBoolean running= new AtomicBoolean(true);

	public ActorThreadPool(int nthreads) {
		//create n different threads
		this.nthreads = new LinkedBlockingQueue<Thread>();
		for(int i=0;i<nthreads;i++){
			this.nthreads.add(new Thread(()->{
				///////////////
				//System.out.println("thread: "+this.toString()+ " started");
				Action<?> action=null;
				while (running.get()) {
					boolean nothing_to_do =true;
					// System.out.println("itarate");

					for(String key:actors.keySet()){//itirate thruough the keys of the actors
						if(!running.get())break;//make sure we are still running
						Queue actor = actors.get(key);//get the actor
						synchronized (actor) {//go inside an actor and search for an action to do
							AtomicBoolean lock = locks.get(key);
							while(lock == null){
								lock = locks.get(key);
							}
							if(!lock.get()) { //if this actor is not locked go on if it is locked move on and stop blocking it
								if (!actor.isEmpty()) {//if u found an action to do

									try {
										action = ((LinkedBlockingQueue<Action<?>>) actor).take();//take the action
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									nothing_to_do = false;
									locks.get(key).set(true);//lock this actor so no one else can take actions from it
									String actionName = action.getActionName();//add this action to the history

									PrivateState p =privateStates.get(key);
									while(p==null){
										p=privateStates.get(key);
									}
									//p.addRecord(actionName);
								}
							}//if actor is not locked
						}//syncronized
						try {
							if (action != null) {//if we have an action to do then who ho lets handle it
								//synchronized (action) {
								action.handle(this, key, privateStates.get(key));//handle shouldnt be syncronized
								//}
								action = null;
								locks.get(key).set(false);//unlock this actor because we just finished with it
								int g=0;//dbugger
							}
						} catch (Exception e) {
							e.printStackTrace();//debugger
						}

					}//for
					//avoid busy wait
					if (nothing_to_do)try{ version.await(version.getVersion());}
					catch(Exception e){
					//	System.out.println("cant await: "+e);
						//System.out.println("cant await: "+e);
					}

				}//infinite while
				///////////////////

				//***********************
			}));
		}
	}

	/**
	 * getter for actors
	 * @return actors
	 */
	public Map<String, PrivateState> getActors(){
		return privateStates;
	}

	/**
	 * getter for actor's private state
	 * @param actorId actor's id
	 * @return actor's private state
	 */
	public PrivateState getPrivateState(String actorId){
		return privateStates.get(actorId);
	}

	/**
	 * submits an action into an actor to be executed by a thread belongs to
	 * this thread pool
	 *
	 * @param action
	 *            the action to execute
	 * @param actorId
	 *            corresponding actor's id
	 * @param actorState
	 *            actor's private state (actor's information)
	 */
	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		if(actors.containsKey(actorId)){//contains the actor
			try {
				((LinkedBlockingQueue)actors.get(actorId)).put(action);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NullPointerException e2) {

			}

			//	privateStates.remove(actorId);
			//	privateStates.put(actorId,actorState);
		}
		else{//does not contain the actor
			LinkedBlockingQueue<Action<?>> actor = 	new LinkedBlockingQueue<Action<?>>();
			try {
				if(action !=null)
					actor.put(action);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			privateStates.put(actorId,actorState);
			locks.put(actorId,new AtomicBoolean(false));//add an unlocked lock to this actor
			actors.put(actorId,actor);
		}
		version.inc();
	}

	/**
	 * closes the thread pool - this method interrupts all the threads and waits
	 * for them to stop - it is returns *only* when there are no live threads in
	 * the queue.
	 *
	 * after calling this method - one should not use the queue anymore.
	 *
	 * @throws InterruptedException
	 *             if the thread that shut down the threads is interrupted
	 */
	public void shutdown() throws InterruptedException {
		System.out.println("shutting down");//test only todo
		running.set(false);
		for(Thread t:nthreads){
			//t.interrupt();
		}
		for(Thread t:nthreads){
			t.join();
		}
	}

	/**
	 * start the threads belongs to this thread pool
	 */
	public void start() {
		for(Thread t:nthreads){
			t.start();
		}
	}
	/**
	 * Private States getter
	 * Written by Amitttttttt
	 * @return Praivate States
	 */
	public HashMap<String, PrivateState> getPrivateStates() {
		HashMap<String, PrivateState> res = new HashMap<String, PrivateState>();
		res.putAll(privateStates);

		return res;
	}
}