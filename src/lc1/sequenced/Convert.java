/*     */ package lc1.sequenced;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.dp.data.collection.LikelihoodDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.HaplotypeData;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ 
/*     */ public class Convert
/*     */ {
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  30 */       File f = new File("deletion_samples.txt");
/*  31 */       Location[] l = getDeletions(f, Integer.valueOf(6), null);
/*  32 */       LikelihoodDataCollection ldl = getLikelihoodDataCollection(l);
/*  33 */       EmissionState emSt = (EmissionState)ldl.dataL.get("21938");
/*  34 */       for (int i = 0; i < l.length; i++) {
/*  35 */         System.err.println(l[i]);
/*     */       }
/*     */     } catch (Exception exc) {
/*  38 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Location[] getDeletions(File f, Integer chr, int[] mid) throws Exception
/*     */   {
/*  44 */     Location midLoc = 
/*  45 */       mid != null ? 
/*  46 */       new Location(chr, mid[0], mid[1]) : null;
/*  47 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  48 */     String st = br.readLine();
/*  49 */     String[] str = st.split("\t");
/*  50 */     Location[] l = new Location[(int)Math.ceil(str.length / 2.0D)];
/*  51 */     for (int i = 0; i < l.length; i++) {
/*  52 */       l[i] = new Location(str[(i * 2)]);
/*     */     }
/*     */     int i;
/*  55 */     for (; (st = br.readLine()) != null; 
/*     */         
/*  57 */         i < l.length)
/*     */     {
/*  56 */       str = st.split("\t");
/*  57 */       i = 0; continue;
/*  58 */       if ((str.length > i * 2) && 
/*  59 */         (str[(i * 2)].length() > 0)) {
/*  60 */         l[i].noObs.add(str[(i * 2)] + "_" + str[(i * 2 + 1)].trim());
/*     */       }
/*  57 */       i++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     List<Location> res = new ArrayList();
/*  66 */     for (int i = 0; i < l.length; i++) {
/*  67 */       double overl = midLoc == null ? 10.0D : l[i].overlaps(midLoc);
/*  68 */       System.err.println("overl " + midLoc + " " + l[i] + " " + overl + " " + chr);
/*  69 */       if (overl > 0.0D)
/*     */       {
/*     */ 
/*  72 */         res.add(l[i]);
/*     */       }
/*     */       else {
/*  75 */         Logger.global.info("excluded " + l[i] + " cf " + mid);
/*     */       }
/*     */     }
/*     */     
/*  79 */     java.util.Collections.sort(res);
/*  80 */     return (Location[])res.toArray(new Location[0]);
/*     */   }
/*     */   
/*     */   public static LikelihoodDataCollection getLikelihoodDataCollection(Location[] d) {
/*  84 */     if (d.length == 0) throw new RuntimeException("zero length");
/*  85 */     Set<String> keys = new HashSet();
/*  86 */     List<Integer> locs = new ArrayList();
/*  87 */     for (int i = 0; i < d.length; i++)
/*     */     {
/*  89 */       keys.addAll(d[i].noObs);
/*  90 */       locs.add(Integer.valueOf((int)d[i].min));
/*  91 */       locs.add(Integer.valueOf((int)d[i].max));
/*     */     }
/*     */     
/*     */ 
/*  95 */     LikelihoodDataCollection ldl = new LikelihoodDataCollection(locs);
/*     */     
/*  97 */     EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(1, 0);
/*  98 */     if (emStSp.defaultList.size() < 3) throw new RuntimeException("!!");
/*  99 */     for (Iterator<String> it = keys.iterator(); it.hasNext();) {
/* 100 */       String[] key = ((String)it.next()).split("_");
/* 101 */       lc1.dp.data.representation.SSOData sso = new HaplotypeData(key[0], d.length);
/*     */       
/* 103 */       HaplotypeEmissionState emSt = new HaplotypeEmissionState(key[0], 2 * d.length, emStSp.size(), emStSp, Integer.valueOf(-1), null, 1.0D);
/*     */       
/* 105 */       for (int j = 0; j < d.length; j++) {
/* 106 */         Boolean homo = Boolean.valueOf(key[1].equals("homo"));
/* 107 */         double[] prob = new double[emStSp.defaultList.size()];
/* 108 */         Arrays.fill(prob, 0.0D);
/* 109 */         for (int k = 0; k < prob.length; k++) {
/* 110 */           int cn = emStSp.getCN(k);
/* 111 */           if (key[1].equals("homo")) {
/* 112 */             if (cn == 0) prob[k] = 1.0D;
/*     */           }
/* 114 */           else if (key[1].equals("hetero")) {
/* 115 */             if (cn == 1) prob[k] = 1.0D;
/*     */           }
/* 117 */           else if (key[1].equals("homoA")) {
/* 118 */             if (cn == 4) prob[k] = 1.0D;
/*     */           }
/* 120 */           else if (key[1].equals("heteroA")) {
/* 121 */             if (cn == 3) prob[k] = 1.0D;
/*     */           } else
/* 123 */             throw new RuntimeException("!!" + key[1]);
/*     */         }
/* 125 */         SimpleExtendedDistribution.normalise(prob);
/* 126 */         emSt.setTheta(prob, j * 2);
/* 127 */         emSt.setTheta(prob, j * 2 + 1);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 132 */       ldl.dataL.put(key[0], emSt);
/*     */     }
/* 134 */     ldl.calculateMLGenotypeData(true);
/*     */     
/* 136 */     if (ldl.snpid == null) {
/* 137 */       ldl.snpid = new ArrayList();
/* 138 */       for (int i = 0; i < ldl.loc.size(); i++) {
/* 139 */         ldl.snpid.add("del" + i);
/*     */       }
/*     */     }
/*     */     
/* 143 */     return ldl;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/sequenced/Convert.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */