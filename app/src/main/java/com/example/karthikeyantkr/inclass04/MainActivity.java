/*
Assignment no: InClass 04 a
File Name : InClass04
Names: 1.Karthikeyan Thorali Krishnamurthy Ragunath
        2.Lakshmanan Ramu Meenal

*/


package com.example.karthikeyantkr.inclass04;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    Handler handler;
    ExecutorService threadPool;
    CharSequence[] genreated_pwd ;
    CharSequence[] genreated_pwd5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        setTitle("InClass4");

        setContentView(R.layout.activity_main);

        threadPool = Executors.newFixedThreadPool(2);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Generating Passwords...");
        progressDialog.setMax(100);
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);


        SeekBar pwd_length = (SeekBar) findViewById(R.id.seekBar2);
        pwd_length.setProgress(8);
        pwd_length.setMax(23);

        final TextView pwd_length_t = (TextView) findViewById(R.id.p_length);

        final TextView pwd_count_t = (TextView) findViewById(R.id.p_count);

        SeekBar pwd_number = (SeekBar) findViewById(R.id.seekBar);
        pwd_number.setProgress(0);
        pwd_number.setMax(10);


        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch(msg.what)
                {
                    case 1:
                        progressDialog.show();
                        break;

                    case 2:
                        progressDialog.setProgress((Integer) msg.obj);
                        break;

                    case 3:
                        progressDialog.dismiss();
                        Bundle genreated_pwd1= (Bundle) msg.obj;
                        genreated_pwd5 =genreated_pwd1.getStringArray("a");
                        break;

                }



                return false;
            }
        });


        pwd_number.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwd_count_t.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        pwd_length.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwd_length_t.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        Button b_async= (Button) findViewById(R.id.button_async);

        b_async.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pwd_count_s = Integer.parseInt(pwd_count_t.getText().toString());
                int pwd_length_s = Integer.parseInt(pwd_length_t.getText().toString());

                threadPool.execute(new DoWork(pwd_length_s,pwd_count_s));

            }
        });


                Button thread_pwd_generator = (Button) findViewById(R.id.Thread_button);
        thread_pwd_generator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                int pwd_count_s = Integer.parseInt(pwd_count_t.getText().toString());
                int pwd_length_s = Integer.parseInt(pwd_length_t.getText().toString());

                    threadPool.execute(new DoWork(pwd_length_s,pwd_count_s));



                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Passwords")
                        .setSingleChoiceItems(genreated_pwd5, -1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("generated",""+genreated_pwd5[which]);
                            }
                        });
            }
        });
        Log.d("DEmo", String.valueOf(genreated_pwd));

    }


    class DoWork implements Runnable {

        Bundle b = new Bundle();
    int pwd_length=0,pwd_count=0;
        public DoWork(int pwd_length_s,int pwd_count_s) {

    Log.d("demo",""+pwd_length_s);
            Log.d("Demo",""+pwd_count_s);
            pwd_length=pwd_length_s;
            pwd_count=pwd_count_s;
        }



        @Override
        public void run() {


            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);

            Log.d("demo","inside run"+pwd_length);

            for(int i = 0; i < pwd_count; i++) {
                genreated_pwd[i]= Util.getPassword(pwd_length);

                msg = new Message();
                msg.what = 2;
                msg.obj = 100/pwd_count;
                handler.sendMessage(msg);

            }


            b.putStringArray("a", (String[]) genreated_pwd);



            msg = new Message();
            msg.what = 3;
            msg.obj=b;
            handler.sendMessage(msg);
        }
    }
}
