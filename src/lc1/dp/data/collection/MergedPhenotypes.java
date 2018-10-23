/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MergedPhenotypes extends Phenotypes
/*     */ {
/*     */   Phenotypes[] phens;
/*     */   
/*     */   class Info
/*     */   {
/*     */     String trait;
/*     */     int[] phenAlias;
/*     */     Map<String, Integer>[] vals;
/*     */     lc1.stats.ProbabilityDistribution[] dists;
/*     */     Map<Integer, Integer>[] recoding;
/*     */     
/*     */     public Info(String trait)
/*     */     {
/*  20 */       this.trait = trait;
/*  21 */       this.phenAlias = new int[MergedPhenotypes.this.phens.length];
/*  22 */       java.util.Arrays.fill(this.phenAlias, -1);
/*  23 */       this.vals = new Map[MergedPhenotypes.this.phens.length];
/*  24 */       this.dists = new lc1.stats.ProbabilityDistribution[MergedPhenotypes.this.phens.length];
/*  25 */       this.recoding = new Map[MergedPhenotypes.this.phens.length];
/*     */     }
/*     */     
/*     */     public void setAlias(int dataset_index, int pos_ind_dataset) {
/*  29 */       this.phenAlias[dataset_index] = pos_ind_dataset;
/*  30 */       this.dists[dataset_index] = MergedPhenotypes.this.phens[dataset_index].phenotypeDistribution[pos_ind_dataset];
/*  31 */       this.vals[dataset_index] = MergedPhenotypes.this.phens[dataset_index].phenVals[pos_ind_dataset];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */     Map<String, Integer> coding = null;
/*  42 */     lc1.stats.ProbabilityDistribution dist = null;
/*     */     
/*     */     public void calcRecoding()
/*     */     {
/*  46 */       int counter = 0;
/*  47 */       for (int i = 0; i < this.vals.length; i++) {
/*  48 */         if (this.phenAlias[i] >= 0) {
/*  49 */           if (this.vals[i] != null) {
/*  50 */             if (this.dist != null) throw new RuntimeException("!!");
/*  51 */             if (this.coding == null) this.coding = new java.util.HashMap();
/*  52 */             this.recoding[i] = new java.util.HashMap();
/*  53 */             for (java.util.Iterator<java.util.Map.Entry<String, Integer>> it = this.vals[i].entrySet().iterator(); it.hasNext();) {
/*  54 */               java.util.Map.Entry<String, Integer> nxt = (java.util.Map.Entry)it.next();
/*  55 */               Integer ind = (Integer)this.coding.get(nxt.getKey());
/*  56 */               if (ind == null) {
/*  57 */                 this.coding.put((String)nxt.getKey(), ind = Integer.valueOf(counter));
/*  58 */                 counter++;
/*     */               }
/*  60 */               this.recoding[i].put((Integer)nxt.getValue(), ind);
/*     */             }
/*     */           }
/*     */           else {
/*  64 */             if (this.coding != null) throw new RuntimeException("!!");
/*  65 */             if (this.dist == null) {
/*  66 */               this.dist = this.dists[i].clone();
/*     */             }
/*     */             else
/*     */             {
/*  70 */               this.dist.addCounts(this.dists[i]);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*  76 */       if (this.dist == null) {
/*  77 */         this.dist = new lc1.stats.SimpleExtendedDistribution(this.coding.keySet().size());
/*  78 */         for (int i = 0; i < this.vals.length; i++) {
/*  79 */           if (this.phenAlias[i] >= 0) {
/*  80 */             ((lc1.stats.SimpleExtendedDistribution)this.dist).addCounts((lc1.stats.SimpleExtendedDistribution)this.dists[i], this.recoding[i]);
/*     */           }
/*     */         }
/*     */       }
/*  84 */       this.dist.transfer(0.0D);
/*     */     }
/*     */     
/*     */     public Double transform(int i, Double phenValue) {
/*  88 */       if (this.recoding[i] == null) { return phenValue;
/*     */       }
/*  90 */       return Double.valueOf(((Integer)this.recoding[i].get(Integer.valueOf(phenValue.intValue()))).intValue());
/*     */     }
/*     */   }
/*     */   
/*  94 */   Map<String, Info> traits = new java.util.HashMap();
/*     */   
/*     */   MergedPhenotypes(DataCollection[] ldl)
/*     */   {
/*  98 */     this.phens = new Phenotypes[ldl.length];
/*  99 */     for (int i = 0; i < this.phens.length; i++) {
/* 100 */       this.phens[i] = ldl[i].pheno;
/*     */     }
/* 102 */     getPhenAlias();
/* 103 */     this.phen = new java.util.ArrayList((java.util.Collection)this.traits.keySet());
/* 104 */     this.phenotypeDistribution = new lc1.stats.ProbabilityDistribution[this.phen.size()];
/* 105 */     this.phenVals = new Map[this.phen.size()];
/* 106 */     for (int i = 0; i < this.phen.size(); i++) {
/* 107 */       String pheno = (String)this.phen.get(i);
/* 108 */       Info inf = (Info)this.traits.get(pheno);
/* 109 */       inf.calcRecoding();
/* 110 */       this.phenotypeDistribution[i] = inf.dist;
/* 111 */       this.phenVals[i] = inf.coding;
/*     */     }
/*     */   }
/*     */   
/*     */   private void getPhenAlias()
/*     */   {
/* 117 */     for (int i = 0; i < this.phens.length; i++) {
/* 118 */       if (this.phens[i] != null) {
/* 119 */         for (int j = 0; j < this.phens[i].size(); j++) {
/* 120 */           String ph = (String)this.phens[i].phen.get(j);
/* 121 */           Info res = (Info)this.traits.get(ph);
/* 122 */           if (res == null) {
/* 123 */             this.traits.put(ph, res = new Info(ph));
/*     */           }
/*     */           
/* 126 */           res.setAlias(i, j);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Double[] getRecodedValues(lc1.dp.states.HaplotypeEmissionState[] tmp) {
/* 133 */     Double[] phenD = new Double[this.phen.size()];
/* 134 */     for (int k = 0; k < this.phen.size(); k++) {
/* 135 */       Info inf = (Info)this.traits.get(this.phen.get(k));
/* 136 */       for (int i = 0; i < tmp.length; i++) {
/* 137 */         int alias = inf.phenAlias[i];
/* 138 */         if ((tmp[i] != null) && (alias >= 0)) {
/* 139 */           Double sc = inf.transform(i, tmp[i].phenValue()[alias]);
/* 140 */           if (phenD[k] != null) {
/* 141 */             if (Math.abs(phenD[k].doubleValue() - sc.doubleValue()) > 0.001D) throw new RuntimeException("!!");
/*     */           } else {
/* 143 */             phenD[k] = sc;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 148 */     return phenD;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/MergedPhenotypes.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */