package com.stationservice.Utilitaires;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.UnitValue;
import com.stationservice.Models.Entretien;
import com.stationservice.Models.Service;

import java.io.FileNotFoundException;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    public static void genererRecuEntretien(Entretien entretien, String destPath) throws FileNotFoundException {
        PdfWriter writer = new PdfWriter(destPath);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Titre du reçu
        Paragraph titre = new Paragraph("Exemple d’un reçu")
                .setBold()
                .setFontSize(16)
                .setUnderline();
        document.add(titre);
        document.add(new Paragraph("\n"));

        // Date (format dd/MM/yyyy)
        String dateFormatted = "";
        if (entretien.getDateEntretien() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            dateFormatted = entretien.getDateEntretien().format(formatter);
        }
        document.add(new Paragraph("Date : " + dateFormatted));
        document.add(new Paragraph("\n"));

        // Nom du Client
        document.add(new Paragraph("Nom du Client : " + (entretien.getNomClient() != null ? entretien.getNomClient() : "")));
        document.add(new Paragraph("\n"));

        // Voiture / Immatriculation
        document.add(new Paragraph("Voiture : " + (entretien.getImmatriculationVoiture() != null ? entretien.getImmatriculationVoiture() : "")));
        document.add(new Paragraph("\n"));

        // Tableau des prestations : Service | Montant
        Table table = new Table(UnitValue.createPercentArray(new float[]{60, 40}));
        table.setWidth(UnitValue.createPercentValue(70));

        // En-tête
        table.addHeaderCell(new Paragraph("Service").setBold());
        table.addHeaderCell(new Paragraph("Montant").setBold());

        // Lignes des services associés
        if (entretien.getServices() != null && !entretien.getServices().isEmpty()) {
            for (Service s : entretien.getServices()) {
                // Remplacement des getters pour correspondre à la classe Service
                String nom = (s.getNom() != null) ? s.getNom() : "";
                String prix = String.valueOf(s.getPrix()) + " AR";

                table.addCell(nom);
                table.addCell(prix);
            }
        }

        // Ligne du Total
        table.addCell(new Paragraph("Total").setBold());
        table.addCell(new Paragraph(entretien.getPrixEntretien() + " AR").setBold());

        document.add(table);
        document.close();
    }
}