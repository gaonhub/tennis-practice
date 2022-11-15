package myweb.secondboard.repository;

import myweb.secondboard.domain.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordRepository extends JpaRepository<Record, Long> {
  @Query("select r from Record r order by r.points desc")
  List<Record> findAllByPoints();
}
