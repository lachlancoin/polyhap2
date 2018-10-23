/*     */ package lc1.stats;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.illumina.IlluminaProbR;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public abstract class PseudoDistribution
/*     */   implements ProbabilityDistribution
/*     */ {
/*  19 */   short data_index = -1;
/*     */   
/*     */   public abstract double[] probs();
/*     */   
/*     */   public abstract void setProb(double[] paramArrayOfDouble);
/*     */   
/*     */   public abstract void transfer(double paramDouble);
/*     */   
/*     */   public abstract void addCount(int paramInt, double paramDouble);
/*     */   
/*     */   public abstract void initialise();
/*     */   
/*     */   public abstract double sample();
/*     */   
/*     */   public abstract double probs(int paramInt);
/*     */   
/*     */   public abstract double sum();
/*     */   
/*     */   public abstract int getMax();
/*     */   
/*     */   public abstract double[] counts();
/*     */   
/*     */   public abstract PseudoDistribution clone();
/*     */   
/*     */   public abstract void validate();
/*     */   
/*     */   public abstract void setProbs(int paramInt, double paramDouble);
/*     */   
/*     */   public abstract String getPrintString();
/*     */   
/*     */   public void printSimple(PrintWriter pw, String string, String string2, double thresh)
/*     */   {
/*  51 */     pw.print(string + " " + string2 + " " + getPrintString());
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, boolean b, String printString, String string) {
/*  55 */     pw.print(printString + " " + string + " " + getPrintString());
/*     */   }
/*     */   
/*     */   public abstract PseudoDistribution clone(double paramDouble);
/*     */   
/*     */   public abstract double evaluate(double[] paramArrayOfDouble);
/*     */   
/*     */   public abstract void setCounts(int paramInt, double paramDouble);
/*     */   
/*     */   public abstract Integer fixedInteger();
/*     */   
/*     */   public abstract void swtchAlleles(int[] paramArrayOfInt);
/*     */   
/*  68 */   public double score(SkewNormal[] probRState) { return 1.0D; }
/*     */   
/*     */   public double score(int j, IlluminaProbB[] probBState) {
/*  71 */     return probs(j);
/*     */   }
/*     */   
/*  74 */   public double score(int j, IlluminaProbB probBState) { return probs(j); }
/*     */   
/*     */   public abstract void addCount(SkewNormal[] paramArrayOfSkewNormal, double paramDouble);
/*     */   
/*     */   public abstract void addCount(IlluminaProbB[] paramArrayOfIlluminaProbB, Integer paramInteger, double paramDouble);
/*     */   
/*     */   public short getDataIndex()
/*     */   {
/*  82 */     return this.data_index;
/*     */   }
/*     */   
/*     */   public double[] calcDistribution(IlluminaProbR probR, IlluminaProbB probB, double[] distribution, EmissionStateSpace emStSp)
/*     */   {
/*  87 */     return probs();
/*     */   }
/*     */   
/*     */   public double[] calcDistribution(IlluminaProbB probB, double[] distribution, EmissionStateSpace emStSp, int nocop) {
/*  91 */     double sum = 0.0D;
/*  92 */     double[] probs = probs();
/*  93 */     Arrays.fill(distribution, 0.0D);
/*  94 */     for (int j = 0; j < distribution.length; j++) {
/*  95 */       if (emStSp.getCN(j) != nocop) {
/*  96 */         distribution[j] = 0.0D;
/*     */       }
/*     */       else {
/*  99 */         distribution[j] = probs[j];
/*     */       }
/* 101 */       sum += distribution[j];
/*     */     }
/* 103 */     for (int i = 0; i < distribution.length; i++) {
/* 104 */       distribution[i] /= sum;
/*     */     }
/* 106 */     return distribution;
/*     */   }
/*     */   
/*     */   public abstract void setFixedIndex(int paramInt);
/*     */   
/*     */   public String getUnderlyingData(EmissionStateSpace emStSp)
/*     */   {
/* 113 */     int ind = Constants.getMax(probs());
/* 114 */     return emStSp.get(ind) + "_" + probs()[ind];
/*     */   }
/*     */   
/*     */   public String getUnderlyingDataAll(EmissionStateSpace emStSp) {
/* 118 */     String all = "";
/* 119 */     SortedMap<Double, Integer> in = new TreeMap();
/* 120 */     for (int i = 0; i < probs().length; i++) {
/* 121 */       if (in.containsKey(Double.valueOf(probs()[i]))) in.put(Double.valueOf(probs()[i] - 1.0E-5D), Integer.valueOf(i)); else
/* 122 */         in.put(Double.valueOf(probs()[i]), Integer.valueOf(i));
/*     */     }
/* 124 */     int[] order = new int[in.size()];
/* 125 */     int count = 0;
/* 126 */     for (Iterator<Double> is = in.keySet().iterator(); is.hasNext();) {
/* 127 */       Double s = (Double)is.next();
/* 128 */       order[count] = ((Integer)in.get(s)).intValue();
/* 129 */       count++;
/*     */     }
/* 131 */     for (int i = order.length - 1; i > 0; i--) {
/* 132 */       all = all + emStSp.get(order[i]) + "_" + probs()[order[i]] + ":";
/*     */     }
/* 134 */     all = all + emStSp.get(order[0]) + "_" + probs()[order[0]];
/* 135 */     return all;
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
/*     */   public void setDataIndex(short data_index2)
/*     */   {
/* 157 */     this.data_index = data_index2;
/*     */   }
/*     */   
/*     */   public void transfercounts(EmissionState innerState, int phen_index, int i)
/*     */   {
/* 162 */     double[] countsData = counts();
/* 163 */     for (int k = 0; k < countsData.length; k++) {
/* 164 */       if (countsData[k] != 0.0D) {
/* 165 */         innerState.addCountDT(k, phen_index, countsData[k], i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract double totalCount();
/*     */   
/*     */   public void applyCorrection(double parseDouble) {}
/*     */   
/*     */   public void addCount(double obj_index, double value)
/*     */   {
/* 177 */     addCount((int)Math.round(obj_index), value);
/*     */   }
/*     */   
/*     */   public double probability(double x)
/*     */   {
/* 182 */     return probs((int)Math.round(x));
/*     */   }
/*     */   
/*     */   public double[] getCount(double[] angle) {
/* 186 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/PseudoDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */