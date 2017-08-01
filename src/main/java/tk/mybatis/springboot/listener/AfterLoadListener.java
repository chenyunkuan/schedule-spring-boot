package tk.mybatis.springboot.listener;/**
 * Created by Administrator on 2016/12/8.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import tk.mybatis.springboot.service.JobService;

/**
 * @author CUTIE
 * @create 2016-12-08 15:30
 **/
@Component
public class AfterLoadListener implements ApplicationListener<ApplicationReadyEvent> {

    private static Logger log = LoggerFactory.getLogger(AfterLoadListener.class);

    @Autowired
    JobService jobService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        log.info("--------------------onApplicationEvent begin----------------------");
        jobService.startAllJob();
        log.info("--------------------onApplicationEvent end----------------------");
    }
}
