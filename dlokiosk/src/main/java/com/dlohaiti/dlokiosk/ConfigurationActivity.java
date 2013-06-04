package com.dlohaiti.dlokiosk;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.dlohaiti.dlokiosk.db.ConfigurationKey;
import com.dlohaiti.dlokiosk.db.ConfigurationRepository;
import com.dlohaiti.dlokiosk.domain.Kiosk;
import com.google.inject.Inject;
import roboguice.activity.RoboActivity;
import roboguice.inject.InjectView;

public class ConfigurationActivity extends RoboActivity {
    @InjectView(R.id.kiosk_id) private EditText kioskIdTextBox;
    @InjectView(R.id.kiosk_password) private EditText kioskPasswordTextBox;
    @InjectView(R.id.reports_home_url) private EditText reportsHomeUrl;
    @Inject private ConfigurationRepository configurationRepository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        Kiosk kiosk = configurationRepository.getKiosk();
        kioskIdTextBox.setText(kiosk.getId());
        kioskPasswordTextBox.setText(kiosk.getPassword());
        reportsHomeUrl.setText(configurationRepository.get(ConfigurationKey.REPORTS_HOME_URL));
    }

    public void save(View v) {
        String kioskId = kioskIdTextBox.getText().toString();
        String kioskPassword = kioskPasswordTextBox.getText().toString();
        String reportsHome = reportsHomeUrl.getText().toString();
        configurationRepository.save(kioskId, kioskPassword);
        configurationRepository.save(ConfigurationKey.REPORTS_HOME_URL, reportsHome);
        finish();
    }
}
