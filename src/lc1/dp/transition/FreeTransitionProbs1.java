/*     */ package lc1.dp.transition;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.Sampler;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution1;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public class FreeTransitionProbs1
/*     */   extends AbstractTransitionProbs
/*     */   implements Serializable
/*     */ {
/*     */   public PseudoDistribution[] transitionsOut;
/*     */   
/*     */   public int noStates()
/*     */   {
/*  23 */     return this.transitionsOut.length;
/*     */   }
/*     */   
/*     */   public AbstractTransitionProbs clone(int[] statesToGroup) {
/*  27 */     return new FreeTransitionProbs1(this, statesToGroup);
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
/*     */   public FreeTransitionProbs1(AbstractTransitionProbs probs)
/*     */   {
/*  43 */     int no_states = probs.noStates();
/*  44 */     this.transitionsOut = new PseudoDistribution[no_states];
/*  45 */     for (int j = 0; j < no_states; j++) {
/*  46 */       double[] probs1 = new double[no_states];
/*  47 */       for (int j1 = 0; j1 < no_states; j1++) {
/*  48 */         probs1[j1] = probs.getTransition(j, j1);
/*     */       }
/*  50 */       this.transitionsOut[j] = new SimpleExtendedDistribution1(probs1, Constants.switchU());
/*     */     }
/*     */   }
/*     */   
/*     */   public FreeTransitionProbs1(FreeTransitionProbs1 probs) {
/*  55 */     int no_states = probs.noStates();
/*  56 */     this.transitionsOut = new PseudoDistribution[no_states];
/*  57 */     for (int j = 0; j < no_states; j++) {
/*  58 */       if (probs.transitionsOut[j] != null)
/*     */       {
/*  60 */         this.transitionsOut[j] = probs.transitionsOut[j].clone(Constants.switchU());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public FreeTransitionProbs1(FreeTransitionProbs1 probs1, int[] statesToGroup) {
/*  66 */     int no_states = statesToGroup.length;
/*  67 */     this.transitionsOut = new PseudoDistribution[no_states];
/*  68 */     for (int j = 0; j < no_states; j++) {
/*  69 */       if (probs1.transitionsOut[j] != null)
/*  70 */         this.transitionsOut[j] = probs1.transitionsOut[j].clone();
/*     */     }
/*     */   }
/*     */   
/*     */   public FreeTransitionProbs1 clone(boolean swtch) {
/*  75 */     return new FreeTransitionProbs1(this);
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
/*     */   public FreeTransitionProbs1(Sampler samplerFirst)
/*     */   {
/*  94 */     int no_states = samplerFirst.dist.length;
/*  95 */     this.transitionsOut = new PseudoDistribution[no_states];
/*  96 */     for (int j = 0; j < no_states; j++)
/*  97 */       this.transitionsOut[j] = new SimpleExtendedDistribution1(samplerFirst);
/*     */   }
/*     */   
/*     */   public FreeTransitionProbs1(Sampler samplerFirst, int length) {
/* 101 */     int no_states = length;
/* 102 */     this.transitionsOut = new PseudoDistribution[no_states];
/* 103 */     for (int j = 0; j < no_states; j++) {
/* 104 */       this.transitionsOut[j] = 
/* 105 */         (samplerFirst == null ? 
/* 106 */         new SimpleExtendedDistribution(length) : 
/* 107 */         new SimpleExtendedDistribution1(samplerFirst));
/*     */     }
/*     */   }
/*     */   
/*     */   public FreeTransitionProbs1(boolean first, Dirichlet samplerFirst, int no_states) throws Exception
/*     */   {
/* 113 */     this.transitionsOut = new PseudoDistribution[no_states];
/*     */     
/* 115 */     int numF = no_states - 1;
/* 116 */     Double conc = Constants.initialConcentration();
/* 117 */     if (samplerFirst != null)
/*     */     {
/* 119 */       if (!first)
/*     */       {
/* 121 */         for (int j = 1; j < no_states; j++) {
/* 122 */           double[] d1 = new double[samplerFirst.dist.length];
/* 123 */           Arrays.fill(d1, (1.0D - conc.doubleValue()) / numF);
/* 124 */           d1[j] += conc.doubleValue();
/* 125 */           d1[0] = 0.0D;
/*     */           
/* 127 */           this.transitionsOut[j] = 
/* 128 */             new SimpleExtendedDistribution1(new Dirichlet(d1, samplerFirst.u()));
/*     */         }
/*     */         
/*     */       } else {
/* 132 */         this.transitionsOut[0] = new SimpleExtendedDistribution1(samplerFirst);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCount(int from, int to, double val)
/*     */   {
/* 139 */     this.transitionsOut[from].addCount(to, val);
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
/*     */   public double getTransition(int from, int to)
/*     */   {
/* 165 */     PseudoDistribution dist = this.transitionsOut[from];
/* 166 */     if (dist != null)
/*     */     {
/*     */ 
/*     */ 
/* 170 */       return dist.probs(to);
/*     */     }
/* 172 */     return 0.0D;
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
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 192 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 193 */       if (this.transitionsOut[j] != null) { this.transitionsOut[j].initialise();
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
/* 208 */     return this.transitionsOut.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double[] pseudoC, double[] pseudo_exp)
/*     */   {
/* 216 */     transfer(pseudoC[0], pseudo_exp[0]);
/*     */   }
/*     */   
/*     */   public void transfer(double pseudoC, double pseudo_exp) {
/* 220 */     for (int j = 0; j < this.transitionsOut.length; j++)
/*     */     {
/* 222 */       if ((this.transitionsOut[j] != null) && (this.transitionsOut[j].fixedInteger() == null))
/*     */       {
/* 224 */         this.transitionsOut[j].transfer(pseudoC);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection getDistributions()
/*     */   {
/* 236 */     return (Collection)Arrays.asList(this.transitionsOut);
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
/*     */   public void validate()
/*     */   {
/* 250 */     for (int j = 0; j < this.transitionsOut.length; j++) {
/* 251 */       PseudoDistribution dist = this.transitionsOut[j];
/* 252 */       if (dist != null) {
/* 253 */         dist.validate();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/* 262 */     return this.transitionsOut.length;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setTransitionScore(int from, int to, double d, int length)
/*     */   {
/* 268 */     PseudoDistribution dist = this.transitionsOut[from];
/* 269 */     if (dist == null) {
/* 270 */       dist = new SimpleExtendedDistribution(length);
/*     */       
/*     */ 
/* 273 */       this.transitionsOut[from] = dist;
/*     */     }
/* 275 */     dist.setProbs(to, d);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void print(PrintWriter pw, Double[] hittingProb, double dist)
/*     */   {
/* 282 */     String st = this.transitionsOut[0] == null ? this.transitionsOut[1].getPrintString() : 
/* 283 */       this.transitionsOut[0].getPrintString();
/* 284 */     pw.println("out");
/*     */     
/*     */ 
/* 287 */     for (int i = 0; i < this.transitionsOut.length; i++)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 292 */       if (this.transitionsOut[i] == null) {
/* 293 */         pw.print("{" + i + "};");
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 298 */         this.transitionsOut[i].printSimple(pw, i + " ", ";", 0.0D);
/*     */       }
/*     */     }
/*     */     
/* 302 */     pw.println("\nin");
/* 303 */     SimpleExtendedDistribution distr = new SimpleExtendedDistribution(this.transitionsOut.length);
/* 304 */     for (int i = 0; i < this.transitionsOut.length; i++) {
/* 305 */       distr.initialise();
/* 306 */       for (int j = 0; j < this.transitionsOut.length; j++)
/*     */       {
/*     */ 
/*     */ 
/* 310 */         if (this.transitionsOut[j] != null) {
/* 311 */           distr.counts[j] += this.transitionsOut[j].probs(i);
/*     */         }
/*     */       }
/* 314 */       if (Constants.sum(distr.counts) != 0.0D) {
/* 315 */         distr.transfer(0.0D);
/* 316 */         distr.printSimple(pw, i + " ", ";", 0.0D);
/*     */       }
/*     */     }
/*     */     
/* 320 */     pw.println();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/FreeTransitionProbs1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */