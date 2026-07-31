package com.stationservice.view;

//import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;

public class ButtonHandler implements EventHandler<MouseEvent> {

	@Override
	public void handle(MouseEvent args) {
		System.out.println("Bouton actionne !");
	}

}
