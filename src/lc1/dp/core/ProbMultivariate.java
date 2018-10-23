/*     */ package lc1.dp.core;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.TrainableNormal;
/*     */ import lc1.util.Constants;
/*     */ import pal.math.ConjugateDirectionSearch;
/*     */ import pal.math.MultivariateFunction;
/*     */ import pal.math.MultivariateMinimum;
/*     */ import pal.math.OrthogonalHints;
/*     */ 
/*     */ public class ProbMultivariate
/*     */   implements MultivariateFunction
/*     */ {
/*  18 */   static final double base = ;
/*  19 */   static final double logbase = Math.log(Constants.base());
/*     */   public final List<SkewNormal> pairs;
/*     */   
/*  22 */   public void minimise(double pseudoM, double pseudoSd, double pseudoSk) { for (int i = 0; i < this.single.size(); i++) {
/*  23 */       ((SkewNormal)this.single.get(i)).setPrior(pseudoM, pseudoSd, pseudoSk);
/*  24 */       ((SkewNormal)this.single.get(i)).updateParamIndex();
/*     */     }
/*  26 */     final MultivariateMinimum os = new ConjugateDirectionSearch();
/*  27 */     double[] init = new double[this.totParam];
/*  28 */     final double[] xvec = new double[this.totParam];
/*  29 */     for (int i = 0; i < xvec.length; i++) {
/*  30 */       xvec[i] = getParamValue(i);
/*     */     }
/*  32 */     System.arraycopy(xvec, 0, init, 0, xvec.length);
/*  33 */     Runnable run = new Runnable() {
/*     */       public void run() {
/*  35 */         os.optimize(ProbMultivariate.this, xvec, 0.01D, 0.01D);
/*     */       }
/*  37 */     };
/*  38 */     Thread th = new Thread(run);
/*  39 */     th.run();
/*     */     try {
/*  41 */       for (int i = 0; i < 100; i++) {
/*  42 */         Thread.sleep(100L);
/*  43 */         if (!th.isAlive()) break;
/*     */       }
/*  45 */       if (th.isAlive())
/*     */       {
/*  47 */         th.stop();
/*  48 */         System.arraycopy(init, 0, xvec, 0, xvec.length);
/*     */       }
/*     */     } catch (Exception exc) {
/*  51 */       exc.printStackTrace();
/*     */     }
/*  53 */     for (int i = 0; i < xvec.length; i++) {
/*  54 */       setParamValue(i, xvec[i]);
/*     */     }
/*  56 */     for (int i = 0; i < this.pairs.size(); i++) {
/*  57 */       SkewNormal pair = (SkewNormal)this.pairs.get(i);
/*  58 */       SkewNormal[] mem = (SkewNormal[])this.members.get(i);
/*  59 */       transferExp(pair, mem);
/*  60 */       pair.updateParamIndex();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final List<SkewNormal[]> members;
/*     */   
/*  68 */   public final List<SkewNormal> single = new ArrayList();
/*     */   
/*     */   final int[] numParam;
/*  71 */   int totParam = 0;
/*     */   
/*     */   final int len;
/*  74 */   public List<Double> noSamples = new ArrayList();
/*     */   
/*     */   public void updateSampleSize() {
/*  77 */     if (this.noSamples.size() > 0) {
/*  78 */       Collections.fill(this.noSamples, Double.valueOf(0.0D));
/*     */     }
/*  80 */     for (int i = 0; i < this.members.size(); i++) {
/*  81 */       SkewNormal[] dist = (SkewNormal[])this.members.get(i);
/*  82 */       for (int k = 0; k < dist.length; k++) {
/*  83 */         int index = this.single.indexOf(dist[k]);
/*  84 */         if (index < 0) {
/*  85 */           this.single.add(dist[k]);
/*  86 */           double sum = ((SkewNormal)this.pairs.get(i)).sumObs();
/*  87 */           this.noSamples.add(Double.valueOf(sum));
/*     */         }
/*     */         else {
/*  90 */           double sum = ((SkewNormal)this.pairs.get(i)).sumObs();
/*  91 */           this.noSamples.set(index, Double.valueOf(((Double)this.noSamples.get(index)).doubleValue() + sum));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ProbMultivariate(List<SkewNormal> s1, List<SkewNormal[]> s1_memb)
/*     */   {
/* 100 */     this.pairs = s1;
/*     */     
/* 102 */     this.members = s1_memb;
/* 103 */     updateSampleSize();
/* 104 */     this.numParam = new int[this.single.size()];
/*     */     
/* 106 */     for (int i = 0; i < this.numParam.length; i++) {
/* 107 */       MultivariateFunction mvf = (MultivariateFunction)this.single.get(i);
/* 108 */       this.numParam[i] = (mvf.getNumArguments() + this.totParam);
/* 109 */       this.totParam += mvf.getNumArguments();
/*     */     }
/* 111 */     this.len = this.numParam.length;
/*     */   }
/*     */   
/*     */   private int findIndex(int n) {
/* 115 */     for (int i = 0; 
/* 116 */         i < this.len - 1; i++)
/* 117 */       if (n < this.numParam[i])
/*     */         break;
/* 119 */     return i;
/*     */   }
/*     */   
/* 122 */   static double[] params = new double[3];
/*     */   
/*     */   public static SkewNormal getExp(SkewNormal[] mem)
/*     */   {
/* 126 */     double[] lower = { -5.0D, 0.001D, -1.0E10D };
/* 127 */     double[] upper = { 5.0D, 1000.0D, 1.0E10D };
/* 128 */     double priorMod = 0.0D;
/* 129 */     Arrays.fill(params, 0.0D);
/* 130 */     for (int k = 0; k < mem.length; k++) {
/* 131 */       SkewNormal m_k = mem[k];
/* 132 */       priorMod += m_k.priorModifier;
/* 133 */       for (int j = 0; j < params.length; j++) {
/* 134 */         if ((j == 0) && (Constants.trainEnsemble() == 2)) {
/* 135 */           params[j] += Math.pow(base, m_k.getParamValue(j));
/*     */         }
/*     */         else {
/* 138 */           params[j] += m_k.getParamValue(j);
/*     */         }
/*     */       }
/*     */     }
/* 142 */     double fir = params[0] / mem.length;
/* 143 */     return 
/*     */     
/* 145 */       params[2] == 0.0D ? new TrainableNormal(Constants.trainEnsemble() == 2 ? Math.log(fir) / logbase : fir, 
/*     */       
/* 147 */       params[1], Constants.round(), priorMod / mem.length) : 
/*     */       
/* 149 */       new SkewNormal(Constants.trainEnsemble() == 2 ? Math.log(fir) / logbase : fir, 
/*     */       
/* 151 */       params[1], params[2], lower, upper, Constants.round(), priorMod / mem.length);
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
/*     */   public static void transferExp(SkewNormal pair, SkewNormal[] mem)
/*     */   {
/* 172 */     Arrays.fill(params, 0.0D);
/* 173 */     for (int k = 0; k < mem.length; k++) {
/* 174 */       SkewNormal m_k = mem[k];
/* 175 */       for (int j = 0; j < params.length; j++) {
/* 176 */         if ((j == 0) && (Constants.trainEnsemble() == 2)) {
/* 177 */           params[j] += Math.pow(base, m_k.getParamValue(j));
/*     */         }
/*     */         else {
/* 180 */           params[j] += m_k.getParamValue(j);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 185 */     for (int i = 0; i < params.length; i++) {
/* 186 */       if (i == 0) {
/* 187 */         if (Constants.trainEnsemble() == 2) {
/* 188 */           pair.setParamValue(i, Math.log(params[i] / mem.length) / logbase);
/*     */         }
/*     */         else {
/* 191 */           pair.setParamValue(i, params[i] / mem.length);
/*     */         }
/*     */       }
/*     */       else {
/* 195 */         pair.setParamValue(i, params[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double evaluate(double[] argument)
/*     */   {
/* 204 */     for (int i = 0; i < argument.length; i++) {
/* 205 */       setParamValue(i, argument[i]);
/*     */     }
/* 207 */     double logL = 0.0D;
/* 208 */     for (int i = 0; i < this.pairs.size(); i++) {
/* 209 */       SkewNormal pair = (SkewNormal)this.pairs.get(i);
/* 210 */       SkewNormal[] mem = (SkewNormal[])this.members.get(i);
/* 211 */       transferExp(pair, mem);
/* 212 */       logL += pair.calcLH();
/*     */     }
/*     */     
/* 215 */     double prior = 0.0D;
/* 216 */     for (int i = 0; i < this.single.size(); i++) {
/* 217 */       prior += ((SkewNormal)this.single.get(i)).prior();
/*     */     }
/* 219 */     return -1.0D * (logL + prior);
/*     */   }
/*     */   
/*     */   public double getLowerBound(int n) {
/* 223 */     int i = findIndex(n);
/* 224 */     int n1 = i == 0 ? n : n - this.numParam[(i - 1)];
/* 225 */     return ((MultivariateFunction)this.single.get(i)).getLowerBound(n1);
/*     */   }
/*     */   
/* 228 */   public double getParamValue(int n) { int i = findIndex(n);
/* 229 */     int n1 = i == 0 ? n : n - this.numParam[(i - 1)];
/* 230 */     return ((SkewNormal)this.single.get(i)).getParamValue(n1);
/*     */   }
/*     */   
/*     */   public void setParamValue(int n, double val) {
/* 234 */     int i = findIndex(n);
/* 235 */     int n1 = i == 0 ? n : n - this.numParam[(i - 1)];
/* 236 */     ((SkewNormal)this.single.get(i)).setParamValue(n1, val);
/*     */   }
/*     */   
/*     */   public int getNumArguments()
/*     */   {
/* 241 */     return this.totParam;
/*     */   }
/*     */   
/*     */   public OrthogonalHints getOrthogonalHints()
/*     */   {
/* 246 */     return null;
/*     */   }
/*     */   
/*     */   public double getUpperBound(int n) {
/* 250 */     int i = findIndex(n);
/* 251 */     int n1 = i == 0 ? n : n - this.numParam[(i - 1)];
/* 252 */     return ((MultivariateFunction)this.single.get(i)).getUpperBound(n1);
/*     */   }
/*     */   
/*     */   public String getCompoundName(int i) {
/* 256 */     SkewNormal[] sn = (SkewNormal[])this.members.get(i);
/* 257 */     StringBuffer sb = new StringBuffer(sn[0].name());
/* 258 */     for (int j = 1; j < sn.length; j++) {
/* 259 */       sb.append(":" + sn[j].name());
/*     */     }
/* 261 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/ProbMultivariate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */