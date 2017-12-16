package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.SuspendingMutex;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

import java.util.LinkedList;

public class CheckObligations extends Action {
    private Warehouse warehouse;
    private String[] students;
    private String[] courses;
    private String computer;

    public CheckObligations (Warehouse warehouse, String[] students, String[] courses, String computer){
        setActionName("Administrative Check");
        this.warehouse = warehouse;
        this.students = students;
        this.courses = courses;
        this.computer = computer;
    }


    @Override
    protected void start() {
        actorState.addRecord(getActionName());
        Computer comp = warehouse.getPC(this.computer);
        SuspendingMutex mutex = warehouse.getMutex(this.computer);
        Promise p;
        synchronized (mutex) {
            p = mutex.down();
        }
        boolean first = p.isResolved();
        p.subscribe(() -> {
            LinkedList<String> courses = new LinkedList<String>();
            for (String c : this.courses)
                courses.add(c);
            for (String student : students) {
                long sig = comp.checkAndSign(courses, ((StudentPrivateState) pool.getPrivateState(student)).getGrades());
                UpdateSig updateSig = new UpdateSig(sig);
                sendMessage(updateSig, student, pool.getPrivateState(student));
            }


            if (first)
                //todo sync
                synchronized (mutex) {
                    mutex.up();
                }
            System.out.println("finish Obligation check " + actorId);
            complete(null);
        });

    }
}
