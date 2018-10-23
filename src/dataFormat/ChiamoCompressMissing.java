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
/*     */ public class ChiamoCompressMissing
/*     */ {
/*  25 */   List<String> maleID = new ArrayList();
/*     */   
/*  27 */   public void getMaleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  29 */     while ((st = br.readLine()) != null) { String st;
/*  30 */       String[] str = st.split("\\s+");
/*  31 */       if (str[1].equals("1"))
/*  32 */         this.maleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeMale;
/*     */   List<String> excludeSNPs;
/*     */   List[] snpInfo;
/*     */   public void getExcludeMale(File f) throws Exception {
/*  40 */     this.excludeMale = new ArrayList();
/*  41 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  43 */     while ((st = br.readLine()) != null) { String st;
/*  44 */       this.excludeMale.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getExcludeSNPs(File f)
/*     */     throws Exception
/*     */   {
/*  51 */     this.excludeSNPs = new ArrayList();
/*  52 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  54 */     while ((st = br.readLine()) != null) { String st;
/*  55 */       this.excludeSNPs.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(File f) throws Exception
/*     */   {
/*  61 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  63 */     String start = null;
/*  64 */     List<String> indivID = new ArrayList();
/*  65 */     String st; while ((st = br.readLine()) != null) { String st;
/*  66 */       String[] str = st.split("\\s+");
/*  67 */       if (start == null) {
/*  68 */         start = str[0];
/*     */       } else
/*  70 */         if (!st.startsWith(start)) break;
/*  71 */       indivID.add(str[1]);
/*     */     }
/*  73 */     br.close();
/*  74 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/*  79 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  80 */     ArrayList<Integer> loc = new ArrayList();
/*  81 */     ArrayList<String> snp = new ArrayList();
/*  82 */     ArrayList<String> A_allele = new ArrayList();
/*  83 */     this.snpInfo = new List[] { snp, loc, A_allele };
/*     */     String st;
/*  85 */     while ((st = br.readLine()) != null) { String st;
/*  86 */       String[] str = st.split("\\s+");
/*  87 */       snp.add(str[0]);
/*  88 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*  89 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*  93 */   Map<String, List<String>> rawData = new HashMap();
/*     */   
/*  95 */   public void ChiamoDataRaw(File bc, File nbs, double cutpoint) throws Exception { BufferedReader br1 = new BufferedReader(new FileReader(bc));
/*  96 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/*  97 */     List<String> indivBC = getIndiv(bc);
/*  98 */     List<String> indivNBS = getIndiv(nbs);
/*  99 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 100 */       String al = this.snpInfo[2].get(i).toString();
/* 101 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 102 */       char a = al.charAt(0);
/* 103 */       for (int k = 0; k < indivBC.size(); k++) {
/* 104 */         String[] str1 = br1.readLine().split("\\s+");
/* 105 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 106 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 107 */         if (!this.rawData.containsKey(str1[1])) {
/* 108 */           List<String> oneIndSNPs = new ArrayList();
/* 109 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 111 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 112 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else {
/* 114 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */         }
/*     */       }
/* 117 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 118 */         String[] str1 = br2.readLine().split("\\s+");
/* 119 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 120 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 121 */         if (!this.rawData.containsKey(str1[1])) {
/* 122 */           List<String> oneIndSNPs = new ArrayList();
/* 123 */           this.rawData.put(str1[1], oneIndSNPs);
/*     */         }
/* 125 */         if (Double.parseDouble(str1[3]) >= cutpoint) {
/* 126 */           ((List)this.rawData.get(str1[1])).add(str1[2]);
/*     */         } else
/* 128 */           ((List)this.rawData.get(str1[1])).add("NA");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   Map<String, Double> missingRate;
/*     */   public Map<String, Double> getProHetreo() {
/* 135 */     Map<String, Double> proHetreo = new HashMap();
/* 136 */     for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 137 */       String s = (String)is.next();
/* 138 */       int heteroCount = 0;
/* 139 */       for (int i = 0; i < ((List)this.rawData.get(s)).size(); i++) {
/* 140 */         if ((!((String)((List)this.rawData.get(s)).get(i)).equals("NA")) && 
/* 141 */           (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1))) {
/* 142 */           heteroCount++;
/*     */         }
/*     */       }
/*     */       
/* 146 */       proHetreo.put(s, Double.valueOf(heteroCount / ((List)this.rawData.get(s)).size()));
/*     */     }
/*     */     
/* 149 */     return proHetreo;
/*     */   }
/*     */   
/*     */   Map<String, Double> maf;
/*     */   public Map<String, Double> calculateMissing()
/*     */   {
/* 155 */     this.missingRate = new HashMap();
/* 156 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 157 */       int count = 0;
/* 158 */       int ind = 0;
/* 159 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 160 */         String s = (String)is.next();
/* 161 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 162 */           ind++;
/* 163 */           if (((String)((List)this.rawData.get(s)).get(i)).charAt(0) != ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */         }
/*     */       }
/* 166 */       this.missingRate.put(this.snpInfo[0].get(i).toString(), Double.valueOf(count / ind));
/*     */     }
/* 168 */     return this.missingRate;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, Double> calculateMaf()
/*     */   {
/* 174 */     this.maf = new HashMap();
/* 175 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 176 */       int count = 0;
/* 177 */       int ind = 0;
/* 178 */       Character a = null;
/* 179 */       for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext();) {
/* 180 */         String s = (String)is.next();
/* 181 */         if ((this.maleID.contains(s)) && (!this.excludeMale.contains(s))) {
/* 182 */           ind++;
/* 183 */           if (a == null) {
/* 184 */             a = Character.valueOf(((String)((List)this.rawData.get(s)).get(i)).charAt(0));
/* 185 */             count++;
/* 186 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */           else {
/* 189 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(0)) count++;
/* 190 */             if (a.charValue() == ((String)((List)this.rawData.get(s)).get(i)).charAt(1)) count++;
/*     */           }
/*     */         }
/*     */       }
/* 194 */       double aFre = count / (ind * 2);
/* 195 */       if (aFre > 0.5D) this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(1.0D - aFre)); else
/* 196 */         this.maf.put(this.snpInfo[0].get(i).toString(), Double.valueOf(aFre));
/*     */     }
/* 198 */     return this.maf;
/*     */   }
/*     */   
/*     */   public List<Integer> getRandomIndex(int noInd) {
/* 202 */     List<Integer> randomIndex = new ArrayList();
/* 203 */     SortedMap<Double, Integer> temp = new TreeMap();
/* 204 */     Random ran = new Random();
/* 205 */     int k = 0;
/* 206 */     while (k < noInd) {
/* 207 */       Double tp = Double.valueOf(ran.nextDouble());
/* 208 */       if (!temp.containsKey(tp)) {
/* 209 */         temp.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(k));
/* 210 */         k++;
/*     */       }
/*     */     }
/* 213 */     for (Iterator<Double> it = temp.keySet().iterator(); it.hasNext();) {
/* 214 */       Double nt = (Double)it.next();
/* 215 */       randomIndex.add((Integer)temp.get(nt));
/*     */     }
/* 217 */     if (randomIndex.size() != noInd) throw new RuntimeException("!!");
/* 218 */     return randomIndex;
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/* 225 */   List<String> heterozygousSite = new ArrayList();
/*     */   
/*     */   public void ChiamoDataCollection(File bc, File nbs, String dir, int numChr, int repIndex) throws Exception {
/* 228 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 229 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 230 */     List<String> indivBC = getIndiv(bc);
/* 231 */     List<String> indivNBS = getIndiv(nbs);
/* 232 */     int noInd = this.maleID.size() - this.excludeMale.size();
/* 233 */     List<Integer> randomIndex = getRandomIndex(noInd);
/* 234 */     this.dest = new FileOutputStream(dir + "X_" + repIndex + ".zip");
/* 235 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 236 */     this.osw = new OutputStreamWriter(this.outS);
/* 237 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 238 */       List<Character> snpGeno = new ArrayList();
/* 239 */       List<Double> snpCall = new ArrayList();
/* 240 */       String al = this.snpInfo[2].get(i).toString();
/* 241 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 242 */       char a = al.charAt(0);
/* 243 */       for (int k = 0; k < indivBC.size(); k++) {
/* 244 */         String[] str1 = br1.readLine().split("\\s+");
/* 245 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 246 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 247 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 248 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 249 */             snpGeno.add(Character.valueOf('N'));
/* 250 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 253 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 254 */               snpGeno.add(Character.valueOf('B'));
/* 255 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/* 259 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 260 */         String[] str1 = br2.readLine().split("\\s+");
/* 261 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 262 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 263 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 264 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 265 */             snpGeno.add(Character.valueOf('N'));
/* 266 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 269 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 270 */               snpGeno.add(Character.valueOf('B'));
/* 271 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 276 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i).toString())) {
/* 277 */         if (snpGeno.size() != noInd) throw new RuntimeException("!!");
/* 278 */         ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 279 */         this.outS.putNextEntry(headings);
/* 280 */         for (int m = 0; m < Math.round(snpGeno.size() / numChr); m++) {
/* 281 */           Double callMulti = Double.valueOf(1.0D);
/* 282 */           for (int c = 0; c < numChr; c++) {
/* 283 */             this.osw.write(((Character)snpGeno.get(((Integer)randomIndex.get(m * numChr + c)).intValue())).charValue());
/* 284 */             callMulti = Double.valueOf(callMulti.doubleValue() * ((Double)snpCall.get(((Integer)randomIndex.get(m * numChr + c)).intValue())).doubleValue());
/*     */           }
/* 286 */           this.osw.write("\t" + callMulti + "\n");
/*     */         }
/* 288 */         this.osw.flush();
/* 289 */         this.outS.closeEntry();
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
/* 342 */     ZipEntry headings = new ZipEntry("Samples");
/* 343 */     this.outS.putNextEntry(headings);
/* 344 */     List<String> indiv = new ArrayList();
/* 345 */     for (int i = 0; i < indivBC.size(); i++) {
/* 346 */       if ((this.maleID.contains(indivBC.get(i))) && (!this.excludeMale.contains(indivBC.get(i))))
/* 347 */         indiv.add((String)indivBC.get(i));
/*     */     }
/* 349 */     for (int i = 0; i < indivNBS.size(); i++) {
/* 350 */       if ((this.maleID.contains(indivNBS.get(i))) && (!this.excludeMale.contains(indivNBS.get(i)))) {
/* 351 */         indiv.add((String)indivNBS.get(i));
/*     */       }
/*     */     }
/* 354 */     for (int i = 0; i < Math.round(indiv.size() / numChr); i++) {
/* 355 */       this.osw.write((String)indiv.get(i * numChr) + "\n");
/*     */     }
/* 357 */     this.osw.flush();
/* 358 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps()
/*     */     throws Exception
/*     */   {
/* 364 */     ZipEntry headings = new ZipEntry("Snps");
/* 365 */     this.outS.putNextEntry(headings);
/* 366 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 367 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i))) {
/* 368 */         this.osw.write("chrX\t");this.osw.write(this.snpInfo[1].get(i).toString() + "\t");
/* 369 */         this.osw.write(Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40 + "\t");
/* 370 */         this.osw.write(this.snpInfo[0].get(i).toString());
/* 371 */         this.osw.write("\n");
/*     */       }
/*     */     }
/* 374 */     this.osw.flush();
/* 375 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 379 */     ZipEntry headings = new ZipEntry("Name");
/* 380 */     this.outS.putNextEntry(headings);
/* 381 */     this.osw.write("genotype callingScore\n");
/* 382 */     this.osw.write("chr start end snpID\n");
/* 383 */     this.osw.write("sampleID");
/* 384 */     this.osw.flush();
/* 385 */     this.outS.closeEntry();
/* 386 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public void printProHetero(PrintStream ps, Map<String, Double> data) {
/* 390 */     for (Iterator<String> is = data.keySet().iterator(); is.hasNext();) {
/* 391 */       String s = (String)is.next();
/*     */       
/* 393 */       if (((Double)data.get(s)).doubleValue() > 0.1D) {
/* 394 */         ps.print(s);ps.print(" ");ps.println(data.get(s));
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
/* 452 */     throw new Error("Unresolved compilation problem: \n\tThe method ChiamoDataCollection(File, File, String, int, String) in the type ChiamoCompressX is not applicable for the arguments (File, File, String, int, int)\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/ChiamoCompressMissing.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */