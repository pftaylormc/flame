package com.mastercard.spark.flame.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuildStatus {


	private String version;

	private String commitId;

}
