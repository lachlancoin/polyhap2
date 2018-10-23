/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.dp.genotype.SimpleExtendedDistribution;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FreeTransitionProbs
/*     */   implements Serializable, AbstractTransitionProbs
/*     */ {
/*     */   public SimpleExtendedDistribution[] transitionsOut;
/*     */   public SimpleExtendedDistribution alpha;
/*     */   public SimpleExtendedDistribution exp_rd;
/*     */   boolean single;
/*     */   final double[] pseudoCountTrans;
/*     */   final double[][] pseudocountWeight;
/*     */   
/*     */   public FreeTransitionProbs clone(AbstractTransitionProbs trans_ps)
/*     */   {
/*  26 */     return new FreeTransitionProbs(this, (FreeTransitionProbs)trans_ps);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeTransitionProbs(FreeTransitionProbs tp_init, FreeTransitionProbs tp_pseudo) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeTransitionProbs(Dirichlet alpha_sampler, Dirichlet samplerFirst, int no_states, double exp_p)
/*     */     throws Exception
/*     */   {
/*  43 */     this(false, alpha_sampler, samplerFirst, no_states, exp_p);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeTransitionProbs(boolean first, Dirichlet alpha_sampler, Dirichlet samplerFirst, int no_states, double exp_p)
/*     */     throws Exception
/*     */   {}
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCounts(StateDistribution[] observed)
/*     */   {
/*  94 */     for (int j = 0; j < observed.length; j++) {
/*  95 */       if (observed[j] != null)
/*     */       {
/*  97 */         if (this.transitionsOut[j] != null) {
/*  98 */           this.transitionsOut[j].addCounts(observed[j]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getTransition(int from, int to, int indicator)
/*     */   {
/* 109 */     double exp = this.exp_rd.probs[0];
/* 110 */     double toProb = this.alpha.probs[to];
/* 111 */     if (to == indicator) {
/* 112 */       return exp + (1.0D - exp) * toProb;
/*     */     }
/*     */     
/* 115 */     return (1.0D - exp) * toProb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getTransition(int from, int to)
/*     */   {
/* 123 */     SimpleExtendedDistribution dist = this.transitionsOut[from];
/* 124 */     if (dist != null) {
/* 125 */       double[] ind_probs = dist.probs;
/* 126 */       double sum = 0.0D;
/* 127 */       for (int k = 0; k < ind_probs.length; k++) {
/* 128 */         sum += ind_probs[k] * getTransition(from, to, k);
/*     */       }
/* 130 */       return sum;
/*     */     }
/* 132 */     return 0.0D;
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
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 152 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 153 */       if (this.transitionsOut[j] != null) { this.transitionsOut[j].initialise();
/*     */       }
/*     */     }
/* 156 */     this.exp_rd.initialise();
/* 157 */     this.alpha.initialise();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double logToProb(double[] probs)
/*     */   {
/* 168 */     double max = Double.NEGATIVE_INFINITY;
/* 169 */     for (int i = 0; i < probs.length; i++) {
/* 170 */       if (probs[i] > max) {
/* 171 */         max = probs[i];
/*     */       }
/*     */     }
/*     */     
/* 175 */     double sum = 0.0D;
/* 176 */     for (int i = 0; i < probs.length; i++) {
/* 177 */       probs[i] = Math.exp(probs[i] - max);
/* 178 */       sum += probs[i];
/*     */     }
/* 180 */     for (int i = 0; i < probs.length; i++) {
/* 181 */       probs[i] /= sum;
/*     */     }
/* 183 */     return -max;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 187 */     return this.transitionsOut.toString();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer1(double pseudoC, double pseudo_exp)
/*     */   {
/* 213 */     throw new Error("Unresolved compilation problems: \n\tThe method evaluate(double) in the type PseudoDistribution is not applicable for the arguments (double[])\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n");
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoC, double pseudo_exp)
/*     */   {
/* 241 */     int no_states = this.transitionsOut.length;
/* 242 */     for (int j = 0; j < no_states; j++)
/*     */     {
/* 244 */       if (this.transitionsOut[j] != null) {
/* 245 */         double[] indicator_probs = this.transitionsOut[j].probs;
/* 246 */         double[] indicator_counts = new double[this.transitionsOut[j].counts.length];
/* 247 */         Arrays.fill(indicator_counts, 0.0D);
/* 248 */         for (int j1 = 0; j1 < no_states; j1++) {
/* 249 */           double count = this.transitionsOut[j].counts[j1];
/* 250 */           if (count != 0.0D) {
/* 251 */             double[] rel_prob = new double[no_states];
/* 252 */             double prob_sum = 0.0D;
/* 253 */             for (int k = 0; k < no_states; k++) {
/* 254 */               double prob = indicator_probs[k];
/* 255 */               rel_prob[k] = (prob * getTransition(j, j1, k));
/* 256 */               prob_sum += rel_prob[k];
/*     */             }
/* 258 */             if (prob_sum > 0.001D) {
/* 259 */               for (int k = 0; k < no_states; k++) {
/* 260 */                 double prob = rel_prob[k] / prob_sum;
/* 261 */                 indicator_counts[k] += prob * count;
/* 262 */                 double val = count * prob;
/* 263 */                 if (j1 != k) {
/* 264 */                   this.alpha.counts[j1] += val;
/* 265 */                   this.exp_rd.counts[1] += val;
/*     */                 }
/*     */                 else {
/* 268 */                   double exp = this.exp_rd.probs[0];
/* 269 */                   double non_jump_prob = exp;
/* 270 */                   double jump_prob = (1.0D - exp) * this.alpha.probs[j1];
/* 271 */                   double alloc = val * (jump_prob / (jump_prob + non_jump_prob));
/* 272 */                   this.alpha.counts[j1] += alloc;
/* 273 */                   this.exp_rd.counts[1] += alloc;
/* 274 */                   this.exp_rd.counts[0] += val - alloc;
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 282 */     transfer1(pseudoC, pseudo_exp);
/*     */   }
/*     */   
/*     */   private void normalise(double[] rel_prob)
/*     */   {
/* 287 */     double sum = 0.0D;
/* 288 */     for (int i = 0; i < rel_prob.length; i++) {
/* 289 */       sum += rel_prob[i];
/*     */     }
/* 291 */     for (int i = 0; i < rel_prob.length; i++) {
/* 292 */       rel_prob[i] /= sum;
/*     */     }
/*     */   }
/*     */   
/*     */   private double[] mix(double[][] pseudocountWeight2, double[] lp)
/*     */   {
/* 298 */     double[] res = new double[lp.length];
/* 299 */     for (int j = 0; j < res.length; j++) {
/* 300 */       res[j] = 0.0D;
/* 301 */       for (int i = 0; i < lp.length; i++) {
/* 302 */         if (lp[i] > 0.0D) { res[j] += lp[i] * pseudocountWeight2[i][j];
/*     */         }
/*     */       }
/*     */     }
/* 306 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection getDistributions()
/*     */   {
/* 313 */     List res = new ArrayList();
/* 314 */     res.addAll((Collection)Arrays.asList(new Object[] { this.alpha, this.exp_rd }));
/* 315 */     res.addAll((Collection)Arrays.asList(this.transitionsOut));
/* 316 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double transitionDistance(AbstractTransitionProbs m)
/*     */   {
/* 327 */     double sum = 0.0D;
/* 328 */     FreeTransitionProbs m1 = (FreeTransitionProbs)m;
/* 329 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 330 */       SimpleExtendedDistribution d1 = this.transitionsOut[j];
/* 331 */       SimpleExtendedDistribution d2 = m1.transitionsOut[j];
/* 332 */       if ((d1 != null) && (d2 != null)) {
/* 333 */         sum += d1.KLDistance(d2);
/*     */       }
/*     */     }
/*     */     
/* 337 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */   {
/* 348 */     throw new Error("Unresolved compilation problems: \n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/* 359 */     return this.transitionsOut.length;
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
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, Double[] hittingProb)
/*     */   {
/* 381 */     throw new Error("Unresolved compilation problems: \n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n\tThe method printSimple(PrintWriter, String, String, double) is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/FreeTransitionProbs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */