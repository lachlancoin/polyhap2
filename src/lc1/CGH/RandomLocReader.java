/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public class RandomLocReader extends Locreader
/*     */ {
/*     */   List<String> keys;
/*     */   boolean randomLoc;
/*     */   
/*     */   public RandomLocReader(Locreader data, List<Integer> locs)
/*     */   {
/*  15 */     super(Long.MAX_VALUE, "");
/*  16 */     this.randomLoc = AberationFinder.randomLoc;
/*  17 */     Logger.global.info("making random");
/*  18 */     this.keys = new java.util.ArrayList(data.keys);
/*  19 */     Iterator<Location> it; for (Iterator<String> it1 = data.keys.iterator(); it1.hasNext(); 
/*     */         
/*  21 */         it.hasNext())
/*     */     {
/*  20 */       String key = (String)it1.next();
/*  21 */       it = data.iterator(key); continue;
/*  22 */       Location loc = (Location)it.next();
/*     */       
/*     */ 
/*  25 */       Location loca = 
/*  26 */         this.randomLoc ? 
/*  27 */         getRandom1(loc.chr, locs, loc) : 
/*  28 */         getRandom(loc.chr, locs, loc);
/*     */       
/*     */ 
/*  31 */       add(loca);
/*     */     }
/*     */     
/*     */ 
/*  35 */     sort();
/*  36 */     Logger.global.info("done making random"); }
/*     */   
/*  38 */   static Random rand = new Random();
/*     */   
/*  40 */   private Location getRandom(String chr, List<Integer> locs, Location templ) { int noCop = templ.noCop();
/*  41 */     int len = (int)templ.size();
/*  42 */     int[] len1 = getStartEnd(templ, locs);
/*     */     
/*     */ 
/*  45 */     int min = 0;
/*  46 */     int max = locs.size();
/*  47 */     if (max < min) throw new RuntimeException("!!");
/*  48 */     int r = rand.nextInt(max - min);
/*  49 */     int st = ((Integer)locs.get(r + min)).intValue() - len1[1];
/*  50 */     int end = st + len - 1;
/*  51 */     int noSnps = 0;
/*  52 */     for (int i = 0; i < locs.size(); i++) {
/*  53 */       if ((((Integer)locs.get(i)).intValue() >= st) && (((Integer)locs.get(i)).intValue() <= end)) noSnps++;
/*     */     }
/*  55 */     Location loc = new Location(chr, st, end);
/*  56 */     loc.setNoCop(noCop);
/*     */     
/*  58 */     loc.setName((String)this.keys.get(rand.nextInt(this.keys.size())));
/*  59 */     loc.setNoSnps(noSnps);
/*  60 */     return loc;
/*     */   }
/*     */   
/*     */   private int[] getStartEnd(Location templ, List<Integer> locs)
/*     */   {
/*  65 */     int cnt = 0;
/*  66 */     int top = 0;
/*  67 */     int bottom = 0;
/*  68 */     boolean first = true;
/*  69 */     for (int i = 0; i < locs.size(); i++)
/*     */     {
/*  71 */       if (((Integer)locs.get(i)).intValue() >= templ.min) {
/*  72 */         if (first) {
/*  73 */           bottom = ((Integer)locs.get(i)).intValue() - (int)templ.min;
/*  74 */           first = false;
/*     */         }
/*  76 */         cnt++;
/*     */       }
/*  78 */       if ((i < locs.size() - 1) && (((Integer)locs.get(i + 1)).intValue() > templ.max)) {
/*  79 */         top = (int)templ.max - ((Integer)locs.get(i)).intValue();
/*  80 */         break;
/*     */       }
/*     */     }
/*     */     
/*  84 */     return new int[] { cnt, bottom, top };
/*     */   }
/*     */   
/*     */   private Location getRandom1(String chr, List<Integer> locs, Location templ) {
/*  88 */     int noCop = templ.noCop();
/*  89 */     int len = (int)templ.size();
/*  90 */     int min = ((Integer)locs.get(0)).intValue();
/*  91 */     int max = ((Integer)locs.get(locs.size() - 1)).intValue() - len;
/*  92 */     int r = rand.nextInt(max - min);
/*  93 */     int st = r + min;
/*  94 */     int end = st + len - 1;
/*     */     
/*  96 */     Location loc = new Location(chr, st, end);
/*  97 */     loc.setNoCop(noCop);
/*     */     
/*  99 */     loc.setName((String)this.keys.get(rand.nextInt(this.keys.size())));
/* 100 */     return loc;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/RandomLocReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */