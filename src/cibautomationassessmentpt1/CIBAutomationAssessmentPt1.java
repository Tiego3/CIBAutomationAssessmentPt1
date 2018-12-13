/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cibautomationassessmentpt1;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author tmathobela
 */
public class CIBAutomationAssessmentPt1  {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here
        CIBAutomationAssessmentPt1 cib = new CIBAutomationAssessmentPt1();
        String urlAllDogsString = "https://dog.ceo/api/breeds/list/all";
        String listOfAllDogs;
        String urlRetreiverSubs = "https://dog.ceo/api/breed/retriever/list";
        String urlrdmImgGolden = "https://dog.ceo/api/breed/retriever/images/random";
        
        listOfAllDogs = cib.getRequest(urlAllDogsString);
        if(listOfAllDogs.contains("retriever")){
            System.out.println("Retriever - is withing the list");
        }
        
        System.out.println(cib.getRequest(urlAllDogsString));
        System.out.println(cib.getRequest(urlAllDogsString));
    }
    
    public String getRequest(String urlString) throws Exception {
        
        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
            
        int status = con.getResponseCode();
        
        if (status!=200){
            System.out.print("Fail");
            System.exit(0);
        }
        
        InputStream inputStream = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String allDogNames;
        StringBuffer listOfAllDogs = new StringBuffer();
        
        while ((allDogNames = reader.readLine()) != null) {
            listOfAllDogs.append(allDogNames);
            System.out.println(allDogNames);           
        } 
        
        return listOfAllDogs.toString();
    }
    
}
