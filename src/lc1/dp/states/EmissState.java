/*    */ package lc1.dp.states;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.List;
/*    */ import lc1.dp.emissionspace.EmissionStateSpace;
/*    */ import lc1.stats.SimpleDistribution;
/*    */ 
/*    */ public abstract class EmissState extends State
/*    */ {
/*    */   public abstract int noSnps();
/*    */   
/*    */   public EmissState(String st, int i)
/*    */   {
/* 14 */     super(st, i);
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract EmissionStateSpace getEmissionStateSpace();
/*    */   
/*    */ 
/*    */   public abstract SimpleDistribution adv(State paramState);
/*    */   
/*    */ 
/*    */   public abstract boolean changedParams();
/*    */   
/*    */ 
/*    */   public abstract void setChangedParams(boolean paramBoolean);
/*    */   
/*    */   public abstract void print(PrintWriter paramPrintWriter, String paramString, List<Integer> paramList);
/*    */   
/*    */   public abstract void initialiseCounts();
/*    */   
/*    */   public abstract Object clone();
/*    */   
/*    */   public abstract boolean transferCountsToProbs(double paramDouble);
/*    */   
/*    */   public abstract Integer getFixedInteger(int paramInt);
/*    */   
/*    */   public abstract boolean isFixed();
/*    */   
/*    */   public abstract double score(EmissionState paramEmissionState, int paramInt1, int paramInt2, int paramInt3);
/*    */   
/*    */   public final double score(EmissionState hmm_state, int i_hmm, int stSpSize)
/*    */   {
/* 45 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract void setFixedIndex(int paramInt1, int paramInt2);
/*    */   
/*    */ 
/*    */   public abstract int length();
/*    */   
/*    */ 
/*    */   public abstract double[] score(EmissionState paramEmissionState, boolean paramBoolean);
/*    */   
/*    */ 
/*    */   public abstract void addCount(EmissionState paramEmissionState, Double paramDouble, int paramInt1, int paramInt2, double paramDouble1);
/*    */   
/*    */   public final void addCount(EmissionState hmm_state, Double double1, int i_hmm, double weight)
/*    */   {
/* 62 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public abstract int noCopies();
/*    */   
/*    */   public abstract void reverse();
/*    */   
/*    */   public abstract String getUnderlyingData(int paramInt);
/*    */   
/*    */   public abstract int getParamIndex();
/*    */   
/*    */   public abstract void removeAll(List<Integer> paramList);
/*    */   
/*    */   public abstract void setAsMissing(List<Integer> paramList, double paramDouble);
/*    */   
/*    */   public abstract void applyAlias(int[] paramArrayOfInt);
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/EmissState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */