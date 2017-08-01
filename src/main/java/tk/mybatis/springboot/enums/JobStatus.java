package tk.mybatis.springboot.enums;/**
 * Created by Administrator on 2016/12/7.
 */

/**
 * @author CUTIE
 * @create 2016-12-07 15:51
 **/
public enum JobStatus {
    RUN("job运行中",1),DOWN("job停止运行",0);

    private String name;
    private int code;

    JobStatus(String name, int code) {
        this.name = name;
        this.code = code;
    }

    /**
     * 根据code获取对应状态值
     */
    public static String getName(int code) {
        for (JobStatus ex: JobStatus.values()) {
            if (ex.getCode()==code) {
                return ex.getName();
            }
        }
        return null;
    }
    /**
     * 根据Name获取对应CODE值
     */
    public static int getCode(String name) {
        for (JobStatus ex: JobStatus.values()) {
            if (ex.getName()==name) {
                return ex.getCode();
            }
        }
        return -1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
