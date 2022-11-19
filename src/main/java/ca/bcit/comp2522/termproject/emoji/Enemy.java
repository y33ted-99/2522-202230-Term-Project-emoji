package ca.bcit.comp2522.termproject.emoji;

public class Enemy extends Entity {
    private  EmojiType emoji;
    private int shootFromX;
    private int shootFromY;

    public Enemy(EmojiType emoji) {
        super("enemy/" + emoji.getFilename());
        this.emoji = emoji;
    }

    public void setShootFromX(final int xPos) {
        shootFromX = xPos;
    }
    public void setShootFromY(final int yPos) {
        shootFromY = yPos;
    }
    /**
     * Shoots Letters at the player.
     */
    public void shoot() {
        int speed = EmojiApp.RNG.nextInt(3, 15);
        Letter letter = new Letter(emoji.getPhrase().substring(0, 1), shootFromX, shootFromY, speed);
        Thread bouncer = new Thread(letter);
        bouncer.setDaemon(true);
        bouncer.start();
    }
}
