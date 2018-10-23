/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.dp.transition.AbstractTransitionProbs;
/*     */ import lc1.dp.transition.FreeTransitionProbs1;
/*     */ import lc1.stats.Dirichlet;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public abstract class SiteTransitions
/*     */   implements Serializable
/*     */ {
/*     */   final int len;
/*     */   public AbstractTransitionProbs[] transProbs;
/*     */   final List<Integer> loc;
/*     */   final int numFounders;
/*     */   final Double[] exp_p1;
/*     */   Double[] r;
/*     */   
/*     */   public String info()
/*     */   {
/*  27 */     return this.transProbs[1].info();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */   double r_prev = 0.0D;
/*     */   
/*     */   public Dirichlet getExp_p(int i) {
/*  38 */     if ((this.loc == null) && (this.exp_p1 == null)) { return null;
/*     */     }
/*  40 */     double exp_p = 
/*     */     
/*  42 */       (this.r == null ? null : (this.loc == null) || (this.loc.size() == 0) ? this.exp_p1[0] : this.exp_p1 == null ? null : Double.valueOf(Math.exp(-1.0D * this.r[0].doubleValue() * (((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue())))).doubleValue();
/*     */     
/*     */ 
/*  45 */     return new Dirichlet(new Double[] { Double.valueOf(exp_p), Double.valueOf(1.0D - exp_p) }, Constants.u_global[2]);
/*     */   }
/*     */   
/*  48 */   public SiteTransitions(int len, int numStates) throws Exception { this.len = len;
/*  49 */     this.transProbs = new AbstractTransitionProbs[len];
/*  50 */     int numF = numStates - 1;
/*  51 */     double[] u = new double[numF + 1];
/*  52 */     Arrays.fill(u, 1.0D / numF);
/*  53 */     u[0] = 0.0D;
/*  54 */     Dirichlet dir = new Dirichlet(u, Double.POSITIVE_INFINITY);
/*  55 */     this.transProbs[0] = new FreeTransitionProbs1(true, dir, numF + 1);
/*  56 */     for (int i = 1; i < len; i++) {
/*  57 */       this.transProbs[i] = new FreeTransitionProbs1(false, dir, numF + 1);
/*  58 */       ((FreeTransitionProbs1)this.transProbs[i]).transitionsOut[0] = null;
/*     */     }
/*  60 */     this.loc = null;
/*  61 */     this.numFounders = numF;
/*     */     
/*  63 */     this.exp_p1 = null;
/*  64 */     this.r = null;
/*     */   }
/*     */   
/*     */   public SiteTransitions(List<Integer> loc, int numF, Double[] exp_p1, Double[] r, int length) {
/*  68 */     if ((r != null) && (exp_p1 != null)) throw new RuntimeException("should not both be non null");
/*  69 */     this.transProbs = new AbstractTransitionProbs[length];
/*  70 */     this.loc = loc;
/*  71 */     this.len = (numF + 1);
/*     */     
/*  73 */     this.numFounders = numF;
/*  74 */     this.exp_p1 = exp_p1;
/*  75 */     this.r = r;
/*     */   }
/*     */   
/*     */   public boolean converged() {
/*  79 */     return true;
/*     */   }
/*     */   
/*     */   public abstract SiteTransitions clone(boolean paramBoolean);
/*     */   
/*     */   public SiteTransitions(SiteTransitions trans_init, boolean swtch) {
/*  85 */     this(trans_init.loc, trans_init.numFounders, trans_init.exp_p1, trans_init.r, trans_init.transProbs.length);
/*  86 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  87 */       this.transProbs[i] = trans_init.transProbs[i].clone(swtch);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setTransitionScore(int from, int to, int indexOfToEmission, double d, double d_ps) {
/*  92 */     FreeTransitionProbs1 transProbs = (FreeTransitionProbs1)this.transProbs[indexOfToEmission];
/*  93 */     transProbs.setTransitionScore(from, to, d, transProbs.length());
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts()
/*     */   {
/*  98 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  99 */       this.transProbs[i].initialiseCounts(i == 0, i == this.transProbs.length - 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<PseudoDistribution> getDistributions(int i)
/*     */   {
/* 107 */     return this.transProbs[i].getDistributions();
/*     */   }
/*     */   
/* 110 */   static double[] init_trans = { 100.0D, 100.0D };
/*     */   
/*     */ 
/*     */ 
/*     */   public void transferTransitions(double[] pseudoTrans, double[] pseudoCExp, int index)
/*     */   {
/* 116 */     ((FreeTransitionProbs1)this.transProbs[0]).transfer(pseudoTrans, pseudoCExp);
/*     */     
/* 118 */     for (int i = 1; i < this.transProbs.length; i++)
/*     */     {
/*     */ 
/* 121 */       this.transProbs[i].transfer(pseudoTrans, pseudoCExp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transferTransitions(double pseudoTrans, double pseudoCExp)
/*     */   {
/* 132 */     throw new RuntimeException("!!");
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
/*     */   public Double[] getTrans(int from, int to)
/*     */   {
/* 151 */     Double[] d = new Double[this.transProbs.length];
/* 152 */     d[0] = Double.valueOf(this.transProbs[0].getTransition(0, to));
/* 153 */     for (int i = 1; i < d.length - 1; i++) {
/* 154 */       d[i] = Double.valueOf(getTransitionScore(from, to, i));
/*     */     }
/*     */     
/* 157 */     d[(d.length - 1)] = Double.valueOf(this.transProbs[(this.transProbs.length - 1)].getTransition(from, 0));
/* 158 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */   public void validateTransAt(int indexOfToEmission)
/*     */   {
/* 164 */     if (indexOfToEmission >= this.transProbs.length) return;
/* 165 */     this.transProbs[indexOfToEmission].validate();
/*     */   }
/*     */   
/*     */   public double getTransitionScore(int from, int to, int indexOfToEmission)
/*     */   {
/* 170 */     if (indexOfToEmission >= this.transProbs.length) return 0.0D;
/* 171 */     if ((from == 0) && 
/* 172 */       (indexOfToEmission != 0)) { return 0.0D;
/*     */     }
/* 174 */     if (to == 0) {
/* 175 */       if (indexOfToEmission == this.transProbs.length - 1) return 1.0D;
/* 176 */       return 0.0D;
/*     */     }
/* 178 */     if ((indexOfToEmission == 0) && (from != 0)) { return 0.0D;
/*     */     }
/* 180 */     AbstractTransitionProbs tp = this.transProbs[indexOfToEmission];
/* 181 */     double d = tp == null ? 0.0D : tp.getTransition(from, to);
/* 182 */     return d;
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate()
/*     */   {
/* 188 */     for (int i = 0; i < this.transProbs.length; i++) {
/* 189 */       this.transProbs[i].validate();
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract void initialise(double[] paramArrayOfDouble, double paramDouble) throws Exception;
/*     */   
/*     */   public void initialise(List<State> states, Double[] exp_p) {
/* 196 */     initialise(states, null, null, exp_p, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialise(List<State> states, List<Integer> loc, Double[] r, Double[] exp_p1, double[] rel)
/*     */   {
/*     */     try
/*     */     {
/* 206 */       int numFounders = states.size() - 1;
/*     */       
/*     */ 
/* 209 */       double[] d2 = new double[numFounders + 1];
/* 210 */       if (rel != null) {
/* 211 */         for (int i = 0; i < rel.length; i++) {
/* 212 */           d2[(i + 1)] = rel[i];
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 218 */         double inv_col = 1.0D / numFounders;
/* 219 */         Arrays.fill(d2, inv_col);
/*     */       }
/* 221 */       d2[0] = 0.0D;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 227 */       this.transProbs[0] = new FreeTransitionProbs1(true, new Dirichlet(d2, 1.0E10D), states.size());
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 233 */       initialise(d2, Constants.samplePermute());
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/* 237 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void validate(AbstractTransitionProbs probs, int nostates, int index)
/*     */   {
/* 243 */     for (int i = 1; i < nostates; i++) {
/* 244 */       double sum = 0.0D;
/* 245 */       double[] dist = new double[nostates];
/* 246 */       Arrays.fill(dist, 0.0D);
/* 247 */       for (int k = 1; k < nostates; k++) {
/* 248 */         dist[k] = probs.getTransition(i, k);
/* 249 */         sum += dist[k];
/*     */       }
/*     */       
/* 252 */       if (Math.abs(sum - 1.0D) > 0.001D) {
/* 253 */         probs.validate();
/* 254 */         throw new RuntimeException("!! " + sum + " " + index + " " + i + " " + probs.getTransition(i, i));
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
/*     */   public void print(PrintWriter sb, String sbS, List<State> states, double[][] hittingProb)
/*     */   {
/* 272 */     Double[] top = new Double[hittingProb[0].length];
/* 273 */     for (int i = 0; i < this.transProbs.length; i++) {
/* 274 */       sb.print(i + "  ");
/* 275 */       if (this.transProbs[i] == null) { sb.print("null");
/*     */       } else {
/* 277 */         sb.println(this.transProbs[i].getClass());
/* 278 */         if (i != 0) {
/* 279 */           for (int k = 0; k < top.length; k++) {
/* 280 */             top[k] = Double.valueOf(hittingProb[(i - 1)][k]);
/*     */           }
/*     */         }
/* 283 */         this.transProbs[i].print(sb, i == 0 ? null : top, (i == 0) || (this.loc == null) ? 1 : ((Integer)this.loc.get(i)).intValue() - ((Integer)this.loc.get(i - 1)).intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/SiteTransitions.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */