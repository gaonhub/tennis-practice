package myweb.secondboard.controller;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myweb.secondboard.domain.Matching;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Player;
import myweb.secondboard.dto.MatchingSaveForm;
import myweb.secondboard.dto.MatchingSearchCondition;
import myweb.secondboard.dto.MatchingUpdateForm;
import myweb.secondboard.dto.PlayerAddForm;
import myweb.secondboard.dto.ResultAddForm;
import myweb.secondboard.service.MatchingService;
import myweb.secondboard.service.PlayerService;
import myweb.secondboard.web.CourtType;
import myweb.secondboard.web.GameResult;
import myweb.secondboard.web.MatchingType;
import myweb.secondboard.web.SessionConst;
import org.json.simple.JSONObject;
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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/matching")
@RequiredArgsConstructor
public class MatchingController {

  private final MatchingService matchingService;
  private final PlayerService playerService;

  @GetMapping("/home")
  public String home(Model model) {

    ArrayList<LocalDate> carousel = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      carousel.add(LocalDate.now().plusDays(i));
    }
    model.addAttribute("carouselDays", carousel);

//    List<Matching> matchingList = matchingService.findAllByDate(LocalDate.now());
    MatchingSearchCondition condition = new MatchingSearchCondition();
    condition.setDate(LocalDate.now().toString());
    List<Matching> matchingList = matchingService.searchMatchingByBuilder(condition);

    model.addAttribute("matchingList", matchingList);

    MatchingSaveForm matchingForm = new MatchingSaveForm();
    model.addAttribute("matching", matchingForm);

    MatchingType[] matchTypes = MatchingType.values();
    model.addAttribute("matchTypes", matchTypes);

    CourtType[] courtTypes = CourtType.values();
    model.addAttribute("courtTypes", courtTypes);

    model.addAttribute("lat", null);

    model.addAttribute("searchCondition", condition);
    model.addAttribute("currDate", LocalDate.now().toString());

    return "/matching/matchingHome";
  }

  @PostMapping("/new")
  public String matchingAdd(@Validated @ModelAttribute("matching") MatchingSaveForm form,
    BindingResult bindingResult, HttpServletRequest request) {

    Member member = (Member) request.getSession(false)
      .getAttribute(SessionConst.LOGIN_MEMBER);

    if (bindingResult.hasErrors()) {
      log.info("errors = {}", bindingResult);
      return "redirect:/matching/home";
    }

    Long matchingId = matchingService.addMatching(form, member);
    return "redirect:/matching/detail/" + matchingId;


  }

  @GetMapping("/detail/{matchingId}")
  public String matchingDetail(@PathVariable("matchingId") Long matchingId, Model model,
    HttpServletRequest request) {

    Matching matching = matchingService.findOne(matchingId);
    model.addAttribute("matching", matching);

    List<Player> players = playerService.findAllByMatchingId(matchingId);
    List<Player> playersA = players.stream().filter(m -> m.getTeam().toString().equals("A"))
      .toList();
    List<Player> playersB = players.stream().filter(m -> m.getTeam().toString().equals("B"))
      .toList();

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
    matchingForm.setLat(matching.getLat());
    matchingForm.setLng(matching.getLng());
    matchingForm.setContent(matching.getContent());
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
  public String matchingResult(@ModelAttribute("result") ResultAddForm result,
    HttpServletRequest request) {

    Member member = (Member) request.getSession(false)
      .getAttribute(SessionConst.LOGIN_MEMBER);

    matchingService.resultTempAdd(result, member);

    return "redirect:/matching/home";
  }

  //  @PostMapping("/new")
  public String matchingFormAddLocation(
    @Validated @ModelAttribute("matching") MatchingSaveForm form,
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

  @PostMapping("/matchingListUpdate/{date}")
  public String matchingListUpdate(@PathVariable("date") String date, Model model) {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    ArrayList<LocalDate> carousel = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      carousel.add(LocalDate.now().plusDays(i));
    }
    model.addAttribute("carouselDays", carousel);

//    List<Matching> matchingList = matchingService.findAllByDate(LocalDate.parse(date, dtf));
    MatchingSearchCondition condition = new MatchingSearchCondition();
    condition.setDate(date);
    List<Matching> matchingList = matchingService.searchMatchingByBuilder(condition);

    model.addAttribute("matchingList", matchingList);

    MatchingSaveForm matchingForm = new MatchingSaveForm();
    model.addAttribute("matching", matchingForm);

    MatchingType[] matchTypes = MatchingType.values();
    model.addAttribute("matchTypes", matchTypes);

    CourtType[] courtTypes = CourtType.values();
    model.addAttribute("courtTypes", courtTypes);

    model.addAttribute("lat", null);

    model.addAttribute("searchCondition", condition);
    model.addAttribute("currDate", date);

    return "/matching/matchingHome";
  }

  @PostMapping("/searchCondition")
  public String matchingSearchCondition(
    @ModelAttribute("searchCondition") MatchingSearchCondition condition, Model model) {

    ArrayList<LocalDate> carousel = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      carousel.add(LocalDate.now().plusDays(i));
    }
    model.addAttribute("carouselDays", carousel);

    List<Matching> matchingList = matchingService.searchMatchingByBuilder(condition);

    model.addAttribute("matchingList", matchingList);

    MatchingSaveForm matchingForm = new MatchingSaveForm();
    model.addAttribute("matching", matchingForm);

    MatchingType[] matchTypes = MatchingType.values();
    model.addAttribute("matchTypes", matchTypes);

    CourtType[] courtTypes = CourtType.values();
    model.addAttribute("courtTypes", courtTypes);

    model.addAttribute("lat", null);

    model.addAttribute("searchCondition", condition);
    model.addAttribute("currDate", condition.getDate());

    return "/matching/matchingHome";
  }
}

