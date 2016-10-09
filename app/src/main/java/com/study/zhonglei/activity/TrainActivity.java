package com.study.zhonglei.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class TrainActivity extends Activity {


    private EditText czText;
    private EditText ccText;
    private TextView textView;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView = (TextView) findViewById(R.id.showResult);
            textView.setText(msg.obj.toString());
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
    }

    public void searchInfo(View view){
        czText = (EditText) findViewById(R.id.czText);
        ccText = (EditText) findViewById(R.id.ccText);

        final String cz = czText.getText().toString().trim();
        final String cc = ccText.getText().toString().trim();
        if(TextUtils.isEmpty(cc) || TextUtils.isEmpty(cz)){
            Toast.makeText(this,"请输入正缺的内容", Toast.LENGTH_SHORT).show();
            return;
        }
          new Thread(new Runnable() {
             @Override
             public void run() {


                 String path = "http://dynamic.12306.cn/map_zwdcx/cx.jsp?";
                 path += "cz=" + URLEncoder.encode(cz.trim());
                 path += "&cc=" + cc.trim();
                 path += "&cxlx=" + 0;
                 path += "&rq="  + changeDate();
                 path += "&czEn=" + URLEncoder.encode(cz.trim()).replace("%","-");
                 path += "&tp=" + new Date().getTime();
                 try {
                     URL url = new URL(path );
                     HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                     httpURLConnection.setRequestMethod("GET");
                     httpURLConnection.setConnectTimeout(5000);
                     httpURLConnection.setReadTimeout(5000);
                     if(httpURLConnection.getResponseCode() == 200){

                         InputStream inputStream = httpURLConnection.getInputStream();
                         InputStreamReader isr = new InputStreamReader(inputStream, "GB2312");//添加这一句话设置相应编码格式

                         BufferedReader br=new BufferedReader(isr);
                         String temp=null;

                         String text ="";
                         while ((temp =br.readLine()) !=null) {
                             text +=temp;
                         }
                         Message msg = handler.obtainMessage();
                         msg.obj = text;
                         handler.sendMessage(msg);
                     }
                 } catch (Exception e) {
                     e.printStackTrace();
                 }

             }
         }).start();


    }


    public String changeDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

}
