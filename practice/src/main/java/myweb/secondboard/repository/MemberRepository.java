package myweb.secondboard.repository;

import myweb.secondboard.domain.Matching;
import myweb.secondboard.domain.Member;
import myweb.secondboard.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryInterface {

  @Query("select m from Member m order by m.record.points desc")
  List<Member> findAllByPoints();

  @Query("select m from Member m where order by m.record.points desc")
  Member findFirstPlace();
}
