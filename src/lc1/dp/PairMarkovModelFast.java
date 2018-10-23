/*    */ package lc1.dp;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class PairMarkovModelFast
/*    */   extends PairMarkovModel
/*    */ {
/*    */   Permutations perm;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean exclude(Object obj, Object previous)
/*    */   {
/* 24 */     throw new Error("Unresolved compilation problem: \n\tThe method exclude(Object, Object) of type PairMarkovModelFast must override a superclass method\n");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PairMarkovModelFast(MarkovModel m1, int no_copies) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PairMarkovModelFast(PairMarkovModelFast m1) {}
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getTransitionScore(State from, State to, int endIndex)
/*    */   {
/* 45 */     throw new Error("Unresolved compilation problems: \n\tThe method getTransitionScore(State, State, int) of type PairMarkovModelFast must override a superclass method\n\tThe method updateFrom(int) in the type PairMarkovModel is not applicable for the arguments (State)\n\tThe method updateTo(int) in the type PairMarkovModel is not applicable for the arguments (State)\n\tCannot invoke getTransitionScore(int, int, int) on the array type MarkovModel[]\n\tCannot invoke getTransitionScore(int, int, int) on the array type MarkovModel[]\n\tCannot invoke getTransitionScore(int, int, int) on the array type MarkovModel[]\n\tCannot invoke getTransitionScore(int, int, int) on the array type MarkovModel[]\n\tCannot invoke getTransitionScore(int, int, int) on the array type MarkovModel[]\n");
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
/*    */   public EmissionState[] disambiguateEmissionStateOrder(PairEmissionState state, EmissionState[] previous, int i, boolean sample)
/*    */   {
/* 65 */     throw new Error("Unresolved compilation problems: \n\tThe method disambiguateEmissionStateOrder(PairEmissionState, EmissionState[], int, boolean) of type PairMarkovModelFast must override a superclass method\n\tThe method get(ComparableArray) in the type Permutations is not applicable for the arguments (EmissionState[])\n\tCannot invoke getTransitionScore(EmissionState, EmissionState, int) on the array type MarkovModel[]\n\tThe method select(double[], double) is undefined for the type Fastphase\n");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/PairMarkovModelFast.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */