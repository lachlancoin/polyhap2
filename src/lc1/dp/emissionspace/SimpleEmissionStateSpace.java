/*    */ package lc1.dp.emissionspace;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import lc1.dp.data.representation.Emiss;
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleEmissionStateSpace
/*    */   extends EmissionStateSpace
/*    */ {
/*    */   public SimpleEmissionStateSpace(List<Comparable> list, int noCopies)
/*    */   {
/* 16 */     super(noCopies);
/* 17 */     init(list);
/*    */   }
/*    */   
/*    */   public String getHaploPairString(Comparable comp) {
/* 21 */     return getHaploString(comp);
/*    */   }
/*    */   
/* 24 */   public String getGenotypeString(int i) { return getGenotypeString(getGenotype(i)); }
/*    */   
/*    */   public String getGenotypeString(Comparable comp) {
/* 27 */     if ((comp instanceof Integer)) {
/* 28 */       return Integer.toString(((Integer)comp).intValue(), Constants.radix());
/*    */     }
/* 30 */     return comp.toString();
/*    */   }
/*    */   
/* 33 */   public String getHaploString(Comparable comp) { if ((comp instanceof Integer)) {
/* 34 */       return Integer.toString(((Integer)comp).intValue(), Constants.radix());
/*    */     }
/* 36 */     if ((comp instanceof Emiss)) {
/* 37 */       return ((Emiss)comp).toStringPrint();
/*    */     }
/* 39 */     return comp.toString();
/*    */   }
/*    */   
/*    */ 
/*    */   public SimpleEmissionStateSpace(Comparable[] stateSpace)
/*    */   {
/* 45 */     this(getList(stateSpace), 1);
/*    */   }
/*    */   
/*    */ 
/*    */   private static List<Comparable> getList(Comparable[] stateSpace)
/*    */   {
/* 51 */     List<Comparable> comp = new ArrayList(stateSpace.length);
/* 52 */     for (int i = 0; i < stateSpace.length; i++) {
/* 53 */       comp.add(stateSpace[i]);
/*    */     }
/* 55 */     return comp;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/emissionspace/SimpleEmissionStateSpace.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */