/*     */ package lc1.stats;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.util.Constants;
/*     */ import org.jfree.data.xy.XYSeries;
/*     */ import pal.math.MultivariateFunction;
/*     */ import pal.math.MultivariateMinimum;
/*     */ import pal.math.OrthogonalHints;
/*     */ import pal.statistics.NormalDistribution;
/*     */ 
/*     */ public class SkewNormal extends JSci.maths.statistics.ProbabilityDistribution implements ProbabilityDistribution, MultivariateFunction
/*     */ {
/*  23 */   int paramIndex = 1;
/*     */   
/*  25 */   public int getParamIndex() { return this.paramIndex; }
/*     */   
/*     */   public SkewNormal clone() {
/*  28 */     return new SkewNormal(this);
/*     */   }
/*     */   
/*  31 */   public SkewNormal clone(double u) { return new SkewNormal(this, u); }
/*     */   
/*     */   private String name;
/*     */   public void transfercounts(EmissionState innerState, int phen_index, int i)
/*     */   {
/*  36 */     for (Iterator<Map.Entry<Double, Double>> it = this.observations.entrySet().iterator(); it.hasNext();) {
/*  37 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/*  38 */       innerState.addCountDT(((Double)nxt.getKey()).doubleValue(), phen_index, ((Double)nxt.getValue()).doubleValue(), i);
/*     */     }
/*     */   }
/*     */   
/*  42 */   public double[] getCount(double[] angle) { throw new RuntimeException("!!"); }
/*     */   
/*     */   public void addCounts(ProbabilityDistribution probabilityDistribution) {
/*  45 */     SkewNormal n = (SkewNormal)probabilityDistribution;
/*  46 */     for (Iterator<Map.Entry<Double, Double>> it = n.observations.entrySet().iterator(); it.hasNext();) {
/*  47 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/*  48 */       addCount(((Double)nxt.getKey()).doubleValue(), ((Double)nxt.getValue()).doubleValue());
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  53 */     SkewNormal sn = new SkewNormal(-1.0D, 0.2D, -10.0D, -5.0D, 5.0D, 100.0D, 1.0D);
/*  54 */     double[] x = { -1.1D, -0.9D, -0.5D, 0.0D };
/*  55 */     for (int i = 0; i < x.length; i++) {
/*  56 */       System.err.print(sn.dsn(x[i], false) + "\t");
/*     */     }
/*  58 */     System.err.println();
/*     */     
/*  60 */     sn = new SkewNormal(-1.0D, 0.2D, 0.0D, -5.0D, 5.0D, 100.0D, 1.0D);
/*  61 */     for (int i = 0; i < x.length; i++) {
/*  62 */       System.err.print(sn.dsn(x[i], false) + "\t");
/*     */     }
/*  64 */     System.err.println();
/*     */     
/*  66 */     sn = new SkewNormal(-1.0D, 0.2D, 10.0D, -5.0D, 5.0D, 100.0D, 1.0D);
/*  67 */     for (int i = 0; i < x.length; i++) {
/*  68 */       System.err.print(sn.dsn(x[i], false) + "\t");
/*     */     }
/*  70 */     System.err.println();
/*     */   }
/*     */   
/*     */   public double getParamValue(int i) {
/*  74 */     if (i == 0) return this.location;
/*  75 */     if (i == 1) return this.scale;
/*  76 */     if (i == 2) return this.shape;
/*  77 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*  80 */   public double probability(double r, double offset) { return probability(r); }
/*     */   
/*     */ 
/*     */   public String name()
/*     */   {
/*  85 */     return this.name;
/*     */   }
/*     */   
/*  88 */   public boolean equals(Object obj) { return toString().equals(((SkewNormal)obj).toString()); }
/*     */   
/*     */ 
/*  91 */   public int hashCode() { return this.name.hashCode(); }
/*     */   
/*     */   public double sum() {
/*  94 */     double s = 0.0D;
/*  95 */     for (Iterator<Double> it = this.observations.values().iterator(); it.hasNext();) {
/*  96 */       s += ((Double)it.next()).doubleValue();
/*     */     }
/*  98 */     return s; }
/*     */   
/* 100 */   public SortedMap<Double, Double> observations = new TreeMap();
/*     */   
/*     */   final double round;
/*     */   
/*     */   static NormalDistribution normal;
/*     */   protected double location;
/*     */   protected double scale;
/*     */   protected double shape;
/*     */   
/* 109 */   public double round(double d) { return Math.round(d * this.round) / this.round; }
/*     */   
/*     */   public void addObservation(double d, double weight) {
/* 112 */     double d1 = round(d);
/* 113 */     Double w = (Double)this.observations.get(Double.valueOf(d1));
/* 114 */     this.observations.put(Double.valueOf(d1), Double.valueOf(w == null ? weight : weight + w.doubleValue()));
/*     */   }
/*     */   
/*     */ 
/* 118 */   static double pi = 3.141592653589793D;
/*     */   
/* 120 */   double[] lower = { -5.0D, 0.001D, -1.0E10D };
/* 121 */   double[] upper = { 5.0D, 1000.0D, 1.0E10D };
/*     */   
/*     */   protected double[] meanPrior;
/*     */   
/* 125 */   public SkewNormal(double mean, double stddev, double skew, double min, double max, double round, double priorModifier) { this(mean, stddev, skew, new double[] { min, 0.001D, -1.0E10D }, new double[] { max, 1000.0D, 1.0E10D }, round, priorModifier); }
/*     */   
/*     */   public void setParams(double mean, double stddev, double skew) {
/* 128 */     this.location = mean;
/* 129 */     this.scale = stddev;
/* 130 */     this.shape = skew;
/* 131 */     this.meanPrior = new double[] { this.location, 1.0D };
/* 132 */     this.stddevPrior = new double[] { this.scale, 1.0D };
/* 133 */     this.skewPrior = new double[] { this.shape, 1.0D };
/*     */   }
/*     */   
/*     */   public SkewNormal(double mean, double stddev, double skew, double[] lower, double[] upper, double round, double priorModifier) {
/* 137 */     this.priorModifier = priorModifier;
/* 138 */     this.round = round;
/* 139 */     this.lower = lower;
/* 140 */     this.upper = upper;
/* 141 */     setParams(mean, stddev, skew);
/* 142 */     this.name = toString();
/* 143 */     if (stddev < 0.0D) throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public SkewNormal(SkewNormal skewNormal) {
/* 147 */     this.priorModifier = skewNormal.priorModifier;
/* 148 */     this.round = skewNormal.round;
/* 149 */     this.lower = skewNormal.lower;
/* 150 */     this.upper = skewNormal.upper;
/* 151 */     setParams(skewNormal.location, skewNormal.scale, skewNormal.shape);
/* 152 */     this.name = skewNormal.name;
/* 153 */     this.observations = new TreeMap(skewNormal.observations);
/*     */   }
/*     */   
/*     */   public SkewNormal(SkewNormal skewNormal, double u)
/*     */   {
/* 158 */     this(skewNormal);
/* 159 */     this.location = NormalDistribution.quantile(Constants.rand.nextDouble(), this.location, 1.0D / u);
/*     */   }
/*     */   
/*     */   protected double[] stddevPrior;
/*     */   protected double[] skewPrior;
/*     */   public void setParamsAsAverageOf(ProbabilityDistribution[] tmp) {
/* 165 */     this.location = 0.0D;this.scale = 0.0D;
/* 166 */     this.shape = 0.0D;
/*     */     
/* 168 */     for (int i = 0; i < tmp.length; i++) {
/* 169 */       this.location += ((SkewNormal)tmp[i]).location;
/* 170 */       this.scale += ((SkewNormal)tmp[i]).scale;
/* 171 */       this.shape += ((SkewNormal)tmp[i]).shape;
/*     */     }
/* 173 */     this.location /= tmp.length;
/* 174 */     this.scale /= tmp.length;
/* 175 */     this.shape /= tmp.length;
/*     */   }
/*     */   
/*     */   public double cumulative(double arg0)
/*     */   {
/* 180 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double inverse(double arg0)
/*     */   {
/* 185 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double probability(double arg0)
/*     */   {
/* 190 */     double res = dsn(arg0, false);
/*     */     
/*     */ 
/*     */ 
/* 194 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public double dsn(double x, boolean log)
/*     */   {
/* 200 */     double z = (x - this.location) / this.scale;
/*     */     double y;
/*     */     double y;
/* 203 */     if (log) {
/* 204 */       y = -0.9189385332046727D - Math.log(this.scale) - Math.pow(z, 2.0D) / 2.0D + zeta(0, this.shape * z).doubleValue();
/*     */     } else
/* 206 */       y = 2.0D * dnorm(z) * pnorm(z * this.shape, false) / this.scale;
/* 207 */     if (this.scale <= 0.0D) { y = NaN.0D;
/*     */     }
/* 209 */     return y;
/*     */   }
/*     */   
/* 212 */   private static double dnorm(double z) { return NormalDistribution.pdf(z, 0.0D, 1.0D); }
/*     */   
/*     */   private static double pnorm(double z, boolean log) {
/* 215 */     if (log) return Math.log(NormalDistribution.cdf(z, 0.0D, 1.0D));
/* 216 */     return NormalDistribution.cdf(z, 0.0D, 1.0D);
/*     */   }
/*     */   
/*     */   public double getMean()
/*     */   {
/* 221 */     return this.location;
/*     */   }
/*     */   
/* 224 */   public double getStdDev() { return this.scale; }
/*     */   
/*     */   public static Double zeta(int k, double x)
/*     */   {
/* 228 */     if (((k < 0 ? 1 : 0) | (k > 4 ? 1 : 0) | (k != Math.round(k) ? 1 : 0)) != 0) return null;
/* 229 */     k = Math.round(k);
/* 230 */     boolean na = Double.isNaN(x);
/* 231 */     if (na) x = 0.0D;
/* 232 */     Double z = null;
/* 233 */     if (k == 0) {
/* 234 */       z = Double.valueOf(pnorm(x, true) + Math.log(2.0D));
/*     */     }
/* 236 */     else if (k == 1) {
/* 237 */       z = Double.valueOf(
/* 238 */         x > -200.0D ? Math.exp(-Math.pow(x, 2.0D) / 2.0D - 0.5D * Math.log(2.0D * pi) - pnorm(x, true)) : x > -20.0D ? dnorm(x) / pnorm(x, false) : 
/* 239 */         -x * (1.0D + 1.0D / Math.pow(x, 2.0D) - 2.0D / Math.pow(x, 4.0D)));
/*     */ 
/*     */     }
/* 242 */     else if (k == 2) {
/* 243 */       z = Double.valueOf(-zeta(1, x).doubleValue() * (x + zeta(1, x).doubleValue()));
/*     */     }
/* 245 */     else if (k == 3) {
/* 246 */       z = Double.valueOf(-zeta(2, x).doubleValue() * (x + zeta(1, x).doubleValue()) - zeta(1, x).doubleValue() * (1.0D + zeta(2, x).doubleValue()));
/*     */     }
/* 248 */     else if (k == 4) {
/* 249 */       z = Double.valueOf(-zeta(3, x).doubleValue() * (x + 2.0D * zeta(1, x).doubleValue()) - 2.0D * zeta(2, x).doubleValue() * (1.0D + zeta(2, x).doubleValue()));
/*     */     }
/* 251 */     if (x == Double.NEGATIVE_INFINITY) {
/* 252 */       if (k != 0)
/* 253 */         if (k == 1) { z = Double.valueOf(z.doubleValue() == Double.NEGATIVE_INFINITY ? Double.POSITIVE_INFINITY : z.doubleValue());
/* 254 */         } else if (k == 2) { z = Double.valueOf(z.doubleValue() == Double.NEGATIVE_INFINITY ? 1.0D : z.doubleValue());
/* 255 */         } else if ((k == 3) || (k == 4)) { z = Double.valueOf(z.doubleValue() == Double.NEGATIVE_INFINITY ? 0.0D : z.doubleValue());
/* 256 */         } else if (k > 4) z = null;
/* 257 */       if (k > 1) z = Double.valueOf(x == Double.POSITIVE_INFINITY ? 0.0D : z.doubleValue());
/* 258 */       z = Double.valueOf(na ? NaN.0D : z.doubleValue());
/*     */     }
/* 260 */     return z; }
/*     */   
/* 262 */   static final double cntThresh = Constants.countThresh();
/*     */   public final double priorModifier;
/*     */   double pseudoM;
/*     */   
/* 266 */   public synchronized void addCount(double d, double w) { if (w > cntThresh)
/* 267 */       addObservation(d, w);
/*     */   }
/*     */   
/*     */   public void initialiseCounts() {
/* 271 */     this.observations.clear();
/*     */   }
/*     */   
/*     */ 
/*     */   public String getObsString()
/*     */   {
/* 277 */     if (this.observations.size() > 0) return mean();
/* 278 */     return "-";
/*     */   }
/*     */   
/*     */   public double mean() {
/* 282 */     double sum = 0.0D;
/* 283 */     double tot = sum();
/*     */     
/*     */ 
/* 286 */     for (Iterator<Double> it = this.observations.keySet().iterator(); it.hasNext();) {
/* 287 */       Double d = (Double)it.next();
/* 288 */       sum += ((Double)this.observations.get(d)).doubleValue() / tot;
/* 289 */       if (sum > 0.5D) { return d.doubleValue();
/*     */       }
/*     */     }
/* 292 */     return ((Double)this.observations.lastKey()).doubleValue();
/*     */   }
/*     */   
/*     */   public void transfer(double ps) {
/* 296 */     maximise(ps, ps, ps);
/*     */   }
/*     */   
/*     */   public void maximise(double pseudoM, double pseudoSd, double pseudoSk) {
/* 300 */     setPrior(pseudoM, pseudoSd, pseudoSk);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 309 */     final MultivariateMinimum os = new pal.math.ConjugateDirectionSearch();
/* 310 */     final double[] xvec = { this.location, this.scale, this.shape };
/* 311 */     double[] init = new double[xvec.length];
/* 312 */     System.arraycopy(xvec, 0, init, 0, xvec.length);
/* 313 */     Runnable run = new Runnable() {
/*     */       public void run() {
/* 315 */         os.optimize(SkewNormal.this, xvec, 0.01D, 0.01D);
/*     */       }
/* 317 */     };
/* 318 */     Thread th = new Thread(run);
/* 319 */     th.run();
/*     */     try {
/* 321 */       for (int i = 0; i < 100; i++) {
/* 322 */         Thread.sleep(100L);
/* 323 */         if (!th.isAlive()) break;
/*     */       }
/* 325 */       if (th.isAlive())
/*     */       {
/* 327 */         th.stop();
/* 328 */         System.arraycopy(init, 0, xvec, 0, xvec.length);
/*     */       }
/*     */     } catch (Exception exc) {
/* 331 */       exc.printStackTrace();
/*     */     }
/* 333 */     this.location = xvec[0];
/* 334 */     this.scale = xvec[1];
/* 335 */     this.shape = xvec[2];
/*     */     
/*     */ 
/*     */ 
/* 339 */     this.paramIndex += 1;
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
/*     */   public void setPrior(double pseudo, double pseudoSD, double pseudoSk)
/*     */   {
/* 360 */     this.meanPrior[1] = (1.0D / (pseudo * this.priorModifier));
/* 361 */     this.stddevPrior[1] = (1.0D / (pseudoSD * this.priorModifier));
/* 362 */     this.skewPrior[1] = (1.0D / pseudoSk);
/* 363 */     this.pseudoM = pseudo;
/* 364 */     this.pseudoSD = pseudoSD;
/* 365 */     this.pseudoSk = pseudoSk;
/* 366 */     if (this.pseudoM > 10000.0D) this.lower[0] = (this.upper[0] = this.meanPrior[0]);
/* 367 */     if (pseudoSD > 10000.0D) this.lower[1] = (this.upper[1] = this.stddevPrior[0]);
/* 368 */     if (pseudoSk > 10000.0D) this.lower[2] = (this.upper[2] = this.skewPrior[0]);
/*     */   }
/*     */   
/*     */   public double calcLH()
/*     */   {
/* 373 */     double l = 0.0D;
/* 374 */     for (Iterator<Map.Entry<Double, Double>> it = this.observations.entrySet().iterator(); it.hasNext();) {
/* 375 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/* 376 */       double val = ((Double)nxt.getKey()).doubleValue();
/* 377 */       double weight = ((Double)nxt.getValue()).doubleValue();
/*     */       
/* 379 */       double prob = dsn(val, true);
/* 380 */       if (prob == Double.NEGATIVE_INFINITY)
/*     */       {
/* 382 */         double res = -1000000.0D * (
/* 383 */           Math.pow(this.location - this.meanPrior[0], 2.0D) + 
/* 384 */           Math.pow(this.scale - this.stddevPrior[0], 2.0D) + 
/* 385 */           Math.pow(this.shape - this.skewPrior[0], 2.0D));
/*     */         
/* 387 */         return res;
/*     */       }
/* 389 */       l += prob * weight;
/*     */     }
/*     */     
/* 392 */     return l;
/*     */   }
/*     */   
/*     */   public double prior() {
/* 396 */     throw new Error("Unresolved compilation problems: \n\tThe method logpdf(double, double, double) is undefined for the type NormalDistribution\n\tThe method logpdf(double, double, double) is undefined for the type NormalDistribution\n\tThe method logpdf(double, double, double) is undefined for the type NormalDistribution\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   double pseudoSD;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   double pseudoSk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double calcL()
/*     */   {
/* 424 */     return calcLH() + prior();
/*     */   }
/*     */   
/*     */   public String toString() {
/* 428 */     Double[] d = { Double.valueOf(this.location), Double.valueOf(this.scale), Double.valueOf(this.shape) };
/* 429 */     return Format.sprintf("c(%5.2g, %5.2g, %5.2g)", d);
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
/*     */   public double evaluate(double[] argument)
/*     */   {
/* 444 */     if (Double.isNaN(argument[0])) throw new RuntimeException("!!" + argument[0] + " " + argument[1] + " " + argument[2]);
/* 445 */     this.location = argument[0];
/* 446 */     this.scale = argument[1];
/* 447 */     this.shape = argument[2];
/*     */     
/* 449 */     double res = -1.0D * calcL();
/*     */     
/*     */ 
/* 452 */     return res;
/*     */   }
/*     */   
/*     */   public void ci(Double[] d, double[] r) {
/* 456 */     for (int i = 0; i < d.length; i++) {
/* 457 */       d[i] = Double.valueOf(NormalDistribution.quantile(r[i], this.location, this.scale));
/*     */     }
/*     */   }
/*     */   
/*     */   public XYSeries plotTheoretical(String name1, boolean cum) {
/* 462 */     XYSeries newD = new XYSeries(name1);
/* 463 */     double sum = 0.0D;
/* 464 */     double minq = 1.0E-7D;
/* 465 */     double maxq = 0.9999999D;
/* 466 */     double min = NormalDistribution.quantile(minq, this.location, this.scale);
/* 467 */     double max = NormalDistribution.quantile(maxq, this.location, this.scale);
/* 468 */     double incr = 0.001D;
/* 469 */     double sum1 = 0.0D;
/* 470 */     for (double j = min; j < max; j += incr) {
/* 471 */       sum1 += probability(j);
/*     */     }
/*     */     
/* 474 */     for (double j = min; j < max; j += incr) {
/* 475 */       double probj = probability(j) / sum1;
/* 476 */       sum += probj * incr;
/* 477 */       double res = cum ? sum : probj;
/* 478 */       if (res > 1.0E-11D)
/*     */       {
/* 480 */         newD.add(j, res);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 485 */     return newD;
/*     */   }
/*     */   
/* 488 */   public double sumObs() { double sum = 0.0D;
/* 489 */     for (Iterator<Map.Entry<Double, Double>> it = this.observations.entrySet().iterator(); it.hasNext();) {
/* 490 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/* 491 */       sum += ((Double)nxt.getValue()).doubleValue();
/*     */     }
/*     */     
/* 494 */     return sum;
/*     */   }
/*     */   
/* 497 */   public XYSeries plotObservations(String name1, boolean cum) { XYSeries newD = new XYSeries(name1);
/* 498 */     double sum = 0.0D;
/* 499 */     for (Iterator<Map.Entry<Double, Double>> it = this.observations.entrySet().iterator(); it.hasNext();) {
/* 500 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/* 501 */       sum += ((Double)nxt.getValue()).doubleValue();
/* 502 */       double res = cum ? sum : ((Double)nxt.getValue()).doubleValue();
/* 503 */       if (res > 1.0E-7D)
/*     */       {
/* 505 */         newD.add(((Double)nxt.getKey()).doubleValue(), res);
/*     */       }
/*     */     }
/*     */     
/* 509 */     return newD;
/*     */   }
/*     */   
/*     */   public double getLowerBound(int n) {
/* 513 */     return this.lower[n];
/*     */   }
/*     */   
/*     */   public int getNumArguments() {
/* 517 */     return 3;
/*     */   }
/*     */   
/* 520 */   public OrthogonalHints getOrthogonalHints() { return null; }
/*     */   
/*     */   public double getUpperBound(int n)
/*     */   {
/* 524 */     return this.upper[n];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double rsn()
/*     */   {
/* 531 */     double u1 = NormalDistribution.quantile(Constants.rand.nextDouble(), 0.0D, 1.0D);
/* 532 */     double u2 = NormalDistribution.quantile(Constants.rand.nextDouble(), 0.0D, 1.0D);
/* 533 */     boolean id = u2 > this.shape * u1;
/* 534 */     if (id) { u1 = -u1;
/*     */     }
/* 536 */     return this.location + this.scale * u1;
/*     */   }
/*     */   
/*     */   public Map<Double, Double> getObservations() {
/* 540 */     return this.observations;
/*     */   }
/*     */   
/*     */   public void transferParams(JSci.maths.statistics.ProbabilityDistribution pdist0) {
/* 544 */     this.location = ((SkewNormal)pdist0).location;
/* 545 */     this.scale = ((SkewNormal)pdist0).scale;
/* 546 */     this.shape = ((SkewNormal)pdist0).shape;
/*     */   }
/*     */   
/* 549 */   public void setParamValue(int n1, double val) { if (n1 == 0) { this.location = val;
/* 550 */     } else if (n1 == 1) { this.scale = val;
/* 551 */     } else if (n1 == 2) this.shape = val; else
/* 552 */       throw new RuntimeException("!!");
/*     */   }
/*     */   
/* 555 */   public void recalcName() { this.name = toString(); }
/*     */   
/*     */   public void appendToName(String string) {
/* 558 */     this.name = (this.name + ";" + string);
/*     */   }
/*     */   
/*     */   public void updateParamIndex() {
/* 562 */     this.paramIndex += 1;
/*     */   }
/*     */   
/*     */   public double shape() {
/* 566 */     return this.shape;
/*     */   }
/*     */   
/* 569 */   public double sample() { throw new RuntimeException("!!"); }
/*     */   
/*     */   public void initialise() {
/* 572 */     initialiseCounts();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/SkewNormal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */