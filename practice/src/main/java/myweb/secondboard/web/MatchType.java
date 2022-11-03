package myweb.secondboard.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MatchType {
    SINGLE("단식"),
    DOUBLE("복식");

    @Getter
    private final String title;
}
