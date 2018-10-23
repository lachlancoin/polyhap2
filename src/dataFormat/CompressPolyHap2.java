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
/*     */ public class CompressPolyHap2
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
/*  55 */         if ((str[m].charAt(0) != 'N') && (str[m].charAt(0) != '_')) {
/*  56 */           a = Character.valueOf(str[m].charAt(0));
/*  57 */           break;
/*     */         }
/*     */       }
/*  60 */       for (int m = 2; m < str.length; m++) {
/*  61 */         if (str[m].equals("NA")) { this.osw.write("NA");
/*     */         } else {
/*  63 */           for (int i = 0; i < str[m].length(); i++) {
/*  64 */             if (str[m].charAt(i) == 'N') { this.osw.write("N");
/*  65 */             } else if (str[m].charAt(i) == '_') { this.osw.write("_");
/*     */             }
/*  67 */             else if (str[m].charAt(i) == a.charValue()) this.osw.write("A"); else {
/*  68 */               this.osw.write("B");
/*     */             }
/*     */           }
/*     */         }
/*  72 */         this.osw.write("\n");
/*     */       }
/*  74 */       this.osw.flush();
/*  75 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void cnvCollectionInternal(File imputeOut, String chr, int ploidy) throws Exception {
/*  80 */     BufferedReader br = new BufferedReader(new FileReader(imputeOut));
/*  81 */     this.dest = new FileOutputStream(chr + ".zip");
/*  82 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  83 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     String st;
/*  85 */     while ((st = br.readLine()) != null) { String st;
/*  86 */       String[] str = st.split("\\s+");
/*  87 */       this.snpInf.put(Integer.valueOf(Integer.parseInt(str[1])), str[0]);
/*     */       
/*  89 */       ZipEntry headings = new ZipEntry(str[0]);
/*  90 */       this.outS.putNextEntry(headings);
/*  91 */       Character a = null;
/*  92 */       if (str.length - 2 != this.sampleID.size()) throw new RuntimeException("the number of sample in the genotype file is not same in sample file");
/*  93 */       for (int m = 2; m < str.length; m++) {
/*  94 */         if ((str[m].charAt(0) != 'N') && (str[m].charAt(0) != '_')) {
/*  95 */           a = Character.valueOf(str[m].charAt(0));
/*  96 */           break;
/*     */         }
/*     */       }
/*  99 */       for (int m = 2; m < str.length; m++) {
/* 100 */         if (str[m].equals("NA")) { this.osw.write("NA");
/*     */         } else {
/* 102 */           for (int i = 0; i < ploidy; i++) {
/* 103 */             if (i < str[m].length()) {
/* 104 */               if (str[m].charAt(i) == 'N') { this.osw.write("N");
/* 105 */               } else if (str[m].charAt(i) == '_') { this.osw.write("_");
/*     */               }
/* 107 */               else if (str[m].charAt(i) == a.charValue()) this.osw.write("A"); else {
/* 108 */                 this.osw.write("B");
/*     */               }
/*     */             } else
/* 111 */               this.osw.write("_");
/*     */           }
/*     */         }
/* 114 */         this.osw.write("\n");
/*     */       }
/* 116 */       this.osw.flush();
/* 117 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample()
/*     */     throws Exception
/*     */   {
/* 124 */     ZipEntry headings = new ZipEntry("Samples");
/* 125 */     this.outS.putNextEntry(headings);
/* 126 */     for (int i = 0; i < this.sampleID.size(); i++) {
/* 127 */       this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */     }
/* 129 */     this.osw.flush();
/* 130 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/* 136 */     ZipEntry headings = new ZipEntry("Snps");
/* 137 */     this.outS.putNextEntry(headings);
/* 138 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/* 139 */       Integer s = (Integer)is.next();
/* 140 */       this.osw.write("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/* 142 */     this.osw.flush();
/* 143 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void printSnps(PrintStream ps, String chr) {
/* 147 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/* 148 */       Integer s = (Integer)is.next();
/* 149 */       ps.print("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/* 154 */     ZipEntry headings = new ZipEntry("Name");
/* 155 */     this.outS.putNextEntry(headings);
/* 156 */     this.osw.write("genotype\n");
/* 157 */     this.osw.write("chr start end snpID\n");
/* 158 */     this.osw.write("sampleID");
/* 159 */     this.osw.flush();
/* 160 */     this.outS.closeEntry();
/* 161 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       if (args.length == 0) {
/* 169 */         args = new String[] { "--paramFile", "param.txt", "--column", "1" };
/*     */       }
/* 171 */       String[] cols = Constants.getCols(args);
/* 172 */       for (int ij = 0; ij < cols.length; ij++) {
/* 173 */         Constants.parse(args, Integer.valueOf(Integer.parseInt(cols[ij])));
/* 174 */         CompressPolyHap2 im = new CompressPolyHap2();
/* 175 */         im.getSampleID(new File(Constants.inputSample));
/* 176 */         if (Constants.phasingType == 0) {
/* 177 */           im.cnvCollection(new File(Constants.inputGenotype), Constants.chrom0());
/*     */         }
/*     */         else {
/* 180 */           im.cnvCollectionInternal(new File(Constants.inputGenotype), Constants.chrom0(), Constants.noCopies[0]);
/*     */         }
/* 182 */         im.writeSample();
/* 183 */         im.writeSnps(Constants.chrom0());
/* 184 */         im.writeName();
/* 185 */         PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File(Constants.build(0)))));
/* 186 */         im.printSnps(ps, Constants.chrom0());
/* 187 */         ps.close();
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 191 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/CompressPolyHap2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */