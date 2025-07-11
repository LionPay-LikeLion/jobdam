# 🦁  Lion Pay | 멋사 벡엔드 자바 15기 돈내고사자 팀 (회고5팀)

#### 스프링부트를 활용한 유료구독 SNS 솔루션 개발 프로젝트 Job談 : 잡담

이 문서는  프로젝트를 위한 Git 및 GitHub 사용 가이드입니다.  
브랜치 기반 워크플로우를 통해 안정적인 협업과 코드 관리를 목표로 합니다.

---
### [Git 브랜치 전략 안내]
### 본인 브랜치를 원격 develop과 merge할 때 과정 요약
🔒 main 브랜치
- 완전 잠금, 오직 박정환 담당만 merge 가능 (권한 상 팀장님도 가능하지만 가이드라인 상 박정환 담당)
- 테스트 서버 배포 / 프로덕션 서버 배포 가능한 안정 버전만 존재 (발표 전까지 프로덕션 서버가 테스트 서버)

⚙️ develop 브랜치
- 공식 개발용 브랜치
- 실수 방지를 위해서 직접 작업은 금지 
- 보호 규칙 없이 운영하지만 워크플로우는 지켜야 함
- 모든 작업은 feature/브랜치에서 진행
- 본인 브랜치 작업 전에는 항상 develop을 pull한 후 새로운 feature 브랜치 또는 본인 브랜치 최신화 후 작업해주세요.
- PR 리뷰 후 박정환 담당 또는 팀장님이 develop에 merge

📌 각자 feature 브랜치에서 작업 → PR → develop merge

### 📌 feature 브랜치 작업 흐름:
1. `git switch develop`
2. `git pull origin develop`
3. `git switch -c feature/기능명`
4. 개발!
5. 완료 후:
#### 1. 본인 develop 브랜치를 원격 develop과 일치시키기 
`git switch develop` → 잠깐 디벨롭 브랜치로 가서
`git pull origin develop` → 최신 내용 풀 해서

#### 2. 본인 작업 브랜치로 이동 
`git switch feature/작업브랜치` → 본인 개발하던 브랜치로 가서

#### 3. 최신 develop 브랜치를 본인 작업 브랜치에 머지하기
`git merge develop` → 충돌 해결 및 테스트 하면서 머지! 빌드까지 해보고 문제 없으면

#### 4. 본인 작업 브랜치를 원격에 푸시
`git push origin feature/작업브랜치` → 깃허브 본인 브랜치로 푸쉬

#### 5. GitHub에서 develop 브랜치로 Pull Request 생성
(이미 develop을 머지했기 때문에, 누군가 그 사이 develop에 푸시하지 않았다면 충돌 없이 머지 가능)
GitHub에서 PR 생성 → 박정환 담당 또는 팀장님이 develop에 merge

🚨 **주의: develop에 PR 생성 시 반드시 로컬에서 실행 확인 후 merge해주세요!**  
🆘 **build/실행에 문제가 생기면 반드시 공유해주세요. 혼자 끙끙대지 마세요!**  


---

### 📌 1. GitHub 저장소 클론하기 (최초 1회만)

#### ✅ 방법
- macOS: 터미널
- Windows: Git Bash 또는 GitHub CLI 추천

```bash
cd ~/Dev  # 원하는 폴더로 이동
git clone https://github.com/LionPay-LikeLion/jobdam.git
# 또는 SSH 사용 시
git clone git@github.com:juanpark/LionPay-LikeLion/jobdam.git
```

---

## 📌 2. main에서 직접 작업하지 마세요!

❌ 금지된 방법:
```bash
git add .
git commit -m "수정함"
git push origin main   # 🚨 금지
```
👉 main은 항상 안정적인 상태를 유지해야 합니다.

---

## 📌 3. 새로운 브랜치에서 작업하기

### 브랜치 생성
```bash
git switch -c feature/작업이름
```

### 브랜치 관련 명령어
```bash
git branch            # 모든 브랜치 목록
git switch 브랜치명    # 브랜치 이동
git switch -           # 이전 브랜치로 이동
```

### 브랜치 이름 예시
```bash
feature/add-login
feature/improve-ui
bugfix/fix-login-error
```

---

## 📌 4. 변경 사항 저장 (커밋하기)

### 변경된 파일 확인
```bash
git status
```

### 스테이징
```bash
git add .
```

### 커밋
```bash
git commit -m "[Feature] 로그인 기능 추가"
```

✅ 커밋 메시지 예시:  
- `[Fix] 로그인 오류 수정`  
- `[Feature] 판매글 업로드 기능 추가`  
- `[Chores] 폴더 구조 정리`

---

## 📌 5. 원격 저장소로 푸시하기

```bash
git push origin feature/작업이름
```


---

## 📌 6. Pull Request (PR) 생성하기

1. GitHub 저장소 → Pull Requests 클릭
2. "New Pull Request" 클릭
3. base: main / compare: feature/작업이름 설정
4. 작업 설명 작성, 팀원에게 리뷰 요청
5. 승인 후 Merge

---

## 📌 7. 머지 후 브랜치 삭제하기
계속 작업 하실 예정이면 삭제 안하셔도 됩니다만 항상 디벨롭 브랜치에서 풀 하셔서 최신 베이스 위에서 진행해주세요!

### 로컬 브랜치 삭제
```bash
git branch -d feature/작업이름
```

### 원격 브랜치 삭제
```bash
git push origin --delete feature/작업이름
```


---

## 📌 8. 새로운 작업을 시작할 때 항상 develop 최신화!

```bash
git switch develop
git pull origin main
git switch -c feature/new-task
```


---

# 🚀 Git 협업 워크플로우 요약

| 단계 | 명령어 | 설명 |
|:----|:----|:----|
| 저장소 클론 | `git clone` | 로컬에 저장소 복제 |
| 새 브랜치 생성 | `git switch -c feature/작업이름` | 새 작업 브랜치 생성 |
| 변경사항 확인 | `git status` | 수정 파일 확인 |
| 변경사항 추가 | `git add .` | 스테이징 |
| 변경사항 커밋 | `git commit -m "메시지"` | 변경 저장 |
| 원격 저장소 푸시 | `git push origin feature/작업이름` | GitHub 업로드 |
| PR 생성 및 병합 | GitHub에서 PR 생성 후 Merge |
| 브랜치 삭제 | `git branch -d`, `git push origin --delete` | 병합 완료 후 삭제 |
| 새로운 작업 시작 전 최신화 | `git pull origin develop` | 항상 최신 develop 기준 |


---

# 🔥 Git 협업 시 주의할 점

✅ main 브랜치 직접 수정 금지  
✅ 작업 전 항상 최신 develop pull  
✅ 의미 있는 커밋 메시지 작성 (`[Feature]`, `[Fix]`, `[Chores]` 등)  
✅ 브랜치 → PR → Merge 순서로 협업  
✅ 병합 완료 후 브랜치 삭제

---
