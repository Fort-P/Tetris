import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    // Initialize Variables
    int score = 0;
    int cellSize = 45;
    int rows = 20;
    int columns = 10;
    TetrominoPiece[][] pieces = new TetrominoPiece[columns][rows];
    TetrominoPiece[][] currentTetromino = new TetrominoPiece[3][4];
    TetrominoPiece[][] nextTetromino = new TetrominoPiece[3][4];
    GridPane gameGrid;
    Font modernTetris16 = Font.loadFont("file:resources/fonts/modern-tetris.otf", 16);
    Font modernTetris32 = Font.loadFont("file:resources/fonts/modern-tetris.otf", 32);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Root Pane
        Pane root = new Pane();
        root.setStyle("-fx-background-color: black");


        // Background music
//        MediaPlayer background = new MediaPlayer(new Media("file:resources/Music/Tetris.mp3"));
//        background.setCycleCount(-1);
//        background.play();


        // Game "Board"
        VBox board = new VBox(10);
        board.setStyle("-fx-border-color: lightgray; -fx-border-width: 5px; -fx-border-radius: 5%;");
        board.setAlignment(Pos.CENTER);
        board.setLayoutX(140); // 145 sets it next to holdPane exactly, 140 overlaps the borders, (135 + 5 for one side of the border)


        // Display the score
        Text scoreDisplay = new Text(String.valueOf(score));
        scoreDisplay.setFont(modernTetris32);
        scoreDisplay.setFill(Color.LIGHTGRAY);


        // The actual playable space
        gameGrid = new GridPane();
        gameGrid.setPrefSize(columns * cellSize, rows * cellSize); // Gives me a grid that is basically 10x20 if all my squares are 50px

        // Set all the columns to be equal width (Without this, they are all 0px)
        ColumnConstraints gameCC = new ColumnConstraints();
        gameCC.setPercentWidth(100d / columns);
        for (int i = 0; i < columns; i++) {
            gameGrid.getColumnConstraints().add(gameCC);
        }

        // Set all the rows to be equal height (without this they are all 0px)
        RowConstraints gameRC = new RowConstraints();
        gameRC.setPercentHeight(100d / rows);
        for (int i = 0; i < rows; i++) {
            gameGrid.getRowConstraints().add(gameRC);
        }


        // Hold Pane
        VBox holdPane = new VBox(10);
        holdPane.setStyle("-fx-border-color: lightgray; -fx-border-width: 5px; -fx-border-radius: 5%;");
        holdPane.setLayoutY(100);
        holdPane.setAlignment(Pos.CENTER);

        Text holdLabel = new Text("HOLD");
        holdLabel.setFont(modernTetris16);
        holdLabel.setFill(Color.LIGHTGRAY);

        GridPane holdBox = new GridPane();
        holdBox.setPadding(new Insets(10));
        holdBox.setPrefSize(3 * cellSize, 4 * cellSize); // The largest width and height respectively of an individual tetromino

        // Set all the columns to be equal width (Without this, they are all 0px)
        ColumnConstraints holdCC = new ColumnConstraints();
        holdCC.setPercentWidth(100d / 3);
        for (int i = 0; i < 3; i++) {
            holdBox.getColumnConstraints().add(holdCC);
        }

        // Set all the rows to be equal height (without this they are all 0px)
        RowConstraints holdRC = new RowConstraints();
        holdRC.setPercentHeight(100d / 4);
        for (int i = 0; i < 4; i++) {
            holdBox.getRowConstraints().add(holdRC);
        }


        // Next Pane (almost identical to holdPane)
        VBox nextPane = new VBox(10);
        nextPane.setStyle("-fx-border-color: lightgray; -fx-border-width: 5px; -fx-border-radius: 5%;");
        nextPane.setLayoutY(100);
        nextPane.setLayoutX(595); // width of holdPane + width of board, + 5px per pane for borders
        nextPane.setAlignment(Pos.CENTER);

        Text nextLabel = new Text("NEXT");
        nextLabel.setFont(modernTetris16);
        nextLabel.setFill(Color.LIGHTGRAY);

        GridPane nextBox = new GridPane();
        nextBox.setPadding(new Insets(10));
        nextBox.setPrefSize(3 * cellSize, 4 * cellSize); // The largest width and height respectively of an individual tetromino

        // Set all the columns to be equal width (Without this, they are all 0px)
        ColumnConstraints nextCC = new ColumnConstraints();
        nextCC.setPercentWidth(100d / 3);
        for (int i = 0; i < 3; i++) {
            nextBox.getColumnConstraints().add(nextCC);
        }

        // Set all the rows to be equal height (without this they are all 0px)
        RowConstraints nextRC = new RowConstraints();
        nextRC.setPercentHeight(100d / 4);
        for (int i = 0; i < 4; i++) {
            nextBox.getRowConstraints().add(nextRC);
        }


        // Create the initial shapes
        currentTetromino = newTetromino();
        nextTetromino = newTetromino();
        for (TetrominoPiece[] row : currentTetromino) {
            for (TetrominoPiece piece : row) {
                if (piece != null) {
                    gameGrid.add(piece, piece.getX(), piece.getY());
                }
            }
        }


        // Falling animation
        EventHandler<ActionEvent> fallingHandler = e -> { // Create a handler for the timeline
//            try {
//                // If we are on the bottom row, or if the cell below us is occupied
//                if (currentTetromino.getY() == 19 || pieces[currentTetromino.getX()][currentTetromino.getY() + 1] != null) {
//                    // Freeze the movement of the current tetromino, and transition to the next tetromino
//                    pieces[currentTetromino.getX()][currentTetromino.getY()] = currentTetromino; // Save the tetromino piece to another array for reference later
//                    if (checkRow(currentTetromino.getY())) {
//                        clearRow(currentTetromino.getY());
//                    }
//                    nextBox.getChildren().remove(nextTetromino); // Remove the tetromino from the next box
//                    currentTetromino = nextTetromino; // set the current tetromino to the next tetromino
//                    nextTetromino = newTetrominoPiece(5, 0, Color.color(Math.random(), Math.random(), Math.random())); // Create a new tetromino to be the next one
//                    gameGrid.add(currentTetromino, currentTetromino.getX(), currentTetromino.getY()); // Add the new current piece to the game
//                    nextBox.add(nextTetromino, 0, 2); // Add the new next piece to the next box
//
//                    printGameState();
//                } else {
//                    currentTetromino.moveDown();
//                }
//            } catch (Exception ignored) {}

            for (TetrominoPiece[] row : currentTetromino) {
                for (TetrominoPiece piece : row) {
                    if (piece != null) {
                        piece.moveDown();
                    }
                }
            }
            updateGame();
        };

        // Create the falling timeline
        Timeline falling = new Timeline(new KeyFrame(Duration.millis(1000), fallingHandler));
        falling.setCycleCount(-1);
        falling.play(); // Start animation


        // Listeners
        root.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.LEFT) {
//                try {
//                    if (pieces[currentTetromino.getX() - 1][currentTetromino.getY()] == null) {
//                        currentTetromino.moveLeft();
//                    }
//                } catch (Exception ignored) {}
//                updateGame();
//            } else if (event.getCode() == KeyCode.RIGHT) {
//                try {
//                    if (pieces[currentTetromino.getX() + 1][currentTetromino.getY()] == null) {
//                        currentTetromino.moveRight();
//                    }
//                } catch (Exception ignored) {}
//                updateGame();
//            } else if (event.getCode() == KeyCode.DOWN) {
//                try {
//                    if (pieces[currentTetromino.getX()][currentTetromino.getY() + 1] == null) {
//                        currentTetromino.moveDown();
//                    }
//                } catch (Exception ignored) {}
//                updateGame();
//            }
        });


        // Add Items to the scene
        holdPane.getChildren().add(holdLabel);
        holdPane.getChildren().add(holdBox);

        board.getChildren().add(scoreDisplay);
        board.getChildren().add(gameGrid);

        nextPane.getChildren().add(nextLabel);
        nextPane.getChildren().add(nextBox);

        root.getChildren().add(holdPane);
        root.getChildren().add(board);
        root.getChildren().add(nextPane);


        // Create & show the scene
        Scene sc = new Scene(root);
        primaryStage.setScene(sc);
        primaryStage.setTitle("Tetris");
        primaryStage.show();
        root.requestFocus(); // Give the root pane focus so that keypress events register

        printGameState();
    }

    public TetrominoPiece[][] newTetromino () {
        boolean[][] template;
        TetrominoPiece[][] shape = new TetrominoPiece[4][3];
        Paint color;
        switch ((int) (Math.random() * 7)) { //Randomly choose one of our block templates
            case 0:
                template = Templates.I;
                color = Color.LIGHTBLUE;
                break;
            case 1:
                template = Templates.O;
                color = Color.YELLOW;
                break;
            case 2:
                template = Templates.T;
                color = Color.PURPLE;
                break;
            case 3:
                template = Templates.S;
                color = Color.LIME;
                break;
            case 4:
                template = Templates.Z;
                color = Color.RED;
                break;
            case 5:
                template = Templates.J;
                color = Color.BLUE;
                break;
            case 6:
                template = Templates.L;
                color = Color.ORANGE;
                break;
            default:
                throw new IllegalStateException("Unexpected value");
        }

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 3; column++) {
                if (template[row][column]) {
                    shape[row][column] = new TetrominoPiece(column + 4, row, color);
                }
            }
        }

        return shape;
    }

    /**
     * Updates the game. Called as often as needed, but at least once per second
     */
    public void updateGame () {
        // Update the current Tetromino
        for (TetrominoPiece[] row : currentTetromino) {
            for (TetrominoPiece piece : row) {
                if (piece != null) {
                    GridPane.setConstraints(piece, piece.getX(), piece.getY());
                }
            }
        }
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) { // For every piece in the grid...
                try {
                    GridPane.setConstraints(pieces[column][row], pieces[column][row].getX(), pieces[column][row].getY()); // ...Update it
                } catch (Exception ignored) {}
            }
        }
    }

    /**
     * Checks if a given row is full
     * @param row The row to check
     * @return boolean: true if full, false if not
     */
    public boolean checkRow (int row) {
        for (int column = 0; column < columns; column++) {
            // If any space within the given row is null, there must be an empty space, so return false
            if (pieces[column][row] == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * Clears the pieces from a given row
     * @param row The row to clear
     */
    public void clearRow (int row) {
        for (int column = 0; column < columns; column++) {
                gameGrid.getChildren().remove(pieces[column][row]);
                pieces[column][row] = null;
        }
        triggerFall(row);
    }

    /**
     * Makes all blocks with air under them fall
     * @param startRow The row to start checking for falling blocks at
     */
    public void triggerFall (int startRow) {
        for (int row = startRow; row >= 0; row--) {
            for (int column = 0; column < columns; column++) {
                if (pieces[column][row] != null && pieces[column][row + 1] == null) {
                    pieces[column][row].moveDown(); // Move the piece down
                    pieces[column][row + 1] = pieces[column][row]; // Set the space below to the piece above
                    pieces[column][row] = null; // Clear the space the piece was in
                }
                printGameState();
            }
        }
    }

    /**
     * Prints the current game state to the console
     */
    public void printGameState () {
        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                System.out.print(pieces[column][row] + " ");
            }
            System.out.println();
        }
    }
}