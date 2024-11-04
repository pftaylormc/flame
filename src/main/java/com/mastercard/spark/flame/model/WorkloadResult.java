package com.mastercard.spark.flame.model;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class WorkloadResult {

	private String id;
	private OffsetDateTime timestamp;
	private String replica;
	private long elapsedTime;
}
