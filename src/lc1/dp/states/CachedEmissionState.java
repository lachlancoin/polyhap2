/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.stats.IntegerDistribution;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.stats.SkewNormal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedEmissionState
/*     */   extends AbstractCachedEmissionState
/*     */ {
/*     */   public CompoundState innerState;
/*     */   public PseudoDistribution[] emissions;
/*     */   public ProbabilityDistribution[][] emissionsDT;
/*     */   
/*     */   public void reverse()
/*     */   {
/*  29 */     List<PseudoDistribution> l = Arrays.asList(this.emissions);
/*  30 */     Collections.reverse(l);
/*  31 */     this.emissions = ((PseudoDistribution[])l.toArray(new PseudoDistribution[0]));
/*     */   }
/*     */   
/*     */   public double[] getEmiss(int i) {
/*  35 */     return this.emissions[i].probs();
/*     */   }
/*     */   
/*     */   public SkewNormal[] probR() {
/*  39 */     return this.innerState.probR();
/*     */   }
/*     */   
/*  42 */   public IlluminaProbB[] probB() { return this.innerState.probB(); }
/*     */   
/*     */   public Integer noCop() {
/*  45 */     return this.innerState.noCop();
/*     */   }
/*     */   
/*     */   public CachedEmissionState(CompoundState state, int stateSpaceSize) {
/*  49 */     super(state);
/*  50 */     int noSites = state.noSnps();
/*  51 */     ProbabilityDistribution[][] dists = ((HaplotypeEmissionState)state.getMemberStates(true)[0]).emissionsDatatype;
/*     */     
/*  53 */     this.emissions = new PseudoDistribution[noSites];
/*  54 */     if ((dists != null) && (dists.length > 0)) {
/*  55 */       this.emissionsDT = new ProbabilityDistribution[dists.length][noSites];
/*     */     }
/*  57 */     this.innerState = state;
/*  58 */     for (int i = 0; i < noSites; i++) {
/*  59 */       Integer ind = calculateIndex(i);
/*  60 */       if (ind != null) {
/*  61 */         this.emissions[i] = new IntegerDistribution(ind.intValue());
/*     */       }
/*     */       else
/*  64 */         this.emissions[i] = new SimpleExtendedDistribution(stateSpaceSize);
/*  65 */       if (this.emissionsDT != null) {
/*  66 */         for (int k = 0; k < this.emissionsDT.length; k++) {
/*  67 */           this.emissionsDT[k][i] = state.calcAverageDistributions(k, i);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  74 */     refreshSiteEmissions();
/*     */   }
/*     */   
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/*  79 */     return this.innerState.getEmissionStateSpace();
/*     */   }
/*     */   
/*     */   public boolean transferCountsToProbs(double pseudo) {
/*  83 */     throw new RuntimeException("!!! " + getClass());
/*     */   }
/*     */   
/*     */   public void transferCountsToMemberStates()
/*     */   {
/*  88 */     for (int i = 0; i < this.emissions.length; i++) {
/*  89 */       if (this.emissions[i].fixedInteger() == null) {
/*  90 */         double[] counts = this.emissions[i].counts();
/*     */         
/*  92 */         for (int k = 0; k < counts.length; k++) {
/*  93 */           if (counts[k] != 0.0D)
/*  94 */             this.innerState.addCount(k, counts[k], i);
/*     */         }
/*  96 */         if (this.emissionsDT != null) {
/*  97 */           for (int k1 = 0; k1 < this.emissionsDT.length; k1++) {
/*  98 */             this.emissionsDT[k1][i].transfercounts(this.innerState, k1, i);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 104 */     if ((this.innerState instanceof CachedEmissionState)) {
/* 105 */       ((CachedEmissionState)this.innerState).transferCountsToMemberStates();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double score(int object_index, int i1)
/*     */   {
/* 113 */     return this.emissions[i1].probs(object_index);
/*     */   }
/*     */   
/*     */ 
/*     */   public double scoreEmiss(Double[] object_index, int i1)
/*     */   {
/* 119 */     double sc = 1.0D;
/* 120 */     for (int k = 0; k < object_index.length; k++) {
/* 121 */       if (object_index[k] != null) {
/* 122 */         sc *= this.emissionsDT[k][i1].probability(object_index[k].doubleValue());
/*     */       }
/*     */     }
/* 125 */     return sc;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 131 */     return new CachedEmissionState((CompoundState)this.innerState.clone(), this.emissions[0].probs().length);
/*     */   }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/* 136 */     for (int i = 0; i < this.emissions.length; i++) {
/* 137 */       this.emissions[i].initialise();
/*     */     }
/*     */     
/* 140 */     if (this.emissionsDT != null) {
/* 141 */       for (int k = 0; k < this.emissionsDT.length; k++) {
/* 142 */         for (int i = 0; i < this.emissions.length; i++) {
/* 143 */           this.emissionsDT[k][i].initialise();
/*     */         }
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
/*     */   public double score(int j, int i, boolean recursive, boolean decompose)
/*     */   {
/* 157 */     return this.innerState.score(j, i, recursive, decompose);
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, double value, int i1)
/*     */   {
/* 162 */     int i = this.emissions.length == 1 ? 0 : i1;
/* 163 */     this.emissions[i].addCount(obj_index, value);
/*     */   }
/*     */   
/*     */   public void addCountDT(double obj_index, int phen_index, double value, int i1) {
/* 167 */     if (this.emissionsDT != null) {
/* 168 */       int i = this.emissions.length == 1 ? 0 : i1;
/*     */       
/* 170 */       this.emissionsDT[phen_index][i].addCount(obj_index, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 176 */   int paramIndex = 1;
/*     */   
/*     */   public void refreshSiteEmissions()
/*     */   {
/* 180 */     if ((this.innerState instanceof CachedEmissionState)) {
/* 181 */       ((CachedEmissionState)this.innerState).refreshSiteEmissions();
/*     */     }
/* 183 */     this.paramIndex = this.innerState.getParamIndex();
/* 184 */     int noSites = this.emissions.length;
/* 185 */     for (int i = 0; i < noSites; i++) {
/* 186 */       PseudoDistribution em = this.emissions[i];
/* 187 */       if (!(em instanceof IntegerDistribution))
/*     */       {
/*     */ 
/* 190 */         double[] probs = this.emissions[i].probs();
/* 191 */         boolean fixed = false;
/* 192 */         for (int j = 0; j < probs.length; j++) {
/* 193 */           double sc = this.innerState.score(j, i);
/* 194 */           if (sc > 0.999D) {
/* 195 */             fixed = true;
/*     */           }
/*     */           
/* 198 */           probs[j] = sc;
/*     */         }
/* 200 */         if (fixed) {
/* 201 */           Integer fixedIndex = calculateIndex(i);
/* 202 */           if (fixedIndex != null) {
/* 203 */             this.emissions[i] = new IntegerDistribution(fixedIndex.intValue());
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 208 */         if (this.emissionsDT != null) {
/* 209 */           for (int k = 0; k < this.emissionsDT.length; k++) {
/* 210 */             this.innerState.setAverageDistributions(this.emissionsDT[k][i], k, i);
/*     */           }
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int mostLikely(int pos)
/*     */   {
/* 241 */     return this.innerState.mostLikely(pos);
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String st, List<Integer> columns)
/*     */   {
/* 246 */     this.innerState.print(pw, st, columns);
/*     */   }
/*     */   
/*     */   public int sample(int i)
/*     */   {
/* 251 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate()
/*     */     throws Exception
/*     */   {
/* 258 */     this.innerState.validate();
/*     */   }
/*     */   
/*     */   public EmissionState[] getMemberStates(boolean real)
/*     */   {
/* 263 */     return this.innerState.getMemberStates(real);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer calculateIndex(int i)
/*     */   {
/* 275 */     return this.innerState.calculateIndex(i);
/*     */   }
/*     */   
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/* 280 */     return this.emissions[i].fixedInteger();
/*     */   }
/*     */   
/*     */   public int getParamIndex() {
/* 284 */     return this.paramIndex;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/CachedEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */