package fr.datasyscom.scopiom.rest.util;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

/**
 * This class offers methods to get information on the Operating System.
 * <p>
 * This class handles both SUN JVM and IBM JVM.
 * 
 * @author julien
 * @see OperatingSystemMXBean
 * @see RuntimeMXBean
 */
public class OperatingSystemInfos {

	/** Operating System Bean (JVM specific) */
	private OperatingSystemMXBean osBean;
	/** Runtime Bean (JVM specific) */
	private RuntimeMXBean rtBean;
	/**
	 * Class determined dynamically using reflection for the Operating System
	 * Bean for the current JVM
	 */
	private Class<?> osMxBeanclazz;
	/**
	 * Class determined dynamically using reflection for the Runtime
	 * Bean for the current JVM
	 */

	public OperatingSystemInfos() {
		this.osBean = ManagementFactory.getOperatingSystemMXBean();
		this.rtBean = ManagementFactory.getRuntimeMXBean();
		try {
			// Try to cast for SUN JVM
			osMxBeanclazz = Class.forName("com.sun.management.OperatingSystemMXBean");
		} catch (Exception e) {
			try {
				// Try to cast for IBM JVM
				osMxBeanclazz = Class.forName("com.ibm.lang.management.OperatingSystemMXBean");
			} catch (Exception e1) {
				// Nothing we can do now, methods will return default values
			}
		}
	}

	/**
	 * Returns the amount of virtual memory that is guaranteed to be available
	 * to the running process in bytes, or -1 if this operation is not
	 * supported.
	 * 
	 * @return the amount of virtual memory that is guaranteed to be available
	 *         to the running process in bytes, or -1 if this operation is not
	 *         supported.
	 */
	public long getCommittedVirtualMemorySize() {
		return callOsBeanMethod("getCommittedVirtualMemorySize", -1L);
	}

	/**
	 * Returns the total amount of swap space in bytes.
	 * 
	 * @return the total amount of swap space in bytes, or -1 if this operation
	 *         is not supported.
	 */
	public long getTotalSwapSpaceSize() {
		return callOsBeanMethod("getTotalSwapSpaceSize", -1L);
	}

	/**
	 * Returns the amount of free swap space in bytes.
	 * 
	 * @return the amount of free swap space in bytes, or -1 if this operation
	 *         is not supported.
	 */
	public long getFreeSwapSpaceSize() {
		return callOsBeanMethod("getFreeSwapSpaceSize", -1L);
	}

	/**
	 * Returns the CPU time used by the process on which the Java virtual
	 * machine is running in nanoseconds. The returned value is of nanoseconds
	 * precision but not necessarily nanoseconds accuracy. This method returns
	 * -1 if the platform does not support this operation.
	 * 
	 * @return the CPU time used by the process in nanoseconds, or -1 if this
	 *         operation is not supported.
	 */
	public long getProcessCpuTime() {
		return callOsBeanMethod("getProcessCpuTime", -1L);
	}

	/**
	 * Returns the total amount of physical memory in bytes.
	 * 
	 * @return the total amount of physical memory in bytes, or -1 if this
	 *         operation is not supported.
	 */
	public long getTotalPhysicalMemorySize() {
		return callOsBeanMethod("getTotalPhysicalMemorySize", -1L);
	}

	/**
	 * Returns the amount of free physical memory in bytes.
	 * 
	 * @return the amount of free physical memory in bytes, or -1 if this
	 *         operation is not supported.
	 */
	public long getFreePhysicalMemorySize() {
		return callOsBeanMethod("getFreePhysicalMemorySize", -1L);
	}

	/**
	 * Returns the "recent cpu usage" for the whole system. This value is a
	 * double in the [0.0,1.0] interval. A value of 0.0 means that all CPUs were
	 * idle during the recent period of time observed, while a value of 1.0
	 * means that all CPUs were actively running 100% of the time during the
	 * recent period being observed. All values betweens 0.0 and 1.0 are
	 * possible depending of the activities going on in the system. If the
	 * system recent cpu usage is not available, the method returns a negative
	 * value.
	 * 
	 * @return the "recent cpu usage" for the whole system; a negative value if
	 *         not available.
	 */
	public double getSystemCpuLoad() {
		return callOsBeanMethod("getSystemCpuLoad", -1D);
	}

	/**
	 * Returns the "recent cpu usage" for the Java Virtual Machine process. This
	 * value is a double in the [0.0,1.0] interval. A value of 0.0 means that
	 * none of the CPUs were running threads from the JVM process during the
	 * recent period of time observed, while a value of 1.0 means that all CPUs
	 * were actively running threads from the JVM 100% of the time during the
	 * recent period being observed. Threads from the JVM include the
	 * application threads as well as the JVM internal threads. All values
	 * betweens 0.0 and 1.0 are possible depending of the activities going on in
	 * the JVM process and the whole system. If the Java Virtual Machine recent
	 * CPU usage is not available, the method returns a negative value.
	 * 
	 * @return the "recent cpu usage" for the Java Virtual Machine process; a
	 *         negative value if not available.
	 */
	public double getProcessCpuLoad() {
		return callOsBeanMethod("getProcessCpuLoad", -1D);
	}

	/**
	 * @see OperatingSystemMXBean#getSystemLoadAverage()
	 */
	public double getSystemLoadAverage() {
		return callOsBeanMethod("getSystemLoadAverage", -1D);
	}

	/**
	 * @see OperatingSystemMXBean#getAvailableProcessors()
	 * @return the number of processors available to the virtual machine, or -1
	 *         if this operation is not supported.
	 */
	public int getAvailableProcessors() {
		return callOsBeanMethod("getAvailableProcessors", -1);
	}
	
	/**
	 * @return the uptime of the Java virtual machine in milliseconds.
	 */
	public long getVmUptime() {
		return rtBean.getUptime();
	}

	@SuppressWarnings("unchecked")
	private <T> T callOsBeanMethod(String methodName, T defaultValue) {
		if (osMxBeanclazz != null) {
			try {
				return (T) osMxBeanclazz.getMethod(methodName, new Class[0]).invoke(osBean);
			} catch (ReflectiveOperationException e) {
				return defaultValue;
			}
		}

		return defaultValue;
	}
	
	public static void main(String[] args) throws Exception {
		OperatingSystemInfos inf = new OperatingSystemInfos();

		System.out.println(inf.getTotalPhysicalMemorySize());
		System.out.println(inf.getFreePhysicalMemorySize());
		System.out.println(inf.getSystemCpuLoad());
		System.out.println(inf.getProcessCpuLoad());
		System.out.println(inf.getSystemLoadAverage());
		System.out.println(inf.getVmUptime());
	}

}
