# Web programming lab 4 EJB + Angular2(Primefaces)

### Wildfly server configuration 

- Download your wildfly to helios 
- Throw ports to your local pc  

`ssh -p 2222 helios.se.ifmo.ru -L 12345:localhost:8080`\
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; △△△△&emsp;&emsp;&emsp;&emsp;&emsp;&ensp; △△△ \
&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;`local port`&emsp;&emsp;`port on helios`

- Run `add_user.sh` in `./bin`
- Create new user to work with admin panel
- In `./modules` download an postgresql driver and place in `./moudles/org/postgres/main`. Also in this directory must be `module.xml` file with:  

``` 
<module xmlns="urn:jboss:module:1.9" name="org.postgres">
	<resources> 
		<resource-root path="postgresql-42.3.1.jar"/>
	</resources>
	<dependencies>      
		<module name="javax.api"/>      
		<module name="javax.transaction.api"/> 
	</dependencies> 
</module>
```
**Inside wildfly workspace**
```
modules
├── org
│   └── postgres
│   	└── main
│	    ├── module.xml
│	    └── postgresql-42.3.1.jar
└── system
    ├── ...
    └── ...

```

- Create datasource (name of db, jdbc driver and this url `jdbc:postgresql://pg:5432/studs` to connect to helios db)
> If you want that it work without changing a `persistence.xml` file,  name datasource as `PostgresDS`.
> Change `persistence.xml` to fit your needs

- In `standalone.xml` change the ports of `admin console` and your web application `http` (default: 9990, 8080; change to anything you want  from 4000-65000)
- Place the builded web app (`WAR`) in `./standalone/deployments`

### Running an app

``` sh
ssh -p 2222 helios.se.ifmo.ru -L <local port of server(backend)>:localhost:<port of server>
ssh -p 2222 helios.se.ifmo.ru -L <local port of web app(frontend)>:localhost:<port of web app>
bash ./<path of wildfly standalone.sh>
npm start (in path where the frontend content is)
```
> I recommend run an web application in different terminals
