---
id: P-BTN-ICON
category: pattern
status: proposed
read_when: 버튼에 SVG가 결합될 때 간격·위치·로딩·전환
depends_on: [C-ACTION, C-SVG, F-GEOMETRY, F-TYPE]
references: [D-001, D-009, S2-04, S2-08, U-05]
---

# Button + SVG Layout Pattern

## Problem / Anatomy
Button Container → Centered Content Group [optional Prefix Icon + gap + Label] OR [Label + gap + optional Suffix Icon]. Prefix+Suffix를 기본으로 동시 강제하지 않는다.

## Default Candidate
Inline SVG 16–18dp(일반 17), Label 14–16sp·600, gap 6–8dp(일반 7). 한 group의 optical center가 버튼 center와 같아야 한다. 높이·padding·line-height는 Button size가 소유한다.

## States & Anti-Jitter
Enabled/Pressed/Loading/Disabled/Success/Error. 로딩 시 전체 버튼 폭과 Label slot을 보존한 채 spinner만 overlay, label 제거/재측정 금지. Pressed scale/letter-spacing/font-weight 변경 금지. Border-width는 모든 state에서 동일 공간 확보.

## CTA Hierarchy
Primary Red=핵심 행동, Brand Weak=브랜드 보조, Brand Outline=로컬 보조, Secondary Gold=보상 수령 등 제한된 맥락, Neutral Weak=기본 보조. Secondary Gold가 모든 CTA를 대체하지 않음.

## QA
동일 fixture에서 hover/pressed/loading/disabled/label replacement 전후 외곽 및 Label center 위치 변화 <=1px; 320dp/200%/RTL 고려. Icon의 장식 여부·accessible name을 명확히 하고 버튼 안의 3D 코인·별·큰 Gift SVG를 장식적으로 남발하지 않는다.

> 수치는 v0.6 실험 후보이며 사용자 시각 승인 및 Android 검증 전에는 approved로 변경하지 않는다.
