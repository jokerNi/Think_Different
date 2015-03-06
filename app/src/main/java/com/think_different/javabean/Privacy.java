package com.think_different.javabean;

/**
 * Created by oceancx on 15/3/6.
 * 隐私设置
 */
public class Privacy {
    //是否可以评论我的微博，0：所有人、1：关注的人、2：可信用户
    private int comment;

    //是否开启地理信息，0：不开启、1：开启
    private int geo;

    //是否可以给我发私信，0：所有人、1：我关注的人、2：可信用户
    private int message;

    //是否可以通过真名搜索到我，0：不可以、1：可以
    private int realname;

    //勋章是否可见，0：不可见、1：可见
    private int badge;

    //是否可以通过手机号码搜索到我，0：不可以、1：可以
    private int mobile;

    //是否开启webim， 0：不开启、1：开启
    private int webim;

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }

    public int getGeo() {
        return geo;
    }

    public void setGeo(int geo) {
        this.geo = geo;
    }

    public int getMessage() {
        return message;
    }

    public void setMessage(int message) {
        this.message = message;
    }

    public int getRealname() {
        return realname;
    }

    public void setRealname(int realname) {
        this.realname = realname;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getMobile() {
        return mobile;
    }

    public void setMobile(int mobile) {
        this.mobile = mobile;
    }

    public int getWebim() {
        return webim;
    }

    public void setWebim(int webim) {
        this.webim = webim;
    }
}
