/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.genotype.HaplotypeEmissionState;
/*     */ import lc1.dp.genotype.SimpleExtendedDistribution;
/*     */ import lc1.dp.genotype.io.Emiss;
/*     */ import lc1.dp.genotype.io.EmissionStateSpace;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class LikelihoodData
/*     */   extends StateIndices
/*     */ {
/*     */   public final EmissionState state_indices;
/*     */   
/*     */   public int getBestIndex(EmissionState paramEmissionState, int paramInt)
/*     */   {
/*  23 */     throw new Error("Unresolved compilation problem: \n\tThe type LikelihoodData must implement the inherited abstract method StateIndices.getBestIndex(EmissionState, int)\n");
/*     */   }
/*     */   
/*     */   public LikelihoodData(CompoundScorableObject obj) {
/*  27 */     this(obj, Emiss.getEmissionStateSpace(obj.noCopies() - 1));
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
/*     */   public LikelihoodData(LikelihoodData father, LikelihoodData mother, LikelihoodData child)
/*     */   {
/*  48 */     this(father, mother, child, Emiss.getEmissionStateSpace(5));
/*     */   }
/*     */   
/*     */   public LikelihoodData(LikelihoodData mother, LikelihoodData child) {
/*  52 */     this(mother, child, Emiss.getEmissionStateSpace(2));
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
/*     */   public LikelihoodData(EmissionState emSt)
/*     */   {
/*  77 */     this(emSt.getName(), emSt);
/*     */   }
/*     */   
/*     */   private int stSize() {
/*  81 */     throw new Error("Unresolved compilation problem: \n\tdistribution cannot be resolved or is not a field\n");
/*     */   }
/*     */   
/*     */   public LikelihoodData[] split() {
/*  85 */     throw new Error("Unresolved compilation problems: \n\tThe method getMemberStates() in the type CompoundState is not applicable for the arguments (boolean)\n\tname cannot be resolved or is not a field\n");
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
/*     */   public void setProb(int i, double[] d)
/*     */   {
/* 113 */     HaplotypeEmissionState state_indices1 = (HaplotypeEmissionState)this.state_indices;
/* 114 */     System.arraycopy(d, 0, state_indices1.emissions[i].probs, 0, d.length);
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
/*     */   public void addDataPoint(double[] str, List<Integer>[] categoryToStateSpaceIndex, int i)
/*     */   {
/* 131 */     throw new Error("Unresolved compilation problems: \n\tThe method logToProb() is undefined for the type SimpleExtendedDistribution\n\tbest_index cannot be resolved or is not a field\n");
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
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 149 */     throw new Error("Unresolved compilation problems: \n\tname cannot be resolved\n\tname cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class DoubleInt
/*     */     implements Comparable
/*     */   {
/*     */     Double d;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     Integer i;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     DoubleInt(Double d, Integer i)
/*     */     {
/* 172 */       this.d = d;
/* 173 */       this.i = i;
/*     */     }
/*     */     
/* 176 */     public int compareTo(Object o) { DoubleInt d1 = (DoubleInt)o;
/* 177 */       return -1 * this.d.compareTo(d1.d);
/*     */     }
/*     */   }
/*     */   
/*     */   public static int getMax(double[] d, int pos) {
/* 182 */     List<DoubleInt> l = new ArrayList();
/*     */     
/* 184 */     for (int i = 0; i < d.length; i++) {
/* 185 */       l.add(new DoubleInt(Double.valueOf(d[i]), Integer.valueOf(i)));
/*     */     }
/* 187 */     Collections.sort(l);
/* 188 */     return ((DoubleInt)l.get(pos)).i.intValue();
/*     */   }
/*     */   
/*     */   public static Object[] emissions(SimpleExtendedDistribution[] d, int ind)
/*     */   {
/* 193 */     Double[] res = new Double[d.length];
/* 194 */     Integer[] res1 = new Integer[d.length];
/* 195 */     for (int i = 0; i < res.length; i++) {
/* 196 */       int pos = i;
/* 197 */       if (d.length == 1) pos = 0;
/* 198 */       int k = getMax(d[pos].probs, ind);
/* 199 */       res[i] = Double.valueOf(d[pos].probs[k]);
/*     */       
/*     */ 
/* 202 */       res1[i] = Integer.valueOf(k);
/*     */     }
/*     */     
/* 205 */     return new Object[] { res, res1 };
/*     */   }
/*     */   
/* 208 */   public double[] getDistribution(int i) { throw new Error("Unresolved compilation problem: \n\tThe method getEmiss(int) is undefined for the type EmissionState\n"); }
/*     */   
/*     */ 
/*     */   public int getBestIndex(EmissionState state, int i, boolean sample)
/*     */   {
/* 213 */     throw new Error("Unresolved compilation problems: \n\tbest_index cannot be resolved or is not a field\n\tdistribution cannot be resolved\n\tdistribution cannot be resolved\n");
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
/*     */   public int length()
/*     */   {
/* 229 */     throw new Error("Unresolved compilation problem: \n\tThe method noSnps() is undefined for the type EmissionState\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final double[] score(EmissionState state, boolean logspace)
/*     */   {
/* 241 */     throw new Error("Unresolved compilation problem: \n\tlen cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(EmissionState state, Double double1, int i)
/*     */   {
/* 252 */     throw new Error("Unresolved compilation problems: \n\tdistribution cannot be resolved\n\tdistribution cannot be resolved\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double calcDistribution(EmissionState state, int i)
/*     */   {
/* 264 */     throw new Error("Unresolved compilation problems: \n\tdistribution cannot be resolved\n\tdistribution cannot be resolved\n\tdistribution cannot be resolved\n\tdistribution cannot be resolved\n");
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
/*     */   public PhasedIntegerGenotypeData getGenotypeData(List<Comparable> stSp)
/*     */   {
/* 277 */     throw new Error("Unresolved compilation problems: \n\tname cannot be resolved\n\tThe method noSnps() is undefined for the type EmissionState\n\tbest_index cannot be resolved or is not a field\n\tbest_index cannot be resolved or is not a field\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transferCountsToProbs(double pseudo)
/*     */   {
/* 286 */     throw new Error("Unresolved compilation problem: \n\tThe method transferCountsToProbs(double) is undefined for the type EmissionState\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialise()
/*     */   {
/* 294 */     this.state_indices.initialiseCounts();
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/* 299 */     throw new Error("Unresolved compilation problem: \n\tname cannot be resolved\n");
/*     */   }
/*     */   
/* 302 */   public void setName(String name) { throw new Error("Unresolved compilation problem: \n\tname cannot be resolved or is not a field\n"); }
/*     */   
/*     */   public LikelihoodData(ScorableObject obj, EmissionStateSpace hmm) {}
/*     */   
/*     */   LikelihoodData(LikelihoodData father, LikelihoodData mother, LikelihoodData child, EmissionStateSpace emStSp) {}
/*     */   
/*     */   LikelihoodData(LikelihoodData mother, LikelihoodData child, EmissionStateSpace emStSp) {}
/*     */   
/*     */   public LikelihoodData(EmissionState[] states, EmissionStateSpace emStSp) {}
/*     */   
/*     */   public LikelihoodData(String name, EmissionState state_i) {}
/*     */   
/*     */   public LikelihoodData(String name, EmissionStateSpace emStSp, int noSnps) {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/LikelihoodData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */