package com.zucc.hpy31501365gbl31501364.JavaBean.Richeng;

import java.util.List;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class Richeng {

    /**
     * status : 1
     * msg :
     * result : [{"_id":"5b46a9acbb263c1dc0a2cffd","userId":"31501365","eventId":"Eve0201807120906525","eventTitle":"换个地方洗脚","eventDate":"2018年7月12日","eventType":"休闲","startTime":"20:30","endTime":"21:00","priority":"!!!","place":"freedom place","beizhu":"洗的不爽想换地洗","liuyan":"洗爽了 嗨皮","__v":0},{"_id":"5b46b13bb79c3e239cae5a39","userId":"31501365","eventId":"Eve6201807120939079","eventTitle":"打游戏","eventDate":"2018年7月12日","eventType":"休闲","startTime":"20:30","endTime":"21:00","priority":"!!","place":"我家","beizhu":"","liuyan":"","__v":0},{"_id":"5b46b175b79c3e239cae5a3a","userId":"31501365","eventId":"Eve3201807120940051","eventTitle":"打游戏","eventDate":"2018年7月12日","eventType":"工作","startTime":"08:30","endTime":"15:50","priority":"!!","place":"理4 218","beizhu":"","liuyan":"","__v":0}]
     */

    private String status;
    private String msg;
    private List<RichengResult> richengresult;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<RichengResult> getRichengresult() {
        return richengresult;
    }

    public void setRichengresult(List<RichengResult> richengresult) {
        this.richengresult = richengresult;
    }
}
