package com.ilin.task.model;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.ilin.task.DateUtil;

public abstract class TaskModel implements Cloneable {

	/**
	 * 任务初始化
	 */
	public final static int TASK_STATU_INIT = -1;

	/**
	 * 任务正在运行
	 */
	public final static int TASK_STATU_RUNING = 1;

	/**
	 * 任务已经运行
	 */
	public final static int TASK_STATU_ENDED = 0;

	private static Object syncObj = new Object();
	// private static Map<String ,Object> taskLockMap=new
	// HashMap<String,Object>();

	private static Map<String, Integer> taskStatu = new HashMap<String, Integer>();
	private static ThreadLocal<Boolean> lockThreadLocal ;
	
	static {
		lockThreadLocal=new ThreadLocal<Boolean>(){

			@Override
			protected Boolean initialValue() {
				return false;
			}
			
		};
	}

	private static Object syncLock = new Object();

	private String taskId = null;

	private String collectTime = null;

	private String startTime = null;

	private String stopTime = null;

	public boolean taskStartSyn() {
		synchronized (syncObj) {
			if (taskStatu.get(this.taskId) == null) {
				taskStatu.put(this.taskId, TASK_STATU_INIT);
			}

			if (taskStatu.get(this.taskId) == TASK_STATU_RUNING) {
				lockThreadLocal.set(false);
				return false;
			} else {
				taskStatu.put(taskId, TASK_STATU_RUNING);
				lockThreadLocal.set(true);
				return true;
			}
		}

	}

	public void taskEndSyn() {
		synchronized (syncObj) {
			if (lockThreadLocal.get() == true) {
				taskStatu.put(taskId, TASK_STATU_ENDED);
			}

			lockThreadLocal.set(false);

		}
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	public void updateStartTimeAndStopTime(Date date, Date nextDate) {
		Calendar startTime = Calendar.getInstance();
		Calendar stopTime = Calendar.getInstance();
		startTime.setTime(date);
		startTime.set(Calendar.SECOND, 0);
		startTime.set(Calendar.MILLISECOND, 0);
		stopTime.setTime(nextDate);
		stopTime.set(Calendar.SECOND, 0);
		stopTime.set(Calendar.MILLISECOND, 0);
		String start_time = DateUtil.formatTime(startTime.getTime());
		String stop_time = DateUtil.formatTime(stopTime.getTime());

		setStartTime(start_time);
		setStopTime(stop_time);
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public abstract boolean isTaskEqual(TaskModel taskModel);

	public boolean isEquals(TaskModel taskModel) {
		if (!taskModel.getTaskId().equals(this.getTaskId())) {
			return false;
		} else if (!taskModel.getCollectTime().equals(this.getCollectTime())) {
			return false;
		} else {
			return isTaskEqual(taskModel);
		}
	}

	// public Object getLockObj(){
	//
	// synchronized(syncLock){
	//
	// if(taskLockMap.get(taskId)==null){
	// taskLockMap.put(taskId, new Object());
	// }
	//
	// return taskLockMap.get(taskId);
	// }
	// }

	public TaskModel cloneOne() throws CloneNotSupportedException {
		super.clone();
		return null;
	}

	@Override
	public String toString() {
		return "TaskModel [taskId=" + taskId + ", collectTime=" + collectTime + ", startTime=" + startTime
				+ ", stopTime=" + stopTime + "]";
	}

}
