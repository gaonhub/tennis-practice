package myweb.secondboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myweb.secondboard.domain.Matching;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Player;
import myweb.secondboard.dto.MatchingSaveForm;
import myweb.secondboard.dto.MatchingUpdateForm;
import myweb.secondboard.dto.PlayerAddForm;
import myweb.secondboard.dto.ResultAddForm;
import myweb.secondboard.service.MatchingService;
import myweb.secondboard.service.PlayerService;
import myweb.secondboard.web.CourtType;
import myweb.secondboard.web.GameResult;
import myweb.secondboard.web.MatchingType;
import myweb.secondboard.web.SessionConst;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {

  private final MatchingService matchingService;

  private final PlayerService playerService;

  @GetMapping("/home")
  public String home(@PageableDefault(page = 0, size = 10, sort = "createdDate", direction = Sort.Direction.DESC)
                     Pageable pageable, Model model) {
    Page<Matching> matchingList = matchingService.getMatchingList(pageable);
    int nowPage = matchingList.getPageable().getPageNumber() + 1;
    int startPage = Math.max(nowPage - 4, 1);
    int endPage = Math.min(nowPage + 9, matchingList.getTotalPages());

    model.addAttribute("matchingList", matchingList);
    model.addAttribute("nowPage", nowPage);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

    MatchingSaveForm matchingForm = new MatchingSaveForm();
    model.addAttribute("matching", matchingForm);

    MatchingType[] matchTypes = MatchingType.values();
    model.addAttribute("matchTypes", matchTypes);

    CourtType[] courtTypes = CourtType.values();
    model.addAttribute("courtTypes", courtTypes);

    model.addAttribute("lat", null);

    return "/matching/matchingHome";
  }

//  @Async
//  @Scheduled(cron="0/3 * * * * *")
//  public void matchScheduleCheck() {
////    LocalDate now = LocalDate.now();
//    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
//
//
//
//    System.out.println("스케줄러 테스트중");
//
//    if(matchingList1.)
//    if(matching.getMatchingStartTime().equals(LocalDateTime.now().format(dtf))) {
//      matching.setMatchingStatus(MatchingStatus.ONGOING);
//      System.out.println("ONGOING 테스트중");
//    }
//
//    if (matching.getMatchingStartTime().equals(LocalDateTime.now().format(dtf))) {
//      matching.setMatchingStatus(MatchingStatus.AFTER);
//      System.out.println("AFTER 테스트중");
//    }
//  }


  @PostMapping("/new")
  @ResponseBody
  public Map<String,Object> matchingAdd(@RequestParam(value = "matchingForm") Map<String, Object> map, HttpServletRequest request) {
    Map<String, Object> map2 = new HashMap<String, Object>();

    System.out.println(map.get("title"));
    System.out.println(map.get("startDate"));
    System.out.println(map.get("endDate"));
    System.out.println(map.get("place"));
    System.out.println(map.get("matchingType"));
    System.out.println(map.get("courtType"));

//    Member member = (Member) request.getSession(false)
//      .getAttribute(SessionConst.LOGIN_MEMBER);
//
//
//    Long matchingId = matchingService.addMatching(form, member);
//    return "redirect:/matching/detail/" + matchingId;
    return map2;
  }

  @GetMapping("/detail/{matchingId}")
  public String matchingDetail(@PathVariable("matchingId") Long matchingId, Model model, HttpServletRequest request) {

    Matching matching = matchingService.findOne(matchingId);
    model.addAttribute("matching", matching);

    List<Player> players = playerService.findAllByMatchingId(matchingId);
    List<Player> playersA = players.stream().filter(m -> m.getTeam().toString().equals("A")).toList();
    List<Player> playersB = players.stream().filter(m -> m.getTeam().toString().equals("B")).toList();

    model.addAttribute("playersA", playersA);
    model.addAttribute("playersB", playersB);
    model.addAttribute("playerAddForm", new PlayerAddForm());
    model.addAttribute("resultForm", new ResultAddForm());
    GameResult[] results = GameResult.values();
    model.addAttribute("results", results);

    HttpSession session = request.getSession(false);

    if (session != null) {
      Member member = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

      Player playerMemberCheck = matchingService.playerMemberCheck(matchingId, member.getId());
      model.addAttribute("playerMemberCheck", playerMemberCheck);
    }

    MatchingType[] matchTypes = MatchingType.values();
    model.addAttribute("matchTypes", matchTypes);

    CourtType[] courtTypes = CourtType.values();
    model.addAttribute("courtTypes", courtTypes);

    MatchingUpdateForm matchingForm = new MatchingUpdateForm();
    matchingForm.setId(matching.getId());
    matchingForm.setTitle(matching.getTitle());
    matchingForm.setPlace(matching.getPlace());
    matchingForm.setCourtType(matching.getCourtType());
    matchingForm.setMatchingDate(matching.getMatchingDate());
    matchingForm.setMatchingStartTime(matching.getMatchingStartTime());
    matchingForm.setMatchingEndTime(matching.getMatchingEndTime());
    matchingForm.setMatchingType(matching.getMatchingType());
    model.addAttribute("matchingForm", matchingForm);

    return "/matching/matchingDetail";
  }

  @PostMapping("/delete/{matchingId}")
  public String matchingDelete(@PathVariable("matchingId") Long matchingId) {
    List<Player> players = playerService.findAllByMatchingId(matchingId);
    matchingService.deleteById(matchingId, players);

    return "redirect:/matching/home";
  }

  @PostMapping("/delete/memberDelete/{matchingId}")
  public String matchingMemberDelete(@PathVariable("matchingId") Long matchingId, Long memberId) {
    matchingService.deleteMatchingMember(matchingId, memberId);
    return "redirect:/matching/detail/" + matchingId;
  }

  @PostMapping("/update/{matchingId}")
  public String matchingUpdate(@Validated @ModelAttribute("matchingForm") MatchingUpdateForm form,
                               BindingResult bindingResult, HttpServletRequest request,
                               @PathVariable("matchingId") Long matchingId) {

    Member member = (Member) request.getSession(false)
      .getAttribute(SessionConst.LOGIN_MEMBER);

    if (bindingResult.hasErrors()) {
      log.info("errors = {}", bindingResult);
      return "redirect:/matching/detail/" + matchingId;
    }

    matchingService.update(form, member);
    return "redirect:/matching/detail/" + matchingId;
  }

  @PostMapping("/player/add")
  public String matchingPlayerAdd(@Validated @ModelAttribute("playerAddForm") PlayerAddForm form,
                                  BindingResult bindingResult, Long matchingId) {

    if (bindingResult.hasErrors()) {
//      log.info("errors = {}", bindingResult);
      return "redirect:/matching/detail/" + matchingId;
    }

    playerService.matchingPlayerAdd(form);
    matchingService.increasePlayerNumber(Long.valueOf(form.getMatchingId()));
    matchingService.matchingCondtionCheck(Long.valueOf(form.getMatchingId()));


    return "redirect:/matching/detail/" + matchingId;
  }

  @PostMapping("/result")
  public String matchingResult(@ModelAttribute("result") ResultAddForm result) {

    matchingService.updateGameResult(result);

    return "redirect:/matching/home";
  }

//  @PostMapping("/new")
  public String matchingFormAddLocation(@Validated @ModelAttribute("matching") MatchingSaveForm form,
    BindingResult bindingResult, HttpServletRequest request, Model model) {

    Member member = (Member) request.getSession(false)
      .getAttribute(SessionConst.LOGIN_MEMBER);

    if (bindingResult.hasErrors()) {
      log.info("errors = {}", bindingResult);
      return "redirect:/matching/home";
    }

    model.addAttribute("form", form);
    return "/matching/matchingLocationAdd";
  }
}
