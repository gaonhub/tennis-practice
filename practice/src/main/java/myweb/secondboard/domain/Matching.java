package myweb.secondboard.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import myweb.secondboard.dto.MatchSaveForm;
import myweb.secondboard.dto.MatchUpdateForm;
import myweb.secondboard.web.CourtType;
import myweb.secondboard.web.MatchCondition;
import myweb.secondboard.web.MatchType;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
public class Matching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @NotNull
    @Column(length = 31)
    private String matchTitle;

    @NotNull
    @Column(length = 31)
    private LocalDate matchDate;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @Enumerated(EnumType.STRING)
    private CourtType courtType;

    @Enumerated(EnumType.STRING)
    private MatchCondition matchCondition;

    @Column(columnDefinition = "integer default 1")
    private Integer playerNumber;

    @NotNull
    @Column(length = 31)
    private String matchPlace;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    public static Matching createMatch(MatchSaveForm match, Member member) {
        Matching matching = new Matching();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        matching.setMatchTitle(match.getMatchTitle());
        matching.setMatchDate(match.getMatchDate());
        matching.setStartTime(match.getStartTime());
        matching.setEndTime(match.getEndTime());
        matching.setMatchType(match.getMatchType());
        matching.setCourtType(match.getCourtType());
        matching.setMatchPlace(match.getMatchPlace());
        matching.setMatchCondition(MatchCondition.AVAILABLE);
        matching.setPlayerNumber(match.getPlayerNumber());
        matching.setMember(member);
        return matching;
    }

    public void update(MatchUpdateForm match, Member member, Matching matching) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

        matching.setId(match.getId());
        matching.setMatchTitle(match.getMatchTitle());
        matching.setMatchDate(match.getMatchDate());
        matching.setStartTime(match.getStartTime());
        matching.setEndTime(match.getEndTime());
        matching.setMatchType(match.getMatchType());
        matching.setCourtType(match.getCourtType());
        matching.setMatchPlace(match.getMatchPlace());
        matching.setMember(member);
    }


    public void increasePlayer(Matching matching) {
        matching.setPlayerNumber(matching.getPlayerNumber() + 1);
    }

    public void matchConditionCheck(Matching matching) {
        if (matching.getPlayerNumber() == 2 && matching.getMatchType().getTitle() == "단식") {
            matching.setMatchCondition(MatchCondition.DONE);
        }
        if (matching.getPlayerNumber() == 4 && matching.getMatchType().getTitle() == "복식") {
            matching.setMatchCondition(MatchCondition.DONE);
        }
    }
}
