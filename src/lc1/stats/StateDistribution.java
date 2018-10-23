/*     */ package lc1.stats;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import lc1.dp.states.State;
/*     */ 
/*     */ public class StateDistribution
/*     */   implements Serializable
/*     */ {
/*     */   public double[] dist;
/*     */   
/*     */   public StateDistribution(int modelLength)
/*     */   {
/*  15 */     this.dist = new double[modelLength];
/*  16 */     Arrays.fill(this.dist, 0.0D);
/*     */   }
/*     */   
/*     */   public StateDistribution(StateDistribution dist1) {
/*  20 */     this.dist = new double[dist1.dist.length];
/*  21 */     System.arraycopy(dist1.dist, 0, this.dist, 0, this.dist.length);
/*     */   }
/*     */   
/*     */   public String toString() {
/*  25 */     StringBuffer sb = new StringBuffer();
/*  26 */     Double[] d = new Double[this.dist.length];
/*  27 */     for (int i = 0; i < d.length; i++) {
/*  28 */       d[i] = Double.valueOf(this.dist[i]);
/*  29 */       sb.append("%5.3g  ");
/*     */     }
/*  31 */     return Format.sprintf(sb.toString(), d);
/*     */   }
/*     */   
/*     */   public void multiplyValues(double sum) {
/*  35 */     for (int j = 0; j < this.dist.length; j++) {
/*  36 */       this.dist[j] *= sum;
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
/*  54 */     if (distribution == null) return;
/*  55 */     if (distribution.dist.length != this.dist.length) throw new RuntimeException("!!");
/*  56 */     for (int j = 0; j < this.dist.length; j++)
/*     */     {
/*     */ 
/*     */ 
/*  60 */       this.dist[j] += distribution.dist[j];
/*     */     }
/*     */   }
/*     */   
/*     */   public void setLength(int length) {
/*  65 */     double[] newdist = new double[length];
/*  66 */     System.arraycopy(this.dist, 0, newdist, 0, this.dist.length);
/*  67 */     this.dist = newdist;
/*     */   }
/*     */   
/*  70 */   public void put(State st, double d) { this.dist[st.getIndex()] = d; }
/*     */   
/*     */ 
/*  73 */   public void put(int st, double d) { this.dist[st] = d; }
/*     */   
/*     */   public Double get(State state_j) {
/*  76 */     Double res = Double.valueOf(this.dist[state_j.getIndex()]);
/*  77 */     if (res == null) return this.zero;
/*  78 */     return res;
/*     */   }
/*     */   
/*  81 */   public Double get(int j) { Double res = Double.valueOf(this.dist[j]);
/*  82 */     if (res == null) return this.zero;
/*  83 */     return res;
/*     */   }
/*     */   
/*     */   public void validate() {
/*  87 */     if (Math.abs(1.0D - sum()) > tolerance) {
/*  88 */       if (Math.abs(1.0D - sum()) > 0.001D) {
/*  89 */         throw new RuntimeException("sum is wrong " + Arrays.asList(new double[][] { this.dist }) + " " + sum());
/*     */       }
/*  91 */       multiplyValues(1.0D / sum());
/*     */     }
/*     */   }
/*     */   
/*     */   public int nonZero() {
/*  96 */     int nonZero = 0;
/*  97 */     for (int i = 0; i < this.dist.length; i++) {
/*  98 */       if (this.dist[i] > 0.0D) nonZero++;
/*     */     }
/* 100 */     return nonZero; }
/*     */   
/* 102 */   public static double tolerance = 0.001D;
/*     */   
/*     */   public double sum() {
/* 105 */     double sum = 0.0D;
/* 106 */     for (int i = 0; i < this.dist.length; i++) {
/* 107 */       Double res = Double.valueOf(this.dist[i]);
/* 108 */       if (res != null) sum += res.doubleValue();
/*     */     }
/* 110 */     return sum;
/*     */   }
/*     */   
/* 113 */   public void normalise() { double sum = sum();
/* 114 */     for (int i = 0; i < this.dist.length; i++)
/*     */     {
/* 116 */       this.dist[i] /= sum; }
/*     */   }
/*     */   
/*     */   public void setRandom(double u) {
/* 120 */     if (Math.abs(1.0D - sum()) > 0.001D) throw new RuntimeException("not valid");
/* 121 */     Dirichlet dir = new Dirichlet(this.dist, u);
/* 122 */     Double[] res = dir.sample();
/* 123 */     for (int i = 0; i < this.dist.length; i++) {
/* 124 */       this.dist[i] = res[i].doubleValue();
/*     */     }
/* 126 */     normalise(); }
/*     */   
/* 128 */   Double zero = Double.valueOf(0.0D);
/*     */   
/* 130 */   public double KLDistance(StateDistribution d2) { double sum = 0.0D;
/* 131 */     for (int i = 0; i < this.dist.length; i++) {
/* 132 */       Double num1 = Double.valueOf(this.dist[i]);
/* 133 */       Double num2 = Double.valueOf(d2.dist[i]);
/* 134 */       if (num1 == null) num1 = this.zero;
/* 135 */       if (num2 == null) num2 = this.zero;
/* 136 */       if (num1.doubleValue() != 0.0D) {
/* 137 */         sum += num1.doubleValue() * Math.log(num1.doubleValue() / num2.doubleValue());
/*     */       }
/*     */     }
/* 140 */     return sum;
/*     */   }
/*     */   
/* 143 */   public void addCount(Object key, Double value) { int ind = ((Integer)key).intValue();
/*     */     
/*     */ 
/* 146 */     this.dist[ind] += value.doubleValue();
/*     */   }
/*     */   
/*     */   public int getMax() {
/* 150 */     int max = 0;
/* 151 */     for (int j = 1; j < this.dist.length; j++) {
/* 152 */       if (this.dist[j] > max) {
/* 153 */         max = j;
/*     */       }
/*     */     }
/* 156 */     return max;
/*     */   }
/*     */   
/* 159 */   public synchronized void add(int j, double sc) { this.dist[j] += sc; }
/*     */   
/*     */   public void reset() {
/* 162 */     Arrays.fill(this.dist, 0.0D);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/StateDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */