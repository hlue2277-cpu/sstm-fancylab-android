package com.liuj.huabo.bean;

import java.io.Serializable;

public class FaceDetectBean implements Serializable {

    public String faceId;
    public String score;
    public String uuid;
    public String checktime;
    public String lastCheck;
    public int checkCount;
    public String cardNo;
    public String realname;

    public int status = 0;// 0 success   1 false
    public String msg;

}
