package at.ac.tuwien.genben;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class GCUtil {

	public static void stabilizeMemory() {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		long oldUsed;
		long used;
        long change = Long.MAX_VALUE;
        long oldChange;
        int iterationCount = 0;
		do {
            oldChange = change;
			oldUsed = memoryMXBean.getHeapMemoryUsage().getUsed();
			System.gc();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            used = memoryMXBean.getHeapMemoryUsage().getUsed();
            change = Math.abs(oldUsed - used);
            System.out.println(change + " " + oldChange + " " + used*0.20);
            iterationCount++;
		} while (change != oldChange && iterationCount < 50);
	}
}
