package com.liuj.huabo.api.net;


import java.util.HashMap;

/**
 * Created by liujun on 2020/4/9.
 */
public class RequestConfig {


    public static class Method {
        public static final String GET = "GET";
        public static final String POST = "POST";
    }

    private static final HashMap<Integer, RequestConfigBean> requestConfigMap = createMap();

    private static HashMap<Integer, RequestConfigBean> createMap() {
        HashMap<Integer, RequestConfigBean> map = new HashMap<>();
        map.put(RequestConfigBean.TYPE_RELEASE, new RequestConfigBean(true, "https://api.gzxuedu.com", ""));
        //测试环境
        map.put(RequestConfigBean.TYPE_DEBUG, new RequestConfigBean(true, "https://api-demo.tongqisz.com", ""));
        return map;
    }

    public static RequestConfigBean getReleaseConfig() {
        return requestConfigMap.get(RequestConfigBean.TYPE_RELEASE);
    }

    public static class Url {

//        private final static String URL_SERVER = "https://ticket.cfexpo2021.com";

//        public  static String URL_SERVER = "http://dev.cfexpo2021.com";

        //public  static String URL_SERVER = "http://sstm.lengliwh.com";
        //public  static String URL_SERVER = "http://192.168.2.203";//暂时
        //public  static String URL_SERVER = "http://172.18.17.31:86";
        public  static String URL_SERVER = "http://172.18.19.31:85";

        public  static String TEST = URL_SERVER +"/ticket/terminal/query.xhtml";

        public  static String HE_XIAO = URL_SERVER + "/ticket/terminal/app/confirm.xhtml";

        public  static String CHECK_TICKET =  URL_SERVER + "/ticket/terminal/app/check.xhtml";
        //public  static String CHECK_TICKET =  URL_SERVER + "/ticket/terminal/app/check";//暂时

        public static String RE_CHECK_TICKET = URL_SERVER ="/ticket/terminal/app/recheck.xhtml";

        public  static String LOGIN = URL_SERVER  + "/ticket/terminalLogin.xhtml";
        //public  static String LOGIN = URL_SERVER  + "/ticket/terminalLogin";//暂时

        public static String FACE_QUERY = URL_SERVER + "/ticket/terminal/app/faceQuery.xhtml";

        public static String FACE_COLLECT = URL_SERVER + "/ticket/terminal/app/addFace.xhtml";

        public static String TEAM_NO_QUERY = URL_SERVER + "/ticket/terminal/app/checkReserveNo.xhtml";

        public static String CHECK_UPDATE = URL_SERVER + "/ticket/terminal/app/checkVersion.xhtml";

        public  static String QueryBat =  URL_SERVER + "/ticket/terminal/sstm/queryOrder.xhtml";//HHJT2023
        //public  static String QueryBat =  URL_SERVER + "/ticket/terminal/sstm/queryOrder";//暂时

        public static void refreshParentUrl(){
            TEST = URL_SERVER +"/ticket/terminal/query.xhtml";
            HE_XIAO = URL_SERVER + "/ticket/terminal/app/confirm.xhtml";
            CHECK_TICKET =  URL_SERVER + "/ticket/terminal/app/check.xhtml";
            //CHECK_TICKET =  URL_SERVER + "/ticket/terminal/app/check";//暂时
            LOGIN = URL_SERVER  + "/ticket/terminalLogin.xhtml";
            //LOGIN = URL_SERVER  + "/ticket/terminalLogin";//暂时
            FACE_QUERY = URL_SERVER + "/ticket/terminal/app/faceQuery.xhtml";
            FACE_COLLECT = URL_SERVER + "/ticket/terminal/app/addFace.xhtml";
            RE_CHECK_TICKET = URL_SERVER +"/ticket/terminal/app/recheck.xhtml";
            TEAM_NO_QUERY = URL_SERVER + "/ticket/terminal/app/checkReserveNo.xhtml";
            CHECK_UPDATE = URL_SERVER + "/ticket/terminal/app/checkVersion.xhtml";
            QueryBat =  URL_SERVER + "/ticket/terminal/sstm/queryOrder.xhtml";//HHJT2023
            //QueryBat =  URL_SERVER + "/ticket/terminal/sstm/queryOrder";//暂时
        }




    }

}
