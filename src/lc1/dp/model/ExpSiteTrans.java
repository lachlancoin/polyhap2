/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.dp.transition.AbstractTransitionProbs;
/*     */ import lc1.dp.transition.ExponentialTransitionProbs;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.util.Constants;
/*     */ import pal.math.UnivariateMinimum;
/*     */ 
/*     */ public class ExpSiteTrans extends SiteTransitions
/*     */ {
/*  15 */   double r_prev_prev = 0.0D;
/*     */   final List<State> states;
/*     */   
/*     */   public ExpSiteTrans(List<Integer> loc, List<State> states, Double[] exp_p1, Double[] r, int length)
/*     */   {
/*  20 */     super(loc, states.size() - 1, exp_p1, r, length);
/*  21 */     this.states = states;
/*     */   }
/*     */   
/*     */   public ExpSiteTrans(ExpSiteTrans trans_init, boolean swtch)
/*     */   {
/*  26 */     super(trans_init, swtch);
/*  27 */     this.states = trans_init.states;
/*     */   }
/*     */   
/*     */   public SiteTransitions clone(boolean swtch) {
/*  31 */     return new ExpSiteTrans(this, swtch);
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialise(double[] rel, double permute)
/*     */     throws Exception
/*     */   {
/*  38 */     Dirichlet dir1 = new Dirichlet(rel, Constants.u_global(0)[1]);
/*     */     
/*  40 */     int len = this.states.size();
/*  41 */     for (int i = 1; i < this.transProbs.length; i++)
/*     */     {
/*  43 */       this.transProbs[i] = new ExponentialTransitionProbs(dir1, getExp_p(i), len);
/*     */     }
/*     */   }
/*     */   
/*     */   public void transferTransitions(double pseudoTrans, final double pseudoCExp)
/*     */   {
/*  49 */     this.r_prev_prev = this.r_prev;
/*  50 */     this.r_prev = this.r[0].doubleValue();
/*  51 */     ((lc1.dp.transition.FreeTransitionProbs1)this.transProbs[0]).transfer(pseudoTrans, pseudoCExp);
/*     */     
/*  53 */     final double[] pseudoC = new double[2];
/*  54 */     pal.math.UnivariateFunction uvf = new pal.math.UnivariateFunction() {
/*     */       public double evaluate(double arg0) {
/*  56 */         double logProb = 0.0D;
/*  57 */         for (int i = 1; i < ExpSiteTrans.this.transProbs.length; i++) {
/*  58 */           double d = -1 * (((Integer)ExpSiteTrans.this.loc.get(i)).intValue() - ((Integer)ExpSiteTrans.this.loc.get(i - 1)).intValue());
/*  59 */           pseudoC[0] = (pseudoCExp * Math.exp(d * arg0));
/*  60 */           pseudoC[1] = (pseudoCExp - pseudoC[0]);
/*  61 */           logProb += ((ExponentialTransitionProbs)ExpSiteTrans.this.transProbs[i]).evaluateExpRd(pseudoC);
/*     */         }
/*  63 */         return -1.0D * logProb;
/*     */       }
/*     */       
/*     */       public double getLowerBound() {
/*  67 */         return 0.0D;
/*     */       }
/*     */       
/*     */       public double getUpperBound() {
/*  71 */         return 1.0D;
/*     */       }
/*  73 */     };
/*  74 */     UnivariateMinimum uvm = new UnivariateMinimum();
/*  75 */     this.r[0] = Double.valueOf(uvm.findMinimum(this.r[0].doubleValue(), uvf));
/*  76 */     for (int i = 1; i < this.transProbs.length; i++) {
/*  77 */       double d = -1 * (((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue());
/*  78 */       pseudoC[0] = (pseudoCExp * Math.exp(d * this.r[0].doubleValue()));
/*  79 */       pseudoC[1] = (pseudoCExp - pseudoC[0]);
/*  80 */       ((ExponentialTransitionProbs)this.transProbs[i]).transfer(pseudoTrans, pseudoC);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean converged()
/*     */   {
/*  87 */     System.err.println("r before is " + this.r_prev / Constants.probCrossOverBetweenBP);
/*  88 */     System.err.println("r is " + this.r[0].doubleValue() / Constants.probCrossOverBetweenBP);
/*  89 */     double diff = Math.abs(this.r_prev - this.r[0].doubleValue()) / Constants.probCrossOverBetweenBP;
/*  90 */     System.err.println("diff is " + diff);
/*     */     
/*  92 */     return (diff < 0.3D) && (((this.r_prev < this.r[0].doubleValue()) && (this.r[0].doubleValue() < this.r_prev_prev)) || ((this.r_prev_prev < this.r[0].doubleValue()) && (this.r[0].doubleValue() < this.r_prev)));
/*     */   }
/*     */   
/*     */   public void print(PrintWriter sb, String sbS, List<State> states, double[][] hittingProb)
/*     */   {
/*  97 */     sb.print("0  ");
/*  98 */     this.transProbs[0].print(sb, null, 1.0D);
/*  99 */     Double[] top = new Double[hittingProb[0].length];
/* 100 */     for (int i = 1; i < this.transProbs.length; i++) {
/* 101 */       sb.print(i + "  ");
/* 102 */       sb.println(this.transProbs[i].getClass());
/*     */       
/* 104 */       for (int k = 0; k < top.length; k++) {
/* 105 */         top[k] = Double.valueOf(hittingProb[(i - 1)][k]);
/*     */       }
/* 107 */       this.transProbs[i].print(sb, top, ((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/ExpSiteTrans.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */