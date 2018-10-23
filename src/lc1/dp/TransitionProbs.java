/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.dp.genotype.SimpleExtendedDistribution;
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
/*     */ public class TransitionProbs
/*     */   implements Serializable
/*     */ {
/*     */   final List<State> states;
/*     */   public SimpleExtendedDistribution[] transitionsOut;
/*  31 */   boolean modelChanged = true;
/*     */   
/*     */   public boolean modelChanged() {
/*  34 */     return this.modelChanged;
/*     */   }
/*     */   
/*  37 */   public void setModelChanged(boolean modelChanged) { this.modelChanged = modelChanged; }
/*     */   
/*     */   public TransitionProbs(TransitionProbs tp_init, TransitionProbs tp_pseudo, List<State> states)
/*     */   {
/*  41 */     this.states = states;
/*  42 */     this.transitionsOut = new SimpleExtendedDistribution[states.size()];
/*  43 */     for (int i = 0; i < this.transitionsOut.length; i++) {
/*  44 */       this.transitionsOut[i] = new SimpleExtendedDistribution(tp_init.transitionsOut[i], tp_pseudo.transitionsOut[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void increaseStates(double small, int len, boolean fromMagic, Dirichlet newStateSampler, Dirichlet nonMagicStateSampler)
/*     */   {
/*  50 */     throw new RuntimeException("redo");
/*     */   }
/*     */   
/*     */   public TransitionProbs(List<State> states, boolean first, Dirichlet samplerFirst) {
/*  54 */     this.states = states;
/*  55 */     this.transitionsOut = new SimpleExtendedDistribution[states.size()];
/*     */     
/*  57 */     this.transitionsOut[0] = new SimpleExtendedDistribution(samplerFirst);
/*     */     
/*     */ 
/*  60 */     for (int j = 1; j < states.size(); j++) {
/*  61 */       this.transitionsOut[j] = new SimpleExtendedDistribution(samplerFirst);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCounts(StateDistribution[] observed, List<State> states)
/*     */   {
/*  68 */     for (int j = 0; j < observed.length; j++) {
/*  69 */       if (observed[j] != null) {
/*  70 */         State k = (State)states.get(j);
/*  71 */         this.transitionsOut[j].addCounts(observed[j]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double getTransition(int from, int to)
/*     */   {
/*  79 */     SimpleExtendedDistribution dist = this.transitionsOut[from];
/*  80 */     if (dist != null) {
/*  81 */       return dist.probs[to];
/*     */     }
/*  83 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/*  89 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/*  90 */       if (this.transitionsOut[j] != null) this.transitionsOut[j].initialise();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setRandom(double u, boolean restart, boolean lastOnly)
/*     */   {
/*  96 */     if (lastOnly) {
/*  97 */       int j = this.states.size() - 1;
/*  98 */       SimpleExtendedDistribution dist = this.transitionsOut[j];
/*  99 */       if (dist == null) return;
/* 100 */       dist.resample(u, restart);
/*     */     }
/*     */     else {
/* 103 */       for (int j = 0; j < this.transitionsOut.length; j++) {
/* 104 */         SimpleExtendedDistribution dist = this.transitionsOut[j];
/* 105 */         if (dist != null) {
/* 106 */           dist.resample(u, restart);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 119 */     for (int j = 0; j < this.transitionsOut.length; j++)
/*     */     {
/* 121 */       pw.print(((State)this.states.get(j)).getName() + "->{");
/* 122 */       for (int k = 0; k < this.states.size(); k++) {
/* 123 */         double prob = this.transitionsOut[j].probs[k];
/* 124 */         if (prob > 0.0D) {
/* 125 */           pw.print(((State)this.states.get(k)).getName() + "~" + prob);
/*     */         }
/*     */       }
/* 128 */       pw.print("} ");
/*     */     }
/*     */   }
/*     */   
/* 132 */   public String toString() { return this.transitionsOut.toString(); }
/*     */   
/*     */   public void transfer(double pseudoCountWeight)
/*     */   {
/* 136 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 137 */       this.transitionsOut[j].transfer(pseudoCountWeight);
/*     */     }
/*     */   }
/*     */   
/*     */   public Collection getDistributions() {
/* 142 */     return (Collection)Arrays.asList(this.transitionsOut);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double transitionDistance(TransitionProbs m1)
/*     */   {
/* 150 */     double sum = 0.0D;
/* 151 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 152 */       SimpleExtendedDistribution d1 = this.transitionsOut[j];
/* 153 */       SimpleExtendedDistribution d2 = m1.transitionsOut[j];
/* 154 */       if ((d1 != null) && (d2 != null)) {
/* 155 */         sum += d1.KLDistance(d2);
/*     */       }
/*     */     }
/*     */     
/* 159 */     return sum;
/*     */   }
/*     */   
/*     */   public void validate()
/*     */   {
/* 164 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 165 */       SimpleExtendedDistribution dist = this.transitionsOut[j];
/* 166 */       if (dist != null) {
/* 167 */         double sum = dist.sum();
/* 168 */         if (Math.abs(sum - 1.0D) > SimpleDistribution.tolerance) throw new RuntimeException("!!! " + sum + " at " + this.states.get(j));
/*     */       }
/*     */     } }
/*     */   
/* 172 */   public void expand(int newModelLength) { SimpleExtendedDistribution[] newTransitionsOut = new SimpleExtendedDistribution[newModelLength];
/* 173 */     System.arraycopy(this.transitionsOut, 0, newTransitionsOut, 0, this.transitionsOut.length);
/*     */     
/* 175 */     this.transitionsOut = newTransitionsOut;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/TransitionProbs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */