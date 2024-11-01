package com.mastercard.spark.controller;

import com.mastercard.spark.model.SparkInfo;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.loader.ResourceEntry;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;


@Controller
@Slf4j
public class SparkController {

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/spark",
			produces = { "application/json" }
	)

	public ResponseEntity<SparkInfo> getSpark() {
		log.info("getSpark called");
		return ok(SparkInfo.builder()
				.name("Spark")
				.version("1.0.0")
				.build());

	}
}
