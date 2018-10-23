/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import lc1.dp.CompoundState;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ 
/*     */ 
/*     */ public class DistributedEmissionState
/*     */   extends CompoundState
/*     */ {
/*     */   public CompoundState innerState;
/*     */   final SimpleExtendedDistribution[] probHomoIsHemiZygous;
/*     */   boolean[] homoToHemi;
/*     */   int[] hemiToHomo;
/*     */   
/*  18 */   public void print(PrintWriter paramPrintWriter, String paramString) { throw new Error("Unresolved compilation problem: \n\tThe type DistributedEmissionState must implement the inherited abstract method EmissionState.print(PrintWriter, String)\n"); } public EmissionState[] getMemberStates() { throw new Error("Unresolved compilation problem: \n\tThe type DistributedEmissionState must implement the inherited abstract method CompoundState.getMemberStates()\n"); }
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public lc1.dp.CompoundMarkovModel getParentModel()
/*     */   {
/*  54 */     throw new Error("Unresolved compilation problem: \n\tThe method getParentModel() is undefined for the type CompoundState\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double score(int object_index, int i)
/*     */   {
/*  61 */     double probHemi = probHemi(i);
/*  62 */     if (probHemi > 0.0D) {
/*  63 */       boolean hemi = this.homoToHemi[object_index];
/*  64 */       if (hemi) {
/*  65 */         return this.innerState.score(object_index, i) * (1.0D - probHemi);
/*     */       }
/*  67 */       int homo = this.hemiToHomo[object_index];
/*  68 */       if (homo != -1) {
/*  69 */         return this.innerState.score(object_index, i) + probHemi * this.innerState.score(homo, i);
/*     */       }
/*     */     }
/*  72 */     return this.innerState.score(object_index, i);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object clone(State pseudo)
/*     */   {
/*  78 */     return new DistributedEmissionState(this.innerState, this.probHomoIsHemiZygous);
/*     */   }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/*  83 */     this.innerState.initialiseCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   public double score(ComparableArray comp_a, int i, boolean recursive, boolean decompose)
/*     */   {
/*  89 */     throw new Error("Unresolved compilation problems: \n\tThe method score(ComparableArray, int, boolean, boolean) of type DistributedEmissionState must override a superclass method\n\tThe method score(int, int) in the type EmissionState is not applicable for the arguments (ComparableArray, int, boolean, boolean)\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCount(int k, Double value, int i)
/*     */   {
/*  95 */     if (value.doubleValue() == 0.0D) return;
/*  96 */     double probHemi = probHemi(i);
/*  97 */     if (probHemi > 0.0D) {
/*  98 */       int homo = this.hemiToHomo[k];
/*  99 */       if (homo != -1) {
/* 100 */         double homo_frac = probHemi * this.innerState.score(homo, i);
/* 101 */         double hemi_frac = this.innerState.score(k, i);
/* 102 */         double total = hemi_frac + homo_frac;
/* 103 */         this.innerState.addCount(homo, Double.valueOf(value.doubleValue() * (homo_frac / total)), i);
/* 104 */         this.innerState.addCount(k, Double.valueOf(value.doubleValue() * (hemi_frac / total)), i);
/* 105 */         this.probHomoIsHemiZygous[i].counts[0] += homo_frac / total * value.doubleValue();
/*     */       }
/*     */       else {
/* 108 */         if (this.homoToHemi[k] != 0) {
/* 109 */           this.probHomoIsHemiZygous[i].counts[1] += value.doubleValue();
/*     */         }
/* 111 */         this.innerState.addCount(k, value, i);
/*     */       }
/*     */     }
/*     */     else {
/* 115 */       this.innerState.addCount(k, value, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   double probHemi(int i)
/*     */   {
/* 123 */     if (this.probHomoIsHemiZygous == null) return 0.0D;
/* 124 */     return this.probHomoIsHemiZygous[i].probs[0];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double KLDistance(EmissionState st)
/*     */   {
/* 131 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public Object mostLikely(int pos)
/*     */   {
/* 136 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String st, java.util.List<Integer> columns) {
/* 140 */     throw new Error("Unresolved compilation problems: \n\tThe method print(PrintWriter, String, List<Integer>) of type DistributedEmissionState must override a superclass method\n\tThe method print(PrintWriter, String) in the type EmissionState is not applicable for the arguments (PrintWriter, String, List<Integer>)\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public Object sample(int i)
/*     */   {
/* 146 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setRandom(double emiss, boolean restart)
/*     */   {
/* 151 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void validate() throws Exception
/*     */   {
/* 156 */     this.innerState.validate();
/*     */   }
/*     */   
/*     */   public EmissionState[] getMemberStates(boolean real) {
/* 160 */     throw new Error("Unresolved compilation problems: \n\tThe method getMemberStates(boolean) of type DistributedEmissionState must override a superclass method\n\tThe method getMemberStates() in the type CompoundState is not applicable for the arguments (boolean)\n");
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialise(lc1.dp.genotype.io.scorable.StateIndices dat)
/*     */   {
/* 166 */     throw new Error("Unresolved compilation problems: \n\tThe method initialise(StateIndices) of type DistributedEmissionState must override a superclass method\n\tThe method initialise(StateIndices) is undefined for the type CompoundState\n");
/*     */   }
/*     */   
/*     */   public DistributedEmissionState(CompoundState state, SimpleExtendedDistribution[] probHomoIsHemi) {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/DistributedEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */