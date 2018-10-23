/*     */ package lc1.dp.genotype.io;
/*     */ 
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
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.genotype.io.scorable.DataCollection;
/*     */ import lc1.dp.genotype.io.scorable.HaplotypeData;
/*     */ import lc1.dp.genotype.io.scorable.SimpleScorableObject;
/*     */ import org.apache.commons.cli.CommandLine;
/*     */ import org.apache.commons.cli.OptionBuilder;
/*     */ import org.apache.commons.cli.Options;
/*     */ import org.apache.commons.cli.Parser;
/*     */ import org.apache.commons.cli.PosixParser;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DickFormat
/*     */ {
/*  34 */   static final Options OPTIONS = new Options() {};
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */   static boolean search = true;
/*  44 */   static boolean all = false;
/*  45 */   static boolean score = !search;
/*  46 */   public static String summaryFile = "summary86.txt";
/*  47 */   static boolean phase = true;
/*  48 */   static boolean simulate = false;
/*  49 */   static int rep = (search) && (all) ? 100 : 1;
/*     */   
/*     */   public static void main(String[] args) {
/*  52 */     Parser parser = new PosixParser();
/*     */     try
/*     */     {
/*  55 */       CommandLine params = parser.parse(OPTIONS, args);
/*  56 */       summaryFile = params.getOptionValue("out", summaryFile);
/*  57 */       Constants.parse(args);
/*     */       
/*  59 */       File dir1 = new File(params.getOptionValue("dir", System.getProperties().getProperty("user.dir")));
/*  60 */       for (int i11 = 0; i11 < rep; i11++) {
/*  61 */         File[] dir = 
/*     */         
/*     */ 
/*     */ 
/*  65 */           {all ? dir1.listFiles(new FileFilter() {
/*     */             public boolean accept(File pathname) {
/*  63 */               return pathname.isDirectory();
/*     */             }
/*  65 */           }) : dir1 };
/*  66 */         if (score) {
/*  67 */           score();
/*     */         }
/*     */         else {
/*  70 */           for (int i = 0; i < dir.length; i++) {
/*  71 */             if (search) {
/*  72 */               run(dir[i], null);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/*  79 */       exc.printStackTrace();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static HaplotypeData[] readFastPhaseOutput(File file)
/*     */     throws Exception
/*     */   {
/* 135 */     BufferedReader br = new BufferedReader(new FileReader(file));
/* 136 */     String st = "";
/* 137 */     List<HaplotypeData> res = new ArrayList();
/* 138 */     int index = 0;
/* 139 */     while ((st = br.readLine()) != null) {
/* 140 */       if (st.startsWith("#")) {
/* 141 */         String[] st1 = br.readLine().split("\\s+");
/* 142 */         HaplotypeData d = new HaplotypeData(index, 10);
/* 143 */         index++;
/* 144 */         for (int i = 0; i < st1.length; i++) {
/* 145 */           Boolean val = st1[i].equals("?") ? null : Boolean.valueOf(!st1[i].equals("0"));
/* 146 */           d.addPoint(val);
/*     */         }
/* 148 */         res.add(d);
/*     */       }
/*     */     }
/* 151 */     HaplotypeData[] data = (HaplotypeData[])res.toArray(new HaplotypeData[0]);
/* 152 */     return data;
/*     */   }
/*     */   
/*     */   public static boolean run(File dir, PrintWriter log)
/*     */     throws Exception
/*     */   {
/* 158 */     File summ = new File(dir, summaryFile);
/* 159 */     if (phase) Constants.setPhasingOptions();
/* 160 */     if (simulate) { Constants.setSimulationParams();
/*     */     }
/*     */     
/*     */ 
/* 164 */     File inp = new File(dir, "all_x.csv.txt");
/*     */     
/* 166 */     if (!inp.exists()) {
/* 167 */       if (phase) { inp = new File(dir, dir.getName() + ".dick.txt");
/*     */       } else {
/* 169 */         Logger.global.warning("input did not exist" + inp);
/* 170 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 174 */     DataCollection obj = 
/* 175 */       inp.getName().endsWith(".dick.txt") ? 
/* 176 */       DataCollection.readDickFormat(inp) : 
/* 177 */       DataCollection.readHapMap(inp, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */     obj.trim(Constants.maxIndiv());
/* 185 */     obj.majorAllelle();
/* 186 */     obj.setAsBoolean();
/*     */     
/* 188 */     if (Constants.noCopies()[0] != 1.0D) { obj.transform(Constants.noCopies());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 193 */     PrintWriter summary = new PrintWriter(new BufferedWriter(new FileWriter(summ, all)));
/*     */     
/*     */ 
/*     */ 
/* 197 */     RunFastPhase.run(obj, summary, new File(dir, "clusters.txt"), summ.getParentFile());
/* 198 */     summary.close();
/* 199 */     if (log != null) {
/* 200 */       log.println("directory \t" + dir.getAbsolutePath());
/* 201 */       log.flush();
/*     */     }
/* 203 */     return true;
/*     */   }
/*     */   
/*     */   public static String getString(String loc, List dat, List<String> ids) {
/* 207 */     StringBuffer sb = new StringBuffer();
/* 208 */     int i = 0;
/* 209 */     while ((i < ids.size()) && (!((String)ids.get(i)).equals(loc))) {
/* 210 */       i++;
/*     */     }
/* 212 */     if (i == ids.size()) throw new RuntimeException("!!");
/* 213 */     for (int j = 0; j < dat.size(); j++) {
/* 214 */       Object obj = ((SimpleScorableObject)dat.get(j)).getElement(i);
/* 215 */       if ((obj instanceof Comparable[])) {
/* 216 */         Comparable[] comp = (Comparable[])obj;
/* 217 */         for (int k = 0; k < comp.length; k++)
/* 218 */           sb.append(comp[k]);
/*     */       } else {
/* 220 */         sb.append(obj == null ? "null" : obj.toString()); }
/* 221 */       sb.append(" ");
/*     */     }
/* 223 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static Integer[][] calculateAverage(File summary, PrintWriter log1) throws Exception {
/* 227 */     BufferedReader br = new BufferedReader(new FileReader(summary));
/* 228 */     String st = "";
/* 229 */     Integer[] ff = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 230 */     Integer[] sampl = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 231 */     Integer[] count = { Integer.valueOf(0), Integer.valueOf(0) };
/* 232 */     List<String> st_sample = new ArrayList();
/* 233 */     List<String> st_ff = new ArrayList();
/* 234 */     while ((st = br.readLine()) != null) {
/* 235 */       if (st.startsWith("comparing with fastphase")) {
/* 236 */         br.readLine();
/* 237 */         st = br.readLine();
/* 238 */         if (st != null) {
/* 239 */           st_ff.add(st); int 
/* 240 */             tmp164_163 = 0; Integer[] tmp164_161 = count;tmp164_161[tmp164_163] = Integer.valueOf(tmp164_161[tmp164_163].intValue() + 1);
/*     */         }
/*     */       }
/* 243 */       if (st.startsWith("comparing with sampling avg")) {
/* 244 */         br.readLine();
/* 245 */         st = br.readLine();
/* 246 */         if (st != null) {
/* 247 */           st_sample.add(st); int 
/* 248 */             tmp211_210 = 1; Integer[] tmp211_208 = count;tmp211_208[tmp211_210] = Integer.valueOf(tmp211_208[tmp211_210].intValue() + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 253 */     for (int i = 0; i < st_sample.size(); i++) {
/* 254 */       add(ff, (String)st_ff.get(i));
/* 255 */       add(sampl, (String)st_sample.get(i));
/*     */     }
/* 257 */     log1.println(summary.getName());
/* 258 */     log1.println("totals ff are" + Arrays.asList(ff));
/* 259 */     log1.println("totals sampling are" + Arrays.asList(sampl));
/* 260 */     log1.flush();
/* 261 */     br.close();
/* 262 */     return new Integer[][] { ff, sampl, count };
/*     */   }
/*     */   
/* 265 */   private static void add(Integer[] ff, String st) { if (st == null) { return;
/*     */     }
/* 267 */     String[] str = st.split("\\s+");
/* 268 */     int[] ind = { 1, 3, 5, 7 };
/* 269 */     for (int i = 0; i < ind.length; i++) {
/* 270 */       int tmp42_40 = i;ff[tmp42_40] = Integer.valueOf(ff[tmp42_40].intValue() + Integer.parseInt(str[ind[i]]));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void main1(String[] args) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static void searchAll()
/*     */     throws Exception
/*     */   {
/* 285 */     File dir1 = new File(System.getProperties().getProperty("user.dir"));
/* 286 */     File[] dir = dir1.listFiles(new FileFilter() {
/*     */       public boolean accept(File pathname) {
/* 288 */         return pathname.isDirectory();
/*     */       }
/* 290 */     });
/* 291 */     File logfile = new File(dir1, "log.txt");
/*     */     
/* 293 */     PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logfile, false)));
/* 294 */     for (int j = 0; j < 1; j++) {
/* 295 */       Logger.getAnonymousLogger().info("doing round " + j);
/* 296 */       log.println("round " + j);
/* 297 */       log.flush();
/* 298 */       for (int i1 = 0; i1 < dir.length; i1++) {
/* 299 */         File f = dir[i1];
/* 300 */         if (!run(f, log)) {
/* 301 */           System.err.println("already done " + f.getName());
/*     */         }
/*     */       }
/*     */     }
/* 305 */     log.close();
/*     */   }
/*     */   
/*     */   static void score() throws Exception {
/* 309 */     File dir1 = new File(System.getProperties().getProperty("user.dir"));
/* 310 */     File logfile1 = new File(dir1, "log_summary.txt");
/* 311 */     PrintWriter log1 = new PrintWriter(new BufferedWriter(new FileWriter(logfile1, true)));
/* 312 */     File[] dir = dir1.listFiles(new FileFilter() {
/*     */       public boolean accept(File pathname) {
/* 314 */         return pathname.isDirectory();
/*     */       }
/* 316 */     });
/* 317 */     log1.println(RunFastPhase.cal.getTime().toString().toUpperCase() + "-----------------");
/* 318 */     Integer[] avg_ff = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 319 */     Integer[] avg_sampl = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 320 */     Integer[][] counts = new Integer[2][dir.length];
/* 321 */     for (int i1 = 0; i1 < dir.length; i1++) {
/* 322 */       counts[0][i1] = Integer.valueOf(0);
/* 323 */       counts[1][i1] = Integer.valueOf(0);
/*     */       
/* 325 */       File f = dir[i1];
/* 326 */       File summ = new File(f, summaryFile);
/* 327 */       if (summ.exists()) {
/* 328 */         Integer[][] res = calculateAverage(summ, log1); int 
/*     */         
/* 330 */           tmp249_247 = i1; Integer[] tmp249_246 = counts[0];tmp249_246[tmp249_247] = Integer.valueOf(tmp249_246[tmp249_247].intValue() + res[2][0].intValue()); int 
/* 331 */           tmp274_272 = i1; Integer[] tmp274_271 = counts[1];tmp274_271[tmp274_272] = Integer.valueOf(tmp274_271[tmp274_272].intValue() + res[2][1].intValue());
/* 332 */         add(res, new Integer[][] { avg_ff, avg_sampl });
/*     */       }
/*     */     }
/*     */     
/* 336 */     log1.println("total ff is " + Arrays.asList(avg_ff));
/* 337 */     log1.println("total sampl is " + Arrays.asList(avg_sampl));
/* 338 */     log1.println("counts are " + Arrays.asList(counts[0]));
/* 339 */     log1.println("counts are " + Arrays.asList(counts[1]));
/* 340 */     log1.flush();
/* 341 */     log1.close();
/*     */   }
/*     */   
/*     */   private static void add(Integer[][] is, Integer[][] is2) {
/* 345 */     for (int i = 0; i < is2.length; i++) {
/* 346 */       for (int j = 0; j < is[i].length; j++) {
/* 347 */         int tmp14_13 = j; Integer[] tmp14_12 = is2[i];tmp14_12[tmp14_13] = Integer.valueOf(tmp14_12[tmp14_13].intValue() + is[i][j].intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/DickFormat.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */