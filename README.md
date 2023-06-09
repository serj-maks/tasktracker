![Build Status](https://github.com/serj-maks/tasktracker/actions/workflows/ci.yml/badge.svg) ![codecov](https://codecov.io/gh/serj-maks/tasktracker/branch/main/graph/badge.svg?token=P7AUZZHWST)

## simple spring-boot task tracker app
### how to start
- open terminal, enter command: `docker compose up -d`
- start app via App.java

### how to see database set
- open browser, enter URL: `http://localhost:8081/`
  
![pgadmin-04](https://github.com/serj-maks/tasktracker/blob/main/readme-image/pgadmin-04.png?raw=true)
```
login: 123@test.com
pass: 123
```

- register server

![pgadmin-01](https://github.com/serj-maks/tasktracker/blob/main/readme-image/pgadmin-01.png?raw=true)

- enter server name

![pgadmin-02](https://github.com/serj-maks/tasktracker/blob/main/readme-image/pgadmin-02.png?raw=true)
```
name: test
```

- enter credentials
 
![pgadmin-03](https://github.com/serj-maks/tasktracker/blob/main/readme-image/pgadmin-03.png?raw=true)
```
hostname: db
port: 5432
maintenance database: tasktracker
username: postgres
password: password
```

### how to see swagger docs
- open browser, enter URL: `http://localhost:8080/swagger-ui/index.html`

![swagger-01](https://github.com/serj-maks/tasktracker/blob/main/readme-image/swagger-01.png?raw=true)
