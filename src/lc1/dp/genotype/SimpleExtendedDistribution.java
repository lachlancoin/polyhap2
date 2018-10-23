/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.io.Serializable;
/*     */ import java.io.StringWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Random;
/*     */ import lc1.dp.Dirichlet;
/*     */ import lc1.dp.State;
/*     */ 
/*     */ public class SimpleExtendedDistribution extends PseudoDistribution implements Serializable
/*     */ {
/*     */   public double[] probs;
/*     */   public double[] pseudo;
/*     */   public double[] counts;
/*     */   
/*     */   public int sample()
/*     */   {
/*  19 */     double r = lc1.dp.genotype.io.Constants.rand.nextDouble();
/*  20 */     double cum = 0.0D;
/*  21 */     for (int i = 0; i < this.probs.length; i++) {
/*  22 */       cum += this.probs[i];
/*  23 */       if (r < cum) return i;
/*     */     }
/*  25 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public SimpleExtendedDistribution(int len)
/*     */   {
/*  31 */     this.probs = new double[len];
/*  32 */     this.counts = new double[len];
/*  33 */     this.pseudo = new double[len];
/*  34 */     Arrays.fill(this.counts, 0.0D);
/*  35 */     Arrays.fill(this.pseudo, 1.0D / len);
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(int len, double u) {
/*  39 */     this(len);
/*  40 */     Dirichlet dir = new Dirichlet(this.pseudo, u);
/*  41 */     arraycopy(dir.sample(), 0, this.probs, 0, len);
/*     */   }
/*     */   
/*     */ 
/*     */   public void normalise()
/*     */   {
/*  47 */     double sum = 0.0D;
/*  48 */     double sum_ps = 0.0D;
/*  49 */     for (int i = 0; i < this.pseudo.length; i++) {
/*  50 */       sum_ps += this.pseudo[i];
/*  51 */       sum += this.probs[i];
/*     */     }
/*  53 */     if ((sum == 0.0D) || (sum_ps == 0.0D)) throw new RuntimeException("!!");
/*  54 */     for (int i = 0; i < this.pseudo.length; i++) {
/*  55 */       this.pseudo[i] /= sum_ps;
/*  56 */       this.probs[i] /= sum;
/*     */     }
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(double[] init, double u) {
/*  61 */     this(init.length);
/*  62 */     int len = init.length;
/*  63 */     System.arraycopy(init, 0, this.pseudo, 0, len);
/*  64 */     if (u == Double.POSITIVE_INFINITY) {
/*  65 */       System.arraycopy(init, 0, this.probs, 0, len);
/*     */     }
/*     */     else {
/*  68 */       Dirichlet dir = new Dirichlet(this.pseudo, u);
/*  69 */       arraycopy(dir.sample(), 0, this.probs, 0, len);
/*     */     }
/*     */   }
/*     */   
/*     */   public double logProb()
/*     */   {
/*  75 */     double logL = 0.0D;
/*  76 */     for (int j = 0; j < this.counts.length; j++) {
/*  77 */       logL += this.counts[j] * Math.log(this.probs[j]);
/*     */     }
/*  79 */     return logL;
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(SimpleExtendedDistribution dist_init, SimpleExtendedDistribution dist_pseudo) {
/*  83 */     this(dist_init.probs.length);
/*  84 */     int len = dist_init.probs.length;
/*  85 */     System.arraycopy(dist_pseudo.probs, 0, this.pseudo, 0, len);
/*  86 */     System.arraycopy(dist_init.probs, 0, this.probs, 0, len);
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(SimpleExtendedDistribution dist_init) {
/*  90 */     this(dist_init.probs.length);
/*  91 */     int len = dist_init.probs.length;
/*  92 */     System.arraycopy(dist_init.pseudo, 0, this.pseudo, 0, len);
/*  93 */     System.arraycopy(dist_init.probs, 0, this.probs, 0, len);
/*     */   }
/*     */   
/*     */   public SimpleExtendedDistribution(Dirichlet dir) {
/*  97 */     this(dir.dist.length);
/*  98 */     arraycopy(dir.dist, 0, this.pseudo, 0, this.pseudo.length);
/*  99 */     Double[] sample = dir.sample();
/* 100 */     arraycopy(sample, 0, this.probs, 0, this.probs.length);
/*     */   }
/*     */   
/*     */   public void initialise() {
/* 104 */     Arrays.fill(this.counts, 0.0D);
/*     */   }
/*     */   
/*     */   public void setRandom(double emiss, boolean restart) {
/* 108 */     Dirichlet d = new Dirichlet(
/* 109 */       restart ? 
/* 110 */       this.pseudo : 
/* 111 */       this.probs, emiss);
/* 112 */     arraycopy(d.sample(), 0, this.probs, 0, this.probs.length);
/*     */   }
/*     */   
/*     */   private void arraycopy(Double[] doubles, int i, double[] probs2, int j, int length) {
/* 116 */     for (int k = 0; k < length; k++) {
/* 117 */       probs2[(k + j)] = doubles[(k + i)].doubleValue();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void transfer(double pseudocountWeight)
/*     */   {
/* 124 */     double sum = 0.0D;
/* 125 */     for (int i = 0; i < this.probs.length; i++) {
/* 126 */       double total1 = this.counts[i];
/* 127 */       double total2 = pseudocountWeight == 0.0D ? 0.0D : this.pseudo[i] * pseudocountWeight;
/* 128 */       double total = total1 + total2;
/* 129 */       this.probs[i] = total;
/* 130 */       sum += total;
/*     */     }
/* 132 */     if (sum == 0.0D) {
/* 133 */       System.arraycopy(this.pseudo, 0, this.probs, 0, this.probs.length);
/*     */     }
/*     */     else {
/* 136 */       for (int i = 0; i < this.probs.length; i++) {
/* 137 */         this.probs[i] /= sum;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double KLDistance(SimpleExtendedDistribution d2)
/*     */   {
/* 144 */     double sum = 0.0D;
/* 145 */     for (int j = 0; j < this.probs.length; j++) {
/* 146 */       double num1 = this.probs[j];
/* 147 */       double num2 = d2.probs[j];
/* 148 */       if (num1 != 0.0D) {
/* 149 */         sum += num1 * Math.log(num1 / num2);
/*     */       }
/*     */     }
/* 152 */     return sum;
/*     */   }
/*     */   
/*     */   public double sum() {
/* 156 */     double sum = 0.0D;
/* 157 */     for (int i = 0; i < this.probs.length; i++) {
/* 158 */       sum += this.probs[i];
/*     */     }
/* 160 */     return sum;
/*     */   }
/*     */   
/*     */   public void revertToPseudo() {
/* 164 */     System.arraycopy(this.pseudo, 0, this.probs, 0, this.probs.length);
/*     */   }
/*     */   
/*     */   public void resample(double u, boolean restart)
/*     */   {
/* 169 */     Dirichlet dir = new Dirichlet(restart ? this.pseudo : this.probs, u);
/* 170 */     arraycopy(dir.sample(), 0, this.probs, 0, this.probs.length);
/*     */   }
/*     */   
/*     */   public void addCounts(StateDistribution distribution) {
/* 174 */     double[] dist = distribution.dist;
/* 175 */     for (int j = 0; j < dist.length; j++)
/* 176 */       this.counts[j] += dist[j];
/*     */   }
/*     */   
/*     */   public String toString() {
/* 180 */     StringWriter sw = new StringWriter();
/* 181 */     PrintWriter pw = new PrintWriter(sw);
/* 182 */     print(pw);
/* 183 */     pw.close();
/* 184 */     return sw.getBuffer().toString();
/*     */   }
/*     */   
/* 187 */   public void print(PrintWriter pw) { for (int i = 0; i < this.probs.length; i++) {
/* 188 */       pw.print(this.probs[i] + " ");
/*     */     }
/* 190 */     pw.println();
/*     */   }
/*     */   
/*     */ 
/*     */   public void multiplyValues(double d)
/*     */   {
/* 196 */     for (int j = 0; j < this.probs.length; j++) {
/* 197 */       this.probs[j] *= d;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void put(State newState, double small)
/*     */   {
/* 205 */     this.probs[newState.index] = small;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void doubleSt()
/*     */   {
/* 212 */     double[] newp = new double[(this.probs.length - 1) * 2 + 1];
/* 213 */     this.counts = new double[newp.length];
/* 214 */     double[] newps = new double[newp.length];
/* 215 */     newp[0] = this.probs[0];
/* 216 */     newps[0] = this.pseudo[0];
/* 217 */     for (int i = 1; i < this.probs.length; i++) {
/* 218 */       newp[i] = (this.probs[i] / 2.0D);
/* 219 */       newp[(i + this.probs.length - 1)] = (this.probs[i] / 2.0D);
/* 220 */       newps[i] = (this.pseudo[i] / 2.0D);
/* 221 */       newps[(i + this.probs.length - 1)] = (this.pseudo[i] / 2.0D);
/*     */     }
/* 223 */     this.probs = newp;
/* 224 */     this.pseudo = newps;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void extend(double small, Double[] ps, Dirichlet newStates)
/*     */   {
/* 231 */     Double[] d = newStates.dist;
/* 232 */     this.pseudo = new double[ps.length];
/* 233 */     if (this.pseudo.length != d.length + this.probs.length) throw new RuntimeException("!!");
/* 234 */     arraycopy(ps, 0, this.pseudo, 0, ps.length);
/* 235 */     for (int i = 0; i < this.probs.length; i++) {
/* 236 */       this.probs[i] *= (1.0D - small);
/*     */     }
/* 238 */     double[] newprobs = new double[this.pseudo.length];
/* 239 */     System.arraycopy(this.probs, 0, newprobs, 0, this.probs.length);
/* 240 */     Double[] samp = newStates.sample();
/* 241 */     for (int i = 0; i < samp.length; i++) {
/* 242 */       newprobs[(i + this.probs.length)] = (samp[i].doubleValue() * small);
/*     */     }
/* 244 */     this.probs = newprobs;
/* 245 */     this.counts = new double[this.probs.length];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void fix()
/*     */   {
/* 252 */     Arrays.fill(this.pseudo, 0.0D);
/* 253 */     if (this.probs[0] < 0.5D) {
/* 254 */       this.probs[0] = 0.0D;
/* 255 */       this.probs[1] = 1.0D;
/*     */     }
/*     */     else {
/* 258 */       this.probs[0] = 1.0D;
/* 259 */       this.probs[1] = 0.0D;
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
/*     */ 
/*     */ 
/*     */   public double getProbs(int[] nullIndices, double[] probs2)
/*     */   {
/* 282 */     double sum = 0.0D;
/* 283 */     for (int i = 0; i < nullIndices.length; i++) {
/* 284 */       probs2[i] = this.probs[nullIndices[i]];
/* 285 */       sum += probs2[i];
/*     */     }
/* 287 */     return sum;
/*     */   }
/*     */   
/*     */   public int mostLikely()
/*     */   {
/* 292 */     int m_i = 0;
/* 293 */     double max = this.probs[0];
/* 294 */     for (int j = 1; j < this.probs.length; j++) {
/* 295 */       if (this.probs[j] > max) {
/* 296 */         max = this.probs[j];
/* 297 */         m_i = j;
/*     */       }
/*     */     }
/* 300 */     return m_i;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/SimpleExtendedDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */