/*
 * This is to check the update and get all the Id
 * Zening Zhang
 */
package HexoSkin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ReadId {

	// Define user-credentials
	private static String PRIVATE_KEY, PUBLIC_KEY, USERNAME, PASSWORD, RECORDS_NUMBER, URL;
	
	private String reco[]=new String[4];
	//String privateKey,publicKey,userName,password
	static String sha_header, auth_string, auth_string_encoded;
	static long utc_date;
	
	public ReadId(String recoo[]) {
		this.reco=recoo;
		// TODO Auto-generated constructor stub
	}
	public void getCredentials(String reco[]) throws IOException{
		ReadProperties properties = new ReadProperties();
		properties.process(reco);
		if (properties.getCount().length() == 0 || properties.getPassword().length() == 0 || properties.getPrivateKey().length() == 0 ||
					properties.getPublicKey().length() == 0 || properties.getUsername().length() == 0){
			//System.out.println("Properties not set in config.properties");
		}
		else{
			
			PRIVATE_KEY =reco[2];
			PUBLIC_KEY =reco[3];
			USERNAME = reco[0];
			PASSWORD = reco[1];
			RECORDS_NUMBER = properties.getCount();
			URL = "https://api.hexoskin.com/api/v1/record/?limit="+ RECORDS_NUMBER;
			startExecution(reco);
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
	private void sendGetRequest(String reco[]) throws IOException {
		
		// The following code is to check for the update
		InputStream inputStream = null;
		Properties propR = new Properties();
		String userNameSpilting[]=reco[0].split("@");
		
		File file=new File(userNameSpilting[0]+"DataSet.properties");
		//System.out.println(file.getAbsolutePath());
		if(!file.exists())
		{
			Properties propSData = new Properties();
			
			//System.out.println(propS.setProperty("recordId", setPropId));
			FileOutputStream fos = new FileOutputStream(userNameSpilting[0]+"DataSet.properties"); 
			propSData.setProperty("recordId", "");
			propSData.setProperty("count","");
			propSData.store(fos, "Copyright (c) Boxcode Studio"); 
			fos.close();//closing outputstream
			
		}
		
		try {
			inputStream = new FileInputStream(userNameSpilting[0]+"DataSet.properties");
			propR.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String Id=propR.getProperty("recordId"); 
		try{
			// To scan the user name password etc...
			
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

			JSONParser parser = new JSONParser();
			JSONObject jsonObj = (JSONObject)parser.parse(response.toString());
			JSONArray objectArray = (JSONArray) jsonObj.get("objects");
			
			ArrayList<String> recordIDs = new ArrayList<>();
			String setPropId="";
			int counting=0; 
			for (int i=0; i < objectArray.size(); i++){
				JSONObject eachObject = (JSONObject) objectArray.get(i);
				long recordId = (long) eachObject.get("id");
				recordIDs.add(recordId+"");
				setPropId+=(Long.toString(recordId))+",";
				counting++;
			}
			
			//After getting the latest string, we can do the comparation
			String setIdIntoConfig=checkUpdate(Id,setPropId);
			
			//the code is to set the config.properties for the update
			Properties propSconfig = new Properties();
			//System.out.println(propS.setProperty("recordId", setPropId));
			FileOutputStream fos1 = new FileOutputStream(userNameSpilting[0]+"config.properties"); 
			if(setIdIntoConfig==null)
			{
				setIdIntoConfig="";
			}
			
			propSconfig.setProperty("private_key", reco[2]);
			propSconfig.setProperty("public_key", reco[3]);
			propSconfig.setProperty("username", reco[0]);
			propSconfig.setProperty("password", reco[1]);
			propSconfig.setProperty("directory", "Data_Download/");
			propSconfig.setProperty("recordId", setIdIntoConfig);
			propSconfig.setProperty("count", "100000");
			propSconfig.store(fos1, "Copyright (c) Boxcode Studio"); 
			fos1.close();//closing outputstream
		
			//The following code is to write the record id in the the DataSet.properites
			Properties propSData = new Properties();
			//System.out.println(propS.setProperty("recordId", setPropId));
			FileOutputStream fos = new FileOutputStream(userNameSpilting[0]+"DataSet.properties"); 

			propSData.setProperty("recordId", setPropId);
			propSData.setProperty("count", Integer.toString(counting));
			propSData.store(fos, "Copyright (c) Boxcode Studio"); 
			fos.close();//closing outputstream
		}
		catch(Exception exception){
			System.out.println(exception.toString());
		}
	}

	// To generate SHA-1 hash 
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

	
	private String checkUpdate(String old,String now)
	{
		//System.out.println("here!");

		String outputOld[]=old.split(",");
		String outputNew[]=now.split(",");
		if(outputOld.length>3)
		{
			System.out.println("before checking the update:"+outputOld[0]+" "+outputOld[1]+" "+outputOld[2]+" ...");
			System.out.println("after checking the update:"+outputNew[0]+" "+outputNew[1]+" "+outputNew[2]+" ...");
			
		}
		else{
			System.out.println("This is the first time! Welcome to download the data.");
		}
		if(now.compareTo(old)==0)  //which means that no updation
		{
			System.out.println("Nothing updated!");
			return null;
		}
		if((old==null)||(now==null))
		{
			System.out.println("no record gained from the server!");
			return null;
		}
		String splitOld[]=old.split(",");
		String splitNew[]=now.split(",");
		int i=0;
		String newMade="";
		int findOutOrNot=0;
		for(int indexout=0;indexout<splitNew.length;indexout++)
		{
			findOutOrNot=0;
			
			for(int indexin=0;indexin<splitOld.length;indexin++)
			{
				if(splitNew[indexout].compareTo(splitOld[indexin])==0)
				{
					findOutOrNot=1;
				}
				
			}
			//System.out.println("the record is "+splitNew[indexout]+" "+findOutOrNot);
			if(findOutOrNot==0)
			{
				newMade+=splitNew[indexout];
				newMade+=",";
			}

		}
		
		
		System.out.println("Needs to be update is: "+newMade);
		return newMade;
	}
	
}
