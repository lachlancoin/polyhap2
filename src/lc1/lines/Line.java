/*    */ package lc1.lines;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ public class Line implements Comparable
/*    */ {
/*  9 */   Integer allocation = null;
/*    */   
/*    */   public void setAllocation(int val) {
/* 12 */     this.allocation = Integer.valueOf(val);
/*    */   }
/*    */   
/* 15 */   public int getAllocation() { return this.allocation.intValue(); }
/*    */   
/* 17 */   List<Long> loc = new ArrayList();
/* 18 */   List<Integer> pos = new ArrayList();
/*    */   
/*    */ 
/*    */ 
/*    */   public Line(Long firstLoc, Integer firstPos)
/*    */   {
/* 24 */     this.pos.add(firstPos);
/* 25 */     this.loc.add(firstLoc);
/*    */   }
/*    */   
/* 28 */   public Iterator<Integer> getPositions() { return this.pos.iterator(); }
/*    */   
/*    */   public Long getEnd() {
/* 31 */     return (Long)this.loc.get(this.loc.size() - 1);
/*    */   }
/*    */   
/* 34 */   public int size() { return this.loc.size(); }
/*    */   
/*    */   public void add(Long l, Integer i)
/*    */   {
/* 38 */     this.loc.add(l);
/* 39 */     this.pos.add(i);
/*    */   }
/*    */   
/*    */   public Long getStart() {
/* 43 */     return (Long)this.loc.get(0);
/*    */   }
/*    */   
/*    */   public String toString() {
/* 47 */     return this.loc.size();
/*    */   }
/*    */   
/*    */   public int compareTo(Object o) {
/* 51 */     Line line1 = (Line)o;
/* 52 */     if (getStart() != line1.getStart()) {
/* 53 */       return getStart().longValue() < line1.getStart().longValue() ? -1 : 1;
/*    */     }
/* 55 */     if (getEnd() != line1.getEnd()) {
/* 56 */       return getEnd().longValue() < line1.getEnd().longValue() ? -1 : 1;
/*    */     }
/* 58 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/lines/Line.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */