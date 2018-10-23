/*    */ package lc1.dp.profile;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import lc1.dp.SimpleEmissionState;
/*    */ import lc1.dp.State;
/*    */ import lc1.dp.genotype.SimpleExtendedDistribution;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProfileEmissionState
/*    */   extends SimpleEmissionState
/*    */ {
/*    */   public final boolean MATCH;
/*    */   static final double epsilon = 1.0E-30D;
/*    */   static final double thresh = 0.1D;
/*    */   
/*    */   public void setDistribution(double[] freq)
/*    */   {
/* 19 */     this.emissions = new SimpleExtendedDistribution(freq, Double.POSITIVE_INFINITY);
/*    */   }
/*    */   
/*    */   public double[] getDistribution()
/*    */   {
/* 24 */     return this.emissions.probs;
/*    */   }
/*    */   
/*    */ 
/*    */   public ProfileEmissionState(String name, short num, int adv, boolean MATCH)
/*    */   {
/* 30 */     super(name + "_" + num, adv);
/* 31 */     this.MATCH = MATCH;
/*    */   }
/*    */   
/*    */ 
/*    */   public String extendedString()
/*    */   {
/* 37 */     StringBuffer sb = new StringBuffer(this.name + "\n");
/* 38 */     return sb.toString();
/*    */   }
/*    */   
/*    */   protected ProfileEmissionState(String name, short num, double[] freq, int adv, boolean MATCH) {
/* 42 */     this(name, num, adv, MATCH);
/* 43 */     setDistribution(freq);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 77 */     return this.name;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void print(PrintWriter pw, String st)
/*    */   {
/* 85 */     this.emissions.print(pw);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object clone()
/*    */   {
/* 93 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */ 
/*    */   public Object clone(State pseudo)
/*    */   {
/* 99 */     throw new RuntimeException("!!");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/profile/ProfileEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */