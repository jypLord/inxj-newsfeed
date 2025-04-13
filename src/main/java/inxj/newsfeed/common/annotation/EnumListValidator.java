package inxj.newsfeed.common.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumListValidator implements ConstraintValidator<ValidEnum, List<String>> {
  private Set<String> allowedValues;    // enum 내 유효값
  private String messageTemplate;

  // 검증 실행 시 처음 한번만 실행
  // parameter : 어노테이션으로 들어오는 메타정보
  @Override
  public void initialize(ValidEnum constraintAnnotation) {
    Enum<?>[] enumValues = constraintAnnotation.target().getEnumConstants();  // enum 내 정의된 상수 목록 가져오기
    allowedValues = Arrays.stream(enumValues).map(Enum::name).collect(Collectors.toSet());
    messageTemplate = convertMessageTemplate(constraintAnnotation.message());
  }

  // List<Enum> 유효값 검증 메소드
  @Override
  public boolean isValid(List<String> values, ConstraintValidatorContext constraintValidatorContext) {
    if(values == null) {
      return true;
    }
    List<String> convertedValue = checkIgnoreCase(values);  // 대소문자 무시

    boolean valid = new HashSet<>(convertedValue).containsAll(convertedValue);// 유효값 검증
    if(valid) return true;
    else {
      constraintValidatorContext.disableDefaultConstraintViolation();
      constraintValidatorContext.buildConstraintViolationWithTemplate(messageTemplate)
          .addConstraintViolation();
      return false;
    }
  }

  // 리스트 대소문자 무시
  private List<String> checkIgnoreCase(List<String> values) {
    return values.stream()
        .map(String::toUpperCase).toList();
  }

  // 커스텀 메시지
  private String convertMessageTemplate(String message) {
    return message.replace("{enumValues}", String.join(" | ", allowedValues));
  }
}
