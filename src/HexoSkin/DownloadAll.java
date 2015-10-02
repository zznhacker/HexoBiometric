package HexoSkin;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;
import java.util.Timer;


/*
 * @Manav Singhal
 * DownloadAll.java - Main file to download data for each parameter (heart rate, inspiration etc.) for record ID passed as input 
 * 
 * */

public class DownloadAll {
	private static String[] reco;
	
	public static void main(String[] args) throws ParseException
	{
		
		DownloadAll all=new DownloadAll();
		synchronized (DownloadAll.class) {
			if(reco==null)
			{
//				reco=new String[4];
//				reco[0]="emotionregstudy@gmail.com";
//				reco[1]="emotion1";
//				reco[2]="kl2mi5kNflARdRTbmR80z8PCO0SOdNpW7dqyXmiV";
//				reco[3]="dBzvgbiBpqNmiRQ4";
				if((args!=null)&&(args.length>3))
				{
					reco=new String[4];
					reco[0]=args[0];
					reco[1]=args[1];
					reco[2]=args[2];
					reco[3]=args[3];
					
					
				}
				else{
					System.out.println("Not enough input!");
					return ;
 				}
			}
		}
		
		ReadId readling=new ReadId(reco);
		
		try {
			readling.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// Downloading activity data
		Activity activity = new Activity(reco);
		try {
			activity.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// Downloading breathing_rate data
		BreathingRate object = new BreathingRate(reco);
		try {
			object.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// Downloading breathing_rate_quality data
		BreathingRateQuality brRateQualityObject = new BreathingRateQuality(reco);
		try {
			brRateQualityObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading cadence data
		Cadence cadObject = new Cadence(reco);
		try {
			cadObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading expiration data
		Expiration expObject = new Expiration(reco);
		try {
			expObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading heart_rate data
		HeartRate heartObject = new HeartRate(reco);
		try {
			heartObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading heart_rate_quality data
		HeartRateQuality heartQualityObject = new HeartRateQuality(reco);
		try {
			heartQualityObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading inspiration data
		Inspiration inspirationObject = new Inspiration(reco);
		try {
			inspirationObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading minute_ventilation data
		MinuteVentilation minuteObject = new MinuteVentilation(reco);
		try {
			minuteObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		// Downloading rr_interval data
		RRInterval rrObject = new RRInterval(reco);
		try {
			rrObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		TidalVolume tidalObject = new TidalVolume(reco);
		try {
			tidalObject.getCredentials(reco);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Thread.sleep(400);
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		try {
			all.gatherCsvTogether(reco);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void gatherCsvTogether(String reco[]) throws IOException, ParseException
	{
		InputStream inputStream = null;
		Properties propR = new Properties();
		File file=new File("DataSet.properties");
		if(!file.exists())
		{
			Properties propSData = new Properties();
			//System.out.println(propS.setProperty("recordId", setPropId));
			FileOutputStream fos = new FileOutputStream("DataSet.properties"); 
			propSData.setProperty("recordId", "");
			propSData.setProperty("count","100");
			propSData.store(fos, "Copyright (c) Boxcode Studio"); 
			fos.close();//closing outputstream
		}
		try {
			inputStream = new FileInputStream("DataSet.properties");
			propR.load(inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
		ReadProperties properties = new ReadProperties();                                                                                   
		properties.process(reco);                                                                                                           
	    String BASE_DIRECTORY="";
		if (properties.getDirectory().length() == 0 || properties.getPassword().length() == 0 || properties.getPrivateKey().length() == 0 ||
		properties.getPublicKey().length() == 0 || properties.getRecordID().length() == 0 || properties.getUsername().length() == 0)
		{                                                                                                                                   
			System.out.println("Properties not set in config.properties");                                                                
		}                                                                                                                                   
		else{                                                                                                                               
			BASE_DIRECTORY = properties.getDirectory();                                                                                     
		   
		String Id=properties.getRecordID(); 
		String IdSet[]=Id.split(",");
		String[] folderName=reco[0].split("@");
		File folder = new File(BASE_DIRECTORY + folderName[0] );
		String[] folderList=folder.list();
		//System.out.println(folderList[1]);
		for(int Inde=0;Inde<folderList.length;Inde++)
		{
			if(folderList[Inde].compareTo(".DS_Store")==0)
			{
				continue;
			}
			File folder2 = new File(BASE_DIRECTORY + folderName[0] + "/" + folderList[Inde]);
//			System.out.println(folder2);
			String[] folderList2=folder2.list();
			
			String initialTime;
			//String PARAMETER[]={"breathingrate","heartrate","minuteventilation","cadence","activity"};
			String PARAMETER[]={"activity","breathingrate","breathingratestatus","cadence","expiration","heartrate","heartratestatus","inspiration","minuteventilation","rrinterval","tidalvolume"};
		//	int lengthofPAR=PARAMETER.length;
			String TimeStamps[];
			TimeStamps=new String[10000000];
			//"activity""breathingrate","breathingratestatus","cadence","expiration","heartrate","heartratestatus","inspiration","minuteventilation","rrinterval","tidalvolume"
			//System.out.println("the idset length is "+IdSet.length);
			for (int i = 0; i < folderList2.length; i++) {
				//To make sure the directory exist
				
				File directory = new File(BASE_DIRECTORY + folderName[0] +'/' + folderList[Inde] + "/" + folderList2[i]);
	//			System.out.println(directory);
	//			if (!directory.exists()) {                                
	//				boolean success = directory.mkdir();                  
	//				System.out.println("here!");                          
	//			}
				String fileNameForTheSum=folderList2[i]+".csv";
				if(folderList2[i].compareTo(".DS_Store")==0)
				{
					continue;
				}
				
				FileWriter writerSum = new FileWriter(directory + "/" + fileNameForTheSum); 
				FileReader reader[]=new FileReader[12];
				BufferedReader br[]=new BufferedReader[12];
				
				String[] spliting=folderList2[i].split("_");
				for(int j=0;j<PARAMETER.length;j++)
				{
					//System.out.println("The j is "+j);
					
					String fileName= spliting[1] +"_"+ PARAMETER[j] +".csv" ;  
					//System.out.println("the file name is "+fileName);
					int ifFileExsist=this.checkFileifExsist(directory+"/"+spliting[1] +"_"+ PARAMETER[j] +".csv");
					//System.out.println(IdSet[i]+" "+PARAMETER[j]+" the fileifExsist is"+ifFileExsist);
					if(ifFileExsist==1)
					{
						reader[j] = new FileReader(directory + "/" + fileName);
						br[j] = new BufferedReader(reader[j]);
					}
					
				} 
				String s[]=new String[12];
				
				
				
				
				if((br[0])!=null)				
				{
				int whileLoop=0;
				while (((s[0] = br[0].readLine()) != null)) {
					
					String tempRecord[]=new String[2];
					tempRecord=s[0].split(",");
					TimeStamps[whileLoop]=tempRecord[0];
					//System.out.println("Time stamps is"+ TimeStamps[whileLoop]);
					writerSum.append(tempRecord[0]	+	"");
					writerSum.append(',');
					if(whileLoop==0)
					{
						writerSum.append("Athelete");
						writerSum.append(",");
					}
					else{
						writerSum.append(spliting[0]);
						writerSum.append(",");
					}
					if(tempRecord[1].compareTo("Value")==0)
					{
						writerSum.append(PARAMETER[0]	+	"");
					}
					else writerSum.append(tempRecord[1]	+	"");
					
					writerSum.append(',');
					
					for(int index=1;index<PARAMETER.length;index++)
					{
						if(br[index]!=null)
						{
						//System.out.println("The id is"+IdSet[i]); //This is used to check error
							br[index].mark(100);
							if((s[index]=br[index].readLine())!=null)
							{
								tempRecord=s[index].split(",");
								SimpleDateFormat sfEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
								SimpleDateFormat sfStart = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",java.util.Locale.ENGLISH) ;
							
								if((tempRecord[0].compareTo("TimeStamp")!=0)&&(TimeStamps[0].compareTo("TimeStamp")!=0))
								{
									Date dateFinal=sfStart.parse(tempRecord[0]);
									Date dateInitial=sfStart.parse(TimeStamps[0]);
									if(dateInitial.after(dateFinal))
									{
										System.out.println("data init: "+dateInitial.toString());
										System.out.println("data final: "+dateFinal.toString());
										index--;
										continue;
									}
								}
								if((tempRecord[0].compareTo("TimeStamp")!=0)&&(TimeStamps[0].compareTo("TimeStamp")!=0))
								{
									SimpleDateFormat sfStart1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",java.util.Locale.ENGLISH) ;
									Date dateFinal=sfStart1.parse(tempRecord[0]);
									Date dateInitial=sfStart1.parse(TimeStamps[whileLoop]);
									if(tempRecord[1].compareTo("Value")==0)
									{
										writerSum.append(PARAMETER[index]	+	"");
									}
									else if(tempRecord[0].compareTo(TimeStamps[whileLoop])==0)
									{
										writerSum.append(tempRecord[1]	+	"");
									}
									else if(dateFinal.before(dateInitial))
									{
										while(!dateFinal.equals(dateInitial))
										{
											String[] tempRecord1=br[index].readLine().split(",");
											dateInitial=sfStart1.parse(TimeStamps[whileLoop]);
											dateFinal=sfStart1.parse(tempRecord1[0]);
											System.out.println("data init: "+dateInitial.toString());
											System.out.println("data final: "+dateFinal.toString());
										}
										writerSum.append(tempRecord[1]	+	"");
									}
									else{
											writerSum.append(null);
											br[index].reset();
									}
								}
								writerSum.append(',');
								
							}
							else{
									writerSum.append(null);
									writerSum.append(',');
							}
							
						}
						else{
							if(whileLoop==0)
							{
								writerSum.append(PARAMETER[index]	+	"");
							}
							else
							{
								writerSum.append(null);
							}
							writerSum.append(',');
							
						}
						
					}
					writerSum.append('\n');
					s=new String[12];
					whileLoop++;
				}
			}
				else
				{
					writerSum.append(',');
					writerSum.append(null);
					writerSum.append('\n');
					
				}
				writerSum.flush();
				writerSum.close();
				
			}
		}
		}
	}
	private String[] recordTheScan() throws IOException
	{
//		String userName=scanString("user name");
//		
//		String password=scanString("password");
//		String privateKey=scanString("private key");
//		String publicKey=scanString("public key");
//		Properties propSconfig = new Properties();
//		//System.out.println(propS.setProperty("recordId", setPropId));
//		int i=0;
//		String reco[]=new String[4];
//		reco[0]=userName;
//		reco[1]=password;
//		reco[3]=publicKey;
//		String fileNam[]=reco[0].split("@");
//		FileOutputStream fos1 = new FileOutputStream(fileNam[0]+"config.properties"); 
//		//���Properties��������������������� 
//		propSconfig.setProperty("private_key", privateKey);
//		propSconfig.setProperty("public_key", publicKey);
//		propSconfig.setProperty("username", userName);
//		propSconfig.setProperty("password", password);
//		propSconfig.setProperty("directory", "Data_Download/");
//		propSconfig.setProperty("recordId", "");
//		propSconfig.setProperty("count", "100");
//		propSconfig.store(fos1, "Copyright (c) Boxcode Studio"); 
//		fos1.close();//closing outputstream
		String tempFortest[]=new String[4];
		tempFortest[0]="trullt@missouri.edu";
		tempFortest[1]="timmy1960";
		tempFortest[2]="YLI4brZb6UDFwAwh9UdfN97rNH64waWqWLAbOzsw";
		tempFortest[3]="fNZ3Bblu9MQKLPjr";
		return tempFortest;
		
	}
	
	
	private String scanString(String a)
	{
		System.out.println("please input the "+a);
		Scanner s=new Scanner(System.in);
		String str=s.next();
		return str;
		
		
	}
	private int checkFileifExsist(String fileName)
	{
		File file=new File(fileName);
		if(!file.exists())
		{
			return 0;
		}
		else{
			return 1;
		}
	}


	

}
