package com.mastercard.spark.flame.service;

import com.mastercard.spark.flame.model.MetaData;
import com.mastercard.spark.flame.model.Workload;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class WorkloadService {

	//private final GitProperties gitProperties;

	//private final BuildProperties buildProperties;
	private final ApplicationContext context;
	Map<String, Workload> workloads = new HashMap<>();
	List<byte[]> memory = new ArrayList<>();
	Random rng = new Random();


	public WorkloadService(ApplicationContext context, MeterRegistry meterRegistry) {
		this.context = context;
	}

	public List<Workload> getWorkloads() {
		log.info("getWorkloads - returning {}", workloads.size());
		return new ArrayList<>(workloads.values());
	}

	public Optional<Workload> getWorkload(String id) {
		return workloads.values().stream()
				.filter(workload -> workload.getId().equals(id))
				.findFirst();
	}

	public Workload createWorkload(Workload workload) {
		log.info("createWorkload called");

		long before = System.currentTimeMillis();

		workload.setId(UUID.randomUUID().toString());
		workload.setMetadata(buildMetaData());

		workloads.put(workload.getId(), workload);

		switch (workload.getAction()) {

			case NORMAL:
				break;

			case SHUTDOWN:
				shutdown();
				break;

			case TERMINATE:
				terminate();
				break;
		}

		try {
			memoryLoad(workload.getMemoryLoad());
			cpuLoad(workload.getCpuLoad());
			waitDuration(workload.getDuration());
		} catch (InterruptedException | NoSuchAlgorithmException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}

		workload.setElapsedTime(System.currentTimeMillis() - before);
		return workload;
	}


	private MetaData buildMetaData() {

		return MetaData.builder()
				.timestamp(OffsetDateTime.now())
				.replica(getReplicaId())
				.version("?")
				.commitId("?")
				.build();

	}

	private String getReplicaId() {
		return ManagementFactory.getRuntimeMXBean().getName();
	}


	private void terminate() {
		log.warn("terminate");
		System.exit(1);

	}

	private void shutdown() {
		log.warn("shutdown");
		AvailabilityChangeEvent.publish(context, LivenessState.BROKEN);
	}

	private void cpuLoad(long load) throws NoSuchAlgorithmException {
		log.info("CPU load: {}", load);


		byte[] bytes = new byte[32];
		rng.nextBytes(bytes);

		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		for (long i = 0; i < load; i++) {
			bytes = digest.digest(bytes);
		}
	}

	private void memoryLoad(long load) {
		log.info("memory load: {}", load);
		memory.add(new byte[(int) load]);
	}

	private void waitDuration(long duration) throws InterruptedException {
		log.info("waiting {}", duration);
		Thread.sleep(duration);
	}


}
