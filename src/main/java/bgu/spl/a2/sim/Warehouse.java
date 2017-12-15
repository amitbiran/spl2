package bgu.spl.a2.sim;

import java.util.List;

/**
 * represents a warehouse that holds a finite amount of computers
 *  and their suspended mutexes.
 * 
 */
public class Warehouse {
    private Computer pc_arr[];
    int index;
    public Warehouse(int n){
        pc_arr = new Computer[n];
        index =0;
    }

    public void addCmputer(Computer c){
        if(index == pc_arr.length)
            System.out.println("no room in warehouse for " + c);
       pc_arr[index]=c;
       index ++;
    }

    public Computer getComputer(int i){
        return pc_arr[i];
    }
}
