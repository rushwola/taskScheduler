package com.ilin.task;

import com.ilin.task.dao.TaskDao;
import com.ilin.task.exception.TaskDaoException;

/**
 * @author shikun
 * @author johnnyKing.39
 *
 */
public class TaskDaoFactroy {
	private static volatile TaskDaoFactroy daoFactroy = null;
	public static TaskDaoFactroy getFactroy() {
		if (null == daoFactroy) {
			synchronized (TaskDaoFactroy.class) {
				if (null == daoFactroy) {
					daoFactroy = new TaskDaoFactroy();
				}
			}
		}
		return daoFactroy;
	}

	public TaskDao getTaskDao() throws TaskDaoException {
		try {
			return TaskScheduler.getInstance().getGlobalData().getTaskDao();
		}
		catch (Exception e) {
			throw new TaskDaoException(e);
		}
	}
}
