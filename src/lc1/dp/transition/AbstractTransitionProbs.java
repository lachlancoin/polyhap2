/*    */ package lc1.dp.transition;
/*    */ 
/*    */ import com.braju.format.Format;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ import lc1.stats.StateDistribution;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractTransitionProbs
/*    */ {
/*    */   public static String transform(double prob, double dist)
/*    */   {
/* 19 */     return Format.sprintf("%5.3g ", new Object[] { Double.valueOf(-Math.log(prob) / dist) });
/*    */   }
/*    */   
/*    */   public abstract double getTransition(int paramInt1, int paramInt2);
/*    */   
/*    */   public double getTransition(int index, int from, int to) {
/* 25 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */ 
/*    */   public abstract void initialiseCounts(boolean paramBoolean1, boolean paramBoolean2);
/*    */   
/*    */ 
/*    */   public abstract void transfer(double paramDouble1, double paramDouble2);
/*    */   
/*    */ 
/*    */   public abstract void transfer(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2);
/*    */   
/*    */ 
/*    */   public abstract Collection getDistributions();
/*    */   
/*    */ 
/*    */   public abstract void validate();
/*    */   
/*    */   public final void addCounts(StateDistribution[] observed)
/*    */   {
/* 45 */     int no_states = observed.length;
/* 46 */     for (int j = 0; j < no_states; j++) {
/* 47 */       StateDistribution dist1 = observed[j];
/* 48 */       int st = j;
/* 49 */       if (dist1 != null)
/* 50 */         for (int j1 = 0; j1 < no_states; j1++) {
/* 51 */           int state = j1;
/* 52 */           Double val = dist1.get(state);
/* 53 */           if (val.doubleValue() != 0.0D)
/*    */           {
/*    */ 
/*    */ 
/* 57 */             addCount(st, state, val.doubleValue()); }
/*    */         }
/*    */     } }
/*    */   
/*    */   public abstract AbstractTransitionProbs clone(boolean paramBoolean);
/*    */   
/*    */   public abstract void print(PrintWriter paramPrintWriter, Double[] paramArrayOfDouble, double paramDouble);
/*    */   
/*    */   public abstract void addCount(int paramInt1, int paramInt2, double paramDouble);
/*    */   
/*    */   public abstract int noStates();
/*    */   
/*    */   public abstract AbstractTransitionProbs clone(int[] paramArrayOfInt);
/*    */   
/* 71 */   public void addCount(int from, int groupFrom, int groupTo, double val) { throw new RuntimeException("!!"); }
/*    */   
/*    */   public String info() {
/* 74 */     return getClass().toString();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/AbstractTransitionProbs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */