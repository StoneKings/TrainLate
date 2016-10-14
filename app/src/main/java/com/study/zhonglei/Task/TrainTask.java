package com.study.zhonglei.Task;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by zhonglei on 16/10/14.
 */
public class TrainTask extends AsyncTask<String,Void,Object> {

	private Context context ;
	private TrainCallBack trainCallBack;
	private String path;

	public  TrainTask(Context context ,String path ,  TrainCallBack trainCallBcak){
		this.context = context;
		this.path = path;
		this.trainCallBack = trainCallBcak;
	}
	@Override
	protected void onPostExecute(Object o) {
		trainCallBack.callBack(o);
	}

	@Override
	protected Object doInBackground(String... strings) {
		String text = "";
		URL url = null;
		try {
			url = new URL(path);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setRequestMethod("GET");
			httpURLConnection.setConnectTimeout(5000);
			httpURLConnection.setReadTimeout(5000);
			if (httpURLConnection.getResponseCode() == 200) {

				InputStream inputStream = httpURLConnection.getInputStream();
				InputStreamReader isr = new InputStreamReader(inputStream, "GB2312");//添加这一句话设置相应编码格式

				BufferedReader br = new BufferedReader(isr);
				String temp = null;
				while ((temp = br.readLine()) != null) {
					text += temp;
				}

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return text;

	}

	public interface   TrainCallBack{
		 void callBack(Object Object);
	}
}
