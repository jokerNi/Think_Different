package com.think_different.javabean;

/**
 * Created by oceancx on 15/3/6.
 * 消息未读数
 */
public class Remind {
    //新微博未读数
    private int status;

    //新粉丝数
    private int follower;

    //新评论数
    private int cmt;

    //新私信数
    private int dm;

    //新提及我的微博数
    private int mention_status;

    //新提及我的评论数
    private int mention_cmt;

    //微群消息未读数
    private int group;

    //私有微群消息未读数
    private int private_group;

    //新通知未读数
    private int notice;

    //新邀请未读数
    private int invite;

    //新勋章数
    private int badge;

    //相册消息未读数
    private int photo;

    //{{{3}}}
    private int msgbox;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getCmt() {
        return cmt;
    }

    public void setCmt(int cmt) {
        this.cmt = cmt;
    }

    public int getDm() {
        return dm;
    }

    public void setDm(int dm) {
        this.dm = dm;
    }

    public int getMention_status() {
        return mention_status;
    }

    public void setMention_status(int mention_status) {
        this.mention_status = mention_status;
    }

    public int getMention_cmt() {
        return mention_cmt;
    }

    public void setMention_cmt(int mention_cmt) {
        this.mention_cmt = mention_cmt;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getPrivate_group() {
        return private_group;
    }

    public void setPrivate_group(int private_group) {
        this.private_group = private_group;
    }

    public int getNotice() {
        return notice;
    }

    public void setNotice(int notice) {
        this.notice = notice;
    }

    public int getInvite() {
        return invite;
    }

    public void setInvite(int invite) {
        this.invite = invite;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public int getMsgbox() {
        return msgbox;
    }

    public void setMsgbox(int msgbox) {
        this.msgbox = msgbox;
    }
}
