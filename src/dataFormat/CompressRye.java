/*     */ package dataFormat;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ 
/*     */ public class CompressRye
/*     */ {
/*     */   List<Integer> order;
/*     */   FileOutputStream dest;
/*     */   ZipOutputStream outS;
/*     */   OutputStreamWriter osw;
/*     */   List<String> snpID;
/*     */   
/*     */   public void getOrder(File f)
/*     */     throws Exception
/*     */   {
/*  25 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  26 */     this.order = new ArrayList();
/*  27 */     String st = "";
/*  28 */     while ((st = br.readLine()) != null) {
/*  29 */       this.order.add(Integer.valueOf(Integer.parseInt(st)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void RyeDataCollection(File rye, int chr)
/*     */     throws Exception
/*     */   {
/*  40 */     BufferedReader br = new BufferedReader(new FileReader(rye));
/*  41 */     this.dest = new FileOutputStream(chr + ".zip");
/*  42 */     this.outS = new ZipOutputStream(new BufferedOutputStream(this.dest));
/*  43 */     this.osw = new OutputStreamWriter(this.outS);
/*  44 */     String st = "";
/*  45 */     this.snpID = new ArrayList();
/*  46 */     String[] sampleID = br.readLine().split("\\s+");
/*  47 */     br.readLine();
/*  48 */     while ((st = br.readLine()) != null) {
/*  49 */       String[] temp = st.split("\\s+");
/*  50 */       this.snpID.add(temp[0]);
/*     */       
/*  52 */       ZipEntry headings = new ZipEntry(temp[0]);
/*  53 */       this.outS.putNextEntry(headings);
/*  54 */       for (int i = 5; i < temp.length; i += 2) {
/*  55 */         this.osw.write(temp[i] + "\n");
/*     */       }
/*  57 */       this.osw.flush();
/*  58 */       this.outS.closeEntry();
/*     */     }
/*     */     
/*     */ 
/*  62 */     ZipEntry headings = new ZipEntry("Samples");
/*  63 */     this.outS.putNextEntry(headings);
/*  64 */     for (int i = 5; i < sampleID.length; i += 2) {
/*  65 */       sampleID[i].replace("”", "");
/*  66 */       this.osw.write(sampleID[i].replace("”", "") + "\n");
/*     */     }
/*  68 */     this.osw.flush();
/*  69 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeSnps(int chr)
/*     */     throws Exception
/*     */   {
/*  75 */     ZipEntry headings = new ZipEntry("Snps");
/*  76 */     this.outS.putNextEntry(headings);
/*  77 */     int position = 0;
/*  78 */     for (int i = 0; i < this.order.size(); i++) {
/*  79 */       this.osw.write(chr + "\t");
/*  80 */       position += 40;this.osw.write(position + "\t");
/*  81 */       position += 40;this.osw.write(position + "\t");
/*  82 */       this.osw.write((String)this.snpID.get(((Integer)this.order.get(i)).intValue()) + "\n");
/*     */     }
/*  84 */     this.osw.flush();
/*  85 */     this.outS.closeEntry();
/*     */   }
/*     */   
/*     */   public void writeName() throws Exception {
/*  89 */     ZipEntry headings = new ZipEntry("Name");
/*  90 */     this.outS.putNextEntry(headings);
/*  91 */     this.osw.write("B allele\n");
/*  92 */     this.osw.write("chr start end snpID\n");
/*  93 */     this.osw.write("sampleID");
/*  94 */     this.osw.flush();
/*  95 */     this.outS.closeEntry();
/*  96 */     this.outS.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/* 103 */       CompressRye cr = new CompressRye();
/* 104 */       cr.getOrder(new File("orderedMarker.txt"));
/* 105 */       cr.RyeDataCollection(new File("TestDataRye.txt"), 1);
/* 106 */       cr.writeSnps(1);
/* 107 */       cr.writeName();
/*     */     } catch (Exception exc) {
/* 109 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/dataFormat/CompressRye.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */