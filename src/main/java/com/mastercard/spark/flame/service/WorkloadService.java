package com.mastercard.spark.flame.service;

import com.mastercard.spark.flame.model.WorkloadRequest;
import com.mastercard.spark.flame.model.WorkloadResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.info.GitProperties;
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

private final GitProperties gitProperties;

	private final ApplicationContext context;
	Map<String, WorkloadRequest> workloads = new HashMap<>();
	List<byte[]> memory = new ArrayList<>();
	Random rng = new Random();


	public WorkloadService(ApplicationContext context) {
		this.context = context;
	}

	public List<WorkloadRequest> getWorkloads() {
		log.info("getWorkloads - returning {}", workloads.size());
		return new ArrayList<>(workloads.values());
	}

	public Optional<WorkloadRequest> getWorkload(String id) {
		return workloads.values().stream()
				.filter(workload -> workload.getId().equals(id))
				.findFirst();
	}

	public WorkloadResult createWorkload(WorkloadRequest workload) {
		log.info("createWorkload called");

		long before = System.currentTimeMillis();

		workload.setId(UUID.randomUUID().toString());

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

		if (workload.getNumThreads() == 1) {
			run(workload);
		} else {
			List<Thread> threads = new ArrayList<>();
			for (int i = 0; i < workload.getNumThreads(); i++) {
				Thread thread = new Thread(() -> run(workload));
				thread.start();
				threads.add(thread);
			}
			threads.forEach(thread -> {
				try {
					thread.join();
				} catch (InterruptedException e) {
					throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
				}
			});
		}

		return WorkloadResult.builder()
				.id(workload.getId())
				.timestamp(OffsetDateTime.now())
				.replica(getReplicaId())
				.elapsedTime(System.currentTimeMillis() - before)
				.build();
	}

	private void run(WorkloadRequest workload) {
		log.info("running workload {} on thread {}", workload.getId(), Thread.currentThread().getName());
		try {
			memoryLoad(workload.getMemoryLoad());
			cpuLoad(workload.getCpuLoad());
			waitDuration(workload.getDuration());
		} catch (InterruptedException | NoSuchAlgorithmException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
		log.info("finished workload {} on thread {}", workload.getId(), Thread.currentThread().getName());
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
