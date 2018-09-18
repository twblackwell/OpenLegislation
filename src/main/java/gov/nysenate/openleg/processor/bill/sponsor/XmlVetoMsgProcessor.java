package gov.nysenate.openleg.processor.bill.sponsor;

import gov.nysenate.openleg.dao.bill.data.VetoDao;
import gov.nysenate.openleg.model.base.SessionYear;
import gov.nysenate.openleg.model.base.Version;
import gov.nysenate.openleg.model.bill.*;
import gov.nysenate.openleg.model.sourcefiles.sobi.SobiFragment;
import gov.nysenate.openleg.model.sourcefiles.sobi.SobiFragmentType;
import gov.nysenate.openleg.processor.base.ParseError;
import gov.nysenate.openleg.processor.bill.AbstractMemoProcessor;
import gov.nysenate.openleg.processor.sobi.SobiProcessor;
import gov.nysenate.openleg.util.XmlHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by uros on 3/2/17.
 */
@Service
public class XmlVetoMsgProcessor extends AbstractMemoProcessor implements SobiProcessor {

    @Autowired
    XmlHelper xmlHelper;

    @Autowired
    private VetoDao vetoDao;

    /** --- RegEx Patterns ---*/
    private static final Pattern datePattern =
            Pattern.compile("TO THE (SENATE|ASSEMBLY):\\s*([a-zA-Z]+ \\d+, \\d+)?");

    private static final Pattern chapterPattern =
            Pattern.compile("CHAPTER (\\d+)");

    private static final Pattern lineReferencePattern =
            Pattern.compile("Bill Page (\\d+), Line (\\d+)( through Line (\\d+))?.*");

    private static final Pattern signerPattern =
            Pattern.compile("\\s*(?:(?:The|This|These) bills? (?:is|are) disapproved\\.)?\\s*\\(signed\\) ([a-zA-Z.'\\- ]*[a-zA-Z.])");


    @Override
    public SobiFragmentType getSupportedType() {
        return SobiFragmentType.VETOMSG;
    }

    @Override
    public void process(SobiFragment fragment) {
        VetoMessage vetoMessage = new VetoMessage();
        LocalDateTime date = fragment.getPublishedDateTime();
        try {
            final Document doc = xmlHelper.parse(fragment.getText());
            final Node vetoMsgNode = xmlHelper.getNode("veto_message", doc);
            final int number = xmlHelper.getInteger("@no", vetoMsgNode);
            final int year = xmlHelper.getInteger("@year", vetoMsgNode);
            final String action = xmlHelper.getString("@action", vetoMsgNode);

            final Version version = Version.ORIGINAL;
            Bill baseBill = null;

            vetoMessage.setVetoNumber(number);
            vetoMessage.setYear(year);

            if (action.equals("remove")) {
                try { // try to remove throw exception if it does not exists
                    VetoMessage vetoM = vetoDao.getVetoMessage(new VetoId(year, number));
                    baseBill = getOrCreateBaseBill(date, vetoM.getBillId(), fragment);
                    baseBill.getVetoMessages().remove(vetoM.getVetoId());
                }
                catch (EmptyResultDataAccessException emptyResultDataAccessException){
                    return;
                }
            } else if (action.equals("replace")) {
                final String billhse = xmlHelper.getNode("veto_message/billhse", doc).getTextContent();
                final String billno = xmlHelper.getNode("veto_message/billno", doc).getTextContent();

                baseBill = getOrCreateBaseBill(date, new BillId(billhse + billno, new SessionYear(year), version), fragment);

                String textWithHTML = getNodeText(vetoMsgNode);
                String cleanText = parsedMemoHTML(textWithHTML);

                vetoMessage.setVetoNumber(number);
                vetoMessage.setYear(year);
                vetoMessage.setMemoText(cleanText);
                vetoMessage.setSession(baseBill.getSession());
                vetoMessage.setBillId(baseBill.getBaseBillId());
                vetoMessage.setModifiedDateTime(date);
                vetoMessage.setPublishedDateTime(date);
                vetoMessage.setType(VetoType.STANDARD);
                parseTextContent(cleanText, vetoMessage);

                baseBill.getVetoMessages().put(vetoMessage.getVetoId(), vetoMessage);
            }
            billIngestCache.set(baseBill.getBaseBillId(), baseBill, fragment);
            checkIngestCache();

        } catch (IOException | SAXException | XPathExpressionException e) {
            throw new ParseError("Error While Parsing vetoMessageXML", e);
        }
    }

    @Override
    public void checkIngestCache() {
        if (!env.isSobiBatchEnabled() || billIngestCache.exceedsCapacity()) {
            flushBillUpdates();
        }
    }

    @Override
    public void postProcess() {
        flushBillUpdates();
    }

    @Override
    public void init() {
        initBase();
    }

    private String getNodeText(Node node) {
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node temp = childNodes.item(i);
            if (temp.getNodeType() == temp.CDATA_SECTION_NODE) {
                return temp.getTextContent();
            }
        }
        return "";
    }

    private void parseTextContent(String data, VetoMessage vetoMessage) {
        StringBuilder text = new StringBuilder();
        text.ensureCapacity(data.length());

        for (String line : data.split("\n")) {
            parseLine(line, vetoMessage);
        }
    }

    private void parseLine (String line, VetoMessage vetoMessage) {
        Matcher dateMatcher = datePattern.matcher(line);
        Matcher chapterMatcher = chapterPattern.matcher(line);
        Matcher lineRefMatcher = lineReferencePattern.matcher(line);
        Matcher signerMatcher = signerPattern.matcher(line);

        if (dateMatcher.find()) {
                if (dateMatcher.group(2)==null) {
                    vetoMessage.setType(VetoType.STANDARD);
                }
                else {
                    vetoMessage.setType(VetoType.LINE_ITEM);
                    vetoMessage.setSignedDate(LocalDate.parse(dateMatcher.group(2), DateTimeFormatter.ofPattern("MMMM d, yyyy")));
                }
            }
        else if (chapterMatcher.find() && vetoMessage.getType() == VetoType.LINE_ITEM) {
                vetoMessage.setChapter(Integer.parseInt(chapterMatcher.group(1)));
        }

        else if (lineRefMatcher.find() && vetoMessage.getType() == VetoType.LINE_ITEM) {
                vetoMessage.setBillPage(Integer.parseInt(lineRefMatcher.group(1)));
                vetoMessage.setLineStart(Integer.parseInt(lineRefMatcher.group(2)));
                if (lineRefMatcher.group(4) == null) {
                    vetoMessage.setLineEnd(vetoMessage.getLineStart());
                }
                else {
                    vetoMessage.setLineEnd(Integer.parseInt(lineRefMatcher.group(4)));
                }
        }
        else if (signerMatcher.find()) {
                vetoMessage.setSigner(signerMatcher.group(1));
        }
    }
}

