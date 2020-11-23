# kakaopay-recruit-demo
카카오페이 데이터어플리케이션 개발자 사전과제

## Environments & Frameworks
    - IDE: IntelliJ IDEA Ultimate
    - OS: Mac OS X
    - Java8
    - Spring Boot 2.3.0
    - JPA
    - H2
    - Gradle
    - Junit5


## Build 및 실행 방법
####Command line 

```
$ git clone https://github.com/nautilus-alpha/kakaopay-recruit-demo.git
$ cd kakaopay-recruit-demo
$ ./gradlew clean build
$ java -jar build/libs/kakaopay-recruit-demo-0.0.1-SNAPSHOT.jar
```
- 접속 Base URI: `http://localhost:8080`


## API 기능명세 
#### 필수 기능
```
조건 : 각각의 쿠폰은 만료기간이 존재하며, 쿠폰형식은 번호, 코드, 자릿수등 자유롭게 선택 
1. 랜덤한 코드의 쿠폰을 N개 생성하여 데이터베이스에 보관하는 API를 구현하세요. input : N
2. 생성된 쿠폰중 하나를 사용자에게 지급하는 API를 구현하세요.
3. 사용자에게 지급된 쿠폰을 조회하는 API를 구현하세요.
4. 지급된 쿠폰중 하나를 사용하는 API를 구현하세요. (쿠폰 재사용은 불가) input : 쿠폰번호
5. 지급된 쿠폰중 하나를 사용 취소하는 API를 구현하세요. (취소된 쿠폰 재사용 가능) input : 쿠폰번호
6. 발급된 쿠폰중 당일 만료된 전체 쿠폰 목록을 조회하는 API를 구현하세요.
```

#### 선택 기능
```
7. (선택) 발급된 쿠폰중 만료 3일전 사용자에게 메세지(“쿠폰이 3일 후 만료됩니다.”)를 발송하는 기능을 구현하 세요. (실제 메세지를 발송하는것이 아닌 stdout 등으로 출력하시면 됩니다.)
```

#### 제약사항
``` 
1. API 기능명세에서 제시한 기능을 개발하세요.
2. 단위 테스트 (Unit Test) 코드를 개발하여 각 기능을 검증하세요.
3. 프로그램 언어는 평가에 반영되지 않으니 자유롭게 선택하세요.
4. 각 API의 HTTP Method들( GET | POST | PUT | DEL )은 자유롭게 선택하세요.
5. README.md 파일을 추가하여, 개발 프레임워크, 문제해결 전략, 빌드 및 실행 방법을 기술하세요.
```

#### 제약사항(선택)
```
1. API 인증을 위해 JWT(Json Web Token)를 이용해서 Token 기반 API 인증 기능을 개발하고 각 API 호출 시에 HTTP Header에 발급받은 토큰을 가지고 호출하세요.
- signup 계정생성 API: ID, PW를 입력 받아 내부 DB에 계정을 저장하고 토큰을 생성하여 출력한 다.
단, 패스워드는 안전한 방법으로 저장한다.
- signin 로그인 API: 입력으로 생성된 계정 (ID, PW)으로 로그인 요청하면 토큰을 발급한다.
2. 100억개 이상 쿠폰 관리 저장 관리 가능하도록 구현할것 10만개 이상 벌크 csv Import 기능
3. 대용량 트랙픽(TPS 10K 이상)을 고려한 시스템 구현 성능테스트 결과 / 피드백
```

## 문제해결방법

데이터 스키마 및 Entity 설정
```
- 기능을 데이터 관점에서 [사용자 등록/로그인], [쿠폰 조회/수정], [사용자쿠폰 발급/사용/취소] 로 그룹화 하여 생각했습니다.
- 테이블은 User, Coupon, UserCoupons 3개로 분리하여 정규화 하였으며,
- 각 Entity에서는 타 Entity에대해 조회만 할 수 있고 수정(insert, update)할 수 없도록 설정하여 관점을 분리 하였습니다.
- 사용자 관련 기능은 UserController - UserService - UserRepository를 거쳐 User Entity만 Upsert 됩니다. 쿠폰, 사용자 쿠폰에서도 동일하게 도메인을 분리합니다.
- 만료일 기준 쿠폰 조회를 위하여 ExpireDate 컬럼에 인덱스를 생성하였습니다. 
- 만료된 쿠폰을 unique하게 얻어올때는 쿠폰 테이블을,
    사용자의 사용되지 않은 쿠폰 조회시에는 사용자 쿠폰 테이블을,
    곧 만료될 쿠폰의 사용자 정보는 쿠폰>사용자쿠폰>사용자 테이블 INNER JOIN으로 얻어올 수 있습니다.
- 각 Entity는 등록일, 수정일, 등록자id, 수정자id가 있습니다.
- 등록자/수정자 ID로 기록될 시그니처는 enum 및 Entity의 default value로 관리합니다.

```

VO
```
사용자 입력은 Dto 로 받고, Validation 합니다.
Service 단에서 Entity 조회/수정/Upsert가 이러워지고,
모든 요청에 대한 응답은 표준적인 응답 포맷(code, messge, data)을 가진 ResultResponse안에 data object로 전달합니다.
```

Authentication
```
로그인 패스워드는 BCryptPasswordEncoder로 암호화 합니다.
암호문끼리의 비교가 성공하면, 
인증은 제시된 대로 JWT(jjwt)를 사용하였습니다.

TokenAuthenticationFilter에서 토큰을 검증하고 SecurityContextHolder를 세팅합니다.
회원가입/로그인 요청시에는 필터가 적용되지 않습니다.
사용자번호가 있더라도 확실한 검증이 필요한 경우 유효한 정보인지 DB에서 재확인합니다.
```
```
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthTokenService tokenProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable();
		http.csrf().disable();

		http.authorizeRequests()
				// h2-console 관련
				.antMatchers(
						"/user/**"
				).permitAll()
				.anyRequest().authenticated();

		http.addFilterBefore(tokenAuthenticationFilter(authenticationManagerBean()), UsernamePasswordAuthenticationFilter.class);
	}

```
```


public class TokenAuthenticationFilter extends BasicAuthenticationFilter {

	@Autowired
	private AuthTokenService tokenProvider;

	public TokenAuthenticationFilter(AuthenticationManager authenticationManager, AuthTokenService tokenProvider) {
		super(authenticationManager);
		this.tokenProvider = tokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		try {
			String token = getJwtFormRequest(request);
			if (StringUtils.hasText(token) && tokenProvider.verifyToken(token)) {
				Long userNo = tokenProvider.getUserNoFromToken(token);
				tokenProvider.getUserNoFromToken(token);

				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(userNo, null, null);
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		} catch (Exception e) {

		}

		filterChain.doFilter(request, response);
	}

	public String getJwtFormRequest(HttpServletRequest request) {
		return request.getHeader("Authorization");
	}
}
```
```

@Service
public class AuthTokenService {

	@Value("${auth.jwt.secretKey}")
	private String secretKey;

	@Value("${auth.jwt.expireTimeSeconds}")
	private int expireTimeSeconds;

	public AuthTokenResponse create(User user){
		Date expireDate = DateUtils.addSeconds(new Date(), expireTimeSeconds);
		JwtBuilder builder = Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", "HS256")
				.setHeaderParam("regDate", System.currentTimeMillis())
				.setAudience(user.getUserId())
				.setExpiration(expireDate)
				.claim("authGroup", "default")
				.setSubject(user.getUserNo().toString());

		String token = builder.signWith(SignatureAlgorithm.HS256, secretKey).compact();

		AuthTokenResponse loginResponse = new AuthTokenResponse(token, expireDate);

		return loginResponse;
	}

	public boolean verifyToken(String token) throws RuntimeException {
		try{
			Jwts.parser().setSigningKey(secretKey).parse(token);
		} catch (Exception e) {

		}
		return true;
	}
```

CSV Import
```
파일은 스프링의 MultiPartFile 형태로 업로드합니다.
CSV 행을 Entity로 매핑 시 원래는 범용으로 사용할수 있는 유틸성 정적 메서드를 만들고 싶었으나, 
Generic을 사용할 수 없는 static scope에서는 불가능하다고 판단했습니다.
대신 CsvEntityAdapter 추상클래스를 만들고, 
Entity마다 필요시 매핑만 재정의한 클래스를 생성하도록 했습니다. 
```
```
public abstract class CsvEntityAdapter<T> {

	public abstract T mapRow(String row);

	public Function<String, T> mapFunction(){
		return s -> mapRow(s);
	}

	public List<T> parseList(MultipartFile file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		List<T> list = reader.lines().map(mapFunction()).collect(Collectors.toList());
		return list;
	}

	public Iterator<T> iterator(MultipartFile file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
		return reader.lines().map(mapFunction()).iterator();
	}

	public Stream<T> stream(MultipartFile file) throws IOException {
		Iterator iter = iterator(file);
		return StreamSupport.stream(Spliterators.spliteratorUnknownSize(
				iter, Spliterator.ORDERED | Spliterator.NONNULL), false);
	}

}

```

쿠폰 상태값 관리 & 3일내 만료예정 쿠폰 조회, 메세지 
```
- 쿠폰의 사용여부는 사용자쿠폰(userCoupon) 테이블에서 관리합니다.
- 쿠폰을 사용하면 상태값 enum(CouponUseStatus) 멤버를 UNSED->USED로 변경합니다.
- 쿠폰 사용 취소 시에는 반대로 USED->UNUSED로 변경합니다.

- 쿠폰의 만료일자는 쿠폰의 속성, 쿠폰의 사용 여부는 사용자쿠폰의 속성으로 생각하여 각각 테이블에 필드 생성 했습니다.
- 금일 만료된 쿠폰 조회 쿠폰 테이블에서 expireDate가 금일인 데이터를 조회합니다.
- 3일 내 만료될 쿠폰 및 사용자 정보 조회 (메세지 보내기)시에는
  JoinQuery를 사용하지 않고 3일내 만료 쿠폰 목록을 조회한뒤 각각에 대하여 stream하며 userCoupon을 조회합니다.
    
```