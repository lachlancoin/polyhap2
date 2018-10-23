/*     */ package lc1.ensj;
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
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.genotype.io.scorable.CompoundScorableObject;
/*     */ import lc1.dp.genotype.io.scorable.DataCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HapMartData
/*     */ {
/*     */   static File logFile;
/*  33 */   static Integer st = Integer.valueOf(2837180);
/*  34 */   static Integer en = Integer.valueOf(154532741);
/*  35 */   static Integer excl = Integer.valueOf(56264049);
/*     */   
/*     */ 
/*  38 */   static int noSnps = 3000;
/*     */   
/*  40 */   static double thresh_d = 0.0D;
/*  41 */   static double thresh_c = 0.0D;
/*     */   
/*     */ 
/*     */   public static void main(String[] args)
/*     */   {
/*  46 */     checkMother("phased.txt");
/*  47 */     checkMother("all_x.csv.txt");
/*     */   }
/*     */   
/*  50 */   static Comparator<File> fileComp = new Comparator()
/*     */   {
/*     */     public int compare(File o1, File o2) {
/*  53 */       String[] st1 = o1.getName().split("_");
/*  54 */       String[] st2 = o2.getName().split("_");
/*     */       try {
/*  56 */         Integer l1 = Integer.valueOf(Integer.parseInt(st1[0]));
/*  57 */         Integer l2 = Integer.valueOf(Integer.parseInt(st2[0]));
/*  58 */         return l1.compareTo(l2);
/*     */       }
/*     */       catch (Exception exc) {
/*  61 */         Integer l1 = Integer.valueOf(Integer.parseInt(st1[2]));
/*  62 */         Integer l2 = Integer.valueOf(Integer.parseInt(st2[2]));
/*  63 */         return l1.compareTo(l2);
/*     */       }
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */   public static void split()
/*     */   {
/*  72 */     File base = new File(System.getProperties().getProperty("user.dir"));
/*  73 */     System.err.println(base.getAbsolutePath());
/*     */     try {
/*  75 */       String[] pop = { "YRI" };
/*  76 */       List<int[]> startEnd = null;
/*  77 */       for (int i = 0; i < pop.length; i++) {
/*  78 */         HapMartData data = new HapMartData(base, pop[i], null, st, en);
/*  79 */         if (i == 0) { startEnd = data.split(3000, 10);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*  84 */         for (int ik = 0; ik < startEnd.size(); ik++) {
/*  85 */           int[] s_i = (int[])startEnd.get(ik);
/*     */           
/*  87 */           File f1 = new File(base, s_i[0] + "_" + s_i[1]);
/*  88 */           if (!f1.exists()) f1.mkdir();
/*  89 */           File f3 = new File(f1, pop[i] + "_x.csv.txt");
/*     */           
/*  91 */           PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f3)));
/*  92 */           data.dat.printHapMapFormat(f3, s_i);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/*  97 */       exc.printStackTrace();
/*     */     } }
/*     */   
/* 100 */   static boolean sortByScore = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final DataCollection dat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void checkMother(String str)
/*     */   {
/* 151 */     throw new Error("Unresolved compilation problems: \n\tCannot cast from Comparable to Comparable[]\n\tCannot cast from Comparable to Comparable[]\n\tCannot cast from Comparable to Comparable[]\n\tCannot cast from Comparable to Comparable[]\n");
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
/*     */   static class DoubleString
/*     */     implements Comparable
/*     */   {
/*     */     double d;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     String st;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean b;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     DoubleString(double d, boolean b, String st)
/*     */     {
/* 218 */       this.d = d;this.st = st;this.b = b;
/*     */     }
/*     */     
/* 221 */     public int compareTo(Object o) { DoubleString d1 = (DoubleString)o;
/* 222 */       if (d1.d == this.d) return 0;
/* 223 */       if (this.d < d1.d) return 1;
/* 224 */       return -1;
/*     */     }
/*     */   }
/*     */   
/*     */   private static Map<Integer, String> fillLocationsPoly(File poly, int thresh, double thresh1) throws Exception {
/* 229 */     BufferedReader br = new BufferedReader(new FileReader(poly));
/* 230 */     Map<Integer, String> m = new TreeMap();
/* 231 */     br.readLine();
/* 232 */     String st = "";
/* 233 */     while ((st = br.readLine()) != null) {
/* 234 */       String[] str = st.split("\t");
/* 235 */       String[] str1 = str[2].substring(1, str[2].length() - 1).split(",");
/* 236 */       double max_sc = Double.parseDouble(str1[(str1.length - 1)].trim());
/* 237 */       int loc = Integer.parseInt(str[0].split("=")[0]);
/* 238 */       String rd_id = str[1];
/* 239 */       char ch = rd_id.charAt(0);
/* 240 */       if ((ch != '+') && (ch != '-')) throw new RuntimeException("!!");
/* 241 */       if ((str1.length >= thresh) && (max_sc >= thresh1)) {
/* 242 */         m.put(Integer.valueOf(loc), rd_id);
/*     */       }
/*     */     }
/* 245 */     return m;
/*     */   }
/*     */   
/*     */   private static Map<Integer, List<String>> read(File file)
/*     */     throws Exception
/*     */   {
/* 251 */     BufferedReader br = new BufferedReader(new FileReader(file));
/* 252 */     br.readLine();
/* 253 */     String st = "";
/* 254 */     Map<Integer, List<String>> res = new TreeMap();
/* 255 */     while ((st = br.readLine()) != null) {
/* 256 */       String[] str = st.split("\t");
/* 257 */       String name = str[1];
/* 258 */       List<String> list = new ArrayList((Collection)Arrays.asList(str[2].substring(1, str[2].length() - 1).split(",")));
/* 259 */       for (int i = list.size() - 1; i >= 0; i--) {
/* 260 */         list.set(i, ((String)list.get(i)).trim());
/* 261 */         double val = Double.parseDouble(((String)list.get(i)).split("=")[1]);
/* 262 */         if (val < thresh_d) list.remove(i);
/*     */       }
/* 264 */       if (list.size() > thresh_c) {
/* 265 */         res.put(Integer.valueOf(Integer.parseInt(str[0].split("=")[0])), list);
/*     */       }
/*     */     }
/* 268 */     return res;
/*     */   }
/*     */   
/*     */   public static void convertToDickFormat()
/*     */   {
/*     */     try
/*     */     {
/* 275 */       String[] pop = { "CEU" };
/* 276 */       File base = new File(System.getProperties().getProperty("user.dir"));
/* 277 */       System.err.println(base.getAbsolutePath());
/* 278 */       File[] f = base.listFiles(new FileFilter() {
/*     */         public boolean accept(File pathname) {
/* 280 */           return pathname.isDirectory();
/*     */         }
/*     */         
/* 283 */       });
/* 284 */       Arrays.sort(f, fileComp);
/* 285 */       for (int i = 0; 
/*     */           
/* 287 */           i < f.length; 
/* 288 */           i++) {
/* 289 */         System.err.println(f[i].getName());
/* 290 */         HapMartData data = new HapMartData(f[i], pop[0], null, st, en);
/* 291 */         for (int j = 1; j < pop.length; j++) {
/* 292 */           HapMartData data1 = new HapMartData(data, new HapMartData(f[i], pop[j], null, st, en));
/* 293 */           data = data1;
/*     */         }
/* 295 */         File f3 = new File(f[i], "all_x.csv.txt");
/* 296 */         data.dat.collapse();
/* 297 */         data.dat.printHapMapFormat(f3, null);
/*     */         
/* 299 */         System.err.println(data.dat.get(0).length());
/* 300 */         boolean print = false;
/* 301 */         if (print) {
/* 302 */           try { logFile = new File(f[i], "logfile.txt");
/* 303 */             PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 318 */             File out = new File(f[i], f[i].getName() + ".in");
/*     */             
/*     */ 
/* 321 */             data.dat.writeFastphase(out);
/*     */           }
/*     */           catch (Exception exc)
/*     */           {
/* 325 */             exc.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (Exception exc) {
/* 330 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private HapMartData(HapMartData data1, HapMartData data2) throws Exception {
/* 335 */     this.sex.putAll(data1.sex);this.sex.putAll(data2.sex);
/* 336 */     this.mother.putAll(data1.mother);this.mother.putAll(data2.mother);
/* 337 */     this.father.putAll(data1.father);this.father.putAll(data2.father);
/* 338 */     this.males.addAll(data1.males);this.females.addAll(data1.females);
/* 339 */     this.males.addAll(data2.males);this.females.addAll(data2.females);
/*     */     
/* 341 */     this.dat = new DataCollection(data1.dat, data2.dat);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Double calculateAverageR(Double[] maf, Double[] pair_maf)
/*     */   {
/* 349 */     double sum = 0.0D;
/* 350 */     int cnt = 0;
/* 351 */     for (int i = 1; i < maf.length; i++) {
/* 352 */       if ((maf[i] != null) && (maf[(i - 1)] != null) && (pair_maf[i] != null) && (maf[i].doubleValue() != 0.0D) && (maf[(i - 1)].doubleValue() != 0.0D)) {
/* 353 */         double r = Math.pow(pair_maf[i].doubleValue() - maf[i].doubleValue() * maf[(i - 1)].doubleValue(), 2.0D) / (maf[i].doubleValue() * maf[(i - 1)].doubleValue() * (1.0D - maf[i].doubleValue()) * (1.0D - maf[(i - 1)].doubleValue()));
/*     */         
/* 355 */         cnt++;
/* 356 */         sum += r;
/*     */       }
/*     */     }
/* 359 */     return Double.valueOf(sum / cnt);
/*     */   }
/*     */   
/*     */   private static int getMinIndex(Double[] maf) {
/* 363 */     int min_index = 0;
/* 364 */     for (int i = 0; i < maf.length; i++) {
/* 365 */       if ((maf[i] != null) && (maf[i].doubleValue() != 0.0D) && 
/* 366 */         (maf[i].doubleValue() < maf[min_index].doubleValue())) {
/* 367 */         min_index = i;
/*     */       }
/*     */     }
/* 370 */     return min_index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double[] getFracGaps()
/*     */   {
/* 377 */     double cnt = 0.0D;
/* 378 */     Set<Integer> pos = new HashSet();
/* 379 */     for (int i = 0; i < this.dat.size(); i++) {
/* 380 */       for (int j = 0; j < this.dat.get(i).length(); j++) {
/* 381 */         Character ch = (Character)this.dat.get(i).getElement(j);
/* 382 */         if (ch.charValue() == 'N') {
/* 383 */           cnt += 1.0D;
/* 384 */           pos.add(Integer.valueOf(j));
/*     */         }
/*     */       }
/*     */     }
/* 388 */     return new double[] {
/* 389 */       cnt / (this.dat.size() * this.dat.get(0).length()), 
/* 390 */       pos.size() / this.dat.get(0).length() };
/*     */   }
/*     */   
/*     */   private void readPedigreeFamilies(File f) throws Exception
/*     */   {
/* 395 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*     */     String st;
/* 397 */     while ((st = br.readLine()) != null) { String st;
/* 398 */       String[] father = st.split("\\s+");
/* 399 */       String id_f = father[6].split(":")[4];
/* 400 */       String sex_f = father[4];
/* 401 */       this.sex.put(id_f, Boolean.valueOf(sex_f.equals("1")));
/*     */     }
/* 403 */     br.close();
/* 404 */     if ((f.getName().startsWith("ceu")) || (f.getName().startsWith("yri"))) {
/* 405 */       br = new BufferedReader(new FileReader(f));
/* 406 */       while ((st = br.readLine()) != null) {
/* 407 */         Map<Integer, String[]> m = new HashMap();
/* 408 */         Integer child_id = null;
/* 409 */         Integer mother_id = null;
/* 410 */         Integer father_id = null;
/* 411 */         for (int i = 0; i < 3; i++) {
/* 412 */           System.err.println(st);
/* 413 */           String[] str = st.split("\\s+");
/* 414 */           m.put(Integer.valueOf(Integer.parseInt(str[1])), str);
/* 415 */           if ((Integer.parseInt(str[2]) != 0) && (Integer.parseInt(str[3]) != 0)) {
/* 416 */             child_id = Integer.valueOf(Integer.parseInt(str[1]));
/*     */           }
/* 418 */           else if (str[4].equals("2")) {
/* 419 */             mother_id = Integer.valueOf(Integer.parseInt(str[1]));
/*     */           }
/* 421 */           else if (str[4].equals("1")) {
/* 422 */             father_id = Integer.valueOf(Integer.parseInt(str[1]));
/*     */           }
/* 424 */           if (i < 2) st = br.readLine();
/*     */         }
/* 426 */         String id_m = ((String[])m.get(mother_id))[6].split(":")[4];
/* 427 */         String id_c = ((String[])m.get(child_id))[6].split(":")[4];
/* 428 */         String id_f = ((String[])m.get(father_id))[6].split(":")[4];
/* 429 */         this.mother.put(id_c, id_m);
/* 430 */         this.father.put(id_c, id_f);
/*     */       }
/* 432 */       br.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 437 */   boolean failed = false;
/* 438 */   final Map<String, Boolean> sex = new HashMap();
/* 439 */   final Map<String, String> mother = new HashMap();
/* 440 */   final Map<String, String> father = new HashMap();
/*     */   
/* 442 */   final Set<String> males = new HashSet();
/* 443 */   final Set<String> females = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */   public HapMartData(File base, String pop, String namef, Integer start, Integer end)
/*     */     throws Exception
/*     */   {
/* 450 */     Logger.global.info("reading " + base.getParent());
/* 451 */     File ped = new File(base.getParentFile(), pop + "_ped.txt");
/* 452 */     readPedigreeFamilies(ped);
/* 453 */     if (namef == null) namef = pop + "_x.csv.txt";
/* 454 */     File f = new File(base, namef);
/* 455 */     this.dat = DataCollection.readHapMap(f, new int[] { start.intValue(), end.intValue(), excl.intValue() });
/* 456 */     for (int i = 0; i < this.dat.size(); i++) {
/* 457 */       String name = this.dat.get(i).getName();
/* 458 */       boolean male = ((Boolean)this.sex.get(name)).booleanValue();
/* 459 */       if (male) {
/* 460 */         this.males.add(name);
/*     */       }
/*     */       else {
/* 463 */         this.females.add(name);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 468 */     Logger.global.info("done reading " + base.getParent());
/*     */   }
/*     */   
/* 471 */   public List<int[]> split(int no, int overlap) { List<int[]> res = new ArrayList();
/* 472 */     List<Integer> pos = this.dat.loc;
/* 473 */     int num = (int)Math.ceil(pos.size() / no);
/* 474 */     for (int j = 0; j < num; j++) {
/* 475 */       int start = ((Integer)pos.get(j * no)).intValue();
/* 476 */       int end = ((Integer)pos.get(Math.min((j + 1) * no + overlap, pos.size() - 1))).intValue();
/* 477 */       res.add(new int[] { start, end });
/*     */     }
/* 479 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void findStartEnd(PrintWriter pw)
/*     */   {
/* 486 */     throw new Error("Unresolved compilation problem: \n\tCannot cast from Comparable to Comparable[]\n");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/ensj/HapMartData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */