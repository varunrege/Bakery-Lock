package edu.vt.ece;

import edu.vt.ece.bench.Counter;
import edu.vt.ece.bench.SharedCounter;
import edu.vt.ece.bench.TestThread;
import edu.vt.ece.locks.*;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author Mohamed M. Saad
 */
public class Test {

	private static final int THREAD_COUNT = 64;
	
	private static final String LOCK_ONE = "LockOne";
	private static final String LOCK_TWO = "LockTwo";
	private static final String PETERSON = "Peterson";
	private static final String FILTER = "Filter";
	private static final String BAKERY = "Bakery";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		String lockClass = (args.length==0 ? BAKERY : args[0]);
			final Counter counter = new SharedCounter(0, (Lock)Class.forName("edu.vt.ece.locks." + lockClass).getConstructor(int.class).newInstance(THREAD_COUNT));
		for(int t=0; t<THREAD_COUNT; t++)
			new TestThread(counter).start();
	}
}
