package tk.mybatis.springboot.mapper;

import tk.mybatis.springboot.model.Job;
import tk.mybatis.springboot.util.MyMapper;

import java.util.HashMap;
import java.util.List;

public interface JobMapper  extends MyMapper<Job> {


    List<Job> selectJobList();

    void insertJob(Job job);

    List getJobsByJobGroup(String jobGroup);

    /**
     * 运行job或者停止job更新数据库中job的状态
     * @param map
     * @return
     */
    int updateJobStatus(HashMap map);

    List<Job> selectJobListByCon(HashMap map);

//  新增
    Job selectById(Integer id);

    int deleteById(Integer id);

    int updateById(Job job);
}