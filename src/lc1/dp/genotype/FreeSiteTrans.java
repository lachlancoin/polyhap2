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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FreeSiteTrans
/*    */   extends SiteTransitions
/*    */ {
/*    */   public FreeSiteTrans(List<Integer> loc, List<State> states, Double exp_p1, Double r, int length) {}
/*    */   
/*    */   public FreeSiteTrans(FreeSiteTrans trans_init) {}
/*    */   
/*    */   public SiteTransitions clone()
/*    */   {
/* 27 */     return new FreeSiteTrans(this);
/*    */   }
/*    */   
/* 30 */   public void transferTransitions(double pseudoTrans, double pseudoCExp) { throw new Error("Unresolved compilation problems: \n\tThe method transferTransitions(double, double) of type FreeSiteTrans must override a superclass method\n\tCannot cast from TransitionProbs to FreeTransitionProbs1\n\tCannot cast from TransitionProbs to FreeTransitionProbs\n"); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initialise(double[] u, Dirichlet dir1, boolean writeProbs)
/*    */     throws Exception
/*    */   {
/* 38 */     throw new Error("Unresolved compilation problems: \n\tThe method initialise(double[], Dirichlet, boolean) of type FreeSiteTrans must override a superclass method\n\tloc cannot be resolved\n\tloc cannot be resolved\n\texp_p1 cannot be resolved\n\tr cannot be resolved\n\tloc cannot be resolved\n\tloc cannot be resolved\n\tType mismatch: cannot convert from FreeTransitionProbs to TransitionProbs\n\tstates cannot be resolved\n\tstates cannot be resolved\n");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/FreeSiteTrans.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */