/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public class Aberation implements Comparable {
/*    */   public final int start;
/*    */   public int end;
/*    */   public final int copy;
/*    */   public final String name;
/*    */   public double certainty;
/*    */   
/*    */   public Aberation(String name, int start, int copy) {
/* 13 */     this.name = name;
/* 14 */     this.start = start;
/* 15 */     this.copy = copy;
/*    */   }
/*    */   
/* 18 */   public String toString() { return this.name + ":" + this.start + ":" + this.end + ":" + this.copy + " " + this.certainty; }
/*    */   
/*    */   public static List<Aberation> getAberation(String name, int[] noCopies, double[] cert) {
/* 21 */     int len = noCopies.length;
/* 22 */     List<Aberation> l = new java.util.ArrayList();
/* 23 */     for (int i = 0; i < len; i++) {
/* 24 */       if (noCopies[i] != 1) {
/* 25 */         int noCop = noCopies[i];
/* 26 */         double cert_i = 0.0D;
/* 27 */         double cnt = 0.0D;
/* 28 */         Aberation ab = new Aberation(name, i, noCop);
/* 29 */         for (; (i < len) && (noCopies[i] == noCop); i++) {
/* 30 */           cert_i += cert[i];
/* 31 */           cnt += 1.0D;
/*    */         }
/* 33 */         ab.certainty = (cert_i / cnt);
/*    */         
/* 35 */         i--;
/* 36 */         ab.end = i;
/* 37 */         l.add(ab);
/*    */       }
/*    */     }
/* 40 */     return l;
/*    */   }
/*    */   
/* 43 */   public int compareTo(Object o) { Aberation ab = (Aberation)o;
/* 44 */     if (this.start != ab.start) return this.start < ab.start ? -1 : 1;
/* 45 */     if (this.copy != ab.copy) return this.copy < ab.copy ? -1 : 1;
/* 46 */     if (this.end != ab.end) return this.end < ab.end ? -1 : 1;
/* 47 */     return this.name.compareTo(ab.name);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/Aberation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */