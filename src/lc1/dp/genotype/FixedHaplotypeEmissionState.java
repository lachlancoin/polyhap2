/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.genotype.io.EmissionStateSpace;
/*     */ 
/*     */ 
/*     */ public class FixedHaplotypeEmissionState
/*     */   extends EmissionState
/*     */ {
/*     */   final EmissionStateSpace emStSp;
/*     */   final int noSnps;
/*     */   private Integer[] fixedIndex;
/*     */   
/*  16 */   public void print(PrintWriter paramPrintWriter, String paramString) { throw new Error("Unresolved compilation problem: \n\tThe type FixedHaplotypeEmissionState must implement the inherited abstract method EmissionState.print(PrintWriter, String)\n"); } public void setRandom(double paramDouble, boolean paramBoolean) { throw new Error("Unresolved compilation problem: \n\tThe type FixedHaplotypeEmissionState must implement the inherited abstract method EmissionState.setRandom(double, boolean)\n"); }
/*     */   
/*     */   public Integer getFixedInteger(int i)
/*     */   {
/*  20 */     throw new Error("Unresolved compilation problem: \n\tThe method getFixedInteger(int) of type FixedHaplotypeEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public boolean isFixed()
/*     */   {
/*  25 */     throw new Error("Unresolved compilation problem: \n\tThe method isFixed() of type FixedHaplotypeEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public void setFixedIndex(int i, int k)
/*     */   {
/*  30 */     if (k >= this.emStSp.size()) throw new RuntimeException(i + " " + k);
/*  31 */     this.fixedIndex[i] = Integer.valueOf(k);
/*     */   }
/*     */   
/*     */ 
/*     */   public void transferCountsToProbs(double pseudo)
/*     */   {
/*  37 */     throw new Error("Unresolved compilation problem: \n\tThe method transferCountsToProbs(double) of type FixedHaplotypeEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public FixedHaplotypeEmissionState(String name, int noSnps, EmissionStateSpace emStSp, int index)
/*     */   {
/*  42 */     super(name, 1);
/*  43 */     this.emStSp = emStSp;
/*  44 */     this.noSnps = noSnps;
/*  45 */     this.fixedIndex = new Integer[noSnps];
/*  46 */     java.util.Arrays.fill(this.fixedIndex, Integer.valueOf(index));
/*  47 */     if (index >= emStSp.size()) throw new RuntimeException();
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String prefix, List<Integer> columns) {
/*  51 */     StringBuffer sb = new StringBuffer();
/*  52 */     for (int i = 0; i < this.fixedIndex.length; i++) {
/*  53 */       sb.append("%8i ");
/*     */     }
/*  55 */     pw.println(prefix + " " + com.braju.format.Format.sprintf(sb.toString(), this.fixedIndex));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FixedHaplotypeEmissionState(FixedHaplotypeEmissionState st_to_init, FixedHaplotypeEmissionState st_to_pseudo, String name)
/*     */   {
/*  63 */     super(name, 1);
/*  64 */     this.emStSp = st_to_init.emStSp;
/*  65 */     this.noSnps = st_to_init.noSnps;
/*  66 */     this.fixedIndex = st_to_init.fixedIndex;
/*     */   }
/*     */   
/*  69 */   public Object clone(lc1.dp.State pseudo) { return new FixedHaplotypeEmissionState(this, (FixedHaplotypeEmissionState)pseudo, getName()); }
/*     */   
/*     */   public Object clone()
/*     */   {
/*  73 */     return new FixedHaplotypeEmissionState(this, this, getName());
/*     */   }
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
/*  85 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public Object sample(int i) {
/*  89 */     return lc1.dp.genotype.io.Emiss.stateSpace[getFixedInteger(i).intValue()];
/*     */   }
/*     */   
/*     */   public double score(int obj_i, int i1) {
/*  93 */     if (obj_i == getFixedInteger(i1).intValue()) return 1.0D;
/*  94 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     throw new Error("Unresolved compilation problem: \n\tThe method print(PrintWriter, List<Integer>) is ambiguous for the type FixedHaplotypeEmissionState\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Comparable mostLikely(int pos)
/*     */   {
/* 115 */     return getEmissionStateSpace().get(this.fixedIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, List<Integer> cols)
/*     */   {
/* 124 */     print(pw, "", cols);
/*     */   }
/*     */   
/*     */ 
/*     */   public int noSnps()
/*     */   {
/* 130 */     throw new Error("Unresolved compilation problem: \n\tThe method noSnps() of type FixedHaplotypeEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] getEmiss(int i)
/*     */   {
/* 136 */     throw new Error("Unresolved compilation problem: \n\tThe method getEmiss(int) of type FixedHaplotypeEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public EmissionStateSpace getEmissionStateSpace()
/*     */   {
/* 141 */     throw new Error("Unresolved compilation problem: \n\tThe method getEmissionStateSpace() of type FixedHaplotypeEmissionState must override a superclass method\n");
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, Double value, int i) {}
/*     */   
/*     */   public void initialiseCounts() {}
/*     */   
/*     */   public void validate()
/*     */     throws Exception
/*     */   {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/FixedHaplotypeEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */