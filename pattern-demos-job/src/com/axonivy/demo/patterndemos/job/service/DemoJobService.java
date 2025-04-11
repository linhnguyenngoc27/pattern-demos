package com.axonivy.demo.patterndemos.job.service;

import com.axonivy.demo.patterndemos.job.entities.JobStatus;
import com.axonivy.demo.patterndemos.job.pojos.ServiceResult;
import com.axonivy.demo.patterndemos.job.pojos.ServiceResult.ResultStatus;

import ch.ivyteam.ivy.environment.Ivy;

/**
 * Demo job service which will be called by Demo cron job process.
 */
public class DemoJobService {
	private static final DemoJobService INSTANCE = new DemoJobService();

	public static final String DEMO_JOB_NAME = "com.axonivy.demo.patterndemos.job.demojob";

	/**
	 * Note: The following initialization can be done anywhere before the registry is used.
	 * For this type of initialization to work, it might be necessary to instantiate the class.
	 */
	static {
		JobService.get().registerJobDescription(DEMO_JOB_NAME, (jobStatus) -> get().demoJob(jobStatus));
	}

	private DemoJobService() {
	}

	/**
	 * Get a service instance.
	 *
	 * @return
	 */
	public static DemoJobService get() {
		return INSTANCE;
	}

	/**
	 * Handle the logic of demo job.
	 *
	 */
	public ServiceResult demoJob(JobStatus lastJobStatus) {
		var result = new ServiceResult();

		try {
			Ivy.log().info("The cronjob demo triggered.");
			result.add(ResultStatus.OK, "Demo job started.");
			if(Boolean.parseBoolean(Ivy.var().get("com.axonivy.demo.patterndemos.job.forceError"))) {
				result.add(ResultStatus.ERROR, "Forced error. To avoid it, set the global variable 'forceError' to false.");
			}
			else {
				result.add(ResultStatus.OK, "Everything is awesome. If you want to change this, set the global variable 'forceError' to true.");				
			}
			result.add(ResultStatus.OK, "Demo job finished.");
		} catch (Exception e) {
			result.add(ResultStatus.ERROR, "Error by the job of ''{0}''.", e, DEMO_JOB_NAME);
		}

		return result;
	}
}
