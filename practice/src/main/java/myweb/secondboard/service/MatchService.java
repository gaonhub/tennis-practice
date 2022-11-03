package myweb.secondboard.service;

import lombok.RequiredArgsConstructor;
import myweb.secondboard.domain.Matching;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Player;
import myweb.secondboard.dto.MatchSaveForm;
import myweb.secondboard.dto.MatchUpdateForm;
import myweb.secondboard.repository.MatchRepository;
import myweb.secondboard.repository.PlayerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository matchRepository;

    private final PlayerRepository playerRepository;

    public Matching findOne(Long matchId) {
        return matchRepository.findOne(matchId);
    }

    @Transactional
    public Long addMatch(MatchSaveForm form, Member member) {
        Matching matching = Matching.createMatch(form, member);
        Player player = Player.createPlayer(matching, member);
        matchRepository.save(matching);
        playerRepository.save(player);
        return matching.getId();
    }

    @Transactional
    public Page<Matching> getMatchList(Pageable pageable) {
        return matchRepository.findAll(pageable);
    }


    @Transactional
    public void update(MatchUpdateForm match, Member member) {
        Matching matching = matchRepository.findOne(match.getId());
        matching.update(match, member, matching);
    }

    @Transactional
    public void deleteById(Long matchId, List<Player> players) {
        playerRepository.deleteAllInBatch(players);
        matchRepository.deleteById(matchId);
    }

    @Transactional
    public void increasePlayerNumber(Long matchId) {
        matchRepository.increasePlayer(matchId);
    }

    @Transactional
    public void matchConditionCheck(Long matchId) {
        matchRepository.matchConditionCheck(matchId);
    }

    public Player playerCheck(Long matchId, Long memberId) {
        Optional<Player> result = playerRepository.exist(matchId, memberId);
        if (result.isEmpty()) {
            return null;
        }
        return result.get();
    }

    public void deleteMatchPlayer(Long matchingId, Long memberId) {
        Player player = playerCheck(matchingId, memberId);
        playerRepository.delete(player);
        Matching matching = matchRepository.findOne(matchingId);
        matching.setPlayerNumber(matching.getPlayerNumber() - 1);
    }
}
