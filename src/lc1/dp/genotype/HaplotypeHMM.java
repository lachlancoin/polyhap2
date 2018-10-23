/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import lc1.dp.MarkovModel;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class HaplotypeHMM
/*     */   extends MarkovModel
/*     */   implements Comparable
/*     */ {
/*     */   public NullEmitter nullEm;
/*     */   
/*     */   protected List initStateSpace()
/*     */   {
/*  26 */     return Arrays.asList(new Boolean[] { Boolean.TRUE, Boolean.FALSE });
/*     */   }
/*     */   
/*     */ 
/*  30 */   protected double pseudocountWeights = 0.01D;
/*     */   
/*     */   public void setPseudoCountWeights(double d)
/*     */   {
/*  34 */     this.pseudocountWeights = d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  39 */   public double logp = Double.NEGATIVE_INFINITY;
/*     */   
/*     */   final int[] in;
/*     */   
/*     */   public int compareTo(Object obj)
/*     */   {
/*  45 */     HaplotypeHMM obj1 = (HaplotypeHMM)obj;
/*  46 */     if (obj1.logp == this.logp) return 0;
/*  47 */     if (this.logp < obj1.logp) return 1;
/*  48 */     return -1;
/*     */   }
/*     */   
/*     */   public HaplotypeHMM(HaplotypeHMM hmm, HaplotypeHMM pseudo) {
/*  52 */     super(hmm, pseudo);
/*  53 */     this.nullEm = hmm.nullEm;
/*     */     
/*     */ 
/*  56 */     this.in = hmm.in;
/*  57 */     initialiseStateSpace();
/*     */   }
/*     */   
/*     */   public abstract MarkovModel clone(MarkovModel paramMarkovModel, double paramDouble);
/*     */   
/*     */   public HaplotypeHMM(String name, int numFounders, int noSnps, double u)
/*     */   {
/*  64 */     super(name, noSnps);
/*  65 */     if (Constants.modelNull()) {
/*  66 */       this.nullEm = 
/*  67 */         (Constants.averageNullModel() ? 
/*  68 */         new AverageNullEmitter(noSnps) : new SiteNullEmitter(noSnps));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */     for (int j = 0; j < numFounders; j++) {
/*  76 */       addState(makeState(j + 1, noSnps, u));
/*     */     }
/*     */     
/*  79 */     this.in = new int[this.states.size() - 1];
/*  80 */     for (int jk = 1; jk < this.states.size(); jk++) {
/*  81 */       this.in[(jk - 1)] = jk;
/*     */     }
/*  83 */     initialiseStateSpace();
/*     */   }
/*     */   
/*     */ 
/*     */   public HaplotypeEmissionState makeState(String st, int noSnps, double u)
/*     */   {
/*  89 */     return new HaplotypeEmissionState(st, noSnps, u, this.nullEm);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract boolean increaseStates(double paramDouble1, double paramDouble2, int paramInt);
/*     */   
/*     */ 
/*     */   public void initialiseCounts()
/*     */   {
/*  98 */     super.initialiseCounts();
/*  99 */     if (this.nullEm != null) { this.nullEm.initialise();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateEmissionStateSpaceDist()
/*     */   {
/* 105 */     if (this.emissionStateSpaceDist == null) {
/* 106 */       this.emissionStateSpaceDist = new double[this.emissionStateSpace.size()][this.emissionStateSpace.size()];
/*     */     }
/*     */     
/* 109 */     for (int i = 0; i < this.emissionStateSpace.size(); i++) {
/* 110 */       double[] res = this.emissionStateSpaceDist[i];
/*     */       
/* 112 */       if (this.emissionStateSpace.get(i) == null) {
/* 113 */         double probGapIsNotGap = Constants.probGapIsNotGap();
/* 114 */         Arrays.fill(res, probGapIsNotGap / (res.length - 1.0D));
/* 115 */         res[i] = (1.0D - probGapIsNotGap);
/*     */       }
/*     */       else {
/* 118 */         double probNonGapIsGap = Constants.probNonGapIsGap();
/* 119 */         Arrays.fill(res, Constants.modelError() / (res.length - 2.0D));
/* 120 */         res[((Integer)this.stateSpaceToIndex.get(null)).intValue()] = probNonGapIsGap;
/*     */         
/* 122 */         res[i] = (1.0D - probNonGapIsGap - Constants.modelError());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<State> statesOut(State st, int beforeToEmission)
/*     */   {
/* 131 */     if (beforeToEmission == this.noSnps - 1) return this.states.subList(0, 2);
/* 132 */     return this.states.subList(1, this.states.size());
/*     */   }
/*     */   
/*     */   public List<State> statesIn(State st, int beforeToEmission)
/*     */   {
/* 137 */     if (beforeToEmission == 0) return this.states.subList(0, 1);
/* 138 */     return this.states.subList(1, this.states.size());
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract void transferTransitionCountsToProbs();
/*     */   
/*     */   public void transferCountsToProbs()
/*     */   {
/* 146 */     for (int j = 1; j < modelLength(); j++) {
/* 147 */       HaplotypeEmissionState hes = (HaplotypeEmissionState)getState(j);
/* 148 */       hes.transferCountsToProbs(this.pseudocountWeights);
/*     */     }
/* 150 */     transferTransitionCountsToProbs();
/* 151 */     if (this.nullEm != null) { this.nullEm.transferCountsToProbs(this.pseudocountWeights);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 158 */     super.print(pw);
/*     */     
/* 160 */     StringBuffer sb1 = new StringBuffer();
/* 161 */     for (int i = 0; i < this.noSnps; i++) {
/* 162 */       sb1.append("%8.2g ");
/*     */     }
/* 164 */     if (this.nullEm != null)
/* 165 */       this.nullEm.print(pw, "bground ");
/* 166 */     pw.println("hitting probs");
/* 167 */     Double[][] hp = getHittingProb(this.noSnps);
/* 168 */     for (int j = 1; j < modelLength(); j++) {
/* 169 */       State st = getState(j);
/* 170 */       pw.print("State_");
/* 171 */       pw.print(st.getName());
/* 172 */       pw.print(" ");
/* 173 */       pw.print(Format.sprintf(sb1.toString(), hp[j]));
/* 174 */       pw.print("\n");
/*     */     }
/*     */   }
/*     */   
/* 178 */   public void fix() { for (int j = 1; j < modelLength(); j++) {
/* 179 */       HaplotypeEmissionState hes = (HaplotypeEmissionState)getState(j);
/* 180 */       hes.fix();
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTrain(boolean emiss, boolean gap)
/*     */   {
/* 199 */     for (int j = 1; j < modelLength(); j++) {
/* 200 */       HaplotypeEmissionState hes = (HaplotypeEmissionState)getState(j);
/* 201 */       hes.train_j = emiss;
/*     */     }
/*     */   }
/*     */   
/*     */   public void recombine(int i, int k, int j)
/*     */   {
/* 207 */     System.err.println("RECOMBINING AT " + i + " " + k + " " + j);
/* 208 */     HaplotypeEmissionState from = (HaplotypeEmissionState)getState(k);
/* 209 */     HaplotypeEmissionState to = (HaplotypeEmissionState)getState(j);
/* 210 */     from.recombine(to, i);
/*     */   }
/*     */   
/*     */   public double getLogP()
/*     */   {
/* 215 */     return this.logp;
/*     */   }
/*     */   
/*     */ 
/* 219 */   final int[] in0 = new int[1];
/*     */   
/* 221 */   public int[] statesIn(int j, int i) { if (i == 0) return this.in0;
/* 222 */     return this.in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i) {
/* 226 */     if (i == this.noSnps - 1) return this.in0;
/* 227 */     return this.in;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/HaplotypeHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */