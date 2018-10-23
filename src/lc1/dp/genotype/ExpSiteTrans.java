/*    */ package lc1.dp.genotype;
/*    */ 
/*    */ import java.io.PrintWriter;
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
/*    */ public class ExpSiteTrans
/*    */   extends SiteTransitions
/*    */ {
/*    */   double r_prev_prev;
/*    */   
/*    */   public ExpSiteTrans(List<Integer> loc, List<State> states, Double exp_p1, Double r, int length) {}
/*    */   
/*    */   public ExpSiteTrans(ExpSiteTrans trans_init) {}
/*    */   
/*    */   public SiteTransitions clone()
/*    */   {
/* 30 */     return new ExpSiteTrans(this);
/*    */   }
/*    */   
/*    */   public void initialise(double[] u, Dirichlet dir1, boolean writeProbs) throws Exception {
/* 34 */     throw new Error("Unresolved compilation problems: \n\tThe method initialise(double[], Dirichlet, boolean) of type ExpSiteTrans must override a superclass method\n\tloc cannot be resolved\n\tloc cannot be resolved\n\texp_p1 cannot be resolved\n\tr cannot be resolved\n\tloc cannot be resolved\n\tloc cannot be resolved\n\tType mismatch: cannot convert from ExponentialTransitionProbs to TransitionProbs\n\tstates cannot be resolved\n");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void transferTransitions(double pseudoTrans, double pseudoCExp)
/*    */   {
/* 44 */     throw new Error("Unresolved compilation problems: \n\tThe method transferTransitions(double, double) of type ExpSiteTrans must override a superclass method\n\tr_prev cannot be resolved\n\tr_prev cannot be resolved\n\tr cannot be resolved\n\tCannot cast from TransitionProbs to FreeTransitionProbs1\n\tloc cannot be resolved\n\tloc cannot be resolved\n\tCannot cast from TransitionProbs to ExponentialTransitionProbs\n\tThe method evaluate(double) in the type PseudoDistribution is not applicable for the arguments (double[])\n\tr cannot be resolved\n\tr cannot be resolved\n\tloc cannot be resolved\n\tloc cannot be resolved\n\tr cannot be resolved\n\tCannot cast from TransitionProbs to ExponentialTransitionProbs\n");
/*    */   }
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
/*    */   public boolean converged()
/*    */   {
/* 82 */     throw new Error("Unresolved compilation problems: \n\tThe method converged() of type ExpSiteTrans must override a superclass method\n\tr_prev cannot be resolved\n\tr cannot be resolved\n\tr_prev cannot be resolved\n\tr cannot be resolved\n\tr_prev cannot be resolved\n\tr cannot be resolved\n\tr cannot be resolved\n\tr cannot be resolved\n\tr cannot be resolved\n\tr_prev cannot be resolved\n");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void print(PrintWriter sb, String sbS, List<State> states, Double[][] hittingProb)
/*    */   {
/* 91 */     throw new Error("Unresolved compilation problems: \n\tThe method print(PrintWriter, String, List<State>, Double[][]) of type ExpSiteTrans must override a superclass method\n\tThe method print(PrintWriter) in the type TransitionProbs is not applicable for the arguments (PrintWriter, Double[])\n\tCannot cast from TransitionProbs to ExponentialTransitionProbs\n\tloc cannot be resolved\n\tloc cannot be resolved\n\tThe method print(PrintWriter) in the type TransitionProbs is not applicable for the arguments (PrintWriter, Double[])\n");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/ExpSiteTrans.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */