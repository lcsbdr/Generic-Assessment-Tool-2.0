package de.assessmenttool.fileIO;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Criterion.CriterionType;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.kernel.LogicIF;

public class XML_Parser {

  Assessment a;

  ArrayList<Criterion> criterionList = new ArrayList<Criterion>();

  HashMap<Question, HashMap<String, String>> depMap = new HashMap<Question, HashMap<String, String>>();

  // public XML_Parser(String file){
  public XML_Parser(String file, LogicIF logic) {
    try {

      File fXmlFile = new File(file);
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(fXmlFile);
      doc.getDocumentElement().normalize();

      System.out.println("Root element :" + doc.getDocumentElement().getAttribute("name"));
      this.a = new Assessment(Integer.parseInt(doc.getDocumentElement().getAttribute("ID")), doc.getDocumentElement()
        .getAttribute("name"));

      NodeList nList = doc.getDocumentElement().getChildNodes();
      //System.out.println("-----------------------");

      for (int temp = 0; temp < nList.getLength(); temp++) {

        Node nNode = nList.item(temp);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

          Element eElement = (Element)nNode;

          if (eElement.getNodeName().equals("criterion")) {

            Criterion c = null;

            if (eElement.getAttribute("type").equals(Criterion.CriterionType.ALPHANUMERIC.toString())) {
              c = new Criterion(Integer.parseInt(eElement.getAttribute("ID")),
                                eElement.getAttribute("name"),
                                CriterionType.ALPHANUMERIC);
              this.criterionList.add(c);
            }
            if (eElement.getAttribute("type").equals(Criterion.CriterionType.NUMERIC.toString())) {
              c = new Criterion(Integer.parseInt(eElement.getAttribute("ID")),
                                eElement.getAttribute("name"),
                                CriterionType.NUMERIC);
              this.criterionList.add(c);
            }
            if (eElement.getAttribute("type").equals(Criterion.CriterionType.YESNO.toString())) {
              c = new Criterion(Integer.parseInt(eElement.getAttribute("ID")),
                                eElement.getAttribute("name"),
                                CriterionType.YESNO);
              this.criterionList.add(c);
            }

            if (c != null) {
              for (String s : c.getParams().values()) {
                c.addParameter(c.getKey(s), s);
              }
              logic.saveCriterion(c);
            }

          }

          if (eElement.getNodeName().equals("question")) {
            System.out.println("question: " + eElement.getAttribute("name"));

            int cID = 0;

            for (int i = 0; i < this.criterionList.size(); i++) {
              if (eElement.getAttribute("criterion").equals(i)) {
                cID = i;
              }
            }

            String name = eElement.getAttribute("name");
            if (name == null) name = "";
            String note = eElement.getAttribute("note");
            if (note == null) note = "";

            Question q = new Question(name, note, this.criterionList.get(cID));
            q.setId(Integer.parseInt(eElement.getAttribute("ID")));
            q.setWeight(Float.parseFloat(eElement.getAttribute("weight")));

            String[] deps = q.getDependencies().toString().split(" ");
            HashMap<String, String> tempDeps = new HashMap<String, String>();

            for (String s : deps) {

              String[] singleDep = s.split(".");
              tempDeps.put(singleDep[0], singleDep[1]);
            }

            this.depMap.put(q, tempDeps);

            this.a.addQuestion(q);
          }
          if (eElement.getNodeName().equals("category")) {
            System.out.println("category: " + eElement.getAttribute("name"));

            Category c = new Category(eElement.getAttribute("name"));
            c.setId(Integer.parseInt(eElement.getAttribute("ID")));
            c.setNote(eElement.getAttribute("note"));

            this.a.addCategory(c);
            getSubElements(nNode, c);
          }

        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    addDependeciesToAssessment();

  }

  /*
   * private static String getTagValue(String sTag, Element eElement) {
   * NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
   * Node nValue = (Node) nlList.item(0);
   * return nValue.getNodeValue();
   * }
   */

  private void getSubElements(Node n, Category c) {

    NodeList nList = n.getChildNodes();

    Element parentNodeElement = (Element)n;

    for (int temp = 0; temp < nList.getLength(); temp++) {

      Node nNode = nList.item(temp);
      if (nNode.getNodeType() == Node.ELEMENT_NODE) {

        Element eElement = (Element)nNode;

        if (eElement.getNodeName().equals("question")) {
          System.out.println("parentNode: " + parentNodeElement.getAttribute("name") + " question:"
                             + eElement.getAttribute("name"));

          int cID = 0;

          for (int i = 0; i < this.criterionList.size(); i++) {
        	  cID = i;
            if (eElement.getAttribute("criterion").equals(i)) {
            }
          }

          Question q = new Question(eElement.getAttribute("name"),
                                    eElement.getAttribute("note"),
                                    this.criterionList.get(cID));
          q.setId(Integer.parseInt(eElement.getAttribute("ID")));
          q.setWeight(Float.parseFloat(eElement.getAttribute("weight")));
          c.addQuestion(q);

        }
        if (eElement.getNodeName().equals("category")) {
          System.out.println("parentNode: " + parentNodeElement.getAttribute("name") + " category: "
                             + eElement.getAttribute("name"));

          Category c1 = new Category(eElement.getAttribute("name"));
          c1.setId(Integer.parseInt(eElement.getAttribute("ID")));
          c1.setNote(eElement.getAttribute("note"));
          c.addCategory(c1);
          getSubElements(nNode, c1);
        }

      }
    }

  }

  private void addDependeciesToAssessment() {

    for (Question q : this.depMap.keySet()) {
      for (String s : this.depMap.get(q).keySet()) {
        for (Question p : this.a.getAllQuestions()) {
          if (String.valueOf(p.getId()).equals(s)) {
            q.addDependency(p, Integer.parseInt(this.depMap.get(q).get(s)));
          }
        }
      }
    }

  }

  public Assessment getAssessment() {
    return this.a;
  }
}
