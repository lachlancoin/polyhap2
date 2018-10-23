/*    */ package lc1.dp;
/*    */ 
/*    */ public abstract class CompoundState extends EmissionState {
/*    */   public CompoundState(String name, int adv) {
/*  5 */     super(name, adv);
/*    */   }
/*    */   
/*    */   public abstract EmissionState[] getMemberStates();
/*    */   
/* 10 */   public CompoundState(CompoundState st1) { this(st1.name, st1.adv); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/CompoundState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */