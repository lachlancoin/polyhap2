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
/*     */ import java.util.zip.ZipFile;
/*     */ 
/*     */ public class LinkageDisequiCal
/*     */ {
/*  21 */   int no_copies = 4;
/*  22 */   int no_copiesF = 2;
/*     */   
/*  24 */   static int[] mid = { 6674263, 11769228 };
/*     */   int noSNP;
/*     */   static List<String> position;
/*     */   
/*     */   public static List<String> getIndiv(ZipFile f, String entryName, Integer column) throws Exception
/*     */   {
/*  30 */     BufferedReader nxt = 
/*  31 */       new BufferedReader(new InputStreamReader(
/*  32 */       f.getInputStream(f.getEntry(entryName))));
/*  33 */     List<String> indiv = new ArrayList();
/*  34 */     String st = "";
/*  35 */     while ((st = nxt.readLine()) != null) {
/*  36 */       indiv.add(st.split("\t")[column.intValue()]);
/*     */     }
/*  38 */     nxt.close();
/*  39 */     return indiv;
/*     */   }
/*     */   
/*     */   public static List<String> getSNP(ZipFile f, String entryName) throws Exception {
/*  43 */     position = new ArrayList();
/*  44 */     BufferedReader nxt = 
/*  45 */       new BufferedReader(new InputStreamReader(
/*  46 */       f.getInputStream(f.getEntry(entryName))));
/*  47 */     List<String> snp = new ArrayList();
/*  48 */     String st = "";
/*  49 */     while ((st = nxt.readLine()) != null) {
/*  50 */       String[] str = st.split("\t");
/*  51 */       if ((Integer.parseInt(str[1]) >= mid[0]) && (Integer.parseInt(str[2]) <= mid[1])) {
/*  52 */         snp.add(str[3]);
/*  53 */         position.add(str[1]);
/*     */       }
/*     */     }
/*  56 */     nxt.close();
/*  57 */     return snp;
/*     */   }
/*     */   
/*     */   Map<String, List<Character[]>> rawData;
/*     */   List<String> snpID;
/*     */   Map<String, String[]> phasedData;
/*     */   public void getRawData(File f) throws Exception
/*     */   {
/*  65 */     ZipFile zf = new ZipFile(f);
/*  66 */     this.rawData = new HashMap();
/*  67 */     List<String> indiv = new ArrayList();
/*  68 */     indiv = getIndiv(zf, "Samples", Integer.valueOf(0));
/*  69 */     this.snpID = new ArrayList();
/*  70 */     this.snpID = getSNP(zf, "Snps");
/*  71 */     this.noSNP = this.snpID.size();
/*  72 */     String st = "";
/*  73 */     for (int i = 0; i < this.snpID.size(); i++) {
/*  74 */       BufferedReader nxt = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry((String)this.snpID.get(i)))));
/*  75 */       if (this.rawData.isEmpty()) {
/*  76 */         for (int s = 0; s < indiv.size(); s++) {
/*  77 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  78 */           Character[] temp = new Character[this.no_copies];
/*  79 */           for (int k = 0; k < this.no_copies; k++) {
/*  80 */             temp[k] = Character.valueOf(st.charAt(k));
/*     */           }
/*  82 */           List<Character[]> temp1 = new ArrayList();
/*  83 */           temp1.add(temp);
/*  84 */           this.rawData.put((String)indiv.get(s), temp1);
/*     */         }
/*  86 */         if ((st = nxt.readLine()) != null) throw new RuntimeException("!!");
/*     */       }
/*     */       else {
/*  89 */         for (int s = 0; s < indiv.size(); s++) {
/*  90 */           if ((st = nxt.readLine()) == null) throw new RuntimeException("!!");
/*  91 */           Character[] temp = new Character[this.no_copies];
/*  92 */           for (int k = 0; k < this.no_copies; k++) {
/*  93 */             temp[k] = Character.valueOf(st.charAt(k));
/*     */           }
/*  95 */           ((List)this.rawData.get(indiv.get(s))).add(temp);
/*     */         }
/*  97 */         if ((st = nxt.readLine()) != null) throw new RuntimeException("!!");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<String, Double> getMAF()
/*     */   {
/* 104 */     Map<String, Double> maf = new HashMap();
/* 105 */     for (int m = 0; m < this.snpID.size(); m++) {
/* 106 */       int[] count = new int[2];
/* 107 */       int k; for (Iterator<String> is = this.rawData.keySet().iterator(); is.hasNext(); 
/*     */           
/* 109 */           k < this.no_copies)
/*     */       {
/* 108 */         String indivID = (String)is.next();
/* 109 */         k = 0; continue;
/* 110 */         if (((Character[])((List)this.rawData.get(indivID)).get(m))[k].charValue() == 'A') count[0] += 1; else {
/* 111 */           count[1] += 1;
/*     */         }
/* 109 */         k++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 114 */       if (count[0] < count[1]) {
/* 115 */         maf.put((String)this.snpID.get(m), Double.valueOf(count[0] / (this.rawData.size() * 2)));
/*     */       }
/*     */       else {
/* 118 */         maf.put((String)this.snpID.get(m), Double.valueOf(count[1] / (this.rawData.size() * 2)));
/*     */       }
/*     */     }
/* 121 */     return maf;
/*     */   }
/*     */   
/*     */   public void printMAF(PrintStream ps) {
/* 125 */     Map<String, Double> maf = getMAF();
/* 126 */     for (Iterator<String> is = maf.keySet().iterator(); is.hasNext();) {
/* 127 */       String snpID = (String)is.next();
/* 128 */       ps.println(snpID + " " + maf.get(snpID));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void getPhasedData(File f)
/*     */     throws Exception
/*     */   {
/* 136 */     this.phasedData = new HashMap();
/* 137 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 138 */     String st = br.readLine();
/* 139 */     while (!st.startsWith("#")) {
/* 140 */       st = br.readLine();
/*     */     }
/* 142 */     while (st != null) {
/* 143 */       if (st.startsWith("#")) {
/* 144 */         String[] str = st.split("\\s+");
/* 145 */         String[] temp = new String[this.no_copiesF];
/* 146 */         for (int i = 0; i < this.no_copiesF; i++) {
/* 147 */           temp[i] = br.readLine().replaceAll(" ", "");
/*     */         }
/* 149 */         this.phasedData.put(str[1], temp);
/*     */         
/* 151 */         st = br.readLine();
/* 152 */         if (temp[0].length() != this.noSNP) {
/* 153 */           throw new RuntimeException("!!");
/*     */         }
/*     */       }
/* 156 */       if (st.startsWith("END")) break;
/*     */     }
/*     */   }
/*     */   
/* 160 */   List<String> hapSp2 = new ArrayList();
/*     */   
/* 162 */   public void getHapSp2() { this.hapSp2.add("AA");
/* 163 */     this.hapSp2.add("AB");
/* 164 */     this.hapSp2.add("BA");
/* 165 */     this.hapSp2.add("BB");
/*     */   }
/*     */   
/*     */   double[][] rPhased;
/* 169 */   public int getSpIndex(List<String> hapSp, String aa) { int i = -1;
/* 170 */     while (i < hapSp.size()) {
/* 171 */       i++;
/* 172 */       if (((String)hapSp.get(i)).equals(aa)) break;
/*     */     }
/* 174 */     if (i == -1) throw new RuntimeException("!!");
/* 175 */     return i;
/*     */   }
/*     */   
/*     */   double[][] dPhased;
/*     */   double[][] rRaw;
/*     */   public double[] calR2D(int[] counts)
/*     */   {
/* 182 */     int sum = 0;
/* 183 */     for (int i = 0; i < counts.length; i++) {
/* 184 */       sum += counts[i];
/*     */     }
/* 186 */     double[] probs = new double[counts.length];
/* 187 */     for (int i = 0; i < probs.length; i++) {
/* 188 */       probs[i] = (counts[i] / sum);
/*     */     }
/* 190 */     double[] mProbs = new double[counts.length];
/* 191 */     probs[0] += probs[1];
/* 192 */     mProbs[1] = (probs[2] + probs[3]);
/* 193 */     mProbs[2] = (probs[1] + probs[3]);
/* 194 */     mProbs[3] = (probs[0] + probs[2]);
/* 195 */     double d = probs[0] * probs[3] - probs[1] * probs[2];
/* 196 */     double[] dMax = { Math.min(mProbs[0] * mProbs[2], mProbs[1] * mProbs[3]), 
/* 197 */       -Math.min(mProbs[0] * mProbs[3], mProbs[1] * mProbs[2]) };
/* 198 */     double[] score = { d > 0.0D ? d / dMax[0] : d / dMax[1], Math.pow(d, 2.0D) / (mProbs[0] * mProbs[1] * mProbs[2] * mProbs[3]) };
/*     */     
/* 200 */     return score;
/*     */   }
/*     */   
/*     */   double[][] dRaw;
/*     */   Map<String, String> ldSample;
/*     */   List<Double> trueR;
/*     */   List<Double> estimateR;
/*     */   public void countsHap() {
/* 208 */     this.rPhased = new double[this.noSNP][this.noSNP];
/* 209 */     this.dPhased = new double[this.noSNP][this.noSNP];
/* 210 */     this.rRaw = new double[this.noSNP][this.noSNP];
/* 211 */     this.dRaw = new double[this.noSNP][this.noSNP];
/* 212 */     for (int m1 = 0; m1 < this.noSNP - 1; m1++) {
/* 213 */       for (int m2 = m1 + 1; m2 < this.noSNP; m2++) {
/* 214 */         if (Double.parseDouble((String)position.get(m2)) - Double.parseDouble((String)position.get(m1)) <= 500000.0D) {
/* 215 */           int[] countHapPhased = new int[4];
/* 216 */           int[] countHapRaw = new int[4];
/* 217 */           int c; for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext(); 
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 224 */               c < this.no_copies)
/*     */           {
/* 218 */             String indivID = (String)is.next();
/* 219 */             for (int c = 0; c < this.no_copiesF; c++) {
/* 220 */               char[] hap = { ((String[])this.phasedData.get(indivID))[c].charAt(m1), ((String[])this.phasedData.get(indivID))[c].charAt(m2) };
/* 221 */               String s = new String(hap);
/* 222 */               countHapPhased[getSpIndex(this.hapSp2, s)] += 1;
/*     */             }
/* 224 */             c = 0; continue;
/* 225 */             char[] hap = { ((Character[])((List)this.rawData.get(indivID)).get(m1))[c].charValue(), ((Character[])((List)this.rawData.get(indivID)).get(m2))[c].charValue() };
/* 226 */             String s = new String(hap);
/* 227 */             countHapRaw[getSpIndex(this.hapSp2, s)] += 1;c++;
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 231 */           double[] score = calR2D(countHapPhased);
/* 232 */           this.dPhased[m1][m2] = score[0];
/* 233 */           this.rPhased[m1][m2] = score[1];
/* 234 */           score = calR2D(countHapRaw);
/* 235 */           this.dRaw[m1][m2] = score[0];
/* 236 */           this.rRaw[m1][m2] = score[1];
/* 237 */           if (Math.abs(this.rPhased[m1][m2] - this.rRaw[m1][m2]) > 0.9D)
/*     */           {
/* 239 */             System.out.println(m1 + ":" + m2 + ":" + this.rPhased[m1][m2]);
/* 240 */             System.out.println(m1 + ":" + m2 + ":" + this.rRaw[m1][m2]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getLD(File f)
/*     */     throws Exception
/*     */   {
/* 253 */     throw new Error("Unresolved compilation problem: \n\tThe method isEmpty() is undefined for the type String\n");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void print(PrintStream ps, double[][] dPhase, double[][] r2Phase, double[][] dRaw, double[][] r2Raw)
/*     */   {
/* 261 */     for (int m1 = 0; m1 < this.noSNP - 1; m1++) {
/* 262 */       for (int m2 = m1 + 1; m2 < this.noSNP; m2++) {
/* 263 */         if ((!Double.isNaN(r2Phase[m1][m2])) && (r2Phase[m1][m2] != 0.0D)) {
/* 264 */           String ss = Integer.toString(m1 + 1) + "_" + Integer.toString(m2 + 1);
/* 265 */           ps.print(r2Phase[m1][m2]);ps.print("\t");
/* 266 */           ps.print(r2Raw[m1][m2]);ps.print("\t");
/* 267 */           if (this.ldSample.containsKey(ss)) {
/* 268 */             ps.println((String)this.ldSample.get(ss));
/*     */           }
/*     */           else {
/* 271 */             ps.println();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void printfp(PrintStream ps, double[][] dPhase, double[][] r2Phase, double[][] dRaw, double[][] r2Raw)
/*     */   {
/* 281 */     for (int m1 = 0; m1 < this.noSNP - 1; m1++) {
/* 282 */       for (int m2 = m1 + 1; m2 < this.noSNP; m2++) {
/* 283 */         if ((!Double.isNaN(r2Phase[m1][m2])) && (r2Phase[m1][m2] != 0.0D)) {
/* 284 */           ps.print(r2Phase[m1][m2]);ps.print("\t");
/* 285 */           ps.println(r2Raw[m1][m2]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printHaloview(PrintStream ps)
/*     */   {
/* 293 */     int k = 1;
/* 294 */     Random ran = new Random(System.currentTimeMillis());
/* 295 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 296 */       String indivID = (String)is.next();
/* 297 */       ps.print(k + " " + k + " " + 0 + " " + 0 + " " + 0 + " " + 0 + " ");
/* 298 */       for (int m = 0; m < ((List)this.rawData.get(indivID)).size(); m++) {
/* 299 */         int count = 0;
/* 300 */         double ranNum = ran.nextDouble();
/* 301 */         for (int i = 0; i < this.no_copies; i++) {
/* 302 */           if (((Character[])((List)this.rawData.get(indivID)).get(m))[i].equals(Character.valueOf('B'))) count++;
/*     */         }
/* 304 */         if (count == 0) { ps.print("1 1 ");
/* 305 */         } else if (count == this.no_copies) { ps.print("4 4 ");
/*     */         }
/* 307 */         else if (ranNum > 0.5D) ps.print("1 4 "); else {
/* 308 */           ps.print("4 1 ");
/*     */         }
/*     */       }
/* 311 */       k++;
/* 312 */       ps.println();
/*     */     }
/*     */   }
/*     */   
/*     */   public void printFastPhase(PrintStream ps)
/*     */   {
/* 318 */     ps.println(this.phasedData.size());
/* 319 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 320 */       String indivID = (String)is.next();
/* 321 */       ps.println(((String[])this.phasedData.get(indivID))[0].length());
/* 322 */       break;
/*     */     }
/* 324 */     for (Iterator<String> is = this.phasedData.keySet().iterator(); is.hasNext();) {
/* 325 */       String indivID = (String)is.next();
/* 326 */       ps.println("# " + indivID);
/* 327 */       String s1 = "";String s2 = "";
/* 328 */       String[] newCode = { s1, s2 };
/* 329 */       for (int m = 0; m < ((List)this.rawData.get(indivID)).size(); m++) {
/* 330 */         int count = 0;
/* 331 */         for (int i = 0; i < this.no_copies; i++) {
/* 332 */           if (((Character[])((List)this.rawData.get(indivID)).get(m))[i].equals(Character.valueOf('B'))) count++;
/*     */         }
/* 334 */         if (count == 0) {
/* 335 */           newCode[0] = (newCode[0] + "A");
/* 336 */           newCode[1] = (newCode[1] + "A");
/*     */         }
/* 338 */         else if (count == this.no_copies) {
/* 339 */           newCode[0] = (newCode[0] + "B");
/* 340 */           newCode[1] = (newCode[1] + "B");
/*     */         }
/*     */         else {
/* 343 */           newCode[0] = (newCode[0] + "A");
/* 344 */           newCode[1] = (newCode[1] + "B");
/*     */         }
/*     */       }
/* 347 */       ps.println(newCode[0].toString());
/* 348 */       ps.println(newCode[1].toString());
/*     */     }
/*     */   }
/*     */   
/*     */   public void printPosition(PrintStream ps) {
/* 353 */     for (int i = 0; i < position.size(); i++) {
/* 354 */       ps.println(i + 1 + " " + (String)position.get(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 360 */       LinkageDisequiCal ld = new LinkageDisequiCal();
/* 361 */       ld.getHapSp2();
/* 362 */       int rep = 0;
/*     */       
/* 364 */       ld.getRawData(new File(args[0], "X_" + rep + ".zip"));
/* 365 */       ld.getPhasedData(new File(args[0], "phased2_" + rep + "_" + args[1] + ".K20T10"));
/*     */       
/* 367 */       ld.countsHap();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 379 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(args[0] + "ldPhasedTrueFP_" + rep + "_" + args[1]))));
/* 380 */       ld.printfp(ps, ld.dPhased, ld.rPhased, ld.dRaw, ld.rRaw);
/* 381 */       ps.close();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 405 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public void getResults(File ldTrue, File ldEstimate)
/*     */     throws Exception
/*     */   {
/* 412 */     this.trueR = new ArrayList();
/* 413 */     this.estimateR = new ArrayList();
/* 414 */     Map<String, Double> ldTrueR = new HashMap();
/* 415 */     Map<String, Double> ldEstimateR = new HashMap();
/* 416 */     BufferedReader br1 = new BufferedReader(new FileReader(ldTrue));
/* 417 */     BufferedReader br2 = new BufferedReader(new FileReader(ldEstimate));
/* 418 */     String st = "";
/* 419 */     br2.readLine();
/* 420 */     while ((st = br1.readLine()) != null) {
/* 421 */       String[] str = st.split("\t");
/* 422 */       ldTrueR.put(str[0] + "_" + str[1], Double.valueOf(Double.parseDouble(str[5])));
/*     */     }
/* 424 */     while ((st = br2.readLine()) != null) {
/* 425 */       String[] str = st.split("\t");
/* 426 */       ldEstimateR.put(str[0] + "_" + str[1], Double.valueOf(Double.parseDouble(str[4])));
/*     */     }
/* 428 */     for (Iterator<String> is = ldTrueR.keySet().iterator(); is.hasNext();) {
/* 429 */       String index = (String)is.next();
/* 430 */       if (ldEstimateR.containsKey(index)) {
/* 431 */         this.trueR.add((Double)ldTrueR.get(index));
/* 432 */         this.estimateR.add((Double)ldEstimateR.get(index));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printR(PrintStream ps) {
/* 438 */     for (int i = 0; i < this.trueR.size(); i++) {
/* 439 */       ps.println(this.trueR.get(i) + " " + this.estimateR.get(i));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/LinkageDisequiCal.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */