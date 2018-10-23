/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.genotype.CachedHMM;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*     */ import lc1.dp.genotype.io.scorable.StateIndices;
/*     */ 
/*     */ public class BaumWelchTrainer
/*     */ {
/*     */   public MarkovModel hmm;
/*     */   StateIndices[] data;
/*     */   DP[] dp;
/*     */   final int seqLength;
/*     */   final StateDistribution[][] transProbs;
/*     */   
/*     */   public BaumWelchTrainer(CachedHMM hmm2, PhasedIntegerGenotypeData[] data2)
/*     */   {
/*  25 */     this(hmm2, StateIndices.get(data2, hmm2));
/*     */   }
/*     */   
/*     */   public BaumWelchTrainer(MarkovModel hmm, StateIndices[] obj)
/*     */   {
/*  30 */     this.hmm = hmm;
/*  31 */     this.data = obj;
/*  32 */     this.emissionCount = new StateDistribution(hmm.modelLength());
/*  33 */     this.dp = new DP[obj.length];
/*  34 */     Arrays.fill(time, Long.valueOf(0L));
/*  35 */     this.seqLength = obj[0].length();
/*  36 */     this.transProbs = new StateDistribution[this.seqLength + 1][hmm.modelLength()];
/*  37 */     for (int j = 0; j < obj.length; j++) {
/*  38 */       this.dp[j] = new DP(hmm, "", this.data[j], false);
/*     */     }
/*  40 */     for (int j = 0; j < hmm.modelLength(); j++) {
/*  41 */       for (int i = 0; i < this.seqLength + 1; i++) {
/*  42 */         this.transProbs[i][j] = new StateDistribution(hmm.modelLength());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double validate(Collection<StateDistribution> dist, int no)
/*     */   {
/*  53 */     double sum = 0.0D;
/*     */     
/*  55 */     for (Iterator<StateDistribution> it = dist.iterator(); it.hasNext();) {
/*  56 */       StateDistribution d = (StateDistribution)it.next();
/*  57 */       if (d != null) {
/*  58 */         double s = d.sum();
/*     */         
/*  60 */         sum += s;
/*     */       }
/*     */     }
/*  63 */     if (Math.abs(sum / no - 1.0D) > 0.01D) {
/*  64 */       if (Math.abs(sum / no - 1.0D) > 0.3D) {
/*  65 */         throw new RuntimeException("sum not right " + sum + " ");
/*     */       }
/*     */       
/*  68 */       Logger.global.warning("sum was not 1 " + sum * no + ".  Normalising !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
/*  69 */       for (Iterator<StateDistribution> it = dist.iterator(); it.hasNext();) {
/*  70 */         StateDistribution d = (StateDistribution)it.next();
/*  71 */         if (d != null) {
/*  72 */           d.multiplyValues(no / sum);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addTransitionProbSum(int j)
/*     */     throws Exception
/*     */   {
/*  89 */     for (int i = -1; i < this.seqLength; i++) {
/*  90 */       this.dp[j].addTransitionPosterior(i, this.transProbs[(i + 1)]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void validateCounts(int total)
/*     */   {
/*  96 */     for (int i = -1; i < this.seqLength; i++) {
/*     */       try
/*     */       {
/*  99 */         double sum = validate((Collection)Arrays.asList(this.transProbs[(i + 1)]), this.data.length);
/* 100 */         if (Math.abs(sum / total - 1.0D) <= 0.01D) continue; throw new RuntimeException("!! " + sum + " " + total);
/*     */       } catch (Exception exc) {
/* 102 */         exc.printStackTrace();
/* 103 */         System.exit(0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void compare(Map<State, SimpleDistribution> dist1, Map<State, SimpleDistribution> dist2)
/*     */   {
/* 111 */     for (Iterator<State> it = dist1.keySet().iterator(); it.hasNext();) {
/* 112 */       State k = (State)it.next();
/* 113 */       SimpleDistribution d1 = (SimpleDistribution)dist1.get(k);
/* 114 */       SimpleDistribution d2 = (SimpleDistribution)dist2.get(k);
/* 115 */       if (d1.different(d2)) {
/* 116 */         Logger.getAnonymousLogger().info("changed " + k + "-->" + "\n" + d1.toString() + " to " + d2.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 123 */   public static Long[] time = new Long[11];
/* 124 */   public static long[] t = new long[12];
/*     */   public static long t1;
/*     */   final StateDistribution emissionCount;
/*     */   
/*     */   public double expectationStep() {
/* 129 */     for (int i = 0; i < this.seqLength + 1; i++) {
/* 130 */       for (int j = 0; j < this.hmm.modelLength(); j++) {
/* 131 */         this.transProbs[i][j].reset();
/*     */       }
/*     */     }
/* 134 */     t1 = System.currentTimeMillis();
/* 135 */     double logprob = 0.0D;
/* 136 */     Arrays.fill(t, 0L);Arrays.fill(time, Long.valueOf(0L));
/*     */     
/* 138 */     for (int j = 0; j < this.data.length; j++) {
/* 139 */       t[0] = System.currentTimeMillis();
/*     */       
/* 141 */       t[1] = System.currentTimeMillis();
/* 142 */       this.dp[j].reset();
/* 143 */       t[2] = System.currentTimeMillis();
/* 144 */       double logprob_j = this.dp[j].search(true);
/*     */       
/* 146 */       if (Double.isInfinite(logprob_j)) throw new RuntimeException("is infinite!");
/* 147 */       logprob += logprob_j;
/*     */       try
/*     */       {
/* 150 */         t[3] = System.currentTimeMillis();
/* 151 */         addTransitionProbSum(j);
/* 152 */         t[4] = System.currentTimeMillis();
/*     */         
/*     */ 
/* 155 */         addEmissionProbSum(j);
/* 156 */         t[5] = System.currentTimeMillis();
/*     */       }
/*     */       catch (Exception exc) {
/* 159 */         exc.printStackTrace();
/* 160 */         System.exit(0);
/*     */       }
/* 162 */       for (int i = 0; i < t.length - 1; i++) {
/* 163 */         int tmp206_204 = i; Long[] tmp206_201 = time;tmp206_201[tmp206_204] = Long.valueOf(tmp206_201[tmp206_204].longValue() + (t[(i + 1)] - t[i]));
/*     */       }
/*     */     }
/*     */     
/* 167 */     for (int i = -1; i < this.seqLength; i++) {
/* 168 */       StateDistribution[] dist_i = this.transProbs[(i + 1)];
/* 169 */       for (int k = 0; k < this.hmm.modelLength(); k++) {
/* 170 */         StateDistribution dist_ik = dist_i[k];
/* 171 */         for (int j = 0; j < this.hmm.modelLength(); j++) {
/* 172 */           dist_ik.dist[j] *= this.hmm.getTransitionScore(k, j, i + this.dp[0].adv[j]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 179 */     this.hmm.setStatesChanged(false);
/*     */     
/* 181 */     for (int i = -1; i < this.seqLength; i++) {
/* 182 */       this.hmm.addCounts(this.transProbs[(i + 1)], i);
/*     */     }
/* 184 */     t[6] = System.currentTimeMillis();
/* 185 */     if ((this.hmm instanceof CachedHMM)) {
/* 186 */       ((CachedHMM)this.hmm).transferEmissionCountsToMemberStates();
/*     */     }
/*     */     
/*     */ 
/* 190 */     t[7] = System.currentTimeMillis();
/* 191 */     return logprob;
/*     */   }
/*     */   
/* 194 */   public static boolean maximisationStep(MarkovModel hmm) { hmm.transferCountsToProbs();
/* 195 */     t[(t.length - 1)] = System.currentTimeMillis();
/* 196 */     for (int i = 5; i < time.length; i++) {
/* 197 */       time[i] = Long.valueOf(t[(i + 1)] - t[i]);
/*     */     }
/*     */     
/* 200 */     System.err.println(Arrays.asList(time) + "  vs " + (System.currentTimeMillis() - t1));
/*     */     
/* 202 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addEmissionProbSum(int l)
/*     */   {
/* 208 */     for (int i = 0; i < this.seqLength; i++) {
/* 209 */       this.dp[l].getPosterior(i, this.emissionCount);
/*     */       
/* 211 */       for (int j = 0; j < this.hmm.modelLength(); j++)
/*     */       {
/* 213 */         if (this.dp[l].emiss[j] != 0) {
/* 214 */           EmissionState k = (EmissionState)this.hmm.getState(j);
/* 215 */           this.data[l].addCount(k, this.emissionCount.get(j), i);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private double sum(Iterator<SimpleDistribution> it)
/*     */   {
/* 223 */     double sum = 0.0D;
/* 224 */     while (it.hasNext()) {
/* 225 */       sum += ((SimpleDistribution)it.next()).sum();
/*     */     }
/* 227 */     return sum;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/BaumWelchTrainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */