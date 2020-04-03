package facebook2RDF;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
			this("869861266806845", "EAAEwblz0z1sBAOmZClCMVQPBuYc0XehTUZAfTZC793R277jKqwXXuxdL8yzckgoGvUGXjv1iEHKY3WFf27EeOfnHUovlZCO5EwNysju43GKNWreQZBSR4CH0ZAvZAEzvV3ZCbTP4pHJtekcS8qzPsppvIYJvftWDE7lX3u2wWN1T6kOreKxr37TCXWSZBuvU4kteKkNKZBqqbgogZDZD");
		}
		
		public void addField(String field) {
			fields.add(field);
		}
		public String getFields() {
			String concatenatedFields ="";
			for (String field : fields) {
				concatenatedFields+=field+",";
			}
			concatenatedFields=concatenatedFields.substring(0, concatenatedFields.length()-1);
			System.out.println(concatenatedFields);
			return concatenatedFields;
			
		}

		public  StringBuffer request() throws IOException {
			URL url = null;
			if (this.fields.size()<1) {
				 url = new URL("https://graph.facebook.com/"+this.userId+"?access_token="+this.accessToken);
			}
			else {
				String concatenatedFields= this.getFields();
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
		public ArrayList<String> requestToAL(){
			JSONObject obj = null;
			try {
				obj = new JSONObject(this.request());
			} catch (IOException e) {
				e.printStackTrace();
			}
			ArrayList<String> result = new ArrayList<String>();
			JSONArray parsed = null;
			try {
				parsed = obj.getJSONArray("interest");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for(int i = 0 ; i < parsed.length() ; i++){
				try {
					result.add(parsed.getJSONObject(i).getString("interestKey"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;
			
		}
		
		public static void main(String[] args) throws IOException {
			FacebookGetter getter = new FacebookGetter();
			getter.addField("gender");
			getter.addField("name");
			StringBuffer result = getter.request();
			System.out.println(result.toString());
			
	  }
}
