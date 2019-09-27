package gov.nysenate.openleg.service.spotcheck.agenda;

import com.google.common.collect.Range;
import gov.nysenate.openleg.model.agenda.Agenda;
import gov.nysenate.openleg.model.agenda.AgendaNotFoundEx;
import gov.nysenate.openleg.model.spotcheck.agenda.AgendaAlertInfoCommittee;
import gov.nysenate.openleg.model.spotcheck.ReferenceDataNotFoundEx;
import gov.nysenate.openleg.service.agenda.data.CachedAgendaDataService;
import gov.nysenate.openleg.service.spotcheck.base.SpotCheckReportRunMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IntervalAgendaReportService extends BaseAgendaCheckReportService{

    @Autowired
    CachedAgendaDataService agendaDataService;

    /** {@inheritDoc} */
    @Override
    protected List<AgendaAlertInfoCommittee> getReferences(LocalDateTime start, LocalDateTime end) throws ReferenceDataNotFoundEx {
        List<AgendaAlertInfoCommittee> references = agendaAlertDao.getAgendaAlertReferences(Range.closed(start, end));
        if (references.isEmpty()) {
            throw new ReferenceDataNotFoundEx(
                    String.format("no agenda references were found within the given range %s to %s", start, end));
        }
        return references;
    }

    /** {@inheritDoc}
     * @param agendaAlertInfoCommittee*/
    @Override
    protected Agenda getAgenda(AgendaAlertInfoCommittee agendaAlertInfoCommittee) throws AgendaNotFoundEx {
        return agendaDataService.getAgenda(agendaAlertInfoCommittee.getWeekOf());
    }

    /** {@inheritDoc} */
    @Override
    protected void setReferenceChecked(AgendaAlertInfoCommittee reference) {
        // Do nothing
    }

    @Override
    protected String getNotes() {
        return "digest";
    }

    @Override
    public SpotCheckReportRunMode getRunMode() {
        return SpotCheckReportRunMode.PERIODIC;
    }
}
