/*    */ package lc1.dp.transition;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ import lc1.stats.Dirichlet;
/*    */ import lc1.stats.Sampler;
/*    */ import lc1.stats.SimpleExtendedDistribution;
/*    */ import lc1.stats.SimpleExtendedDistribution1;
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ public class SimpleExpTransProb implements ExpTransProb
/*    */ {
/*    */   SimpleExtendedDistribution exp_rd1;
/*    */   
/*    */   public String info()
/*    */   {
/* 17 */     StringBuffer sb = new StringBuffer(getClass().toString());
/* 18 */     return sb.toString();
/*    */   }
/*    */   
/* 21 */   public ExpTransProb clone(boolean swtch, int noStates) { if (swtch) {
/* 22 */       return new MultiExpProbs(new Dirichlet(this.exp_rd1.probs, Constants.switchU()), noStates);
/*    */     }
/* 24 */     return new SimpleExpTransProb(new Dirichlet(this.exp_rd1.probs, Double.POSITIVE_INFINITY), noStates);
/*    */   }
/*    */   
/* 27 */   public SimpleExpTransProb(Sampler samplerFirst, int len) { this.exp_rd1 = new SimpleExtendedDistribution1(samplerFirst); }
/*    */   
/*    */ 
/*    */   public ExpTransProb clone(int[] statesToGroup)
/*    */   {
/* 32 */     return new MultiExpProbs(this, statesToGroup);
/*    */   }
/*    */   
/*    */ 
/*    */   public void printExp(PrintWriter pw, double dist, String pref)
/*    */   {
/* 38 */     pw.print(pref + " ");
/*    */     
/*    */ 
/* 41 */     this.exp_rd1.printSimple(pw, "", "", 0.3D);
/*    */     
/* 43 */     pw.print("; ");
/*    */   }
/*    */   
/* 46 */   public SimpleExpTransProb(ExpTransProb exp) { this.exp_rd1 = new SimpleExtendedDistribution(this.exp_rd1); }
/*    */   
/*    */   public void initialiseExpRd() {
/* 49 */     this.exp_rd1.initialise();
/*    */   }
/*    */   
/* 52 */   public void transferExp(double[] pseudoExp) { this.exp_rd1.transfer(pseudoExp); }
/*    */   
/*    */   public void transferExp(double pseudoExp) {
/* 55 */     this.exp_rd1.transfer(pseudoExp);
/*    */   }
/*    */   
/* 58 */   public void validateExp() { this.exp_rd1.validate(); }
/*    */   
/*    */   public Collection getExpRdColl()
/*    */   {
/* 62 */     return (Collection)java.util.Arrays.asList(new SimpleExtendedDistribution[] { this.exp_rd1 });
/*    */   }
/*    */   
/*    */   public double evaluateExpRd(double[] pseudoC)
/*    */   {
/* 67 */     return this.exp_rd1.evaluate(pseudoC);
/*    */   }
/*    */   
/* 70 */   public SimpleExtendedDistribution getExp(int groupFrom) { return this.exp_rd1; }
/*    */   
/*    */   public int noStates()
/*    */   {
/* 74 */     return this.exp_rd1.probs.length;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/SimpleExpTransProb.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */