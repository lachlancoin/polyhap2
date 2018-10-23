/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ public class MergeCNV
/*     */ {
/*  24 */   Map<String, List<Integer[]>> cnvData = new HashMap();
/*     */   FileOutputStream dest;
/*     */   
/*     */   public void getCNVdata(File f) throws Exception {
/*  28 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  30 */     String st = br.readLine();
/*     */     
/*  32 */     while ((st = br.readLine()) != null) {
/*  33 */       String[] str = st.split("\t");
/*  34 */       if (!str[9].equals("X")) throw new RuntimeException("not X chromosome");
/*  35 */       double lr = Double.parseDouble(str[22]);
/*  36 */       int cn; int cn; if (lr <= -0.5D) { cn = 0;
/*     */       } else
/*  38 */         cn = 2;
/*  39 */       if (!this.cnvData.containsKey(str[6])) {
/*  40 */         this.cnvData.put(str[6], new ArrayList());
/*  41 */         ((List)this.cnvData.get(str[6])).add(new Integer[] { Integer.valueOf(Integer.parseInt(str[10])), Integer.valueOf(Integer.parseInt(str[11])), Integer.valueOf(cn) });
/*     */ 
/*     */       }
/*  44 */       else if (((List)this.cnvData.get(str[6])).isEmpty()) {
/*  45 */         ((List)this.cnvData.get(str[6])).add(new Integer[] { Integer.valueOf(Integer.parseInt(str[10])), Integer.valueOf(Integer.parseInt(str[11])), Integer.valueOf(cn) });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getSNP(ZipFile f, String entryName, Integer column) throws Exception
/*     */   {
/*  52 */     BufferedReader nxt = 
/*  53 */       new BufferedReader(new InputStreamReader(
/*  54 */       f.getInputStream(f.getEntry(entryName))));
/*  55 */     List<String> indiv = new ArrayList();
/*  56 */     String st = "";
/*  57 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*  58 */       indiv.add(st.split("\\s+")[column.intValue()]);
/*     */     }
/*  60 */     nxt.close();
/*  61 */     return indiv;
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(ZipFile f, String entryName, Integer column) throws Exception {
/*  65 */     BufferedReader nxt = 
/*  66 */       new BufferedReader(new InputStreamReader(
/*  67 */       f.getInputStream(f.getEntry(entryName))));
/*  68 */     List<String> indiv = new ArrayList();
/*  69 */     String st = "";
/*  70 */     for (int k = 0; (st = nxt.readLine()) != null; k++) {
/*  71 */       indiv.add(st.split("\\s+")[column.intValue()].split("#")[0]);
/*     */     }
/*  73 */     nxt.close();
/*  74 */     return indiv;
/*     */   }
/*     */   
/*     */   public int getCN(String ind, Integer snploc)
/*     */   {
/*  79 */     int cn = -1;
/*  80 */     for (int i = 0; i < ((List)this.cnvData.get(ind)).size(); i++) {
/*  81 */       Integer[] cnvloc = (Integer[])((List)this.cnvData.get(ind)).get(i);
/*  82 */       if ((snploc.intValue() >= cnvloc[0].intValue()) && (snploc.intValue() <= cnvloc[1].intValue())) {
/*  83 */         cn = cnvloc[2].intValue();
/*  84 */         break;
/*     */       }
/*     */     }
/*  87 */     return cn;
/*     */   }
/*     */   
/*     */   public String getAlleles(int cn, Double baf, String allele)
/*     */   {
/*  92 */     String alleles = "";
/*  93 */     if (cn == 0) {
/*  94 */       alleles = "_";
/*     */     }
/*  96 */     else if (cn == 1) {
/*  97 */       if (baf.doubleValue() < 0.5D) { alleles = "A";
/*  98 */       } else if (baf.doubleValue() >= 0.5D) alleles = "B";
/*  99 */       if (baf.isNaN()) { throw new RuntimeException("missing baf");
/*     */       }
/*     */       
/*     */     }
/* 103 */     else if (cn == 2) {
/* 104 */       if (baf.doubleValue() <= 0.25D) { alleles = "AA";
/* 105 */       } else if (baf.doubleValue() >= 0.75D) alleles = "BB"; else {
/* 106 */         alleles = "AB";
/*     */ 
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 112 */     else if (cn == 3) {
/* 113 */       if (baf.doubleValue() <= 0.16667D) { alleles = "AAA";
/* 114 */       } else if (baf.doubleValue() >= 0.83333D) { alleles = "BBB";
/* 115 */       } else if ((baf.doubleValue() > 0.16667D) && (baf.doubleValue() <= 0.5D)) alleles = "AAB"; else
/* 116 */         alleles = "ABB";
/* 117 */       if (baf.isNaN()) { throw new RuntimeException("missing baf");
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 122 */     else if (cn == -1) {
/* 123 */       if (allele.equals("A")) { alleles = "A";
/* 124 */       } else if (allele.equals("B")) { alleles = "B";
/* 125 */       } else if ((allele.equals("N")) && (!baf.isNaN())) alleles = getMissingAlleles(baf); else
/* 126 */         alleles = "A";
/*     */     } else {
/* 128 */       throw new RuntimeException("cn is wrong!"); }
/* 129 */     return alleles;
/*     */   }
/*     */   
/*     */   public String getAllelesCode(String allele)
/*     */   {
/* 134 */     String alleles = "";
/* 135 */     if (allele.equals("_")) { alleles = "_";
/* 136 */     } else if (allele.equals("A")) { alleles = "A";
/* 137 */     } else if (allele.equals("B")) { alleles = "B";
/* 138 */     } else if (allele.equals("AA")) { alleles = "X";
/* 139 */     } else if (allele.equals("BB")) { alleles = "Z";
/* 140 */     } else if (allele.equals("AB")) { alleles = "Y";
/* 141 */     } else if (allele.equals("AAA")) { alleles = "T";
/* 142 */     } else if (allele.equals("BBB")) { alleles = "W";
/* 143 */     } else if (allele.equals("AAB")) { alleles = "U";
/* 144 */     } else if (allele.equals("ABB")) alleles = "V"; else
/* 145 */       throw new RuntimeException("cn is wrong!");
/* 146 */     return alleles;
/*     */   }
/*     */   
/*     */   public String getMissingAlleles(Double baf)
/*     */   {
/* 151 */     String alleles = "";
/* 152 */     if (baf.doubleValue() <= 0.5D) { alleles = "A";
/* 153 */     } else if (baf.doubleValue() > 0.5D) alleles = "B";
/* 154 */     return alleles;
/*     */   }
/*     */   
/*     */   public List<Integer> getRan(File f) throws Exception
/*     */   {
/* 159 */     List<Integer> ran = new ArrayList();
/* 160 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 162 */     while ((st = br.readLine()) != null) { String st;
/* 163 */       ran.add(Integer.valueOf(Integer.parseInt(st)));
/*     */     }
/* 165 */     return ran;
/*     */   }
/*     */   
/*     */ 
/*     */   ZipOutputStream outS;
/*     */   
/*     */   OutputStreamWriter osw;
/* 172 */   Map<String, List<String>> oneMdata = new HashMap();
/*     */   Map<String, String[]> phasedData;
/*     */   
/* 175 */   public void get1M(File f, boolean code, String fileName) throws Exception { ZipFile zf = new ZipFile(f);
/* 176 */     List<String> indiv = new ArrayList();
/* 177 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/* 178 */     List<String> snp = new ArrayList();
/* 179 */     snp = getSNP(zf, "SNPS", Integer.valueOf(3));
/* 180 */     List<String> loc = new ArrayList();
/* 181 */     loc = getSNP(zf, "SNPS", Integer.valueOf(1));
/* 182 */     String st = "";
/* 183 */     List<Integer> ranOrder = getRan(new File("ranOrderSample.txt"));
/* 184 */     if (ranOrder.size() != indiv.size()) throw new RuntimeException("!!");
/* 185 */     int sampleSize = Math.round(indiv.size() / 2) * 2;
/*     */     
/* 187 */     this.dest = new FileOutputStream(fileName + ".zip");
/* 188 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 189 */     this.osw = new OutputStreamWriter(this.outS);
/* 190 */     for (int i = 0; i < snp.size(); i++) {
/* 191 */       List<String> dataTemp = new ArrayList();
/* 192 */       List<Boolean> missingTemp = new ArrayList();
/* 193 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/* 194 */       ZipEntry headings = new ZipEntry((String)snp.get(i));
/* 195 */       this.outS.putNextEntry(headings);
/* 196 */       for (int s = 0; s < indiv.size(); s++) {
/* 197 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 198 */         int cn = getCN((String)indiv.get(s), Integer.valueOf(Integer.parseInt((String)loc.get(i))));
/* 199 */         String[] str = st.split("\\s+");
/* 200 */         String allele = getAlleles(cn, Double.valueOf(Double.parseDouble(str[1])), str[0].substring(0, 1));
/* 201 */         dataTemp.add(allele);
/*     */       }
/* 203 */       if (code) {
/* 204 */         for (int s = 0; s < sampleSize; s += 2) {
/* 205 */           String hap1 = (String)dataTemp.get(((Integer)ranOrder.get(s)).intValue());
/* 206 */           String hap2 = (String)dataTemp.get(((Integer)ranOrder.get(s + 1)).intValue());
/* 207 */           if (hap1.equals("_")) {
/* 208 */             this.osw.write(hap1);
/* 209 */             this.osw.write(hap2.charAt(0) + "\n");
/*     */           }
/* 211 */           else if (hap2.equals("_")) {
/* 212 */             this.osw.write(hap1.charAt(0));
/* 213 */             this.osw.write(hap2 + "\n");
/*     */           }
/*     */           else {
/* 216 */             this.osw.write(getAllelesCode(hap1));
/* 217 */             this.osw.write(getAllelesCode(hap2) + "\n");
/*     */           }
/*     */           
/*     */         }
/*     */       } else {
/* 222 */         for (int s = 0; s < sampleSize; s += 2) {
/* 223 */           String hap1 = (String)dataTemp.get(((Integer)ranOrder.get(s)).intValue());
/* 224 */           String hap2 = (String)dataTemp.get(((Integer)ranOrder.get(s + 1)).intValue());
/* 225 */           if (hap1.equals("_")) {
/* 226 */             this.osw.write(hap1);
/* 227 */             this.osw.write(hap2.charAt(0) + "\n");
/*     */           }
/* 229 */           else if (hap2.equals("_")) {
/* 230 */             this.osw.write(hap1.charAt(0));
/* 231 */             this.osw.write(hap2 + "\n");
/*     */           }
/*     */           else {
/* 234 */             this.osw.write(hap1);
/* 235 */             this.osw.write(hap2 + "\n");
/*     */           }
/*     */         }
/*     */       }
/* 239 */       this.osw.flush();
/* 240 */       this.outS.closeEntry();
/*     */     }
/*     */     
/*     */ 
/* 244 */     ZipEntry headings = new ZipEntry("Samples");
/* 245 */     this.outS.putNextEntry(headings);
/* 246 */     for (int s = 0; s < sampleSize; s += 2) {
/* 247 */       String hap1 = (String)indiv.get(((Integer)ranOrder.get(s)).intValue());
/* 248 */       this.osw.write(hap1 + "\n");
/*     */     }
/* 250 */     this.osw.flush();
/* 251 */     this.outS.closeEntry();
/*     */     
/*     */ 
/* 254 */     headings = new ZipEntry("Snps");
/* 255 */     this.outS.putNextEntry(headings);
/* 256 */     for (int i = 0; i < snp.size(); i++) {
/* 257 */       this.osw.write("chrX\t" + (String)loc.get(i) + "\t" + (Integer.parseInt((String)loc.get(i)) + 40) + "\t" + (String)snp.get(i) + "\n");
/*     */     }
/* 259 */     this.osw.flush();
/* 260 */     this.outS.closeEntry();
/*     */     
/*     */ 
/*     */ 
/* 264 */     headings = new ZipEntry("Name");
/* 265 */     this.outS.putNextEntry(headings);
/* 266 */     this.osw.write("genotype\n");
/* 267 */     this.osw.write("chr start end snpID\n");
/* 268 */     this.osw.write("sampleID");
/* 269 */     this.osw.flush();
/* 270 */     this.outS.closeEntry();
/* 271 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public void get1Mmissing(File f, String fileName) throws Exception
/*     */   {
/* 276 */     ZipFile zf = new ZipFile(f);
/* 277 */     List<String> indiv = new ArrayList();
/* 278 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/* 279 */     List<String> snp = new ArrayList();
/* 280 */     snp = getSNP(zf, "Snps", Integer.valueOf(3));
/* 281 */     List<String> loc = new ArrayList();
/* 282 */     loc = getSNP(zf, "Snps", Integer.valueOf(1));
/* 283 */     String st = "";
/* 284 */     Random ran = new Random();
/* 285 */     Boolean missing = null;
/* 286 */     Map<String, List<String>> missingIndex = new HashMap();
/*     */     
/* 288 */     this.dest = new FileOutputStream(fileName + ".zip");
/* 289 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 290 */     this.osw = new OutputStreamWriter(this.outS);
/* 291 */     for (int i = 0; i < snp.size(); i++) {
/* 292 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/* 293 */       ZipEntry headings = new ZipEntry((String)snp.get(i));
/* 294 */       this.outS.putNextEntry(headings);
/* 295 */       for (int s = 0; s < indiv.size(); s++) {
/* 296 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 297 */         if (ran.nextDouble() <= 0.1D) missing = Boolean.valueOf(true); else
/* 298 */           missing = Boolean.valueOf(false);
/* 299 */         String[] str = st.split("\\s+");
/* 300 */         String str1 = str[0].replaceAll("_", "");
/*     */         
/*     */ 
/*     */ 
/* 304 */         if ((str1.length() == 1) && (missing.booleanValue())) {
/* 305 */           for (int c = 0; c < str1.length(); c++) {
/* 306 */             this.osw.write("N");
/*     */           }
/* 308 */           this.osw.write("\n");
/* 309 */           if (missingIndex.containsKey(indiv.get(s))) {
/* 310 */             ((List)missingIndex.get(indiv.get(s))).add((String)loc.get(i));
/*     */           }
/*     */           else {
/* 313 */             missingIndex.put((String)indiv.get(s), new ArrayList());
/* 314 */             ((List)missingIndex.get(indiv.get(s))).add((String)loc.get(i));
/*     */           }
/*     */         }
/*     */         else {
/* 318 */           this.osw.write(str[0] + "\n");
/*     */         }
/*     */       }
/* 321 */       this.osw.flush();
/* 322 */       this.outS.closeEntry();
/*     */     }
/*     */     
/*     */ 
/* 326 */     ZipEntry headings = new ZipEntry("Samples");
/* 327 */     this.outS.putNextEntry(headings);
/* 328 */     for (int s = 0; s < indiv.size(); s++) {
/* 329 */       this.osw.write((String)indiv.get(s) + "\n");
/*     */     }
/* 331 */     this.osw.flush();
/* 332 */     this.outS.closeEntry();
/*     */     
/*     */ 
/* 335 */     headings = new ZipEntry("Snps");
/* 336 */     this.outS.putNextEntry(headings);
/* 337 */     for (int i = 0; i < snp.size(); i++) {
/* 338 */       this.osw.write("chrX\t" + (String)loc.get(i) + "\t" + (Integer.parseInt((String)loc.get(i)) + 40) + "\t" + (String)snp.get(i) + "\n");
/*     */     }
/* 340 */     this.osw.flush();
/* 341 */     this.outS.closeEntry();
/*     */     
/*     */ 
/* 344 */     headings = new ZipEntry("missingIndex");
/* 345 */     this.outS.putNextEntry(headings);
/* 346 */     for (Iterator<String> it = missingIndex.keySet().iterator(); it.hasNext();) {
/* 347 */       String ind = (String)it.next();
/* 348 */       this.osw.write(ind + "\t");
/* 349 */       for (int i = 0; i < ((List)missingIndex.get(ind)).size() - 1; i++) {
/* 350 */         this.osw.write((String)((List)missingIndex.get(ind)).get(i) + "\t");
/*     */       }
/* 352 */       this.osw.write((String)((List)missingIndex.get(ind)).get(((List)missingIndex.get(ind)).size() - 1) + "\n");
/*     */     }
/* 354 */     this.osw.flush();
/* 355 */     this.outS.closeEntry();
/*     */     
/*     */ 
/*     */ 
/* 359 */     headings = new ZipEntry("Name");
/* 360 */     this.outS.putNextEntry(headings);
/* 361 */     this.osw.write("genotype\n");
/* 362 */     this.osw.write("chr start end snpID\n");
/* 363 */     this.osw.write("sampleID");
/* 364 */     this.osw.flush();
/* 365 */     this.outS.closeEntry();
/* 366 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public void get1Minternal(File f, boolean code, String fileName) throws Exception
/*     */   {
/* 371 */     ZipFile zf = new ZipFile(f);
/* 372 */     List<String> indiv = new ArrayList();
/* 373 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/* 374 */     List<String> snp = new ArrayList();
/* 375 */     snp = getSNP(zf, "SNPS", Integer.valueOf(3));
/* 376 */     List<String> loc = new ArrayList();
/* 377 */     loc = getSNP(zf, "SNPS", Integer.valueOf(1));
/* 378 */     String st = "";
/* 379 */     List<Integer> ranOrder = getRan(new File("ranOrderSample.txt"));
/* 380 */     if (ranOrder.size() != indiv.size()) throw new RuntimeException("!!");
/* 381 */     int sampleSize = Math.round(indiv.size() / 3) * 3;
/*     */     
/* 383 */     this.dest = new FileOutputStream(fileName + ".zip");
/* 384 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 385 */     this.osw = new OutputStreamWriter(this.outS);
/* 386 */     for (int i = 0; i < snp.size(); i++) {
/* 387 */       List<String> dataTemp = new ArrayList();
/* 388 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)snp.get(i)))));
/* 389 */       ZipEntry headings = new ZipEntry((String)snp.get(i));
/* 390 */       this.outS.putNextEntry(headings);
/* 391 */       for (int s = 0; s < indiv.size(); s++) {
/* 392 */         if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/* 393 */         int cn = getCN((String)indiv.get(s), Integer.valueOf(Integer.parseInt((String)loc.get(i))));
/* 394 */         String[] str = st.split("\\s+");
/* 395 */         if (cn == 0) { dataTemp.add("_");
/*     */         } else {
/* 397 */           String allele = str[0].substring(0, 1);
/* 398 */           Double baf = Double.valueOf(Double.parseDouble(str[1]));
/* 399 */           if ((allele.equals("N")) && (!baf.isNaN())) { dataTemp.add(getMissingAlleles(baf));
/* 400 */           } else if (allele.equals("N")) dataTemp.add("A"); else
/* 401 */             dataTemp.add(allele);
/*     */         }
/*     */       }
/* 404 */       for (int s = 0; s < sampleSize; s += 3) {
/* 405 */         String hap1 = (String)dataTemp.get(((Integer)ranOrder.get(s)).intValue());
/* 406 */         String hap2 = (String)dataTemp.get(((Integer)ranOrder.get(s + 1)).intValue());
/* 407 */         String hap3 = (String)dataTemp.get(((Integer)ranOrder.get(s + 2)).intValue());
/* 408 */         this.osw.write(hap1);
/* 409 */         this.osw.write(hap2);
/* 410 */         this.osw.write(hap3 + "\n");
/*     */       }
/* 412 */       this.osw.flush();
/* 413 */       this.outS.closeEntry();
/*     */     }
/*     */     
/*     */ 
/* 417 */     ZipEntry headings = new ZipEntry("Samples");
/* 418 */     this.outS.putNextEntry(headings);
/* 419 */     for (int s = 0; s < sampleSize; s += 3) {
/* 420 */       String hap1 = (String)indiv.get(((Integer)ranOrder.get(s)).intValue());
/* 421 */       this.osw.write(hap1 + "\n");
/*     */     }
/* 423 */     this.osw.flush();
/* 424 */     this.outS.closeEntry();
/*     */     
/*     */ 
/* 427 */     headings = new ZipEntry("Snps");
/* 428 */     this.outS.putNextEntry(headings);
/* 429 */     for (int i = 0; i < snp.size(); i++) {
/* 430 */       this.osw.write("chrX\t" + (String)loc.get(i) + "\t" + (Integer.parseInt((String)loc.get(i)) + 40) + "\t" + (String)snp.get(i) + "\n");
/*     */     }
/* 432 */     this.osw.flush();
/* 433 */     this.outS.closeEntry();
/*     */     
/*     */ 
/*     */ 
/* 437 */     headings = new ZipEntry("Name");
/* 438 */     this.outS.putNextEntry(headings);
/* 439 */     this.osw.write("genotype\n");
/* 440 */     this.osw.write("chr start end snpID\n");
/* 441 */     this.osw.write("sampleID");
/* 442 */     this.osw.flush();
/* 443 */     this.outS.closeEntry();
/* 444 */     this.outS.close();
/*     */   }
/*     */   
/*     */   List<String> phasedIndID;
/*     */   List[] snpInfo;
/*     */   public void getPhasedData(File f)
/*     */     throws Exception
/*     */   {
/* 452 */     this.phasedData = new HashMap();
/* 453 */     this.phasedIndID = new ArrayList();
/* 454 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 455 */     String st = br.readLine();
/* 456 */     while (!st.startsWith("#")) {
/* 457 */       st = br.readLine();
/*     */     }
/* 459 */     while (st != null) {
/* 460 */       if (st.startsWith("#")) {
/* 461 */         String[] str = st.split("\\s+");
/* 462 */         List<String> te = new ArrayList();
/* 463 */         st = br.readLine();
/* 464 */         while (!st.startsWith("#")) {
/* 465 */           te.add(st);
/* 466 */           st = br.readLine();
/* 467 */           if (st == null) break;
/*     */         }
/* 469 */         String[] temp = new String[te.size()];
/* 470 */         for (int i = 0; i < temp.length; i++) {
/* 471 */           temp[i] = ((String)te.get(i));
/*     */         }
/* 473 */         this.phasedData.put(str[2].replace("|", ""), temp);
/* 474 */         this.phasedIndID.add(str[2].replace("|", ""));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/* 481 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 482 */     ArrayList<Integer> loc = new ArrayList();
/* 483 */     ArrayList<String> snp = new ArrayList();
/* 484 */     this.snpInfo = new List[] { snp, loc };
/*     */     
/* 486 */     br.readLine();
/* 487 */     String st; while ((st = br.readLine()) != null) { String st;
/* 488 */       String[] str = st.split("\\s+");
/* 489 */       snp.add(str[0].replace(":", "_"));
/* 490 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Integer> getRandomIndex(int noInd) {
/* 495 */     List<Integer> randomIndex = new ArrayList();
/* 496 */     SortedMap<Double, Integer> temp = new java.util.TreeMap();
/* 497 */     Random ran = new Random();
/* 498 */     int k = 0;
/* 499 */     while (k < noInd) {
/* 500 */       Double tp = Double.valueOf(ran.nextDouble());
/* 501 */       if (!temp.containsKey(tp)) {
/* 502 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 503 */         k++;
/*     */       }
/*     */     }
/* 506 */     for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 507 */       Double nt = (Double)it.next();
/* 508 */       randomIndex.add((Integer)temp.get(nt));
/*     */     }
/* 510 */     if (randomIndex.size() != noInd) throw new RuntimeException("!!");
/* 511 */     return randomIndex;
/*     */   }
/*     */   
/*     */   public String getAllelesfromCode(char code)
/*     */   {
/*     */     String alleles;
/* 517 */     if (code == '_') { alleles = "_"; } else { String alleles;
/* 518 */       if (code == 'A') { alleles = "A"; } else { String alleles;
/* 519 */         if (code == 'B') { alleles = "B"; } else { String alleles;
/* 520 */           if (code == 'X') { alleles = "AA"; } else { String alleles;
/* 521 */             if (code == 'Z') { alleles = "BB"; } else { String alleles;
/* 522 */               if (code == 'Y') { alleles = "AB"; } else { String alleles;
/* 523 */                 if (code == 'T') { alleles = "AAA"; } else { String alleles;
/* 524 */                   if (code == 'W') { alleles = "BBB"; } else { String alleles;
/* 525 */                     if (code == 'U') { alleles = "AAB"; } else { String alleles;
/* 526 */                       if (code == 'V') alleles = "ABB"; else
/* 527 */                         throw new RuntimeException("cn is wrong!"); } } } } } } } } }
/* 528 */     String alleles; return alleles;
/*     */   }
/*     */   
/*     */   public void nfbcCompress(List<Integer> randomIndexIndiv, boolean code, String fileName)
/*     */     throws Exception
/*     */   {
/* 534 */     this.dest = new FileOutputStream(fileName + ".zip");
/* 535 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 536 */     this.osw = new OutputStreamWriter(this.outS);
/* 537 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 538 */       ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 539 */       this.outS.putNextEntry(headings);
/* 540 */       for (int s = 0; s < Math.round(this.phasedIndID.size() / 2) * 2; s += 2) {
/* 541 */         if (this.snpInfo[0].size() != ((String[])this.phasedData.get(this.phasedIndID.get(((Integer)randomIndexIndiv.get(s)).intValue())))[0].length()) throw new RuntimeException("!!");
/* 542 */         char allele1 = ((String[])this.phasedData.get(this.phasedIndID.get(((Integer)randomIndexIndiv.get(s)).intValue())))[0].charAt(i);
/* 543 */         char allele2 = ((String[])this.phasedData.get(this.phasedIndID.get(((Integer)randomIndexIndiv.get(s + 1)).intValue())))[0].charAt(i);
/* 544 */         if (code) {
/* 545 */           if (allele1 == '_') {
/* 546 */             this.osw.write(allele1);
/* 547 */             this.osw.write(getAllelesfromCode(allele2).charAt(0) + "\n");
/*     */           }
/* 549 */           else if (allele2 == '_') {
/* 550 */             this.osw.write(getAllelesfromCode(allele1).charAt(0));
/* 551 */             this.osw.write(allele2 + "\n");
/*     */           }
/*     */           else {
/* 554 */             this.osw.write(allele1);
/* 555 */             this.osw.write(allele2 + "\n");
/*     */           }
/*     */           
/*     */         }
/* 559 */         else if (allele1 == '_') {
/* 560 */           this.osw.write(allele1);
/* 561 */           this.osw.write(getAllelesfromCode(allele2).charAt(0) + "\n");
/*     */         }
/* 563 */         else if (allele2 == '_') {
/* 564 */           this.osw.write(getAllelesfromCode(allele1).charAt(0));
/* 565 */           this.osw.write(allele2 + "\n");
/*     */         }
/*     */         else {
/* 568 */           this.osw.write(getAllelesfromCode(allele1));
/* 569 */           this.osw.write(getAllelesfromCode(allele2) + "\n");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 574 */       this.osw.flush();
/* 575 */       this.outS.closeEntry();
/*     */     }
/*     */     
/*     */ 
/* 579 */     ZipEntry headings = new ZipEntry("Samples");
/* 580 */     this.outS.putNextEntry(headings);
/* 581 */     for (int s = 0; s < Math.round(this.phasedIndID.size() / 2) * 2; s += 2) {
/* 582 */       this.osw.write((String)this.phasedIndID.get(((Integer)randomIndexIndiv.get(s)).intValue()) + "\n");
/*     */     }
/* 584 */     this.osw.flush();
/* 585 */     this.outS.closeEntry();
/*     */     
/*     */ 
/* 588 */     headings = new ZipEntry("Snps");
/* 589 */     this.outS.putNextEntry(headings);
/* 590 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 591 */       this.osw.write("chrX\t" + this.snpInfo[1].get(i).toString() + "\t" + (Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40) + "\t" + this.snpInfo[0].get(i).toString() + "\n");
/*     */     }
/* 593 */     this.osw.flush();
/* 594 */     this.outS.closeEntry();
/*     */     
/*     */ 
/*     */ 
/* 598 */     headings = new ZipEntry("Name");
/* 599 */     this.outS.putNextEntry(headings);
/* 600 */     this.osw.write("genotype\n");
/* 601 */     this.osw.write("chr start end snpID\n");
/* 602 */     this.osw.write("sampleID");
/* 603 */     this.osw.flush();
/* 604 */     this.outS.closeEntry();
/* 605 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public void nfbcChr1Compress(boolean code, String fileName)
/*     */     throws Exception
/*     */   {
/* 611 */     this.dest = new FileOutputStream(fileName + ".zip");
/* 612 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 613 */     this.osw = new OutputStreamWriter(this.outS);
/* 614 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 615 */       ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 616 */       this.outS.putNextEntry(headings);
/* 617 */       for (int s = 0; s < this.phasedIndID.size(); s++) {
/* 618 */         if (this.snpInfo[0].size() != ((String[])this.phasedData.get(this.phasedIndID.get(s)))[0].length()) throw new RuntimeException("!!");
/* 619 */         char allele1 = ((String[])this.phasedData.get(this.phasedIndID.get(s)))[0].charAt(i);
/* 620 */         char allele2 = ((String[])this.phasedData.get(this.phasedIndID.get(s)))[1].charAt(i);
/* 621 */         if (code) {
/* 622 */           this.osw.write(allele1);
/* 623 */           this.osw.write(allele2 + "\n");
/*     */         }
/*     */         else {
/* 626 */           this.osw.write(getAllelesfromCode(allele1));
/* 627 */           this.osw.write(getAllelesfromCode(allele2) + "\n");
/*     */         }
/*     */       }
/* 630 */       this.osw.flush();
/* 631 */       this.outS.closeEntry();
/*     */     }
/*     */     
/*     */ 
/* 635 */     ZipEntry headings = new ZipEntry("Samples");
/* 636 */     this.outS.putNextEntry(headings);
/* 637 */     for (int s = 0; s < this.phasedIndID.size(); s++) {
/* 638 */       this.osw.write((String)this.phasedIndID.get(s) + "\n");
/*     */     }
/* 640 */     this.osw.flush();
/* 641 */     this.outS.closeEntry();
/*     */     
/*     */ 
/* 644 */     headings = new ZipEntry("Snps");
/* 645 */     this.outS.putNextEntry(headings);
/* 646 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 647 */       this.osw.write("chr1\t" + this.snpInfo[1].get(i).toString() + "\t" + (Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40) + "\t" + this.snpInfo[0].get(i).toString() + "\n");
/*     */     }
/* 649 */     this.osw.flush();
/* 650 */     this.outS.closeEntry();
/*     */     
/*     */ 
/*     */ 
/* 654 */     headings = new ZipEntry("Name");
/* 655 */     this.outS.putNextEntry(headings);
/* 656 */     this.osw.write("genotype\n");
/* 657 */     this.osw.write("chr start end snpID\n");
/* 658 */     this.osw.write("sampleID");
/* 659 */     this.osw.flush();
/* 660 */     this.outS.closeEntry();
/* 661 */     this.outS.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 670 */       MergeCNV mc = new MergeCNV();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 701 */       mc.get1Mmissing(new File("X_1M_oneCopyDuplication.zip"), "X_1M_oneCopyDuplicationMissing1CN_0.1");
/*     */     }
/*     */     catch (Exception exc) {
/* 704 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/MergeCNV.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */