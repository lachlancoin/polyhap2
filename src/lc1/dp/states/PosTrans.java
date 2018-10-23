/*    */ package lc1.dp.states;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class PosTrans
/*    */   extends PosTrans1
/*    */ {
/*  9 */   PosTrans(int pos, int ind, int loc, String snp_id) { super(pos, ind, loc, snp_id); }
/*    */   
/*    */   public int compareTo(Object o) {
/* 12 */     int possc = this.pos.compareTo(((PosTrans)o).pos);
/* 13 */     if (possc == 0) return this.snp_id.compareTo(((PosTrans)o).snp_id);
/* 14 */     return possc;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/PosTrans.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */