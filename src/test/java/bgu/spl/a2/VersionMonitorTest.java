package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Lenovo on 12/3/2017.
 */
public class VersionMonitorTest extends TestCase {

    public void testGetVersion() throws Exception {
        try{
            VersionMonitor testVersionMonitor = new VersionMonitor();//create a mock versionmonitor it should call the java deafult constructor
            int version = testVersionMonitor.getVersion();// get initial version
            assertEquals(version, 0);//make sure initial version is 0
            System.out.println("versionMonitor testGetVersion completed");
        }
        catch(Exception e){
            System.out.println("VM.getVersion: " + e.getMessage());//make sure its the correct type of exception
            Assert.fail();
        }
    }

    public void testInc() throws Exception {
        try {
            VersionMonitor testVersionMonitor = new VersionMonitor();//create a mock versionmonitor it should call the java deafult constructor
            int version = testVersionMonitor.getVersion();// get initial version
            testVersionMonitor.inc();//make sure
            version = testVersionMonitor.getVersion();
            assertEquals(version, 1);
            testVersionMonitor.inc();//make sure
            version = testVersionMonitor.getVersion();
            assertEquals(version, 2);
            System.out.println("versionMonitor testInc completed");
        }
        catch(Exception e){
            System.out.println("VM.inc: " + e.getMessage());//make sure its the correct type of exception
            Assert.fail();
        }
    }

    public void testAwait() throws Exception {//todo
        VersionMonitor testVersionMonitor = new VersionMonitor();//create a mock versionmonitor it should call the java deafult constructor
        int testVersion = 3;
        Thread t1 = new Thread(() -> {
            try {
                testVersionMonitor.await(testVersion);
                System.out.println("test 4 versionMonitor testAwait completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });//create a thread that will now wait till i increase the version

        //create a thread that will increase todo

         try {


             t1.start();//now t1 will go into wait

             synchronized (this) {
                   this.wait(1000);//wait so it wait for a while
             }
             testVersionMonitor.inc();
             assertEquals(testVersionMonitor.getVersion(),1);
             System.out.println("test 1 versionMonitor testAwait completed");
             synchronized (this) {
                 this.wait(1000);//wait so it wait for a while
             }
             testVersionMonitor.inc();
             assertEquals(testVersionMonitor.getVersion(),2);
             System.out.println("test 2 versionMonitor testAwait completed");
             synchronized (this) {
                 this.wait(1000);//wait so it wait for a while
             }
             testVersionMonitor.inc();
             assertEquals(testVersionMonitor.getVersion(),3);
             System.out.println("test 3 versionMonitor testAwait completed");
             t1.join();
        }



        catch(Exception e){
            System.out.println("error: " + e.getMessage());
            Assert.fail();
        }

    }

}