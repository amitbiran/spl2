package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PromiseTest extends TestCase {
    public void testGet() throws Exception {
        try{
            Promise<Integer> testPromise  = new Promise<Integer>();
            //make sure i cant use get before resolved
            try{
                testPromise.get();//try to get but we cant because its not resolved yet
                Assert.fail();//make sure that if it passed we failed
            }
            catch(IllegalStateException e){
                assertEquals(testPromise.isResolved(),false);
            }
            //make sure after resolve get gives the resolved value
            try{
                testPromise.resolve(5);
                int getvalue = testPromise.get();
                assertEquals(getvalue,5);
                getvalue=0;
                getvalue=testPromise.get();
                assertEquals(getvalue,5);
                System.out.println("promise testGet completed");
            }
            catch(Exception e) {
                System.out.println("get Test 1 Failed!");
                Assert.fail();
            }
        }

        catch(UnsupportedOperationException e){
            System.out.println("Promise.get " + e.getMessage());
        }

        catch(Exception e){
            System.out.println("Unknown Error - Promise.get: " + e.getMessage());
            Assert.fail();
        }
    }

    public void testIsResolved() throws Exception {

        try{
            Promise<Integer> testPromise = new Promise<Integer>();
            try {
                assertEquals(testPromise.isResolved() , false);
            } catch (Exception e){
                System.out.println("isResolved Test 1 Failed!");
                Assert.fail();
            }
            try {
                testPromise.resolve(5);
                assertEquals(testPromise.isResolved() , true);
                System.out.println("promise testIsResolved completed");
            } catch (Exception e){
                System.out.println("isResolved Test 2 Failed!");
                Assert.fail();
            }

        } catch (UnsupportedOperationException e){
            System.out.println("Promise.isResolved " + e.getMessage());

        } catch (Exception e){
            System.out.println("Unknown Error - Promise.isResolved: " + e.getMessage());
            Assert.fail();
        }
    }

    public void testResolve() throws Exception {
        try {
            Promise<Integer> testPromise = new Promise<Integer>();
            try {
                testPromise.resolve(5);
                int value = testPromise.get();
                assertEquals(value , 5);
                assertEquals(testPromise.isResolved(), true);
            } catch (Exception e) {
                System.out.println("resolve Test 1 Failed!");
                Assert.fail();
            }
            try {
                testPromise.resolve(10);
                System.out.println("resolve Test 2 Failed!");
                Assert.fail();
            } catch (IllegalStateException e) {
                int value = testPromise.get();
                assertEquals(value, 5);
                System.out.println("promise testResolved 1 completed");
            }
            testPromise = new Promise<Integer>();
            testPromise.subscribe(()->{
                System.out.println("promise testResolved 2 completed");
            });
            testPromise.subscribe(()->{
                System.out.println("promise testResolved 3 completed");
            });
            testPromise.resolve(1);
            testPromise.subscribe(()->{
                System.out.println("promise testResolved 4 completed");
            });

        } catch (UnsupportedOperationException e) {
            System.out.println("Promise.resolve " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unknown Error - Promise.resolve: " + e.getMessage());
            Assert.fail();
        }
    }

    public void testSubscribe() throws Exception {
        try {
            Promise<Integer> testPromise = new Promise<Integer>();
            testPromise.subscribe(()->{
                System.out.println("testSubscribe test 1 passed");
            });
            testPromise.subscribe(()->{
                System.out.println("testSubscribe test 2 passed");
            });
            testPromise.resolve(5);
            testPromise.subscribe(()->{
                System.out.println("testSubscribe test 3 passed");
            });

        } catch (Exception e) {
            System.out.println("Unknown Error - Promise.subscribe: " + e.getMessage());
            Assert.fail();
        }
    }

}