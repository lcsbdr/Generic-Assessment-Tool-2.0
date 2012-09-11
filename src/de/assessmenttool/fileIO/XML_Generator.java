/**
 * XML_Generator
 * Creates custom xml-files
 * author: Cedric Lumma
 * created: 20.03.2012
 * last change: 20.03.2012: file created
 * 
 * An Instance of this class represents an xml-file to be generated.
 * Add Attributes to the attribute_map and create an Element. The content
 * of the attribute_map will be new attributes. Clear the attribute_map
 * before you add attributes to the map for a new element. The rootElement
 * exists by default.
 * 
 * Example:
 * XML_Generator my_xml_generator = new XML_Generator("example","testfile.xml");
 * my_xml_generator.add_Attribute("id", "1");
 * my_xml_generator.add_Attribute("date", "20.12.2012");
 * my_xml_generator.add_Element("my_first_element", "first_one", "rootElement");
 * my_xml_generator.clear_Attributes();
 * [...]
 * my_xml_generator.create_file();
 * 
 * This class can:
 * CONSTRUCT AN INSTANCE: void XML_Generator(String root_name, String file)
 * ATTACH NEW ELEMENT TO NODE: void add_Element(String type, String name, String node)
 * ADD ATTRIBUTE TO MAP: void add_Attribute(String name, String value)
 * RESET ATTRIBUTE MAP: void clear_Attributes()
 * RETURN ATTRIBUTE_MAP: HashMap<String,String> getAttributeMap()
 * GENERATE FILE: boolean create_File();
 */
package de.assessmenttool.fileIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.kernel.LogicIF;

public class XML_Generator {

  Document doc;

  DocumentBuilder docBuilder;

  Element rootElement;

  String file_name;

  HashMap<String, Element> elements = new HashMap<String, Element>();

  HashMap<String, String> attribute_map = new HashMap<String, String>();
  
  ArrayList<Criterion> criterionList = new ArrayList<Criterion>();

  public XML_Generator(Assessment assessment, String file, LogicIF logic) {
	  
	getCriterions(logic);
	  
	ArrayList<Category> categories=assessment.getCategories();
		
    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    this.file_name = file;

    try {
      this.docBuilder = docFactory.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      // TODO Auto-generated catch block
      pce.printStackTrace();
    }  
	
    this.doc = this.docBuilder.newDocument();
    this.rootElement = this.doc.createElement("Assessment");
    this.doc.appendChild(this.rootElement);
    
    this.elements.put(assessment.getName(), rootElement);    
    this.attribute_map.put("name", assessment.getName().toString());
    this.attribute_map.put("ID", String.valueOf(assessment.getId()));
    
    for (Object it : this.attribute_map.keySet()) {
      
      Attr attr = this.doc.createAttribute((String)it);
      attr.setValue(this.attribute_map.get(it));
      this.elements.get(assessment.getName().toString()).setAttributeNode(attr);
    }
    
    clear_Attributes();
    
    for(Criterion c: criterionList){
    	
    	add_Criterion("criterion",c.getType().toString() ,c.getName(),assessment.getName().toString(),c);
    }
    
	for(Question q: assessment.getQuestions()){
		System.out.println(assessment.getQuestions().size());
		add_Question("question", q.getName(),assessment.getName().toString(),q);
		
	    /*Element e = this.doc.createElement("question");
	    this.elements.put(q.getName(), e);
	    rootElement.appendChild(e);*/
	    
	    this.attribute_map.put("name", q.getName());
	    this.attribute_map.put("criterion", q.getCriterion().toString());
	   
	    this.attribute_map.put("weight", String.valueOf(q.getWeight()));
	    /*TODO CONTINUE HERE */
	}
	
	for(Category cat: categories){
		System.out.println("cat found: "+cat.getName());
		add_Category(cat.getName(), assessment.getName().toString(), cat);
		addSubCategories(cat,cat.getName());
	}
	
	create_file();
	addDocType(file);
  }

  public XML_Generator(String root_name, String file) {

    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
    this.file_name = file;

    try {
      this.docBuilder = docFactory.newDocumentBuilder();
    } catch (ParserConfigurationException pce) {
      // TODO Auto-generated catch block
      pce.printStackTrace();
    }

    // root elements
    this.doc = this.docBuilder.newDocument();
    this.rootElement = this.doc.createElement(root_name);
    this.doc.appendChild(this.rootElement);
    this.elements.put("rootElement", this.rootElement);
  }
  
  
  public void addSubCategories(Category c, String node){
	  
	  for(Question q: c.getQuestions()){
			add_Question("question", q.getName(),c.getName(),q);
		}
	  
	  ArrayList<Category> categories=c.getCategories();
		for(Category cat: categories){
			add_Category(cat.getName(), node, cat);
			for(Question q: cat.getQuestions()){
				add_Question("question", q.getName(),cat.getName(),q);
			}
			addSubCategories(cat,cat.getName());
		}
  }

  public void add_Attribute(String name, String value) {
    this.attribute_map.put(name, value);
  }

  public void clear_Attributes() {
    this.attribute_map = new HashMap<String, String>();
  }

  public HashMap<String, String> getAttributeMap() {
    return this.attribute_map;
  }

  public void add_Category(String name, String node, Category cat) {

    Element e = this.doc.createElement("category");
    
    this.elements.put(name, e);
    this.elements.get(node).appendChild(e);    
    this.attribute_map.put("name", name);
    this.attribute_map.put("ID", String.valueOf(cat.getId()));
    this.attribute_map.put("weight", String.valueOf(cat.getWeight()));
    this.attribute_map.put("note", cat.getNote());
    /** TODO implement when method cat.getQuestDeps() is available for categories
    String questionDependencyString="";
    if(cat.getDependencies().size()!=0){	   
    	   ArrayList<QuestDependency> questDeps = cat.getQuestDeps();
    	   for(QuestDependency tmp : questDeps) {
    		   questionDependencyString+=tmp.getDepQuest().getId()+" ";
    	   }
    }
    
    String categoryDependencyString="";
    if(cat.getDependencies().size()!=0){	   
    	   ArrayList<CatDependency> catDeps = cat.getCatDeps();
    	   for(CatDependency tmp : catDeps) {
    		   categoryDependencyString+=tmp.getDepCat().getId()+" ";
    	   }
    }
    
    */
    
    for (Object it : this.attribute_map.keySet()) {
      
      Attr attr = this.doc.createAttribute((String)it);
      attr.setValue(this.attribute_map.get(it));
      this.elements.get(name).setAttributeNode(attr);
    }
    
    clear_Attributes();
  }

  public void add_Question(String type, String name, String node, Question q){
	  System.out.println("test");
	    Element e = this.doc.createElement(type);
	    this.elements.put(name, e);
	    this.elements.get(node).appendChild(e);
	    
	    String questionDependencyString="";
	    if(q.getDependencies().size()!=0){	   
	    	   ArrayList<Question> questDeps = q.getDependencies();
	    	   System.out.println("qdepsSize: "+questDeps.size());
	    	   for(Question tmp : questDeps) {
	    		   questionDependencyString+=tmp.getId()+"."+tmp.getAnswerInt();
	    	   }
	    }

	    
	    this.attribute_map.put("name", name);
	    this.attribute_map.put("criterion", getCriterionID(q.getCriterion()));
	    this.attribute_map.put("ID", String.valueOf(q.getId()));
	    this.attribute_map.put("questionDependenciesByID", questionDependencyString);
//	    this.attribute_map.put("categoryDependenciesByID", categoryDependencyString);
	    this.attribute_map.put("note", q.getNote());
	    this.attribute_map.put("weight", String.valueOf(q.getWeight()));

	    for (Object it : this.attribute_map.keySet()) {
	      
	      Attr attr = this.doc.createAttribute((String)it);
	      attr.setValue(this.attribute_map.get(it));
	      this.elements.get(name).setAttributeNode(attr);
	    }
	    
	    clear_Attributes();  
  }
  
  public void add_Criterion(String type,String cType, String name, String node, Criterion c){
	  	
	  
	  	System.out.println("*");
	  
	    Element e = this.doc.createElement(type);
	    this.elements.put(name, e);
	    this.elements.get(node).appendChild(e);
	    
	    this.attribute_map.put("type", cType);
	    this.attribute_map.put("name", name);
	    this.attribute_map.put("params", c.getParams().values().toString());
	    this.attribute_map.put("ID", String.valueOf(c.getId()));

	    for (Object it : this.attribute_map.keySet()) {
	      
	      Attr attr = this.doc.createAttribute((String)it);
	      attr.setValue(this.attribute_map.get(it));
	      this.elements.get(name).setAttributeNode(attr);
	    }
	    
	    clear_Attributes();  
}
  
  public boolean create_file() {

    // write the content into xml file
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer;
    try {
      transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(this.doc);
      StreamResult result = new StreamResult(new File(this.file_name+".xml"));
      transformer.transform(source, result);
      return true;
      
    } catch (TransformerConfigurationException tce) {
      // TODO Auto-generated catch block
      tce.printStackTrace();
    } catch (TransformerException te) {
      // TODO Auto-generated catch block
      te.printStackTrace();
    }
    return false;

  }
  
  
  /* Method Source: http://www.roseindia.net/xml/dom/AddDocType.shtml*/
  private void addDocType(String f){
	  try{
			  BufferedReader bf = new BufferedReader(
			  new InputStreamReader(System.in));
			  String xmlFile = f;
			  File file = new File(xmlFile);
			  if (file.exists()){
				  DocumentBuilderFactory factory = 
				  DocumentBuilderFactory.newInstance();
				  DocumentBuilder builder = factory.newDocumentBuilder();
				  Document document = builder.parse(xmlFile);
				  Transformer tFormer = 
				  TransformerFactory.newInstance().newTransformer();
				  Source source = new DOMSource(document);
				  Result result = new StreamResult(new File(this.file_name+".xml"));
				  tFormer.transform(source, result);
			  }
			  else{
				  System.out.println("File not found!");
			  }
		  }
		  catch (Exception e){
			  e.getMessage();
			  /* TODO logging?*/
		  }
	}
  
  private void getCriterions(LogicIF l){
	  
	  criterionList = l.getPossibleCriteria();
	  System.out.println("crit:" + criterionList);
	  
  }
  
  private String getCriterionID(Criterion c){
	  
	  //System.out.println("get Criterion: "+c.getName()+" ID= "+criterionList.);
	  
	  for(int i=0; i<criterionList.size();i++){
		  
		  //System.out.println("comparing: ---"+criterionList.get(i)+"--- and ---"+c.getName()+"---");
		  
		  if(criterionList.get(i).toString().equals(c.getName().toString())){
			  //System.out.println("match");
			  return ""+i;
		  }
	  }
	  
	  
	  return "-1";
	  
  }
  
}

