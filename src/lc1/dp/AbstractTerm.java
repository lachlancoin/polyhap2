/*    */ package lc1.dp;
/*    */ 
/*    */ public abstract class AbstractTerm {
/*    */   protected double score;
/*    */   public final int i;
/*    */   
/*  7 */   protected AbstractTerm(int i, double sc) { this.score = sc;
/*  8 */     this.i = i;
/*    */   }
/*    */   
/* 11 */   public double score() { return this.score; }
/*    */   
/*    */ 
/*    */ 
/* 15 */   public double scaleScore() { return this.score; }
/*    */   
/*    */   public abstract int getBestPath();
/*    */   
/* 19 */   public int compare(Term t) { if (this.score > t.score()) return -1;
/* 20 */     if (this.score == t.score()) return 0;
/* 21 */     return 1;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 25 */     return this.score + " " + this.i;
/*    */   }
/*    */   
/*    */   public void scale(double d)
/*    */   {
/* 30 */     this.score *= d;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/AbstractTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */