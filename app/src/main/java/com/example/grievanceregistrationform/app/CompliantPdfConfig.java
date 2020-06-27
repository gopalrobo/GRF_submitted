package com.example.grievanceregistrationform.app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.Mailbean;
import com.example.grievanceregistrationform.Projectbean;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CompliantPdfConfig {

    private static Font headingFont = new Font(Font.FontFamily.HELVETICA, 12,
            Font.BOLD);
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.NORMAL);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.NORMAL);
    private static Font tableFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.BOLD);
    private static Font tableAnsFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.NORMAL);

    private static SimpleDateFormat formatnew = new SimpleDateFormat("dd-MMM-yyyy");
    private static SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

    public static void printFunction(Context context, ComplaintLetterbean complaintLetterbean) {



    }

    public static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public static void addContent(Document document, ComplaintLetterbean complaintLetterbean, Handler handler) throws Exception {

        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        table1.setWidths(new int[]{1});
        table1.addCell(createTextCellCenter("PROJECT GRIEVANCE REDRESS COMPLAINT LETTER \n\n", headingFont));
        table1.addCell(createTextCell("No# " + complaintLetterbean.getId(), catFont));
        Date date1 = format.parse(complaintLetterbean.date);
        String formattedDate = formatnew.format(date1);  // 20120821
        table1.addCell(createTextCell("Dated: " + formattedDate, catFont));
        table1.addCell(createTextCell("\nTo \n" +
                "CHAIR PERSON,\n" +
                "GRIEVANCE REDRASSAL COMMITTEE,\n" +
                "FIRST LEVEL.\n", subFont));
        Paragraph p1 = new Paragraph();
        p1.add(new Chunk("Dear Madam/Sir \n", subFont));
        p1.setLeading(16);
        ArrayList<Mailbean> mailbeans = complaintLetterbean.mailbeans;
        StringBuilder mailBuilder = new StringBuilder(1);
        for (int i = 0; i < mailbeans.size(); i++) {
            /*mailBuilder.append(mailbeans.get(i).salutation).append(", ");
            mailBuilder.append(mailbeans.get(i).name).append(", ");
            mailBuilder.append(mailbeans.get(i).surname).append(", ");
            mailBuilder.append(mailbeans.get(i).parentname).append(", ");
       */
            mailBuilder.append(mailbeans.get(i).mobile).append(", ");
            mailBuilder.append(mailbeans.get(i).doornumber).append(". ");
            mailBuilder.append(mailbeans.get(i).village).append(", ");
            mailBuilder.append(mailbeans.get(i).commune).append(", ");
            mailBuilder.append(mailbeans.get(i).district).append(", ");
            mailBuilder.append(mailbeans.get(i).Province).append(", ");
            mailBuilder.append(mailbeans.get(i).email).append(", ");
        }
        ArrayList<Projectbean> projectbeans = complaintLetterbean.projectbeans;
        StringBuilder projectBuilder = new StringBuilder(1);
        for (int i = 0; i < projectbeans.size(); i++) {
            projectBuilder.append(projectbeans.get(i).detail).append(", ");
        }
        p1.add(new Chunk("We would like to file a complaint regarding the ", subFont));
        p1.add(new Chunk(complaintLetterbean.getProjectname(), catFont));
        p1.add(new Chunk(", an ADB-assisted Project.", subFont));
        p1.add(new Chunk("We believe that due to this Project, we have suffered or are likely to suffer the following", subFont));
        table1.addCell(createTextCellPara(p1));
        table1.addCell(createTextCell("", subFont));
        table1.setKeepTogether(true);
        document.add(table1);


        PdfPTable table2 = new PdfPTable(5);
        table2.setWidthPercentage(100);
        table2.setWidths(new float[]{0.1f, 0.6f, 0.3f, 0.6f, 0.6f});
        table2.addCell(createTextCellBorderSign("#", tableFont));
        table2.addCell(createTextCellBorderSign("Name of Affected Person(s)", tableFont));
        table2.addCell(createTextCellBorderSign("Geotags", tableFont));
        table2.addCell(createTextCellBorderSign("Evidence Pictures /Videos ", tableFont));
        table2.addCell(createTextCellBorderSign("Description of Complaint(s) ", tableFont));


        Map<String, Projectbean> projectbeanMap =
                new HashMap<String, Projectbean>();
        for (int i = 0; i < complaintLetterbean.getProjectbeans().size(); i++) {
            projectbeanMap.put(complaintLetterbean.projectbeans.get(i).getName(), complaintLetterbean.projectbeans.get(i));
        }


        for (int i = 0; i < complaintLetterbean.getMailbeans().size(); i++) {

            Message personal = new Message();
            personal.obj = "Processing ( Complaints " + String.valueOf(i+1) + "/" + String.valueOf(
                    complaintLetterbean.getMailbeans().size()) + ")...";
            handler.sendMessage(personal);

            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            if (complaintLetterbean.getMailbeans().get(i).getImages() != null) {
                ArrayList<String> list = complaintLetterbean.getMailbeans().get(i).getImages();
                for (int k = 0; k < list.size(); k++) {
                    String name = list.get(k);
                    if (projectbeanMap.containsKey(name)) {
                        Projectbean projectbeanTemp = projectbeanMap.get(name);

                        table2.addCell(createTextCellBorderSign(String.valueOf(k + 1), tableAnsFont));
                        StringBuilder stringBuilder = new StringBuilder(1);
                        try {
                            stringBuilder.append(fields.salutation).append(".");
                            stringBuilder.append(fields.name).append(", \n");
                            stringBuilder.append(fields.doornumber).append(", ");
                            stringBuilder.append(fields.village).append(" (Village),\n");
                            stringBuilder.append(fields.commune).append(" (Commune),\n");
                            stringBuilder.append(fields.district).append(" (District),\n");
                            stringBuilder.append(fields.Province).append(" (Province),\n +855.");
                            stringBuilder.append(fields.mobile).append(",\n");
                            stringBuilder.append(fields.email);
                        } catch (Exception e) {

                        }
                        table2.addCell(createTextCellBorderSign(stringBuilder.toString(), tableAnsFont));
                        String geotag = projectbeanTemp.geotag;
                        try {
                            geotag = AppConfig.dfFour.format(Double.parseDouble(
                                    projectbeanTemp.geotag.split(",")[0])) + ",\n" + AppConfig.dfFour.format(Double.parseDouble(
                                    projectbeanTemp.geotag.split(",")[1]));
                        } catch (Exception e) {

                        }
                        table2.addCell(createTextCellBorderSign(geotag, tableAnsFont));
                        String uriProfile = "";
                        if (projectbeanTemp.attachment.length() > 0) {
                            uriProfile = projectbeanTemp.attachment;
                        }
                        if (uriProfile != null && uriProfile.length() > 0) {
                            Image image = cretaeImage(uriProfile);

                            if (image != null) {
                                Image img = Image.getInstance(image);
                                PdfPCell cell = new PdfPCell(img, true);
                                cell.setFixedHeight(90);
                                cell.setBorder(Rectangle.BOX);
                                cell.setPadding(5);
                                table2.addCell(cell);

                            } else {
                                table2.addCell(createTextCellBorder("", catFont));
                            }
                        } else {
                            table2.addCell(createTextCellBorder("", catFont));
                        }
                        table2.addCell(createTextCellBorderSign(projectbeanTemp.detail, tableAnsFont));


                    }
                }
            }
        }
        table2.setKeepTogether(true);
        document.add(table2);

        PdfPTable table3 = new PdfPTable(1);
        table3.setWidthPercentage(100);
        table3.setWidths(new int[]{1});


        Paragraph p3 = new Paragraph();
        p3.setLeading(16);
        p3.add(new Chunk("We have authorized ", subFont));
        StringBuilder stringBuildername = new StringBuilder(1);
        for (int i = 0; i < mailbeans.size(); i++) {
            stringBuildername.append(mailbeans.get(i).name).append(", ");
        }
        p3.add(new Chunk(" to represent us in this complaint: ", subFont));

        String name = "";
        String mobile = "";
        String email = "";
        String address = "";

        if (complaintLetterbean.getRepresentativebeans().size() > 1) {
            name = complaintLetterbean.getRepresentativebeans().get(1).name;
            mobile = complaintLetterbean.getRepresentativebeans().get(1).mobile;
            email = complaintLetterbean.getRepresentativebeans().get(1).email;
            StringBuilder stringBuilder = new StringBuilder(1);
            stringBuilder.append(name).append(", ");
            stringBuilder.append(complaintLetterbean.getRepresentativebeans().get(1).doornumber).append(", ");
            stringBuilder.append(complaintLetterbean.getRepresentativebeans().get(1).village).append("(Village), ");
            stringBuilder.append(complaintLetterbean.getRepresentativebeans().get(1).commune).append("(Commune), ");
            stringBuilder.append(complaintLetterbean.getRepresentativebeans().get(1).district).append("(District), ");
            stringBuilder.append(complaintLetterbean.getRepresentativebeans().get(1).Province).append("(Province) ");
            address = stringBuilder.toString();
        }
        p3.add(new Chunk(address, subFont));
        p3.add(new Chunk("Cambodia Mobile:  ", subFont));
        p3.add(new Chunk("+ 855." + mobile, catFont));
        p3.add(new Chunk(" Email: ", subFont));
        p3.add(new Chunk(email, catFont));
        p3.add(new Chunk(" who has assisted us in preparing the complaint.", subFont));
        p3.add(new Chunk("\n\nHence, we are choosing problem solving GRG at first level to expeditiously address our problems with a fair resolution. We request you to keep our identities confidential.", subFont));

        table3.addCell(createTextCellPara(p3));
        table3.setKeepTogether(true);
        document.add(table3);

        PdfPTable table6 = new PdfPTable(1);
        table6.setWidthPercentage(100);
        table6.setWidths(new int[]{1});
        table6.addCell(createTextCell("\nSincerely", subFont));
        table6.addCell(createTextCell("Signature", subFont));
        table6.addCell(createTextCell("\n", subFont));
        table6.setKeepTogether(true);
        document.add(table6);


        PdfPTable table4 = new PdfPTable(3);
        table4.setWidthPercentage(100);
        table4.setWidths(new float[]{0.5f, 3, 5});

        table4.addCell(createTextCellBorder("#", subFont));
        table4.addCell(createTextCellBorder("Affected Person Name ", subFont));
        table4.addCell(createTextCellBorder("Signature / Thumb impression ", subFont));


        for (int i = 1; i < complaintLetterbean.getMailbeans().size(); i++) {

            Message personal = new Message();
            personal.obj = "Processing ( Sign " + String.valueOf(i+1) + "/" + String.valueOf(
                    complaintLetterbean.getMailbeans().size()) + ")...";
            handler.sendMessage(personal);

            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            table4.addCell(createTextCellBorderSign(String.valueOf(i), tableAnsFont));
            table4.addCell(createTextCellBorderSign(fields.salutation + "." + fields.name, tableAnsFont));
            String uriProfile = "";
            if (complaintLetterbean.mailbeans.get(i).sign != null && complaintLetterbean.mailbeans.get(i).sign.length() > 0) {
                uriProfile = complaintLetterbean.mailbeans.get(i).sign;
            }

            if (uriProfile != null && uriProfile.length() > 0) {
                Image image = cretaeImage(uriProfile);
                if (image != null) {
                    Image img = Image.getInstance(image);
                    PdfPCell cell = new PdfPCell(img, true);
                    cell.setBorder(Rectangle.BOX);
                    cell.setFixedHeight(40);
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    table4.addCell(cell);
                } else {
                    table4.addCell(createTextCellBorder("", catFont));
                }
            } else {
                table4.addCell(createTextCellBorder("", catFont));
            }
        }

        table4.setKeepTogether(true);
        document.add(table4);


        PdfPTable table41 = new PdfPTable(3);
        table41.setWidthPercentage(100);
        table41.setWidths(new float[]{0.5f, 3, 5});

        table41.addCell(createTextCellBorder("#", subFont));
        table41.addCell(createTextCellBorder("Representative Person Name ", subFont));
        table41.addCell(createTextCellBorder("Signature / Thumb impression ", subFont));

        if (complaintLetterbean.representativebeans.size() > 1) {
            table41.addCell(createTextCellBorderSign("1", tableAnsFont));
            table41.addCell(createTextCellBorderSign(complaintLetterbean.getRepresentativebeans().get(1).salutation + "." + complaintLetterbean.getRepresentativebeans().get(1).name, tableAnsFont));
            String uriProfile = "";
            if (complaintLetterbean.getRepresentativebeans()
                    != null && complaintLetterbean.getRepresentativebeans().size() > 0
                    && complaintLetterbean.getRepresentativebeans().get(1).sign != null && complaintLetterbean.representativebeans.get(1).sign.length() > 0) {
                uriProfile = complaintLetterbean.representativebeans.get(1).sign;
            }

            if (uriProfile != null && uriProfile.length() > 0) {
                Image image = cretaeImage(uriProfile);
                if (image != null) {
                    Image img = Image.getInstance(image);
                    PdfPCell cell = new PdfPCell(img, true);
                    cell.setBorder(Rectangle.BOX);
                    cell.setFixedHeight(40);
                    cell.setPadding(5);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    table41.addCell(cell);
                } else {
                    table41.addCell(createTextCellBorder("", catFont));
                }
            } else {
                table41.addCell(createTextCellBorder("", catFont));
            }
        }

        table41.setKeepTogether(true);
        document.add(table41);

        //level 2
     /*   document.newPage();
        PdfPTable table5 = new PdfPTable(1);
        table5.setWidthPercentage(100);
        table5.setWidths(new int[]{1});
        table5.addCell(createTextCellCenter("\n\nProject Grievance Redress COMPLAINT LETTER \n", headingFont));
        table5.addCell(createTextCellCenter("(Level -2 Provincial)", subFont));
        table5.addCell(createTextCell("\n", subFont));
        table5.addCell(createTextCell(complaintLetterbean.getDate(), subFont));
        table5.addCell(createTextCell(complaintLetterbean.getId(), catFont));
        table5.addCell(createTextCell("Dear Madam/Sir", subFont));
        Paragraph p12 = new Paragraph();
        p12.setLeading(22);

        p12.add(new Chunk("I / We, " , subFont));
        p12.add(new Chunk(mailname.toString() , catFont));
        p12.add(new Chunk("have signed this letter, are residents of " , subFont));
        p12.add(new Chunk(mailBuilder.toString() , catFont));
        p12.add(new Chunk("and would like to file a complaint regarding the ", subFont));
        p12.add(new Chunk(complaintLetterbean.getProjectname(), catFont));
        p12.add(new Chunk(", an ADB-assisted Project.", subFont));
        p12.add(new Chunk("\n\nWe believe that due to this Project,we have suffered or are likely to suffer the following ", subFont));
//      p12.add(new Chunk(complaintLetterbean.getSuffer() , catFont));


        p12.add(new Chunk("\n\nWe have previously raised our concerns with the\t", subFont));
        p12.add(new Chunk(complaintLetterbean.getAdboffice(), catFont));
        p12.add(new Chunk(" but are not satisfied with the results. Below is a summary of what happened:\n ", subFont));

        table5.addCell(createTextCellPara(p12));
        table5.setKeepTogether(true);
        document.add(table5);


        PdfPTable table12 = new PdfPTable(5);
        table12.setWidthPercentage(100);
        table12.setWidths(new float[]{0.5f, 3, 2, 4, 3});
        table12.addCell(createTextCellBorder("#", subFont));
        table12.addCell(createTextCellBorder("Name of affected persons \n(Mr., Ms., Mrs.)", subFont));
        table12.addCell(createTextCellBorder("Geotag", subFont));
        table12.addCell(createTextCellBorder("Description of Complaint (Typing / Speech to text) ", subFont));
        table12.addCell(createTextCellBorder("Evidence Picture /Video / Both ", subFont));


        for (int i = 0; i < complaintLetterbean.getProjectbeans().size(); i++) {
            Projectbean fields = complaintLetterbean.getProjectbeans().get(i);

            table12.addCell(createTextCellBorder(String.valueOf(i+1), catFont));
            table12.addCell(createTextCellBorder(fields.name, catFont));
            table12.addCell(createTextCellBorder(fields.geotag, catFont));
            table12.addCell(createTextCellBorder(fields.detail, catFont));
            //  table12.addCell(createTextCellBorder(fields.attachment, catFont));
            String uriProfile = fields.attachment;
            if (uriProfile != null && uriProfile.length()>0) {
                Image image = cretaeImage(uriProfile);
                if (image != null) {
                    Image img = Image.getInstance(image);
                    PdfPCell cell = new PdfPCell(img, true);
                    cell.setBorder(Rectangle.BOX);
                    cell.setFixedHeight(90);
                    cell.setPadding(5);
                    table12.addCell(cell);
                }
                else{
                    table12.addCell(createTextCellBorder("", catFont));
                }
            }else{
                table12.addCell(createTextCellBorder("", catFont));
            }
        }
        table12.setKeepTogether(true);
        document.add(table12);

        PdfPTable table13 = new PdfPTable(1);
        table13.setWidthPercentage(100);
        table13.setWidths(new int[]{1});
        Paragraph p13 = new Paragraph();
        p13.setLeading(22);
        p13.add(new Chunk("\nwe have authorized " , subFont));
        //  p3.add(new Chunk(complaintLetterbean.getName() , subFont));
        p13.add(new Chunk(" to represent us in this complaint:  " , subFont));
        p13.add(new Chunk(" Mailing Address:  ", subFont));
        p13.add(new Chunk(stringBuilder.toString() , catFont));
        p13.add(new Chunk(" Email: " , subFont));
        p13.add(new Chunk(complaintLetterbean.getEmailid(), catFont));
        p13.add(new Chunk(" Telephone: " , subFont));
        p13.add(new Chunk(complaintLetterbean.getTelephone1() , catFont));
        p13.add(new Chunk(" who has assisted us in preparing the complaint." , subFont));

        p13.add(new Chunk("\n\nHence, we are choosing problem solving GRG at first level to expeditiously address our problems with a fair resolution.  We (request or do not request) that you keep our identities confidential." , subFont));

        table13.addCell(createTextCellPara(p13));
        table13.setKeepTogether(true);
        document.add(table13);



        PdfPTable table16 = new PdfPTable(1);
        table16.setWidthPercentage(100);
        table16.setWidths(new int[]{1});
        table16.addCell(createTextCell("\nSincerely,", subFont));
        table16.addCell(createTextCell("Signature\n", subFont));
        table16.addCell(createTextCell("\n", subFont));



        table16.setKeepTogether(true);
        document.add(table16);


        PdfPTable table14 = new PdfPTable(3);
        table14.setWidthPercentage(100);
        table14.setWidths(new float[]{0.5f, 5, 5});

        table14.addCell(createTextCellBorder("#", subFont));
        table14.addCell(createTextCellBorder("(Complainant Name auto fill with designation  e.g. Mr., Mrs., Ms.)", subFont));
        table14.addCell(createTextCellBorder("Signature / Thumb impression ", subFont));


        for (int i = 0; i < complaintLetterbean.getMailbeans().size(); i++) {
            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            table14.addCell(createTextCellBorder(String.valueOf(i+1), tableAnsFont));
            table14.addCell(createTextCellBorder(fields.salutation, tableAnsFont));
            String uriProfile = fields.sign;
            if (uriProfile != null && uriProfile.length()>0) {
                Image image = cretaeImage(uriProfile);
                if (image != null) {
                    Image img = Image.getInstance(image);
                    PdfPCell cell = new PdfPCell(img, true);
                    cell.setBorder(Rectangle.BOX);
                    cell.setFixedHeight(90);
                    cell.setPadding(5);
                    table14.addCell(cell);
                }
                else{
                    table14.addCell(createTextCellBorder("", catFont));
                }
            }else{
                table14.addCell(createTextCellBorder("", catFont));
            }

        }


        table14.setKeepTogether(true);
        document.add(table14);

        //level-3
        document.newPage();
        PdfPTable table25 = new PdfPTable(1);
        table25.setWidthPercentage(100);
        table25.setWidths(new int[]{1});
        table25.addCell(createTextCellCenter("\n\nProject Grievance Redress COMPLAINT LETTER \n", headingFont));
        table25.addCell(createTextCellCenter("(Level -3 National)", catFont));
        table25.addCell(createTextCell("\n", subFont));
        table25.addCell(createTextCell(complaintLetterbean.getDate(), catFont));
        table25.addCell(createTextCell(complaintLetterbean.getId(), catFont));

        Paragraph p32 = new Paragraph();
        p32.setLeading(22);

        p32.add(new Chunk("I / We, " , subFont));
        p32.add(new Chunk(mailname.toString() , catFont));
        p32.add(new Chunk("have signed this letter, are residents of " , subFont));
        p32.add(new Chunk(mailBuilder.toString() , catFont));
        p32.add(new Chunk("and would like to file a complaint regarding the ", subFont));
        p32.add(new Chunk(complaintLetterbean.getProjectname(), catFont));
        p32.add(new Chunk(", an ADB-assisted Project.", subFont));
        p32.add(new Chunk("\n\nWe believe that due to this Project,we have suffered or are likely to suffer the following ", subFont));
        //p32.add(new Chunk(complaintLetterbean.getSuffer() , catFont));
        p32.add(new Chunk("\n\nWe have previously raised our concerns with the", subFont));
        p32.add(new Chunk(complaintLetterbean.getAdboffice(), catFont));
        p32.add(new Chunk(" but are not satisfied with the results. Below is a summary of what happened:\n ", subFont));

        table25.addCell(createTextCellPara(p32));

        table25.setKeepTogether(true);
        document.add(table25);


        PdfPTable table22 = new PdfPTable(5);
        table22.setWidthPercentage(100);
        table22.setWidths(new float[]{0.5f, 3, 2, 4, 3});
        table22.addCell(createTextCellBorder("#", subFont));
        table22.addCell(createTextCellBorder("Name of affected persons \n(Mr., Ms., Mrs.)", subFont));
        table22.addCell(createTextCellBorder("Geotag", subFont));
        table22.addCell(createTextCellBorder("Description of Complaint (Typing / Speech to text) ", subFont));
        table22.addCell(createTextCellBorder("Evidence Picture /Video / Both ", subFont));


        for (int i = 0; i < complaintLetterbean.getProjectbeans().size(); i++) {
            Projectbean fields = complaintLetterbean.getProjectbeans().get(i);

            table22.addCell(createTextCellBorder(String.valueOf(i+1), catFont));
            table22.addCell(createTextCellBorder(fields.name, catFont));
            table22.addCell(createTextCellBorder(fields.geotag, catFont));
            table22.addCell(createTextCellBorder(fields.detail, catFont));
            String uriProfile = fields.attachment;
            if (uriProfile != null && uriProfile.length()>0) {
                Image image = cretaeImage(uriProfile);
                if (image != null) {
                    Image img = Image.getInstance(image);
                    PdfPCell cell = new PdfPCell(img, true);
                    cell.setBorder(Rectangle.BOX);
                    cell.setFixedHeight(90);
                    cell.setPadding(5);
                    table22.addCell(cell);
                }
                else{
                    table22.addCell(createTextCellBorder("", catFont));
                }
            }else{
                table22.addCell(createTextCellBorder("", catFont));
            }

        }
        table22.setKeepTogether(true);
        document.add(table22);

        PdfPTable table23 = new PdfPTable(1);
        table23.setWidthPercentage(100);
        table23.setWidths(new int[]{1});
        Paragraph p23 = new Paragraph();
        p23.setLeading(22);
        p23.add(new Chunk("\nwe have authorized " , subFont));
        //  p3.add(new Chunk(complaintLetterbean.getName() , subFont));
        p23.add(new Chunk(" to represent us in this complaint: " , subFont));
        p23.add(new Chunk(" Mailing Address: " , subFont));

        p23.add(new Chunk(stringBuilder.toString() , catFont));
        p23.add(new Chunk(" Email: " , subFont));
        p23.add(new Chunk(complaintLetterbean.getEmailid() , catFont));
        p23.add(new Chunk(" Telephone: " , subFont));
        p23.add(new Chunk(complaintLetterbean.getTelephone1() , catFont));
        p23.add(new Chunk(" who has assisted us in preparing the complaint." , subFont));

        p23.add(new Chunk("\n\nHence, we are choosing problem solving GRG at first level to expeditiously address our problems with a fair resolution.  We (request or do not request) that you keep our identities confidential." , subFont));

        table23.addCell(createTextCellPara(p23));
        table23.setKeepTogether(true);
        document.add(table23);





        PdfPTable table26 = new PdfPTable(1);
        table26.setWidthPercentage(100);
        table26.setWidths(new int[]{1});
        table26.addCell(createTextCell("\nSincerely,", catFont));
        table26.addCell(createTextCell("Signature\n", catFont));
        table26.addCell(createTextCell("\n", catFont));
        table26.setKeepTogether(true);
        document.add(table26);




        PdfPTable table24 = new PdfPTable(3);
        table24.setWidthPercentage(100);
        table24.setWidths(new float[]{0.5f, 5, 5});

        table24.addCell(createTextCellBorder("#", subFont));
        table24.addCell(createTextCellBorder("(Complainant Name auto fill with designation  e.g. Mr., Mrs., Ms.)", subFont));
        table24.addCell(createTextCellBorder("Signature / Thumb impression ", subFont));


        for (int i = 0; i < complaintLetterbean.getMailbeans().size(); i++) {
            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            table24.addCell(createTextCellBorder(String.valueOf(i+1), tableAnsFont));
            table24.addCell(createTextCellBorder(fields.salutation, tableAnsFont));
            String uriProfile = fields.sign;
            if (uriProfile != null && uriProfile.length()>0) {
                Image image = cretaeImage(uriProfile);

                if (image != null) {
                    Image img = Image.getInstance(image);
                    PdfPCell cell = new PdfPCell(img, true);
                    cell.setBorder(Rectangle.BOX);
                    cell.setFixedHeight(90);
                    cell.setPadding(5);
                    table24.addCell(cell);
                }
                else{
                    table24.addCell(createTextCellBorder("", catFont));
                }
            }else{
                table24.addCell(createTextCellBorder("", catFont));
            }

        }


        table24.setKeepTogether(true);
        document.add(table24);*/


    }


    public static PdfPCell createTextCell(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellCenter(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellPara(Paragraph text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        text.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(text);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellParaCenter(Paragraph text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        text.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(text);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellParaRight(Paragraph text) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        text.setAlignment(Element.ALIGN_RIGHT);
        cell.addElement(text);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.NO_BORDER);
        return cell;
    }

    public static PdfPCell createTextCellBorder(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    public static PdfPCell createTextCellBorderSign(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setPadding(5);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    private static Image cretaeImage(String uri) {

        try {
            URL url = new URL(uri);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            Bitmap image = decodeSampledBitmapFromResource(url, 100, 100);
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Image convertBmp = Image.getInstance(byteArray);
            convertBmp.setAlignment(Element.ALIGN_CENTER);
            convertBmp.scaleToFit(50, 50);
            return convertBmp;
        } catch (Exception e) {
            return null;
        }

    }

    public static Bitmap decodeSampledBitmapFromResource(URL resId,
                                                         int reqWidth, int reqHeight) throws Exception {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(resId.openConnection().getInputStream(), null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeStream(resId.openConnection().getInputStream(), null, options);
        Bitmap newBitmap = Bitmap.createScaledBitmap(bitmap, 90, 90, true);

        // Decode bitmap with inSampleSize set
        return newBitmap;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}