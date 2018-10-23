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
/*     */ public class ImputationCompress
/*     */ {
/*  31 */   List<String> sampleID = new ArrayList();
/*     */   
/*  33 */   public void getSampleID(File f) throws Exception { BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/*  35 */     while ((st = br.readLine()) != null) { String st;
/*  36 */       String[] str = st.split("\\s+");
/*  37 */       this.sampleID.add(str[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*  45 */   SortedMap<Integer, String> snpInf = new TreeMap();
/*     */   
/*  47 */   public void imputeCollection(File imputeOut, String chr) throws Exception { BufferedReader br = new BufferedReader(new FileReader(imputeOut));
/*  48 */     this.dest = new FileOutputStream(chr + ".zip");
/*  49 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  50 */     this.osw = new OutputStreamWriter(this.outS);
/*     */     String st;
/*  52 */     while ((st = br.readLine()) != null) { String st;
/*  53 */       String[] str = st.split("\\s+");
/*  54 */       this.snpInf.put(Integer.valueOf(Integer.parseInt(str[2])), str[1]);
/*     */       
/*  56 */       ZipEntry headings = new ZipEntry(str[1]);
/*  57 */       this.outS.putNextEntry(headings);
/*  58 */       if ((str.length - 5) / 3 != this.sampleID.size()) throw new RuntimeException("!!");
/*  59 */       for (int m = 5; m < str.length; m += 3) {
/*  60 */         this.osw.write("AB\t" + str[m] + "\t" + str[(m + 1)] + "\t" + str[(m + 2)] + "\n");
/*     */       }
/*  62 */       this.osw.flush();
/*  63 */       this.outS.closeEntry();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSample()
/*     */     throws Exception
/*     */   {
/*  70 */     ZipEntry headings = new ZipEntry("Samples");
/*  71 */     this.outS.putNextEntry(headings);
/*  72 */     for (int i = 0; i < this.sampleID.size(); i++) {
/*  73 */       this.osw.write((String)this.sampleID.get(i) + "\n");
/*     */     }
/*  75 */     this.osw.flush();
/*  76 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(String chr)
/*     */     throws Exception
/*     */   {
/*  82 */     ZipEntry headings = new ZipEntry("Snps");
/*  83 */     this.outS.putNextEntry(headings);
/*  84 */     for (Iterator<Integer> is = this.snpInf.keySet().iterator(); is.hasNext();) {
/*  85 */       Integer s = (Integer)is.next();
/*  86 */       this.osw.write("chr" + chr + "\t" + s + "\t" + (s.intValue() + 40) + "\t" + (String)this.snpInf.get(s) + "\n");
/*     */     }
/*  88 */     this.osw.flush();
/*  89 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/*  93 */     ZipEntry headings = new ZipEntry("Name");
/*  94 */     this.outS.putNextEntry(headings);
/*  95 */     this.osw.write("genotype call1 call2 call3\n");
/*  96 */     this.osw.write("chr start end snpID\n");
/*  97 */     this.osw.write("sampleID");
/*  98 */     this.osw.flush();
/*  99 */     this.outS.closeEntry();
/* 100 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 107 */       ImputationCompress im = new ImputationCompress();
/* 108 */       im.getSampleID(new File(args[0]));
/* 109 */       im.imputeCollection(new File(args[1]), args[2]);
/* 110 */       im.writeSample();
/* 111 */       im.writeSnps(args[2]);
/* 112 */       im.writeName();
/*     */     } catch (Exception exc) {
/* 114 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/ImputationCompress.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */