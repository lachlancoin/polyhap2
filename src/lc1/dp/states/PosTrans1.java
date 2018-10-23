/*    */ package lc1.dp.states;
/*    */ 
/*    */ class PosTrans1 implements Comparable
/*    */ {
/*    */   Integer pos;
/*    */   int[] ind_loc;
/*    */   String snp_id;
/*    */   
/*    */   public String toString()
/*    */   {
/* 11 */     return this.pos + "_" + this.ind_loc[0] + "_" + this.ind_loc[1];
/*    */   }
/*    */   
/* 14 */   public void replace(PosTrans1 pt) { this.ind_loc = pt.ind_loc; }
/*    */   
/*    */   PosTrans1(int pos, int ind, int loc, String snp_id)
/*    */   {
/* 18 */     this.pos = Integer.valueOf(pos);
/* 19 */     this.snp_id = snp_id;
/* 20 */     this.ind_loc = new int[] { ind, loc };
/*    */   }
/*    */   
/* 23 */   public int compareTo(Object o) { return this.pos.compareTo(((PosTrans1)o).pos); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/PosTrans1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */