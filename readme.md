[TOC]

# Инструкция для Linux окружения
## Сборка сервиса и push образа
Выполнить
```bash
./run.sh
```
## Работа с kubernetes
#### Создадим синонимы:
```bash
alias mi='minikube'
alias ku='kubectl'
```

#### Запустим minikube:
```bash
mi start --driver=hyperv
mi config set driver hyperv
```

#### Проверим, что запустился:
```bash
mi status
```

#### Создадим namespace для наших изысканий с приложением и посмотрим чего есть:
```bash
ku create namespace myapp
ku config set-context --current --namespace=myapp
ku get pods -A
```

#### Подключимся к docker-y minikube-а
```bash
mi docker-env
eval $(minikube -p minikube docker-env)
docker ps
```

#### Выкачаем и установим Nginx Ingress Controller вместо того, чтобы использовать встроенный в minikube. Для этого нам понадобиться также установить helm (считаем, что уже установлен):
```bash
ku create namespace m   # сделаем отдельный namespace 
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/ 
helm repo update
helm install nginx ingress-nginx/ingress-nginx --namespace m -f nginx-ingress.yaml
```

#### Создадим сервис и деплоймент:
```bash
ku apply -f deployment.yaml -f service.yaml
ku get all
```

#### Проверим, что можно достучаться:
```bash
mi service eterna1ity-health-service --url -n myapp # допустим получили в ответе 172.25.193.130:31287
curl http://172.25.193.130:31287/health
```

#### Добавляем ingress:
```bash
ku apply -f ingress.yaml
ku get ing
```
#### Получаем внешний ip-ник, допустим `172.25.197.86`:
```bash
mi ip
```
#### Сохраняем в etc/hosts:
```
172.25.197.86 arch.homework
```

#### Проверяем, что всё работает (при этом помним, что настроен rewrite):
##### Через browser
```bash
curl http://arch.homework/otusapp/aleksandr
```
Ответ:
```json
Alive from eterna1ity-health-deployment-fff886897-d2wj2!
```

```bash
curl http://arch.homework/otusapp/aleksandr/health
```
Ответ:
```json
{"status": "OK"}
```
#### Через postman
```bash
cd postman
newman run otus-arch-tasks.postman_collection.json
```

#### Удаляем minikube
```bash
mi delete
```