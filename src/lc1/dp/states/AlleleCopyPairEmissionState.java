/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AlleleCopyPairEmissionState
/*     */   extends PairEmissionState
/*     */ {
/*     */   public Integer calculateIndex(int i)
/*     */   {
/*  15 */     EmissionState[] states = getMemberStates(false);
/*     */     
/*  17 */     Integer[] internal_indices = new Integer[states.length];
/*  18 */     for (int j = 0; j < states.length; j++) {
/*  19 */       if ((states[j] instanceof CompoundState)) {
/*  20 */         internal_indices[j] = ((CompoundState)states[j]).calculateIndex(i);
/*  21 */         if (internal_indices[j] == null) {
/*  22 */           throw new RuntimeException("!!");
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  27 */         internal_indices[j] = states[j].getFixedInteger(i);
/*  28 */         if (internal_indices[j] == null) {
/*  29 */           return null;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  35 */     int[] indices = new int[states.length];
/*  36 */     for (int j = 0; j < states.length; j++)
/*     */     {
/*  38 */       Integer index_i = internal_indices[j];
/*  39 */       if (index_i == null) {
/*  40 */         return null;
/*     */       }
/*  42 */       indices[j] = index_i.intValue();
/*     */     }
/*  44 */     Integer result = Integer.valueOf(this.emStSp.getIndex(indices));
/*  45 */     if (result == null) {
/*  46 */       throw new RuntimeException("is null");
/*     */     }
/*     */     
/*  49 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AlleleCopyPairEmissionState(List<EmissionState> dist, EmissionStateSpace emStSp, boolean decompose, IlluminaProbB[] probB)
/*     */   {
/*  58 */     super(dist, emStSp, decompose, probB, null);
/*  59 */     if (this.emStSp.getMembers()[0].size() == 1) { throw new RuntimeException("!!");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double score(int key, int i, boolean recursive, boolean decompose)
/*     */   {
/*  66 */     double sc = 1.0D;
/*  67 */     int[] indices = this.emStSp.getMemberIndices(key);
/*  68 */     if (indices[0] == 0) { sc = getInnerState(0, false).score(0, i);
/*     */     } else {
/*  70 */       for (int j = 0; j < indices.length; j++) {
/*  71 */         EmissionState innerSt = getInnerState(j, false);
/*  72 */         sc *= innerSt.score(indices[j], i);
/*     */       }
/*     */     }
/*  75 */     return sc;
/*     */   }
/*     */   
/*     */   public void addCount(int key, Double value, int i, boolean decompose) {
/*  79 */     int[] indices = this.emStSp.getMemberIndices(key);
/*  80 */     if (indices[0] == 0) { getInnerState(0, false).addCount(0, value.doubleValue(), i);
/*     */     } else {
/*  82 */       for (int j = 0; j < indices.length; j++) {
/*  83 */         getInnerState(j, false).addCount(indices[j], value.doubleValue(), i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCount(int key1, double value, int i)
/*     */   {
/*  90 */     if (value == 0.0D) return;
/*  91 */     addCount(key1, value, i, this.decomp);
/*     */   }
/*     */   
/*     */   public double score(int key1, int i) {
/*  95 */     Comparable key = getEmissionStateSpace().get(key1);
/*  96 */     double sc = score(key1, i, false, this.decomp);
/*  97 */     return sc;
/*     */   }
/*     */   
/*     */   public boolean transferCountsToProbs(double pseudo) {
/* 101 */     EmissionState[] stats = getMemberStates(true);
/* 102 */     boolean chnged = false;
/* 103 */     for (int i = 0; i < stats.length; i++) {
/* 104 */       chnged = (chnged) || (((HaplotypeEmissionState)stats[i]).transferCountsToProbs(pseudo));
/*     */     }
/*     */     
/* 107 */     return chnged;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/AlleleCopyPairEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */