package fr.datasyscom.scopiom.rest.health;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import fr.datasyscom.scopiom.rest.util.OperatingSystemInfos;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class HealthInfos implements Serializable {

	private static final long serialVersionUID = 1L;

	// TODO Create a Singleton Bean?
	private static final SystemInfo si = new SystemInfo();
	private static final HardwareAbstractionLayer hal = si.getHardware();
	private static final CentralProcessor processor = hal.getProcessor();
	private static final GlobalMemory memory = hal.getMemory();
	private static final OperatingSystem os = si.getOperatingSystem();
	
	public long vmUptime;
	public long systemUptime;
	public PhysicalMemory physicalMemory;
	public VmMemory vmMemory;
	public Cpu cpu;
	public Fs fs;

	public HealthInfos() {
	}

	public HealthInfos(boolean physicalMemory, boolean vmMemory, boolean cpu, boolean fs) {
		OperatingSystemInfos osInfos = new OperatingSystemInfos();

		this.vmUptime = osInfos.getVmUptime();
		this.systemUptime = processor.getSystemUptime() * 1000;
		
		if (physicalMemory) {
			this.physicalMemory = new PhysicalMemory();
		}
		if (vmMemory) {
			this.vmMemory = new VmMemory();
		}
		if (cpu) {
			this.cpu = new Cpu();
		}
		if (fs) {
			this.fs = new Fs();
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class PhysicalMemory implements Serializable {

		private static final long serialVersionUID = 1L;

		public long totalMemory;
		public long freeMemory;
		public long usedMemory;

		public PhysicalMemory() {
			this.totalMemory = memory.getTotal();
			this.freeMemory = memory.getAvailable();
			this.usedMemory = (this.totalMemory - this.freeMemory);
		}

	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class VmMemory implements Serializable {

		private static final long serialVersionUID = 1L;

		public long totalMemory;
		public long freeMemory;
		public long usedMemory;
		public long maxMemory;

		public VmMemory() {
			this.totalMemory = Runtime.getRuntime().totalMemory();
			this.freeMemory = Runtime.getRuntime().freeMemory();
			this.usedMemory = (this.totalMemory - this.freeMemory);
			this.maxMemory = Runtime.getRuntime().maxMemory();
			if (this.maxMemory == Long.MAX_VALUE) {
				this.maxMemory = -1;
			}
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Cpu implements Serializable {

		private static final long serialVersionUID = 1L;

		public int vmProcessors;
		public double systemCpuLoad;
		public double processCpuLoad;
		public double systemLoadAverage;

		public Cpu() {
			OperatingSystemInfos osInfos = new OperatingSystemInfos();

			this.vmProcessors = Runtime.getRuntime().availableProcessors();
			this.systemCpuLoad = processor.getSystemCpuLoad();
			this.processCpuLoad = osInfos.getProcessCpuLoad();
			this.systemLoadAverage = processor.getSystemLoadAverage();
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Fs implements Serializable {

		private static final long serialVersionUID = 1L;

		public List<Disk> disks;

		public Fs() {
			this.disks = new ArrayList<>();

			FileSystem fileSystem = os.getFileSystem();
			OSFileStore[] fsArray = fileSystem.getFileStores();

			for (OSFileStore fs : fsArray) {
				this.disks.add(new Disk(fs.getMount(), fs.getTotalSpace(), fs.getUsableSpace(), fs
						.getUsableSpace()));
			}
		}
	}

	@XmlRootElement
	@XmlAccessorType(XmlAccessType.FIELD)
	public static class Disk implements Serializable {

		private static final long serialVersionUID = 1L;

		public String root;
		public long totalSpace;
		public long freeSpace;
		public long usableSpace;

		public Disk() {
		}

		public Disk(File root) {
			this.root = root.getAbsolutePath();
			this.totalSpace = root.getTotalSpace();
			this.freeSpace = root.getFreeSpace();
			this.usableSpace = root.getUsableSpace();
		}

		public Disk(String root, long totalSpace, long freeSpace, long usableSpace) {
			super();
			this.root = root;
			this.totalSpace = totalSpace;
			this.freeSpace = freeSpace;
			this.usableSpace = usableSpace;
		}

	}

}