/*    */ package lc1.dp;
/*    */ 
/*    */ 
/*    */ public class Term
/*    */   extends AbstractTerm
/*    */ {
/*    */   public int j;
/*    */   
/*  9 */   public int getBestPath() { return this.j; }
/*    */   
/*    */   protected Term(int j, int i, double sc) {
/* 12 */     super(i, sc);
/* 13 */     this.j = j;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/Term.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */