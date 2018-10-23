/*    */ package lc1.dp.core;
/*    */ 
/*    */ public abstract class AbstractTerm {
/*    */   protected double score;
/*    */   public final int i;
/*    */   
/*    */   protected AbstractTerm(int i, double sc) {
/*  8 */     this.score = sc;
/*  9 */     this.i = i;
/*    */   }
/*    */   
/* 12 */   public double score() { return this.score; }
/*    */   
/*    */ 
/*    */ 
/* 16 */   public double scaleScore() { return this.score; }
/*    */   
/*    */   public abstract int getBestPath();
/*    */   
/* 20 */   public int compare(Term t) { if (this.score > t.score()) return -1;
/* 21 */     if (this.score == t.score()) return 0;
/* 22 */     return 1;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 26 */     return this.score + " " + this.i;
/*    */   }
/*    */   
/*    */   public void scale(double d)
/*    */   {
/* 31 */     this.score *= d;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/AbstractTerm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */