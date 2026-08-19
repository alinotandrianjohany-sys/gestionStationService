package com.stationservice.Utilitaires;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.stationservice.Models.Entretien;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {
    public static void genererRecuEntretien(Entretien entretien, String destPath) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(destPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        String texteTitre = "Reçu N° " + (entretien.getNumEntr() != null ? entretien.getNumEntr() : "");
        Paragraph titre = new Paragraph(texteTitre).setBold().setFontSize(16).setUnderline();
        document.add(titre);
        document.add(new Paragraph("\n"));

        String dateFormatted = "";
        if (entretien.getDateEntretien() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            dateFormatted = entretien.getDateEntretien().format(formatter);
        }

        document.add(new Paragraph("Date : " + dateFormatted));
        document.add(new Paragraph("Client : " + (entretien.getNomClient() != null ? entretien.getNomClient() : "")));
        document.add(new Paragraph("Immatriculation : " + (entretien.getImmatriculationVoiture() != null ? entretien.getImmatriculationVoiture() : "")));
        document.add(new Paragraph("Montant : " + entretien.getPrixEntretien() + " Ar"));

        document.close();
    }
}