# Предусловия
Возможно придётся каждый sh файл прогнать через `dos2nix`:
```shell
dos2unix run.sh
dos2unix install.sh
dos2unix run_tests.sh
dos2unix delete.sh
```

# Сборка docker образа и push
По идее эту команду выполнять не нужно, но если очень хочется...
```shell
./run.sh
```

# Накат домашней работы
```shell
./install.sh
```
После выполнения скрипта в конце `output`-а будет что-то типа
```shell
external ip = 172.25.194.202  # example
```
Взять ip и занести его в файл `/etc/hosts`:

```shell
172.25.194.202  arch.homework
```

То ли там ingress долго подымается (поле ip команды `kubectl get ing`), то ли что, 
но можно выполнить вот это и вроде сразу починится (хоть и напишет, что уже всё существует):
```shell
cd kubernetes
helm install nginx ingress-nginx/ingress-nginx --namespace m -f nginx-ingress.yaml
```

Запуск тестов:
```shell
./run_tests.sh
```

Url openApi:
```shell
http://arch.homework/otusapp/aleksandr/api-docs
```
Почему-то swagger в образе не работает (при локальном запуске без образа работает, видимо из-за security):
```shell
http://arch.homework/otusapp/aleksandr/swagger-ui/index.html # does not work :(
```

После успешных тестов можно выполнить:

# Удаление созданного
Запустить:
```shell
./delete.sh
```
Можно дополнительно выполнить:
```shell
minikube delete
```