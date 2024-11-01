package com.mastercard.spark.flame.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class MetaData {

	private OffsetDateTime timestamp;
	private String replica;

	private String version;

	private String commitId;

}
