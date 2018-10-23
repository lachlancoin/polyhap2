/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.dp.transition.AbstractTransitionProbs;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.stats.ProbabilityDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.stats.StateDistribution;
/*     */ import lc1.util.Constants;
/*     */ import lc1.util.CopyEnumerator;
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
/*  32 */   static double epsilon = 1.0E-8D;
/*     */   Dirichlet dir;
/*     */   
/*  35 */   public String info() { return this.trans.info(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeHaplotypeHMM(String name, int numFounders, int noSnps, double[] init, EmissionStateSpace emStSp, Object[] clazz, List<Integer> locs, Double[] r, Double[] exp_p1, char[] mod, double[] rel, boolean correlateR, ProbabilityDistribution[] numLevels)
/*     */   {
/*  42 */     super(name, numFounders, noSnps, init, emStSp, mod, locs, numLevels);
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
/*  58 */     if (clazz.length > 1) {
/*  59 */       this.trans = new FreeExpSiteTrans(locs, this.states, exp_p1, r, noSnps, clazz);
/*     */     }
/*  61 */     else if (correlateR) {
/*  62 */       this.trans = new ExpSiteTrans(locs, this.states, exp_p1, r, noSnps);
/*     */     }
/*     */     else {
/*  65 */       this.trans = new FreeSiteTrans1(locs, this.states, exp_p1, r, noSnps, clazz[0]);
/*     */     }
/*  67 */     if (rel.length != mod.length) { throw new RuntimeException("!!" + rel.length + " " + mod.length);
/*     */     }
/*  69 */     this.trans.initialise(this.states, locs, r, exp_p1, rel);
/*  70 */     boolean mod1 = Constants.modifyWithData() > 0;
/*  71 */     this.modifyWithData = (((clazz.length == 1) || (((Class[])clazz[1]).length == 1)) && (mod1));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeHaplotypeHMM(FreeHaplotypeHMM hmm, boolean swtch)
/*     */   {
/*  83 */     super(hmm);
/*     */     
/*  85 */     this.modifyWithData = hmm.modifyWithData;
/*  86 */     this.trans = hmm.trans.clone(swtch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeHaplotypeHMM(final MarkovModel hmm)
/*     */     throws Exception
/*     */   {
/*  96 */     super(hmm);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 103 */     this.modifyWithData = false;
/*     */     
/*     */ 
/* 106 */     this.trans = new FreeSiteTrans1(hmm.noSnps, hmm.modelLength());
/*     */     
/* 108 */     CopyEnumerator dblIterator = new CopyEnumerator(2)
/*     */     {
/* 110 */       public Iterator getPossibilities(int depth) { return FreeHaplotypeHMM.this.states(); }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/* 113 */         State from = (State)list[0];
/* 114 */         State to = (State)list[1];
/* 115 */         for (int i = 0; i < FreeHaplotypeHMM.this.noSnps.intValue(); i++) {
/* 116 */           double sc = hmm.getTransitionScore(from.getIndex(), to.getIndex(), i);
/* 117 */           if ((i == FreeHaplotypeHMM.this.noSnps.intValue() - 1) && (to.getIndex() == 0)) sc = 0.0D;
/* 118 */           if (sc > 0.0D) {
/* 119 */             FreeHaplotypeHMM.this.trans.setTransitionScore(from.getIndex(), to.getIndex(), i, sc, sc);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       public boolean exclude(Object obj, Object previous) {
/* 125 */         return false;
/*     */       }
/*     */       
/* 128 */     };
/* 129 */     dblIterator.run();
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
/*     */   public MarkovModel clone(boolean swtch)
/*     */   {
/* 180 */     return new FreeHaplotypeHMM(this, swtch);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean converged()
/*     */   {
/* 186 */     return this.trans.converged();
/*     */   }
/*     */   
/*     */ 
/*     */   public void validateTransAt(int to)
/*     */   {
/* 192 */     this.trans.validateTransAt(to);
/*     */   }
/*     */   
/*     */   public double getTransitionScore(int from, int to, int indexOfToEmission) {
/* 196 */     return this.trans.getTransitionScore(from, to, indexOfToEmission);
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs(int index) {
/* 200 */     super.transferCountsToProbs(index);
/*     */   }
/*     */   
/*     */   public void validate(int length) throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 207 */       super.validate(length);
/*     */     }
/*     */     catch (Exception exc) {
/* 210 */       exc.printStackTrace();
/* 211 */       System.err.println("validating transitions ");
/* 212 */       this.trans.validate();
/* 213 */       System.err.println("validating transitions done");
/* 214 */       System.exit(0);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private int[] getRandomTransformation()
/*     */   {
/* 223 */     int len = modelLength();
/* 224 */     List<Integer> l = new ArrayList(len);
/* 225 */     for (int i = 1; i < len; i++) {
/* 226 */       l.add(Integer.valueOf(i));
/*     */     }
/* 228 */     int[] res = new int[len];
/* 229 */     res[0] = 0;
/* 230 */     for (int i = 1; i < len; i++) {
/* 231 */       res[i] = ((Integer)l.remove(Constants.nextInt(l.size()))).intValue();
/*     */     }
/*     */     
/* 234 */     return res;
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
/*     */   class DoubleInteger
/*     */     implements Comparable
/*     */   {
/*     */     double d;
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
/*     */     int i;
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
/*     */     DoubleInteger(int i, double d)
/*     */     {
/* 282 */       this.i = i;
/* 283 */       this.d = d;
/*     */     }
/*     */     
/* 286 */     public String toString() { return this.i + ":" + this.d + " "; }
/*     */     
/*     */     public int compareTo(Object o) {
/* 289 */       DoubleInteger d1 = (DoubleInteger)o;
/* 290 */       if (this.d == d1.d) return 0;
/* 291 */       if (this.d < d1.d) return 1;
/* 292 */       return -1;
/*     */     }
/*     */   }
/*     */   
/* 296 */   private List<DoubleInteger> getList(SimpleExtendedDistribution[] probs, int j) { List<DoubleInteger> l = new ArrayList();
/* 297 */     for (int i = 1; i < probs.length; i++) {
/* 298 */       l.add(new DoubleInteger(i, probs[i].probs[j]));
/*     */     }
/* 300 */     return l;
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
/*     */   private static int[] getTransformation(Double[] doubles)
/*     */   {
/* 325 */     int len = doubles.length;
/* 326 */     Double[] copy = new Double[len];
/*     */     
/* 328 */     int[] res = new int[len];
/* 329 */     System.arraycopy(doubles, 0, copy, 0, len);
/* 330 */     Arrays.sort(copy);
/* 331 */     Set<Integer> done = new HashSet();
/* 332 */     for (int i = 0; i < len; i++) {
/* 333 */       Double d = doubles[i];
/* 334 */       int j = 0;
/*     */       for (;;) {
/* 336 */         if (done.contains(Integer.valueOf(j))) {
/* 337 */           j++;
/*     */         }
/*     */         else {
/* 340 */           if (copy[j] == d) break;
/* 341 */           j++;
/*     */         } }
/* 343 */       done.add(Integer.valueOf(j));
/* 344 */       res[i] = j;
/*     */     }
/* 346 */     if (done.size() != doubles.length) throw new RuntimeException("!!");
/* 347 */     if (res[0] != 0) throw new RuntimeException("!!");
/* 348 */     for (int i = 1; i < len; i++) {
/* 349 */       res[i] = (len - 1 - res[i] + 1);
/*     */     }
/* 351 */     return res;
/*     */   }
/*     */   
/* 354 */   public void print(PrintWriter sb, List<Integer> cols, int popsize) { super.print(sb, cols, popsize);
/* 355 */     StringBuffer sb1 = new StringBuffer();
/* 356 */     Double[] u = new Double[this.noSnps.intValue() + 1];
/* 357 */     for (int i = 0; i < this.noSnps.intValue(); i++)
/*     */     {
/* 359 */       sb1.append("%8.3g ");
/* 360 */       u[i] = Double.valueOf(this.pseudocountWeights[0]);
/*     */     }
/* 362 */     String sbS = sb1.toString();
/* 363 */     sb.println("transitions");
/* 364 */     this.trans.print(sb, sbS, this.states, getHittingProb(this.noSnps.intValue()));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 369 */   static Double zero = new Double(0.0D);
/*     */   final boolean modifyWithData;
/*     */   
/*     */   public void addCounts(StateDistribution[] observed, int i, int numIndiv) {
/* 373 */     if (i + 1 >= this.trans.transProbs.length) return;
/* 374 */     this.trans.transProbs[(i + 1)].addCounts(observed);
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 379 */     this.trans.initialiseTransitionCounts();
/*     */   }
/*     */   
/*     */   public void transferTransitionCountsToProbs(int index) {
/* 383 */     double[] p = { this.pseudocountWeights[1], this.pseudocountWeights1[1] };
/* 384 */     double[] e = { this.pseudocountWeights[2], this.pseudocountWeights1[2] };
/* 385 */     this.trans.transferTransitions(p, e, index);
/*     */   }
/*     */   
/*     */   public boolean modifyWithData()
/*     */   {
/* 390 */     return this.modifyWithData;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/FreeHaplotypeHMM.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */