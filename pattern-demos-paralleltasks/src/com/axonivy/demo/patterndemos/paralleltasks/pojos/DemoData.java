package com.axonivy.demo.patterndemos.paralleltasks.pojos;

/**
 * Demo Task data.
 * 
 * A unique Id is needed so that multiple processes using this pattern can be identified.
 * Additional data, like the taskNumber in this example, can be provided. Note, that the
 * signal is only used locally in one process, therefore it is save here to use an object as
 * signal data.
 */
public class DemoData {
	private String uniqueId;
	private int taskNumber;
	private int totalTasks;

	public static DemoData create(String uniqueId, int taskNumber, int totalTasks) {
		var data = new DemoData();
		data.uniqueId = uniqueId;
		data.taskNumber = taskNumber;
		data.totalTasks = totalTasks;
		return data;
	}

	/**
	 * @return the uniqueId
	 */
	public String getUniqueId() {
		return uniqueId;
	}

	/**
	 * @param uniqueId the uniqueId to set
	 */
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	/**
	 * @return the taskNumber
	 */
	public int getTaskNumber() {
		return taskNumber;
	}

	/**
	 * @param taskNumber the taskNumber to set
	 */
	public void setTaskNumber(int taskNumber) {
		this.taskNumber = taskNumber;
	}

	/**
	 * @return the totalTasks
	 */
	public int getTotalTasks() {
		return totalTasks;
	}

	/**
	 * @param totalTasks the totalTasks to set
	 */
	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}
}