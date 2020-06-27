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
import java.util.HashMap;
import java.util.Map;

public class InvestReportPdfConfig {

    private static Font headingFont = new Font(Font.FontFamily.HELVETICA, 12,
            Font.BOLD);
    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.NORMAL);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.NORMAL);
    private static Font subFontsub = new Font(Font.FontFamily.HELVETICA, 10,
            Font.BOLD);
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
        table1.addCell(createTextCell("INVESTIGATION REPORT \n", headingFont));
        Paragraph p1 = new Paragraph();
        ArrayList<Mailbean> mailbeans = complaintLetterbean.mailbeans;
        StringBuilder mailBuilder = new StringBuilder(1);
        for (int i = 0; i < mailbeans.size(); i++) {

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
        table1.addCell(createTextCell("\nTo\nCHAIRPERSON,\n" +
                "GRIEVANCE REDRASSAL COMMITTEE,\n" +
                "FIRST LEVEL.\n", subFont));


        table1.addCell(createTextCell("Subject: Investigation Report Submission\n", subFontsub));
        p1.add(new Chunk("Dear Madam/ Sir,\n", subFont));
        p1.setLeading(16);

        p1.add(new Chunk("We undertook the field investigation along with the contractor met with the affected persons and Investigated the complaint registered in the GRM logbook under No #42285-013-0001. Dated: 27-March-2020 of ADB Project # 44285-013 Integrated Urban Environmental Management in the Tonle Sap Basin. Our investigation report with photo video and suggested resolutions is attached below", subFont));
       /* p1.add(new Chunk("No#"+complaintLetterbean.getId(), subFont));
        Date date1 = format.parse(complaintLetterbean.date);
        String formattedDate = formatnew.format(date1);  // 20120821
        p1.add(new Chunk(". Dated: " + formattedDate, catFont));*/
        p1.add(new Chunk(complaintLetterbean.getProjectname()+".\n", catFont));

        table1.addCell(createTextCellPara(p1));
        table1.setKeepTogether(true);
        document.add(table1);


        PdfPTable table2 = new PdfPTable(6);
        table2.setWidthPercentage(100);
        table2.setWidths(new float[]{0.1f, 0.6f, 0.3f, 0.6f, 0.6f,0.6f});
        table2.addCell(createTextCellBorderSign("#", tableFont));
        table2.addCell(createTextCellBorderSign("Name of Affected Person(s)", tableFont));
        table2.addCell(createTextCellBorderSign("Geotags", tableFont));
        table2.addCell(createTextCellBorderSign("Evidence Pictures /Videos ", tableFont));
        table2.addCell(createTextCellBorderSign("Description of Complaint(s) ", tableFont));
        table2.addCell(createTextCellBorderSign("Nature of Complaint ", tableFont));


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
                        table2.addCell(createTextCellBorderSign(projectbeanTemp.detail, tableAnsFont));


                    }
                }
            }
        }
        table2.setKeepTogether(true);
        document.add(table2);


        PdfPTable table22 = new PdfPTable(1);
        table22.setWidthPercentage(100);
        table22.setWidths(new float[]{1});
        table22.addCell(createTextCell("First level investigation report and suggested resolution", tableAnsFont));

        table22.setKeepTogether(true);
        document.add(table22);

        PdfPTable table12 = new PdfPTable(5);
        table12.setWidthPercentage(100);
        table12.setWidths(new float[]{0.1f, 0.6f, 0.6f, 0.6f, 0.6f});
        table12.addCell(createTextCellBorderSign("#", tableFont));
        table12.addCell(createTextCellBorderSign("Investigators", tableFont));
        table12.addCell(createTextCellBorderSign("Technical Investigation Report", tableFont));
        table12.addCell(createTextCellBorderSign("Investigation picture ", tableFont));
        table12.addCell(createTextCellBorderSign("Suggested Resolution  ", tableFont));


        table12.addCell(createTextCellBorderSign("1", catFont));
        table12.addCell(createTextCellBorderSign("Mr Nek Neron \n" +
                "GRC Member Prey Ni – Lead investigator\n" +
                "\n" +
                "Mr. Socheat Ty \n" +
                "GRC Member \n" +
                "Kaba Spean Thma Village \n" +
                "Member Investigation ", catFont));
        table12.addCell(createTextCellBorderSign("The complaint was true and we found that 20 ft X 9 inch double brick compound wall was damaged due to earth mover trenching for the drainage. The damaged structure is located within the complaints own land. Hence contractor should rebuild or compensate for the damage", catFont));
        String uriProfile = "";

        if (uriProfile != null && uriProfile.length() > 0) {
            Image image = cretaeImage(uriProfile);

            if (image != null) {
                Image img = Image.getInstance(image);
                PdfPCell cell = new PdfPCell(img, true);
                cell.setFixedHeight(90);
                cell.setBorder(Rectangle.BOX);
                cell.setPadding(5);
                table12.addCell(cell);

            } else {
                table12.addCell(createTextCellBorder("", catFont));
            }
        } else {
            table12.addCell(createTextCellBorder("", catFont));
        }
        table12.addCell(createTextCellBorderSign("The contractor was asked to provide compensation for the damaged wall. ", catFont));





        table12.setKeepTogether(true);
        document.add(table12);


        PdfPTable table3 = new PdfPTable(1);
        table3.setWidthPercentage(100);
        table3.setWidths(new int[]{1});


        Paragraph p3 = new Paragraph();
        p3.setLeading(16);
        p3.add(new Chunk("We would be glad to provide available additional details you may require during the GRM Resolution meeting.  ", subFont));
       /* StringBuilder stringBuildername = new StringBuilder(1);
        for (int i = 0; i < mailbeans.size(); i++) {
            stringBuildername.append(mailbeans.get(i).name).append(", ");
        }

        String name = "";
        String mobile = "";

        if (complaintLetterbean.getRepresentativebeans().size() > 1) {
            name = complaintLetterbean.getRepresentativebeans().get(1).name;
            mobile = complaintLetterbean.getRepresentativebeans().get(1).mobile;
            StringBuilder stringBuilder = new StringBuilder(1);
            stringBuilder.append(name).append(", ");
        }
        p3.add(new Chunk("Cambodia Mobile:  ", subFont));
        p3.add(new Chunk("+ 855." + mobile, catFont));
*/
        table3.addCell(createTextCellPara(p3));
        table3.setKeepTogether(true);
        document.add(table3);

        PdfPTable table6 = new PdfPTable(1);
        table6.setWidthPercentage(100);
        table6.setWidths(new int[]{1});
        table6.addCell(createTextCell("\nSincerely", subFont));
        table6.addCell(createTextCell("\n", subFont));
        table6.setKeepTogether(true);
        document.add(table6);


        PdfPTable table4 = new PdfPTable(3);
        table4.setWidthPercentage(100);
        table4.setWidths(new float[]{0.5f, 3, 5});

        table4.addCell(createTextCellBorderSign("#", tableFont));
        table4.addCell(createTextCellBorderSign("Investigator Name ", tableFont));
        table4.addCell(createTextCellBorderSign("Signature / Thumb impression ", tableFont));


        for (int i = 1; i < complaintLetterbean.getMailbeans().size(); i++) {

            Message personal = new Message();
            personal.obj = "Processing ( Sign " + String.valueOf(i + 1) + "/" + String.valueOf(
                    complaintLetterbean.getMailbeans().size()) + ")...";
            handler.sendMessage(personal);

            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            table4.addCell(createTextCellBorderSign(String.valueOf(i), catFont));
            table4.addCell(createTextCellBorderSign("Mr Nek Neron", catFont));
            table4.addCell(createTextCellBorderSign("", catFont));


            table4.addCell(createTextCellBorderSign("2", catFont));
            table4.addCell(createTextCellBorderSign("Mr. Socheat Ty", catFont));
            table4.addCell(createTextCellBorderSign("", catFont));


            table4.addCell(createTextCellBorderSign("3", catFont));
            table4.addCell(createTextCellBorderSign("Mr. Det Sophy", catFont));
            table4.addCell(createTextCellBorderSign("", catFont));
        }
        table4.setKeepTogether(true);
        document.add(table4);






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