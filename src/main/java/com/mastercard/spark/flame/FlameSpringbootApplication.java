package com.mastercard.spark.flame;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.Arrays;

@SpringBootApplication
@Slf4j
public class FlameSpringbootApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(FlameSpringbootApplication.class, args);
	}

	@PostConstruct
	public void init() {
		log.info("Active profiles: {}", Arrays.asList(env.getActiveProfiles()));

		MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
		long mi = mbean.getHeapMemoryUsage().getInit();
		long mc = mbean.getHeapMemoryUsage().getCommitted();
		long mm = mbean.getHeapMemoryUsage().getMax();
		long mu = mbean.getHeapMemoryUsage().getUsed();
		log.info("Initial memory:   {}", mem(mi));
		log.info("Committed memory: {}", mem(mc));
		log.info("Max memory:       {}", mem(mm));
		log.info("Used memory:      {}", mem(mu));
		int ap = Runtime.getRuntime().availableProcessors();
		log.info("Number of CPUs:   {}", ap);
	}

	private String mem(long v) {
		if (v > 1024 * 1024) {
			return String.format("%.2f MB", v / (1024.0 * 1024));
		} else if (v > 1024) {
			return String.format("%.2f KB", v / 1024.0);
		} else {
			return String.format("%d B", v);
		}
	}
}
