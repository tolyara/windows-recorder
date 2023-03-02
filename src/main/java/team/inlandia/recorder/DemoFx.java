package team.inlandia.recorder;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import team.inlandia.recorder.event.UserEvent;
import team.inlandia.recorder.listener.GlobalKeyBoardListener;
import team.inlandia.recorder.listener.GlobalMouseListener;
import team.inlandia.recorder.reproducer.Reproducer;
import team.inlandia.recorder.tool.EventObserver;
import team.inlandia.recorder.tool.SelectedZone;
import team.inlandia.recorder.util.CoordinateCalculator;
import team.inlandia.recorder.util.PathCreator;

public class DemoFx extends Application {

	private GlobalMouseListener mouseListener;

	private GlobalKeyBoardListener keyBoardListener;

	private Reproducer reproducer = Reproducer.getInstance();

	private Point startPoint;

	private boolean zoneSelected = false;

	private final String scriptsPath = System.getProperty("user.dir") + "\\scripts\\";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		createFxApp(primaryStage);
	}

	private void createFxApp(Stage primaryStage) {
		HBox top = new HBox();
		HBox left = new HBox();
		HBox right = new HBox();
		VBox bottom = new VBox();
		VBox center = new VBox();

		Button startButton = new Button("Start");
		Button stopButton = new Button("Stop");
		Button replayButton = new Button("Replay");
		Label userEventsLogTitle = new Label("User events: ");
		ListView<UserEvent> userEventsLog = new ListView<UserEvent>();
		ImageView screenshotImg = new ImageView();
		HBox saveOpenOptions = new HBox();
		Button openButton = new Button("Open");
		Button saveButton = new Button("Save");

		BorderPane borderPane = new BorderPane();

		prepareNativeListenerTools();
		EventObserver observer = new EventObserver(userEventsLog);
		reproducer.addObserver(observer);

		startButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				turnOnListeners();
				reproducer.clearEvents();
				userEventsLog.getItems().clear();
				screenshotImg.setImage(null);
				saveButton.setDisable(true);
			}
		});

		stopButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				reproducer.removeLastEvent(); // remove last click on button stop
//				writeToEventLog(userEventsLog, reproducer.getEvents());
				turnOffListeners();
				if (!reproducer.isEmpty()) {
					saveButton.setDisable(false);
				}
			}
		});

		replayButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<UserEvent> selectedEvents = userEventsLog.getSelectionModel().getSelectedItems();
				if (selectedEvents.isEmpty()) {
					reproducer.reproduce(screenshotImg);
				} else {
					reproducer.setEvents(selectedEvents);
					reproducer.reproduce(screenshotImg);
				}
			}
		});

		userEventsLog.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		userEventsLog.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				String screenshot = userEventsLog.getSelectionModel().getSelectedItem().getScreenshotPath();
				if (screenshot != null) {
					screenshotImg.setImage(new Image(PathCreator.getPathAsURL(screenshot)));
				} else {
					screenshotImg.setImage(null);
				}
			}
		});

		screenshotImg.setOnMouseEntered((MouseEvent event) -> {
			screenshotImg.setCursor(Cursor.CROSSHAIR);
		});
		screenshotImg.setOnMouseDragged((MouseEvent event) -> {
			zoneSelected = true;
		});
		screenshotImg.setOnMousePressed((MouseEvent event) -> {
			startPoint = getCoordinates(screenshotImg, (int) event.getX(), (int) event.getY());
		});
		screenshotImg.setOnMouseReleased((MouseEvent event) -> {
			Point endPoint = getCoordinates(screenshotImg, (int) event.getX(), (int) event.getY());
			if (zoneSelected) {
				userEventsLog.getSelectionModel().getSelectedItem().setSelectedZone(
						new SelectedZone(startPoint.getX(), startPoint.getY(), endPoint.getX(), endPoint.getY()));
				System.out.println("Selected zone saved");
			}
		});

		saveButton.setDisable(true);
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save Script");
				fileChooser.setInitialDirectory(new File(scriptsPath));
				File destinationFile = fileChooser.showSaveDialog(primaryStage);
				if (destinationFile != null) {
					writeToFile(destinationFile, reproducer.getEvents());
				}
			}
		});

		openButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open Script");
				fileChooser.setInitialDirectory(new File(scriptsPath));
				File srcFile = fileChooser.showOpenDialog(primaryStage);
				if (srcFile != null) {
					List<UserEvent> events = readFromFile(srcFile);
					reproducer.setEvents(events);
					writeToEventLog(userEventsLog, reproducer.getEvents());
				}
			}
		});

		screenshotImg.setFitWidth(480);
		screenshotImg.setFitHeight(270);
		screenshotImg.setCache(true);

		saveOpenOptions.getChildren().addAll(openButton, saveButton);
		saveOpenOptions.setAlignment(Pos.CENTER);

		top.getChildren().add(startButton);
		left.getChildren().add(stopButton);
		right.getChildren().add(replayButton);
		bottom.getChildren().addAll(userEventsLogTitle, userEventsLog);
		center.getChildren().add(screenshotImg);
		center.getChildren().add(saveOpenOptions);
		center.setAlignment(Pos.CENTER);

		borderPane.setTop(top);
		borderPane.setLeft(left);
		borderPane.setRight(right);
		borderPane.setBottom(bottom);
		borderPane.setCenter(center);

		Scene scene = new Scene(borderPane, 700, 600);
		setupPrimaryStage(primaryStage, scene);
	}

	private void setupPrimaryStage(Stage primaryStage, Scene scene) {
		primaryStage.setTitle("Recorder Demo");
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(event -> {
			System.out.println("Stage is closing");
			unregisterNativeHookLibrary();
		});
		primaryStage.show();
	}

	private void prepareNativeListenerTools() {
		registerNativeHookLibrary();
		mouseListener = new GlobalMouseListener();
		keyBoardListener = new GlobalKeyBoardListener();
		mouseListener.addObserver(keyBoardListener);
	}

	private List<UserEvent> readFromFile(File file) {
		List<UserEvent> events = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			events = (List<UserEvent>) objectInputStream.readObject();
			objectInputStream.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return events;
	}

	private void writeToFile(File file, List<UserEvent> events) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file.getCanonicalPath());
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(reproducer.getEvents());
			objectOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeToEventLog(ListView<UserEvent> userEventsLog, List<UserEvent> events) {
		for (UserEvent userEvent : events) {
			userEventsLog.getItems().add(userEvent);
		}
	}

	private Point getCoordinates(ImageView screenshotImg, int x, int y) {
		int originX = CoordinateCalculator.getOrigin(screenshotImg.getImage().getWidth(), screenshotImg.getFitWidth(),
				x);
		int originY = CoordinateCalculator.getOrigin(screenshotImg.getImage().getHeight(), screenshotImg.getFitHeight(),
				y);
		System.out.println("coordinates = " + originX + " x " + originY);
		return new Point(originX, originY);
	}

	private void registerNativeHookLibrary() {
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
		logger.setLevel(Level.OFF);
	}

	private void unregisterNativeHookLibrary() {
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException e) {
			e.printStackTrace();
		}
	}

	private void turnOnListeners() {
		GlobalScreen.addNativeMouseListener(mouseListener);
		GlobalScreen.addNativeKeyListener(keyBoardListener);
	}

	protected void turnOffListeners() {
		GlobalScreen.removeNativeMouseListener(mouseListener);
		GlobalScreen.removeNativeKeyListener(keyBoardListener);
	}

}
