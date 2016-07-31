package com.gaoyehua.bean;

import android.graphics.drawable.Drawable;
import android.widget.CheckBox;

/**
 * Created by gaoyehua on 2016/7/27.
 */
public class TaskInfo {
    //名称
    private String name;
    //图标
    private Drawable icon;
    ////占用的内存空间
    private long ramSize;
    //包名
    private String packageName;
    //是否是用户程序
    private boolean isUser;
    //checkBox
    private boolean ischecked =false;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public long getRamSize() {
        return ramSize;
    }

    public void setRamSize(long ramSize) {
        this.ramSize = ramSize;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
    public TaskInfo() {
        super();
    }
    public TaskInfo(String name, Drawable icon, long ramSize,
                    String packageName, boolean isUser) {
        super();
        this.name = name;
        this.icon = icon;
        this.ramSize = ramSize;
        this.packageName = packageName;
        this.isUser = isUser;
    }

    @Override
    public String toString() {
        return "TaskInfo [name=" + name + ", icon=" + icon + ", ramSize="
                + ramSize + ", packageName=" + packageName + ", isUser="
                + isUser + "]";
    }


    public boolean ischecked() {
        return ischecked;
    }

    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }
}
