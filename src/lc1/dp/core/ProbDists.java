/*     */ package lc1.dp.core;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.Callable;
/*     */ import lc1.dp.swing.ColorAdapter;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.StateDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class ProbDists
/*     */ {
/*     */   public ProbMultivariate mvf;
/*     */   public static ColorAdapter ca_r;
/*  23 */   public static ColorAdapter ca_b = new ColorAdapter();
/*  24 */   public List<SkewNormal> s1 = new ArrayList();
/*  25 */   public final List<SkewNormal[]> s1_memb = new ArrayList();
/*  26 */   public final List<SkewNormal> s2 = new ArrayList();
/*  27 */   public final List<Integer> probBIndices = new ArrayList();
/*  28 */   public final List<List<Integer>> stateIndices = new ArrayList();
/*  29 */   public final List<Integer> stateIndexCNV = new ArrayList();
/*     */   
/*  31 */   public String getString(SkewNormal sn) { List l = new ArrayList((Collection)sn.observations.entrySet());
/*  32 */     return l.subList(0, Math.min(l.size(), 5)).toString();
/*     */   }
/*     */   
/*  35 */   public void initColors() { if (ca_r != null) return;
/*  36 */     SortedMap<Double, String> m = new TreeMap();
/*  37 */     List<Color> l = new ArrayList();
/*  38 */     for (int i = 0; i < this.mvf.single.size(); i++) {
/*  39 */       SkewNormal sn = (SkewNormal)this.mvf.single.get(i);
/*  40 */       m.put(Double.valueOf(sn.getMean()), sn.name()); }
/*     */     Color blue;
/*     */     Color green;
/*  43 */     Color red = green = blue = null;
/*  44 */     for (Iterator<Double> it = m.keySet().iterator(); it.hasNext();) {
/*  45 */       Double key = (Double)it.next();
/*  46 */       if (key.doubleValue() < -0.01D) {
/*  47 */         if (blue != null) {
/*  48 */           blue = modify(blue);
/*     */         }
/*     */         else
/*  51 */           blue = Color.BLUE;
/*  52 */         l.add(blue);
/*     */       }
/*  54 */       else if (key.doubleValue() > 0.01D) {
/*  55 */         if (green != null) {
/*  56 */           green = modify(green);
/*     */         }
/*     */         else
/*  59 */           green = Color.GREEN;
/*  60 */         l.add(green);
/*     */       }
/*     */       else {
/*  63 */         if (red != null) {
/*  64 */           red = modify(red);
/*     */         }
/*     */         else
/*  67 */           red = Color.red;
/*  68 */         l.add(red);
/*     */       }
/*     */     }
/*  71 */     ca_r = new ColorAdapter((Color[])l.toArray(new Color[0]));
/*  72 */     for (Iterator<String> it = m.values().iterator(); it.hasNext();)
/*  73 */       ca_r.getColor((String)it.next());
/*     */   }
/*     */   
/*     */   private Color modify(Color red) {
/*  77 */     return new Color(Math.min(255, red.getRed() + 50), 
/*  78 */       Math.min(255, red.getGreen() + 50), 
/*  79 */       Math.min(255, red.getBlue() + 50));
/*     */   }
/*     */   
/*     */   public void compareTo(ProbDists probDists) {
/*  83 */     System.err.println("comparing");
/*  84 */     for (int i = 0; i < this.s1.size(); i++) {
/*  85 */       System.err.println(getString((SkewNormal)this.s1.get(i)) + " cf \n" + getString((SkewNormal)probDists.s1.get(i)) + " " + ((SkewNormal)this.s1.get(i)).equals(probDists.s1.get(i)) + "\n");
/*     */     }
/*  87 */     for (int i = 0; i < this.s2.size(); i++) {
/*  88 */       System.err.println(getString((SkewNormal)this.s2.get(i)) + " cf \n" + getString((SkewNormal)probDists.s2.get(i)) + " " + ((SkewNormal)this.s2.get(i)).equals(probDists.s2.get(i)) + "\n");
/*     */     }
/*  90 */     System.err.println("done comparing");
/*     */   }
/*     */   
/*     */   public void getTasks(List tasks, final double[] mvst, final double pseudo) {
/*  94 */     if (this.mvf != null) {
/*  95 */       tasks.add(new Callable() {
/*     */         public Object call() {
/*  97 */           ProbDists.this.mvf.minimise(mvst[0] * pseudo * Constants.r_train(), mvst[1] * pseudo * Constants.r_train(), mvst[2] * pseudo * Constants.r_train());
/*  98 */           return null;
/*     */         }
/*     */         
/*     */ 
/*     */       });
/*     */     }
/*     */     else
/*     */     {
/* 106 */       BaumWelchTrainer.train(this.s2, pseudo * Constants.r_train(), tasks);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getBTasks(List tasks, double pseudo) {
/* 111 */     BaumWelchTrainer.train(this.s2, pseudo * Constants.b_train(), tasks);
/*     */   }
/*     */   
/*     */   public double[] getProbOverRDists(StateDistribution emC) {
/* 115 */     double[] prob_r = new double[this.stateIndices.size()];
/* 116 */     Arrays.fill(prob_r, 0.0D);
/* 117 */     for (int j0 = 0; j0 < this.stateIndices.size(); j0++) {
/* 118 */       List<Integer> ind = (List)this.stateIndices.get(j0);
/* 119 */       for (int j1 = 0; j1 < ind.size(); j1++) {
/* 120 */         prob_r[j0] += emC.dist[((Integer)ind.get(j1)).intValue()];
/*     */       }
/*     */     }
/* 123 */     return prob_r;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/ProbDists.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */