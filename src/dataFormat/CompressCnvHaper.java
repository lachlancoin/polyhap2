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
/*     */ public class CompressCnvHaper
/*     */ {
/*  25 */   List<String> sampleID = new ArrayList();
/*     */   
/*  27 */   public void getSampleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  29 */     while ((st = br.readLine()) != null) { String st;
/*  30 */       String[] str = st.split("\\s+");
/*  31 */       this.sampleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*  39 */   SortedMap<Integer, String> snpInf = new TreeMap();
/*     */   
/*  41 */   public void cnvCollection(File imputeOut, String chr) throws Exception { BufferedReader br = new BufferedReader(new FileReader(imputeOut));
/*  42 */     this.dest = new FileOutputStream(chr + ".zip");
/*  43 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  44 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     String st;
/*  46 */     while ((st = br.readLine()) != null) { String st;
/*  47 */       String[] str = st.split("\\s+");
/*  48 */       this.snpInf.put(Integer.valueOf(Integer.parseInt(str[1])), str[0]);
/*     */       
/*  50 */       ZipEntry headings = new ZipEntry(str[0]);
/*  51 */       this.outS.putNextEntry(headings);
/*  52 */       Character a = null;
/*  53 */       if (str.length - 2 != this.sampleID.size()) throw new RuntimeException("the number of sample in the genotype file is not same in sample file");
/*  54 */       for (int m = 2; m < str.length; m++) {
/*  55 */         if (str[m].charAt(0) != 'N') a = Character.valueOf(str[m].charAt(0));
/*  56 */         for (int i = 0; i < str[m].length(); i++) {
/*  57 */           if (str[m].charAt(i) == 'N') { this.osw.write("N");
/*     */           }
/*  59 */           else if (str[(m + i)].charAt(i) == a.charValue()) this.osw.write("A"); else {
/*  60 */             this.osw.write("B");
/*     */           }
/*     */         }
/*  63 */         this.osw.write("\n");
/*     */       }
/*  65 */       this.osw.flush();
/*  66 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample()
/*     */     throws Exception
/*     */   {
/*  73 */     ZipEntry headings = new ZipEntry("Samples");
/*  74 */     this.outS.putNextEntry(headings);
/*  75 */     for (int i = 0; i < this.sampleID.size(); i++) {
/*  76 */       this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */     }
/*  78 */     this.osw.flush();
/*  79 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/*  85 */     ZipEntry headings = new ZipEntry("Snps");
/*  86 */     this.outS.putNextEntry(headings);
/*  87 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/*  88 */       Integer s = (Integer)is.next();
/*  89 */       this.osw.write("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/*  91 */     this.osw.flush();
/*  92 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void printSnps(PrintStream ps, String chr) {
/*  96 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/*  97 */       Integer s = (Integer)is.next();
/*  98 */       ps.print("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 103 */     ZipEntry headings = new ZipEntry("Name");
/* 104 */     this.outS.putNextEntry(headings);
/* 105 */     this.osw.write("genotype\n");
/* 106 */     this.osw.write("chr start end snpID\n");
/* 107 */     this.osw.write("sampleID");
/* 108 */     this.osw.flush();
/* 109 */     this.outS.closeEntry();
/* 110 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 117 */       if (args.length == 0) {
/* 118 */         args = new String[] { "--paramFile", "param.txt", "--column", "1" };
/*     */       }
/* 120 */       String[] cols = Constants.getCols(args);
/* 121 */       for (int ij = 0; ij < cols.length; ij++) {
/* 122 */         Constants.parse(args, Integer.valueOf(Integer.parseInt(cols[ij])));
/* 123 */         CompressCnvHaper im = new CompressCnvHaper();
/* 124 */         im.getSampleID(new File(Constants.inputSample));
/* 125 */         im.cnvCollection(new File(Constants.inputGenotype), Constants.chrom0());
/* 126 */         im.writeSample();
/* 127 */         im.writeSnps(Constants.chrom0());
/* 128 */         im.writeName();
/* 129 */         PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(Constants.build(0)))));
/* 130 */         im.printSnps(ps, Constants.chrom0());
/* 131 */         ps.close();
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 135 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/CompressCnvHaper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */