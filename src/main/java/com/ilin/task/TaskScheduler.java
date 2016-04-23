package com.ilin.task;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ilin.localization.StringManager;
import com.ilin.task.model.GlobalData;

/**
 * @author shikun
 * @author johnnyKing.39
 *
 */
public class TaskScheduler {

	private static final Logger logger = Logger.getLogger(TaskScheduler.class);
	private static final StringManager sm = StringManager.getManager(Constance.PACKAGE);
	private static volatile TaskScheduler taskScheduler = null;
	// private CommonDBO commonDBO = null;
	private GlobalData globalData = null;
	private boolean isInit = false;
	private static final int THREAD_POOL_SIZE = 1;
	private static final int SECONDS_PER_MINUTE = 60;

	private TaskScheduler() {
	}

	public static TaskScheduler getInstance() {
		if (null == taskScheduler) {
			synchronized (TaskScheduler.class) {
				if (null == taskScheduler) {
					taskScheduler = new TaskScheduler();
				}
			}
		}
		return taskScheduler;
	}

	public void startTask() {
		if (isInit) {
			logger.info(sm.getString("taskStarted"));
			return;
		}
		// if (null == commonDBO) {
		// logger.fatal(sm.getString("dataBaseNull"));
		// }
		if (null == globalData) {
			logger.fatal(sm.getString("globalDataNull"));
		}
		long minuteSize = Long.parseLong(globalData.getTaskUpdateTime());

		new ScheduledThreadPoolExecutor(THREAD_POOL_SIZE).scheduleAtFixedRate(new Runnable() {
			public void run() {
				// TaskUti.getInstance().initTask();
				try {
					TaskUtil.getInstance().initTask();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, minuteSize * SECONDS_PER_MINUTE, TimeUnit.SECONDS);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error(e.getLocalizedMessage());
		}
		isInit = true;
		logger.info(sm.getString("taskStart"));
	}

	// public final CommonDBO getCommonDBO() {
	// return commonDBO;
	// }
	//
	// public final void setCommonDBO(final CommonDBO commonDBO) {
	// this.commonDBO = commonDBO;
	// }

	public final GlobalData getGlobalData() {
		return globalData;
	}

	public final void setGlobalData(final GlobalData globalData) {
		this.globalData = globalData;
	}

}
