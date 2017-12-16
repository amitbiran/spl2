package bgu.spl.a2.sim;

import java.lang.reflect.Array;
import java.util.List;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse {
	private Computer PC_arr[];
	int index;


	public Warehouse (int PCnumber){
        PC_arr = new Computer[PCnumber];
        index = 0;
    }
    public void  addPC (Computer pc) {
	    if (index == PC_arr.length)
	        System.out.println("No room in WareHouse for: " + pc);
	    else {
            PC_arr[index] = pc;
            index++;
        }
    }
}
