/*    */ package lc1.sequenced;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Deletion implements Comparable
/*    */ {
/*    */   public int chr;
/*    */   public int start;
/*    */   public int end;
/*    */   public int id;
/*    */   
/*    */   public int mid()
/*    */   {
/* 15 */     return (int)Math.round((this.end + this.start) / 2.0D);
/*    */   }
/*    */   
/* 18 */   public String toString() { return this.id + "_" + this.chr + "_" + this.start + "_" + this.end; }
/*    */   
/*    */   Deletion(String st) {
/* 21 */     String[] str = st.split("_");
/*    */     try {
/* 23 */       this.id = Integer.parseInt(str[2]);
/*    */     } catch (Exception exc) {
/* 25 */       this.id = Integer.parseInt(str[1]);
/*    */     }
/* 27 */     this.end = Integer.parseInt(str[(str.length - 1)]);
/* 28 */     this.start = Integer.parseInt(str[(str.length - 2)]);
/* 29 */     this.chr = Integer.parseInt(str[(str.length - 3)]);
/*    */   }
/*    */   
/* 32 */   public void add(String st1, String st2) { if (st1.length() > 1)
/* 33 */       this.m.put(st1, Boolean.valueOf(st2.indexOf("homo") >= 0));
/*    */   }
/*    */   
/* 36 */   public Map<String, Boolean> m = new HashMap();
/*    */   
/* 38 */   public int compareTo(Object o) { Deletion o1 = (Deletion)o;
/* 39 */     if (this.chr != o1.chr) return this.chr < o1.chr ? -1 : 1;
/* 40 */     if (this.start != o1.start) return this.start < o1.start ? -1 : 1;
/* 41 */     if (this.end != o1.end) return this.end < o1.end ? -1 : 1;
/* 42 */     return 0;
/*    */   }
/*    */   
/* 45 */   public String toStringShort() { return this.id + "_" + this.chr + "_" + this.start + "_" + this.end; }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/sequenced/Deletion.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */