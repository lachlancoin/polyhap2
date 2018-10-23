/*   */ package lc1.dp.states;
/*   */ 
/*   */ public abstract class AbstractCachedEmissionState extends CompoundState
/*   */ {
/*   */   public AbstractCachedEmissionState(CompoundState state) {
/* 6 */     super(state.getName() + "c", state.adv);
/*   */   }
/*   */   
/*   */   public abstract void transferCountsToMemberStates();
/*   */   
/*   */   public abstract void refreshSiteEmissions();
/*   */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/AbstractCachedEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */