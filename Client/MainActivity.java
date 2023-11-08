package com.example.remoteclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import java.io.IOException;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Mouse mouse;
    FrameLayout frameLayout;
    Button btn_left;
    Button btn_right;
    boolean left_down;
    boolean left_up;
    boolean right_down;
    boolean right_up;
    EditText type;
    boolean sent;
    boolean delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mouse = new Mouse(this);
        frameLayout = (FrameLayout)findViewById(R.id.mainframe);
        frameLayout.addView(mouse);

        type = (EditText)findViewById(R.id.type);

        btn_left = (Button) findViewById(R.id.button_left);
        btn_left.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do something
                        left_down = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        // No longer down
                        left_up = true;
                        return true;
                }
                return false;
            }});

        btn_right = (Button) findViewById(R.id.button_right);
        btn_right.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // Do something
                        right_down = true;
                        return true;
                    case MotionEvent.ACTION_UP:
                        // No longer down
                        right_up = true;
                        return true;
                }
                return false;
            }});


        sent = false;

        type.setOnKeyListener(this);

        ClientThread connectThread = new ClientThread();
        connectThread.start();


    }

    @Override
    public void onClick(View v)
    {


    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event)
    {
        if( (event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_DEL) )
        {
            delete = true;
            return true;
        }
        return false;
    }

    class ClientThread extends Thread {

        @Override
        public void run()
        {
            boolean connected = false;
            try
            {
                SocketHandler.setSocket(new Socket("192.168.200.4", 12345));
                connected = true;
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            while (connected)
            {

                if (mouse.changed)
                {
                    SocketHandler.send_with_size("MOUSE_" + String.valueOf(mouse.xDist) + "_" + String.valueOf(mouse.yDist));
                    mouse.changed = false;
                }
                if (left_down)
                {
                    SocketHandler.send_with_size("LEFT_DOWN");
                    left_down = false;
                }
                if (left_up)
                {
                    SocketHandler.send_with_size("LEFT_UP");
                    left_up = false;
                }
                if (right_down)
                {
                    SocketHandler.send_with_size("RIGHT_DOWN");
                    right_down = false;
                }
                if (right_up)
                {
                    SocketHandler.send_with_size("RIGHT_UP");
                    right_up = false;
                }

                if(delete)
                {
                    SocketHandler.send_with_size("DEL");
                    delete = false;
                }

               if (type.getText().toString().length() > 0 && !sent)
               {
                   SocketHandler.send_with_size("TYPE_" + type.getText().toString());

                   sent = true;

                   runOnUiThread(new Runnable() {

                       @Override
                       public void run() {

                           type.setText("");
                           sent = false;


                       }
                   });


               }
            }
        }
    }
}
