public class SimpleThread implements Runnable{
	private volatile static int num=0;

	@Override
	public void run(){
		num++;
		System.out.println("Simple thread num "+num);
	}
}