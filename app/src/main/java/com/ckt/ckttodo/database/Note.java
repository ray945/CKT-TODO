package com.ckt.ckttodo.database;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Note extends RealmObject implements Serializable {
    //便签id
    @PrimaryKey
    private String noteId;
    //便签标题
    @Required
    private String noteTitle;
    //便签内容
    @Required
    private String noteContent;
    //便签创建时间
    private long noteCreateTime;
    //便签更新时间
    private long noteUpdateTime;
    //便签创建人
    private UserInfo userInfo;

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public long getNoteCreateTime() {
        return noteCreateTime;
    }

    public void setNoteCreateTime(long noteCreateTime) {
        this.noteCreateTime = noteCreateTime;
    }

    public long getNoteUpdateTime() {
        return noteUpdateTime;
    }

    public void setNoteUpdateTime(long noteUpdateTime) {
        this.noteUpdateTime = noteUpdateTime;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
