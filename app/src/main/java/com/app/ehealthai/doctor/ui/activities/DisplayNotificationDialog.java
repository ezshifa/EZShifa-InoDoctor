package com.app.ehealthai.doctor.ui.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.app.ehealthaidoctor.R;
import com.bumptech.glide.Glide;

public class DisplayNotificationDialog extends AppCompatActivity implements View.OnClickListener {
    Dialog dialog;
    Button yes,no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        ImageView imageview = (ImageView) findViewById(R.id.imagenotifd);
        Intent intent=getIntent();
        String notiftitle = intent.getStringExtra("notiftitle");
        String notifbody = intent.getStringExtra("notifbody");
        String notifimage= intent.getStringExtra("notifimage");
        try
        {
            if(notifimage.startsWith("http")) {
                imageview.setVisibility(View.VISIBLE);
                Glide.with(DisplayNotificationDialog.this).load(notifimage).error(DisplayNotificationDialog.this.getResources().getDrawable(R.drawable.ic_notif2)).dontAnimate().into(imageview);
            }

        }
        catch (Exception e)
        {

        }
        TextView title = (TextView) findViewById(R.id.titley);
        title.setText(notiftitle);
        TextView body = (TextView) findViewById(R.id.bodyy);
        body.setText(notifbody);
        TextView btnclose = (Button) findViewById(R.id.start_compaign_yes);
        btnclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // do something when the corky is clicked
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.start_compaign_yes){
            this.finish();
            Log.e("","customer press yes.");

        }else if (v.getId() == R.id.start_compaign_no){
            Log.e("","customer press no.");
        }
    }
}