package de.assessmenttool.fileIO;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Criterion;
import de.assessmenttool.assessments.Question;

public class PDF_Generator {

  private final Document document;

  public PDF_Generator(Assessment a, String file) {

    int chapterCount = 1;
    int catCount = 0;

    /* TODO validate filename? */
    this.document = new Document(PageSize.A4, 50, 50, 50, 50);
    try {
      @SuppressWarnings("unused")
      PdfWriter writer = PdfWriter.getInstance(this.document, new FileOutputStream(file + ".pdf"));
    } catch (FileNotFoundException fnfe) {
      // TODO Auto-generated catch block
      fnfe.printStackTrace();
    } catch (DocumentException de) {
      // TODO Auto-generated catch block
      de.printStackTrace();
    }
    this.document.open();

    Paragraph docTitle = new Paragraph(a.getName() + "\n\n\n", FontFactory.getFont(FontFactory.HELVETICA,
                                                                                   18,
                                                                                   Font.BOLDITALIC,
                                                                                   new BaseColor(0, 0, 0)));
    Chapter chapter1 = new Chapter(docTitle, 1);
    chapter1.setNumberDepth(0);

    Paragraph overViewTitle = new Paragraph("Category Overview\n\n", FontFactory.getFont(FontFactory.HELVETICA,
                                                                                         16,
                                                                                         Font.BOLD,
                                                                                         new BaseColor(0, 0, 0)));

    Section overViewSection = chapter1.addSection(overViewTitle);
    overViewSection.setNumberDepth(0);
    Paragraph chapterTitle;
    for (Category cat : a.getCategories()) {
      catCount++;
      chapterTitle = new Paragraph(catCount + ". " + cat.getName() + " ID: <" + cat.getId() + ">",
                                   FontFactory.getFont(FontFactory.HELVETICA, 14, Font.NORMAL, new BaseColor(0, 0, 0)));
      overViewSection.add(chapterTitle);
      addSubCategories(overViewSection, cat, 0, true, catCount, catCount + ".");
    }

    Paragraph overViewQuestionsTitle = new Paragraph("\nTop Category Questions\n",
                                                     FontFactory.getFont(FontFactory.HELVETICA,
                                                                         16,
                                                                         Font.BOLD,
                                                                         new BaseColor(0, 0, 0)));

    Section overViewQuestionsSection = chapter1.addSection(overViewQuestionsTitle);

    overViewQuestionsSection.setNumberDepth(0);

    for (Question q : a.getQuestions())
      addQuestion(overViewQuestionsSection, q);

    addChapter(chapter1);

    catCount = 0;
    for (Category cat : a.getCategories()) {
      catCount++;
      // System.out.println("cat: "+cat.getName()+" questions: "+cat.getQuestions().size());

      chapterTitle = new Paragraph(catCount + ". " + cat.getName(), FontFactory.getFont(FontFactory.HELVETICA,
                                                                                        16,
                                                                                        Font.BOLD,
                                                                                        new BaseColor(0, 0, 0)));

      overViewSection.add(chapterTitle);

      chapterCount++;
      Chapter tempChapter = new Chapter(chapterTitle, chapterCount);
      tempChapter.setNumberDepth(0);
      Section s2 = tempChapter.addSection("");
      s2.setNumberDepth(0);
      for (Question q : cat.getQuestions())
        addQuestion(s2, q);
      addSubCategories(s2, cat, 0, false, catCount, catCount + ".");
      addChapter(tempChapter);

    }

    this.document.close();
  }

  private void addSubCategories(Section s, Category c, int sublevel, boolean isTop, int catCount, String catString) {
    Paragraph categoryTitle;
    String initSpace = "    ";

    int subCatCount = 0;
    String catTitleString = catString + catCount + ".";

    for (int i = 0; i <= sublevel; i++)
      initSpace += "    ";

    // String chapterNumbers="";

    for (int n = 1; n <= sublevel; n++) {
      // chapterNumbers+=""
    }

    for (Category cat : c.getCategories()) {
      subCatCount++;
      catTitleString = catString + subCatCount + ".";
      if (!isTop) {
        categoryTitle = new Paragraph("\n" + catTitleString + " " + cat.getName() + " ID: <" + cat.getId() + ">" + "\n",
                                      FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new BaseColor(0, 0, 0)));
      } else {
        categoryTitle = new Paragraph(initSpace + catTitleString + " " + cat.getName() + " ID: <" + cat.getId() + ">"
                                      + "\n", FontFactory.getFont(FontFactory.HELVETICA,
                                                                  14,
                                                                  Font.NORMAL,
                                                                  new BaseColor(0, 0, 0)));
      }

      s.add(categoryTitle);
      if (!isTop) for (Question q : cat.getQuestions())
        addQuestion(s, q);
      if (!isTop)
        addSubCategories(s, cat, sublevel + 1, false, subCatCount, catTitleString);
      else {
        addSubCategories(s, cat, sublevel + 1, true, subCatCount, catTitleString);
      }
    }
  }

  private void addChapter(Chapter c) {
    try {
      this.document.add(c);
    } catch (DocumentException de) {
      // TODO Auto-generated catch block
      de.printStackTrace();
    }
  }

  private void addQuestion(Section s, Question q) {

    Paragraph questionTitle;

    questionTitle = new Paragraph("\n" + q.getName() + " ID: <" + q.getId() + ">",
                                  FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new BaseColor(0, 0, 0)));

    s.add(questionTitle);

    // System.out.println(q.getCriterion().getType());

     String dependencyString = "";
    
     if (q.getDependencies().size() != 0) {
     ArrayList<Question> questDeps = q.getDependencies();
     for (Question tmp : questDeps) {
     dependencyString += tmp.getId() + " ";
     }

    
     questionTitle = new Paragraph("\n NOTE: Answer following Questions before:\nQuestions:"+dependencyString,
     FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC, new BaseColor(0, 0, 0)));
    
     s.add(questionTitle);
    
     }

    if (q.getCriterion().getType() == Criterion.CriterionType.YESNO) {

      questionTitle = new Paragraph(q.getNote(), FontFactory.getFont(FontFactory.HELVETICA,
                                                                     13,
                                                                     Font.ITALIC,
                                                                     new BaseColor(0, 0, 0)));

      s.add(questionTitle);

      questionTitle = new Paragraph("Mark \"yes\" or \"no\"with a cross\n\n",
                                    FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLDITALIC, new BaseColor(0,
                                                                                                                  0,
                                                                                                                  0)));

      s.add(questionTitle);

      PdfPTable table = new PdfPTable(2);

      PdfPCell c1 = new PdfPCell(new Phrase("Yes"));
      c1.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(c1);

      c1 = new PdfPCell(new Phrase("No"));
      c1.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(c1);

      table.setHeaderRows(1);

      if (q.getAnswer() != null) {
        table.addCell(q.getAnswer() + " ");
      } else {
        table.addCell(" ");
      }
      if (q.getAnswer() != null) {
        table.addCell(q.getAnswer() + " ");
      } else {
        table.addCell(" ");
      }

      s.add(table);
    } else {

      int answers = q.getCriterion().getParams().size();

      // System.out.println("question: "+q.getName()+" criterion: "+q.getCriterion()+" getparams: "+q.getCriterion().getParams());

      questionTitle = new Paragraph(q.getNote(), FontFactory.getFont(FontFactory.HELVETICA,
                                                                     13,
                                                                     Font.ITALIC,
                                                                     new BaseColor(0, 0, 0)));

      s.add(questionTitle);

      questionTitle = new Paragraph("Mark only one row with a cross.\n\n", FontFactory.getFont(FontFactory.HELVETICA,
                                                                                               12,
                                                                                               Font.BOLDITALIC,
                                                                                               new BaseColor(0, 0, 0)));

      s.add(questionTitle);

      PdfPTable table = new PdfPTable(2);

      PdfPCell c1 = new PdfPCell(new Phrase("Criterion"));
      c1.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(c1);

      c1 = new PdfPCell(new Phrase("Answer"));
      c1.setHorizontalAlignment(Element.ALIGN_CENTER);
      table.addCell(c1);

      table.setHeaderRows(1);

      int answered = 0;

      if (q.getAnswer() != null) {
        answered = q.getAnswerInt();
      }

      // System.out.println(answers);

      for (int n = 1; n <= answers; n++) {

        table.addCell(q.getCriterion().getParams().get(Integer.toString(n)) + " ");
        if ((n == answered) && (answered != 0))
          table.addCell(q.getAnswer() + " ");
        else {
          table.addCell(" ");
        }
      }
      s.add(table);
    }

  }

}