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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FreeExpSiteTrans
/*    */   extends SiteTransitions
/*    */ {
/*    */   final Class transProbType;
/*    */   
/*    */   public FreeExpSiteTrans(List<Integer> loc, List<State> states, Double exp_p1, Double r, int length, Class transProbtype) {}
/*    */   
/*    */   public FreeExpSiteTrans(FreeExpSiteTrans trans_init) {}
/*    */   
/*    */   public SiteTransitions clone()
/*    */   {
/* 34 */     return new FreeExpSiteTrans(this);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static int[] group(List<State> states, int[] stateToGroup, int[] stateToIndexWithinGroup)
/*    */   {
/* 42 */     throw new Error("Unresolved compilation problems: \n\tThe method getBestIndex(int) is undefined for the type EmissionState\n\tThe method getBestIndex(int) is undefined for the type EmissionState\n\tThe method getEmissionStateSpace() is undefined for the type EmissionState\n");
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
/*    */   public void transferTransitions(double pseudoTrans, double pseudoCExp)
/*    */   {
/* 73 */     throw new Error("Unresolved compilation problems: \n\tThe method transferTransitions(double, double) of type FreeExpSiteTrans must override a superclass method\n\tCannot cast from TransitionProbs to FreeTransitionProbs1\n");
/*    */   }
/*    */   
/*    */ 
/*    */   public void initialise(double[] u, Dirichlet dir1, boolean writeProbs)
/*    */     throws Exception
/*    */   {
/* 80 */     throw new Error("Unresolved compilation problems: \n\tThe method initialise(double[], Dirichlet, boolean) of type FreeExpSiteTrans must override a superclass method\n\tstates cannot be resolved\n\tstates cannot be resolved\n\tstates cannot be resolved\n\tnumFounders cannot be resolved\n\tloc cannot be resolved\n\tloc cannot be resolved\n\texp_p1 cannot be resolved\n\tr cannot be resolved\n\tloc cannot be resolved\n\tloc cannot be resolved\n\tType mismatch: cannot convert from AbstractTransitionProbs to TransitionProbs\n\tstates cannot be resolved\n");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/FreeExpSiteTrans.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */