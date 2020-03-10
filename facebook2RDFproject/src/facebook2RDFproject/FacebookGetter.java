package facebook2RDFproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class FacebookGetter {
		private String accessToken = "";
		private String userId = "";
		private ArrayList<String> fields = new ArrayList<String>();
		public FacebookGetter(String userId, String accessToken) {
			super();
			this.accessToken = accessToken;
			this.userId = userId;
		}
		
		public FacebookGetter() {
			this("869861266806845", "EAAEwblz0z1sBACh8Yrm5OoYXBfZAZBWn1x88QgkkZBCOd81GnhZAfmkZABNAR6zIhHidAo40gqkr7O58h8ueM7AZBvusB4fZATmhSDgJhkfO2ZBSFpGQg4S6ydZB0ESng8BEIg265ii2vxosqw541ZCIGpyuJUYRzeNkOXVBTQBIbchKaIXyoVRf4vZAJtxCvtZAyYMZD");
		}
		
		public void addField(String field) {
			fields.add(field);
		}

		public  StringBuffer request() throws IOException {
			URL url = null;
			if (this.fields.size()<1) {
				 url = new URL("https://graph.facebook.com/"+this.userId+"?access_token="+this.accessToken);
			}
			else {
				String concatenatedFields ="";
				for (String field : fields) {
					concatenatedFields+=field+",";
				}
				concatenatedFields=concatenatedFields.substring(0, concatenatedFields.length()-1);
				System.out.println(concatenatedFields);
				url = new URL("https://graph.facebook.com/"+this.userId+"?access_token="+this.accessToken+"&fields="+concatenatedFields);
			}
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
			con.disconnect();
			return content;
	  }
		
}
