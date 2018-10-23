/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ 
/*     */ public class SiteEmissionState
/*     */   extends EmissionState
/*     */ {
/*     */   public double[] emissions;
/*     */   final int noSnps;
/*     */   boolean logspace;
/*     */   
/*     */   public boolean transferCountsToProbs(double pseudo)
/*     */   {
/*  16 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public SiteEmissionState(String name, double[] logprob, int[] alias, boolean logspace)
/*     */   {
/*  21 */     super(name, 1);
/*  22 */     this.noSnps = alias.length;
/*  23 */     this.emissions = new double[alias.length];
/*  24 */     for (int i = 0; i < this.noSnps; i++) {
/*  25 */       this.emissions[i] = (logspace ? logprob[alias[i]] : Math.exp(logprob[alias[i]]));
/*  26 */       if (this.emissions[i] < 1.0E-300D) throw new RuntimeException("danger of underflow");
/*     */     }
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String prefix)
/*     */   {
/*  32 */     StringBuffer sb1 = new StringBuffer(prefix);
/*  33 */     for (int i = 0; i < this.noSnps; i++) {
/*  34 */       sb1.append("%8.2g ");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  45 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, double value, int i)
/*     */   {
/*  50 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  53 */   public void addCountDT(double data_index, int phen_index, double value, int i) { throw new RuntimeException("!!"); }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/*  57 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int sample(int i)
/*     */   {
/*  64 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void reverse() {}
/*     */   
/*     */   public double score(int obj_index, int i)
/*     */   {
/*  71 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setRandom(double emiss, boolean restart) {
/*  75 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString(int i)
/*     */   {
/*  83 */     return getName();
/*     */   }
/*     */   
/*     */   public void validate()
/*     */     throws Exception
/*     */   {
/*  89 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int noSnps()
/*     */   {
/* 101 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/* 108 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, String st, List<Integer> columns) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] getEmiss(int i)
/*     */   {
/* 122 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/* 129 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int mostLikely(int pos)
/*     */   {
/* 139 */     return -1;
/*     */   }
/*     */   
/*     */   public int getParamIndex() {
/* 143 */     return 1;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/SiteEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */