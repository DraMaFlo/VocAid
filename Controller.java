package data.vocaid;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Controller {

    private ArrayList<String> wordsInFirstLanguage;
    private ArrayList<String> wordsInSecondLanguage;
    private String firstLanguage;
    private String secondLanguage;
    private File file;
    private int counter;
    private int correctCounter;
    private int incorrectCounter;
    private ArrayList<Integer> progression;
    private Image cImage = new Image(getClass().getResourceAsStream("check-circle.svg.png"));
    private Image ximage =  new Image(getClass().getResourceAsStream("x-circle.svg.png"));


    @FXML
    Label languagesTopLabel;
    @FXML
    Label translateFromLabel;
    @FXML
    Label translateToLabel;
    @FXML
    Label progressLabel;
    @FXML
    Button checkButton;
    @FXML
    Label wordToTranslateLabel;
    @FXML
    TextField translationTextField;
    @FXML
    Label verificationLabel;
    @FXML
    ImageView checkImage;
    @FXML
    Label correctLabel;
    @FXML
    Label incorrectLabel;
    @FXML
    Label percentageLabel;
    @FXML
    Button startButton;
    @FXML
    Button switchTranslationButton;


    public void loadFile(ActionEvent e) throws FileNotFoundException {
        Node node = (Node) e.getSource();
        Stage thisStage = (Stage) node.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(thisStage);

        updateInitial(file);
        startButton.setDisable(false);
        switchTranslationButton.setDisable(false);
    }

    private void updateInitial(File file) throws FileNotFoundException {
        Scanner fileScan = new Scanner(file);
        String[] languages = fileScan.nextLine().split("--");
        this.firstLanguage = languages[0];
        this.secondLanguage = languages[1];

        this.wordsInFirstLanguage = new ArrayList<>();
        this.wordsInSecondLanguage = new ArrayList<>();

        while (fileScan.hasNextLine()) {
            String[] words = fileScan.nextLine().split("--");
            this.wordsInFirstLanguage.add(words[0]);
            this.wordsInSecondLanguage.add(words[1]);
            //

        }

        languagesTopLabel.setText(this.firstLanguage + " -> " +  this.secondLanguage);
        translateFromLabel.setText("From " + this.firstLanguage);
        translateToLabel.setText("To " + this.secondLanguage);
        progressLabel.setText("Progress: 0/" + this.wordsInSecondLanguage.size());


    }


    public void start(ActionEvent e) {
        this.counter = 1;
        this.correctCounter = 0;
        this.incorrectCounter = 0;

        checkButton.setDisable(false);
        translationTextField.setDisable(false);

        createProgression();
        updateGUI();




    }

    private void updateGUI(){

        int size = this.wordsInFirstLanguage.size();
        if (size < this.counter) {
            this.counter = size;
            checkButton.setDisable(true);
            translationTextField.setDisable(true);
            wordToTranslateLabel.setText("");
            percentageLabel.setText("Score: " + ((this.correctCounter * 100) / (this.counter ) + "%"));
        }else if (correctCounter == 0) {
            percentageLabel.setText("Score: 0%" );

        } else {
            percentageLabel.setText("Score: " + ((this.correctCounter * 100) / (this.counter - 1 ) + "%"));
        }

        progressLabel.setText("Progress: " + this.counter + "/" + size);
        correctLabel.setText("Correct: " + this.correctCounter);
        incorrectLabel.setText("Incorrect: " + this.incorrectCounter);
        wordToTranslateLabel.setText(this.wordsInFirstLanguage.get(this.progression.get(this.counter-1)));
        translationTextField.setText("");

    }

    private void createProgression() {
        ArrayList<Integer> shuffleList = new ArrayList<>();
        for (int i = 0; i < this.wordsInSecondLanguage.size(); i++) {
            shuffleList.add(i);
        }
        Collections.shuffle(shuffleList);
        this.progression = shuffleList;
    }

    public void check() {

        if (checkButton.getText().equals("Continue")) {
            checkButton.setText("Check");
            verificationLabel.setText("");
            checkImage.setImage(null);
            this.counter += 1;


            updateGUI();
        } else {
            if (counter > this.wordsInSecondLanguage.size()) {
                checkButton.setDisable(true);
                return;
            }

            String input = translationTextField.getText();
            verificationLabel.setText(this.wordsInSecondLanguage.get(this.progression.get(this.counter - 1)));
            if (verificationLabel.getText().equals(input)) {
                this.correctCounter += 1;
                checkImage.setImage(this.cImage);
            } else {
                this.incorrectCounter += 1;
                checkImage.setImage(this.ximage);
            }



            checkButton.setText("Continue");
        }

    }

    public void swap() {
        ArrayList<String> switcher = this.wordsInFirstLanguage;
        this.wordsInFirstLanguage = this.wordsInSecondLanguage;
        this.wordsInSecondLanguage = switcher;
        String nameSwitcher = this.firstLanguage;
        this.firstLanguage = this.secondLanguage;
        this.secondLanguage = nameSwitcher;

        languagesTopLabel.setText(this.firstLanguage + " -> " +  this.secondLanguage);
        translateFromLabel.setText("From " + this.firstLanguage);
        translateToLabel.setText("To " + this.secondLanguage);
        progressLabel.setText("Progress: 0/" + this.wordsInSecondLanguage.size());
    }
}