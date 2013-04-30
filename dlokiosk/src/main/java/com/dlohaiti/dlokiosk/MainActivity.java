package com.dlohaiti.dlokiosk;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity implements StatusView {
    @InjectView(R.id.serverStatusProgressBar)
    ProgressBar serverStatusProgressBar;
    @InjectView(R.id.statusImage)
    ImageView statusImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            new CheckServerStatusTask(this.getApplicationContext(), this, getString(R.string.dlo_server_url)).execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void moveToNewReadingScreen(View view) {
        Intent intent = new Intent(this, EnterReadingActivity.class);
        startActivity(intent);
    }

    public void doManualSync(View view) {
        new ManualSyncReadingsTask(this).execute();
    }

    @Override
    public void showProgressBar() {
        statusImage.setVisibility(View.INVISIBLE);
        serverStatusProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissProgressBar() {
        serverStatusProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void refreshStatus(Boolean result) {
        int imageResource;
        if (result) {
            imageResource = R.drawable.green_checkmark;
        } else {
            imageResource = R.drawable.red_x;
        }
        statusImage.setImageResource(imageResource);
        statusImage.setVisibility(View.VISIBLE);
    }
}
