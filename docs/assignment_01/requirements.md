##  **1주차 과제 요구사항 정리**

목표: 에브리타임 화면설계서를 보고 게시글 관련하여 필요한 기능 글로 정리


## 1. 필요 기능
- 게시글 (조회, 상세 조회, 작성, 삭제)
- 댓글 (조회, 작성, 삭제)
- 게시글 공감 
- 게시글 공감 취소
- 댓글 공감
- 댓글 공감 취소
- 익명 게시글 작성
- 익명 댓글 작성
- 게시글 신고
- 댓글 신고
- 게시글 스크랩
- 게시글 스크랩 취소

## 2. 도메인 모델(저장할 데이터)
- | 필드명 | 타입 | 설명 |
  기능명세만을 읽고 유추 가능한 것만을 작성함

### Post(게시글)

- | 'id' | 'Long' | 게시글 id, Primary Key |
- | 'title' | 'String' | 게시글 제목, 최대 50자 제한 |
- | 'content' | 'String' | 게시글 내용, 미리보기에도 활용 |
- | 'createdAt' | 'LocalDateTime' | 게시글 작성일자 |
- | 'likeCount' | 'Long' | 공감수 |
- | 'viewCount' | 'Long' | 조회수 |
- | 'commentCount' | 'Long' | 댓글수 |
- | 'isAnonymous' | 'Boolean' | 익명 여부 |
- | 'status' | 'Enum' | 'PUBLISHED', 'SUPPRESSED', 'DELETED' |
- | 'authorId' | 'Long' | 게시글 작성자, Foreign Key(User) |
- | 'boardId' | 'Long' | 게시판 id, Foreign Key(Board) |

### Board(게시판)

- | 'id' | 'Long' | 게시판 id, Primary Key |
- | 'name' | 'String' | 게시판 이름, 화면설계서에는 없지만 글자수 제한 필요 |

### User(사용자)

- | 'id' | 'Long' | 사용자 id, Primary Key |
- | 'nickname' | 'String' | 닉네임 |

### Comment(댓글)

- | 'id' | 'Long' | 댓글 id, Primary Key |
- | 'createdAt' | 'LocalDateTime' | 작성일자 |
- | 'content' | 'String' | 댓글 내용 |
- | 'likeCount' | 'Long' | 공감수 |
- | 'isAnonymous' | 'Boolean' | 익명 여부 |
- | 'anonymousNumber' | 'Long' | 익명1, 익명2처럼 구분하기 위한 숫자, 작성자 댓글은 anonymousNumber = 0|
- | 'status' | 'Enum' | 'PUBLISHED', 'SUPPRESSED', 'DELETED' |
- | 'authorId' | 'Long' | 댓글 작성자, Foreign Key(User) |
- | 'postId' | 'Long' | 어떤 게시글에 달린 댓글인지, Foreign Key(Post) |
- | 'parentId' | 'Long' | 댓글이면 null, 대댓글이면 댓글 id, Foreign Key(Comment) |

### PostLikes(게시글 공감)

- | 'id' | 'Long' | 공감한 내역 id, Primary Key |
- | 'postId' | 'Long' | 공감한 게시글에 대한 id, Foreign Key(Post), Unique Key(postId, userId) |
- | 'userId' | 'Long' | 공감한 사람 id, Foreign Key(User), Unique Key(postId, userId) |

### CommentLikes(댓글 공감)

- | 'id' | 'Long' | 공감한 내역 id, Primary Key |
- | 'commentId' | 'Long' | 공감한 댓글에 대한 id, Foreign Key(Comment), Unique Key(commentId, userId) |
- | 'userId' | 'Long' | 공감한 사람 id, Foreign Key(User), Unique Key(commentId, userId) |

### Report(신고 내역)

- | 'id' | 'Long' | 신고 내역 id, Primary Key |
- | 'targetType' | 'Enum' | 'POST', 'COMMENT' |
- | 'reason' | 'Enum' | 신고 이유에 대한 것들 |
- | 'status' | 'Enum' | 'RECEIVED', 'REVIEWING', 'RESOLVED' |
- | 'createdAt' | 'LocalDateTime' | 신고한 날짜 |
- | 'reporterId' | 'Long' | 신고자 id, Foreign Key(User) |
- | 'targetId' | 'Long' | 신고된 대상의 id, Foreign Key(Post, Comment) |

### Scrap(스크랩)

- | 'id' | 'Long' | 스크랩 id, Primary Key |
- | 'userId' | 'Long' | 스크랩한 사용자의 id, Foreign Key(User) |
- | 'postId' | 'Long' | 스크랩한 게시글 id, Foreign Key(Post) |

## 3. API(데이터 요청 및 응답)
게시글 관련한 API와 그 DTO 들의 필드명을 작성

### 게시글 조회

GET /posts

[응답 DTO 필드명]

- id
- title
- contentPreview
- authorName
- likeCount
- commentCount
- createdAt

[응답 설계 주의사항]

- Post의 isAnonymous 속성이 true일 시, authorName은 '익명'으로 처리하고, 아니라면 닉네임을 가져온다.
- contentPreview는 content에서 정한 글자수까지만 자른 미리보기용이다.

### 게시글 상세 조회

GET /posts/{postId}

[응답 DTO 필드명]

- id
- title
- content
- authorName
- commentCount
- likeCount
- createdAt
- comments(List)

[응답 설계 주의사항]

- Post 의 isAnonymous 속성이 true일 시, authorName 은 '익명'으로 처리하고, 아니라면 닉네임을 가져온다.
- comments의 parentId가 null이라는 것은 원 댓글이라는 뜻이다.
- replies는 대댓글들로, parentId는 이 대댓글이 달린 원 댓글의 id이다.
- comments는 Comment의 List이다.
- comments의 authorName과 replies의 authorName 뒤에 붙어있는 숫자는 Comment의 anonymousNumber로 부여된다.
- 익명 글쓴이가 댓글 작성 시, "익명(작성자)"로 반환한다.

### 게시글 작성

POST /posts

[요청 DTO 필드명]

- title
- content
- isAnonymous
- boardId

[요청 설계 주의사항]

- title은 50자 제한이다.
- isAnonymous가 true일 시 익명으로 처리, false일 시 사용자의 닉네임이 들어간다.

### 게시글 삭제

DELETE /posts/{postId}

### 댓글 조회

GET /posts/{postId}/comments

[응답 DTO 필드명]
- id
- content
- likeCount
- authorName
- createdAt
- replies(List)

[응답 설계 주의사항]
- replies는 이 댓글에 달린 대댓글의 List이다.
- authorName은 익명1, 익명2와 같이 되며 Comment의 anonymousNumber에 의해 부여된다.

### 댓글 작성

POST /posts/{postId}/comments

[요청 DTO 필드명]
- content
- isAnonymous
- parentId

[요청 설계 주의사항]
- isAnonymous는 익명 처리 여부에 대한 것이다.
- parentId는 대댓글일 시 그 댓글의 id가 들어가며, null 일 시 게시글에 대한 댓글이다.

### 댓글 삭제

DELETE /comments/{commentId}

### 게시글 공감

POST /posts/{postId}/likes

### 게시글 공감 취소

DELETE /posts/{postId}/likes

### 댓글 공감

POST /comments/{commentId}/likes

### 댓글 공감 취소

DELETE /comments/{commentId}/likes

### 신고

POST /reports

[요청 DTO 필드명]
- targetType
- targetId
- reason

[요청 설계 주의사항]
- targetType은 신고 대상이 게시글인지, 댓글인지를 구분한다.
- targetId는 신고 대상의 id이다.
- reason은 신고 사유로서, 정책에 따라 정해진 enum이다.

### 게시글 스크랩

POST /posts/{postId}/scraps

### 게시글 스크랩 취소

DELETE /posts/{postId}/scraps

## 4. 데이터 검증
확인해야할 데이터의 검증사항

### 게시글 
- 게시글의 제목이 최대 글자수를 넘지 않아야한다.
- 게시글의 내용이 최대 글자수를 넘지 않아야한다.
- 게시글의 제목과 내용은 빈칸이 아니어야한다.
- 신고 처리가 완료된 게시글은 삭제 처리된다.
- 익명 게시글일 시 작성자는 '익명'으로 보여야한다.

### 게시판
- 게시판의 이름이 최대 글자수를 넘지 않아야한다.

### 댓글 
- 댓글의 내용이 최대 글자수를 넘지 않아야한다.
- 댓글의 내용이 빈칸이 아니어야한다.
- 익명 댓글일 시 작성자는 '익명1' 과 같이 보여야한다.
- 존재하는 게시글에만 댓글을 작성할 수 있어야 한다.
- 존재하는 댓글에만 대댓글을 작성할 수 있어야 한다.
- 댓글 삭제 시 "삭제 된 댓글입니다" 라는 내용을 보여준다.
- 신고 처리가 완료된 댓글은 삭제 처리된다.

### 공감
- 공감은 게시글과 댓글 각각 한 번만 할 수 있다.
- 존재하는 게시글과 댓글에만 공감을 누를 수 있다.
- 공감을 누른 게시글과 댓글이 삭제될 시 자동으로 공감이 취소된다.

### 스크랩
- 존재하는 게시글만 스크랩을 할 수 있다.
- 삭제된 게시글은 자동으로 스크랩에서 취소된다.