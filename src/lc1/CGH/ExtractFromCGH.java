/*     */ package lc1.CGH;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ExtractFromCGH
/*     */ {
/*  22 */   List<String> chrs = new ArrayList();
/*     */   
/*     */   File[] files;
/*     */   Map<String, PrintWriter> output;
/*     */   int[] indices;
/*     */   Map<String, String> arrays;
/*     */   File outputParent;
/*     */   
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  34 */       ExtractFromCGH eh = new ExtractFromCGH();
/*  35 */       eh.run();
/*     */     } catch (Exception exc) {
/*  37 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   File[] outputDirs;
/*     */   BufferedReader[] in;
/*     */   PrintWriter log;
/*     */   String formatString;
/*     */   String formatString_h;
/*     */   String[] forb;
/*     */   ExtractFromCGH()
/*     */     throws Exception
/*     */   {
/*  24 */     for (int i = 1; i <= 22; i++) {
/*  25 */       this.chrs.add(i);
/*     */     }
/*     */     
/*  28 */     this.chrs.add("X");
/*  29 */     this.chrs.add("Y");
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
/*  42 */     this.output = new HashMap();
/*  43 */     this.indices = new int[] { 11, 12, 16, 17 };
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 157 */     this.forb = new String[] { "Corner", "NC2", "SRN", "SM", "PC" };File user = new File(System.getProperty("user.dir"));this.outputParent = user.getParentFile().getParentFile();this.outputDirs = new File[this.chrs.size()];this.log = new PrintWriter(new BufferedWriter(new FileWriter("log.txt")));
/*  54 */     for (int i = 0; i < this.outputDirs.length; i++) {
/*  55 */       this.outputDirs[i] = new File(user, (String)this.chrs.get(i));
/*  56 */       if (!this.outputDirs[i].exists()) this.outputDirs[i].mkdir();
/*     */     }
/*  58 */     this.arrays = readArrays();
/*  59 */     this.files = user.listFiles(new FileFilter() {
/*     */       public boolean accept(File f) {
/*  61 */         return (ExtractFromCGH.this.get(f.getName()) != null) && (f.getName().endsWith(".txt"));
/*     */       }
/*     */       
/*  64 */     });
/*  65 */     this.in = new BufferedReader[this.files.length];
/*  66 */     if (this.in.length < this.arrays.size()) {
/*  67 */       Set<String> set = new java.util.HashSet();
/*  68 */       for (int i = 0; i < this.in.length; i++) {
/*  69 */         int index = this.files[i].getName().indexOf("_S0");
/*  70 */         System.err.println(this.files[i].getName());
/*  71 */         set.add(this.files[i].getName().substring(0, index));
/*     */       }
/*  73 */       if (set.size() != this.in.length) {
/*  74 */         throw new RuntimeException("!!");
/*     */       }
/*     */       
/*  77 */       Set<String> set1 = this.arrays.keySet();
/*  78 */       set1.removeAll(set);
/*  79 */       for (Iterator<String> it = set1.iterator(); it.hasNext();) {
/*  80 */         this.log.println((String)it.next());
/*     */       }
/*  82 */       this.log.close();
/*  83 */       System.err.println(set);
/*     */       
/*  85 */       System.exit(0);
/*  86 */       throw new RuntimeException(this.in.length + " " + this.arrays.size());
/*     */     }
/*  88 */     StringBuffer sb = new StringBuffer();
/*  89 */     StringBuffer sb_h = new StringBuffer();
/*  90 */     String[] headRow = new String[this.files.length * 2];
/*     */     
/*  92 */     for (int i = 0; i < this.in.length; i++) {
/*  93 */       this.in[i] = new BufferedReader(new FileReader(this.files[i]));
/*  94 */       sb.append("\t%10.3f\t%10.3f");
/*  95 */       sb_h.append("\t%10s\t%10s");
/*  96 */       String name = this.files[i].getName();
/*  97 */       String alt = get(name);
/*  98 */       if (alt == null) {
/*  99 */         System.err.println("not contained " + name);
/*     */       }
/*     */       else {
/* 102 */         System.err.println("cont " + name);
/*     */       }
/* 104 */       headRow[(2 * i)] = (alt + "_R");
/* 105 */       headRow[(2 * i + 1)] = (alt + "_RU");
/*     */     }
/* 107 */     this.formatString = sb.toString();
/* 108 */     this.formatString_h = sb_h.toString();
/* 109 */     for (int i = 0; i < this.outputDirs.length; i++) {
/* 110 */       PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(this.outputDirs[i], "cghdata.txt"))));
/* 111 */       this.output.put(this.outputDirs[i].getName(), pw);
/* 112 */       pw.println("Name\tStart\tEnd" + Format.sprintf(this.formatString_h, headRow));
/*     */     }
/*     */   }
/*     */   
/*     */   public void finish() throws Exception
/*     */   {
/* 118 */     for (int i = 0; i < this.in.length; i++) {
/* 119 */       this.in[i].close();
/*     */     }
/* 121 */     for (Iterator<PrintWriter> it = this.output.values().iterator(); it.hasNext();) {
/* 122 */       ((PrintWriter)it.next()).close();
/*     */     }
/*     */   }
/*     */   
/*     */   public String get(String st) {
/* 127 */     for (Iterator<String> it = this.arrays.keySet().iterator(); it.hasNext();) {
/* 128 */       String key = (String)it.next();
/* 129 */       if (st.startsWith(key)) {
/* 130 */         return (String)this.arrays.get(key);
/*     */       }
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */   
/* 136 */   public Map<String, String> readArrays() throws Exception { Map<String, String> arrays = new HashMap();
/* 137 */     File f = new File("aCGH_barcodes.csv");
/* 138 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 139 */     String st = br.readLine();
/* 140 */     while ((st = br.readLine()) != null) {
/* 141 */       String[] str = st.split("\\t");
/* 142 */       arrays.put(str[0], 
/*     */       
/* 144 */         str[1]);
/*     */     }
/* 146 */     return arrays;
/*     */   }
/*     */   
/*     */   private void checkHeaders(String st)
/*     */   {
/* 151 */     if ((st.indexOf("LogRatio") >= 0) && (st.indexOf("ProbeName") >= 0)) {
/* 152 */       String[] str = st.split("\\t");
/* 153 */       if ((!str[15].equals("LogRatio")) || (!str[16].equals("LogRatioError"))) throw new RuntimeException("!!");
/*     */     }
/*     */   }
/*     */   
/*     */   public void run()
/*     */     throws Exception
/*     */   {
/* 160 */     Double[] res = new Double[this.in.length * 2];
/*     */     
/* 162 */     String st = "";
/* 163 */     String name = "";
/* 164 */     String chr = "";
/* 165 */     int start = 0;
/* 166 */     int end = 0;
/* 167 */     for (int ik = 0; ik < 13; ik++) {
/* 168 */       for (int i = 0; i < this.in.length; i++) {
/* 169 */         st = this.in[i].readLine();
/* 170 */         if (i == 0) System.err.println("skipping " + st);
/* 171 */         checkHeaders(st);
/*     */       }
/*     */     }
/* 174 */     System.err.println(st);
/*     */     for (;;)
/*     */     {
/* 177 */       for (int i = 0; i < this.in.length; i++) {
/* 178 */         st = this.in[i].readLine();
/* 179 */         if (st == null) break label518;
/* 180 */         String[] str = st.split("\\t");
/*     */         try
/*     */         {
/* 183 */           if (i == 0) {
/* 184 */             for (int j = 0; j < this.forb.length; j++) {
/* 185 */               if (st.indexOf("A_") < 0) {
/* 186 */                 checkHeaders(st);
/* 187 */                 for (int i1 = 1; i1 < this.in.length; i1++) {
/* 188 */                   st = this.in[i1].readLine();
/* 189 */                   if (st.indexOf("A_") >= 0) throw new RuntimeException("");
/*     */                 }
/* 191 */                 break;
/*     */               }
/*     */             }
/*     */             
/* 195 */             name = str[10];
/* 196 */             String[] st1 = str[12].split(":");
/* 197 */             chr = st1[0].substring(st1[0].indexOf("chr") + 3);
/*     */             
/* 199 */             String[] st2 = st1[1].split("-");
/* 200 */             start = Integer.parseInt(st2[0]);
/* 201 */             end = Integer.parseInt(st2[1]);
/*     */ 
/*     */           }
/* 204 */           else if (!str[10].equals(name)) {
/* 205 */             throw new RuntimeException("!!");
/*     */           }
/*     */         } catch (Exception exc) {
/* 208 */           exc.printStackTrace();
/* 209 */           System.err.println("prob with " + str[12] + "\n" + st);
/* 210 */           System.exit(0);
/*     */         }
/* 212 */         String sc1 = str[15];
/* 213 */         String sc2 = str[16];
/* 214 */         res[(2 * i)] = Double.valueOf(Double.parseDouble(sc1));
/* 215 */         res[(2 * i + 1)] = Double.valueOf(Double.parseDouble(sc2));
/*     */       }
/* 217 */       PrintWriter outp = (PrintWriter)this.output.get(chr);
/* 218 */       outp.println(name + "\t" + start + "\t" + end + Format.sprintf(this.formatString, res));
/* 219 */       outp.flush(); }
/*     */     label518:
/* 221 */     finish();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/ExtractFromCGH.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */