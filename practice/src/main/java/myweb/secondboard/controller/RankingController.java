package myweb.secondboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Record;
import myweb.secondboard.service.MemberService;
import myweb.secondboard.service.RecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/ranking")
@RequiredArgsConstructor
public class RankingController {

  private final MemberService memberService;
  private final RecordService recordService;



  @GetMapping("/home")
  public String home(Model model) {

    List<Member> members = memberService.findAllByPoints();
    model.addAttribute("members", members);

    Member member = recordService.findFirstPlace();

//    Member member = recordService.findSecondPlace();

//    Member member = recordService.findThirdPlace();


//    List<Record> records = recordService.getRecordList();
//    model.addAttribute("records", records);
//    System.out.println("records = " + records);

    return "/ranking/rankingHome";
  }
}
