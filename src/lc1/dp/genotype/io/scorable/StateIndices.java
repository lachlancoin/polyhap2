/*    */ package lc1.dp.genotype.io.scorable;
/*    */ 
/*    */ import lc1.dp.EmissionState;
/*    */ 
/*    */ public abstract class StateIndices
/*    */ {
/*    */   public abstract int getBestIndex(EmissionState paramEmissionState, int paramInt);
/*    */   
/*    */   public abstract int length();
/*    */   
/*    */   public abstract double[] score(EmissionState paramEmissionState, boolean paramBoolean);
/*    */   
/*    */   public abstract void addCount(EmissionState paramEmissionState, Double paramDouble, int paramInt);
/*    */   
/*    */   public static StateIndices get(PhasedIntegerGenotypeData data, lc1.dp.MarkovModel genotypeHMM)
/*    */   {
/* 17 */     StateIndices ind = new StateScorableObjectDistribution(data, genotypeHMM);
/* 18 */     return ind;
/*    */   }
/*    */   
/*    */   public static StateIndices[] get(PhasedIntegerGenotypeData[] data2, lc1.dp.MarkovModel hmm2) {
/* 22 */     StateIndices[] res = new StateIndices[data2.length];
/* 23 */     for (int i = 0; i < res.length; i++) {
/* 24 */       res[i] = new StateScorableObject(data2[i], hmm2);
/*    */     }
/* 26 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/StateIndices.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */