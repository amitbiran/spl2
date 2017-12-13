package bgu.spl.a2;



        import org.omg.CORBA.VersionSpecHelper;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.concurrent.CountDownLatch;

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
   //     System.out.println("start conf");
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
        System.out.println("Start Transmission");

        List<Action<Boolean>> actions = new ArrayList<>();
        Action<Boolean> confAction1 = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        Action<Boolean> confAction = new confirmation(clientA,clientB,bankB,new PrivateState() {});
        actions.add(confAction);
        actions.add(confAction1);
        sendMessage(confAction1, bankB, new PrivateState() {});
        sendMessage(confAction, bankB, new PrivateState() {});
        then(actions,()->{
            Boolean result = actions.get(0).getResult().get();
            if(result==true){
                complete("transmission good");
                System.out.println("transmission good");
            }
            else{
                complete("transmission bad");
                System.out.println("transmission bad");
            }
        });

    }

}

public class banksTest {

    public static void main(String[] args) throws InterruptedException {
        for (int n = 0; n < 10001; n++) {
            System.out.println("=================================="+n);
            ActorThreadPool pool = new ActorThreadPool(2);
            Action<String> trans = new Transmission(100, "A", "B", "bank1", "bank2", new PrivateState() {
            });
            Action<String> trans1 = new Transmission(100, "B", "A", "bank2", "bank1", new PrivateState() {
            });
            Action<String> trans2 = new Transmission(100, "A", "B", "bank1", "bank3", new PrivateState() {
            });
            Action<String> trans3 = new Transmission(100, "A", "B", "bank3", "bank2", new PrivateState() {
            });
            Action<String> trans4 = new Transmission(100, "C", "B", "bank2", "bank3", new PrivateState() {
            });
            Action<String> trans5 = new Transmission(100, "A", "B", "bank1", "bank2", new PrivateState() {
            });



            CountDownLatch l = new CountDownLatch(6);
            trans.getResult().subscribe(() -> l.countDown());
            trans1.getResult().subscribe(() -> l.countDown());
            trans2.getResult().subscribe(() -> l.countDown());
            trans3.getResult().subscribe(() -> l.countDown());
            trans4.getResult().subscribe(() -> l.countDown());
            trans5.getResult().subscribe(() -> l.countDown());
            //  Action<String> trans6 = new Transmission(100,"A","B","bank1","bank2",new PrivateState(){});
            //Action<String> trans7 = new Transmission(100,"B","A","bank2","bank1",new PrivateState(){});
            //Action<String> trans8 = new Transmission(100,"A","B","bank1","bank3",new PrivateState(){});
            //Action<String> trans9 = new Transmission(100,"A","B","bank3","bank2",new PrivateState(){});
            //Action<String> trans10 = new Transmission(100,"C","B","bank2","bank3",new PrivateState(){});
            //Action<String> trans11 = new Transmission(100,"A","B","bank1","bank2",new PrivateState(){});


            pool.submit(trans, "bank1", new PrivateState() {
            });


            Object lock = new Object();

            pool.submit(trans2, "bank1", new PrivateState() {
            });
            synchronized (lock) {
                //     lock.wait(10);
            }
            pool.submit(trans3, "bank3", new PrivateState() {
            });

            pool.start();
            pool.submit(trans4, "bank2", new PrivateState() {
            });
            pool.submit(trans5, "bank1", new PrivateState() {
            });

            pool.submit(trans1, "bank2", new PrivateState() {
            });

            // pool.submit(trans6,"bank2",new PrivateState() {});
            //pool.submit(trans7,"bank1",new PrivateState() {});
            //pool.submit(trans8,"bank2",new PrivateState() {});
            //pool.submit(trans9,"bank1",new PrivateState() {});
            //pool.submit(trans10,"bank2",new PrivateState() {});
            //pool.submit(trans11,"bank1",new PrivateState() {});



            //   trans6.getResult().subscribe(()-> l.countDown());
            //  trans7.getResult().subscribe(()-> l.countDown());
            //  trans8.getResult().subscribe(()-> l.countDown());
            //  trans9.getResult().subscribe(()-> l.countDown());
            // trans10.getResult().subscribe(()-> l.countDown());
            // trans11.getResult().subscribe(()-> l.countDown());
            try {
                l.await();
            } catch (InterruptedException e) {
            }
            pool.shutdown();
        }
    }
}

/*
import org.omg.CORBA.VersionSpecHelper;

import java.util.ArrayList;
import java.util.List;


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
        System.out.println("start conf");
        complete(true);//just for test lets say the other bank always approve the transaction
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
        System.out.println("start transmission");
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
      System.out.println("error");
        }


    }

}
*/