/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import lc1.dp.genotype.ExtendedDistribution;
/*     */ import lc1.dp.genotype.StateDistribution;
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
/*     */ public abstract class FastMarkovModel
/*     */   extends MarkovModel
/*     */ {
/*     */   ExtendedDistribution[] transProbs;
/*     */   
/*     */   public FastMarkovModel(String name)
/*     */   {
/*  32 */     this(name, 0.0D);
/*     */   }
/*     */   
/*  35 */   public FastMarkovModel(String name, DotState magic) { this(name, magic, 0.0D); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FastMarkovModel(String name, DotState magic, double pseudocount)
/*     */   {
/*  42 */     super(name, magic, -1);
/*     */   }
/*     */   
/*     */   public void updateEmissionStateSpaceDist()
/*     */   {
/*  47 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public FastMarkovModel(FastMarkovModel fmm) {
/*  51 */     super(fmm, fmm);
/*  52 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public FastMarkovModel(String name, double pseudocount)
/*     */   {
/*  57 */     super(name, -1);
/*     */   }
/*     */   
/*     */   public void addCounts(StateDistribution[] observed, int i) {
/*  61 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public State addState(State st) {
/*  65 */     super.addState(st);
/*  66 */     return st;
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw)
/*     */   {
/*  71 */     super.print(pw);
/*  72 */     pw.print("\nTrans: ");
/*  73 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  74 */       State st = getState(i);
/*  75 */       pw.print(st.getName() + "->" + this.transProbs[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void fix()
/*     */   {
/*  81 */     initialiseStateSpace();
/*  82 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  83 */       ExtendedDistribution outDist = this.transProbs[i];
/*  84 */       for (Iterator<Map.Entry<Object, Double>> it = outDist.probs.iterator(); it.hasNext();) {
/*  85 */         Map.Entry<Object, Double> nxt = (Map.Entry)it.next();
/*  86 */         this.statesIn[((Integer)nxt.getKey()).intValue()].put(Integer.valueOf(i), (Double)nxt.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double getTransitionScore(int from, int to, int fromIndex)
/*     */   {
/*  94 */     ExtendedDistribution dist = this.transProbs[from];
/*  95 */     if (dist != null) {
/*  96 */       Double res = dist.get(Integer.valueOf(to));
/*  97 */       if (res != null) return res.doubleValue();
/*     */     }
/*  99 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts() {
/* 103 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void validateTransitions() {
/* 107 */     for (int i = 0; i < this.transProbs.length; i++) {
/* 108 */       this.transProbs[i].validate();
/*     */     }
/*     */   }
/*     */   
/* 112 */   public boolean modelChanged = false;
/*     */   ExtendedDistribution[] statesIn;
/*     */   
/* 115 */   public void initialiseTransitions() { this.transProbs = new ExtendedDistribution[this.states.size()];
/* 116 */     this.statesIn = new ExtendedDistribution[this.states.size()];
/* 117 */     for (int i = 0; i < this.transProbs.length; i++) {
/* 118 */       this.transProbs[i] = new ExtendedDistribution();
/* 119 */       this.statesIn[i] = new ExtendedDistribution();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTransition(State from, State to, double sc) {
/* 124 */     this.transProbs[from.index].put(Integer.valueOf(to.index), Double.valueOf(sc));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean transitionsChanged()
/*     */   {
/* 130 */     return this.modelChanged;
/*     */   }
/*     */   
/*     */   public void transitionsFixed() {
/* 134 */     this.modelChanged = false;
/*     */   }
/*     */   
/*     */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly) {
/* 138 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double totalTransitionDistance(MarkovModel m1)
/*     */   {
/* 143 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<State> statesOut(State k, int i)
/*     */   {
/* 151 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<State> statesIn(State k, int i)
/*     */   {
/* 158 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs()
/*     */   {
/* 163 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public int[] statesIn(int j, int i)
/*     */   {
/* 168 */     return this.statesIn[j].getNonZero();
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i)
/*     */   {
/* 173 */     return this.transProbs[j].getNonZero();
/*     */   }
/*     */   
/*     */   public void setPseudoCountWeights(double d) {
/* 177 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/FastMarkovModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */