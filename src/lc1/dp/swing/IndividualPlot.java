/*     */ package lc1.dp.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JSplitPane;
/*     */ import lc1.dp.core.BaumWelchTrainer;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.model.MarkovModel;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.StateDistribution;
/*     */ import org.jfree.chart.ChartPanel;
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
/*     */ public class IndividualPlot
/*     */   extends JPanel
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   final int index;
/*     */   static int width;
/*     */   static int height;
/*     */   static Dimension dim2;
/*     */   static Dimension dim;
/*     */   static EmissionStateSpace[] stateEm;
/*     */   CompoundEmissionStateSpace emStSp;
/*     */   EmissionStateSpace emStSp1;
/*     */   final MarkovModel hmm;
/*     */   JComponent jpB;
/*     */   JComponent jpDist;
/*     */   JScrollPane jB;
/*     */   JScrollPane jR;
/*     */   JComponent[] chartB;
/*     */   JComponent[] chartR;
/*     */   JComponent[] chartDist;
/*     */   ChartPanel[] chartBNew;
/*     */   ChartPanel[] chartRNew;
/*     */   JSplitPane jpBS;
/*     */   public static Font font16;
/*     */   public static Font font10;
/*     */   public static Font font4;
/*     */   final int noSnps;
/*     */   BaumWelchTrainer bwt;
/*     */   List<Integer> location;
/*     */   final Map<K, V> rdc;
/*     */   public File chartDistF;
/*     */   public File chartBF;
/*     */   public File chartRF;
/*     */   int plot;
/*     */   double[] res;
/*     */   public static Font font;
/*     */   public static Stroke dashed;
/*     */   public static Stroke dotted;
/*     */   static char[] shapes;
/*     */   public static Font font8;
/*     */   long lastupdate;
/*     */   
/*     */   public IndividualPlot(BaumWelchTrainer bwt, List<Integer> loc, int index, String name) {}
/*     */   
/*     */   public JComponent getBottomPane(JComponent[] chartDist)
/*     */   {
/* 172 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public JComponent getTabbedPane(JComponent r_pair, JComponent r_single)
/*     */   {
/* 190 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setToPlot(int i)
/*     */   {
/* 201 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/* 204 */   public void writeCurrentCharts(int i) { throw new Error("Unresolved compilation problem: \n"); }
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
/*     */   public void writeToZipFile(Component charts, File dir, String id)
/*     */     throws Exception
/*     */   {
/* 220 */     throw new Error("Unresolved compilation problems: \n\tImageGraphics2D cannot be resolved to a type\n\tImageGraphics2D cannot be resolved to a type\n\tImageConstants cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeToZipFile(Component[] charts, File dir, String[] id)
/*     */     throws Exception
/*     */   {
/* 230 */     throw new Error("Unresolved compilation problems: \n\tImageGraphics2D cannot be resolved to a type\n\tImageGraphics2D cannot be resolved to a type\n\tImageConstants cannot be resolved\n");
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
/*     */   JComponent getJFrame(JComponent[] cp)
/*     */   {
/* 246 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public ChartPanel[] getDistCharts()
/*     */   {
/* 266 */     throw new Error("Unresolved compilation problems: \n\tlu_rpair cannot be resolved or is not a field\n\tlu_r cannot be resolved\n\tlu_b cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reinitialise(int l)
/*     */   {
/* 278 */     throw new Error("Unresolved compilation problems: \n\tXYSeriesCollection cannot be resolved to a type\n\tThe method getBSeriesCollection(int) is undefined for the type IndividualPlot\n\tXYSeriesCollection cannot be resolved to a type\n\tThe method getRSeriesCollection(int) is undefined for the type IndividualPlot\n");
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
/*     */   public void addedInformation(StateDistribution emissionC, int ll, int i)
/*     */   {
/* 292 */     throw new Error("Unresolved compilation problems: \n\tXYSeriesCollection cannot be resolved to a type\n\tThe method getBSeriesCollection(int) is undefined for the type IndividualPlot\n\tXYSeriesCollection cannot be resolved to a type\n\tThe method getRSeriesCollection(int) is undefined for the type IndividualPlot\n");
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
/*     */   private synchronized double[] getProbOverStates(StateDistribution emissionC, MarkovModel hmm, HaplotypeEmissionState obj, int i)
/*     */   {
/* 319 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public JComponent getCurrentBPanel(int l)
/*     */   {
/* 337 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */   public JComponent getCurrentRPanel(int l)
/*     */   {
/* 342 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void update()
/*     */   {
/* 349 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Shape getShape(char ch)
/*     */   {
/* 386 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private static double sum(Collection<SkewNormal> s1)
/*     */   {
/* 624 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final List<SkewNormal> extract(Map<String, List<SkewNormal>> m)
/*     */   {
/* 636 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final Map<String, List<SkewNormal>> transform(Collection<SkewNormal> s2)
/*     */   {
/* 643 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void propertyChange(PropertyChangeEvent arg0)
/*     */   {
/* 762 */     throw new Error("Unresolved compilation problems: \n\tXYSeriesCollection cannot be resolved to a type\n\tThe method getRSeriesCollection(Integer) is undefined for the type IndividualPlot\n\tXYSeriesCollection cannot be resolved to a type\n\tThe method getBSeriesCollection(Integer) is undefined for the type IndividualPlot\n\tbdc cannot be resolved\n\tlu_rpair cannot be resolved\n\tlu_b cannot be resolved\n\tImageGraphics2D cannot be resolved to a type\n\tImageGraphics2D cannot be resolved to a type\n\tImageConstants cannot be resolved\n\tImageGraphics2D cannot be resolved to a type\n\tImageGraphics2D cannot be resolved to a type\n\tImageConstants cannot be resolved\n\tImageGraphics2D cannot be resolved to a type\n\tImageGraphics2D cannot be resolved to a type\n\tImageConstants cannot be resolved\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/swing/IndividualPlot.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */