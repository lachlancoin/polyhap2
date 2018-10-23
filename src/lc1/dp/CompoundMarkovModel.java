/*    */ package lc1.dp;
/*    */ 
/*    */ public abstract class CompoundMarkovModel extends MarkovModel { public abstract MarkovModel getMarkovModel(int paramInt);
/*    */   
/*    */   public abstract int noCopies();
/*    */   
/*  7 */   public CompoundMarkovModel(MarkovModel m, MarkovModel pseudo) { super(m, pseudo); }
/*    */   
/*    */   public CompoundMarkovModel(String name, int noSnps)
/*    */   {
/* 11 */     super(name, SimpleDistribution.noOffset, SimpleDistribution.noOffset, noSnps);
/*    */   }
/*    */   
/*    */   public CompoundMarkovModel(String name, SimpleDistribution start, SimpleDistribution end, int noSnps) {
/* 15 */     super(name, new DotState("!", start, end), noSnps);
/*    */   }
/*    */   
/* 18 */   public CompoundMarkovModel(String name, DotState magic, int noSnps) { super(name, magic, noSnps); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/CompoundMarkovModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */