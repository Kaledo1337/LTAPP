# Учебное задание Perfomance QA x5 Group
---
 В данном репозитории предоставлен отчёт о выполненной работе стажёром Дустовым М.М. за отведённый период.
 
---
* 🏆 В ветку xs5 был склонирован репозиторий  *https://github.com/Penelopa23/LTAPP.git*, были изучены зависимости проекта для дальнейшей работы.
* 🏆 Для подключения брокера сообщений ***kafka*** через терминал установлена версия 7.3.0, также для корректной работы брокера установлен ***zookepeer*** той же версии. Для корректной работы образа в контейнере ***kafka*** была введена зависимость:

```
    depends_on:
        zookeeper:
          condition: service_healthy
```
Чтобы брокер запускался только после полного старта ***zookeeper***  (реализовано в *docker-compose.yaml* файле).
* 🏆 Остальные подключенные сервисы (в контейнерах) и занимаемые ими порты можно увидеть в следующей сводной таблице:
  
  | Название сервиса              | Порт хоста         |
  |------------------------------|--------------------|
  | xs5-grafana-1                | 3000               |
  | xs5-prometheus-1             | 9090               |
  | xs5-app-1                    | 8080               |
  | xs5-kafka-exporter-1         | 9308               |
  | xs5-kafka-1                  | 9092, 29092        |
  | xs5-postgres-exporter-1      | 9187               |
  | xs5-zookeeper-1              | 2181               |
  | xs5-postgres-1               | 5432               |
  | xs5-node-exporter-1          | 9100               |

* 🏆 Для того, чтобы запустить все сервисы с приложением, необходимо склонировать проект и в терминале прописать следующие команды:
  ### 1. Сборка приложения и Docker-образов
    mvn clean package
    docker-compose build

  ### 2. Запуск всех сервисов
    docker-compose up -d

  ### 3. Проверка статуса
    docker-compose ps
  
После этого в терминале отобразятся статусы, названия и id всех контейнеров в системе. Для билда приложения была добавлена зависимость

```
  app:
    build:
      dockerfile: Dockerfile
```
---

В *docker-compose.yaml* и был создан сам ***Dockerfile*** для запуска приложения с указанием пути по конфигурационному файлу.✨ 
В файле *prometheus.yaml* были добавлены job'ы ***kafka,node,postgres,prometheus,app*** для сбора метрик.✨ 

---
Для того, чтобы отследить, с каких сервисов приходят метрики, необходимо перейти по ссылке *http://localhost:9090/targets* сразу после запуска приложения. Рабочие сервисы отображены в статусе ***UP***:

![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/Screenshot_271.png)

### Все ссылки для метрик подключенных сервисов:
- *http://localhost:9090* - прометей
- *http://localhost:9090/targets* - таргеты прометея (видимость передачи метрик контейнеров).
- *http://localhost:9187/metrics* - метрики postgres.
- *http://localhost:9308/metrics* - метрики kafka.
- *http://localhost:8080/actuator/prometheus* - все метрики приложения.
- *http://localhost:3000/* - порт графаны.
---
* Далее переходим в ***Grafana***. Нажимаем *import dashboard*, средством вывода метрик берём prometheus и указываем его хост *http://localhost:9090*, при импорте указываем id дашборда (***1860*** - node, ***7589*** - kafka, ***9628*** - postgres):

![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/Screenshot_.png)

---
* Проверим, что kafka и postgres имеют синхронизацию с grafana, запущены и передают метрики корректно. Для этого выведем 2 ***promql***: *pg_up* для постгреса (должно быть 1) и *kafka_brokers* (должно быть 1).
  
![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/scr2.png)

![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/scr4.png)

Системные метрики перед началом теста (дашборд ***1860*** - node):

![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/Screenshot3.png)

Метрики postgreSQL (дашборд ***9628*** - postgres):

![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/Screenshot33.png)

Метрики kafka (дашборд ***7589*** - kafka):

![docker](https://raw.githubusercontent.com/Kaledo1337/LTAPP/xs5/images/screen55.png)

🎉 


🎆 🎊 
