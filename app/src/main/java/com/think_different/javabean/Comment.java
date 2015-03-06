package com.think_different.javabean;

/**
 * Created by oceancx on 15/3/6.
 */
public class Comment {
    //评论创建时间 
    private String created_at;
    //评论的ID 
    private Long id;
    //评论的内容 
    private String text;
    //评论的来源 
    private String source;
    //评论作者的用户信息字段 详细 
    private User user;
    //评论的MID 
    private String mid;
    //字符串型的评论ID 
    private String idstr;
    //评论的微博信息字段 详细 
    private Statuse status;
    //评论来源评论，当本评论属于对另一评论的回复时返回此字段 
    private Comment reply_comment;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public Statuse getStatus() {
        return status;
    }

    public void setStatus(Statuse status) {
        this.status = status;
    }

    public Comment getReply_comment() {
        return reply_comment;
    }

    public void setReply_comment(Comment reply_comment) {
        this.reply_comment = reply_comment;
    }
}
