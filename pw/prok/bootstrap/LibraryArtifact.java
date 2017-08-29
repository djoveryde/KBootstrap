/*     */ package pw.prok.bootstrap;
/*     */ 
/*     */ import java.io.File;
/*     */ import pw.prok.damask.dsl.Builder;
/*     */ import pw.prok.damask.dsl.IArtifact;
/*     */ 
/*     */ public final class LibraryArtifact
/*     */ {
/*     */   private IArtifact mArtifact;
/*     */   private final String mLocation;
/*     */   private final String mFilename;
/*     */   private File mTarget;
/*     */   
/*     */   public LibraryArtifact(String group, String name, String version, String location, String filename)
/*     */   {
/*  16 */     this(Builder.create().group(group).name(name).version(version).asArtifact(), location, filename);
/*     */   }
/*     */   
/*     */   public LibraryArtifact(String group, String name, String version)
/*     */   {
/*  21 */     this(Builder.create().group(group).name(name).version(version).asArtifact(), null, null);
/*     */   }
/*     */   
/*     */   public LibraryArtifact(IArtifact artifact) {
/*  25 */     this(artifact, null, null);
/*     */   }
/*     */   
/*     */   public LibraryArtifact(IArtifact artifact, String location, String filename) {
/*  29 */     this.mArtifact = artifact;
/*  30 */     this.mLocation = location;
/*  31 */     this.mFilename = filename;
/*     */   }
/*     */   
/*     */   public LibraryArtifact(IArtifact artifact, File target) {
/*  35 */     this(artifact, null, null);
/*  36 */     this.mTarget = target;
/*     */   }
/*     */   
/*     */   public IArtifact getArtifact() {
/*  40 */     return this.mArtifact;
/*     */   }
/*     */   
/*     */   public boolean hasLocation() {
/*  44 */     return this.mLocation != null;
/*     */   }
/*     */   
/*     */   public String getLocation() {
/*  48 */     return this.mLocation;
/*     */   }
/*     */   
/*     */   public String getRealLocation() {
/*  52 */     if (this.mLocation != null) {
/*  53 */       return compute(this.mLocation);
/*     */     }
/*  55 */     String groupId = this.mArtifact.getGroup().replace('.', '/');
/*  56 */     String artifactId = this.mArtifact.getName();
/*  57 */     String version = this.mArtifact.getVersion().toRawString();
/*  58 */     return String.format("%s/%s/%s", new Object[] { groupId, artifactId, version });
/*     */   }
/*     */   
/*     */   public boolean hasFilename() {
/*  62 */     return this.mFilename != null;
/*     */   }
/*     */   
/*     */   public String getFilename() {
/*  66 */     return this.mFilename;
/*     */   }
/*     */   
/*     */   public String getRealFilename() {
/*  70 */     if (this.mFilename != null) {
/*  71 */       return compute(this.mFilename);
/*     */     }
/*  73 */     String artifactId = this.mArtifact.getName();
/*  74 */     String version = this.mArtifact.getVersion().toRawString();
/*  75 */     String classifier = this.mArtifact.getClassifier();
/*  76 */     String extension = this.mArtifact.getExtension();
/*  77 */     classifier = (classifier != null) && (classifier.length() > 0) ? '-' + classifier : "";
/*  78 */     return String.format("%s-%s%s.%s", new Object[] { artifactId, version, classifier, extension });
/*     */   }
/*     */   
/*     */   public String compute(String s) {
/*  82 */     s = s.replace("<group>", this.mArtifact.getGroup().replace('.', '/'));
/*  83 */     s = s.replace("<artifact>", this.mArtifact.getName());
/*  84 */     s = s.replace("<version>", this.mArtifact.getVersion().toRawString());
/*  85 */     s = s.replace("<classifier>", this.mArtifact.getClassifier());
/*  86 */     s = s.replace("<extension>", this.mArtifact.getExtension());
/*  87 */     return s;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  92 */     return String.valueOf(this.mArtifact);
/*     */   }
/*     */   
/*     */   public void setArtifact(IArtifact artifact) {
/*  96 */     this.mArtifact = artifact;
/*     */   }
/*     */   
/*     */   public File getTarget(File rootDir) {
/* 100 */     if (this.mTarget != null) return this.mTarget;
/* 101 */     return new File(new File(rootDir, getRealLocation()), getRealFilename());
/*     */   }
/*     */   
/*     */   public void setTarget(File target) {
/* 105 */     this.mTarget = target;
/*     */   }
/*     */ }


/* Location:              D:\Users\djove\Desktop\KBootstrap-0.3.2.jar!\pw\prok\bootstrap\LibraryArtifact.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */