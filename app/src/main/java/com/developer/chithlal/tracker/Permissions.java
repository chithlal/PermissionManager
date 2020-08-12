package com.developer.chithlal.tracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.developer.chithlal.tracker.Utils.PermissionUtils;

import java.util.ArrayList;
import java.util.List;

public class Permissions extends AppCompatActivity {
    RecyclerView recyclerView;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Permissions");
        Intent intent=getIntent();
        final String packageName;
        packageName=intent.getStringExtra("PACKAGE");
        Log.d("Pack", "onCreate: "+packageName);

        setContentView(R.layout.activity_permissions);
        recyclerView=findViewById(R.id.rv_permission);
        refreshLayout=findViewById(R.id.permission_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        final Context context=this;
        final PermissionUtils permissionUtils=new PermissionUtils();
        PermissionAdapter permissionAdapter = new PermissionAdapter(permissionUtils.normalze(getPermissions(packageName)),this,packageName);
        recyclerView.setAdapter(permissionAdapter);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.green),getResources().getColor(R.color.red));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                PermissionAdapter permissionAdapter = new PermissionAdapter(permissionUtils.normalze(getPermissions(packageName)),context,packageName);
                recyclerView.setAdapter(permissionAdapter);
                refreshLayout.setRefreshing(false);
            }
        });
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
}
