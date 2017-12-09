package bgu.spl.a2;

import jdk.nashorn.internal.objects.annotations.Constructor;
import junit.framework.TestCase;
import java.util.List;
import java.util.ArrayList;

class simpleAction extends Action{
    String text;


    public simpleAction(String text){
        this.text = text;
        result = new Promise();
    }


    @Override
    protected void start() {
       System.out.println(text);
        List<anotherAction> collection = new ArrayList<>();
        Action b = new anotherAction();
        collection.add((anotherAction)b);
        sendMessage(b,"actor1",new PrivateState() {});
        then(collection,()->{
            System.out.println("test 3 handle");
        });
    }
}

class anotherAction extends Action{

    public anotherAction(){
        result = new Promise();
    }
    @Override
    protected void start() {
        System.out.println("test 2 handle");
        result.resolve(null);
    }
}
/**
 * Created by Lenovo on 12/9/2017.
 */
public class ActionTest extends TestCase {
    public void testHandle() throws Exception {
        ActorThreadPool pool =new ActorThreadPool(5);
        String actorId = "actor1";
        PrivateState actorPrivateState = new PrivateState() {};
        Action a = new simpleAction("test 1 handle");
        pool.start();
        pool.submit(a,actorId,actorPrivateState);


    }

    public void testThen() throws Exception {
    }

    public void testComplete() throws Exception {
    }

    public void testGetResult() throws Exception {
    }

    public void testSendMessage() throws Exception {
    }

    public void testSetActionName() throws Exception {
    }

    public void testGetActionName() throws Exception {
    }

}