package ca.bcit.comp2522.termproject.emoji;

public abstract class TextBubbleFactory extends Entity{
    private final Entity emoji;

    public TextBubbleFactory(int xPosition, int yPosition, int width, int height, String type) {
        super(xPosition, yPosition, width, height);
        this.emoji = createEmoji(type);
    }

     protected abstract Entity createEmoji(final String type);

    protected abstract void pop();
}
