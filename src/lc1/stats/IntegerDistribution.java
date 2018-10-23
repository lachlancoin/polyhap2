/*     */ package lc1.stats;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ 
/*     */ public class IntegerDistribution extends PseudoDistribution
/*     */ {
/*     */   Integer i;
/*     */   
/*     */   public IntegerDistribution(int i)
/*     */   {
/*  13 */     this.i = Integer.valueOf(i);
/*     */   }
/*     */   
/*  16 */   public double KLDistance(PseudoDistribution d2) { throw new RuntimeException("!!"); }
/*     */   
/*     */   public void addCount(int obj_index, double value)
/*     */   {
/*  20 */     this.cnt = ((int)(this.cnt + value));
/*     */   }
/*     */   
/*     */   public void setFixedIndex(int k)
/*     */   {
/*  25 */     this.i = Integer.valueOf(k);
/*     */   }
/*     */   
/*  28 */   public void setParamsAsAverageOf(ProbabilityDistribution[] tmp) { throw new RuntimeException("!!"); }
/*     */   
/*     */   public void addCounts(ProbabilityDistribution probabilityDistribution) {
/*  31 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  34 */   public PseudoDistribution clone(double swtch) { return new IntegerDistribution(this.i.intValue()); }
/*     */   
/*     */   public PseudoDistribution clone() {
/*  37 */     return new IntegerDistribution(this.i.intValue());
/*     */   }
/*     */   
/*     */   public double[] counts() {
/*  41 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  44 */   public String getUnderlyingData(EmissionStateSpace emStSp) { return emStSp.get(this.i.intValue()) + "_" + 1.0D; }
/*     */   
/*     */   public double evaluate(double[] ds)
/*     */   {
/*  48 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public int getMax() {
/*  52 */     return this.i.intValue();
/*     */   }
/*     */   
/*     */ 
/*  56 */   public String getPrintString() { return this.i; }
/*     */   
/*  58 */   int cnt = 0;
/*     */   
/*  60 */   public void initialise() { this.cnt = 0; }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw, boolean b, String printString, String string)
/*     */   {
/*  65 */     pw.print(printString + " " + this.i);
/*     */   }
/*     */   
/*     */   public void printSimple(PrintWriter pw, String name, String newLine, double thresh)
/*     */   {
/*  70 */     pw.print(name + "->{");
/*     */     
/*     */ 
/*  73 */     pw.print(this.i + ":" + 100 + ",");
/*     */     
/*     */ 
/*  76 */     pw.print("}" + newLine);
/*     */   }
/*     */   
/*     */   public double[] probs() {
/*  80 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double probs(int obj_i) {
/*  84 */     if (obj_i == this.i.intValue()) return 1.0D;
/*  85 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public double sample() {
/*  89 */     return this.i.intValue();
/*     */   }
/*     */   
/*     */   public void setCounts(int i1, double cnt) {
/*  93 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setProb(double[] prob)
/*     */   {
/*  98 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setProbs(int to, double d)
/*     */   {
/* 103 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double sum()
/*     */   {
/* 108 */     return 1.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoC1) {}
/*     */   
/*     */ 
/*     */   public void validate() {}
/*     */   
/*     */ 
/*     */   public Integer fixedInteger()
/*     */   {
/* 121 */     return this.i;
/*     */   }
/*     */   
/* 124 */   public void swtchAlleles(int[] switchTranslation) { this.i = Integer.valueOf(switchTranslation[this.i.intValue()]); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(SkewNormal[] probR, double d) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(IlluminaProbB[] probB, Integer index, double d) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public double totalCount()
/*     */   {
/* 140 */     return this.cnt;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/IntegerDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */