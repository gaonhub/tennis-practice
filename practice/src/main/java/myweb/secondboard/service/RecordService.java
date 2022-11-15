package myweb.secondboard.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Record;
import myweb.secondboard.repository.MemberRepository;
import myweb.secondboard.repository.RecordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecordService {

  private final RecordRepository recordRepository;

  private final MemberRepository memberRepository;
  public Member findFirstPlace() {
    return memberRepository.findFirstPlace();
  }
}
