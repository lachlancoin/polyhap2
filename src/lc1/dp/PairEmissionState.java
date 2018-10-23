/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import lc1.dp.genotype.HaplotypeEmissionState;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PairEmissionState
/*     */   extends CompoundState
/*     */ {
/*  17 */   public static Permutations perm = new Permutations();
/*     */   public final EmissionState[] dist;
/*     */   String name1;
/*     */   
/*  21 */   public static boolean isEqualGenotype(ComparableArray comp1, ComparableArray comp2) { if (comp1.size() != comp2.size()) { return false;
/*     */     }
/*  23 */     ComparableArray exemplar1 = (ComparableArray)perm.get(comp1).get(0);
/*  24 */     ComparableArray exemplar2 = (ComparableArray)perm.get(comp2).get(0);
/*  25 */     return exemplar1.compareTo(exemplar2) == 0;
/*     */   }
/*     */   
/*     */   public Object clone(State pseudo)
/*     */   {
/*  30 */     return new PairEmissionState(copy(this.dist, ((PairEmissionState)pseudo).dist), this.parentModel);
/*     */   }
/*     */   
/*     */   public String toString(int i)
/*     */   {
/*  35 */     StringBuffer sb = new StringBuffer(this.dist[0].toString(i));
/*  36 */     for (int j = 1; j < this.dist.length; j++) {
/*  37 */       sb.append(this.dist[j].toString(i));
/*     */     }
/*  39 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected static List<EmissionState> copy(EmissionState[] em, EmissionState[] pseudo) {
/*  43 */     EmissionState[] res = new EmissionState[em.length];
/*  44 */     for (int k = 0; k < res.length; k++) {
/*  45 */       res[k] = ((EmissionState)em[k].clone(pseudo[k]));
/*     */     }
/*  47 */     return Arrays.asList(res);
/*     */   }
/*     */   
/*     */   public Object mostLikely(int pos) {
/*  51 */     Object[] mostLikely = new Object[this.dist.length];
/*  52 */     for (int j = 0; j < mostLikely.length; j++) {
/*  53 */       mostLikely[j] = ((HaplotypeEmissionState)this.dist[j]).mostLikely(pos);
/*     */     }
/*  55 */     return mostLikely;
/*     */   }
/*     */   
/*     */   static String getConcatenation(Iterator<EmissionState> states) {
/*  59 */     StringBuffer sb = new StringBuffer();
/*  60 */     while (states.hasNext())
/*     */     {
/*  62 */       sb.append(((EmissionState)states.next()).getName());
/*  63 */       if (states.hasNext()) sb.append("_");
/*     */     }
/*  65 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   public static List<ComparableArray> decompose(ComparableArray key, int noCopies)
/*     */   {
/*  71 */     return perm.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */   public String getEmissionName()
/*     */   {
/*  77 */     return this.name1;
/*     */   }
/*     */   
/*     */   protected PairEmissionState(List<EmissionState> dist, PairMarkovModel parent)
/*     */   {
/*  82 */     super(getConcatenation(dist.iterator()), 1);
/*  83 */     this.parentModel = parent;
/*  84 */     this.noCopies = dist.size();
/*  85 */     this.dist = ((EmissionState[])dist.toArray(new EmissionState[0]));
/*  86 */     SortedSet<EmissionState> distSet = new TreeSet(dist);
/*  87 */     this.name1 = getConcatenation(distSet.iterator());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setRandom(double u, boolean restart)
/*     */   {
/*  94 */     for (int i = 0; i < this.dist.length; i++) {
/*  95 */       this.dist[i].setRandom(u, restart);
/*     */     }
/*     */   }
/*     */   
/*     */   public void print(PrintWriter pw, String prefix) {
/* 100 */     for (int i = 0; i < this.dist.length; i++) {
/* 101 */       pw.println(prefix + " " + this.dist[i]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   final int noCopies;
/*     */   protected final PairMarkovModel parentModel;
/*     */   public double score(int key1, int i)
/*     */   {
/* 110 */     ComparableArray key = (ComparableArray)this.parentModel.emissionStateSpace.get(key1);
/* 111 */     double score = 0.0D;
/* 112 */     int[] num_copies = this.parentModel.num_copies;
/* 113 */     List<ComparableArray> l = decompose(key, this.noCopies);
/* 114 */     for (Iterator<ComparableArray> poss = l.iterator(); poss.hasNext();)
/*     */     {
/* 116 */       Iterator<Comparable> obj = ((ComparableArray)poss.next()).iterator();
/* 117 */       double sc = this.dist[0].score(this.parentModel.m1[num_copies[0]].getEmissionStateSpaceIndex(obj.next()), i);
/* 118 */       for (int j = 1; obj.hasNext(); j++) {
/* 119 */         sc *= this.dist[j].score(this.parentModel.m1[num_copies[j]].getEmissionStateSpaceIndex(obj.next()), i);
/*     */       }
/* 121 */       score += sc;
/*     */     }
/*     */     
/* 124 */     return score;
/*     */   }
/*     */   
/*     */ 
/*     */   public double KLDistance(EmissionState st)
/*     */   {
/* 130 */     PairEmissionState ges = (PairEmissionState)st;
/* 131 */     double tot = 0.0D;
/* 132 */     for (int j = 0; j < this.dist.length; j++) {
/* 133 */       tot += this.dist[j].KLDistance(ges.dist[j]) + this.dist[j].KLDistance(ges.dist[j]);
/*     */     }
/* 135 */     return tot / (2.0D * this.dist.length);
/*     */   }
/*     */   
/*     */   public void initialiseCounts()
/*     */   {
/* 140 */     for (int j = 0; j < this.dist.length; j++) {
/* 141 */       this.dist[j].initialiseCounts();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Comparable[] sample(int i)
/*     */   {
/* 148 */     Comparable[] sample = new Comparable[this.dist.length];
/* 149 */     for (int j = 0; j < this.dist.length; j++) {
/* 150 */       sample[j] = ((Comparable)this.dist[j].sample(i));
/*     */     }
/* 152 */     return sample;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(int obj_index, Double value, int i)
/*     */   {
/* 160 */     List<ComparableArray> l = decompose((ComparableArray)this.parentModel.getEmissionStateSpace().get(obj_index), this.noCopies);
/* 161 */     double[] prob = new double[l.size()];
/* 162 */     double sum = 0.0D;
/* 163 */     int[] num_copies = this.parentModel.num_copies;
/* 164 */     for (int j = 0; j < prob.length; j++) {
/* 165 */       Iterator<Comparable> obj = ((ComparableArray)l.get(j)).iterator();
/* 166 */       prob[j] = this.dist[0].score(this.parentModel.m1[num_copies[0]].getEmissionStateSpaceIndex(obj.next()), i);
/* 167 */       for (int k = 1; obj.hasNext(); k++) {
/* 168 */         prob[j] *= this.dist[k].score(this.parentModel.m1[num_copies[k]].getEmissionStateSpaceIndex(obj.next()), i);
/*     */       }
/* 170 */       sum += prob[j];
/*     */     }
/* 172 */     for (int j = 0; j < prob.length; j++) {
/* 173 */       double prob_j = value.doubleValue() * (sum == 0.0D ? 0.0D : prob[j] / sum);
/* 174 */       Iterator<Comparable> obj = ((ComparableArray)l.get(j)).iterator();
/* 175 */       for (int k = 0; obj.hasNext(); k++) {
/* 176 */         this.dist[k].addCount(this.parentModel.m1[num_copies[k]].getEmissionStateSpaceIndex(obj.next()), Double.valueOf(prob_j), i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void validate() throws Exception
/*     */   {
/* 183 */     for (int k = 0; k < this.dist.length; k++) {
/* 184 */       this.dist[k].validate();
/*     */     }
/* 186 */     this.lengthDistrib.validate();
/*     */   }
/*     */   
/*     */ 
/*     */   public EmissionState[] getMemberStates()
/*     */   {
/* 192 */     return this.dist;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/PairEmissionState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */