/*
 * @Manav Singhal
 * ReadProperties.java - To read input from config.properties file passed as input file to this program.
 * 
 * */
package HexoSkin;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {

	private static String RECORD_ID,RECORD_ID_SAVING,PRIVATE_KEY, PUBLIC_KEY, USERNAME, PASSWORD, directoryPath,COUNT;
	
	public void process(String reco[]) throws IOException {

		Properties prop = new Properties();
		InputStream inputStream = null;
		String userNameSpilting[]=reco[0].split("@");
		File file=new File(userNameSpilting[0]+"config.properties");
		if(file.exists())
		{
			try {
				inputStream = new FileInputStream(userNameSpilting[0]+"config.properties");
				
				prop.load(inputStream);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
	
			PRIVATE_KEY =reco[2];
			PUBLIC_KEY =reco[3];
			USERNAME = reco[0];
			PASSWORD = reco[1];
			RECORD_ID = prop.getProperty("recordId");
			directoryPath = prop.getProperty("directory");
			COUNT=prop.getProperty("count");
			RECORD_ID_SAVING=prop.getProperty("recordIdSaving");
		}
		else
		{
	
			FileOutputStream fos1 = new FileOutputStream(file); 
			Properties propSconfig = new Properties();
			propSconfig.setProperty("private_key", reco[2]);
			propSconfig.setProperty("public_key",reco[3] );
			propSconfig.setProperty("username", reco[0]);
			propSconfig.setProperty("password", reco[1]);
			propSconfig.setProperty("directory", "Data_Download/");
			propSconfig.setProperty("recordId", "");
			propSconfig.setProperty("count", "100");
			propSconfig.store(fos1, "Copyright (c) Boxcode Studio"); 
			fos1.close();//closing outputstream
			inputStream = new FileInputStream(userNameSpilting[0]+"config.properties");	
			prop.load(inputStream);
			PRIVATE_KEY =reco[2];
			PUBLIC_KEY =reco[3];
			USERNAME = reco[0];
			PASSWORD = reco[1];
			RECORD_ID = prop.getProperty("recordId");
			directoryPath = prop.getProperty("directory");
			COUNT=prop.getProperty("count");
			RECORD_ID_SAVING=prop.getProperty("recordIdSaving");
			//���Properties��������������������� 
			
		}
	}
	
	public String getPrivateKey(){
		return PRIVATE_KEY;
	}
	
	public String getPublicKey(){
		return PUBLIC_KEY;
	}

	public String getUsername(){
		return USERNAME;
	}
	
	public String getPassword(){
		return PASSWORD;
	}
	
	public String getRecordID(){
		return RECORD_ID;
	}
	
	public String getDirectory(){
		return directoryPath;
	}
	public String getCount(){
		return COUNT;
	}
	public String getRecordIdSaving(){
		return RECORD_ID_SAVING;
	}

}
