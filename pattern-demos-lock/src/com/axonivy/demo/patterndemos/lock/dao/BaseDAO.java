package com.axonivy.demo.patterndemos.lock.dao;

/**
 * Define the persistence unit name to use in DAOs implementing this one.
 */
public interface BaseDAO extends com.axonivy.utils.persistence.dao.BaseDAO {
	@Override
	default public String getPersistenceUnitName() {
		return "pattern-demos-lock";
	}
}
