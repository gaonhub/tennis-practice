package myweb.secondboard.repository;

import myweb.secondboard.domain.Matching;

public interface MatchRepositoryInterface {
    Matching findOne(Long matchId);

    void increasePlayer(Long matchId);

    void matchConditionCheck(Long matchId);
}
