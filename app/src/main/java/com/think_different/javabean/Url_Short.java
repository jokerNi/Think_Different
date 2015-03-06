package com.think_different.javabean;

/**
 * Created by oceancx on 15/3/6.
 */
public class Url_Short {

    //短链接
    private String url_short;

    //原始长链接
    private String url_long;

    //链接的类型，0：普通网页、1：视频、2：音乐、3：活动、5、投票
    private int type;

    //短链的可用状态，true：可用、false：不可用。
    private boolean result;

    public String getUrl_short() {
        return url_short;
    }

    public void setUrl_short(String url_short) {
        this.url_short = url_short;
    }

    public String getUrl_long() {
        return url_long;
    }

    public void setUrl_long(String url_long) {
        this.url_long = url_long;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
