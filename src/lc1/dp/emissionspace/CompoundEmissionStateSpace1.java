/*    */ package lc1.dp.emissionspace;
/*    */ 
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ import lc1.dp.data.representation.Emiss;
/*    */ 
/*    */ public class CompoundEmissionStateSpace1
/*    */   extends CompoundEmissionStateSpace
/*    */ {
/*    */   public String getHaploPairString(Comparable o1)
/*    */   {
/* 11 */     return getGenotypeString(o1);
/*    */   }
/*    */   
/*    */ 
/*    */   public String getGenotypeString(Comparable o1)
/*    */   {
/* 17 */     return ((Emiss)o1).toString();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Comparable translate(ComparableArray array)
/*    */   {
/* 28 */     if (array.get(0) == Emiss.zero) {
/* 29 */       return Emiss.N;
/*    */     }
/* 31 */     if (array.get(0) == Emiss.one) return array.get(1);
/* 32 */     if (array.get(0) == Emiss.two) {
/* 33 */       if (array.get(1) == Emiss.A) return Emiss.aa();
/* 34 */       if (array.get(1) == Emiss.B) return Emiss.bb();
/*    */     }
/* 36 */     throw new RuntimeException("!! " + array);
/*    */   }
/*    */   
/* 39 */   public CompoundEmissionStateSpace1(EmissionStateSpace[] stateSpaces, int noCop) { super(stateSpaces, noCop); }
/*    */   
/*    */ 
/* 42 */   int[] zero = new int[2];
/*    */   
/* 44 */   public int getIndex(int[] indices) { if (indices[0] == 0) {
/* 45 */       return this.haploToHaploPair[((Integer)this.membersIndexToIndex.get(this.zero)).intValue()];
/*    */     }
/*    */     
/* 48 */     return this.haploToHaploPair[((Integer)this.membersIndexToIndex.get(indices)).intValue()]; }
/*    */   
/* 50 */   static ComparableArray[] comps = {
/* 51 */     ComparableArray.make(Emiss.zero, Emiss.A), 
/* 52 */     ComparableArray.make(Emiss.one, Emiss.A), 
/* 53 */     ComparableArray.make(Emiss.one, Emiss.B), 
/* 54 */     ComparableArray.make(Emiss.two, Emiss.A), 
/* 55 */     ComparableArray.make(Emiss.two, Emiss.B) };
/*    */   
/*    */ 
/*    */   protected ComparableArray backTranslate(Comparable c)
/*    */   {
/* 60 */     if (c == Emiss.N) return comps[0];
/* 61 */     if (c == Emiss.A) return comps[1];
/* 62 */     if (c == Emiss.B) return comps[2];
/* 63 */     if (c == Emiss.aa()) return comps[3];
/* 64 */     if (c == Emiss.bb()) return comps[4];
/* 65 */     throw new RuntimeException("!!");
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/emissionspace/CompoundEmissionStateSpace1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */