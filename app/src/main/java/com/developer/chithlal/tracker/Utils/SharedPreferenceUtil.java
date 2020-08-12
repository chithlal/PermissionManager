package com.developer.chithlal.tracker.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUtil {
    private Context context;
    SharedPreferences sharedpreferences;
    String prefName="Permission_pref";
    SharedPreferences.Editor editor;
    String DANGEROUS="DA_NG";
    String NORMAL="NO_ML";
    String MODERATE="MOD_ETE";
    String SIGNATURE="SIG";
    String TOTAL_DAN="TOT_DAN";
    String TOTAL_NOR="TOT_NOR";
    String TOTAL_MOD="TOT_MOD";
    String TOTAL_SIG="TOT_SIG";
    public SharedPreferenceUtil(Context context){

        this.context = context;
        sharedpreferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
        editor=sharedpreferences.edit();
    }
    public void setDangerous(int count){
        editor.putInt(DANGEROUS,count);
        editor.commit();

    }
    public void setNormal(int count){
        editor.putInt(NORMAL,count);
        editor.commit();

    }
    public void setSignature(int count){
        editor.putInt(SIGNATURE,count);
        editor.commit();

    }
    public void setModerate(int count){
        editor.putInt(MODERATE,count);
        editor.commit();

    }
    public int getDangerous(){
        return sharedpreferences.getInt(DANGEROUS,0);

    }
    public int getNormal(){
        return sharedpreferences.getInt(NORMAL,0);

    }
    public int getSignature(){
        return sharedpreferences.getInt(SIGNATURE,0);

    }
    public int getModerate(){
        return sharedpreferences.getInt(MODERATE,0);

    }
    public void setTotDangerous(int count){
        editor.putInt(TOTAL_DAN,count);
        editor.commit();

    }
    public void setTotNormal(int count){
        editor.putInt(TOTAL_NOR,count);
        editor.commit();

    }
    public void setTotSignature(int count){
        editor.putInt(TOTAL_SIG,count);
        editor.commit();

    }
    public void setTotModerate(int count){
        editor.putInt(TOTAL_MOD,count);
        editor.commit();

    }
    public int getTotDangerous(){
        return sharedpreferences.getInt(TOTAL_DAN,0);

    }
    public int getTotNormal(){
        return sharedpreferences.getInt(TOTAL_NOR,0);

    }
    public int getTotSignature(){
        return sharedpreferences.getInt(TOTAL_SIG,0);

    }
    public int getTotModerate(){
        return sharedpreferences.getInt(TOTAL_MOD,0);

    }
}
