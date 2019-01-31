/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cibautomationassessmentpt1;


import java.net.*;
import java.util.*;
import org.json.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

/**
 *
 * @author tmathobela
 */
public class CIBAutomationAssessmentPt1  {

    /**
     * @param args the command line arguments
     */
    private URL url;
    private HttpURLConnection con;
    private InputStream inputStream;
    private BufferedReader reader;
    private StringBuffer listOfDogs;
    private Workbook workbook;
    private Sheet urlsheet;
    private Row row;
    private Cell cell;
    String workingDir = System.getProperty("user.dir");
    
//    ExtentReports report; 
//    ExtentTest logStatus;
//    ExtentHtmlReporter  extentHtmlReporter;
// 
    public static void main(String[] args) throws Exception {
        
        // TODO code application logic here
              
        //Initialize contructor object
        CIBAutomationAssessmentPt1 cib = new CIBAutomationAssessmentPt1();
        
        //Declare and initialise variable for Extent  Report
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(System.getProperty("user.dir")+"\\CIBAutomationAssessmentPt1.html");
        ExtentReports extReport = new ExtentReports();
        extReport.attachReporter(htmlReporter);
        ExtentTest test = extReport.createTest("CIBAutomationAssessmentPt1");
               
        /*Initialize html string variables   */
        //Calling the getURL method that returns the api url according to the name of column in the file
        String urlAllDogsString = cib.getUrl("AllDogBreedsListURL");
        String listOfAllDogs;
        String urlRetreiverSubs = cib.getUrl("AllRetrieverBreedsListURL");
        String urlrdmImgGolden = cib.getUrl("RandomRetrieverImgURL");
        
        //print list of all dog breeds
        System.out.println(cib.getRequestList(urlAllDogsString));
        test.log(Status.INFO, cib.getRequestList(urlAllDogsString));
        
        //Check if retriever breed is within list 
        listOfAllDogs = cib.getRequestList(urlAllDogsString);
        if(listOfAllDogs.contains("retriever")){
            System.out.println("Retriever breed - is within the list");
            test.log(Status.PASS, "Retriever breed - is within the list");
        }
        else{
            System.out.println("Retriever breed - is not within the list");
            test.log(Status.FAIL,"Retriever breed - is not within the list");
        }
        //        test.pass("example", MediaEntityBuilder.createScreenCaptureFromPath(System.getProperty("user.dir") + "\\screenshot.png","example").build());
      
        //print list of all retriver sub breeds
        System.out.println(cib.getRequestList(urlRetreiverSubs));
        test.log(Status.INFO, cib.getRequestList(urlRetreiverSubs));
        
        //print random image / link for the sub-breed “golden
        System.out.println(cib.getRequestList(urlrdmImgGolden)); 
        test.log(Status.INFO, cib.getRequestList(urlrdmImgGolden));

        extReport.flush();
        
}
    
    //Get urls from excel sheet 
    //Returns the api url according to the name of column in the file
    public String getUrl(String urlString) throws Exception{
       
        /*Declare and initialise objects*/
        //Define file path - the file is located within the project - 
        //Get the current working to locate the file should project be moved
        String excelFilePath = workingDir+"\\APIURL.xlsx";
        
        //open connection to enable using the file
        inputStream = new FileInputStream(new File(excelFilePath));
        workbook = new XSSFWorkbook(inputStream);
        urlsheet = workbook.getSheet("APIURL");
        row = urlsheet.getRow(0);
        cell = null;
        int colNum = 0;
        
        /*Get column number - to be used to get value according to the heading(column name)*/
        //Iterating through the headings until the last cell number in the first row(The headings/ column name)
        //When the value in the column matches the value passed the variable colNum(Column Number) is assigned the column number        
        for(int i=0;i<row.getLastCellNum();i++){
            if(row.getCell(i).getStringCellValue().trim().equals(urlString)){
                colNum = i;
            }
        }
        
        //Get first row - The data is in first row
        row = urlsheet.getRow(1);
        //Get cell according to column number got from loop
        cell = row.getCell(colNum);
        
        //Get value according to cell number
        String urlVal = String.valueOf(cell.getStringCellValue());
//        System.out.println("Value from the excel sheet:"+ urlVal);
       
        //Return value
       return urlVal; 
    }
    
    //Get api request
    public int getRequest(String urlString) throws Exception {
        
        //Initialize url link variable to open connection and get Request
        url = new URL(urlString);
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        
        //Getting response code to check if url connected 
        int status = con.getResponseCode();
        
        //Check if url connected, if response if not 200 the test will report fail and exit test
        if (status!=200){
            System.out.print("Fail");
            System.exit(0);
        }
        
        //returns the status
        return status;
    }
    
    //Get list of items from a getRequest and retunr them
    public String getRequestList(String urlString) throws Exception{
        
        //Call getRequest function to check connection of url first
        this.getRequest(urlString);
        
        //Initialize a reader to read the list from request
        inputStream = con.getInputStream();
        reader = new BufferedReader(new InputStreamReader(inputStream));
        
        //Declare dogNames variables to be used to get all the dog names from the request
        String dogNames;
        
       //Declare listOfDogs names to Stringbuffer tobe able to manipulate(append to one string) the list of dog names 
       listOfDogs = new StringBuffer();
        
       //Reads the lines of dog breeds per line and stores them in the listOfDogs variable
        while ((dogNames = reader.readLine()) != null) {
            listOfDogs.append(dogNames);
                   
        } 
        
       //Returns list of Dogs variable
        return listOfDogs.toString();
    }
    
}