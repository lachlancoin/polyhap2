/*    */ package lc1.dp.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import lc1.dp.states.State;
/*    */ import lc1.dp.transition.ExponentialTransitionProbs;
/*    */ import lc1.dp.transition.FreeTransitionProbs1;
/*    */ import lc1.stats.Dirichlet;
/*    */ import lc1.stats.PermutationSampler;
/*    */ import lc1.stats.Sampler;
/*    */ import lc1.stats.SimpleExtendedDistribution;
/*    */ 
/*    */ public class FreeSiteTrans1 extends SiteTransitions
/*    */ {
/*    */   final Object clazz;
/*    */   
/*    */   public FreeSiteTrans1(List<Integer> loc, List<State> states, Double[] exp_p1, Double[] r, int length, Object clazz)
/*    */   {
/* 18 */     super(loc, states.size() - 1, exp_p1, r, length);
/* 19 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */ 
/*    */   public FreeSiteTrans1(FreeSiteTrans1 trans_init, boolean swtch)
/*    */   {
/* 25 */     super(trans_init, swtch);
/* 26 */     this.clazz = trans_init.clazz;
/*    */   }
/*    */   
/*    */   public FreeSiteTrans1(List<State> states, Integer noSnps, Object clazz) {
/* 30 */     this(null, states, null, null, noSnps.intValue(), clazz);
/*    */   }
/*    */   
/*    */   public FreeSiteTrans1(Integer noSnps, int i) throws Exception {
/* 34 */     super(noSnps.intValue(), i);
/* 35 */     this.clazz = null;
/*    */   }
/*    */   
/*    */   public SiteTransitions clone(boolean swtche)
/*    */   {
/* 40 */     return new FreeSiteTrans1(this, swtche);
/*    */   }
/*    */   
/*    */   public void initialise(double[] rel1, double permute)
/*    */     throws Exception
/*    */   {
/*    */     Sampler dir1;
/*    */     Sampler dir1;
/* 48 */     if (permute > 0.1D) {
/* 49 */       double[] rel = new double[rel1.length];
/*    */       
/*    */ 
/*    */ 
/* 53 */       for (int j = 0; j < rel.length; j++) {
/* 54 */         double mult = 1.0D;
/* 55 */         rel1[j] *= 
/* 56 */           Math.pow(permute, j);
/*    */       }
/*    */       
/* 59 */       SimpleExtendedDistribution.normalise(rel);
/* 60 */       dir1 = new PermutationSampler(rel1, lc1.util.Constants.u_global(0)[1]);
/*    */     } else {
/* 62 */       dir1 = new Dirichlet(rel1, lc1.util.Constants.u_global(0)[1]);
/*    */     }
/*    */     
/* 65 */     for (int i = 1; i < this.transProbs.length; i++) {
/* 66 */       this.transProbs[i] = 
/* 67 */         ExponentialTransitionProbs.get(this.clazz, dir1, getExp_p(i), this.len);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 76 */       if ((this.transProbs[i] instanceof FreeTransitionProbs1)) {
/* 77 */         ((FreeTransitionProbs1)this.transProbs[i]).transitionsOut[0] = null;
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/FreeSiteTrans1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */