/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import lc1.dp.genotype.SimpleExtendedDistribution;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ 
/*     */ 
/*     */ public class ExponentialTransitionProbs
/*     */   implements AbstractTransitionProbs
/*     */ {
/*     */   public SimpleExtendedDistribution exp_rd;
/*     */   SimpleExtendedDistribution alpha;
/*     */   
/*     */   public void addCounts(StateDistribution[] observed)
/*     */   {
/*  19 */     SimpleExtendedDistribution jump = this.alpha;
/*  20 */     int no_states = observed.length;
/*  21 */     SimpleExtendedDistribution exp_count = this.exp_rd;
/*  22 */     for (int j = 0; j < observed.length; j++) {
/*  23 */       StateDistribution dist1 = observed[j];
/*  24 */       int st = j;
/*     */       
/*  26 */       if (dist1 != null) {
/*  27 */         for (int j1 = 0; j1 < no_states; j1++)
/*     */         {
/*  29 */           int state = j1;
/*  30 */           Double val = dist1.get(state);
/*  31 */           if (val.doubleValue() != 0.0D) {
/*  32 */             if (state != st) {
/*  33 */               jump.counts[state] += val.doubleValue();
/*  34 */               exp_count.counts[1] += val.doubleValue();
/*     */             }
/*     */             else {
/*  37 */               double exp = this.exp_rd.probs[0];
/*  38 */               double non_jump_prob = exp;
/*  39 */               double jump_prob = (1.0D - exp) * jump.probs[state];
/*  40 */               double alloc = val.doubleValue() * (jump_prob / (jump_prob + non_jump_prob));
/*  41 */               jump.counts[state] += alloc;
/*  42 */               exp_count.counts[1] += alloc;
/*  43 */               exp_count.counts[0] += val.doubleValue() - alloc;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ExponentialTransitionProbs(Dirichlet samplerFirst, double exp_p) throws Exception
/*     */   {
/*  53 */     this.alpha = new SimpleExtendedDistribution(samplerFirst);
/*  54 */     this.exp_rd = new SimpleExtendedDistribution(new double[] { exp_p, 1.0D - exp_p }, Double.POSITIVE_INFINITY);
/*     */   }
/*     */   
/*     */   public ExponentialTransitionProbs(ExponentialTransitionProbs probs) {
/*  58 */     this.exp_rd = new SimpleExtendedDistribution(probs.exp_rd);
/*  59 */     this.alpha = new SimpleExtendedDistribution(probs.alpha);
/*     */   }
/*     */   
/*     */   public AbstractTransitionProbs clone(AbstractTransitionProbs probs) {
/*  63 */     return new ExponentialTransitionProbs((ExponentialTransitionProbs)probs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection getDistributions()
/*     */   {
/*  70 */     return (Collection)Arrays.asList(new Object[] { this.alpha, this.exp_rd });
/*     */   }
/*     */   
/*     */   public double getTransition(int from, int to) {
/*  74 */     double exp = this.exp_rd.probs[0];
/*  75 */     double toProb = this.alpha.probs[to];
/*  76 */     if (to == from) {
/*  77 */       return exp + (1.0D - exp) * toProb;
/*     */     }
/*     */     
/*  80 */     return (1.0D - exp) * toProb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/*  89 */     this.exp_rd.initialise();
/*  90 */     this.alpha.initialise();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoTrans, double[] pseudoExp)
/*     */   {
/*  99 */     throw new Error("Unresolved compilation problem: \n\tThe method transfer(double) in the type SimpleExtendedDistribution is not applicable for the arguments (double[])\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double transitionDistance(AbstractTransitionProbs probs)
/*     */   {
/* 106 */     ExponentialTransitionProbs probs1 = (ExponentialTransitionProbs)probs;
/* 107 */     return this.alpha.KLDistance(probs1.alpha) + this.exp_rd.KLDistance(probs1.exp_rd);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw, Double[] hittingProb)
/*     */   {
/* 113 */     throw new Error("Unresolved compilation problem: \n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 119 */     StringWriter sw = new StringWriter();
/* 120 */     PrintWriter pw = new PrintWriter(sw);
/* 121 */     print(pw, null);
/* 122 */     return sw.getBuffer().toString();
/*     */   }
/*     */   
/*     */   public void validate() {
/* 126 */     throw new Error("Unresolved compilation problems: \n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */   
/*     */   public void transfer(double pseudoTrans, double pseudoExp) {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/ExponentialTransitionProbs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */