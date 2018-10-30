package net.minecraft.server;

//import com.google.common.collect.Lists; // CloudSpigot
//import com.google.common.collect.Maps; // CloudSpigot
//import java.util.ArrayList; // CloudSpigot
//import java.util.Collections; // CloudSpigot
//import java.util.Iterator; // CloudSpigot
import java.util.List;
//import java.util.Map; // CloudSpigot
//import org.apache.logging.log4j.LogManager; // CloudSpigot
//import org.apache.logging.log4j.Logger; // CloudSpigot

// CraftBukkit start - Strip down to empty methods, performance cost
public class MethodProfiler {

	public boolean a;

	public void a() {
	}

	public void a(String s) {
	}

	public void b() {
	}

	public List<MethodProfiler.ProfilerInfo> b(String s) {
		return null;
	}

	public void c(String s) {
	}

	public String c() {
		return "";
	}

	public static final class ProfilerInfo implements Comparable<MethodProfiler.ProfilerInfo> {

		public double a;
		public double b;
		public String c;

		public ProfilerInfo(String s, double d0, double d1) {
			this.c = s;
			this.a = d0;
			this.b = d1;
		}

		public int a(MethodProfiler.ProfilerInfo methodprofiler_profilerinfo) {
			return methodprofiler_profilerinfo.a < this.a ? -1
					: (methodprofiler_profilerinfo.a > this.a ? 1 : methodprofiler_profilerinfo.c.compareTo(this.c));
		}

		@Override
		public int compareTo(MethodProfiler.ProfilerInfo object) {
			return this.a(object);
		}
	}
}
