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
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HapmapCompress
/*     */ {
/*  36 */   List<String> sampleID = new ArrayList();
/*     */   
/*  38 */   public void getSampleID(File f, int numSample) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  40 */     int k = 0;
/*  41 */     String st; while (((st = br.readLine()) != null) && (k < numSample)) { String st;
/*  42 */       String[] str = st.split("\\s+");
/*  43 */       this.sampleID.add(str[0]);
/*  44 */       k++;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  49 */   List<String[]> snpInfo = new ArrayList();
/*     */   
/*  51 */   public void getSNPinfo(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*  52 */     String st = br.readLine();
/*  53 */     int k = 0;
/*  54 */     while ((st = br.readLine()) != null) {
/*  55 */       this.snpInfo.add(st.split("\\s+"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  60 */   List<String[]> genoInfo = new ArrayList();
/*     */   
/*  62 */   public void getGenoInfo(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     
/*  64 */     int k = 0;
/*  65 */     String st; while ((st = br.readLine()) != null) { String st;
/*  66 */       this.genoInfo.add(st.split("\\s+"));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean checkPolymorphic(int m)
/*     */   {
/*  72 */     List<Character> count = new ArrayList();
/*  73 */     for (int i = 0; i < this.sampleID.size(); i++) {
/*  74 */       char allele = ((String[])this.genoInfo.get(i))[m].charAt(0);
/*  75 */       if ((allele != '0') && (allele != '1')) throw new RuntimeException("phased alleles are not either 0 or 1");
/*  76 */       if (!count.contains(Character.valueOf(allele))) count.add(Character.valueOf(allele));
/*     */     }
/*  78 */     if (count.size() > 2) throw new RuntimeException("there are more than two alleles");
/*  79 */     if (count.size() == 2) return true;
/*  80 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*  87 */   List<Integer> noPloymorphicSNP = new ArrayList();
/*     */   List<String> matchedSNP;
/*     */   
/*  90 */   public void HapmapCollection(boolean phased, String chr) throws Exception { this.dest = new FileOutputStream(chr + ".zip");
/*  91 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  92 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     
/*  94 */     if (this.snpInfo.size() != ((String[])this.genoInfo.get(0)).length) throw new RuntimeException("!!");
/*  95 */     if (this.sampleID.size() * 2 != this.genoInfo.size()) throw new RuntimeException("!!");
/*  96 */     for (int m = 0; m < this.snpInfo.size(); m++) {
/*  97 */       if (checkPolymorphic(m)) {
/*  98 */         ZipEntry headings = new ZipEntry(((String[])this.snpInfo.get(m))[0]);
/*  99 */         this.outS.putNextEntry(headings);
/* 100 */         if (phased) {
/* 101 */           for (int i = 0; i < this.sampleID.size() * 2; i++) {
/* 102 */             if (((String[])this.genoInfo.get(i))[m].equals("0")) this.osw.write("A\n"); else {
/* 103 */               this.osw.write("B\n");
/*     */             }
/*     */           }
/*     */         } else {
/* 107 */           for (int i = 0; i < this.sampleID.size() * 2; i += 2) {
/* 108 */             if (((String[])this.genoInfo.get(i))[m].equals("0")) this.osw.write("A"); else
/* 109 */               this.osw.write("B");
/* 110 */             if (((String[])this.genoInfo.get(i + 1))[m].equals("0")) this.osw.write("A\n"); else {
/* 111 */               this.osw.write("B\n");
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 116 */         this.noPloymorphicSNP.add(Integer.valueOf(m)); }
/* 117 */       this.osw.flush();
/* 118 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample(boolean phased)
/*     */     throws Exception
/*     */   {
/* 125 */     ZipEntry headings = new ZipEntry("Samples");
/* 126 */     this.outS.putNextEntry(headings);
/* 127 */     if (phased) {
/* 128 */       for (int i = 0; i < this.sampleID.size(); i++) {
/* 129 */         this.osw.write((String)this.sampleID.get(i) + "_1" + "\n");
/* 130 */         this.osw.write((String)this.sampleID.get(i) + "_2" + "\n");
/*     */       }
/*     */       
/*     */     } else {
/* 134 */       for (int i = 0; i < this.sampleID.size(); i++) {
/* 135 */         this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */       }
/*     */     }
/* 138 */     this.osw.flush();
/* 139 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/* 145 */     ZipEntry headings = new ZipEntry("Snps");
/* 146 */     this.outS.putNextEntry(headings);
/* 147 */     for (int m = 0; m < this.snpInfo.size(); m++) {
/* 148 */       if (!this.noPloymorphicSNP.contains(Integer.valueOf(m))) {
/* 149 */         String[] snp = (String[])this.snpInfo.get(m);
/* 150 */         this.osw.write("chr" + chr + "\t" + snp[1] + "\t" + (Integer.parseInt(snp[1]) + 40) + "\t" + snp[0] + "\n");
/*     */       }
/*     */     }
/* 153 */     this.osw.flush();
/* 154 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 158 */     ZipEntry headings = new ZipEntry("Name");
/* 159 */     this.outS.putNextEntry(headings);
/* 160 */     this.osw.write("genotype\n");
/* 161 */     this.osw.write("chr start end snpID\n");
/* 162 */     this.osw.write("sampleID");
/* 163 */     this.osw.flush();
/* 164 */     this.outS.closeEntry();
/* 165 */     this.outS.close();
/*     */   }
/*     */   
/*     */ 
/*     */   SortedMap<Double, Integer> randomIndex;
/*     */   List<String> markedSNP;
/*     */   public void matchedSNPs(File f, File fhapmap, int from, int to)
/*     */     throws Exception
/*     */   {
/* 174 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 175 */     BufferedReader br1 = new BufferedReader(new FileReader(fhapmap));
/*     */     
/* 177 */     List<String> hapmapSNP = new ArrayList();
/* 178 */     this.matchedSNP = new ArrayList();
/* 179 */     String st; while ((st = br1.readLine()) != null) { String st;
/* 180 */       String[] str = st.split("\\s+");
/* 181 */       if ((Integer.parseInt(str[1]) >= from) && (Integer.parseInt(str[1]) <= to)) {
/* 182 */         hapmapSNP.add(st.split("\\s+")[3]);
/*     */       }
/*     */     }
/* 185 */     while ((st = br.readLine()) != null) {
/* 186 */       if (hapmapSNP.contains(st.split("\\s+")[3])) { this.matchedSNP.add(st.split("\\s+")[3]);
/*     */       }
/*     */     }
/* 189 */     this.randomIndex = new TreeMap();
/* 190 */     Random ran = new Random(System.currentTimeMillis());
/* 191 */     for (int h = 0; h < this.matchedSNP.size(); h++) {
/* 192 */       this.randomIndex.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(h));
/*     */     }
/*     */   }
/*     */   
/*     */   public void markedSNPs(double percentageMarked)
/*     */   {
/* 198 */     this.markedSNP = new ArrayList();
/* 199 */     int count = 0;
/* 200 */     int cap = (int)(this.matchedSNP.size() * percentageMarked);
/* 201 */     for (Iterator<Double> is = this.randomIndex.keySet().iterator(); is.hasNext();) {
/* 202 */       Double s = (Double)is.next();
/* 203 */       count++;
/* 204 */       this.markedSNP.add((String)this.matchedSNP.get(((Integer)this.randomIndex.get(s)).intValue()));
/* 205 */       if (count > cap)
/*     */         break;
/*     */     }
/*     */   }
/*     */   
/* 210 */   public void printBuild(PrintStream ps, PrintStream ps1, File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 212 */     while ((st = br.readLine()) != null) { String st;
/* 213 */       if (this.markedSNP.contains(st.split("\\s+")[3])) {
/* 214 */         ps1.println(st);
/*     */       }
/*     */       else {
/* 217 */         ps.println(st);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void printBuildHapmap(PrintStream ps, File f) throws Exception {
/* 223 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 225 */     while ((st = br.readLine()) != null) { String st;
/* 226 */       if (this.matchedSNP.contains(st.split("\\s+")[3])) ps.println(st);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try {
/* 233 */       HapmapCompress im = new HapmapCompress();
/*     */       
/* 235 */       im.matchedSNPs(new File("build36Wtccc.txt"), new File("build36hapmap.txt"), 27498710, 30300858);
/* 236 */       im.markedSNPs(Double.parseDouble(args[1]));
/* 237 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("build36Wtccc_" + args[1]))));
/* 238 */       PrintStream ps1 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("marked_" + args[1]))));
/* 239 */       im.printBuild(ps, ps1, new File("build36Wtccc.txt"));
/* 240 */       ps.close();
/* 241 */       ps1.close();
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
/* 257 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/HapmapCompress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */