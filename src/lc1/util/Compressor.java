/*     */ package lc1.util;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileFilter;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.SequenceInputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.zip.Adler32;
/*     */ import java.util.zip.CheckedInputStream;
/*     */ import java.util.zip.CheckedOutputStream;
/*     */ import java.util.zip.Checksum;
/*     */ import java.util.zip.ZipEntry;
/*     */ import java.util.zip.ZipFile;
/*     */ import java.util.zip.ZipInputStream;
/*     */ import java.util.zip.ZipOutputStream;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ 
/*     */ public class Compressor
/*     */ {
/*  32 */   static int idColumn = 0;
/*  33 */   static int[] snpColumns = { 3, 2, 1 };
/*  34 */   static int[] resultColumns = { 4, 5 };
/*  35 */   static String[] resultColumnHeader = { "", "", "", "", "Log R", "B allele" };
/*  36 */   static String split = "\t";
/*     */   final File in;
/*     */   final File out;
/*  39 */   static boolean header = true;
/*     */   static final int BUFFER = 2048;
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/*  44 */       File user = new File(System.getProperty("user.dir"));
/*  45 */       File[] f = user.listFiles(new FileFilter()
/*     */       {
/*     */         public boolean accept(File pathname) {
/*  48 */           return pathname.getName().endsWith("data1M.txt");
/*     */         }
/*     */       });
/*     */       
/*  52 */       for (int i = 0; i < f.length; i++) {
/*  53 */         Compressor compr = new Compressor(f[i]);
/*  54 */         compr.compress();
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/*  58 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private void writeLine(OutputStreamWriter osw, String[] str) throws Exception {
/*  63 */     osw.write(str[resultColumns[0]]);
/*  64 */     for (int i = 1; i < resultColumns.length; i++) {
/*  65 */       osw.write("\t");
/*  66 */       int j = resultColumns[i];
/*  67 */       osw.write(str[j]);
/*     */     }
/*  69 */     osw.write("\n");
/*     */   }
/*     */   
/*     */   private void writeLine(OutputStreamWriter osw, List<String> ids) throws Exception {
/*  73 */     for (int i = 0; i < ids.size(); i++) {
/*  74 */       osw.write((String)ids.get(i) + "\n");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private String getEntryName(String[] str)
/*     */   {
/*  81 */     StringBuffer sb = new StringBuffer(str[snpColumns[0]]);
/*  82 */     for (int i = 1; i < snpColumns.length; i++) {
/*  83 */       sb.append("_");
/*  84 */       sb.append(modify(str[snpColumns[i]]));
/*     */     }
/*  86 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public void compress()
/*     */   {
/*  92 */     if (this.out.exists()) return;
/*     */     try {
/*  94 */       List<String> ids = new ArrayList();
/*  95 */       BufferedReader origin = new BufferedReader(new FileReader(this.in));
/*  96 */       if (header) origin.readLine();
/*  97 */       FileOutputStream dest = new FileOutputStream(this.out);
/*  98 */       CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
/*  99 */       ZipOutputStream out = 
/* 100 */         new ZipOutputStream(
/* 101 */         new BufferedOutputStream(checksum));
/* 102 */       OutputStreamWriter osw = new OutputStreamWriter(out);
/* 103 */       out.setMethod(8);
/* 104 */       String st = "";
/* 105 */       String comp = null;
/*     */       
/* 107 */       int index = snpColumns[0];
/* 108 */       ZipEntry headings = new ZipEntry("Name");
/* 109 */       out.putNextEntry(headings);
/* 110 */       writeLine(osw, resultColumnHeader);
/* 111 */       osw.flush();
/* 112 */       for (int ik = 0; (st = origin.readLine()) != null; ik++) {
/* 113 */         String[] str = st.split(split);
/* 114 */         if ((comp == null) || (!comp.equals(str[index]))) {
/* 115 */           osw.flush();
/* 116 */           ZipEntry entry = new ZipEntry(getEntryName(str));
/* 117 */           out.putNextEntry(entry);
/* 118 */           comp = str[index];
/* 119 */           ik = 0;
/*     */         }
/* 121 */         if (ik >= ids.size()) { ids.add(str[0]);
/* 122 */         } else if (!((String)ids.get(ik)).equals(str[0])) throw new RuntimeException("!!");
/* 123 */         writeLine(osw, str);
/*     */       }
/* 125 */       osw.flush();
/* 126 */       ZipEntry samples = new ZipEntry("Samples");
/* 127 */       out.putNextEntry(samples);
/* 128 */       writeLine(osw, ids);
/* 129 */       osw.flush();
/* 130 */       out.close();
/* 131 */       System.out.println("checksum: " + 
/* 132 */         checksum.getChecksum().getValue());
/*     */     } catch (Exception e) {
/* 134 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void readZip(ZipFile f, String st, String[] res, int col) {
/*     */     try {
/* 140 */       BufferedReader br = new BufferedReader(new InputStreamReader(f.getInputStream(f.getEntry(st))));
/* 141 */       String str = "";
/* 142 */       for (int i = 0; (str = br.readLine()) != null; i++) {
/* 143 */         String[] stri = str.split("\t");
/* 144 */         res[i] = stri[col];
/*     */       }
/* 146 */       br.close();
/*     */     } catch (Exception exc) {
/* 148 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static BufferedReader readZip(final ZipFile f, List<String> l)
/*     */   {
/* 154 */     Enumeration<InputStream> inputStreams = new Enumeration() {
/* 155 */       int i = 0;
/*     */       
/* 157 */       public boolean hasMoreElements() { return this.i < Compressor.this.size(); }
/*     */       
/*     */       public InputStream nextElement()
/*     */       {
/*     */         try
/*     */         {
/* 163 */           System.err.println((String)Compressor.this.get(this.i));
/* 164 */           InputStream nxt = f.getInputStream(f.getEntry((String)Compressor.this.get(this.i)));
/* 165 */           this.i += 1;
/* 166 */           return nxt;
/*     */         } catch (Exception exc) {
/* 168 */           exc.printStackTrace(); }
/* 169 */         return null;
/*     */ 
/*     */       }
/*     */       
/*     */ 
/* 174 */     };
/* 175 */     return new BufferedReader(new InputStreamReader(new SequenceInputStream(inputStreams)));
/*     */   }
/*     */   
/*     */   public static List<String> getIndiv(ZipFile f, String entryName, Integer column, int index) throws Exception {
/* 179 */     BufferedReader nxt = 
/* 180 */       new BufferedReader(new InputStreamReader(
/* 181 */       f.getInputStream(f.getEntry(entryName))));
/* 182 */     List<String> indiv = new ArrayList();
/* 183 */     String st = "";
/* 184 */     for (int k = 0; ((st = nxt.readLine()) != null) && (k < Constants.maxIndiv(index)); k++) {
/* 185 */       String str = st.trim();
/* 186 */       if (column == null) {
/* 187 */         indiv.add(str);
/*     */       }
/*     */       else {
/* 190 */         indiv.add(str.split("\t")[column.intValue()]);
/*     */       }
/*     */     }
/* 193 */     nxt.close();
/* 194 */     return indiv;
/*     */   }
/*     */   
/*     */   private static void read(ZipFile f, String string, List<String> indiv, int i) throws Exception
/*     */   {
/* 199 */     BufferedReader nxt = 
/* 200 */       new BufferedReader(new InputStreamReader(
/* 201 */       f.getInputStream(f.getEntry(string))));
/* 202 */     String st = "";
/* 203 */     while ((st = nxt.readLine()) != null) {
/* 204 */       indiv.add(st.split("\t")[i]);
/*     */     }
/* 206 */     nxt.close();
/*     */   }
/*     */   
/*     */   private static String conc(String[] str, int from) {
/* 210 */     StringBuffer sb = new StringBuffer(str[from]);
/* 211 */     for (int i = from + 1; i < str.length; i++) {
/* 212 */       sb.append(str[i]);
/*     */     }
/* 214 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static void readBuildFile(File buildFile, String chrom, int from, int to, SortedMap<Integer, String> buildAll, int loc_index, int chr_index, int snp_index) throws Exception
/*     */   {
/* 219 */     BufferedReader br = DataCollection.getBufferedReader(buildFile);
/* 220 */     if (br == null) throw new RuntimeException("build file does not exist! " + buildFile);
/* 221 */     String st = "";
/* 222 */     while ((st = br.readLine()) != null) {
/* 223 */       String[] str = st.split("\\s+");
/* 224 */       if (str[chr_index].equals(chrom)) {
/* 225 */         int no = Integer.parseInt(str[loc_index]);
/* 226 */         if (no >= from) {
/* 227 */           if (no > to) break;
/* 228 */           if (!buildAll.containsValue(str[snp_index])) {
/* 229 */             buildAll.put(Integer.valueOf(no), str[snp_index]); continue;
/*     */             
/*     */ 
/* 232 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void readBuildFile1(File buildFile, String chrom, int from, int to, List<Integer> loc, List<String> chr, List<String> snpid, int loc_index, int chr_index, int snp_index)
/*     */     throws Exception
/*     */   {
/* 244 */     List<String> l = new ArrayList();
/* 245 */     BufferedReader br = DataCollection.getBufferedReader(buildFile);
/* 246 */     if (br == null) throw new RuntimeException("build file does not exist! " + buildFile);
/* 247 */     String st = "";
/* 248 */     while ((st = br.readLine()) != null) {
/* 249 */       String[] str = st.split("\\s+");
/* 250 */       if (str[chr_index].equals(chrom)) {
/* 251 */         int no = Integer.parseInt(str[loc_index]);
/* 252 */         if (no >= from) {
/* 253 */           if (no > to) break;
/* 254 */           l.add(str[loc_index]);
/* 255 */           loc.add(Integer.valueOf(no));
/* 256 */           chr.add(str[chr_index].substring(3));
/* 257 */           snpid.add(str[snp_index]); continue;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 267 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void readBuildFile(File buildFile, String chrom, int from, int to, List<Integer> loc, List<String> chr, List<String> snpid, List<Character> majorAllele, List<Character> minorAllele, List<Double> recesiveGenoFreq, int loc_index, int chr_index, int snp_index, int[] maf_index, int recesFreq_index)
/*     */     throws Exception
/*     */   {
/* 281 */     List<String> l = new ArrayList();
/* 282 */     BufferedReader br = DataCollection.getBufferedReader(buildFile);
/* 283 */     if (br == null) throw new RuntimeException("build file does not exist! " + buildFile);
/* 284 */     String st = "";
/* 285 */     while ((st = br.readLine()) != null) {
/* 286 */       String[] str = st.split("\\s+");
/* 287 */       if (str[chr_index].equals(chrom)) {
/* 288 */         int no = Integer.parseInt(str[loc_index]);
/* 289 */         if (no >= from) {
/* 290 */           if (no > to) break;
/* 291 */           l.add(str[loc_index]);
/* 292 */           loc.add(Integer.valueOf(no));
/* 293 */           chr.add(str[chr_index].substring(3));
/* 294 */           snpid.add(str[snp_index]);
/* 295 */           if ((maf_index != null) && (str.length > maf_index[0])) {
/* 296 */             majorAllele.add(Character.valueOf(str[maf_index[0]].charAt(0)));
/* 297 */             minorAllele.add(Character.valueOf(str[maf_index[1]].charAt(0)));
/*     */           }
/* 299 */           if (str.length > recesFreq_index) {
/* 300 */             recesiveGenoFreq.add(Double.valueOf(Double.parseDouble(str[recesFreq_index])));
/*     */           }
/* 302 */           System.err.println(no); continue;
/*     */           
/* 304 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static BufferedReader readZip(ZipFile f, int from, int to, List<Integer> loc, List<String> chr, List<String> snpid)
/*     */     throws Exception
/*     */   {
/* 313 */     List<String> l = new ArrayList();
/* 314 */     Enumeration entries = f.entries();
/* 315 */     while (entries.hasMoreElements()) {
/* 316 */       ZipEntry entry = (ZipEntry)entries.nextElement();
/* 317 */       String name = entry.getName();
/* 318 */       if ((!name.equals("Name")) && (!name.equals("Sample"))) {
/*     */         try {
/* 320 */           String[] names = name.split("_");
/* 321 */           int no = Integer.parseInt(names[0]);
/*     */           
/* 323 */           if (no < from) continue;
/* 324 */           if (no > to) break;
/* 325 */           l.add(name);
/* 326 */           loc.add(Integer.valueOf(no));
/* 327 */           chr.add(names[1]);
/* 328 */           snpid.add(conc(names, 2));
/*     */         }
/*     */         catch (NumberFormatException localNumberFormatException) {}
/* 331 */         break;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 338 */     return readZip(f, l);
/*     */   }
/*     */   
/*     */   public static List<String> readZipFrom(ZipFile f, String name) throws Exception
/*     */   {
/* 343 */     BufferedReader br = new BufferedReader(new InputStreamReader(f.getInputStream(f.getEntry(name))));
/*     */     
/* 345 */     List<String> l = new ArrayList();
/* 346 */     String st; while ((st = br.readLine()) != null) { String st;
/* 347 */       l.add(st);
/*     */     }
/* 349 */     return l;
/*     */   }
/*     */   
/*     */   public static BufferedReader readZipFrom(ZipFile f, int from, int to, List<Integer> loc, List<String> chr, List<String> snpid)
/*     */     throws Exception
/*     */   {
/* 355 */     Enumeration entries = f.entries();
/* 356 */     List<String> l = new ArrayList();
/* 357 */     for (int i = 0; i < from; i++) {
/* 358 */       entries.nextElement();
/*     */     }
/* 360 */     for (int i = 0; i < to; i++) {
/* 361 */       String name = ((ZipEntry)entries.nextElement()).getName();
/* 362 */       l.add(name);
/* 363 */       String[] names = name.split("_");
/* 364 */       loc.get(Integer.parseInt(names[0]));
/* 365 */       chr.add(names[1]);
/* 366 */       snpid.add(conc(names, 2));
/*     */     }
/*     */     
/* 369 */     return readZip(f, l);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void unzip(File in)
/*     */   {
/*     */     try
/*     */     {
/* 377 */       int i = 2048;
/* 378 */       BufferedOutputStream dest = null;
/* 379 */       FileInputStream fis = 
/* 380 */         new FileInputStream(in);
/* 381 */       CheckedInputStream checksum = 
/* 382 */         new CheckedInputStream(fis, new Adler32());
/* 383 */       ZipInputStream zis = 
/* 384 */         new ZipInputStream(
/* 385 */         new BufferedInputStream(checksum));
/*     */       ZipEntry entry;
/* 387 */       while ((entry = zis.getNextEntry()) != null) { ZipEntry entry;
/* 388 */         int BUFFER; System.out.println("Extracting: " + entry);
/*     */         
/* 390 */         byte[] data = new byte['ࠀ'];
/*     */         
/* 392 */         FileOutputStream fos = 
/* 393 */           new FileOutputStream(entry.getName());
/* 394 */         dest = new BufferedOutputStream(fos, 
/* 395 */           2048);
/* 396 */         int count; while ((count = zis.read(data, 0, 
/* 397 */           2048)) != -1) { int count;
/* 398 */           dest.write(data, 0, count);
/*     */         }
/* 400 */         dest.flush();
/* 401 */         dest.close();
/*     */       }
/* 403 */       zis.close();
/* 404 */       System.out.println("Checksum:  " + checksum.getChecksum().getValue());
/*     */     }
/*     */     catch (Exception e) {
/* 407 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public String modify(String st) {
/* 412 */     return st.replace(':', ',');
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Compressor(File f)
/*     */     throws Exception
/*     */   {
/* 423 */     this.in = f;
/* 424 */     this.out = new File(f.getAbsolutePath() + ".zip");
/*     */   }
/*     */   
/*     */ 
/*     */   public static List<Integer> readPositions(File file)
/*     */     throws Exception
/*     */   {
/* 431 */     ZipFile f = new ZipFile(file);
/* 432 */     List<Integer> l = new ArrayList();
/* 433 */     for (Enumeration en = f.entries(); en.hasMoreElements();) {
/* 434 */       ZipEntry ent = (ZipEntry)en.nextElement();
/* 435 */       if ((!ent.getName().equals("Samples")) && (!ent.getName().equals("Name"))) {
/* 436 */         String nm = ent.getName().split("_")[0];
/* 437 */         l.add(Integer.valueOf(Integer.parseInt(nm)));
/*     */       } }
/* 439 */     return l;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/util/Compressor.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */