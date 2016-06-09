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
import android.widget.Toast;

public class CommunicationForm extends Activity {
    private SeekBar seekBar;
    private EditText input;
    private Button sendInfo;
    private String markerId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_communication_form);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            markerId = extras.getString("id");
        }

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        input = (EditText)findViewById(R.id.user_input);
        sendInfo = (Button)findViewById(R.id.btnSend);
        sendInfo.setOnClickListener(new send());
    }

    private class send implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            //Här ska vi skicka infon!
            Context context = getApplicationContext();
            Toast.makeText(context, "Tack för info, vi kommer se över detta!", Toast.LENGTH_LONG).show();
        }
    }
}