package ca.bcit.comp2522.termproject.emoji;

public enum EnemyEmoji {
    ANGRY("angry.png");

    private String filename;

     EnemyEmoji(String filename) {
         this.filename = filename;
     }

     public String getFilename() {
         return filename;
     }
}
