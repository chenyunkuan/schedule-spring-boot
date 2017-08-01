/**
 * @Description: 
 *
 * @Title: QuartzManager.java
 * @Package com.joyce.quartz
 * @Copyright: Copyright (c) 2014
 *
 * @author Comsys-LZP
 * @date 2014-6-26 下午03:15:52
 * @version V2.0
 */
package tk.mybatis.springboot.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tk.mybatis.springboot.service.JobService;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * 
 * @comment 定时任务管理，提供了各种操作定时任务的方法
 * @author xhl
 * @version V1.0
 * @date 2015年10月29日  下午5:05:54
 */
public class QuartzManager {
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static Logger log = LoggerFactory.getLogger(QuartzManager.class);

  /**
	* 
	* @comment 添加新任务
	* @param   jobName--任务名称   
	* 		   jobGroup--任务组
	* 		   url---远程调用的定时任务url
	*          cls---定时任务执行类
	*          time---周期表达式，详见quartz文档
	* @return
	* @Author xhl
	* @Date 2015年10月29日 下午5:10:22
	* @since 1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static void startJob(String jobName, String jobGroup, String triggerName, String url, Class cls, String cronExpression) {
		try {
			Scheduler scheduler = gSchedulerFactory.getScheduler();
			JobDetail job = newJob(cls).withIdentity(jobName, jobGroup).build();
			job.getJobDataMap().put("url", url);
			CronTrigger trigger = newTrigger().withIdentity(triggerName, jobGroup).withSchedule(cronSchedule(cronExpression)).build();
			Date ft = scheduler.scheduleJob(job, trigger);
			log.info("-------------------QuartzManager startJob begin------------------");
			log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());

			if (!scheduler.isShutdown()) {
				scheduler.start();
			}
			log.info("-------------------QuartzManager startJob end------------------");
		} catch (Exception e) {
			log.error("------------------QuartzManager startJob error:" + e.getMessage() + "-----------------");
			throw new RuntimeException(e);
		}
	}
	
  /**
	* @comment 停止定时任务
	* @param
	* @return
	* @Author xhl
	* @Date 2015年10月30日 下午3:17:28
	* @since 1.0.0
	 */
	public static void pauseJob(String triggerName, String triggerGroupName){
		try {
			log.info("-------------------QuartzManager pauseJob begin------------------");
			Scheduler scheduler = gSchedulerFactory.getScheduler();
			scheduler.pauseTrigger(new TriggerKey(triggerName,triggerGroupName));
			scheduler.unscheduleJob(new TriggerKey(triggerName,triggerGroupName));
			log.info("-------------------QuartzManager pauseJob end------------------");
		} catch (Exception e) {
			log.error("------------------QuartzManager pauseJob error:" + e.getMessage() + "-----------------");
			throw new RuntimeException(e);
		}
	}	
	
	/**
	* @comment 移除一个定时任务
	* @param
	* @return
	* @Author xhl
	* @Date 2015年10月30日 下午3:16:05
	* @since 1.0.0
	*/
	public static void removeJob(String jobName, String groupName, String triggerName) {
		try {
			Scheduler scheduler = gSchedulerFactory.getScheduler();
			scheduler.pauseTrigger(new TriggerKey(triggerName,groupName));// 停止触发器
			scheduler.unscheduleJob(new TriggerKey(triggerName,groupName));// 移除触发器
			scheduler.deleteJob(new JobKey(jobName,groupName));// 删除任务
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	* @comment 启动所有定时任务
	* @param
	* @return
	* @Author xhl
	* @Date 2015年10月30日 下午4:36:33
	* @since 1.0.0
	 */
	public static void startJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.start();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	* @comment 关闭所有定时任务
	* @param
	* @return
	* @Author xhl
	* @Date 2015年10月30日 下午4:36:52
	* @since 1.0.0
	 */
	public static void shutdownJobs() {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (!sched.isShutdown()) {
				sched.shutdown();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
