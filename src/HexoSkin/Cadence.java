/*
 * @Manav Singhal
 * Cadence.java - To get cadence data for record ID passed as input parameter to this program.
 * 
 * */
package HexoSkin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public class Cadence {

	private static String PARAMETER = "cadence";
	private static String PARAMETER_ID = "53";
	private static String changeLine=System.getProperty("line.separator");

	// Reading Developer Credentials
	private static String IDS, RECORD_ID, PRIVATE_KEY, PUBLIC_KEY, USERNAME, PASSWORD, BASE_DIRECTORY;
	private static String URL ;
	private static String fileName;
	private String reco[]=new String[4];

	static String sha_header, auth_string, auth_string_encoded;
	static long utc_date;

	
	public Cadence(String recoo[]) {
		this.reco=recoo;
		// TODO Auto-generated constructor stub
	}
	

	public void getCredentials(String reco[]) throws IOException{

		ReadProperties properties = new ReadProperties();
		properties.process(reco);

		if (properties.getDirectory().length() == 0 || properties.getPassword().length() == 0 || properties.getPrivateKey().length() == 0 ||
				properties.getPublicKey().length() == 0 || properties.getRecordID().length() == 0 || properties.getUsername().length() == 0)
		{
			//System.out.println("Properties not set in config.properties");
		}
		else{
			PRIVATE_KEY =reco[2];
			PUBLIC_KEY =reco[3];
			USERNAME = reco[0];
			PASSWORD = reco[1];
			IDS = properties.getRecordID();
			BASE_DIRECTORY = properties.getDirectory();

			if (IDS.contains(",")){
				String[] records = IDS.split(",");
				
				for (int i = 0; i < records.length; i++){
					
					RECORD_ID = records[i];
					RECORD_ID = RECORD_ID.replaceAll("^\\s+|\\s+$", "");
					
					URL = Constants.BASE_URL + PARAMETER +"/?record=" + RECORD_ID ;
					fileName = RECORD_ID +"_"+ PARAMETER +".csv" ;
					try {
						Thread.sleep(700);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					startExecution(reco);
				}
			}
			else{
				RECORD_ID = IDS;
				URL = Constants.BASE_URL + PARAMETER +"/?record=" + RECORD_ID ;
				fileName = RECORD_ID +"_"+ PARAMETER +".csv" ;
				
				startExecution(reco);
			}
		}
	}
	
	public void startExecution(String reco[]){

		try{
			// Get Date in UTC format
			utc_date = getUTCDate();
			
			// Generate SHA-1 key using Private Key + UTC_Date + URL
			sha_header = generateSHA(PRIVATE_KEY + utc_date + URL );
			
			// Base64 Encoding for Auth. String 
			auth_string = USERNAME +":" + PASSWORD;
			byte[] authEncBytes = Base64.encodeBase64(auth_string.getBytes());
			auth_string_encoded = new String(authEncBytes);
			
			sendGetRequest(reco);
		}
		catch(Exception exception){
			System.out.println(exception.toString());
		}

	}

	// Method to retrieve date in UTC format
	private static long getUTCDate(){
		long secondsSinceEpoch = (long) ((long)System.currentTimeMillis() / 1000l);
		return secondsSinceEpoch;
	}

	// Method definition for sending HTTP GET request
	private void sendGetRequest(String reco[]) {

		try{
			URL obj = new URL(URL);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");

			// Setting Request Properties for HTTP GET Header
			con.setRequestProperty("X-HEXOTIMESTAMP", Long.toString(utc_date));
			con.setRequestProperty("X-HEXOAPIKEY", PUBLIC_KEY );
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
			JSONArray jsonArray = (JSONArray) object;
			if(jsonArray.size()>0)
			{
			JSONObject mainObj = (JSONObject) jsonArray.get(0);
			JSONObject dataObj = (JSONObject) mainObj.get("data");
			JSONArray valueArray = (JSONArray) dataObj.get(PARAMETER_ID);
			
			// Writing data to user-defined (.csv) filepath
			String[] folderName=reco[0].split("@");
			File dirBaseOnUserName = new File(BASE_DIRECTORY + folderName[0]);
			if (!dirBaseOnUserName.exists()) {
				boolean success = dirBaseOnUserName.mkdir();
				System.out.println("here!");
			}
			//------------To get the sub folder name-----------
			obj=new URL("https://api.hexoskin.com/api/v1/record/"+RECORD_ID+"/");
			con = (HttpsURLConnection) obj.openConnection();
			sha_header = generateSHA(PRIVATE_KEY + utc_date + "https://api.hexoskin.com/api/v1/record/"+RECORD_ID+"/" );
			con.setRequestMethod("GET");

			// Setting Request Properties for HTTP GET Header
			con.setRequestProperty("X-HEXOTIMESTAMP", Long.toString(utc_date));
			con.setRequestProperty("X-HEXOAPIKEY", PUBLIC_KEY );
			con.setRequestProperty("X-HEXOAPISIGNATURE", sha_header );
			con.setRequestProperty("Authorization", "Basic " + auth_string_encoded);

			in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			object =  JSONValue.parse(response.toString());
			net.sf.json.JSONArray arr1=net.sf.json.JSONArray.fromObject(object);
			
			net.sf.json.JSONObject jobj1=arr1.getJSONObject(0);
			String startDate=jobj1.getString("start_date");
			net.sf.json.JSONObject jobj2=jobj1.getJSONObject("user");
			String subfolder=jobj2.getString("last_name");
			startDate=startDate.replaceAll("-", "_");
			startDate=startDate.replaceAll("T", "_");
			startDate=startDate.replaceAll(":", "_");
			//------------To get the sub folder name-----------
			
			
			File directory = new File(BASE_DIRECTORY + folderName[0] +"/" + subfolder );
			if (!directory.exists()) {
				boolean success = directory.mkdir();
			}
			
			directory = new File(BASE_DIRECTORY + folderName[0] +"/" + subfolder + "/" +subfolder + "_" + RECORD_ID + "_" + startDate);
			if (!directory.exists()) {
				boolean success = directory.mkdir();
			}
			FileWriter writer = new FileWriter(directory + "/" + fileName);

			writer.append("TimeStamp");
			writer.append(',');
			writer.append("Value");
			writer.append('\n');

			for (int i = 0; i< valueArray.size(); i++){

				JSONArray eachValueArray = (JSONArray) valueArray.get(i);
				Long timestamp = (Long) eachValueArray.get(0);
				Long value = (Long) eachValueArray.get(1);

				Long actualTimestamp = timestamp / 256;
				Date date = new Date(actualTimestamp * 1000);
				
				writer.append(date+"");
				writer.append(',');
				writer.append(value+"");
				writer.append('\n');
			}

			writer.flush();
			writer.close();

			System.out.println(RECORD_ID + " - " +PARAMETER + " data saved to file successfully !! " + changeLine);
			}
			else{
				System.out.println(RECORD_ID + " - " +PARAMETER + " no data sync !! " + changeLine);
			}
		}
		catch(Exception exception){
			System.out.println(exception.toString());
		}
	}

	// Generating SHA-1 encryption
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
}
