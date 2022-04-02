package gov.nysenate.openleg.legislation.member;

import com.google.common.base.Objects;
import gov.nysenate.openleg.legislation.committee.Chamber;

/**
 * Represents a person holding a specific office
 */
public class Member extends Person {

    /** Unique member id generated by the persistence layer. */
    protected int memberId;

    /** The legislative chamber this member is associated with. */
    protected Chamber chamber;

    /** Indicates if the member is currently an incumbent. */
    protected boolean incumbent;

    public Member() {}

    public Member(int memberId) {
        this.memberId = memberId;
    }

    public Member(Person person, int memberId, Chamber chamber, boolean incumbent) {
        super(person);
        this.memberId = memberId;
        this.chamber = chamber;
        this.incumbent = incumbent;
    }

    public Member(Member member) {
        super(member);
        this.memberId = member.memberId;
        this.chamber = member.chamber;
        this.incumbent = member.incumbent;
    }

    /**
     * Updates a Members fields to be equal to other.
     * @param other to copy from.
     */
    public void updateFromOther(Member other) {
        super.updateFromOther(other);
        this.memberId = other.getMemberId();
        this.chamber = other.getChamber();
        this.incumbent = other.incumbent;
    }

    /** --- Overrides --- */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member member)) return false;
        if (!super.equals(o)) return false;
        return memberId == member.memberId &&
                chamber == member.chamber &&
                incumbent == member.incumbent;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(super.hashCode(), memberId, chamber, incumbent);
    }

    /** --- Getters / Setters --- */

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Chamber getChamber() {
        return chamber;
    }

    public void setChamber(Chamber chamber) {
        this.chamber = chamber;
    }

    public boolean isIncumbent() {
        return this.incumbent;
    }

    public void setIncumbent(boolean incumbent) {
        this.incumbent = incumbent;
    }
}
