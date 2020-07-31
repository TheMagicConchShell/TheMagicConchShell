package com.blsa.ezilog.controller;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.CategoryDao;
import com.blsa.ezilog.dao.LikeCountDao;
import com.blsa.ezilog.dao.PostDao;
import com.blsa.ezilog.dao.ReplyDao;
import com.blsa.ezilog.dao.ReplyLikeCountDao;
import com.blsa.ezilog.dao.UserDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.category.Category;
import com.blsa.ezilog.model.category.CategoryCreateRequest;
import com.blsa.ezilog.model.category.CategoryUpdateRequest;
import com.blsa.ezilog.model.like.LikeCount;
import com.blsa.ezilog.model.like.LikeCountRequest;
import com.blsa.ezilog.model.like.ReplyLikeCount;
import com.blsa.ezilog.model.like.ReplyLikeCountRequest;
import com.blsa.ezilog.model.post.Post;
import com.blsa.ezilog.model.post.PostCreateRequest;
import com.blsa.ezilog.model.post.PostUpdateRequest;
import com.blsa.ezilog.model.reply.Reply;
import com.blsa.ezilog.model.reply.ReplyCreateRequest;
import com.blsa.ezilog.model.reply.ReplyUpdateRequest;
import com.blsa.ezilog.model.user.User;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/counsel")
public class CounselController {

    @Autowired
    PostDao postDao;

    @Autowired
    ReplyDao replyDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    LikeCountDao likecountDao;

    @Autowired
    ReplyLikeCountDao replylikecountDao;

    @Autowired
    UserDao userDao;

    @ApiOperation(value = "고민 전체 목록 반환", notes = "Input : page, Output: 성공 : [status = true, data = 고민 리스트(Post)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping("/post")
    public Object retrievePost(@RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Long total = postDao.count();

        if (page <= 0 || page > total / 10 + 1) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");
            Page<Post> pList = postDao.findAll(pageable);

            if (!pList.isEmpty()) {

                pList.forEach((e) -> {

                    Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                    Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                    Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();

                    e.setLikeCount(like + likelike);
                    e.setUnlikeCount(unlike);

                    if (e.getWriter().equals(nickname)) {
                        e.setMine(true);
                    }
                    if (e.isSecret() == true) {
                        e.setWriter("익명의 작성자");
                    }
                });

                result.status = "S-200";
                result.message = "고민 목록 불러오기에 성공했습니다.";
                result.data = pList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "E-4401";
                eresult.message = "불러 올 고민이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPost");
                errorMap.put("data", pageable);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }

        return response;

    }

    @ApiOperation(value = "카테고리에 해당 되는 작성자가 쓴 고민 반환", notes = "작성자 이름을 통해서 고민 검색", response = List.class)
    @GetMapping("/post/category")
    public Object searchNoticeByCategory(@RequestParam String category, @RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {

            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");

            Optional<Category> optCate = categoryDao.findCategoryByName(category);

            if (optCate.isPresent()) {
                Category cate = optCate.get();
                Page<Post> pList = postDao.findPostByCategoryId(cate.getId(), pageable);

                if (!pList.isEmpty()) {

                    pList.forEach((e) -> {

                        Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                        Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                        Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();

                        e.setLikeCount(like + likelike);
                        e.setUnlikeCount(unlike);

                        if (e.getWriter().equals(nickname)) {
                            e.setMine(true);
                        }
                        if (e.isSecret() == true) {
                            e.setWriter("익명의 작성자");
                        }
                    });

                    result.status = "S-200";
                    result.message = "작성자를 이용하여 고민 목록들 가져오기 성공";
                    result.data = pList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4402";
                    eresult.message = "카테고리에 해당되는 고민이  없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noPostByCategory");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4403";
                eresult.message = "해당 카테고리가  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noCategory");
                errorMap.put("data", category);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }
        return response;
    }

    @ApiOperation(value = "검색어에 해당 되는 작성자가 쓴 고민 반환", notes = "작성자 이름을 통해서 고민 검색", response = List.class)
    @GetMapping("/post/writer")
    public Object searchNoticeByWriter(@RequestParam String writer, @RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {

            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");

            if (writer.contains("익명")) {
                Page<Post> aList = postDao.findPostByAnonymous(pageable);

                if (!aList.isEmpty()) {

                    aList.forEach((e) -> {

                        Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                        Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                        Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();

                        e.setLikeCount(like + likelike);
                        e.setUnlikeCount(unlike);

                        if (e.getWriter().equals(nickname)) {
                            e.setMine(true);
                        }
                        if (e.isSecret() == true) {
                            e.setWriter("익명의 작성자");
                        }
                    });

                    result.status = "S-200";
                    result.message = "작성자를 이용하여 고민 목록들 가져오기 성공";
                    result.data = aList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4404";
                    eresult.message = "작성자에 해당되는 고민이  없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noPostByWriter");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {

                Page<Post> pList = postDao.findPostByWriter(writer, pageable);

                if (!pList.isEmpty()) {

                    pList.forEach((e) -> {

                        Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                        Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                        Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();
                        e.setLikeCount(like + likelike);
                        e.setUnlikeCount(unlike);

                        if (e.getWriter().equals(nickname)) {
                            e.setMine(true);
                        }
                    });

                    result.status = "S-200";
                    result.message = "작성자를 이용하여 고민 목록들 가져오기 성공";
                    result.data = pList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4404";
                    eresult.message = "작성자에 해당되는 고민이  없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noPostByWriter");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            }

        }
        return response;
    }

    @ApiOperation(value = "고민 번호에 해당되는 고민 반환", response = List.class)
    @GetMapping("/post/post-no")
    public Object seasrchPostByNo(@RequestParam BigInteger no,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            Optional<Post> optPost = postDao.findPostByNo(no);

            if (optPost.isPresent()) {
                Post post = optPost.get();

                Integer like = likecountDao.countTotal(post.getNo(), "p").intValue();
                Integer likelike = likecountDao.countTotal(post.getNo(), "pp").intValue();
                Integer unlike = likecountDao.countTotal(post.getNo(), "m").intValue();
                post.setViews(post.getViews() + 1);
                post.setLikeCount(like + likelike);
                post.setUnlikeCount(unlike);

                postDao.save(post);

                if (post.isSecret() == true) {
                    post.setWriter("익명의 작성자");
                }

                if (post.getWriter().equals(nickname)) {
                    post.setMine(true);
                }

                List<Reply> allList = replyDao.ReplyByPostNum(post.getNo());

                // 같이오는 댓글들 목록 좋아요, 싫어요 수 가져오기
                for (int i = 0; i < allList.size(); i++) {
                    Integer r_like = replylikecountDao.countTotal(allList.get(i).getId(), "p").intValue();
                    Integer r_likelike = replylikecountDao.countTotal(allList.get(i).getId(), "pp").intValue();
                    Integer r_unlike = replylikecountDao.countTotal(allList.get(i).getId(), "m").intValue();

                    allList.get(i).setLikeCount(r_like + r_likelike);
                    allList.get(i).setUnlikeCount(r_unlike);

                    if (allList.get(i).isSecret() == true) {
                        allList.get(i).setWriter("익명의 작성자");
                    }
                    
                    if(allList.get(i).getWriter().equals(nickname)) {
                        allList.get(i).setMine(true);
                    }
                        
                }

                Map<String, Object> PostMap = new HashMap<>();

                result.status = "S-200";
                result.message = "고민 ID를 이용하여 공지사항  및 댓글 가져오기 성공";

                PostMap.put("post", post);
                PostMap.put("replies", allList);

                result.data = PostMap;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "S-4405";
                eresult.message = "공지 번호에 해당되는 고민 글이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPostByNo");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알 수 없는 회원 입니다. 고민 글을 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;

    }

    @ApiOperation(value = "제목에 해당되는 고민 반환", response = List.class)
    @GetMapping("/post/post-title")
    public Object seasrchPostByTitle(@RequestParam String title, @RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");
            Page<Post> pList = postDao.findPostByTitle(title, pageable);

            if (!pList.isEmpty()) {

                pList.forEach((e) -> {

                    // 고민에 해당되는 좋아요, 싫어요 가져오기
                    Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                    Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                    Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();

                    e.setLikeCount(like + likelike);
                    e.setUnlikeCount(unlike);

                    if (e.getWriter().equals(nickname)) {
                        e.setMine(true);
                    }

                    if (e.isSecret() == true) {
                        e.setWriter("익명의 작성자");
                    }

                });

                result.status = "S-200";
                result.message = "제목을 이용하여 고민글들 가져오기 성공";
                result.data = pList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "S-4406";
                eresult.message = "제목에 해당 되는고민 글들이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPostByTitle");
                errorMap.put("data", pageable);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }
        return response;
    }

    @ApiOperation(value = "제목 또는 작성자에서 keyword를 포함하는 고민 반환", response = List.class)
    @GetMapping("/post/all")
    public Object seasrchPostByTitleORWriter(@RequestParam(required = false) String keyword, @RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");

            if (keyword.contains("익명")) {
                Page<Post> aList = postDao.findPostByAnonymousAll(keyword, pageable);

                if (!aList.isEmpty()) {

                    aList.forEach((e) -> {

                        // 고민에 해당되는 좋아요, 싫어요 가져오기
                        Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                        Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                        Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();

                        e.setLikeCount(like + likelike);
                        e.setUnlikeCount(unlike);

                        if (e.getWriter().equals(nickname)) {
                            e.setMine(true);
                        }
                        if (e.isSecret() == true) {
                            e.setWriter("익명의 작성자");
                        }
                    });

                    result.status = "S-200";
                    result.message = "작성자 또는 제목을 이용하여 고민 목록들 가져오기 성공";
                    result.data = aList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4407";
                    eresult.message = "제목 또는 작성자에 해당 되는고민 글들이  없습니다..";
                    eresult.data = null;
                    errorMap.put("field", "noPostByWriterORTitle");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }
            } else {
                Page<Post> pList = postDao.findPostByTitleORWriter(keyword, pageable);

                if (!pList.isEmpty()) {

                    pList.forEach((e) -> {

                        // 고민에 해당되는 좋아요, 싫어요 가져오기
                        Integer like = likecountDao.countTotal(e.getNo(), "p").intValue();
                        Integer likelike = likecountDao.countTotal(e.getNo(), "pp").intValue();
                        Integer unlike = likecountDao.countTotal(e.getNo(), "m").intValue();

                        e.setLikeCount(like + likelike);
                        e.setUnlikeCount(unlike);

                        if (e.getWriter().equals(nickname)) {
                            e.setMine(true);
                        }

                        if (e.isSecret() == true) {
                            e.setWriter("익명의 작성자");
                        }

                    });

                    result.status = "S-200";
                    result.message = "제목을 이용하여 고민글들 가져오기 성공";
                    result.data = pList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "S-4407";
                    eresult.message = "제목 또는 작성자에 해당 되는고민 글들이  없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noPostByWriterORTitle");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }
            }

        }
        return response;
    }

    @ApiOperation(value = "고민 글 작성", notes = "Input: PostCreateRequest OutPut: 성공(status = true, data= sucess), 실패(status=false, data=오류 디버그 메세지)", response = List.class)
    @PostMapping("/post")
    public Object createPost(@RequestBody PostCreateRequest post) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(post.getWriter());

        if (!optUser.isPresent()) {
            eresult.status = "E-4408";
            eresult.message = "알 수 없는 회원 입니다. 고민 글을 작성 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", post.getWriter());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);

        } else {

            LocalDateTime currentTime = LocalDateTime.now();
            Post ctemp = new Post(post.getWriter(), post.getCategoryId(), post.getTitle(), post.getContent(),
                    currentTime, post.getAllow(), post.getSecret());
            postDao.save(ctemp);
            result.status = "S-200";
            result.message = "고민 글 작성에 성공했습니다.";
            result.data = null;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        }
        return response;
    }

    @ApiOperation(value = "고민글 삭제", response = List.class)
    @DeleteMapping("/post")
    public Object deletePost(@RequestParam BigInteger no,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            Optional<Post> optPost = postDao.findPostByNo(no);

            if (optPost.isPresent()) {
                Post ptemp = optPost.get();
                // 관리자 또는 글 작성자 인 경우
                if (nickname.equals("admin") || ptemp.getWriter().equals(nickname)) {

                    postDao.delete(ptemp);

                    result.status = "S-200";
                    result.message = "고민 글 삭제 완료";
                    result.data = null;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4409";
                    eresult.message = "허가 된 계정이 아닙니다. 글을 삭제 할 수 없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "deletePost");
                    errorMap.put("data", nickname);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
                }
            } else {
                eresult.status = "E-4410";
                eresult.message = "존재하지 않는 고민입니다. 고민을 삭제 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deletePost");
                errorMap.put("data", no);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알 수 없는 회원 입니다. 고민 글을 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "고민 글 내용 변경", response = List.class)
    @PutMapping("/post")
    public Object updatePost(@RequestBody PostUpdateRequest post,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        // 회원이 아닐 때
        if (optUser.isPresent()) {
            Optional<Post> optPost = postDao.findPostByNo(post.getNo());
            Post updateTemp = optPost.get();
            if (optPost.isPresent()) {
                if (updateTemp.getWriter().equals(nickname) || nickname.equals("admin")) {

                    updateTemp.setAllow(post.getAllow());
                    updateTemp.setCategoryId(post.getCategoryId());
                    updateTemp.setContent(post.getContent());
                    updateTemp.setTitle(post.getTitle());
                    updateTemp.setSecret(post.getSecret());

                    postDao.save(updateTemp);
                    result.status = "S-200";
                    result.message = "고민 글 수정 완료";
                    result.data = null;

                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    eresult.status = "E-4411";
                    eresult.message = "허가 된 계정이 아닙니다. 고민 글을 수정 할 수 없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "updatePost");
                    errorMap.put("data", nickname);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
                }
            } else {
                eresult.status = "E-4412";
                eresult.message = "존재하지 않는 질문입니다. 질문을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updatePost");
                errorMap.put("data", post.getNo());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 질문을 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;

    }

    @ApiOperation(value = "고민 글에 해당하는 답변 전체 목록 반환", notes = "Input : page, postNo Output: 성공 : [status = true, data = 고민 리스트(Post)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping("/reply")
    public Object retrieveReplyByPostNo(@RequestParam BigInteger postNo, @RequestParam int page,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            Optional<Post> optPost = postDao.findPostByNo(postNo);

            if (optPost.isPresent()) {
                Post post = optPost.get();

                PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id");
                Page<Reply> rList = replyDao.findReplyByPostNo(postNo, pageable);

                if (!rList.isEmpty()) {
                    rList.forEach((e) -> {

                        // 답변에 해당되는 좋아요, 싫어요 가져오기
                        Integer like = replylikecountDao.countTotal(e.getId(), "p").intValue();
                        Integer likelike = replylikecountDao.countTotal(e.getId(), "pp").intValue();
                        Integer unlike = replylikecountDao.countTotal(e.getId(), "m").intValue();

                        e.setLikeCount(like + likelike);
                        e.setUnlikeCount(unlike);
                        if (e.getWriter().equals(nickname)) {
                            e.setMine(true);
                        }
                        if (nickname.equals(post.getWriter())) {
                            e.setAuthor(true);
                        }
                        if (e.isSecret() == true) {
                            e.setWriter("익명의 작성자");
                        }
                    });
                    result.status = "S-200";
                    result.message = "고민글에 해당하는 답변 불러오기에 성공했습니다.";
                    result.data = rList;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4413";
                    eresult.message = "불러 올 답변이  없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noReply");
                    errorMap.put("data", pageable);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4414";
                eresult.message = "불러 올 고민이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPost");
                errorMap.put("data", postNo);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }
        return response;

    }

    @ApiOperation(value = "작성자가 작성한 전체 답변 목록 반환", notes = "Input : page, 작성자 Output: 성공 : [status = true, data = 고민 리스트(Post)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping("/reply/writer")
    public Object retrieveReplyByWriter(@RequestParam String writer, @RequestParam int page) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4400";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "id");
            Page<Reply> rList = replyDao.findReplyByWriter(writer, pageable);
            if (!rList.isEmpty()) {

                rList.forEach(e -> {
                    // 답변에 해당되는 좋아요, 싫어요 가져오기
                    Integer like = replylikecountDao.countTotal(e.getId(), "p").intValue();
                    Integer likelike = replylikecountDao.countTotal(e.getId(), "pp").intValue();
                    Integer unlike = replylikecountDao.countTotal(e.getId(), "m").intValue();

                    e.setLikeCount(like + likelike);
                    e.setUnlikeCount(unlike);
                });

                result.status = "S-200";
                result.message = "작성자가 작성한 모든 답변 불러오기에 성공했습니다.";
                result.data = rList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "E-4413";
                eresult.message = "불러 올 답변이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noReply");
                errorMap.put("data", pageable);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }
        return response;

    }

    @ApiOperation(value = "답변 작성", notes = "Input: ReplyCreateRequest OutPut: 성공(status = true, data= sucess), 실패(status=false, data=오류 디버그 메세지)", response = List.class)
    @PostMapping("/reply")
    public Object createReply(@RequestBody ReplyCreateRequest reply) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(reply.getWriter());

        if (!optUser.isPresent()) {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 답변을  작성 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);

        } else {
            Optional<Post> optPost = postDao.findPostByNo(reply.getPostNo());

            if (optPost.isPresent()) {

                LocalDateTime currentTime = LocalDateTime.now();
                Reply ptemp = new Reply(reply.getWriter(), reply.getPostNo(), reply.getContent(), currentTime,
                        reply.isSecret());
                replyDao.save(ptemp);
                result.status = "S-200";
                result.message = "답변 작성에 성공했습니다.";
                result.data = null;
                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "E-4415";
                eresult.message = "존재하지 않는 고민글입니다. 답변 글을 작성 할 수  없습니다.";
                eresult.data = null;
                errorMap.put("field", "createReply");
                errorMap.put("data", reply.getPostNo());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        }

        return response;
    }

    @ApiOperation(value = "고민 답변 글 삭제", response = List.class)
    @DeleteMapping("/reply")
    public Object deleteReply(@RequestParam BigInteger id,
            @RequestHeader(value = "nickname", required = false) String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            Optional<Reply> rtemp = replyDao.findReplyById(id);

            if (!rtemp.isPresent()) {
                eresult.status = "E-4416";
                eresult.message = "존재하지 않는 답변입니다. 답변을 삭제 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deleteReply");
                errorMap.put("data", id);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            } else {
                // 관리자 또는 글 작성자 인 경우
                Reply temp = rtemp.get();
                if (nickname.equals("admin") || temp.getWriter().equals(nickname)) {

                    Optional<Reply> rrtemp = replyDao.findReplyById(id);
                    temp = rrtemp.get();
                    replyDao.delete(temp);

                    result.status = "S-200";
                    result.message = "답변 글 삭제 완료";
                    result.data = null;
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4417";
                    eresult.message = "허가 된 계정이 아닙니다. 답변을 삭제 할 수 없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "deleteReply");
                    errorMap.put("data", nickname);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
                }
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 답변을 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "답변 글 내용 변경", response = List.class)
    @PutMapping("/reply")
    public Object updateReply(@RequestBody ReplyUpdateRequest reply,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            Optional<Reply> optReply = replyDao.findReplyById(reply.getId());

            if (optReply.isPresent()) {
                Reply temp = optReply.get();
                if (reply.getWriter().equals("admin") || temp.getWriter().equals(nickname)) {
                    Optional<Reply> utemp = replyDao.findReplyById(reply.getId());
                    Reply updateTemp = utemp.get();
                    updateTemp.setContent(reply.getContent());
                    updateTemp.setSecret(reply.isSecret());
                    updateTemp.setSelected(reply.isSelected());

                    replyDao.save(updateTemp);
                    result.status = "S-200";
                    result.message = "답변 글 수정 완료";
                    result.data = null;

                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    eresult.status = "E-448";
                    eresult.message = "허가 된 계정이 아닙니다. 답변 글을 수정 할 수 없습니다.";
                    eresult.data = null;
                    errorMap.put("field", "updateReply");
                    errorMap.put("data", reply.getWriter());
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
                }
            } else {
                eresult.status = "E-4419";
                eresult.message = "존재하지 않는 답변입니다. 답변을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updateReply");
                errorMap.put("data", reply.getId());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 답변을 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;

    }
    
    @ApiOperation(value = "allow = true이면서 히스토리에 없는 고민 글 목록 반환")
    @GetMapping("/post/allowed")
    public Object retrieveAllowedPost(@RequestParam int page) {
        ResponseEntity<BasicResponse> response = null;
        Map<String, Object> errors = new HashMap<>();

        PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "no");
        Page<Post> pList = postDao.findPostByAllowIsTrueAndNotInHistory(pageable);

        if (pList.isEmpty()) {
            errors.put("field", "lastPage");
            errors.put("data", page);
            ErrorResponse result = new ErrorResponse();
            result.status = "E-4401";
            result.message = "불러올 고민이 없습니다.";
            result.errors = errors;

            response = new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        } else {
            BasicResponse result = new BasicResponse();
            result.status = "S-200";
            result.message = "고민 목록 불러오기에 성공했습니다.";
            result.data = pList;

            response = new ResponseEntity<>(result, HttpStatus.OK);
        }

        return response;
    }

    @ApiOperation(value = "카테고리 목록 가져오기", notes = "output: 전체 가테고리 리스트")
    @GetMapping("/category")
    public Object retrieveCategory() {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        List<Category> cList = categoryDao.findAll();

        if (!cList.isEmpty()) {
            result.status = "S-200";
            result.message = "고민 목록 불러오기에 성공했습니다.";
            result.data = cList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "E-4420";
            eresult.message = "카테고리가 없습니다.";
            eresult.data = null;
            errorMap.put("field", "noCategory");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation(value = "카테고리 생성", notes = "현재 로그인 유저 닉네임, CategoryRequest")
    @PostMapping("/category")
    public Object createCategory(@RequestBody CategoryCreateRequest crequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            if (nickname.equals("admin")) {
                Optional<Category> optCate = categoryDao.findCategoryByName(crequest.getName());

                if (!optCate.isPresent()) {

                    Category temp = new Category(crequest.getName(), crequest.getDescription());
                    categoryDao.save(temp);

                    result.status = "S-200";
                    result.message = "카테고리 생성에 성공했습니다.";
                    result.data = crequest.getName();
                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4421";
                    eresult.message = "이미 카테고리가 있습니다.";
                    eresult.data = null;
                    errorMap.put("field", "existCategory");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                }
            } else {
                eresult.status = "E-4422";
                eresult.message = "카테고리를 만들 수 있는 권한이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "createCategory");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 답변을 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "카테고리 삭제", notes = "Input : 지우려는 카테고리 이름, 현재 로그인 되어있는 사람 Output: 성공 - null, 실패 : 중복시 카테고리 이름 반환")
    @DeleteMapping("/category")
    public Object deleteCategory(@RequestParam String categoryName,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> optUser = userDao.findByNickname(nickname);

        if (optUser.isPresent()) {
            if (nickname.equals("admin")) {
                Optional<Category> optCate = categoryDao.findCategoryByName(categoryName);

                if (optCate.isPresent()) {
                    Category ctemp = optCate.get();
                    categoryDao.delete(ctemp);

                    result.status = "S-200";
                    result.message = "카테고리 삭제에 성공했습니다.";
                    result.data = categoryName;
                    response = new ResponseEntity<>(eresult, HttpStatus.OK);
                } else {
                    eresult.status = "E-4423";
                    eresult.message = "지우려는 카테고리가 존재하지 않습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noCategory");
                    errorMap.put("data", categoryName);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4424";
                eresult.message = "카테고리를 지울 수 있는 권한이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deleteCategory");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 카테고리를 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        }

        return response;
    }

    @ApiOperation(value = "카테고리 내용 변경", notes = "input CategoryRequest, 현재 로그인 닉네임 output : 성공 여부")
    @PutMapping("/category")
    public Object updateCategory(@RequestBody CategoryUpdateRequest urequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);

        if (!userOpt.isPresent()) {
            eresult.status = "E-4408";
            eresult.message = "알수 없는 유저입니다. 카테고리를 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "unkownUser");
            errorMap.put("data", null);
            eresult.errors = errorMap;
            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
        } else {
            if (nickname.equals("admin")) {

                Optional<Category> cateOpt = categoryDao.findCategoryByName(urequest.getCurName());

                if (cateOpt.isPresent()) {

                    Optional<Category> duplicateOpt = categoryDao.findCategoryByName(urequest.getChangName());

                    if (!duplicateOpt.isPresent()) {

                        Category ctemp = cateOpt.get();
                        ctemp.setName(urequest.getChangName());
                        ctemp.setDescription(urequest.getDescription());
                        categoryDao.save(ctemp);

                        result.status = "S-200";
                        result.message = "카테고리 수정에 성공했습니다.";
                        result.data = urequest.getChangName();
                        response = new ResponseEntity<>(result, HttpStatus.OK);
                    }

                    else {
                        eresult.status = "E-4425";
                        eresult.message = "중복된 카테고리 이름입니다.";
                        eresult.data = null;
                        errorMap.put("field", "existCategoryName");
                        errorMap.put("data", urequest.getChangName());
                        eresult.errors = errorMap;

                        response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                    }

                } else {
                    eresult.status = "E-4426";
                    eresult.message = "수정하려는 카테고리가 존재하지 않습니다.";
                    eresult.data = null;
                    errorMap.put("field", "noCategory");
                    errorMap.put("data", urequest.getCurName());
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }

            } else {
                eresult.status = "E-4427";
                eresult.message = "카테고리를 수정 할 수 있는 권한이 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updateCategory");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        }

        return response;
    }

    @ApiOperation(value = "어떤 글의 고민 좋아요, 싫어요, 더 좋아요 총 갯수 가져오기")
    @GetMapping("/post/like")
    public Object retrieveLikeCountPost(@RequestParam BigInteger postNo,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Long plus = (long) 0, minus = (long) 0, plus_plus = (long) 0;

        Optional<Post> optPost = postDao.findPostByNo(postNo);

        if (optPost.isPresent()) {
            plus = likecountDao.countTotal(postNo, "p");
            minus = likecountDao.countTotal(postNo, "m");
            plus_plus = likecountDao.countTotal(postNo, "pp");

            Map<String, Object> resultMap = new HashMap<>();

            result.status = "S-200";
            result.message = "좋아요 개수 불러오기 성공";
            resultMap.put("like", plus + plus_plus);
            resultMap.put("unlike", minus);
            result.data = resultMap;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            eresult.status = "E-4428";
            eresult.message = "해당 번호의 게시글이 없습니다";
            eresult.data = null;
            errorMap.put("field", "noPost");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation("종아요, 싫어요 추가")
    @PostMapping("/post/like")
    public Object createLikeCountPost(@RequestBody LikeCountRequest lcrequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<LikeCount> tOpt = likecountDao.checkExistLikeCount(user.getUid(), lcrequest.getType(),
                    lcrequest.getPostNo());

            if (lcrequest.getType().equals("p")) {

                Optional<LikeCount> converseOpt = likecountDao.checkExistLikeCount(user.getUid(), "m",
                        lcrequest.getPostNo());

                if (!converseOpt.isPresent()) {
                    if (!tOpt.isPresent()) {

                        LocalDateTime currentTime = LocalDateTime.now();
                        LikeCount lc = new LikeCount(lcrequest.getPostNo(), BigInteger.valueOf(user.getUid()),
                                currentTime, lcrequest.getType());
                        likecountDao.save(lc);

                        result.status = "S-200";
                        result.message = "좋아요 추가  성공";
                        result.data = null;

                        response = new ResponseEntity<>(result, HttpStatus.OK);
                    } else {
                        eresult.status = "E-4429";
                        eresult.message = "이미 좋아요를 눌렀습니다.";
                        eresult.data = null;
                        errorMap.put("field", "existLike");
                        errorMap.put("data", null);
                        eresult.errors = errorMap;

                        response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                    }
                } else {
                    eresult.status = "E-4430";
                    eresult.message = "이미 싫어요를 눌렀습니다. 해제하고 다시 시도하세요";
                    eresult.data = null;
                    errorMap.put("field", "existUnLike");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                }

            } else if (lcrequest.getType().equals("m")) {

                Optional<LikeCount> converseOpt = likecountDao.checkExistLikeCount(user.getUid(), "p",
                        lcrequest.getPostNo());

                if (!converseOpt.isPresent()) {
                    if (!tOpt.isPresent()) {

                        LocalDateTime currentTime = LocalDateTime.now();
                        LikeCount lc = new LikeCount(lcrequest.getPostNo(), BigInteger.valueOf(user.getUid()),
                                currentTime, lcrequest.getType());
                        likecountDao.save(lc);

                        result.status = "S-200";
                        result.message = "싫어요 추가  성공";
                        result.data = null;

                        response = new ResponseEntity<>(result, HttpStatus.OK);
                    } else {
                        eresult.status = "E-4431";
                        eresult.message = "이미 싫어요를 눌렀습니다.";
                        eresult.data = null;
                        errorMap.put("field", "existUnLike");
                        errorMap.put("data", null);
                        eresult.errors = errorMap;

                        response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                    }
                } else {
                    eresult.status = "E-4432";
                    eresult.message = "이미 좋아요를 눌렀습니다. 해제하고 다시 시도하세요";
                    eresult.data = null;
                    errorMap.put("field", "existLike");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                }

                // 추가 하는 것이 또 좋아요 기능인 경우
            } else if (lcrequest.getType().equals("pp")) {
                Optional<LikeCount> pOpt = likecountDao.checkExistLikeCount(user.getUid(), "p", lcrequest.getPostNo());

                // 좋아요가 있는지 체크
                if (pOpt.isPresent()) {
                    // 또 좋아요가 있는지 체크
                    if (!tOpt.isPresent()) {
                        LocalDateTime currentTime = LocalDateTime.now();
                        LikeCount lc = new LikeCount(lcrequest.getPostNo(), BigInteger.valueOf(user.getUid()),
                                currentTime, lcrequest.getType());
                        likecountDao.save(lc);

                        result.status = "S-200";
                        result.message = "또 좋아요 추가  성공";
                        result.data = null;

                        response = new ResponseEntity<>(result, HttpStatus.OK);
                    } else {
                        eresult.status = "E-4433";
                        eresult.message = "이미 또 좋아요를 눌렀습니다.";
                        eresult.data = null;
                        errorMap.put("field", "existUnLike");
                        errorMap.put("data", null);
                        eresult.errors = errorMap;

                        response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                    }
                } else {
                    eresult.status = "E-4434";
                    eresult.message = "좋아요를 누르지 않았습니다. 먼저 좋아요를 눌러주세요.";
                    eresult.data = null;
                    errorMap.put("field", "needPreLike");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }
            } else {
                eresult.status = "E-4435";
                eresult.message = "잘못 된 좋아요 타입 입니다";
                eresult.data = null;
                errorMap.put("field", "noExistType");
                errorMap.put("data", lcrequest.getType());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        } else {
            eresult.status = "E-4408";
            eresult.message = "존재하지 않는 유저 입니다";
            eresult.data = null;
            errorMap.put("field", "unKnownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @ApiOperation("좋아요, 싫어요 삭제")
    @DeleteMapping("/post/like")
    public Object deleteLikeCountPost(@RequestBody LikeCountRequest lcrequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (lcrequest.getType().equals("p") || lcrequest.getType().equals("pp")
                    || lcrequest.getType().equals("m")) {
                Optional<LikeCount> lcOpt = likecountDao.checkExistLikeCount(user.getUid(), lcrequest.getType(),
                        lcrequest.getPostNo());
                if (lcOpt.isPresent()) {
                    LikeCount lctemp = lcOpt.get();

                    likecountDao.delete(lctemp);

                    result.status = "S-200";
                    result.message = "좋아요, 싫어요 삭제  성공";
                    result.data = null;
                    response = new ResponseEntity<>(result, HttpStatus.OK);

                } else {
                    eresult.status = "E-4436";
                    eresult.message = "해당 글에 좋아요, 싫어요를 표시한 적이 없습니다";
                    eresult.data = null;
                    errorMap.put("field", "noLikeCount");
                    errorMap.put("postNo", lcrequest.getPostNo());
                    errorMap.put("nickname", nickname);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }
            } else {
                eresult.status = "E-4435";
                eresult.message = "잘못 된 좋아요 타입 입니다";
                eresult.data = null;
                errorMap.put("field", "noExistType");
                errorMap.put("data", lcrequest.getType());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        } else {
            eresult.status = "E-4408";
            eresult.message = "존재하지 않는 유저 입니다";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @ApiOperation(value = "어떤  답변 글의 고민 좋아요, 싫어요, 더 좋아요 총 갯수 가져오기")
    @GetMapping("/reply/like")
    public Object retrieveLikeCountReply(@RequestParam BigInteger replyId,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Long plus = (long) 0, minus = (long) 0, plus_plus = (long) 0;

        Optional<Reply> optReply = replyDao.findReplyById(replyId);

        if (optReply.isPresent()) {
            plus = replylikecountDao.countTotal(replyId, "p");
            minus = replylikecountDao.countTotal(replyId, "m");
            plus_plus = replylikecountDao.countTotal(replyId, "pp");

            Map<String, Object> resultMap = new HashMap<>();

            result.status = "S-200";
            result.message = "답변 글의 좋아요 개수 불러오기 성공";
            resultMap.put("like", plus + plus_plus);
            resultMap.put("unlike", minus);
            result.data = resultMap;
            response = new ResponseEntity<>(result, HttpStatus.OK);

        } else {
            eresult.status = "E-4437";
            eresult.message = "해당 번호의 게시글이 없습니다";
            eresult.data = null;
            errorMap.put("field", "noPost");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation("댓 글에 종아요, 싫어요 추가")
    @PostMapping("/reply/like")
    public Object createLikeCountReply(@RequestBody ReplyLikeCountRequest rlcrequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            Optional<ReplyLikeCount> tOpt = replylikecountDao.checkExistLikeCount(user.getUid(), rlcrequest.getType(),
                    rlcrequest.getReplyId());

            if (rlcrequest.getType().equals("p")) {

                if (!tOpt.isPresent()) {

                    LocalDateTime currentTime = LocalDateTime.now();
                    ReplyLikeCount rlc = new ReplyLikeCount(rlcrequest.getReplyId(), BigInteger.valueOf(user.getUid()),
                            currentTime, rlcrequest.getType());
                    replylikecountDao.save(rlc);

                    result.status = "S-200";
                    result.message = "좋아요 추가  성공";
                    result.data = null;

                    response = new ResponseEntity<>(result, HttpStatus.OK);
                } else {
                    eresult.status = "E-4431";
                    eresult.message = "이미 좋아요를 눌렀습니다.";
                    eresult.data = null;
                    errorMap.put("field", "existLike");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                }
            } else if (rlcrequest.getType().equals("m")) {
                if (!tOpt.isPresent()) {

                    LocalDateTime currentTime = LocalDateTime.now();
                    ReplyLikeCount rlc = new ReplyLikeCount(rlcrequest.getReplyId(), BigInteger.valueOf(user.getUid()),
                            currentTime, rlcrequest.getType());
                    replylikecountDao.save(rlc);

                    result.status = "S-200";
                    result.message = "싫어요 추가  성공";
                    result.data = null;

                    response = new ResponseEntity<>(eresult, HttpStatus.OK);
                } else {
                    eresult.status = "E-4432";
                    eresult.message = "이미 싫어요를 눌렀습니다.";
                    eresult.data = null;
                    errorMap.put("field", "existUnLike");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                }
                // 추가 하는 것이 또 좋아요 기능인 경우
            } else if (rlcrequest.getType().equals("pp")) {
                Optional<ReplyLikeCount> pOpt = replylikecountDao.checkExistLikeCount(user.getUid(), "p",
                        rlcrequest.getReplyId());

                // 좋아요가 있는지 체크
                if (pOpt.isPresent()) {
                    // 또 좋아요가 있는지 체크
                    if (!tOpt.isPresent()) {
                        LocalDateTime currentTime = LocalDateTime.now();
                        ReplyLikeCount rlc = new ReplyLikeCount(rlcrequest.getReplyId(),
                                BigInteger.valueOf(user.getUid()), currentTime, rlcrequest.getType());
                        replylikecountDao.save(rlc);
                        result.status = "S-200";
                        result.message = "또 좋아요 추가  성공";
                        result.data = null;

                        response = new ResponseEntity<>(result, HttpStatus.OK);
                    } else {
                        eresult.status = "E-4433";
                        eresult.message = "이미 또 좋아요를 눌렀습니다.";
                        eresult.data = null;
                        errorMap.put("field", "existUnLike");
                        errorMap.put("data", null);
                        eresult.errors = errorMap;

                        response = new ResponseEntity<>(eresult, HttpStatus.CONFLICT);
                    }
                } else {
                    eresult.status = "E-4434";
                    eresult.message = "좋아요를 누르지 않았습니다. 먼저 좋아요를 눌러주세요.";
                    eresult.data = null;
                    errorMap.put("field", "needPreLike");
                    errorMap.put("data", null);
                    eresult.errors = errorMap;

                    response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
                }
            } else {
                eresult.status = "E-4438";
                eresult.message = "잘못 된 좋아요 타입 입니다";
                eresult.data = null;
                errorMap.put("field", "noExistType");
                errorMap.put("data", rlcrequest.getType());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        } else {
            eresult.status = "E-4408";
            eresult.message = "존재하지 않는 유저 입니다";
            eresult.data = null;
            errorMap.put("field", "unknowUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @ApiOperation("좋아요, 싫어요 삭제")
    @DeleteMapping("/reply/like")
    public Object deleteLikeCountReply(@RequestBody ReplyLikeCountRequest rlcrequest,
            @RequestHeader(value = "nickname", required = false) String nickname) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<User> userOpt = userDao.findByNickname(nickname);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            Optional<LikeCount> lcOpt = likecountDao.checkExistLikeCount(user.getUid(), rlcrequest.getType(),
                    rlcrequest.getReplyId());
            if (lcOpt.isPresent()) {
                LikeCount lctemp = lcOpt.get();

                likecountDao.delete(lctemp);

                result.status = "S-200";
                result.message = "좋아요, 싫어요 삭제  성공";
                result.data = null;
                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else {
                eresult.status = "E-4439";
                eresult.message = "해당 글에 좋아요, 싫어요를 표시한 적이 없습니다";
                eresult.data = null;
                errorMap.put("field", "noLikeCount");
                errorMap.put("replyId", rlcrequest.getReplyId());
                errorMap.put("nickname", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        } else {
            eresult.status = "E-4408";
            eresult.message = "존재하지 않는 유저 입니다";
            eresult.data = null;
            errorMap.put("field", "unknownUser");
            errorMap.put("data", nickname);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

}
