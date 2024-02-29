package com.mysite.sbb.comment;

import com.mysite.sbb.question.Question;
import com.mysite.sbb.user.SiteUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    //아이디 생성
    public Comment create(Question question, SiteUser author, String content){
        Comment c=new Comment();
        c.setContent(content);
        c.setCreateDate(LocalDateTime.now());
        c.setQuestion(question);
        c.setAuthor(author);
        this.commentRepository.save(c); //수정
        return c;
    }

    //아이디 찾기
    public Optional<Comment> getComment(Integer id){
        return this.commentRepository.findById(id);
    }
    //아이디 수정
    public Comment modify(Comment c,String content){
        c.setContent(content);
        c.setModifyDate(LocalDateTime.now());
        this.commentRepository.save(c);
        return c;
    }

    public void delete(Comment c){
        this.commentRepository.delete(c);
    }

}
