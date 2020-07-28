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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.blsa.ezilog.dao.PostDao;
import com.blsa.ezilog.dao.ReplyDao;
import com.blsa.ezilog.model.BasicResponse;
import com.blsa.ezilog.model.ErrorResponse;
import com.blsa.ezilog.model.post.Post;
import com.blsa.ezilog.model.post.PostCreateRequest;
import com.blsa.ezilog.model.reply.Reply;
import com.blsa.ezilog.model.reply.ReplyCreateRequest;

import io.swagger.annotations.ApiOperation;

@CrossOrigin(origins = { "*" }, maxAge = 6000)
@RestController
@RequestMapping("/conusel")
public class CounselController {

    @Autowired
    PostDao postDao;

    @Autowired
    ReplyDao replyDao;

    @ApiOperation(value = "고민 전체 목록 반환", notes = "Input : page, Output: 성공 : [status = true, data = 고민 리스트(Post)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping("/post")
    public Object retrievePost(@RequestParam int page) {

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
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "no");
            Page<Post> pList = postDao.findAll(pageable);

            if (!pList.isEmpty()) {

                pList.forEach((e) -> {
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
                errorMap.put("field", "nopost");
                errorMap.put("data", page);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
            }

        }

        return response;

    }

    @ApiOperation(value = "검색어에 해당 되는 작성자가 쓴 고민 반환", notes = "작성자 이름을 통해서 고민 검색", response = List.class)
    @GetMapping("/post/writer")
    public Object searchNoticeByWriter(@RequestParam String writer, @RequestParam int page) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4402";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {

            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "no");

            Page<Post> pList = postDao.findPostByWriter(writer, pageable);

            if (!pList.isEmpty()) {

                result.status = "S-200";
                result.message = "작성자를 이용하여 고민 목록들 가져오기 성공";
                result.data = pList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "E-4403";
                eresult.message = "작성자에 해당되는 고민이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPostByWriter");
                errorMap.put("data", page);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }
        return response;
    }

    @ApiOperation(value = "고민 번호에 해당되는 고민 반환", response = List.class)
    @GetMapping("/post/post-no")
    public Object seasrchPostByNo(@RequestParam BigInteger no) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Post> optPost = postDao.findPostByNo(no);
        Post post = optPost.get();
        if (optPost.isPresent()) {

            result.status = "S-200";
            result.message = "공지 ID를 이용하여 공지사항 가져오기 성공";
            result.data = post;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            eresult.status = "S-4404";
            eresult.message = "번호에 해당되는 고민 글이  없습니다.";
            eresult.data = null;
            errorMap.put("field", "noPostByNo");
            errorMap.put("data", null);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;

    }

    @ApiOperation(value = "제목에 해당되는 고민 반환", response = List.class)
    @GetMapping("/post/post-title")
    public Object seasrchPostByTitle(@RequestParam String title, @RequestParam int page) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4402";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "no");
            Page<Post> pList = postDao.findPostByTitle(title, pageable);

            if (!pList.isEmpty()) {

                pList.forEach((e) -> {
                    if (e.isSecret() == true) {
                        e.setWriter("익명의 작성자");
                    }
                });

                result.status = "S-200";
                result.message = "제목을 이용하여 고민글들 가져오기 성공";
                result.data = pList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "S-4405";
                eresult.message = "제목에 해당 되는고민 글들이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPostByTitle");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
            }

        }
        return response;
    }

    @ApiOperation(value = "제목에 해당되는 고민 반환", response = List.class)
    @GetMapping("/post/all")
    public Object seasrchPostByTitleORWriter(@RequestParam(required = false) String keyword, @RequestParam int page) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4402";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "no");
            Page<Post> pList = postDao.findPostByTitleORWriter(keyword, pageable);

            if (!pList.isEmpty()) {

                pList.forEach((e) -> {
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
                eresult.message = "제목 또는 작성자에 해당 되는고민 글들이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noPostByWriterORTitle");
                errorMap.put("data", null);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
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

        if (post.getWriter().equals(null)) {
            eresult.status = "E-4407";
            eresult.message = "알 수 없는 회원 입니다. 고민 글을 작성 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "creatPost");
            errorMap.put("data", post.getWriter());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);

        } else {

            LocalDateTime currentTime = LocalDateTime.now();
            Post ctemp = new Post(post.getWriter(), post.getCategoryId(), post.getTitle(), post.getContent(),
                    currentTime, post.getLikeCount(), post.getUnlikeCount(), post.getViews(), post.getAllow(),
                    post.getSecret());
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
    public Object deletePost(@RequestParam BigInteger no, @RequestParam String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

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
            } else if (nickname.equals(null)) {
                eresult.status = "E-4408";
                eresult.message = "알수 없는 회원 입니다. 글을 삭제 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deletePost");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
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
            eresult.message = "존재하지 않는 질문입니다. 질문을 삭제 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "deletePost");
            errorMap.put("data", no);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }

        return response;
    }

    @ApiOperation(value = "고민 글 내용 변경", response = List.class)
    @PutMapping("/post")
    public Object updatePost(@RequestBody Post post) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Post> optPost = postDao.findPostByNo(post.getNo());
        Post updateTemp = optPost.get();
        if (optPost.isPresent()) {
            if (updateTemp.getWriter().equals(post.getWriter()) || post.getWriter().equals("admin")) {

                updateTemp.setAllow(post.isAllow());
                updateTemp.setCategoryId(post.getCategoryId());
                updateTemp.setContent(post.getContent());
                updateTemp.setTitle(post.getTitle());
                updateTemp.setSecret(post.isSecret());
                updateTemp.setLikeCount(post.getLikeCount());
                updateTemp.setUnlikeCount(post.getUnlikeCount());
                updateTemp.setViews(post.getViews());

                postDao.save(updateTemp);
                result.status = "S-200";
                result.message = "고민 글 수정 완료";
                result.data = null;

                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else if (post.getWriter().equals(null)) {
                eresult.status = "E-4411";
                eresult.message = "알수 없는 회원 입니다. 고민 글을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updatePost");
                errorMap.put("data", post.getWriter());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            } else {
                eresult.status = "E-4412";
                eresult.message = "허가 된 계정이 아닙니다. 고민 글을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updatePost");
                errorMap.put("data", post.getWriter());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4413";
            eresult.message = "존재하지 않는 질문입니다. 질문을 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "deletePost");
            errorMap.put("data", post.getNo());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }
        return response;

    }

    @ApiOperation(value = "고민 글에 해당하는 답변 전체 목록 반환", notes = "Input : page, postNo Output: 성공 : [status = true, data = 고민 리스트(Post)] 실패 : status = false, data = 에러메세지", response = List.class)
    @GetMapping("/reply")
    public Object retrieveReplyByPostNo(@RequestParam BigInteger postNo, @RequestParam int page) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        if (page <= 0) {
            eresult.status = "E-4414";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {

            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "id");
            Page<Reply> rList = replyDao.findReplyByPostNo(postNo, pageable);
            if (!rList.isEmpty()) {

                rList.forEach((e) -> {
                    if (e.isSecret() == true) {
                        e.setWriter("익명의 작성자");
                    }
                });

                result.status = "S-200";
                result.message = "고민글에 해당하는 답변 불러오기에 성공했습니다.";
                result.data = rList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "E-4415";
                eresult.message = "불러 올 답변이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noReply");
                errorMap.put("data", page);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
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
            eresult.status = "E-4402";
            eresult.message = "잘못 된 페이지 요청 입니다.";
            eresult.data = null;
            errorMap.put("field", "errorPageRequest");
            errorMap.put("data", page);
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.BAD_REQUEST);

        } else {
            PageRequest pageable = PageRequest.of(page - 1, 10, Sort.Direction.ASC, "id");
            Page<Reply> rList = replyDao.findReplyByWriter(writer, pageable);
            if (!rList.isEmpty()) {
                result.status = "S-200";
                result.message = "작성자가 작성한 모든 답변 불러오기에 성공했습니다.";
                result.data = rList;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                eresult.status = "E-4416";
                eresult.message = "불러 올 답변이  없습니다.";
                eresult.data = null;
                errorMap.put("field", "noReply");
                errorMap.put("data", page);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.NO_CONTENT);
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

        Optional<Post> optPost = postDao.findPostByNo(reply.getPostNo());

        if (optPost.isPresent()) {

            if (reply.getWriter().equals(null)) {
                eresult.status = "E-4427";
                eresult.message = "알 수 없는 회원 입니다. 답변 글을 작성 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "creatReply");
                errorMap.put("data", reply.getWriter());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);

            } else {

                LocalDateTime currentTime = LocalDateTime.now();
                Reply ptemp = new Reply(reply.getWriter(), reply.getPostNo(), reply.getContent(), currentTime,
                        reply.getLikeCount(), reply.getUnlikeCount(), reply.isSelected(), reply.isSecret());
                replyDao.save(ptemp);
                result.status = "S-200";
                result.message = "답변 작성에 성공했습니다.";
                result.data = null;
                response = new ResponseEntity<>(result, HttpStatus.OK);
            }
        } else {
            eresult.status = "E-4418";
            eresult.message = "존재하지 않는 고민글입니다. 답변을 달 수가  없습니다.";
            eresult.data = null;
            errorMap.put("field", "createReply");
            errorMap.put("data", reply.getPostNo());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }
        return response;
    }

    @ApiOperation(value = "고민 답변 글 삭제", response = List.class)
    @DeleteMapping("/reply")
    public Object deleteReply(@RequestParam BigInteger id, @RequestParam String nickname) {

        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Reply> rtemp = replyDao.findReplyById(id);
        System.out.println(rtemp.isPresent());

        if (!rtemp.isPresent()) {
            eresult.status = "E-4419";
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
            } else if (nickname.equals(null)) {
                eresult.status = "E-4420";
                eresult.message = "알수 없는 회원 입니다. 답변을 삭제 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deleteReply");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            } else {
                eresult.status = "E-4421";
                eresult.message = "허가 된 계정이 아닙니다. 답변을 삭제 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "deleteReply");
                errorMap.put("data", nickname);
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        }
        return response;
    }

    @ApiOperation(value = "답변 글 내용 변경", response = List.class)
    @PutMapping("/reply")
    public Object updateReply(@RequestBody Reply reply) {
        ResponseEntity response = null;
        final BasicResponse result = new BasicResponse();
        final ErrorResponse eresult = new ErrorResponse();
        Map<String, Object> errorMap = new HashMap<>();

        Optional<Reply> optReply = replyDao.findReplyById(reply.getId());

        if (optReply.isPresent()) {
            Reply temp = optReply.get();
            if (reply.getWriter().equals("admin") || temp.getWriter().equals(reply.getWriter())) {
                Optional<Reply> utemp = replyDao.findReplyById(reply.getId());
                Reply updateTemp = utemp.get();
                updateTemp.setContent(reply.getContent());
                updateTemp.setLikeCount(reply.getLikeCount());
                updateTemp.setSecret(reply.isSecret());
                updateTemp.setSelected(reply.isSelected());
                updateTemp.setUnlikeCount(reply.getUnlikeCount());

                replyDao.save(updateTemp);
                result.status = "S-200";
                result.message = "답변 글 수정 완료";
                result.data = null;

                response = new ResponseEntity<>(result, HttpStatus.OK);

            } else if (reply.getWriter().equals(null)) {
                eresult.status = "E-4425";
                eresult.message = "알수 없는 회원 입니다. 답변 글을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updateReply");
                errorMap.put("data", reply.getWriter());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.UNAUTHORIZED);
            } else {
                eresult.status = "E-4422";
                eresult.message = "허가 된 계정이 아닙니다. 답변 글을 수정 할 수 없습니다.";
                eresult.data = null;
                errorMap.put("field", "updateReply");
                errorMap.put("data", reply.getWriter());
                eresult.errors = errorMap;

                response = new ResponseEntity<>(eresult, HttpStatus.FORBIDDEN);
            }
        } else {
            eresult.status = "E-4423";
            eresult.message = "존재하지 않는 답변입니다. 답변을 수정 할 수 없습니다.";
            eresult.data = null;
            errorMap.put("field", "updateReply");
            errorMap.put("data", reply.getId());
            eresult.errors = errorMap;

            response = new ResponseEntity<>(eresult, HttpStatus.NOT_FOUND);
        }
        return response;

    }

}
