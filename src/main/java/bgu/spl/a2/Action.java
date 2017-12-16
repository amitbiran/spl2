package bgu.spl.a2;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * an abstract class that represents an action that may be executed using the
 * {@link ActorThreadPool}
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the action result type
 */

public abstract class Action<R> {
    protected Promise<R> result = new Promise<R>();
    protected String actorId;
    protected String actionName;
    protected PrivateState actorState;
    protected ActorThreadPool pool;
    protected callback cont=null;
    protected AtomicInteger count;
    Object lock =new Object();
	/**
     * start handling the action - note that this method is protected, a thread
     * cannot call it directly.
     */
    protected abstract void start();
    

    /**
    *
    * start/continue handling the action
    *
    * this method should be called in order to start this action
    * or continue its execution in the case where it has been already started.
    *
    * IMPORTANT: this method is package protected, i.e., only classes inside
    * the same package can access it - you should *not* change it to
    * public/private/protected
    *
    */
   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {
       this.actorId = actorId;
       this.actorState =actorState;
       this.pool = pool;
       //syncronize? todo
       if(cont!=null){
           cont.call();//make sure that cont is now incharge of resolving this action
           cont = null;//so it cant run again
       }
       else start();
   }
    
    
    /**
     * add a callback to be executed once *all* the given actions results are
     * resolved
     * 
     * Implementors note: make sure that the callback is running only once when
     * all the given actions completed.
     *
     * @param actions
     * @param callback the callback to execute once all the results are resolved
     */
    protected final void then(Collection<? extends Action<?>> actions, callback callback) {
        int num = actions.size();
        count = new AtomicInteger(num);
        cont = callback;//assume the action is out of queue
        for(Action<?>item:actions) {
            item.getResult().subscribe(()->{
             //   System.out.println("count is "+count);
                count.decrementAndGet();
                if(count.get()==0){
                    sendMessage(this, actorId, actorState);//put myself in the queue again
                }
            });
            /*
            callback c = ()->{
                synchronized (lock) {
                    int temp = count.get()-1;
                    count.set(temp);
                    if (count.get() == 0) {
                        sendMessage(this, actorId, actorState);//put myself in the queue again
                    }
                }
            };
           // synchronized (c) {
                item.getResult().subscribe(c);//make sure that each action count down as it finish
            //}//lock it on the action
            */
        }

    }

    /**
     * resolve the internal result - should be called by the action derivative
     * once it is done.
     *
     * @param result - the action calculated result
     */
    protected final void complete(R result) {
        getResult().resolve(result);
    }

    /**
     * @return action's promise (result)
     */
    public final Promise<R> getResult() {
        return result;
    }
    
    /**
     * send an action to an other actor
     * 
     * @param action
     * 				the action
     * @param actorId
     * 				actor's id
     * @param actorState
	 * 				actor's private state (actor's information)
	 *    
     * @return promise that will hold the result of the sent action
     */
	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState){
        pool.submit(action,actorId,actorState);
        return action.getResult();//return the promise
	}
	
	/**
	 * set action's name
	 * @param actionName
	 */
	public synchronized void setActionName(String actionName){//syncronized because if two threds try to change at the same time
        this.actionName = actionName;
	}
	
	/**
	 * @return action's name
	 */
	public String getActionName(){
        return actionName;
	}
}