package project.backend.business.post.request.summary;

import lombok.Getter;

@Getter
public enum SummaryLanguage {
  KR("한국어"),
  EN("영어");

  private final String value;

  SummaryLanguage(String value) {
    this.value = value;
  }

  public static SummaryLanguage stringToEnum(String level) {
    return SummaryLanguage.valueOf(level);
  }
}
