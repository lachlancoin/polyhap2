/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FreeTransitionProbs1
/*     */   implements Serializable, AbstractTransitionProbs
/*     */ {
/*     */   public SimpleExtendedDistribution[] transitionsOut;
/*     */   
/*     */   public FreeTransitionProbs1 clone(AbstractTransitionProbs trans_ps)
/*     */   {
/*  35 */     return new FreeTransitionProbs1(this, (FreeTransitionProbs1)trans_ps);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeTransitionProbs1(FreeTransitionProbs1 tp_init, FreeTransitionProbs1 tp_pseudo) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public FreeTransitionProbs1(Dirichlet samplerFirst, int no_states)
/*     */     throws Exception
/*     */   {
/*  49 */     this(false, samplerFirst, no_states);
/*     */   }
/*     */   
/*     */   public FreeTransitionProbs1(Dirichlet samplerFirst) {
/*  53 */     int no_states = samplerFirst.dist.length;
/*  54 */     this.transitionsOut = new SimpleExtendedDistribution[no_states];
/*  55 */     for (int j = 0; j < no_states; j++) {
/*  56 */       this.transitionsOut[j] = new SimpleExtendedDistribution(samplerFirst);
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
/*     */   public FreeTransitionProbs1(boolean first, Dirichlet samplerFirst, int no_states)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
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
/*  88 */     for (int j = 0; j < observed.length; j++) {
/*  89 */       if (observed[j] != null)
/*     */       {
/*  91 */         if (this.transitionsOut[j] != null) {
/*  92 */           this.transitionsOut[j].addCounts(observed[j]);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getTransition(int from, int to)
/*     */   {
/* 112 */     throw new Error("Unresolved compilation problem: \n\tThe method getProbs(int[], double[]) in the type SimpleExtendedDistribution is not applicable for the arguments (int)\n");
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
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 134 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 135 */       if (this.transitionsOut[j] != null) { this.transitionsOut[j].initialise();
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
/*     */   public String toString()
/*     */   {
/* 150 */     return this.transitionsOut.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoC, double pseudo_exp)
/*     */   {
/* 158 */     for (int j = 0; j < this.transitionsOut.length; j++)
/*     */     {
/* 160 */       if (this.transitionsOut[j] != null)
/*     */       {
/*     */ 
/*     */ 
/* 164 */         this.transitionsOut[j].transfer(pseudoC);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection getDistributions()
/*     */   {
/* 173 */     return (Collection)Arrays.asList(this.transitionsOut);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double transitionDistance(AbstractTransitionProbs m)
/*     */   {
/* 184 */     double sum = 0.0D;
/* 185 */     FreeTransitionProbs1 m1 = (FreeTransitionProbs1)m;
/* 186 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 187 */       SimpleExtendedDistribution d1 = this.transitionsOut[j];
/* 188 */       SimpleExtendedDistribution d2 = m1.transitionsOut[j];
/* 189 */       if ((d1 != null) && (d2 != null)) {
/* 190 */         sum += d1.KLDistance(d2);
/*     */       }
/*     */     }
/*     */     
/* 194 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void validate()
/*     */   {
/* 205 */     throw new Error("Unresolved compilation problem: \n\tThe method validate() is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/* 214 */     return this.transitionsOut.length;
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
/*     */   public void setTransitionScore(int from, int to, double d, int length)
/*     */   {
/* 227 */     throw new Error("Unresolved compilation problem: \n\tThe method setProbs(int, double) is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, Double[] hittingProb)
/*     */   {
/* 234 */     throw new Error("Unresolved compilation problems: \n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n\tThe method getPrintString() is undefined for the type SimpleExtendedDistribution\n\tThe method printSimple(PrintWriter, String, String, double) is undefined for the type SimpleExtendedDistribution\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/FreeTransitionProbs1.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */