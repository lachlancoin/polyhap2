/*     */ package lc1.dp.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTabbedPane;
/*     */ import lc1.dp.data.collection.Phenotypes;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.model.FreeHaplotypeHMM;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HMMPanel
/*     */   extends JTabbedPane
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   final List<Integer> location;
/*     */   final List<Character> major;
/*     */   final List<Character> minor;
/*     */   Color background_color;
/*     */   Color line_color;
/*     */   Color font_color;
/*     */   int x_start;
/*     */   int x_end;
/*     */   final File hmmF;
/*     */   final double noIndiv;
/*     */   Font small_font;
/*     */   Font large_font;
/*     */   Font small_italic_font;
/*     */   Font large_italic_font;
/*     */   FontMetrics fm_small;
/*     */   FontMetrics fm_large;
/*     */   FontMetrics fm_small_italic;
/*     */   FontMetrics fm_large_italic;
/*     */   Logger logger;
/*     */   Color[] colors;
/*     */   Phenotypes pheno;
/*     */   FreeHaplotypeHMM hmm;
/*     */   public static Font font16;
/*     */   double wid;
/*     */   double wid1;
/*     */   double height;
/*     */   double shape;
/*     */   double max_shape;
/*     */   double prev_loc;
/*     */   final int minloc;
/*     */   final double reg_length;
/*     */   final double maxLineWidth = 10.0D;
/*     */   static int len;
/*     */   static int width;
/*     */   final int noSnps;
/*     */   final int numFounders;
/*     */   final double offset_x = 50.0D;
/*     */   final double offset_xR = 100.0D;
/*     */   final double offset_y = 20.0D;
/*     */   static Color LIGHTGREEN;
/*     */   static Color DARKGREEN;
/*     */   static ColorAdapter ca;
/*     */   static final double log2;
/*     */   final double[][] mat;
/*     */   final EmissionStateSpace emStSp;
/*     */   Character uncertain;
/*     */   double y_0;
/*     */   double y_1;
/*     */   
/*     */   void mediumFonts()
/*     */   {
/*  81 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/*  92 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HMMPanel(FreeHaplotypeHMM hmm, List<Integer> location, List<Character> major, List<Character> minor, int noIndiv, Phenotypes pheno) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getX(int i, boolean useLoc)
/*     */   {
/* 154 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */   public double getY(int i)
/*     */   {
/* 159 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */   public double getStartX()
/*     */   {
/* 164 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */   public double getStartY() {
/* 168 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   class InnerPanel
/*     */     extends JPanel
/*     */   {
/*     */     final int phenIndexToPaint;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final double[] angle;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final double[] angle1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public InnerPanel() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public InnerPanel(String arg2) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void paint0()
/*     */     {
/* 232 */       throw new Error("Unresolved compilation problems: \n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void drawOval(double xcen, double ycen, double shape, double startAngle, double endAngle)
/*     */     {
/* 246 */       throw new Error("Unresolved compilation problem: \n\tvg cannot be resolved\n");
/*     */     }
/*     */     
/* 249 */     void fillOval(double xcen, double ycen, double shape, double startAngle, double endAngle) { throw new Error("Unresolved compilation problem: \n\tvg cannot be resolved\n"); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void write(int i)
/*     */     {
/* 256 */       throw new Error("Unresolved compilation problems: \n\tvg cannot be resolved\n\tvg cannot be resolved\n");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void paint(int i)
/*     */     {
/* 277 */       throw new Error("Unresolved compilation problems: \n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 360 */       throw new Error("Unresolved compilation problems: \n\tvg cannot be resolved\n\tVectorGraphics cannot be resolved\n\tvg cannot be resolved\n\tvg cannot be resolved\n");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double mod(double[] probs, int max, int[] comp)
/*     */   {
/* 381 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Color modify(Color c, double frac)
/*     */   {
/* 389 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double transform(double d)
/*     */   {
/* 412 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/* 415 */   private double transform1(double d) { throw new Error("Unresolved compilation problem: \n"); }
/*     */   
/*     */   private Color getLineColor(double d, Color c) {
/* 418 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void propertyChange(PropertyChangeEvent arg0)
/*     */   {
/* 433 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/swing/HMMPanel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */