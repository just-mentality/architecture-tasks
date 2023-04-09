#!/bin/sh
clear

alias mi='minikube'
alias ku='kubectl'
wait

# mi delete
# wait

mi start
wait

mi status
wait

ku create namespace myapp
wait

ku config set-context --current --namespace=myapp
wait

mi docker-env
wait

eval $(minikube -p minikube docker-env)
wait

docker ps
wait

cd kubernetes
helm dependency update ./eternality-chart
wait

helm install myapp ./eternality-chart --dry-run > will_create.log
wait

helm install myapp ./eternality-chart
wait

ku get all
wait

service_ip=$(minikube service myapp-eternality-chart -n myapp --url)
wait

echo
curl $service_ip
echo
wait

ku create namespace m   # сделаем отдельный namespace 
wait

helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx/ 
wait

helm repo update
wait

helm install nginx ingress-nginx/ingress-nginx --namespace m -f nginx-ingress.yaml
wait

ku get all
wait

ku get ing
wait

external_ip=$(mi ip)
echo "external ip = $external_ip"

cd ..