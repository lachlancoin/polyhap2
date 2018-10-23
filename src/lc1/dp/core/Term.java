/*    */ package lc1.dp.core;
/*    */ 
/*    */ 
/*    */ public class Term
/*    */   extends AbstractTerm
/*    */ {
/*    */   public int j;
/*    */   
/*    */ 
/* 10 */   public int getBestPath() { return this.j; }
/*    */   
/*    */   protected Term(int j, int i, double sc) {
/* 13 */     super(i, sc);
/* 14 */     this.j = j;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/Term.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */