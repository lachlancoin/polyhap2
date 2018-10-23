/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.stats.SkewNormal;
/*     */ 
/*     */ public abstract class CompoundState
/*     */   extends EmissionState
/*     */ {
/*     */   ProbabilityDistribution[] tmp;
/*     */   
/*     */   public CompoundState(String name, int adv)
/*     */   {
/*  16 */     super(name, adv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reverse()
/*     */   {
/*  23 */     EmissionState[] emStates = getMemberStates(true);
/*  24 */     for (int i = 0; i < emStates.length; i++) {
/*  25 */       emStates[i].reverse();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isFixed(int k)
/*     */   {
/*  33 */     EmissionState[] emStates = getMemberStates(true);
/*  34 */     for (int i = 0; i < emStates.length; i++) {
/*  35 */       if (emStates[i].getFixedInteger(k) == null) return false;
/*     */     }
/*  37 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract EmissionState[] getMemberStates(boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComparableArray getMemberStatesRecursive(boolean real)
/*     */   {
/*  51 */     EmissionState[] membs = getMemberStates(real);
/*  52 */     List<Comparable> res = new ArrayList();
/*  53 */     for (int i = 0; i < membs.length; i++) {
/*  54 */       if ((membs[i] instanceof CompoundState)) {
/*  55 */         res.add(((CompoundState)membs[i]).getMemberStatesRecursive(real));
/*     */       }
/*     */       else {
/*  58 */         res.add(membs[i]);
/*     */       }
/*     */     }
/*  61 */     return new ComparableArray(res);
/*     */   }
/*     */   
/*  64 */   public CompoundState(CompoundState st1) { this(st1.name, st1.adv); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Integer calculateIndex(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract double score(int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2);
/*     */   
/*     */ 
/*     */ 
/*     */   public int noSnps()
/*     */   {
/*  79 */     return getMemberStates(false)[0].noSnps();
/*     */   }
/*     */   
/*     */ 
/*     */   public int memberStatesAreCompound()
/*     */   {
/*  85 */     EmissionState[] memberStates = getMemberStates(false);
/*  86 */     int cntMoreThan1 = 0;
/*  87 */     for (int i = 0; i < memberStates.length; i++) {
/*  88 */       if (((memberStates[i] instanceof CompoundState)) && 
/*  89 */         (((CompoundState)memberStates[i]).getMemberStates(false).length > 1)) {
/*  90 */         cntMoreThan1++;
/*     */       }
/*     */     }
/*  93 */     return cntMoreThan1;
/*     */   }
/*     */   
/*     */   public SkewNormal[] getProbabilityDist(int index) {
/*  97 */     EmissionState[] membs = getMemberStates(true);
/*  98 */     SkewNormal[] res = new SkewNormal[membs.length];
/*  99 */     for (int k = 0; k < res.length; k++) {
/* 100 */       res[k] = membs[k].probR()[index];
/*     */     }
/* 102 */     return res;
/*     */   }
/*     */   
/*     */   public ProbabilityDistribution calcAverageDistributions(int k, int i)
/*     */   {
/* 107 */     EmissionState[] st = getMemberStates(true);
/* 108 */     if (this.tmp == null) this.tmp = new ProbabilityDistribution[st.length];
/* 109 */     for (int i1 = 0; i1 < this.tmp.length; i1++) {
/* 110 */       this.tmp[i1] = ((HaplotypeEmissionState)st[i1]).emissionsDatatype[k][i];
/*     */     }
/* 112 */     return this.tmp[0].clone();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAverageDistributions(ProbabilityDistribution res, int k, int i)
/*     */   {
/* 118 */     EmissionState[] st = getMemberStates(true);
/* 119 */     if (this.tmp == null) this.tmp = new ProbabilityDistribution[st.length];
/* 120 */     for (int i1 = 0; i1 < this.tmp.length; i1++) {
/* 121 */       this.tmp[i1] = ((HaplotypeEmissionState)st[i1]).emissionsDatatype[k][i];
/*     */     }
/* 123 */     res.setParamsAsAverageOf(this.tmp);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/CompoundState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */