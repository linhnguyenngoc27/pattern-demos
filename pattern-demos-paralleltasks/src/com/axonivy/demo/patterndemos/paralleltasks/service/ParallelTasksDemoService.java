package com.axonivy.demo.patterndemos.paralleltasks.service;

import com.axonivy.demo.patterndemos.paralleltasks.pojos.DemoData;

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.security.exec.Sudo;
import ch.ivyteam.ivy.workflow.query.TaskQuery;

public class ParallelTasksDemoService {
	private static final ParallelTasksDemoService INSTANCE = new ParallelTasksDemoService();
	private static final String CREATE_TASK_SIGNAL = "com:axonivy:demo:patterndemos:paralleltasks:createTask";
	private static final String CANCEL_TASKS_SIGNAL = "com:axonivy:demo:patterndemos:paralleltasks:cancelTasks:%s";
	private static final String CONTINUE_AFTER_TASKS_SIGNAL = "com:axonivy:demo:patterndemos:paralleltasks:continueAfterTasks:%s";
	private static final String DEMO_TASK_NUMBER_FIELD = "DEMO_TASK_NUMBER";
	private static final String DEMO_TASK_STATUS_FIELD = "DEMO_TASK_STATUS";
	private static final String DEMO_TASK_ID_FIELD = "DEMO_TASK_ID";
	private static final String FINISHED = "FINISHED";

	/**
	 * Returns instance of the class.
	 *
	 * @return FundingApplicationService
	 */
	public static ParallelTasksDemoService get() {
		return INSTANCE;
	}

	/**
	 * Create parallel tasks by sending signals.
	 * 
	 * @param uniqueId a unique id for this run, so that we can wait later for specific signals for this run only
	 * @param count the number of tasks to create
	 */
	public void createTasks(String uniqueId, int count) {
		for (var i = 0; i < count; i++) {
			Ivy.wf().signals().create()
			.makeCurrentTaskPersistent()
			.data(DemoData.create(uniqueId, i, count))
			.send(CREATE_TASK_SIGNAL);
		}
	}

	/**
	 * Sends a signal to cancel left over tasks.
	 *
	 * @param uniqueId
	 */
	public void cancelTasks(String uniqueId) {
		Ivy.wf().signals().send(CANCEL_TASKS_SIGNAL.formatted(uniqueId));
	}

	/**
	 * Sends a signal if all tasks are finished.
	 *
	 * @param uniqueId
	 */
	public void signalFinished(String uniqueId) {
		Ivy.wf().signals().send(CONTINUE_AFTER_TASKS_SIGNAL.formatted(uniqueId));
	}

	/**
	 * Are all tasks finished?
	 * 
	 * In this example we count the number of tasks which set a custom field to finished.
	 * 
	 * @param demoData
	 * @return
	 */
	public boolean allFinished(DemoData demoData) {
		// Count all finished tasks with same unique id.
		var done = Sudo.get(() -> {
			return TaskQuery.create()
					.where()
					.customField().stringField(DEMO_TASK_ID_FIELD).isEqual(demoData.getUniqueId()).and()
					.customField().stringField(DEMO_TASK_STATUS_FIELD).isEqual(FINISHED).executor().count();});

		return done >= demoData.getTotalTasks();
	}


	/**
	 * Simulated finish of a task.
	 * 
	 * @param demoData
	 */
	public void finishTask(DemoData demoData) {
		Ivy.wfTask().customFields().stringField(DEMO_TASK_ID_FIELD).set(demoData.getUniqueId());
		Ivy.wfTask().customFields().stringField(DEMO_TASK_STATUS_FIELD).set(FINISHED);
		Ivy.wfTask().customFields().numberField(DEMO_TASK_NUMBER_FIELD).set(demoData.getTaskNumber());
	}
}
