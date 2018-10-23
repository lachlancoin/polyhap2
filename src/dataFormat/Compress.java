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
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Compress
/*     */ {
/*  34 */   List<String> sampleID = new ArrayList();
/*     */   
/*  36 */   public void getSampleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  38 */     while ((st = br.readLine()) != null) { String st;
/*  39 */       String[] str = st.split("\\s+");
/*  40 */       this.sampleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*  48 */   SortedMap<Integer, String> snpInf = new TreeMap();
/*     */   
/*  50 */   public void imputeCollection(File imputeOut, String chr, int ploidy) throws Exception { BufferedReader br = new BufferedReader(new FileReader(imputeOut));
/*  51 */     this.dest = new FileOutputStream(chr + ".zip");
/*  52 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  53 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     String st;
/*  55 */     while ((st = br.readLine()) != null) { String st;
/*  56 */       String[] str = st.split("\\s+");
/*  57 */       this.snpInf.put(Integer.valueOf(Integer.parseInt(str[1])), str[0]);
/*     */       
/*  59 */       ZipEntry headings = new ZipEntry(str[0]);
/*  60 */       this.outS.putNextEntry(headings);
/*  61 */       String a = "";
/*  62 */       for (int m = 2; m < str.length; m++) {
/*  63 */         if (!str[m].equals("N")) {
/*  64 */           a = str[m];
/*  65 */           break;
/*     */         }
/*     */       }
/*  68 */       if ((str.length - 2) / ploidy != this.sampleID.size()) throw new RuntimeException("the number of sample in the genotype file is not same in sample file");
/*  69 */       for (int m = 2; m < str.length; m += ploidy) {
/*  70 */         for (int i = 0; i < ploidy; i++) {
/*  71 */           if (str[(m + i)].equals("N")) { this.osw.write("N");
/*     */           }
/*  73 */           else if (str[(m + i)].equals(a)) this.osw.write("A"); else {
/*  74 */             this.osw.write("B");
/*     */           }
/*     */         }
/*  77 */         this.osw.write("\n");
/*     */       }
/*  79 */       this.osw.flush();
/*  80 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample()
/*     */     throws Exception
/*     */   {
/*  87 */     ZipEntry headings = new ZipEntry("Samples");
/*  88 */     this.outS.putNextEntry(headings);
/*  89 */     for (int i = 0; i < this.sampleID.size(); i++) {
/*  90 */       this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */     }
/*  92 */     this.osw.flush();
/*  93 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/*  99 */     ZipEntry headings = new ZipEntry("Snps");
/* 100 */     this.outS.putNextEntry(headings);
/* 101 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/* 102 */       Integer s = (Integer)is.next();
/* 103 */       this.osw.write("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/* 105 */     this.osw.flush();
/* 106 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void printSnps(PrintStream ps, String chr) {
/* 110 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/* 111 */       Integer s = (Integer)is.next();
/* 112 */       ps.print("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 117 */     ZipEntry headings = new ZipEntry("Name");
/* 118 */     this.outS.putNextEntry(headings);
/* 119 */     this.osw.write("genotype\n");
/* 120 */     this.osw.write("chr start end snpID\n");
/* 121 */     this.osw.write("sampleID");
/* 122 */     this.osw.flush();
/* 123 */     this.outS.closeEntry();
/* 124 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 131 */       if (args.length == 0) {
/* 132 */         args = new String[] { "--paramFile", "param.txt", "--column", "1" };
/*     */       }
/* 134 */       String[] cols = Constants.getCols(args);
/* 135 */       for (int ij = 0; ij < cols.length; ij++) {
/* 136 */         Constants.parse(args, Integer.valueOf(Integer.parseInt(cols[ij])));
/* 137 */         Compress im = new Compress();
/* 138 */         im.getSampleID(new File(Constants.inputSample));
/* 139 */         im.imputeCollection(new File(Constants.inputGenotype), Constants.chrom0(), Constants.maxNoCopies);
/* 140 */         im.writeSample();
/* 141 */         im.writeSnps(Constants.chrom0());
/* 142 */         im.writeName();
/* 143 */         PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("build35.txt"))));
/* 144 */         im.printSnps(ps, Constants.chrom0());
/* 145 */         ps.close();
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 149 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/Compress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */