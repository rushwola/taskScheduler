package com.ilin.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import com.ilin.localization.StringManager;
import com.ilin.task.exception.TaskDaoException;
import com.ilin.task.model.TaskModel;

/**
 * schedule job
 * 
 * @author shikun
 * @author johnnyKing.39
 *
 */
public class TaskUtil {
	private static Logger logger = Logger.getLogger(TaskUtil.class);
	private static final StringManager sm = StringManager.getManager(Constance.PACKAGE);
	public static final String JOB_DATA_MAP_PLAN = "JOB_DATA_MAP_PLAN";
	private static TaskUtil instance;
	private static Map<String, TaskModel> oldTaskMap = new HashMap<String, TaskModel>();
	private Scheduler _sched = null;

	public synchronized static TaskUtil getInstance() {
		if (instance == null) {
			instance = new TaskUtil();
		}

		return instance;
	}

	private TaskUtil() {
		try {
			_sched = StdSchedulerFactory.getDefaultScheduler();
		} catch (SchedulerException e) {
			logger.fatal(sm.getString("schedulerStartException"), e);
		}
	}

	public void initTask() {
		SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<String> serverIpList = Host.getAllIp();
		logger.info(sm.getString("getHostIp", serverIpList));
		if (serverIpList.isEmpty()) {
			logger.fatal(sm.getString("schedulerCantRun"));
			return;
		}
		try {
			Map<String, TaskModel> taskMap = TaskDaoFactroy.getFactroy().getTaskDao()
					.getTaskModelByServerIp(serverIpList);
			Map<String, TaskModel> taskTmpMap = new HashMap<String, TaskModel>();
			boolean isTaskListChanged = false;
			if(taskTmpMap==null){
				taskTmpMap=new HashMap<String, TaskModel>();
			}
			for (Iterator<String> iter = oldTaskMap.keySet().iterator(); iter.hasNext();) {
				String taskId = iter.next();

				TaskModel newTaskModel = taskMap.remove(taskId);
				TaskModel oldTaskModel = oldTaskMap.get(taskId);

				if (null == newTaskModel) {
					_sched.interrupt(oldTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP);
					_sched.deleteJob(oldTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP);
					isTaskListChanged = true;
					logger.info(sm.getString("taskDeleteInfo", oldTaskModel.getTaskId()));
					continue;
				}
				/**
				 * job is not changed, scheduler continue
				 */
				if (newTaskModel != null && newTaskModel.isEquals(oldTaskModel)) {
					taskTmpMap.put(taskId, oldTaskModel);
					continue;
				}
				/**
				 * job is modified,stop the original one and start the modified
				 * one
				 */
				if (newTaskModel != null && !newTaskModel.isEquals(oldTaskModel)) {
					_sched.interrupt(oldTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP);
					_sched.deleteJob(oldTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP);
					isTaskListChanged = true;
					JobDetail job = new JobDetail(newTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP, CollectJob.class);
					job.getJobDataMap().put(JOB_DATA_MAP_PLAN, newTaskModel);
					Trigger trigger = new CronTrigger(newTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP,
							newTaskModel.getCollectTime());
					logger.info(sm.getString("taskRuninfo", job.getFullName(),
							fullDateFormat.format(_sched.scheduleJob(job, trigger))));
					taskTmpMap.put(taskId, newTaskModel);
				}
			}
			for (Iterator<String> iter = taskMap.keySet().iterator(); iter.hasNext();) {
				String taskId = iter.next();
				TaskModel newTaskModel = taskMap.get(taskId);
				isTaskListChanged = true;
				JobDetail job = new JobDetail(newTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP, CollectJob.class);
				job.getJobDataMap().put(JOB_DATA_MAP_PLAN, newTaskModel);
				Trigger trigger = new CronTrigger(newTaskModel.getTaskId(), Scheduler.DEFAULT_GROUP,
						newTaskModel.getCollectTime());
				logger.info(sm.getString("taskRuninfo", job.getFullName(),
						fullDateFormat.format(_sched.scheduleJob(job, trigger))));
				taskTmpMap.put(taskId, newTaskModel);
			}
			if (isTaskListChanged) {
				_sched.start();
			} else {
				logger.info(sm.getString("taskNotChanged"));
			}
			oldTaskMap = taskTmpMap;
		} catch (SchedulerException e) {
			logger.fatal(sm.getString("schedulerStartException"), e);
		} catch (ParseException e) {
			logger.fatal(sm.getString("cronExpressionException"), e);
		} catch (TaskDaoException e) {
			logger.fatal(sm.getString("jobJDBCException"), e);
		}
	}
}
