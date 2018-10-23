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
/*     */ public class NfbcCompress
/*     */ {
/*     */   public void getSNPposition(File f)
/*     */     throws Exception
/*     */   {
/*  29 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  31 */     while ((st = br.readLine()) != null) { String st;
/*  32 */       String[] str = st.split("\\s+");
/*  33 */       if (str[1].equals("X")) {
/*  34 */         System.out.println(str[0] + "\t\t" + str[1] + "\t\t" + str[2]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  40 */   List<String> maleID = new ArrayList();
/*     */   
/*  42 */   public void getMaleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  44 */     while ((st = br.readLine()) != null) { String st;
/*  45 */       String[] str = st.split("\\s+");
/*  46 */       if (str[1].equals("1")) {
/*  47 */         this.maleID.add(str[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeMale;
/*     */   public void getExcludeMale(File f) throws Exception
/*     */   {
/*  55 */     this.excludeMale = new ArrayList();
/*  56 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  58 */     while ((st = br.readLine()) != null) { String st;
/*  59 */       this.excludeMale.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getExcludeSNPs(File f)
/*     */     throws Exception
/*     */   {
/*  66 */     this.excludeSNPs = new ArrayList();
/*  67 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  69 */     while ((st = br.readLine()) != null) { String st;
/*  70 */       this.excludeSNPs.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(File f)
/*     */     throws Exception
/*     */   {
/*  77 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  79 */     String start = null;
/*  80 */     List<String> indivID = new ArrayList();
/*  81 */     String st; while ((st = br.readLine()) != null) { String st;
/*  82 */       String[] str = st.split("\\s+");
/*  83 */       if (start == null) {
/*  84 */         start = str[0];
/*     */       } else
/*  86 */         if (!st.startsWith(start)) break;
/*  87 */       indivID.add(str[1]);
/*     */     }
/*  89 */     br.close();
/*  90 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/*  95 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  96 */     ArrayList<Integer> loc = new ArrayList();
/*  97 */     ArrayList<String> snp = new ArrayList();
/*  98 */     ArrayList<String> A_allele = new ArrayList();
/*  99 */     this.snpInfo = new List[] { snp, loc, A_allele };
/*     */     String st;
/* 101 */     while ((st = br.readLine()) != null) { String st;
/* 102 */       String[] str = st.split("\\s+");
/* 103 */       snp.add(str[0]);
/* 104 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/* 105 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeSNPs;
/*     */   List[] snpInfo;
/*     */   public void getMAF(File f)
/*     */     throws Exception
/*     */   {
/* 114 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String[] s;
/* 116 */     label71: String st; for (; (st = br.readLine()) != null; 
/*     */         
/*     */ 
/* 119 */         this.excludeSNPs.add(s[0]))
/*     */     {
/*     */       String st;
/* 117 */       s = st.split("\\s+");
/*     */       
/* 119 */       if ((Double.parseDouble(s[1]) >= 0.01D) || (this.excludeSNPs.contains(s[0])))
/*     */         break label71;
/*     */     }
/*     */   }
/*     */   
/* 124 */   Map<String, List<String>> rawData = new HashMap();
/*     */   
/* 126 */   public void ChiamoDataRaw(File bc, File nbs, double cutpoint) throws Exception { BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 127 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 128 */     List<String> indivBC = getIndiv(bc);
/* 129 */     List<String> indivNBS = getIndiv(nbs);
/* 130 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 131 */       String al = this.snpInfo[2].get(i).toString();
/* 132 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 133 */       char a = al.charAt(0);
/* 134 */       for (int k = 0; k < indivBC.size(); k++) {
/* 135 */         String[] str1 = br1.readLine().split("\\s+");
/* 136 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 137 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 138 */         if (!this.rawData.containsKey(str1[1])) {
/* 139 */           List<String> oneIndSNPs = new ArrayList();
/* 140 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 142 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 143 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else {
/* 145 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */         }
/*     */       }
/* 148 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 149 */         String[] str1 = br2.readLine().split("\\s+");
/* 150 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 151 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 152 */         if (!this.rawData.containsKey(str1[1])) {
/* 153 */           List<String> oneIndSNPs = new ArrayList();
/* 154 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 156 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 157 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else
/* 159 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Map<String, Double> missingRate;
/*     */   public Map<String, Double> getProHetreo() {
/* 166 */     Map<String, Double> proHetreo = new HashMap();
/* 167 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 168 */       String s = (String)is.next();
/* 169 */       int heteroCount = 0;
/* 170 */       for (int i = 0; i < ((List)this.rawData.get(s)).size(); i++) {
/* 171 */         if ((!((String)((List)this.rawData.get(s)).get(i)).equals("NA")) && 
/* 172 */           (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1))) {
/* 173 */           heteroCount++;
/*     */         }
/*     */       }
/*     */       
/* 177 */       proHetreo.put(s, Double.valueOf(heteroCount / ((List)this.rawData.get(s)).size()));
/*     */     }
/*     */     
/* 180 */     return proHetreo;
/*     */   }
/*     */   
/*     */   Map<String, Double> maf;
/*     */   public Map<String, Double> calculateMissing()
/*     */   {
/* 186 */     this.missingRate = new HashMap();
/* 187 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 188 */       int count = 0;
/* 189 */       int ind = 0;
/* 190 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 191 */         String s = (String)is.next();
/* 192 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 193 */           ind++;
/* 194 */           if (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */         }
/*     */       }
/* 197 */       this.missingRate.put(this.snpInfo[0].get(i).toString(), Double.valueOf(count / ind));
/*     */     }
/* 199 */     return this.missingRate;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Double> calculateMaf()
/*     */   {
/* 205 */     this.maf = new HashMap();
/* 206 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 207 */       int count = 0;
/* 208 */       int ind = 0;
/* 209 */       Character a = null;
/* 210 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 211 */         String s = (String)is.next();
/* 212 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 213 */           ind++;
/* 214 */           if (a == null) {
/* 215 */             a = Character.valueOf(((String)((List)this.rawData.get(s)).get(i)).charAt(0));
/* 216 */             count++;
/* 217 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */           else {
/* 220 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(0)) count++;
/* 221 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */         }
/*     */       }
/* 225 */       double aFre = count / (ind * 2);
/* 226 */       if (aFre > 0.5D) this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(1.0D - aFre)); else
/* 227 */         this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(aFre));
/*     */     }
/* 229 */     return this.maf;
/*     */   }
/*     */   
/*     */   public void printMAF(PrintStream ps) {
/* 233 */     for (int i = 0; i < this.maf.size(); i++) {
/* 234 */       for (Iterator<String> is = this.maf.keySet().iterator(); is.hasNext();) {
/* 235 */         String s = (String)is.next();
/* 236 */         ps.println(s + " " + this.maf.get(s));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int[] getNoInd(List<Character> snpGeno)
/*     */   {
/* 243 */     int[] temp = new int[2];
/* 244 */     temp[0] = (Math.round(snpGeno.size() / 277) * 77);
/* 245 */     temp[1] = (Math.round(snpGeno.size() / 277) * 23);
/* 246 */     return temp;
/*     */   }
/*     */   
/*     */   public List<Integer> getRandomIndex(int noInd) {
/* 250 */     List<Integer> randomIndex = new ArrayList();
/* 251 */     SortedMap<Double, Integer> temp = new TreeMap();
/* 252 */     Random ran = new Random();
/* 253 */     int k = 0;
/* 254 */     while (k < noInd) {
/* 255 */       Double tp = Double.valueOf(ran.nextDouble());
/* 256 */       if (!temp.containsKey(tp)) {
/* 257 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 258 */         k++;
/*     */       }
/*     */     }
/* 261 */     for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 262 */       Double nt = (Double)it.next();
/* 263 */       randomIndex.add((Integer)temp.get(nt));
/*     */     }
/* 265 */     if (randomIndex.size() != noInd) throw new RuntimeException("!!");
/* 266 */     return randomIndex;
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/* 273 */   List<String> heterozygousSite = new ArrayList();
/*     */   int[] noExportInd;
/*     */   
/*     */   public void ChiamoDataCollection(File bc, File nbs, String dir, int numChr, int repIndex) throws Exception {
/* 277 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 278 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 279 */     List<String> indivBC = getIndiv(bc);
/* 280 */     List<String> indivNBS = getIndiv(nbs);
/* 281 */     int noInd = this.maleID.size() - this.excludeMale.size();
/* 282 */     List<Integer> randomIndex = getRandomIndex(noInd);
/* 283 */     this.dest = new FileOutputStream(dir + "X_" + repIndex + ".zip");
/* 284 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 285 */     this.osw = new OutputStreamWriter(this.outS);
/* 286 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 287 */       List<Character> snpGeno = new ArrayList();
/* 288 */       List<Double> snpCall = new ArrayList();
/* 289 */       String al = this.snpInfo[2].get(i).toString();
/* 290 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 291 */       char a = al.charAt(0);
/* 292 */       for (int k = 0; k < indivBC.size(); k++) {
/* 293 */         String[] str1 = br1.readLine().split("\\s+");
/* 294 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 295 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 296 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 297 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 298 */             snpGeno.add(Character.valueOf('N'));
/* 299 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 302 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 303 */               snpGeno.add(Character.valueOf('B'));
/* 304 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/* 308 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 309 */         String[] str1 = br2.readLine().split("\\s+");
/* 310 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 311 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 312 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 313 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 314 */             snpGeno.add(Character.valueOf('N'));
/* 315 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 318 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 319 */               snpGeno.add(Character.valueOf('B'));
/* 320 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 325 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i).toString())) {
/* 326 */         if (snpGeno.size() != noInd) throw new RuntimeException("!!");
/* 327 */         ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 328 */         this.outS.putNextEntry(headings);
/* 329 */         for (int m = 0; m < Math.round(snpGeno.size() / numChr); m++) {
/* 330 */           Double callMulti = Double.valueOf(1.0D);
/* 331 */           for (int c = 0; c < numChr; c++) {
/* 332 */             this.osw.write(((Character)snpGeno.get(((Integer)randomIndex.get(m * numChr + c)).intValue())).charValue());
/* 333 */             callMulti = Double.valueOf(callMulti.doubleValue() * ((Double)snpCall.get(((Integer)randomIndex.get(m * numChr + c)).intValue())).doubleValue());
/*     */           }
/* 335 */           this.osw.write("\t" + callMulti + "\n");
/*     */         }
/* 337 */         this.osw.flush();
/* 338 */         this.outS.closeEntry();
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
/* 433 */     ZipEntry headings = new ZipEntry("Samples");
/* 434 */     this.outS.putNextEntry(headings);
/* 435 */     List<String> indiv = new ArrayList();
/* 436 */     for (int i = 0; i < indivBC.size(); i++) {
/* 437 */       if ((this.maleID.contains(indivBC.get(i))) && (!this.excludeMale.contains(indivBC.get(i))))
/* 438 */         indiv.add((String)indivBC.get(i));
/*     */     }
/* 440 */     for (int i = 0; i < indivNBS.size(); i++) {
/* 441 */       if ((this.maleID.contains(indivNBS.get(i))) && (!this.excludeMale.contains(indivNBS.get(i)))) {
/* 442 */         indiv.add((String)indivNBS.get(i));
/*     */       }
/*     */     }
/* 445 */     for (int i = 0; i < Math.round(indiv.size() / numChr); i++) {
/* 446 */       this.osw.write((String)indiv.get(i * numChr) + "\n");
/*     */     }
/* 448 */     this.osw.flush();
/* 449 */     this.outS.closeEntry();
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
/* 477 */     ZipEntry headings = new ZipEntry("Snps");
/* 478 */     this.outS.putNextEntry(headings);
/* 479 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 480 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i))) {
/* 481 */         this.osw.write("chrX\t");this.osw.write(this.snpInfo[1].get(i).toString() + "\t");
/* 482 */         this.osw.write(Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40 + "\t");
/* 483 */         this.osw.write(this.snpInfo[0].get(i).toString());
/* 484 */         this.osw.write("\n");
/*     */       }
/*     */     }
/* 487 */     this.osw.flush();
/* 488 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 492 */     ZipEntry headings = new ZipEntry("Name");
/* 493 */     this.outS.putNextEntry(headings);
/* 494 */     this.osw.write("genotype callingScore\n");
/* 495 */     this.osw.write("chr start end snpID\n");
/* 496 */     this.osw.write("sampleID");
/* 497 */     this.osw.flush();
/* 498 */     this.outS.closeEntry();
/* 499 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public List<String[]> getPotato(File f) throws Exception
/*     */   {
/* 504 */     List<String[]> potato = new ArrayList();
/* 505 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 507 */     while ((st = br.readLine()) != null) { String st;
/* 508 */       String[] str = st.split("\\s+");
/* 509 */       potato.add(str);
/*     */     }
/* 511 */     return potato;
/*     */   }
/*     */   
/*     */   public void ChiamoDataCollection(int numChr) throws Exception {
/* 515 */     List<String[]> potato = getPotato(new File("potatoGenotype.txt"));
/* 516 */     this.dest = new FileOutputStream("5.zip");
/* 517 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 518 */     this.osw = new OutputStreamWriter(this.outS);
/* 519 */     for (int i = 0; i < ((String[])potato.get(0)).length; i++) {
/* 520 */       ZipEntry headings = new ZipEntry("snp" + i);
/* 521 */       this.outS.putNextEntry(headings);
/* 522 */       String a = ((String[])potato.get(0))[i];
/* 523 */       for (int k = 0; k < potato.size() / numChr; k++) {
/* 524 */         for (int c = 0; c < 4; c++) {
/* 525 */           if (((String[])potato.get(c + k * numChr))[i].equals(a)) this.osw.write("A"); else
/* 526 */             this.osw.write("B");
/*     */         }
/* 528 */         this.osw.write("\n");
/*     */       }
/* 530 */       this.osw.flush();
/* 531 */       this.outS.closeEntry();
/*     */     }
/*     */     
/* 534 */     ZipEntry headings = new ZipEntry("Samples");
/* 535 */     this.outS.putNextEntry(headings);
/* 536 */     for (int k = 0; k < potato.size() / numChr; k++) {
/* 537 */       this.osw.write("ind" + k + "\n");
/*     */     }
/* 539 */     this.osw.flush();
/* 540 */     this.outS.closeEntry();
/*     */     
/* 542 */     headings = new ZipEntry("Snps");
/* 543 */     this.outS.putNextEntry(headings);
/* 544 */     for (int i = 0; i < ((String[])potato.get(0)).length; i++) {
/* 545 */       this.osw.write("chr5\t");this.osw.write(10000 + i * 300 + "\t");
/* 546 */       this.osw.write(10000 + i * 300 + 40 + "\t");
/* 547 */       this.osw.write("snp" + i);
/* 548 */       this.osw.write("\n");
/*     */     }
/* 550 */     this.osw.flush();
/* 551 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public List<String[]> getPotatoAdd(File f) throws Exception
/*     */   {
/* 556 */     List<String[]> potato = new ArrayList();
/* 557 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 559 */     while ((st = br.readLine()) != null) { String st;
/* 560 */       String[] str = st.split("\\s+");
/* 561 */       potato.add(str);
/*     */     }
/* 563 */     return potato;
/*     */   }
/*     */   
/* 566 */   public void ChiamoDataCollectionAdd(int numChr) throws Exception { List<String[]> potato = getPotato(new File("potatoGenotypeAdd.txt"));
/* 567 */     this.dest = new FileOutputStream("5a.zip");
/* 568 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 569 */     this.osw = new OutputStreamWriter(this.outS);
/* 570 */     for (int i = 2; i < ((String[])potato.get(0)).length; i++) {
/* 571 */       ZipEntry headings = new ZipEntry("snp" + (i - 2));
/* 572 */       this.outS.putNextEntry(headings);
/* 573 */       char a = ((String[])potato.get(0))[i].charAt(0);
/* 574 */       for (int k = 0; k < potato.size(); k++) {
/* 575 */         for (int c = 0; c < 4; c++) {
/* 576 */           if (((String[])potato.get(k))[i].charAt(c) == a) this.osw.write("A"); else
/* 577 */             this.osw.write("B");
/*     */         }
/* 579 */         this.osw.write("\n");
/*     */       }
/* 581 */       this.osw.flush();
/* 582 */       this.outS.closeEntry();
/*     */     }
/*     */     
/* 585 */     ZipEntry headings = new ZipEntry("Samples");
/* 586 */     this.outS.putNextEntry(headings);
/* 587 */     for (int k = 0; k < potato.size(); k++) {
/* 588 */       this.osw.write("ind" + k + "\n");
/*     */     }
/* 590 */     this.osw.flush();
/* 591 */     this.outS.closeEntry();
/*     */     
/* 593 */     headings = new ZipEntry("Snps");
/* 594 */     this.outS.putNextEntry(headings);
/* 595 */     for (int i = 0; i < ((String[])potato.get(0)).length - 2; i++) {
/* 596 */       this.osw.write("chr5\t");this.osw.write(10000 + i * 300 + "\t");
/* 597 */       this.osw.write(10000 + i * 300 + 40 + "\t");
/* 598 */       this.osw.write("snp" + i);
/* 599 */       this.osw.write("\n");
/*     */     }
/* 601 */     this.osw.flush();
/* 602 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeNamePotato() throws Exception {
/* 606 */     ZipEntry headings = new ZipEntry("Name");
/* 607 */     this.outS.putNextEntry(headings);
/* 608 */     this.osw.write("genotype\n");
/* 609 */     this.osw.write("chr start end snpID\n");
/* 610 */     this.osw.write("sampleID");
/* 611 */     this.osw.flush();
/* 612 */     this.outS.closeEntry();
/* 613 */     this.outS.close();
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
/* 673 */       NfbcCompress cc = new NfbcCompress();
/* 674 */       cc.getSNPposition(new File("lcoin_export.ai4Kdo.txt"));
/* 675 */       cc.ChiamoDataCollectionAdd(Integer.parseInt(args[1]));
/* 676 */       cc.writeNamePotato();
/*     */       
/* 678 */       cc.getMaleID(new File("sample/Affx_20070205fs1_sample_58C.txt"));
/* 679 */       cc.getMaleID(new File("sample/Affx_20070205fs1_sample_NBS.txt"));
/* 680 */       cc.getSNPinfo(new File("X_snp.txt"));
/* 681 */       cc.getExcludeMale(new File("excludeIndMale.txt"));
/* 682 */       cc.getExcludeSNPs(new File("excludeMarkers1.txt"));
/* 683 */       cc.getMAF(new File("MAFall"));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 689 */       for (int r = 100; r < 101; r++) {
/* 690 */         cc.ChiamoDataCollection(new File("genotype/Affx_20070205fs1_gt_58C_Chiamo_X.txt"), new File("genotype/Affx_20070205fs1_gt_NBS_Chiamo_X.txt"), args[0], Integer.parseInt(args[1]), r);
/* 691 */         cc.writeSnps();
/* 692 */         cc.writeName();
/*     */ 
/*     */ 
/*     */ 
/*     */       }
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
/* 707 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/NfbcCompress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */