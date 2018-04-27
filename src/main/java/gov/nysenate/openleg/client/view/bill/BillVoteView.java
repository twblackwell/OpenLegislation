package gov.nysenate.openleg.client.view.bill;

import gov.nysenate.openleg.client.view.base.ListView;
import gov.nysenate.openleg.client.view.base.MapView;
import gov.nysenate.openleg.client.view.base.ViewObject;
import gov.nysenate.openleg.client.view.committee.CommitteeIdView;
import gov.nysenate.openleg.client.view.entity.MemberView;
import gov.nysenate.openleg.model.bill.BillVote;
import gov.nysenate.openleg.model.bill.BillVoteCode;
import gov.nysenate.openleg.model.bill.BillVoteType;

import java.time.LocalDate;
import java.util.stream.Collectors;

public class BillVoteView implements ViewObject
{
    protected BillIdView billId;
    protected String version;
    protected BillVoteType voteType;
    protected LocalDate voteDate;
    protected CommitteeIdView committee;
    protected MapView<String, ListView<MemberView>> memberVotes;

    public BillVoteView(BillVote billVote) {
        if(billVote != null) {
            this.billId = new BillIdView(billVote.getBillId());
            this.version = new BillIdView(billVote.getBillId()).getVersion();
            this.voteType = billVote.getVoteType();
            this.voteDate = billVote.getVoteDate();
            this.committee = billVote.getCommitteeId() != null ? new CommitteeIdView(billVote.getCommitteeId()) : null;
            this.memberVotes = MapView.of(
                billVote.getMemberVotes().keySet().stream()
                    .collect(Collectors.toMap(BillVoteCode::name, voteCode ->
                        ListView.of(billVote.getMembersByVote(voteCode).stream()
                            .map(m -> new MemberView(m))
                            .sorted((o1, o2) -> o1.getShortName().compareTo(o2.getShortName()))
                            .collect(Collectors.toList()))))
            );
        }
    }
    public BillVoteView(){

    }

    public BillIdView getBillId() {
        return billId;
    }

    public String getVersion() {
        return version;
    }

    public void setVoteDate(String date){
        voteDate = LocalDate.parse(date);
    }

    public BillVoteType getVoteType() {
        return voteType;
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public CommitteeIdView getCommittee() {
        return committee;
    }

    public MapView<String, ListView<MemberView>> getMemberVotes() {
        return memberVotes;
    }

    @Override
    public String getViewType() {
        return "bill-vote";
    }
}