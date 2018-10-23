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
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ 
/*     */ public class ChiamoCompressDart
/*     */ {
/*  23 */   List<String> maleID = new ArrayList();
/*     */   
/*  25 */   public void getMaleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  27 */     while ((st = br.readLine()) != null) { String st;
/*  28 */       String[] str = st.split("\\s+");
/*  29 */       if (str[1].equals("1")) {
/*  30 */         this.maleID.add(str[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   List<String> excludeMale;
/*     */   public void getExcludeMale(File f) throws Exception
/*     */   {
/*  38 */     this.excludeMale = new ArrayList();
/*  39 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  41 */     while ((st = br.readLine()) != null) { String st;
/*  42 */       this.excludeMale.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void getExcludeSNPs(File f)
/*     */     throws Exception
/*     */   {
/*  49 */     this.excludeSNPs = new ArrayList();
/*  50 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  52 */     while ((st = br.readLine()) != null) { String st;
/*  53 */       this.excludeSNPs.add(st.split("\\s+")[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getIndiv(File f) throws Exception
/*     */   {
/*  59 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  61 */     String start = null;
/*  62 */     List<String> indivID = new ArrayList();
/*  63 */     String st; while ((st = br.readLine()) != null) { String st;
/*  64 */       String[] str = st.split("\\s+");
/*  65 */       if (start == null) {
/*  66 */         start = str[0];
/*     */       } else
/*  68 */         if (!st.startsWith(start)) break;
/*  69 */       indivID.add(str[1]);
/*     */     }
/*  71 */     br.close();
/*  72 */     return indivID;
/*     */   }
/*     */   
/*     */   public void getSNPinfo(File f) throws Exception
/*     */   {
/*  77 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  78 */     ArrayList<Integer> loc = new ArrayList();
/*  79 */     ArrayList<String> snp = new ArrayList();
/*  80 */     ArrayList<String> A_allele = new ArrayList();
/*  81 */     ArrayList<Double> alleleFreq = new ArrayList();
/*  82 */     this.snpInfo = new List[] { snp, loc, A_allele, alleleFreq };
/*     */     String st;
/*  84 */     while ((st = br.readLine()) != null) { String st;
/*  85 */       String[] str = st.split("\\s+");
/*  86 */       snp.add(str[0]);
/*  87 */       loc.add(Integer.valueOf(Integer.parseInt(str[2])));
/*  88 */       A_allele.add(str[4]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void calAlleleFreq(File bc, File nbs) throws Exception {
/*  93 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/*  94 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/*  95 */     List<String> indivBC = getIndiv(bc);
/*  96 */     List<String> indivNBS = getIndiv(nbs);
/*  97 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/*  98 */       String al = this.snpInfo[2].get(i).toString();
/*  99 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 100 */       char a = al.charAt(0);
/* 101 */       int indCount = 0;
/* 102 */       int aAlleleCount = 0;
/* 103 */       for (int k = 0; k < indivBC.size(); k++) {
/* 104 */         String[] str1 = br1.readLine().split("\\s+");
/* 105 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 106 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 107 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 108 */           indCount++;
/* 109 */           if (a == str1[2].charAt(0)) {
/* 110 */             aAlleleCount++;
/*     */           }
/*     */         }
/*     */       }
/* 114 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 115 */         String[] str1 = br2.readLine().split("\\s+");
/* 116 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 117 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 118 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 119 */           indCount++;
/* 120 */           if (a == str1[2].charAt(0)) {
/* 121 */             aAlleleCount++;
/*     */           }
/*     */         }
/*     */       }
/* 125 */       Double aFreq = Double.valueOf(aAlleleCount / indCount);
/* 126 */       this.snpInfo[3].add(aFreq);
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Double> getRandomNum(int numChr)
/*     */   {
/* 132 */     List<Double> ranNum = new ArrayList();
/* 133 */     Random ran = new Random();
/* 134 */     for (int i = 0; i < 1000; i++) {
/* 135 */       ranNum.add(Double.valueOf(ran.nextDouble()));
/*     */     }
/* 137 */     return ranNum;
/*     */   }
/*     */   
/*     */   List<String> excludeSNPs;
/*     */   List[] snpInfo;
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/* 145 */   List<String> heterozygousSite = new ArrayList();
/* 146 */   Map<String, Double> recesiveGenoFreq = new HashMap();
/*     */   
/*     */   public void ChiamoDataCollection(File bc, File nbs, String dir, int numChr, String dartPro) throws Exception {
/* 149 */     BufferedReader br1 = new BufferedReader(new FileReader(bc));
/* 150 */     BufferedReader br2 = new BufferedReader(new FileReader(nbs));
/* 151 */     List<String> indivBC = getIndiv(bc);
/* 152 */     List<String> indivNBS = getIndiv(nbs);
/* 153 */     this.dest = new FileOutputStream(dir + "Xpi" + dartPro + ".zip");
/* 154 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/* 155 */     this.osw = new OutputStreamWriter(this.outS);
/* 156 */     Random ran = new Random();
/* 157 */     List<Double> ranNum = getRandomNum(numChr);
/*     */     
/* 159 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 160 */       double dartCount = ran.nextDouble();
/* 161 */       List<Character> snpGeno = new ArrayList();
/* 162 */       List<Double> snpCall = new ArrayList();
/* 163 */       String al = this.snpInfo[2].get(i).toString();
/* 164 */       if (al.length() != 1) throw new RuntimeException("!!" + al);
/* 165 */       char a = al.charAt(0);
/* 166 */       int countAA = 0;
/* 167 */       for (int k = 0; k < indivBC.size(); k++) {
/* 168 */         String[] str1 = br1.readLine().split("\\s+");
/* 169 */         if (!str1[1].equals(indivBC.get(k))) throw new RuntimeException("!!");
/* 170 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 171 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 172 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 173 */             snpGeno.add(Character.valueOf('N'));
/* 174 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 177 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 178 */               snpGeno.add(Character.valueOf('B'));
/* 179 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/* 183 */       for (int k = 0; k < indivNBS.size(); k++) {
/* 184 */         String[] str1 = br2.readLine().split("\\s+");
/* 185 */         if (!str1[1].equals(indivNBS.get(k))) throw new RuntimeException("!!");
/* 186 */         if (!str1[0].equals(this.snpInfo[0].get(i))) throw new RuntimeException("!!");
/* 187 */         if ((this.maleID.contains(str1[1])) && (!this.excludeMale.contains(str1[1]))) {
/* 188 */           if (str1[2].charAt(0) != str1[2].charAt(1)) {
/* 189 */             snpGeno.add(Character.valueOf('N'));
/* 190 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */           else {
/* 193 */             if (str1[2].charAt(0) == a) snpGeno.add(Character.valueOf('A')); else
/* 194 */               snpGeno.add(Character.valueOf('B'));
/* 195 */             snpCall.add(Double.valueOf(Double.parseDouble(str1[3])));
/*     */           }
/*     */         }
/*     */       }
/*     */       char domAllele;
/*     */       char domAllele;
/* 201 */       if (Double.parseDouble(this.snpInfo[3].get(i).toString()) < 0.5D) {
/* 202 */         domAllele = 'A';
/*     */       } else {
/* 204 */         domAllele = 'B';
/*     */       }
/*     */       
/* 207 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i).toString())) {
/* 208 */         ZipEntry headings = new ZipEntry(this.snpInfo[0].get(i).toString());
/* 209 */         this.outS.putNextEntry(headings);
/* 210 */         for (int m = 0; m < Math.round(snpGeno.size() / numChr); m++) {
/* 211 */           Double callMulti = Double.valueOf(1.0D);
/* 212 */           List<Character> genotype = new ArrayList();
/*     */           
/* 214 */           for (int c = 0; c < numChr; c++) {
/* 215 */             genotype.add((Character)snpGeno.get(m * numChr + c));
/* 216 */             callMulti = Double.valueOf(callMulti.doubleValue() * ((Double)snpCall.get(m * numChr + c)).doubleValue());
/*     */           }
/*     */           
/* 219 */           if ((genotype.contains(Character.valueOf(domAllele))) && (((Double)ranNum.get(m)).doubleValue() <= Double.parseDouble(dartPro))) {
/* 220 */             this.osw.write("NN");
/* 221 */             this.osw.write("\t1\n");
/*     */ 
/*     */ 
/*     */           }
/* 225 */           else if ((genotype.contains(Character.valueOf(domAllele))) && (((Double)ranNum.get(m)).doubleValue() > Double.parseDouble(dartPro))) {
/* 226 */             for (int c = 0; c < numChr; c++) {
/* 227 */               this.osw.write(((Character)genotype.get(c)).charValue());
/*     */             }
/* 229 */             this.osw.write("\t" + callMulti + "\n");
/*     */           }
/*     */           else
/*     */           {
/* 233 */             for (int c = 0; c < numChr; c++) {
/* 234 */               this.osw.write(((Character)genotype.get(c)).charValue());
/*     */             }
/* 236 */             this.osw.write("\t" + callMulti + "\n");
/* 237 */             countAA++;
/*     */           }
/*     */         }
/* 240 */         this.osw.flush();
/* 241 */         this.outS.closeEntry();
/*     */       }
/* 243 */       this.recesiveGenoFreq.put(this.snpInfo[0].get(i).toString(), Double.valueOf(countAA / Math.round(snpGeno.size() / numChr)));
/*     */     }
/*     */     
/* 246 */     ZipEntry headings = new ZipEntry("Samples");
/* 247 */     this.outS.putNextEntry(headings);
/* 248 */     List<String> indiv = new ArrayList();
/* 249 */     for (int i = 0; i < indivBC.size(); i++) {
/* 250 */       if ((this.maleID.contains(indivBC.get(i))) && (!this.excludeMale.contains(indivBC.get(i)))) indiv.add((String)indivBC.get(i));
/*     */     }
/* 252 */     for (int i = 0; i < indivNBS.size(); i++) {
/* 253 */       if ((this.maleID.contains(indivNBS.get(i))) && (!this.excludeMale.contains(indivNBS.get(i)))) { indiv.add((String)indivNBS.get(i));
/*     */       }
/*     */     }
/* 256 */     for (int i = 0; i < Math.round(indiv.size() / numChr); i++) {
/* 257 */       this.osw.write((String)indiv.get(i * numChr) + "\n");
/*     */     }
/* 259 */     this.osw.flush();
/* 260 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps()
/*     */     throws Exception
/*     */   {
/* 266 */     ZipEntry headings = new ZipEntry("Snps");
/* 267 */     this.outS.putNextEntry(headings);
/* 268 */     for (int i = 0; i < this.snpInfo[0].size(); i++) {
/* 269 */       if (!this.excludeSNPs.contains(this.snpInfo[0].get(i))) {
/* 270 */         this.osw.write("chrX\t");this.osw.write(this.snpInfo[1].get(i).toString() + "\t");
/* 271 */         this.osw.write(Integer.parseInt(this.snpInfo[1].get(i).toString()) + 40 + "\t");
/* 272 */         this.osw.write(this.snpInfo[0].get(i).toString() + "\t");
/* 273 */         if (Double.parseDouble(this.snpInfo[3].get(i).toString()) < 0.5D) {
/* 274 */           this.osw.write("B\t");this.osw.write("A\t");
/*     */         }
/*     */         else {
/* 277 */           this.osw.write("A\t");this.osw.write("B\t");
/*     */         }
/* 279 */         this.osw.write(((Double)this.recesiveGenoFreq.get(this.snpInfo[0].get(i))).toString());
/* 280 */         this.osw.write("\n");
/*     */       }
/*     */     }
/* 283 */     this.osw.flush();
/* 284 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 288 */     ZipEntry headings = new ZipEntry("Name");
/* 289 */     this.outS.putNextEntry(headings);
/* 290 */     this.osw.write("genotype callingScore\n");
/* 291 */     this.osw.write("chr start end snpID majAllele minAllele recesiveGenoFreq\n");
/* 292 */     this.osw.write("sampleID");
/* 293 */     this.osw.flush();
/* 294 */     this.outS.closeEntry();
/* 295 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public void printAAfreq(PrintStream ps) { String s;
/* 299 */     for (Iterator<String> is = this.recesiveGenoFreq.keySet().iterator(); is.hasNext(); 
/*     */         
/* 301 */         ps.println(this.recesiveGenoFreq.get(s)))
/*     */     {
/* 300 */       s = (String)is.next();
/* 301 */       ps.print(s + " ");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 310 */       ChiamoCompressDart cc = new ChiamoCompressDart();
/* 311 */       cc.getMaleID(new File("sample/Affx_20070205fs1_sample_58C.txt"));
/* 312 */       cc.getMaleID(new File("sample/Affx_20070205fs1_sample_NBS.txt"));
/* 313 */       cc.getSNPinfo(new File("X_snp.txt"));
/* 314 */       cc.getExcludeMale(new File("excludeIndMale.txt"));
/* 315 */       cc.getExcludeSNPs(new File("excludeMarkers1.txt"));
/* 316 */       cc.calAlleleFreq(new File("genotype/Affx_20070205fs1_gt_58C_Chiamo_X.txt"), new File("genotype/Affx_20070205fs1_gt_NBS_Chiamo_X.txt"));
/* 317 */       cc.ChiamoDataCollection(new File("genotype/Affx_20070205fs1_gt_58C_Chiamo_X.txt"), new File("genotype/Affx_20070205fs1_gt_NBS_Chiamo_X.txt"), args[0] + "Dart/", Integer.parseInt(args[1]), args[2]);
/* 318 */       cc.writeSnps();
/* 319 */       cc.writeName();
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
/* 336 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/ChiamoCompressDart.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */