## 현생패치 디자인 리서치 · 2026.11.04

먼저 핵심부터 짚겠음.

**우리가 만들어야 하는 건 iOS 스타일의 게임형 할 일 앱이 아니라, 한국 청소년의 소셜 문화와 게임 경험을 결합한 독립적인 플랫폼임.**

그리고 디자인 시드는 단순히 색상·폰트·라운딩을 정하는 작업이 아님.

- **제품 언어**: 사용자에게 어떤 세계로 인식되는가
- **정보구조**: 사용자가 무엇을 먼저 보고 어디로 이동하는가
- **시각 언어**: 정보와 콘텐츠를 어떻게 구분하고 강조하는가
- **인터랙션**: 조작과 보상에서 어떤 반응을 경험하는가
- **컴포넌트 규칙**: 동일한 의도를 항상 동일하게 표현하는가

### 가장 중요한 문서상의 발견

Apple은 Liquid Glass를 콘텐츠를 꾸미는 재질이 아니라 **콘텐츠 위에 놓이는 탐색·조작 계층**으로 정의함. 본문 카드에 무분별하게 쓰지 말라고 명시하고 있음. <Cite ref="turn847167search0"/>

SEED는 Bottom Navigation, Tabs, Segmented Control을 외형이 아니라 **조작 범위와 탐색 목적**으로 구분함. <Cite refs={["turn353268view3","turn353268view4","turn353268view5"]}/>

Haze는 이러한 유리 표현을 Android에서 구현할 수 있는 기술적 선택지지만, 성능·접근성·투명도 대체 처리까지 설계해야 함. <Cite refs={["turn378311view5","turn378311view7"]}/>

따라서 현재 가장 유력한 방향은 **콘텐츠 중심의 현대적 플랫 UI 위에, 제한적인 글래스 내비게이션과 게임 특유의 강한 시각적 순간을 결합하는 방식**임.

이는 확정 시드가 아니라, 문서에 근거한 첫 번째 설계 가설임.

## 앱 비교에서 달라진 판단

네가 제시한 **청소년 중심 앱은 디자인과 관계성, 성인 중심 앱은 기능과 계층을 강조한다**는 구분은 출발점으로 유효함.

다만 조사해 보니 나이보다는 **사용자가 앱을 여는 목적**이 화면 구조를 더 강하게 결정하는 것 같음.

| 사례 | 실제로 참고할 점 | 현생패치에 적용할 해석 |
|---|---|---|
| <Entity category="mobile_app" value="Bump" disambig="친구 위치 공유 소셜 앱"/> · <Entity category="mobile_app" value="setlog" disambig="친구들과 일상을 영상으로 기록하는 소셜 앱"/> | 친구의 존재와 공동 경험이 콘텐츠 자체가 됨 <Cite refs={["turn733554search0","turn919004view2"]}/> | 친구·클랜을 별도 기능으로만 두지 말고 일상적인 퀘스트 경험에 연결 |
| <Entity category="mobile_app" value="치지직" disambig="네이버 라이브 스트리밍 플랫폼"/> | '치즈', '통나무 파워', '같이보기+' 같은 고유한 게임·커뮤니티 언어 <Cite refs={["turn101971search3","turn101971search4"]}/> | 보상과 공동 활동에 우리만의 용어를 부여할 수 있음 |
| <Entity category="mobile_app" value="네이버 웹툰" disambig="한국 웹툰 콘텐츠 플랫폼"/> · <Entity category="mobile_app" value="포스타입" disambig="창작자 팬 커뮤니티 플랫폼"/> · <Entity category="mobile_app" value="티빙" disambig="한국 OTT 스트리밍 서비스"/> | 콘텐츠와 취향이 화면의 중심이고, 도구는 상대적으로 조용함 <Cite refs={["turn733554search1","turn101971search0","turn966002search1"]}/> | 퀘스트북·캐릭터·활동 기록이 주인공이어야 함 |
| <Entity category="mobile_app" value="토스" disambig="한국 금융 서비스 앱"/> · <Entity category="mobile_app" value="당근" disambig="한국 지역생활 플랫폼"/> | 정보의 우선순위와 즉각적인 피드백을 일관된 시스템으로 관리 <Cite refs={["turn733554search2","turn627642view8"]}/> | 콘텐츠가 풍부해져도 조작 경로는 짧고 명확하게 유지 |
| <Entity category="mobile_app" value="열품타" disambig="공부 타이머 및 학습 커뮤니티 앱"/> | 개인 기록과 그룹의 실시간 상태·랭킹이 자연스럽게 연결 <Cite ref="turn101971search11"/> | 경쟁은 별도의 장식이 아니라 실제 활동의 결과여야 함 |

특히 셋로그의 사례가 인상적임. 2026년 7월 정돈된 레이아웃을 불규칙하게 바꾸자 반발이 발생했고 기존 배치로 되돌리는 조치가 나왔음. **젊은 사용자도 일관성과 예측 가능성을 중요하게 본다는 실증적인 반례**임. <Cite ref="turn919004view2"/>

즉 **트렌디함 = 복잡한 시각 효과**가 아님.

## 네비게이션과 콘텐츠를 분리해서 생각해야 함

2026년 UI 변화는 분명함. Apple의 Liquid Glass와 Android의 Material 3 Expressive 모두 움직임, 깊이, 반응성과 조작 요소의 시각적 표현을 강화했음. <Cite refs={["turn847167search0","turn179953search0"]}/>

하지만 바텀 내비게이션, 탭, 세그먼트의 기본 역할이 뒤집힌 건 아님. 바뀐 것은 주로 **외형, 전환 방식, 콘텐츠와의 공간 관계**임.

이걸 현생패치에 대입하면 다음처럼 판단할 수 있음.

<box gap={2}>
  <row align="start" gap={3}>
    <box background="surface-secondary" radius="lg" padding={2}>
      <icon name="navigation" size="lg"/>
    </box>
    <box flex="1" gap={1}>
      **바텀 내비게이션**: 앱 전체에서 이동하는 큰 영역. 현재 맥락을 잃지 않고 탐색하게 해야 함.
    </box>
  </row>
  <row align="start" gap={3}>
    <box background="surface-secondary" radius="lg" padding={2}>
      <icon name="panel-top" size="lg"/>
    </box>
    <box flex="1" gap={1}>
      **탑 내비게이션**: 현재 위치와 해당 화면의 핵심 조작. 뒤로가기와 닫기도 의미를 구분해야 함.
    </box>
  </row>
  <row align="start" gap={3}>
    <box background="surface-secondary" radius="lg" padding={2}>
      <icon name="panels-top-left" size="lg"/>
    </box>
    <box flex="1" gap={1}>
      **탭**: 서로 다른 콘텐츠 영역 사이의 탐색. 화면 구조를 전환함.
    </box>
  </row>
  <row align="start" gap={3}>
    <box background="surface-secondary" radius="lg" padding={2}>
      <icon name="sliders-horizontal" size="lg"/>
    </box>
    <box flex="1" gap={1}>
      **세그먼트**: 같은 영역 안에서 보기 방식이나 범위를 바꾸는 조작. 예를 들면 클랜 순위의 이번 주 / 지난주.
    </box>
  </row>
</box>

SEED의 명시적인 구분과도 일치함. <Cite refs={["turn179953search2","turn179953search4","turn117681view0"]}/>

여기서 중요한 건 **모든 화면에 똑같은 상단바와 똑같은 카드 구조를 반복하지 않는 것**임. 같은 디자인 시스템을 쓰더라도 홈, 퀘스트북, 클랜, DM은 콘텐츠의 역할에 맞게 레이아웃이 달라져야 함.

## 표현 방식에 관한 판단

사용자가 지적한 언어 문제도 단순한 카피라이팅이 아님.

Apple은 2026년 디자인 세션에서 명칭이 사용자의 기대를 형성해야 한다고 설명함. 명확한 이름과 감성적인 이름 모두 가능하지만, 위치에 따라 적합성이 다름. <Cite ref="turn117681view2"/>

그래서 나는 **탐색 언어는 익숙하게, 경험 언어는 독창적으로** 쓰는 쪽을 제안함.

가령 하단 이동 영역에 정체를 알 수 없는 게임 용어를 남발하는 건 오히려 불편함. 반면 챕터 달성, 공동 퀘스트, 획득한 보상, 친구의 활동 메시지에는 게임의 세계관과 커뮤니티 감성을 충분히 담을 수 있음.

그리고 청소년 대상 제품에서 친구의 존재가 중요하다고 해서 위치 공유나 강제 공개가 필요한 건 아님. **공유 범위를 사용자가 제어할 수 있어야 한다**는 조건까지 함께 참고해야 함.

## 현생패치에 대한 현재 설계 가설

<box gap={3}>
  <box gap={1}>
    <text color="secondary" weight="medium" size="xs">VISUAL DIRECTION · 아직 미확정</text>
    <title size="lg">Social Quest × Expressive Minimal</title>
    게임의 성취감과 소셜 서비스의 연결성을 중심에 두고, 탐색과 기본 조작은 현대적인 미니멀 UI로 정리하는 방향.
  </box>
  <grid columns={3} gap={2}>
    <grid-item>
      <box background="surface-secondary" radius="lg" padding={3} gap={2} align="center">
        <icon name="layers" size="xl"/>
        <text weight="medium" size="xs" textAlign="center">콘텐츠 중심</text>
        <caption textAlign="center">퀘스트·친구·캐릭터</caption>
      </box>
    </grid-item>
    <grid-item>
      <box background="surface-secondary" radius="lg" padding={3} gap={2} align="center">
        <icon name="glass-water" size="xl"/>
        <text weight="medium" size="xs" textAlign="center">제한적 글래스</text>
        <caption textAlign="center">탐색·조작 계층</caption>
      </box>
    </grid-item>
    <grid-item>
      <box background="surface-secondary" radius="lg" padding={3} gap={2} align="center">
        <icon name="sparkles" size="xl"/>
        <text weight="medium" size="xs" textAlign="center">표현적인 보상</text>
        <caption textAlign="center">성취 순간의 연출</caption>
      </box>
    </grid-item>
  </grid>
</box>

다만 이 방향도 비판적으로 검증해야 함.

**홈은 할 일 리스트도, 게임 대시보드도, SNS 피드도 단독으로 되어선 안 됨.** 사용자가 현재 수행 중인 여정, 바로 실행할 퀘스트, 함께하는 사람들의 존재가 하나의 경험으로 읽혀야 함. 그 과정에서 같은 진행률과 재화를 여러 번 노출하는 건 정보 밀도만 망치는 일임.

그리고 퀘스트 완료처럼 자주 수행하는 동작은 빠르게, 큰 챕터 달성처럼 드문 사건은 강하게 연출하는 게 적절함. Apple의 2026년 디자인 원칙도 단순함을 미니멀함과 구분하고, 즐거움은 단순한 효과 남발이 아니라고 강조함. <Cite ref="turn117681view3"/>

### 디자인 시드를 확정하는 방식

바로 색상표나 컴포넌트 라이브러리를 고정하지 않겠음.

먼저 동일한 퀘스트와 친구·클랜 데이터를 가진 웹 시연에서 **콘텐츠 중심형 / 소셜 중심형 / 게임 표현 강화형**을 비교하는 게 타당함. 각 안의 내용과 동선이 달라지면 디자인 언어를 공정하게 비교할 수 없으므로, 동일한 조건을 유지해야 함.

그 후 홈 진입, 퀘스트 수행, 완료 보상, 클랜 공동 활동, 친구와의 상호작용까지 하나의 흐름으로 검증하고, 합의된 결과에서만 토큰·컴포넌트·모션 규칙을 추출하면 됨.

Haze는 그 이후 Android 구현 단계의 기술 후보임. 최신 문서에서 글래스 굴절과 컨테이너 전환을 제공하지만, iOS와 픽셀 단위 동일성을 보장하지 않고 접근성 설정도 앱에서 연결해야 함. 웹의 CSS 시연을 네이티브로 옮길 때 별도 검증이 필요함. <Cite refs={["turn870276view3","turn378311view7"]}/>

## 조사 범위와 남은 한계

Haze의 구조·블러·글래스·성능·플랫폼·레시피 문서, SEED의 전체 컴포넌트 색인과 주요 컴포넌트·파운데이션·패턴 문서를 검토했음. Apple HIG는 한국어 페이지의 동적 렌더링 제약 때문에 검색 가능한 개별 지침과 WWDC 2025·2026 원문 및 발표 전문으로 교차 확인했음.

**세 사이트의 모든 하위 문서를 빠짐없이 정독했다고 주장할 단계는 아님.** 특히 방대한 Apple HIG 전체와 SEED의 모든 컴포넌트 세부 수치까지 검증이 끝난 것은 아님. 다만 현재 논의의 핵심인 계층, 내비게이션, 디자인 언어, 글래스와 Android 구현의 판단 근거는 확보했음.

프로젝트의 `AGENTS.md`, `AGENT.md`, `IDEA.md`도 확인했음. 현재 저장소 문서가 정의하는 **갓생패치**의 단순 체크리스트 방향과 지금 논의 중인 **현생패치**의 소셜 퀘스트 플랫폼 방향에는 차이가 있음. 디자인 시드를 확정할 때 이전의 기획 가정까지 무비판적으로 계승하면 안 된다는 뜻임.

**현재 결론은 명확함.**

SEED의 구조적 정확성, Apple의 조작 계층, Haze의 재질 표현, 국내 엔터테인먼트 앱의 콘텐츠·관계성·용어 체계를 각각 역할에 맞게 받아들여야 함.

단순히 토스에 글래스를 씌우고 보상 상자를 붙이는 방식은 다시 실패할 가능성이 높음. 이번에는 제품 경험과 시각적 정체성을 함께 결정하는 기준으로 접근하겠음.