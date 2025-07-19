# 📋 API de Cadastro de Usuário - Spring Boot

API REST feita com **Spring Boot** para cadastro e gerenciamento de usuários, incluindo **endereços**, **telefones** e autenticação via **JWT**.

---

## 🚀 Tecnologias Utilizadas

- **Java + Spring Boot**
- **Gradle** (gerenciador de dependências)
- **PostgreSQL** (banco de dados)
- **JJWT** (autenticação JWT)

---

## 📦 Dependências Utilizadas

- `Spring Web` – Criação da API REST  
- `Spring Security` – Autenticação e autorização com JWT  
- `Spring Data JPA` – ORM com Hibernate  
- `Spring DevTools` – Reload automático para desenvolvimento  
- `Lombok` – Reduz código boilerplate  
- `jjwt-api`, `jjwt-impl`, `jjwt-jackson` – Geração e validação de JWT

---

## 🧾 Funcionalidades

- Cadastro de **usuário** com:
  - `nome`
  - `sobrenome`
  - `dataNascimento`
  - `email`
  - `senha`
- Cadastro de **endereços** vinculados ao usuário
- Cadastro de **telefones** vinculados ao usuário
- Login com autenticação **JWT**
- **Somente com o token JWT** é possível:
  - Cadastrar ou atualizar dados (endereço & telefone)
  - O **email é extraído do token** JWT e usado para associar os dados ao usuário correto

---

## 🔐 Autenticação

- Após criar o usuário, faça login:
  ```
  POST /usuario/login
  ```
  Envie `email` e `senha`, e receba o **token JWT**

- Esse token deve ser enviado no **Header Authorization** dos próximos requests:
  ```
  Authorization: Bearer seu_token
  ```

---

## 🧪 Endpoints Básicos

| Método | Rota                                 | Descrição                               |
|--------|--------------------------------------|-----------------------------------------|
| POST   | `/usuario/criar`                     | Cadastra novo usuário                   |
| POST   | `/usuario/login`                     | Login com email/senha                   |
| GET    | `/usuario/listar`                    | Lista todos os usuários                 |
| POST   | `/usuario/endereco/adicionar`        | Cadastra endereço (JWT obrigatório)     |
| POST   | `/usuario/telefone/adicionar`        | Cadastra telefone (JWT obrigatório)     |
| PUT    | `/usuario/atualizar`                 | Atualiza dados do usuário (via JWT)     |
| GET    | `/usuario/buscar/`                   | Buscar usuario por ID (JWT obrigatório) | 

---

## ✅ Status do Projeto

🚧 Em desenvolvimento. Melhorias previstas:
- Swagger para documentação da API  
- Testes unitários com JUnit  

---

Feito com ☕ por **Kaio Cesar aka Kadoo The Developer**
