package me.mateass.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;


public class AuthExec 
{
	 
		public String sendPost(URL url, String post, String contentType) throws Exception 
		{			
			byte[] postBytes = post.getBytes(Charset.forName("UTF-8"));
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			
			conn.setRequestMethod("POST");
			
			conn.setRequestProperty("Content-Type", contentType + "; charset=utf-8");
			conn.setRequestProperty("Content-Lenght", "" + postBytes.length);
			
			conn.setDoOutput(true);
			
			OutputStream os = null;
			
			try {
				os = conn.getOutputStream();
				os.write(postBytes);
			} finally {
				os.close();
			}
			
			InputStream is = null;
			
			try {
				is = conn.getInputStream();
				String result = isToString(is);
				return result;
			} catch(Exception e) {
				closeIsWithoutError(is);
				is = conn.getErrorStream();
				String result = isToString(is);
				return result;
			} finally {
				closeIsWithoutError(is);
			}
		}			
			
		
		public String isToString(final InputStream is) throws Exception
		{
		  final char[] buffer = new char[1024];
		  final StringBuilder out = new StringBuilder();
		    final Reader in = new InputStreamReader(is, "UTF-8");
		    try {
		      for (;;) {
		        int rsz = in.read(buffer, 0, buffer.length);
		        if (rsz < 0)
		          break;
		        out.append(buffer, 0, rsz);
		      }
		    }
		    finally {
		      in.close();
		    }
		  
		  return out.toString();
		}
		
		public void closeIsWithoutError(InputStream is) {
			try {
					if(is != null) {
						is.close();
					}			
				} catch (IOException e) { }
		}
			
}
