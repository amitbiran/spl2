package bgu.spl.a2;

import org.omg.CORBA.VersionSpecHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lenovo on 12/9/2017.
 */
class confirmation extends Action{
    String clientA;
    String clientB;
    String bankB;
    PrivateState bankState;
    public confirmation(String clientA,String clientB,String bankB,PrivateState bankState){
    this.clientA = clientA;
    this.clientB =clientB;
    this.bankB =bankB;
    this.bankState =bankState;
    result = new Promise<Boolean>();
    }
    @Override
    protected void start() {
        result.resolve(true);//just for test lets say the other bank always approve the transaction
    }
}



class Transmission extends Action{
    int amount;
    String clientA;
    String clientB;
    String bankA;
    String bankB;
    PrivateState bankState;
    VersionMonitor vm ;
    public Transmission(int amount,String clientA,String clientB,String bankA,String bankB,PrivateState bankState){
        this.amount = amount;
        this.clientA =clientA;
        this.clientB = clientB;
        this.bankA =bankA;
        this.bankB =bankB;
        this.bankState =bankState;
        vm = new VersionMonitor();
        result = new Promise<String>();
    }

    public VersionMonitor getVm(){
        return vm;
    }
    protected void start(){
        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        actions.add(confAction);
        sendMessage(confAction, bankB, new PrivateState() {});
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result==true){
                complete("transmission good");
                System.out.println("transmission good");
                vm.inc();
            }
            else{
                complete("transmission bad");
                System.out.println("transmission bad");
            }
        });

    }

}

public class banksTest {

    public static void main(String[] args){
        ActorThreadPool pool = new ActorThreadPool(8);
        Action<String> trans = new Transmission(100,"A","B","bank1","bank2",new PrivateState(){});
        pool.start();
        pool.submit(trans,"bank1",new PrivateState() {});
        VersionMonitor l= new VersionMonitor();
        try {
            ((Transmission) trans).getVm().await(0);
            pool.shutdown();
        }
catch(Exception e){

        }


    }

}
