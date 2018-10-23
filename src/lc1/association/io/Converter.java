/*     */ package lc1.association.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Converter
/*     */ {
/*  15 */   static final Converter converter = new Converter();
/*     */   final String[] raw;
/*     */   final char[] sym;
/*     */   
/*  19 */   static short getChromosome(String str1) { String str = str1.substring(0, str1.indexOf('\t'));
/*  20 */     if (str.indexOf('_') > 0) {
/*  21 */       str = str.split("_")[1];
/*     */     }
/*  23 */     short num = Short.parseShort(str.substring(0, str.length() - 9));
/*  24 */     return num;
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
/*     */   public void convert(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  41 */       File f = new File(args[0]);
/*     */       
/*  43 */       BufferedReader br = new BufferedReader(new FileReader(f));
/*  44 */       PrintWriter[] out = new PrintWriter[24];
/*  45 */       for (int i = 0; i < out.length; i++) {
/*  46 */         out[i] = new PrintWriter(new BufferedWriter(new FileWriter(new File("out_" + (i + 1) + ".txt"))));
/*     */       }
/*     */       
/*  49 */       int length = br.readLine().trim().split("\\s+").length;
/*  50 */       int chrom = 24;
/*  51 */       for (int i = 0; i < length; i++) {
/*  52 */         System.err.println("Individual " + (i + 1));
/*  53 */         String st = "";
/*  54 */         while ((st = br.readLine()) != null)
/*     */         {
/*  56 */           String[] split = st.split("\\s+");
/*  57 */           int d = getChromosome(split[0]) - 1;
/*  58 */           if (d != chrom) {
/*  59 */             chrom = d;
/*  60 */             System.err.println("\t chromosome " + (d + 1));
/*     */           }
/*  62 */           char genotype = getCode(split[(i + 1)]);
/*  63 */           out[d].print(genotype);
/*     */         }
/*     */         
/*  66 */         for (int d = 0; d < out.length; d++) {
/*  67 */           out[d].println();
/*  68 */           out[d].flush();
/*     */         }
/*  70 */         br.close();
/*  71 */         br = new BufferedReader(new FileReader(f));
/*  72 */         br.readLine();
/*     */       }
/*     */       
/*  75 */       for (int d = 0; d < out.length; d++) {
/*  76 */         out[d].close();
/*     */       }
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*  81 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private Converter() {
/*  86 */     this.raw = new String[] { "AA", "AC", "AG", "AT", "A-", "AN", "CC", "CG", "CT", "C-", "CN", "GG", "GT", "G-", "GN", "TT", "T-", "TN", "--", "-N", "NN" };this.sym = new char[] { 'A', 'M', 'R', 'W', 'L', 'E', 'C', 'S', 'Y', 'O', 'F', 'G', 'K', 'P', 'I', 'T', 'Q', 'J', '-', 'Z', 'N' };this.conv = new HashMap();this.conv1 = new HashMap(); for (int i = 0; i < this.raw.length; i++) { this.conv.put(this.raw[i], Character.valueOf(this.sym[i]));this.conv1.put(Character.valueOf(this.sym[i]), this.raw[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final Map<String, Character> conv;
/*     */   
/*     */ 
/*     */ 
/*     */   final Map<Character, String> conv1;
/*     */   
/*     */ 
/*     */ 
/*     */   String getGenotype(char code)
/*     */   {
/* 103 */     return (String)this.conv1.get(Character.valueOf(code));
/*     */   }
/*     */   
/*     */   char getCode(String genotype) {
/* 107 */     return ((Character)this.conv.get(genotype)).charValue();
/*     */   }
/*     */   
/*     */   public void convert(Data d) {}
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/Converter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */