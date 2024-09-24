package project.backend.business.post.request.summary;

import lombok.Getter;

@Getter
public enum SummaryTone {
  FORMAL("공식적 말투"),
  CASUAL("비공식적 말투"),
  CUTE("귀여운 말투");

  private final String value;

  SummaryTone(String value) {
    this.value = value;
  }

  public static SummaryTone stringToEnum(String level) {
    return SummaryTone.valueOf(level);
  }
}
