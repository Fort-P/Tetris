import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class TetrominoPiece extends Pane {
    private int x = 0;
    private int y = 0;
    private Paint color = Color.WHITE;

    public TetrominoPiece (int x, int y, Paint color) {
        this.x = x;
        this.y = y;
        this.color = color;

        Rectangle piece = new Rectangle(45, 45, color);
        getChildren().add(piece);
    }

    public int getX () {
        return x;
    }

    public void moveLeft () {
        if (this.x != 0) { // If we aren't in the leftmost column already
            this.x--; // Move left
        }
    }

    public void moveRight () {
        if (this.x != 9) { // If we aren't in the rightmost column already
            this.x++; // Move right
        }
    }

    public int getY () {
        return y;
    }

    public void moveDown () {
        if (this.y != 19) { // If we aren't at the bottom
            this.y++; // Move down
        }
    }

    public Paint getColor () {
        return color;
    }

    public void setColor (Paint color) {
        this.color = color;
    }
}
