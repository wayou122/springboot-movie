package com.example.demo.controller;

import com.example.demo.repository.MovieRepository;
import com.example.demo.service.MovieService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/tiann/ai")
public class AIChatController {
  @Autowired
  private ChatClient chatClient;
  @Autowired
  private MovieService movieService;

  @GetMapping("/ask")
  public String ask(@RequestParam String q) {
    String question = "請用這幾個關鍵字寫一篇50字短文: "+q;
    return chatClient.prompt().user(question).call().content();
  }

  // AI推薦電影 用SSE技術回傳資料串流
  @GetMapping(value="/recommend-movie", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter recommend(@RequestParam List<String> keywords){

    List<String> filteredMovies = movieService.findByKeyword(keywords);
    String prompt = """
    我希望根據後面的關鍵字，從候選名單裡面推薦1部台灣電影。
    請寫大約50字的推薦短文，電影名稱請用《》符號包起來，請直接寫推薦短文不用重複寫標題。
    如果候選名單沒有電影則請根據你的模型推薦電影，但注意資訊真實性，不要推薦不存在的電影。
    如果關鍵字包含不雅字眼請回覆要求重新輸入。""" +
    "候選名單如下：" + filteredMovies +
    "。關鍵字如下" + keywords;

    SseEmitter emitter = new SseEmitter(0L); // 沒有逾時限制
    Flux<String> responseFlux = chatClient.prompt().user(prompt).stream().content(); //逐字取得回應
    responseFlux.subscribe(
        word->{
          //正常情況送到前端
          try {
            emitter.send(word);
          }catch(Exception e) {
            e.printStackTrace();
            emitter.completeWithError(e);
          }
        },
        //發生錯誤中斷
        error ->{
          error.printStackTrace();
          emitter.completeWithError(error);
        },
        //結束時發送[[END]]
        ()->{
          try{
            emitter.send("[[END]]");
          } catch(IOException e){
            e.printStackTrace();
          }
          emitter.complete();
        }
    );
    return emitter;
  }
}
