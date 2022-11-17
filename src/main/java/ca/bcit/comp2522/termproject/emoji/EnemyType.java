package ca.bcit.comp2522.termproject.emoji;

public enum EnemyType implements EmojiType {
    ANGRY("angry.png");

    private String filename;

     EnemyType(String filename) {
         this.filename = filename;
     }

     public String getFilename() {
         return filename;
     }
}
