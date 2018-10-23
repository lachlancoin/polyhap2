/*    */ package lc1.dp;
/*    */ 
/*    */ import java.util.List;
/*    */ import lc1.dp.genotype.CachedEmissionState;
/*    */ import lc1.dp.genotype.CachedHMM;
/*    */ import lc1.dp.genotype.HaplotypeEmissionState;
/*    */ import lc1.dp.genotype.io.ComparableArray;
/*    */ 
/*    */ public class TrioEmissionState extends PairEmissionState
/*    */ {
/*    */   final int no_states;
/*    */   CachedEmissionState thirdState;
/*    */   
/*    */   protected TrioEmissionState(List<EmissionState> dist, PairMarkovModel parent)
/*    */   {
/* 16 */     super(dist, parent);
/* 17 */     this.no_states = 
/* 18 */       getHaplotypeModel().modelLength();
/* 19 */     this.thirdState = getThirdEmissionState();
/*    */   }
/*    */   
/*    */   private CachedHMM getParentModel() {
/* 23 */     return (CachedHMM)this.parentModel.m1[0];
/*    */   }
/*    */   
/* 26 */   private CachedHMM getSwitchingModel() { return (CachedHMM)this.parentModel.m1[1]; }
/*    */   
/*    */   private lc1.dp.genotype.HaplotypeHMM getHaplotypeModel() {
/* 29 */     return (lc1.dp.genotype.HaplotypeHMM)((PairMarkovModel)((CollapsedHMM)getParentModel().innerModel).hmm).m1[0];
/*    */   }
/*    */   
/*    */   public CachedEmissionState getThirdEmissionState() {
/* 33 */     int father_index = ((HaplotypeEmissionState)((PairEmissionState)
/* 34 */       ((CachedEmissionState)this.dist[2]).innerState).dist[0]).getIndex();
/* 35 */     int mother_index = ((HaplotypeEmissionState)((PairEmissionState)
/* 36 */       ((CachedEmissionState)this.dist[2]).innerState).dist[1]).getIndex();
/* 37 */     HaplotypeEmissionState hes1 = (HaplotypeEmissionState)((PairEmissionState)
/* 38 */       ((CachedEmissionState)this.dist[0]).innerState).dist[(father_index - 1)];
/* 39 */     HaplotypeEmissionState hes2 = (HaplotypeEmissionState)((PairEmissionState)
/* 40 */       ((CachedEmissionState)this.dist[1]).innerState).dist[(mother_index - 1)];
/* 41 */     int pairIndex = getPairIndex(hes1.getIndex(), hes2.getIndex());
/* 42 */     int collapsedIndex = ((CollapsedHMM)getParentModel().innerModel).collapse[pairIndex];
/* 43 */     return (CachedEmissionState)getParentModel().getState(collapsedIndex);
/*    */   }
/*    */   
/*    */   public double score(int key1, int i)
/*    */   {
/* 48 */     ComparableArray key = (ComparableArray)this.parentModel.emissionStateSpace.get(key1);
/* 49 */     double sc = this.thirdState.score(getParentModel().getEmissionStateSpaceIndex(key.get(2)), i);
/* 50 */     for (int j = 0; j < key.size() - 1; j++) {
/* 51 */       sc *= this.dist[j].score(getParentModel().getEmissionStateSpaceIndex(key.get(j)), i);
/*    */     }
/* 53 */     return sc; }
/*    */   
/*    */   private int getPairIndex(int index, int index2) { int i2;
/*    */     int i1;
/* 57 */     int i2; if (index < index2) {
/* 58 */       int i1 = index;i2 = index2;
/*    */     }
/*    */     else {
/* 61 */       i1 = index2;i2 = index;
/*    */     }
/* 63 */     return (i1 - 4) * this.no_states + index2;
/*    */   }
/*    */   
/*    */   public void addCount(int key1, Double value, int i)
/*    */   {
/* 68 */     if (value.doubleValue() == 0.0D) return;
/* 69 */     ComparableArray key = (ComparableArray)this.parentModel.emissionStateSpace.get(key1);
/* 70 */     this.thirdState.addCount(getParentModel().getEmissionStateSpaceIndex(key.get(2)), value, i);
/* 71 */     for (int j = 0; j < key.size() - 1; j++) {
/* 72 */       this.dist[j].addCount(getParentModel().getEmissionStateSpaceIndex(key.get(j)), value, i);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/TrioEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */