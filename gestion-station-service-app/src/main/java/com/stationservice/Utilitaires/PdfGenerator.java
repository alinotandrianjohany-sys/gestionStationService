package com.stationservice.Utilitaires;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.stationservice.Models.Entretien;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    public static void genererRecuEntretien(Entretien entretien, String destPath) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(destPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // --- EN-TÊTE DU REÇU ---
        Paragraph titre = new Paragraph("REÇU DE PAIEMENT - STATION ESSENCE")
                .setBold()
                .setFontSize(14)
                .setTextAlignment(TextAlignment.CENTER);
        document.add(titre);
        document.add(new Paragraph("\n"));

        // --- INFORMATIONS GÉNÉRALES ---
        String dateFormatted = "";
        if (entretien.getDateEntretien() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            dateFormatted = entretien.getDateEntretien().format(formatter);
        }

        document.add(new Paragraph("N° Entretien : " + (entretien.getNumEntr() != null ? entretien.getNumEntr() : "")).setBold());
        document.add(new Paragraph("Date : " + dateFormatted));
        document.add(new Paragraph("Client : " + (entretien.getNomClient() != null ? entretien.getNomClient() : "")));
        document.add(new Paragraph("Immatriculation : " + (entretien.getImmatriculationVoiture() != null ? entretien.getImmatriculationVoiture() : "")));
        document.add(new Paragraph("\n"));

        // --- TABLEAU DES SERVICES / PRESTATIONS ---
        // Création d'un tableau à 2 colonnes (Description / Montant)
        Table table = new Table(UnitValue.createPercentArray(new float[]{3f, 1f}));
        table.setWidth(UnitValue.createPercentValue(100));

        // En-têtes du tableau
        table.addHeaderCell(new Cell().add(new Paragraph("Désignation des prestations / Services").setBold()));
        table.addHeaderCell(new Cell().add(new Paragraph("Montant (Ar)").setBold()).setTextAlignment(TextAlignment.RIGHT));

        // Ligne de l'entretien (ou des services associés)
        table.addCell(new Cell().add(new Paragraph("Entretien véhicule (" + entretien.getImmatriculationVoiture() + ")")));
        table.addCell(new Cell().add(new Paragraph(entretien.getPrixEntretien() + " Ar")).setTextAlignment(TextAlignment.RIGHT));

        document.add(table);
        document.add(new Paragraph("\n"));

        // --- TOTAL ---
        Paragraph total = new Paragraph("TOTAL À PAYER : " + entretien.getPrixEntretien() + " Ar")
                .setBold()
                .setFontSize(12)
                .setTextAlignment(TextAlignment.RIGHT);
        document.add(total);

        // Fermeture du document
        document.close();
    }
}