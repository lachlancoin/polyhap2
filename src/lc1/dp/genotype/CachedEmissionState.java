/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import lc1.dp.CompoundState;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.State;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedEmissionState
/*     */   extends CompoundState
/*     */ {
/*     */   public CompoundState innerState;
/*     */   public SimpleExtendedDistribution[] emissions;
/*     */   
/*     */   public CachedEmissionState(CompoundState state, int noSites, int stateSpaceSize)
/*     */   {
/*  19 */     super(state.getName() + "c", state.adv);
/*  20 */     this.emissions = new SimpleExtendedDistribution[noSites];
/*  21 */     for (int i = 0; i < noSites; i++) {
/*  22 */       this.emissions[i] = new SimpleExtendedDistribution(stateSpaceSize);
/*     */     }
/*  24 */     this.innerState = state;
/*  25 */     refreshSiteEmissions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transferCountsToMemberStates()
/*     */   {
/*  37 */     for (int i = 0; i < this.emissions.length; i++) {
/*  38 */       double[] counts = this.emissions[i].counts;
/*  39 */       for (int k = 0; k < counts.length; k++) {
/*  40 */         this.innerState.addCount(k, Double.valueOf(counts[k]), i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone(State pseudo)
/*     */   {
/*  52 */     return new CachedEmissionState(this.innerState, this.emissions.length, this.emissions[0].probs.length);
/*     */   }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/*  57 */     for (int i = 0; i < this.emissions.length; i++) {
/*  58 */       this.emissions[i].initialise();
/*     */     }
/*     */   }
/*     */   
/*     */   public double score(int object_index, int i)
/*     */   {
/*  64 */     return this.emissions[i].probs[object_index];
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, Double value, int i) {
/*  68 */     this.emissions[i].counts[obj_index] += value.doubleValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void refreshSiteEmissions()
/*     */   {
/*  76 */     int noSites = this.emissions.length;
/*  77 */     for (int i = 0; i < noSites; i++) {
/*  78 */       double[] probs = this.emissions[i].probs;
/*  79 */       Arrays.fill(probs, 0.0D);
/*  80 */       for (int j = 0; j < probs.length; j++) {
/*  81 */         probs[j] += this.innerState.score(j, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double KLDistance(EmissionState st)
/*     */   {
/*  90 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public Object mostLikely(int pos)
/*     */   {
/*  95 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String st)
/*     */   {
/* 100 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public Object sample(int i)
/*     */   {
/* 105 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setRandom(double emiss, boolean restart)
/*     */   {
/* 110 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void validate() throws Exception
/*     */   {
/* 115 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public EmissionState[] getMemberStates()
/*     */   {
/* 120 */     return this.innerState.getMemberStates();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/CachedEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */