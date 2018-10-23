/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class FastPhaseFormat
/*     */ {
/*  18 */   List<String> maleID = new ArrayList();
/*     */   
/*  20 */   public void getMaleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  22 */     while ((st = br.readLine()) != null) { String st;
/*  23 */       String[] str = st.split("\\s+");
/*  24 */       if (str[1].equals("1"))
/*  25 */         this.maleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   List[] snpInfo;
/*     */   List<String> excludeMale;
/*     */   List<String> excludeSNPs;
/*  32 */   public void getSNPinfo(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*  33 */     ArrayList<Integer> loc = new ArrayList();
/*  34 */     ArrayList<String> snp = new ArrayList();
/*  35 */     ArrayList<String> A_allele = new ArrayList();
/*  36 */     this.snpInfo = new List[] { snp, loc, A_allele };
/*     */     String st;
/*  38 */     while ((st = br.readLine()) != null) { String st;
/*  39 */       String[] str = st.split("\\s+");
/*  40 */       snp.add(str[0]);
/*  41 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*  42 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(File f) throws Exception
/*     */   {
/*  48 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  50 */     String start = null;
/*  51 */     List<String> indivID = new ArrayList();
/*  52 */     String st; while ((st = br.readLine()) != null) { String st;
/*  53 */       String[] str = st.split("\\s+");
/*  54 */       if (start == null) {
/*  55 */         start = str[0];
/*     */       } else
/*  57 */         if (!st.startsWith(start)) break;
/*  58 */       indivID.add(str[1]);
/*     */     }
/*  60 */     br.close();
/*  61 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getExcludeMale(File f)
/*     */     throws Exception
/*     */   {
/*  67 */     this.excludeMale = new ArrayList();
/*  68 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  70 */     while ((st = br.readLine()) != null) { String st;
/*  71 */       this.excludeMale.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getExcludeSNPs(File f)
/*     */     throws Exception
/*     */   {
/*  78 */     this.excludeSNPs = new ArrayList();
/*  79 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  81 */     while ((st = br.readLine()) != null) { String st;
/*  82 */       this.excludeSNPs.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*  86 */   Map<String, List<Character>> rawData = new HashMap();
/*     */   
/*  88 */   public void ChiamoDataRaw(File bc, File nbs, double cutpoint) throws Exception { BufferedReader br1 = new BufferedReader(new FileReader(bc));
/*  89 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/*  90 */     List<String> indivBC = getIndiv(bc);
/*  91 */     List<String> indivNBS = getIndiv(nbs);
/*  92 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/*  93 */       if (this.excludeSNPs.contains(this.snpInfo[0].get(i))) {
/*  94 */         for (int k = 0; k < indivBC.size(); k++) {
/*  95 */           br1.readLine();
/*     */         }
/*  97 */         for (int k = 0; k < indivNBS.size(); k++) {
/*  98 */           br2.readLine();
/*     */         }
/*     */       }
/*     */       else {
/* 102 */         String al = this.snpInfo[2].get(i).toString();
/* 103 */         if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 104 */         char a = al.charAt(0);
/* 105 */         for (int k = 0; k < indivBC.size(); k++) {
/* 106 */           if (!this.maleID.contains(indivBC.get(k))) { br1.readLine();
/*     */           } else {
/* 108 */             String[] str1 = br1.readLine().split("\\s+");
/* 109 */             if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 110 */             if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 111 */             if (!this.rawData.containsKey(str1[1])) {
/* 112 */               List<Character> oneIndSNPs = new ArrayList();
/* 113 */               this.rawData.put(str1[1], oneIndSNPs);
/*     */             }
/* 115 */             if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 116 */               if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 117 */                 ((List)this.rawData.get(str1[1])).add(Character.valueOf('?'));
/*     */ 
/*     */               }
/* 120 */               else if (str1[2].charAt(0) == a) ((List)this.rawData.get(str1[1])).add(Character.valueOf('A')); else {
/* 121 */                 ((List)this.rawData.get(str1[1])).add(Character.valueOf('B'));
/*     */               }
/*     */             } else
/* 124 */               ((List)this.rawData.get(str1[1])).add(Character.valueOf('?'));
/*     */           }
/*     */         }
/* 127 */         for (int k = 0; k < indivNBS.size(); k++) {
/* 128 */           if (!this.maleID.contains(indivNBS.get(k))) { br2.readLine();
/*     */           } else {
/* 130 */             String[] str1 = br2.readLine().split("\\s+");
/* 131 */             if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 132 */             if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 133 */             if (!this.rawData.containsKey(str1[1])) {
/* 134 */               List<Character> oneIndSNPs = new ArrayList();
/* 135 */               this.rawData.put(str1[1], oneIndSNPs);
/*     */             }
/* 137 */             if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 138 */               if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 139 */                 ((List)this.rawData.get(str1[1])).add(Character.valueOf('?'));
/*     */ 
/*     */               }
/* 142 */               else if (str1[2].charAt(0) == a) ((List)this.rawData.get(str1[1])).add(Character.valueOf('A')); else {
/* 143 */                 ((List)this.rawData.get(str1[1])).add(Character.valueOf('B'));
/*     */               }
/*     */             } else
/* 146 */               ((List)this.rawData.get(str1[1])).add(Character.valueOf('?'));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printPF(PrintStream ps, int noSNPs) {
/* 154 */     int noInd = Math.round((this.rawData.size() - this.excludeMale.size()) / 2);
/* 155 */     ps.println(noInd);
/* 156 */     ps.println(noSNPs);
/* 157 */     int count = 0;
/* 158 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 159 */       String s = (String)is.next();
/* 160 */       if (!this.excludeMale.contains(s)) {
/* 161 */         if (count < noInd * 2) {
/* 162 */           for (int i = 0; i < noSNPs; i++) {
/* 163 */             ps.print(((List)this.rawData.get(s)).get(i));ps.print(" ");
/*     */           }
/* 165 */           ps.println();
/*     */         }
/* 167 */         count++;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 174 */       FastPhaseFormat fp = new FastPhaseFormat();
/* 175 */       fp.getMaleID(new File("sample/Affx_20070205fs1_sample_58C.txt"));
/* 176 */       fp.getMaleID(new File("sample/Affx_20070205fs1_sample_NBS.txt"));
/* 177 */       fp.getSNPinfo(new File("X_snp.txt"));
/* 178 */       fp.getExcludeMale(new File("excludeIndMale.txt"));
/* 179 */       fp.getExcludeSNPs(new File("excludeMarkers1.txt"));
/* 180 */       fp.ChiamoDataRaw(new File("genotype/Affx_20070205fs1_gt_58C_Chiamo_X.txt"), new File("genotype/Affx_20070205fs1_gt_NBS_Chiamo_X.txt"), 0.0D);
/* 181 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("fastPhase/fastphase.inp.txt"))));
/* 182 */       fp.printPF(ps, 2000);
/* 183 */       ps.close();
/*     */     } catch (Exception exc) {
/* 185 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/FastPhaseFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */