/*     */ package lc1.CGH;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.collection.LikelihoodDataCollection;
/*     */ 
/*     */ 
/*     */ public class Locreader
/*     */ {
/*     */   public final long lengthLimit;
/*     */   LikelihoodDataCollection dc;
/*     */   final String name;
/*  25 */   public SortedSet<String> keys = new TreeSet();
/*     */   
/*     */   public SortedSet<Integer> probes;
/*  28 */   private Map<String, List<Location>> deletions = new HashMap();
/*  29 */   private Map<String, List<Location>> amplifications = new HashMap();
/*     */   
/*  31 */   public void sort() { for (Iterator<List<Location>> it = this.deletions.values().iterator(); it.hasNext();) {
/*  32 */       Collections.sort((List)it.next());
/*     */     }
/*  34 */     for (Iterator<List<Location>> it = this.amplifications.values().iterator(); it.hasNext();) {
/*  35 */       Collections.sort((List)it.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public Integer[] detected(Locreader loc1, int overlp, PrintWriter out1)
/*     */   {
/*  41 */     out1.println("how many does " + this.name + " find of " + loc1.name + " predictions");
/*  42 */     boolean[] del = { false, true };
/*  43 */     Integer[] res = { Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0) };
/*  44 */     for (int i = 0; i < del.length; i++) {
/*  45 */       for (Iterator<String> it = loc1.keys.iterator(); it.hasNext();) {
/*  46 */         detected(loc1, (String)it.next(), del[i], res, overlp, out1);
/*     */       }
/*     */     }
/*  49 */     return res;
/*     */   }
/*     */   
/*  52 */   public void printDetails(PrintWriter out1, Location loc, String name) { if (this.dc != null) {
/*  53 */       if (name.equals("")) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  59 */       out1.println(this.name);
/*  60 */       List<Double> l = new ArrayList();
/*  61 */       List<Integer> l1 = new ArrayList();
/*  62 */       List<Integer> l2 = new ArrayList();
/*  63 */       List<Double> b = new ArrayList();
/*  64 */       double[] max = this.dc.getR(loc, l, l1, l2, b, name);
/*  65 */       if (max == null) return;
/*  66 */       StringBuffer sb = new StringBuffer();
/*  67 */       StringBuffer sb1 = new StringBuffer();
/*  68 */       for (int i = 0; i < l.size(); i++) {
/*  69 */         sb.append("%7.3f ");
/*     */       }
/*  71 */       for (int i = 0; i < l.size(); i++) {
/*  72 */         sb1.append("%7i ");
/*     */       }
/*  74 */       out1.println("max " + max[0] + " " + max[1]);
/*  75 */       out1.println(Format.sprintf(sb.toString(), l.toArray()));
/*  76 */       if (b.size() > 0) out1.println(Format.sprintf(sb.toString(), b.toArray()));
/*  77 */       out1.println(Format.sprintf(sb1.toString(), l1.toArray()));
/*  78 */       out1.println(Format.sprintf(sb1.toString(), l2.toArray()));
/*     */     }
/*     */   }
/*     */   
/*     */   public void detected(Locreader loc1, String key, boolean del, Integer[] res, int overlapNoProbes, PrintWriter out1) {
/*  83 */     List<Location> map1 = del ? (List)this.deletions.get(key) : (List)this.amplifications.get(key);
/*  84 */     List<Location> dectable = new ArrayList();
/*  85 */     SortedSet<Integer> detectableProbes = new TreeSet();
/*  86 */     SortedSet<Integer> detectedProbes = new TreeSet();
/*  87 */     detectable(loc1, key, del, dectable, detectableProbes, overlapNoProbes);
/*  88 */     List<Location> detected = new ArrayList();
/*  89 */     for (int i = 0; i < dectable.size(); i++) {
/*  90 */       Location loc = (Location)dectable.get(i);
/*  91 */       if (map1 != null) {
/*  92 */         for (int j = 0; j < map1.size(); j++) {
/*  93 */           Location loc_1 = (Location)map1.get(j);
/*     */           
/*  95 */           if (loc.overlaps(loc_1) >= 0.0D) {
/*  96 */             detectedProbes.addAll(noPosIn(loc.getOverlap(loc_1)));
/*  97 */             detected.add(loc);
/*  98 */             out1.println("detected " + loc + " " + loc_1);
/*  99 */             printDetails(out1, loc, loc.name());
/* 100 */             loc1.printDetails(out1, loc, loc.name());
/* 101 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 106 */       out1.println("not detected " + loc);
/* 107 */       printDetails(out1, loc, loc.name());
/* 108 */       loc1.printDetails(out1, loc, loc.name());
/*     */     }
/*     */     
/* 111 */     int tmp312_311 = 0; Integer[] tmp312_309 = res;tmp312_309[tmp312_311] = Integer.valueOf(tmp312_309[tmp312_311].intValue() + detected.size()); int 
/* 112 */       tmp332_331 = 1; Integer[] tmp332_329 = res;tmp332_329[tmp332_331] = Integer.valueOf(tmp332_329[tmp332_331].intValue() + dectable.size()); int 
/* 113 */       tmp352_351 = 2; Integer[] tmp352_349 = res;tmp352_349[tmp352_351] = Integer.valueOf(tmp352_349[tmp352_351].intValue() + detectedProbes.size()); int 
/* 114 */       tmp372_371 = 3; Integer[] tmp372_369 = res;tmp372_369[tmp372_371] = Integer.valueOf(tmp372_369[tmp372_371].intValue() + detectableProbes.size());
/*     */     
/*     */ 
/*     */ 
/* 118 */     out1.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void detectable(Locreader loc1, String key, boolean del, List<Location> detecable, SortedSet<Integer> detectableProbes, int overlapNoProbes)
/*     */   {
/* 125 */     List<Location> map2 = del ? (List)loc1.deletions.get(key) : (List)loc1.amplifications.get(key);
/* 126 */     if (map2 == null) return;
/* 127 */     for (int i = 0; i < map2.size(); i++) {
/* 128 */       Location loc = (Location)map2.get(i);
/* 129 */       SortedSet<Integer> noPos = noPosIn((Location)map2.get(i));
/* 130 */       if ((noPos != null) && (noPos.size() >= overlapNoProbes)) {
/* 131 */         detecable.add(loc);
/* 132 */         detectableProbes.addAll(noPos);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public SortedSet<Integer> noPosIn(Location loc) {
/* 138 */     SortedSet<Integer> tail = this.probes.tailSet(Integer.valueOf((int)loc.min));
/* 139 */     if (tail == null) return null;
/* 140 */     return tail.headSet(Integer.valueOf((int)loc.max + 1));
/*     */   }
/*     */   
/*     */   public int number() {
/* 144 */     int num = 0;
/* 145 */     for (Iterator<String> it = this.keys.iterator(); it.hasNext();) {
/* 146 */       num += number((String)it.next());
/*     */     }
/* 148 */     return num;
/*     */   }
/*     */   
/*     */   public int number(String st) {
/* 152 */     List<Location> loc = (List)this.deletions.get(st);
/* 153 */     List<Location> loc2 = (List)this.amplifications.get(st);
/* 154 */     int num = loc == null ? 0 : loc.size();
/* 155 */     num += (loc2 == null ? 0 : loc2.size());
/* 156 */     return num;
/*     */   }
/*     */   
/* 159 */   public int number(String st, int noCop) { List<Location> loc = noCop == 0 ? (List)this.deletions.get(st) : (List)this.amplifications.get(st);
/* 160 */     return loc == null ? 0 : loc.size();
/*     */   }
/*     */   
/*     */   public long totalSize(String key) {
/* 164 */     long size = 0L;
/* 165 */     for (Iterator<Location> it = iterator(key); it.hasNext();) {
/* 166 */       size += ((Location)it.next()).size();
/*     */     }
/* 168 */     return size;
/*     */   }
/*     */   
/* 171 */   public long totalSize(String key, int nocop) { long size = 0L;
/* 172 */     for (Iterator<Location> it = iterator(key, nocop); it.hasNext();) {
/* 173 */       size += ((Location)it.next()).size();
/*     */     }
/* 175 */     return size;
/*     */   }
/*     */   
/* 178 */   public long[] getTotal() { long[] res = new long[2];
/* 179 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 180 */       Location loc = (Location)it.next();
/* 181 */       if (loc.noCop() == 0) res[0] += loc.max - loc.min; else
/* 182 */         res[1] += loc.max - loc.min;
/*     */     }
/* 184 */     return res;
/*     */   }
/*     */   
/* 187 */   public Location getFirst(String name) { Location loc1 = getFirst(0, name);
/* 188 */     Location loc2 = getFirst(2, name);
/* 189 */     if (loc1 == null) return loc2;
/* 190 */     if (loc2 == null) return loc1;
/* 191 */     return loc1.compareTo(loc2) < 0 ? loc1 : loc2;
/*     */   }
/*     */   
/* 194 */   public Location getFirst() { SortedSet<Location> l = new TreeSet();
/* 195 */     for (Iterator<String> it = this.keys.iterator(); it.hasNext();) {
/* 196 */       Location loc = getFirst((String)it.next());
/* 197 */       if (loc != null) l.add(loc);
/*     */     }
/* 199 */     return (Location)l.first();
/*     */   }
/*     */   
/* 202 */   public Location getLast() { SortedSet<Location> l = new TreeSet();
/* 203 */     for (Iterator<String> it = this.keys.iterator(); it.hasNext();) {
/* 204 */       Location loc = getLast((String)it.next());
/* 205 */       if (loc != null) l.add(loc);
/*     */     }
/* 207 */     return (Location)l.last();
/*     */   }
/*     */   
/* 210 */   public Location getLast(String name) { Location loc1 = getLast(0, name);
/* 211 */     Location loc2 = getLast(2, name);
/* 212 */     if (loc1 == null) return loc2;
/* 213 */     if (loc2 == null) return loc1;
/* 214 */     return loc1.compareTo(loc2) > 0 ? loc1 : loc2;
/*     */   }
/*     */   
/*     */   public Location getFirst(int noCop, String nm) {
/* 218 */     if (noCop == 0) return ((List)this.deletions.get(nm)).size() > 0 ? (Location)((List)this.deletions.get(nm)).get(0) : null;
/* 219 */     return ((List)this.amplifications.get(nm)).size() > 0 ? (Location)((List)this.amplifications.get(nm)).get(0) : null;
/*     */   }
/*     */   
/* 222 */   public Location getLast(int noCop, String name) { if (noCop == 0) return ((List)this.deletions.get(name)).size() > 0 ? (Location)((List)this.deletions.get(name)).get(((List)this.deletions.get(name)).size() - 1) : null;
/* 223 */     return ((List)this.amplifications.get(name)).size() > 0 ? (Location)((List)this.amplifications.get(name)).get(((List)this.amplifications.get(name)).size() - 1) : null;
/*     */   }
/*     */   
/* 226 */   public void addAll(Locreader loc1) { for (Iterator<Location> it = loc1.iterator(); it.hasNext();)
/* 227 */       add(new Location((Location)it.next()));
/*     */   }
/*     */   
/*     */   public void addAll(Iterator<Location> it) {
/* 231 */     while (it.hasNext())
/* 232 */       add((Location)it.next());
/*     */   }
/*     */   
/*     */   public Locreader(long lengthLim, String name) {
/* 236 */     this.lengthLimit = lengthLim;
/* 237 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int getMax()
/*     */   {
/* 249 */     return (int)getLast().max;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 254 */   int getMin() { return (int)getFirst().min; }
/*     */   
/*     */   void printPlotCode(PrintWriter pw, int i) {
/* 257 */     int maxo = getMax();
/* 258 */     int mino = getMin();
/*     */     
/* 260 */     Iterator<Location> it = iterator();
/* 261 */     if (it.hasNext()) {
/* 262 */       ((Location)it.next()).plotCode(pw, i == 0, mino, maxo, i);
/* 263 */       while (it.hasNext()) {
/* 264 */         ((Location)it.next()).plotCode(pw, false, 0, maxo, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void merge(double frac)
/*     */   {
/* 271 */     Logger.global.info("merging ");
/*     */     
/* 273 */     for (Iterator<String> it = this.keys.iterator(); it.hasNext();) {
/* 274 */       String nm = (String)it.next();
/* 275 */       merge(0, nm, frac);
/* 276 */       merge(2, nm, frac);
/*     */     }
/* 278 */     Logger.global.info("finished merging ");
/*     */   }
/*     */   
/*     */   public void mergeNames()
/*     */   {
/* 283 */     int[] ind = { 0, 2 };
/* 284 */     for (int j = 0; j < ind.length; j++) {
/* 285 */       int i = ind[j];
/* 286 */       Map<String, List<Location>> m = i == 2 ? this.amplifications : this.deletions;
/* 287 */       List<Location> list = new ArrayList();
/*     */       
/* 289 */       for (Iterator<String> it = m.keySet().iterator(); it.hasNext();) {
/* 290 */         String key = (String)it.next();
/* 291 */         List<Location> loc = (List)m.get(key);
/* 292 */         for (Iterator<Location> it1 = loc.iterator(); it1.hasNext();) {
/* 293 */           Location loc1 = (Location)it1.next();
/* 294 */           loc1.setName("");
/* 295 */           list.add(loc1);
/* 296 */           it1.remove();
/*     */         }
/* 298 */         if (loc.size() == 0) { it.remove();
/*     */         }
/*     */       }
/*     */       
/* 302 */       m.put("", list);
/* 303 */       Collections.sort(list);
/*     */     }
/* 305 */     this.keys.clear();
/* 306 */     this.keys.add("");
/*     */   }
/*     */   
/*     */   public void merge(int noCop, String name, double frac) {
/* 310 */     List<Location> ss = noCop == 0 ? (List)this.deletions.get(name) : (List)this.amplifications.get(name);
/* 311 */     if (ss.size() == 0) return;
/* 312 */     Location loc = (Location)ss.get(0);
/* 313 */     long st = -2147483648L;
/* 314 */     while (loc != null) {
/* 315 */       if (loc.min < st) throw new RuntimeException("!!");
/* 316 */       st = loc.min;
/*     */       
/* 318 */       Location nxt = growRight(loc, name, frac);
/* 319 */       loc = nxt;
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeWithObsLessThan(int thresh)
/*     */   {
/* 325 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 326 */       if (((Location)it.next()).noObs.size() < thresh) it.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public Location growRight(Location loc, String name, double frac) {
/* 331 */     if (loc == null) throw new RuntimeException("!!");
/* 332 */     List<Location> abs = loc.noCop() == 0 ? (List)this.deletions.get(name) : (List)this.amplifications.get(name);
/*     */     
/* 334 */     int index = abs.indexOf(loc);
/* 335 */     Iterator<Location> it = abs.subList(index + 1, abs.size()).iterator();
/* 336 */     Location loc1 = null;
/*     */     
/* 338 */     while (it.hasNext()) {
/* 339 */       loc1 = (Location)it.next();
/* 340 */       double overl = loc.overlaps(loc1);
/* 341 */       if (overl >= 0.0D) {
/* 342 */         if (overl >= frac * Math.min(loc.size(), loc1.size()))
/*     */         {
/* 344 */           loc.min = Math.min(loc.min, loc1.min);
/* 345 */           loc.max = Math.max(loc.max, loc1.max);
/* 346 */           loc.incrObs(loc1);
/* 347 */           it.remove();
/*     */         }
/*     */       } else
/* 350 */         return (Location)abs.get(index + 1);
/*     */     }
/* 352 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void add(Location loc)
/*     */   {
/* 358 */     if (loc.size() > this.lengthLimit)
/*     */     {
/*     */ 
/* 361 */       throw new RuntimeException(loc);
/*     */     }
/* 363 */     this.keys.add(loc.name());
/* 364 */     Map<String, List<Location>> m1 = loc.noCop() == 0 ? this.deletions : this.amplifications;
/* 365 */     if (!m1.containsKey(loc.name())) {
/* 366 */       this.deletions.put(loc.name(), new ArrayList());
/* 367 */       this.amplifications.put(loc.name(), new ArrayList());
/*     */     }
/* 369 */     List<Location> abs = (List)m1.get(loc.name());
/*     */     
/* 371 */     abs.add(loc);
/*     */   }
/*     */   
/*     */   public Iterator<Location> iterator()
/*     */   {
/* 376 */     final Iterator<String> it = this.keys.iterator();
/* 377 */     new Iterator()
/*     */     {
/*     */       Iterator<Location> current;
/*     */       
/*     */       Iterator<Location> prev;
/*     */       
/*     */       private void getNextIt()
/*     */       {
/* 385 */         while ((it.hasNext()) && ((this.current == null) || (!this.current.hasNext())))
/* 386 */           this.current = Locreader.this.iterator((String)it.next());
/*     */       }
/*     */       
/*     */       public boolean hasNext() {
/* 390 */         return (this.current != null) && (this.current.hasNext());
/*     */       }
/*     */       
/*     */       public Location next() {
/* 394 */         Location res = (Location)this.current.next();
/* 395 */         this.prev = this.current;
/* 396 */         if (!this.current.hasNext()) getNextIt();
/* 397 */         return res;
/*     */       }
/*     */       
/*     */       public void remove() {
/* 401 */         this.prev.remove();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */   public Iterator<Location> iterator(String name)
/*     */   {
/* 409 */     Collection<Location> del = (Collection)this.deletions.get(name);
/* 410 */     if (del == null) del = (Collection)Arrays.asList(new Location[0]);
/* 411 */     Collection<Location> ampl = (Collection)this.amplifications.get(name);
/* 412 */     if (ampl == null) ampl = (Collection)Arrays.asList(new Location[0]);
/* 413 */     final Iterator<Location> it1 = del.iterator();
/* 414 */     final Iterator<Location> it2 = ampl.iterator();
/* 415 */     new Iterator() {
/* 416 */       boolean use1 = true;
/*     */       
/* 418 */       public boolean hasNext() { return (it1.hasNext()) || (it2.hasNext()); }
/*     */       
/*     */       public Location next()
/*     */       {
/* 422 */         if (!this.use1) return (Location)it2.next();
/* 423 */         if (it1.hasNext()) { return (Location)it1.next();
/*     */         }
/* 425 */         this.use1 = false;
/* 426 */         return (Location)it2.next();
/*     */       }
/*     */       
/*     */       public void remove()
/*     */       {
/* 431 */         if (this.use1) it1.remove(); else {
/* 432 */           it2.remove();
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/* 438 */   public List<Location> get(String name, int noCop) { return noCop == 0 ? (List)this.deletions.get(name) : (List)this.amplifications.get(name); }
/*     */   
/*     */   public Iterator<Location> iterator(String name, int noCop) {
/* 441 */     Collection<Location> del = get(name, noCop);
/*     */     
/* 443 */     if (del == null) return Arrays.asList(new Location[0]).iterator();
/* 444 */     return del.iterator();
/*     */   }
/*     */   
/* 447 */   public void thin(int thres) { for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 448 */       Location nxt = (Location)it.next();
/* 449 */       if (nxt.noObs.size() < thres) it.remove();
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Integer> getLocs() {
/* 454 */     List<Integer> l = new ArrayList();
/* 455 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 456 */       Location lo = (Location)it.next();
/* 457 */       if (lo.min != lo.max) throw new RuntimeException("!!");
/* 458 */       l.add(Integer.valueOf((int)lo.min));
/*     */     }
/* 460 */     return l;
/*     */   }
/*     */   
/* 463 */   public Location overlaps(Location li, int thresh) { for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 464 */       Location loc = (Location)it.next();
/* 465 */       if (loc.overlaps(li) > thresh) return loc;
/*     */     }
/* 467 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Location contains(int pos, int thresh)
/*     */   {
/* 473 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 474 */       Location loc = (Location)it.next();
/* 475 */       if (loc.overlaps(pos) > thresh) return loc;
/*     */     }
/* 477 */     return null;
/*     */   }
/*     */   
/*     */   public void setChr(String chr)
/*     */   {
/* 482 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 483 */       Location loc = (Location)it.next();
/* 484 */       loc.chr = chr;
/*     */     }
/*     */   }
/*     */   
/*     */   public void restrict()
/*     */   {
/* 490 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 491 */       Location loc = (Location)it.next();
/* 492 */       SortedSet<Integer> ss = noPosIn(loc);
/* 493 */       if (ss.size() > 0) {
/* 494 */         loc.min = ((Integer)ss.first()).intValue();
/* 495 */         loc.max = ((Integer)ss.last()).intValue();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/Locreader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */