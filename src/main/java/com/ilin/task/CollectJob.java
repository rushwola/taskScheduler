package com.ilin.task;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.InterruptableJob;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.UnableToInterruptJobException;

import com.ilin.task.model.TaskModel;

public class CollectJob implements Job, InterruptableJob {
	public static Logger logger = Logger.getLogger(CollectJob.class);

	public void execute(JobExecutionContext excutorContext) throws JobExecutionException {
		SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JobDataMap data = excutorContext.getJobDetail().getJobDataMap();
		TaskModel task = (TaskModel) data.get(TaskUtil.JOB_DATA_MAP_PLAN);
		// logger.info(sm.getString("collectJobStart",fullDateFormat.format(excutorContext.getFireTime())));
		task.updateStartTimeAndStopTime(excutorContext.getFireTime(), excutorContext.getNextFireTime());

		//List<ICalculator> jobList = TaskScheduler.getInstance().getGlobalData().getCaculatorList();
		// ICalculator
		// job=TaskScheduler.getInstance().getGlobalData().getCaculator();
		// job.caculate(task);
		try {
			Map<String, String> caculateMap = TaskScheduler.getInstance().getGlobalData().getCaculatorMap();
			String taskCalssname = task.getClass().getName();
			String caculateClassName = caculateMap.get(taskCalssname);
			if (caculateClassName != null) {
			
				Class<?> classtemp=Class.forName(caculateClassName);
				ICalculator obj = (ICalculator) classtemp.newInstance();
				obj.caculate(task);
			}
		} catch (Throwable e) {
			logger.error(e, e);
		}finally{
		
		}
		// ExecutorService exec = Executors.newCachedThreadPool();
		// for (Iterator<ICalculator> iter = jobList.iterator();
		// iter.hasNext();){
		// exec.execute(new CaculatorImpl(iter.next(), task));
		// }
		// exec.shutdown();
		// logger.info(sm.getString("collectJobStop",fullDateFormat.format(new
		// Date())));
	}

	public void interrupt() throws UnableToInterruptJobException {
	}

	static class CaculatorImpl implements Runnable {
		private final ICalculator caculator;
		private final TaskModel task;

		public CaculatorImpl(ICalculator caculator, TaskModel task) {
			this.caculator = caculator;
			this.task = task;
		}

		public void run() {
			this.caculator.caculate(task);
		}
	}

}
