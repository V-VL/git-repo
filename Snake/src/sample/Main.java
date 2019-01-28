package sample;

        import javafx.animation.*;
        import javafx.application.Application;
        import javafx.geometry.Point3D;
        import javafx.scene.*;
        import javafx.scene.image.Image;
        import javafx.scene.paint.Color;
        import javafx.scene.transform.Rotate;
        import javafx.scene.transform.Translate;
        import javafx.stage.Stage;
        import java.util.Random;


public class Main extends Application {

    private Point3D dir = new Point3D(0, 0, 0);
    private Point3D next = new Point3D(0, 0, 0);
    private Group root = new Group();
    private Group snake = new Group();
    private Cube food = new Cube(Color.RED);
    private Random random = new Random();
    private double t = 0;
    private AnimationTimer timer;


    private void moveFood() {
        food.setTranslateX(random.nextInt(20) - 5);
        food.setTranslateY(random.nextInt(20) - 5);
    }

    private void grow() {
        moveFood();
        Cube cube = new Cube(Color.GREEN);
        cube.set(next.add(dir));

        snake.getChildren().add(cube);
    }

    private void onUpdate() {
        next = next.add(dir);
        Cube c = (Cube) snake.getChildren().remove(0);
        c.set(next);
        snake.getChildren().add(c);

        boolean collision = snake.getChildren()
                .stream()
                .map(n -> (Cube) n)
                .anyMatch(cube -> cube.isColliding(food));

        if (collision) {
            grow();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {

        Cube cube = new Cube(Color.GREEN);
        snake.getChildren().add(cube);

        moveFood();
        root.getChildren().addAll(snake, food);

        Scene scene = new Scene(root, 1280, 800, true);
        scene.setFill(Color.rgb(150, 150, 150));




        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.getTransforms().addAll(new Translate(0, -25, -25), new Rotate(-45, Rotate.X_AXIS));

        scene.setCamera(camera);

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                t += 0.015;

                 if (t > 0.1) {
                    onUpdate();
                    t = 0;
                }
            }
        };

        timer.start();

        stage.setTitle("Snake");
        Image image1 = new Image("/resources/icons/snake1.jpg");
        stage.getIcons().add(image1);
        scene.setOnKeyPressed(e -> {
            switch (e.getCode()) {

                case LEFT:
                    dir = new Point3D(-1, 0, 0);
                    break;

                case RIGHT:
                    dir = new Point3D(1, 0, 0);
                    break;

                case UP:
                    dir = new Point3D(0, -1, 0);
                    break;

                case DOWN:
                    dir = new Point3D(0, 1, 0);
                    break;
            }
        });
        stage.setScene(scene);
        stage.show();
    }



    public static void main(String[] args) {
        launch(args);
    }
}

