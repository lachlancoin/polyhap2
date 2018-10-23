/*     */ package lc1.dp.transition;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.Sampler;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class ExponentialTransitionProbs extends AbstractTransitionProbs
/*     */ {
/*     */   public ExpTransProb exp;
/*     */   public ExpTransProb alpha;
/*     */   
/*     */   public String info()
/*     */   {
/*  21 */     StringBuffer info = new StringBuffer(super.info());
/*  22 */     info.append("exp " + this.exp.info() + "  alpha " + this.alpha.info());
/*  23 */     return info.toString();
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
/*     */   public void addCount(int from, int to, double val)
/*     */   {
/*  49 */     SimpleExtendedDistribution exp_rd = this.exp.getExp(from);
/*  50 */     PseudoDistribution alpha = this.alpha.getExp(from);
/*  51 */     if (to != from) {
/*  52 */       alpha.addCount(to, val);
/*  53 */       exp_rd.counts[1] += val;
/*     */     }
/*     */     else {
/*  56 */       double exp = exp_rd.probs[0];
/*  57 */       double non_jump_prob = exp;
/*  58 */       double jump_prob = (1.0D - exp) * alpha.probs(to);
/*  59 */       double alloc = val * (jump_prob / (jump_prob + non_jump_prob));
/*  60 */       alpha.addCount(to, alloc);
/*  61 */       exp_rd.counts[1] += alloc;
/*  62 */       exp_rd.counts[0] += val - alloc;
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCount(int index, int from, int to, double val) {
/*  67 */     SimpleExtendedDistribution exp_rd = this.exp.getExp(index);
/*  68 */     PseudoDistribution alpha = this.alpha.getExp(index);
/*  69 */     if (to != from) {
/*  70 */       alpha.addCount(to, val);
/*  71 */       exp_rd.counts[1] += val;
/*     */     }
/*     */     else {
/*  74 */       double exp = exp_rd.probs[0];
/*  75 */       double non_jump_prob = exp;
/*  76 */       double jump_prob = (1.0D - exp) * alpha.probs(to);
/*  77 */       double alloc = val * (jump_prob / (jump_prob + non_jump_prob));
/*  78 */       alpha.addCount(to, alloc);
/*  79 */       exp_rd.counts[1] += alloc;
/*  80 */       exp_rd.counts[0] += val - alloc;
/*     */     }
/*     */   }
/*     */   
/*     */   public ExponentialTransitionProbs(ExpTransProb exp, ExpTransProb alpha) {
/*  85 */     this.exp = exp;
/*  86 */     this.alpha = alpha;
/*     */   }
/*     */   
/*  89 */   public ExponentialTransitionProbs(Sampler samplerFirst, Sampler exp_p, int len) throws Exception { this.alpha = new SimpleExpTransProb(samplerFirst, len);
/*  90 */     this.exp = new SimpleExpTransProb(exp_p, len);
/*     */   }
/*     */   
/*     */   public ExponentialTransitionProbs(ExponentialTransitionProbs probs, boolean swtch) {
/*  94 */     if ((probs.exp instanceof SimpleExpTransProb)) {
/*  95 */       this.exp = probs.exp.clone(swtch, probs.alpha.noStates());
/*  96 */       this.alpha = probs.alpha.clone(false, probs.alpha.noStates());
/*     */     }
/*  98 */     else if ((probs.alpha instanceof SimpleExpTransProb)) {
/*  99 */       this.exp = probs.exp.clone(false, probs.alpha.noStates());
/* 100 */       this.alpha = probs.alpha.clone(swtch, probs.alpha.noStates());
/*     */     }
/*     */     else
/*     */     {
/* 104 */       this.exp = probs.exp.clone(false, this.alpha.noStates());
/* 105 */       this.alpha = probs.alpha.clone(false, this.alpha.noStates());
/*     */     }
/*     */   }
/*     */   
/* 109 */   public ExponentialTransitionProbs(ExponentialTransitionProbs probs, int[] statesToGroup) { this.alpha = probs.exp.clone(statesToGroup);
/* 110 */     this.exp = probs.exp.clone(statesToGroup);
/*     */   }
/*     */   
/*     */   public AbstractTransitionProbs clone(boolean swtch)
/*     */   {
/* 115 */     if ((Constants.useFree()) && (swtch) && 
/* 116 */       ((this.exp instanceof MultiExpProbs)) && ((this.alpha instanceof MultiExpProbs))) {
/* 117 */       return new FreeTransitionProbs1(this);
/*     */     }
/*     */     
/*     */ 
/* 121 */     return new ExponentialTransitionProbs(this, swtch);
/*     */   }
/*     */   
/*     */   public AbstractTransitionProbs clone(int[] statesToGroup)
/*     */   {
/* 126 */     return new ExponentialTransitionProbs(this, statesToGroup);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection getDistributions()
/*     */   {
/* 133 */     List l = new ArrayList();
/* 134 */     l.add(this.alpha.getExpRdColl());
/* 135 */     l.addAll(this.exp.getExpRdColl());
/* 136 */     return l;
/*     */   }
/*     */   
/*     */   public double getTransition(int from, int to)
/*     */   {
/* 141 */     SimpleExtendedDistribution exp_rd = this.exp.getExp(from);
/* 142 */     PseudoDistribution alpha = this.alpha.getExp(from);
/* 143 */     double exp = exp_rd.probs[0];
/* 144 */     double toProb = alpha.probs(to);
/* 145 */     if (to == from) {
/* 146 */       return exp + (1.0D - exp) * toProb;
/*     */     }
/*     */     
/* 149 */     return (1.0D - exp) * toProb;
/*     */   }
/*     */   
/*     */   public double getTransition(int index, int from, int to)
/*     */   {
/* 154 */     SimpleExtendedDistribution exp_rd = this.exp.getExp(index);
/* 155 */     PseudoDistribution alpha = this.alpha.getExp(index);
/* 156 */     double exp = exp_rd.probs[0];
/* 157 */     double toProb = alpha.probs(to);
/* 158 */     if (to == from) {
/* 159 */       return exp + (1.0D - exp) * toProb;
/*     */     }
/*     */     
/* 162 */     return (1.0D - exp) * toProb;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseCounts(boolean start, boolean end)
/*     */   {
/* 170 */     this.exp.initialiseExpRd();
/* 171 */     this.alpha.initialiseExpRd();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudoTrans, double[] pseudoExp)
/*     */   {
/* 182 */     this.exp.transferExp(pseudoExp);
/* 183 */     this.alpha.transferExp(pseudoTrans);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void transfer(double[] pseudoTrans, double[] pseudoExp)
/*     */   {
/* 190 */     this.exp.transferExp(pseudoExp[0]);
/* 191 */     this.alpha.transferExp(pseudoTrans[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double transitionDistance(AbstractTransitionProbs probs)
/*     */   {
/* 198 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw, Double[] hittingProb, double dist)
/*     */   {
/* 204 */     this.exp.printExp(pw, dist, "exp");
/*     */     
/* 206 */     pw.print("; ");
/* 207 */     this.alpha.printExp(pw, dist, "alpha");
/*     */     
/* 209 */     pw.println();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 216 */     StringWriter sw = new StringWriter();
/* 217 */     PrintWriter pw = new PrintWriter(sw);
/* 218 */     print(pw, null, 1.0D);
/* 219 */     return sw.getBuffer().toString();
/*     */   }
/*     */   
/*     */   public void validate() {
/* 223 */     this.alpha.validateExp();
/* 224 */     this.exp.validateExp();
/* 225 */     for (int i = 0; i < noStates(); i++) {
/* 226 */       double sum = 0.0D;
/* 227 */       for (int j = 0; j < noStates(); j++) {
/* 228 */         sum += getTransition(i, j);
/*     */       }
/* 230 */       if (Math.abs(sum - 1.0D) > 0.001D) { throw new RuntimeException("!!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void transfer(double pseudoTrans, double pseudoExp)
/*     */   {
/* 238 */     this.exp.transferExp(pseudoExp);
/* 239 */     this.alpha.transferExp(pseudoTrans);
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
/*     */   public int noStates()
/*     */   {
/* 253 */     return this.alpha.getExp(0).probs.length;
/*     */   }
/*     */   
/*     */   public double evaluateExpRd(double[] pseudoC) {
/* 257 */     return this.exp.evaluateExpRd(pseudoC);
/*     */   }
/*     */   
/*     */   public static AbstractTransitionProbs get(Object clazz, Sampler samplerFirst, Sampler expD, int noStates) throws Exception {
/* 261 */     if ((clazz instanceof Class)) {
/* 262 */       return 
/*     */       
/* 264 */         (AbstractTransitionProbs)((Class)clazz).getConstructor(new Class[] { Sampler.class, Integer.TYPE }).newInstance(
/* 265 */         new Object[] { samplerFirst, Integer.valueOf(noStates) });
/*     */     }
/*     */     
/* 268 */     Class[] cl = (Class[])clazz;
/* 269 */     if (cl[0] == FreeTransitionProbs1.class) {
/* 270 */       return new FreeTransitionProbs1(samplerFirst);
/*     */     }
/* 272 */     ExpTransProb exp = (ExpTransProb)cl[0].getConstructor(new Class[] { Sampler.class, Integer.TYPE }).newInstance(
/* 273 */       new Object[] { expD, Integer.valueOf(noStates) });
/* 274 */     ExpTransProb alpha = (ExpTransProb)cl[1].getConstructor(new Class[] { Sampler.class, Integer.TYPE }).newInstance(
/* 275 */       new Object[] { samplerFirst, Integer.valueOf(noStates) });
/* 276 */     return new ExponentialTransitionProbs(exp, alpha);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/ExponentialTransitionProbs.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */