package com.example.sbb;

import com.example.sbb.answer.Answer;
import com.example.sbb.answer.AnswerRepository;
import com.example.sbb.question.Question;
import com.example.sbb.question.QuestionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;


	@Test
	void testJPA(){
		Question q1 = new Question();
		q1.setSubject("what is sbb?");
		q1.setContent("I would learn about sbb");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);

		Question q2 = new Question();
		q2.setSubject("QuestionRepository Autowired question.");
		q2.setContent("how to autowired questionRepository??");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);

	}

	@Test
	void testFindAll(){
		List<Question> questionList = this.questionRepository.findAll();
		Assertions.assertEquals(questionList.size(), 3);

		Question q = questionList.get(0);
		Assertions.assertEquals(q.getSubject(), "what is sbb?");
	}

	@Test
	void testFindById(){
		Optional<Question> question = this.questionRepository.findById(1L);
		if(question.isPresent()){
			Question question1 = question.get();
			Assertions.assertEquals(question1.getSubject(), "what is sbb?");
		}
	}

	@Test
	void testFindBySubject(){
		Question bySubject = this.questionRepository.findBySubject("what is sbb?");
		Assertions.assertEquals(bySubject.getId(), 1L);
	}

	@Test
	void testFindJoin(){
		Question q = this.questionRepository.findBySubjectAndContent("what is sbb?", "I would learn about sbb");
		System.out.println(q.getContent());
	}

	@Test
	void testFindLike(){
		List<Question> bySubjectLike = this.questionRepository.findBySubjectLike("wh%");
		for (Question q : bySubjectLike){
			System.out.println(q.getSubject());
		}
	}

	@Test
	void testUpdate(){
		Optional<Question> byId = this.questionRepository.findById(4L);
		if(byId.isPresent()){
			Question question = byId.get();
			question.setSubject("updated Subject");
			this.questionRepository.save(question);
		}


	}

	@Test
	void testAnswerSave(){
		Optional<Question> byId = this.questionRepository.findById(1L);
		if(byId.isPresent()){
			Question question = byId.get();

			Answer answer = new Answer();
			answer.setContent("ok I already know that!");
			answer.setQuestion(question);
			answer.setCreateDate(LocalDateTime.now());

			//answer.setId(1L);
			this.answerRepository.save(answer);
		}


	}

	@Transactional
	@Test
	void testGetAnswerByQuestion(){
		Optional<Question> byId = this.questionRepository.findById(1L);
		if(byId.isPresent()) {
			Question question = byId.get();
			List<Answer> answerList = question.getAnswerList();
			Assertions.assertEquals(answerList.size(), 1);
			Assertions.assertEquals(answerList.get(0).getContent(), "ok I already know that!");
		}
	}

}
