/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import lc1.dp.CollapsedHMM;
/*     */ import lc1.dp.CompoundMarkovModel;
/*     */ import lc1.dp.CompoundState;
/*     */ import lc1.dp.CopyEnumerator;
/*     */ import lc1.dp.MarkovModel;
/*     */ import lc1.dp.PairMarkovModel;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedHMM
/*     */   extends CompoundMarkovModel
/*     */ {
/*     */   public CompoundMarkovModel innerModel;
/*     */   private final SiteTransitions trans;
/*     */   
/*     */   public void transferEmissionCountsToMemberStates()
/*     */   {
/*  29 */     for (int j = 1; j < modelLength(); j++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*  34 */       ((CachedEmissionState)getState(j)).transferCountsToMemberStates(); }
/*     */   }
/*     */   
/*     */   public static CachedHMM makeGenotypeHMM(MarkovModel hapHMM, int noCopies) {
/*  38 */     int[] nCopies = new int[noCopies];
/*  39 */     Arrays.fill(nCopies, 0);
/*  40 */     CompoundMarkovModel hmm_1 = new PairMarkovModel(new MarkovModel[] { hapHMM }, nCopies);
/*  41 */     if (Constants.fast()) hmm_1 = new CollapsedHMM(hmm_1);
/*  42 */     return new CachedHMM(hmm_1);
/*     */   }
/*     */   
/*     */   public CachedHMM(CompoundMarkovModel innerModel) {
/*  46 */     super(innerModel.getName() + "x" + innerModel.getName(), innerModel.MAGIC, innerModel.noSnps);
/*  47 */     this.innerModel = innerModel;
/*     */     
/*  49 */     this.trans = new SiteTransitions(this.noSnps);
/*  50 */     for (int j = 1; j < innerModel.modelLength(); j++) {
/*  51 */       addState(new CachedEmissionState((CompoundState)innerModel.getState(j), this.noSnps, innerModel.getEmissionStateSpace().size()));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void refreshSiteTransitions()
/*     */   {
/*  59 */     this.trans.initialise(this.states, Double.POSITIVE_INFINITY);
/*  60 */     CopyEnumerator dblIterator = new CopyEnumerator(2)
/*     */     {
/*  62 */       public Iterator getPossibilities(int depth) { return CachedHMM.this.states(); }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/*  65 */         State from = (State)list[0];
/*  66 */         State to = (State)list[1];
/*  67 */         for (int i = 0; i < CachedHMM.this.noSnps; i++) {
/*  68 */           double sc = CachedHMM.this.innerModel.getTransitionScore(from.index, to.index, i);
/*  69 */           if (sc > 0.0D) CachedHMM.this.trans.setTransitionScore(from.index, to.index, i, sc, sc);
/*     */         }
/*     */       }
/*     */       
/*  73 */       public boolean exclude(Object obj, Object previous) { return false;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  78 */     };
/*  79 */     dblIterator.run();
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/*  84 */     CompoundMarkovModel m = (CompoundMarkovModel)this.innerModel.clone();
/*  85 */     return new CachedHMM(m);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double getTransitionScore(int from, int to, int indexOfToEmission)
/*     */   {
/*  92 */     return this.trans.getTransitionScore(from, to, indexOfToEmission);
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs() {
/*  96 */     this.innerModel.transferCountsToProbs();
/*     */   }
/*     */   
/*     */   public void refresh()
/*     */   {
/* 101 */     lc1.dp.BaumWelchTrainer.t[8] = System.currentTimeMillis();
/* 102 */     refreshSiteTransitions();
/* 103 */     lc1.dp.BaumWelchTrainer.t[9] = System.currentTimeMillis();
/* 104 */     for (int j = 1; j < modelLength(); j++) {
/* 105 */       ((CachedEmissionState)getState(j)).refreshSiteEmissions();
/*     */     }
/* 107 */     lc1.dp.BaumWelchTrainer.t[10] = System.currentTimeMillis();
/*     */   }
/*     */   
/*     */   public void addCounts(StateDistribution[] transProbs, int i)
/*     */   {
/* 112 */     this.innerModel.addCounts(transProbs, i);
/*     */   }
/*     */   
/*     */ 
/*     */   protected List initStateSpace()
/*     */   {
/* 118 */     return this.innerModel.getEmissionStateSpace();
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 123 */     this.innerModel.initialiseTransitionCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPseudoCountWeights(double d)
/*     */   {
/* 129 */     this.innerModel.setPseudoCountWeights(d);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly)
/*     */   {
/* 135 */     this.innerModel.setRandomTransitions(u, restart, lastOnly);
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] statesIn(int j, int i)
/*     */   {
/* 141 */     return this.innerModel.statesIn(j, i);
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i)
/*     */   {
/* 146 */     return this.innerModel.statesOut(j, i);
/*     */   }
/*     */   
/*     */   public double totalTransitionDistance(MarkovModel m1)
/*     */   {
/* 151 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public boolean transitionsChanged()
/*     */   {
/* 156 */     return this.innerModel.transitionsChanged();
/*     */   }
/*     */   
/*     */   public void transitionsFixed()
/*     */   {
/* 161 */     this.innerModel.transitionsFixed();
/*     */   }
/*     */   
/*     */ 
/*     */   public List getEmissionStateSpace()
/*     */   {
/* 167 */     return this.innerModel.getEmissionStateSpace();
/*     */   }
/*     */   
/*     */   public int getEmissionStateSpaceIndex(Object element)
/*     */   {
/* 172 */     return this.innerModel.getEmissionStateSpaceIndex(element);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateEmissionStateSpaceDist()
/*     */   {
/* 179 */     this.innerModel.updateEmissionStateSpaceDist();
/*     */   }
/*     */   
/*     */   public double[] getEmissionStateSpaceDistribution(int index)
/*     */   {
/* 184 */     return this.innerModel.getEmissionStateSpaceDistribution(index);
/*     */   }
/*     */   
/*     */   public MarkovModel getMarkovModel(int i)
/*     */   {
/* 189 */     return this.innerModel.getMarkovModel(i);
/*     */   }
/*     */   
/*     */   public int noCopies() {
/* 193 */     return this.innerModel.noCopies();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/CachedHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */