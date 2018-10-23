/*    */ package lc1.dp.data.classification;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ import lc1.dp.data.representation.Emiss;
/*    */ import lc1.dp.emissionspace.EmissionStateSpace;
/*    */ 
/*    */ 
/*    */ public class ErrorClassificationHaplopair
/*    */   extends ErrorClassificationAbstract
/*    */ {
/*    */   public ErrorClassificationHaplopair(EmissionStateSpace ss, double thresh, int no_sources, PrintWriter log)
/*    */   {
/* 16 */     super(ss, thresh, new ErrorClassificationGenotype(ss, thresh, no_sources, log), Integer.valueOf(ss.haplopairListSize()), no_sources, log);
/*    */   }
/*    */   
/*    */ 
/* 20 */   static Comparator comp = new Comparator() {
/*    */     public int compare(Comparable o1, Comparable o2) {
/* 22 */       if (o1 == o2) return 0;
/* 23 */       if (o1 == null) return -1;
/* 24 */       if (o2 == null) return 1;
/* 25 */       return o1.compareTo(o2);
/*    */     }
/*    */   };
/*    */   
/*    */ 
/*    */   protected int wobbleRoom(ComparableArray compA)
/*    */   {
/* 32 */     EmissionStateSpace ss = (EmissionStateSpace)this.emSt;
/* 33 */     return ss.getHaplopairForGeno(ss.getGenotype(compA).intValue()).length;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void compare(ComparableArray orig, ComparableArray pred, int[] fromTo)
/*    */   {
/* 46 */     Integer from = ((EmissionStateSpace)this.emSt).getHaploPair(orig);
/* 47 */     Integer to = ((EmissionStateSpace)this.emSt).getHaploPair(pred);
/* 48 */     if (from == null) {
/* 49 */       throw new RuntimeException("no haplo pair for " + orig + " " + this.emSt.size() + " " + this.emSt);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 55 */     fromTo[0] = from.intValue();
/* 56 */     fromTo[1] = to.intValue();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String getString(int j)
/*    */   {
/* 63 */     Comparable comp = (Comparable)this.emSt.get(j);
/* 64 */     if ((comp instanceof ComparableArray)) {
/* 65 */       return ((ComparableArray)comp).toString();
/*    */     }
/* 67 */     if ((comp instanceof Emiss)) {
/* 68 */       return ((Emiss)comp).toStringShort();
/*    */     }
/* 70 */     return comp.toString();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/classification/ErrorClassificationHaplopair.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */