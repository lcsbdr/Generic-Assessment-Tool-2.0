package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Questaxes {

	 ArrayList<HashMap<String, Object>> axe = new ArrayList<HashMap<String, Object>>();
	 private static Connection conn ;
	 
	 Questaxes(Connection conn){
		 this.conn = conn;
	 }
	 
	 public ArrayList<HashMap<String, Object>> getAssessmentAxes(int assessmentId){
		 // Keys which the map explicitly contains are:  ["id", "name", "position", "questionnaire"]
		    try{
		    		  Statement stmt = conn.createStatement();
		    		  String query = "SELECT * FROM  at_questaxes where id = "+assessmentId;
		    		  ResultSet rs =stmt.executeQuery(query);
		    		  
		    		  while(rs.next()){
		    			
	                    HashMap<String, Object> hashmap = new HashMap<String, Object>();
	                    hashmap.put("id", rs.getInt("id"));
	                    hashmap.put("name", rs.getString("name"));
	                    hashmap.put("position", rs.getInt("position"));
	                    hashmap.put("questionnaire", rs.getInt("questionnaire"));
	                    axe.add(hashmap);
		    		  }
		    		  rs.close();
		    }
		    catch (SQLException s){
		    	System.out.println("es konnten keine Fragen gefunden werden");
	     	}
		   return axe;
	 }
	 
	 public void saveAssessmentAxes(ArrayList<HashMap<String, Object>> assessmentAxes){
		 
		 for(HashMap<String, Object> tmp : assessmentAxes) {
				int id= Integer.parseInt((String) tmp.get("id"));
				
				try{
					  Statement stmt = conn.createStatement();
					  String query = "SELECT * FROM  at_questaxes WHERE id = "+id;
					  ResultSet rs =stmt.executeQuery(query);
					  if(rs.getInt("id")== id){
						  updateAxes(tmp);
					  }else{
						  insertAxes(tmp);
					  }
					  }catch(SQLException s){
					    	System.out.println("es konnten keine Fragen gefunden werden");
				     }
	 }
}

	private void updateAxes(HashMap<String, Object> tmp) {
		try{
	  		  Statement stmt = conn.createStatement();
	  		  String mysql =  "UPDATE at_questaxes SET `name` = "+tmp.get("name")+",`position` = "+Integer.getInteger((String)tmp.get("position"))+", `questionnaire` ="+Integer.getInteger((String)tmp.get("questionnaire"))+" WHERE id ="+Integer.getInteger((String)tmp.get("id"));
	  		  stmt.executeUpdate(mysql);
	  		  System.out.println("at_questaxes wurde mit der Frage geupdatet");
	  		  }
	  		  catch (SQLException s){
	  		  System.out.println("SQL befehl nicht ausgeführt!");
	  		  }
	}

	private void insertAxes(HashMap<String, Object> tmp) {
		try{
	  		  Statement stmt = conn.createStatement();
	  		  String mysql = "INSERT INTO at_questaxes (`name`, `position`, `questionnaire`) VALUES ("+tmp.get("name")+", "+Integer.getInteger((String)tmp.get("position"))+", "+Integer.getInteger((String)tmp.get("questionnaire"))+")";
	  		  stmt.executeUpdate(mysql);
	  		  System.out.println("at_questaxes wurde mit der Frage befüllt");
	  		  }
	  		  catch (SQLException s){
	  		  System.out.println("SQL befehl nicht ausgeführt!");
	  		  }
		
	}
}
