/**
 Status:        COMPLETE
 File name:     ChatClientPM.java
 Project name: 	Assignment 4
 Programmer:    Pawel Misiak
 Instructor:    SWEETY VARGHESE
 Class:         Java Application Development
 Date:          05/02/17

 Software Development Method

 1) Problem:
 Write a program that enables two users to chat using JavaFX, threads, sockets.

 Implement one user as the server and the other as the client.

 The client too has two text areas: One for entering text and the other(non editable) for displaying text received from
 the server. When the user presses the enter key , the current line is send to the server.

 2) User input:
 User can use ONLY the top text area to send messages. The bottom text area is disabled and no changes can be implied there.
 To send a messages, user needs to use ENTER(return) key. Messages sent from Client will be marked as "You" and the one
 received will display "Server" Prior to the message.

 3) Example of an output:
 Server: Hello, there!
 You: Hi!
 Server: What time is it?
 You: It is 4:35 PM

*/
package ServerClient;

import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class ChatClientPM extends Application {
    // IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;

    @Override // Override the start method in the Application class
    public void start(Stage primaryStage) {
        // Panel p to hold the label and text field
        BorderPane paneForTextField = new BorderPane();
        paneForTextField.setPadding(new Insets(5, 5, 5, 5));
        paneForTextField.setStyle("-fx-border-color: green");
        paneForTextField.setLeft(new Label("Enter a message: "));

        //Create a editable textfield where user will type the message
        TextField tf = new TextField();
        tf.setAlignment(Pos.BOTTOM_RIGHT);
        paneForTextField.setCenter(tf);

        BorderPane mainPane = new BorderPane();
        // Text area to display contents
        TextArea ta = new TextArea();
        ta.setStyle("-fx-control-inner-background: lightgreen");
        // Make the text are being not editable
        ta.setEditable(false);

        mainPane.setCenter(new ScrollPane(ta));
        mainPane.setTop(paneForTextField);

        // Create a scene and place it in the stage
        Scene scene = new Scene(mainPane, 450, 200);
        primaryStage.setTitle("Client"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage


        new Thread( () -> {
            try {

                    // Create a socket to connect to the server
                    Socket socket = new Socket("localhost", 8888);
                    // Create an input stream to receive data from the server
                    fromServer = new DataInputStream(socket.getInputStream());

                    // Create an output stream to send data to the server
                    toServer = new DataOutputStream(socket.getOutputStream());



                tf.setOnAction(e -> {
                    try {
                        // Get the message from the text field
                        String message = new String(tf.getText());

                        // Send the message to the server
                        toServer.writeUTF(message);
                        toServer.flush();

                        ta.appendText("You: " + message + "\n");
                        tf.clear();
                    } catch (IOException ex) {
                        System.err.println(ex);
                    }
                });


                while(true)
                {
                    //Receive message from the server
                    String messageIn = fromServer.readUTF();

                    Platform.runLater(() ->
                    {
                        ta.appendText("Server: " + messageIn + "\n");
                    });
                }


            }
            catch(IOException ex) {
                ex.printStackTrace();
            }
        }).start();

        }

    /**
     * The main method is only needed for the IDE with limited
     * JavaFX support. Not needed for running from the command line.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
