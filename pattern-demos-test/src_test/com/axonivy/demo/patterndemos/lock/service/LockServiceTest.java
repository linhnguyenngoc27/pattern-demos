package com.axonivy.demo.patterndemos.lock.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import ch.ivyteam.ivy.environment.IvyTest;

@IvyTest
public class LockServiceTest {

	@Test
	public void testLock1() {
		assertThat(LockService.get().lock("test")).as("Locking works").isTrue();
		assertThat(LockService.get().lock("test")).as("Locking locked again does not lock").isFalse();
		assertThat(LockService.get().unlock("test")).as("Unlocking works").isTrue();
		assertThat(LockService.get().unlock("test")).as("Second unlocking works (is a no-op)").isTrue();
		assertThat(LockService.get().lock("test")).as("Locking works").isTrue();
		assertThat(LockService.get().unlock("test")).as("Unlocking works").isTrue();
	}

}
