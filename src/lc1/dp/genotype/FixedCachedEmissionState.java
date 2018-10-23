/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import lc1.dp.CompoundState;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ 
/*     */ 
/*     */ public class FixedCachedEmissionState
/*     */   extends CompoundState
/*     */ {
/*     */   public CompoundState innerState;
/*     */   public Integer[] fixedIndex;
/*     */   
/*  16 */   public void print(PrintWriter paramPrintWriter, String paramString) { throw new Error("Unresolved compilation problem: \n\tThe type FixedCachedEmissionState must implement the inherited abstract method EmissionState.print(PrintWriter, String)\n"); } public void setRandom(double paramDouble, boolean paramBoolean) { throw new Error("Unresolved compilation problem: \n\tThe type FixedCachedEmissionState must implement the inherited abstract method EmissionState.setRandom(double, boolean)\n"); } public EmissionState[] getMemberStates() { throw new Error("Unresolved compilation problem: \n\tThe type FixedCachedEmissionState must implement the inherited abstract method CompoundState.getMemberStates()\n"); }
/*     */   
/*     */   public boolean isFixed()
/*     */   {
/*  20 */     throw new Error("Unresolved compilation problem: \n\tThe method isFixed() of type FixedCachedEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double[] getEmiss(int i)
/*     */   {
/*  27 */     throw new Error("Unresolved compilation problem: \n\tThe method getEmiss(int) of type FixedCachedEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/*  32 */     throw new Error("Unresolved compilation problem: \n\tThe method getFixedInteger(int) of type FixedCachedEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public lc1.dp.genotype.io.EmissionStateSpace getEmissionStateSpace()
/*     */   {
/*  44 */     throw new Error("Unresolved compilation problem: \n\tThe method getEmissionStateSpace() is undefined for the type CompoundState\n");
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs(double pseudo) {
/*  48 */     throw new Error("Unresolved compilation problem: \n\tThe method transferCountsToProbs(double) of type FixedCachedEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score(int object_index, int i1)
/*     */   {
/*  57 */     if (object_index == getFixedInteger(i1).intValue()) return 1.0D;
/*  58 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object clone(State pseudo)
/*     */   {
/*  64 */     return new FixedCachedEmissionState((CompoundState)this.innerState.clone(pseudo));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score(ComparableArray comp_a, int i, boolean recursive, boolean decompose)
/*     */   {
/*  74 */     throw new Error("Unresolved compilation problems: \n\tThe method score(ComparableArray, int, boolean, boolean) of type FixedCachedEmissionState must override a superclass method\n\tThe method score(int, int) in the type EmissionState is not applicable for the arguments (ComparableArray, int, boolean, boolean)\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double KLDistance(EmissionState st)
/*     */   {
/*  88 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public Comparable mostLikely(int pos)
/*     */   {
/*  93 */     throw new Error("Unresolved compilation problem: \n\tType mismatch: cannot convert from Object to Comparable\n");
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String st, java.util.List<Integer> columns) {
/*  97 */     throw new Error("Unresolved compilation problems: \n\tThe method print(PrintWriter, String, List<Integer>) of type FixedCachedEmissionState must override a superclass method\n\tThe method print(PrintWriter, String) in the type EmissionState is not applicable for the arguments (PrintWriter, String, List<Integer>)\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public Object sample(int i)
/*     */   {
/* 103 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate()
/*     */     throws Exception
/*     */   {
/* 110 */     this.innerState.validate();
/*     */   }
/*     */   
/*     */   public EmissionState[] getMemberStates(boolean real) {
/* 114 */     throw new Error("Unresolved compilation problems: \n\tThe method getMemberStates(boolean) of type FixedCachedEmissionState must override a superclass method\n\tThe method getMemberStates() in the type CompoundState is not applicable for the arguments (boolean)\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialise(lc1.dp.genotype.io.scorable.StateIndices dat)
/*     */   {
/* 120 */     throw new Error("Unresolved compilation problems: \n\tThe method initialise(StateIndices) of type FixedCachedEmissionState must override a superclass method\n\tThe method initialise(StateIndices) is undefined for the type CompoundState\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer[] calculateIndex()
/*     */   {
/* 126 */     throw new Error("Unresolved compilation problem: \n\tThe method calculateIndex() of type FixedCachedEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public FixedCachedEmissionState(CompoundState state) {}
/*     */   
/*     */   public void transferCountsToMemberStates() {}
/*     */   
/*     */   public void initialiseCounts() {}
/*     */   
/*     */   public void addCount(int obj_index, Double value, int i1) {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/FixedCachedEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */