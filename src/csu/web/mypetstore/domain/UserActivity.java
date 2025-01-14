package csu.web.mypetstore.domain;
import java.io.Serializable;
import java.util.Date;

public class UserActivity {
    private static final long serialVersionUID=6321792448424485623L;

    private int id;               // 日志的唯一标识符
    private String userId;          // 用户ID
    private String action;        // 用户行为
    private String setItemId;    // 相关产品ID（可为空）
    private Date timestamp;       // 行为发生的时间


    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getUserId() {return userId;}

    public void setUserId(String userId) {this.userId = userId;}

    public String getAction() {return action;}

    public void setAction(String action) {this.action = action;}

    public String getItemId() {return setItemId;}

    public void setItemId(String setItemId) {this.setItemId = setItemId;}

    public Date getTimestamp() {return timestamp;}

    public void setTimestamp(Date timestamp) {this.timestamp = timestamp;}
}
