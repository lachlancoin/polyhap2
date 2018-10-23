/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpaceTranslation;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.stats.SkewNormal;
/*     */ 
/*     */ 
/*     */ public class WrappedEmissionState
/*     */   extends EmissionState
/*     */ {
/*     */   EmissionState inner;
/*     */   final EmissionStateSpace emStSp;
/*     */   final EmissionStateSpaceTranslation trans;
/*     */   
/*     */   public WrappedEmissionState(EmissionState inner, EmissionStateSpace target, EmissionStateSpaceTranslation trans)
/*     */   {
/*  20 */     super(inner.name, inner.adv);
/*  21 */     this.trans = trans;
/*  22 */     this.inner = inner;
/*  23 */     this.emStSp = target;
/*     */   }
/*     */   
/*     */   public int noSnps() {
/*  27 */     return this.inner.noSnps();
/*     */   }
/*     */   
/*     */   public SkewNormal[] probR() {
/*  31 */     return this.inner.probR();
/*     */   }
/*     */   
/*     */   public IlluminaProbB[] probB()
/*     */   {
/*  36 */     return this.inner.probB();
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
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/*  56 */     return this.emStSp;
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, double value, int i) {
/*  60 */     this.inner.addCount(this.trans.bigToSmall(obj_index).intValue(), value, i);
/*     */   }
/*     */   
/*     */   public void addCountDT(double data_index, int phen_index, double value, int i)
/*     */   {
/*  65 */     this.inner.addCountDT(data_index, phen_index, value, i);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getUnderlyingData(int i)
/*     */   {
/*  71 */     return this.inner.getUnderlyingData(i);
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String st, List<Integer> columns) {
/*  75 */     this.inner.print(pw, st, columns);
/*     */   }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/*  80 */     this.inner.initialiseCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   public int sample(int i)
/*     */   {
/*  86 */     throw new RuntimeException("");
/*     */   }
/*     */   
/*     */   public double score(int obj_index, int i) {
/*  90 */     Integer sm = this.trans.bigToSmall(obj_index);
/*  91 */     if (sm == null) return 0.0D;
/*  92 */     return this.inner.score(sm.intValue(), i);
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] getEmiss(int i)
/*     */   {
/*  98 */     double[] d = new double[this.emStSp.size()];
/*  99 */     for (int j = 0; j < d.length; j++) {
/* 100 */       d[j] = score(j, i);
/*     */     }
/* 102 */     return d;
/*     */   }
/*     */   
/*     */   public int mostLikely(int pos)
/*     */   {
/* 107 */     return this.trans.smallToBig(pos)[0];
/*     */   }
/*     */   
/*     */   public boolean transferCountsToProbs(double pseudo)
/*     */   {
/* 112 */     return this.inner.transferCountsToProbs(pseudo);
/*     */   }
/*     */   
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/* 117 */     Integer i1 = this.inner.getFixedInteger(i);
/* 118 */     if (i1 == null) return null;
/* 119 */     return Integer.valueOf(this.trans.smallToBig(i1.intValue())[0]);
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 124 */     return new WrappedEmissionState((EmissionState)this.inner.clone(), this.emStSp, this.trans);
/*     */   }
/*     */   
/*     */   public void validate() throws Exception {
/* 128 */     this.inner.validate();
/*     */   }
/*     */   
/*     */   public void reverse()
/*     */   {
/* 133 */     this.inner.reverse();
/*     */   }
/*     */   
/*     */   public int getParamIndex() {
/* 137 */     return this.inner.getParamIndex();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/WrappedEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */