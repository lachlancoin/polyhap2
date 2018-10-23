/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.dp.transition.FreeTransitionProbs1;
/*     */ import lc1.stats.StateDistribution;
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
/*     */ public abstract class FastMarkovModel
/*     */   extends MarkovModel
/*     */ {
/*     */   FreeTransitionProbs1 transProbs;
/*     */   
/*  37 */   public FastMarkovModel(String name, EmissionStateSpace emStSp) { super(name, 0, emStSp); }
/*     */   
/*     */   public void initialiseTransitions() {
/*     */     try {
/*  41 */       this.transProbs = new FreeTransitionProbs1(false, null, modelLength());
/*     */     } catch (Exception exc) {
/*  43 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public FastMarkovModel(FastMarkovModel fmm) {
/*  48 */     super(fmm);
/*  49 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCounts(StateDistribution[] observed, int i)
/*     */   {
/*  55 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public State addState(State st) {
/*  59 */     super.addState(st);
/*  60 */     return st;
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw)
/*     */   {
/*  65 */     super.print(pw, null, 0);
/*  66 */     this.transProbs.print(pw, null, 1.0D);
/*  67 */     pw.print("\nTrans: ");
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
/*     */   public double getTransitionScore(int from, int to, int fromIndex)
/*     */   {
/*  85 */     return this.transProbs.getTransition(from, to);
/*     */   }
/*     */   
/*     */ 
/*     */   public void validateTransitions()
/*     */   {
/*  91 */     this.transProbs.validate();
/*     */   }
/*     */   
/*  94 */   public boolean modelChanged = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTransition(State from, State to, double sc)
/*     */   {
/* 106 */     this.transProbs.setTransitionScore(from.getIndex(), to.getIndex(), sc, this.transProbs.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<State> statesOut(State k, int i)
/*     */   {
/* 116 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public List<State> statesIn(State k, int i)
/*     */   {
/* 122 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/FastMarkovModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */