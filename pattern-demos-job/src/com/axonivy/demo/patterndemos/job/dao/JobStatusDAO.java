package com.axonivy.demo.patterndemos.job.dao;

import com.axonivy.demo.patterndemos.job.entities.JobStatus;
import com.axonivy.demo.patterndemos.job.entities.JobStatus_;
import com.axonivy.utils.persistence.dao.AuditableIdDAO;


public class JobStatusDAO extends AuditableIdDAO<JobStatus_, JobStatus> implements BaseDAO {
	private static final JobStatusDAO INSTANCE = new JobStatusDAO();

	public static JobStatusDAO get() {
		return INSTANCE; 
	}

	@Override
	protected Class<JobStatus> getType() {
		return JobStatus.class;
	}

	/**
	 * Find a {@link JobStatus} by it's name.
	 *
	 * @param name
	 * @return
	 */
	public JobStatus findByName(String name) {
		JobStatus result = null;
		try(var query = initializeQuery()){
			var nameEx = getExpression(null, query.r, JobStatus_.name);
			query.where(query.c.equal(nameEx, name));
			result = forceSingleResult(findByCriteria(query));
		}
		return result;
	}
}

