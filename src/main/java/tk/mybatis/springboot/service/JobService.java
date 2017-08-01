/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package tk.mybatis.springboot.service;

import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.springboot.mapper.JobMapper;
import tk.mybatis.springboot.model.Job;
import tk.mybatis.springboot.quartz.ExpAnalysisJob;
import tk.mybatis.springboot.quartz.QuartzManager;

import java.util.HashMap;
import java.util.List;

/**
 * @author liuzh
 * @since 2015-12-19 11:09
 */
@Service
public class JobService {
    private static Logger log = LoggerFactory.getLogger(JobService.class);

    @Autowired
    private JobMapper jobMapper;

    public List<Job> getAll(Job job) {
        if (job.getPage() != null && job.getRows() != null) {
            PageHelper.startPage(job.getPage(), job.getRows(), "group_name,trigger_name");
        }
        List<Job> jobList = jobMapper.selectJobList();
        return  jobList;
    }

    public Job getById(Integer id) {
        return jobMapper.selectById(id);
    }

    public void deleteById(Integer id) {
        jobMapper.deleteById(id);
    }

    public void save(Job job) {
        if (job.getId() != null) {
            jobMapper.updateById(job);
        } else {
            //判断是否数据库中group中是否已经存在该jobName
            List<Job> jobList = jobMapper.getJobsByJobGroup(job.getGroupName());
            if(contain(jobList,job.getJobName())){
                return;
            }
            jobMapper.insertJob(job);
        }
    }

    /**
     * 根据jobid查询job信息，运行job
     * @param id
     */
    public void startJob(Integer id) {
        log.info("--------------------startJob begin----------------------");
        Job job = jobMapper.selectById(id);
        String jobName = job.getJobName();
        String jobGroup = job.getGroupName();
        String triggerName = job.getTriggerName();
        String url = job.getUrl();
        String cronExpression = job.getCronExpression();
        QuartzManager.startJob(jobName,jobGroup, triggerName, url, ExpAnalysisJob.class, cronExpression);
        log.info("--------------------startJob end----------------------");
    }

    /**
     *
     * @comment 判断数组中是否包含字符串
     * @param
     * @return
     * @Author xhl
     * @Date 2015年10月30日 下午4:59:03
     * @since 1.0.0
     */
    public static boolean contain(List<Job> list, String str) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getJobName().equals(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 运行job或者停止job，更新数据库中job的状态
     * @param id
     * @param status 0：job停止 1：job运行中
     */
    public void updateJobStatus(Integer id, String status) {
        HashMap map = new HashMap();
        map.put("id", id);
        map.put("status", status);
        jobMapper.updateJobStatus(map);
    }

    /**
     * 根据jobid查询job信息，运行job
     * @param id
     */
    public void stopJob(Integer id) {
        log.info("--------------------stopJob begin----------------------");
        Job job = jobMapper.selectById(id);
        String jobGroup = job.getGroupName();
        String triggerName = job.getTriggerName();
        QuartzManager.pauseJob(triggerName, jobGroup);
        log.info("--------------------stopJob end----------------------");
    }

    /**
     * 根据jobid查询job信息，运行job
     * @param id
     */
    public void removeJob(Integer id) {
        log.info("--------------------removeJob begin----------------------");
        Job job = jobMapper.selectById(id);
        String jobName = job.getJobName();
        String jobGroup = job.getGroupName();
        String triggerName = job.getTriggerName();
        QuartzManager.removeJob(jobName, jobGroup, triggerName);
        log.info("--------------------removeJob end----------------------");
    }

    /**
     * 获取数据库中所有标示为1的保存的job运行，并更新job的状态
     */
    public void startAllJob() {
        log.info("--------------------startAllJob begin----------------------");
        HashMap map = new HashMap();
        map.put("status","1");
        List<Job> jobList = jobMapper.selectJobListByCon(map);
        for(int i=0;i<jobList.size();i++){
            String jobName = jobList.get(i).getJobName();
            String jobGroup = jobList.get(i).getGroupName();
            String triggerName = jobList.get(i).getTriggerName();
            String url = jobList.get(i).getUrl();
            String cronExpression = jobList.get(i).getCronExpression();
            QuartzManager.startJob(jobName,jobGroup, triggerName, url, ExpAnalysisJob.class, cronExpression);
        }
        log.info("--------------------startAllJob end----------------------");
    }
}
