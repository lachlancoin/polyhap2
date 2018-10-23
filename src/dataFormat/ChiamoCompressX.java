/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.OutputStreamWriter;
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
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChiamoCompressX
/*     */ {
/*  31 */   List<String> maleID = new ArrayList();
/*     */   
/*  33 */   public void getMaleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  35 */     while ((st = br.readLine()) != null) { String st;
/*  36 */       String[] str = st.split("\\s+");
/*  37 */       if (str[1].equals("1")) {
/*  38 */         this.maleID.add(str[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeMale;
/*     */   public void getExcludeMale(File f) throws Exception
/*     */   {
/*  46 */     this.excludeMale = new ArrayList();
/*  47 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  49 */     while ((st = br.readLine()) != null) { String st;
/*  50 */       this.excludeMale.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getExcludeSNPs(File f)
/*     */     throws Exception
/*     */   {
/*  57 */     this.excludeSNPs = new ArrayList();
/*  58 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  60 */     while ((st = br.readLine()) != null) { String st;
/*  61 */       this.excludeSNPs.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(File f)
/*     */     throws Exception
/*     */   {
/*  68 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  70 */     String start = null;
/*  71 */     List<String> indivID = new ArrayList();
/*  72 */     String st; while ((st = br.readLine()) != null) { String st;
/*  73 */       String[] str = st.split("\\s+");
/*  74 */       if (start == null) {
/*  75 */         start = str[0];
/*     */       } else
/*  77 */         if (!st.startsWith(start)) break;
/*  78 */       indivID.add(str[1]);
/*     */     }
/*  80 */     br.close();
/*  81 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/*  86 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  87 */     ArrayList<Integer> loc = new ArrayList();
/*  88 */     ArrayList<String> snp = new ArrayList();
/*  89 */     ArrayList<String> A_allele = new ArrayList();
/*  90 */     this.snpInfo = new List[] { snp, loc, A_allele };
/*     */     String st;
/*  92 */     while ((st = br.readLine()) != null) { String st;
/*  93 */       String[] str = st.split("\\s+");
/*  94 */       snp.add(str[0]);
/*  95 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*  96 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeSNPs;
/*     */   List[] snpInfo;
/*     */   public void getMAF(File f)
/*     */     throws Exception
/*     */   {
/* 105 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String[] s;
/* 107 */     label71: String st; for (; (st = br.readLine()) != null; 
/*     */         
/*     */ 
/* 110 */         this.excludeSNPs.add(s[0]))
/*     */     {
/*     */       String st;
/* 108 */       s = st.split("\\s+");
/*     */       
/* 110 */       if ((Double.parseDouble(s[1]) >= 0.01D) || (this.excludeSNPs.contains(s[0])))
/*     */         break label71;
/*     */     }
/*     */   }
/*     */   
/* 115 */   Map<String, List<String>> rawData = new HashMap();
/*     */   
/* 117 */   public void ChiamoDataRaw(File bc, File nbs, double cutpoint) throws Exception { BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 118 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 119 */     List<String> indivBC = getIndiv(bc);
/* 120 */     List<String> indivNBS = getIndiv(nbs);
/* 121 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 122 */       String al = this.snpInfo[2].get(i).toString();
/* 123 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 124 */       char a = al.charAt(0);
/* 125 */       for (int k = 0; k < indivBC.size(); k++) {
/* 126 */         String[] str1 = br1.readLine().split("\\s+");
/* 127 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 128 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 129 */         if (!this.rawData.containsKey(str1[1])) {
/* 130 */           List<String> oneIndSNPs = new ArrayList();
/* 131 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 133 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 134 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else {
/* 136 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */         }
/*     */       }
/* 139 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 140 */         String[] str1 = br2.readLine().split("\\s+");
/* 141 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 142 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 143 */         if (!this.rawData.containsKey(str1[1])) {
/* 144 */           List<String> oneIndSNPs = new ArrayList();
/* 145 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 147 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 148 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else
/* 150 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Map<String, Double> missingRate;
/*     */   public Map<String, Double> getProHetreo() {
/* 157 */     Map<String, Double> proHetreo = new HashMap();
/* 158 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 159 */       String s = (String)is.next();
/* 160 */       int heteroCount = 0;
/* 161 */       for (int i = 0; i < ((List)this.rawData.get(s)).size(); i++) {
/* 162 */         if ((!((String)((List)this.rawData.get(s)).get(i)).equals("NA")) && 
/* 163 */           (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1))) {
/* 164 */           heteroCount++;
/*     */         }
/*     */       }
/*     */       
/* 168 */       proHetreo.put(s, Double.valueOf(heteroCount / ((List)this.rawData.get(s)).size()));
/*     */     }
/*     */     
/* 171 */     return proHetreo;
/*     */   }
/*     */   
/*     */   Map<String, Double> maf;
/*     */   public Map<String, Double> calculateMissing()
/*     */   {
/* 177 */     this.missingRate = new HashMap();
/* 178 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 179 */       int count = 0;
/* 180 */       int ind = 0;
/* 181 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 182 */         String s = (String)is.next();
/* 183 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 184 */           ind++;
/* 185 */           if (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */         }
/*     */       }
/* 188 */       this.missingRate.put(this.snpInfo[0].get(i).toString(), Double.valueOf(count / ind));
/*     */     }
/* 190 */     return this.missingRate;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Double> calculateMaf()
/*     */   {
/* 196 */     this.maf = new HashMap();
/* 197 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 198 */       int count = 0;
/* 199 */       int ind = 0;
/* 200 */       Character a = null;
/* 201 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 202 */         String s = (String)is.next();
/* 203 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 204 */           ind++;
/* 205 */           if (a == null) {
/* 206 */             a = Character.valueOf(((String)((List)this.rawData.get(s)).get(i)).charAt(0));
/* 207 */             count++;
/* 208 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */           else {
/* 211 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(0)) count++;
/* 212 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */         }
/*     */       }
/* 216 */       double aFre = count / (ind * 2);
/* 217 */       if (aFre > 0.5D) this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(1.0D - aFre)); else
/* 218 */         this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(aFre));
/*     */     }
/* 220 */     return this.maf;
/*     */   }
/*     */   
/*     */   public void printMAF(PrintStream ps) {
/* 224 */     for (int i = 0; i < this.maf.size(); i++) {
/* 225 */       for (Iterator<String> is = this.maf.keySet().iterator(); is.hasNext();) {
/* 226 */         String s = (String)is.next();
/* 227 */         ps.println(s + " " + this.maf.get(s));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int[] getNoInd(List<Character> snpGeno)
/*     */   {
/* 234 */     int[] temp = new int[2];
/* 235 */     temp[0] = (Math.round(snpGeno.size() / 277) * 77);
/* 236 */     temp[1] = (Math.round(snpGeno.size() / 277) * 23);
/* 237 */     return temp;
/*     */   }
/*     */   
/*     */   public List<Integer> getRandomIndex(int noInd) {
/* 241 */     List<Integer> randomIndex = new ArrayList();
/* 242 */     SortedMap<Double, Integer> temp = new TreeMap();
/* 243 */     Random ran = new Random();
/* 244 */     int k = 0;
/* 245 */     while (k < noInd) {
/* 246 */       Double tp = Double.valueOf(ran.nextDouble());
/* 247 */       if (!temp.containsKey(tp)) {
/* 248 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 249 */         k++;
/*     */       }
/*     */     }
/* 252 */     for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 253 */       Double nt = (Double)it.next();
/* 254 */       randomIndex.add((Integer)temp.get(nt));
/*     */     }
/* 256 */     if (randomIndex.size() != noInd) throw new RuntimeException("!!");
/* 257 */     return randomIndex;
/*     */   }
/*     */   
/*     */ 
/* 261 */   Map<String, String> AalleleHapmap = null;
/*     */   
/* 263 */   public void getAalleleHapmap(File f) throws Exception { this.AalleleHapmap = new HashMap();
/* 264 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 266 */     while ((st = br.readLine()) != null) { String st;
/* 267 */       this.AalleleHapmap.put(st.split("\\s+")[0], st.split("\\s+")[1]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/* 275 */   List<String> heterozygousSite = new ArrayList();
/*     */   int[] noExportInd;
/*     */   
/*     */   public void ChiamoDataCollection(File bc, File nbs, String dir, int numChr, String maskRate) throws Exception {
/* 279 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 280 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 281 */     List<String> indivBC = getIndiv(bc);
/* 282 */     List<String> indivNBS = getIndiv(nbs);
/* 283 */     int noInd = this.maleID.size() - this.excludeMale.size();
/*     */     
/* 285 */     this.dest = new FileOutputStream(dir + "X_" + maskRate + ".zip");
/* 286 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 287 */     this.osw = new OutputStreamWriter(this.outS);
/* 288 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 289 */       List<Character> snpGeno = new ArrayList();
/* 290 */       List<Double> snpCall = new ArrayList();
/* 291 */       String al = this.snpInfo[2].get(i).toString();
/* 292 */       if (al.length() != 1) { throw new RuntimeException("!!" + al);
/*     */       }
/* 294 */       List<Character> checkGeno = null;
/* 295 */       char a; char a; if ((this.AalleleHapmap != null) && (this.AalleleHapmap.containsKey(this.snpInfo[0].get(i)))) {
/* 296 */         checkGeno = new ArrayList();
/* 297 */         String g = (String)this.AalleleHapmap.get(this.snpInfo[0].get(i));
/* 298 */         checkGeno.add(Character.valueOf(g.charAt(0)));checkGeno.add(Character.valueOf(g.charAt(2)));
/* 299 */         a = g.charAt(0);
/*     */       } else {
/* 301 */         a = al.charAt(0); }
/* 302 */       for (int k = 0; k < indivBC.size(); k++) {
/* 303 */         String[] str1 = br1.readLine().split("\\s+");
/* 304 */         if (checkGeno != null) {
/* 305 */           if (!checkGeno.contains(Character.valueOf(str1[2].charAt(0)))) throw new RuntimeException("!!");
/* 306 */           if (!checkGeno.contains(Character.valueOf(str1[2].charAt(1)))) throw new RuntimeException("!!");
/*     */         }
/* 308 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 309 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 310 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 311 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 312 */             snpGeno.add(Character.valueOf('N'));
/* 313 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 316 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 317 */               snpGeno.add(Character.valueOf('B'));
/* 318 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/* 322 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 323 */         String[] str1 = br2.readLine().split("\\s+");
/* 324 */         if (checkGeno != null) {
/* 325 */           if (!checkGeno.contains(Character.valueOf(str1[2].charAt(0)))) throw new RuntimeException("!!");
/* 326 */           if (!checkGeno.contains(Character.valueOf(str1[2].charAt(1)))) throw new RuntimeException("!!");
/*     */         }
/* 328 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 329 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 330 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 331 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 332 */             snpGeno.add(Character.valueOf('N'));
/* 333 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 336 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 337 */               snpGeno.add(Character.valueOf('B'));
/* 338 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 343 */       if ((!this.excludeSNPs.contains(this.snpInfo[0].get(i).toString())) && (!this.maskSNPs.contains(this.snpInfo[0].get(i).toString())))
/*     */       {
/* 345 */         if (snpGeno.size() != noInd) throw new RuntimeException("!!");
/* 346 */         ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 347 */         this.outS.putNextEntry(headings);
/* 348 */         for (int m = 0; m < Math.round(snpGeno.size() / numChr); m++) {
/* 349 */           Double callMulti = Double.valueOf(1.0D);
/* 350 */           for (int c = 0; c < numChr; c++)
/*     */           {
/*     */ 
/* 353 */             this.osw.write(((Character)snpGeno.get(m * numChr + c)).charValue());
/* 354 */             callMulti = Double.valueOf(callMulti.doubleValue() * ((Double)snpCall.get(m * numChr + c)).doubleValue());
/*     */           }
/* 356 */           this.osw.write("\t" + callMulti + "\n");
/*     */         }
/* 358 */         this.osw.flush();
/* 359 */         this.outS.closeEntry();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 454 */     ZipEntry headings = new ZipEntry("Samples");
/* 455 */     this.outS.putNextEntry(headings);
/* 456 */     List<String> indiv = new ArrayList();
/* 457 */     for (int i = 0; i < indivBC.size(); i++) {
/* 458 */       if ((this.maleID.contains(indivBC.get(i))) && (!this.excludeMale.contains(indivBC.get(i))))
/* 459 */         indiv.add((String)indivBC.get(i));
/*     */     }
/* 461 */     for (int i = 0; i < indivNBS.size(); i++) {
/* 462 */       if ((this.maleID.contains(indivNBS.get(i))) && (!this.excludeMale.contains(indivNBS.get(i)))) {
/* 463 */         indiv.add((String)indivNBS.get(i));
/*     */       }
/*     */     }
/* 466 */     for (int i = 0; i < Math.round(indiv.size() / numChr); i++) {
/* 467 */       this.osw.write((String)indiv.get(i * numChr) + "\n");
/*     */     }
/* 469 */     this.osw.flush();
/* 470 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeSnps()
/*     */     throws Exception
/*     */   {
/* 498 */     ZipEntry headings = new ZipEntry("Snps");
/* 499 */     this.outS.putNextEntry(headings);
/* 500 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 501 */       if ((!this.excludeSNPs.contains(this.snpInfo[0].get(i))) && (!this.maskSNPs.contains(this.snpInfo[0].get(i).toString())))
/*     */       {
/* 503 */         this.osw.write("chrX\t");this.osw.write(this.snpInfo[1].get(i).toString() + "\t");
/* 504 */         this.osw.write(Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40 + "\t");
/* 505 */         this.osw.write(this.snpInfo[0].get(i).toString());
/* 506 */         this.osw.write("\n");
/*     */       }
/*     */     }
/* 509 */     this.osw.flush();
/* 510 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 514 */     ZipEntry headings = new ZipEntry("Name");
/* 515 */     this.outS.putNextEntry(headings);
/* 516 */     this.osw.write("genotype callingScore\n");
/* 517 */     this.osw.write("chr start end snpID\n");
/* 518 */     this.osw.write("sampleID");
/* 519 */     this.osw.flush();
/* 520 */     this.outS.closeEntry();
/* 521 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public List<String[]> getPotato(File f) throws Exception
/*     */   {
/* 526 */     List<String[]> potato = new ArrayList();
/* 527 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 529 */     while ((st = br.readLine()) != null) { String st;
/* 530 */       String[] str = st.split("\\s+");
/* 531 */       potato.add(str);
/*     */     }
/* 533 */     return potato;
/*     */   }
/*     */   
/*     */ 
/* 537 */   List<String> maskSNPs = null;
/*     */   
/* 539 */   public void getMaskSNPs(File f) throws Exception { this.maskSNPs = new ArrayList();
/* 540 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 542 */     while ((st = br.readLine()) != null) { String st;
/* 543 */       String[] str = st.split("\\s+");
/* 544 */       this.maskSNPs.add(str[3]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void ChiamoDataCollection(int numChr) throws Exception
/*     */   {
/* 550 */     List<String[]> potato = getPotato(new File("potatoGenotype.txt"));
/* 551 */     this.dest = new FileOutputStream("5.zip");
/* 552 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 553 */     this.osw = new OutputStreamWriter(this.outS);
/* 554 */     for (int i = 0; i < ((String[])potato.get(0)).length; i++) {
/* 555 */       ZipEntry headings = new ZipEntry("snp" + i);
/* 556 */       this.outS.putNextEntry(headings);
/* 557 */       String a = ((String[])potato.get(0))[i];
/* 558 */       for (int k = 0; k < potato.size() / numChr; k++) {
/* 559 */         for (int c = 0; c < 4; c++) {
/* 560 */           if (((String[])potato.get(c + k * numChr))[i].equals(a)) this.osw.write("A"); else
/* 561 */             this.osw.write("B");
/*     */         }
/* 563 */         this.osw.write("\n");
/*     */       }
/* 565 */       this.osw.flush();
/* 566 */       this.outS.closeEntry();
/*     */     }
/*     */     
/* 569 */     ZipEntry headings = new ZipEntry("Samples");
/* 570 */     this.outS.putNextEntry(headings);
/* 571 */     for (int k = 0; k < potato.size() / numChr; k++) {
/* 572 */       this.osw.write("ind" + k + "\n");
/*     */     }
/* 574 */     this.osw.flush();
/* 575 */     this.outS.closeEntry();
/*     */     
/* 577 */     headings = new ZipEntry("Snps");
/* 578 */     this.outS.putNextEntry(headings);
/* 579 */     for (int i = 0; i < ((String[])potato.get(0)).length; i++) {
/* 580 */       this.osw.write("chr5\t");this.osw.write(10000 + i * 300 + "\t");
/* 581 */       this.osw.write(10000 + i * 300 + 40 + "\t");
/* 582 */       this.osw.write("snp" + i);
/* 583 */       this.osw.write("\n");
/*     */     }
/* 585 */     this.osw.flush();
/* 586 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public List<String[]> getPotatoAdd(File f) throws Exception
/*     */   {
/* 591 */     List<String[]> potato = new ArrayList();
/* 592 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 594 */     while ((st = br.readLine()) != null) { String st;
/* 595 */       String[] str = st.split("\\s+");
/* 596 */       potato.add(str);
/*     */     }
/* 598 */     return potato;
/*     */   }
/*     */   
/* 601 */   public void ChiamoDataCollectionAdd(int numChr) throws Exception { List<String[]> potato = getPotato(new File("potatoGenotypeAdd.txt"));
/* 602 */     this.dest = new FileOutputStream("5a.zip");
/* 603 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 604 */     this.osw = new OutputStreamWriter(this.outS);
/* 605 */     for (int i = 2; i < ((String[])potato.get(0)).length; i++) {
/* 606 */       ZipEntry headings = new ZipEntry("snp" + (i - 2));
/* 607 */       this.outS.putNextEntry(headings);
/* 608 */       char a = ((String[])potato.get(0))[i].charAt(0);
/* 609 */       for (int k = 0; k < potato.size(); k++) {
/* 610 */         for (int c = 0; c < 4; c++) {
/* 611 */           if (((String[])potato.get(k))[i].charAt(c) == a) this.osw.write("A"); else
/* 612 */             this.osw.write("B");
/*     */         }
/* 614 */         this.osw.write("\n");
/*     */       }
/* 616 */       this.osw.flush();
/* 617 */       this.outS.closeEntry();
/*     */     }
/*     */     
/* 620 */     ZipEntry headings = new ZipEntry("Samples");
/* 621 */     this.outS.putNextEntry(headings);
/* 622 */     for (int k = 0; k < potato.size(); k++) {
/* 623 */       this.osw.write("ind" + k + "\n");
/*     */     }
/* 625 */     this.osw.flush();
/* 626 */     this.outS.closeEntry();
/*     */     
/* 628 */     headings = new ZipEntry("Snps");
/* 629 */     this.outS.putNextEntry(headings);
/* 630 */     for (int i = 0; i < ((String[])potato.get(0)).length - 2; i++) {
/* 631 */       this.osw.write("chr5\t");this.osw.write(10000 + i * 300 + "\t");
/* 632 */       this.osw.write(10000 + i * 300 + 40 + "\t");
/* 633 */       this.osw.write("snp" + i);
/* 634 */       this.osw.write("\n");
/*     */     }
/* 636 */     this.osw.flush();
/* 637 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeNamePotato() throws Exception {
/* 641 */     ZipEntry headings = new ZipEntry("Name");
/* 642 */     this.outS.putNextEntry(headings);
/* 643 */     this.osw.write("genotype\n");
/* 644 */     this.osw.write("chr start end snpID\n");
/* 645 */     this.osw.write("sampleID");
/* 646 */     this.osw.flush();
/* 647 */     this.outS.closeEntry();
/* 648 */     this.outS.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 708 */       ChiamoCompressX cc = new ChiamoCompressX();
/*     */       
/*     */ 
/* 711 */       cc.getMaleID(new File("sample/Affx_20070205fs1_sample_58C.txt"));
/* 712 */       cc.getMaleID(new File("sample/Affx_20070205fs1_sample_NBS.txt"));
/* 713 */       cc.getSNPinfo(new File("X_snp.txt"));
/* 714 */       cc.getExcludeMale(new File("excludeIndMale.txt"));
/* 715 */       cc.getExcludeSNPs(new File("excludeMarkers1.txt"));
/* 716 */       cc.getMAF(new File("MAFall"));
/* 717 */       cc.getMaskSNPs(new File("marked_" + args[2]));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 723 */       cc.getAalleleHapmap(new File("Hapmap/genotypes_chrX_CEU_r23a_nr.b36_fwd.txt/genotypes_chrX_CEU_r23a_nr.b36_fwd.txt"));
/*     */       
/* 725 */       cc.ChiamoDataCollection(new File("genotype/Affx_20070205fs1_gt_58C_Chiamo_X.txt"), new File("genotype/Affx_20070205fs1_gt_NBS_Chiamo_X.txt"), args[0], Integer.parseInt(args[1]), args[2]);
/* 726 */       cc.writeSnps();
/* 727 */       cc.writeName();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 742 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/ChiamoCompressX.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */