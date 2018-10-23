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
/*     */ public class HapmapCompressX
/*     */ {
/*  36 */   List<String> sampleID = new ArrayList();
/*     */   
/*  38 */   public void getSampleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  40 */     while ((st = br.readLine()) != null) { String st;
/*  41 */       String[] str = st.split("\\s+");
/*  42 */       if ((str[2].equals("0")) && (str[3].equals("0")) && (str[4].equals("1"))) {
/*  43 */         this.sampleID.add(str[6].split(":")[4]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Integer> getSampleIndex(String[] headline)
/*     */   {
/*  50 */     List<Integer> sampleIndex = new ArrayList();
/*  51 */     for (int i = 0; i < headline.length; i++) {
/*  52 */       if (this.sampleID.contains(headline[i])) sampleIndex.add(Integer.valueOf(i));
/*     */     }
/*  54 */     return sampleIndex;
/*     */   }
/*     */   
/*     */   public boolean checkPolymorphic(String[] geno, List<Integer> sampleIndex)
/*     */   {
/*  59 */     List<Character> count = new ArrayList();
/*  60 */     for (int i = 0; i < sampleIndex.size(); i++) {
/*  61 */       String genotype = geno[((Integer)sampleIndex.get(i)).intValue()];
/*  62 */       if (!genotype.equals("NN")) {
/*  63 */         if (!count.contains(Character.valueOf(genotype.charAt(0)))) count.add(Character.valueOf(genotype.charAt(0)));
/*  64 */         if (!count.contains(Character.valueOf(genotype.charAt(1)))) { count.add(Character.valueOf(genotype.charAt(1)));
/*     */         }
/*     */       }
/*     */     }
/*  68 */     if (count.size() > 2) throw new RuntimeException("there are more than two alleles");
/*  69 */     if (count.size() == 2) return true;
/*  70 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*  77 */   SortedMap<Integer, String> snpInf = new TreeMap();
/*     */   
/*  79 */   public void HapmapCollection(File f, String chr) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*  80 */     String[] headline = br.readLine().split("\\s+");
/*  81 */     List<Integer> sampleIndex = getSampleIndex(headline);
/*     */     
/*  83 */     this.dest = new FileOutputStream(chr + ".zip");
/*  84 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  85 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     
/*     */     String st;
/*  88 */     while ((st = br.readLine()) != null) { String st;
/*  89 */       String[] str = st.split("\\s+");
/*  90 */       if (checkPolymorphic(str, sampleIndex)) {
/*  91 */         this.snpInf.put(Integer.valueOf(Integer.parseInt(str[3])), str[0]);
/*  92 */         char a = str[1].charAt(0);
/*     */         
/*  94 */         ZipEntry headings = new ZipEntry(str[0]);
/*  95 */         this.outS.putNextEntry(headings);
/*  96 */         for (int i = 0; i < sampleIndex.size(); i++) {
/*  97 */           String genotype = str[((Integer)sampleIndex.get(i)).intValue()];
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 102 */           if (genotype.charAt(0) == a) this.osw.write("A"); else
/* 103 */             this.osw.write("B");
/* 104 */           if (genotype.charAt(1) == a) this.osw.write("A\n"); else {
/* 105 */             this.osw.write("B\n");
/*     */           }
/*     */         }
/* 108 */         this.osw.flush();
/* 109 */         this.outS.closeEntry();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample()
/*     */     throws Exception
/*     */   {
/* 117 */     ZipEntry headings = new ZipEntry("Samples");
/* 118 */     this.outS.putNextEntry(headings);
/* 119 */     for (int i = 0; i < this.sampleID.size(); i++) {
/* 120 */       this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */     }
/* 122 */     this.osw.flush();
/* 123 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(File f, String chr)
/*     */     throws Exception
/*     */   {
/* 129 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 130 */     String[] headline = br.readLine().split("\\s+");
/* 131 */     List<Integer> sampleIndex = getSampleIndex(headline);
/*     */     
/* 133 */     ZipEntry headings = new ZipEntry("Snps");
/* 134 */     this.outS.putNextEntry(headings);
/* 135 */     String st; while ((st = br.readLine()) != null) { String st;
/* 136 */       String[] str = st.split("\\s+");
/* 137 */       if (checkPolymorphic(str, sampleIndex)) {
/* 138 */         this.osw.write("chr" + chr + "\t" + str[3] + "\t" + (Integer.parseInt(str[3]) + 40) + "\t" + str[0] + "\t" + str[1].charAt(0) + "\t" + str[1].charAt(2) + "\n");
/*     */       }
/*     */     }
/* 141 */     this.osw.flush();
/* 142 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 146 */     ZipEntry headings = new ZipEntry("Name");
/* 147 */     this.outS.putNextEntry(headings);
/* 148 */     this.osw.write("genotype\n");
/* 149 */     this.osw.write("chr start end snpID A B\n");
/* 150 */     this.osw.write("sampleID");
/* 151 */     this.osw.flush();
/* 152 */     this.outS.closeEntry();
/* 153 */     this.outS.close();
/*     */   }
/*     */   
/*     */   List<String> matchedSNP;
/*     */   SortedMap<Double, Integer> randomIndex;
/*     */   List<String> markedSNP;
/*     */   public void matchedSNPs(File f, File fhapmap)
/*     */     throws Exception
/*     */   {
/* 162 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 163 */     BufferedReader br1 = new BufferedReader(new FileReader(fhapmap));
/*     */     
/* 165 */     List<String> hapmapSNP = new ArrayList();
/* 166 */     this.matchedSNP = new ArrayList();
/* 167 */     String st; while ((st = br1.readLine()) != null) { String st;
/* 168 */       hapmapSNP.add(st.split("\\s+")[3]);
/*     */     }
/* 170 */     int count = 0;
/* 171 */     while ((st = br.readLine()) != null) {
/* 172 */       if (hapmapSNP.contains(st.split("\\s+")[3])) this.matchedSNP.add(st.split("\\s+")[3]);
/* 173 */       if (st.split("\\s+")[0].equals("chrX")) count++;
/*     */     }
/* 175 */     this.randomIndex = new TreeMap();
/* 176 */     Random ran = new Random(System.currentTimeMillis());
/* 177 */     for (int h = 0; h < this.matchedSNP.size(); h++) {
/* 178 */       this.randomIndex.put(Double.valueOf(ran.nextDouble()), Integer.valueOf(h));
/*     */     }
/*     */   }
/*     */   
/*     */   public void markedSNPs(double percentageMarked)
/*     */   {
/* 184 */     this.markedSNP = new ArrayList();
/* 185 */     int count = 0;
/* 186 */     int cap = (int)(this.matchedSNP.size() * percentageMarked);
/* 187 */     for (Iterator<Double> is = this.randomIndex.keySet().iterator(); is.hasNext();) {
/* 188 */       Double s = (Double)is.next();
/* 189 */       count++;
/* 190 */       this.markedSNP.add((String)this.matchedSNP.get(((Integer)this.randomIndex.get(s)).intValue()));
/* 191 */       if (count > cap)
/*     */         break;
/*     */     }
/*     */   }
/*     */   
/* 196 */   public void printBuild(PrintStream ps, PrintStream ps1, File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 198 */     while ((st = br.readLine()) != null) { String st;
/* 199 */       String[] str = st.split("\\s+");
/* 200 */       if ((!str[4].equals("-")) && (!str[5].equals("-"))) {
/* 201 */         if (this.markedSNP.contains(st.split("\\s+")[3])) ps1.println(st); else {
/* 202 */           ps.println(st);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 210 */       HapmapCompressX im = new HapmapCompressX();
/*     */       
/* 212 */       im.matchedSNPs(new File("build36_1M.txt"), new File("build36.txt"));
/* 213 */       im.markedSNPs(Double.parseDouble(args[1]));
/* 214 */       PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("build36_1M_" + args[1]))));
/* 215 */       PrintStream ps1 = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("marked_1M_" + args[1]))));
/* 216 */       im.printBuild(ps, ps1, new File("build36_1M.txt"));
/* 217 */       ps.close();
/* 218 */       ps1.close();
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*     */ 
/*     */ 
/* 227 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/HapmapCompressX.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */