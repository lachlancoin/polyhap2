/*    */ package lc1.dp.genotype.io.scorable;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import lc1.dp.genotype.io.Constants;
/*    */ 
/*    */ public class HaplotypeData
/*    */   extends SimpleScorableObject
/*    */ {
/*    */   public Comparable copyElement(Comparable obj)
/*    */   {
/* 13 */     return obj;
/*    */   }
/*    */   
/*    */ 
/* 17 */   public HaplotypeData(String id, int noSites) { super(id, noSites, Comparable.class); }
/*    */   
/*    */   public HaplotypeData(String id, String data) {
/* 20 */     super(id, data.length(), Boolean.class);
/* 21 */     for (int i = 0; i < data.length(); i++) {
/* 22 */       char c = data.charAt(i);
/* 23 */       if (c == '0') { addPoint(Boolean.FALSE);
/* 24 */       } else if (c == '1') addPoint(Boolean.TRUE); else
/* 25 */         addPoint(null);
/*    */     }
/*    */   }
/*    */   
/* 29 */   public HaplotypeData(HaplotypeData data) { super(data); }
/*    */   
/*    */   public void print(PrintWriter pw)
/*    */   {
/* 33 */     for (int i = 0; i < this.l1.size(); i++) {
/* 34 */       Object num = this.l1.get(i);
/* 35 */       pw.print(((Boolean)num).booleanValue() ? '1' : num == null ? '?' : '0');
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public void switchElement(int i)
/*    */   {
/* 42 */     Boolean val = (Boolean)getElement(i);
/* 43 */     if (val == null) return;
/* 44 */     if (val == Boolean.FALSE) set(i, Boolean.TRUE); else
/* 45 */       set(i, Boolean.FALSE);
/*    */   }
/*    */   
/*    */   public ScorableObject clone() {
/* 49 */     return new HaplotypeData(this);
/*    */   }
/*    */   
/*    */   public void addLongGaps(double d, double median_length) {
/* 53 */     int i = 0;
/* 54 */     double lambda = Math.log(2.0D) / median_length;
/* 55 */     for (i = 0; i < length(); i++) {
/* 56 */       if (Math.random() < d) {
/* 57 */         int len = (int)Math.round(-Math.log(Constants.rand.nextDouble()) / lambda);
/* 58 */         for (; (i < len) && (i < length()); i++) {
/* 59 */           set(i, null);
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public void clear() {
/* 66 */     this.l1.clear();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/HaplotypeData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */