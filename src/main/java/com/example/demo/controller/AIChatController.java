package com.example.demo.controller;

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

@RestController
@RequestMapping("/tiann/ai")
public class AIChatController {
  @Autowired
  private ChatClient chatClient;

  @GetMapping("/ask")
  public String ask(@RequestParam String q) {
    String question = "請用這幾個關鍵字寫一篇50字短文: "+q;
    return chatClient.prompt().user(question).call().content();
  }

  // 尋找一部電影
  @GetMapping(value="/recommend-movie", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
  public SseEmitter tour(@RequestParam String keywords){

    String prompt = """
    我希望根據後面的關鍵字推薦1部台灣電影，請寫大約50字的推薦短文。
    電影名稱請用《》符號包起來，直接寫短文不用重複寫標題。
    如果關鍵字包含不雅字眼請回覆要求重新輸入。
    關鍵字如下，
    """ + keywords;

    SseEmitter emitter = new SseEmitter(0L);
    Flux<String> responseFlux = chatClient.prompt().user(prompt).stream().content();
    responseFlux.subscribe(
        word->{
          try {
            emitter.send(word);
          }catch(Exception e) {
            e.printStackTrace();
            emitter.completeWithError(e);
          }
        },
        error ->{
          error.printStackTrace();
          emitter.completeWithError(error);
        },
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
