/**
 * XLS_Generator
 * Creates custom xls-files
 * author: Cedric Lumma
 * created: 21.03.2012
 * last change: 30.03.2012: finished inital creation
 * 
 * An instance of this class creates an xls-file containig all categories,
 * subcategories, questions+answers of an assessment. The assessment is given 
 * with the construction of an instance of this class.
 * 
 * Example:
 * XLS_Generator(myAssessment,myFileName); //this is all you have to do.
 * 
 * This class can:
 * CONSTRUCT AN INSTANCE...everything else is private
 */

package de.assessmenttool.fileIO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.WorkbookUtil;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;

public class XLS_Generator {

  private Workbook wb;
  private Sheet sheetOverview;
  private Row row = null;
  private Cell cell = null;
  
  private Font header_f;
  private CellStyle header_cs;
  private CellStyle question_cs;
  private CellStyle empty_cs;
  private CellStyle debug_header_cs;
  private CellStyle topCategory_cs;
  
  private int columnCounter=0;
  

  public XLS_Generator(Assessment assessment,String output) {
	  	  	  
	  createSheet("Overview");
	  addTopCategories(assessment);
	  create_file(output);

  }

  private void createSheet(String name) {

    this.wb = new HSSFWorkbook();
    String safeName = WorkbookUtil.createSafeSheetName(name);
    sheetOverview = this.wb.createSheet(safeName);
    
	header_f = wb.createFont(); 
	header_f.setFontHeightInPoints((short) 12);
	header_f.setBoldweight(Font.BOLDWEIGHT_BOLD);
	  
	header_cs = wb.createCellStyle(); 
	header_cs.setFont(header_f);
	header_cs.setWrapText(true);
	header_cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
	header_cs.setBorderBottom(CellStyle.BORDER_MEDIUM);
	header_cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	header_cs.setBorderLeft(CellStyle.BORDER_MEDIUM);
	header_cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	header_cs.setBorderRight(CellStyle.BORDER_MEDIUM);
	header_cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
	header_cs.setBorderTop(CellStyle.BORDER_MEDIUM);
	header_cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
	  
	question_cs = wb.createCellStyle();
	question_cs.setVerticalAlignment(CellStyle.VERTICAL_TOP);
	question_cs.setWrapText(true);
	question_cs.setBorderBottom(CellStyle.BORDER_MEDIUM);
	question_cs.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	question_cs.setBorderLeft(CellStyle.BORDER_MEDIUM);
	question_cs.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	question_cs.setBorderRight(CellStyle.BORDER_MEDIUM);
	question_cs.setRightBorderColor(IndexedColors.BLACK.getIndex());
	question_cs.setBorderTop(CellStyle.BORDER_MEDIUM);
	question_cs.setTopBorderColor(IndexedColors.BLACK.getIndex());
	
	empty_cs = wb.createCellStyle();
	empty_cs.setBorderBottom(CellStyle.BORDER_NONE);
	empty_cs.setBorderLeft(CellStyle.BORDER_NONE);
	empty_cs.setBorderRight(CellStyle.BORDER_NONE);
	empty_cs.setBorderTop(CellStyle.BORDER_NONE);
	empty_cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	empty_cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
	
	debug_header_cs = wb.createCellStyle();
	debug_header_cs.setBorderLeft(CellStyle.BORDER_MEDIUM);
	debug_header_cs.setBorderLeft(CellStyle.BORDER_MEDIUM);
	debug_header_cs.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	debug_header_cs.setFillPattern(CellStyle.SOLID_FOREGROUND);
	
	topCategory_cs = wb.createCellStyle();
	topCategory_cs.setBorderLeft(CellStyle.BORDER_THIN);
	topCategory_cs.setBorderRight(CellStyle.BORDER_THIN);
	topCategory_cs.setBorderBottom(CellStyle.BORDER_THIN);
	
  }

  private void addTopCategories(Assessment a) {

	/* TODO optimize layout*/
	ArrayList<Category> categories = a.getCategories();
	int rowCount=2;
	CreationHelper createHelper = wb.getCreationHelper();
	Hyperlink link;
	String linkString;
	ArrayList<Question> questions = a.getQuestions();	
	
	row = sheetOverview.createRow(0);
	for(int y=0;y<4;y++){
		cell = row.createCell(y);
		cell.setCellStyle(empty_cs);	
	}
	row = sheetOverview.createRow(1);
	cell = row.createCell(0);
	cell.setCellStyle(empty_cs);
	cell = row.createCell(1);
	cell.setCellStyle(header_cs);
	row.setHeight((short)600); 
	cell.setCellValue("Top Categories");
	sheetOverview.setColumnWidth(1, 8000);
	sheetOverview.setColumnWidth(2, 8000);
	cell = row.createCell(2);
	cell.setCellStyle(empty_cs);
	cell = row.createCell(3);
	cell.setCellStyle(empty_cs);
	
    for(Category cat: categories){
    	String safeName = WorkbookUtil.createSafeSheetName(cat.getName());
    	Sheet subSheet = wb.createSheet(safeName);
    	columnCounter=0;
    	addSubCategories(cat.getCategories(), subSheet, cat.getQuestions().size(),0);
    	row = sheetOverview.createRow(rowCount);
    	row.setRowNum(rowCount);    			
		cell = row.createCell(0);
		cell.setCellStyle(empty_cs);
		cell = row.createCell(1);
		cell.setCellStyle(topCategory_cs);
		link = createHelper.createHyperlink(Hyperlink.LINK_DOCUMENT);
		linkString="'"+cat.getName()+"'!A1";
		link.setAddress(linkString);		
		cell.setCellValue(""+cat.getName());	
		cell.setHyperlink(link);
		cell = row.createCell(2);
		cell.setCellStyle(empty_cs);
		cell = row.createCell(3);
		cell.setCellStyle(empty_cs);
		rowCount++;
    }
    
    row = sheetOverview.createRow(rowCount);
	for(int y=0;y<4;y++){
		cell = row.createCell(y);
		cell.setCellStyle(empty_cs);	
	}
    rowCount++;
    row = sheetOverview.createRow(rowCount);
    row.setHeight((short)600); 
    cell = row.createCell(0);
    cell.setCellStyle(empty_cs);
    cell = row.createCell(1);
    cell.setCellStyle(header_cs);
    cell.setCellValue("Top Category Questions");
    cell = row.createCell(2);
    cell.setCellStyle(empty_cs);
    cell = row.createCell(3);
    cell.setCellStyle(empty_cs);
    rowCount++;
    for(Question q: questions){
    	row = sheetOverview.createRow(rowCount);
    	row.setRowNum(rowCount);    			
		cell = row.createCell(0);
		cell.setCellStyle(empty_cs);
		cell = row.createCell(1);
		cell.setCellStyle(question_cs);
		cell.setCellValue(""+q.getName());
		cell = row.createCell(2);
		cell.setCellStyle(question_cs);
		cell.setCellValue(""+q.getAnswer());
	    cell = row.createCell(3);
	    cell.setCellStyle(empty_cs);
		rowCount++;	
    }
    
    row = sheetOverview.createRow(rowCount);
	for(int y=0;y<4;y++){
		cell = row.createCell(y);
		cell.setCellStyle(empty_cs);	
	}
	
  }

  private void addSubCategories(ArrayList<Category> categories, Sheet s, int initialRow, int sublevel){
	  
	  int rowCount = initialRow;

	  if(categories.size()>0){
		  columnCounter=3+(sublevel*2);
		  for(Category cat: categories){
			  	row = s.createRow(rowCount);
				for(int n=0; n<columnCounter;n++){
					cell = row.createCell(n);
					cell.setCellStyle(empty_cs);
				}
				if(columnCounter==3){
					cell = row.createCell(3);
					cell.setCellStyle(empty_cs);
				}
			  	rowCount++;
		    	row = s.createRow(rowCount);
		    	row.setRowNum(rowCount);
				for(int n=0; n<=1+sublevel;n++){
					cell = row.createCell(n);
					cell.setCellStyle(empty_cs);
				}
				s.setColumnWidth(1+sublevel, 8000);
				row.setHeight((short)600);
				s.addMergedRegion(new CellRangeAddress(rowCount, rowCount, 1+sublevel, 2+sublevel));
				cell.setCellStyle(header_cs);
				cell.setCellValue(""+cat.getName());				
				cell = row.createCell(3+sublevel);
				cell.setCellStyle(debug_header_cs);
				rowCount++;
				ArrayList<Question> questions = cat.getQuestions();
				for(Question q: questions){				  
					row = s.createRow(rowCount);
			    	row.setRowNum(rowCount);
			    	row.setHeight((short) 2000);
			    	for(int n=0; n<=1+sublevel;n++){
			    		cell = row.createCell(n);
			    		cell.setCellStyle(empty_cs);
			    	}
			    	cell.setCellStyle(question_cs);
					cell.setCellValue(""+q.getName());	
					cell = row.createCell(2+sublevel);
					cell.setCellStyle(question_cs);
					cell.setCellValue(""+q.getAnswer());
		    		cell = row.createCell(3+sublevel);
		    		cell.setCellStyle(empty_cs);
					rowCount++;
				}
				
				if(cat.getCategories().size()>0){
					addSubCategories(cat.getCategories(),s, cat.getQuestions().size()+1,sublevel+1);			
				}else{
		   			row = s.createRow(rowCount);
		  	    	for(int i=0;i<=3+sublevel;i++){
		  	    		cell = row.createCell(i);
		  	    		cell.setCellStyle(empty_cs);
		  	    	}
		  		}
		 }
	 }
  }
  

  private void create_file(String name) {
	/* TODO validate filename?*/
    FileOutputStream fileOut = null;
    try {
      fileOut = new FileOutputStream(name+".xls");
      this.wb.write(fileOut);
      fileOut.close();
    } catch (FileNotFoundException fnf) {
      fnf.printStackTrace();
    } catch (IOException ioe) {
      // TODO Auto-generated catch block
      ioe.printStackTrace();
    }

  }

}
