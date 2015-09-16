/*
 * Zening Zhang 
 * THis class is to achieve the real time data
 */
package HexoSkin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONValue;


public class RealTime {
		public RealTime(String[] reco) throws IOException {
			// TODO Auto-generated constructor stub
			readJson(reco);
		}
		
	
	private static long getUTCDate(){
		long secondsSinceEpoch = (long) ((long)System.currentTimeMillis() / 1000l);
		return secondsSinceEpoch;
	}
	private static String generateSHA(String key)
	{
		String sha1 = "";
		try
		{
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(key.getBytes("UTF-8"));
			sha1 = new String(Hex.encodeHex(crypt.digest()));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return sha1;
	}
	public static void main(String[] args) throws IOException {
		String[] reco=new String[4];
		reco[0]="emotionregstudy@gmail.com";
		reco[1]="emotion1";
		reco[2]="kl2mi5kNflARdRTbmR80z8PCO0SOdNpW7dqyXmiV";
		reco[3]="dBzvgbiBpqNmiRQ4";
		RealTime t=new RealTime(reco);
	}
	private void readJson(String reco[]) throws IOException{
			
			long utc_date = getUTCDate();
			//String URL="https://api.hexoskin.com/api/v1/record/?user="+reco[0];
			String URL="https://api.hexoskin.com/api/v1/record/57775/";

			String sha_header = generateSHA(reco[2] + utc_date + URL );
			URL obj = new URL(URL);
			String auth_string = reco[0] +":" + reco[1];
			byte[] authEncBytes = Base64.encodeBase64(auth_string.getBytes());
			String auth_string_encoded = new String(authEncBytes);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// Setting Request Properties for HTTP GET Header
			con.setRequestProperty("X-HEXOTIMESTAMP", Long.toString(utc_date));
			con.setRequestProperty("X-HEXOAPIKEY", reco[3] );
			con.setRequestProperty("X-HEXOAPISIGNATURE", sha_header );
			con.setRequestProperty("Authorization", "Basic " + auth_string_encoded);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			Object object =  JSONValue.parse(response.toString());
			JSONArray arr1=JSONArray.fromObject(object);
//			JSONObject jobj1 = arr1.getJSONObject(0);
//			JSONArray arr2=JSONArray.fromObject(jobj1.getString("objects"));
//			JSONObject jobj2 = arr2.getJSONObject(0);
//			JSONObject jobj3=jobj2.getJSONObject("user");
			System.out.println(arr1);
//			System.out.println(jobj3.get("first_name"));
//			System.out.println(jobj3.get("last_name"));
//			System.out.println(jobj2.get("start_date"));
//			
//			String date = jobj2.getString("start_date");
//			String fname = jobj3.getString("first_name");  
//			String lname = jobj3.getString("last_name");
//			
//			date = date.replace('-', '_');
//			
//			String fileName = fname + "_" + lname;
//			
//			System.out.println(fileName);
//			System.out.println(date);
			
//			String[] nick = date.split("T");
//		
//			
//			System.out.println(nick[0]);
//			System.out.println(nick[1]);
//			
//			String zening = nick[1].replace('+', 'T');
//			
//			String date1 = nick[1];
//			
//			char dude = date1.charAt(8);
//			
			
			
			//String[] dude3 = nick[1].split(String.valueOf(dude));
			
		//	System.out.println(dude);
		//	System.out.println(dude3[0]);
	//		System.out.println(zening);
			
			
//			System.out.println();
			
	//		JSONArray arr3=JSONArray.fromObject(jobj2.getJSONArray("status"));
//			if(jobj2.getString("status").equals("realtime"))  //now using complete for testing
//			{
//				URL="https://api.hexoskin.com/api/v1/data/?datatype=54006&start=364154545486";
//				
//				sha_header = generateSHA(reco[2] + utc_date + URL );
//				obj = new URL(URL);
//				HttpsURLConnection conReal = (HttpsURLConnection) obj.openConnection();
//				conReal.setRequestMethod("GET");
//				
//				// Setting Request Properties for HTTP GET Header
//				conReal.setRequestProperty("X-HEXOTIMESTAMP", Long.toString(utc_date));
//				conReal.setRequestProperty("X-HEXOAPIKEY", reco[3] );
//				conReal.setRequestProperty("X-HEXOAPISIGNATURE", sha_header );
//				conReal.setRequestProperty("Authorization", "Basic " + auth_string_encoded);
//
//				BufferedReader inn = new BufferedReader(new InputStreamReader(con.getInputStream()));
//				String inputLi;
//				StringBuffer res = new StringBuffer();
//
//				while ((inputLi = inn.readLine()) != null) {
//					res.append(inputLi);
//				}
//				in.close();
//				System.out.println("success!");
//				Object objReal =  JSONValue.parse(res.toString());
//				JSONArray arrReal1=JSONArray.fromObject(objReal);
//				System.out.println(objReal);
//			}
//			else if(jobj2.getString("status").equals("complete"))
//			{
//				System.out.println("complete!");
//			}
			//System.out.println(jobj2.getString("status"));
		

			
		//	Jobj=(JSONObject) object;
			
		
	
	}

}

