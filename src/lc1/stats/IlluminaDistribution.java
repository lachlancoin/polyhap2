/*     */ package lc1.stats;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.illumina.IlluminaProbR;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class IlluminaDistribution extends PseudoDistribution
/*     */ {
/*  11 */   Double r = null;
/*  12 */   Double b = null;
/*     */   
/*     */   public IlluminaDistribution(short data_index) {
/*  15 */     this.data_index = data_index;
/*     */   }
/*     */   
/*     */   public String getUnderlyingData(EmissionStateSpace emStSp) {
/*  19 */     return getUnderlyingData();
/*     */   }
/*     */   
/*  22 */   public String getPrintString() { return ""; }
/*     */   
/*     */ 
/*  25 */   public void addCounts(ProbabilityDistribution probabilityDistribution) { throw new RuntimeException("!!"); }
/*     */   
/*     */   public IlluminaDistribution(IlluminaDistribution ill) {
/*  28 */     this.r = ill.r;
/*  29 */     this.b = ill.b;
/*     */   }
/*     */   
/*     */   public void setParamsAsAverageOf(ProbabilityDistribution[] tmp) {
/*  33 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void applyCorrection(double parseDouble) {
/*  37 */     if (this.r != null) this.r = Double.valueOf(this.r.doubleValue() + parseDouble);
/*     */   }
/*     */   
/*     */   public String getUnderlyingData()
/*     */   {
/*  42 */     String r1 = Math.round(this.r.doubleValue() * 100.0D) / 100.0D;
/*  43 */     String b1 = Math.round(this.b.doubleValue() * 100.0D) / 100.0D;
/*  44 */     return r1 + "_" + b1;
/*     */   }
/*     */   
/*     */   public double score(int j, IlluminaProbB[] probB) {
/*  48 */     return this.b == null ? 1.0D : probB[this.data_index].calcB(j, this.b.doubleValue());
/*     */   }
/*     */   
/*     */   public double score(int j, IlluminaProbB probBState)
/*     */   {
/*  53 */     return this.b == null ? 1.0D : probBState.calcB(j, this.b.doubleValue());
/*     */   }
/*     */   
/*     */   public double score(SkewNormal[] probR) {
/*  57 */     return this.r == null ? 1.0D : probR[this.data_index].probability(this.r.doubleValue());
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, double value) {
/*  61 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCount(IlluminaProbB[] probB, Integer index, double val)
/*     */   {
/*  67 */     if (this.b != null) probB[this.data_index].addBCount(index.intValue(), val, this.b.doubleValue());
/*     */   }
/*     */   
/*     */   public void addCount(SkewNormal[] probR, double val) {
/*  71 */     if (this.r != null)
/*     */     {
/*     */ 
/*  74 */       probR[this.data_index].addCount(this.r.doubleValue(), val);
/*     */     }
/*     */   }
/*     */   
/*     */   public Double r()
/*     */   {
/*  80 */     return this.r;
/*     */   }
/*     */   
/*     */   public Double b() {
/*  84 */     return this.b;
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] calcDistribution(IlluminaProbB probB, double[] distribution, EmissionStateSpace emStSp, int nocop)
/*     */   {
/*  90 */     double sum = 0.0D;
/*  91 */     Arrays.fill(distribution, 0.0D);
/*  92 */     for (int j = 0; j < distribution.length; j++) {
/*  93 */       if (emStSp.getCN(j) != nocop) {
/*  94 */         distribution[j] = 0.0D;
/*     */       }
/*     */       else {
/*  97 */         distribution[j] = (score(j, probB) * emStSp.getWeight(j));
/*     */       }
/*  99 */       sum += distribution[j];
/*     */     }
/* 101 */     for (int i = 0; i < distribution.length; i++) {
/* 102 */       distribution[i] /= sum;
/*     */     }
/* 104 */     return distribution;
/*     */   }
/*     */   
/*     */   public double[] calcDistribution(IlluminaProbR probR, IlluminaProbB probB, double[] distribution, EmissionStateSpace emStSp)
/*     */   {
/* 109 */     double sum = 0.0D;
/* 110 */     Arrays.fill(distribution, 0.0D);
/* 111 */     for (int j = 0; j < distribution.length; j++)
/*     */     {
/*     */ 
/* 114 */       distribution[j] = (score(j, probB, probR, emStSp) * emStSp.getWeight(j));
/* 115 */       sum += distribution[j];
/*     */     }
/* 117 */     for (int i = 0; i < distribution.length; i++) {
/* 118 */       distribution[i] /= sum;
/*     */     }
/* 120 */     return distribution;
/*     */   }
/*     */   
/* 123 */   private double score(int j, IlluminaProbB probB, IlluminaProbR probR, EmissionStateSpace emStsp) { if ((this.r == null) && (this.b == null)) { return emStsp.invSize();
/*     */     }
/* 125 */     double sc1 = this.r == null ? emStsp.invCNVSize() : 
/* 126 */       probR.calcR(Constants.backgroundCount(), emStsp.getCN(j), this.r.doubleValue());
/* 127 */     double sc2 = this.b == null ? emStsp.bSpaceSize(j) : 
/* 128 */       probB.calcB(j, this.b.doubleValue());
/* 129 */     return sc1 * sc2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PseudoDistribution clone()
/*     */   {
/* 137 */     return new IlluminaDistribution(this);
/*     */   }
/*     */   
/*     */   public PseudoDistribution clone(double swtch)
/*     */   {
/* 142 */     return clone();
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] counts()
/*     */   {
/* 148 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public double evaluate(double[] ds)
/*     */   {
/* 154 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer fixedInteger()
/*     */   {
/* 160 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMax()
/*     */   {
/* 168 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialise() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public double[] probs()
/*     */   {
/* 180 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public double probs(int obj_i)
/*     */   {
/* 186 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public double sample()
/*     */   {
/* 192 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCounts(int i1, double cnt) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProb(double[] prob) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProbs(int to, double d) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double sum()
/*     */   {
/* 216 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public void swtchAlleles(int[] switchTranslation)
/*     */   {
/* 221 */     this.b = Double.valueOf(1.0D - this.b.doubleValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoC1) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setB(double b2)
/*     */   {
/* 239 */     if ((Double.isNaN(b2)) || (Constants.suppressB())) {
/* 240 */       this.b = null;
/*     */     }
/*     */     else {
/* 243 */       this.b = Double.valueOf(b2);
/* 244 */       if ((this.b.doubleValue() < 0.0D) || (this.b.doubleValue() > 1.0D)) {
/* 245 */         throw new RuntimeException("!!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setR(double r2)
/*     */   {
/* 252 */     if ((Double.isNaN(r2)) || (Constants.suppressR())) {
/* 253 */       this.r = null;
/*     */     }
/*     */     else {
/* 256 */       this.r = Double.valueOf(r2);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setFixedIndex(int k) {
/* 261 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double totalCount()
/*     */   {
/* 266 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/IlluminaDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */