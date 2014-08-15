import java.util.List;
import java.util.LinkedList;

public final class ThreadPool {
	  private static ThreadPool pool=null;
	  private static int pool_size=5;
	  private volatile int finish_size=0;
	  private WorkThread[] workThread;
	  private List<Runnable> taskQueue;

	  private ThreadPool(){
	  	this(pool_size);
	  }
	  private ThreadPool(int pool_size){
	  	this.pool_size=pool_size;
	  	taskQueue=new LinkedList<Runnable>();
	  	workThread=new WorkThread[pool_size];
	  	for(int i=0;i<pool_size;i++){
	  		workThread[i]=new WorkThread();
	  		workThread[i].start();
	  	}
	  }
	  public static ThreadPool createThreadPool(){
	  	return createThreadPool(ThreadPool.pool_size);
	  }
	  public static ThreadPool createThreadPool(int pool_size){
	  	if(pool_size<=0)
	  		pool_size=ThreadPool.pool_size;
	  	if(pool==null)
	  		pool=new ThreadPool(pool_size);
	  	return pool;
	  }
	  public void execute(Runnable task){
	  	synchronized(taskQueue){
	  		taskQueue.add(task);
	  		taskQueue.notify();
	  	}
	  }
	  public void execute(Runnable[] tasks){
	  	synchronized(taskQueue){
	  		for(Runnable task : tasks){
	  			taskQueue.add(task);
	  		}
	  		taskQueue.notify();
	  	}
	  }
	  public int getThreadPoolSize(){
	  	return pool_size;
	  }
	  public int getFinishedThreadSize(){
	  	return finish_size;
	  }
	  public int getWaitingThreadSize(){
	  	return taskQueue.size();
	  }
	  public void destroy(){
	  	while(!taskQueue.isEmpty()){
	  		try{
	  			Thread.sleep(5);
	  		}catch(InterruptedException e){
	  			e.printStackTrace();
	  		}
	  	}
	  	for(int i=0;i<pool_size;i++){
	  		workThread[i].stopRunning();
	  		workThread[i]=null;
	  	}
	  	pool=null;
	  	taskQueue.clear();
	  }
	  @Override
	  public String toString(){
	  	return"ThreadPool Info:\nthread pool size: "+pool_size+"\nfinished thread num: "+getFinishedThreadSize()+"\nwaiting thread num: "+getWaitingThreadSize();
	  }

	  private class WorkThread extends Thread{
	  	private boolean isRunning=true;
	  	@Override
	  	public void run(){
	  		Runnable task=null;
	  		while(isRunning){
	  			synchronized(taskQueue){
	  				while(isRunning&&taskQueue.isEmpty()){
	  					try{
	  						taskQueue.wait(20);
	  					}catch(InterruptedException e){
	  						e.printStackTrace();
	  					}
	  				}
	  				if(!taskQueue.isEmpty())
	  					task=taskQueue.remove(0);
	  			}
	  			if(task!=null){
	  				task.run();
	  			}
	  			addFinishedThreadSize();
	  			task=null;
	  		}
	  	}
		private synchronized void addFinishedThreadSize(){
			finish_size++;
		}
	  	public void stopRunning(){
	  		isRunning=false;
	  	}
	  }
}