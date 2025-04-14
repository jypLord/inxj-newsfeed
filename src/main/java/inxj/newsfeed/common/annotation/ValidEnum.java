package inxj.newsfeed.common.annotation;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER}) // 검증 대상 (필드, 파라미터)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {EnumValidator.class, EnumListValidator.class})    // 검증 구현체
public @interface ValidEnum {
  String message() default "지원하는 값은 [{enumValues}] 입니다.";

  Class<?>[] groups() default {};     // 유효성 검사 적용 대상 구분할 때 사용

  Class<? extends Payload>[] payload() default {};

  Class<? extends Enum<?>> target();    // 검증 대상 enum 설정

  boolean ignoreCase() default true;    // 대소문자 무시 여부
}
