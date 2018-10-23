/*    */ package lc1.util;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public abstract class CopyEnumerator {
/*    */   final int no_copies;
/*    */   Comparable[] list;
/*    */   
/*    */   public abstract Iterator<Comparable> getPossibilities(int paramInt);
/*    */   
/*    */   public abstract void doInner(Comparable[] paramArrayOfComparable);
/*    */   
/*    */   public CopyEnumerator(int length) {
/* 14 */     this.list = new Comparable[length];
/* 15 */     this.no_copies = length;
/*    */   }
/*    */   
/* 18 */   public void run() { inner(0); }
/*    */   
/*    */   public abstract boolean exclude(Object paramObject1, Object paramObject2);
/*    */   
/*    */   private void inner(int depth) {
/* 23 */     for (Iterator<Comparable> it = getPossibilities(depth); it.hasNext();) {
/* 24 */       Comparable nxt = (Comparable)it.next();
/* 25 */       if (!exclude(nxt, depth == 0 ? null : this.list[(depth - 1)])) {
/* 26 */         this.list[depth] = nxt;
/* 27 */         if (depth + 1 == this.list.length) {
/* 28 */           doInner(this.list);
/*    */         }
/*    */         else {
/* 31 */           inner(depth + 1);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/util/CopyEnumerator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */