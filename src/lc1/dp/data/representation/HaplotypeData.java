/*    */ package lc1.dp.data.representation;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ public class HaplotypeData
/*    */   extends SimpleScorableObject
/*    */ {
/*    */   public Comparable copyElement(Comparable obj)
/*    */   {
/* 14 */     return obj;
/*    */   }
/*    */   
/*    */ 
/* 18 */   public HaplotypeData(String id, int noSites) { super(id, noSites, Comparable.class); }
/*    */   
/*    */   public HaplotypeData(String id, String data) {
/* 21 */     super(id, data.length(), Boolean.class);
/* 22 */     for (int i = 0; i < data.length(); i++) {
/* 23 */       char c = data.charAt(i);
/* 24 */       if (c == '0') { addPoint(i, Emiss.a());
/* 25 */       } else if (c == '1') addPoint(i, Emiss.b()); else
/* 26 */         addPoint(i, Emiss.N());
/*    */     }
/*    */   }
/*    */   
/* 30 */   public HaplotypeData(HaplotypeData data) { super(data); }
/*    */   
/*    */   public void print(PrintWriter pw, boolean b, boolean b2, Collection<Integer> s)
/*    */   {
/* 34 */     for (int i = 0; i < this.l1.size(); i++) {
/* 35 */       Object num = this.l1.get(i);
/* 36 */       pw.print(((Boolean)num).booleanValue() ? '1' : num == null ? '?' : '0');
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void switchElement(int i)
/*    */   {
/* 43 */     Boolean val = (Boolean)getElement(i);
/* 44 */     if (val == null) return;
/* 45 */     if (val == Boolean.FALSE) set(i, Boolean.TRUE); else
/* 46 */       set(i, Boolean.FALSE);
/*    */   }
/*    */   
/*    */   public ScorableObject clone() {
/* 50 */     return new HaplotypeData(this);
/*    */   }
/*    */   
/*    */   public void addLongGaps(double d, double median_length) {
/* 54 */     int i = 0;
/* 55 */     double lambda = Math.log(2.0D) / median_length;
/* 56 */     for (i = 0; i < length(); i++) {
/* 57 */       if (Math.random() < d) {
/* 58 */         int len = (int)Math.round(-Math.log(Constants.rand.nextDouble()) / lambda);
/* 59 */         for (; (i < len) && (i < length()); i++) {
/* 60 */           set(i, null);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void clear() {
/* 67 */     this.l1.clear();
/*    */   }
/*    */   
/*    */   public int compareTo(Object arg0)
/*    */   {
/* 72 */     return getName().compareTo(((SimpleScorableObject)arg0).getName());
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/HaplotypeData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */