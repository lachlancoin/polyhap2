/*    */ package lc1.dp.genotype.io.trio;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import lc1.dp.model.PairMarkovModel;
/*    */ import lc1.dp.states.EmissState;
/*    */ import lc1.dp.states.EmissionState;
/*    */ import lc1.dp.states.PairEmissionState;
/*    */ import lc1.dp.states.State;
/*    */ 
/*    */ 
/*    */ public class HalfTrioEmissionState
/*    */   extends PairEmissionState
/*    */ {
/*    */   EmissionState thirdState;
/*    */   
/*    */   public HalfTrioEmissionState(List<EmissionState> st, PairMarkovModel parent, boolean decompose)
/*    */   {
/* 19 */     super(st, ((EmissState)parent.getState(1)).getEmissionStateSpace(), decompose, null, null);
/* 20 */     State swtch = (State)st.get(1);
/*    */     
/* 22 */     EmissionState thSt = ((PairEmissionState)getInnerState(0, true)).getMemberStates(false)[(swtch.getIndex() - 1)];
/*    */     
/*    */ 
/*    */ 
/* 26 */     this.thirdState = new PairEmissionState(Arrays.asList(new EmissionState[] { thSt }), 
/* 27 */       ((EmissionState)st.get(1)).getEmissionStateSpace(), 
/* 28 */       decompose, null, null);
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
/*    */   public EmissionState getInnerState(int j, boolean real)
/*    */   {
/* 43 */     if (real) return super.getInnerState(j, real);
/* 44 */     if (j == 1) return this.thirdState;
/* 45 */     return super.getInnerState(j, real);
/*    */   }
/*    */   
/*    */ 
/*    */   public EmissionState[] getMemberStates(boolean real)
/*    */   {
/* 51 */     if (real) return super.getMemberStates(real);
/* 52 */     EmissionState[] res = new EmissionState[2];
/* 53 */     res[0] = getInnerState(0, real);
/* 54 */     res[1] = getInnerState(1, real);
/* 55 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/trio/HalfTrioEmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */