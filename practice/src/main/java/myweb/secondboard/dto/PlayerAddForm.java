package myweb.secondboard.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class PlayerAddForm {

    String memberId;
    @NotNull
    String team;
    String matchId;
}
