/*     */ package lc1.possel;
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
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import lc1.CGH.AberationFinder;
/*     */ import lc1.stats.TrainableNormal;
/*     */ import lc1.util.Constants;
/*     */ import org.apache.commons.cli.CommandLine;
/*     */ import org.apache.commons.cli.OptionBuilder;
/*     */ import org.apache.commons.cli.Options;
/*     */ import org.apache.commons.cli.Parser;
/*     */ import org.apache.commons.cli.PosixParser;
/*     */ 
/*     */ public class EHHMultiChromosome
/*     */ {
/*     */   final File user;
/*     */   Map<Double, TrainableNormal> normal;
/*  37 */   public static boolean overl = true;
/*  38 */   public static boolean graphAll = false;
/*     */   int noCop;
/*     */   PrintWriter score;
/*     */   
/*  42 */   public void setNoCop(int noCop) throws Exception { File nF = new File(this.user, "norm.txt");
/*  43 */     this.scoring = ((nF.exists()) && (nF.length() > 0L));
/*  44 */     this.normal = read(nF);
/*  45 */     File out = new File(this.user, "score_" + noCop + "_" + "0.txt");
/*  46 */     for (int i = 0; (out.exists()) && (out.length() > 0L); i++) {
/*  47 */       out = new File(this.user, "score_" + noCop + "_" + i + ".txt");
/*     */     }
/*  49 */     System.err.println("out file is " + out);
/*  50 */     this.score = new PrintWriter(new BufferedWriter(new FileWriter(out)));
/*  51 */     String fs1 = "%-5s\t%15s\t%15s\t%10s\t%5s\t%5s\t%5s";
/*  52 */     Object[] toPrintH = { "Name", "snp_id", "snp_id", "loc", "derF", "ratio", "pval" };
/*     */     
/*  54 */     this.score.println(Format.sprintf(fs1, toPrintH));
/*  55 */     this.score.flush();
/*  56 */     this.noCop = noCop;
/*     */   }
/*     */   
/*  59 */   public EHHMultiChromosome(File user, File baseDir, File input, int num) throws Exception { this.user = user;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  65 */     if (input == null) {
/*  66 */       File[] f1 = user.listFiles(new FileFilter()
/*     */       {
/*     */         public boolean accept(File pathname) {
/*  69 */           return (pathname.getName().endsWith(".tar.gz")) && (!pathname.getName().startsWith("X")) && (!pathname.getName().startsWith("Y"));
/*     */         }
/*     */         
/*  72 */       });
/*  73 */       File[] f = new File[f1.length];
/*  74 */       for (int i = 0; i < f.length; i++) {
/*  75 */         f[i] = f1[i];
/*     */       }
/*     */     }
/*     */     else {
/*  79 */       throw new RuntimeException("!!");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     File[] f;
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
/*     */   static Map<Double, TrainableNormal> read(File file)
/*     */     throws Exception
/*     */   {
/* 110 */     Map<Double, TrainableNormal> normal = new TreeMap();
/* 111 */     if (file.exists()) {
/* 112 */       BufferedReader br = new BufferedReader(new FileReader(file));
/* 113 */       String st = "";
/* 114 */       while ((st = br.readLine()) != null) {
/* 115 */         String[] str = st.split("\\t");
/* 116 */         normal.put(Double.valueOf(Double.parseDouble(str[0])), new TrainableNormal(str[2], 100));
/*     */       }
/*     */       
/*     */ 
/* 120 */       return normal;
/*     */     }
/* 122 */     return new HashMap(); }
/*     */   
/* 124 */   static Comparator<File> cc = new Comparator()
/*     */   {
/*     */     public int compare(File o1, File o2) {
/* 127 */       int i1 = Integer.parseInt((String)EHHMultiChromosome.split(o1).get(1));
/* 128 */       int i2 = Integer.parseInt((String)EHHMultiChromosome.split(o2).get(1));
/* 129 */       if (i1 != i2) return i1 < i2 ? -1 : 1;
/* 130 */       return 0;
/*     */     }
/*     */   };
/*     */   boolean scoring;
/*     */   
/* 135 */   static List<String> split(File f) { String[] st = f.getName().split("\\.")[0].split("_");
/* 136 */     return Arrays.asList(st);
/*     */   }
/*     */   
/* 139 */   Class clazz = LDInner.class;
/*     */   
/*     */   public static Iterator<Iterator<Object[]>> getIterator(File user) {
/* 142 */     Map<String, List<File>> fileMap = new TreeMap(new Comparator()
/*     */     {
/*     */       public int compare(String o1, String o2) {
/* 145 */         String[] oo1 = o1.split("_");
/* 146 */         String[] oo2 = o2.split("_");
/* 147 */         for (int i = 0; i < oo1.length; i++) {
/* 148 */           int i1 = Integer.parseInt(oo1[i]);
/* 149 */           int i2 = Integer.parseInt(oo2[i]);
/* 150 */           if (i1 != i2) return i1 < i2 ? -1 : 1;
/*     */         }
/* 152 */         return 0;
/*     */       }
/*     */       
/* 155 */     });
/* 156 */     File[] f = user.listFiles(new FileFilter()
/*     */     {
/*     */       public boolean accept(File pathname) {
/* 159 */         return (pathname.getName().endsWith(".tar.gz")) && (!pathname.getName().startsWith("X")) && (!pathname.getName().startsWith("Y"));
/*     */       }
/*     */     });
/*     */     
/*     */ 
/* 164 */     for (int i = 0; i < f.length; i++) {
/* 165 */       String chr = 
/* 166 */         (String)split(f[i]).get(0);
/*     */       
/* 168 */       List<File> l = (List)fileMap.get(chr);
/* 169 */       if (l == null) fileMap.put(chr, l = new ArrayList());
/* 170 */       l.add(f[i]);
/*     */     }
/*     */     
/* 173 */     new Iterator() {
/*     */       Iterator<List<File>> it1;
/*     */       
/* 176 */       public boolean hasNext() { return this.it1.hasNext(); }
/*     */       
/*     */       public void remove() {}
/*     */       
/* 180 */       public Iterator<Object[]> next() { final List<File> nxt = (List)this.it1.next();
/* 181 */         Collections.sort(nxt, EHHMultiChromosome.cc);
/* 182 */         new Iterator() {
/* 183 */           int i = 0;
/* 184 */           int end_prev = 0;
/* 185 */           Object[] res = new Object[3];
/*     */           
/* 187 */           public boolean hasNext() { return this.i < nxt.size(); }
/*     */           
/*     */           public Object[] next()
/*     */           {
/* 191 */             this.res[0] = nxt.get(this.i);
/* 192 */             this.res[2] = Integer.valueOf(this.end_prev);
/*     */             
/* 194 */             if (this.i < nxt.size() - 1) {
/*     */               try {
/* 196 */                 File f_i = (File)nxt.get(this.i + 1);
/* 197 */                 BufferedReader br = AberationFinder.getBufferedReader(f_i, "snp.txt");
/* 198 */                 br.readLine();
/* 199 */                 this.res[1] = Integer.valueOf(Integer.parseInt(br.readLine().split("\\s+")[2]));
/* 200 */                 br.close();
/*     */               } catch (Exception exc) {
/* 202 */                 exc.printStackTrace();
/*     */               }
/*     */             } else
/* 205 */               this.res[1] = Integer.valueOf(Integer.MAX_VALUE);
/*     */             try {
/* 207 */               this.end_prev = EHHMultiChromosome.getStartEnd((File)nxt.get(this.i))[1];
/*     */             } catch (Exception exc) {
/* 209 */               exc.printStackTrace();
/*     */             }
/* 211 */             this.i += 1;
/* 212 */             return this.res;
/*     */           }
/*     */           
/*     */           public void remove() {}
/*     */         };
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static int[] getStartEnd(File f) throws Exception
/*     */   {
/* 223 */     int[] res = new int[2];
/* 224 */     BufferedReader br = AberationFinder.getBufferedReader(f, "snp.txt");
/* 225 */     br.readLine();
/* 226 */     res[0] = Integer.parseInt(br.readLine().split("\\s+")[2]);
/*     */     
/* 228 */     String st1 = "";
/* 229 */     String st; while ((st = br.readLine()) != null) { String st;
/* 230 */       st1 = st;
/*     */     }
/*     */     
/* 233 */     res[1] = Integer.parseInt(st1.split("\\s+")[2]);
/* 234 */     br.close();
/* 235 */     return res;
/*     */   }
/*     */   
/*     */   public void run(int noCop) throws Exception {
/* 239 */     setNoCop(noCop);
/* 240 */     for (Iterator<Iterator<Object[]>> it = getIterator(this.user); it.hasNext();) {
/* 241 */       Iterator<Object[]> chr = (Iterator)it.next();
/*     */       try
/*     */       {
/* 244 */         RunEHH rehh = new RunEHH(this.user, this.normal, this.score, this.scoring, this.clazz);
/*     */         
/*     */ 
/* 247 */         boolean first = true;
/* 248 */         while (chr.hasNext()) {
/* 249 */           Object[] obj = (Object[])chr.next();
/* 250 */           File f = (File)obj[0];
/* 251 */           if (first) {
/* 252 */             first = false;
/* 253 */             rehh.setChrom(f.getName().split("_")[0]);
/*     */           }
/*     */           
/* 256 */           int nxtStart = ((Integer)obj[1]).intValue();
/* 257 */           rehh.run(f, nxtStart, noCop);
/*     */         }
/*     */         
/* 260 */         System.err.println("calculating scores!");
/* 261 */         rehh.calcScores();
/* 262 */         rehh.score.flush();
/*     */ 
/*     */       }
/*     */       catch (Exception exc)
/*     */       {
/* 267 */         exc.printStackTrace();
/*     */       }
/*     */     }
/* 270 */     this.score.close();
/*     */   }
/*     */   
/* 273 */   public void printNormal() throws Exception { File out = new File(this.user, "norm_" + this.noCop + "_0.txt");
/* 274 */     for (int i = 0; (out.exists()) && (out.length() > 0L); i++) {
/* 275 */       out = new File(this.user, "norm_" + this.noCop + "_" + i + ".txt");
/*     */     }
/* 277 */     System.err.println("normal output is " + out);
/* 278 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(out)));
/* 279 */     for (Iterator<Double> it = this.normal.keySet().iterator(); it.hasNext();) {
/* 280 */       Double key = (Double)it.next();
/* 281 */       TrainableNormal norm = (TrainableNormal)this.normal.get(key);
/* 282 */       if (norm.observations.size() > 0) norm.maximise(0.0D, 0.0D, 0.0D, 0.0D);
/* 283 */       pw.println(key + "\t" + norm.size() + "\t" + norm.toString());
/*     */     }
/* 285 */     pw.close();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/* 290 */       Options opt = Constants.OPTIONS;
/* 291 */       OptionBuilder.withLongOpt("hap");OptionBuilder.withDescription("hap");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 292 */       OptionBuilder.withLongOpt("baseDir");OptionBuilder.withDescription("baseDir");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 293 */       OptionBuilder.withLongOpt("overl");OptionBuilder.withDescription("overl");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 294 */       OptionBuilder.withLongOpt("cn");OptionBuilder.withDescription("cn");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 295 */       OptionBuilder.withLongOpt("input");OptionBuilder.withDescription("input");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 296 */       OptionBuilder.withLongOpt("graphAll");OptionBuilder.withDescription("graphAll");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 297 */       OptionBuilder.withLongOpt("extra");OptionBuilder.withDescription("extra");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 298 */       OptionBuilder.withLongOpt("chr");OptionBuilder.withDescription("chr");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/* 299 */       OptionBuilder.withLongOpt("start");OptionBuilder.withDescription("start");OptionBuilder.withValueSeparator(':');OptionBuilder.hasArgs();opt.addOption(OptionBuilder.create());
/*     */       
/* 301 */       Parser parser = new PosixParser();
/* 302 */       CommandLine params = parser.parse(opt, args, false);
/*     */       
/* 304 */       EHHFinder.thresh = 0;
/* 305 */       EHHFinder.lim = Integer.valueOf(Integer.MAX_VALUE);
/* 306 */       EHHFinder.thresh1 = 0.0D;
/* 307 */       File user = new File(System.getProperty("user.dir"));
/* 308 */       File baseDir = new File(user, params.getOptionValue("baseDir"));
/* 309 */       File input = null;
/* 310 */       if (params.hasOption("input")) {
/* 311 */         input = new File(user, params.getOptionValue("input"));
/*     */       }
/* 313 */       if (params.hasOption("hap")) {
/* 314 */         String[] hap = params.getOptionValues("hap");
/* 315 */         RunEHH rehh = 
/* 316 */           new RunEHH(user, read(new File(user, "norm.txt")), null, true, EHH.class);
/* 317 */         rehh.setChrom(params.getOptionValue("chr"));
/* 318 */         rehh.run(input, 
/* 319 */           Integer.parseInt(params.getOptionValue("start")), 
/*     */           
/* 321 */           hap[0], hap[1]);
/* 322 */         return;
/*     */       }
/* 324 */       overl = Boolean.parseBoolean(params.getOptionValue("overl", "false"));
/* 325 */       graphAll = Boolean.parseBoolean(params.getOptionValue("graphAll", "false"));
/*     */       
/* 327 */       EHHMultiChromosome rehh = new EHHMultiChromosome(user, baseDir, input, Integer.parseInt(params.getOptionValue("extra", "0")));
/* 328 */       String[] cn = params.getOptionValues("cn");
/*     */       
/* 330 */       for (int i = 0; i < cn.length; i++) {
/* 331 */         System.err.println("running with " + i + " cnv");
/* 332 */         rehh.run(Integer.parseInt(cn[i]));
/* 333 */         rehh.printNormal();
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/* 339 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/EHHMultiChromosome.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */