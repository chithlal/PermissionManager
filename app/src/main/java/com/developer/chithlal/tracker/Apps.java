package com.developer.chithlal.tracker;

import android.graphics.drawable.Drawable;

import java.util.Comparator;

public class Apps  {
    private String name;
    private String packageName;
    private Drawable icon;
    public Apps(String name,Drawable icon,String packageName){
        this.name=name;
        this.icon = icon;
        this.packageName = packageName;
    }

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

    public String getPackageName() {
        return packageName;
    }
    public static Comparator<Apps> AppNameComparator = new Comparator<Apps>() {

        public int compare(Apps app1, Apps app2) {
            String appName1 = app1.getName().toUpperCase();
            String appName2 = app2.getName().toUpperCase();

            //ascending order
            return appName1.compareTo(appName2);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }};
}
