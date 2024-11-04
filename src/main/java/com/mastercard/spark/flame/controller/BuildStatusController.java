package com.mastercard.spark.flame.controller;

import com.mastercard.spark.flame.model.BuildStatus;
import com.mastercard.spark.flame.service.BuildStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Slf4j
public class BuildStatusController {

	private final BuildStatusService buildStatusService;

	public BuildStatusController(BuildStatusService buildStatusService) {
		this.buildStatusService = buildStatusService;
	}
	@RequestMapping(
			method = RequestMethod.GET,
			value = "/build/status",
			produces = { "application/json" }
	)

	public ResponseEntity<BuildStatus> status() {
		return ResponseEntity.ok(buildStatusService.getBuildStatus());
	}
}
