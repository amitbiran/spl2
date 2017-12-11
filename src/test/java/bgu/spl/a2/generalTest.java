package bgu.spl.a2;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Created by Lenovo on 12/8/2017.
 */
public class generalTest extends TestCase {
    int a = 0;
    public void test()  {
        Promise<Integer> amit=new Promise<Integer>();
        amit.subscribe(()->{
            a+=1;
        });
        Promise<Integer> amit1=new Promise<Integer>();
        amit1.subscribe(()->{
            a+=1;
        });
        amit.resolve(0);
        amit1.resolve(0);
        System.out.println(a);
    }
}




