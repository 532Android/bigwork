package com.zucc.hpy31501365gbl31501364.JavaBean.Richeng;

/**
 * Created by Administrator on 2018/7/12 0012.
 */

public class RichengResult {

    private String userId;
    private String eventId;
    private String eventTitle;
    private String eventDate;
    private String eventType;
    private String startTime;
    private String endTime;
    private String priority;
    private String place;
    private String beizhu;
    private String liuyan;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }

    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getBeizhu() {
        return beizhu;
    }

    public void setBeizhu(String beizhu) {
        this.beizhu = beizhu;
    }

    public String getLiuyan() {
        return liuyan;
    }

    public void setLiuyan(String liuyan) {
        this.liuyan = liuyan;
    }

    @Override
    public String toString() {
        return "{" + "EventId: " + this.eventId + ", Title: " + this.eventTitle + ", StartTime: " + this.startTime + ", EndTime: " + this.endTime + ", Place: " + this.place + "}";
    }
}

