/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
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
/*     */ public class ExponentialFreeTransitionProbsMulti
/*     */   implements AbstractTransitionProbs
/*     */ {
/*     */   final SimpleExtendedDistribution[] exp_rd;
/*     */   final SimpleExtendedDistribution alpha;
/*     */   final SimpleExtendedDistribution[] alphaWithinGroup;
/*     */   final FreeTransitionProbs1[] transWithinGroups;
/*     */   final int[] stateToGroup;
/*     */   final int[] stateToIndexWithinGroup;
/*     */   
/*     */   public double getTransition(int from, int to)
/*     */   {
/*  27 */     int groupFrom = this.stateToGroup[from];
/*  28 */     int indexFrom = this.stateToIndexWithinGroup[from];
/*  29 */     int groupTo = this.stateToGroup[to];
/*  30 */     int indexTo = this.stateToIndexWithinGroup[to];
/*  31 */     double exp = this.exp_rd[groupFrom].probs[0];
/*  32 */     double toProb = this.alpha.probs[groupTo];
/*  33 */     if (groupTo == groupFrom) {
/*  34 */       return exp * this.transWithinGroups[groupFrom].getTransition(indexFrom, indexTo) + 
/*  35 */         (1.0D - exp) * toProb * this.alphaWithinGroup[groupTo].probs[indexTo];
/*     */     }
/*     */     
/*  38 */     return (1.0D - exp) * toProb * this.alphaWithinGroup[groupTo].probs[indexTo];
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
/*     */   public void addCounts(StateDistribution[] observed)
/*     */   {
/*  59 */     int no_states = observed.length;
/*  60 */     SimpleExtendedDistribution jump = this.alpha;
/*     */     
/*  62 */     for (int from = 0; from < observed.length; from++) {
/*  63 */       int groupFrom = this.stateToGroup[from];
/*  64 */       SimpleExtendedDistribution exp_count = this.exp_rd[groupFrom];
/*  65 */       int indexFrom = this.stateToIndexWithinGroup[from];
/*  66 */       StateDistribution dist1 = observed[from];
/*  67 */       if (dist1 != null) {
/*  68 */         for (int to = 0; to < no_states; to++) {
/*  69 */           Double val = dist1.get(to);
/*  70 */           if (val.doubleValue() != 0.0D) {
/*  71 */             int groupTo = this.stateToGroup[to];
/*  72 */             int indexTo = this.stateToIndexWithinGroup[to];
/*  73 */             if (groupTo != groupFrom) {
/*  74 */               jump.counts[groupTo] += val.doubleValue();
/*  75 */               exp_count.counts[1] += val.doubleValue();
/*  76 */               this.alphaWithinGroup[groupTo].counts[indexTo] += val.doubleValue();
/*     */             }
/*     */             else {
/*  79 */               double exp = this.exp_rd[groupFrom].probs[0];
/*  80 */               double non_jump_prob = exp;
/*  81 */               double jump_prob = (1.0D - exp) * jump.probs[groupFrom];
/*  82 */               double alloc = val.doubleValue() * (jump_prob / (jump_prob + non_jump_prob));
/*  83 */               jump.counts[groupTo] += alloc;
/*  84 */               exp_count.counts[1] += alloc;
/*  85 */               exp_count.counts[0] += val.doubleValue() - alloc;
/*  86 */               this.transWithinGroups[groupFrom].transitionsOut[indexFrom].counts[indexTo] += val.doubleValue();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExponentialFreeTransitionProbsMulti(Dirichlet samplerFirst, Dirichlet[] samplers, int[] stateToGroup, int[] stateToIndexWithinGroup, double exp_p)
/*     */     throws Exception
/*     */   {
/* 100 */     this.alpha = new SimpleExtendedDistribution(samplerFirst);
/* 101 */     this.exp_rd = new SimpleExtendedDistribution[samplers.length];
/* 102 */     this.stateToGroup = stateToGroup;
/* 103 */     this.stateToIndexWithinGroup = stateToIndexWithinGroup;
/* 104 */     this.alphaWithinGroup = new SimpleExtendedDistribution[samplers.length];
/* 105 */     this.transWithinGroups = new FreeTransitionProbs1[samplers.length];
/* 106 */     for (int i = 0; i < samplers.length; i++) {
/* 107 */       this.exp_rd[i] = new SimpleExtendedDistribution(new double[] { exp_p, 1.0D - exp_p }, Double.POSITIVE_INFINITY);
/*     */       
/* 109 */       this.alphaWithinGroup[i] = new SimpleExtendedDistribution(samplers[i]);
/* 110 */       this.transWithinGroups[i] = new FreeTransitionProbs1(samplers[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExponentialFreeTransitionProbsMulti(ExponentialFreeTransitionProbsMulti probs) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractTransitionProbs clone(AbstractTransitionProbs probs)
/*     */   {
/* 129 */     return new ExponentialFreeTransitionProbsMulti((ExponentialFreeTransitionProbsMulti)probs);
/*     */   }
/*     */   
/*     */   public Collection getDistributions()
/*     */   {
/* 134 */     List l = new ArrayList();
/* 135 */     l.add(this.alpha);l.add(this.exp_rd);
/* 136 */     l.addAll((Collection)Arrays.asList(this.alphaWithinGroup));
/* 137 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 138 */       l.addAll(this.transWithinGroups[i].getDistributions());
/*     */     }
/* 140 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 151 */     this.alpha.initialise();
/* 152 */     for (int i = 0; i < this.alphaWithinGroup.length; i++) {
/* 153 */       this.exp_rd[i].initialise();
/* 154 */       this.alphaWithinGroup[i].initialise();
/* 155 */       this.transWithinGroups[i].initialiseCounts(start, end);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoTrans, double pseudoExp)
/*     */   {
/* 165 */     this.alpha.transfer(pseudoExp);
/* 166 */     for (int i = 0; i < this.alphaWithinGroup.length; i++) {
/* 167 */       this.exp_rd[i].transfer(pseudoExp);
/* 168 */       this.alphaWithinGroup[i].transfer(pseudoTrans);
/* 169 */       this.transWithinGroups[i].transfer(pseudoTrans, pseudoExp);
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
/*     */   public double transitionDistance(AbstractTransitionProbs probs)
/*     */   {
/* 184 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */   {
/* 192 */     throw new Error("Unresolved compilation problems: \n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n");
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
/*     */   public void print(PrintWriter pw, Double[] hittingProb)
/*     */   {
/* 207 */     throw new Error("Unresolved compilation problem: \n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/ExponentialFreeTransitionProbsMulti.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */