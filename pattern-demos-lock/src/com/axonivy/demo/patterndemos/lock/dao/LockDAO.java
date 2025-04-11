package com.axonivy.demo.patterndemos.lock.dao;

import com.axonivy.demo.patterndemos.lock.entities.Lock;
import com.axonivy.demo.patterndemos.lock.entities.Lock_;
import com.axonivy.utils.persistence.dao.AuditableIdDAO;

public class LockDAO extends AuditableIdDAO<Lock_, Lock> implements BaseDAO {
	private static final LockDAO INSTANCE = new LockDAO();

	public static LockDAO get() {
		return INSTANCE;
	}

	@Override
	protected Class<Lock> getType() {
		return Lock.class;
	}

	/**
	 * Find a {@link Lock} by it's name.
	 *
	 * @param type
	 * @return
	 */
	public Lock findByName(String type) {
		Lock lock = null;
		try (var ctx = initializeQuery()) {
			var nameEx = getExpression(null, ctx.r, Lock_.name);

			ctx.where(ctx.c.equal(nameEx, type));
			lock = forceSingleResult(findByCriteria(ctx));
		}
		return lock;
	}
}
