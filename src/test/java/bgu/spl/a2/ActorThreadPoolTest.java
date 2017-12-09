package bgu.spl.a2;

import junit.framework.TestCase;

/**
 * Created by Lenovo on 12/9/2017.
 */
public class ActorThreadPoolTest extends TestCase {
    public void testSubmit() throws Exception {
    }

    public void testShutdown() throws Exception {
    }

    public void testStart() throws Exception {
        ActorThreadPool pool = new ActorThreadPool(5);
        pool.start();
        int i =1;
    }

}