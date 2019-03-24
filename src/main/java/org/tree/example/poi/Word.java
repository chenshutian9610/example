package org.tree.example.poi;

import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLConverter;
import fr.opensagres.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.List;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 * XWPF > docx  文本替换能力弱 (Document > Paragraph > Run > Text)
 * HWPF > doc   需要引入补丁 poi-scratchpad (Document > Range > Text)
 * ps: 推荐 doc 模式 (个人感觉)
 */
public class Word {

    private final String in = "S:/Project/generate/in.";
    private final String out = "S:/Project/generate/out.";

    /****************************** docx *******************************/

    @Test
    public void newDocx() throws IOException {
        XWPFDocument document = new XWPFDocument();
        document.createParagraph().createRun().setText("hello world");
        document.createParagraph().createRun().setText("hello china");
        document.createParagraph().createRun().setText("At tutorialspoint.com, we strive hard to provide quality " +
                "tutorials for self-learning purpose in the domains of Academics, Information Technology, " +
                "Management and Computer Programming Languages.");
        document.write(new FileOutputStream(out + "docx"));
    }

    @Test
    public void dealDocx() throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(in + "docx"));
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            List<XWPFRun> runs = paragraph.getRuns();
            for (XWPFRun run : runs) {
                String temp = run.getText(0);   // 参数为位置, 一个完整的语句会被胡乱的分成几个 Run
                if (temp != null) {
                    temp = temp.replace("$company$", "company");
                    run.setText(temp, 0);
                }
            }
        }
        document.write(new FileOutputStream(out));
    }

    @Test // 只支持 ooxml, 不美观 (行距有问题)
    public void docx2html() throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(in + "docx"));
        XHTMLConverter.getInstance().convert(document, new FileOutputStream(out + "docx"), XHTMLOptions.create());
        System.out.println("success to convert !!!");
    }

    /****************************** doc *******************************/

    @Test
    public void dealDoc() throws IOException {
        HWPFDocument document = new HWPFDocument(new FileInputStream(in + "doc"));
        Range range = document.getRange();
        range.replaceText("$company$", "company");
        document.write(new FileOutputStream(out));
    }

    @Test // 复杂, 且不支持 ooxml, 但美观
    public void doc2html() throws Exception {
        HWPFDocument wordDoc = new HWPFDocument(new FileInputStream(in + "doc"));
        WordToHtmlConverter wthc = new WordToHtmlConverter(
                DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
//        wthc.setPicturesManager(new PicturesManager() {
//            @Override
//            public String savePicture(byte[] bytes, PictureType pt, String string, float f, float f1) {
//                return string;
//            }
//        });
        wthc.processDocument(wordDoc);
        Document htmlDocument = wthc.getDocument();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DOMSource domSource = new DOMSource(htmlDocument);
        StreamResult streamResult = new StreamResult(out);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer serializer = tf.newTransformer();
        serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        serializer.setOutputProperty(OutputKeys.INDENT, "yes");
        serializer.setOutputProperty(OutputKeys.METHOD, "html");
        serializer.transform(domSource, streamResult);
        OutputStream outputStream = new FileOutputStream("done.html");
        outputStream.write(out.toByteArray());
        out.close();
        outputStream.close();
        System.out.println("success to convert !!!");
    }
}
