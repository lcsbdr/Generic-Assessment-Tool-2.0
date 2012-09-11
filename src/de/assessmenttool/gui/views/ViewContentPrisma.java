package de.assessmenttool.gui.views;

import java.awt.ScrollPane;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATPanel;
import de.assessmenttool.kernel.LogicIF;
import de.assessmenttool.prisma.Prisma;

public class ViewContentPrisma extends ATPanel {

  private static final long serialVersionUID = 1L;

  private ScrollPane displayPane = null;

  private Prisma prism = null;

  private JSlider slideSize = null;

  private int oldSize;

  private final LogicIF logic;

  private Assessment usedAssessment = null;

  public ViewContentPrisma(LogicIF logic, ViewIF view) {
    this.logic = logic;
  }

  public void printPrisma() {
    if (this.usedAssessment != null) {
      removeAll();
      this.prism = null;

      this.prism = new Prisma(this.usedAssessment);
      this.prism.addValuesToAngleList();

      printBox("Prism Diagram");

      addPrismaCanvasToBox();

      setBoxVisible();
      scrollRightIfNeeded();

      updateUI();
      validate();
    }
  }

  public void printBox(String title) {

    setLayout(new MigLayout("", "[20][grow, fill]", "[grow, fill]"));

    this.displayPane = new ScrollPane();
    this.displayPane.setWheelScrollingEnabled(false);

    addMouseWheelListener(new MouseWheelListener(){
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        int change = e.getWheelRotation();
        if (change < 0 && ViewContentPrisma.this.oldSize < 69) {
          ViewContentPrisma.this.oldSize = ViewContentPrisma.this.oldSize - change;
          ViewContentPrisma.this.prism.bigger(-change);
        }
        if (change > 0 && ViewContentPrisma.this.oldSize > 2) {
          ViewContentPrisma.this.oldSize = ViewContentPrisma.this.oldSize - change;
          ViewContentPrisma.this.prism.smaller(change);
        }
        ViewContentPrisma.this.slideSize.setValue(ViewContentPrisma.this.oldSize);
        setSize(ViewContentPrisma.this.prism.getPrismaWidth(), ViewContentPrisma.this.prism.getPrismaHeight());
        updateUI();
        ViewContentPrisma.this.displayPane.doLayout();
      }
    });

    addComponentListener(new ComponentListener(){
      @Override
      public void componentResized(ComponentEvent e) {
        ViewContentPrisma.this.prism.setUpAngleX();
        ViewContentPrisma.this.prism.repaint();
        updateUI();
        ViewContentPrisma.this.displayPane.doLayout();
      }

      @Override
      public void componentMoved(ComponentEvent e) {}

      @Override
      public void componentShown(ComponentEvent e) {}

      @Override
      public void componentHidden(ComponentEvent e) {}
    });

    this.slideSize = new JSlider(SwingConstants.VERTICAL);
    this.slideSize.setMaximum(69);
    this.slideSize.setMinimum(2);
    this.slideSize.addChangeListener(new ChangeListener(){
      @Override
      public void stateChanged(ChangeEvent e) {
        int compareValue = (ViewContentPrisma.this.slideSize.getValue());
        if (ViewContentPrisma.this.oldSize != compareValue) {
          int transformValue = compareValue - ViewContentPrisma.this.oldSize;
          ViewContentPrisma.this.oldSize = compareValue;
          if (transformValue > 0)
            ViewContentPrisma.this.prism.bigger(transformValue);
          else
            ViewContentPrisma.this.prism.smaller(-transformValue);
        }
        setSize(ViewContentPrisma.this.prism.getPrismaWidth(), ViewContentPrisma.this.prism.getPrismaHeight());
        updateUI();
        ViewContentPrisma.this.displayPane.doLayout();
      }
    });
  }

  public void addPrismaCanvasToBox() {

    this.oldSize = this.prism.getSideMultiplicator();
    this.slideSize.setValue(this.oldSize);
    add(this.slideSize, "cell 0 0");
    this.displayPane.setSize(this.prism.getPrismaWidth(), this.prism.getPrismaHeight());
    this.displayPane.add(this.prism);
  }

  public void setBoxVisible() {

    add(this.displayPane, "cell 1 0");
    setVisible(true);
  }

  public void scrollRightIfNeeded() {

    int displaySize = this.displayPane.getWidth();

    if (displaySize < this.prism.getPrismaWidth()) {
      this.displayPane.setScrollPosition(this.prism.getPrismaWidth() - displaySize + 20, 0);
    }
  }

  public ScrollPane getDisplayPane() {
    return this.displayPane;
  }

  public void setDisplayPane(ScrollPane displayPane) {
    this.displayPane = displayPane;
  }

  public JSlider getSlideSize() {
    return this.slideSize;
  }

  public void setSlideSize(JSlider slideSize) {
    this.slideSize = slideSize;
  }

  public void elementChanged(Assessment assessment) {
    if (assessment != null) {
      this.usedAssessment = assessment;
      printPrisma();
    }
  }

  public void elementChanged(Category category) {
    this.usedAssessment = this.logic.getRelatedAssessment(category);
    printPrisma();
  }

  public void elementChanged(Question question) {
    this.usedAssessment = this.logic.getRelatedAssessment(question);
    printPrisma();
  }
}
