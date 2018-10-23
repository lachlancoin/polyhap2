/*    */ package lc1.dp;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import lc1.dp.genotype.io.scorable.StateIndices;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SiteEmissionState
/*    */   extends EmissionState
/*    */ {
/*    */   public double[] emissions;
/*    */   final int noSnps;
/*    */   boolean logspace;
/*    */   
/*    */   SiteEmissionState(String name, double[] logprob, int[] alias, boolean logspace)
/*    */   {
/* 17 */     super(name, 1);
/* 18 */     this.noSnps = alias.length;
/* 19 */     this.emissions = new double[alias.length];
/* 20 */     for (int i = 0; i < this.noSnps; i++) {
/* 21 */       this.emissions[i] = (logspace ? logprob[alias[i]] : Math.exp(logprob[alias[i]]));
/* 22 */       if (this.emissions[i] < 1.0E-300D) throw new RuntimeException("danger of underflow");
/*    */     }
/*    */   }
/*    */   
/*    */   public void print(PrintWriter pw, String prefix)
/*    */   {
/* 28 */     StringBuffer sb1 = new StringBuffer(prefix);
/* 29 */     for (int i = 0; i < this.noSnps; i++) {
/* 30 */       sb1.append("%8.2g ");
/*    */     }
/*    */   }
/*    */   
/*    */   public double[] score(StateIndices obj, boolean logspace)
/*    */   {
/* 36 */     if (logspace != this.logspace) throw new RuntimeException("mismatch");
/* 37 */     return this.emissions;
/*    */   }
/*    */   
/*    */   public Object clone() {
/* 41 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public void addCount(int obj_index, Double value, int i)
/*    */   {
/* 46 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public void initialiseCounts() {
/* 50 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public double KLDistance(EmissionState st) {
/* 54 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public Object sample(int i)
/*    */   {
/* 59 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public double score(int obj_index, int i)
/*    */   {
/* 64 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public void setRandom(double emiss, boolean restart) {
/* 68 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString(int i)
/*    */   {
/* 76 */     return getName();
/*    */   }
/*    */   
/*    */   public void validate()
/*    */     throws Exception
/*    */   {
/* 82 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public Object mostLikely(int pos)
/*    */   {
/* 87 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */ 
/*    */   public Object clone(State pseudo)
/*    */   {
/* 93 */     throw new RuntimeException("!!");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/SiteEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */