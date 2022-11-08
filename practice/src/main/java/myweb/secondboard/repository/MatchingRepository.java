package myweb.secondboard.repository;

import myweb.secondboard.domain.ClubMember;
import myweb.secondboard.domain.Matching;
import myweb.secondboard.dto.ScheduleForm;
import myweb.secondboard.dto.ScheduleMatching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<Matching, Long>, MatchingRepositoryInterface {


}
