package facebook2RDFproject;
import java.io.*;
import java.net.*;


public class TestGitShare {
	public static void main(String[] args) throws IOException {
			FacebookGetter getter = new FacebookGetter();
			getter.addField("gender");
			getter.addField("name");
			StringBuffer result = getter.request();
			System.out.println(result.toString());
			
		
			/*URL url = new URL("https://graph.facebook.com/869861266806845?access_token=EAAEwblz0z1sBACh8Yrm5OoYXBfZAZBWn1x88QgkkZBCOd81GnhZAfmkZABNAR6zIhHidAo40gqkr7O58h8ueM7AZBvusB4fZATmhSDgJhkfO2ZBSFpGQg4S6ydZB0ESng8BEIg265ii2vxosqw541ZCIGpyuJUYRzeNkOXVBTQBIbchKaIXyoVRf4vZAJtxCvtZAyYMZD");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.connect();
			
			
			System.out.println(con.getResponseCode());
			System.out.println(con.getResponseMessage().toString());
			
			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer content = new StringBuffer();
					while ((inputLine = in.readLine()) != null) {
					    content.append(inputLine);
					}
				in.close();
			System.out.println(content.toString());
			con.disconnect();*/
	  }
	
}
