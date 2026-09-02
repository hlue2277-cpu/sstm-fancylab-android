package com.liuj.huabo.bean;

import java.io.Serializable;
import java.util.List;

public class Display implements Serializable {

    public DisplayRow title;
    public List<DisplayRow> details;


    public static class DisplayRow implements Serializable{
        public int type;
        public DisplayItem left;
        public DisplayItem right;
        public String url;
        public String background;

    }

    public static class DisplayItem implements Serializable{
        public int fontSize;
        public boolean bold;
        public String text;
    }

}
