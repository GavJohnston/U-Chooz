package com.example.gavin.newapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Gavin on 18/12/2017.
 */

public class DownloadURL {
    public String readUrl(String myUrl) throws IOException
    {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(myUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            BufferedReader br =new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();

            String Line = "";
            while((Line = br.readLine()) !=null)
            {
                sb.append(Line);
            }
            data = sb.toString();
            br.close();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        Log.d("DownloadURL","Returning Data"+data);
        return data;
    }

}

