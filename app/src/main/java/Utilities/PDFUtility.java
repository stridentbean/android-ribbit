package Utilities;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import ind.habanero.realestatecalculator.CachedFileProvider;
import ind.habanero.realestatecalculator.R;
import models.BasicInformation;
import models.Property;
import models.RepairItem;

/**
 * Created by stridentbean on 3/2/2015.
 */
public class PDFUtility {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.BOLD);
    public static final String TAG = PDFUtility.class.getSimpleName();

    public static Intent getAssignmentEmailIntent(BasicInformation basicInformation, Property property, Context context) {
        Intent toRet = null;

        try {
            //creating file in memory
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            addMetaData(document, context.getResources().getString(R.string.pdf_assignment), context.getResources().getString(R.string.pdf_assignment), basicInformation);
            addTitlePage(context.getResources().getString(R.string.pdf_assignment), document);
            addPropertyAddress(property, document);
            addPropertyInformation(property, document);
            addPreparedBy(basicInformation, document);
            addAssignmentTable(property, canvas, document);
            document.close();

            CachedFileProvider.createCachedFile(context, context.getResources().getString(R.string.pdf_assignment_name), outputStream);
            toRet = CachedFileProvider.getSendEmailIntent(context, basicInformation.getEmail(), context.getResources().getString(R.string.email_assignment_subject), context.getResources().getString(R.string.email_message), context.getResources().getString(R.string.pdf_assignment_name));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toRet;

    }

    public static Intent getDoubleCloseEmailIntent(BasicInformation basicInformation, Property property, Context context) {
        Intent toRet = null;

        try {
            //creating file in memory
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            addMetaData(document, context.getResources().getString(R.string.pdf_double_close), context.getResources().getString(R.string.pdf_double_close), basicInformation);
            addTitlePage(context.getResources().getString(R.string.pdf_double_close), document);
            addPropertyAddress(property, document);
            addPropertyInformation(property, document);
            addPreparedBy(basicInformation, document);
            addDoubleCloseTable(property, canvas, document);
            document.close();

            CachedFileProvider.createCachedFile(context, context.getResources().getString(R.string.pdf_double_close_name), outputStream);
            toRet = CachedFileProvider.getSendEmailIntent(context, basicInformation.getEmail(), context.getResources().getString(R.string.email_pdf_double_close_subject), context.getResources().getString(R.string.email_message), context.getResources().getString(R.string.pdf_double_close_name));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return toRet;

    }



    public static Intent getRepairEstimateEmailIntent(BasicInformation basicInformation, Property property, java.util.List<RepairItem> groupList, Context context) {
        Intent toRet = null;

        try {
            //creating file in memory
            Document document = new Document();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            PdfContentByte canvas = writer.getDirectContent();
            addMetaData(document, context.getResources().getString(R.string.pdf_repair_estimate), context.getResources().getString(R.string.pdf_repair_estimate), basicInformation);
            addTitlePage(context.getResources().getString(R.string.pdf_repair_estimate), document);
            addPropertyAddress(property, document);
            addPropertyInformation(property, document);
            addPreparedBy(basicInformation, document);
            addRepairCostsTable(groupList,property, canvas, document);
            document.close();

            //CachedFileProvider.createCachedFile(context, context.getResources().getString(R.string.pdf_repair_estimate_name), fileOutput);
            CachedFileProvider.createCachedFile(context, context.getResources().getString(R.string.pdf_repair_estimate_name), outputStream);
            toRet = CachedFileProvider.getSendEmailIntent(context,
                    basicInformation.getEmail(),
                    context.getResources().getString(R.string.email_repair_estimate_subject),
                    context.getResources().getString(R.string.email_repair_estimate_message),
                    context.getResources().getString(R.string.pdf_repair_estimate_name));

        } catch (Exception e) {
            Log.i(TAG, e.getMessage());
            e.printStackTrace();
        }
        return toRet;

    }

    private static void addPreparedBy(BasicInformation basicInformation, Document document) throws DocumentException {
        Font font = FontFactory.getFont("Times-Roman");
        Font fontBold = FontFactory.getFont("Times-Roman", 13, Font.BOLD);

        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        document.add(paragraph);

        document.add(new Paragraph("Estimate Prepared By:", fontBold));
        document.add(new Paragraph(basicInformation.getFirstName() + " " + basicInformation.getLastName()));
        document.add(new Paragraph("Phone: " + basicInformation.getPhone()));
        document.add(new Paragraph("Email: " + basicInformation.getEmail()));
    }


    private static void addPropertyAddress(Property property, Document document) throws DocumentException {
        Font font = FontFactory.getFont("Times-Roman");
        Font fontBold = FontFactory.getFont("Times-Roman", 13, Font.BOLD);

        document.add(new Paragraph("Property Information", fontBold));
        document.add(new Paragraph(property.getAddressLine1()));
        document.add(new Paragraph(property.getAddressLine2()));
        document.add(new Paragraph(property.getCity() + ", " + property.getCity() + " " + property.getZip()));

    }

    private static void addPropertyInformation(Property property, Document document) throws DocumentException {
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 1);
        document.add(paragraph);

        document.add(new Paragraph("Bedrooms: " + property.getBedrooms()));
        document.add(new Paragraph("Bathrooms: " + property.getBathrooms()));
        document.add(new Paragraph("Square Footage: " + property.getSquareFootage()));
        document.add(new Paragraph("Year Built: " + property.getYearBuilt()));
    }

    private static void addContent(Document document) throws DocumentException {
        Anchor anchor = new Anchor("First Chapter", catFont);
        anchor.setName("First Chapter");

        // Second parameter is the number of the chapter
        Chapter catPart = new Chapter(new Paragraph(anchor), 1);

        Paragraph subPara = new Paragraph("Subcategory 1", subFont);
        Section subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Hello"));

        subPara = new Paragraph("Subcategory 2", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("Paragraph 1"));
        subCatPart.add(new Paragraph("Paragraph 2"));
        subCatPart.add(new Paragraph("Paragraph 3"));

        // add a list
        createList(subCatPart);
        Paragraph paragraph = new Paragraph();
        addEmptyLine(paragraph, 5);
        subCatPart.add(paragraph);

        // now add all this to the document
        document.add(catPart);

        // Next section
        anchor = new Anchor("Second Chapter", catFont);
        anchor.setName("Second Chapter");

        // Second parameter is the number of the chapter
        catPart = new Chapter(new Paragraph(anchor), 1);

        subPara = new Paragraph("Subcategory", subFont);
        subCatPart = catPart.addSection(subPara);
        subCatPart.add(new Paragraph("This is a very important message"));

        // now add all this to the document
        document.add(catPart);
    }

    private static void addTitlePage(String title, Document document) throws DocumentException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        preface.add(new Paragraph(title, catFont));

        addEmptyLine(preface, 1);
        /*
        // Will create: Report generated by: _name, _date
        preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                smallBold));
        addEmptyLine(preface, 3);
        preface.add(new Paragraph("This document describes something which is very important ",
                smallBold));

        addEmptyLine(preface, 8);

        preface.add(new Paragraph("This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
                redFont));

        document.add(preface);
        // Start a new page
        document.newPage();
        */

    }

    private static void addMetaData(Document document, String title, String subject, BasicInformation basicInformation) {
        document.addTitle(title);
        document.addSubject(subject);
        document.addKeywords("Java, PDF, iText");
        document.addAuthor(basicInformation.getFirstName() + " " + basicInformation.getLastName());
        document.addCreator(basicInformation.getFirstName() + " " + basicInformation.getLastName());
    }

    private static void addRepairCostsTable(java.util.List<RepairItem> groupList, Property property, PdfContentByte canvas, Document document) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(220F);

        Font font = FontFactory.getFont("Times-Roman");
        Font fontBold = FontFactory.getFont("Times-Roman", 12, Font.BOLD);
        PdfPCell c1 = new PdfPCell(new Phrase("Estimated Repair Costs", fontBold));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        c1.setColspan(2);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Item"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Total"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);
        table.setHeaderRows(1);

        double total = 0;
        for(RepairItem repairItem: groupList) {
            if(repairItem.isSwitched()) {
                table.addCell(repairItem.getTitle());

                c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, repairItem.getTotal() + "")));
                c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(c1);

                total += repairItem.getTotal();
            }
        }

        table.addCell(new Phrase("Total", fontBold));
        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, total + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        table.addCell(new Phrase("Price Per Sq Foot", fontBold));
        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(false, total/property.getSquareFootage() + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        Log.i(TAG, "table.getTotalHeight(): " + table.getTotalHeight() + " || " + (600 + table.getTotalHeight()));
        table.writeSelectedRows(0, groupList.size()+3, 270, 800, canvas);
    }

    private static void addDoubleCloseTable(Property property, PdfContentByte canvas, Document document) throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(220F);

        Font font = FontFactory.getFont("Times-Roman");
        Font fontBold = FontFactory.getFont("Times-Roman", 12, Font.BOLD);

        PdfPCell c1 = new PdfPCell(new Phrase("After Repair Value"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, property.getARV()+ "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Estimated Costs"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, property.getRepairEstimate() + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Estimated Closing Costs"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, property.getClosingCosts() + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Sale Price"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        int multValue = Utility.getMultiplierValue(property.getADOM(), property.getARV());
        int resale = multValue - property.getRepairEstimate();

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, resale + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        table.writeSelectedRows(0, 4, 270, 800, canvas);

        addCallToAction(canvas, "Call to Action!", 270, 800 - table.getTotalHeight() - 20);

    }

    private static void addAssignmentTable(Property property, PdfContentByte canvas, Document document) throws IOException, DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setTotalWidth(220F);

        Font font = FontFactory.getFont("Times-Roman");
        Font fontBold = FontFactory.getFont("Times-Roman", 12, Font.BOLD);

        PdfPCell c1 = new PdfPCell(new Phrase("ARV"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, property.getARV()+ "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Estimated Costs"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, property.getRepairEstimate() + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Sale Price"));
        c1.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(c1);

        int multValue = Utility.getMultiplierValue(property.getADOM(), property.getARV());
        int resale = multValue - property.getRepairEstimate();

        c1 = new PdfPCell(new Phrase(Utility.getFormattedText(true, resale + "")));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        table.writeSelectedRows(0, 3, 270, 800, canvas);

        addCallToAction(canvas, "Call to Action1 Call to Action2 Call to Action!3 Call to Action!4", 270, 800 - table.getTotalHeight() - 20);

    }


    private static void createList(Section subCatPart) {
        List list = new List(true, false, 10);
        list.add(new ListItem("First point"));
        list.add(new ListItem("Second point"));
        list.add(new ListItem("Third point"));
        subCatPart.add(list);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    private static void addCallToAction(PdfContentByte canvas, String text, float x, float y) throws IOException, DocumentException {
        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        canvas.beginText();
        canvas.setFontAndSize(bf, 12);
        canvas.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, 0);
        canvas.endText();
    }
}
