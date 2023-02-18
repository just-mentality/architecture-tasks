#!/bin/sh
clear

helm uninstall myapp
wait

kubectl delete all --all
wait

kubectl delete pvc $(kubectl get pvc | awk '{print $1}' | grep myapp)
wait