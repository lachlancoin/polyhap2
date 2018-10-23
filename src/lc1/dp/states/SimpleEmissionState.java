/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.SimpleDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ 
/*     */ 
/*     */ public class SimpleEmissionState
/*     */   extends EmissionState
/*     */ {
/*     */   protected PseudoDistribution emissions;
/*     */   
/*     */   public SimpleEmissionState(String name, int adv, PseudoDistribution em)
/*     */   {
/*  19 */     super(name, adv);
/*  20 */     this.emissions = new SimpleExtendedDistribution(em);
/*     */   }
/*     */   
/*  23 */   protected Collection getStateSpace() { throw new RuntimeException("!!"); }
/*     */   
/*     */   public void addCount(int element, double value, int i)
/*     */   {
/*  27 */     this.emissions.addCount(element, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCountDT(double element, int phen_index, double value, int i) {}
/*     */   
/*     */   public SimpleEmissionState(SimpleEmissionState st1)
/*     */   {
/*  35 */     this(st1.name, st1.adv, st1.emissions);
/*     */   }
/*     */   
/*     */   public void reverse() {}
/*     */   
/*     */   public SimpleEmissionState(String string, int adv)
/*     */   {
/*  42 */     super(string, adv);
/*     */   }
/*     */   
/*  45 */   public void initialiseCounts() { this.emissions.initialise(); }
/*     */   
/*     */ 
/*     */ 
/*     */   public int sample(int i)
/*     */   {
/*  51 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  54 */   public double score(int obj, int i) { return this.emissions.probs(obj); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRandom(double u, boolean restart)
/*     */   {
/*  63 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  66 */   public String getEmissionString() { return this.emissions; }
/*     */   
/*     */   public void transfer() {
/*  69 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void validate()
/*     */   {
/*  74 */     if (Math.abs(this.emissions.sum() - 1.0D) > 0.001D) throw new RuntimeException("!!");
/*  75 */     this.lengthDistrib.validate();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/*  80 */     return new SimpleEmissionState(this);
/*     */   }
/*     */   
/*     */   public int noSnps() {
/*  84 */     return 0;
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String st, List<Integer> columns) {
/*  88 */     throw new RuntimeException("");
/*     */   }
/*     */   
/*     */ 
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/*  94 */     return null;
/*     */   }
/*     */   
/*     */   public double[] getEmiss(int i)
/*     */   {
/*  99 */     return null;
/*     */   }
/*     */   
/*     */   public int mostLikely(int pos)
/*     */   {
/* 104 */     return -1;
/*     */   }
/*     */   
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/* 109 */     return this.emissions.fixedInteger();
/*     */   }
/*     */   
/*     */   public boolean transferCountsToProbs(double pseudo)
/*     */   {
/* 114 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public int getParamIndex()
/*     */   {
/* 119 */     return 1;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/SimpleEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */