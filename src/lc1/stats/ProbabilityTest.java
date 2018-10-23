/*     */ package lc1.stats;
/*     */ 
/*     */ import JSci.maths.statistics.ProbabilityDistribution;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ProbabilityTest
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*  13 */     ProbabilityTest pt = new ProbabilityTest();
/*  14 */     double bef = Double.NEGATIVE_INFINITY;
/*  15 */     for (int i = 0; i < 1000; i++) {
/*  16 */       System.err.println(i);
/*  17 */       double n = pt.infer();
/*  18 */       if (n - bef < 0.01D) break;
/*  19 */       bef = n;
/*     */     }
/*     */   }
/*     */   
/*  23 */   List<ProbabilityDistribution> dist = new ArrayList();
/*     */   
/*  25 */   List<Double> samples = new ArrayList();
/*     */   
/*  27 */   static int lim = 10000;
/*  28 */   static int grid = 100000;
/*     */   double[] prob;
/*     */   SimpleExtendedDistribution mix;
/*     */   
/*     */   ProbabilityTest() {
/*  33 */     setUp1();
/*     */   }
/*     */   
/*     */   public void setUp() {
/*  37 */     this.dist.add(new SkewNormal(0.0D, 0.02D, 1.0E10D, -10.0D, 10.0D, grid, 1.0D));
/*  38 */     this.dist.add(new SkewNormal(1.0D, 0.02D, -1.0E10D, -10.0D, 10.0D, grid, 1.0D));
/*  39 */     this.dist.add(new TrainableNormal(0.28D, 0.02D, grid, 1.0D));
/*  40 */     this.dist.add(new TrainableNormal(0.33D, 0.02D, grid, 1.0D));
/*  41 */     this.dist.add(new TrainableNormal(0.48D, 0.12D, grid, 1.0D));
/*  42 */     this.dist.add(new TrainableNormal(0.65D, 0.02D, grid, 1.0D));
/*  43 */     this.dist.add(new TrainableNormal(0.78D, 0.02D, grid, 1.0D));
/*  44 */     this.mix = new SimpleExtendedDistribution1(this.dist.size());
/*  45 */     Arrays.fill(this.mix.counts, 1.0D);
/*  46 */     this.mix.counts[0] = 2.0D;
/*  47 */     this.mix.counts[1] = 2.0D;
/*  48 */     this.mix.counts[2] = 2.0D;
/*  49 */     this.mix.transfer(0.0D);
/*  50 */     this.mix.initialise();
/*     */     
/*  52 */     System.err.println("mix " + this.mix.toString());
/*     */     
/*     */ 
/*  55 */     for (int j = 0; j < lim; j++) {
/*  56 */       SkewNormal dist_i = (SkewNormal)this.dist.get((int)this.mix.sample());
/*  57 */       double x = dist_i.rsn();
/*     */       
/*  59 */       this.samples.add(Double.valueOf(x));
/*     */     }
/*  61 */     this.mix = new SimpleExtendedDistribution1(this.dist.size());
/*     */     
/*  63 */     this.dist.set(0, new SkewNormal(0.0D, 0.2D, 1.0E10D, -10.0D, 10.0D, grid, 1.0D));
/*  64 */     this.dist.set(1, new SkewNormal(1.0D, 0.2D, -1.0E10D, -10.0D, 10.0D, grid, 1.0D));
/*  65 */     this.dist.set(2, new TrainableNormal(0.25D, 0.02D, grid, 1.0D));
/*  66 */     this.dist.set(3, new TrainableNormal(0.33D, 0.02D, grid, 1.0D));
/*  67 */     this.dist.set(4, new TrainableNormal(0.5D, 0.02D, grid, 1.0D));
/*  68 */     this.dist.set(5, new TrainableNormal(0.66D, 0.02D, grid, 1.0D));
/*  69 */     this.dist.set(6, new TrainableNormal(0.75D, 0.02D, grid, 1.0D));
/*  70 */     this.prob = new double[this.dist.size()];
/*  71 */     System.err.println("old params ");
/*  72 */     for (int j = 0; j < this.prob.length; j++) {
/*  73 */       System.err.println(((ProbabilityDistribution)this.dist.get(j)).toString());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setUp1()
/*     */   {
/*  81 */     this.dist.add(new TrainableNormal(-1.0D, 0.2D, grid, 1.0D));
/*  82 */     this.dist.add(new TrainableNormal(1.0D, 0.2D, grid, 1.0D));
/*     */     
/*     */ 
/*     */ 
/*  86 */     this.mix = new SimpleExtendedDistribution1(this.dist.size());
/*  87 */     Arrays.fill(this.mix.counts, 1.0D);
/*     */     
/*  89 */     this.mix.transfer(0.0D);
/*  90 */     this.mix.initialise();
/*     */     
/*  92 */     System.err.println("mix " + this.mix.toString());
/*     */     
/*     */ 
/*  95 */     for (int j = 0; j < lim; j++) {
/*  96 */       SkewNormal dist_i = (SkewNormal)this.dist.get((int)this.mix.sample());
/*  97 */       double x = dist_i.rsn();
/*     */       
/*  99 */       this.samples.add(Double.valueOf(x));
/*     */     }
/* 101 */     this.mix = new SimpleExtendedDistribution1(this.dist.size());
/*     */     
/*     */ 
/* 104 */     this.dist.set(0, new TrainableNormal(-1.0D, 0.1D, grid, 1.0D));
/* 105 */     this.dist.set(1, new TrainableNormal(-2.0D, 0.1D, grid, 1.0D));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 110 */     this.dist.set(0, new TrainableNormal(0.0D, 0.01D, grid, 1.0D));
/* 111 */     this.dist.set(1, new TrainableNormal(1.0D, 0.01D, grid, 1.0D));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 116 */     this.prob = new double[this.dist.size()];
/* 117 */     System.err.println("old params ");
/* 118 */     for (int j = 0; j < this.prob.length; j++) {
/* 119 */       System.err.println(((ProbabilityDistribution)this.dist.get(j)).toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public double infer() {
/* 124 */     double likelihood = 0.0D;
/* 125 */     this.mix.initialise();
/* 126 */     for (int i = 0; i < this.samples.size(); i++) {
/* 127 */       Arrays.fill(this.prob, 0.0D);
/* 128 */       double x = ((Double)this.samples.get(i)).doubleValue();
/* 129 */       for (int j = 0; j < this.prob.length; j++) {
/* 130 */         this.prob[j] = ((ProbabilityDistribution)this.dist.get(j)).probability(x);
/*     */       }
/*     */       
/* 133 */       likelihood += Math.log(lc1.util.Constants.sum(this.prob));
/*     */       try {
/* 135 */         SimpleExtendedDistribution.normalise(this.prob);
/*     */       }
/*     */       catch (Exception exc) {
/* 138 */         Arrays.fill(this.prob, 1.0D / this.prob.length);
/*     */       }
/* 140 */       for (int j = 0; j < this.prob.length; j++) {
/* 141 */         ProbabilityDistribution distj = (ProbabilityDistribution)this.dist.get(j);
/* 142 */         this.mix.addCount(j, this.prob[j]);
/* 143 */         ((SkewNormal)distj).addCount(x, this.prob[j]);
/*     */       }
/*     */     }
/* 146 */     System.err.println("log likelihood is " + likelihood);
/* 147 */     System.err.println("new params ");
/* 148 */     this.mix.transfer(0.0D);
/* 149 */     System.err.println(this.mix);
/* 150 */     for (int j = 0; j < this.prob.length; j++) {
/* 151 */       ProbabilityDistribution distj = (ProbabilityDistribution)this.dist.get(j);
/* 152 */       if ((j > 1) && 
/* 153 */         (getDist(j).mean() < getDist(j - 1).mean())) { throw new RuntimeException("!!");
/*     */       }
/*     */       
/* 156 */       ((SkewNormal)distj).maximise(0.0D, 0.0D, 0.0D);
/* 157 */       System.err.println(((ProbabilityDistribution)this.dist.get(j)).toString());
/*     */     }
/* 159 */     return likelihood;
/*     */   }
/*     */   
/*     */   public SkewNormal getDist(int j) {
/* 163 */     return (SkewNormal)this.dist.get(j);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/ProbabilityTest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */