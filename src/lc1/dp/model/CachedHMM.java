/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.Callable;
/*     */ import lc1.dp.core.BaumWelchTrainer;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.states.AbstractCachedEmissionState;
/*     */ import lc1.dp.states.CachedEmissionState;
/*     */ import lc1.dp.states.CompoundState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.dp.transition.FreeTransitionProbs1;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.StateDistribution;
/*     */ import lc1.util.Constants;
/*     */ import lc1.util.CopyEnumerator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedHMM
/*     */   extends CompoundMarkovModel
/*     */   implements WrappedModel
/*     */ {
/*     */   public CompoundMarkovModel innerModel;
/*     */   public final SiteTransitions trans;
/*     */   final List tasks;
/*     */   
/*  34 */   public CompoundMarkovModel getHMM() { return this.innerModel; }
/*     */   
/*     */   public PairMarkovModel unwrapModel() {
/*  37 */     CompoundMarkovModel hmm = this.innerModel;
/*  38 */     while ((hmm instanceof WrappedModel)) {
/*  39 */       hmm = ((WrappedModel)hmm).getHMM();
/*     */     }
/*  41 */     return (PairMarkovModel)hmm;
/*     */   }
/*     */   
/*     */   public boolean trainEmissions() {
/*  45 */     return this.innerModel.trainEmissions();
/*     */   }
/*     */   
/*     */   public void transferEmissionCountsToMemberStates() {
/*  49 */     if (trainEmissions())
/*     */     {
/*  51 */       for (int j = 1; j < modelLength(); j++) {
/*  52 */         final int j1 = j;
/*  53 */         this.tasks.set(j - 1, new Callable() {
/*     */           public Object call() {
/*  55 */             ((AbstractCachedEmissionState)CachedHMM.this.getState(j1)).transferCountsToMemberStates();
/*  56 */             return null;
/*     */           }
/*     */         });
/*     */       }
/*     */       try {
/*  61 */         BaumWelchTrainer.involeTasks(this.tasks, true);
/*     */       } catch (Exception exc) {
/*  63 */         exc.printStackTrace();
/*     */       }
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
/*     */   public CachedHMM(CompoundMarkovModel innerModel)
/*     */   {
/*  80 */     super(innerModel.getName() + "x" + innerModel.getName(), innerModel.noSnps.intValue(), null);
/*     */     
/*  82 */     this.innerModel = innerModel;
/*     */     
/*     */ 
/*     */ 
/*  86 */     for (int j = 1; j < innerModel.modelLength(); j++)
/*     */     {
/*  88 */       CompoundState state_j = (CompoundState)innerModel.getState(j);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */       EmissionState newState = new CachedEmissionState(
/*  96 */         state_j, 
/*  97 */         state_j.getEmissionStateSpace().size());
/*     */       
/*  99 */       addState(newState);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 104 */     this.trans = new FreeSiteTrans1(this.states, this.noSnps, FreeTransitionProbs1.class);
/*     */     
/* 106 */     double[] rel = new double[this.states.size()];
/* 107 */     Arrays.fill(rel, 1.0D / (this.states.size() - 1));
/* 108 */     rel[0] = 0.0D;
/*     */     try {
/* 110 */       this.trans.transProbs[0] = new FreeTransitionProbs1(true, new Dirichlet(rel, 1.0E10D), this.states.size());
/*     */       
/* 112 */       this.trans.initialise(rel, 0.0D);
/*     */     }
/*     */     catch (Exception exc) {
/* 115 */       exc.printStackTrace();
/*     */     }
/* 117 */     this.tasks = Arrays.asList(new Callable[modelLength() - 1]);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean needInit()
/*     */   {
/* 123 */     return this.trans.transProbs[0] == null;
/*     */   }
/*     */   
/*     */   private void refreshSiteTransitions() {
/* 127 */     CopyEnumerator dblIterator = new CopyEnumerator(2)
/*     */     {
/* 129 */       public Iterator getPossibilities(int depth) { return CachedHMM.this.states(); }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/* 132 */         State from = (State)list[0];
/* 133 */         State to = (State)list[1];
/* 134 */         for (int i = 0; i < CachedHMM.this.noSnps.intValue(); i++) {
/* 135 */           double sc = CachedHMM.this.innerModel.getTransitionScore(from.getIndex(), to.getIndex(), i);
/*     */           
/*     */ 
/* 138 */           CachedHMM.this.trans.setTransitionScore(from.getIndex(), to.getIndex(), i, sc, sc);
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean exclude(Object obj, Object previous) {
/* 143 */         return false;
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 148 */     };
/* 149 */     dblIterator.run();
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
/*     */   public Object clone(boolean swtch)
/*     */   {
/* 168 */     CompoundMarkovModel m = (CompoundMarkovModel)this.innerModel.clone(swtch);
/* 169 */     return new CachedHMM(m);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getTransitionScore(int from, int to, int indexOfToEmission)
/*     */   {
/* 176 */     return this.trans.getTransitionScore(from, to, indexOfToEmission);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transferCountsToProbs(int index)
/*     */   {
/* 185 */     this.innerModel.transferCountsToProbs(index);
/*     */   }
/*     */   
/*     */   public void initialiseEmissionCounts()
/*     */   {
/* 190 */     super.initialiseEmissionCounts();
/* 191 */     for (int i = 0; i < Constants.format().length; i++) {
/* 192 */       probB(i).initialiseBCounts();
/*     */       
/*     */ 
/* 195 */       List<SkewNormal> s = new ArrayList();
/* 196 */       probR(null, null, s, null, i);
/* 197 */       for (Iterator<SkewNormal> it = s.iterator(); it.hasNext();) {
/* 198 */         SkewNormal dist = (SkewNormal)it.next();
/*     */         
/* 200 */         dist.initialiseCounts();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void refresh() {
/* 206 */     initialiseEmissionCounts();
/* 207 */     this.innerModel.refresh();
/*     */     
/* 209 */     refreshSiteTransitions();
/*     */     
/* 211 */     if (trainEmissions()) {
/* 212 */       for (int j = 1; j < modelLength(); j++) {
/* 213 */         final State st = getState(j);
/* 214 */         this.tasks.set(j - 1, 
/* 215 */           new Callable()
/*     */           {
/*     */             public Object call() {
/* 218 */               ((AbstractCachedEmissionState)st).refreshSiteEmissions();
/* 219 */               return null;
/*     */             }
/*     */           });
/*     */       }
/*     */       try
/*     */       {
/* 225 */         BaumWelchTrainer.involeTasks(this.tasks, false);
/*     */       } catch (Exception exc) {
/* 227 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCounts(StateDistribution[] transProbs, int i, int numIndiv)
/*     */   {
/* 235 */     this.innerModel.addCounts(transProbs, i, numIndiv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 247 */     this.innerModel.initialiseTransitionCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPseudoCountWeights(double[] d, double[] d1)
/*     */   {
/* 253 */     this.innerModel.setPseudoCountWeights(d, d1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] statesIn(int j, int i)
/*     */   {
/* 265 */     return this.innerModel.statesIn(j, i);
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i)
/*     */   {
/* 270 */     return this.innerModel.statesOut(j, i);
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
/*     */   public MarkovModel getMarkovModel(int i)
/*     */   {
/* 301 */     return this.innerModel.getMarkovModel(i);
/*     */   }
/*     */   
/*     */   public int noCopies() {
/* 305 */     return this.innerModel.noCopies();
/*     */   }
/*     */   
/*     */ 
/*     */   public EmissionState[] disambiguate(EmissionState[] memberStates, EmissionState[] prev, int i, boolean sample)
/*     */   {
/* 311 */     return this.innerModel.disambiguate(memberStates, prev, i, sample);
/*     */   }
/*     */   
/*     */   public CompoundState getCompoundState(EmissionState[] res)
/*     */   {
/* 316 */     return this.innerModel.getCompoundState(res);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public MarkovModel[] getMemberModels()
/*     */   {
/* 323 */     return this.innerModel.getMemberModels();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IlluminaProbB probB(int i)
/*     */   {
/* 331 */     return this.innerModel.probB(i);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/CachedHMM.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */