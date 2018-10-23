/*    */ package lc1.dp.genotype.io.scorable;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import lc1.dp.EmissionState;
/*    */ import lc1.dp.MarkovModel;
/*    */ import lc1.dp.PairEmissionState;
/*    */ import lc1.dp.genotype.CachedEmissionState;
/*    */ 
/*    */ public class StateScorableObjectDistribution extends StateIndices
/*    */ {
/*    */   private double[][] state_indices;
/*    */   private int[] best_index;
/*    */   private int len;
/*    */   double[] distribution;
/*    */   
/*    */   public StateScorableObjectDistribution(ScorableObject obj, MarkovModel hmm)
/*    */   {
/* 18 */     this.len = hmm.getEmissionStateSpace().size();
/* 19 */     this.state_indices = new double[obj.length()][this.len];
/* 20 */     this.best_index = new int[obj.length()];
/* 21 */     this.distribution = new double[this.len];
/* 22 */     for (int i = 0; i < this.state_indices.length; i++) {
/* 23 */       this.best_index[i] = hmm.getEmissionStateSpaceIndex(obj.getElement(i));
/* 24 */       this.state_indices[i] = hmm.getEmissionStateSpaceDistribution(this.best_index[i]);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public int getBestIndex(EmissionState state, int i)
/*    */   {
/* 32 */     double sum = calcDistribution(state, i);
/*    */     try {
/* 34 */       return lc1.dp.genotype.io.Constants.sample(this.distribution, sum);
/*    */     }
/*    */     catch (Exception exc) {
/* 37 */       exc.printStackTrace(); }
/* 38 */     return 0;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public int length()
/*    */   {
/* 47 */     return this.state_indices.length;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public final double[] score(EmissionState state, boolean logspace)
/*    */   {
/* 55 */     double[] score = new double[length()];
/* 56 */     for (int i = 0; i < score.length; i++) {
/* 57 */       double sc = 0.0D;
/* 58 */       double[] indi = this.state_indices[i];
/* 59 */       for (int j = 0; j < this.len; j++) {
/* 60 */         double prob_i = indi[j];
/* 61 */         if (prob_i > 0.0D) sc += prob_i * state.score(j, i);
/*    */       }
/* 63 */       score[i] = (logspace ? Math.log(sc) : sc);
/*    */     }
/* 65 */     return score;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addCount(EmissionState state, Double double1, int i)
/*    */   {
/* 72 */     EmissionState[] dist = ((PairEmissionState)((CachedEmissionState)state).innerState).dist;
/* 73 */     double sum = calcDistribution(state, i);
/* 74 */     for (int j = 0; j < this.distribution.length; j++) {
/* 75 */       double prob_j = this.distribution[j] / sum;
/* 76 */       if (prob_j > 0.0D) state.addCount(j, Double.valueOf(prob_j * double1.doubleValue()), i);
/*    */     }
/*    */   }
/*    */   
/*    */   private double calcDistribution(EmissionState state, int i)
/*    */   {
/* 82 */     double sum = 0.0D;
/* 83 */     Arrays.fill(this.distribution, 0.0D);
/* 84 */     double[] indi = this.state_indices[i];
/* 85 */     for (int j = 0; j < indi.length; j++) {
/* 86 */       double prob_j = indi[j];
/* 87 */       if (prob_j > 0.0D) {
/* 88 */         this.distribution[j] = (prob_j * state.score(j, i));
/* 89 */         sum += this.distribution[j];
/*    */       }
/*    */     }
/* 92 */     return sum;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/StateScorableObjectDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */