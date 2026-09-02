package com.liuj.huabo.bean;

import java.io.Serializable;
import java.util.List;

public class CheckTicketResultBean implements Serializable {

//    {
//        "success":true,
//            "data":{
//        "uuid": "票号",                      //票号,当前16位字母+数字
//                "available": "Y",                   //是否可用：Y：可用
//                "reserveDate":"2020-05-01",         //预约日期
//                "ticketName":"平日成人票",            //票种名称
//                "certificateNo": "3401****2577",    //证件号
//                "realname": "张三"                   //姓名
//    }
//    }


    public String uuid;
    public String available;
    public String reserveDate;
    public String ticketName;
    public String scheduleName;
    public String reserveTime;
    public String certificateNo;
    public String realname;
    public String checkTime;
    public String statusText;
    public String confirm;
    public String needConfirm = ""; // Y / N
    public String printType;

    //上次检票时间
    public String lastCheckTime;

    public String image;//人脸照片

    public Display display;

    public int status = 0;// 1 success   0 fail
    public String msg;

}
