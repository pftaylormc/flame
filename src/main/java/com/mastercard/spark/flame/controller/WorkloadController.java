package com.mastercard.spark.flame.controller;

import com.mastercard.spark.flame.model.WorkloadRequest;
import com.mastercard.spark.flame.model.WorkloadResult;
import com.mastercard.spark.flame.service.WorkloadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;


@Controller
@Slf4j
public class WorkloadController {

	private final WorkloadService workloadService;

	public WorkloadController(WorkloadService workloadService) {
		this.workloadService = workloadService;
	}

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/workloads",
			produces = { "application/json" }
	)

	public ResponseEntity<List<WorkloadRequest>> getWorkloads() {
		return ok(workloadService.getWorkloads());

	}

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/workloads/{id}",
			produces = { "application/json" }
	)

	public ResponseEntity<WorkloadRequest> getWorkload(@PathVariable("id") String id) {
		return workloadService.getWorkload(id).map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@RequestMapping(
			method = RequestMethod.POST,
			value = "/workloads",
			consumes = { "application/json" },
			produces = { "application/json" }
	)
	public ResponseEntity<WorkloadResult> createWorkload(@RequestBody WorkloadRequest workload) {
		return ok(workloadService.createWorkload(workload));
	}
}
