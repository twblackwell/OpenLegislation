package gov.nysenate.openleg.api.legislation.bill.view;

import gov.nysenate.openleg.api.ViewObject;
import gov.nysenate.openleg.legislation.bill.ApprovalMessage;

public class ApprovalMessageView implements ViewObject
{
    protected BillIdView billId;
    protected int year;
    protected int approvalNumber;
    protected int chapter;
    protected String signer;
    protected String text;

    public ApprovalMessageView(){}
    public ApprovalMessageView(ApprovalMessage approvalMessage) {
        if (approvalMessage != null) {
            this.billId = new BillIdView(approvalMessage.getBillId());
            this.year = approvalMessage.getYear();
            this.approvalNumber = approvalMessage.getApprovalNumber();
            this.chapter = approvalMessage.getChapter();
            this.signer = approvalMessage.getSigner();
            this.text = approvalMessage.getMemoText();
        }
    }

    @Override
    public String getViewType() {
        return "approval-message";
    }

    public BillIdView getBillId() {
        return billId;
    }

    public int getYear() {
        return year;
    }

    public int getApprovalNumber() {
        return approvalNumber;
    }

    public int getChapter() {
        return chapter;
    }

    public String getSigner() {
        return signer;
    }

    public String getText() {
        return text;
    }
}
