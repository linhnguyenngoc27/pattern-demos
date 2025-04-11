package com.axonivy.demo.patterndemos.lock.service;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.axonivy.demo.patterndemos.lock.dao.LockDAO;
import com.axonivy.demo.patterndemos.lock.entities.Lock;

import ch.ivyteam.ivy.bpm.error.BpmError;
import ch.ivyteam.ivy.environment.Ivy;

public class LockService {
	private static final String LOCK_ERROR = "com:axonivy:demo:patterndemos:lock";
	private static final String UNLOCK_ERROR = "com:axonivy:demo:patterndemos:unlock";
	private static final LockService INSTANCE = new LockService();

	private LockService() {
	}

	/**
	 * Get service instance.
	 *
	 * @return Instance of the class
	 */
	public static LockService get() {
		return INSTANCE;
	}

	/**
	 * Get lock unlimited in time.
	 *
	 * @param lockName
	 * @return
	 */
	public boolean lock(String lockName) {
		return lock(lockName, false);
	}

	/**
	 * Get lock unlimited in time.
	 *
	 * @param lockName
	 * @param failWithException handle exceptions and return lock status
	 * @return
	 */
	public boolean lock(String lockName, boolean failWithException) {
		return lock(lockName, (Instant)null, failWithException);
	}

	/**
	 * Get lock of given name valid for certain time.
	 *
	 * @param lockName lock name
	 * @param validDuration duration until lock expires
	 * @param failWithException handle exceptions and return lock status
	 * @return <code>true</code> if the lock could be created, <code>false</code> if not (e.g. someone else has the lock)
	 */
	public boolean lock(String lockName, Duration validDuration, boolean failWithException) {
		return lock(lockName, validDuration != null ? Instant.now().plus(validDuration) : (Instant)null, failWithException);
	}

	/**
	 * Get lock of given name valid for certain time.
	 *
	 * @param lockName lock name
	 * @param validDuration duration until lock expires
	 * @return <code>true</code> if the lock could be created, <code>false</code> if not (e.g. someone else has the lock)
	 */
	public boolean lock(String lockName, Duration validDuration) {
		return lock(lockName, validDuration, false);
	}

	/**
	 * Get lock of given name and with an optional validity.
	 *
	 * @param name lock name
	 * @param validUntil valid until instant, if <code>null</code>, then lock will be valid forever
	 * @param failWithException handle exceptions and return lock status
	 * @return <code>true</code> if the lock could be created, <code>false</code> if not (e.g. someone else has the lock)
	 */
	public boolean lock(String name, Instant validUntil, boolean failWithException) {
		var locked = false;

		var lock = getLock(name);

		if(lock == null) {
			lock = new Lock();
			lock.setName(name);
		}

		var curValidUntil = lock.getValidUntil() != null ? lock.getValidUntil() : "not set";

		if(lock.isLocked() && (lock.getValidUntil() == null || Instant.now().isBefore(lock.getValidUntil()))) {
			Ivy.log().info("Cannot get lock {0} because it is already locked, valid until is {1}", lock.getName(), curValidUntil);
		}
		else {
			try {
				lock.setLocked(true);
				lock.setValidUntil(validUntil);
				Ivy.log().info("Locking {0}, valid until is {1}.", lock.getName(), curValidUntil);

				lock = LockDAO.get().save(lock);
				locked = lock.isLocked();
			} catch (Exception e) {
				if(failWithException) {
					throw e;
				}
				else {
					Ivy.log().warn("Exception while trying to lock {0}: {1}", lock.getName(), ExceptionUtils.getRootCauseMessage(e));
				}
			}
		}

		return locked;
	}

	/**
	 * Get a lock.
	 *
	 * @param name
	 * @return
	 */
	public Lock getLock(String name) {
		return LockDAO.get().findByName(name);
	}

	/**
	 * Is this name locked?
	 *
	 * @param name
	 * @return
	 */
	public boolean isLocked(String name) {
		var lock = getLock(name);
		return lock != null && lock.isLocked();
	}

	/**
	 * Unlock lock with the given name.
	 *
	 * @param lockName
	 * @return true if the lock could be unlocked, false if cannot (eg concurrent access).
	 */
	public boolean unlock(String name) {
		return unlock(name, false);
	}

	/**
	 * Unlock lock with the given name.
	 *
	 * @param lockName
	 * @param failWithException handle exceptions and return lock status
	 * @return true if the lock could be unlocked, false if cannot (eg concurrent access).
	 */
	public boolean unlock(String name, boolean failWithException) {
		var unlocked = false;

		var lock = getLock(name);

		if(lock == null) {
			lock = new Lock();
			lock.setName(name);
		}

		unlocked = !lock.isLocked();

		if(unlocked) {
			Ivy.log().info("Lock {0} is already unlocked, no further activity.", lock.getName());
		}
		else {
			try {
				lock.setLocked(false);
				lock.setValidUntil(null);
				Ivy.log().info("Unlocking {0}.", lock.getName());

				lock = LockDAO.get().save(lock);
				unlocked = !lock.isLocked();
			} catch (Exception e) {
				if(failWithException) {
					throw e;
				}
				else {
					Ivy.log().warn("Exception while trying to lock {0}: {1}", lock.getName(), ExceptionUtils.getRootCauseMessage(e));
				}
			}
		}

		return unlocked;
	}

	/**
	 * Perform a locked service returning some result.
	 *
	 * @param <R>
	 * @param lockName
	 * @param validDuration
	 * @param function
	 * @return result of function
	 */
	public <R> R doLocked(String lockName, Duration validDuration, Supplier<R> function) {
		return doLockedInternally(lockName, validDuration, () -> lock(lockName, validDuration), function);
	}

	/**
	 * Perform a forced locked service returning some result.
	 *
	 * @param <R>
	 * @param lockName
	 * @param validDuration
	 * @param tryDuration
	 * @param function
	 * @return result of function
	 */
	public <R> R doForcedLocked(String lockName, Duration validDuration, Duration tryDuration, Supplier<R> function) {
		return doLockedInternally(lockName, validDuration, () -> forceLock(lockName, validDuration, tryDuration), function);
	}

	/**
	 * Force lock.
	 *
	 * Wait until able to get lock
	 *
	 * @param name name to lock
	 * @param validDuration valid date of lock, if <code>null</code> then no valid date limit set
	 * @param tryDuration timeout, if <code>null</code>, then no timeout
	 * @return true if managed to get a lock
	 */
	public boolean forceLock(String name, Duration validDuration, Duration tryDuration) {
		return forceFunction("lock %s".formatted(name), tryDuration, () -> lock(name, validDuration));
	}

	/**
	 * Force unlock.
	 *
	 * @param name name to unlock
	 * @param tryDuration timeout, if null then no timeout
	 * @return true if unlocking worked
	 */
	public boolean forceUnlock(String name, Duration tryDuration) {
		return forceFunction("unlock %s".formatted(name), tryDuration, () -> unlock(name));
	}

	protected boolean forceFunction(String what, Duration tryDuration, Supplier<Boolean> function) {
		var result = false;

		var finish = tryDuration != null ? Instant.now().plus(tryDuration) : null;

		while (!result && (finish == null || finish.isAfter(Instant.now()))) {

			result = function.get();

			if(!result) {
				Ivy.log().debug("Could not {0}, retrying until {1}.", what, tryDuration);
			}
		}

		return result;

	}

	/**
	 * Perform a locked service returning some result.
	 *
	 * @param <R>
	 * @param lockName
	 * @param validDuration
	 * @param function
	 * @return result of function
	 */
	protected <R> R doLockedInternally(String lockName, Duration validDuration, Supplier<Boolean> lockFunction, Supplier<R> function) {
		R result = null;

		if(lockFunction.get()) {
			try {
				result = function.get();
			}
			finally {
				if(!unlock(lockName)) {
					throw BpmError.create(UNLOCK_ERROR).build();
				}
			}
		} else {
			throw BpmError.create(LOCK_ERROR).build();
		}

		return result;
	}
}

