 package pw.prok.bootstrap;
 
 import java.io.File;
 import pw.prok.damask.dsl.Builder;
 import pw.prok.damask.dsl.IArtifact;
 
  public final class LibraryArtifact
 {
   private IArtifact mArtifact;
   private final String mLocation;
   private final String mFilename;
   private File mTarget;
   
   public LibraryArtifact(String group, String name, String version, String location, String filename)
   {
     this(Builder.create().group(group).name(name).version(version).asArtifact(), location, filename);
   }
   
   public LibraryArtifact(String group, String name, String version)
   {
     this(Builder.create().group(group).name(name).version(version).asArtifact(), null, null);
   }
   
   public LibraryArtifact(IArtifact artifact) {
     this(artifact, null, null);
   }
   
   public LibraryArtifact(IArtifact artifact, String location, String filename) {
     this.mArtifact = artifact;
     this.mLocation = location;
     this.mFilename = filename;
   }
   
   public LibraryArtifact(IArtifact artifact, File target) {
     this(artifact, null, null);
     this.mTarget = target;
   }
   
   public IArtifact getArtifact() {
     return this.mArtifact;
   }
   
   public boolean hasLocation() {
     return this.mLocation != null;
   }
   
   public String getLocation() {
     return this.mLocation;
   }
   
   public String getRealLocation() {
     if (this.mLocation != null) {
       return compute(this.mLocation);
     }
     String groupId = this.mArtifact.getGroup().replace('.', '/');
     String artifactId = this.mArtifact.getName();
     String version = this.mArtifact.getVersion().toRawString();
      return String.format("%s/%s/%s", new Object[] { groupId, artifactId, version });
   }
   
   public boolean hasFilename() {
     return this.mFilename != null;
   }
   
   public String getFilename() {
     return this.mFilename;
   }
   
   public String getRealFilename() {
     if (this.mFilename != null) {
       return compute(this.mFilename);
     }
     String artifactId = this.mArtifact.getName();
     String version = this.mArtifact.getVersion().toRawString();
     String classifier = this.mArtifact.getClassifier();
     String extension = this.mArtifact.getExtension();
     classifier = (classifier != null) && (classifier.length() > 0) ? '-' + classifier : "";
     return String.format("%s-%s%s.%s", new Object[] { artifactId, version, classifier, extension });
   }
   
   public String compute(String s) {
     s = s.replace("<group>", this.mArtifact.getGroup().replace('.', '/'));
     s = s.replace("<artifact>", this.mArtifact.getName());
     s = s.replace("<version>", this.mArtifact.getVersion().toRawString());
     s = s.replace("<classifier>", this.mArtifact.getClassifier());
     s = s.replace("<extension>", this.mArtifact.getExtension());
     return s;
   }
   
   public String toString()
   {
     return String.valueOf(this.mArtifact);
   }
   
   public void setArtifact(IArtifact artifact) {
    this.mArtifact = artifact;
   }
   
   public File getTarget(File rootDir) {
     if (this.mTarget != null) return this.mTarget;
     return new File(new File(rootDir, getRealLocation()), getRealFilename());
   }
   
   public void setTarget(File target) {
     this.mTarget = target;
   }
 }
