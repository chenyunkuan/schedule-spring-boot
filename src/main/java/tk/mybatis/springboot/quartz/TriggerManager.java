package tk.mybatis.springboot.quartz;/**
 * Created by Administrator on 2016/12/5.
 */

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * @author CUTIE
 * @create 2016-12-05 20:10
 **/
@Component("triggerManager")
public class TriggerManager {

    public void runExpAnalysisJob() throws Exception {
        Logger log = LoggerFactory.getLogger(TriggerManager.class);

        log.info("------- Initializing Scheduler-------------------");
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        log.info("------- Initialization Complete --------");
        log.info("------- Scheduling Jobs ----------------");
        JobDetail job = newJob(ExpAnalysisJob.class).withIdentity("expAnalysisJob", "httpRequest").build();
        job.getJobDataMap().put("url","http://127.0.0.1:8080/expAna/doExpStatisticsDefault.do");
        //0 0 2 * * ?    每天2点触发
        // 0/20 * * * * ? 20 seconds
        CronTrigger trigger = newTrigger().withIdentity("expAnalysisJobTrigger", "httpRequest").withSchedule(cronSchedule("0 0 2 * * ?")).build();
        Date ft = sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " has been scheduled to run at: " + ft + " and repeat based on expression: " + trigger.getCronExpression());
        sched.start();
        log.info("------- Started Scheduler -----------------");

        //完成之后需要shutdown,这里会一直调用，所以不会shutdown
        //sched.shutdown(true);
        /*log.info("------- Shutdown Complete -----------------");*/
        /*log.info("------- Waiting five minutes... ------------");
        try {
            // wait five minutes to show jobs
            Thread.sleep(300L * 1000L);
            // executing...
        } catch (Exception e) {
            //
        }
        log.info("------- Shutting Down ---------------------");
        sched.shutdown(true);
        log.info("------- Shutdown Complete -----------------");*/
    }



}
