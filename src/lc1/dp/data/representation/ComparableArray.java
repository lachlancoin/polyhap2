/*     */ package lc1.dp.data.representation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ComparableArray
/*     */   implements Comparable, Serializable
/*     */ {
/*     */   private final List<Comparable> elements;
/*     */   final boolean order_known;
/*     */   
/*     */   public Comparable getReal(int j)
/*     */   {
/*  24 */     return get(j);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/*  33 */     return this.elements.size();
/*     */   }
/*     */   
/*  36 */   public Comparable get(int i) { return (Comparable)this.elements.get(i); }
/*     */   
/*     */   public ComparableArray(boolean b) {
/*  39 */     throw new RuntimeException("!!");
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
/*     */   public ComparableArray copy()
/*     */   {
/*  56 */     return new ComparableArray(this.elements);
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
/*     */   public void set(int i, Emiss emiss)
/*     */   {
/* 109 */     this.elements.set(i, emiss);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ComparableArray make(Comparable comp)
/*     */   {
/* 181 */     return new ComparableArray(new Comparable[] { comp });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean containsNull()
/*     */   {
/* 188 */     for (int i = 0; i < size(); i++) {
/* 189 */       if (((get(i) instanceof ComparableArray)) && (((ComparableArray)get(i)).containsNull())) return true;
/* 190 */       if (get(i).equals(Emiss.N())) return true;
/*     */     }
/* 192 */     return false;
/*     */   }
/*     */   
/*     */   public int countNull() {
/* 196 */     int count = 0;
/* 197 */     for (int i = 0; i < size(); i++) {
/* 198 */       if ((get(i) instanceof ComparableArray)) { count += ((ComparableArray)get(i)).countNull();
/* 199 */       } else if (get(i).equals(Emiss.N())) count++;
/*     */     }
/* 201 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addObjectsRecursive(List<Comparable> l)
/*     */   {
/* 207 */     for (Iterator<Comparable> it = iterator(); it.hasNext();) {
/* 208 */       Comparable nxt = (Comparable)it.next();
/* 209 */       if ((nxt instanceof ComparableArray)) { ((ComparableArray)nxt).addObjectsRecursive(l);
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 218 */         l.add(nxt);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Iterator<Comparable> iterator() {
/* 224 */     return this.elements.iterator();
/*     */   }
/*     */   
/* 227 */   public static ComparableArray make(Comparable ma, Comparable ma2) { return new ComparableArray(new Comparable[] { ma, ma2 }); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComparableArray(Comparable[] objects)
/*     */   {
/* 234 */     this.elements = new ArrayList(objects.length);
/* 235 */     for (int i = 0; i < objects.length; i++) {
/* 236 */       this.elements.add(objects[i]);
/*     */     }
/* 238 */     this.order_known = (objects[0] instanceof ComparableArray);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ComparableArray(List name)
/*     */   {
/* 247 */     this((Comparable[])name.toArray(new Comparable[0]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 252 */   public ComparableArray(ComparableArray obj) { this(obj.elements); }
/*     */   
/*     */   public void flatten(List<Comparable> l) {
/* 255 */     for (Iterator<Comparable> it = iterator(); it.hasNext();) {
/* 256 */       Comparable obj = (Comparable)it.next();
/* 257 */       if ((obj instanceof ComparableArray)) {
/* 258 */         ((ComparableArray)obj).flatten(l);
/*     */       } else
/* 260 */         l.add(obj);
/*     */     }
/*     */   }
/*     */   
/*     */   public int noCopies(boolean expandEmiss) {
/* 265 */     int no_cop = 0;
/* 266 */     for (Iterator it = iterator(); it.hasNext();) {
/* 267 */       Object obj = it.next();
/* 268 */       if ((obj instanceof ComparableArray)) {
/* 269 */         no_cop += ((ComparableArray)obj).noCopies(expandEmiss);
/*     */       }
/* 271 */       else if ((obj instanceof Integer)) {
/* 272 */         no_cop++;
/*     */       }
/*     */       else {
/* 275 */         no_cop += (expandEmiss ? ((Emiss)obj).noCopies() : 1);
/*     */       }
/*     */     }
/* 278 */     return no_cop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void shuffle()
/*     */   {
/* 287 */     if ((size() > 0) && ((get(0) instanceof ComparableArray))) {
/* 288 */       for (Iterator it = iterator(); it.hasNext();) {
/* 289 */         ((ComparableArray)it.next()).shuffle();
/*     */       }
/*     */       
/*     */     } else {
/* 293 */       Collections.shuffle(this.elements);
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
/*     */   public boolean needsPhasing()
/*     */   {
/* 310 */     if ((get(0) instanceof ComparableArray)) {
/* 311 */       for (int i = 0; i < size(); i++) {
/* 312 */         if (((ComparableArray)get(i)).needsPhasing()) return true;
/*     */       }
/* 314 */       return false;
/*     */     }
/*     */     
/* 317 */     for (int i = 1; i < size(); i++) {
/* 318 */       if (get(i) != get(0)) return true;
/*     */     }
/* 320 */     return false;
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
/*     */   public boolean orderKnown()
/*     */   {
/* 374 */     return this.order_known;
/*     */   }
/*     */   
/*     */   public boolean isNested() {
/* 378 */     return get(0) instanceof ComparableArray;
/*     */   }
/*     */   
/*     */   public int homoCount() {
/* 382 */     if (isNested()) {
/* 383 */       int res = 0;
/* 384 */       for (int i = 0; i < size(); i++) {
/* 385 */         res += ((ComparableArray)get(i)).homoCount();
/*     */       }
/* 387 */       return res;
/*     */     }
/*     */     
/* 390 */     for (int i = 1; i < size(); i++) {
/* 391 */       if (get(i) != get(0)) {
/* 392 */         return 0;
/*     */       }
/*     */     }
/* 395 */     return 1;
/*     */   }
/*     */   
/*     */   public void setAsBoolean(Comparable all)
/*     */   {
/* 400 */     for (int k = 0; k < size(); k++) {
/* 401 */       Comparable nxt = get(k);
/* 402 */       if ((nxt instanceof ComparableArray)) {
/* 403 */         ((ComparableArray)nxt).setAsBoolean(all);
/*     */       }
/*     */       else {
/* 406 */         if (!nxt.equals(Emiss.N())) {
/* 407 */           if (nxt.equals(all)) {
/* 408 */             nxt = Emiss.b();
/*     */           }
/*     */           else {
/* 411 */             nxt = Emiss.a();
/*     */           }
/*     */         }
/* 414 */         this.elements.set(k, nxt);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void convertHemizygousHomozygous()
/*     */   {
/* 421 */     if (isNested()) {
/* 422 */       for (int j = 0; j < size(); j++) {
/* 423 */         ((ComparableArray)get(j)).convertHemizygousHomozygous();
/*     */       }
/*     */     }
/*     */     else {
/* 427 */       int countFalse = 0;
/* 428 */       int countTrue = 0;
/* 429 */       int countNull = countNull();
/* 430 */       if ((countNull > 0) && (countNull < size())) {
/* 431 */         for (int j = 0; j < size(); j++) {
/* 432 */           if (get(j).equals(Emiss.b())) { countTrue++;
/* 433 */           } else if (get(j) == Emiss.a()) { countFalse++;
/* 434 */           } else if (get(j) != Emiss.N()) throw new RuntimeException("!!");
/*     */         }
/* 436 */         Emiss val = countTrue >= countFalse ? Emiss.b() : Emiss.a();
/* 437 */         for (int j = 0; j < size(); j++) {
/* 438 */           if (get(j) == Emiss.N()) { set(j, val);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addError(double error)
/*     */   {
/* 448 */     if (isNested()) {
/* 449 */       for (int j = 0; j < size(); j++) {
/* 450 */         ((ComparableArray)get(j)).addError(error);
/*     */       }
/*     */       
/*     */     } else {
/* 454 */       for (int j = 0; j < size(); j++) {
/* 455 */         double ra = Constants.rand.nextDouble();
/* 456 */         if ((ra < error) && (get(j) != Emiss.N())) {
/* 457 */           set(j, get(j) == Emiss.b() ? Emiss.a() : Emiss.b());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 465 */     return this.elements.toString();
/*     */   }
/*     */   
/*     */   public String toStringShort() {
/* 469 */     StringBuffer sb = new StringBuffer();
/* 470 */     for (int i = 0; i < size(); i++) {
/* 471 */       Object obj = get(i);
/* 472 */       sb.append((obj instanceof Emiss) ? ((Emiss)obj).toStringShort() : obj);
/*     */     }
/* 474 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String toStringPrint() {
/* 478 */     StringBuffer sb = new StringBuffer();
/* 479 */     for (int i = 0; i < size(); i++) {
/* 480 */       Object obj = get(i);
/* 481 */       sb.append((obj instanceof Emiss) ? ((Emiss)obj).toStringPrint() : obj);
/*     */     }
/* 483 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public int copyNumber() {
/* 487 */     int no = 0;
/* 488 */     for (int i = 0; i < size(); i++) {
/* 489 */       no += ((Emiss)get(i)).noCopies();
/*     */     }
/* 491 */     return no;
/*     */   }
/*     */   
/*     */   public double noB() {
/* 495 */     double no = 0.0D;
/* 496 */     for (int i = 0; i < size(); i++) {
/* 497 */       no += ((Emiss)get(i)).noB();
/*     */     }
/* 499 */     return no;
/*     */   }
/*     */   
/*     */   public void add(Comparable o) {
/* 503 */     this.elements.add(o);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getHaplotypeString()
/*     */   {
/* 509 */     return getString(this.elements);
/*     */   }
/*     */   
/*     */   public String getHaploPairString() {
/* 513 */     List<Comparable> orig1 = new ArrayList(this.elements);
/*     */     
/* 515 */     Collections.sort(orig1);
/* 516 */     return getString(orig1);
/*     */   }
/*     */   
/*     */   public static String getString(List<Comparable> orig1)
/*     */   {
/* 521 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 523 */     for (int i = 0; i < orig1.size(); i++) {
/* 524 */       Comparable comp = (Comparable)orig1.get(i);
/*     */       
/* 526 */       if ((comp instanceof ComparableArray)) {
/* 527 */         throw new RuntimeException("!!");
/*     */       }
/*     */       
/* 530 */       if ((comp instanceof Integer)) {
/* 531 */         sb.append(Integer.toString(((Integer)comp).intValue(), Constants.radix()));
/*     */       }
/*     */       else {
/* 534 */         sb.append(((Emiss)comp).toStringPrint());
/*     */       }
/* 536 */       if (i < orig1.size() - 1) { sb.append(",");
/*     */       }
/*     */     }
/* 539 */     String hapString = sb.toString();
/* 540 */     char[] ch = hapString.replaceAll("_", "").toCharArray();
/*     */     
/* 542 */     return new String(ch);
/*     */   }
/*     */   
/*     */   public String getGenotypeString() {
/* 546 */     StringBuffer sb = new StringBuffer();
/*     */     
/* 548 */     for (int i = 0; i < size(); i++) {
/* 549 */       Comparable comp = (Comparable)this.elements.get(i);
/*     */       
/* 551 */       if ((comp instanceof ComparableArray)) {
/* 552 */         sb.append(((ComparableArray)comp).getGenotypeString());
/*     */       }
/* 554 */       else if ((comp instanceof Integer)) {
/* 555 */         sb.append(Integer.toString(((Integer)comp).intValue(), Constants.radix()));
/*     */       }
/*     */       else {
/* 558 */         sb.append(((Emiss)comp).toStringShort());
/*     */       }
/*     */     }
/*     */     
/* 562 */     String hapString = sb.toString();
/* 563 */     char[] ch = hapString.replaceAll("_", "").replaceAll(",", "").toCharArray();
/* 564 */     Arrays.sort(ch);
/* 565 */     return new String(ch);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 574 */   public List elements() { return this.elements; }
/*     */   
/*     */   public Comparable remove(int i) {
/* 577 */     Comparable res = (Comparable)this.elements.remove(i);
/*     */     
/* 579 */     return res;
/*     */   }
/*     */   
/* 582 */   public void add(int i, Comparable newFirstPosition) { this.elements.add(i, newFirstPosition); }
/*     */   
/*     */ 
/*     */   public boolean contains(Comparable comparable)
/*     */   {
/* 587 */     return this.elements.contains(comparable);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 593 */     return this.elements.equals(((ComparableArray)obj).elements);
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
/*     */   public int hashCode()
/*     */   {
/* 608 */     return this.elements.hashCode();
/*     */   }
/*     */   
/*     */   public int compareTo(Object o) {
/* 612 */     ComparableArray compA = (ComparableArray)o;
/* 613 */     if (compA.size() != size()) { return size() < compA.size() ? -1 : 1;
/*     */     }
/* 615 */     for (int i = 0; i < size(); i++) {
/* 616 */       int res = get(i).compareTo(compA.get(i));
/* 617 */       if (res != 0) { return res;
/*     */       }
/*     */     }
/* 620 */     return 0;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/ComparableArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */