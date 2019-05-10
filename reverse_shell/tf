#!/bin/bash

TF_VAR_public_key=$(cat ~/.ssh/id_rsa.pub) terraform $@
echo
echo ===================
echo === SSH Command ===
echo ===================
echo ssh ec2-user@$(aws --region us-west-1 ec2 describe-instances --filters  "Name=tag:Name,Values=VulnadoReverseShellReceiver" | jq -r '.Reservations[0].Instances[0]|.PublicIpAddress')
