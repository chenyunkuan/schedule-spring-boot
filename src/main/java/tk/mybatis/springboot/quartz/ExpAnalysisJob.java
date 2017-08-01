/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
 
package tk.mybatis.springboot.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import tk.mybatis.springboot.util.HttpClientUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * This is just a simple job that gets fired off many times by example 1
 * </p>
 * 
 * @author Bill Kratzer
 */
@Component("expAnalysisJob")
public class ExpAnalysisJob implements Job {

    private static Logger log = LoggerFactory.getLogger(ExpAnalysisJob.class);

    public ExpAnalysisJob() {
    }

    /**
     * @throws JobExecutionException if there is an exception while executing the job.
     */
    public void execute(JobExecutionContext context) throws JobExecutionException {
        String url = context.getJobDetail().getJobDataMap().getString("url");
        JobKey jobKey = context.getJobDetail().getKey();
        log.info("ExpAnalysisJob says: " + jobKey + " executing at " + new Date());
        //调用httpClient url执行远程方法
        if(context.getJobDetail().getJobDataMap().containsKey("anaTime")) {
            String anaTime = context.getJobDetail().getJobDataMap().getString("anaTime");
            doExpStatisticsRun(url, anaTime);
        }else{
            doExpStatisticsDefaultRun(url);
        }
    }

    /**
     * 默认，只需要传递url，不需要传递时间参数，（默认时间（昨天）：example 2016-12-05 00:00:00 ）
     * @param url
     */
    public void doExpStatisticsDefaultRun(String url) {
//        String url = "http://127.0.0.1:8080/expAna/doExpStatisticsDefault.do";
        Map params = new HashMap();
        log.info("url excute start.");

        HttpClientUtil util = HttpClientUtil.getInstance();
        try {
            util.getResponseBodyAsString(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("url excute finished.");
    }

    /**
     * 需要传递时间参数
     * @param url
     * @param anaTime
     */
    public void doExpStatisticsRun(String url, String anaTime) {
        /*String url = "http://127.0.0.1:8080/expAna/expStatistics.do";
        String anaTime = "2016-12-05 00:00:00";*/
        Map params = new HashMap();
        params.put("anaTime", anaTime);

        log.info("url excute start.");

        HttpClientUtil util = HttpClientUtil.getInstance();
        try {
            util.getResponseBodyAsString(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("url excute finished.");
    }


}
