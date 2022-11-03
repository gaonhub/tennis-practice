package myweb.secondboard.controller;

import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myweb.secondboard.domain.Matching;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Player;
import myweb.secondboard.dto.MatchSaveForm;
import myweb.secondboard.dto.MatchUpdateForm;
import myweb.secondboard.dto.PlayerAddForm;
import myweb.secondboard.service.MatchService;
import myweb.secondboard.service.PlayerService;
import myweb.secondboard.web.SessionConst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/match")
@Controller
public class MatchController {


    private final MatchService matchService;

    private final PlayerService playerService;

    @GetMapping("/home")
    public String matchHome(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
                            Pageable pageable, Model model) {

        Page<Matching> matchList = matchService.getMatchList(pageable);
        int nowPage = matchList.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 9, matchList.getTotalPages());

        model.addAttribute("matchList", matchList);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "/match/matchHome";

    }

    @GetMapping("/matchAdd")
    public String matchAddForm(Model model) {
        model.addAttribute("match", new MatchSaveForm());
        return "/match/matchAddForm";
    }

    @PostMapping("/new")
    public String matchAdd(@Validated @ModelAttribute("match") MatchSaveForm match,
                           BindingResult bindingResult, HttpServletRequest request) {

        Member member = (Member) request.getSession(false)
                .getAttribute(SessionConst.LOGIN_MEMBER); //로그인검사

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "/match/matchAddForm";
        }


        Long matchId = matchService.addMatch(match, member);
        return "redirect:/match/detail/" + matchId;
    }

    @GetMapping("/detail/{matchId}")
    public String matchDetail(@PathVariable("matchId") Long matchId, Model model, HttpServletRequest request) {
        Matching matching = matchService.findOne(matchId);
        model.addAttribute("match", matching);

        List<Player> players = playerService.findAllByMatchingId(matchId);
        List<Player> playersA = players.stream().filter(m -> m.getTeam().toString().equals("A")).toList();
        List<Player> playersB = players.stream().filter(m -> m.getTeam().toString().equals("B")).toList();

        model.addAttribute("playersA", playersA);
        model.addAttribute("playersB", playersB);
        model.addAttribute("playerAddForm", new PlayerAddForm());

        HttpSession session = request.getSession(false);

        if (session != null) {
            Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
            Player playerCheck = matchService.playerCheck(matchId, member.getId());
            model.addAttribute("playerCheck", playerCheck);
        }

        return "/match/matchDetail";
    }

    @GetMapping("/update/{matchId}")
    public String matchUpdateForm(@PathVariable("matchId") Long matchId, Model model) {
        Matching matching = matchService.findOne(matchId);

        MatchUpdateForm matchUpdateForm = new MatchUpdateForm();

        matchUpdateForm.setId(matching.getId());
        matchUpdateForm.setMatchTitle(matching.getMatchTitle());
        matchUpdateForm.setMatchDate(matching.getMatchDate());
        matchUpdateForm.setStartTime(matching.getStartTime());
        matchUpdateForm.setEndTime(matching.getEndTime());
        matchUpdateForm.setMatchType(matching.getMatchType());
        matchUpdateForm.setCourtType(matching.getCourtType());
        matchUpdateForm.setMatchPlace(matching.getMatchPlace());
        model.addAttribute("match", matchUpdateForm);

        return "/match/matchUpdateForm";
    }

    @PostMapping("/update/{matchId}")
    public String matchUpdate(@PathVariable("matchId") Long matchId,
                              @Validated @ModelAttribute("match") MatchUpdateForm match,
                              BindingResult bindingResult, HttpServletRequest request) {

        Member member = (Member) request.getSession(false)
                .getAttribute(SessionConst.LOGIN_MEMBER); //로그인검사

        if (bindingResult.hasErrors()) {
            log.info("errors = {}", bindingResult);
            return "/match/matchUpdateForm";
        }

        matchService.update(match, member);

        return "redirect:/match/detail/" + matchId;
    }

    @PostMapping("/delete/{matchId}")
    public String matchDelete(@PathVariable("matchId") Long matchId) {
        List<Player> players = playerService.findAllByMatchingId(matchId);
        matchService.deleteById(matchId, players);
        return "redirect:/match/home";
    }

    @PostMapping("/player/add")
    public String matchPlayerAdd(@ModelAttribute("playerAddForm") PlayerAddForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String matchId = form.getMatchId();
            return "redirect:/match/detail/" + matchId;
        }
        playerService.matchPlayerAdd(form);
        matchService.increasePlayerNumber(Long.valueOf(form.getMatchId()));
        matchService.matchConditionCheck(Long.valueOf(form.getMatchId()));
        String matchId = form.getMatchId();
        return "redirect:/match/detail/" + matchId;
    }

    @PostMapping("/playerDelete")
    public String matchDeletePlayer(HttpServletRequest request, Long id) {
        Long matchingId = matchService.findOne(id).getId();
        Member member = (Member) request.getSession(false).getAttribute(SessionConst.LOGIN_MEMBER);
        Long memberId = member.getId();
        matchService.deleteMatchPlayer(matchingId, memberId);
        return "redirect:/match/detail/" + matchingId;

    }

}
