# be18-2nd-YuGuanZhang-Lumi

# Lumi

# 1. 팀원 소개

<table>
  <tr align="center">
    <td>김대의</td>
    <td>윤동기</td>
    <td>조용주</td>
  </tr>
  <tr align="center">
    <td><a target="_blank" href="https://github.com/kimeodml"><img src="https://avatars.githubusercontent.com/u/88065770?v=4" width="100px"><br>@kimeodml</a></td>
    <td><a target="_blank" href="https://github.com/ydg010"><img src="https://avatars.githubusercontent.com/u/97106031?v=4" width="100px"><br>@ydg010</a> </td>
    <td><a target="_blank" href="https://github.com/whwjyj"><img src="https://avatars.githubusercontent.com/u/138665848?v=4" width="100px"><br>@whwjyj</a> </td>
  </tr>
</table>

# 2. 기술 스택
✓Frontend

![Vue.js](https://img.shields.io/badge/Vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)
![Vuetify](https://img.shields.io/badge/Vuetify-1867C0?style=for-the-badge&logo=vuetify&logoColor=white)
![Pinia](https://img.shields.io/badge/Pinia-FFD859?style=for-the-badge&logo=pinia&logoColor=black)
![Vue Router](https://img.shields.io/badge/Vue%20Router-4FC08D?style=for-the-badge&logo=vue.js&logoColor=white)

![JavaScript](https://img.shields.io/badge/JavaScript%20(ES6%2B)-F7DF1E?style=for-the-badge&logo=javascript&logoColor=black)
![HTML5](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![CSS3](https://img.shields.io/badge/CSS3-1572B6?style=for-the-badge&logo=css3&logoColor=white)
![Sass](https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=sass&logoColor=white)

![Axios](https://img.shields.io/badge/Axios-5A29E4?style=for-the-badge)
![Chart.js](https://img.shields.io/badge/Chart.js-FF6384?style=for-the-badge&logo=chartdotjs&logoColor=white)

![Vite](https://img.shields.io/badge/Vite-646CFF?style=for-the-badge&logo=vite&logoColor=white)
![ESLint](https://img.shields.io/badge/ESLint-4B32C3?style=for-the-badge&logo=eslint&logoColor=white)
![Prettier](https://img.shields.io/badge/Prettier-F7B93E?style=for-the-badge&logo=prettier&logoColor=black)

✓ Backend
 
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=OpenJDK&logoColor=white"> <img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=Spring&logoColor=white"> <img src="https://img.shields.io/badge/spring boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white">  <img src="https://img.shields.io/badge/JUnit5-25A162?style=for-the-badge&logo=JUnit5&logoColor=white">
![JWT](https://img.shields.io/badge/JWT-black?style=for-the-badge&logo=JSON%20web%20tokens)

<img src="https://img.shields.io/badge/Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white"> <img src="https://img.shields.io/badge/JPA-Hibernate-blue?style=for-the-badge&logo=hibernate&logoColor=white"> <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white">

✓ DB Server

<img src="https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white"> <img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=Redis&logoColor=white"> 

✓ API Test & Tools

<img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white">

✓ Infra & DevOps

<img src="https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github"> ![GitHub Actions](https://img.shields.io/badge/GitHub%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white)
![Notion](https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=notion&logoColor=white)
![Figma](https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white)
![ERDCloud](https://img.shields.io/badge/ERDCloud-0052CC?style=for-the-badge&logo=icloud&logoColor=white)
# 3. 서비스 소개
## 3-1. 배경
### 기존 과외 플랫폼의 문제점
1. **과외 과정에서 필요한 관리와 소통을 지원하는 기능의 부족**
    
    <img width="1346" height="648" alt="image" src="https://github.com/user-attachments/assets/e045736e-fce1-4d3a-aaf3-8521521a819f" />

    
    (출처: https://www.donga.com/news/Culture/article/all/20250409/131379599/1)
    
    (출처: https://biz.heraldcorp.com/article/10002626)
    
    기존 온라인 과외 플랫폼들은 *멘토와 학생을 연결해 주는 매칭 기능*에 집중하여, 학부모가 원하는 과목과 시간대에 맞춰 멘토를 추천하는 데는 효율적입니다. 하지만 매칭 이후의 학생 학습 현황이나 멘토링 피드백을 관리하는 시스템은 부족합니다.
    
2. **학습 자료의 분산**
    
    실제 과외 환경에서는 수업 내용 정리, 과제 제출과 피드백, 시험 대비, 성적 관리 등 다양한 활동이 이루어집니다. 하지만 이를 체계적으로 기록하고 공유할 수 있는 도구가 없다 보니, 멘토와 학생은 단순 메신저나 오프라인 기록에 의존하는 경우가 많습니다. 이로 인해 학습 자료가 흩어지고, 학습 진척 상황을 한눈에 확인하기 어려우며, 체계적인 관리가 어렵습니다.
    
3. **학부모의 과도한 개입**
    
    <img width="1344" height="330" alt="image" src="https://github.com/user-attachments/assets/e7e73b44-4d56-45d6-9fc9-18dd1f2bcc87" />

    
    (출처: https://www.nocutnews.co.kr/news/5983805)
    
    학부모는 자녀의 학습 현황을 잘 알지 못하기 때문에 작은 진척 상황도 멘토에게 직접 묻거나, 수업 방식에 대해 불필요한 간섭을 하게 됩니다. 이는 멘토의 수업 자율성을 해치고, 학생에게도 불필요한 압박감을 주게 됩니다. 예를 들어, 한 학부모가 “이번 주에 무슨 내용을 가르쳤는지, 숙제는 다 했는지, 시험 준비는 잘 되고 있는지”를 매번 직접 확인하려고 할 때, 멘토는 본질적인 수업보다 보고와 설명에 더 많은 시간을 빼앗기게 됩니다.
    

저희 서비스는 이러한 문제를 해결하기 위해 만들어졌습니다.

단순히 멘토와 학생을 연결해 주는 역할에 머무르지 않고, **멘토–학생 간의 원활한 과외를 위한 협업 환경**을 제공합니다. 일종의 *과외 전용 노션, 디스코드*같은 공간을 제공하는 것을 지향합니다.

저희의 서비스를 통해 학부모는 자녀의 과제 제출 여부와 성적 변화를 직접 확인할 수 있고,  멘토는 더 이상 일일이 답변하거나 보고서를 작성할 필요가 없고, 학생은 과제와 수업 내용이 한곳에 정리되어 있어 학습 효율이 크게 높아집니다.
## 3-2. 소개
**LUMI**는 과외를 보다 효율적이고 체계적으로 운영할 수 있도록 설계된 **과외 전용 협업 플랫폼**입니다.

기존 과외 플랫폼이 주로 멘토와 학생 매칭에 초점을 맞춘 것과 달리, 본 서비스는 **매칭 이후 실제 수업 과정과 학습 관리를 지원**하는 데 집중합니다.


### 핵심 기능 및 특징

1. **멘토 중심의 관리 기능**
    - 멘토는 채널 단위로 학생을 관리하며, 수업 자료를 업로드하고 과제를 부여할 수 있습니다.
    - 제출 현황, 피드백, 평가 결과를 한 곳에서 확인하며, 수업 운영에 필요한 모든 기록을 체계적으로 관리할 수 있습니다.
2. **학생 중심의 학습 공간**
    - 학생은 본인의 채널에서 과제 제출, 수업 자료 열람, 피드백 확인 등을 수행하며 학습 진행 상황을 한눈에 파악할 수 있습니다.
    - 이를 통해 학생은 과외 활동에 집중하면서 자기주도 학습을 경험할 수 있습니다.
3. **학부모를 위한 객관적 정보 제공**
    - 과제 제출 기록, 수업 자료, 시험 성적, 평가 결과 등 투명한 지표를 학부모가 직접 확인할 수 있습니다.
    - 학부모는 멘토에게 불필요하게 연락하거나 간섭할 필요가 없으며, 데이터를 기반으로 학습 진행 상황을 신뢰할 수 있습니다.
    - 이를 통해 멘토의 자율성이 보장되고, 학부모는 필요한 정보를 충분히 얻으면서도 과외 운영에 방해되지 않습니다.
4. **협업과 소통의 최적화**
    - 수업 자료, 과제, 피드백, 일정 등 과외 전반을 한 공간에서 관리할 수 있어, 멘토와 학생 간 소통이 자연스럽고 효율적입니다.
    - 학부모 역시 필요할 때만 정보를 확인하며, 과외 과정에 직접 개입하지 않도록 설계되어 있습니다.


### 서비스 지향점

**LUMI**는 단순한 과외 관리 툴이 아니라, **과외 전용 디지털 협업 공간**입니다.

멘토, 학생, 학부모 간 역할과 정보 흐름을 명확히 하여 과외 전반을 독립적이고 효율적으로 운영할 수 있는 통합 플랫폼을 지향합니다. 

멘토는 수업 준비와 지도에 온전히 집중할 수 있고, 

학생은 자신의 학습 진행 상황과 피드백을 명확히 확인하며 자기주도적으로 학습을 관리할 수 있습니다. 

학부모는 객관적인 데이터를 통해 자녀의 학습 상황을 신뢰하며 확인할 수 있어, 불필요한 개입 없이도 안심할 수 있습니다.

결국, **LUMI**의 궁극적인 목표는 멘토와 학생 중심의 학습 자율성을 보장하고, 학부모에게는 투명한 정보를 제공하며, 과외 전반을 체계적으로 운영할 수 있는 **과외 전용 디지털 협업 공간**을 실현하는 것입니다.
# 4. 기획

<details>
  <summary>요구사항 명세서</summary>
  <ul>
    <li><a href="https://docs.google.com/spreadsheets/d/1hU3ODNUjGjJ8DMJBtgb53_1PkK_MNOq-_kNV6GPlQLo/edit?gid=0#gid=0" target="_blank">요구사항 명세서</a></li>
  </ul>
  <img width="948" height="590" alt="image" src="https://github.com/user-attachments/assets/f8dc4da2-9fc4-4ab2-9d2e-561344b8f91e" />
</details>

<details>
  <summary>ERD</summary>
  <ul>
    <li><a href="https://www.erdcloud.com/p/bvxEFumiWireyp8mz" target="_blank">ERD</a></li>
  </ul>
  <img width="1400" height="905" alt="image" src="https://github.com/user-attachments/assets/cb65b2bb-621c-4266-bd19-b6d08898e73f" />
  
</details>

<details>
  <summary>테이블 명세서</summary>
  <ul>
    <li><a href="https://docs.google.com/spreadsheets/d/1hU3ODNUjGjJ8DMJBtgb53_1PkK_MNOq-_kNV6GPlQLo/edit?gid=561737687#gid=561737687" target="_blank">테이블 명세서</a></li>
  </ul>
  <img width="1346" height="589" alt="image" src="https://github.com/user-attachments/assets/bb02768d-fbeb-404d-9d88-7382ad565576" />



</details>

<details>
  <summary>API 명세서</summary>
  <ul>
    <li><a href="https://goldenrod-wildebeest-a18.notion.site/API-2333689564888188814febb1f26786c9?source=copy_link" target="_blank">API 명세서</a></li>
  </ul>
</details>


<details>
  <summary>테스트 계획 및 결과 보고서</summary>
    <ul>
      <li><a href="https://docs.google.com/spreadsheets/d/1hU3ODNUjGjJ8DMJBtgb53_1PkK_MNOq-_kNV6GPlQLo/edit?gid=1886778291#gid=1886778291">BE 단위 테스트</a></li>
    </ul>
  <details>
    <summary>회원가입</summary>
      <h4>이메일 인증 발송</h4>
      <img width="851" height="536" alt="스크린샷 2025-09-25 오전 12 28 28" src="https://github.com/user-attachments/assets/d1f8893a-eb50-4087-9b06-4ace1bd482c3" />
      <h4>이메일 인증</h4>
      <img width="835" height="491" alt="스크린샷 2025-09-25 오전 12 35 47" src="https://github.com/user-attachments/assets/0ee5b3b8-4913-452c-ba2d-046a9c21c11e" />
      <h4>회원가입</h4>
      <img width="849" height="626" alt="스크린샷 2025-09-25 오전 12 30 02" src="https://github.com/user-attachments/assets/23c346ef-ea9f-4c52-a4d3-6b9b2bef9296" />
    
  </details>
  <details>
    <summary>로그인 / 로그아웃</summary>
  <h4>로그인</h4>
  <img width="844" height="636" alt="스크린샷 2025-09-25 오전 12 31 05" src="https://github.com/user-attachments/assets/989971a1-a75f-4d08-b644-8fc880d511ca" />
  <h4>로그아웃</h4>
  <img width="844" height="534" alt="스크린샷 2025-09-25 오전 12 33 26" src="https://github.com/user-attachments/assets/05edeead-014b-421a-beba-67585c9f5b12" />
</details>
 
  <details>
    <summary>사용자</summary>
     <h4>JWT 재발급</h4>
     <img width="829" height="534" alt="스크린샷 2025-09-25 오전 12 33 13" src="https://github.com/user-attachments/assets/c3b349ce-f100-4a75-b415-5c7e8cbe67d6" />
     <h4>사용자 정보 가져오기</h4>
     <img width="835" height="623" alt="스크린샷 2025-09-25 오전 12 34 16" src="https://github.com/user-attachments/assets/648164c4-b34d-4d5b-a965-fa6e29b5e486" />
     <h4>계정 탈퇴</h4>
     <img width="843" height="616" alt="스크린샷 2025-09-25 오전 12 34 45" src="https://github.com/user-attachments/assets/992cc5e1-64c4-43e1-96b8-13b77546aa2a" />
  </details>

  <details>
    <summary>과제</summary>
    <h4>과제 생성</h4>
    <img width="1065" height="777" alt="스크린샷 2025-09-25 오전 12 39 31" src="https://github.com/user-attachments/assets/79879dc8-2f49-4d2c-a310-af6381a71eb4" />
    <h4>과제 전제 조회</h4>
    <img width="1065" height="571" alt="스크린샷 2025-09-25 오전 12 39 49" src="https://github.com/user-attachments/assets/07143380-65ba-4e40-b201-e754d10471b6" />
    <h4>과제 상세 조회</h4>
    <img width="1072" height="777" alt="스크린샷 2025-09-25 오전 12 40 02" src="https://github.com/user-attachments/assets/7d0a545a-a3b0-4522-a64e-e05b47181fba" />
    <h4>과제 수정</h4>
    <img width="1057" height="755" alt="스크린샷 2025-09-25 오전 12 40 28" src="https://github.com/user-attachments/assets/058962b5-c245-47d2-9eb8-4594a71feeb0" />
    <h4>과제 삭제</h4>
    <img width="1069" height="497" alt="스크린샷 2025-09-25 오전 12 40 38" src="https://github.com/user-attachments/assets/4544b075-0fac-4e87-a591-ce34f4d4d49b" />
  </details>

  <details>
    <summary>수업 관리</summary>
    <h4>수업 등록</h4>
    <img width="1065" height="670" alt="스크린샷 2025-09-25 오전 12 41 32" src="https://github.com/user-attachments/assets/2e62a78c-1c37-4866-85d8-56e51e14cf34" />
    <h4>수업 수정</h4>
    <img width="1068" height="680" alt="스크린샷 2025-09-25 오전 12 41 25" src="https://github.com/user-attachments/assets/cbea3ef1-6537-415b-a54c-f8cda19685b6" />
    <h4>수업 상태 수정</h4>
    <img width="1071" height="690" alt="스크린샷 2025-09-25 오전 12 41 39" src="https://github.com/user-attachments/assets/7a8579eb-8f5c-4ea8-a25a-9c68549bff28" />
    <h4>수업 월별 조회</h4>
    <img width="1066" height="661" alt="스크린샷 2025-09-25 오전 12 41 47" src="https://github.com/user-attachments/assets/b9b94577-296c-4e57-9333-35a9bcf1d1a0" />
    <h4>수업 일별 조회</h4>
    <img width="1074" height="655" alt="스크린샷 2025-09-25 오전 12 41 57" src="https://github.com/user-attachments/assets/fb01e621-5b74-449c-9322-3333774fdc34" />
    <h4>수업 삭제</h4>
    <img width="1066" height="505" alt="스크린샷 2025-09-25 오전 12 42 03" src="https://github.com/user-attachments/assets/99152f09-383f-455b-820b-f1f93b93207f" />
  </details>

  <details>
    <summary>채널</summary>
    <h4>채널 생성</h4>
    <img width="1066" height="633" alt="스크린샷 2025-09-25 오전 12 49 16" src="https://github.com/user-attachments/assets/8285df99-9f1b-460d-a17d-700833042d99" />
    <h4>채널 전체 조회</h4>
    <img width="1075" height="685" alt="스크린샷 2025-09-25 오전 12 49 23" src="https://github.com/user-attachments/assets/5ce0468a-c8a0-4fa8-bcc3-54b95c19941f" />
    <h4>채널 상세 조회</h4>
    <img width="1063" height="641" alt="스크린샷 2025-09-25 오전 12 49 30" src="https://github.com/user-attachments/assets/72b5612a-9d29-4388-ad11-e2150daee969" />
    <h4>채널 수정</h4>
    <img width="1071" height="654" alt="스크린샷 2025-09-25 오전 11 02 24" src="https://github.com/user-attachments/assets/cad050cf-a5f4-4d6c-86e3-a279af05eec7" />
    <h4>채널 삭제</h4>
    <img width="1065" height="650" alt="스크린샷 2025-09-25 오전 11 02 33" src="https://github.com/user-attachments/assets/0be23032-1c85-4210-bf64-3304108615d8" />
  </details>

  <details>
    <summary>캘린더</summary>
    <h4>월별 조회</h4>
    <img width="1058" height="490" alt="스크린샷 2025-09-25 오전 12 53 52" src="https://github.com/user-attachments/assets/6faf31eb-7a2d-48c1-9b1a-a035ecb666ca" />
    <h4>일별 조회</h4>
    <img width="1063" height="669" alt="스크린샷 2025-09-25 오전 12 54 00" src="https://github.com/user-attachments/assets/86b67de5-c944-448a-9641-153a5b068b37" />
  </details>

  <details>
    <summary>파일</summary>
    <h4>파일 업로드</h4>
    <img width="1069" height="602" alt="스크린샷 2025-09-25 오전 12 56 57" src="https://github.com/user-attachments/assets/c73b5156-f9b5-47c9-98d3-9e189f635029" />
    <h4>파일 삭제</h4>
    <img width="1062" height="515" alt="스크린샷 2025-09-25 오전 12 57 07" src="https://github.com/user-attachments/assets/5cb40596-f36b-44a9-84e8-e631515357d1" />
    <h4>파일 다운로드</h4>
    <img width="1073" height="739" alt="스크린샷 2025-09-25 오전 12 57 15" src="https://github.com/user-attachments/assets/d1e7a8b6-1dc5-4f36-bd89-2da817495c25" />
  </details>

  <details>
    <summary>투두</summary>
    <h4>투두 등록</h4>
    <img width="1070" height="655" alt="스크린샷 2025-09-25 오전 12 59 56" src="https://github.com/user-attachments/assets/f1268e9a-14d9-4c7e-8429-f0d21918e5b8" />
    <h4>월별 투두 조회</h4>
    <img width="1067" height="495" alt="스크린샷 2025-09-25 오전 12 59 40" src="https://github.com/user-attachments/assets/507cd9f5-a5d6-49b8-8f27-1a0ffc690feb" />
    <h4>일별 투두 조회</h4>
    <img width="1074" height="503" alt="스크린샷 2025-09-25 오전 12 59 48" src="https://github.com/user-attachments/assets/404dc782-3d9f-4508-b8d1-358036a3c329" />
    <h4>투두 수정</h4>
    <img width="1073" height="618" alt="스크린샷 2025-09-25 오전 1 00 03" src="https://github.com/user-attachments/assets/38e17235-4303-46d5-9342-758370888154" />
    <h4>투두 삭제</h4>
    <img width="1070" height="491" alt="스크린샷 2025-09-25 오전 1 00 10" src="https://github.com/user-attachments/assets/7237e3aa-6b10-4314-b9ef-6f8a839742ee" />
  </details>

<details>
    <summary>성적</summary>
    <h4>성적 생성</h4>
    <img width="1070" height="651" alt="스크린샷 2025-09-25 오전 11 13 46" src="https://github.com/user-attachments/assets/22ba2e35-9167-49b5-bef5-c361e70eaaa2" />
    <h4>성적 조회</h4>
    <img width="1072" height="749" alt="스크린샷 2025-09-25 오전 11 13 55" src="https://github.com/user-attachments/assets/7300cbc5-6f9c-46c4-8e7b-44778d12bf11" />
    <h4>성적 삭제</h4>
    <img width="1064" height="491" alt="스크린샷 2025-09-25 오전 11 28 32" src="https://github.com/user-attachments/assets/5fa0ccb0-7c1d-4276-97bb-fb23dee6635d" />
  </details>  

<details>
    <summary>제출</summary>
    <h4>제출 생성</h4>
    <img width="1063" height="724" alt="스크린샷 2025-09-25 오전 11 29 22" src="https://github.com/user-attachments/assets/b03959ef-321e-4ab4-b181-15e927f63c36" />
    <h4>제출 조회</h4>
    <img width="1071" height="773" alt="스크린샷 2025-09-25 오전 11 29 32" src="https://github.com/user-attachments/assets/c4afbaa9-5023-470a-a28f-6a100b56cc70" />
    <h4>제출 수정</h4>
    <img width="1073" height="726" alt="스크린샷 2025-09-25 오전 11 29 40" src="https://github.com/user-attachments/assets/882d80b6-d4e2-4bec-83a3-58dcbfb4c7cf" />
    <h4>제출 삭제</h4>
    <img width="1066" height="514" alt="스크린샷 2025-09-25 오전 11 30 18" src="https://github.com/user-attachments/assets/9852db26-592e-4956-b832-a0a7d46c60be" />
  </details>  

  <details>
    <summary>채널유저</summary>
    <h4>초대 생성</h4>
    <img width="1068" height="639" alt="스크린샷 2025-09-25 오전 11 37 28" src="https://github.com/user-attachments/assets/6154860a-e85d-472c-a982-b5cd79752713" />
    <h4>초대코드 인증</h4>
    <img width="1065" height="663" alt="스크린샷 2025-09-25 오전 11 40 19" src="https://github.com/user-attachments/assets/9c7b28b6-0d96-4584-b1de-3de7ab32ceb7" />
    <h4>채널 유저 전체 조회</h4>
    <img width="1067" height="588" alt="스크린샷 2025-09-25 오전 11 41 00" src="https://github.com/user-attachments/assets/ba7682e5-53a9-409c-9cc5-b8c79278bd8b" />
    <h4>채널 유저 상세 조회</h4>
    <img width="1065" height="635" alt="스크린샷 2025-09-25 오전 11 41 09" src="https://github.com/user-attachments/assets/d91dbeac-ff1b-45fb-a631-90e8bccef87b" />
    <h4>채널 유저 정보 수정</h4>
    <img width="1068" height="674" alt="스크린샷 2025-09-25 오전 11 41 34" src="https://github.com/user-attachments/assets/4c5bf461-43c1-4f1f-af26-f874d1c4c8cb" />
    <h4>채널 유저 탈퇴</h4>
    <img width="1072" height="673" alt="스크린샷 2025-09-25 오전 11 41 42" src="https://github.com/user-attachments/assets/4c4a9c6d-9b79-4665-b98d-01036b137e45" />
  </details>  

   <details>
    <summary>평가</summary>
     <h4>평가 생성</h4>
     <img width="1067" height="614" alt="스크린샷 2025-09-25 오후 12 12 27" src="https://github.com/user-attachments/assets/9ba261c1-5243-4ade-8315-dc47d6d27e70" />
     <h4>평가 조회</h4>
     <img width="1061" height="620" alt="스크린샷 2025-09-25 오후 12 12 35" src="https://github.com/user-attachments/assets/1bb12dfa-0dbb-4e5b-aea1-693a9b859487" />
     <h4>평가 수정</h4>
     <img width="1069" height="623" alt="스크린샷 2025-09-25 오후 12 12 42" src="https://github.com/user-attachments/assets/088a26b5-f745-4837-9455-36ab470ba6a1" />
     <h4>평가 삭제</h4>
     <img width="1061" height="490" alt="스크린샷 2025-09-25 오후 12 12 49" src="https://github.com/user-attachments/assets/6aa8f6d3-eaa9-465b-a00e-24cd9fccb220" />
  </details>  
</details>

# 5. 시스템 아키텍처 및 기술적 특징
<img width="1050" height="761" alt="아키텍처 그림" src="https://github.com/user-attachments/assets/68a01ca8-8549-4f75-bd12-3c26ccf7c3b2" />

# 6. 회고록
| 이름                                    | 회고 내용                                           |
|--------------|-----------------------------------------------------------------------------------------------------------------|
|      김대의        |    항상 프론트엔드만 경험해 보았기 때문에 API 연동을 위해 스웨거 밖에 참고할 일이 없었는데 이렇게 설계부터 개발까지 경험해 보면서 백엔드와 관련한 다양항 경험을 할 수 있었습니다. 특히, DB 설계 당시에는 이상 현상을 예방하기 위해 정규화를 진행하였지만 직접 JPA를 활용한 쿼리문을 작성해 보니 오히려 복잡해지는 문제가 있어서 반정규화를 진행한 적이 있었습니다. 이렇듯 설계 및 개발을 진행하면서 프로젝트의 전체 흐름을 파악할 수 있는 좋은 경험이 되었습니다. 또한 리액트가 아닌 뷰를 통해 뷰에서 사용되는 다양한 메소드를 사용해 볼 수 있었고, vuetify를 통해 일관된 디자인으로 구성하여 조금 더 완성도 있는 프로젝트를 구현할 수 있게 되었습니다. 다른 팀에 비해 인원수도 적어서 할 일이 배가 되었을 텐데 팀원들이 잘 따라줘서 잘 마칠 수 있었습니다. 모두 고생하셨습니다. |
|      윤동기      |    이번 2달간의 프로젝트를 진행하면서 여러 가지 배운 점과 어려웠던 것들, 아쉬운 것들을 동시에 경험할 수 있었습니다. SpringBoot를 사용하여 백엔드 개발을 진행하면서 백엔드 시스템이 어떻게 동작하는지 이해할 수 있었고, 그렇게 만들어진 API들을 실제 Postman을 활용하여 테스트를 진행하며 백엔드에서 개발한 것들이 어떤 형식으로 데이터를 주고받는지 확인했습니다. 처음 Postman 테스트에서 제가 원하는 결과가 나왔을 때의 기분은 어려운 수학 문제를 마침내 풀어낸 기분이었습니다. 또한, 팀으로 진행하는 프로젝트인 만큼 팀과의 협업 과정에서의 소통과 역할 조율의 중요성을 느꼈고, GitHub Repository를 팀원과 같이 쓰면서 브랜치를 관리하는 방법, Pull Request를 등록하고 팀원들의 코드를 리뷰하는 방법, 리뷰 과정에서의 내가 개발하지 않았던 코드를 이해하는 과정들이 저에게 큰 배움이 되었습니다. 프로젝트 기간 어려운 점도 많았습니다. vue와 javascript 사용에 익숙하지 않아서 짧은 프론트 개발 기간에 학습과 개발을 동시에 진행해야 했던 점이 어려웠습니다. 팀원이 줄어들면서 원래 계획했던 사항들의 볼륨을 수정해야 할지 말지, 배분은 어떻게 해야 할지를 남은 팀원들과 정말 많이 상의했었습니다. 회의를 통해 저희는 최대한 가능한 만큼 다 구현해 보기로 했고, 모두가 열심히 했지만, 결국 원했던 기능 모두를 만들지는 못했습니다. 다 구현하지 못한 것에 대한 아쉬움이 많이 남아서 저희 팀원들은 비록 평가일은 끝났지만, 못다 한 구현을 틈틈이 완성해 나가기로 했습니다. 이번 프로젝트에서 부족한 점, 아쉬운 점도 많았지만 실제 개발 과정에서 부딪힐 수 있는 다양한 상황을 경험하며 성장할 수 있는 뜻깊은 시간이였습니다. 끝까지 열심히 해준 팀원들에게 정말 감사합니다.          |
|      조용주        |    이번 프로젝트를 통해 백엔드와 프론트엔드 전반에 대한 이해도를 높이고, Git를 활용한 협업 능력을 기를 수 있었습니다. 또한 백엔드의 기본 구조와 개발 흐름을 학습하며, 하나의 웹 서비스가 어떻게 동작하는지 깊이 이해할 수 있었던 뜻깊은 경험이었습니다. 특히 Spring Boot 활용법, Spring Security의 동작 방식, 그리고 JWT를 통한 인증 및 보안 처리를 직접 구현해보면서 기술적인 성장을 이룰 수 있었습니다. 현재는 머릿속으로 개발 구조를 설계할 수 있는 단계에 있지만, 앞으로는 더 나아가 사용자에게 가치 있는 기능과 서비스를 제공할 수 있는 개발자로 성장하고자 합니다.  |

