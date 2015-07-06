package com.amoszhang.camwifi;

/**
 * Created by Amos Zhang on 2015/7/3.
 */
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class ClientThread extends Thread{
    public final String ip = "192.168.1.100";
    public final int port = 8082;
    public Socket socket = null;
    public OutputStream outputstream = null;
    public DataOutputStream out = null;
    public String message;
    public volatile boolean exit = false;

    /*
     * ���캯�������ӷ�����
     */
    public ClientThread(){
        Runnable runnable = new Runnable(){

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    Log.d("DEBUG","Client Connecting...");
                    //IP��ַ�Ͷ˿ںţ���Ӧ����ˣ�
                    socket = new Socket(ip, port);
                    outputstream = socket.getOutputStream();
                    out = new DataOutputStream(outputstream);
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d("DEBUG","Client connected!!");
            }

        };
        new Thread(runnable).start();
        start();
    }
    /*
     * �߳�run����
     */
    public void run(){
        while(!exit){
            synchronized(this){
                try {
                    wait();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            // ���Ѻ�Ĵ���
            Log.d("DEBUG","Notified");
            try {
                out.writeBytes(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void send(String str){
        Log.d("DEBUG","Send");
        message = str;
        synchronized(this){
            notifyAll();
        }
    }
}
