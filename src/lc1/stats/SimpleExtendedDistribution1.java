/*    */ package lc1.stats;
/*    */ 
/*    */ import com.braju.format.Format;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class SimpleExtendedDistribution1
/*    */   extends SimpleExtendedDistribution
/*    */ {
/*    */   private final double[] pseudo;
/*    */   
/*    */   public SimpleExtendedDistribution1(Sampler dir)
/*    */   {
/* 13 */     super(dir);
/* 14 */     this.pseudo = new double[dir.dist.length];
/* 15 */     arraycopy(dir.dist, 0, this.pseudo, 0, dir.dist.length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void swtchAlleles(int[] switchTranslation)
/*    */   {
/* 23 */     swtch(this.pseudo, switchTranslation);
/*    */   }
/*    */   
/* 26 */   public PseudoDistribution clone(double swtch) { return new SimpleExtendedDistribution1(this.probs, swtch); }
/*    */   
/*    */ 
/* 29 */   public PseudoDistribution clone() { return new SimpleExtendedDistribution1(this); }
/*    */   
/*    */   public SimpleExtendedDistribution1(int len) {
/* 32 */     super(len);
/* 33 */     this.pseudo = new double[len];
/* 34 */     Arrays.fill(this.pseudo, 1.0D / this.pseudo.length);
/*    */   }
/*    */   
/* 37 */   public SimpleExtendedDistribution1(double[] init, double u) { super(init, u);
/* 38 */     this.pseudo = new double[init.length];
/* 39 */     System.arraycopy(init, 0, this.pseudo, 0, init.length);
/*    */   }
/*    */   
/*    */ 
/*    */   public SimpleExtendedDistribution1(SimpleExtendedDistribution1 exp_rd)
/*    */   {
/* 45 */     super(exp_rd);
/* 46 */     this.pseudo = new double[exp_rd.pseudo.length];
/* 47 */     System.arraycopy(exp_rd.pseudo, 0, this.pseudo, 0, this.pseudo.length);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void transfer(double[] pseudo)
/*    */   {
/* 54 */     double sum = 0.0D;
/* 55 */     for (int i = 0; i < this.probs.length; i++) {
/* 56 */       double total = this.counts[i] + pseudo[i];
/* 57 */       this.probs[i] = total;
/* 58 */       sum += total;
/*    */     }
/* 60 */     if (sum > 1.0E-9D) {
/* 61 */       for (int i = 0; i < this.probs.length; i++) {
/* 62 */         this.probs[i] /= sum;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void transfer(double pseudo)
/*    */   {
/* 72 */     double sum = 0.0D;
/*    */     
/* 74 */     for (int i = 0; i < this.probs.length; i++) {
/* 75 */       this.counts[i] += pseudo * this.pseudo[i];
/* 76 */       sum += this.counts[i];
/*    */     }
/*    */     
/* 79 */     if (sum > 0.0D) {
/* 80 */       for (int i = 0; i < this.probs.length; i++) {
/* 81 */         this.probs[i] = (this.counts[i] / sum);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String print(double[] d)
/*    */   {
/* 91 */     StringBuffer sb = new StringBuffer();
/* 92 */     Double[] d1 = new Double[d.length];
/* 93 */     for (int i = 0; i < d.length; i++) {
/* 94 */       sb.append("%5.3g ");
/* 95 */       d1[i] = Double.valueOf(d[i]);
/*    */     }
/* 97 */     return Format.sprintf(sb.toString(), d1);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/SimpleExtendedDistribution1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */