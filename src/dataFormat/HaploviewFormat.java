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
/*     */ public class HaploviewFormat
/*     */ {
/*  26 */   List<String> maleID = new ArrayList();
/*     */   
/*  28 */   public void getMaleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  30 */     while ((st = br.readLine()) != null) { String st;
/*  31 */       String[] str = st.split("\\s+");
/*  32 */       if (str[1].equals("1"))
/*  33 */         this.maleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeMale;
/*     */   List<String> excludeSNPs;
/*     */   List[] snpInfo;
/*     */   public void getExcludeMale(File f) throws Exception {
/*  41 */     this.excludeMale = new ArrayList();
/*  42 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  44 */     while ((st = br.readLine()) != null) { String st;
/*  45 */       this.excludeMale.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getExcludeSNPs(File f)
/*     */     throws Exception
/*     */   {
/*  52 */     this.excludeSNPs = new ArrayList();
/*  53 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  55 */     while ((st = br.readLine()) != null) { String st;
/*  56 */       this.excludeSNPs.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(File f) throws Exception
/*     */   {
/*  62 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  64 */     String start = null;
/*  65 */     List<String> indivID = new ArrayList();
/*  66 */     String st; while ((st = br.readLine()) != null) { String st;
/*  67 */       String[] str = st.split("\\s+");
/*  68 */       if (start == null) {
/*  69 */         start = str[0];
/*     */       } else
/*  71 */         if (!st.startsWith(start)) break;
/*  72 */       indivID.add(str[1]);
/*     */     }
/*  74 */     br.close();
/*  75 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/*  80 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  81 */     ArrayList<Integer> loc = new ArrayList();
/*  82 */     ArrayList<String> snp = new ArrayList();
/*  83 */     ArrayList<String> A_allele = new ArrayList();
/*  84 */     this.snpInfo = new List[] { snp, loc, A_allele };
/*     */     String st;
/*  86 */     while ((st = br.readLine()) != null) { String st;
/*  87 */       String[] str = st.split("\\s+");
/*  88 */       snp.add(str[0]);
/*  89 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*  90 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*  94 */   Map<String, List<String>> rawData = new HashMap();
/*     */   
/*  96 */   public void ChiamoDataRaw(File bc, File nbs, double cutpoint) throws Exception { BufferedReader br1 = new BufferedReader(new FileReader(bc));
/*  97 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/*  98 */     List<String> indivBC = getIndiv(bc);
/*  99 */     List<String> indivNBS = getIndiv(nbs);
/* 100 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 101 */       String al = this.snpInfo[2].get(i).toString();
/* 102 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 103 */       char a = al.charAt(0);
/* 104 */       for (int k = 0; k < indivBC.size(); k++) {
/* 105 */         String[] str1 = br1.readLine().split("\\s+");
/* 106 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 107 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 108 */         if (!this.rawData.containsKey(str1[1])) {
/* 109 */           List<String> oneIndSNPs = new ArrayList();
/* 110 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 112 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 113 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else {
/* 115 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */         }
/*     */       }
/* 118 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 119 */         String[] str1 = br2.readLine().split("\\s+");
/* 120 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 121 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 122 */         if (!this.rawData.containsKey(str1[1])) {
/* 123 */           List<String> oneIndSNPs = new ArrayList();
/* 124 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 126 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 127 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else
/* 129 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Map<String, Double> missingRate;
/*     */   public Map<String, Double> getProHetreo() {
/* 136 */     Map<String, Double> proHetreo = new HashMap();
/* 137 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 138 */       String s = (String)is.next();
/* 139 */       int heteroCount = 0;
/* 140 */       for (int i = 0; i < ((List)this.rawData.get(s)).size(); i++) {
/* 141 */         if ((!((String)((List)this.rawData.get(s)).get(i)).equals("NA")) && 
/* 142 */           (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1))) {
/* 143 */           heteroCount++;
/*     */         }
/*     */       }
/*     */       
/* 147 */       proHetreo.put(s, Double.valueOf(heteroCount / ((List)this.rawData.get(s)).size()));
/*     */     }
/*     */     
/* 150 */     return proHetreo;
/*     */   }
/*     */   
/*     */   Map<String, Double> maf;
/*     */   public Map<String, Double> calculateMissing()
/*     */   {
/* 156 */     this.missingRate = new HashMap();
/* 157 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 158 */       int count = 0;
/* 159 */       int ind = 0;
/* 160 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 161 */         String s = (String)is.next();
/* 162 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 163 */           ind++;
/* 164 */           if (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */         }
/*     */       }
/* 167 */       this.missingRate.put(this.snpInfo[0].get(i).toString(), Double.valueOf(count / ind));
/*     */     }
/* 169 */     return this.missingRate;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Double> calculateMaf()
/*     */   {
/* 175 */     this.maf = new HashMap();
/* 176 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 177 */       int count = 0;
/* 178 */       int ind = 0;
/* 179 */       Character a = null;
/* 180 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 181 */         String s = (String)is.next();
/* 182 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 183 */           ind++;
/* 184 */           if (a == null) {
/* 185 */             a = Character.valueOf(((String)((List)this.rawData.get(s)).get(i)).charAt(0));
/* 186 */             count++;
/* 187 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */           else {
/* 190 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(0)) count++;
/* 191 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */         }
/*     */       }
/* 195 */       double aFre = count / (ind * 2);
/* 196 */       if (aFre > 0.5D) this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(1.0D - aFre)); else
/* 197 */         this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(aFre));
/*     */     }
/* 199 */     return this.maf;
/*     */   }
/*     */   
/*     */   public int[] getNoInd(List<Character> snpGeno)
/*     */   {
/* 204 */     int[] temp = new int[2];
/* 205 */     temp[0] = (Math.round(snpGeno.size() / 277) * 77);
/* 206 */     temp[1] = (Math.round(snpGeno.size() / 277) * 23);
/* 207 */     return temp;
/*     */   }
/*     */   
/*     */   public List<Integer> getRandomIndex(int noInd) {
/* 211 */     List<Integer> randomIndex = new ArrayList();
/* 212 */     SortedMap<Double, Integer> temp = new TreeMap();
/* 213 */     Random ran = new Random();
/* 214 */     int k = 0;
/* 215 */     while (k < noInd) {
/* 216 */       Double tp = Double.valueOf(ran.nextDouble());
/* 217 */       if (!temp.containsKey(tp)) {
/* 218 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 219 */         k++;
/*     */       }
/*     */     }
/* 222 */     for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 223 */       Double nt = (Double)it.next();
/* 224 */       randomIndex.add((Integer)temp.get(nt));
/*     */     }
/* 226 */     if (randomIndex.size() != noInd) throw new RuntimeException("!!");
/* 227 */     return randomIndex;
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/* 234 */   List<String> heterozygousSite = new ArrayList();
/*     */   int[] noExportInd;
/*     */   
/*     */   public void ChiamoDataCollection(File bc, File nbs, String dir, int numChr, int repIndex) throws Exception {
/* 238 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 239 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 240 */     List<String> indivBC = getIndiv(bc);
/* 241 */     List<String> indivNBS = getIndiv(nbs);
/* 242 */     int noInd = this.maleID.size() - this.excludeMale.size();
/* 243 */     List<Integer> randomIndex = getRandomIndex(noInd);
/* 244 */     this.dest = new FileOutputStream(dir + "X_" + repIndex + ".zip");
/* 245 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 246 */     this.osw = new OutputStreamWriter(this.outS);
/* 247 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 248 */       List<Character> snpGeno = new ArrayList();
/* 249 */       List<Double> snpCall = new ArrayList();
/* 250 */       String al = this.snpInfo[2].get(i).toString();
/* 251 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 252 */       char a = al.charAt(0);
/* 253 */       for (int k = 0; k < indivBC.size(); k++) {
/* 254 */         String[] str1 = br1.readLine().split("\\s+");
/* 255 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 256 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 257 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 258 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 259 */             snpGeno.add(Character.valueOf('N'));
/* 260 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 263 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 264 */               snpGeno.add(Character.valueOf('B'));
/* 265 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/* 269 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 270 */         String[] str1 = br2.readLine().split("\\s+");
/* 271 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 272 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 273 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 274 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 275 */             snpGeno.add(Character.valueOf('N'));
/* 276 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 279 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 280 */               snpGeno.add(Character.valueOf('B'));
/* 281 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 352 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i).toString())) {
/* 353 */         if (snpGeno.size() != noInd) throw new RuntimeException("!!");
/* 354 */         ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 355 */         this.outS.putNextEntry(headings);
/* 356 */         for (int m = 0; m < 250; m++) {
/* 357 */           Double callMulti = Double.valueOf(1.0D);
/* 358 */           this.osw.write(((Character)snpGeno.get(((Integer)randomIndex.get(m)).intValue())).charValue());
/* 359 */           callMulti = Double.valueOf(callMulti.doubleValue() * ((Double)snpCall.get(((Integer)randomIndex.get(m)).intValue())).doubleValue());
/* 360 */           this.osw.write("\t" + callMulti + "\n");
/*     */         }
/* 362 */         for (int m = 250; m < 750; m += 2) {
/* 363 */           Double callMulti = Double.valueOf(1.0D);
/* 364 */           this.osw.write(((Character)snpGeno.get(((Integer)randomIndex.get(m)).intValue())).charValue());
/* 365 */           this.osw.write(((Character)snpGeno.get(((Integer)randomIndex.get(m + 1)).intValue())).charValue());
/* 366 */           callMulti = Double.valueOf(callMulti.doubleValue() * ((Double)snpCall.get(((Integer)randomIndex.get(m)).intValue())).doubleValue());
/* 367 */           this.osw.write("\t" + callMulti + "\n");
/*     */         }
/* 369 */         this.osw.flush();
/* 370 */         this.outS.closeEntry();
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
/* 412 */     ZipEntry headings = new ZipEntry("Samples");
/* 413 */     this.outS.putNextEntry(headings);
/* 414 */     List<String> indiv = new ArrayList();
/* 415 */     for (int i = 0; i < indivBC.size(); i++) {
/* 416 */       if ((this.maleID.contains(indivBC.get(i))) && (!this.excludeMale.contains(indivBC.get(i))))
/* 417 */         indiv.add((String)indivBC.get(i));
/*     */     }
/* 419 */     for (int i = 0; i < indivNBS.size(); i++) {
/* 420 */       if ((this.maleID.contains(indivNBS.get(i))) && (!this.excludeMale.contains(indivNBS.get(i)))) {
/* 421 */         indiv.add((String)indivNBS.get(i));
/*     */       }
/*     */     }
/* 424 */     for (int i = 0; i < 500; i++)
/*     */     {
/*     */ 
/* 427 */       this.osw.write("wtccc" + i + "\n");
/*     */     }
/*     */     
/*     */ 
/* 431 */     this.osw.flush();
/* 432 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps()
/*     */     throws Exception
/*     */   {
/* 438 */     ZipEntry headings = new ZipEntry("Snps");
/* 439 */     this.outS.putNextEntry(headings);
/* 440 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 441 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i))) {
/* 442 */         this.osw.write("chrX\t");this.osw.write(this.snpInfo[1].get(i).toString() + "\t");
/* 443 */         this.osw.write(Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40 + "\t");
/* 444 */         this.osw.write(this.snpInfo[0].get(i).toString());
/* 445 */         this.osw.write("\n");
/*     */       }
/*     */     }
/* 448 */     this.osw.flush();
/* 449 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 453 */     ZipEntry headings = new ZipEntry("Name");
/* 454 */     this.outS.putNextEntry(headings);
/* 455 */     this.osw.write("genotype callingScore\n");
/* 456 */     this.osw.write("chr start end snpID\n");
/* 457 */     this.osw.write("sampleID");
/* 458 */     this.osw.flush();
/* 459 */     this.outS.closeEntry();
/* 460 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public void printProHetero(PrintStream ps, Map<String, Double> data) {
/* 464 */     for (Iterator<String> is = data.keySet().iterator(); is.hasNext();) {
/* 465 */       String s = (String)is.next();
/*     */       
/* 467 */       if (((Double)data.get(s)).doubleValue() > 0.1D) {
/* 468 */         ps.print(s);ps.print(" ");ps.println(data.get(s));
/*     */       }
/*     */     }
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
/*     */   public static void main(String[] args)
/*     */   {
/* 526 */     throw new Error("Unresolved compilation problem: \n\tThe method ChiamoDataCollection(File, File, String, int, String) in the type ChiamoCompressX is not applicable for the arguments (File, File, String, int, int)\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/HaploviewFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */