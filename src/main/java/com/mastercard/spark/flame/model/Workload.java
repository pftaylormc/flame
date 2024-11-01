package com.mastercard.spark.flame.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Workload {

	private String id;

	private String description = "";


	private long duration = 0L;

	private long memoryLoad = 0L;

	private long cpuLoad = 0L;

	private WorkloadAction action = WorkloadAction.NORMAL;

	private long elapsedTime;
	private MetaData metadata;

}
