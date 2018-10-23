/*    */ package lc1.dp.genotype.io.scorable;
/*    */ 
/*    */ import lc1.dp.EmissionState;
/*    */ import lc1.dp.MarkovModel;
/*    */ 
/*    */ public class StateScorableObject extends StateIndices
/*    */ {
/*    */   private int[] state_indices;
/*    */   
/*    */   public StateScorableObject(ScorableObject obj, MarkovModel hmm)
/*    */   {
/* 12 */     this.state_indices = new int[obj.length()];
/* 13 */     for (int i = 0; i < this.state_indices.length; i++) {
/* 14 */       this.state_indices[i] = hmm.getEmissionStateSpaceIndex(obj.getElement(i));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public int getBestIndex(EmissionState st, int i)
/*    */   {
/* 21 */     return this.state_indices[i];
/*    */   }
/*    */   
/* 24 */   private int getIndex(int i) { return this.state_indices[i]; }
/*    */   
/*    */ 
/*    */ 
/*    */   public int length()
/*    */   {
/* 30 */     return this.state_indices.length;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final double[] score(EmissionState state, boolean logspace)
/*    */   {
/* 38 */     double[] score = new double[length()];
/* 39 */     for (int i = 0; i < score.length; i++) {
/* 40 */       int val = getIndex(i);
/* 41 */       double sc = state.score(val, i);
/*    */       
/* 43 */       score[i] = (logspace ? Math.log(sc) : sc);
/*    */     }
/* 45 */     return score;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addCount(EmissionState k, Double double1, int i)
/*    */   {
/* 52 */     k.addCount(getIndex(i), double1, i);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/StateScorableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */