/*     */ package lc1.dp.illumina;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.CGH.AberationFinder;
/*     */ import lc1.CGH.AbstractAberatiionReader;
/*     */ import lc1.CGH.CGHDataCollection;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.CGH.Locreader;
/*     */ import lc1.CGH.SNPAberrationReader;
/*     */ 
/*     */ public class ErrorRate implements Runnable
/*     */ {
/*     */   Locreader french;
/*     */   lc1.dp.data.collection.SimpleDataCollection sdt;
/*     */   
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  31 */       ConsoleHandler handler = new ConsoleHandler();
/*  32 */       FileHandler handlerF = new FileHandler("stderr", false);
/*  33 */       Logger.global.addHandler(handlerF);
/*  34 */       Formatter formatter = 
/*  35 */         new Formatter() {
/*     */           public String format(LogRecord record) {
/*  37 */             return record.getSourceClassName() + ":\n" + record.getMessage() + "\n";
/*     */           }
/*  39 */         };
/*  40 */         handler.setFormatter(formatter);
/*  41 */         handlerF.setFormatter(formatter);
/*     */       } catch (Exception exc) {
/*  43 */         exc.printStackTrace();
/*  44 */         System.exit(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  49 */     AbstractAberatiionReader agilent = new lc1.CGH.MultiProbeAberationReader(Long.MAX_VALUE, "");
/*     */     
/*     */     CGHDataCollection cghdat;
/*     */     
/*     */     File[] files;
/*     */     String chromosome;
/*     */     
/*     */     public ErrorRate(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosome, Location reg)
/*     */       throws Exception
/*     */     {
/*  59 */       this.agilent.initialise(dir, chromosome, null, 0, 0, "MultiProbeByIndividual.txt", null);
/*  60 */       this.agilent.sort();
/*  61 */       this.agilent.mergeNames();
/*  62 */       this.agilent.sort();
/*  63 */       this.agilent.merge(0.5D);
/*  64 */       this.french = new SNPAberrationReader(dir, this.chromosome, AberationFinder.noOfSnps, 
/*  65 */         null, "FrenchSamples.txt", AberationFinder.threshold, null, "snp");
/*  66 */       File df = dir.getParentFile();
/*     */       for (;;) {
/*  68 */         System.err.println(df);
/*  69 */         if (new File(df, "data").exists()) break;
/*  70 */         df = df.getParentFile();
/*     */       }
/*  72 */       File cghFile = new File(df, "data/" + chromosome + "_cghdata.txt");
/*  73 */       this.cghdat = null;
/*     */       
/*     */ 
/*  76 */       this.files = user;
/*  77 */       this.chromosome = chromosome;
/*     */     }
/*     */     
/*  80 */     double cntfalse = 0.0D;
/*  81 */     double cnttrue = 0.0D;
/*  82 */     double cntfalseR = 0.0D;
/*  83 */     double cnttrueR = 0.0D;
/*     */     
/*  85 */     double cont = 0.0D;
/*  86 */     double notcont = 0.0D;
/*     */     
/*     */ 
/*     */ 
/*     */     public void run()
/*     */     {
/*  92 */       if (!this.chromosome.equals("12")) return;
/*     */       try {
/*  94 */         Logger.global.info("files " + java.util.Arrays.asList(this.files));
/*  95 */         for (int i = 0; 
/*     */             
/*     */ 
/*  98 */             i < this.files.length; 
/*  99 */             i++) {
/* 100 */           SNPAberrationReader snps = new SNPAberrationReader(this.files[i], this.chromosome, AberationFinder.noOfSnps, 
/* 101 */             null, "cnv.txt", AberationFinder.threshold, null, "snp");
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
/* 130 */           List<Integer> snplocs = new ArrayList();
/* 131 */           lc1.dp.data.collection.IlluminaRDataCollection.readPosInfo(
/* 132 */             AberationFinder.getBufferedReader(this.files[i], "snp.txt"), new int[] { 2 }, true, new List[] { snplocs }, new Class[] { Integer.class });
/*     */           
/* 134 */           System.err.println("doing " + i);
/* 135 */           Logger.global.info("file  " + i + " of " + this.files.length);
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */           Iterator<Location> it = this.french.iterator();
/*     */           
/* 145 */           while (it.hasNext())
/*     */           {
/* 147 */             Location loc = (Location)it.next();
/*     */             
/*     */ 
/* 150 */             Boolean val = this.cghdat.validate(loc, 0.05D, 0.2D, false);
/* 151 */             if (val != null) {
/* 152 */               if (val.booleanValue()) this.cnttrue += 1.0D; else
/* 153 */                 this.cntfalse += 1.0D;
/*     */             }
/* 155 */             double ratio = this.cnttrue / (this.cnttrue + this.cntfalse);
/* 156 */             System.err.println("CNT " + this.cnttrue + " " + this.cntfalse + " " + ratio);
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (Exception exc)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 178 */         exc.printStackTrace();
/*     */       }
/* 180 */       double ratio = this.cnttrue / (this.cnttrue + this.cntfalse);
/* 181 */       double ratioR = this.cnttrueR / (this.cnttrueR + this.cntfalseR);
/* 182 */       System.err.println("cnts " + this.cnttrue + " " + this.cntfalse + " " + ratio);
/* 183 */       System.err.println("cnts RANDOM " + this.cnttrueR + " " + this.cntfalseR + " " + ratioR);
/* 184 */       System.err.println();
/*     */     }
/*     */   }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/illumina/ErrorRate.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */