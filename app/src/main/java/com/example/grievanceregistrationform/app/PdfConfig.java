package com.example.grievanceregistrationform.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.grievanceregistrationform.ComplaintLetter.ComplaintLetterbean;
import com.example.grievanceregistrationform.Mailbean;
import com.example.grievanceregistrationform.Projectbean;
import com.example.grievanceregistrationform.R;
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
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import androidx.core.content.FileProvider;

public class PdfConfig {

    private static Font catFont = new Font(Font.FontFamily.HELVETICA, 12,
            Font.BOLD);
    private static Font subFont = new Font(Font.FontFamily.HELVETICA, 10,
            Font.NORMAL);



    public static void printFunction(Context context, ComplaintLetterbean complaintLetterbean) {

        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/PDF";
            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            Log.d("PDFCreator", "PDF Path: " + path);
            File file = new File(dir, "Grievance Registration" + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);


            Document document = new Document();
            PdfWriter pdfWriter = PdfWriter.getInstance(document, fOut);
            Rectangle rect = new Rectangle(175, 20, 530, 800);
            pdfWriter.setBoxSize("art", rect);

            Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.cst_pdf);
            Bitmap bu = BitmapFactory.decodeResource(context.getResources(), R.drawable.cst_pdf);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            icon.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bu.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            byte[] byteArray1 = stream1.toByteArray();

            HeaderFooterPageEvent event = new HeaderFooterPageEvent(Image.getInstance(byteArray),
                    Image.getInstance(byteArray1));
            pdfWriter.setPageEvent(event);

            document.open();
            PdfConfig.addMetaData(document);
            // AppConfig.addTitlePage(document);
            PdfConfig.addContent(document,complaintLetterbean);
            document.close();


        } catch (Error | Exception e) {
            e.printStackTrace();
        }

        Uri photoURI = FileProvider.getUriForFile(context,
                context.getPackageName() + ".provider",
                new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath() + "/PDF/" + "Grievance Registration" + ".pdf"));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(photoURI
                , "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(intent);

    }

    public static void addMetaData(Document document) {
        document.addTitle("My first PDF");
        document.addSubject("Using iText");
        document.addKeywords("Java, PDF, iText");
        document.addAuthor("Lars Vogel");
        document.addCreator("Lars Vogel");
    }

    public static void addContent(Document document, ComplaintLetterbean complaintLetterbean) throws Exception {


        PdfPTable table1 = new PdfPTable(1);
        table1.setWidthPercentage(100);
        table1.setWidths(new int[]{1});
        Paragraph p = new Paragraph();
        p.add(new Chunk((char) R.drawable.adb));
        table1.addCell(createTextCellCenter("Asian Development Bank (ADB), Accountability Mechanism, Complaint Form\n", catFont));
        table1.addCell(createTextCellCenter("(Add rows or pages, if needed)", subFont));
        document.add(table1);

        PdfPTable table2 = new PdfPTable(1);
        table2.setWidthPercentage(100);
        table2.setWidths(new int[]{1});
        table2.addCell(createTextCell("A.  Choice of function - problem solving or compliance review (Choose one below)",subFont));
        table2.addCell(createTextCell(complaintLetterbean.getFacilitator(),catFont));
        table2.addCell(createTextCellBorderRight("Special Project Facilitator for problem solving (Assists people who are directly and materially harmed by specific problems caused, or is likely to be caused, by ADB-assisted projects through informal, flexible, and consensus-based methods with the consent and participation of all parties concerned)\n ",subFont));
        table2.addCell(createTextCell(complaintLetterbean.getReviewpanel(),catFont));
        table2.addCell(createTextCellBorderRight("Compliance Review Panel for compliance review (Investigates alleged noncompliance by ADB with its operational policies and procedures in any ADB-assisted project in the course of the formulation, processing, or implementation of the project that directly, materially, and adversely affects, or is likely to affect, local people, as well as monitors the implementation of remedial action relates to the harm or likely harm caused by noncompliance)\n ",subFont));
        table2.setKeepTogether(true);
        document.add(table2);


        PdfPTable table3 = new PdfPTable(1);
        table3.setWidthPercentage(100);
        table3.setWidths(new int[]{1});
        table3.addCell(createTextCell("B. Confidentiality\n",subFont));
        table3.addCell(createTextCellBorderRight("Do you want your identities to be kept confidential? \n",subFont));
        table3.addCell(createTextCellBorder(complaintLetterbean.getConfidential(),catFont));
        table3.addCell(createTextCell("C. Complainants (Anonymous complaints will not be accepted. There must be at least two project-affected complainants.)\n",subFont));
        table3.setKeepTogether(true);
        document.add(table3);

        PdfPTable table4 = new PdfPTable(6);
        table4.setWidthPercentage(100);
        table4.setWidths(new int[]{1, 1, 1, 1, 1, 1});
        for (int i = 0; i < complaintLetterbean.getMailbeans().size(); i++) {
            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            table4.addCell(createTextCellBorder("Name and designation\n(Mr., Ms., Mrs.)", catFont));
            table4.addCell(createTextCellBorder("Signature", catFont));
            table4.addCell(createTextCellBorder("Position/\n" +
                    "Organization (If any)", catFont));
            table4.addCell(createTextCellBorder("Mailing Address", catFont));
            table4.addCell(createTextCellBorder("Telephone number\n (landline/mobile)", catFont));
            table4.addCell(createTextCellBorder("E-mail address", catFont));

            table4.addCell(createTextCellBorder(fields.getName()+fields.getSurname()+fields.getParentname(), catFont));
            table4.addCell(createTextCellBorder(fields.getSign(), catFont));
            table4.addCell(createTextCellBorder(fields.getPosition(), catFont));
            table4.addCell(createTextCellBorder(fields.getDoornumber()+fields.getVillage()+fields.getCommune()+fields.getDistrict()+fields.getProvince(), catFont));
            table4.addCell(createTextCellBorder(fields.getMobile(), catFont));
            table4.addCell(createTextCellBorder(fields.getEmail(), catFont));
        }
        table4.setKeepTogether(true);
        document.add(table4);


        PdfPTable table6 = new PdfPTable(1);
        table6.setWidthPercentage(100);
        table6.setWidths(new int[]{1});
        table6.addCell(createTextCellBorderRight("Authorized Representative or Assistant (if any). (Information regarding the representatives, or persons assisting complainants in filing the complaint, will be disclosed, except when they are also complainants and they request confidentiality.)\n", catFont));
        table6.setKeepTogether(true);
        document.add(table6);

        PdfPTable table5 = new PdfPTable(6);
        table5.setWidthPercentage(100);
        table5.setWidths(new int[]{1, 1, 1, 1, 1, 1});
        for (int i = 0; i < complaintLetterbean.getMailbeans().size(); i++) {
            Mailbean fields = complaintLetterbean.getMailbeans().get(i);
            table5.addCell(createTextCellBorder("Complainant represented", subFont));
            table5.addCell(createTextCellBorder("Name and designation\n(Mr., Ms., Mrs.)", subFont));
            table5.addCell(createTextCellBorder("Signature", subFont));
            table5.addCell(createTextCellBorder("Position/\n" +
                    "Organization (If any)", subFont));
            table5.addCell(createTextCellBorder("Mailing Address", subFont));
            table5.addCell(createTextCellBorder("Telephone number\n (landline/mobile)", subFont));
            table5.addCell(createTextCellBorder("E-mail address", subFont));

            table5.addCell(createTextCellBorder("", catFont));
            table5.addCell(createTextCellBorder(fields.getName()+fields.getSurname()+fields.getParentname(), catFont));
            table5.addCell(createTextCellBorder(fields.getSign(), catFont));
            table5.addCell(createTextCellBorder(fields.getPosition(), catFont));
            table5.addCell(createTextCellBorder(fields.getDoornumber()+fields.getVillage()+fields.getCommune()+fields.getDistrict()+fields.getProvince(), catFont));
            table5.addCell(createTextCellBorder(fields.getMobile(), catFont));
            table5.addCell(createTextCellBorder(fields.getEmail(), catFont));
        }
        table5.setKeepTogether(true);
        document.add(table5);

        PdfPTable table7 = new PdfPTable(1);
        table7.setWidthPercentage(100);
        table7.setWidths(new int[]{1});
        table7.addCell(createTextCell("D. Project\n", catFont));
        table7.setKeepTogether(true);
        document.add(table7);

        PdfPTable table8 = new PdfPTable(2);
        table8.setWidthPercentage(100);
        table8.setWidths(new int[]{1, 1});
        for (int i = 0; i < complaintLetterbean.getProjectbeans().size(); i++) {
            Projectbean fields = complaintLetterbean.getProjectbeans().get(i);
            table8.addCell(createTextCellBorderRight("Name", catFont));
            table8.addCell(createTextCell(complaintLetterbean.getProjectname(),catFont));
        }
        table8.setKeepTogether(true);
        document.add(table8);

        PdfPTable table18 = new PdfPTable(2);
        table18.setWidthPercentage(100);
        table18.setWidths(new int[]{1, 1});
        for (int i = 0; i < complaintLetterbean.getProjectbeans().size(); i++) {
            Projectbean fields = complaintLetterbean.getProjectbeans().get(i);
            table18.addCell(createTextCellBorderRight("Location", catFont));
            table18.addCell(createTextCellBorder(fields.getGeotag(),catFont));
            table18.addCell(createTextCellBorderRight("Brief description", catFont));
            table18.addCell(createTextCellBorder(fields.getDetail(),catFont));
        }
        table18.setKeepTogether(true);
        document.add(table18);

        PdfPTable table9 = new PdfPTable(1);
        table9.setWidthPercentage(100);
        table9.setWidths(new int[]{1});
        table9.addCell(createTextCell("E. Complaint:\n", catFont));
        table9.setKeepTogether(true);
        document.add(table9);

        PdfPTable table10 = new PdfPTable(1);
        table10.setWidthPercentage(100);
        table10.setWidths(new int[]{1});
        table10.addCell(createTextCellBorderRight("What direct and material harm has the ADB-assisted project caused, or will likely cause, to the complainants?  ", catFont));
        table10.addCell(createTextCellBorderRight("Have the complainants made prior efforts to solve the problem(s) and issue(s) with the ADB operations department including Resident Mission concerned?\n", catFont));
        table10.addCell(createTextCellBorderRight(complaintLetterbean.getResidentmission(),catFont));
        table10.addCell(createTextCellBorderRight(" If YES, please provide the following: when, how, by whom, and with whom the efforts were made. Please describe any response the complainants may have received from or any actions taken by ADB. ", catFont));

        table10.setKeepTogether(true);
        document.add(table10);

        PdfPTable table11 = new PdfPTable(1);
        table11.setWidthPercentage(100);
        table11.setWidths(new int[]{1});
        table11.addCell(createTextCell("F. Optional Information\n", catFont));
        table11.setKeepTogether(true);
        document.add(table11);

        PdfPTable table12 = new PdfPTable(1);
        table12.setWidthPercentage(100);
        table12.setWidths(new int[]{1});
        table12.addCell(createTextCellBorderRight("1.  What is the complainants’ desired outcome or remedy for the complaint?\n  ", catFont));
        table12.addCell(createTextCellBorderRight(complaintLetterbean.getDesiredoutcome(),catFont));
        table12.addCell(createTextCellBorderRight("2. Anything else you would like to add?\n ", catFont));
        table12.addCell(createTextCellBorderRight(complaintLetterbean.getAdd(),catFont));

        table12.setKeepTogether(true);
        document.add(table12);

        PdfPTable table13 = new PdfPTable(1);
        table13.setWidthPercentage(100);
        table13.setWidths(new int[]{1});
        table13.addCell(createTextCell("Name of the person who completed this form:"+complaintLetterbean.getName(), catFont));
        table13.addCell(createTextCell("Signature:"+"\t\t\t\tDate:"+complaintLetterbean.getDate(), catFont));
        table13.setKeepTogether(true);
        document.add(table13);

        PdfPTable table14 = new PdfPTable(1);
        table14.setWidthPercentage(100);
        table14.setWidths(new int[]{1});
        table14.addCell(createTextCell("Please send the complaint, by mail, fax, e-mail, or hand delivery, or through any ADB Resident Mission, to the following:", subFont));
        table14.addCell(createTextCell("Complaint Receiving Officer (CRO),", catFont));
        table14.addCell(createTextCell("Accountability Mechanism\n" +
                "\tADB Headquarters, 6 ADB Avenue, Mandaluyong City 1550, Philippines, \t\n" +
                "Telephone number: +63-2-6324444 local 70309, Fax: +63-2-6362086, \n" +
                "E-mail: amcro@adb.org", subFont));

        table14.setKeepTogether(true);
        document.add(table14);
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
    public static PdfPCell createTextCellBorderRight(String text, Font font) throws DocumentException, IOException {
        PdfPCell cell = new PdfPCell();
        Paragraph p = new Paragraph(text, font);
        p.setAlignment(Element.ALIGN_LEFT);
        cell.addElement(p);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setBorder(Rectangle.BOX);
        return cell;
    }

    public static Bitmap decodeSampledBitmapFromResource(URL resId,
                                                         int reqWidth, int reqHeight) throws Exception{

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(resId.openConnection().getInputStream(), null, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(resId.openConnection().getInputStream(), null, options);
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