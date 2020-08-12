package com.developer.chithlal.tracker;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class PermissionModel implements Serializable {
    private String permissionName;
    private int permissioinIcon;
    private boolean status;
    private int level;
    private int desc;
    public PermissionModel(String permissionName,int permissioinIcon,boolean status,int level,int desc){

        this.permissionName = permissionName;
        this.permissioinIcon = permissioinIcon;
        this.status = status;
        this.level = level;
        this.desc = desc;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public int getPermissioinIcon() {
        return permissioinIcon;
    }

    public boolean isStatus() {
        return status;
    }

    public int getLevel() {
        return level;
    }

    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
        this.desc = desc;
    }
}
