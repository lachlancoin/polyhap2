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
/*     */ 
/*     */ public class ExponentialFreeTransitionProbs
/*     */   implements AbstractTransitionProbs
/*     */ {
/*     */   final SimpleExtendedDistribution exp_rd;
/*     */   final SimpleExtendedDistribution alpha;
/*     */   final SimpleExtendedDistribution[] alphaWithinGroup;
/*     */   final FreeTransitionProbs1[] transWithinGroups;
/*     */   final int[] stateToGroup;
/*     */   final int[] stateToIndexWithinGroup;
/*     */   
/*     */   public double getTransition(int from, int to)
/*     */   {
/*  28 */     int groupFrom = this.stateToGroup[from];
/*  29 */     int indexFrom = this.stateToIndexWithinGroup[from];
/*  30 */     int groupTo = this.stateToGroup[to];
/*  31 */     int indexTo = this.stateToIndexWithinGroup[to];
/*  32 */     double exp = this.exp_rd.probs[0];
/*  33 */     double toProb = this.alpha.probs[groupTo];
/*  34 */     if (groupTo == groupFrom) {
/*  35 */       return exp * this.transWithinGroups[groupFrom].getTransition(indexFrom, indexTo) + 
/*  36 */         (1.0D - exp) * toProb * this.alphaWithinGroup[groupTo].probs[indexTo];
/*     */     }
/*     */     
/*  39 */     return (1.0D - exp) * toProb * this.alphaWithinGroup[groupTo].probs[indexTo];
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
/*  60 */     int no_states = observed.length;
/*  61 */     SimpleExtendedDistribution jump = this.alpha;
/*  62 */     SimpleExtendedDistribution exp_count = this.exp_rd;
/*  63 */     for (int from = 0; from < observed.length; from++) {
/*  64 */       int groupFrom = this.stateToGroup[from];
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
/*  79 */               double exp = this.exp_rd.probs[0];
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
/*     */   public ExponentialFreeTransitionProbs(Dirichlet samplerFirst, Dirichlet[] samplers, int[] stateToGroup, int[] stateToIndexWithinGroup, double exp_p)
/*     */     throws Exception
/*     */   {
/* 100 */     this.alpha = new SimpleExtendedDistribution(samplerFirst);
/* 101 */     this.exp_rd = new SimpleExtendedDistribution(new double[] { exp_p, 1.0D - exp_p }, Double.POSITIVE_INFINITY);
/* 102 */     this.stateToGroup = stateToGroup;
/* 103 */     this.stateToIndexWithinGroup = stateToIndexWithinGroup;
/* 104 */     this.alphaWithinGroup = new SimpleExtendedDistribution[samplers.length];
/* 105 */     this.transWithinGroups = new FreeTransitionProbs1[samplers.length];
/* 106 */     for (int i = 0; i < samplers.length; i++) {
/* 107 */       this.alphaWithinGroup[i] = new SimpleExtendedDistribution(samplers[i]);
/* 108 */       this.transWithinGroups[i] = new FreeTransitionProbs1(samplers[i]);
/*     */     }
/* 110 */     validate();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExponentialFreeTransitionProbs(ExponentialFreeTransitionProbs probs) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractTransitionProbs clone(AbstractTransitionProbs probs)
/*     */   {
/* 128 */     return new ExponentialFreeTransitionProbs((ExponentialFreeTransitionProbs)probs);
/*     */   }
/*     */   
/*     */   public Collection getDistributions()
/*     */   {
/* 133 */     List l = new ArrayList();
/* 134 */     l.add(this.alpha);l.add(this.exp_rd);
/* 135 */     l.addAll((Collection)Arrays.asList(this.alphaWithinGroup));
/* 136 */     for (int i = 0; i < this.transWithinGroups.length; i++) {
/* 137 */       l.addAll(this.transWithinGroups[i].getDistributions());
/*     */     }
/* 139 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 149 */     this.exp_rd.initialise();
/* 150 */     this.alpha.initialise();
/* 151 */     for (int i = 0; i < this.alphaWithinGroup.length; i++) {
/* 152 */       this.alphaWithinGroup[i].initialise();
/* 153 */       this.transWithinGroups[i].initialiseCounts(start, end);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoTrans, double pseudoExp)
/*     */   {
/* 162 */     this.exp_rd.transfer(pseudoExp);
/* 163 */     this.alpha.transfer(pseudoExp);
/* 164 */     for (int i = 0; i < this.alphaWithinGroup.length; i++) {
/* 165 */       this.alphaWithinGroup[i].transfer(pseudoTrans);
/* 166 */       this.transWithinGroups[i].transfer(pseudoTrans, pseudoExp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double transitionDistance(AbstractTransitionProbs probs)
/*     */   {
/* 173 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */   {
/* 181 */     throw new Error("Unresolved compilation problems: \n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, Double[] hittingProb)
/*     */   {
/* 192 */     throw new Error("Unresolved compilation problems: \n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/ExponentialFreeTransitionProbs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */