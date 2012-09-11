package de.assessmenttool.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateMySQLTables {


		String table;
	    
		private  Connection conn;
		
		public CreateMySQLTables(Connection con) {
			this.conn = con;
		}	

		
		public void createQuestionsTable(){
			 table= 	"CREATE TABLE IF NOT EXISTS  at_questions  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   text  text NOT NULL," +
			"   weight  float NOT NULL," +
			"   answered  int(1) NOT NULL," +
			"   note  text NOT NULL," +
			"   category  int(10) NOT NULL," +
			"   assessment  int(10) NOT NULL," +
			"   criterion  int(10) NOT NULL," +
			"   answer  int(10) NOT NULL," +
			"	FOREIGN KEY ( category ) references at_categories( id )," +
			"	FOREIGN KEY ( criterion ) references at_criteria( id )," +
			"	FOREIGN KEY ( assessment ) references at_questionnaires( id )," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.executeQuery(table);
				  System.out.println("Tabelle Question wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Question konnte nicht erstellt werden");
			 }
		}
		
		public void createCategoryTable(){
			 table = 	"CREATE TABLE IF NOT EXISTS  at_categories  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   name  text NOT NULL," +
			"   weight  float NOT NULL," +
			"   note  text NOT NULL," +
			"   category  int(10) NOT NULL," +
			"	FOREIGN KEY ( category ) references at_categories( id )," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle Categories wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Categories konnte nicht erstellt werden");
			 }
		}
		
		public void createAssessmentTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_questionnaires  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   name  text NOT NULL," +
			"   author  int(10) NOT NULL," +
			"   depth3d  tinyint(1) NOT NULL," +
			"   creation  TIMESTAMP(8) NOT NULL," +
			"	FOREIGN KEY ( author )references at_users( id )," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{

				  Statement st = conn.createStatement();
				  st.executeQuery(table);
				  System.out.println("Tabelle Questionnaires wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Questionnaires konnte nicht erstellt werden");
			 }
		}
		
		public void createUserTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_users  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   username  text NOT NULL," +
			"   passwort  text NOT NULL," +
			"   role  int(10) NOT NULL," +
			"   creation  TIMESTAMP(8) NOT NULL," +
			"	FOREIGN KEY ( role )references at_role( id )," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle Users wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Users konnte nicht erstellt werden");
			 }
		}
		
		public void createRoleTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_users  (" +
			"    id  int(10) NOT NULL AUTO_INCREMENT, ," +
			"  name text NOT NULL" +
			"PRIMARY KEY ( id ))";
				 
			 try{
				 Statement statement = conn.createStatement();
				 statement.executeUpdate(table);
				  System.out.println("Tabelle Role wurde erstellt");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Role wurde nicht erstellt. Folgender Fehler "+ s.getMessage());
			 }
		}
		
		public void createCriteriaTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_criteria  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   name  text NOT NULL," +
			"   crittype  int(10) NOT NULL," +
			"	FOREIGN KEY ( crittype )references at_critPossible( id )," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle Criteria wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Criteria konnte nicht erstellt werden");
			 }
		}
		
		public void createCritPossibleTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_critPossible  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   type  text NOT NULL," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle critPossible wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle critPossible konnte nicht erstellt werden");
			 }
		}
		
		public void createCritParamsTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_critParams  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   key  text NOT NULL," +
			"   value  text NOT NULL," +
			"   criterion  int(10) NOT NULL," +
			"	FOREIGN KEY ( criterion ) references at_criteria( id )," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle critParams wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle critParams konnte nicht erstellt werden");
			 }
		}
		
		public void createCatDepTable(){			//name noch einmal �berarbeiten
			table = "CREATE TABLE IF NOT EXISTS  at_catDepMap  (" +
			"   category  int(10) NOT NULL ," +
			"   dependency  int(10) NOT NULL," +
			"   isCondition  int(1) NOT NULL," +
			"	FOREIGN KEY  (dependency ) references at_dependencies( id )," +
			"	FOREIGN KEY ( category ) references at_categories( id ))" ;
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle CatDep wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle CatDep konnte nicht erstellt werden");
			 }
		}
		
		public void createQuestionDepTable(){			//name noch einmal �berarbeiten
			table = "CREATE TABLE IF NOT EXISTS  at_catDepMap` (" +
			"   question  int(10) NOT NULL ," +
			"   dependency  int(10) NOT NULL," +
			"   isCondition  int(1) NOT NULL," +
			"	FOREIGN KEY ( dependency ) references at_dependencies( id )," +
			"	FOREIGN KEY ( category ) references at_questions( id ))" ;
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle QuestionDep wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle QuestionDep konnte nicht erstellt werden");
			 }
		}
		
		public void createDependenciesTable(){
			table = "CREATE TABLE IF NOT EXISTS  at_dependencies  (" +
			"   id  int(10) NOT NULL AUTO_INCREMENT," +
			"   name  text NOT NULL," +
			"   completionNeeded  int(10) NOT NULL," +
			" 	PRIMARY KEY ( id ))";
				 
			 try{
				  Statement st = conn.createStatement();
				  st.execute(table);
				  System.out.println("Tabelle Dependencies wurde erstellt!");
			 }
			 catch(SQLException s){
				  System.out.println("Tabelle Dependencies konnte nicht erstellt werden");
			 }
		}

}
