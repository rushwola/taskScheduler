package com.ilin.task.model;

import java.util.List;
import java.util.Map;

import com.ilin.task.ICalculator;
import com.ilin.task.dao.TaskDao;


public class GlobalData {

	private TaskDao taskDao;

	private String taskModelClass;


	public List<ICalculator> caculatorList;
	
	public ICalculator caculator;
	
	public Map<String,String> caculatorMap; 
	
	public String taskUpdateTime;

	public String getTaskUpdateTime() {
		return taskUpdateTime;
	}

	public void setTaskUpdateTime(String taskUpdateTime) {
		this.taskUpdateTime = taskUpdateTime;
	}

	

	public final List<ICalculator> getCaculatorList() {
		return caculatorList;
	}

	public final void setCaculatorList(List<ICalculator> caculatorList) {
		this.caculatorList = caculatorList;
	}

	public final TaskDao getTaskDao() {
		return taskDao;
	}

	public final void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public final TaskModel getTaskModel() {
		try {
			return (TaskModel)Class.forName(taskModelClass).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public final void setTaskModelClass(String taskModelClass) {
		this.taskModelClass = taskModelClass;
	}

	public ICalculator getCaculator() {
		return caculator;
	}

	public void setCaculator(ICalculator caculator) {
		this.caculator = caculator;
	}

	public Map<String, String> getCaculatorMap() {
		return caculatorMap;
	}

	public void setCaculatorMap(Map<String, String> caculatorMap) {
		this.caculatorMap = caculatorMap;
	}






}
