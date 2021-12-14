# financas-api ->BACKEND 
Backend api-application in Java<br>
Java Open-JDK 11<br>
Maven 3.6.3 <br>
SpringBoot Project 2.6.1<br>
####Dependências: <br>
Lombok (necessário plugin de acordo com sua IDE)<br>
Data JPA (Persistence) <br>
Starter Web<br>
H2 Database (utilizado apenas para testes)<br>
PostgreSQL Driver (banco principal)


#Database
###PostgreSQL<br>

Utilizei um container com docker, deixarei o comando logo abaixo, mas se prefirir pode usar o standalone.

Docker comando: `docker run --name bd-postgre -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=123 -e POSTGRES_DB=financas-db -p 5432:5432 -d postgres`<br>
Cria um container postgresql com database -> `financas-db` , caso deseja mudar o nome da database 
basta trocar no argumento `POSTGRES_DB=` não esquecer de mudar no `application.properties`.
 Para acessar o banco pode utilizar um gerenciador de bancos: `Dbeaver` , `Datagrip` ou `psql entrando no container`, as configurações de conexão se encontram no `application.properties`.<br>
```
localhost:5432/financas-db
username : postgres
password: 123
```
###Script utilizado para criar as tabelas no banco:<br>

####Tabela Usuário:
```
create table usuario (
id bigserial NOT NULL PRIMARY KEY ,
nome character varying(150),
email character varying(100),
senha character varying(20),
data_cadastro date DEFAULT now()
);
```

####Tabela Lançamento: <br>
```
create table lancamento(
id bigserial NOT NULL PRIMARY KEY ,
descricao character varying(100) NOT NULL ,
mes integer NOT NULL ,
valor numeric(16, 2),
tipo character varying(20) CHECK (tipo in ('RECEITA', 'DESPESA')) NOT NULL ,
status character varying(20) CHECK ( status in ('PENDENTE', 'CANCELADO', 'EFETIVADO') ) NOT NULL ,
id_usuario bigint REFERENCES usuario (id),
data_cadastro date DEFAULT now()
);
```

Caso não deseja rodar os scripts na mão, basta descomentar (tirar #) no application.properties:<br>

`#spring.jpa.hibernate.ddl-auto=update` <br>

Que ao rodar o projeto ele irá verificar se as tabelas existem e caso não existir ele criará.