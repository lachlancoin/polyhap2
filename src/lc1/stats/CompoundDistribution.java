/*     */ package lc1.stats;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.illumina.IlluminaProbR;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CompoundDistribution
/*     */   extends PseudoDistribution
/*     */ {
/*  16 */   List<PseudoDistribution> l = new ArrayList(2);
/*     */   
/*     */   public CompoundDistribution(PseudoDistribution dist1, PseudoDistribution dist2) {
/*  19 */     this.data_index = -1;
/*  20 */     if (((dist1 instanceof IlluminaDistribution)) && ((dist2 instanceof IlluminaDistribution))) {
/*  21 */       Double b1 = ((IlluminaDistribution)dist1).b;
/*  22 */       Double localDouble1 = ((IlluminaDistribution)dist2).b;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  28 */     this.l.add(dist1);
/*  29 */     this.l.add(dist2);
/*     */   }
/*     */   
/*  32 */   public CompoundDistribution(CompoundDistribution cd) { for (int i = 0; i < cd.l.size(); i++)
/*  33 */       this.l.add(((PseudoDistribution)cd.l.get(i)).clone());
/*     */   }
/*     */   
/*     */   public void addCounts(ProbabilityDistribution probabilityDistribution) {
/*  37 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  40 */   public void setParamsAsAverageOf(ProbabilityDistribution[] tmp) { throw new RuntimeException("!!"); }
/*     */   
/*     */   public String getUnderlyingData(EmissionStateSpace emStSp) {
/*  43 */     StringBuffer sb = new StringBuffer();
/*  44 */     for (int i = 0; i < this.l.size(); i++) {
/*  45 */       sb.append(((PseudoDistribution)this.l.get(i)).getUnderlyingData(emStSp) + ";");
/*     */     }
/*  47 */     return sb.toString();
/*     */   }
/*     */   
/*  50 */   public void applyCorrection(double parseDouble) { for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();)
/*  51 */       ((PseudoDistribution)it.next()).applyCorrection(parseDouble);
/*     */   }
/*     */   
/*     */   public String getPrintString() {
/*  55 */     return "";
/*     */   }
/*     */   
/*     */   public double score(int j, IlluminaProbB probB)
/*     */   {
/*  60 */     double sc = 1.0D;
/*  61 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/*  62 */       sc *= ((PseudoDistribution)it.next()).score(j, probB);
/*     */     }
/*  64 */     return sc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double score(int j, IlluminaProbB[] probB)
/*     */   {
/*  71 */     double sc = 1.0D;
/*  72 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/*  73 */       sc *= ((PseudoDistribution)it.next()).score(j, probB);
/*     */     }
/*  75 */     return sc;
/*     */   }
/*     */   
/*     */   public double score(SkewNormal[] probR) {
/*  79 */     double sc = 1.0D;
/*  80 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/*  81 */       sc *= ((PseudoDistribution)it.next()).score(probR);
/*     */     }
/*  83 */     return sc;
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, double value) {
/*  87 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/*  88 */       ((PseudoDistribution)it.next()).addCount(obj_index, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCount(IlluminaProbB[] probB, Integer index, double val)
/*     */   {
/*  95 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/*  96 */       ((PseudoDistribution)it.next()).addCount(probB, index, val);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCount(SkewNormal[] probR, double val) {
/* 101 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/* 102 */       ((PseudoDistribution)it.next()).addCount(probR, val);
/*     */     }
/*     */   }
/*     */   
/*     */   public double[] calcDistribution(IlluminaProbB probB, double[] distribution, EmissionStateSpace emStSp, int nocop)
/*     */   {
/* 108 */     double sum = 0.0D;
/* 109 */     Arrays.fill(distribution, 0.0D);
/* 110 */     for (int j = 0; j < distribution.length; j++) {
/* 111 */       if (emStSp.getCN(j) != nocop) {
/* 112 */         distribution[j] = 0.0D;
/*     */       }
/*     */       else {
/* 115 */         distribution[j] = (score(j, probB) * emStSp.getWeight(j));
/*     */       }
/* 117 */       sum += distribution[j];
/*     */     }
/* 119 */     for (int i = 0; i < distribution.length; i++) {
/* 120 */       distribution[i] /= sum;
/*     */     }
/* 122 */     return distribution;
/*     */   }
/*     */   
/*     */   public double[] calcDistribution(IlluminaProbR probR, IlluminaProbB probB, double[] distribution, EmissionStateSpace emStSp)
/*     */   {
/* 127 */     throw new RuntimeException("!!");
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
/*     */   public PseudoDistribution clone()
/*     */   {
/* 147 */     return new CompoundDistribution(this);
/*     */   }
/*     */   
/*     */   public PseudoDistribution clone(double swtch)
/*     */   {
/* 152 */     return clone();
/*     */   }
/*     */   
/*     */   public double[] counts()
/*     */   {
/* 157 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double evaluate(double[] ds)
/*     */   {
/* 162 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public Integer fixedInteger()
/*     */   {
/* 167 */     return ((PseudoDistribution)this.l.get(0)).fixedInteger();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getMax()
/*     */   {
/* 174 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void initialise()
/*     */   {
/* 179 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/* 180 */       ((PseudoDistribution)it.next()).initialise();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] probs()
/*     */   {
/* 187 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double probs(int obj_i)
/*     */   {
/* 192 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double sample()
/*     */   {
/* 197 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setCounts(int i1, double cnt)
/*     */   {
/* 202 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void setProb(double[] prob)
/*     */   {
/* 208 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void setProbs(int to, double d)
/*     */   {
/* 214 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public double sum()
/*     */   {
/* 220 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void swtchAlleles(int[] switchTranslation)
/*     */   {
/* 225 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/* 226 */       ((PseudoDistribution)it.next()).swtchAlleles(switchTranslation);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoC1)
/*     */   {
/* 234 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/* 235 */       ((PseudoDistribution)it.next()).transfer(pseudoC1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate()
/*     */   {
/* 242 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/* 243 */       ((PseudoDistribution)it.next()).validate();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFixedIndex(int k)
/*     */   {
/* 251 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double totalCount()
/*     */   {
/* 256 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/* 259 */   public void addDist(PseudoDistribution pseudoDistribution) { this.l.add(pseudoDistribution); }
/*     */   
/*     */ 
/*     */ 
/* 263 */   public short getDataIndex() { return ((PseudoDistribution)this.l.get(0)).getDataIndex(); }
/*     */   
/*     */   public PseudoDistribution getForIndex(short index) {
/* 266 */     for (Iterator<PseudoDistribution> it = this.l.iterator(); it.hasNext();) {
/* 267 */       PseudoDistribution dist = (PseudoDistribution)it.next();
/* 268 */       if (dist.getDataIndex() == index) return dist;
/*     */     }
/* 270 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/CompoundDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */