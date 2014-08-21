package gov.nysenate.openleg.service.spotcheck;

import com.google.common.base.Strings;
import gov.nysenate.openleg.dao.daybreak.DaybreakDao;
import gov.nysenate.openleg.model.base.PublishStatus;
import gov.nysenate.openleg.model.base.Version;
import gov.nysenate.openleg.model.bill.BaseBillId;
import gov.nysenate.openleg.model.bill.Bill;
import gov.nysenate.openleg.model.bill.BillAction;
import gov.nysenate.openleg.model.bill.BillSponsor;
import gov.nysenate.openleg.model.daybreak.DaybreakBill;
import gov.nysenate.openleg.model.entity.Member;
import gov.nysenate.openleg.model.spotcheck.ReferenceDataNotFoundEx;
import gov.nysenate.openleg.model.spotcheck.SpotCheckMismatch;
import gov.nysenate.openleg.model.spotcheck.SpotCheckObservation;
import gov.nysenate.openleg.util.BillTextUtils;
import gov.nysenate.openleg.util.DateUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static gov.nysenate.openleg.model.spotcheck.SpotCheckMismatchType.*;
import static java.util.stream.Collectors.joining;

@Service("daybreak")
public class DaybreakCheckService implements SpotCheckBillService
{
    @Autowired
    protected DaybreakDao daybreakDao;

    /** --- Implemented Methods --- */

    /** {@inheritDoc} */
    @Override
    public SpotCheckObservation<BaseBillId> check(Bill bill) throws ReferenceDataNotFoundEx {
        return check(bill, LocalDateTime.now());
    }

    /** {@inheritDoc} */
    @Override
    public SpotCheckObservation<BaseBillId> check(Bill bill, LocalDateTime latestDateTime)
                                                  throws ReferenceDataNotFoundEx {
        if (bill == null) {
            throw new IllegalArgumentException("Supplied bill cannot be null");
        }
        BaseBillId baseBillId = bill.getBaseBillId();
        final DaybreakBill daybreakBill;
//        try {
            daybreakBill = new DaybreakBill(); // TODO: Use dao here.. Get the latest matching DaybreakBill
//        }
//        catch (ReferenceDataNotFoundEx ex) {
//
//        }


        final SpotCheckObservation<BaseBillId> observation = new SpotCheckObservation<>(daybreakBill, baseBillId);

        // Perform the checks
        checkBillTitle(bill, daybreakBill, observation);
        checkBillLawAndSummary(bill, daybreakBill, observation);
        checkBillSponsor(bill, daybreakBill, observation);
        checkCoSponsors(bill, daybreakBill, observation);
        checkMultiSponsors(bill, daybreakBill, observation);
        checkBillActions(bill, daybreakBill, observation);
        checkFullTextPageCounts(bill, daybreakBill, observation);
        checkActiveVersions(bill, daybreakBill, observation);

        return observation;
    }

    /** --- Methods --- */

    /**
     * Check that the active version matches and also that only amendments before and including the active version
     * are published.
     */
    protected void checkActiveVersions(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        Version daybreakActiveVersion = daybreakBill.getActiveVersion();
        if (!daybreakActiveVersion.equals(bill.getActiveVersion())) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_ACTIVE_AMENDMENT, daybreakActiveVersion.name(),
                ((bill.getActiveVersion() != null) ? bill.getActiveVersion().name() : "NULL")));
        }

        String daybreakPubVersionsStr = publishedVersionsString(daybreakActiveVersion);
        String billPubVersionsStr = publishedVersionsString(bill);
        if (!daybreakPubVersionsStr.equals(billPubVersionsStr)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_AMENDMENT_PUBLISH, daybreakPubVersionsStr, billPubVersionsStr));
        }
    }

    /**
     * Checks the full text page counts for each amendment version. The page count for the daybreak comes from
     * the page file.
     */
    protected void checkFullTextPageCounts(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        Map<Version, Integer> billPageCounts = new HashMap<>();
        Map<Version, Integer> daybreakPageCounts = new HashMap<>();
        bill.getAmendmentMap().forEach((k,v) -> billPageCounts.put(k, BillTextUtils.getPageCount(v.getFullText())));
        daybreakBill.getAmendments().forEach((k, v) -> daybreakPageCounts.put(k, v.getPageCount()));
        if (!daybreakPageCounts.equals(billPageCounts)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_FULLTEXT_PAGE_COUNT, daybreakPageCounts.toString(),
                                                                              billPageCounts.toString()));
        }
    }

    /**
     * Compare the actions in the daybreak with the bill's actions.
     */
    protected void checkBillActions(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        if (daybreakBill.getActions() != null && !daybreakBill.getActions().equals(bill.getActions())) {
            String daybreakActionsStr = actionsListString(daybreakBill.getActions());
            String billActionsStr = actionsListString(bill.getActions());
            obsrv.addMismatch(new SpotCheckMismatch(BILL_ACTION, daybreakActionsStr, billActionsStr));
        }
    }

    /**
     * Check the active bill amendment's multisponsor list.
     */
    protected void checkMultiSponsors(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        List<Member> billMultiSponsors = bill.hasActiveAmendment() ? bill.getActiveAmendment().getMultiSponsors()
                                                                   : new ArrayList<>();
        String daybreakMultiSponsorsStr = StringUtils.join(daybreakBill.getMultiSponsors(), " ");
        String billMultiSponsorsStr = memberListString(billMultiSponsors);
        if (!stringEquals(daybreakMultiSponsorsStr, billMultiSponsorsStr, true, true)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_MULTISPONSOR, daybreakMultiSponsorsStr, billMultiSponsorsStr));
        }
    }

    /**
     * Check the active bill amendment's cosponsor list.
     */
    protected void checkCoSponsors(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        List<Member> billCoSponsors = bill.hasActiveAmendment() ? bill.getActiveAmendment().getCoSponsors()
                                                                : new ArrayList<>();
        String daybreakCoSponsorsStr = StringUtils.join(daybreakBill.getCosponsors(), " ");
        String billCoSponsorsStr = memberListString(billCoSponsors);
        if (!stringEquals(daybreakCoSponsorsStr, billCoSponsorsStr, true, true)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_COSPONSOR, daybreakCoSponsorsStr, billCoSponsorsStr));
        }
    }

    /**
     * Check the BillSponsor by comparing the string representation. The toString method for BillSponsor should
     * produce the same formatting as the sponsor string found in the DaybreakBill.
     */
    protected void checkBillSponsor(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        String billSponsorStr = sponsorString(bill.getSponsor());
        if (!stringEquals(daybreakBill.getSponsor(), billSponsorStr, true, true)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_SPONSOR, daybreakBill.getSponsor(), billSponsorStr));
        }
    }

    /**
     * The daybreaks concatenate the law code and the summary. Since it's not trivial to parse this out, we simply
     * concatenate our own law and summary and just compare the strings.
     */
    protected void checkBillLawAndSummary(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        String billLawSummary = (Strings.nullToEmpty(bill.getLaw()) + " " +
                                 Strings.nullToEmpty(bill.getSummary())).trim();
        billLawSummary = billLawSummary.replace('§', 'S').replace('¶', 'P');
        String dayBreakLawSummary = daybreakBill.getLawCodeAndSummary();
        if (!stringEquals(billLawSummary, dayBreakLawSummary, false, true)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_LAW_CODE_SUMMARY, dayBreakLawSummary, billLawSummary));
        }
    }

    /**
     * Compare the bill title with the daybreak title.
     */
    protected void checkBillTitle(Bill bill, DaybreakBill daybreakBill, SpotCheckObservation<BaseBillId> obsrv) {
        if (!stringEquals(daybreakBill.getTitle(), bill.getTitle(), false, true)) {
            obsrv.addMismatch(new SpotCheckMismatch(BILL_TITLE, daybreakBill.getTitle(), bill.getTitle()));
        }
    }

    /** --- Internal --- */

    /**
     * Returns a string with any html escapes converted to unicode.
     */
    private String unescapeHTML(String text) {
        return StringEscapeUtils.unescapeHtml4(text).replace("&apos;", "'");
    }

    /**
     * Compare two strings a and b with the option to ignore case and extra whitespace.
     */
    protected boolean stringEquals(String a, String b, boolean ignoreCase, boolean normalizeSpaces) {
        // Convert null values to empty strings.
        a = (a == null) ? "" : a;
        b = (b == null) ? "" : b;
        // Remove excess spaces if requested
        if (normalizeSpaces) {
            a = a.replaceAll("\\s+", " ");
            b = b.replaceAll("\\s+", " ");
        }
        return (ignoreCase) ? StringUtils.equalsIgnoreCase(a,b) : StringUtils.equals(a,b);
    }

    /**
     * Convert a BillSponsor to its string representation.
     */
    protected String sponsorString(BillSponsor billSponsor) {
        return (billSponsor != null) ? billSponsor.toString() : "NULL";
    }

    /**
     * Convert a member list into a string containing the short names, delimited by a space.
     */
    protected String memberListString(List<Member> members) {
        String membersStr = "";
        if (members != null) {
            membersStr = members.stream().map(Member::getLbdcShortName).collect(joining(" "));
        }
        return membersStr.toUpperCase();
    }

    /**
     * Convert the actions list into a string with each line e.g SENATE - 12/16/13 PRINT NUMBER 1234A\n.
     */
    protected String actionsListString(List<BillAction> actions) {
        String actionsStr = "";
        if (actions != null) {
            actionsStr = actions.stream()
                .map(a -> a.getChamber() + " - " + DateUtils.LRS_ACTIONS_DATE.format(a.getDate()) + " " + a.getText())
                .collect(joining("\n"));
        }
        return actionsStr.toUpperCase();
    }

    /**
     * Given the bill return a single string that has the name of each published version,
     * e.g. 'DEFAULT A B C' if the base and amendments A, B, and C are all published. If
     * the amendment is missing from the bill but is in the publish map, tack on a [MISSING_DATA]
     * like B[MISSING_DATA]
     */
    protected String publishedVersionsString(Bill bill) {
        return bill.getAmendPublishStatusMap().entrySet().stream()
            .filter(e -> e.getValue().isPublished())
            .map(e -> e.getKey().name() + ((!bill.hasAmendment(e.getKey())) ? "[MISSING_DATA]" : ""))
            .collect(joining(" "));
    }

    /**
     * Given an active amendment version, return a string that has the names of every version before
     * and including the active version.
     */
    protected String publishedVersionsString(Version activeVersion) {
        return Arrays.asList(Version.values()).stream()
            .filter(v -> v.compareTo(activeVersion) <= 0).map(v -> v.name()).collect(joining(" "));
    }
}