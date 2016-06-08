package se.mah.babbage;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class CommunicationForm extends AppCompatActivity {
    private SeekBar seekBar;
    private EditText input;
    private Button sendInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_communication_form);

        input = (EditText)findViewById(R.id.user_input);
        sendInfo = (Button)findViewById(R.id.btnSend);
        sendInfo.setOnClickListener(new send());




    }

    private class send implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Context context = getApplicationContext();
            Toast.makeText(context, "Tack för info, vi kommer se över detta!", Toast.LENGTH_LONG).show();
        }
    }
}