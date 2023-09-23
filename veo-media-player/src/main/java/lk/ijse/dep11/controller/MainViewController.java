package lk.ijse.dep11.controller;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;

public class MainViewController {

    public Button btnBack;
    public Button btnSlow;
    public Button btnFast;
    public Slider slrVolume;
    public MediaView mvDisplay;
    public Slider slrScroll;
    public Button btnBrowse;
    public Button btnPlay;
    public Button btnPause;
    public Button btnStop;
    public Button btnForward;
    public BorderPane root;
    MediaPlayer videoPlayer;

    public void btnBrowseOnAction(ActionEvent actionEvent) {
        FileChooser videoFileChooser = new FileChooser();
        videoFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Video Files","*.mp4",".avi",".mkv"));
        videoFileChooser.setTitle("Select an Video file");
        File videoFile = videoFileChooser.showOpenDialog(root.getScene().getWindow());

        if(videoFile != null) {
            Media media = new Media(videoFile.toURI().toString());
            videoPlayer = new MediaPlayer(media);

            DoubleProperty widthProperty = mvDisplay.fitWidthProperty();
            DoubleProperty heightProperty = mvDisplay.fitHeightProperty();

            widthProperty.bind(Bindings.selectDouble(mvDisplay.sceneProperty(),"width"));
            heightProperty.bind(Bindings.selectDouble(mvDisplay.sceneProperty(),"height"));

            videoPlayer.currentTimeProperty().addListener(new ChangeListener<Duration>() {
                @Override
                public void changed(ObservableValue<? extends Duration> observableValue, Duration duration, Duration t1) {
                    slrScroll.setValue(t1.toSeconds());
                }
            });

            videoPlayer.setOnReady(new Runnable() {
                @Override
                public void run() {
                    Duration totalDuration = media.getDuration();
                    slrScroll.setMax(totalDuration.toSeconds());
                }
            });

            slrVolume.setValue(videoPlayer.getVolume()*100);
            slrVolume.valueProperty().addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable observable) {
                    videoPlayer.setVolume(slrVolume.getValue()/100);
                }
            });

        } else {
            btnBrowse.requestFocus();
        }

        btnPlay.fire();

    }

    public void btnPlayOnAction(ActionEvent actionEvent) {
        if(videoPlayer != null) {
            mvDisplay.setMediaPlayer(videoPlayer);

            videoPlayer.play();
            videoPlayer.setRate(1);
        }
    }

    public void btnPauseOnAction(ActionEvent actionEvent) {
        if (videoPlayer != null) {
            mvDisplay.setMediaPlayer(videoPlayer);
            videoPlayer.pause();
        }
    }

    public void btnFastOnAction(ActionEvent actionEvent) {
        if (videoPlayer != null) {
            mvDisplay.setMediaPlayer(videoPlayer);
            videoPlayer.setRate(2);
        }
    }

    public void btnSlowOnAction(ActionEvent actionEvent) {
        if (videoPlayer != null) {
            mvDisplay.setMediaPlayer(videoPlayer);
            videoPlayer.setRate(0.5);
        }
    }

    public void btnStopOnAction(ActionEvent actionEvent) {
        if (videoPlayer != null) {
            mvDisplay.setMediaPlayer(videoPlayer);
            videoPlayer.stop();
        }
    }

    public void btnForwardOnAction(ActionEvent actionEvent) {
        videoPlayer.seek(videoPlayer.getCurrentTime().add(Duration.seconds(10)));
    }

    public void btnBackOnAction(ActionEvent actionEvent) {
        videoPlayer.seek(videoPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }

    public void slrScrollOnMouseDragged(MouseEvent mouseEvent) {
        videoPlayer.seek(Duration.seconds(slrScroll.getValue()));
    }

    public void slrScrollOnMousePressed(MouseEvent mouseEvent) {
        videoPlayer.seek(Duration.seconds(slrScroll.getValue()));
    }
}
