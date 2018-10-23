/*    */ package lc1.dp.transition;
/*    */ 
/*    */ import lc1.stats.Dirichlet;
/*    */ import lc1.stats.Sampler;
/*    */ 
/*    */ public class BetweenWithinTransitionProbs3
/*    */   extends BetweenWithinTransitionProbs1
/*    */ {
/*    */   public double getBetweeenGroupTrans(int from, int groupFrom, int groupTo)
/*    */   {
/* 11 */     return this.transBetweenGroups.getTransition(from, 
/* 12 */       groupFrom, 
/* 13 */       groupTo);
/*    */   }
/*    */   
/*    */   public void addGroupTransCount(int from, int groupFrom, int groupTo, double val) {
/* 17 */     this.transBetweenGroups.addCount(
/* 18 */       from, 
/* 19 */       groupFrom, 
/* 20 */       groupTo, val);
/*    */   }
/*    */   
/*    */   public AbstractTransitionProbs makeTransBetweenGroups(Object clazz, Sampler samplerFirst, Sampler exp_p, int no_states) throws Exception {
/* 24 */     AbstractTransitionProbs res = ExponentialTransitionProbs.get(clazz, samplerFirst, exp_p, 
/*    */     
/* 26 */       no_states);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 31 */     return res;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public AbstractTransitionProbs clone(boolean swtch)
/*    */   {
/* 38 */     if (swtch) return new FreeTransitionProbs1(this);
/* 39 */     return new BetweenWithinTransitionProbs3(this, swtch);
/*    */   }
/*    */   
/*    */   public AbstractTransitionProbs makeTransBetweenGroups(AbstractTransitionProbs transBetweenGroups, boolean swtch, int[] statesToGroup)
/*    */   {
/* 44 */     return transBetweenGroups.clone(statesToGroup);
/*    */   }
/*    */   
/*    */   public BetweenWithinTransitionProbs3(BetweenWithinTransitionProbs1 probs1) {
/* 48 */     super(probs1, false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BetweenWithinTransitionProbs3(Sampler samplerFirst, Sampler[] samplers, int[] stateToGroup, int[] stateToIndexWithinGroup, Dirichlet[] exp_p, Object[] clazz)
/*    */     throws Exception
/*    */   {
/* 61 */     super(samplerFirst, samplers, stateToGroup, stateToIndexWithinGroup, exp_p, clazz);
/*    */   }
/*    */   
/*    */   public BetweenWithinTransitionProbs3(BetweenWithinTransitionProbs3 probs, boolean swtch) {
/* 65 */     super(probs, swtch);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/BetweenWithinTransitionProbs3.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */