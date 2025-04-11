package com.axonivy.demo.patterndemos.job.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.demo.patterndemos.job.entities.JobStatus;
import com.axonivy.demo.patterndemos.job.enums.JobRunStatus;
import com.axonivy.demo.patterndemos.job.pojos.ServiceResult;
import com.axonivy.demo.patterndemos.job.pojos.ServiceResult.ResultStatus;

import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class JobServiceTest {
	private JobService jobSvc = JobService.get();

	@BeforeEach
	public void initializedDemoJobService() {
		DemoJobService.get();
	}

	@Test
	public void testSimpleRun() {
		var jobName = "testJob";
		assertThat(jobSvc.loadJobStatus(jobName).getRunStatus()).isNull();
		assertThat(jobSvc.getJobLock(jobName)).isNull();

		assertThat(jobSvc.isLocked(jobName)).isFalse();

		var result = jobSvc.runJob(jobName, true, Duration.ofMinutes(1), this::simpleJob);

		assertThat(jobSvc.isLocked(jobName)).isFalse();

		assertThat(result.isOk()).isTrue();
		var jobStatus = jobSvc.loadJobStatus(jobName);
		assertThat(jobStatus).isNotNull();
		assertThat(jobStatus.getRunStatus()).isEqualTo(JobRunStatus.OK);
	}


	@Test
	public void testRegisteredRun() {
		var result = jobSvc.runJob(DemoJobService.DEMO_JOB_NAME);
		assertThat(result.isOk()).isTrue();
	}

	@Test
	public void testRunWithLastJobStatus() {
		var jobName = "memoryjob";

		ServiceResult result = null;

		result = jobSvc.runJob(jobName, true, Duration.ofMinutes(1), this::memoryJob);
		assertThat(result.isOk()).isTrue();
		assertThat(jobSvc.loadJobStatus(jobName).getJobData()).contains("This is run 1");

		result = jobSvc.runJob(jobName, true, Duration.ofMinutes(1), this::memoryJob);
		assertThat(result.isOk()).isTrue();
		assertThat(jobSvc.loadJobStatus(jobName).getJobData()).contains("This is run 2");

		result = jobSvc.runJob(jobName, true, Duration.ofMinutes(1), this::memoryJob);
		assertThat(result.isOk()).isTrue();
		assertThat(jobSvc.loadJobStatus(jobName).getJobData()).contains("This is run 3");
	}

	public static class DemoJobData {
		private int count = 0;
		private String someInfo = "";
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		public String getSomeInfo() {
			return someInfo;
		}
		public void setSomeInfo(String someInfo) {
			this.someInfo = someInfo;
		}
	}

	private ServiceResult simpleJob(JobStatus lastJobStatus) {
		var result = new ServiceResult();
		result.add(ResultStatus.OK, "Everything is awesome!!!");
		return result;
	}

	private ServiceResult memoryJob(JobStatus lastJobStatus) {
		var result = new ServiceResult();

		DemoJobData jobData = null;
		if(lastJobStatus != null) {
			jobData = lastJobStatus.unpackFromJobData(DemoJobData.class);
		}

		if(jobData == null) {
			jobData = new DemoJobData();
		}

		jobData.setCount(jobData.getCount() + 1);
		jobData.setSomeInfo("This is run %d".formatted(jobData.getCount()));

		var curJobStatus = jobSvc.loadCurrentJobStatus(lastJobStatus);
		curJobStatus.packToJobData(jobData);
		jobSvc.saveJobStatus(curJobStatus);

		result.add(ResultStatus.OK, "Everything is awesome!!!");

		return result;
	}
}
