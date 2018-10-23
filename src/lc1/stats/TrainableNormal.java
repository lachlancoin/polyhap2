/*     */ package lc1.stats;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.util.Constants;
/*     */ import org.jfree.data.xy.XYSeries;
/*     */ import pal.statistics.NormalDistribution;
/*     */ 
/*     */ 
/*     */ public class TrainableNormal
/*     */   extends SkewNormal
/*     */ {
/*     */   public TrainableNormal clone()
/*     */   {
/*  21 */     return new TrainableNormal(this);
/*     */   }
/*     */   
/*  24 */   public TrainableNormal clone(double u) { return new TrainableNormal(this, u); }
/*     */   
/*     */   public XYSeries plotTheoretical(String name1, boolean cum)
/*     */   {
/*  28 */     XYSeries newD = new XYSeries(name1);
/*  29 */     double min = NormalDistribution.quantile(1.0E-7D, this.location, this.scale);
/*  30 */     double max = NormalDistribution.quantile(0.9999999D, this.location, this.scale);
/*  31 */     double incr = 0.001D;
/*  32 */     for (double j = min; j < max; j += incr) {
/*  33 */       double res = cum ? NormalDistribution.cdf(j, this.location, this.scale) : NormalDistribution.pdf(j, this.location, this.scale);
/*  34 */       if (res > 1.0E-11D) {
/*  35 */         newD.add(j, res);
/*     */       }
/*     */       else {
/*  38 */         System.err.println("res");
/*     */       }
/*     */     }
/*     */     
/*  42 */     return newD;
/*     */   }
/*     */   
/*     */   public int size() {
/*  46 */     return this.observations.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public TrainableNormal(double mean, double stddev, double round, double priorMod)
/*     */   {
/*  52 */     super(mean, stddev, 0.0D, new double[] { NormalDistribution.quantile(0.001D, mean, stddev), 0.001D, 0.0D }, new double[] { NormalDistribution.quantile(0.999D, mean, stddev), 1000.0D, 0.0D }, round, priorMod);
/*     */   }
/*     */   
/*     */ 
/*     */   public TrainableNormal(String string, int i)
/*     */   {
/*  58 */     this(0.0D, 0.0D, 0.0D, 1.0D);
/*  59 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public TrainableNormal(TrainableNormal trainableNormal) {
/*  63 */     super(trainableNormal);
/*     */   }
/*     */   
/*     */   public TrainableNormal(TrainableNormal skewNormal, double u)
/*     */   {
/*  68 */     this(skewNormal);
/*     */     
/*  70 */     this.location = NormalDistribution.quantile(Constants.rand.nextDouble(), this.location, 1.0D / u);
/*     */   }
/*     */   
/*     */ 
/*     */   public double dsn(double x, boolean log)
/*     */   {
/*  76 */     return log ? Math.log(probability(x)) : probability(x);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double cumulative(double arg0)
/*     */   {
/*  84 */     return NormalDistribution.cdf(arg0, this.location, this.scale);
/*     */   }
/*     */   
/*     */   public double inverse(double arg0)
/*     */   {
/*  89 */     return NormalDistribution.quantile(arg0, this.location, this.scale);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double probability(double arg0)
/*     */   {
/*  97 */     double prob = NormalDistribution.pdf(arg0, this.location, this.scale);
/*  98 */     return prob;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double average(double pseudo)
/*     */   {
/* 106 */     double tot = pseudo;
/* 107 */     double exp = pseudo * this.meanPrior[0];
/* 108 */     for (Iterator<Map.Entry<Double, Double>> it = this.observations.entrySet().iterator(); it.hasNext();) {
/* 109 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/* 110 */       double sc = ((Double)nxt.getKey()).doubleValue();
/* 111 */       double w = ((Double)nxt.getValue()).doubleValue();
/*     */       
/* 113 */       exp += sc * w;
/* 114 */       tot += w;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 120 */     return exp / tot;
/*     */   }
/*     */   
/*     */   public double variance(double mu, double pseudo) {
/* 124 */     double tot = pseudo;
/* 125 */     double exp = Math.pow(this.stddevPrior[0], 2.0D) * pseudo;
/* 126 */     double sc; double w; label154: for (Iterator<Map.Entry<Double, Double>> it = this.observations.entrySet().iterator(); it.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */         throw new RuntimeException("!! " + w + " " + sc))
/*     */     {
/* 127 */       Map.Entry<Double, Double> nxt = (Map.Entry)it.next();
/* 128 */       sc = Math.pow(((Double)nxt.getKey()).doubleValue() - mu, 2.0D);
/* 129 */       w = ((Double)nxt.getValue()).doubleValue();
/* 130 */       exp += sc * w;
/* 131 */       tot += w;
/* 132 */       if ((exp >= 0.0D) && (tot >= 0.0D)) break label154;
/*     */     }
/* 134 */     if (tot == 0.0D) {
/* 135 */       throw new RuntimeException(" " + this.observations + "\n" + this.observations);
/*     */     }
/*     */     
/* 138 */     return exp / tot;
/*     */   }
/*     */   
/* 141 */   public String getObsString() { if (this.observations.size() > 0) return mean();
/* 142 */     return "-";
/*     */   }
/*     */   
/*     */   public double mean() {
/* 146 */     double sum = 0.0D;
/* 147 */     double tot = sum();
/*     */     
/*     */ 
/* 150 */     for (Iterator<Double> it = this.observations.keySet().iterator(); it.hasNext();) {
/* 151 */       Double d = (Double)it.next();
/* 152 */       sum += ((Double)this.observations.get(d)).doubleValue() / tot;
/* 153 */       if (sum > 0.5D) { return d.doubleValue();
/*     */       }
/*     */     }
/* 156 */     return ((Double)this.observations.lastKey()).doubleValue(); }
/*     */   
/* 158 */   static final double cntThresh = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void addCount(double d, double w)
/*     */   {
/* 166 */     if (w > cntThresh) {
/* 167 */       addObservation(d, w);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void maximise(double pseudo, double pseudoSD, double pseudoSkew)
/*     */   {
/* 175 */     maximise(pseudo, pseudoSD, pseudoSkew, Constants.trainThresh());
/*     */   }
/*     */   
/*     */   public double[] getCount(double[] angle) {
/* 179 */     double[] res = new double[angle.length];
/* 180 */     for (int i = 0; i < res.length; i++)
/*     */     {
/* 182 */       res[i] = 
/* 183 */         (cumulative(angle[i]) - (i == 0 ? 0.0D : cumulative(angle[(i - 1)])));
/*     */     }
/* 185 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public void maximise(double pseudo, double pseudoSD, double pseudoSkew, double trainThresh)
/*     */   {
/* 191 */     double mean = pseudo < 100000.0D ? average(pseudo) : getMean();
/* 192 */     double variance = variance(mean, pseudoSD);
/*     */     
/*     */ 
/*     */ 
/* 196 */     updateVariance(variance);
/* 197 */     this.location = mean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void maximiseVariance()
/*     */   {
/* 207 */     double mean = getMean();
/* 208 */     double variance = variance(mean, 0.0D);
/* 209 */     if ((Double.isNaN(variance)) || (Double.isNaN(mean))) {
/* 210 */       throw new RuntimeException("!!");
/*     */     }
/* 212 */     Logger.global.info("after " + mean + " " + variance);
/* 213 */     updateVariance(variance);
/*     */   }
/*     */   
/*     */   public String toString() {
/* 217 */     Double[] d = { Double.valueOf(getMean()), Double.valueOf(getStdDev()), Double.valueOf(0.0D) };
/* 218 */     return 
/* 219 */       Format.sprintf("c(%5.2g, %5.2g, %5.2g)", d);
/*     */   }
/*     */   
/*     */   public void updateVariance(double var) {
/* 223 */     this.scale = Math.sqrt(var);
/*     */   }
/*     */   
/*     */   public double probability(double r, double offset) {
/* 227 */     return NormalDistribution.pdf(r - offset, this.location, this.scale);
/*     */   }
/*     */   
/*     */   public double rsn() {
/* 231 */     return inverse(Constants.rand.nextDouble());
/*     */   }
/*     */   
/*     */   public int getNumArguments() {
/* 235 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */   public void getCi(Double[] ci, double[] mm) {}
/*     */   
/*     */   public double[] getAngle(int num)
/*     */   {
/* 243 */     double[] res = new double[num];
/* 244 */     double incr = 1.0D / num;
/* 245 */     for (int i = 0; i < res.length; i++) {
/* 246 */       res[i] = (i == res.length - 1 ? inverse(0.99999999D) : 
/* 247 */         inverse((i + 1) * incr));
/*     */     }
/*     */     
/* 250 */     return res;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/TrainableNormal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */