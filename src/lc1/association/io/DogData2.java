/*     */ package lc1.association.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class DogData2
/*     */   extends Data
/*     */ {
/*  24 */   static Logger logger = ;
/*     */   BufferedReader br;
/*     */   String currentString;
/*     */   int qtl_chrom;
/*     */   Number[] phen2;
/*     */   List<IntSNP> snps;
/*     */   final long length;
/*     */   short offset;
/*     */   boolean numeric;
/*     */   
/*     */   int getQtlChrom() {
/*  35 */     return this.qtl_chrom;
/*     */   }
/*     */   
/*     */   short getChromosome(String str)
/*     */   {
/*  40 */     return Short.parseShort(str.split("_")[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   void seek(short chromosome, String id)
/*     */     throws Exception
/*     */   {
/*  47 */     throw new Exception("not implemented");
/*     */   }
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
/*     */   IntSNP readSnp()
/*     */   {
/*  94 */     int i = Constants.nextInt(this.snps.size());
/*  95 */     return (IntSNP)this.snps.remove(i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   IntSNP nextSnp()
/*     */   {
/* 102 */     if (this.currentString == null) throw new RuntimeException("current string is null ");
/* 103 */     IntSNP snp = null;
/*     */     try
/*     */     {
/*     */       do {
/* 107 */         this.count1 += 1;
/* 108 */         if (this.count1 == 1000) {
/* 109 */           this.count2 += 1;
/* 110 */           this.count1 = 0;
/* 111 */           logger.info(this.count2 * 1000 + " ");
/*     */         }
/*     */         
/* 114 */         List<String> l = Arrays.asList(this.currentString.split("\t"));
/* 115 */         if (this.numeric) {
/*     */           try {
/* 117 */             snp = new IntSNP((String)l.get(0), l.subList(1, l.size()), this.offset);
/*     */           }
/*     */           catch (NumberFormatException exc) {
/* 120 */             logger.info("detected ACTG encoding");
/* 121 */             this.numeric = false;
/* 122 */             snp = (IntSNP)IntSNP.makeSNP((String)l.get(0), "0", l.subList(1, l.size()));
/*     */           }
/*     */           catch (ArithmeticException exc) {
/* 125 */             logger.info("detected 0-2 range");
/* 126 */             if (this.offset == 1) throw new Exception(" cannot determine form of numerical encoding");
/* 127 */             this.offset = ((short)(this.offset + 1));
/* 128 */             snp = new IntSNP((String)l.get(0), l.subList(1, l.size()), this.offset);
/*     */           }
/*     */         } else {
/* 131 */           snp = (IntSNP)IntSNP.makeSNP((String)l.get(0), "0", l.subList(1, l.size()));
/*     */         }
/*     */         
/*     */ 
/* 135 */         this.currentString = this.br.readLine();
/* 106 */         if (this.currentString == null) break; } while (snp == null);
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
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
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
/* 139 */       exc.printStackTrace();
/* 140 */       logger.warning("terminal error ");
/* 141 */       System.exit(0);
/*     */     }
/* 143 */     return snp;
/*     */   }
/*     */   
/*     */   DogData2(File f, boolean trait)
/*     */     throws Exception
/*     */   {
/*  26 */     logger.setLevel(Level.ALL);
/*     */     
/*     */ 
/*     */ 
/*  30 */     this.currentString = "";
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
/*  44 */     this.snps = new LinkedList();
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
/*  98 */     this.offset = 0;
/*  99 */     this.numeric = true;
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
/* 150 */     this.length = f.length();
/* 151 */     logger.info("length is " + this.length);
/* 152 */     this.br = new BufferedReader(new FileReader(f));
/* 153 */     this.br.readLine();
/* 154 */     readQtls(this.br, trait);
/* 155 */     this.currentString = this.br.readLine();
/* 156 */     logger.info("currentString " + this.currentString);
/* 157 */     while (this.currentString != null) {
/* 158 */       this.snps.add(nextSnp());
/*     */     }
/*     */   }
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
/*     */   void readQtls(BufferedReader f, boolean trait)
/*     */     throws Exception
/*     */   {
/* 178 */     String[] split = f.readLine().split("\t");
/* 179 */     setIndividuals(split.length - 1);
/* 180 */     this.phen2 = new Number[split.length - 1];
/*     */     
/*     */ 
/*     */ 
/* 184 */     for (int j = 1; j < split.length; j++) {
/* 185 */       this.phenotype[(j - 1)] = Integer.valueOf(Integer.parseInt(split[j]));
/*     */     }
/* 187 */     split = f.readLine().split("\t");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 195 */     for (int j = 1; j < split.length; j++) {
/* 196 */       this.breed[(j - 1)] = split[j];
/*     */     }
/* 198 */     logger.info("breeds " + Arrays.asList(this.breed));
/* 199 */     this.breed_types = new HashSet((Collection)Arrays.asList(this.breed));
/* 200 */     logger.info("breeds " + this.breed_types);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Integer currentChromosome()
/*     */     throws Exception
/*     */   {
/* 214 */     throw new Exception("not implementd");
/*     */   }
/*     */   
/* 217 */   boolean hasMore() { return this.snps.size() > 0; }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/DogData2.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */