/*    */ package lc1.dp;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ import lc1.dp.genotype.SimpleExtendedDistribution;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleEmissionState
/*    */   extends EmissionState
/*    */ {
/*    */   protected SimpleExtendedDistribution emissions;
/*    */   
/*    */   public SimpleEmissionState(String name, int adv, SimpleExtendedDistribution em)
/*    */   {
/* 16 */     super(name, adv);
/* 17 */     this.emissions = new SimpleExtendedDistribution(em, em);
/*    */   }
/*    */   
/* 20 */   protected Collection getStateSpace() { throw new RuntimeException("!!"); }
/*    */   
/*    */   public void addCount(int element, Double value, int i)
/*    */   {
/* 24 */     this.emissions.counts[element] += value.doubleValue();
/*    */   }
/*    */   
/*    */   public SimpleEmissionState(SimpleEmissionState st1) {
/* 28 */     this(st1.name, st1.adv, st1.emissions);
/*    */   }
/*    */   
/*    */   public SimpleEmissionState(String string, int adv)
/*    */   {
/* 33 */     super(string, adv);
/*    */   }
/*    */   
/* 36 */   public void initialiseCounts() { this.emissions.initialise(); }
/*    */   
/*    */   public double KLDistance(EmissionState st)
/*    */   {
/* 40 */     return this.emissions.KLDistance(((SimpleEmissionState)st).emissions);
/*    */   }
/*    */   
/*    */   public Object sample(int i)
/*    */   {
/* 45 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/* 48 */   public double score(int obj, int i) { return this.emissions.probs[obj]; }
/*    */   
/*    */ 
/*    */   public Object mostLikely(int pos)
/*    */   {
/* 53 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */ 
/*    */   public void setRandom(double u, boolean restart)
/*    */   {
/* 59 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/* 62 */   public String getEmissionString() { return this.emissions; }
/*    */   
/*    */   public void transfer() {
/* 65 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public void validate()
/*    */   {
/* 70 */     if (Math.abs(this.emissions.sum() - 1.0D) > 0.001D) throw new RuntimeException("!!");
/* 71 */     this.lengthDistrib.validate();
/*    */   }
/*    */   
/*    */   public void print(PrintWriter pw, String st) {
/* 75 */     pw.println(st + " " + getName());
/*    */   }
/*    */   
/*    */   public Object clone(State pseudo)
/*    */   {
/* 80 */     return new SimpleEmissionState(this);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/SimpleEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */