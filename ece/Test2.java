package edu.vt.ece;

import edu.vt.ece.bench.Counter;
import edu.vt.ece.bench.SharedCounter;
import edu.vt.ece.bench.TestThread;
import edu.vt.ece.bench.TestThread2;
import edu.vt.ece.locks.*;

import java.lang.reflect.InvocationTargetException;

/**
 * 
 * @author Balaji Arun
 */
public class Test2 {
	
	private static final String LOCK_ONE = "LockOne";
	private static final String LOCK_TWO = "LockTwo";
	private static final String PETERSON = "Peterson";
	private static final String FILTER = "Filter";
	private static final String BAKERY = "Bakery";

	public static void main(String[] args) throws InstantiationException, IllegalAccessException, ClassNotFoundException, InterruptedException, NoSuchMethodException, InvocationTargetException {
		String lockClass = (args.length == 0 ? FILTER : args[0]);
		int threadCount = (args.length <= 1 ? 16 : Integer.parseInt(args[1]));
		int totalIters = (args.length <= 2 ? 64000 : Integer.parseInt(args[2]));
		int iters = totalIters / threadCount;

		for (int i = 0; i < 1; i++) {
			run(lockClass, threadCount, iters);
		}
	}

	private static void run(String lockClass, int threadCount, int iters) throws InterruptedException, ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
		final Counter counter = new SharedCounter(0, (Lock)Class.forName("edu.vt.ece.locks." + lockClass).getConstructor(int.class).newInstance(threadCount));
		final TestThread2[] threads = new TestThread2[threadCount];
		TestThread2.reset();

		for(int t=0; t<threadCount; t++) {
			threads[t] = new TestThread2(counter, iters);
		}

		for(int t=0; t<threadCount; t++) {
			threads[t].start();
		}

		long totalTime = 0;
		for(int t=0; t<threadCount; t++) {
			threads[t].join();
			totalTime += threads[t].getElapsedTime();
		}

		System.out.println("Average time per thread is " + totalTime/threadCount + "ms");
	}
}
