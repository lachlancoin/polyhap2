/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.CopyEnumerator;
/*     */ import lc1.dp.Dirichlet;
/*     */ import lc1.dp.EmissionState;
/*     */ import lc1.dp.MarkovModel;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.TransitionProbs;
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
/*     */ public class FreeHaplotypeHMM
/*     */   extends HaplotypeHMM
/*     */ {
/*     */   public final SiteTransitions trans;
/*     */   static final long serialVersionUID = 1L;
/*  29 */   static double epsilon = 1.0E-8D;
/*     */   Dirichlet dir;
/*     */   
/*  32 */   public FreeHaplotypeHMM(String name, int numFounders, int noSnps, double u) { super(name, numFounders, noSnps, u);
/*  33 */     this.trans = new SiteTransitions(noSnps);
/*  34 */     this.trans.initialise(this.states, u);
/*     */   }
/*     */   
/*  37 */   public FreeHaplotypeHMM(final HaplotypeHMM hmm) { super(hmm, hmm);
/*  38 */     this.trans = new SiteTransitions(this.noSnps);
/*  39 */     this.trans.initialise(this.states, 1.0D);
/*  40 */     CopyEnumerator dblIterator = new CopyEnumerator(2)
/*     */     {
/*  42 */       public Iterator getPossibilities(int depth) { return FreeHaplotypeHMM.this.states(); }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/*  45 */         State from = (State)list[0];
/*  46 */         State to = (State)list[1];
/*  47 */         for (int i = 0; i < FreeHaplotypeHMM.this.noSnps; i++) {
/*  48 */           double sc = hmm.getTransitionScore(from.index, to.index, i);
/*  49 */           if (sc > 0.0D)
/*  50 */             FreeHaplotypeHMM.this.trans.setTransitionScore(from.index, to.index, i, sc, sc);
/*     */         }
/*     */       }
/*     */       
/*  54 */       public boolean exclude(Object obj, Object previous) { return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  59 */     };
/*  60 */     dblIterator.run();
/*     */   }
/*     */   
/*     */ 
/*     */   public MarkovModel clone(MarkovModel pseudo, double d)
/*     */   {
/*  66 */     return new FreeHaplotypeHMM(this, (FreeHaplotypeHMM)pseudo, d);
/*     */   }
/*     */   
/*     */   public boolean increaseStates(double u, double small, int numToAdd)
/*     */   {
/*  71 */     this.hittingProb = null;
/*     */     
/*  73 */     int j = this.states.size() - 1;
/*  74 */     HaplotypeEmissionState toAdd = new HaplotypeEmissionState(j + 1, this.noSnps, new double[] { 0.0D, 0.0D, 1.0D });
/*  75 */     addState(toAdd);
/*     */     
/*  77 */     this.trans.increaseStates(small, numToAdd, modelLength(), u);
/*     */     
/*  79 */     Logger.getAnonymousLogger().info("finished");
/*  80 */     return true;
/*     */   }
/*     */   
/*     */   public FreeHaplotypeHMM(FreeHaplotypeHMM hmm_init, FreeHaplotypeHMM hmm_ps, double pseudocount)
/*     */   {
/*  85 */     super(hmm_init, hmm_ps);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  91 */     this.trans = new SiteTransitions(hmm_init.trans, hmm_ps.trans, this.states);
/*     */   }
/*     */   
/*     */   public Object clone() {
/*  95 */     return new FreeHaplotypeHMM(this, this, 0.0D);
/*     */   }
/*     */   
/*     */   public double getTransitionScore(int from, int to, int indexOfToEmission) {
/*  99 */     return this.trans.getTransitionScore(from, to, indexOfToEmission);
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs() {
/* 103 */     super.transferCountsToProbs();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double totalTransitionDistance(MarkovModel m1)
/*     */   {
/* 115 */     FreeHaplotypeHMM hmm = (FreeHaplotypeHMM)m1;
/* 116 */     return this.trans.totalTransitionDistance(hmm.trans);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(final PrintWriter sb)
/*     */   {
/* 122 */     super.print(sb);
/* 123 */     StringBuffer sb1 = new StringBuffer();
/* 124 */     Double[] u = new Double[this.noSnps + 1];
/* 125 */     for (int i = 0; i < this.noSnps; i++)
/*     */     {
/* 127 */       sb1.append("%8.3g ");
/* 128 */       u[i] = Double.valueOf(this.pseudocountWeights);
/*     */     }
/* 130 */     final String sbS = sb1.toString();
/* 131 */     sb.println("pseudo " + Format.sprintf(sbS.toString(), u));
/* 132 */     CopyEnumerator dblIterator = new CopyEnumerator(2)
/*     */     {
/* 134 */       public Iterator getPossibilities(int depth) { return FreeHaplotypeHMM.this.states(); }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/* 137 */         State st1 = (State)list[0];
/* 138 */         State st2 = (State)list[1];
/* 139 */         sb.print(st1.getName() + "->" + st2.getName());
/* 140 */         sb.println(Format.sprintf(sbS.toString(), FreeHaplotypeHMM.this.trans.getTrans(st1.index, st2.index)));
/*     */       }
/*     */       
/* 143 */       public boolean exclude(Object obj, Object previos) { if (!(obj instanceof EmissionState)) return true;
/* 144 */         return false;
/*     */       }
/*     */       
/* 147 */     };
/* 148 */     dblIterator.run();
/*     */   }
/*     */   
/*     */ 
/* 152 */   static Double zero = new Double(0.0D);
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
/*     */   public void addCounts(StateDistribution[] observed, int i)
/*     */   {
/* 166 */     if (i + 1 >= this.trans.transProbs.length) return;
/* 167 */     this.trans.transProbs[(i + 1)].addCounts(observed, this.states);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 176 */     this.trans.initialiseTransitionCounts();
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
/*     */ 
/*     */ 
/*     */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly)
/*     */   {
/* 262 */     this.trans.setRandomTransitions(u, restart, lastOnly);
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
/*     */   public boolean transitionsChanged()
/*     */   {
/* 300 */     return this.trans.transitionsChanged();
/*     */   }
/*     */   
/*     */   public void transitionsFixed() {
/* 304 */     this.trans.transitionsFixed();
/*     */   }
/*     */   
/*     */   public void transferTransitionCountsToProbs() {
/* 308 */     this.trans.transferTransitions(this.pseudocountWeights);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/FreeHaplotypeHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */