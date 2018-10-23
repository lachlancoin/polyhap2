/*    */ package lc1.dp.genotype.io.trio;
/*    */ 
/*    */ import java.util.List;
/*    */ import lc1.dp.model.PairMarkovModel;
/*    */ import lc1.dp.states.EmissState;
/*    */ import lc1.dp.states.EmissionState;
/*    */ import lc1.dp.states.PairEmissionState;
/*    */ import lc1.dp.states.State;
/*    */ 
/*    */ 
/*    */ public class TrioEmissionState2
/*    */   extends PairEmissionState
/*    */ {
/*    */   final EmissionState thirdState;
/*    */   
/*    */   public TrioEmissionState2(List<EmissionState> dist, PairMarkovModel parent, boolean decompose)
/*    */   {
/* 18 */     super(dist, ((EmissState)parent.getState(1)).getEmissionStateSpace(), decompose, null, null);
/* 19 */     PairEmissionState swtch = (PairEmissionState)dist.get(2);
/* 20 */     State swtch1 = swtch.getInnerState(0, true);
/* 21 */     State swtch2 = swtch.getInnerState(1, true);
/* 22 */     int index1 = swtch1.getIndex();
/* 23 */     int index2 = swtch2.getIndex();
/* 24 */     if (index1 == -1) {
/* 25 */       this.thirdState = swtch;
/*    */     }
/*    */     else {
/* 28 */       EmissionState third1 = ((PairEmissionState)getInnerState(0, true)).getMemberStates(false)[(index1 - 1)];
/* 29 */       EmissionState third2 = ((PairEmissionState)getInnerState(1, true)).getMemberStates(false)[(index2 - 1)];
/* 30 */       this.thirdState = ((PairMarkovModel)parent.getMarkovModel(0)).getCompoundState(new EmissionState[] { third1, third2 });
/*    */     }
/* 32 */     if (this.thirdState == null) { throw new RuntimeException("!!");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public EmissionState getInnerState(int j, boolean real)
/*    */   {
/*    */     EmissionState res;
/*    */     EmissionState res;
/* 41 */     if (real) { res = super.getInnerState(j, real); } else { EmissionState res;
/* 42 */       if (j == 2) res = this.thirdState; else
/* 43 */         res = super.getInnerState(j, real); }
/* 44 */     if (res == null) throw new RuntimeException("!!");
/* 45 */     return res;
/*    */   }
/*    */   
/*    */ 
/*    */   public EmissionState[] getMemberStates(boolean real)
/*    */   {
/* 51 */     if (real) return super.getMemberStates(real);
/* 52 */     EmissionState[] res = new EmissionState[3];
/* 53 */     res[0] = getInnerState(0, real);
/* 54 */     res[1] = getInnerState(1, real);
/* 55 */     res[2] = getInnerState(2, real);
/* 56 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/trio/TrioEmissionState2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */