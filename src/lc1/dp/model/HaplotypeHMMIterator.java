/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.transition.FreeTransitionProbs1;
/*     */ import lc1.dp.transition.MultiExpProbs;
/*     */ import lc1.dp.transition.SimpleExpTransProb;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class HaplotypeHMMIterator implements Iterator<MarkovModel>
/*     */ {
/*     */   final int noSites;
/*     */   final int count;
/*  18 */   int i = 0;
/*     */   
/*     */   final double[] init;
/*     */   
/*     */   final EmissionStateSpace emStSp;
/*     */   
/*     */   int numF;
/*     */   
/*     */   final char[] modify;
/*     */   
/*     */   final double[] modifyFrac;
/*     */   final List<Integer> locs;
/*     */   Object[] clazz;
/*     */   
/*     */   public HaplotypeHMMIterator(String name, int noSites, int count, List<Integer> locs, int numF, EmissionStateSpace emStSp, int[] mode, char[] modify, double[] modifyFrac, ProbabilityDistribution[] numLevels)
/*     */   {
/*  34 */     this.noSites = noSites;
/*     */     
/*  36 */     this.name = name;
/*  37 */     this.count = count;
/*  38 */     this.emStSp = emStSp;
/*  39 */     this.locs = locs;
/*  40 */     this.init = new double[emStSp.size()];
/*  41 */     Arrays.fill(this.init, 1.0D / emStSp.size());
/*  42 */     setMode(mode);
/*  43 */     this.numF = numF;
/*  44 */     this.modify = modify;
/*  45 */     this.modifyFrac = modifyFrac;
/*  46 */     this.numLevels = numLevels;
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
/*  59 */   double[] u_g = Constants.u_global(0);
/*     */   final String name;
/*     */   
/*  62 */   public void setMode(int[] transMode) { this.clazz = new Class[transMode.length][2];
/*  63 */     for (int i = 0; i < transMode.length; i++) {
/*  64 */       if (transMode[i] == 0) {
/*  65 */         this.clazz[i] = { SimpleExpTransProb.class, SimpleExpTransProb.class };
/*     */ 
/*     */       }
/*  68 */       else if (transMode[i] == 1) {
/*  69 */         this.clazz[i] = { FreeTransitionProbs1.class };
/*     */ 
/*     */       }
/*  72 */       else if (transMode[i] == 2) {
/*  73 */         this.clazz[i] = { SimpleExpTransProb.class, MultiExpProbs.class };
/*     */ 
/*     */       }
/*  76 */       else if (transMode[i] == 3) {
/*  77 */         this.clazz[i] = { MultiExpProbs.class, SimpleExpTransProb.class };
/*     */       }
/*  79 */       else if (transMode[i] == 4) {
/*  80 */         this.clazz[i] = { MultiExpProbs.class, MultiExpProbs.class };
/*     */       }
/*  82 */       else if (transMode[i] == 5) {
/*  83 */         throw new RuntimeException("!!");
/*     */       }
/*     */       
/*  86 */       Logger.global.info("clazz " + Arrays.asList(this.clazz));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final ProbabilityDistribution[] numLevels;
/*     */   
/*     */ 
/*     */ 
/*     */   public MarkovModel next()
/*     */   {
/* 100 */     this.i += 1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 105 */     Double[] r = 
/* 106 */       { Double.valueOf(Constants.expModelIntHotSpot(0) * Constants.probCrossOverBetweenBP), 
/* 107 */       Double.valueOf(Constants.expModelIntHotSpot(1) * Constants.probCrossOverBetweenBP) };
/*     */     
/*     */ 
/* 110 */     FreeHaplotypeHMM res = new FreeHaplotypeHMM(this.name + "_free_haplotype_" + this.i, this.numF, this.noSites, this.init, this.emStSp, this.clazz, this.locs, r, 
/* 111 */       null, 
/* 112 */       this.modify, this.modifyFrac, false, this.numLevels);
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
/* 143 */     return res;
/*     */   }
/*     */   
/*     */   public boolean hasNext() {
/* 147 */     return this.i < this.count;
/*     */   }
/*     */   
/*     */   public void remove() {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/HaplotypeHMMIterator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */