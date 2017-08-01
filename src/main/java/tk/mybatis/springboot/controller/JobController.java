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

package tk.mybatis.springboot.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tk.mybatis.springboot.model.Country;
import tk.mybatis.springboot.model.Job;
import tk.mybatis.springboot.service.JobService;

import java.util.List;

/**
 * @author liuzh
 * @since 2015-12-19 11:10
 */
@Controller
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping
    public ModelAndView getAll(Job job) {
        ModelAndView result = new ModelAndView("index");
        List<Job> jobList = jobService.getAll(job);
        result.addObject("pageInfo", new PageInfo<Job>(jobList));
        result.addObject("queryParam", job);
        result.addObject("page", job.getPage());
        result.addObject("rows", job.getRows());
        return result;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add() {
        ModelAndView result = new ModelAndView("view");
        result.addObject("job", new Country());
        return result;
    }

    @RequestMapping(value = "/view/{id}")
    public ModelAndView view(@PathVariable Integer id) {
        ModelAndView result = new ModelAndView("view");
        Job job = jobService.getById(id);
        result.addObject("job", job);
        return result;
    }

    @RequestMapping(value = "/delete/{id}")
    public ModelAndView delete(@PathVariable Integer id, RedirectAttributes ra) {
        ModelAndView result = new ModelAndView("redirect:/job");
        jobService.removeJob(id);
        jobService.deleteById(id);
        ra.addFlashAttribute("msg", "删除成功!");
        return result;
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public ModelAndView save(Job job) {
        ModelAndView result = new ModelAndView("view");
        String msg = job.getId() == null ? "新增成功!" : "更新成功!";
        jobService.save(job);
        job.setStatus("0");
        result.addObject("job", job);
        result.addObject("msg", msg);
        return result;
    }

    /**
     * 根据job的id获取数据库中保存的job运行，并更新job的状态
     * @param id
     * @param job
     * @return
     */
    @RequestMapping(value = "/startJob/{id}/{jobStatus}", method = RequestMethod.GET)
    public ModelAndView startJob(@PathVariable Integer id, @PathVariable int jobStatus, Job job) {
        ModelAndView result = new ModelAndView("redirect:/job");
        if (jobStatus ==1){
            result.addObject("msg", "job已启动");
            return result;
        }
        jobService.startJob(id);
        jobService.updateJobStatus(id,"1");//0：job停止  1：job运行中（数据库中）

        job.setStatus("1");
        result.addObject("job", job);
        result.addObject("msg", "启动job成功");
        return result;
    }

    /**
     * 根据job的id获取数据库中保存的job运行，并更新job的状态
     * @param id
     * @param job
     * @return
     */
    @RequestMapping(value = "/stopJob/{id}", method = RequestMethod.GET)
    public ModelAndView stopJob(@PathVariable Integer id, Job job) {
        ModelAndView result = new ModelAndView("redirect:/job");
        jobService.stopJob(id);
        jobService.updateJobStatus(id,"0");//0：job停止  1：job运行中（数据库中）
        result.addObject("job", job);
        result.addObject("msg", "停止job成功");
        return result;
    }

    /**
     * 获取数据库中所有标示为1的保存的job运行，并更新job的状态
     * @param job
     * @return
     */
    @RequestMapping(value = "/startAllJob", method = RequestMethod.GET)
    public ModelAndView stopJob(Job job) {
        ModelAndView result = new ModelAndView("redirect:/job");
        jobService.startAllJob();
        result.addObject("job", job);
        result.addObject("msg", "停止job成功");
        return result;
    }

}
