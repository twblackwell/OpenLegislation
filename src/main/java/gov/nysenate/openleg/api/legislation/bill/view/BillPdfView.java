package gov.nysenate.openleg.api.legislation.bill.view;

import gov.nysenate.openleg.api.BasePdfView;
import gov.nysenate.openleg.legislation.bill.*;
import gov.nysenate.openleg.legislation.bill.exception.BillAmendNotFoundEx;
import gov.nysenate.openleg.legislation.bill.utils.BillTextUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static gov.nysenate.openleg.legislation.bill.BillTextFormat.HTML;
import static gov.nysenate.openleg.legislation.bill.BillTextFormat.PLAIN;

/**
 * PDF representation of a bill.
 */
public class BillPdfView extends BasePdfView {
    private static final float BILL_MARGIN = 10f, RESOLUTION_MARGIN = 46f;
    private static final String STYLES = "\n" +
            "u {color: green;}\n" +
            "s {color: red;}\n" +
            "p.brk {page-break-before: always;}\n" +
            "body {font-size: 14px;}\n" +
            ".header {font-size: 1.8em; text-align: center; font-weight: bold;}\n";
    private static final String BILL_STYLES = STYLES +
            "@page {margin-left: 31.5px; margin-top: 27.5px;}\n" +
            "body {font-size: 14px;}\n";
    private static final String RESO_STYLES = STYLES +
            "@page {margin-left: 50px; margin-top: 30px;}\n" +
            "body {font-size: 16px;}\n";

    /**
     * Writes bill text in pdf format to the given OutputStream
     *
     * @param bill         Bill - The bill that contains the text to write
     * @param version      Version - Identifies an amendment in the passed in bill that will have its text converted to pdf
     * @param outputStream OutputStream - The stream which will accept the pdf data
     */
    public void writeBillPdf(Bill bill, Version version, OutputStream outputStream) throws IOException, COSVisitorException {
        if (bill == null) {
            throw new IllegalArgumentException("Supplied bill cannot be null when converting to pdf!");
        }
        if (!bill.hasAmendment(version)) {
            throw new BillAmendNotFoundEx(bill.getBaseBillId().withVersion(version));
        }
        BillAmendment ba = bill.getAmendment(version);

        BillTextFormat format = ba.getFullText(HTML).isEmpty() ? PLAIN : HTML;
        String fullText = ba.getFullText(format);
        switch (format) {
            case HTML:
                writeHtmlPdf(bill.isResolution(), fullText, outputStream);
                break;
            case PLAIN:
                writePlainTextPdf(ba.getBillId(), fullText, outputStream);
                break;
            default:
                throw new IllegalStateException("Unable to write pdf for text format: " + format);
        }
    }

    private static void writeHtmlPdf(boolean resolution, String fullTextHtml, OutputStream outputStream) {
        ITextRenderer renderer = new ITextRenderer();
        Document doc = Jsoup.parse(fullTextHtml);
        // Remove backwards html
        doc.getElementsByTag("basefont").remove();
        doc.getElementsByTag("style").remove();
        doc.select("font[size=\"5\"]").forEach(fontEle -> {
            String text = fontEle.getAllElements().stream()
                    .map(Element::textNodes)
                    .flatMap(Collection::stream)
                    .map(TextNode::getWholeText)
                    .collect(Collectors.joining());
            Element replacement = new Element(Tag.valueOf("span"), "/");
            replacement.addClass("header");
            replacement.text(text);
            fontEle.replaceWith(replacement);
        });
        // Add our own styles
        Element styleTag = doc.head().appendElement("style");
        styleTag.appendText(resolution ? RESO_STYLES : BILL_STYLES);
        // Get reformatted html
        StringBuilder htmlBuilder = new StringBuilder();
        doc.children().forEach(child -> writeHtml(child, htmlBuilder, false));
        String formattedHtml = htmlBuilder.toString();
        // Render and output pdf
        renderer.setDocumentFromString(formattedHtml);
        renderer.layout();
        renderer.createPDF(outputStream);
    }

    /**
     *  This method is needed because JSoup does not preserve whitespace when parsing font tags
     *  (These are replaced with <pre class="header"></pre> after parsing.)
     */
    private static void writeHtml(Node node, StringBuilder sBuilder, boolean headerParent) {
        if (node instanceof TextNode) {
            TextNode textNode = (TextNode) node;
            if (headerParent) {
                sBuilder.append(textNode.getWholeText());
            } else {
                sBuilder.append(textNode.outerHtml());
            }
        } else if (node instanceof Element){
            Element ele = (Element) node;
            sBuilder.append("<")
                    .append(ele.tagName())
                    .append(ele.attributes().html());

            boolean header = ele.hasClass("header");

            if (ele.tag().isSelfClosing() && ele.childNodes().isEmpty()) {
                sBuilder.append(" />");
            } else {
                sBuilder.append(">");
                ele.childNodes().forEach(child -> writeHtml(child, sBuilder, headerParent || header));
                sBuilder.append("</")
                        .append(ele.tagName())
                        .append(">");
            }
        }
    }

    private void writePlainTextPdf(BillId billId, String fullText, OutputStream outputStream) throws IOException, COSVisitorException {
        List<List<String>> pages;
        if (StringUtils.isBlank(fullText)) {
            pages = Collections.singletonList(Collections.singletonList(
                    "No full text available for " + billId));
        } else if (billId.getBillType().isResolution()) {
            pages = BillTextUtils.getResolutionPages(fullText);
        } else {
            pages = BillTextUtils.getBillPages(fullText);
        }

        float margin = billId.getBillType().isResolution() ? RESOLUTION_MARGIN : BILL_MARGIN;
        writePages(pages, margin);
        saveDoc(outputStream);
    }
}
