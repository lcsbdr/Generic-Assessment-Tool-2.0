package de.assessmenttool.prisma;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.assessmenttool.assessments.Assessment;
import de.assessmenttool.assessments.Category;
import de.assessmenttool.assessments.Question;

// import de.prisma.view.Main;

public class Prisma extends Canvas implements MouseListener, MouseMotionListener {
  /**
   * Prisma uses Canvas for the output. variables angle.. are used for
   * coordinates of the first angle at the right corner of the top
   */
  private int sideMultiplicator = 10;

  private int angle1_x;

  private int angle1_y = 175 + 2 * this.sideMultiplicator;

  private int angle2_x;

  private int angle2_y = 175 + 2 * this.sideMultiplicator;

  private int angle3_x;

  private int angle3_y = 25 + 2 * this.sideMultiplicator;

  // additional resize information:
  private int size1_x = 315 + 2 * this.sideMultiplicator;

  private int size2_x = 35 - 2 * this.sideMultiplicator;

  private final int size3_x = 175;

  private int spacer = 35;

  private int fontSize = 13;

  private double color1;

  private double color2;

  private double color3;

  private static final long serialVersionUID = 5007331261090359041L;

  private List<Catalog3Angle> angleList = new ArrayList<Catalog3Angle>();

  private boolean selectionMade = false;

  private int selectedAngle;

  private int newSelectedAngle;

  private int tillAngleDefined = 0;

  private JColorChooser colorChooser;

  private Image buffImage;

  private JPopupMenu contextMenu;

  private Color bgColor;

  private Graphics graphics;

  private Graphics2D g2d;

  private JFrame saveFrame = null;

  private JMenuItem color = null;

  private JMenuItem savePrisma = null;

  private JMenuItem randomColors = null;

  private JPopupMenu colorBox = null;

  private final Assessment usedAssessment;

  public Prisma(Assessment usedAssessment) {
    this.usedAssessment = usedAssessment;

    Locale.setDefault(Locale.US);
    JComponent.setDefaultLocale(Locale.US);

    addValuesToAngleList();
    this.selectedAngle = this.angleList.size();

    addMouseListener(this);
    addMouseMotionListener(this);

    setPreferredSize(new Dimension(getPrismaWidth(), getPrismaHeight()));
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    this.graphics = g;

    this.graphics.drawImage(getGraphicsScreen(), 0, 0, this);

    getUsedAssessment();

    if (this.selectionMade == false) {
      printWholePrisma(this.graphics);
    }
    if (this.selectionMade == true) {
      printBackPrisma(this.graphics);
      printFrontPrisma(this.graphics);
    }
  }

  public void printWholePrisma(Graphics g) {

    printPrismaPart(g,
                    1,
                    this.angleList.size(),
                    this.angle1_x - 2 * this.sideMultiplicator,
                    this.angle1_y,
                    this.angle2_x + 2 * this.sideMultiplicator,
                    this.angle2_y,
                    this.angle3_x,
                    this.angle3_y);
  }

  public void printBackPrisma(Graphics g) {

    printPrismaPart(g,
                    1,
                    this.selectedAngle,
                    this.angle1_x - 2 * this.sideMultiplicator,
                    this.angle1_y,
                    this.angle2_x + 2 * this.sideMultiplicator,
                    this.angle2_y,
                    this.angle3_x,
                    this.angle3_y);
  }

  private void printFrontPrisma(Graphics g) {

    printPrismaPart(g,
                    this.tillAngleDefined + 1,
                    this.angleList.size(),
                    this.angle1_x - (this.size1_x - this.size2_x) - 6 * this.sideMultiplicator,
                    this.angle1_y,
                    this.angle2_x - (this.size1_x - this.size2_x) - 2 * this.sideMultiplicator,
                    this.angle2_y,
                    this.angle3_x - (this.size1_x - this.size2_x) - 4 * this.sideMultiplicator,
                    this.angle3_y);
  }

  public void printPrismaPart(Graphics g,
                              int fromAngle,
                              int tillAngle,
                              int first1x,
                              int first1y,
                              int first2x,
                              int first2y,
                              int first3x,
                              int first3y) {
    this.tillAngleDefined = tillAngle;

    for (int i = fromAngle - 1; i < tillAngle; i++) {
      if (this.angleList.get(i).getColor1() == -1) {
        this.color1 = Math.random() * 255;
        this.color2 = Math.random() * 255;
        this.color3 = Math.random() * 255;

        this.angleList.get(i).setColor1(this.color1);
        this.angleList.get(i).setColor2(this.color2);
        this.angleList.get(i).setColor3(this.color3);
      } else {
        this.color1 = this.angleList.get(i).getColor1();
        this.color2 = this.angleList.get(i).getColor2();
        this.color3 = this.angleList.get(i).getColor3();
      }
      // unused coordinates:
      // int mom1_x = first1x - (i*sideMultiplicator);
      // int mom1_y = first1y + (i*sideMultiplicator);

      int mom2_x = first2x - (i * this.sideMultiplicator);
      int mom2_y = first2y + (i * this.sideMultiplicator);

      int mom3_x = first3x - (i * this.sideMultiplicator);
      int mom3_y = first3y + (i * this.sideMultiplicator);

      int side1_x = first1x - ((i + 1) * this.sideMultiplicator);
      int side1_y = first1y + ((i + 1) * this.sideMultiplicator);

      int side2_x = first2x - ((i + 1) * this.sideMultiplicator);
      int side2_y = first2y + ((i + 1) * this.sideMultiplicator);

      int side3_x = first3x - ((i + 1) * this.sideMultiplicator);
      int side3_y = first3y + ((i + 1) * this.sideMultiplicator);

      g.setColor(new Color((int)this.color1, (int)this.color2, (int)this.color3));

      int[] sideX = {mom2_x, mom3_x, side3_x, side2_x};
      int[] sideY = {mom2_y, mom3_y, side3_y, side2_y};
      g.fillPolygon(sideX, sideY, 4);

      int[] momPolySideX = {mom2_x, mom3_x, side3_x, side2_x};
      int[] momPolySideY = {mom2_y, mom3_y, side3_y, side2_y};
      Polygon momPolySide = new Polygon(momPolySideX, momPolySideY, 4);

      int[] momPolyFrontX = {side1_x, side2_x, side3_x};
      int[] momPolyFrontY = {side1_y, side2_y, side3_y};
      Polygon momPolyFront = new Polygon(momPolyFrontX, momPolyFrontY, 3);

      this.angleList.get(i).setAngleSide(momPolySide);
      this.angleList.get(i).setAngleFront(momPolyFront);

      if (i + 1 == tillAngle) {
        g.fillPolygon(momPolyFrontX, momPolyFrontY, 3);

        setFlowColors(this.angleList.get(i), g, side1_x, side1_y, side2_x, side2_y, side3_x, side3_y);
        g.setColor(new Color(0, 0, 0));

        int start3_x = first3x - (fromAngle - 1) * this.sideMultiplicator;
        int start3_y = first3y + (fromAngle - 1) * this.sideMultiplicator;
        int start2_x = first2x - (fromAngle - 1) * this.sideMultiplicator;
        int start2_y = first2y + (fromAngle - 1) * this.sideMultiplicator;

        int end3_x = first3x - tillAngle * this.sideMultiplicator;
        int end3_y = first3y + tillAngle * this.sideMultiplicator;
        int end2_x = first2x - tillAngle * this.sideMultiplicator;
        int end2_y = first2y + tillAngle * this.sideMultiplicator;

        g.drawLine(start3_x, start3_y, end3_x, end3_y);
        g.drawLine(start2_x, start2_y, end2_x, end2_y);
        g.drawLine(side2_x, side2_y, side3_x, side3_y);
        g.drawLine(side1_x, side1_y, side2_x, side2_y);
        g.drawLine(side1_x, side1_y, side3_x, side3_y);
      }
      g.setColor(new Color(0, 0, 0));
      g.drawLine(mom2_x, mom2_y, mom3_x, mom3_y);

      double angle = Math.atan2(side2_y - side3_y, side2_x - side3_x);
      rotateGraphics(angle, g, side3_x + 3, side3_y);
      g.setColor(new Color(255, 255, 255));
      g.setFont(new Font("Arial", Font.PLAIN, this.fontSize));
      g.drawString(this.angleList.get(i).getCatalogName(), side3_x + 3, side3_y);
      rotateGraphics(-angle, g, side3_x + 3, side3_y);

      if (i == this.selectedAngle - 1 || i == this.angleList.size() - 1) {
        g.setFont(new Font("Arial", Font.PLAIN, this.fontSize + 10));
        g.drawString(Math.round(this.angleList.get(i).getPercentage()) + "%", (side1_x + side2_x) / 2
                                                                              - this.sideMultiplicator, side1_y - 1);
      }
    }
  }

  public void setFlowColors(Catalog3Angle angle,
                            Graphics g,
                            int frontAngle1_x,
                            int frontAngle1_y,
                            int frontAngle2_x,
                            int frontAngle2_y,
                            int frontAngle3_x,
                            int frontAngle3_y) {
    int flow1 = (int)angle.getColor1();
    int flow2 = (int)angle.getColor2();
    int flow3 = (int)angle.getColor3();

    for (int i = frontAngle1_y - 1; i > frontAngle3_y + 1; i--) {
      int mom1x = (-1)
                  * (((frontAngle3_x - frontAngle1_x) * (i - frontAngle3_y)) / (frontAngle1_y - frontAngle3_y) - frontAngle3_x);
      int mom2x = ((frontAngle2_x - frontAngle3_x) * (i - frontAngle3_y)) / (frontAngle2_y - frontAngle3_y)
                  + frontAngle3_x;

      if (flow1 < 255) flow1++;
      if (flow2 < 255) flow2++;
      if (flow3 < 255) flow3++;
      g.setColor(new Color(flow1, flow2, flow3));
      g.drawLine(mom1x + 1, i, mom2x - 1, i);
    }
  }

  public void addValuesToAngleList() {
    this.angleList = new ArrayList<Catalog3Angle>();
    for (Category tmp : this.usedAssessment.getCategories()) {
      Catalog3Angle momAngle = new Catalog3Angle(tmp.getName());
      momAngle.setPercentage(tmp.getAnswered());

      this.angleList.add(momAngle);
    }

    for (Question tmp : this.usedAssessment.getQuestions()) {
      Catalog3Angle momAngle = new Catalog3Angle(tmp.getName());
      momAngle.setPercentage(tmp.getAnswered());

      this.angleList.add(momAngle);
    }
  }

  private void getUsedAssessment() {
    if (this.usedAssessment != null) {
      // angleList = new ArrayList<Catalog3Angle>();
      // addValuesToAngleList();
      // repaint();
    }
  }

  public void setNewRandomColors() {
    for (Catalog3Angle angle : this.angleList) {
      angle.setColor1(-1);
      angle.setColor2(-1);
      angle.setColor3(-1);
      super.repaint();
    }
  }

  @Override
  public void mouseClicked(MouseEvent e) {}

  @Override
  public void mousePressed(MouseEvent e) {

    if (this.contextMenu != null) {
      this.contextMenu.setVisible(false);
      this.contextMenu = null;
    }
    final Component comp = e.getComponent();
    final int clickX = e.getX();
    final int clickY = e.getY();

    if (e.getButton() == 1 && getClickedAngleNumber(clickX, clickY) == 0
        && !this.angleList.get(this.selectedAngle - 1).getAngleFront().contains(clickX, clickY)
        && !this.angleList.get(this.angleList.size() - 1).getAngleFront().contains(clickX, clickY)) {
      if (this.selectionMade == true && this.selectedAngle != this.angleList.size()) {
        this.angleList.get(this.selectedAngle).setSelected(false);
        super.repaint();
      }
      setSelectionMade(false);
      this.selectedAngle = this.angleList.size();
    }
    if (e.getButton() == 1 && getClickedAngleNumber(clickX, clickY) > 0
        && !this.angleList.get(this.selectedAngle - 1).getAngleFront().contains(clickX, clickY)
        && this.contextMenu == null) {
      boolean found = false;

      this.newSelectedAngle = getClickedAngleNumber(e.getX(), e.getY());
      if (this.newSelectedAngle > 0) found = true;

      if (this.newSelectedAngle != this.selectedAngle && found == true) {
        this.selectedAngle = this.newSelectedAngle;
        super.repaint();
      }
      setSelectionMade(true);
    }
    if (e.getButton() == 3 && !this.angleList.get(this.selectedAngle - 1).getAngleFront().contains(clickX, clickY)
        && !this.angleList.get(this.angleList.size() - 1).getAngleFront().contains(clickX, clickY)) {
      this.newSelectedAngle = getClickedAngleNumber(clickX, clickY);

      this.contextMenu = new JPopupMenu("Properties");
      this.contextMenu.setLightWeightPopupEnabled(false);

      this.savePrisma = new JMenuItem("Save Prisma");

      if (this.newSelectedAngle > 0) {
        this.color = new JMenuItem("Catalogue Colour");
      } else {
        this.color = new JMenuItem("Background Colour");

        this.randomColors = new JMenuItem("New Random Colours");
        this.randomColors.addActionListener(new ActionListener(){
          @Override
          public void actionPerformed(ActionEvent e) {
            setNewRandomColors();
            Prisma.this.contextMenu.setVisible(false);
            Prisma.this.contextMenu = null;
          }
        });
        this.contextMenu.insert(this.randomColors, 1);
      }

      this.colorChooser = new JColorChooser();
      final JButton buSubmitColor = new JButton("CHANGE COLOUR");
      buSubmitColor.setAlignmentX(Component.CENTER_ALIGNMENT);
      buSubmitColor.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          if (Prisma.this.newSelectedAngle > 0) {
            final Catalog3Angle newAngle = Prisma.this.angleList.get(Prisma.this.newSelectedAngle - 1);
            newAngle.setColor1(Prisma.this.colorChooser.getColor().getRed());
            newAngle.setColor2(Prisma.this.colorChooser.getColor().getGreen());
            newAngle.setColor3(Prisma.this.colorChooser.getColor().getBlue());
            repaint();
            Prisma.this.colorBox.setVisible(false);
            Prisma.this.colorBox = null;
          } else {
            changeBackground(Prisma.this.colorChooser.getColor().getRed(), Prisma.this.colorChooser.getColor()
              .getGreen(), Prisma.this.colorChooser.getColor().getBlue());
            Prisma.this.colorBox.setVisible(false);
            Prisma.this.colorBox = null;
          }
        }
      });

      this.color.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          deleteMenuItems();
          Prisma.this.contextMenu.setVisible(false);
          Prisma.this.contextMenu = null;
          Prisma.this.colorBox = new JPopupMenu();
          Prisma.this.colorBox.add(Prisma.this.colorChooser);
          Prisma.this.colorBox.add(buSubmitColor);
          Prisma.this.colorBox.show(comp, clickX, clickY);
        }
      });

      this.savePrisma.addActionListener(new ActionListener(){
        @Override
        public void actionPerformed(ActionEvent e) {
          deleteMenuItems();
          Prisma.this.contextMenu.setVisible(false);
          Prisma.this.contextMenu = null;

          Prisma.this.saveFrame = new JFrame();
          Prisma.this.saveFrame.setBounds(clickX, clickY, 500, 400);
          Prisma.this.saveFrame.addWindowListener(new WindowListener(){
            @Override
            public void windowOpened(WindowEvent e) {}

            @Override
            public void windowClosing(WindowEvent e) {}

            @Override
            public void windowClosed(WindowEvent e) {
              System.exit(0);
              Prisma.this.saveFrame.setVisible(false);
              Prisma.this.saveFrame = null;
            }

            @Override
            public void windowIconified(WindowEvent e) {}

            @Override
            public void windowDeiconified(WindowEvent e) {}

            @Override
            public void windowActivated(WindowEvent e) {}

            @Override
            public void windowDeactivated(WindowEvent e) {}
          });
          JFileChooser saver = new JFileChooser();
          saver.setDialogTitle("Save Prisma");

          int result = saver.showSaveDialog(Prisma.this.saveFrame);
          if (result == JFileChooser.APPROVE_OPTION) {
            Prisma.this.saveFrame.setVisible(false);
            Prisma.this.saveFrame = null;
            String saveFile = saver.getSelectedFile().getPath();

            BufferedImage image = new BufferedImage(getPrismaWidth(), getPrismaHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            paint(g2);

            try {
              ImageIO.write(image, "jpg", new File(saveFile));
            } catch (IOException e2) {
              e2.printStackTrace();
            }
          }
        }
      });

      this.contextMenu.insert(this.color, 0);
      this.contextMenu.insert(this.savePrisma, 3);
      this.contextMenu.show(comp, clickX, clickY);
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {}

  @Override
  public void mouseEntered(MouseEvent e) {}

  @Override
  public void mouseExited(MouseEvent e) {}

  @Override
  public void mouseDragged(MouseEvent e) {}

  @Override
  public void mouseMoved(MouseEvent e) {

    if (this.contextMenu != null) {
      this.contextMenu.setVisible(false);
      this.contextMenu = null;
    }

    // if (this.angleList.get(this.selectedAngle -
    // 1).getAngleFront().contains(clickX, clickY)) {
    // Catalog3Angle momAngle = this.angleList.get(this.selectedAngle - 1);
    //
    // Question hoveredQuestion = new Question("");
    // hoveredQuestion.setOrdered(-1);
    // for (int i = 0; i < momAngle.getQuestionList().size(); i++) {
    // if (momAngle.getQuestionList().get(i).getOrdered() == clickY -
    // momAngle.getAngleFront().ypoints[2]) {
    // hoveredQuestion = momAngle.getQuestionList().get(i);
    // System.out.println(hoveredQuestion.getName());
    // }
    // }
    // }
  }

  public int getClickedAngleNumber(int momX, int momY) {

    int angleNumber = 0;
    for (int i = 0; i < this.angleList.size(); i++) {
      if (this.angleList.get(i).getAngleSide().contains(momX, momY)) {
        this.angleList.get(i).setSelected(true);
        angleNumber = i + 1;
      }
    }
    return angleNumber;
  }

  public void setUpAngleX() {

    int currentWidth = getPrismaWidth();
    this.angle1_x = currentWidth - this.size1_x;
    this.angle2_x = currentWidth - this.size2_x;
    this.angle3_x = currentWidth - this.size3_x;
  }

  public int getPrismaHeight() {

    int height = this.angleList.size() * this.sideMultiplicator + (this.angle2_y - this.angle3_y) + this.spacer;
    return height;
  }

  public int getPrismaWidth() {

    int width = this.angleList.size() * this.sideMultiplicator + 2 * (this.size1_x - this.size2_x) + this.spacer + 6
                * this.sideMultiplicator;
    return width;
  }

  public void bigger(int count) {
    this.size1_x = this.size1_x - count;
    this.size2_x = this.size2_x + count;
    this.angle3_y = this.angle3_y - count;
    this.angle1_y = this.angle1_y + count;
    this.angle2_y = this.angle2_y + count;
    this.spacer = this.spacer + count;
    this.sideMultiplicator = this.sideMultiplicator + count;
    this.fontSize = this.fontSize + count;
    setPreferredSize(new Dimension(getPrismaWidth(), getPrismaHeight()));
    setUpAngleX();
    repaint();
  }

  public void smaller(int count) {
    this.size1_x = this.size1_x + count;
    this.size2_x = this.size2_x - count;
    this.angle3_y = this.angle3_y + count;
    this.angle1_y = this.angle1_y - count;
    this.angle2_y = this.angle2_y - count;
    this.spacer = this.spacer - count;
    this.sideMultiplicator = this.sideMultiplicator - count;
    this.fontSize = this.fontSize - count;
    setPreferredSize(new Dimension(getPrismaWidth(), getPrismaHeight()));
    setUpAngleX();
    repaint();
  }

  public void rotateGraphics(double angle, Graphics g, int x, int y) {
    this.g2d = (Graphics2D)g;
    this.g2d.rotate(angle, x, y);
  }

  public Image getGraphicsScreen() {
    this.buffImage = createImage(getPrismaWidth(), getPrismaHeight());
    return this.buffImage;
  }

  public void changeBackground(int red, int green, int blue) {
    this.bgColor = new Color(red, green, blue);
    setBackground(this.bgColor);
  }

  public void addCatalog3AngleToList(Catalog3Angle catalog) {
    this.angleList.add(catalog);
  }

  public void setSelectionMade(boolean selectionMade) {
    this.selectionMade = selectionMade;
  }

  public boolean isSelectionMade() {
    return this.selectionMade;
  }

  public void setAngleList(List<Catalog3Angle> angleList) {
    this.angleList = angleList;
  }

  public List<Catalog3Angle> getAngleList() {
    return this.angleList;
  }

  public int getSideMultiplicator() {
    return this.sideMultiplicator;
  }

  public void setSelectedAngle(int selectedAngle) {
    this.selectedAngle = selectedAngle;
  }

  public int getSelectedAngle() {
    return this.selectedAngle;
  }

  public void setContextMenu(JPopupMenu contextMenu) {
    this.contextMenu = contextMenu;
  }

  public JPopupMenu getContextMenu() {
    return this.contextMenu;
  }

  private void deleteMenuItems() {
    this.contextMenu.remove(this.color);
    this.color = null;
    this.contextMenu.remove(this.savePrisma);
    this.savePrisma = null;
    if (this.randomColors != null) {
      this.contextMenu.remove(this.randomColors);
      this.randomColors = null;
    }
  }
}