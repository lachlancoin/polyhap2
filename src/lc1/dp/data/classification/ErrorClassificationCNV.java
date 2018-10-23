/*    */ package lc1.dp.data.classification;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ import lc1.dp.emissionspace.EmissionStateSpace;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ErrorClassificationCNV
/*    */   extends ErrorClassificationAbstract
/*    */ {
/*    */   EmissionStateSpace ss;
/*    */   
/* 20 */   public ErrorClassificationCNV(EmissionStateSpace ss, double thresh, int no_sources, PrintWriter log) { super(getCNV(ss), thresh, null, null, no_sources, log); }
/*    */   
/*    */   private static List<Comparable> getCNV(EmissionStateSpace ss) {
/* 23 */     Set<Integer> s = new TreeSet();
/* 24 */     s.add(Integer.valueOf(0));
/* 25 */     s.add(Integer.valueOf(1));
/* 26 */     for (int i = 0; i < ss.size(); i++) {
/* 27 */       s.add(Integer.valueOf(((ComparableArray)ss.get(i)).noCopies(true)));
/*    */     }
/* 29 */     return new ArrayList(s);
/*    */   }
/*    */   
/*    */   protected void compare(ComparableArray orig, ComparableArray pred, int[] fromTo) {
/* 33 */     fromTo[0] = orig.noCopies(true);
/* 34 */     fromTo[1] = pred.noCopies(true);
/*    */   }
/*    */   
/*    */   protected int wobbleRoom(ComparableArray compA) {
/* 38 */     return 4;
/*    */   }
/*    */   
/*    */   public String getString(int j) {
/* 42 */     return ((Comparable)this.emSt.get(j)).toString();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/classification/ErrorClassificationCNV.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */