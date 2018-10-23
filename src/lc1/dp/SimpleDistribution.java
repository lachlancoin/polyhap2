/*     */ package lc1.dp;
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
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleDistribution
/*     */   implements Serializable
/*     */ {
/*  21 */   public static final transient SimpleDistribution noOffset = new SimpleDistribution(new int[1], new double[] { 1.0D });
/*  22 */   public static final transient SimpleDistribution oneOffset = new SimpleDistribution(new int[] { -1, 0, 1 }, new double[] { 0.01D, 0.98D, 0.01D });
/*  23 */   public static transient double tolerance = 0.001D;
/*     */   
/*  25 */   static transient Double zero = new Double(0.0D);
/*     */   public HashMap<Object, Double> dist;
/*     */   
/*  28 */   public static Object sample(Iterator<Map.Entry<Object, Double>> it) { double pr = Math.random();
/*  29 */     double sum = 0.0D;
/*  30 */     while (it.hasNext()) {
/*  31 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  32 */       sum += ((Double)entry.getValue()).doubleValue();
/*  33 */       if (sum >= pr) {
/*  34 */         return entry.getKey();
/*     */       }
/*     */     }
/*  37 */     throw new RuntimeException("did not find sample " + sum + " " + pr);
/*     */   }
/*     */   
/*     */   public SimpleDistribution() {
/*  41 */     this.dist = new HashMap();
/*     */   }
/*     */   
/*     */   public SimpleDistribution(int[] dist1, double[] d) {
/*  45 */     this();
/*  46 */     for (int i = 0; i < dist1.length; i++) {
/*  47 */       put(Integer.valueOf(dist1[i]), d[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public SimpleDistribution(Object[] o, double[] d)
/*     */   {
/*  53 */     this();
/*  54 */     for (int i = 0; i < o.length; i++)
/*  55 */       this.dist.put(o[i], Double.valueOf(d[i]));
/*     */   }
/*     */   
/*     */   public SimpleDistribution(SimpleDistribution dist1) {
/*  59 */     this(dist1, false, 0);
/*     */   }
/*     */   
/*     */   SimpleDistribution(SimpleDistribution dist1, boolean invert, int offset) {
/*  63 */     this();
/*  64 */     for (Iterator<Map.Entry<Object, Double>> it = dist1.iterator(); it.hasNext();) {
/*  65 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  66 */       Object key = entry.getKey();
/*  67 */       if (offset != 0) key = Integer.valueOf(((Integer)entry.getKey()).intValue() - offset);
/*  68 */       if (invert) key = Integer.valueOf(-1 * ((Integer)entry.getKey()).intValue());
/*  69 */       this.dist.put(key, (Double)entry.getValue());
/*     */     }
/*     */   }
/*     */   
/*  73 */   public SimpleDistribution(SimpleDistribution left, SimpleDistribution right) { this(left, true, 0);
/*  74 */     addAll(right);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  79 */   public void addAll(SimpleDistribution dist) { this.dist.putAll(dist.dist); }
/*     */   
/*     */   public void addCount(Object key, double d) {
/*  82 */     Double val = (Double)this.dist.get(key);
/*  83 */     this.dist.put(key, Double.valueOf(d + (val == null ? 0.0D : val.doubleValue())));
/*     */   }
/*     */   
/*     */   public void addCounts(SimpleDistribution distribution) {
/*  87 */     for (Iterator<Map.Entry<Object, Double>> it = distribution.dist.entrySet().iterator(); it.hasNext();) {
/*  88 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  89 */       Double val = (Double)this.dist.get(entry.getKey());
/*  90 */       this.dist.put(entry.getKey(), Double.valueOf(((Double)entry.getValue()).doubleValue() + (val == null ? 0.0D : val.doubleValue())));
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCounts(StateDistribution distribution, List<State> states) {
/*  95 */     for (int j = 0; j < states.size(); j++) {
/*  96 */       double val1 = distribution.get(j).doubleValue();
/*  97 */       if (val1 != 0.0D) {
/*  98 */         Double val = Double.valueOf(get(states.get(j)));
/*  99 */         this.dist.put(states.get(j), Double.valueOf(val1 + (val == null ? 0.0D : val.doubleValue())));
/*     */       }
/*     */     } }
/*     */   
/* 103 */   public boolean different(SimpleDistribution d2) { for (Iterator it = this.dist.keySet().iterator(); it.hasNext();) {
/* 104 */       Object key = it.next();
/* 105 */       double num1 = ((Double)this.dist.get(key)).doubleValue();
/* 106 */       double num2 = ((Double)d2.dist.get(key)).doubleValue();
/* 107 */       double diff = num1 - num2;
/* 108 */       if ((num1 == 0.0D) || (num2 == 0.0D)) {
/* 109 */         return true;
/*     */       }
/* 111 */       if (Math.abs(diff) > 0.001D) {
/* 112 */         return true;
/*     */       }
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */   
/* 118 */   public double get(Object obj) { Double d = (Double)this.dist.get(obj);
/* 119 */     return d == null ? 0.0D : d.doubleValue();
/*     */   }
/*     */   
/* 122 */   public Map.Entry<Object, Double> getMax() { Iterator<Map.Entry<Object, Double>> it = iterator();
/* 123 */     Map.Entry<Object, Double> max = (Map.Entry)it.next();
/* 124 */     while (it.hasNext()) {
/* 125 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 126 */       if (((Double)entry.getValue()).doubleValue() > ((Double)max.getValue()).doubleValue()) {
/* 127 */         max = entry;
/*     */       }
/*     */     }
/* 130 */     return max;
/*     */   }
/*     */   
/*     */   public Iterator<Map.Entry<Object, Double>> iterator()
/*     */   {
/* 135 */     return this.dist.entrySet().iterator();
/*     */   }
/*     */   
/*     */   public double KLDistance(SimpleDistribution d2)
/*     */   {
/* 140 */     double sum = 0.0D;
/* 141 */     for (Iterator it = this.dist.keySet().iterator(); it.hasNext();) {
/* 142 */       Object key = it.next();
/* 143 */       Double num1 = (Double)this.dist.get(key);
/* 144 */       Double num2 = (Double)d2.dist.get(key);
/* 145 */       if (num1 == null) num1 = zero;
/* 146 */       if (num2 == null) num2 = zero;
/* 147 */       if (num1.doubleValue() != 0.0D) {
/* 148 */         sum += num1.doubleValue() * Math.log(num1.doubleValue() / num2.doubleValue());
/*     */       }
/*     */     }
/* 151 */     return sum;
/*     */   }
/*     */   
/*     */   public int length()
/*     */   {
/* 156 */     return this.dist.size();
/*     */   }
/*     */   
/*     */   public Object mostLikely() {
/* 160 */     Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator();
/* 161 */     Map.Entry<Object, Double> best = (Map.Entry)it.next();
/* 162 */     while (it.hasNext()) {
/* 163 */       Map.Entry<Object, Double> nxt = (Map.Entry)it.next();
/* 164 */       if (((Double)nxt.getValue()).doubleValue() > ((Double)best.getValue()).doubleValue()) {
/* 165 */         best = nxt;
/*     */       }
/*     */     }
/* 168 */     return best.getKey();
/*     */   }
/*     */   
/*     */   public void multiplyValues(double sum) {
/* 172 */     for (Iterator<Map.Entry<Object, Double>> it = iterator(); it.hasNext();) {
/* 173 */       Map.Entry entry = (Map.Entry)it.next();
/* 174 */       entry.setValue(Double.valueOf(((Number)entry.getValue()).doubleValue() * sum));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void normalise()
/*     */   {
/* 182 */     double sum = sum();
/* 183 */     if (sum == 0.0D) throw new RuntimeException("!!");
/* 184 */     normalise(sum);
/*     */   }
/*     */   
/*     */   public void normalise(double sum1) {
/* 188 */     for (Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator(); it.hasNext();) {
/* 189 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 190 */       entry.setValue(Double.valueOf(((Double)entry.getValue()).doubleValue() / sum1));
/*     */     }
/*     */   }
/*     */   
/*     */   public void put(Object obj, double d) {
/* 195 */     this.dist.put(obj, Double.valueOf(d));
/*     */   }
/*     */   
/*     */ 
/* 199 */   public Object sample() { return sample(this.dist.entrySet().iterator()); }
/*     */   
/*     */   public void setRandom(double u, boolean restart) {
/* 202 */     if (Math.abs(1.0D - sum()) > 0.001D) throw new RuntimeException("not valid");
/* 203 */     Double[] d = new Double[this.dist.values().size()];
/* 204 */     if (restart) Arrays.fill(d, Double.valueOf(1.0D / d.length)); else
/* 205 */       this.dist.values().toArray(d);
/* 206 */     Dirichlet dir = new Dirichlet(d, u);
/* 207 */     Double[] res = dir.sample();
/* 208 */     int i = 0;
/* 209 */     for (Iterator<Map.Entry<Object, Double>> it = iterator(); it.hasNext(); i++) {
/* 210 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*     */       
/* 212 */       entry.setValue(res[i]);
/*     */     }
/* 214 */     normalise();
/*     */   }
/*     */   
/* 217 */   public int size() { return this.dist.size(); }
/*     */   
/*     */   public double sum() {
/* 220 */     return sum(Object.class);
/*     */   }
/*     */   
/*     */   public double sum(Class clazz) {
/* 224 */     double sum1 = 0.0D;
/* 225 */     for (Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator(); it.hasNext();) {
/* 226 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 227 */       if (clazz.isInstance(entry.getKey()))
/*     */       {
/* 229 */         sum1 += ((Double)entry.getValue()).doubleValue();
/*     */       }
/*     */     }
/* 232 */     return sum1;
/*     */   }
/*     */   
/*     */   public void removeSmallCounts(double d) {
/* 236 */     for (Iterator<Map.Entry<Object, Double>> it = this.dist.entrySet().iterator(); it.hasNext();) {
/* 237 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/* 238 */       if (((Double)entry.getValue()).doubleValue() < d) it.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString() {
/* 243 */     StringBuffer sb = new StringBuffer("{");
/* 244 */     boolean first = true;
/* 245 */     for (Iterator<Map.Entry<Object, Double>> it = iterator(); it.hasNext();) {
/* 246 */       Map.Entry<Object, Double> id = (Map.Entry)it.next();
/* 247 */       if (((Double)id.getValue()).doubleValue() >= 1.0E-5D) {
/* 248 */         if (!first) sb.append(","); else
/* 249 */           first = false;
/* 250 */         sb.append(id.getKey());
/* 251 */         sb.append("->");
/* 252 */         sb.append(Double.isNaN(((Double)id.getValue()).doubleValue()) ? "NaN" : Format.sprintf("%5.3f", new Object[] { id.getValue() }));
/*     */       } }
/* 254 */     sb.append("}");
/* 255 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void validate() {
/* 259 */     if (Math.abs(1.0D - sum()) > tolerance) throw new RuntimeException("sum is wrong " + Arrays.asList(new HashMap[] { this.dist }) + " " + sum());
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/SimpleDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */