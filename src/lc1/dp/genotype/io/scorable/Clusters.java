/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Clusters
/*     */ {
/*     */   private Cluster[] clusters;
/*  14 */   Map<String, Integer[]> m = new HashMap();
/*     */   public int start;
/*     */   public int end;
/*     */   
/*  18 */   public Clusters(int numF, int start, int end) { this.clusters = new Cluster[numF];
/*  19 */     this.start = start;
/*  20 */     this.end = end;
/*  21 */     for (int i = 0; i < numF; i++)
/*  22 */       this.clusters[i] = new Cluster();
/*     */   }
/*     */   
/*     */   public Cluster get(int i) {
/*  26 */     return this.clusters[(i - 1)];
/*     */   }
/*     */   
/*     */   public String toString() {
/*  30 */     StringBuffer sb = new StringBuffer("{");
/*  31 */     for (int i = 0; i < this.clusters.length; i++)
/*  32 */       if (this.clusters[i].size() != 0) {
/*  33 */         sb.append(this.clusters[i].toString());
/*  34 */         if (i < this.clusters.length - 1) sb.append(",");
/*     */       }
/*  36 */     sb.append("}");
/*  37 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public int[] score(Clusters clust, Collection<String> keys)
/*     */   {
/*  43 */     int[] res = new int[2];
/*  44 */     Iterator<String> it1; for (Iterator<String> it = keys.iterator(); it.hasNext(); 
/*     */         
/*  46 */         it1.hasNext())
/*     */     {
/*  45 */       String nxt1 = (String)it.next();
/*  46 */       it1 = keys.iterator(); continue;
/*  47 */       String nxt2 = (String)it1.next();
/*  48 */       int equals_c = clust.sameInt(nxt1, nxt2);
/*  49 */       int equals_c1 = sameInt(nxt1, nxt2);
/*  50 */       if (equals_c == equals_c1) {
/*  51 */         res[0] += 1;
/*     */       }
/*     */       else {
/*  54 */         res[1] += 1;
/*     */       }
/*     */     }
/*     */     
/*  58 */     return res;
/*     */   }
/*     */   
/*     */   public int[] scoreHomo(Clusters clust, Collection<String> keys) {
/*  62 */     int[] res = new int[2];
/*  63 */     for (Iterator<String> it = keys.iterator(); it.hasNext();)
/*     */     {
/*  65 */       String nxt2 = (String)it.next();
/*  66 */       boolean equals_c = clust.isHomozygous(nxt2);
/*  67 */       boolean equals_c1 = isHomozygous(nxt2);
/*  68 */       if (equals_c == equals_c1) {
/*  69 */         res[0] += 1;
/*     */       }
/*     */       else {
/*  72 */         res[1] += 1;
/*     */       }
/*     */     }
/*  75 */     return res;
/*     */   }
/*     */   
/*     */   private Collection<Integer> index(String nxt) {
/*  79 */     return new HashSet((Collection)Arrays.asList((Integer[])this.m.get(nxt)));
/*     */   }
/*     */   
/*     */   private boolean same(String nxt1, String nxt2) {
/*  83 */     Collection<Integer> ind1 = index(nxt1);
/*  84 */     Collection<Integer> ind2 = index(nxt2);
/*  85 */     ind1.retainAll(ind2);
/*  86 */     if (ind1.size() > 0) { return true;
/*     */     }
/*  88 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isHomozygous(String nxt) {
/*  92 */     Integer[] res = (Integer[])this.m.get(nxt);
/*  93 */     for (int i = 1; i < res.length; i++) {
/*  94 */       if (res[i] != res[0]) return false;
/*     */     }
/*  96 */     return true;
/*     */   }
/*     */   
/*  99 */   private int sameInt(String nxt1, String nxt2) { Collection<Integer> ind1 = index(nxt1);
/* 100 */     Collection<Integer> ind2 = index(nxt2);
/* 101 */     ind1.retainAll(ind2);
/* 102 */     return ind1.size();
/*     */   }
/*     */   
/*     */   public double[] compare(Clusters clust, double thresh) {
/* 106 */     double[] res = { 0.0D, 0.0D };
/* 107 */     for (int i = 0; i < this.clusters.length; i++) {
/* 108 */       if (this.clusters[i].size() != 0) {
/* 109 */         res[0] += clust.overlap(this.clusters[i]);
/* 110 */         res[1] += 1.0D;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 116 */     return res;
/*     */   }
/*     */   
/* 119 */   private double overlap(Cluster cluster) { double max = 0.0D;
/* 120 */     for (int i = 0; i < this.clusters.length; i++) {
/* 121 */       double sc = cluster.overlap(this.clusters[i]);
/* 122 */       if (sc > max) max = sc;
/*     */     }
/* 124 */     return max;
/*     */   }
/*     */   
/* 127 */   private boolean contains(Cluster cluster) { for (int i = 0; i < this.clusters.length; i++) {
/* 128 */       if (this.clusters[i].equals(cluster)) return true;
/*     */     }
/* 130 */     return false;
/*     */   }
/*     */   
/* 133 */   public void sort() { for (int i = 0; i < this.clusters.length; i++) {
/* 134 */       Collections.sort(this.clusters[i]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void add(Integer[] val, String name) {
/* 139 */     for (int i = 0; i < val.length; i++) {
/* 140 */       this.clusters[(val[i].intValue() - 1)].add(name);
/*     */     }
/* 142 */     this.m.put(name, val);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/Clusters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */