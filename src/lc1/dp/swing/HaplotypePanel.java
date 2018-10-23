/*     */ package lc1.dp.swing;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.File;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JScrollPane;
/*     */ import lc1.dp.core.BaumWelchTrainer;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.model.MarkovModel;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.PhasedDataState;
/*     */ import lc1.stats.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HaplotypePanel
/*     */   extends JPanel
/*     */   implements PropertyChangeListener
/*     */ {
/*     */   final int no_copies;
/*     */   final MarkovModel hmm;
/*     */   static EmissionStateSpace[] stateEm;
/*     */   final int data_index;
/*     */   CompoundEmissionStateSpace emStSp;
/*     */   EmissionStateSpace emStSp1;
/*     */   BaumWelchTrainer bwt;
/*     */   Dimension dim4;
/*     */   JScrollPane jG;
/*     */   int noSnps;
/*     */   public static Font font16;
/*     */   List<Integer> location;
/*     */   Map<Integer, LogoPanel> chartG;
/*     */   final List<Character> maj;
/*     */   final List<Character> min;
/*     */   JPanel jpBS;
/*     */   int heig;
/*     */   LocPanel locP;
/*     */   public static Font font10;
/*     */   public static Font font10i;
/*     */   final double reg_length;
/*     */   static Color background_color;
/*     */   double y_0;
/*     */   double y_1;
/*     */   double y_2;
/*     */   Dimension dim5;
/*     */   final File logoF;
/*     */   public boolean plot;
/*     */   double[] res;
/*     */   
/*     */   boolean isImputed(int k, int i)
/*     */   {
/*  77 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public HaplotypePanel(BaumWelchTrainer bwt, List<Integer> loc, List<Character> maj, List<Character> min, String name, int no_copies, int data_index) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   JComponent getJFrame(Map<Integer, LogoPanel> cp, JComponent jpBS)
/*     */   {
/* 125 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public void addedInformation1(PhasedDataState[] samples, int ll)
/*     */   {
/* 143 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public synchronized LogoPanel getCurrentGPanel(int l)
/*     */   {
/* 165 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized LogoPanel removePanel(int l)
/*     */   {
/* 173 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   class LocPanel
/*     */     extends JPanel
/*     */   {
/*     */     double prev_loc;
/*     */     
/*     */ 
/*     */ 
/*     */     LocPanel() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public double getX(int i, boolean useLoc)
/*     */     {
/* 192 */       throw new Error("Unresolved compilation problem: \n");
/*     */     }
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
/* 204 */       throw new Error("Unresolved compilation problems: \n\tVectorGraphics cannot be resolved to a type\n\tVectorGraphics cannot be resolved\n");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   class LogoPanel
/*     */     extends JPanel
/*     */   {
/*     */     String[][] hap1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     double[] cert;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     short[][] state1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     String name;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     double y_offset;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     int index;
/*     */     
/*     */ 
/*     */ 
/*     */     Character uncertain;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     LogoPanel(int arg2, String arg3) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void paint(Graphics g)
/*     */     {
/* 256 */       throw new Error("Unresolved compilation problems: \n\tVectorGraphics cannot be resolved to a type\n\tVectorGraphics cannot be resolved\n");
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
/*     */     private String getString(String[] strings)
/*     */     {
/* 315 */       throw new Error("Unresolved compilation problem: \n");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */   public void addedInformation(StateDistribution emissionC, int ll, int i)
/*     */   {
/* 403 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public synchronized void propertyChange(PropertyChangeEvent arg0)
/*     */   {
/* 426 */     throw new Error("Unresolved compilation problems: \n\tg cannot be resolved\n\tImageGraphics2D cannot be resolved to a type\n\tImageConstants cannot be resolved\n\tg cannot be resolved or is not a field\n\tg cannot be resolved\n\tg cannot be resolved\n\tg cannot be resolved\n\tg cannot be resolved or is not a field\n");
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
/*     */   public void setToPlot()
/*     */   {
/* 488 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/swing/HaplotypePanel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */