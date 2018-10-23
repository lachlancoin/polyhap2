/*    */ package lc1.dp.data.classification;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ import lc1.dp.emissionspace.EmissionStateSpace;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ErrorClassificationGenotype
/*    */   extends ErrorClassificationAbstract
/*    */ {
/*    */   public ErrorClassificationGenotype(EmissionStateSpace ss, double thresh, int no_sources, PrintWriter log)
/*    */   {
/* 17 */     super(ss, thresh, new ErrorClassificationCNV(ss, thresh, no_sources, null), ss.genoListSize(), no_sources, log);
/*    */   }
/*    */   
/*    */   protected void compare(ComparableArray orig, ComparableArray pred, int[] fromTo) {
/* 21 */     fromTo[0] = ((EmissionStateSpace)this.emSt).getGenotype(orig).intValue();
/* 22 */     fromTo[1] = ((EmissionStateSpace)this.emSt).getGenotype(pred).intValue();
/*    */   }
/*    */   
/*    */   protected int wobbleRoom(ComparableArray compA) {
/* 26 */     EmissionStateSpace ss = (EmissionStateSpace)this.emSt;
/* 27 */     return ss.getGenoForCopyNo(ss.getCN(compA)).length;
/*    */   }
/*    */   
/*    */   public String getString(int j) {
/* 31 */     return ((EmissionStateSpace)this.emSt).getGenotypeString(((EmissionStateSpace)this.emSt).get(j));
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/classification/ErrorClassificationGenotype.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */