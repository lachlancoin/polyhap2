/*    */ package lc1.dp;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class EmissionState
/*    */   extends State
/*    */ {
/* 10 */   protected boolean changedParams = true;
/*    */   
/*    */   protected final SimpleDistribution lengthDistrib;
/*    */   
/*    */   public EmissionState(String name, int adv)
/*    */   {
/* 16 */     super(name, adv);
/* 17 */     this.lengthDistrib = new SimpleDistribution(new int[] { adv }, new double[] { 1.0D });
/*    */   }
/*    */   
/*    */   public abstract void addCount(int paramInt1, Double paramDouble, int paramInt2);
/*    */   
/*    */   public SimpleDistribution adv(State st) {
/* 23 */     return this.lengthDistrib;
/*    */   }
/*    */   
/*    */   public EmissionState(EmissionState st1) {
/* 27 */     this(st1.name, st1.adv);
/*    */   }
/*    */   
/* 30 */   public boolean changedParams() { return this.changedParams; }
/*    */   
/*    */   public void setChangedParams(boolean b) {
/* 33 */     this.changedParams = b;
/*    */   }
/*    */   
/*    */   public abstract void print(PrintWriter paramPrintWriter, String paramString);
/*    */   
/* 38 */   public int getClassId() { return 1; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract void initialiseCounts();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract double KLDistance(EmissionState paramEmissionState);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public abstract Object sample(int paramInt);
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract double score(int paramInt1, int paramInt2);
/*    */   
/*    */ 
/*    */ 
/*    */   public abstract void setRandom(double paramDouble, boolean paramBoolean);
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString(int i)
/*    */   {
/* 67 */     return "";
/*    */   }
/*    */   
/* 70 */   public String toString() { return getName(); }
/*    */   
/*    */   public abstract Object mostLikely(int paramInt);
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/EmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */