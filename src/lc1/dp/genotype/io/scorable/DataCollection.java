/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ 
/*     */ 
/*     */ public class DataCollection
/*     */ {
/*  26 */   String name = "-";
/*     */   
/*     */   public static void convert(File in, File out, String inF, String outF) {
/*     */     try { DataCollection coll;
/*  30 */       if (inF.equals("dick")) { coll = readDickFormat(in); } else { DataCollection coll;
/*  31 */         if (inF.equals("hapmap")) { coll = readHapMap(in, null); } else { DataCollection coll;
/*  32 */           if (inF.equals("fastphase")) coll = readFastPhaseOutput(in); else
/*  33 */             throw new Exception("unknown format"); } }
/*  34 */       DataCollection coll; coll.collapse();
/*  35 */       if (outF.equals("dick")) coll.writeDickFormat(out);
/*  36 */       if (outF.endsWith("hapmap")) coll.printHapMapFormat(out, null);
/*     */     } catch (Exception exc) {
/*  38 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  43 */     String input_f = args[0];
/*  44 */     String output_f = args[1];
/*  45 */     String input_file = args[2];
/*  46 */     String output_file = args[3];
/*     */   }
/*     */   
/*     */   public static DataCollection readDickFormat(File f) throws Exception
/*     */   {
/*  51 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  52 */     DataCollection coll = new DataCollection();
/*  53 */     String[] st = br.readLine().split("\\s+");
/*  54 */     int noIndiv = Integer.parseInt(st[1]);
/*  55 */     int noSites = Integer.parseInt(st[2]);
/*  56 */     Character[] majorAllelle = new Character[noSites];
/*  57 */     Character[] minorAllelle = new Character[noSites];
/*  58 */     st = br.readLine().split("\\s+");
/*  59 */     for (int i = 1; i < st.length; i++) {
/*  60 */       coll.loc.add(Integer.valueOf(Integer.parseInt(st[i])));
/*     */     }
/*  62 */     st = br.readLine().split("\\s+");
/*  63 */     for (int i = 1; i < st.length; i++) {
/*  64 */       coll.names.add(st[i]);
/*     */     }
/*  66 */     for (int i = st.length; i < noSites; i++) {
/*  67 */       coll.names.add("-");
/*     */     }
/*  69 */     for (int i = 0; i < noIndiv; i++) {
/*  70 */       String[] str = br.readLine().split("\\s+");
/*  71 */       PhasedIntegerGenotypeData dat = new PhasedIntegerGenotypeData(str[0], noSites);
/*  72 */       for (int j = 0; j < noSites; j++) {
/*  73 */         char c = str[(j + 1)].charAt(0);
/*  74 */         if ((c == '?') || (c == '-')) { dat.addPoint(ComparableArray.make(null));
/*     */         } else {
/*  76 */           if (majorAllelle[j] == null) {
/*  77 */             majorAllelle[j] = new Character(c);
/*     */           }
/*  79 */           else if ((minorAllelle[j] == null) && (c != majorAllelle[j].charValue())) {
/*  80 */             minorAllelle[j] = Character.valueOf(c);
/*     */           }
/*  82 */           dat.addPoint(ComparableArray.make(Character.valueOf(c)));
/*     */         }
/*     */       }
/*     */       
/*  86 */       coll.data.add(dat);
/*     */     }
/*  88 */     coll.majorAllelle = new ArrayList((Collection)Arrays.asList(majorAllelle));
/*  89 */     coll.minorAllelle = new ArrayList((Collection)Arrays.asList(minorAllelle));
/*  90 */     coll.makeDataIndex();
/*  91 */     return coll;
/*     */   }
/*     */   
/*     */   public static DataCollection readFastPhaseOutput(File fp) throws Exception {
/*  95 */     BufferedReader br = new BufferedReader(new FileReader(fp));
/*  96 */     DataCollection coll = new DataCollection();
/*  97 */     String st = "";
/*  98 */     while (!(st = br.readLine()).startsWith("#")) {}
/*     */     
/* 100 */     int i = 0;
/* 101 */     while ((st != null) && (st.startsWith("#"))) {
/* 102 */       String name = st.split("\\s+")[2];
/*     */       
/* 104 */       st = br.readLine();
/* 105 */       List<String> dat = new ArrayList();
/* 106 */       while ((st != null) && (!st.startsWith("#")) && (!st.startsWith("END"))) {
/* 107 */         st = st.replaceAll("\\s+", "");
/* 108 */         dat.add(new String(st));
/* 109 */         st = br.readLine();
/*     */       }
/*     */       
/* 112 */       coll.data.add(new PhasedIntegerGenotypeData(name, (String[])dat.toArray(new String[0])));
/*     */       
/* 114 */       i++;
/* 115 */       if (st.startsWith("END")) break;
/*     */     }
/* 117 */     coll.makeDataIndex();
/* 118 */     return coll;
/*     */   }
/*     */   
/*     */   public static DataCollection readHapMap(File f, int[] start_end) throws Exception {
/* 122 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 123 */     String st = br.readLine().split(",")[4];
/* 124 */     DataCollection coll = new DataCollection();
/* 125 */     coll.name = st.substring(st.indexOf(':') + 1, st.indexOf('[')).trim();
/* 126 */     String[] idents = st.substring(st.indexOf('[') + 1, st.indexOf(']')).split("\\s+");
/*     */     
/* 128 */     for (int j = 0; j < idents.length; j++) {
/* 129 */       coll.data.add(new PhasedIntegerGenotypeData(idents[j], 100));
/*     */     }
/* 131 */     String currentString = "";
/* 132 */     for (int i = 0; (currentString = br.readLine()) != null; i++) {
/* 133 */       String[] str = currentString.split(",");
/* 134 */       Integer posi = Integer.valueOf(Integer.parseInt(str[1]));
/* 135 */       if ((start_end == null) || (posi.intValue() >= start_end[0])) {
/* 136 */         if ((start_end != null) && (posi.intValue() > start_end[1])) break;
/* 137 */         if ((start_end == null) || (posi.intValue() != start_end[2]))
/*     */         {
/* 139 */           String[] str_5 = str[4].split("\\s+");
/* 140 */           Character mA = null;
/* 141 */           Character minA = null;
/* 142 */           List<ComparableArray> l = new ArrayList();
/* 143 */           for (int j = 0; j < coll.data.size(); j++)
/*     */           {
/* 145 */             String string = str_5[j].trim();
/* 146 */             ComparableArray comp = new ComparableArray();
/* 147 */             for (int k = 0; k < string.length(); k++) {
/* 148 */               char c1 = string.charAt(k);
/* 149 */               if ((c1 == 'N') || (c1 == '?') || (c1 == '-')) {
/* 150 */                 comp.add(null);
/*     */               }
/*     */               else {
/* 153 */                 if (mA == null) {
/* 154 */                   mA = Character.valueOf(c1);
/*     */                 }
/* 156 */                 else if ((minA == null) && (c1 != mA.charValue())) {
/* 157 */                   minA = Character.valueOf(c1);
/*     */                 }
/* 159 */                 comp.add(Character.valueOf(c1));
/*     */               }
/*     */             }
/*     */             
/* 163 */             l.add(comp);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 169 */           if (minA != null) {
/* 170 */             coll.loc.add(posi);
/* 171 */             coll.names.add(str[2] + str[3]);
/* 172 */             coll.majorAllelle.add(mA);
/* 173 */             coll.minorAllelle.add(minA);
/* 174 */             for (int j = 0; j < coll.data.size(); j++)
/* 175 */               ((PhasedIntegerGenotypeData)coll.data.get(j)).addPoint((Comparable)l.get(j));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 180 */     coll.makeDataIndex();
/* 181 */     br.close();
/* 182 */     return coll;
/*     */   }
/*     */   
/*     */ 
/* 186 */   List<PhasedIntegerGenotypeData> data = new ArrayList();
/* 187 */   public List<Integer> loc = new ArrayList();
/* 188 */   public List<String> names = new ArrayList();
/* 189 */   public List<Comparable> majorAllelle = new ArrayList();
/* 190 */   public List<Comparable> minorAllelle = new ArrayList();
/* 191 */   public List<Integer> maf = new ArrayList();
/* 192 */   public List<Integer> maf_null = new ArrayList();
/* 193 */   public Map<String, Integer> name_index = new HashMap();
/*     */   
/*     */   DataCollection() {}
/*     */   
/* 197 */   public DataCollection(List<PhasedIntegerGenotypeData> list) { this.data = list;
/* 198 */     makeDataIndex();
/*     */   }
/*     */   
/*     */   public void modify() {
/* 202 */     for (int i = 0; i < this.data.size(); i++) {
/* 203 */       PhasedIntegerGenotypeData data_i = (PhasedIntegerGenotypeData)this.data.get(i);
/* 204 */       if ((Constants.addMissing() > 0.0D) || (Constants.addError() > 0.0D)) data_i.addMissingData(Constants.addMissing(), Constants.addError());
/* 205 */       if (Constants.convertHemizygousToHomozygous()) data_i.convertHemizygousHomozygous();
/* 206 */       data_i.mix();
/*     */     }
/*     */   }
/*     */   
/*     */   public DataCollection(DataCollection dat) {
/* 211 */     this.loc = dat.loc;
/* 212 */     this.names = dat.names;
/* 213 */     this.maf = dat.maf;
/* 214 */     this.maf_null = dat.maf_null;
/* 215 */     this.majorAllelle = dat.majorAllelle;
/* 216 */     this.minorAllelle = dat.minorAllelle;
/* 217 */     this.data = new ArrayList();
/* 218 */     for (int i = 0; i < dat.size(); i++) {
/* 219 */       PhasedIntegerGenotypeData data_i = new PhasedIntegerGenotypeData((PhasedIntegerGenotypeData)dat.get(i));
/* 220 */       this.data.add(data_i);
/*     */     }
/* 222 */     makeDataIndex();
/*     */   }
/*     */   
/* 225 */   public DataCollection(DataCollection dat, DataCollection dat2) { int len1 = dat.size();
/* 226 */     int len2 = dat2.size();
/* 227 */     this.name = (dat.name + "_" + dat2.name);
/* 228 */     for (int j = 0; j < len1; j++) {
/* 229 */       String name = dat.get(j).getName();
/* 230 */       this.data.add(new PhasedIntegerGenotypeData(name, 100));
/*     */     }
/* 232 */     for (int j = 0; j < len2; j++) {
/* 233 */       String name = dat2.get(j).getName();
/* 234 */       this.data.add(new PhasedIntegerGenotypeData(name, 100));
/*     */     }
/* 236 */     int i1 = 0;
/* 237 */     int i2 = 0;
/* 238 */     while ((i1 < dat.loc.size()) && (i2 < dat2.loc.size())) {
/* 239 */       Integer pos_1 = (Integer)dat.loc.get(i1);
/* 240 */       Integer pos_2 = (Integer)dat2.loc.get(i2);
/* 241 */       if (pos_1 == pos_2) {
/* 242 */         this.loc.add(pos_1);
/* 243 */         this.names.add((String)dat.names.get(i1));
/* 244 */         this.majorAllelle.add((Comparable)dat.majorAllelle.get(i1));
/* 245 */         this.minorAllelle.add((Comparable)dat.minorAllelle.get(i1));
/* 246 */         for (int k1 = 0; k1 < len1; k1++)
/*     */         {
/* 248 */           dat.get(k1).addPoint(dat.get(k1).getElement(i1));
/*     */         }
/* 250 */         for (int k2 = 0; k2 < len2; k2++) {
/* 251 */           dat.get(k2 + len1).addPoint(dat2.get(k2).getElement(i2));
/*     */         }
/* 253 */         i1++;
/* 254 */         i2++;
/*     */       }
/* 256 */       else if (pos_1.intValue() < pos_2.intValue()) {
/* 257 */         Comparable mA = (Comparable)dat.majorAllelle.get(i1);
/*     */         
/* 259 */         this.loc.add(pos_1);
/* 260 */         this.names.add((String)dat.names.get(i1));
/* 261 */         this.majorAllelle.add(mA);
/* 262 */         this.minorAllelle.add((Comparable)dat.minorAllelle.get(i1));
/* 263 */         for (int k1 = 0; k1 < len1; k1++) {
/* 264 */           dat.get(k1).addPoint(dat.get(k1).getElement(i1));
/*     */         }
/* 266 */         for (int k2 = 0; k2 < len2; k2++) {
/* 267 */           dat.get(k2 + len1).addPoint(ComparableArray.make(mA, mA));
/*     */         }
/* 269 */         i1++;
/*     */       }
/* 271 */       else if (pos_2.intValue() < pos_1.intValue()) {
/* 272 */         Comparable mA = (Comparable)dat2.majorAllelle.get(i2);
/* 273 */         this.minorAllelle.add((Comparable)dat2.minorAllelle.get(i1));
/* 274 */         this.loc.add(pos_2);
/* 275 */         this.names.add((String)dat2.names.get(i2));
/* 276 */         this.majorAllelle.add(mA);
/* 277 */         for (int k1 = 0; k1 < len1; k1++) {
/* 278 */           dat.get(k1).addPoint(ComparableArray.make(mA, mA));
/*     */         }
/* 280 */         for (int k2 = 0; k2 < len2; k2++) {
/* 281 */           dat.get(k2 + len1).addPoint(dat2.get(k2).getElement(i2));
/*     */         }
/* 283 */         i2++;
/*     */       }
/*     */     }
/* 286 */     makeDataIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 293 */   public CompoundScorableObject get(int j) { return (CompoundScorableObject)this.data.get(j); }
/*     */   
/*     */   public void collapse() {
/* 296 */     for (int i = 0; i < this.data.size(); i++) {
/* 297 */       PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)this.data.get(i);
/* 298 */       if (dat.homozygous()) dat.collapse();
/*     */     }
/*     */   }
/*     */   
/*     */   public PhasedIntegerGenotypeData get(String st)
/*     */   {
/* 304 */     PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)this.data.get(((Integer)this.name_index.get(st)).intValue());
/* 305 */     if (!dat.getName().equals(st)) throw new RuntimeException("!!");
/* 306 */     return dat;
/*     */   }
/*     */   
/*     */   public int getFirstIndexAbove(long pos) {
/* 310 */     for (int i = 0; i < this.loc.size(); i++) {
/* 311 */       if (((Integer)this.loc.get(i)).intValue() > pos) return i;
/*     */     }
/* 313 */     return this.loc.size();
/*     */   }
/*     */   
/*     */   public int getIndex(Integer name) {
/* 317 */     return this.loc.indexOf(name);
/*     */   }
/*     */   
/*     */   String getPrintString(int no, String st)
/*     */   {
/* 322 */     StringBuffer sb = new StringBuffer();
/* 323 */     for (int i = 0; i < no; i++) {
/* 324 */       sb.append(st);
/*     */     }
/* 326 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public Iterator<PhasedIntegerGenotypeData> iterator() {
/* 330 */     return this.data.iterator();
/*     */   }
/*     */   
/*     */   public int length() {
/* 334 */     return ((PhasedIntegerGenotypeData)this.data.get(0)).length();
/*     */   }
/*     */   
/*     */   public void majorAllelle() {
/* 338 */     for (int i = ((PhasedIntegerGenotypeData)this.data.get(0)).length() - 1; i >= 0; i--) {
/* 339 */       Object majorA = this.majorAllelle.get(i);
/* 340 */       Object minorA = this.minorAllelle.get(i);
/* 341 */       if (minorA == null) throw new RuntimeException("!! " + (String)this.names.get(i));
/* 342 */       if (majorA == null) throw new RuntimeException("!!");
/* 343 */       int majorAllelleCnt = 0;
/* 344 */       int minorAllelleCnt = 0;
/* 345 */       int nullCount = 0;
/* 346 */       for (int j = 0; j < this.data.size(); j++) {
/* 347 */         Iterator<Comparable> comp = ((ComparableArray)((PhasedIntegerGenotypeData)this.data.get(j)).getElement(i)).iterator();
/* 348 */         for (int k = 0; comp.hasNext(); k++) {
/* 349 */           Comparable nxt = (Comparable)comp.next();
/* 350 */           if (nxt == null) {
/* 351 */             nullCount++;
/*     */ 
/*     */           }
/* 354 */           else if (nxt.equals(majorA)) {
/* 355 */             majorAllelleCnt++;
/*     */           }
/* 357 */           else if (nxt.equals(minorA)) {
/* 358 */             minorAllelleCnt++;
/*     */           } else {
/* 360 */             throw new RuntimeException("!!");
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 366 */       if (minorAllelleCnt > 2 * this.data.size()) throw new RuntimeException("!!");
/* 367 */       this.maf.add(0, Integer.valueOf(minorAllelleCnt));
/* 368 */       this.maf_null.add(0, Integer.valueOf(nullCount));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void printHapMapFormat(File f, int[] s_i)
/*     */     throws Exception
/*     */   {
/* 379 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
/* 380 */     int pos_index = s_i == null ? 0 : getFirstIndexAbove(s_i[0]);
/* 381 */     pw.print("chromosome,position,strand,marker id,POP:" + this.name + " [");
/* 382 */     for (int ij = 0; ij < size(); ij++) {
/* 383 */       pw.print(get(ij).getName());
/* 384 */       if (ij == size() - 1) pw.println("]"); else
/* 385 */         pw.print(" ");
/*     */     }
/* 387 */     while ((pos_index < this.loc.size()) && ((s_i == null) || (((Integer)this.loc.get(pos_index)).intValue() < s_i[1]))) {
/* 388 */       pw.print("chrX,");
/* 389 */       pw.print(this.loc.get(pos_index) + ",");
/* 390 */       String name = (String)this.names.get(pos_index);
/* 391 */       pw.print(name.substring(0, 1) + ",");
/* 392 */       pw.print(name.substring(1, name.length()) + ",");
/* 393 */       for (int ij = 0; ij < size(); ij++) {
/* 394 */         Iterator<Comparable> comp = ((ComparableArray)get(ij).getElement(pos_index)).iterator();
/* 395 */         for (int k = 0; comp.hasNext(); k++) {
/* 396 */           Comparable nxt = (Comparable)comp.next();
/* 397 */           if (nxt == null) { pw.print("N");
/* 398 */           } else if ((nxt instanceof Boolean)) {
/* 399 */             if (((Boolean)nxt).booleanValue()) pw.print(this.majorAllelle.get(pos_index)); else
/* 400 */               pw.print(this.minorAllelle.get(pos_index));
/*     */           } else
/* 402 */             pw.print(nxt.toString());
/*     */         }
/* 404 */         if (ij == size() - 1) pw.println(); else
/* 405 */           pw.print(" ");
/*     */       }
/* 407 */       pos_index++;
/*     */     }
/* 409 */     pw.close();
/*     */   }
/*     */   
/*     */   public void restricToAlias(Collection<String> alias) {
/* 413 */     for (Iterator<PhasedIntegerGenotypeData> it = iterator(); it.hasNext();) {
/* 414 */       if (!alias.contains(((PhasedIntegerGenotypeData)it.next()).getName())) {
/* 415 */         it.remove();
/*     */       }
/*     */     }
/* 418 */     makeDataIndex();
/*     */   }
/*     */   
/* 421 */   public void selectMale() { Set<String> alias = new HashSet();
/* 422 */     for (Iterator<PhasedIntegerGenotypeData> it = this.data.iterator(); it.hasNext();) {
/* 423 */       PhasedIntegerGenotypeData nxt = (PhasedIntegerGenotypeData)it.next();
/* 424 */       if (nxt.noCopies() == 1) {
/* 425 */         alias.add(nxt.getName());
/*     */       }
/* 427 */       else if (nxt.homozygous()) {
/* 428 */         Logger.global.warning("should not be homozygous");
/*     */       }
/*     */     }
/* 431 */     restricToAlias(alias);
/*     */   }
/*     */   
/*     */   private void makeDataIndex()
/*     */   {
/* 436 */     for (int i = 0; i < this.data.size(); i++) {
/* 437 */       this.name_index.put(get(i).getName(), Integer.valueOf(i));
/*     */     }
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/* 443 */     return this.data.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void transform(double[] fraction)
/*     */   {
/* 450 */     List<PhasedIntegerGenotypeData> newL = new ArrayList();
/* 451 */     int no_copies = Constants.sample(fraction) + 1;
/* 452 */     while (this.data.size() >= no_copies)
/*     */     {
/* 454 */       CompoundScorableObject[] unit = new CompoundScorableObject[no_copies];
/* 455 */       for (int i = 0; i < unit.length; i++) {
/* 456 */         int i1 = 
/* 457 */           no_copies == 1 ? 0 : 
/* 458 */           Constants.nextInt(this.data.size());
/* 459 */         unit[i] = ((CompoundScorableObject)this.data.remove(i1));
/* 460 */         if (unit[i].noCopies() != 1) throw new RuntimeException("!!");
/*     */       }
/* 462 */       PhasedIntegerGenotypeData pid = new PhasedIntegerGenotypeData(unit);
/* 463 */       newL.add(pid);
/* 464 */       no_copies = Constants.sample(fraction) + 1;
/*     */     }
/*     */     
/* 467 */     this.data = newL;
/* 468 */     makeDataIndex();
/*     */   }
/*     */   
/*     */   public void writeDickFormat(File f) throws Exception {
/* 472 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(f)));
/* 473 */     pw.println("\t" + size() + "\t" + length());
/* 474 */     pw.print("\t");
/* 475 */     pw.println(Format.sprintf(getPrintString(this.loc.size(), "%5i "), this.loc.toArray(new Integer[0])));
/* 476 */     pw.print("\t");
/* 477 */     pw.println(Format.sprintf(getPrintString(this.names.size(), "%5s "), this.names.toArray(new String[0])));
/* 478 */     for (int k = 0; k < size(); k++)
/*     */     {
/* 480 */       pw.print(get(k).getName() + "\t");
/* 481 */       for (int i = 0; i < get(k).length(); i++) {
/* 482 */         ComparableArray c = (ComparableArray)get(k).getElement(i);
/*     */         
/* 484 */         if (((Character)c.get(0)).charValue() == 'N') { pw.print("-");
/*     */         } else
/* 486 */           pw.print(c.get(0));
/* 487 */         if (i < get(k).length() - 1) pw.print("\t"); else
/* 488 */           pw.println();
/*     */       }
/*     */     }
/* 491 */     pw.close();
/*     */   }
/*     */   
/*     */   public void writeFastphase(File outp) throws Exception {
/* 495 */     PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(outp)));
/* 496 */     pw.println(this.data.size());
/* 497 */     pw.println(((ScorableObject)this.data.get(0)).length());
/* 498 */     for (int i = 0; i < this.data.size(); i++) {
/* 499 */       ((ScorableObject)this.data.get(i)).print(pw);
/*     */     }
/* 501 */     pw.close();
/*     */   }
/*     */   
/*     */   public void setAsBoolean() {
/* 505 */     for (int i = length() - 1; i >= 0; i--) {
/* 506 */       Object all = this.minorAllelle.get(i);
/* 507 */       for (int j = 0; j < this.data.size(); j++) {
/* 508 */         PhasedIntegerGenotypeData dat_j = (PhasedIntegerGenotypeData)this.data.get(j);
/* 509 */         ComparableArray comp = (ComparableArray)dat_j.getElement(i);
/* 510 */         for (int k = 0; k < comp.size(); k++) {
/* 511 */           Comparable nxt = (Comparable)comp.get(k);
/* 512 */           if (nxt != null) {
/* 513 */             if (nxt.equals(all)) {
/* 514 */               nxt = Boolean.TRUE;
/*     */             }
/*     */             else {
/* 517 */               nxt = Boolean.FALSE;
/*     */             }
/*     */           }
/*     */           
/* 521 */           comp.set(k, nxt);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<String> getNames()
/*     */   {
/* 529 */     List<String> l = new ArrayList();
/* 530 */     for (int i = 0; i < this.data.size(); i++) {
/* 531 */       l.add(((PhasedIntegerGenotypeData)this.data.get(i)).getName());
/*     */     }
/* 533 */     return l;
/*     */   }
/*     */   
/*     */   public void trim(int i) {
/* 537 */     if (this.data.size() <= i) return;
/* 538 */     List<String> names = getNames();
/* 539 */     List<String> toKeep = new ArrayList();
/* 540 */     while (toKeep.size() < i) {
/* 541 */       toKeep.add((String)names.remove(Constants.nextInt(names.size())));
/*     */     }
/* 543 */     restricToAlias(toKeep);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/DataCollection.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */