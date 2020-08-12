package com.developer.chithlal.tracker.Statistics;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.developer.chithlal.tracker.MainActivity;
import com.developer.chithlal.tracker.R;
import com.developer.chithlal.tracker.Services.AnalyzerJob;
import com.developer.chithlal.tracker.Services.AppService;
import com.developer.chithlal.tracker.Utils.SharedPreferenceUtil;
import com.github.lzyzsd.circleprogress.ArcProgress;

import jp.wasabeef.blurry.Blurry;

public class Statistics extends AppCompatActivity {
LinearLayout root;
    JobScheduler jobScheduler;
    ArcProgress normal,dangerous,moderate,signature;
    LinearLayout lNormal,lDang,lSig,lMod;
Button manage;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        normal=findViewById(R.id.arc_progress_normal);
        dangerous=findViewById(R.id.arc_progress_dangerous);
        moderate=findViewById(R.id.arc_progress_moderate);
        signature=findViewById(R.id.arc_progress_signature);
        manage=findViewById(R.id.bt_manage_permission);
        lDang=findViewById(R.id.l_dang);
        lNormal=findViewById(R.id.l_normal);
        lSig=findViewById(R.id.l_sig);
        lMod=findViewById(R.id.l_mod);
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            startService(new Intent(this,AppService.class));
        else
        createAnalyzerJob();
        manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
     //   11root=findViewById(R.id.stat_root);
        final Activity activity=this;

        SharedPreferenceUtil sp=new SharedPreferenceUtil(this);
        normal.setMax(sp.getTotNormal());
        dangerous.setMax(sp.getTotDangerous());
        moderate.setMax(sp.getTotModerate());
        signature.setMax(sp.getTotSignature());
        normal.setProgress(sp.getNormal());

        dangerous.setProgress(sp.getDangerous());

        moderate.setProgress(sp.getModerate());

        signature.setProgress(sp.getSignature());
        normal.setFinishedStrokeColor(getResources().getColor(R.color.green));
        dangerous.setFinishedStrokeColor(getResources().getColor(R.color.red));
        moderate.setFinishedStrokeColor(getResources().getColor(R.color.blue));
        signature.setFinishedStrokeColor(getResources().getColor(R.color.orrange));

//        Blurry.with(activity).radius(25).sampling(2).onto((ViewGroup)lMod);
//        Blurry.with(activity).radius(25).sampling(2).onto((ViewGroup)lSig);
//        Blurry.with(activity).radius(25).sampling(2).onto((ViewGroup)lNormal);
//        Blurry.with(activity).radius(25).sampling(2).onto((ViewGroup)lDang);

      /*  Handler handler=new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Blurry.with(activity).radius(25).sampling(2).onto((ViewGroup) (activity).getWindow().getDecorView().findViewById(android.R.id.content));


            }
        },1000);*/
      //  Blurry.with(this).radius(25).sampling(2).onto((ViewGroup) ((Activity)this).getWindow().getDecorView().findViewById(android.R.id.content));
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void createAnalyzerJob(){
        jobScheduler=(JobScheduler)getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder( 1,
                new ComponentName( getPackageName(),
                        AnalyzerJob.class.getName() ) );


        builder.setPeriodic(12000000);
        builder.setPersisted(true);
        if( jobScheduler.schedule( builder.build() ) <= 0 ) {
            //If something goes wrong
            Toast.makeText(this, "Unable to analyze system", Toast.LENGTH_SHORT).show();
        }
    }
}
