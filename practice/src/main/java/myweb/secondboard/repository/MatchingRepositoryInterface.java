package myweb.secondboard.repository;

import myweb.secondboard.domain.Matching;

import java.util.HashMap;
import java.util.List;

public interface MatchingRepositoryInterface {

  List<Matching> findAll();

  Matching findOne(Long matchingId);

  void increasePlayerNumber(Long matchingId);

  void matchingCondtionCheck(Long matchingId);

  void matchingOngoingCheck(Long matchingId);

  void matchingAfterCheck(Long matchingId);

}
