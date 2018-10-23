/*    */ package lc1.dp.transition;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import lc1.stats.Dirichlet;
/*    */ import lc1.stats.Sampler;
/*    */ import lc1.stats.SimpleExtendedDistribution;
/*    */ import lc1.stats.SimpleExtendedDistribution1;
/*    */ 
/*    */ public class MultiExpProbs implements ExpTransProb
/*    */ {
/*    */   private SimpleExtendedDistribution1[] exp_rd;
/*    */   
/*    */   public MultiExpProbs(Sampler dir, int len)
/*    */   {
/* 17 */     this.exp_rd = new SimpleExtendedDistribution1[len];
/* 18 */     for (int i = 0; i < this.exp_rd.length; i++) {
/* 19 */       this.exp_rd[i] = new SimpleExtendedDistribution1(dir);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public MultiExpProbs(MultiExpProbs probs)
/*    */   {
/* 26 */     int len = probs.exp_rd.length;
/* 27 */     this.exp_rd = new SimpleExtendedDistribution1[len];
/* 28 */     for (int i = 0; i < this.exp_rd.length; i++) {
/* 29 */       Dirichlet dir = new Dirichlet(probs.getExp(i).probs, Double.POSITIVE_INFINITY);
/* 30 */       this.exp_rd[i] = new SimpleExtendedDistribution1(dir);
/*    */     }
/*    */   }
/*    */   
/*    */   public MultiExpProbs(ExpTransProb probs, int[] statesToGroup)
/*    */   {
/* 36 */     this.exp_rd = new SimpleExtendedDistribution1[statesToGroup.length];
/* 37 */     for (int i = 0; i < this.exp_rd.length; i++)
/* 38 */       this.exp_rd[i] = ((SimpleExtendedDistribution1)probs.getExp(statesToGroup[i]).clone());
/*    */   }
/*    */   
/*    */   public ExpTransProb clone(int[] statesToGroup) {
/* 42 */     return new MultiExpProbs(this, statesToGroup);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 47 */   public ExpTransProb clone(boolean swtch, int noStates) { return new MultiExpProbs(this); }
/*    */   
/*    */   public double evaluateExpRd(double[] pseudoC) {
/* 50 */     double sum = 0.0D;
/* 51 */     for (int i = 0; i < this.exp_rd.length;) {
/* 52 */       return sum += this.exp_rd[i].evaluate(pseudoC);
/*    */     }
/* 54 */     return sum;
/*    */   }
/*    */   
/* 57 */   public SimpleExtendedDistribution1 getExp(int groupFrom) { return this.exp_rd[groupFrom]; }
/*    */   
/*    */ 
/* 60 */   public Collection getExpRdColl() { return (Collection)Arrays.asList(this.exp_rd); }
/*    */   
/*    */   public void initialiseExpRd() {
/* 63 */     for (int i = 0; i < this.exp_rd.length; i++)
/* 64 */       this.exp_rd[i].initialise();
/*    */   }
/*    */   
/*    */   public void transferExp(double[] pseudoExp) {
/* 68 */     for (int i = 0; i < this.exp_rd.length; i++)
/* 69 */       this.exp_rd[i].transfer(pseudoExp);
/*    */   }
/*    */   
/*    */   public void transferExp(double pseudoExp) {
/* 73 */     for (int i = 0; i < this.exp_rd.length; i++)
/* 74 */       this.exp_rd[i].transfer(pseudoExp);
/*    */   }
/*    */   
/*    */   public void validateExp() {
/* 78 */     for (int i = 0; i < this.exp_rd.length; i++) {
/* 79 */       this.exp_rd[i].validate();
/*    */     }
/*    */   }
/*    */   
/*    */   public void printExp(PrintWriter pw, double dist, String pref) {
/* 84 */     pw.print(pref + " ");
/* 85 */     for (int i = 0; i < this.exp_rd.length; i++) {
/* 86 */       pw.print(i + " : ");
/*    */       
/*    */ 
/* 89 */       this.exp_rd[i].printSimple(pw, "", "", 0.3D);
/*    */       
/* 91 */       pw.println("; ");
/*    */     }
/*    */   }
/*    */   
/* 95 */   public int noStates() { return this.exp_rd[0].probs.length; }
/*    */   
/*    */   public String info() {
/* 98 */     StringBuffer sb = new StringBuffer(getClass().toString());
/* 99 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/MultiExpProbs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */