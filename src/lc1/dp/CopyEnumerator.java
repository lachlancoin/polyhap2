/*    */ package lc1.dp;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public abstract class CopyEnumerator
/*    */ {
/*    */   final int no_copies;
/*    */   Comparable[] list;
/*    */   
/*    */   public abstract Iterator<Comparable> getPossibilities(int paramInt);
/*    */   
/*    */   public abstract void doInner(Comparable[] paramArrayOfComparable);
/*    */   
/*    */   public CopyEnumerator(int length)
/*    */   {
/* 16 */     this.list = new Comparable[length];
/* 17 */     this.no_copies = length;
/*    */   }
/*    */   
/* 20 */   public void run() { inner(0); }
/*    */   
/*    */   public abstract boolean exclude(Object paramObject1, Object paramObject2);
/*    */   
/*    */   private void inner(int depth) {
/* 25 */     for (Iterator<Comparable> it = getPossibilities(depth); it.hasNext();) {
/* 26 */       Comparable nxt = (Comparable)it.next();
/* 27 */       if (!exclude(nxt, depth == 0 ? null : this.list[(depth - 1)])) {
/* 28 */         this.list[depth] = nxt;
/* 29 */         if (depth + 1 == this.list.length) {
/* 30 */           doInner(this.list);
/*    */         }
/*    */         else {
/* 33 */           inner(depth + 1);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/CopyEnumerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */