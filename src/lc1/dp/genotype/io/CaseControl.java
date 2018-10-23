/*     */ package lc1.dp.genotype.io;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import lc1.dp.PairMarkovModel;
/*     */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CaseControl
/*     */ {
/*     */   PrintWriter summary;
/*     */   PrintWriter clust_cases;
/*     */   PrintWriter clust_controls;
/*     */   Double[] pval;
/*     */   Double[] evalue;
/*     */   Integer[] sigStates;
/*     */   Integer[] sigSites;
/*     */   ArrayList<Integer[][]> vit_casesD;
/*     */   ArrayList<Integer[][]> vit_controlD;
/*     */   PairMarkovModel model;
/*     */   String sb_d;
/*     */   List<PhasedIntegerGenotypeData> union;
/*     */   Inner mainInner;
/*     */   Inner randomInner;
/*     */   static boolean dirichlet;
/*     */   
/*     */   class Inner
/*     */   {
/*     */     Double[][] fb_cases;
/*     */     Double[][] fb_controls;
/*     */     Sampler caseMonitor;
/*     */     Sampler controlMonitor;
/*     */     
/*     */     Inner(List<PhasedIntegerGenotypeData> arg2) {}
/*     */     
/*     */     public Double[] calculatePValues()
/*     */       throws Exception
/*     */     {
/*  50 */       throw new Error("Unresolved compilation problem: \n");
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
/*     */     private void printCaseControl()
/*     */     {
/*  63 */       throw new Error("Unresolved compilation problem: \n");
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
/*     */   void update(List<PhasedIntegerGenotypeData> cases, List<PhasedIntegerGenotypeData> controls)
/*     */   {
/*  77 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */   void updateModel(PairMarkovModel hmm, String sb_d)
/*     */   {
/*  83 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CaseControl(PrintWriter summary, PrintWriter clust_cases, PrintWriter clust_controls) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Double[] getDirichletPVal(Double[][] control, Double[][] cases)
/*     */   {
/*  96 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private static Double[] getKL(Double[][] control, Double[][] cases)
/*     */   {
/* 109 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private int getSigStates(int s)
/*     */   {
/* 127 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private void getSigSites()
/*     */   {
/* 140 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   private boolean close(double double1, double min)
/*     */   {
/* 178 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */   private static void normalise(Double[][] vit_controlD) {
/* 182 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getRandomCaseControlData(List<PhasedIntegerGenotypeData> union, double fracCases)
/*     */   {
/* 193 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public boolean run()
/*     */     throws Exception
/*     */   {
/* 209 */     throw new Error("Unresolved compilation problem: \n");
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
/*     */   public void calcEvalue()
/*     */     throws Exception
/*     */   {
/* 249 */     throw new Error("Unresolved compilation problems: \n\tHistogram cannot be resolved to a type\n\tHistogram cannot be resolved to a type\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double[] getMinMax(List<Double> l)
/*     */   {
/* 260 */     throw new Error("Unresolved compilation problem: \n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/CaseControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */