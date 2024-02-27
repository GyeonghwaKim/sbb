package com.mysite.sbb.question;

import com.mysite.sbb.answer.AnswerForm;
import com.mysite.sbb.user.SiteUser;
import com.mysite.sbb.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.security.Principal;
import java.util.List;

@RequestMapping("/question") //prefix
@RequiredArgsConstructor//final 붙은 속성을 자동 생성자 방식으로 주입 해줌
@Controller
public class QuestionController {


    private final QuestionService questionService;
    private final UserService userService;


    @GetMapping("/list")
    public String list(Model model,@RequestParam(value = "page", defaultValue = "0") int page)
    {
        //List<Question> questionList=this.questionService.getList();
        Page<Question> paging=this.questionService.getList(page);
        model.addAttribute("paging",paging); //Model 클래스를 사용하여 조회한 질문 목록 데이터를 템플릿에 전달
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm){

        Question question=this.questionService.getQuestion(id);
        model.addAttribute("question",question);
        return "question_detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm){//th:object로 인해 QuestionForm이 필요함
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm,
                                 BindingResult bindingResult,//@Valid가 적옹된 객체
                                 Principal principal){
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        SiteUser siteUser=this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(),questionForm.getContent(),siteUser);
        return "redirect:/question/list";

    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm,@PathVariable("id") Integer id,
                                 Principal principal){
        Question question=this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");
        }
        questionForm.setContent(question.getContent());
        questionForm.setSubject(question.getSubject());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id){
        //question 데이터 검증
        if(bindingResult.hasErrors()){
            return "question_form";
        }
        Question question=this.questionService.getQuestion(id);

        //수정권한 있는지 확인
        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");
        }
        this.questionService.modify(question,questionForm.getSubject(),questionForm.getContent());
        return String.format("redirect:/question/detail/%s",id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id){
        Question question=this.questionService.getQuestion(id);

        if(!question.getAuthor().getUsername().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제 권한이 없습니다");
        }
        this.questionService.delete(question);
        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal,@PathVariable("id") Integer id){
        Question question=this.questionService.getQuestion(id);

        SiteUser siteUser=this.userService.getUser(principal.getName());

        this.questionService.vote(question,siteUser);
        return String.format("redirect:/question/detail/%s",id);
    }

}
