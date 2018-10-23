/*     */ package lc1.dp.data.collection;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.PhasedDataState;
/*     */ import lc1.stats.IntegerDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class DartDataCollection extends SimpleDataCollection
/*     */ {
/*     */   public void process(String indiv, String[] header, String[] geno, int i, Character majorAllele, Character minorAllele, Double recessiveFreq, boolean missing)
/*     */   {
/*     */     try
/*     */     {
/*  18 */       PhasedDataState data = (PhasedDataState)this.data.get(indiv);
/*  19 */       HaplotypeEmissionState dataSt = (HaplotypeEmissionState)this.dataL.get(indiv);
/*  20 */       int no_copies = data.noCopies();
/*  21 */       EmissionStateSpace stsp = data.getEmissionStateSpace();
/*  22 */       List<Integer> callingScoreIndex = new java.util.ArrayList();
/*     */       
/*  24 */       for (int c = 1; c < 4; c++) { int index;
/*  25 */         if ((index = getIndex(header, "call" + c)) != -1) {
/*  26 */           callingScoreIndex.add(Integer.valueOf(index));
/*     */         }
/*     */       }
/*  29 */       double[] callingScore = new double[callingScoreIndex.size()];
/*  30 */       for (int c = 0; c < callingScoreIndex.size(); c++) {
/*  31 */         callingScore[c] = Double.parseDouble(geno[((Integer)callingScoreIndex.get(c)).intValue()]);
/*     */       }
/*  33 */       double[] dist = new double[stsp.size()];
/*  34 */       double sum = 0.0D;
/*  35 */       Integer ind = trans(geno[0], data);
/*  36 */       int numMissAllele = getNumMissAllele(geno[0]);
/*  37 */       Integer majIndex = null;
/*  38 */       if ((majorAllele != null) && (!Constants.topBottom)) {
/*  39 */         majIndex = trans(majorAllele.toString() + majorAllele.toString());
/*     */       }
/*  41 */       if ((numMissAllele == no_copies) || (numMissAllele == 0)) {
/*  42 */         if (recessiveFreq != null) {
/*  43 */           double q = Math.sqrt(recessiveFreq.doubleValue());
/*  44 */           double p = 1.0D - q;
/*  45 */           double pSqure = Math.pow(p, 2.0D);
/*  46 */           double pq = 2.0D * p * q;
/*  47 */           for (int ij = 0; ij < dist.length; ij++) {
/*  48 */             if (stsp.getCN(ij) == no_copies) {
/*  49 */               if (majIndex.intValue() == ij) {
/*  50 */                 dist[ij] = 0.0D;
/*     */ 
/*     */               }
/*  53 */               else if (ij == 1) {
/*  54 */                 dist[ij] = (pq / (pSqure + pq));
/*     */               }
/*     */               else {
/*  57 */                 dist[ij] = (pSqure / (pSqure + pq));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/*  64 */           for (int ij = 0; ij < dist.length; ij++) {
/*  65 */             if (stsp.getCN(ij) == no_copies) {
/*  66 */               dist[ij] = 1.0D;
/*  67 */               sum += 1.0D;
/*     */             }
/*     */           }
/*  70 */           for (int ij = 0; ij < dist.length; ij++) {
/*  71 */             dist[ij] /= sum;
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/*  76 */         List<Comparable> genoList = stsp.defaultList;
/*  77 */         if (dist.length != genoList.size()) throw new RuntimeException("!!");
/*  78 */         for (int ij = 0; ij < dist.length; ij++) {
/*  79 */           int[] numAlleleData = getNumAllele(geno[0]);
/*  80 */           if (stsp.getCN(ij) == no_copies) {
/*  81 */             String ge = ((Comparable)genoList.get(ij)).toString();
/*  82 */             int[] numAllele = getNumAllele(ge);
/*  83 */             if ((numAllele[0] >= numAlleleData[0]) && (numAllele[1] >= numAlleleData[1])) {
/*  84 */               dist[ij] = 1.0D;
/*  85 */               sum += 1.0D;
/*     */             } else {
/*  87 */               dist[ij] = 0.0D;
/*     */             }
/*     */           } }
/*  90 */         for (int ij = 0; ij < dist.length; ij++) {
/*  91 */           dist[ij] /= sum;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*  96 */       if (data.emissions[i] == null) {
/*  97 */         data.emissions[i] = (ind == null ? new IntegerDistribution(0) : new IntegerDistribution(ind.intValue()));
/*  98 */         if ((ind == null) || (missing)) {
/*  99 */           dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */         }
/* 101 */         else if ((stsp.getCN(ind.intValue()) != 0) && (callingScore.length == 1) && (callingScore[0] < 0.95D)) {
/* 102 */           dist = new double[stsp.size()];
/* 103 */           int genoIndex = stsp.getHaploPairFromHaplo(ind.intValue());
/* 104 */           for (int s = 0; s < dist.length; s++) {
/* 105 */             if (stsp.getCN(s) == no_copies) {
/* 106 */               dist[s] = ((1.0D - callingScore[0]) / (dist.length - 1));
/*     */             }
/*     */           }
/* 109 */           dist[genoIndex] = callingScore[0];
/* 110 */           dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */         }
/* 112 */         else if ((stsp.getCN(ind.intValue()) != 0) && (callingScore.length == 3)) {
/* 113 */           dist = callingScore;
/* 114 */           int max = Constants.getMax(dist);
/* 115 */           if (dist.length != callingScore.length) throw new RuntimeException("!!");
/* 116 */           if (dist[max] > 0.95D) {
/* 117 */             dataSt.emissions[i] = new IntegerDistribution(max);
/*     */           }
/*     */           else {
/* 120 */             dataSt.emissions[i] = new SimpleExtendedDistribution(dist, Double.POSITIVE_INFINITY);
/*     */           }
/*     */         }
/*     */         else {
/* 124 */           dataSt.emissions[i] = new IntegerDistribution(ind.intValue());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 131 */       data.emissions[i].setDataIndex(this.index);
/*     */     } catch (Exception exc) {
/* 133 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/collection/DartDataCollection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */