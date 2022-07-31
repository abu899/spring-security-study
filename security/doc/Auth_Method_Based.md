# 메소드 기반 인가

## Intro

메소드 기반 인증 인가는 서비스 계층의 처리 방식이다. 즉, 리소스 단위가 아니고 메소드 처리 전, 후로 보안 검사를
수행하여 인가처리를 진행하게 된다. 보안 설정 방식은 `어노테이션` 또는 `맵 기반` 권한 설정 방식이 존재한다.

- URL 방식은 Filter 기반 Method 방식은 AOP 기반으로 동작한다.

## 어노테이션 권한 설정

<p align="center"><img src="./img/core_5.png" width="80%"></p>

보안이 필요한 메소드에 어노테이션을 설정한다

- @PreAuthorize, @PostAuthorize
  - `@PreAuthroize("hasRole('ROLE_USER') and (#account.username == principla.username)`
  - SpEL 지원
  - `PrePostAnnotationSecurityMetadataSource`가 담당한다
- @Secured, @RolesAllowed
  - `@Secured("ROLE_USER"), @RolesAllowed("ROLE_USER")`
  - SpEL 미지원
  - `SecuredAnnotationSecurityMetadataSource`, `Jsr250MethodSecurityMetadataSource`가 담당
- @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)를 설정해줘야한다.