package se.mah.babbage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class CommunicationForm extends Activity {
    private SeekBar seekBar;
    private EditText input;
    private Button sendInfo;
    private String markerId;
    private String name;
    private Controller controller;
    private TextView tvPoiName, tvRating, tvGood, tvBad;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_communication_form);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            markerId = extras.getString("id");
            name = extras.getString("name");
        }


        tvPoiName = (TextView) findViewById(R.id.tvPoiName);
        tvPoiName.setText(name);

        tvRating = (TextView) findViewById(R.id.tvRating);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setMax(10);
        seekBar.setProgress(5);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvRating.setText("Ditt betyg: " + seekBar.getProgress() + "/10");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        input = (EditText) findViewById(R.id.etComments);
        sendInfo = (Button) findViewById(R.id.btnSend);
        sendInfo.setOnClickListener(new send());
        controller = new Controller(this);

        tvBad = (TextView) findViewById(R.id.tvBad);
        tvBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() > 0) {
                    seekBar.setProgress(seekBar.getProgress() - 1);
                }
            }
        });

        tvGood = (TextView) findViewById(R.id.tvGood);
        tvGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBar.getProgress() < 10) {
                    seekBar.setProgress(seekBar.getProgress() + 1);
                }
            }
        });
    }

    private class send implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String comment = input.getText().toString();
            int rating = seekBar.getProgress();
            controller.sendUGC(markerId, comment, rating);
            Context context = getApplicationContext();
            Toast.makeText(context, "Tack för info, vi kommer se över detta!", Toast.LENGTH_LONG).show();
            onBackPressed();
        }
    }
}