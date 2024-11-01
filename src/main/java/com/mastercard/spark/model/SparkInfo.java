package com.mastercard.spark.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SparkInfo {
	private String name;
	private String version;
}
