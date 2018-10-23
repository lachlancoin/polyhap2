/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class AbstractAberatiionReader extends Locreader
/*     */ {
/*     */   protected int[] col;
/*     */   
/*     */   public AbstractAberatiionReader(long lengthLim, String name)
/*     */   {
/*  18 */     super(lengthLim, name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  24 */   public static Iterator<String[]> getIterator(BufferedReader br)
/*  24 */     throws Exception { new Iterator() {
/*  25 */       String[] nxt = AbstractAberatiionReader.this.readLine().split("\\t");
/*     */       
/*  27 */       public boolean hasNext() { return (this.nxt != null) && (this.nxt.length > 0) && ((this.nxt.length > 1) || (this.nxt[0].length() > 1)); }
/*     */       
/*     */       public String[] next() {
/*  30 */         String[] res = this.nxt;
/*     */         try {
/*  32 */           String str = AbstractAberatiionReader.this.readLine();
/*  33 */           if (str != null) {
/*  34 */             this.nxt = str.trim().split("\\t");
/*     */           }
/*     */           else
/*  37 */             this.nxt = null;
/*     */         } catch (Exception exc) {
/*  39 */           exc.printStackTrace();System.exit(0); }
/*  40 */         return res; }
/*     */       
/*     */       public void remove() {} }; }
/*     */   
/*     */   public abstract String getName(String[] paramArrayOfString);
/*     */   
/*     */   public abstract String getChr(String[] paramArrayOfString);
/*     */   
/*     */   public abstract String getStart(String[] paramArrayOfString);
/*     */   public abstract String getEnd(String[] paramArrayOfString);
/*     */   public abstract int getNoProbes(String[] paramArrayOfString);
/*  51 */   public String getProbeId(String[] str) { return ""; }
/*     */   
/*     */   public abstract double getNoCopy(String[] paramArrayOfString);
/*     */   
/*     */   public boolean exclude(String[] str) {
/*  56 */     return Math.abs(getNoCopy(str)) < AberationFinder.lowIntens;
/*     */   }
/*     */   
/*     */   public abstract String[] getCols();
/*     */   
/*     */   public void initialise(BufferedReader br, String chromosome, Location region, int first, int second, String name, Set<String> indiv) throws Exception {
/*  62 */     initialise(getIterator(br), chromosome, region, first, second, name, indiv);
/*     */   }
/*     */   
/*     */   public void initialise(File dir, String chromosome, Location region, int first, int second, String name, Set<String> indiv) throws Exception
/*     */   {
/*  67 */     File f = new File(dir, name);
/*     */     BufferedReader br;
/*  69 */     BufferedReader br; if (f.exists()) {
/*  70 */       br = new BufferedReader(new java.io.InputStreamReader(new java.io.FileInputStream(f)));
/*     */     }
/*     */     else
/*     */     {
/*  74 */       f = new File(dir, name + ".gz");
/*  75 */       br = new BufferedReader(new java.io.InputStreamReader(new java.util.zip.GZIPInputStream(new java.io.FileInputStream(f))));
/*     */     }
/*  77 */     initialise(br, chromosome, region, first, second, name, indiv);
/*  78 */     br.close();
/*     */   }
/*     */   
/*     */   public void initialise(Iterator<String[]> it, String chromosome, Location region, int firstCount, int secondCount, String name, Set<String> indiv) throws Exception
/*     */   {
/*  83 */     for (int i = 0; i < firstCount; i++)
/*     */     {
/*  85 */       System.err.println("skipping " + Arrays.asList((String[])it.next()));
/*     */     }
/*  87 */     fillMap((String[])it.next(), getCols());
/*  88 */     for (int i = 0; i < secondCount; i++) {
/*  89 */       System.err.println("skipping " + Arrays.asList((String[])it.next()));
/*     */     }
/*  91 */     while (it.hasNext()) {
/*  92 */       String[] str = (String[])it.next();
/*     */       
/*  94 */       String chrom = getChr(str);
/*  95 */       if ((chromosome == null) || (chrom.equals(chromosome)))
/*     */       {
/*     */ 
/*  98 */         if (exclude(str)) {
/*  99 */           java.util.logging.Logger.global.info("excluding " + Arrays.asList(str));
/*     */         }
/*     */         else {
/* 102 */           long min = Long.parseLong(getStart(str));
/* 103 */           long max = Long.parseLong(getEnd(str));
/*     */           
/* 105 */           double noCop = 
/* 106 */             getNoCopy(str);
/*     */           
/* 108 */           Location location = max > min ? new Location(chrom, min, max) : new Location(chrom, max, min);
/* 109 */           if ((indiv == null) || (indiv.contains(getName(str)))) {
/* 110 */             location.setName(getName(str));
/* 111 */             if (noCop == 0.0D) location.setNoCop(-1); else
/* 112 */               location.setNoCop(noCop > 0.0D ? 2 : 0);
/* 113 */             location.setLogL(noCop);
/* 114 */             location.setNoSnps(getNoProbes(str));
/* 115 */             location.setProbeId(getProbeId(str));
/* 116 */             if ((region == null) || (region.overlaps(location) > 0.0D)) {
/* 117 */               add(location);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 123 */     sort();
/*     */   }
/*     */   
/*     */   protected void fillMap(String[] header, String[] cols) {
/* 127 */     this.col = new int[cols.length];
/* 128 */     Arrays.fill(this.col, -1);
/* 129 */     for (int j = 0; j < cols.length; j++) {
/* 130 */       for (int i = 0; i < header.length; i++) {
/* 131 */         if (header[i].startsWith(cols[j])) {
/* 132 */           this.col[j] = i;
/* 133 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addAll(Collection<Location> probesinMultiRegion) {
/* 140 */     for (Iterator<Location> it = probesinMultiRegion.iterator(); it.hasNext();) {
/* 141 */       add((Location)it.next());
/*     */     }
/*     */   }
/*     */   
/*     */   public void restrictEnds(Locreader p244k) {
/* 146 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 147 */       Location loc = (Location)it.next();
/*     */       
/* 149 */       List<Location> l = loc.getStartOverlaps(p244k.iterator(), 0.0D);
/* 150 */       java.util.Collections.sort(l);
/* 151 */       if (l.size() == 0) { throw new RuntimeException("!!");
/*     */       }
/* 153 */       loc.min = ((Location)l.get(0)).min;
/* 154 */       loc.max = ((Location)l.get(l.size() - 1)).max;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void getNoProbes(Locreader locsL)
/*     */   {
/* 161 */     for (Iterator<Location> it = iterator(); it.hasNext();) {
/* 162 */       Location loc = (Location)it.next();
/* 163 */       List<Location> l = loc.getStartOverlaps(locsL.iterator(), 0.0D);
/*     */       
/* 165 */       loc.noSnps = l.size();
/* 166 */       if (l.size() == 0) {
/* 167 */         System.err.println("removed " + loc);
/* 168 */         it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/AbstractAberatiionReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */