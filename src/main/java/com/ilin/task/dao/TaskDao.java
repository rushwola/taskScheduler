package com.ilin.task.dao;

import java.util.List;
import java.util.Map;

import com.ilin.task.exception.TaskDaoException;
import com.ilin.task.model.TaskModel;


public interface TaskDao {

	public Map<String,TaskModel> getTaskModelByServerIp(List<String> serverIps) throws TaskDaoException;

}
