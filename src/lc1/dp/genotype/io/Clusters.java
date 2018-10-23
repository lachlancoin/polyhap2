/*     */ package lc1.dp.genotype.io;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ 
/*     */ public class Clusters
/*     */ {
/*     */   int noStates;
/*     */   
/*     */   public void compare(Clusters clusters)
/*     */   {
/*  24 */     double[] overlap = { 0.0D, 0.0D };
/*  25 */     int numpos = getNumPos();
/*  26 */     if (clusters.getNumPos() != numpos) { throw new RuntimeException("!!");
/*     */     }
/*  28 */     for (int i = 0; i <= numpos; i++) {
/*  29 */       SortedSet<Group> g1 = getClustersAt(Integer.valueOf(i));
/*  30 */       compare(clusters.getClustersAt(Integer.valueOf(i)), g1, overlap);
/*  31 */       if (g1.size() == 0) break;
/*     */     }
/*  33 */     System.err.println("total : overlap " + overlap[0] / (numpos + 1) + " : " + overlap[1] / (numpos + 1));
/*     */   }
/*     */   
/*     */   private int getNumPos() {
/*  37 */     int max = 0;
/*  38 */     for (Iterator<Group> it = this.clusters.iterator(); it.hasNext();) {
/*  39 */       List<Integer> pos = ((Group)it.next()).positions;
/*  40 */       int max_i = ((Integer)pos.get(pos.size() - 1)).intValue();
/*  41 */       if (max_i > max) {
/*  42 */         max = max_i;
/*     */       }
/*     */     }
/*  45 */     return max;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/*  50 */       Clusters cl1 = new Clusters(new File("cluster_output6_true.txt"));
/*  51 */       Clusters cl2 = new Clusters(new File("cluster_output6_false.txt"));
/*  52 */       System.err.println("train emissions as background");
/*  53 */       cl1.compare(cl2);
/*  54 */       System.err.println("fixed emissions as background");
/*  55 */       cl2.compare(cl1);
/*     */     } catch (Exception exc) {
/*  57 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public Clusters(File f) throws Exception {
/*  62 */     BufferedReader br = new BufferedReader(new FileReader(f));
/*  63 */     String st = "";
/*  64 */     while ((st = br.readLine()) != null) {
/*  65 */       String[] str = st.split(":");
/*  66 */       String[] membr = str[1].trim().substring(1, str[1].length() - 2).split(",");
/*  67 */       Group g = new Group();
/*  68 */       for (int i = 0; i < membr.length; i++) {
/*  69 */         g.add(Integer.parseInt(membr[i].trim()));
/*     */       }
/*  71 */       String[] pos = str[0].trim().substring(0, str[0].length() - 1).split(",");
/*  72 */       for (int i = 0; i < pos.length; i++) {
/*  73 */         String[] pos_i = pos[i].split("-");
/*  74 */         int start = Integer.parseInt(pos_i[0]);
/*  75 */         int end = Integer.parseInt(pos_i[1]);
/*  76 */         for (int j = start; j <= end; j++) {
/*  77 */           g.positions.add(Integer.valueOf(j));
/*     */         }
/*     */       }
/*  80 */       this.clusters.add(g);
/*     */     }
/*     */     
/*  83 */     br.close();
/*     */   }
/*     */   
/*     */   private void compare(SortedSet<Group> clustersAt, SortedSet<Group> c1, double[] overlap) {
/*  87 */     Iterator<Group> it1 = clustersAt.iterator();
/*  88 */     Iterator<Group> it2 = c1.iterator();
/*  89 */     while (it2.hasNext()) {
/*  90 */       Group g1 = it1.hasNext() ? (Group)it1.next() : null;
/*  91 */       Group g2 = (Group)it2.next();
/*  92 */       overlap[0] += g2.size();
/*  93 */       overlap[1] += g2.overlap(g1);
/*     */     }
/*     */   }
/*     */   
/*     */   public SortedSet<Group> getClustersAt(Integer i)
/*     */   {
/*  99 */     SortedSet<Group> sub_clust = new TreeSet(clustcomp);
/*     */     
/* 101 */     for (Iterator<Group> it = this.clusters.iterator(); it.hasNext();) {
/* 102 */       Group g = (Group)it.next();
/* 103 */       if (g.positions.contains(i)) {
/* 104 */         sub_clust.add(g);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 109 */     return sub_clust; }
/*     */   
/* 111 */   SortedSet<Group> clusters = new TreeSet(clustcomp);
/*     */   
/*     */   public Clusters(ArrayList<Integer[][]> res, int noStates) {
/* 114 */     this.noStates = noStates;
/* 115 */     int len = ((Integer[][])res.get(0))[0].length;
/* 116 */     for (int i = 0; i < len; i++) {
/* 117 */       Cluster clust = new Cluster(res, i);
/* 118 */       for (Iterator<Group> it = clust.values.iterator(); it.hasNext();) {
/* 119 */         Group group = (Group)it.next();
/* 120 */         if (group.size() > 0) {
/* 121 */           boolean contains = this.clusters.contains(group);
/* 122 */           if (contains) {
/* 123 */             SortedSet<Group> tail = this.clusters.tailSet(group);
/* 124 */             Group first = (Group)tail.first();
/* 125 */             if (!first.equals(group)) throw new RuntimeException("!!");
/* 126 */             first.positions.add(Integer.valueOf(i));
/*     */           }
/*     */           else {
/* 129 */             this.clusters.add(group);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class ClustComp implements Comparator<Clusters.Group> {
/*     */     public int compare(Clusters.Group l1, Clusters.Group l2) {
/* 138 */       int minSize = Math.min(l1.size(), l2.size());
/* 139 */       for (int i = 0; i < minSize; i++) {
/* 140 */         int comp = l1.get(i).compareTo(l2.get(i));
/* 141 */         if (comp != 0) return comp;
/*     */       }
/* 143 */       if (l1.size() == l2.size()) return 0;
/* 144 */       return l1.size() < l2.size() ? -1 : 1;
/*     */     } }
/*     */   
/* 147 */   static Comparator clustcomp = new ClustComp();
/*     */   
/*     */   public void print(PrintWriter pw) {
/* 150 */     for (Iterator<Group> it = this.clusters.iterator(); it.hasNext();) {
/* 151 */       ((Group)it.next()).print(pw);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   class Group
/*     */   {
/*     */     Group() {}
/*     */     
/* 160 */     Group(int pos) { this.positions.add(Integer.valueOf(pos)); }
/*     */     
/*     */     public int overlap(Group g1) {
/* 163 */       if (g1 == null) return 0;
/* 164 */       List<Integer> members1 = new ArrayList(this.members);
/* 165 */       members1.retainAll(g1.members);
/* 166 */       return members1.size();
/*     */     }
/*     */     
/* 169 */     public void print(PrintWriter pw) { StringBuffer pos = new StringBuffer();
/* 170 */       for (int i = 0; i < this.positions.size(); i++) {
/* 171 */         if ((i == 0) || (((Integer)this.positions.get(i)).intValue() - 1 != ((Integer)this.positions.get(i - 1)).intValue())) {
/* 172 */           pos.append(this.positions.get(i) + "-");
/*     */         }
/* 174 */         if ((i == this.positions.size() - 1) || (((Integer)this.positions.get(i)).intValue() + 1 != ((Integer)this.positions.get(i + 1)).intValue())) {
/* 175 */           pos.append(this.positions.get(i) + ",");
/*     */         }
/*     */       }
/* 178 */       pw.println(pos.toString() + ": " + this.members);
/*     */     }
/*     */     
/*     */     public String toString() {
/* 182 */       return this.members.toString();
/*     */     }
/*     */     
/* 185 */     public Integer get(int i) { return (Integer)this.members.get(i); }
/*     */     
/*     */ 
/* 188 */     public int size() { return this.members.size(); }
/*     */     
/* 190 */     List<Integer> positions = new ArrayList();
/* 191 */     List<Integer> members = new ArrayList();
/*     */     
/* 193 */     public boolean equals(Object obj) { return this.members.equals(((Group)obj).members); }
/*     */     
/*     */     public void add(int k) {
/* 196 */       this.members.add(Integer.valueOf(k));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   class Cluster
/*     */   {
/*     */     List<Clusters.Group> values;
/*     */     
/*     */ 
/*     */     Cluster(int res)
/*     */     {
/* 208 */       SortedMap<Integer, Clusters.Group> membership = new TreeMap();
/* 209 */       for (int j = 1; j < Clusters.this.noStates; j++) {
/* 210 */         membership.put(Integer.valueOf(j), new Clusters.Group(Clusters.this, pos));
/*     */       }
/* 212 */       for (int k = 0; k < res.size(); k++) {
/* 213 */         Integer[][] vit = (Integer[][])res.get(k);
/* 214 */         for (int j = 0; j < vit.length; j++) {
/* 215 */           Clusters.Group cluster = (Clusters.Group)membership.get(vit[j][pos]);
/* 216 */           cluster.add(k);
/*     */         }
/*     */       }
/* 219 */       this.values = new ArrayList(membership.values());
/* 220 */       Collections.sort(this.values, Clusters.clustcomp);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/Clusters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */