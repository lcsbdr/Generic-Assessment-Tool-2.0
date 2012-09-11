package de.assessmenttool.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

import net.infonode.docking.DockingWindow;
import net.infonode.docking.DockingWindowAdapter;
import net.infonode.docking.RootWindow;
import net.infonode.docking.SplitWindow;
import net.infonode.docking.TabWindow;
import net.infonode.docking.theme.DockingWindowsTheme;
import net.infonode.docking.theme.ShapedGradientDockingTheme;
import net.infonode.docking.util.DockingUtil;
import net.infonode.docking.util.ViewMap;
import net.infonode.util.Direction;
import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;
import de.assessmenttool.components.ATMenuItem;
import de.assessmenttool.constants.GeneralConstants;
import de.assessmenttool.gui.status.StatusPanel;
import de.assessmenttool.gui.views.ViewAssessmentTree;
import de.assessmenttool.gui.views.ViewAssessments;
import de.assessmenttool.gui.views.ViewContentAnswer;
import de.assessmenttool.gui.views.ViewContentBar;
import de.assessmenttool.gui.views.ViewContentEdit;
import de.assessmenttool.gui.views.ViewContentKiviat;
import de.assessmenttool.gui.views.ViewContentPrisma;
import de.assessmenttool.gui.views.ViewCriteria;
import de.assessmenttool.gui.views.ViewDashboard;
import de.assessmenttool.gui.views.ViewDatabase;
import de.assessmenttool.gui.views.ViewDependency;
import de.assessmenttool.gui.views.ViewIF;
import de.assessmenttool.gui.views.ViewOverview;
import de.assessmenttool.gui.views.ViewTopCategories;
import de.assessmenttool.gui.views.ViewUserLogin;
import de.assessmenttool.kernel.LogicIF;

public class View extends JFrame implements ViewIF {

  /**
	 * 
	 */
  private final int ANZAHL_VIEWS = 14;

  private static final long serialVersionUID = -1513166659492458309L;

  private ViewDatabase viewDatabase;

  private ViewUserLogin viewUserLogin;

  private ViewAssessments viewAssessments;

  private ViewOverview viewOverview;

  private ViewTopCategories viewTopCategories;

  private ViewContentEdit viewContentEdit;

  private ViewContentAnswer viewContentAnswer;

  private ViewContentBar viewContentBar;

  private ViewContentKiviat viewContentKiviat;

  private ViewContentPrisma viewContentPrisma;

  private ViewDashboard viewDashboard;

  private ViewAssessmentTree viewAssessmentTree;

  private final LogicIF logic;

  private DockingWindow aTLayout;

  net.infonode.docking.View[] views = new net.infonode.docking.View[this.ANZAHL_VIEWS];

  private ViewCriteria viewCriteria;

  private ViewDependency viewDependency;// TODO change

  private Object activeElement;

  public View(LogicIF logic) {

    this.logic = logic;

    this.setTitle("AssessmentTool " + GeneralConstants.REVISION);
    this.setSize(1024, 768);
    this.setExtendedState(MAXIMIZED_BOTH);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    createMenuBar();
    createStatusPanel();

    initComponents();

    this.setVisible(true);
  }

  private void createMenuBar() {

    JMenuBar menuBar = new JMenuBar();
    this.setJMenuBar(menuBar);

    // START
    JMenu mnStart = new JMenu("File");

    JMenuItem mntmLogin = new ATMenuItem("Login");
    mntmLogin.addActionListener(null); // TODO implement Action Listener
    mnStart.add(mntmLogin);

    JMenuItem mntmLoad = new ATMenuItem("Import Assessment (XML)");
    mnStart.add(mntmLoad);
    mntmLoad.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.OPEN_DIALOG);

        int state = fc.showOpenDialog(null);

        if (state == JFileChooser.APPROVE_OPTION) {
          String path = fc.getSelectedFile().getPath();
          View.this.logic.saveAssessment(View.this.logic.importXML(path));
          forceAssessmentRefresh();
        }
      }
    });
    JSeparator separator1 = new JSeparator();
    mnStart.add(separator1);

    JMenuItem mntmXML = new ATMenuItem("Export Assessment (XML)");
    mntmXML.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);

        int state = fc.showSaveDialog(null);

        if (state == JFileChooser.APPROVE_OPTION) {
          View.this.logic.generateXML(View.this.logic.getRelatedAssessment(View.this.activeElement), fc
            .getSelectedFile().getPath());
        }
      }
    });
    mnStart.add(mntmXML);

    JMenuItem mntmXLS = new ATMenuItem("Export Assessment (XLS)");
    mntmXLS.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);

        int state = fc.showSaveDialog(null);

        if (state == JFileChooser.APPROVE_OPTION) {
          View.this.logic.generateXLS(View.this.logic.getRelatedAssessment(View.this.activeElement), fc
            .getSelectedFile().getPath());
        }
      }
    });
    mnStart.add(mntmXLS);

    JMenuItem mntmPDF = new ATMenuItem("Export Assessment (PDF)");
    mntmPDF.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        JFileChooser fc = new JFileChooser();
        fc.setDialogType(JFileChooser.SAVE_DIALOG);

        int state = fc.showSaveDialog(null);

        if (state == JFileChooser.APPROVE_OPTION) {
          View.this.logic.generatePDF(View.this.logic.getRelatedAssessment(View.this.activeElement), fc
            .getSelectedFile().getPath());
        }
      }
    });
    mnStart.add(mntmPDF);

    JSeparator separator2 = new JSeparator();
    mnStart.add(separator2);

    JMenuItem mntmBeenden = new ATMenuItem("Quit");
    mntmBeenden.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        View.this.logic.quit();
      }
    });
    mnStart.add(mntmBeenden);

    // VIEWS
    JMenu mnViews = new JMenu("Views");

    // JMenuItem itmAssessments = new ATMenuItem("Assessments");
    // mnViews.add(itmAssessments);

    JMenuItem itmEdit = new ATMenuItem("Edit");
    itmEdit.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[1].restore();
      }
    });
    mnViews.add(itmEdit);

    // JMenuItem itmDashboard = new ATMenuItem("Dashboard");
    // mnViews.add(itmDashboard);

    JMenuItem itmDatabase = new ATMenuItem("Database");
    itmDatabase.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (View.this.views[3].isMinimized()) {
          View.this.views[3].restore();
        } else {
          View.this.views[3].restore();
          View.this.views[3].minimize(Direction.UP);
        }
      }
    });
    mnViews.add(itmDatabase);

    JMenuItem itmOverview = new ATMenuItem("Overview");
    itmOverview.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[4].restore();
      }
    });
    mnViews.add(itmOverview);

    // JMenuItem itmUserLogin = new ATMenuItem("User Login");
    // itmUserLogin.addActionListener(new ActionListener() {
    // @Override
    // public void actionPerformed(ActionEvent e) {
    // views[5].restore();
    // }});
    // mnViews.add(itmUserLogin);

    JMenuItem itmHistory = new ATMenuItem("History");
    itmHistory.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[6].restore();
      }
    });
    mnViews.add(itmHistory);

    JMenuItem itmAssessmentTree = new ATMenuItem("Assessment-Tree");
    itmAssessmentTree.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent arg0) {
        View.this.views[7].restore();
      }
    });
    mnViews.add(itmAssessmentTree);

    JMenuItem itmAnswer = new ATMenuItem("Answer");
    itmAnswer.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[8].restore();
      }
    });
    mnViews.add(itmAnswer);

    JMenuItem itmBar = new ATMenuItem("Bar");
    itmBar.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[9].restore();
      }
    });
    mnViews.add(itmBar);

    JMenuItem itmKiviat = new ATMenuItem("Kiviat");
    itmKiviat.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[10].restore();
      }
    });
    mnViews.add(itmKiviat);

    JMenuItem itmPrisma = new ATMenuItem("Prisma");
    itmPrisma.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        View.this.views[11].restore();
      }
    });
    mnViews.add(itmPrisma);

    JMenuItem itmCriteria = new ATMenuItem("Criteria");
    itmCriteria.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        if (View.this.views[12].isMinimized()) {
          View.this.views[12].restore();
        } else {
          View.this.views[12].restore();
          View.this.views[12].minimize(Direction.UP);
        }
      }
    });
    mnViews.add(itmCriteria);

    JMenuItem itmDependency = new ATMenuItem("Dependency");
    itmDependency.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!View.this.logic.dbConnected()) return;
        if (View.this.views[13].isMinimized()) {
          View.this.views[13].restore();
        } else {
          View.this.views[13].restore();
          View.this.views[13].minimize(Direction.UP);
        }
      }
    });
    mnViews.add(itmDependency);

    // HELP
    JMenu mnHelp = new JMenu("Help");

    JMenuItem mntmHelp = new ATMenuItem("Help (F1)");
    mntmHelp.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        File myFile = new File("manual.pdf");
        try {
          Desktop.getDesktop().open(myFile);
        } catch (IOException e1) {
          e1.printStackTrace();
        }
      }
    });
    mnHelp.add(mntmHelp);

    JMenuItem mntmAbout = new ATMenuItem("About");
    mnHelp.add(mntmAbout);
    mntmAbout.addActionListener(new ActionListener(){
      @Override
      public void actionPerformed(ActionEvent e) {
        JFrame frame = null;
        JOptionPane
          .showMessageDialog(frame,
                             "Das Generic Assessment Tool wurde als Projekt im Fach 'Software Engineering' an der Dualen \n"
                                     + "Hochschule Stuttgart unter Leitung der Dozenten Markus Rentschler und\n"
                                     + "Andreas Stuckert von unten genannten Studenten erstellt:\n\n"
                                     + "Generic Assessment Tool Version 1.0 (Kurs TIT10AID)\n" + "Cedric Lumma,"
                                     + "Philipp Drehwitz, " + "Daniel Vietz, " + "Jens Martin, " + "Christian Janz",
                             "About",
                             JOptionPane.PLAIN_MESSAGE);
      }
    });
    // ADD
    menuBar.add(mnStart);
    menuBar.add(mnViews);
    menuBar.add(mnHelp);
  }

  private void createStatusPanel() {

    StatusPanel statusPanel = StatusPanel.getInstance();
    getContentPane().add(statusPanel, BorderLayout.SOUTH);
  }

  private void createATLayout() {

    // TODO
    // Variablen erzeugen, um auch hier Eigenschaften wie minimizable setzen zu können.
    // CloseWithAbort (siehe auch net.infonode.docking.DockingWindow.class)

    this.aTLayout = new SplitWindow(true,
                                    0.2f,
                                    new SplitWindow(false, 0.4f, this.views[3], this.views[7]),
                                    new SplitWindow(false,
                                                    0.7f,
                                                    new TabWindow(new DockingWindow[] {this.views[1],
                                                                                       this.views[8],
                                                                                       this.views[9],
                                                                                       this.views[10],
                                                                                       this.views[11]}),
                                                    new TabWindow(new DockingWindow[] {this.views[4],
                                                                                       this.views[6],
                                                                                       this.views[13],
                                                                                       this.views[12]})));
  }

  private void initComponents() {
    ViewMap viewMap = new ViewMap();
    for (int i = 0; i < this.ANZAHL_VIEWS; i++) {
      if (i == 0) {
        this.views[i] = new net.infonode.docking.View("Assessments", null, getViewAssessments());
      } else if (i == 1) {
        this.views[i] = new net.infonode.docking.View("Edit", null, getViewContentEdit());
      } else if (i == 2) {
        this.views[i] = new net.infonode.docking.View("Dashboard", null, getViewDashboard());
      } else if (i == 3) {
        this.views[i] = new net.infonode.docking.View("Database", null, getViewDatabase());
      } else if (i == 4) {
        this.views[i] = new net.infonode.docking.View("Overview", null, getViewOverview());
      } else if (i == 5) {
        this.views[i] = new net.infonode.docking.View("User Login", null, getViewUserLogin());
      } else if (i == 6) {
        this.views[i] = new net.infonode.docking.View("History", null, new JLabel("History"));
      } else if (i == 7) {
        this.views[i] = new net.infonode.docking.View("Assessment tree", null, getViewAssessmentTree());
      } else if (i == 8) {
        this.views[i] = new net.infonode.docking.View("Answer", null, getViewContentAnswer());
      } else if (i == 9) {
        this.views[i] = new net.infonode.docking.View("Bar", null, getViewContentBar());
        this.views[i].addListener(new DockingWindowAdapter(){
          @Override
          public void windowShown(DockingWindow window) {
            repaintBarChart();
          }
        });
      } else if (i == 10) {
        this.views[i] = new net.infonode.docking.View("Kiviat", null, getViewContentKiviat());
        this.views[i].addListener(new DockingWindowAdapter(){
          @Override
          public void windowShown(DockingWindow window) {
            repaintKiviatChart();
          }
        });
      } else if (i == 11) {
        this.views[i] = new net.infonode.docking.View("Prism", null, getViewContentPrisma());
      } else if (i == 12) {
        this.views[i] = new net.infonode.docking.View("Criteria", null, getViewCriteria());
      } else if (i == 13) {
        this.views[i] = new net.infonode.docking.View("Dependency", null, getViewDependency());
      }
      viewMap.addView(i, this.views[i]);
    }

    RootWindow rootWindow = DockingUtil.createRootWindow(viewMap, true);
    getContentPane().add(rootWindow);

    rootWindow.getRootWindowProperties().getFloatingWindowProperties().setUseFrame(true);

    createATLayout();
    rootWindow.setWindow(this.aTLayout);

    rootWindow.getWindowBar(Direction.UP).setEnabled(true);

    this.views[1].makeVisible();
    this.views[1].getWindowProperties().setMinimizeEnabled(false);
    this.views[3].minimize(Direction.UP);
    this.views[3].getWindowProperties().setCloseEnabled(false);
    this.views[4].getWindowProperties().setMinimizeEnabled(false);
    this.views[5].getWindowProperties().setMinimizeEnabled(false);
    this.views[7].getWindowProperties().setMinimizeEnabled(false);
    this.views[7].getWindowProperties().setMaximizeEnabled(false);
    this.views[7].getWindowProperties().setCloseEnabled(false);
    this.views[8].getWindowProperties().setMinimizeEnabled(false);
    this.views[9].getWindowProperties().setMinimizeEnabled(false);
    this.views[10].getWindowProperties().setMinimizeEnabled(false);
    this.views[11].getWindowProperties().setMinimizeEnabled(false);
    this.views[12].minimize(Direction.UP);
    this.views[12].getWindowProperties().setCloseEnabled(false);
    this.views[13].minimize(Direction.UP);
    this.views[13].getWindowProperties().setCloseEnabled(false);

    DockingWindowsTheme theme = new ShapedGradientDockingTheme();
    // LookAndFeelDockingTheme theme = new LookAndFeelDockingTheme();
    // SoftBlueIceDockingTheme theme = new SoftBlueIceDockingTheme();

    // Apply theme
    rootWindow.getRootWindowProperties().addSuperObject(theme.getRootWindowProperties());
    rootWindow.getRootWindowProperties().setDoubleClickRestoresWindow(true);
    // Remove theme
    // rootWindow.getRootWindowProperties().removeSuperObject(
    // theme.getRootWindowProperties());

  }

  private Component getViewDependency() {
    if (this.viewDependency == null) {
      this.viewDependency = new ViewDependency(this.logic, this);
      this.viewDependency.setVisible(true);
    }
    return this.viewDependency;
  }

  private Component getViewContentAnswer() {
    if (this.viewContentAnswer == null) {
      this.viewContentAnswer = new ViewContentAnswer(this.logic, this);
      this.viewContentAnswer.setVisible(true);
    }
    return this.viewContentAnswer;
  }

  public ViewDashboard getViewDashboard() {
    if (this.viewDashboard == null) {
      this.viewDashboard = new ViewDashboard();
      this.viewDashboard.setVisible(true);
    }
    return this.viewDashboard;
  }

  public ViewContentEdit getViewContentEdit() {
    if (this.viewContentEdit == null) {
      this.viewContentEdit = new ViewContentEdit(this.logic, this);
      this.viewContentEdit.setVisible(true);
    }
    return this.viewContentEdit;
  }

  public ViewContentBar getViewContentBar() {
    if (this.viewContentBar == null) {
      this.viewContentBar = new ViewContentBar(this.logic, this);
      this.viewContentBar.setVisible(true);
    }
    return this.viewContentBar;
  }

  public ViewContentKiviat getViewContentKiviat() {
    if (this.viewContentKiviat == null) {
      this.viewContentKiviat = new ViewContentKiviat(this.logic, this);
      this.viewContentKiviat.setVisible(true);
    }
    return this.viewContentKiviat;
  }

  public ViewContentPrisma getViewContentPrisma() {
    if (this.viewContentPrisma == null) {
      this.viewContentPrisma = new ViewContentPrisma(this.logic, this);
      this.viewContentPrisma.setVisible(true);
    }
    return this.viewContentPrisma;
  }

  public ViewOverview getViewOverview() {
    if (this.viewOverview == null) {
      this.viewOverview = new ViewOverview();
      this.viewOverview.setVisible(true);
    }
    return this.viewOverview;
  }

  public ViewAssessments getViewAssessments() {
    if (this.viewAssessments == null) {
      this.viewAssessments = new ViewAssessments();
      this.viewAssessments.setVisible(true);
    }
    return this.viewAssessments;
  }

  public ViewUserLogin getViewUserLogin() {
    if (this.viewUserLogin == null) {
      this.viewUserLogin = new ViewUserLogin();
      this.viewUserLogin.setVisible(true);
    }
    return this.viewUserLogin;
  }

  public ViewDatabase getViewDatabase() {
    if (this.viewDatabase == null) {
      this.viewDatabase = new ViewDatabase(this.logic, this);
      this.viewDatabase.setVisible(true);
    }
    return this.viewDatabase;
  }

  public ViewCriteria getViewCriteria() {
    if (this.viewCriteria == null) {
      this.viewCriteria = new ViewCriteria(this.logic, this);
      this.viewCriteria.setVisible(true);
    }
    return this.viewCriteria;
  }

  public ViewAssessmentTree getViewAssessmentTree() {
    if (this.viewAssessmentTree == null) {
      this.viewAssessmentTree = new ViewAssessmentTree(this.logic, this);
      this.viewAssessmentTree.setVisible(true);
    }
    return this.viewAssessmentTree;
  }

  @Override
  public void showViewDependency() {
    this.views[13].makeVisible();
  }

  @Override
  public void hideViewDependency() {
    this.views[13].restore();
    this.views[13].minimize(Direction.UP);
  }

  @Override
  public void setActiveElement(Object object) {
    this.activeElement = object;
    if (object instanceof Assessment) {
      Assessment assessment = (Assessment)object;
      this.viewContentAnswer.elementChanged(assessment);
      this.viewOverview.elementChanged(assessment);
      this.viewContentEdit.elementChanged(assessment);
      this.viewAssessmentTree.elementChanged(assessment);
      this.viewContentBar.elementChanged(assessment);
      this.viewContentKiviat.elementChanged(assessment);
      this.viewContentPrisma.elementChanged(assessment);
      this.viewDependency.elementChanged(assessment);
    }
    if (object instanceof Category) {
      Category category = (Category)object;
      this.viewContentAnswer.elementChanged(category);
      this.viewOverview.elementChanged(category);
      this.viewContentEdit.elementChanged(category);
      this.viewAssessmentTree.elementChanged(category);
      this.viewContentBar.elementChanged(category);
      this.viewContentKiviat.elementChanged(category);
      this.viewContentPrisma.elementChanged(category);
      this.viewDependency.elementChanged(category);
    }
    if (object instanceof Question) {

      Question question = (Question)object;
      System.out.println("question" + question.getName());
      this.viewContentAnswer.elementChanged(question);
      this.viewOverview.elementChanged(question);
      this.viewContentEdit.elementChanged(question);
      this.viewAssessmentTree.elementChanged(question);
      this.viewContentBar.elementChanged(question);
      this.viewContentKiviat.elementChanged(question);
      this.viewContentPrisma.elementChanged(question);
      this.viewDependency.elementChanged(question);
    }
  }

  @Override
  public void forceAssessmentRefresh() {
    getViewAssessmentTree().actualize();
    getViewContentEdit().addCategoryLinks();
  }

  public net.infonode.docking.View[] getViews() {
    return this.views;
  }

  @Override
  public void repaintBarChart() {
    this.viewContentBar.repaintChart();
  }

  @Override
  public void repaintKiviatChart() {
    this.viewContentKiviat.repaintChart();
  }

  /**
   * Closes all views.
   */
  public void closeAllViews() {
    for (net.infonode.docking.View tmp : this.views) {
      if (tmp.getTitle().equals("Assessments") || tmp.getTitle().equals("Assessment tree")
          || tmp.getTitle().equals("Database")) continue;
      tmp.close();
    }

    // if (i == 0) {
    // this.views[i] = new net.infonode.docking.View("Assessments", null, getViewAssessments());
    // } else if (i == 1) {
    // this.views[i] = new net.infonode.docking.View("Edit", null, getViewContentEdit());
    // } else if (i == 2) {
    // this.views[i] = new net.infonode.docking.View("Dashboard", null, getViewDashboard());
    // } else if (i == 3) {
    // this.views[i] = new net.infonode.docking.View("Database", null, getViewDatabase());
    // } else if (i == 4) {
    // this.views[i] = new net.infonode.docking.View("Overview", null, getViewOverview());
    // } else if (i == 5) {
    // this.views[i] = new net.infonode.docking.View("User Login", null, getViewUserLogin());
    // } else if (i == 6) {
    // this.views[i] = new net.infonode.docking.View("History", null, new JLabel("History"));
    // } else if (i == 7) {
    // this.views[i] = new net.infonode.docking.View("Assessment tree", null, getViewAssessmentTree());
    // } else if (i == 8) {
    // this.views[i] = new net.infonode.docking.View("Answer", null, getViewContentAnswer());
    // } else if (i == 9) {
    // this.views[i] = new net.infonode.docking.View("Bar", null, getViewContentBar());
    // this.views[i].addListener(new DockingWindowAdapter(){
    // @Override
    // public void windowShown(DockingWindow window) {
    // repaintBarChart();
    // }
    // });
    // } else if (i == 10) {
    // this.views[i] = new net.infonode.docking.View("Kiviat", null, getViewContentKiviat());
    // } else if (i == 11) {
    // this.views[i] = new net.infonode.docking.View("Prisma", null, getViewContentPrisma());
    // } else if (i == 12) {
    // this.views[i] = new net.infonode.docking.View("Criteria", null, getViewCriteria());
    // } else if (i == 13) {
    // this.views[i] = new net.infonode.docking.View("Dependency", null, getViewDependency());
    // }
  }
}
