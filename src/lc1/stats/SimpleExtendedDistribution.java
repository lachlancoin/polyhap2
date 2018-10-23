/*     */ package lc1.stats;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ public class SimpleExtendedDistribution
/*     */   extends PseudoDistribution
/*     */   implements Serializable
/*     */ {
/*     */   public double[] probs;
/*     */   public double[] counts;
/*     */   
/*     */   public void setProb(double[] prob)
/*     */   {
/*  24 */     System.arraycopy(prob, 0, this.probs, 0, prob.length);
/*     */   }
/*     */   
/*     */   public double totalCount() {
/*  28 */     return Constants.sum(this.counts);
/*     */   }
/*     */   
/*     */   public double evaluate(double[] pseudocountWeight)
/*     */   {
/*  33 */     transfer(pseudocountWeight);
/*  34 */     return logProb();
/*     */   }
/*     */   
/*     */   public double sample() {
/*  38 */     double r = Constants.rand.nextDouble();
/*  39 */     double cum = 0.0D;
/*  40 */     for (int i = 0; i < this.probs.length; i++) {
/*  41 */       cum += this.probs[i];
/*  42 */       if (r < cum) return i;
/*     */     }
/*  44 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleExtendedDistribution(int len)
/*     */   {
/*  50 */     this.probs = new double[len];
/*  51 */     this.counts = new double[len];
/*     */     
/*  53 */     Arrays.fill(this.counts, 0.0D);
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(int len, double u)
/*     */   {
/*  58 */     this(len);
/*  59 */     double[] pseudo = new double[len];
/*  60 */     Arrays.fill(pseudo, 1.0D / len);
/*  61 */     Dirichlet dir = new Dirichlet(pseudo, u);
/*  62 */     arraycopy(dir.sample(), 0, this.probs, 0, len);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void normalise(double[] probs)
/*     */   {
/*  68 */     double sum = 0.0D;
/*  69 */     for (int i = 0; i < probs.length; i++)
/*     */     {
/*  71 */       sum += probs[i];
/*     */     }
/*  73 */     if ((sum == 0.0D) && (probs.length > 0)) throw new RuntimeException("!!");
/*  74 */     for (int i = 0; i < probs.length; i++)
/*     */     {
/*  76 */       probs[i] /= sum; }
/*     */   }
/*     */   
/*     */   public static void normalise(Double[] probs) {
/*  80 */     double sum = 0.0D;
/*  81 */     for (int i = 0; i < probs.length; i++)
/*     */     {
/*  83 */       sum += probs[i].doubleValue();
/*     */     }
/*  85 */     if ((sum == 0.0D) && (probs.length > 0)) throw new RuntimeException("!!");
/*  86 */     for (int i = 0; i < probs.length; i++)
/*     */     {
/*  88 */       probs[i] = Double.valueOf(probs[i].doubleValue() / sum); }
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(double[] init, double u) {
/*  92 */     this(init.length);
/*     */     
/*  94 */     int len = init.length;
/*     */     
/*  96 */     if (u == Double.POSITIVE_INFINITY)
/*     */     {
/*  98 */       System.arraycopy(init, 0, this.probs, 0, len);
/*     */     }
/*     */     else {
/* 101 */       Dirichlet dir = new Dirichlet(init, u);
/* 102 */       arraycopy(dir.sample(), 0, this.probs, 0, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public double logProb()
/*     */   {
/* 108 */     double logL = 0.0D;
/* 109 */     for (int j = 0; j < this.counts.length; j++) {
/* 110 */       if (this.counts[j] != 0.0D)
/* 111 */         logL += this.counts[j] * Math.log(this.probs[j]);
/*     */     }
/* 113 */     return logL;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleExtendedDistribution(PseudoDistribution dist_init)
/*     */   {
/* 123 */     this(dist_init.probs().length);
/* 124 */     int len = dist_init.probs().length;
/*     */     
/* 126 */     setProb(dist_init.probs());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleExtendedDistribution(Sampler dir)
/*     */   {
/* 137 */     this(dir.dist.length);
/*     */     
/* 139 */     Double[] sample = dir.sample();
/* 140 */     arraycopy(sample, 0, this.probs, 0, this.probs.length);
/*     */   }
/*     */   
/*     */   public void initialise() {
/* 144 */     Arrays.fill(this.counts, 0.0D);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void arraycopy(Double[] doubles, int i, double[] probs2, int j, int length)
/*     */   {
/* 156 */     for (int k = 0; k < length; k++) {
/* 157 */       probs2[(k + j)] = doubles[(k + i)].doubleValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public void transfer(double[] pseudo)
/*     */   {
/* 163 */     double sum = 0.0D;
/* 164 */     for (int i = 0; i < this.probs.length; i++) {
/* 165 */       double total = this.counts[i] + pseudo[i];
/* 166 */       this.probs[i] = total;
/* 167 */       sum += total;
/*     */     }
/* 169 */     if (sum > 1.0E-9D) {
/* 170 */       for (int i = 0; i < this.probs.length; i++) {
/* 171 */         this.probs[i] /= sum;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void transfer(double pseudo)
/*     */   {
/* 180 */     double sum = 0.0D;
/*     */     
/* 182 */     for (int i = 0; i < this.probs.length; i++) {
/* 183 */       double total = this.counts[i] + (this.probs[i] == 0.0D ? 0.0D : pseudo);
/* 184 */       this.probs[i] = total;
/* 185 */       sum += total;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */     if (sum > 0.0D) {
/* 196 */       for (int i = 0; i < this.probs.length; i++) {
/* 197 */         this.probs[i] /= sum;
/*     */       }
/*     */       
/*     */     } else {
/* 201 */       Arrays.fill(this.probs, 1.0D / this.probs.length);
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
/*     */   public double KLDistance(PseudoDistribution d2)
/*     */   {
/* 214 */     double sum = 0.0D;
/* 215 */     for (int j = 0; j < this.probs.length; j++) {
/* 216 */       double num1 = this.probs[j];
/* 217 */       double num2 = d2.probs(j);
/* 218 */       if (num1 != 0.0D) {
/* 219 */         sum += num1 * Math.log(num1 / num2);
/*     */       }
/*     */     }
/* 222 */     return sum;
/*     */   }
/*     */   
/*     */   public double sum() {
/* 226 */     double sum = 0.0D;
/* 227 */     for (int i = 0; i < this.probs.length; i++) {
/* 228 */       sum += this.probs[i];
/*     */     }
/* 230 */     return sum;
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
/*     */   public void addCounts(StateDistribution distribution)
/*     */   {
/* 244 */     double[] dist = distribution.dist;
/* 245 */     for (int j = 0; j < dist.length; j++)
/* 246 */       this.counts[j] += dist[j];
/*     */   }
/*     */   
/*     */   public String toString() {
/* 250 */     StringWriter sw = new StringWriter();
/* 251 */     PrintWriter pw = new PrintWriter(sw);
/* 252 */     print(pw, false, getPrintString(), "\n");
/* 253 */     pw.close();
/* 254 */     return sw.getBuffer().toString();
/*     */   }
/*     */   
/*     */   public String getPrintString() {
/* 258 */     StringBuffer sb = new StringBuffer();
/* 259 */     for (int i = 0; i < this.probs.length; i++) {
/* 260 */       sb.append("%5.3g ");
/*     */     }
/* 262 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void printSimple(PrintWriter pw, String name, String newLine, double thresh)
/*     */   {
/* 267 */     pw.print(name + "->{");
/* 268 */     int[] l = Constants.getMax2(this.probs);
/*     */     
/*     */ 
/* 271 */     for (int k = 0; k < l.length; k++) {
/* 272 */       pw.print(l[k] + ":" + Math.round(100.0D * this.probs[l[k]]) + ",");
/*     */     }
/*     */     
/* 275 */     pw.print("}" + newLine);
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, boolean probsOnly, String st, String newLine) {
/* 279 */     Double[] d = new Double[this.probs.length];
/* 280 */     for (int i = 0; i < this.probs.length; i++) {
/* 281 */       d[i] = Double.valueOf(this.probs[i]);
/*     */     }
/* 283 */     pw.print(Format.sprintf(st, d) + ";   ");
/* 284 */     if (!probsOnly) {
/* 285 */       for (int i = 0; i < this.probs.length; i++) {
/* 286 */         d[i] = Double.valueOf(this.counts[i]);
/*     */       }
/* 288 */       pw.print(Format.sprintf(st, d) + ";   ");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 294 */     pw.print(newLine);
/*     */   }
/*     */   
/*     */ 
/*     */   public void multiplyValues(double d)
/*     */   {
/* 300 */     for (int j = 0; j < this.probs.length; j++) {
/* 301 */       this.probs[j] *= d;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void put(State newState, double small)
/*     */   {
/* 309 */     this.probs[newState.getIndex()] = small;
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
/*     */   public double getProbs(int[] nullIndices, double[] probs2)
/*     */   {
/* 386 */     double sum = 0.0D;
/* 387 */     for (int i = 0; i < nullIndices.length; i++) {
/* 388 */       probs2[i] = this.probs[nullIndices[i]];
/* 389 */       sum += probs2[i];
/*     */     }
/* 391 */     return sum;
/*     */   }
/*     */   
/*     */   public int mostLikely()
/*     */   {
/* 396 */     int m_i = 0;
/* 397 */     double max = this.probs[0];
/* 398 */     for (int j = 1; j < this.probs.length; j++) {
/* 399 */       if (this.probs[j] > max) {
/* 400 */         max = this.probs[j];
/* 401 */         m_i = j;
/*     */       }
/*     */     }
/* 404 */     return m_i;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static double logToProb(double[] probs)
/*     */   {
/* 412 */     double max = Double.NEGATIVE_INFINITY;
/* 413 */     for (int i = 0; i < probs.length; i++) {
/* 414 */       if (probs[i] > max) {
/* 415 */         max = probs[i];
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 420 */     for (int i = 0; i < probs.length; i++) {
/* 421 */       probs[i] = Math.exp(probs[i] - max);
/*     */     }
/*     */     
/* 424 */     return -max;
/*     */   }
/*     */   
/* 427 */   public double getProbs(int to) { return this.probs[to]; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double[] probs()
/*     */   {
/* 434 */     return this.probs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProbs(int to, double d)
/*     */   {
/* 444 */     this.probs[to] = d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PseudoDistribution clone()
/*     */   {
/* 452 */     return new SimpleExtendedDistribution(this);
/*     */   }
/*     */   
/* 455 */   public PseudoDistribution clone(double swtch) { return new SimpleExtendedDistribution(this.probs, swtch); }
/*     */   
/*     */   public void validate()
/*     */   {
/* 459 */     double sum = sum();
/* 460 */     if (Math.abs(sum - 1.0D) > SimpleDistribution.tolerance) throw new RuntimeException("!!! " + sum);
/*     */   }
/*     */   
/*     */   public void apply(int[] transformation)
/*     */   {
/* 465 */     double[] cp = new double[transformation.length];
/* 466 */     System.arraycopy(this.probs, 0, cp, 0, this.probs.length);
/* 467 */     for (int j = 0; j < transformation.length; j++) {
/* 468 */       this.probs[transformation[j]] = cp[j];
/*     */     }
/* 470 */     System.arraycopy(this.counts, 0, cp, 0, this.probs.length);
/* 471 */     for (int j = 0; j < transformation.length; j++) {
/* 472 */       this.counts[transformation[j]] = cp[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCount(int obj_index, double value)
/*     */   {
/* 478 */     this.counts[obj_index] += value;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double probs(int obj_i)
/*     */   {
/* 485 */     return this.probs[obj_i];
/*     */   }
/*     */   
/* 488 */   public int getMax() { return Constants.getMax(this.probs); }
/*     */   
/*     */   public double[] counts() {
/* 491 */     return this.counts;
/*     */   }
/*     */   
/* 494 */   public void setCounts(int i1, double cnt) { this.counts[i1] = cnt; }
/*     */   
/*     */ 
/*     */   public Integer fixedInteger()
/*     */   {
/* 499 */     return null;
/*     */   }
/*     */   
/*     */   public PseudoDistribution makeFixed() {
/* 503 */     int ind = Constants.getMax(this.probs);
/* 504 */     if (this.probs[ind] > Constants.fixedThresh()) {
/* 505 */       return new IntegerDistribution(ind);
/*     */     }
/* 507 */     return null;
/*     */   }
/*     */   
/* 510 */   public void swtch(double[] d, int[] alias) { double[] tmp = new double[d.length];
/* 511 */     System.arraycopy(d, 0, tmp, 0, d.length);
/* 512 */     for (int i = 0; i < d.length; i++) {
/* 513 */       d[alias[i]] = tmp[i];
/*     */     }
/*     */   }
/*     */   
/*     */   public void swtchAlleles(int[] switchTranslation) {
/* 518 */     swtch(this.probs, switchTranslation);
/* 519 */     swtch(this.counts, switchTranslation);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(SkewNormal[] probR, double d) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(IlluminaProbB[] probB, Integer index, double d) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFixedIndex(int k)
/*     */   {
/* 537 */     double[] probs = probs();
/* 538 */     int max_index = Constants.getMax(probs);
/* 539 */     double prob_k = probs[k];
/* 540 */     double prob_max = probs[max_index];
/* 541 */     probs[k] = prob_max;
/* 542 */     probs[max_index] = prob_k;
/*     */   }
/*     */   
/*     */   public void setParamsAsAverageOf(ProbabilityDistribution[] tmp) {
/* 546 */     Arrays.fill(this.probs, 0.0D);
/* 547 */     for (int i = 0; i < tmp.length; i++) {
/* 548 */       for (int j = 0; j < this.probs.length; j++) {
/* 549 */         this.probs[j] += ((SimpleExtendedDistribution)tmp[i]).probs(j);
/*     */       }
/*     */     }
/*     */     
/* 553 */     normalise(this.probs);
/*     */   }
/*     */   
/*     */   public void addCounts(ProbabilityDistribution probabilityDistribution) {
/* 557 */     SimpleExtendedDistribution dist = (SimpleExtendedDistribution)probabilityDistribution;
/* 558 */     for (int i = 0; i < dist.counts.length; i++) {
/* 559 */       this.counts[i] += dist.counts[i];
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCounts(SimpleExtendedDistribution dist, Map<Integer, Integer> map) {
/* 564 */     for (int i = 0; i < dist.counts.length; i++) {
/* 565 */       this.counts[((Integer)map.get(Integer.valueOf(i))).intValue()] += dist.counts[i];
/*     */     }
/*     */   }
/*     */   
/*     */   public double[] getCount(double[] angle)
/*     */   {
/* 571 */     if (angle.length == this.probs.length) return this.probs;
/* 572 */     int ratio = (int)Math.round(this.probs.length / angle.length);
/* 573 */     if (Math.abs(this.counts.length - ratio * angle.length) > 1.0E-4D) throw new RuntimeException("!!");
/* 574 */     double[] res = new double[angle.length];
/* 575 */     Arrays.fill(res, 0.0D);
/* 576 */     for (int i = 0; i < angle.length; i++) {
/* 577 */       for (int j = 0; j < ratio; j++) {
/* 578 */         res[i] += this.probs[(i * ratio + j)];
/*     */       }
/*     */     }
/* 581 */     return res;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/SimpleExtendedDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */