---
id: P-SHEET
category: pattern
status: proposed
read_when: Sheet의 본문·고정 Footer·내용 분기·제스처
depends_on: [C-SHEET, C-ACTION, C-TEXT, C-GESTURE, S-PAGE]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Bottom Sheet Composition Pattern

## Problem
시트 내 텍스트와 선택 컨텐츠가 늘어날 때 Footer CTA가 사라지거나, OS 영역·본문 스크롤·닫기 제스처가 충돌하는 문제를 막는다.

## Context & Composition
Page → Sheet Backdrop → Header(제목 필수 후보 + 설명 선택) → Scrollable Body(Image/List/Field) → Fixed Footer(필요한 경우에만). 적은 콘텐츠는 **내용 높이에 맞는 최소**로 시작. 더 많은 콘텐츠는 Handle로 중간/전체 펼침.

## Decision Rules
- 동작 하나: 단일 Primary. 두 동작: 관계를 보고 Primary + neutral Secondary; 똑같은 버튼 50:50을 전역 강제하지 않는다.
- 좁은 화면/긴 한글은 Footer 세로 Stack 가능. 불필요한 보조 텍스트가 CTA보다 크지 않게.
- Full에서 모든 콘텐츠가 스크롤로 닿고 Footer는 항상 보임. Keyboard/IME가 열리면 가용 viewport 재측정.
- 저장되지 않은 입력의 강제 닫힘 손실 위험이 높으면 드래그 닫기 제한, 명시 Close와 확인 경로 제공.

## States
Closed→Min→Mid→Full (역방향 포함), Pending, Error, Keyboard Open. Snap이 중복되는 짧은 시트는 불필요한 단계를 생략한다.

## Do / Don't / QA
Do: Footer CTA margin + OS inset single owner, handle keyboard alternative. Don't: 본문 전체를 움직여 입력 내용 삭제, CTA와 Bottom Nav 동시 하단 점유, 전면 다이얼로그처럼 모든 상황에 3단 시트 강제. 320·390·430폭/200%/drag cancel/Esc/backdrop/IME 검증.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.

## v0.15 내부 스크롤 경계 인계

Body의 native scroll이 가능한 동안은 시트 높이를 변경하지 않는다. 본문 최상단에서 아래로 당기기 / 최하단에서 위로 밀기 때 동일 제스처의 잔여 변위만 시트로 넘긴다. Release는 현재 물리 높이에서 가장 가까운 유효 Snap으로 settle. 고정 Footer CTA는 Body 바깥, OS inset은 Shell 한 곳에서 합산. 정확한 C-GESTURE/C-SHEET가 우선한다.
