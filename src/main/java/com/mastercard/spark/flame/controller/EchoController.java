package com.mastercard.spark.flame.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@Slf4j
public class EchoController {

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/echo",
			produces = { "application/json" }
	)

	public ResponseEntity<String> status(@RequestParam("msg") String msg) {
		log.info("echo - {}", msg);
		return ResponseEntity.ok(msg + "\n");
	}
}
