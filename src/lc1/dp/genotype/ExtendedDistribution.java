/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import lc1.dp.SimpleDistribution;
/*     */ import lc1.dp.State;
/*     */ 
/*     */ public class ExtendedDistribution extends PseudoDistribution implements java.io.Serializable
/*     */ {
/*     */   public final SimpleDistribution probs;
/*     */   public SimpleDistribution pseudo;
/*     */   public final SimpleDistribution counts;
/*     */   int[] nonZero;
/*     */   
/*     */   public ExtendedDistribution(ExtendedDistribution d_init, ExtendedDistribution d_pseudo)
/*     */   {
/*  19 */     this.probs = new SimpleDistribution(d_init.probs);
/*  20 */     this.counts = new SimpleDistribution();
/*  21 */     this.pseudo = new SimpleDistribution(d_pseudo.probs);
/*     */   }
/*     */   
/*  24 */   public String toString() { StringBuffer sb = new StringBuffer("probs ");
/*  25 */     sb.append(this.probs.toString());
/*  26 */     sb.append("\n");
/*  27 */     sb.append("counts ");
/*  28 */     sb.append(this.counts.toString());
/*  29 */     sb.append("\n");
/*  30 */     sb.append("pseudo ");
/*  31 */     sb.append(this.pseudo.toString());
/*  32 */     sb.append("\n");
/*  33 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int[] getNonZero()
/*     */   {
/*  40 */     if (this.nonZero == null) {
/*  41 */       Set s = this.probs.dist.keySet();
/*  42 */       this.nonZero = new int[s.size()];
/*  43 */       int i = 0;
/*  44 */       for (Iterator it = s.iterator(); it.hasNext(); i++) {
/*  45 */         this.nonZero[i] = ((Integer)it.next()).intValue();
/*     */       }
/*     */     }
/*  48 */     return this.nonZero;
/*     */   }
/*     */   
/*     */   public void setPseudoCounts() {
/*  52 */     Double d = Double.valueOf(1.0D / this.probs.size());
/*  53 */     for (Iterator it = this.probs.dist.keySet().iterator(); it.hasNext();) {
/*  54 */       this.pseudo.put(it.next(), d.doubleValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void put(Object obj, Double d)
/*     */   {
/*  63 */     this.probs.put(obj, d.doubleValue());
/*     */   }
/*     */   
/*     */   public void validate() {
/*  67 */     this.probs.validate();
/*     */   }
/*     */   
/*  70 */   public Double get(Object obj) { return Double.valueOf(this.probs.get(obj)); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   ExtendedDistribution(SimpleDistribution dist, SimpleDistribution pseudo)
/*     */   {
/*  77 */     this.probs = dist;
/*  78 */     this.counts = new SimpleDistribution();
/*  79 */     this.pseudo = pseudo;
/*     */   }
/*     */   
/*     */   ExtendedDistribution(SimpleDistribution dist, boolean useAsPseudo) {
/*  83 */     this(dist, useAsPseudo ? new SimpleDistribution(dist) : null);
/*     */   }
/*     */   
/*  86 */   public ExtendedDistribution() { this(new SimpleDistribution(), new SimpleDistribution()); }
/*     */   
/*     */   public void initialise()
/*     */   {
/*  90 */     this.counts.dist.clear();
/*     */   }
/*     */   
/*  93 */   public void addCounts(StateDistribution observed, java.util.List<State> states) { this.counts.addCounts(observed, states); }
/*     */   
/*     */   public void transfer(double pseudocountWeight) {
/*  96 */     for (Iterator<Map.Entry<Object, Double>> it = this.probs.iterator(); it.hasNext();) {
/*  97 */       Map.Entry<Object, Double> entry = (Map.Entry)it.next();
/*  98 */       State nxt = (State)entry.getKey();
/*  99 */       Double total1 = Double.valueOf(this.counts.get(nxt));
/* 100 */       Double total2 = Double.valueOf((this.pseudo == null) || (pseudocountWeight == 0.0D) ? 0.0D : this.pseudo.get(nxt) * pseudocountWeight);
/* 101 */       double total = (total1 == null ? 0.0D : total1.doubleValue()) + (total2 == null ? 0.0D : total2.doubleValue());
/* 102 */       if (total != 0.0D) {
/* 103 */         entry.setValue(Double.valueOf(total));
/*     */       }
/*     */     }
/* 106 */     this.probs.normalise();
/*     */   }
/*     */   
/*     */   public double evaluate(double pseudocountWeight) {
/* 110 */     transfer(pseudocountWeight);
/* 111 */     return logProb();
/*     */   }
/*     */   
/*     */   public double logProb() {
/* 115 */     double logL = 0.0D;
/* 116 */     for (Iterator<Map.Entry<Object, Double>> it = this.counts.iterator(); it.hasNext();) {
/* 117 */       Map.Entry<Object, Double> count_i = (Map.Entry)it.next();
/* 118 */       logL += ((Double)count_i.getValue()).doubleValue() * Math.log(this.probs.get(count_i.getKey()));
/*     */     }
/* 120 */     return logL;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/ExtendedDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */