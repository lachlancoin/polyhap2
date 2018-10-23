/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
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
/*     */ public class IMPUTECompress
/*     */ {
/*  32 */   List<String> sampleID = new ArrayList();
/*     */   
/*  34 */   public void getSampleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  36 */     while ((st = br.readLine()) != null) { String st;
/*  37 */       String[] str = st.split("\\s+");
/*  38 */       this.sampleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*  46 */   SortedMap<Integer, String> snpInf = new TreeMap();
/*     */   
/*  48 */   public void imputeCollection(File imputeOut, String chr) throws Exception { BufferedReader br = new BufferedReader(new FileReader(imputeOut));
/*  49 */     this.dest = new FileOutputStream(chr + ".zip");
/*  50 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  51 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     String st;
/*  53 */     while ((st = br.readLine()) != null) { String st;
/*  54 */       String[] str = st.split("\\s+");
/*  55 */       this.snpInf.put(Integer.valueOf(Integer.parseInt(str[2])), str[1]);
/*     */       
/*  57 */       ZipEntry headings = new ZipEntry(str[1]);
/*  58 */       this.outS.putNextEntry(headings);
/*  59 */       if ((str.length - 5) / 3 != this.sampleID.size()) throw new RuntimeException("!!");
/*  60 */       for (int m = 5; m < str.length; m += 3) {
/*  61 */         this.osw.write("AB\t" + str[m] + "\t" + str[(m + 1)] + "\t" + str[(m + 2)] + "\n");
/*     */       }
/*  63 */       this.osw.flush();
/*  64 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample()
/*     */     throws Exception
/*     */   {
/*  71 */     ZipEntry headings = new ZipEntry("Samples");
/*  72 */     this.outS.putNextEntry(headings);
/*  73 */     for (int i = 0; i < this.sampleID.size(); i++) {
/*  74 */       this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */     }
/*  76 */     this.osw.flush();
/*  77 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/*  83 */     ZipEntry headings = new ZipEntry("Snps");
/*  84 */     this.outS.putNextEntry(headings);
/*  85 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/*  86 */       Integer s = (Integer)is.next();
/*  87 */       this.osw.write("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/*  89 */     this.osw.flush();
/*  90 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/*  94 */     ZipEntry headings = new ZipEntry("Name");
/*  95 */     this.outS.putNextEntry(headings);
/*  96 */     this.osw.write("genotype call1 call2 call3\n");
/*  97 */     this.osw.write("chr start end snpID\n");
/*  98 */     this.osw.write("sampleID");
/*  99 */     this.osw.flush();
/* 100 */     this.outS.closeEntry();
/* 101 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 108 */       IMPUTECompress im = new IMPUTECompress();
/* 109 */       im.getSampleID(new File(args[0]));
/* 110 */       im.imputeCollection(new File(args[1]), args[2]);
/* 111 */       im.writeSample();
/* 112 */       im.writeSnps(args[2]);
/* 113 */       im.writeName();
/*     */     } catch (Exception exc) {
/* 115 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/IMPUTECompress.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */