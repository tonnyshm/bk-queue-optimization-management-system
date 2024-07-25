package com.finalYearProject.queueManagement.services;


import com.finalYearProject.queueManagement.model.BookedUserInfo;
import com.finalYearProject.queueManagement.repository.BookUserInfoRepo;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


@Service
public class AdminReport {

    @Autowired
    private BookUserInfoRepo bookedUserInfoRepository;


    public List<BookedUserInfo> getReportData(String timeFrame) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate;

        switch (timeFrame.toLowerCase()) {
            case "daily":
                startDate = now.minus(1, ChronoUnit.DAYS);
                break;
            case "weekly":
                startDate = now.minus(1, ChronoUnit.WEEKS);
                break;
            case "monthly":
                startDate = now.minus(1, ChronoUnit.MONTHS);
                break;
            case "yearly":
                startDate = now.minus(1, ChronoUnit.YEARS);
                break;
            default:
                throw new IllegalArgumentException("Invalid time frame");
        }

        return bookedUserInfoRepository.findByUserVisitedDateBetween(startDate, now);
    }

    public void generatePdfReport(List<BookedUserInfo> reportData, String timeFrame, HttpServletResponse response) throws IOException, DocumentException {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=report_" + timeFrame + ".pdf");

        // Initialize the document
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        // Add title
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Paragraph title = new Paragraph("Report for " + timeFrame, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        // Add a blank line
        document.add(new Paragraph(" "));

        // Create a table with 4 columns
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 3, 3, 2});

        // Add table headers
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        addTableHeader(table, headerFont, "User Email");
        addTableHeader(table, headerFont, "Service");
        addTableHeader(table, headerFont, "Branch");
        addTableHeader(table, headerFont, "Visited Date");

        // Add data to the table
        for (BookedUserInfo info : reportData) {
            table.addCell(new PdfPCell(new Phrase(info.getUserEmail())));
            table.addCell(new PdfPCell(new Phrase(info.getService().getName())));
            table.addCell(new PdfPCell(new Phrase(info.getBranch().getBkBranchName())));
            table.addCell(new PdfPCell(new Phrase(info.getUserVisitedDate().toString())));
        }

        // Add the table to the document
        document.add(table);
        document.close();
    }

    private void addTableHeader(PdfPTable table, Font headerFont, String headerTitle) {
        PdfPCell header = new PdfPCell(new Phrase(headerTitle, headerFont));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        header.setPadding(8);
        table.addCell(header);
    }
}
