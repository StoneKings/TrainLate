package com.study.zhonglei.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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


    private AutoCompleteTextView czText;
    private EditText ccText;
    private TextView textView;
    private RadioGroup radioGroup ;
    private RadioButton radioButton;


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
        czText = (AutoCompleteTextView) findViewById(R.id.czText);
        ccText = (EditText) findViewById(R.id.ccText);
        radioGroup = (RadioGroup) findViewById(R.id.cxlx);

        czText.setThreshold(1);
        String[]  countries_array = getResources().getStringArray(R.array.citys);
        SearchAdapter<String> czs = new SearchAdapter<>(this,android.R.layout.simple_list_item_1,countries_array,SearchAdapter.ALL);
        czText.setAdapter(czs);
    }


    public void searchInfo(View view){

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
                if(radioGroup.getCheckedRadioButtonId()==R.id.fc)
                {
                    path += "&cxlx=" + 1;
                }else{
                    path += "&cxlx=" + 0;
                }
                 path += "&rq="  + changeDate();
                 path += "&czEn=" + URLEncoder.encode(cz.trim()).replace("%","-");
                 path += "&tp=" + new Date().getTime();
                 System.out.println(path);
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

}
