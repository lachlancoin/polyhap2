/*     */ package lc1.dp.transition;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.stats.Sampler;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class BetweenWithinTransitionProbs1 extends AbstractTransitionProbs
/*     */ {
/*     */   final AbstractTransitionProbs transBetweenGroups;
/*     */   final ExpTransProb[] alphaWithinGroup;
/*     */   final AbstractTransitionProbs[] transWithinGroups;
/*     */   final int[] stateToGroup;
/*     */   final int[] stateToIndexWithinGroup;
/*     */   
/*     */   public String info()
/*     */   {
/*  22 */     StringBuffer info = new StringBuffer(super.info());
/*  23 */     info.append("between " + this.transBetweenGroups.info() + "  within " + this.transWithinGroups[1].info() + " alpha wihtin " + this.alphaWithinGroup[1].info());
/*  24 */     return info.toString();
/*     */   }
/*     */   
/*     */   public AbstractTransitionProbs clone(boolean swtch)
/*     */   {
/*  29 */     if (swtch) return new BetweenWithinTransitionProbs3(this);
/*  30 */     return new BetweenWithinTransitionProbs1(this, swtch);
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
/*     */   public int noStates()
/*     */   {
/*  53 */     return this.stateToGroup.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getBetweeenGroupTrans(int from, int groupFrom, int groupTo)
/*     */   {
/*  62 */     return this.transBetweenGroups.getTransition(groupFrom, groupTo);
/*     */   }
/*     */   
/*     */   public final double getTransition(int from, int to) {
/*  66 */     int groupFrom = this.stateToGroup[from];
/*  67 */     int indexFrom = this.stateToIndexWithinGroup[from];
/*  68 */     int groupTo = this.stateToGroup[to];
/*  69 */     int indexTo = this.stateToIndexWithinGroup[to];
/*  70 */     double groupSc = getBetweeenGroupTrans(from, groupFrom, groupTo);
/*     */     
/*     */ 
/*  73 */     if (groupTo == groupFrom) {
/*  74 */       return groupSc * this.transWithinGroups[groupFrom].getTransition(indexFrom, indexTo);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  79 */     return groupSc * this.alphaWithinGroup[groupTo].getExp(from).probs(indexTo);
/*     */   }
/*     */   
/*     */ 
/*     */   public void addGroupTransCount(int from, int groupFrom, int groupTo, double val)
/*     */   {
/*  85 */     this.transBetweenGroups.addCount(groupFrom, groupTo, val);
/*     */   }
/*     */   
/*     */   public final void addCount(int from, int to, double val)
/*     */   {
/*  90 */     int groupFrom = this.stateToGroup[from];
/*  91 */     int indexFrom = this.stateToIndexWithinGroup[from];
/*  92 */     int groupTo = this.stateToGroup[to];
/*  93 */     int indexTo = this.stateToIndexWithinGroup[to];
/*  94 */     addGroupTransCount(from, groupFrom, groupTo, val);
/*     */     
/*     */ 
/*  97 */     if (groupTo != groupFrom)
/*     */     {
/*     */ 
/* 100 */       this.alphaWithinGroup[groupTo].getExp(from).addCount(indexTo, val);
/*     */     }
/*     */     else {
/* 103 */       this.transWithinGroups[groupTo].addCount(indexFrom, indexTo, val);
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
/*     */   public AbstractTransitionProbs makeTransBetweenGroups(Object clazz, Sampler samplerFirst, Sampler exp_p, int no_states)
/*     */     throws Exception
/*     */   {
/* 120 */     return ExponentialTransitionProbs.get(clazz, samplerFirst, exp_p, 
/* 121 */       samplerFirst.dist.length);
/*     */   }
/*     */   
/* 124 */   public AbstractTransitionProbs makeTransBetweenGroups(AbstractTransitionProbs transBetweenGroups, boolean swtch, int[] statesToGroup) { return transBetweenGroups.clone(swtch); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BetweenWithinTransitionProbs1(Sampler samplerFirst, Sampler[] samplers, int[] stateToGroup, int[] stateToIndexWithinGroup, Dirichlet[] exp_p, Object[] clazz)
/*     */     throws Exception
/*     */   {
/* 138 */     this.transBetweenGroups = makeTransBetweenGroups(clazz[0], samplerFirst, exp_p[0], stateToGroup.length);
/*     */     
/*     */ 
/* 141 */     this.stateToGroup = stateToGroup;
/* 142 */     this.stateToIndexWithinGroup = stateToIndexWithinGroup;
/* 143 */     this.alphaWithinGroup = new ExpTransProb[samplers.length];
/* 144 */     this.transWithinGroups = new AbstractTransitionProbs[samplers.length];
/* 145 */     for (int i = 0; i < samplers.length; i++) {
/* 146 */       this.alphaWithinGroup[i] = ((ExpTransProb)((Class[])clazz[2])[1].getConstructor(new Class[] { Sampler.class, Integer.TYPE }).newInstance(new Object[] { samplers[i], Integer.valueOf(stateToGroup.length) }));
/* 147 */       this.transWithinGroups[i] = ExponentialTransitionProbs.get(clazz[1], samplers[i], exp_p[1], samplers[i].dist.length);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BetweenWithinTransitionProbs1(BetweenWithinTransitionProbs1 probs, boolean swtch)
/*     */   {
/* 156 */     this.transBetweenGroups = makeTransBetweenGroups(probs.transBetweenGroups, swtch, probs.stateToGroup);
/*     */     
/*     */ 
/* 159 */     this.alphaWithinGroup = new ExpTransProb[probs.alphaWithinGroup.length];
/* 160 */     this.transWithinGroups = new AbstractTransitionProbs[probs.transWithinGroups.length];
/* 161 */     this.stateToIndexWithinGroup = probs.stateToIndexWithinGroup;
/* 162 */     this.stateToGroup = probs.stateToGroup;
/* 163 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 164 */       this.alphaWithinGroup[i] = probs.alphaWithinGroup[i].clone(swtch, this.stateToGroup.length);
/* 165 */       this.transWithinGroups[i] = probs.transWithinGroups[i].clone(swtch);
/*     */     }
/* 167 */     validate();
/*     */   }
/*     */   
/*     */ 
/*     */   public final Collection getDistributions()
/*     */   {
/* 173 */     List l = new java.util.ArrayList();
/* 174 */     l.addAll(this.transBetweenGroups.getDistributions());
/*     */     
/* 176 */     l.addAll((Collection)Arrays.asList(this.alphaWithinGroup));
/* 177 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 178 */       l.addAll(this.transWithinGroups[i].getDistributions());
/*     */     }
/* 180 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 189 */     this.transBetweenGroups.initialiseCounts(start, end);
/*     */     
/*     */ 
/* 192 */     for (int i = 0; i < this.alphaWithinGroup.length; i++)
/*     */     {
/* 194 */       this.alphaWithinGroup[i].initialiseExpRd();
/*     */     }
/* 196 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 197 */       this.transWithinGroups[i].initialiseCounts(start, end);
/*     */     }
/*     */   }
/*     */   
/*     */   public void transfer(double[] pseudoTrans, double[] pseudoExp)
/*     */   {
/* 203 */     this.transBetweenGroups.transfer(pseudoTrans[0], pseudoExp[0]);
/* 204 */     for (int i = 0; i < this.alphaWithinGroup.length; i++)
/*     */     {
/* 206 */       this.alphaWithinGroup[i].transferExp(pseudoTrans[1]);
/*     */     }
/* 208 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 209 */       this.transWithinGroups[i].transfer(pseudoTrans[1], pseudoExp[1]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public final void transfer(double pseudoTrans, double pseudoExp)
/*     */   {
/* 216 */     this.transBetweenGroups.transfer(pseudoTrans, pseudoExp);
/* 217 */     for (int i = 0; i < this.alphaWithinGroup.length; i++)
/*     */     {
/* 219 */       this.alphaWithinGroup[i].transferExp(pseudoTrans);
/*     */     }
/* 221 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 222 */       this.transWithinGroups[i].transfer(pseudoTrans, pseudoExp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double transitionDistance(AbstractTransitionProbs probs)
/*     */   {
/* 229 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public final void validate()
/*     */   {
/* 235 */     this.transBetweenGroups.validate();
/*     */     
/*     */ 
/* 238 */     for (int i = 0; i < this.alphaWithinGroup.length; i++)
/*     */     {
/* 240 */       this.alphaWithinGroup[i].validateExp();
/*     */     }
/* 242 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 243 */       this.transWithinGroups[i].validate();
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
/*     */ 
/*     */ 
/*     */   void printExp(PrintWriter pw, double dist)
/*     */   {
/* 264 */     this.transBetweenGroups.print(pw, null, dist);
/*     */     
/* 266 */     pw.print("; ");
/*     */   }
/*     */   
/* 269 */   public final void print(PrintWriter pw, Double[] hittingProb, double dist) { if (Constants.annotate())
/*     */     {
/*     */ 
/* 272 */       pw.println("state to group " + print(this.stateToGroup));
/* 273 */       pw.println("state to index in group " + print(this.stateToIndexWithinGroup));
/*     */     }
/* 275 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 276 */       if (this.transWithinGroups[i].noStates() > 1) {
/* 277 */         pw.println("trans Within Groups " + i);
/* 278 */         this.transWithinGroups[i].print(pw, null, dist);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 283 */     pw.print("trans between groups: ");
/* 284 */     this.transBetweenGroups.print(pw, hittingProb, dist);
/*     */     
/* 286 */     if (Constants.annotate()) pw.print("alpha within");
/* 287 */     for (int i = 0; i < this.alphaWithinGroup.length; i++) {
/* 288 */       if (this.alphaWithinGroup[i].noStates() > 1) {
/* 289 */         pw.println("alpha within groups " + i);
/* 290 */         this.alphaWithinGroup[i].printExp(pw, 0.0D, "; ");
/*     */       }
/*     */     }
/* 293 */     pw.println();
/*     */   }
/*     */   
/*     */   private String print(int[] s) {
/* 297 */     Integer[] res = new Integer[s.length];
/* 298 */     for (int i = 0; i < res.length; i++) {
/* 299 */       res[i] = Integer.valueOf(s[i]);
/*     */     }
/* 301 */     return Arrays.asList(res).toString();
/*     */   }
/*     */   
/*     */   public AbstractTransitionProbs clone(int[] statesToGroup) {
/* 305 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/BetweenWithinTransitionProbs1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */