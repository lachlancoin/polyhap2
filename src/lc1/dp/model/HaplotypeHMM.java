/*     */ package lc1.dp.model;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.util.Constants;
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
/*     */ public abstract class HaplotypeHMM
/*     */   extends MarkovModel
/*     */   implements Comparable
/*     */ {
/*     */   public EmissionStateSpace getStateEmissionStateSpace()
/*     */   {
/*  36 */     return this.emissionStateSpace;
/*     */   }
/*     */   
/*  39 */   public boolean trainEmissions() { return this.pseudocountWeights[0] < 10000.0D; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */   protected double[] pseudocountWeights = { 0.01D, 0.01D, 0.01D, 0.01D, 0.01D };
/*  46 */   protected double[] pseudocountWeights1 = { 0.01D, 0.01D, 0.01D, 0.01D, 0.01D };
/*     */   
/*     */   public void setPseudoCountWeights(double[] d, double[] d1)
/*     */   {
/*  50 */     this.pseudocountWeights = d;
/*  51 */     this.pseudocountWeights1 = d1;
/*     */   }
/*     */   
/*     */ 
/*  55 */   public double logp = Double.NEGATIVE_INFINITY;
/*     */   
/*     */ 
/*     */ 
/*     */   public int compareTo(Object obj)
/*     */   {
/*  61 */     HaplotypeHMM obj1 = (HaplotypeHMM)obj;
/*  62 */     if (obj1.logp == this.logp) return 0;
/*  63 */     if (this.logp < obj1.logp) return 1;
/*  64 */     return -1;
/*     */   }
/*     */   
/*  67 */   public HaplotypeHMM(MarkovModel mm) { super(mm.getName() + "x" + mm.getName(), mm.noSnps.intValue(), null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  72 */     for (int j = 1; j < mm.modelLength(); j++)
/*     */     {
/*  74 */       EmissionState state_j = (EmissionState)mm.getState(j);
/*     */       
/*  76 */       EmissionState newState = (EmissionState)state_j.clone();
/*     */       
/*  78 */       addState(newState);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  83 */     this.in = mm.statesIn(1, 1);
/*     */   }
/*     */   
/*  86 */   public HaplotypeHMM(HaplotypeHMM hmm) { super(hmm);
/*     */     
/*  88 */     this.in = hmm.in;
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
/*     */   public abstract MarkovModel clone(boolean paramBoolean);
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
/*     */   public HaplotypeHMM(String name, int numFounders, int noSnps, double[] init, EmissionStateSpace emStSp, char[] codes, List<Integer> locs, ProbabilityDistribution[] numLevels)
/*     */   {
/* 126 */     super(name, noSnps, emStSp);
/*     */     
/*     */ 
/* 129 */     if (codes == null) {
/* 130 */       codes = new char[numFounders];
/* 131 */       Arrays.fill(codes, 'e');
/*     */     }
/* 133 */     if (codes.length != numFounders) { throw new RuntimeException("!!");
/*     */     }
/* 135 */     double[] r_prior = Constants.r_prior();
/* 136 */     for (int j = 0; j < codes.length; j++) {
/* 137 */       double[] probs = emStSp.getArray(codes[j]);
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
/* 148 */       EmissionState sta = makeState(j + 1, noSnps, probs, Math.abs(probs[Constants.getMax(probs)] - 1.0D) < 1.0E-5D, Constants.meanvarskew(j), r_prior[j], numLevels);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 156 */       addState(sta);
/*     */     }
/*     */     
/* 159 */     this.in = new int[this.states.size() - 1];
/* 160 */     for (int jk = 1; jk < this.states.size(); jk++) {
/* 161 */       this.in[(jk - 1)] = jk;
/*     */     }
/* 163 */     Logger.global.info("state space size is " + this.states.size() + " for " + getName());
/*     */   }
/*     */   
/*     */   private static void distributeAmongSame(double[] probs, EmissionStateSpace emStSp)
/*     */   {
/* 168 */     double[] copyCount = new double[3];
/* 169 */     int[] count = new int[3];
/* 170 */     Arrays.fill(copyCount, 0.0D);
/* 171 */     Arrays.fill(count, 0);
/* 172 */     for (int i = 0; i < probs.length; i++) {
/* 173 */       int cp = ((Emiss)emStSp.get(i)).noCopies();
/* 174 */       copyCount[cp] += probs[i];
/* 175 */       count[cp] += 1;
/*     */     }
/* 177 */     Arrays.fill(probs, 0.0D);
/* 178 */     double sum = 0.0D;
/* 179 */     for (int i = 0; i < probs.length; i++) {
/* 180 */       int cp = ((Emiss)emStSp.get(i)).noCopies();
/* 181 */       double val = copyCount[cp] / count[cp];
/* 182 */       probs[i] = val;
/* 183 */       sum += val;
/*     */     }
/* 185 */     if (Math.abs(sum - 1.0D) > 1.0E-4D) {
/* 186 */       throw new RuntimeException("!!");
/*     */     }
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
/* 212 */   boolean allowedScore = true;
/*     */   final int[] in;
/*     */   
/*     */   public EmissionState makeState(String st, int noSnps, double[] init, boolean fixed, double[][] meanvarskew, double prior_mod, ProbabilityDistribution[] numLevels) {
/* 216 */     int index = Constants.getMax(init);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 224 */     return new HaplotypeEmissionState(st, noSnps, Constants.u_global(0)[0], init, this.emissionStateSpace, Integer.valueOf(this.emissionStateSpace.getCN(index)), meanvarskew, prior_mod, numLevels);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseCounts()
/*     */   {
/* 235 */     super.initialiseCounts();
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
/*     */   public List<State> statesOut(State st, int beforeToEmission)
/*     */   {
/* 280 */     if (beforeToEmission == this.noSnps.intValue() - 1) return this.states.subList(0, 2);
/* 281 */     return this.states.subList(1, this.states.size());
/*     */   }
/*     */   
/*     */   public List<State> statesIn(State st, int beforeToEmission)
/*     */   {
/* 286 */     if (beforeToEmission == 0) return this.states.subList(0, 1);
/* 287 */     return this.states.subList(1, this.states.size());
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void transferTransitionCountsToProbs(int paramInt);
/*     */   
/*     */   public void transferCountsToProbs(int index)
/*     */   {
/* 295 */     if (this.pseudocountWeights[0] < 1000.0D) {
/* 296 */       for (int j = 1; j < modelLength(); j++) {
/* 297 */         EmissionState hes = (EmissionState)getState(j);
/*     */         
/* 299 */         hes.transferCountsToProbs(this.pseudocountWeights[0]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 309 */     transferTransitionCountsToProbs(index);
/*     */   }
/*     */   
/*     */ 
/*     */   public static Double[] subList(Double[] l, List<Integer> cols)
/*     */   {
/* 315 */     if (cols == null) return l;
/* 316 */     Double[] res = new Double[cols.size()];
/* 317 */     for (int i = 0; i < cols.size(); i++) {
/* 318 */       res[i] = l[((Integer)cols.get(i)).intValue()];
/*     */     }
/* 320 */     return res;
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, List<Integer> cols, int popsize) {
/* 324 */     StringBuffer sb1 = new StringBuffer();
/* 325 */     for (int i = 0; i < modelLength(); i++) {
/* 326 */       sb1.append("%8.2g ");
/*     */     }
/* 328 */     super.print(pw, cols, popsize);
/*     */     
/*     */ 
/* 331 */     pw.println("hitting probs");
/* 332 */     double[][] hp = getHittingProb(this.noSnps.intValue());
/* 333 */     Double[] hp1 = new Double[hp[0].length];
/* 334 */     if (hp != null) {
/* 335 */       for (int i = 0; i < hp.length; i++) {
/* 336 */         for (int j = 0; j < hp[i].length; j++) {
/* 337 */           hp[i][j] = (Math.round(10000.0D * hp[i][j]) / 100.0D);
/*     */         }
/*     */       }
/*     */     }
/* 341 */     pw.println("hitting probs ");
/* 342 */     for (int i = 0; i < this.noSnps.intValue(); i++) {
/* 343 */       for (int k = 0; k < hp1.length; k++) {
/* 344 */         hp1[k] = Double.valueOf(hp[i][k]);
/*     */       }
/* 346 */       if ((cols == null) || (cols.contains(Integer.valueOf(i)))) {
/* 347 */         pw.println(i + " " + Format.sprintf(sb1.toString(), hp1));
/*     */       }
/*     */     }
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
/*     */   public double getLogP()
/*     */   {
/* 370 */     return this.logp;
/*     */   }
/*     */   
/*     */ 
/* 374 */   final int[] in0 = new int[1];
/*     */   
/* 376 */   public int[] statesIn(int j, int i) { if (i == 0) return this.in0;
/* 377 */     return this.in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i) {
/* 381 */     if (i == this.noSnps.intValue() - 1) return this.in0;
/* 382 */     return this.in;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/HaplotypeHMM.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */