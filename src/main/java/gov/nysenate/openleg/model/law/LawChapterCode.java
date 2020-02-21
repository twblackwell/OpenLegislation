package gov.nysenate.openleg.model.law;

import com.google.common.collect.Sets;

import java.util.*;

import static gov.nysenate.openleg.model.law.LawType.*;

/**
 * Current enumeration of all law chapters. It is possible that new laws chapters may come
 * in as part of an update so this listing should be maintained accordingly.
 */
public enum LawChapterCode
{
    /** --- Consolidated Laws --- */

    ABP("Abandoned Property", Sets.newHashSet("Ab Prop L"), CONSOLIDATED),
    AGM("Agriculture & Markets", Sets.newHashSet("Ag & Mkts L"), CONSOLIDATED),
    ABC("Alcoholic Beverage Control", Sets.newHashSet("ABC L"), CONSOLIDATED),
    ACG("Alternative County Government", Sets.newHashSet(""), CONSOLIDATED),
    ACA("Arts and Cultural Affairs", Sets.newHashSet("Art & Cul L", "Arts & Cul L"), CONSOLIDATED),
    BNK("Banking", Sets.newHashSet("Bank L"),  CONSOLIDATED),
    BVO("Benevolent Orders", Sets.newHashSet("Ben Ord L"), CONSOLIDATED),
    BSC("Business Corporation", Sets.newHashSet("BC L"), CONSOLIDATED),
    CAL("Canal", Sets.newHashSet("Can L"), CONSOLIDATED),
    CVP("Civil Practice Law & Rules", Sets.newHashSet("CPLR", "CPLR L", "CPLR & Department of Law"), CONSOLIDATED),
    CVR("Civil Rights", Sets.newHashSet("Civ Rts L", "Civ Rts"), CONSOLIDATED),
    CVS("Civil Service", Sets.newHashSet("Civ Serv L"), CONSOLIDATED),
    CCO("Cooperative Corporations", Sets.newHashSet("Coop Corps L"), CONSOLIDATED),
    COR("Correction", Sets.newHashSet("Cor L"), CONSOLIDATED),
    CNT("County", Sets.newHashSet("County L"), CONSOLIDATED),
    CPL("Criminal Procedure", Sets.newHashSet("CP L"), CONSOLIDATED),
    DCD("Debtor & Creditor", Sets.newHashSet("D & C L"), CONSOLIDATED),
    DOM("Domestic Relations", Sets.newHashSet("Dom Rel L"), CONSOLIDATED),
    COM("Economic Development Law", Sets.newHashSet("Ec Dev L"), CONSOLIDATED),
    EDN("Education", Sets.newHashSet("Ed L"), CONSOLIDATED),
    ELD("Elder", Sets.newHashSet("Eld L"), CONSOLIDATED),
    ELN("Election", Sets.newHashSet("El L"), CONSOLIDATED),
    EDP("Eminent Domain Procedure", Sets.newHashSet("EDP L"), CONSOLIDATED),
    EML("Employers' Liability", Sets.newHashSet(""), CONSOLIDATED),
    ENG("Energy", Sets.newHashSet("Energy L"), CONSOLIDATED),
    ENV("Environmental Conservation", Sets.newHashSet("En Con L"), CONSOLIDATED),
    EPT("Estates, Powers & Trusts", Sets.newHashSet("EPT L"), CONSOLIDATED),
    EXC("Executive", Sets.newHashSet("Exec L"), CONSOLIDATED),
    FIS("Financial Services Law", Sets.newHashSet("Fin Serv L"), CONSOLIDATED),
    GAS("General Associations", Sets.newHashSet(""), CONSOLIDATED),
    GBS("General Business", Sets.newHashSet("Gen Bus L"), CONSOLIDATED),
    GCT("General City", Sets.newHashSet("Gen City L"), CONSOLIDATED),
    GCN("General Construction", Sets.newHashSet("Gen Con L"), CONSOLIDATED),
    GMU("General Municipal", Sets.newHashSet("Gen Mun L", "Gen Muni L", "Gen Mini L"), CONSOLIDATED),
    GOB("General Obligations", Sets.newHashSet("Gen Ob L"), CONSOLIDATED),
    HAY("Highway", Sets.newHashSet("Hway L"), CONSOLIDATED),
    IND("Indian", Sets.newHashSet("Indian L"), CONSOLIDATED),
    ISC("Insurance", Sets.newHashSet("Ins L"), CONSOLIDATED),
    JUD("Judiciary", Sets.newHashSet("Judy L"), CONSOLIDATED),
    LAB("Labor", Sets.newHashSet("Lab L"), CONSOLIDATED),
    LEG("Legislative", Sets.newHashSet("Leg L"), CONSOLIDATED),
    LIE("Lien", Sets.newHashSet("Lien L"), CONSOLIDATED),
    LLC("Limited Liability Company Law", Sets.newHashSet("Lim Lil L"), CONSOLIDATED),
    LFN("Local Finance", Sets.newHashSet("Loc Fin L"), CONSOLIDATED),
    MHY("Mental Hygiene", Sets.newHashSet("Ment Hgy L", "Ment Hyg L", "Mental Health L", "Ment Health L"), CONSOLIDATED),
    MIL("Military", Sets.newHashSet("Mil L"), CONSOLIDATED),
    MDW("Multiple Dwelling", Sets.newHashSet("Mult Dwell L"), CONSOLIDATED),
    MRE("Multiple Residence", Sets.newHashSet("Mult Res L"), CONSOLIDATED),
    MHA("Municipal Housing Authorities", Sets.newHashSet(""), CONSOLIDATED),
    MHR("Municipal Home Rule", Sets.newHashSet("Munic Home R L", "Munic Home Rule L"), CONSOLIDATED),
    NAV("Navigation", Sets.newHashSet("Nav L"), CONSOLIDATED),
    PPD("New York State Printing and Public Documents", Sets.newHashSet("NYS Print L"), CONSOLIDATED),
    NPC("Not-For-Profit Corporation", Sets.newHashSet("N-PC L"), CONSOLIDATED),
    PAR("Parks, recreation and historic preservation", Sets.newHashSet("Pk & Rec L", "Pks & Rec L", "Rec & Pks L"), CONSOLIDATED),
    PTR("Partnership", Sets.newHashSet("Partn L"), CONSOLIDATED),
    PEN("Penal", Sets.newHashSet("Pen L"), CONSOLIDATED),
    PEP("Personal Property", Sets.newHashSet("Pers Prop L", "Per Prop L"), CONSOLIDATED),
    PVH("Private Housing Finance", Sets.newHashSet("Priv Hous Fin L", "Pr Hous Fin L"), CONSOLIDATED),
    PBA("Public Authorities", Sets.newHashSet("Pub Auth L"), CONSOLIDATED),
    PBB("Public Buildings", Sets.newHashSet("Pub Bldg L", "Pub Bldgs L"), CONSOLIDATED),
    PBH("Public Health", Sets.newHashSet("Pub Health L"), CONSOLIDATED),
    PBG("Public Housing", Sets.newHashSet("Pub Hous L"), CONSOLIDATED),
    PBL("Public Lands", Sets.newHashSet("Pub Lds L"), CONSOLIDATED),
    PBO("Public Officers", Sets.newHashSet("Pub Off L"), CONSOLIDATED),
    PBS("Public Service", Sets.newHashSet("Pub Serv L"), CONSOLIDATED),
    PML("Racing, Pari-Mutuel Wagering and Breeding Law", Sets.newHashSet("RWB L"), CONSOLIDATED),
    RRD("Railroad", Sets.newHashSet("Rail L"), CONSOLIDATED),
    RAT("Rapid Transit", Sets.newHashSet(""), CONSOLIDATED),
    RPP("Real Property", Sets.newHashSet("RP L"), CONSOLIDATED),
    RPA("Real Property Actions & Proceedings", Sets.newHashSet("RPAP L", "RPAP"), CONSOLIDATED),
    RPT("Real Property Tax", Sets.newHashSet("RPT L"), CONSOLIDATED),
    RCO("Religious Corporations", Sets.newHashSet("Rel Corp L"), CONSOLIDATED),
    RSS("Retirement & Social Security", Sets.newHashSet("R & SS L"), CONSOLIDATED),
    REL("Rural Electric Cooperative", Sets.newHashSet("Rur Elec Coop L"), CONSOLIDATED),
    SCC("Second Class Cities", Sets.newHashSet(""), CONSOLIDATED),
    SOS("Social Services", Sets.newHashSet("Soc Serv L"), CONSOLIDATED),
    SWC("Soil & Water Conservation Districts", Sets.newHashSet("Cons Dists L"), CONSOLIDATED),
    STL("State", Sets.newHashSet("St L", " State L"), CONSOLIDATED),
    SAP("State Administrative Procedure Act", Sets.newHashSet("St Ad Proc Act", "St Ad Proc L"), CONSOLIDATED),
    STF("State Finance", Sets.newHashSet("St Fin L", "St. Fin L", "St Fi", "St Fin", "St Fin L."), CONSOLIDATED),
    STT("State Technology", Sets.newHashSet("St Tech L", "St Te"), CONSOLIDATED),
    SLG("Statute of Local Governments", Sets.newHashSet(""), CONSOLIDATED),
    TAX("Tax", Sets.newHashSet("Tax L", " Tax Law"), CONSOLIDATED),
    TWN("Town", Sets.newHashSet("Town L"), CONSOLIDATED),
    TRA("Transportation", Sets.newHashSet("Transp L"), CONSOLIDATED),
    TCP("Transportation Corporations", Sets.newHashSet("Transp Corps L"), CONSOLIDATED),
    UCC("Uniform Commercial Code", Sets.newHashSet("UCC"), CONSOLIDATED),
    VAT("Vehicle & Traffic", Sets.newHashSet("V & T L"), CONSOLIDATED),
    VIL("Village", Sets.newHashSet("Vil L"), CONSOLIDATED),
    VAW("Volunteer Ambulance Workers' Benefit", Sets.newHashSet("Vol Amb Wkr Ben L", "Vol Amb Work Ben L"), CONSOLIDATED),
    VOL("Volunteer Firefighters' Benefit", Sets.newHashSet("Vol Ffs Ben L"), CONSOLIDATED),
    WKC("Workers' Compensation", Sets.newHashSet("Work Comp L"), CONSOLIDATED),

    /** --- Unconsolidated Laws --- */

    BSW("Boxing, Sparring and Wrestling Ch. 912/20", Sets.newHashSet("Chap 912 of 1920"), UNCONSOLIDATED),
    BAT("Bridges and Tunnels New York/New Jersey 47/31", Sets.newHashSet("Chap 47 of 1931"), UNCONSOLIDATED),
    CCT("Cigarettes, Cigars, Tobacco 235/52", Sets.newHashSet("Chap 235 of 1952"), UNCONSOLIDATED),
    TRY("City of Troy Issuance of Serial Bonds", Sets.newHashSet(""), UNCONSOLIDATED),
    DEA("Defense Emergency Act 1951 784/51", Sets.newHashSet("Chap 784 of 1951"), UNCONSOLIDATED),
    DPN("Development of Port of New York 43/22", Sets.newHashSet("Chap 43 of 1922"), UNCONSOLIDATED),
    ETP("Emergency Tenant Protection Act 576/74", Sets.newHashSet("Emerg Ten Prot Act", "Emerg Ten Prot Act of 1974", "Chap 576 of 1974"), UNCONSOLIDATED),
    EHC("Expanded Health Care Coverage Act 703/88", Sets.newHashSet("Chap 703 of 1988"), UNCONSOLIDATED),
    FEA("NYS Financial Emergency Act for the city of NY 868/75", Sets.newHashSet("Chap 868 of 1975"), UNCONSOLIDATED),
    NYP("NYS Project Finance Agency Act 7/75", Sets.newHashSet("Chap 7 of 1975"), UNCONSOLIDATED),
    YFA("Yonkers financial emergency act 103/84", Sets.newHashSet("Chap 103 of 1984"), UNCONSOLIDATED),
    YTS("Yonkers income tax surcharge", Sets.newHashSet(""), UNCONSOLIDATED),
    FDC("Facilities Development Corporation Act 359/68", Sets.newHashSet("Fac Dev Corp Act", "Chap 359 of 1968"), UNCONSOLIDATED),
    GCM("General City Model 772/66", Sets.newHashSet("Chap 772 of 1966"), UNCONSOLIDATED),
    LEH("Local Emergency Housing Rent Control Act 21/62", Sets.newHashSet("Chap 21 of 1962"), UNCONSOLIDATED),
    ERL("Emergency Housing Rent Control Law 274/46 337/61", Sets.newHashSet("Emerg Hous Rent Cont L", "Chap 274 of 1946"), UNCONSOLIDATED),
    LSA("Lost and Strayed Animals 115/1894", Sets.newHashSet("Chap 115 of 1894"), UNCONSOLIDATED),
    MCF("Medical Care Facilities Finance Agency 392/73", Sets.newHashSet("Chap 392 of 1973", "NYS Med Care Fac Fin Ag Act"), UNCONSOLIDATED),
    NYW("N. Y. wine/grape 80/85", Sets.newHashSet("Chap 80 of 1985"), UNCONSOLIDATED),
    HHC("New York City health and hospitals corporation act 1016/69", Sets.newHashSet("NYC Health & Hosp Corp Act", "Chap 1016 of 1969"), UNCONSOLIDATED),
    PCM("Police Certain Municipalities 360/11", Sets.newHashSet("Chap 360 of 1911"), UNCONSOLIDATED),
    PNY("Port of New York Authority 154/21", Sets.newHashSet("Chap 154 of 1921"), UNCONSOLIDATED),
    POA("Port of Albany 192/25", Sets.newHashSet("Chap 192 of 1925"), UNCONSOLIDATED),
    PAB("Private Activity Bond 47/90", Sets.newHashSet("Chap 47 of 1990"), UNCONSOLIDATED),
    RLA("Regulation of Lobbying Act 1040/81", Sets.newHashSet("Chap 1040 of 1981"), UNCONSOLIDATED),
    SNH("Special Needs Housing Act 261/88", Sets.newHashSet("Chap 261 of 1988"), UNCONSOLIDATED),
    SCT("Suffolk County Tax Act", Sets.newHashSet(""), UNCONSOLIDATED),
    TSF("Tobacco Settlement Financing Corporation Act", Sets.newHashSet(""), UNCONSOLIDATED),
    UDG("Urban development guarantee fund of New York 175/68", Sets.newHashSet("Chap 175 of 1968"), UNCONSOLIDATED),
    UDA("Urban Development Corporation Act 174/68", Sets.newHashSet("UDC Act", "UDCA"), UNCONSOLIDATED),
    UDR("Urban development research corporation act 173/68", Sets.newHashSet("Chap 173 of 1968"), UNCONSOLIDATED),
    NNY("New, New York Bond Act 649/92", Sets.newHashSet("Chap 649 of 1992"), UNCONSOLIDATED),

    /** --- Court Acts --- */

    CTC("Court of Claims", Sets.newHashSet("Ct Claims Act"), COURT_ACTS),
    FCT("Family Court", Sets.newHashSet("Fam Ct Act"), COURT_ACTS),
    CCA("New York City Civil Court", Sets.newHashSet("NYC Civ Ct Act"), COURT_ACTS),
    CRC("New York City Criminal Court", Sets.newHashSet("NYC Crim Ct Act"), COURT_ACTS),
    SCP("Surrogate's Court Procedure", Sets.newHashSet("SCPA"), COURT_ACTS),
    UCT("Uniform City Court", Sets.newHashSet("UCCA"), COURT_ACTS),
    UDC("Uniform District Court", Sets.newHashSet(""), COURT_ACTS),
    UJC("Uniform Justice Court", Sets.newHashSet("UJCA"), COURT_ACTS),

    /** --- Rules --- */

    CMA("Assembly Rules", Sets.newHashSet(""), RULES),
    CMS("Senate Rules", Sets.newHashSet(""), RULES),

    /** --- Misc --- */

    CNS("Constitution", Sets.newHashSet("Constn", "Constn L"), MISC),
    ADC("New York City Administrative Code", Sets.newHashSet("NYC Ad Cd"), MISC),
    NYC("New York City Charter", Sets.newHashSet("NYC Chart"), MISC);

    /** --- Fields --- */

    private String name;
    private Set<String> citations;
    private LawType type;

    private static Map<String, LawChapterCode> citationMap = new HashMap<>();
    static {
        Arrays.stream(values())
                .forEach(law -> law.getCitations()
                        .forEach(citation -> {
                            if (citation != null && !citation.trim().isEmpty()) {
                                citationMap.put(citation.toUpperCase().trim(), law);
                            }
                        }));
    }

    /** --- Constructor --- */

    LawChapterCode(String name, Set<String> citations, LawType type) {
        this.name = name;
        this.citations = citations;
        this.type = type;
    }

    /**
     * Returns the LawChapterType that has a citation that matches the one provided.
     *
     * @param citation String
     * @return Optional<LawChapterType> - Matching LawChapterType or empty optional otherwise..
     * @throws java.lang.IllegalArgumentException - If the citation provided is null.
     */
    public static Optional<LawChapterCode> lookupCitation(String citation) {
        if (citation == null)
            throw new IllegalArgumentException("Null citation supplied.");
        citation = citation.toUpperCase().trim().replaceAll("(\\s{2,})", " ");
        LawChapterCode type = citationMap.get(citation);
        // Try varations of the code with or without the L.
        if (type == null) {
            if (citation.endsWith(" L"))
                type = citationMap.get(citation.substring(0, citation.length() - 2));
            else
                type = citationMap.get(citation + " L");
        }
        return Optional.ofNullable(type);
    }

    /**
     * For use in LawTitleParser.
     * @param lawId to check
     * @return if the law chapter is unconsolidated.
     */
    public static boolean isUnconsolidated(String lawId) {
        return LawChapterCode.valueOf(lawId).type == UNCONSOLIDATED;
    }

    /** --- Basic Getters --- */

    public String getName() {
        return name;
    }

    public Set<String> getCitations() {
        return citations;
    }

    public LawType getType() {
        return type;
    }

    public boolean hasNumericalTitles() {
        final Set<LawChapterCode> nonNumericalVolumes = Sets.newHashSet(LawChapterCode.ACA,
                LawChapterCode.CPL, LawChapterCode.CVS, LawChapterCode.PAR, LawChapterCode.MHY, LawChapterCode.PEN);
        return !nonNumericalVolumes.contains(this);
    }
}
