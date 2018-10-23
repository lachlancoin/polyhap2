/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.List;
/*    */ import lc1.dp.data.collection.DataCollection;
/*    */ import lc1.dp.data.collection.LikelihoodDataCollection;
/*    */ import lc1.dp.states.EmissState;
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
/*    */ public abstract class RatioDataCollection
/*    */   extends LikelihoodDataCollection
/*    */ {
/*    */   public final boolean bgIsAvgOfFg;
/*    */   protected EmissState bg;
/*    */   
/*    */   public RatioDataCollection(RatioDataCollection collection) {}
/*    */   
/*    */   public RatioDataCollection(boolean bgIsAvgOfFg) {}
/*    */   
/*    */   public void reverse()
/*    */   {
/* 30 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */   protected List<Integer> getBackgroundCN()
/*    */   {
/* 35 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */   public void makeBg(double[] prob)
/*    */   {
/* 40 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */ 
/*    */   public void make()
/*    */   {
/* 46 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */   public void make(double[] bg)
/*    */   {
/* 51 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void printDist(PrintWriter pw)
/*    */   {
/* 61 */     throw new Error("Unresolved compilation problems: \n\tIncompatible conditional operand types EmissState and HaplotypeEmissionState\n\tCannot cast from EmissState to HaplotypeEmissionState\n");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void maximisationStep(double[] pseudo, int i)
/*    */   {
/* 69 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void makeFix()
/*    */   {
/* 79 */     throw new Error("Unresolved compilation problem: \n");
/*    */   }
/*    */   
/*    */   public RatioDataCollection(DataCollection obj) {}
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/RatioDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */