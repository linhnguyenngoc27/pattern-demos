package com.axonivy.demo.patterndemos.job.entities;

import java.io.IOException;
import java.time.Instant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.axonivy.demo.patterndemos.job.enums.JobRunStatus;
import com.axonivy.utils.persistence.beans.AuditableIdEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.ivyteam.ivy.environment.Ivy;

@Entity
@Table
public class JobStatus extends AuditableIdEntity {
	private static final long serialVersionUID = 1L;
	public static final int MAX_MESSAGE_LENGTH = 1024*1024;

	private static final ObjectMapper objectMapper = new ObjectMapper();

	@Column
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private JobRunStatus runStatus;

	@Column
	private Instant startTime;

	@Column
	private Instant endTime;

	@Column
	private Instant lastSuccessStartTime;

	@Column
	private Instant lastSuccessEndTime;

	@Column(length = MAX_MESSAGE_LENGTH)
	@Lob
	private String message;

	@Column
	@Lob
	private String jobData;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the runStatus
	 */
	public JobRunStatus getRunStatus() {
		return runStatus;
	}

	/**
	 * @param runStatus the runStatus to set
	 */
	public void setRunStatus(JobRunStatus runStatus) {
		this.runStatus = runStatus;
	}

	/**
	 * @return the startTime
	 */
	public Instant getStartTime() {
		return startTime;
	}

	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return the endTime
	 */
	public Instant getEndTime() {
		return endTime;
	}

	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	/**
	 * @return the lastSuccessStartTime
	 */
	public Instant getLastSuccessStartTime() {
		return lastSuccessStartTime;
	}

	/**
	 * @param lastSuccessStartTime the lastSuccessStartTime to set
	 */
	public void setLastSuccessStartTime(Instant lastSuccessStartTime) {
		this.lastSuccessStartTime = lastSuccessStartTime;
	}

	/**
	 * @return the lastSuccessEndTime
	 */
	public Instant getLastSuccessEndTime() {
		return lastSuccessEndTime;
	}

	/**
	 * @param lastSuccessEndTime the lastSuccessEndTime to set
	 */
	public void setLastSuccessEndTime(Instant lastSuccessEndTime) {
		this.lastSuccessEndTime = lastSuccessEndTime;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Data the Job wants to remember for it's next run.
	 *
	 * Jobs will be started with the saved job status of the previous run.
	 *
	 * @return the jobData
	 */
	public String getJobData() {
		return jobData;
	}

	/**
	 * Data the Job wants to remember for it's next run.
	 *
	 * Jobs will be started with the saved job status of the previous run.
	 *
	 * @param jobData the jobData to set
	 */
	public void setJobData(String jobData) {
		this.jobData = jobData;
	}

	/**
	 * Extract the job's data.
	 *
	 * @param <T>
	 * @param jobDataClass type to expectjson coded in job's data
	 * @return
	 */
	public <T> T unpackFromJobData(Class<T> jobDataClass) {
		T result = null;
		var data = getJobData();
		if(data != null) {
			try {
				result = objectMapper.readerFor(jobDataClass).readValue(data);
			} catch (IOException e) {
				Ivy.log().error("Could not unpack JobData from JobStatus ''{0}'' {1}", e, data, name);
			}
		}
		return result;
	}

	/**
	 * Pack the job's data.
	 *
	 * A job can save data for a later run. It is up to the job to make sure, that the data can be serialized and desrialized to and from a json string.
	 *
	 * @param <T>
	 * @param jobData
	 */
	public <T> void packToJobData(T jobData) {
		String jobDataString = null;
		try {
			jobDataString = objectMapper.writeValueAsString(jobData);
			setJobData(jobDataString);
		} catch (JsonProcessingException e) {
			Ivy.log().error("Could not pack JobData to JobStatus ''{0}''. Leaving the previous message untouched. {1}", e, jobDataString, name);
		}
	}
}
