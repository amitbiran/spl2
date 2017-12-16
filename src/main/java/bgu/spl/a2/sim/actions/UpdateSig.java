package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class UpdateSig extends Action {
    private long signature;

    public UpdateSig (long signature){
        this.signature = signature;
        setActionName("Update Signature");
    }

    @Override
    protected void start() {
        ((StudentPrivateState)actorState).setSignature(signature);
        System.out.println(getActionName()+ " " + actorId + " " + signature);
        complete(null);
    }
}
