package com.developer.chithlal.tracker.Utils;

import com.developer.chithlal.tracker.PermissionModel;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {
    private List<PermissionModel> permissionModelList;

    public List<PermissionModel> getPermissionModelList() {
        return permissionModelList;
    }

    public void setPermissionModelList(List<PermissionModel> permissionModelList) {
        this.permissionModelList = permissionModelList;
    }
    public List<PermissionModel> normalze(List<PermissionModel> list){
        List<PermissionModel> output=new ArrayList<>();
        for(PermissionModel per:list){
            String pack=per.getPermissionName();
            String[] words=pack.split("\\.");
            String name=words[words.length-1].replaceAll("_"," ");
            output.add(new PermissionModel(capitalise(name),per.getPermissioinIcon(),per.isStatus(),per.getLevel(),per.getDesc()));

        }


    return output;

    }
    private String capitalise(String string){
        string=string.toLowerCase();
        String[] words = string.split(" ");

// capitalize each word
        String nString="";
        for (int i = 0; i < words.length; i++)
        {
            words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
            nString +=words[i]+" ";
        }

// rejoin back into a sentence
        return nString;

    }
}
