/*     */ package lc1.dp;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import lc1.dp.genotype.CachedEmissionState;
/*     */ import lc1.dp.genotype.SimpleExtendedDistribution;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ 
/*     */ 
/*     */ public class CollapsedHMM
/*     */   extends CompoundMarkovModel
/*     */ {
/*     */   public CompoundMarkovModel hmm;
/*     */   public final int[] collapse;
/*  16 */   public final List<int[]> expand = new ArrayList();
/*     */   final int[] in;
/*     */   
/*     */   public void splitCounts()
/*     */   {
/*  21 */     for (int j = 1; j < this.expand.size(); j++) {
/*  22 */       CachedEmissionState ges = (CachedEmissionState)getState(j);
/*  23 */       SimpleExtendedDistribution[] emissions = ges.emissions;
/*  24 */       int[] ex = (int[])this.expand.get(j);
/*  25 */       SimpleExtendedDistribution[][] l = new SimpleExtendedDistribution[ex.length][];
/*  26 */       for (int k = 0; k < ex.length; k++) {
/*  27 */         l[k] = ((CachedEmissionState)getState(ex[k])).emissions;
/*     */       }
/*  29 */       for (int i = 0; i < emissions.length; i++) {
/*  30 */         double[] count = emissions[i].counts;
/*  31 */         for (int i1 = 0; i1 < count.length; i1++) {
/*  32 */           double cnt = count[i1] / ex.length;
/*  33 */           for (int k = 0; k < ex.length; k++) {
/*  34 */             l[k][i].counts[i1] = cnt;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public CollapsedHMM(CompoundMarkovModel hmm)
/*     */   {
/*  43 */     super(hmm.getName() + "f", hmm.MAGIC, hmm.noSnps);
/*  44 */     this.hmm = hmm;
/*  45 */     this.collapse = new int[hmm.states.size()];
/*  46 */     this.collapse[0] = 0;
/*  47 */     this.expand.add(new int[1]);
/*  48 */     for (Iterator<int[]> it = hmm.equivalenceClasses.iterator(); it.hasNext();) {
/*  49 */       int[] equiv = (int[])it.next();
/*  50 */       State st = addState(hmm.getState(equiv[0]));
/*  51 */       for (int i = 0; i < equiv.length; i++) {
/*  52 */         this.collapse[equiv[i]] = st.getIndex();
/*     */       }
/*  54 */       if (st.getIndex() != this.expand.size()) throw new RuntimeException("!!");
/*  55 */       this.expand.add(equiv);
/*     */     }
/*  57 */     this.in = new int[this.states.size() - 1];
/*  58 */     for (int jk = 1; jk < this.states.size(); jk++) {
/*  59 */       this.in[(jk - 1)] = jk;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCounts(StateDistribution[] transProbs, int i)
/*     */   {
/*  68 */     StateDistribution[] transProbs1 = new StateDistribution[this.hmm.states.size()];
/*  69 */     for (int j = 0; j < this.hmm.states.size(); j++) {
/*  70 */       transProbs1[j] = new StateDistribution(this.hmm.states.size());
/*     */     }
/*  72 */     for (int j = 0; j < transProbs.length; j++) {
/*  73 */       for (int k = 0; k < transProbs[j].dist.length; k++) {
/*  74 */         int[] from = (int[])this.expand.get(j);
/*  75 */         double[][] prob = new double[from.length][];
/*  76 */         double sum = 0.0D;
/*  77 */         double value = transProbs[j].dist[k];
/*  78 */         int[] to = (int[])this.expand.get(k);
/*  79 */         for (int m = 0; m < from.length; m++) {
/*  80 */           prob[m] = new double[to.length];
/*  81 */           for (int n = 0; n < to.length; n++) {
/*  82 */             prob[m][n] = this.hmm.getTransitionScore(from[m], to[n], i + 1);
/*  83 */             sum += prob[m][n];
/*     */           }
/*     */         }
/*     */         
/*  87 */         for (int m = 0; m < from.length; m++) {
/*  88 */           for (int n = 0; n < to.length; n++) {
/*  89 */             transProbs1[from[m]].dist[to[n]] += value * (sum == 0.0D ? 1.0D : prob[m][n] / sum);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  94 */     double diff = Math.abs(sum(transProbs) - sum(transProbs1));
/*     */     
/*     */ 
/*     */ 
/*  98 */     this.hmm.addCounts(transProbs1, i);
/*     */   }
/*     */   
/*     */   private double sum(StateDistribution[] transProbs)
/*     */   {
/* 103 */     double sum = 0.0D;
/* 104 */     for (int i = 0; i < transProbs.length; i++) {
/* 105 */       sum += transProbs[i].sum();
/*     */     }
/* 107 */     return sum;
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/* 112 */     return null;
/*     */   }
/*     */   
/*     */   public double getTransitionScore(int from, int to, int positionOfToEmission)
/*     */   {
/* 117 */     int[] frome = (int[])this.expand.get(from);
/* 118 */     int[] toe = (int[])this.expand.get(to);
/* 119 */     double sc = 0.0D;
/* 120 */     for (int i = 0; i < frome.length; i++) {
/* 121 */       for (int j = 0; j < toe.length; j++) {
/* 122 */         sc += this.hmm.getTransitionScore(frome[i], toe[j], positionOfToEmission);
/*     */       }
/*     */     }
/* 125 */     return sc / frome.length;
/*     */   }
/*     */   
/*     */   protected List initStateSpace()
/*     */   {
/* 130 */     return this.hmm.initStateSpace();
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 135 */     this.hmm.initialiseTransitionCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPseudoCountWeights(double d)
/*     */   {
/* 141 */     this.hmm.setPseudoCountWeights(d);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly)
/*     */   {
/* 147 */     this.hmm.setRandomTransitions(u, restart, lastOnly);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double totalTransitionDistance(MarkovModel m1)
/*     */   {
/* 155 */     return 0.0D;
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs()
/*     */   {
/* 160 */     this.hmm.transferCountsToProbs();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean transitionsChanged()
/*     */   {
/* 166 */     return this.hmm.transitionsChanged();
/*     */   }
/*     */   
/*     */   public void transitionsFixed()
/*     */   {
/* 171 */     this.hmm.transitionsFixed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 176 */   final int[] in0 = new int[1];
/*     */   
/*     */   public int[] statesIn(int j, int i) {
/* 179 */     if (i == 0) return this.in0;
/* 180 */     return this.in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i) {
/* 184 */     if (i == this.noSnps - 1) return this.in0;
/* 185 */     return this.in;
/*     */   }
/*     */   
/*     */   public List getEmissionStateSpace()
/*     */   {
/* 190 */     return this.hmm.getEmissionStateSpace();
/*     */   }
/*     */   
/*     */   public int getEmissionStateSpaceIndex(Object element)
/*     */   {
/* 195 */     return this.hmm.getEmissionStateSpaceIndex(element);
/*     */   }
/*     */   
/*     */   public double[] getEmissionStateSpaceDistribution(int index) {
/* 199 */     return this.hmm.getEmissionStateSpaceDistribution(index);
/*     */   }
/*     */   
/*     */   public void updateEmissionStateSpaceDist()
/*     */   {
/* 204 */     this.hmm.updateEmissionStateSpaceDist();
/*     */   }
/*     */   
/*     */   public MarkovModel getMarkovModel(int i)
/*     */   {
/* 209 */     return this.hmm.getMarkovModel(i);
/*     */   }
/*     */   
/*     */   public int noCopies()
/*     */   {
/* 214 */     return this.hmm.noCopies();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/CollapsedHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */