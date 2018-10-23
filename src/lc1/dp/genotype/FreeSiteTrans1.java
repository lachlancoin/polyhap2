/*    */ package lc1.dp.genotype;
/*    */ 
/*    */ import java.util.List;
/*    */ import lc1.dp.Dirichlet;
/*    */ import lc1.dp.State;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FreeSiteTrans1
/*    */   extends SiteTransitions
/*    */ {
/*    */   public FreeSiteTrans1(List<Integer> loc, List<State> states, Double exp_p1, Double r, int length) {}
/*    */   
/*    */   public FreeSiteTrans1(FreeSiteTrans1 trans_init) {}
/*    */   
/*    */   public FreeSiteTrans1(List<State> states, Integer noSnps)
/*    */   {
/* 24 */     this(null, states, null, null, noSnps.intValue());
/*    */   }
/*    */   
/*    */   public SiteTransitions clone()
/*    */   {
/* 29 */     return new FreeSiteTrans1(this);
/*    */   }
/*    */   
/* 32 */   public void transferTransitions(double pseudoTrans, double pseudoCExp) { throw new Error("Unresolved compilation problems: \n\tThe method transferTransitions(double, double) of type FreeSiteTrans1 must override a superclass method\n\tCannot cast from TransitionProbs to FreeTransitionProbs1\n\tCannot cast from TransitionProbs to FreeTransitionProbs1\n"); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initialise(double[] u, Dirichlet dir1, boolean writeProbs)
/*    */     throws Exception
/*    */   {
/* 40 */     throw new Error("Unresolved compilation problems: \n\tThe method initialise(double[], Dirichlet, boolean) of type FreeSiteTrans1 must override a superclass method\n\tType mismatch: cannot convert from FreeTransitionProbs1 to TransitionProbs\n\tstates cannot be resolved\n\tstates cannot be resolved\n");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/FreeSiteTrans1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */