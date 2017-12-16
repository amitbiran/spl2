package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class addPlaces extends Action {
    Integer places;
    public addPlaces(Integer placesToAdd){
        this.places = placesToAdd;
        this.setActionName("Opening New places In a Course");

    }

    @Override
    protected void start() {
        this.actorState.addRecord(getActionName());
       Integer spots =  ((CoursePrivateState)this.actorState).getAvailableSpots();
       if(spots.intValue()!=-1) {
           ((CoursePrivateState) this.actorState).setAvailableSpots(spots.intValue() + places.intValue());
           System.out.println("added places: "  + actorId + " " + places.intValue());
           complete(null);
       }
       else System.out.println("course " + actorId+ " is already closed cant add more places" );

    }
}
