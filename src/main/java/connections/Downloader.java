package connections;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public final class Downloader {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36";
	
	public static String getData(String url) {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestMethod("GET");
			con.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));

			String res;
			StringBuffer sb = new StringBuffer();
			while((res = br.readLine()) != null)
			{
				sb.append(res + "\n");
			}
			return sb.toString();
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}

	}
}
