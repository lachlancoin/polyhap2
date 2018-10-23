/*     */ package lc1.dp.appl;
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
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.CGH.Locreader;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.collection.IlluminaRDataCollection;
/*     */ import lc1.dp.data.collection.MergedDataCollection;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.HaplotypeData;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.ensj.PedigreeDataCollection;
/*     */ import lc1.sequenced.Convert;
/*     */ import lc1.util.Constants;
/*     */ import org.apache.commons.cli.CommandLine;
/*     */ 
/*     */ public class DickFormat
/*     */ {
/*  36 */   public static CNVFrame cnvf = null;
/*     */   
/*     */   public static int rep;
/*     */   
/*     */   static String summaryFile;
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*     */     try
/*     */     {
/*  47 */       if (args.length == 0) {
/*  48 */         args = new String[] { "--paramFile", "param.txt", "--column", "1" };
/*     */       }
/*  50 */       String[] cols = Constants.getCols(args);
/*  51 */       for (int ij = 0; ij < cols.length; ij++) {
/*  52 */         CommandLine para = Constants.parse(args, Integer.valueOf(Integer.parseInt(cols[ij])));
/*     */         
/*  54 */         summaryFile = Constants.out + Constants.column();
/*  55 */         PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(Constants.getDirFile(), "log_out.txt" + Constants.column))));
/*  56 */         Constants.printOptions(pw, "\n");
/*  57 */         pw.close();
/*  58 */         String dirF = Constants.getDirFile();
/*  59 */         if (dirF.equals(".")) {
/*  60 */           dirF = System.getProperty("user.dir");
/*     */         }
/*  62 */         File dir1 = new File(dirF);
/*  63 */         File[] dir = 
/*  64 */           { dir1 };
/*  65 */         for (int i = 0; i < dir.length; i++) {
/*  66 */           if (Constants.type.equals("simulate")) {
/*  67 */             simulate(dir[i], null, (short)i);
/*     */           }
/*  69 */           else if (para == null) {
/*  70 */             String[] str = dir[i].list(new java.io.FilenameFilter()
/*     */             {
/*     */               public boolean accept(File dir, String name) {
/*  73 */                 return name.endsWith("_log.txt");
/*     */               }
/*     */               
/*  76 */             });
/*  77 */             Arrays.sort(str, new java.util.Comparator()
/*     */             {
/*     */               public int compare(String o1, String o2)
/*     */               {
/*  81 */                 String[] st1 = o1.split("_");
/*  82 */                 String[] st2 = o2.split("_");
/*  83 */                 for (int i = 0; i < 2; i++) {
/*  84 */                   Integer i1 = Integer.valueOf(Integer.parseInt(st1[i]));
/*  85 */                   int comp = i1.compareTo(Integer.valueOf(Integer.parseInt(st2[i])));
/*  86 */                   if (comp != 0) return comp;
/*     */                 }
/*  88 */                 return 0;
/*     */               }
/*     */             });
/*     */             
/*  92 */             for (int ik = 0; ik < str.length; ik++) {
/*  93 */               String st = str[ik];
/*  94 */               File f = new File(dir[i], st.substring(0, st.indexOf("_log.txt")));
/*  95 */               if (f.exists()) {
/*  96 */                 File p1 = new File(f, "raw.txt");
/*  97 */                 if ((p1.exists()) && (p1.length() > 0L)) {}
/*     */               } else {
/*  99 */                 f.mkdir();
/* 100 */                 File newLog = new File(f, "log.txt");
/*     */                 
/* 102 */                 copy(new File(dir[i], st), newLog);
/* 103 */                 String[] args1 = { "--paramFile", "log.txt", "--dir", f.getName() };
/* 104 */                 Constants.parse(args1);
/*     */                 
/*     */                 try
/*     */                 {
/* 108 */                   run(f, null);
/* 109 */                   if (Constants.plot() == 1) System.exit(0);
/*     */                 } catch (Exception exc) {
/* 111 */                   exc.printStackTrace();
/*     */                 }
/*     */               }
/*     */             }
/*     */           } else {
/* 116 */             run(dir[i], null);
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception exc) {
/* 121 */       exc.printStackTrace();
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
/*     */   private static void copy(File from, File to)
/*     */     throws Exception
/*     */   {
/* 177 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(to)));
/* 178 */     BufferedReader br = new BufferedReader(new FileReader(from));
/* 179 */     String st = "";
/* 180 */     while ((st = br.readLine()) != null) {
/* 181 */       pw.println(st);
/*     */     }
/* 183 */     br.close();
/* 184 */     pw.close();
/*     */   }
/*     */   
/*     */   private static HaplotypeData[] readFastPhaseOutput(File file)
/*     */     throws Exception
/*     */   {
/* 190 */     BufferedReader br = new BufferedReader(new FileReader(file));
/* 191 */     String st = "";
/* 192 */     List<HaplotypeData> res = new ArrayList();
/* 193 */     int index = 0;
/* 194 */     while ((st = br.readLine()) != null) {
/* 195 */       if (st.startsWith("#")) {
/* 196 */         String[] st1 = br.readLine().split("\\s+");
/* 197 */         HaplotypeData d = new HaplotypeData(index, 10);
/* 198 */         index++;
/* 199 */         for (int i = 0; i < st1.length; i++) {
/* 200 */           Boolean val = st1[i].equals("?") ? null : Boolean.valueOf(!st1[i].equals("0"));
/* 201 */           d.addPoint(i, val);
/*     */         }
/* 203 */         res.add(d);
/*     */       }
/*     */     }
/* 206 */     HaplotypeData[] data = (HaplotypeData[])res.toArray(new HaplotypeData[0]);
/* 207 */     return data;
/*     */   }
/*     */   
/*     */   public static void simulate(File dir, PrintWriter log, short index) throws Exception
/*     */   {
/* 212 */     File inp = 
/* 213 */       new File(dir, Constants.inputFile()[0]);
/* 214 */     SimpleDataCollection obj = 
/* 215 */       new SimpleDataCollection(inp, index, 2);
/*     */     
/* 217 */     IlluminaRDataCollection coll = new IlluminaRDataCollection(obj);
/* 218 */     File out = new File("data.txt");
/* 219 */     new File("data.txt.loc").delete();
/* 220 */     coll.printHapMapFormat(out, "1"); }
/*     */   
/* 222 */   public static boolean simulateMissing = false;
/*     */   
/*     */   public static DataCollection read(File dir) throws Exception {
/* 225 */     double cn_ratio = Constants.cn_ratio();
/* 226 */     String[] format = Constants.format();
/* 227 */     String[] inpF = Constants.inputFile();
/* 228 */     DataCollection[] res = new DataCollection[format.length];
/* 229 */     int[] no_copies = Constants.noCopies();
/*     */     
/* 231 */     File[] inp = new File[inpF.length];
/* 232 */     for (int i = 0; i < inp.length; i++) {
/* 233 */       inp[i] = new File(dir, inpF[i]);
/*     */     }
/* 235 */     for (int i = 0; i < format.length; i++) { EmissionState emst;
/* 236 */       if (format[i].startsWith("geno"))
/*     */       {
/* 238 */         res[i] = new SimpleDataCollection(inp[i], (short)i);
/*     */         
/* 240 */         emst = (EmissionState)res[0].dataLvalues().next();
/*     */ 
/*     */       }
/* 243 */       else if (format[i].startsWith("marchini")) { res[i] = DataCollection.getMarchiniFile(new File(Constants.getDirFile()), Constants.index(), Constants.readPedigree(), true);
/* 244 */       } else if (format[i].startsWith("illumina"))
/*     */       {
/* 246 */         res[i] = new IlluminaRDataCollection(inp[i], (short)i, no_copies[i]);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 251 */       else if (format[i].startsWith("seq"))
/*     */       {
/* 253 */         Location[] ni = Convert.getDeletions(inp[i], 
/* 254 */           Integer.valueOf(Integer.parseInt(Constants.chrom0())), Constants.mid());
/* 255 */         res[i] = Convert.getLikelihoodDataCollection(ni);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 260 */         System.err.println("format seq");
/*     */       }
/* 262 */       else if (format[i].startsWith("cghall"))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */         res[i] = new IlluminaRDataCollection(inp[i], (short)i, no_copies[i]);
/*     */       }
/*     */       else
/*     */       {
/* 272 */         throw new RuntimeException("!!" + format[i]); }
/* 273 */       res[i].dropIndiv(Constants.toDel());
/* 274 */       int sumInd = 0;
/* 275 */       for (int n = 0; n < inpF.length; n++) {
/* 276 */         sumInd += Constants.maxIndiv(n);
/*     */       }
/* 278 */       if (res[i] != null) { res[i].trim(sumInd);
/*     */       }
/*     */     }
/* 281 */     if (Constants.reverse()) {
/* 282 */       for (int i = 0; i < res.length; i++) {
/* 283 */         res[i].reverse();
/*     */       }
/*     */     }
/* 286 */     int[] indexToRestrict = Constants.indexToRestrict();
/* 287 */     Locreader[] locs = new Locreader[indexToRestrict.length];
/* 288 */     for (int i = 0; i < indexToRestrict.length; i++) {
/* 289 */       locs[i] = res[indexToRestrict[i]].getMergedDeletions(true);
/*     */     }
/* 291 */     for (int i = 0; i < res.length; i++) {
/* 292 */       for (int j = 0; j < locs.length; j++)
/* 293 */         res[i].fix(locs[j], -50);
/*     */     }
/*     */     DataCollection result;
/*     */     DataCollection result;
/* 297 */     if (res.length == 1) {
/* 298 */       if (simulateMissing) {
/* 299 */         boolean rand = false;
/*     */         
/*     */ 
/* 302 */         DataCollection dc = (DataCollection)res[0].clone();
/* 303 */         DataCollection[] res0 = rand ? dc.splitRandom() : dc.splitOnSize();
/* 304 */         res0[1].dropRandom(0.8D, false);
/*     */         
/* 306 */         DataCollection result = MergedDataCollection.getMergedDataCollection(res0, cn_ratio);
/* 307 */         for (int i = 0; i < result.loc.size(); i++) {
/* 308 */           if (((Integer)result.loc.get(i)).intValue() != ((Integer)dc.loc.get(i)).intValue())
/* 309 */             throw new RuntimeException("!!");
/*     */         }
/* 311 */         for (Iterator<PIGData> it = res[0].iterator(); it.hasNext();) {
/* 312 */           PIGData nxt = (PIGData)it.next();
/* 313 */           result.data.put(nxt.getName(), nxt);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 331 */       result = res[0];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 351 */       result = MergedDataCollection.getMergedDataCollection(res, cn_ratio);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 356 */     return result;
/*     */   }
/*     */   
/* 359 */   public static boolean run(File dir, PrintWriter log) throws Exception { File summ = new File(dir, summaryFile);
/* 360 */     DataCollection obj = read(dir);
/* 361 */     if (Constants.plot() > 0) {
/* 362 */       cnvf = new CNVFrame();
/*     */     }
/*     */     
/* 365 */     File pedigree = new File(dir, "ped.txt");
/* 366 */     if (!pedigree.exists()) {
/* 367 */       pedigree = new File(dir.getParent(), "ceu_ped.txt");
/*     */     }
/* 369 */     if ((Constants.readPedigree()) && (obj.ped == null)) {
/* 370 */       if (Constants.inputFile()[0].endsWith(".lhood")) {
/* 371 */         File ped = new File(new File(Constants.getDirFile()), "ped.txt");
/* 372 */         if (ped.exists()) {
/* 373 */           PedigreeDataCollection pedData = new PedigreeDataCollection(ped);
/* 374 */           obj.setPedigree(pedData);
/*     */         }
/* 376 */       } else if (pedigree.exists()) {
/* 377 */         PedigreeDataCollection pedData = new PedigreeDataCollection(pedigree);
/* 378 */         obj.setPedigree(pedData);
/*     */       }
/*     */       else
/*     */       {
/* 382 */         if (((PIGData)((SimpleDataCollection)obj).data.values().iterator().next()).noCopies() == 1) {
/* 383 */           ((SimpleDataCollection)obj).transform(new double[] { 0.0D, 1.0D }, new ArrayList((Collection)obj.data.keySet()), ".");
/* 384 */           ((SimpleDataCollection)obj).rename();
/*     */         }
/* 386 */         ((SimpleDataCollection)obj).makeTrioData();
/*     */         
/* 388 */         if (!Constants.xchrom()) {
/* 389 */           ((SimpleDataCollection)obj).transform(new double[] { 0.0D, 1.0D }, new ArrayList((Collection)obj.ped.mother.keySet()), ".");
/*     */         }
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 409 */     RunFastPhase.run(obj, summ, new File(dir, "clusters.txt" + Constants.column()), summ.getParentFile());
/*     */     
/* 411 */     if (log != null) {
/* 412 */       log.println("directory \t" + dir.getAbsolutePath());
/* 413 */       log.flush();
/*     */     }
/* 415 */     return true;
/*     */   }
/*     */   
/*     */   public static String getString(String loc, List dat, List<String> ids) {
/* 419 */     StringBuffer sb = new StringBuffer();
/* 420 */     int i = 0;
/* 421 */     while ((i < ids.size()) && (!((String)ids.get(i)).equals(loc))) {
/* 422 */       i++;
/*     */     }
/* 424 */     if (i == ids.size()) throw new RuntimeException("!!");
/* 425 */     for (int j = 0; j < dat.size(); j++) {
/* 426 */       Object obj = ((SimpleScorableObject)dat.get(j)).getElement(i);
/* 427 */       if ((obj instanceof Comparable[])) {
/* 428 */         Comparable[] comp = (Comparable[])obj;
/* 429 */         for (int k = 0; k < comp.length; k++)
/* 430 */           sb.append(comp[k]);
/*     */       } else {
/* 432 */         sb.append(obj == null ? "null" : obj.toString()); }
/* 433 */       sb.append(" ");
/*     */     }
/* 435 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static Integer[][] calculateAverage(File summary, PrintWriter log1) throws Exception {
/* 439 */     BufferedReader br = new BufferedReader(new FileReader(summary));
/* 440 */     String st = "";
/* 441 */     Integer[] ff = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 442 */     Integer[] sampl = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 443 */     Integer[] count = { Integer.valueOf(0), Integer.valueOf(0) };
/* 444 */     List<String> st_sample = new ArrayList();
/* 445 */     List<String> st_ff = new ArrayList();
/* 446 */     while ((st = br.readLine()) != null) {
/* 447 */       if (st.startsWith("comparing with fastphase")) {
/* 448 */         br.readLine();
/* 449 */         st = br.readLine();
/* 450 */         if (st != null) {
/* 451 */           st_ff.add(st); int 
/* 452 */             tmp164_163 = 0; Integer[] tmp164_161 = count;tmp164_161[tmp164_163] = Integer.valueOf(tmp164_161[tmp164_163].intValue() + 1);
/*     */         }
/*     */       }
/* 455 */       if (st.startsWith("comparing with sampling avg")) {
/* 456 */         br.readLine();
/* 457 */         st = br.readLine();
/* 458 */         if (st != null) {
/* 459 */           st_sample.add(st); int 
/* 460 */             tmp211_210 = 1; Integer[] tmp211_208 = count;tmp211_208[tmp211_210] = Integer.valueOf(tmp211_208[tmp211_210].intValue() + 1);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 465 */     for (int i = 0; i < st_sample.size(); i++) {
/* 466 */       add(ff, (String)st_ff.get(i));
/* 467 */       add(sampl, (String)st_sample.get(i));
/*     */     }
/* 469 */     log1.println(summary.getName());
/* 470 */     log1.println("totals ff are" + Arrays.asList(ff));
/* 471 */     log1.println("totals sampling are" + Arrays.asList(sampl));
/* 472 */     log1.flush();
/* 473 */     br.close();
/* 474 */     return new Integer[][] { ff, sampl, count };
/*     */   }
/*     */   
/* 477 */   private static void add(Integer[] ff, String st) { if (st == null) { return;
/*     */     }
/* 479 */     String[] str = st.split("\\s+");
/* 480 */     int[] ind = { 1, 3, 5, 7 };
/* 481 */     for (int i = 0; i < ind.length; i++) {
/* 482 */       int tmp43_41 = i;ff[tmp43_41] = Integer.valueOf(ff[tmp43_41].intValue() + Integer.parseInt(str[ind[i]]));
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
/* 497 */     File dir1 = new File(System.getProperties().getProperty("user.dir"));
/* 498 */     File[] dir = dir1.listFiles(new FileFilter() {
/*     */       public boolean accept(File pathname) {
/* 500 */         return pathname.isDirectory();
/*     */       }
/* 502 */     });
/* 503 */     File logfile = new File(dir1, "log.txt");
/*     */     
/* 505 */     PrintWriter log = new PrintWriter(new BufferedWriter(new FileWriter(logfile, false)));
/* 506 */     for (int j = 0; j < 1; j++) {
/* 507 */       Logger.getAnonymousLogger().info("doing round " + j);
/* 508 */       log.println("round " + j);
/* 509 */       log.flush();
/* 510 */       for (int i1 = 0; i1 < dir.length; i1++) {
/* 511 */         File f = dir[i1];
/* 512 */         if (!run(f, log)) {
/* 513 */           System.err.println("already done " + f.getName());
/*     */         }
/*     */       }
/*     */     }
/* 517 */     log.close();
/*     */   }
/*     */   
/*     */   static void score() throws Exception {
/* 521 */     File dir1 = new File(System.getProperties().getProperty("user.dir"));
/* 522 */     File logfile1 = new File(dir1, "log_summary.txt");
/* 523 */     PrintWriter log1 = new PrintWriter(new BufferedWriter(new FileWriter(logfile1, true)));
/* 524 */     File[] dir = dir1.listFiles(new FileFilter() {
/*     */       public boolean accept(File pathname) {
/* 526 */         return pathname.isDirectory();
/*     */       }
/* 528 */     });
/* 529 */     log1.println(RunFastPhase.cal.getTime().toString().toUpperCase() + "-----------------");
/* 530 */     Integer[] avg_ff = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 531 */     Integer[] avg_sampl = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/* 532 */     Integer[][] counts = new Integer[2][dir.length];
/* 533 */     for (int i1 = 0; i1 < dir.length; i1++) {
/* 534 */       counts[0][i1] = Integer.valueOf(0);
/* 535 */       counts[1][i1] = Integer.valueOf(0);
/*     */       
/* 537 */       File f = dir[i1];
/* 538 */       File summ = new File(f, summaryFile);
/* 539 */       if (summ.exists()) {
/* 540 */         Integer[][] res = calculateAverage(summ, log1); int 
/*     */         
/* 542 */           tmp249_247 = i1; Integer[] tmp249_246 = counts[0];tmp249_246[tmp249_247] = Integer.valueOf(tmp249_246[tmp249_247].intValue() + res[2][0].intValue()); int 
/* 543 */           tmp274_272 = i1; Integer[] tmp274_271 = counts[1];tmp274_271[tmp274_272] = Integer.valueOf(tmp274_271[tmp274_272].intValue() + res[2][1].intValue());
/* 544 */         add(res, new Integer[][] { avg_ff, avg_sampl });
/*     */       }
/*     */     }
/*     */     
/* 548 */     log1.println("total ff is " + Arrays.asList(avg_ff));
/* 549 */     log1.println("total sampl is " + Arrays.asList(avg_sampl));
/* 550 */     log1.println("counts are " + Arrays.asList(counts[0]));
/* 551 */     log1.println("counts are " + Arrays.asList(counts[1]));
/* 552 */     log1.flush();
/* 553 */     log1.close();
/*     */   }
/*     */   
/*     */   private static void add(Integer[][] is, Integer[][] is2) {
/* 557 */     for (int i = 0; i < is2.length; i++) {
/* 558 */       for (int j = 0; j < is[i].length; j++) {
/* 559 */         int tmp14_13 = j; Integer[] tmp14_12 = is2[i];tmp14_12[tmp14_13] = Integer.valueOf(tmp14_12[tmp14_13].intValue() + is[i][j].intValue());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/appl/DickFormat.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */