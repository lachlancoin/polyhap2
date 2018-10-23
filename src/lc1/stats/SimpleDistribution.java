/*     */ package lc1.stats;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import lc1.dp.states.State;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleDistribution
/*     */   implements Serializable
/*     */ {
/*  20 */   public static final transient SimpleDistribution noOffset = new SimpleDistribution(new int[1], new double[] { 1.0D });
/*  21 */   public static final transient SimpleDistribution oneOffset = new SimpleDistribution(new int[] { -1, 0, 1 }, new double[] { 0.01D, 0.98D, 0.01D });
/*  22 */   public static transient double tolerance = 1.0E-4D;
/*     */   
/*  24 */   static transient Double zero = new Double(0.0D);
/*     */   public HashMap<Object, Double> dist;
/*     */   
/*  27 */   public static Object sample(Iterator<Map.Entry<Object, Double>> it) { double pr = Math.random();
/*  28 */     double sum = 0.0D;
/*  29 */     while (it.hasNext()) {
/*  30 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  31 */       sum += ((Double)entry.getValue()).doubleValue();
/*  32 */       if (sum >= pr) {
/*  33 */         return entry.getKey();
/*     */       }
/*     */     }
/*  36 */     throw new RuntimeException("did not find sample " + sum + " " + pr);
/*     */   }
/*     */   
/*     */   public SimpleDistribution() {
/*  40 */     this.dist = new HashMap();
/*     */   }
/*     */   
/*     */   public SimpleDistribution(int[] dist1, double[] d) {
/*  44 */     this();
/*  45 */     for (int i = 0; i < dist1.length; i++) {
/*  46 */       put(Integer.valueOf(dist1[i]), d[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public SimpleDistribution(Object[] o, double[] d)
/*     */   {
/*  52 */     this();
/*  53 */     for (int i = 0; i < o.length; i++)
/*  54 */       this.dist.put(o[i], Double.valueOf(d[i]));
/*     */   }
/*     */   
/*     */   public SimpleDistribution(SimpleDistribution dist1) {
/*  58 */     this(dist1, false, 0);
/*     */   }
/*     */   
/*     */   SimpleDistribution(SimpleDistribution dist1, boolean invert, int offset) {
/*  62 */     this();
/*  63 */     for (Iterator<Map.Entry<Object, Double>> it = dist1.iterator(); it.hasNext();) {
/*  64 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  65 */       Object key = entry.getKey();
/*  66 */       if (offset != 0) key = Integer.valueOf(((Integer)entry.getKey()).intValue() - offset);
/*  67 */       if (invert) key = Integer.valueOf(-1 * ((Integer)entry.getKey()).intValue());
/*  68 */       this.dist.put(key, (Double)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*  72 */   public SimpleDistribution(SimpleDistribution left, SimpleDistribution right) { this(left, true, 0);
/*  73 */     addAll(right);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  78 */   public void addAll(SimpleDistribution dist) { this.dist.putAll(dist.dist); }
/*     */   
/*     */   public void addCount(Object key, double d) {
/*  81 */     Double val = (Double)this.dist.get(key);
/*  82 */     this.dist.put(key, Double.valueOf(d + (val == null ? 0.0D : val.doubleValue())));
/*     */   }
/*     */   
/*     */   public void addCounts(SimpleDistribution distribution) {
/*  86 */     for (Iterator<Map.Entry<Object, Double>> it = distribution.dist.entrySet().iterator(); it.hasNext();) {
/*  87 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  88 */       Double val = (Double)this.dist.get(entry.getKey());
/*  89 */       this.dist.put(entry.getKey(), Double.valueOf(((Double)entry.getValue()).doubleValue() + (val == null ? 0.0D : val.doubleValue())));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCounts(StateDistribution distribution, List<State> states) {
/*  94 */     for (int j = 0; j < states.size(); j++) {
/*  95 */       double val1 = distribution.get(j).doubleValue();
/*  96 */       if (val1 != 0.0D) {
/*  97 */         Double val = Double.valueOf(get(states.get(j)));
/*  98 */         this.dist.put(states.get(j), Double.valueOf(val1 + (val == null ? 0.0D : val.doubleValue())));
/*     */       }
/*     */     } }
/*     */   
/* 102 */   public boolean different(SimpleDistribution d2) { for (Iterator it = this.dist.keySet().iterator(); it.hasNext();) {
/* 103 */       Object key = it.next();
/* 104 */       double num1 = ((Double)this.dist.get(key)).doubleValue();
/* 105 */       double num2 = ((Double)d2.dist.get(key)).doubleValue();
/* 106 */       double diff = num1 - num2;
/* 107 */       if ((num1 == 0.0D) || (num2 == 0.0D)) {
/* 108 */         return true;
/*     */       }
/* 110 */       if (Math.abs(diff) > 0.001D) {
/* 111 */         return true;
/*     */       }
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */   
/* 117 */   public double get(Object obj) { Double d = (Double)this.dist.get(obj);
/* 118 */     return d == null ? 0.0D : d.doubleValue();
/*     */   }
/*     */   
/* 121 */   public Map.Entry<Object, Double> getMax() { Iterator<Map.Entry<Object, Double>> it = iterator();
/* 122 */     Map.Entry<Object, Double> max = (Map.Entry)it.next();
/* 123 */     while (it.hasNext()) {
/* 124 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 125 */       if (((Double)entry.getValue()).doubleValue() > ((Double)max.getValue()).doubleValue()) {
/* 126 */         max = entry;
/*     */       }
/*     */     }
/* 129 */     return max;
/*     */   }
/*     */   
/*     */   public Iterator<Map.Entry<Object, Double>> iterator()
/*     */   {
/* 134 */     return this.dist.entrySet().iterator();
/*     */   }
/*     */   
/*     */   public double KLDistance(SimpleDistribution d2)
/*     */   {
/* 139 */     double sum = 0.0D;
/* 140 */     for (Iterator it = this.dist.keySet().iterator(); it.hasNext();) {
/* 141 */       Object key = it.next();
/* 142 */       Double num1 = (Double)this.dist.get(key);
/* 143 */       Double num2 = (Double)d2.dist.get(key);
/* 144 */       if (num1 == null) num1 = zero;
/* 145 */       if (num2 == null) num2 = zero;
/* 146 */       if (num1.doubleValue() != 0.0D) {
/* 147 */         sum += num1.doubleValue() * Math.log(num1.doubleValue() / num2.doubleValue());
/*     */       }
/*     */     }
/* 150 */     return sum;
/*     */   }
/*     */   
/*     */   public int length()
/*     */   {
/* 155 */     return this.dist.size();
/*     */   }
/*     */   
/*     */   public Object mostLikely() {
/* 159 */     Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator();
/* 160 */     Map.Entry<Object, Double> best = (Map.Entry)it.next();
/* 161 */     while (it.hasNext()) {
/* 162 */       Map.Entry<Object, Double> nxt = (Map.Entry)it.next();
/* 163 */       if (((Double)nxt.getValue()).doubleValue() > ((Double)best.getValue()).doubleValue()) {
/* 164 */         best = nxt;
/*     */       }
/*     */     }
/* 167 */     return best.getKey();
/*     */   }
/*     */   
/*     */   public void multiplyValues(double sum) {
/* 171 */     for (Iterator<Map.Entry<Object, Double>> it = iterator(); it.hasNext();) {
/* 172 */       Map.Entry entry = (Map.Entry)it.next();
/* 173 */       entry.setValue(Double.valueOf(((Number)entry.getValue()).doubleValue() * sum));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void normalise()
/*     */   {
/* 181 */     double sum = sum();
/* 182 */     if (sum == 0.0D) throw new RuntimeException("!!");
/* 183 */     normalise(sum);
/*     */   }
/*     */   
/*     */   public void normalise(double sum1) {
/* 187 */     for (Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator(); it.hasNext();) {
/* 188 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 189 */       entry.setValue(Double.valueOf(((Double)entry.getValue()).doubleValue() / sum1));
/*     */     }
/*     */   }
/*     */   
/*     */   public void put(Object obj, double d) {
/* 194 */     this.dist.put(obj, Double.valueOf(d));
/*     */   }
/*     */   
/*     */ 
/* 198 */   public Object sample() { return sample(this.dist.entrySet().iterator()); }
/*     */   
/*     */   public void setRandom(double u, boolean restart) {
/* 201 */     if (Math.abs(1.0D - sum()) > 0.001D) throw new RuntimeException("not valid");
/* 202 */     Double[] d = new Double[this.dist.values().size()];
/* 203 */     if (restart) Arrays.fill(d, Double.valueOf(1.0D / d.length)); else
/* 204 */       this.dist.values().toArray(d);
/* 205 */     Dirichlet dir = new Dirichlet(d, u);
/* 206 */     Double[] res = dir.sample();
/* 207 */     int i = 0;
/* 208 */     for (Iterator<Map.Entry<Object, Double>> it = iterator(); it.hasNext(); i++) {
/* 209 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*     */       
/* 211 */       entry.setValue(res[i]);
/*     */     }
/* 213 */     normalise();
/*     */   }
/*     */   
/* 216 */   public int size() { return this.dist.size(); }
/*     */   
/*     */   public double sum() {
/* 219 */     return sum(Object.class);
/*     */   }
/*     */   
/*     */   public double sum(Class clazz) {
/* 223 */     double sum1 = 0.0D;
/* 224 */     for (Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator(); it.hasNext();) {
/* 225 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 226 */       if (clazz.isInstance(entry.getKey()))
/*     */       {
/* 228 */         sum1 += ((Double)entry.getValue()).doubleValue();
/*     */       }
/*     */     }
/* 231 */     return sum1;
/*     */   }
/*     */   
/*     */   public void removeSmallCounts(double d) {
/* 235 */     for (Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator(); it.hasNext();) {
/* 236 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 237 */       if (((Double)entry.getValue()).doubleValue() < d) it.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 242 */     StringBuffer sb = new StringBuffer("{");
/* 243 */     boolean first = true;
/* 244 */     for (Iterator<Map.Entry<Object, Double>> it = iterator(); it.hasNext();) {
/* 245 */       Map.Entry<Object, Double> id = (Map.Entry)it.next();
/* 246 */       if (((Double)id.getValue()).doubleValue() >= 1.0E-5D) {
/* 247 */         if (!first) sb.append(","); else
/* 248 */           first = false;
/* 249 */         sb.append(id.getKey());
/* 250 */         sb.append("->");
/* 251 */         sb.append(Double.isNaN(((Double)id.getValue()).doubleValue()) ? "NaN" : Format.sprintf("%5.3f", new Object[] { id.getValue() }));
/*     */       } }
/* 253 */     sb.append("}");
/* 254 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void validate() {
/* 258 */     if (Math.abs(1.0D - sum()) > tolerance) throw new RuntimeException("sum is wrong " + Arrays.asList(new HashMap[] { this.dist }) + " " + sum());
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/SimpleDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */