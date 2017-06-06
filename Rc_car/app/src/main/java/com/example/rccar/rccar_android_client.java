package com.example.rccar;

import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private EditText et;
    private String ip;
    private int port;

    private Handler mHandler;
    private Socket socket;

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

        }

        btn = (Button) findViewById(R.id.button_set);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        set(v);
                    }
                }
        );

        mHandler = new Handler();

        btn = (Button) findViewById(R.id.button_go);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        PrintWriter out = new PrintWriter(networkWriter, true);
                        out.println("g");
                    }
                }
        );

        btn = (Button) findViewById(R.id.button_stop);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        PrintWriter out = new PrintWriter(networkWriter, true);
                        out.println("s");
                    }
                }
        );

        btn = (Button) findViewById(R.id.button_end);
        btn.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        PrintWriter out = new PrintWriter(networkWriter, true);
                        out.println(" ");
                    }
                }
        );
    }

    public void set(View view)
    {
        et = (EditText) findViewById(R.id.editText_ip);
        ip = et.getText().toString();
        Log.e("set","ip : " + ip);

        et = (EditText) findViewById(R.id.editText_port);
        port = Integer.parseInt(et.getText().toString());
        Log.e("set","port : " + port);

        try {
            setSocket(ip, port);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void setSocket(String ip, int port) throws IOException {
        try {
            socket = new Socket(ip, port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }
    }
}
