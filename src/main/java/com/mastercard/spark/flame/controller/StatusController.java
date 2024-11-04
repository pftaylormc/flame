package com.mastercard.spark.flame.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Slf4j
public class StatusController {

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/status/{code}",
			produces = { "application/json" }
	)

	public ResponseEntity<String> status(@PathVariable("code") Integer code) {
		log.info("status - {}", code);
		return ResponseEntity.status(code).body("Status code " + code + "\n");
	}
}
