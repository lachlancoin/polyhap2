/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CNVphaser
/*     */ {
/*  27 */   static int[] mid = { 151801138, 154499338 };
/*     */   
/*  29 */   Map<String, List<Integer[]>> cnvData = new HashMap();
/*     */   
/*     */   public void getCNVdata(File f) throws Exception
/*     */   {
/*  33 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  35 */     String st = br.readLine();
/*     */     
/*  37 */     while ((st = br.readLine()) != null) {
/*  38 */       String[] str = st.split("\t");
/*  39 */       if (!str[9].equals("X")) throw new RuntimeException("not X chromosome");
/*  40 */       double lr = Double.parseDouble(str[22]);
/*  41 */       int cn; int cn; if (lr <= -0.5D) { cn = 0; } else { int cn;
/*  42 */         if (lr > 0.3D) cn = 3; else
/*  43 */           cn = 2; }
/*  44 */       if (!this.cnvData.containsKey(str[6])) {
/*  45 */         this.cnvData.put(str[6], new ArrayList());
/*  46 */         ((List)this.cnvData.get(str[6])).add(new Integer[] { Integer.valueOf(Integer.parseInt(str[10])), Integer.valueOf(Integer.parseInt(str[11])), Integer.valueOf(cn) });
/*     */       }
/*     */       else {
/*  49 */         ((List)this.cnvData.get(str[6])).add(new Integer[] { Integer.valueOf(Integer.parseInt(str[10])), Integer.valueOf(Integer.parseInt(str[11])), Integer.valueOf(cn) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getSNP(ZipFile f, String entryName, Integer column) throws Exception {
/*  55 */     BufferedReader nxt = 
/*  56 */       new BufferedReader(new InputStreamReader(
/*  57 */       f.getInputStream(f.getEntry(entryName))));
/*  58 */     List<String> indiv = new ArrayList();
/*  59 */     String st = "";
/*  60 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*  61 */       indiv.add(st.split("\\s+")[column.intValue()]);
/*     */     }
/*  63 */     nxt.close();
/*  64 */     return indiv;
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(ZipFile f, String entryName, Integer column) throws Exception {
/*  68 */     BufferedReader nxt = 
/*  69 */       new BufferedReader(new InputStreamReader(
/*  70 */       f.getInputStream(f.getEntry(entryName))));
/*  71 */     List<String> indiv = new ArrayList();
/*  72 */     String st = "";
/*  73 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*  74 */       indiv.add(st.split("\\s+")[column.intValue()].split("#")[0]);
/*     */     }
/*  76 */     nxt.close();
/*  77 */     return indiv;
/*     */   }
/*     */   
/*     */   public int getCN(String ind, Integer snploc)
/*     */   {
/*  82 */     int cn = -1;
/*  83 */     for (int i = 0; i < ((List)this.cnvData.get(ind)).size(); i++) {
/*  84 */       Integer[] cnvloc = (Integer[])((List)this.cnvData.get(ind)).get(i);
/*  85 */       if ((snploc.intValue() >= cnvloc[0].intValue()) && (snploc.intValue() <= cnvloc[1].intValue())) {
/*  86 */         cn = cnvloc[2].intValue();
/*  87 */         break;
/*     */       }
/*     */     }
/*  90 */     return cn;
/*     */   }
/*     */   
/*     */   public String getMissingAlleles(Double baf)
/*     */   {
/*  95 */     String alleles = "";
/*  96 */     if (baf.doubleValue() <= 0.5D) { alleles = "A";
/*  97 */     } else if (baf.doubleValue() > 0.5D) alleles = "B";
/*  98 */     return alleles;
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer[] getAllelesCount(int cn, Double baf, String allele)
/*     */   {
/* 104 */     Integer[] allelesCount = { Integer.valueOf(0), Integer.valueOf(0) };
/*     */     
/* 106 */     if (cn != 0)
/* 107 */       if (cn == 1) {
/* 108 */         if (baf.doubleValue() < 0.5D) { int tmp43_42 = 0; Integer[] tmp43_40 = allelesCount;tmp43_40[tmp43_42] = Integer.valueOf(tmp43_40[tmp43_42].intValue() + 1);
/* 109 */         } else if (baf.doubleValue() >= 0.5D) { int tmp71_70 = 1; Integer[] tmp71_68 = allelesCount;tmp71_68[tmp71_70] = Integer.valueOf(tmp71_68[tmp71_70].intValue() + 1); }
/* 110 */         if (baf.isNaN()) throw new RuntimeException("missing baf");
/*     */       }
/* 112 */       else if (cn == 2) {
/* 113 */         if (baf.doubleValue() <= 0.25D) { int tmp121_120 = 0; Integer[] tmp121_118 = allelesCount;tmp121_118[tmp121_120] = Integer.valueOf(tmp121_118[tmp121_120].intValue() + 2);
/* 114 */         } else if (baf.doubleValue() >= 0.75D) { int tmp149_148 = 1; Integer[] tmp149_146 = allelesCount;tmp149_146[tmp149_148] = Integer.valueOf(tmp149_146[tmp149_148].intValue() + 2);
/*     */         } else {
/* 116 */           int tmp166_165 = 0; Integer[] tmp166_163 = allelesCount;tmp166_163[tmp166_165] = Integer.valueOf(tmp166_163[tmp166_165].intValue() + 1); int tmp180_179 = 1; Integer[] tmp180_177 = allelesCount;tmp180_177[tmp180_179] = Integer.valueOf(tmp180_177[tmp180_179].intValue() + 1);
/*     */         }
/* 118 */       } else if (cn == 3) {
/* 119 */         if (baf.doubleValue() <= 0.16667D) { int tmp213_212 = 0; Integer[] tmp213_210 = allelesCount;tmp213_210[tmp213_212] = Integer.valueOf(tmp213_210[tmp213_212].intValue() + 3);
/* 120 */         } else if (baf.doubleValue() >= 0.83333D) { int tmp241_240 = 1; Integer[] tmp241_238 = allelesCount;tmp241_238[tmp241_240] = Integer.valueOf(tmp241_238[tmp241_240].intValue() + 3);
/* 121 */         } else if ((baf.doubleValue() > 0.16667D) && (baf.doubleValue() <= 0.5D)) { int tmp280_279 = 0; Integer[] tmp280_277 = allelesCount;tmp280_277[tmp280_279] = Integer.valueOf(tmp280_277[tmp280_279].intValue() + 2); int tmp294_293 = 1; Integer[] tmp294_291 = allelesCount;tmp294_291[tmp294_293] = Integer.valueOf(tmp294_291[tmp294_293].intValue() + 1);
/*     */         } else {
/* 123 */           int tmp311_310 = 0; Integer[] tmp311_308 = allelesCount;tmp311_308[tmp311_310] = Integer.valueOf(tmp311_308[tmp311_310].intValue() + 1); int tmp325_324 = 1; Integer[] tmp325_322 = allelesCount;tmp325_322[tmp325_324] = Integer.valueOf(tmp325_322[tmp325_324].intValue() + 2);
/*     */         }
/* 125 */         if (baf.isNaN()) throw new RuntimeException("missing baf");
/*     */       }
/* 127 */       else if (cn == -1) {
/* 128 */         if (allele.equals("A")) { int tmp373_372 = 0; Integer[] tmp373_370 = allelesCount;tmp373_370[tmp373_372] = Integer.valueOf(tmp373_370[tmp373_372].intValue() + 1);
/* 129 */         } else if (allele.equals("B")) { int tmp399_398 = 1; Integer[] tmp399_396 = allelesCount;tmp399_396[tmp399_398] = Integer.valueOf(tmp399_396[tmp399_398].intValue() + 1);
/*     */         }
/* 131 */         else if ((allele.equals("N")) && (!baf.isNaN())) {
/* 132 */           if (getMissingAlleles(baf).equals("A")) { int tmp445_444 = 0; Integer[] tmp445_442 = allelesCount;tmp445_442[tmp445_444] = Integer.valueOf(tmp445_442[tmp445_444].intValue() + 1);
/* 133 */           } else if (getMissingAlleles(baf).equals("B")) { int tmp475_474 = 1; Integer[] tmp475_472 = allelesCount;tmp475_472[tmp475_474] = Integer.valueOf(tmp475_472[tmp475_474].intValue() + 1);
/*     */           }
/* 135 */         } else { int tmp492_491 = 0; Integer[] tmp492_489 = allelesCount;tmp492_489[tmp492_491] = Integer.valueOf(tmp492_489[tmp492_491].intValue() + 1);
/*     */         }
/* 137 */       } else { throw new RuntimeException("cn is wrong!"); }
/* 138 */     return allelesCount;
/*     */   }
/*     */   
/*     */   public String getAllelesCode(int cn, Double baf, String allele)
/*     */   {
/* 143 */     String alleles = "";
/* 144 */     if (cn == 0) { alleles = "_";
/* 145 */     } else if (cn == 1) {
/* 146 */       if (baf.doubleValue() < 0.5D) { alleles = "A";
/* 147 */       } else if (baf.doubleValue() >= 0.5D) alleles = "B";
/*     */     }
/* 149 */     else if (cn == 2) {
/* 150 */       if (baf.doubleValue() <= 0.25D) { alleles = "X";
/* 151 */       } else if (baf.doubleValue() >= 0.75D) alleles = "Z"; else {
/* 152 */         alleles = "Y";
/*     */       }
/* 154 */     } else if (cn == 3) {
/* 155 */       if (baf.doubleValue() <= 0.16667D) { alleles = "T";
/* 156 */       } else if (baf.doubleValue() >= 0.83333D) { alleles = "W";
/* 157 */       } else if ((baf.doubleValue() > 0.16667D) && (baf.doubleValue() <= 0.5D)) alleles = "U"; else {
/* 158 */         alleles = "V";
/*     */       }
/* 160 */     } else if (cn == -1) {
/* 161 */       if (allele.equals("A")) { alleles = "A";
/* 162 */       } else if (allele.equals("B")) { alleles = "B";
/*     */       }
/* 164 */       else if ((allele.equals("N")) && (!baf.isNaN())) alleles = getMissingAlleles(baf); else
/* 165 */         alleles = "A";
/*     */     } else
/* 167 */       throw new RuntimeException("cn is wrong!");
/* 168 */     return alleles;
/*     */   }
/*     */   
/*     */   public List<Integer> getRan(File f) throws Exception
/*     */   {
/* 173 */     List<Integer> ran = new ArrayList();
/* 174 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 176 */     while ((st = br.readLine()) != null) { String st;
/* 177 */       ran.add(Integer.valueOf(Integer.parseInt(st)));
/*     */     }
/* 179 */     return ran;
/*     */   }
/*     */   
/*     */   public void cnvPhaserData(File f, int noSite, PrintStream ps, PrintStream ps1, PrintStream ps2) throws Exception
/*     */   {
/* 184 */     Map<String, List<Integer[]>> cnvPhaserData = new HashMap();
/* 185 */     ZipFile zf = new ZipFile(f);
/* 186 */     List<String> indiv = new ArrayList();
/* 187 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/* 188 */     List<String> snp = new ArrayList();
/* 189 */     snp = getSNP(zf, "SNPS", Integer.valueOf(3));
/* 190 */     List<String> loc = new ArrayList();
/* 191 */     loc = getSNP(zf, "SNPS", Integer.valueOf(1));
/* 192 */     String st = "";
/* 193 */     List<Integer> ranOrder = getRan(new File("ranOrderSample.txt"));
/* 194 */     if (ranOrder.size() != indiv.size()) throw new RuntimeException("!!");
/* 195 */     int sampleSize = Math.round(indiv.size() / 2) * 2;
/* 196 */     int count = 0;
/* 197 */     for (int i = 0; i < snp.size(); i++) {
/* 198 */       if ((Integer.parseInt((String)loc.get(i)) > mid[0]) && (Integer.parseInt((String)loc.get(i)) < mid[1]))
/*     */       {
/* 200 */         ps.println((String)snp.get(i) + "\t" + (String)loc.get(i));
/*     */         
/* 202 */         BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/* 203 */         for (int s = 0; s < indiv.size(); s++) {
/* 204 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 205 */           String[] str = st.split("\\s+");
/* 206 */           int cn = getCN((String)indiv.get(s), Integer.valueOf(Integer.parseInt((String)loc.get(i))));
/*     */           
/* 208 */           Integer[] alleleCount = getAllelesCount(cn, Double.valueOf(Double.parseDouble(str[1])), str[0].substring(0, 1));
/* 209 */           if (!cnvPhaserData.containsKey(indiv.get(s))) {
/* 210 */             cnvPhaserData.put((String)indiv.get(s), new ArrayList());
/* 211 */             ((List)cnvPhaserData.get(indiv.get(s))).add(alleleCount);
/*     */           }
/*     */           else {
/* 214 */             ((List)cnvPhaserData.get(indiv.get(s))).add(alleleCount);
/*     */           }
/*     */         }
/*     */         
/* 218 */         count++;
/*     */       }
/* 220 */       if (noSite == count) break;
/*     */     }
/* 222 */     for (int s = 0; s < sampleSize; s += 2) {
/* 223 */       String hap1 = (String)indiv.get(((Integer)ranOrder.get(s)).intValue());
/* 224 */       String hap2 = (String)indiv.get(((Integer)ranOrder.get(s + 1)).intValue());
/* 225 */       ps1.print(hap1 + "\t" + hap1 + "\t" + "0" + "\t" + "0" + "\t" + "0" + "\t" + "0");
/* 226 */       ps2.println(hap1);
/* 227 */       for (int i = 0; i < noSite; i++) {
/* 228 */         int[] sumCount = { ((Integer[])((List)cnvPhaserData.get(hap1)).get(i))[0].intValue() + ((Integer[])((List)cnvPhaserData.get(hap2)).get(i))[0].intValue(), ((Integer[])((List)cnvPhaserData.get(hap1)).get(i))[1].intValue() + ((Integer[])((List)cnvPhaserData.get(hap2)).get(i))[1].intValue() };
/* 229 */         ps1.print("\t" + sumCount[0] + "\t" + sumCount[1]);
/*     */       }
/* 231 */       ps1.println();
/* 232 */       for (int i = 0; i < noSite; i++) {
/* 233 */         for (int k = 0; k < ((Integer[])((List)cnvPhaserData.get(hap1)).get(i))[0].intValue(); k++) {
/* 234 */           ps2.print("A");
/*     */         }
/* 236 */         for (int k = 0; k < ((Integer[])((List)cnvPhaserData.get(hap1)).get(i))[1].intValue(); k++) {
/* 237 */           ps2.print("B");
/*     */         }
/* 239 */         ps2.print("\t");
/*     */       }
/* 241 */       ps2.println();
/* 242 */       for (int i = 0; i < noSite; i++) {
/* 243 */         for (int k = 0; k < ((Integer[])((List)cnvPhaserData.get(hap2)).get(i))[0].intValue(); k++) {
/* 244 */           ps2.print("A");
/*     */         }
/* 246 */         for (int k = 0; k < ((Integer[])((List)cnvPhaserData.get(hap2)).get(i))[1].intValue(); k++) {
/* 247 */           ps2.print("B");
/*     */         }
/* 249 */         ps2.print("\t");
/*     */       }
/* 251 */       ps2.println();
/*     */     }
/*     */   }
/*     */   
/*     */   public void getRandomNum(int numInd, PrintStream ps)
/*     */   {
/* 257 */     SortedMap<Double, Integer> ranOrder = new TreeMap();
/* 258 */     Random ran = new Random();
/* 259 */     for (int i = 0; i < numInd; i++) {
/* 260 */       ranOrder.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(i));
/*     */     }
/* 262 */     for (Iterator<Double> is = ranOrder.keySet().iterator(); is.hasNext();) {
/* 263 */       Double id = (Double)is.next();
/* 264 */       ps.println(ranOrder.get(id));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try {
/* 271 */       int noSite = 1106;
/* 272 */       CNVphaser cv = new CNVphaser();
/* 273 */       cv.getCNVdata(new File("cnvRegionX.txt"));
/* 274 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(noSite + "Site" + ".info"))));
/* 275 */       PrintStream ps1 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(noSite + "Site" + ".ped"))));
/* 276 */       PrintStream ps2 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(noSite + "Site" + ".true"))));
/* 277 */       cv.cnvPhaserData(new File("X_M.zip"), noSite, ps, ps1, ps2);
/* 278 */       ps.close();
/* 279 */       ps1.close();
/* 280 */       ps2.close();
/*     */ 
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*     */ 
/* 286 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/CNVphaser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */