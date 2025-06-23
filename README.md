# 鼎電影專案
這是一個電影影評網站。使用者可以查尋電影、收藏電影、瀏覽影評、撰寫影評、按讚影評。還有即時聊天論壇（WebSocket）、景點地圖（React Leaflet）、AI 推薦電影（Spring AI）等功能。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(4).JPG)

## 系統架構
本系統採用前後端分離架構，後端使用Java、SpringBoot框架，採用Spring MVC架構。即時聊天訊息使用WebSocket技術、STOMP傳輸協定。資料存取使用JPA與資料庫連結。
前後端之間使用REST API互相溝通。前端使用React構成，以Bootstrap設定基礎樣式。地圖採用React Leaflet套件完成。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(5).JPG)

## 資料表結構
本系統的資料表符合第三正規化。
1. 所有欄位都是單一值，沒有複合值。
2. 所有非主鍵欄位都依賴於整個主鍵，也就是沒有部份相依。
3. 所有非主鍵欄位之間沒有依賴關係，也就是沒有移轉相依。

電影收藏表的關聯是使用者1:1電影，所以設計2個外鍵組成複合主鍵。影評按讚表的關聯是使用者1:1影評，所以也是2個外鑑組成複合主鍵。影評表的關聯是使用者1:1電影，但為了方便其他表格引用，設計獨立主鍵，不使用複合主鍵。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(6).JPG)

## 電影列表頁
使用者可以搜尋電影（電影標題）、排序（依評價高低、依評論數量、依上映日期）、篩選電影（依照電影類型劇情片/紀錄片/動畫/短片）。資料查詢使用Spring Data Pageable達到分頁查詢的效果，提升查詢與傳輸效率。查詢參數顯示在網址，讓使用者切換瀏覽器上下頁可以符合直覺體驗。

登入帳號後在卡片右側可以點選收藏。點選標題或電影海報則可以進入電影專頁。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(7).JPG)

### 電影專頁
使用者可以撰寫影評，此處限定一個人對一部電影只能評論一次。若已評論則可以點選編輯和刪除。此處API設計REST風格，編輯是PUT請求，刪除是DELETE請求。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(8).JPG)

使用者在電影專頁還可以瀏覽此部電影的所有評論，並且可排序（依評論日期、依熱門程度）、篩選評論（依照分數）。點選評論人名稱可以跳至評論人專頁。針對每一部影評則可以按讚、按倒讚，表達自己對這篇影評的感想。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(9).JPG)

### 影評人專頁
此頁面會列出影評人的所有影評，路徑參數直接使用影評人的名字，能夠讓使用者方便搜尋、儲存、分享。此處的影評也使用Pageable分頁查詢。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(10).JPG)

### 聊天論壇頁
此頁面可以在電影大型活動時啟用（如：頒獎典禮），讓使用者即時聊天互動交流。後端使用WebSocket傳輸技術，並且用STOMP協定實作即時通訊功能，讓訂閱和發布訊息更有效率。前端用SockJS連線至後端，並且建立stompClient客戶端連線，訂閱與發送消息。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(11).JPG)

### 景點地圖
有許多電影拍攝場景會成為影迷朝聖的地點，此頁面提供使用者共同編輯電影景點地圖，類似於開放街圖/地圖版維基百科，符合PPGIS公眾參與地理資訊系統的精神。此頁面使用React Leaflet套件架設，後端存取經緯度坐標，前端存取坐標呈現在地圖上。

使用者搜尋電影，可以篩選出此電影的景點，地圖根據地點縮放。景點資訊卡可以連結至電影頁面，也可以連結搜尋Google地圖，方便使用者收藏和規畫路徑。相對地，電影頁面也可以連結至景點地圖，自動篩選符合景點。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(12).JPG)

使用者可以自由新增景點。點選地圖會自動產出坐標，並且選擇拍攝的電影（限定選擇資料庫內的電影，用於建立電影外鍵）。新增完成後點選景點可以看到景點資訊，也可以連結至電影頁面。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(13).JPG)

## AI 推薦電影
此頁面讓使用者輸入或點選關鍵字，讓AI根據關鍵字推薦電影，解決許多人不知道看什麼的問題。前端發送請求，後端取得關鍵字組合成 prompt。後端使用 Spring AI 作為框架，並在本地用 Ollama 運行 gemma3 模型。為了讓前端有良好使用體驗，使用SSE單向通訊技術，後端用 WebFlux 資料流推播結果。前端用 EventSource API 建立連線，讓結果可以逐字顯示（如同chatGPT）。最後根據結果顯示本網站的電影連結。

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(14).JPG)

## 專案資訊
本專案由王昱堯在2025年5-6月獨自完成。
* 專案後端連結 https://github.com/wayou122/springboot-movie
* 專案前端連結 https://github.com/wayou122/react-movie

![image](https://github.com/wayou122/react-movie/blob/master/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E%E5%9C%96/%E5%B0%88%E6%A1%88%E8%AA%AA%E6%98%8E(15).JPG)


