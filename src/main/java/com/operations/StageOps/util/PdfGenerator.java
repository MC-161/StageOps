package com.operations.StageOps.util;

import com.operations.StageOps.model.Contract;
import com.operations.StageOps.model.Invoice;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PdfGenerator {

    /**
     * Generates a PDF for all invoices of a client
     */
    public static void generateInvoicePdf(List<Invoice> invoices, String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph header = new Paragraph("Client Invoices Summary", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(Chunk.NEWLINE);

        // Add generation timestamp
        Font timestampFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Paragraph timestamp = new Paragraph("Generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), timestampFont);
        document.add(timestamp);
        document.add(Chunk.NEWLINE);

        // Create table for invoices
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        // Add headers to table
        Font tableHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        table.addCell(new PdfPCell(new Phrase("Invoice ID", tableHeader)));
        table.addCell(new PdfPCell(new Phrase("Client ID", tableHeader)));
        table.addCell(new PdfPCell(new Phrase("Status", tableHeader)));

        // Add data rows
        Font tableData = new Font(Font.FontFamily.HELVETICA, 12);
        for (Invoice invoice : invoices) {
            table.addCell(new Phrase(String.valueOf(invoice.getInvoiceId()), tableData));
            table.addCell(new Phrase(String.valueOf(invoice.getClientId()), tableData));
            table.addCell(new Phrase(invoice.getStatus(), tableData));
        }

        document.add(table);
        document.close();
    }

    /**
     * Generates a PDF for a single invoice
     */
    // StageOps Green: #65C915
    private static final BaseColor STAGE_OPS_GREEN = new BaseColor(101, 201, 21); // RGB for #65C915
    // Lighter Grey: #D3D3D3
    private static final BaseColor LIGHT_GREY = new BaseColor(211, 211, 211); // RGB for #D3D3D3

    public static void generateSingleInvoicePdf(Invoice invoice, String clientName, String clientAddress, String filePath) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add logo
        Image logo = Image.getInstance("./src/main/resources/static/StageOpsLogo.png");
        logo.scaleToFit(80, 80);
        logo.setAlignment(Image.ALIGN_LEFT);
        document.add(logo);

        // Add company info (updated to City University of London)
        Paragraph company = new Paragraph("City University of London\nNorthampton Square, London EC1V 0HB, UK\n(020) 7040 5000 | stageOps@lancaster.com",
                new Font(Font.FontFamily.HELVETICA, 11));
        company.setSpacingAfter(20);
        document.add(company);

        // Recipient info
        Paragraph recipient = new Paragraph("RECIPIENT:\n" + clientName + "\nClient Address:\n" + clientAddress ,
                new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD));
        recipient.setSpacingAfter(20);
        document.add(recipient);

        // Invoice Summary Box
        PdfPTable summary = new PdfPTable(2);
        summary.setWidthPercentage(50);
        summary.setHorizontalAlignment(Element.ALIGN_RIGHT);
        summary.addCell(getColoredCell("Invoice #" + invoice.getInvoiceId(), STAGE_OPS_GREEN));
        summary.addCell(getPlainCell(""));

        summary.addCell(getPlainCell("Issued"));
        summary.addCell(getPlainCell(invoice.getIssueDate().toLocalDate().toString()));

        summary.addCell(getPlainCell("Due"));
        summary.addCell(getPlainCell(invoice.getDueDate().toLocalDate().toString()));

        summary.addCell(getColoredCell("Total", STAGE_OPS_GREEN));
        summary.addCell(getColoredCell("$" + invoice.getTotalAmount(), STAGE_OPS_GREEN));
        document.add(summary);

        // Services rendered (next step)
        PdfPTable services = new PdfPTable(2);
        services.setWidthPercentage(100);
        services.setSpacingBefore(20);
        services.setSpacingAfter(20);
        services.addCell(getColoredCell("Service", LIGHT_GREY));
        services.addCell(getColoredCell("Price", LIGHT_GREY));
        services.addCell(getPlainCell("Service 1"));
        services.addCell(getPlainCell("$100.00"));
        services.addCell(getPlainCell("Service 2"));
        services.addCell(getPlainCell("$22.00"));
        services.addCell(getPlainCell("Service 3"));
        services.addCell(getPlainCell("$22.00"));
        services.addCell(getColoredCell("Total", LIGHT_GREY));
        services.addCell(getColoredCell(String.valueOf(invoice.getTotalAmount()), LIGHT_GREY));
        document.add(services);

        // Payment instructions
        Paragraph paymentInstructions = new Paragraph("Payment Instructions:\n" +
                "Please make all checks payable to StageOps.\n" +
                "For wire transfers, please use the following details:\n" +
                "Bank: XYZ Bank\n" +
                "Account Number: 123456789\n" +
                "Routing Number: 987654321",
                new Font(Font.FontFamily.HELVETICA, 12));
        paymentInstructions.setSpacingAfter(20);
        document.add(paymentInstructions);

        // Terms and conditions
        Paragraph terms = new Paragraph("Terms and Conditions:\n" +
                "1. Payment is due within 30 days of the invoice date.\n" +
                "2. Late payments may incur a fee of 1.5% per month.\n" +
                "3. All services are subject to our standard terms and conditions.",
                new Font(Font.FontFamily.HELVETICA, 12));
        terms.setSpacingAfter(20);
        document.add(terms);

        // Footer
        document.add(new Chunk("\n\n\n"));
        final Font footerFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Paragraph footer = new Paragraph("This is a computer-generated document and does not require a signature.", footerFont);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.add(footer);

        document.close();
    }

    private static PdfPCell getColoredCell(String text, BaseColor color) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setBackgroundColor(color);
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    private static PdfPCell getPlainCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(10);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }



    /**
     * Generates a PDF for all contracts of a client
     */
    public static void generateContractPdf(List<Contract> contracts, String filePath) throws Exception {
        Document document = new Document(PageSize.A4);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add header
        Font headerFont = new Font(Font.FontFamily.HELVETICA, 16, Font.BOLD, BaseColor.DARK_GRAY);
        Paragraph header = new Paragraph("Client Contracts Summary", headerFont);
        header.setAlignment(Element.ALIGN_CENTER);
        document.add(header);
        document.add(Chunk.NEWLINE);

        // Add generation timestamp
        Font timestampFont = new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, BaseColor.GRAY);
        Paragraph timestamp = new Paragraph("Generated on: " +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), timestampFont);
        document.add(timestamp);
        document.add(Chunk.NEWLINE);

        // Create table for contracts
        PdfPTable table = new PdfPTable(3);
        table.setWidthPercentage(100);

        // Add headers to table
        Font tableHeader = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        table.addCell(new PdfPCell(new Phrase("Contract ID", tableHeader)));
        table.addCell(new PdfPCell(new Phrase("Client ID", tableHeader)));
        table.addCell(new PdfPCell(new Phrase("Status", tableHeader)));

        // Add data rows
        Font tableData = new Font(Font.FontFamily.HELVETICA, 12);
        for (Contract contract : contracts) {
            table.addCell(new Phrase(String.valueOf(contract.getContractId()), tableData));
            table.addCell(new Phrase(String.valueOf(contract.getClientId()), tableData));
            table.addCell(new Phrase(contract.getStatus(), tableData));
        }

        document.add(table);
        document.close();
    }

    /**
     * Generates a PDF for a single contract
     */
    public static void generateSingleContractPdf(Contract contract, String ownerName, String renterName, String filePath) throws Exception {
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Add company logo
        try {
            Image logo = Image.getInstance("./src/main/resources/static/StageOpsLogo.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);
        } catch (Exception e) {
            // If logo file not found, continue without it
            System.out.println("Company logo not found: " + e.getMessage());
        }

        // Add contract title
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("VENUE RENTAL CONTRACT TEMPLATE", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(10);
        document.add(title);

        // Add contract introduction
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 11);
        Font boldFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

        String introText = "This Venue Rental Agreement (\"Agreement\") is made effective as of " +
                contract.getEffectiveDate() + ", by and between " +
                ownerName + " (\"Owner\") and " +
                renterName + " (\"Renter\").";

        Paragraph intro = new Paragraph(introText, normalFont);
        intro.setAlignment(Element.ALIGN_LEFT);
        intro.setSpacingAfter(20);
        document.add(intro);

        // PART I: GENERAL TERMS
        Font sectionFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Paragraph section1 = new Paragraph("PART I: GENERAL TERMS", sectionFont);
        section1.setSpacingBefore(10);
        section1.setSpacingAfter(15);
        document.add(section1);

        // 1. Description of Premises and Rental
        Paragraph heading1 = new Paragraph("1. Description of Premises and Rental", boldFont);
        heading1.setSpacingBefore(10);
        heading1.setSpacingAfter(5);
        document.add(heading1);

        String premisesText = "1.1 Premises: The Owner hereby agrees to rent to the Renter the premises located at " +
                contract.getVenueAddress() + " (\"Venue\"), described in detail in Appendix A attached hereto, for the " +
                "purpose of " + contract.getEventDescription() + " to be held on " + contract.getEventDates() + ".";

        Paragraph premises = new Paragraph(premisesText, normalFont);
        premises.setIndentationLeft(10);
        premises.setSpacingAfter(10);
        document.add(premises);

        String rentalPeriodText = "1.2 Rental Period: The rental period shall commence at " + contract.getStartTime() +
                " on " + contract.getStartDate() + " and conclude at " + contract.getEndTime() +
                " on " + contract.getEndDate() + ".";

        Paragraph rentalPeriod = new Paragraph(rentalPeriodText, normalFont);
        rentalPeriod.setIndentationLeft(10);
        rentalPeriod.setSpacingAfter(15);
        document.add(rentalPeriod);

        // 2. Payment Terms
        Paragraph heading2 = new Paragraph("2. Payment Terms", boldFont);
        heading2.setSpacingBefore(10);
        heading2.setSpacingAfter(5);
        document.add(heading2);

        String rentalFeeText = "2.1 Rental Fee: The Renter shall pay the Owner a total fee of " +
                contract.getTotalAmount() + " (\"Rental Fee\"), payable as follows:";

        Paragraph rentalFee = new Paragraph(rentalFeeText, normalFont);
        rentalFee.setIndentationLeft(10);
        rentalFee.setSpacingAfter(5);
        document.add(rentalFee);

        Paragraph initialPayment = new Paragraph("- " + contract.getInitialAmount() +
                " due upon signing this Agreement.", normalFont);
        initialPayment.setIndentationLeft(20);
        document.add(initialPayment);

        Paragraph finalPayment = new Paragraph("- " + contract.getFinalAmount() +
                " due on " + contract.getFinalPaymentDate() + ".", normalFont);
        finalPayment.setIndentationLeft(20);
        finalPayment.setSpacingAfter(10);
        document.add(finalPayment);

        String latePaymentText = "2.2 Late Payment: If any instalment of the Rental Fee is not paid within " +
                contract.getGracePeriod() + " days after its due date, a late fee of " +
                contract.getLateFee() + " will accrue on the amount due from the due date until the amount is paid.";

        Paragraph latePayment = new Paragraph(latePaymentText, normalFont);
        latePayment.setIndentationLeft(10);
        latePayment.setSpacingAfter(15);
        document.add(latePayment);

        // 3. Security Deposit
        Paragraph heading3 = new Paragraph("3. Security Deposit", boldFont);
        heading3.setSpacingBefore(10);
        heading3.setSpacingAfter(5);
        document.add(heading3);

        String depositAmountText = "3.1 Deposit Amount: A security deposit of " + contract.getDepositAmount() +
                " shall be paid by the Renter upon signing this Agreement. This deposit will cover any damages to the Venue or " +
                "additional cleaning required following the event, beyond normal wear and tear.";

        Paragraph depositAmount = new Paragraph(depositAmountText, normalFont);
        depositAmount.setIndentationLeft(10);
        depositAmount.setSpacingAfter(10);
        document.add(depositAmount);

        String depositReturnText = "3.2 Return of Deposit: The deposit will be returned within " +
                contract.getDepositReturnDays() + " days of the conclusion of the event, minus any deductions for damages or additional services required.";

        Paragraph depositReturn = new Paragraph(depositReturnText, normalFont);
        depositReturn.setIndentationLeft(10);
        depositReturn.setSpacingAfter(15);
        document.add(depositReturn);

        // 4. Cancellation and Termination
        Paragraph heading4 = new Paragraph("4. Cancellation and Termination", boldFont);
        heading4.setSpacingBefore(10);
        heading4.setSpacingAfter(5);
        document.add(heading4);

        String cancellationText = "4.1 Cancellation by Renter: The Renter may cancel the event by providing written notice to " +
                "the Owner no later than " + contract.getCancellationNoticeDays() + " days prior to the event date. If cancellation occurs with " +
                "sufficient notice, the Owner will retain " + contract.getCancellationFee() + " of the initial deposit as a cancellation fee.";

        Paragraph cancellation = new Paragraph(cancellationText, normalFont);
        cancellation.setIndentationLeft(10);
        cancellation.setSpacingAfter(15);
        document.add(cancellation);

        // Add signature lines at the end
        document.add(Chunk.NEWLINE);
        document.add(Chunk.NEWLINE);

        PdfPTable signatureTable = new PdfPTable(2);
        signatureTable.setWidthPercentage(100);

        PdfPCell ownerSignCell = new PdfPCell(new Phrase("Owner Signature: ________________\nDate: __________", normalFont));
        ownerSignCell.setBorder(Rectangle.NO_BORDER);
        signatureTable.addCell(ownerSignCell);

        PdfPCell renterSignCell = new PdfPCell(new Phrase("Renter Signature: ________________\nDate: __________", normalFont));
        renterSignCell.setBorder(Rectangle.NO_BORDER);
        signatureTable.addCell(renterSignCell);

        document.add(signatureTable);

        document.close();
    }
    // Helper method for creating table cells

    private static PdfPCell createCell(String text, Font font, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setHorizontalAlignment(alignment);
        cell.setPadding(5);
        return cell;
    }
}