# Reverse Shell Listener

A reverse shell is a shell obtained by reaching out from a victim host to a separate host waiting to receive connections. This terraform code allows you to spin up your own reverse shell listener so you can have a way to interact with a target host. To spin this up make sure you have the ability to create networking components and instances on AWS.

```
export TF_VAR_public_key=$(cat ~/.ssh/id_rsa.pub)
terraform apply
```

To get into the instance

```
ssh ec2-user@$(aws --region us-west-2 ec2 describe-instances --filters "Name=tag:Name,Values=VulnadoReverseShellReceiver" | jq -r '.Reservations[0].Instances[0]|.PublicIpAddress')
```

Once you're on the host, execute the following to start listening for connections on port 443. This should just hang waiting for connections. If you need to exit, just hit Ctrl+C.

```
[ec2-user@ip-10-42-0-83 ~]$ sudo nc -l -p 443
```

To finish the test

```
terraform destroy
```
