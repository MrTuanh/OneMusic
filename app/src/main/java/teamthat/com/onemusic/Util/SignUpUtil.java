package teamthat.com.onemusic.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static teamthat.com.onemusic.activity.LoginActivity.LOGIN_API;

/**
 * Created by ASUS on 11/22/2016.
 */

public class SignUpUtil {
    public String getJson(String email,String password) {
        String data = null;
        try {
            data = URLEncoder.encode("dnjson", "UTF-8")
                    + "=" + URLEncoder.encode("", "UTF-8");
            data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                    + URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                    + URLEncoder.encode(password, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String text = "";
        BufferedReader reader=null;
        URL url = null;
        try {
            url = new URL(LOGIN_API);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write( data );
            wr.flush();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }
            text = sb.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally
        {
            try
            {

                reader.close();
            }

            catch(Exception ex) {}

        }

        return text;

    }
}
