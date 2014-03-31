package at.ac.tuwien.genben;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

public class GCUtil {

	public static void stabilizeMemory() {
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		long oldUsed = 0;
		long used;
		do {
			used = memoryMXBean.getHeapMemoryUsage().getUsed();

			System.out.println(oldUsed);

			System.gc();

			oldUsed = used;
		} while (oldUsed != used);
	}
}
