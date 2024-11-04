package com.mastercard.spark.flame.service;

import com.mastercard.spark.flame.model.BuildStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.info.GitProperties;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuildStatusService {
	private final GitProperties gitProperties;


	public BuildStatusService(GitProperties gitProperties) {
		this.gitProperties = gitProperties;
	}


	public BuildStatus getBuildStatus() {
		return BuildStatus.builder()
				.version(gitProperties.get("build.version"))
				.commitId(gitProperties.getCommitId())
				.build();
	}

}
