/*    */ package lc1.dp;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import lc1.dp.genotype.HaplotypeHMM;
/*    */ import lc1.dp.genotype.io.ComparableArray;
/*    */ 
/*    */ public class TrioMarkovModel extends PairMarkovModel
/*    */ {
/*    */   public static MarkovModel makeTrioMarkovModel(HaplotypeHMM hmm, HaplotypeHMM switching)
/*    */   {
/* 15 */     TrioMarkovModel trios = new TrioMarkovModel(hmm, switching);
/* 16 */     return trios;
/*    */   }
/*    */   
/*    */   public TrioMarkovModel(MarkovModel m1, MarkovModel switchingModel) {
/* 20 */     super(new MarkovModel[] { m1, switchingModel }, new int[] { 0, 0, 1 });
/*    */   }
/*    */   
/*    */   protected boolean stateSpaceExclude(Comparable obj, Comparable previous)
/*    */   {
/* 25 */     return false;
/*    */   }
/*    */   
/*    */   protected void putInStateSpaceIndex(ComparableArray compa, int index)
/*    */   {
/* 30 */     Collection<ComparableArray> equiv = PairEmissionState.perm.get(compa);
/* 31 */     this.stateSpaceToIndex.put(compa, Integer.valueOf(index));
/*    */   }
/*    */   
/*    */   public PairEmissionState constructPair(List st1) {
/* 35 */     return new TrioEmissionState(st1, this);
/*    */   }
/*    */   
/*    */   protected void makeEquivalenceClasses(Iterator<List<Integer>> it) {}
/*    */   
/*    */   protected Iterator<ComparableArray> getEquivalentEmissionStates(ComparableArray compa_inner) {
/* 41 */     return new ArrayList(compa_inner).iterator();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/TrioMarkovModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */