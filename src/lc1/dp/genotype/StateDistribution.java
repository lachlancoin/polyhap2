/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import lc1.dp.Dirichlet;
/*     */ import lc1.dp.State;
/*     */ 
/*     */ public class StateDistribution
/*     */   implements Serializable
/*     */ {
/*     */   public double[] dist;
/*     */   
/*     */   public StateDistribution(int modelLength)
/*     */   {
/*  16 */     this.dist = new double[modelLength];
/*  17 */     Arrays.fill(this.dist, 0.0D);
/*     */   }
/*     */   
/*     */   public StateDistribution(StateDistribution dist1) {
/*  21 */     this.dist = new double[dist1.dist.length];
/*  22 */     System.arraycopy(dist1.dist, 0, this.dist, 0, this.dist.length);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  26 */     StringBuffer sb = new StringBuffer();
/*  27 */     Double[] d = new Double[this.dist.length];
/*  28 */     for (int i = 0; i < d.length; i++) {
/*  29 */       d[i] = Double.valueOf(this.dist[i]);
/*  30 */       sb.append("%5.3g  ");
/*     */     }
/*  32 */     return Format.sprintf(sb.toString(), d);
/*     */   }
/*     */   
/*     */   public void multiplyValues(double sum) {
/*  36 */     for (int j = 0; j < this.dist.length; j++) {
/*  37 */       this.dist[j] *= sum;
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
/*     */   public void addCounts(StateDistribution distribution)
/*     */   {
/*  55 */     if (distribution == null) return;
/*  56 */     if (distribution.dist.length != this.dist.length) throw new RuntimeException("!!");
/*  57 */     for (int j = 0; j < this.dist.length; j++)
/*     */     {
/*     */ 
/*     */ 
/*  61 */       this.dist[j] += distribution.dist[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLength(int length) {
/*  66 */     double[] newdist = new double[length];
/*  67 */     System.arraycopy(this.dist, 0, newdist, 0, this.dist.length);
/*  68 */     this.dist = newdist;
/*     */   }
/*     */   
/*  71 */   public void put(State st, double d) { this.dist[st.getIndex()] = d; }
/*     */   
/*     */ 
/*  74 */   public void put(int st, double d) { this.dist[st] = d; }
/*     */   
/*     */   public Double get(State state_j) {
/*  77 */     Double res = Double.valueOf(this.dist[state_j.getIndex()]);
/*  78 */     if (res == null) return this.zero;
/*  79 */     return res;
/*     */   }
/*     */   
/*  82 */   public Double get(int j) { Double res = Double.valueOf(this.dist[j]);
/*  83 */     if (res == null) return this.zero;
/*  84 */     return res;
/*     */   }
/*     */   
/*     */   public void validate() {
/*  88 */     if (Math.abs(1.0D - sum()) > tolerance) {
/*  89 */       if (Math.abs(1.0D - sum()) > 0.1D) {
/*  90 */         throw new RuntimeException("sum is wrong " + Arrays.asList(new double[][] { this.dist }) + " " + sum());
/*     */       }
/*  92 */       multiplyValues(1.0D / sum());
/*     */     }
/*     */   }
/*     */   
/*     */   public int nonZero() {
/*  97 */     int nonZero = 0;
/*  98 */     for (int i = 0; i < this.dist.length; i++) {
/*  99 */       if (this.dist[i] > 0.0D) nonZero++;
/*     */     }
/* 101 */     return nonZero; }
/*     */   
/* 103 */   public static double tolerance = 0.001D;
/*     */   
/*     */   public double sum() {
/* 106 */     double sum = 0.0D;
/* 107 */     for (int i = 0; i < this.dist.length; i++) {
/* 108 */       Double res = Double.valueOf(this.dist[i]);
/* 109 */       if (res != null) sum += res.doubleValue();
/*     */     }
/* 111 */     return sum;
/*     */   }
/*     */   
/* 114 */   public void normalise() { double sum = sum();
/* 115 */     for (int i = 0; i < this.dist.length; i++)
/*     */     {
/* 117 */       this.dist[i] /= sum; }
/*     */   }
/*     */   
/*     */   public void setRandom(double u) {
/* 121 */     if (Math.abs(1.0D - sum()) > 0.001D) throw new RuntimeException("not valid");
/* 122 */     Dirichlet dir = new Dirichlet(this.dist, u);
/* 123 */     Double[] res = dir.sample();
/* 124 */     for (int i = 0; i < this.dist.length; i++) {
/* 125 */       this.dist[i] = res[i].doubleValue();
/*     */     }
/* 127 */     normalise(); }
/*     */   
/* 129 */   Double zero = Double.valueOf(0.0D);
/*     */   
/* 131 */   public double KLDistance(StateDistribution d2) { double sum = 0.0D;
/* 132 */     for (int i = 0; i < this.dist.length; i++) {
/* 133 */       Double num1 = Double.valueOf(this.dist[i]);
/* 134 */       Double num2 = Double.valueOf(d2.dist[i]);
/* 135 */       if (num1 == null) num1 = this.zero;
/* 136 */       if (num2 == null) num2 = this.zero;
/* 137 */       if (num1.doubleValue() != 0.0D) {
/* 138 */         sum += num1.doubleValue() * Math.log(num1.doubleValue() / num2.doubleValue());
/*     */       }
/*     */     }
/* 141 */     return sum;
/*     */   }
/*     */   
/* 144 */   public void addCount(Object key, Double value) { int ind = ((Integer)key).intValue();
/*     */     
/*     */ 
/* 147 */     this.dist[ind] += value.doubleValue();
/*     */   }
/*     */   
/*     */   public int getMax() {
/* 151 */     int max = 0;
/* 152 */     for (int j = 1; j < this.dist.length; j++) {
/* 153 */       if (this.dist[j] > max) {
/* 154 */         max = j;
/*     */       }
/*     */     }
/* 157 */     return max;
/*     */   }
/*     */   
/* 160 */   public void add(int j, double sc) { this.dist[j] += sc; }
/*     */   
/*     */   public void reset() {
/* 163 */     Arrays.fill(this.dist, 0.0D);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/StateDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */