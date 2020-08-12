package com.developer.chithlal.tracker.Services;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.developer.chithlal.tracker.Apps;
import com.developer.chithlal.tracker.PermissionModel;
import com.developer.chithlal.tracker.R;
import com.developer.chithlal.tracker.Utils.SharedPreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AnalyzerJob extends JobService {
    private String channel_id="100";
    private CharSequence channel_name="APP_NOTIFICATION";

    class Data{
        public int normalCount=0;
        public int dangerousCount=0;
        public int signatureCount=0;
        public int moderateCount=0;
        public int TotnormalCount=0;
        public int TotdangerousCount=0;
        public int TotsignatureCount=0;
        public int TotmoderateCount=0;
    }
    @Override
    public boolean onStartJob(JobParameters params) {
        Toast.makeText(this, "Job called", Toast.LENGTH_SHORT).show();
        Data data=analyze();
        if(data.dangerousCount>0){
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(createNotificationChannel());
            }
            createNotification(notificationManager,data.dangerousCount);

            Toast.makeText(this, "Dangerous Permission :"+data.dangerousCount, Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
    private List<Apps> getCurentList(){
        PackageManager packageManager = this.getPackageManager();
        List<Apps> appList=new ArrayList<>();
        Drawable icon=null;
        ApplicationInfo applicationInfo = null;
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfo = am.getRunningAppProcesses();
        List<ApplicationInfo> installedApps=packageManager.getInstalledApplications(0);
        for(ApplicationInfo info:installedApps){
            try {
                applicationInfo = packageManager.getApplicationInfo(info.packageName, 0);

            } catch (final PackageManager.NameNotFoundException e) {}
            final String title = (String)((applicationInfo != null) ? packageManager.getApplicationLabel(applicationInfo) : "No Name");
            appList.add(new Apps(title,null,info.packageName));
        }
        Collections.sort(appList,Apps.AppNameComparator);
        return appList;

    }
    List<PermissionModel> getPermissions(String packageName){

        List<PermissionModel> perms=new ArrayList<>();
        PackageManager pm=this.getPackageManager();
        try {
            PackageInfo pkgInfo = getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
            String[] requestedPermissions = pkgInfo.requestedPermissions;

            if (requestedPermissions == null) {
                perms.add(new PermissionModel("No declared permissions",0,false,0,0));
            } else {

                for (int i = 0; i < pkgInfo.requestedPermissions.length; i++) {
                    PermissionInfo info = pm.getPermissionInfo(requestedPermissions[i],0);
                    int level=info.protectionLevel;
                    int desc=info.descriptionRes;


                    if ((pkgInfo.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                        perms.add(new PermissionModel(requestedPermissions[i], R.drawable.ic_correct,true,level,desc));
                    }
                    else{
                        perms.add(new PermissionModel(requestedPermissions[i], R.drawable.ic_cancel,false,level,desc));
                    }

                }

            }

        } catch (PackageManager.NameNotFoundException e) {
            perms.add(new PermissionModel("No declared permissions",0,false,0,0));
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
        return perms;

    }
    public Data analyze(){
        List<Apps> appsList=getCurentList();

        Data data=new Data();
        for(Apps apps:appsList){
            String packageName=apps.getPackageName();
            List<PermissionModel> perms=getPermissions(packageName);
            for(PermissionModel permissionModel:perms){

                if(permissionModel.getLevel()==0){
                    data.TotnormalCount++;
                    if(permissionModel.isStatus())
                        data.normalCount++;
                }
                else if(permissionModel.getLevel()==1){
                    data.TotdangerousCount++;
                    if(permissionModel.isStatus())
                        data.dangerousCount++;}
                else if(permissionModel.getLevel()==2||permissionModel.getLevel()==3){
                    data.TotsignatureCount++;
                    if(permissionModel.isStatus())
                        data.signatureCount++;}
                else {
                    data.TotmoderateCount++;
                    if (permissionModel.isStatus())
                        data.moderateCount++;
                }
            }
        }
        SharedPreferenceUtil sharedPreferenceUtil=new SharedPreferenceUtil(this);
        sharedPreferenceUtil.setDangerous(data.dangerousCount);
        sharedPreferenceUtil.setNormal(data.normalCount);
        sharedPreferenceUtil.setModerate(data.moderateCount);
        sharedPreferenceUtil.setSignature(data.signatureCount);
        sharedPreferenceUtil.setTotDangerous(data.TotdangerousCount);
        sharedPreferenceUtil.setTotModerate(data.TotmoderateCount);
        sharedPreferenceUtil.setTotNormal(data.TotnormalCount);
        sharedPreferenceUtil.setTotSignature(data.TotsignatureCount);
        return data;

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel createNotificationChannel(){

        NotificationChannel notificationChannel = new NotificationChannel(channel_id , channel_name, NotificationManager.IMPORTANCE_HIGH);
        notificationChannel.enableLights(true);
        notificationChannel.enableVibration(true);
        notificationChannel.canShowBadge();
        notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        return notificationChannel;


    }
    public void createNotification(NotificationManager notificationManager,int count){
        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, channel_id)
                .setContentTitle("Permission Stats")
                .setContentText(count +" Dangerous Permission found")
                .setSmallIcon(R.drawable.ic_cancel)
                .setChannelId(channel_id);


        notificationManager.notify(1, notification.build());
    }
}
