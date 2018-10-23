/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Location implements Comparable
/*     */ {
/*     */   public String chr;
/*     */   public long min;
/*     */   public long max;
/*     */   
/*     */   public List<Location> getStartOverlaps(Iterator<Location> it, double perc)
/*     */   {
/*  16 */     List<Location> res = new ArrayList();
/*  17 */     while (it.hasNext()) {
/*  18 */       Location nxt = (Location)it.next();
/*  19 */       double overl = overlaps(nxt);
/*     */       
/*  21 */       if (overl > perc * Math.min(nxt.size(), size())) {
/*  22 */         res.add(nxt);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  27 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  33 */   public double logL = 0.0D;
/*  34 */   public String probeId = "";
/*     */   
/*  36 */   public void setProbeId(String id) { this.probeId = id; }
/*     */   
/*     */ 
/*  39 */   public void setLogL(double logL) { this.logL = logL; }
/*     */   
/*  41 */   private int noCop = -1;
/*  42 */   private String name = "";
/*  43 */   public List<String> noObs = new ArrayList();
/*     */   
/*  45 */   public void incrObs(Location i) { this.noObs.addAll(i.noObs); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  50 */   public long size() { return this.max - this.min + 1L; }
/*     */   
/*     */   public String toString() {
/*  53 */     StringBuffer sb = new StringBuffer();
/*  54 */     if (this.chr != "") sb.append(this.chr + ":");
/*  55 */     sb.append(this.min);
/*  56 */     if (this.max != this.min) sb.append("-" + this.max);
/*  57 */     if (this.noCop >= 0) sb.append("/" + this.noCop);
/*  58 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   java.util.Properties ann;
/*     */   
/*     */   public int[] overl1;
/*     */   
/*     */   public int getIndivOverlaps(Location loc)
/*     */   {
/*  69 */     int res = 0;
/*  70 */     for (Iterator<String> it = this.noObs.iterator(); it.hasNext();) {
/*  71 */       if (loc.noObs.contains(it.next())) res++;
/*     */     }
/*  73 */     return res;
/*     */   }
/*     */   
/*  76 */   public Location(String st) { String[] str = st.split("_");
/*     */     try {
/*  78 */       this.name = Integer.parseInt(str[2]);
/*     */     } catch (Exception exc) {
/*  80 */       this.name = Integer.parseInt(str[1]);
/*     */     }
/*  82 */     this.max = Integer.parseInt(str[(str.length - 1)]);
/*  83 */     this.min = Integer.parseInt(str[(str.length - 2)]);
/*  84 */     this.chr = Integer.parseInt(str[(str.length - 3)]);
/*     */   }
/*     */   
/*  87 */   public Location(String str, long min, long max) { this.chr = str;
/*  88 */     this.min = min;
/*  89 */     this.max = max;
/*     */   }
/*     */   
/*     */   public Location(java.util.Collection<Location> overlap)
/*     */   {
/*  94 */     Iterator<Location> it = overlap.iterator();
/*  95 */     Location first = (Location)it.next();
/*  96 */     this.noCop = first.noCop;
/*  97 */     this.min = first.min;
/*  98 */     this.max = first.max;
/*  99 */     this.chr = first.chr;
/* 100 */     while (it.hasNext()) {
/* 101 */       Location nxt = (Location)it.next();
/* 102 */       if (nxt.noCop != this.noCop) {
/* 103 */         throw new RuntimeException("!!");
/*     */       }
/* 105 */       if (nxt.min < this.min) this.min = nxt.min;
/* 106 */       if (nxt.max > this.max) this.max = nxt.max;
/*     */     }
/*     */   }
/*     */   
/* 110 */   public Location(int start, int end, String chr) { this.min = start;
/* 111 */     this.max = end;
/* 112 */     this.chr = chr;
/*     */   }
/*     */   
/* 115 */   public Location(Location loc) { this.chr = loc.chr;
/* 116 */     this.name = loc.name;
/* 117 */     this.min = loc.min;
/* 118 */     this.max = loc.max;
/* 119 */     this.noCop = loc.noCop;
/* 120 */     this.noObs = new ArrayList(loc.noObs);
/* 121 */     this.noSnps = loc.noSnps;
/*     */   }
/*     */   
/* 124 */   public boolean contains(int pos) { return (pos >= this.min) && (pos <= this.max); }
/*     */   
/*     */   public double overlaps(Location loc)
/*     */   {
/* 128 */     if (loc.chr.equals(this.chr)) {
/* 129 */       double min_interval = Math.max(this.min, loc.min);
/* 130 */       double max_interval = Math.min(this.max, loc.max);
/* 131 */       return max_interval - min_interval + 1.0D;
/*     */     }
/* 133 */     return Double.NEGATIVE_INFINITY;
/*     */   }
/*     */   
/*     */   public double overlaps(int pos) {
/* 137 */     double min_interval = Math.max(this.min, pos);
/* 138 */     double max_interval = Math.min(this.max, pos);
/* 139 */     return max_interval - min_interval + 1.0D;
/*     */   }
/*     */   
/*     */   public Location getOverlap(Location loc) {
/* 143 */     if (loc.chr.equals(this.chr)) {
/* 144 */       Location newLoc = new Location(this.chr, (int)Math.max(this.min, loc.min), 
/* 145 */         (int)Math.min(this.max, loc.max));
/* 146 */       return newLoc;
/*     */     }
/* 148 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 156 */     return compareTo(o) == 0;
/*     */   }
/*     */   
/*     */   public int compareTo(Object o) {
/* 160 */     Location o2 = (Location)o;
/* 161 */     int res1 = this.chr.compareTo(o2.chr);
/* 162 */     if (res1 != 0) return res1;
/* 163 */     if (this.min != o2.min) return this.min < o2.min ? -1 : 1;
/* 164 */     if (this.max != o2.max) return this.max < o2.max ? -1 : 1;
/* 165 */     if (this.noCop != o2.noCop) { return this.noCop < o2.noCop ? -1 : 1;
/*     */     }
/* 167 */     return this.name.compareTo(o2.name);
/*     */   }
/*     */   
/*     */   public void plotCode(PrintWriter pw, boolean b, int mino, int maxo, int i) {
/* 171 */     pw.print(b ? "plot" : "lines");
/* 172 */     double off = i * 0.2D;
/* 173 */     pw.print("(x = c(" + this.min + "," + this.max + "), y = c(" + off + "," + off + "), col =  " + (i + 1) + " ,lwd =" + 2);
/* 174 */     if (b) pw.print(" ,xlim = c(" + mino + "," + maxo + "), ylim = c(" + 0 + "," + 0.5D + ") ");
/* 175 */     if (b) pw.print(",xlab = \"Location\", ylab = \"Copy Number\"");
/* 176 */     if (b) pw.println(",type = \"l\")"); else {
/* 177 */       pw.println(")");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setNoCop(int noCop) {
/* 182 */     this.noCop = noCop;
/*     */   }
/*     */   
/*     */   public void setName(String nme1) {
/* 186 */     if (nme1 == null) {
/* 187 */       this.name = "";
/* 188 */       this.noObs.add(this.name);
/* 189 */       return;
/*     */     }
/*     */     
/* 192 */     this.name = 
/*     */     
/* 194 */       nme1;
/*     */     
/*     */ 
/* 197 */     this.noObs.add(nme1);
/*     */   }
/*     */   
/*     */   public int noCop()
/*     */   {
/* 202 */     return this.noCop;
/*     */   }
/*     */   
/* 205 */   public String name() { return this.name; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOverl(int[] is)
/*     */   {
/* 214 */     this.overl1 = is;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 219 */   public String toStringPrint() { return this.chr + "_" + this.min + "_" + this.max; }
/*     */   
/* 221 */   public int noSnps = 0;
/*     */   
/* 223 */   public void setNoSnps(int noSnps) { this.noSnps = noSnps; }
/*     */   
/*     */ 
/* 226 */   public int mid() { return (int)Math.floor((this.min + this.max) / 2.0D); }
/*     */   
/*     */   public void setNeg() {
/* 229 */     this.min = (-this.min);
/* 230 */     this.max = (-this.max);
/*     */   }
/*     */   
/*     */   public void print(PrintWriter regions) {
/* 234 */     regions.println(this.min + "\t" + this.max + "\t" + this.name + "\t" + this.noCop + "\t" + this.noSnps);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/Location.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */