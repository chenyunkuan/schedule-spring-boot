package tk.mybatis.springboot.util;/**
 * Created by Administrator on 2016/12/5.
 */

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.springboot.Application;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author CUTIE
 * @create 2016-12-05 17:04
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@Transactional
@SpringApplicationConfiguration(Application.class)
public class HttpClientTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void testSimpleExampleRun() {
        //String url = "http://localhost:8080/auth/org/delete.do";
        String url = "http://localhost:8080/expAna/expStatistics.do";

        Map params = new HashMap();
        params.put("anaTime", "2016-12-05 00:00:00");
        String result = "";

        HttpClientUtil util = HttpClientUtil.getInstance();
        try {
            result = util.getResponseBodyAsString(url,params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info("the result is {}", result);
        logger.info("OK");
    }

}
