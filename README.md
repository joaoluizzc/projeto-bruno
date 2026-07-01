# Projeto Bruno

Projeto simples em Java com Spring Boot para atender a atividade de consumo de endpoint pelo frontend.

## O que foi implementado

- Backend REST com `GET`, `POST`, `PUT` e `DELETE`.
- Entidade academica `Professor` com `nome`, `email` e `materia`.
- `GET /api/professores`: lista todos os registros em uma tabela no frontend.
- `POST /api/professores`: cadastra professor e rejeita nome duplicado.
- `GET /api/professores/{id}`: busca por ID.
- `PUT /api/professores/{id}`: o frontend busca o ID e preenche os campos antes de atualizar.
- `DELETE /api/professores/{id}`: o frontend busca o ID e mostra confirmacao antes de excluir.
- Banco H2 em arquivo, sem precisar instalar PostgreSQL.

## Como rodar

No PowerShell, dentro desta pasta:

```powershell
.\gradlew.bat bootRun
```

Depois acesse:

```text
http://localhost:8080
```

## Endpoints para testar no Postman

Listar:

```text
GET http://localhost:8080/api/professores
```

Buscar por ID:

```text
GET http://localhost:8080/api/professores/1
```

Cadastrar:

```text
POST http://localhost:8080/api/professores
Content-Type: application/json

{
  "nome": "Joao Souza",
  "email": "joao.souza@faculdade.edu",
  "materia": "Java"
}
```

Atualizar:

```text
PUT http://localhost:8080/api/professores/1
Content-Type: application/json

{
  "nome": "Ana Martins",
  "email": "ana.martins@faculdade.edu",
  "materia": "Spring Boot"
}
```

Excluir:

```text
DELETE http://localhost:8080/api/professores/1
```
