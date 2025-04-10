package inxj.newsfeed.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {
  private Set<String> allowedValues;    // enum 내 유효값

  // 검증 실행 시 처음 한번만 실행
  // parameter : 어노테이션으로 들어오는 메타정보
  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    Enum<?>[] enumValues = constraintAnnotation.target().getEnumConstants();  // enum 내 정의된 상수 목록 가져오기
    allowedValues = Arrays.stream(enumValues).map(Enum::name).collect(Collectors.toSet());
  }

  // Enum 유효값 검증 메소드
  @Override
  public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
    if(value == null) {
      return true;
    }
    String convertedValue = checkIgnoreCase(value);  // 대소문자 무시

    return allowedValues.contains(convertedValue);    // 유효값 검증
  }

  // 대소문자 무시
  private String checkIgnoreCase(String value) {
    return value.toUpperCase();
  }

  // 커스텀 메시지
  private String convertMessageTemplate(String message) {
    return message.replace("{enumValues}", String.join(" | ", allowedValues));
  }
}
