/**
 * @(#)ThreadPool.java
 *
 * @author Black
 *
 * @version 1.00 2014/8/11
 */

public class ThreadPoolTest {

    public static void main(String[] args) {
		ThreadPool pool=ThreadPool.createThreadPool(6);
		pool.execute(new Runnable[]{new SimpleThread(),new SimpleThread(),new SimpleThread()});
		pool.execute(new Runnable[]{new SimpleThread(),new SimpleThread(),new SimpleThread()});
		pool.execute(new Runnable[]{new SimpleThread(),new SimpleThread(),new SimpleThread()});
		System.out.println("-----------------------\n"+pool+"\n-----------------------");
		pool.destroy();
		System.out.println("-----------------------\n"+pool+"\n-----------------------");
    }
}