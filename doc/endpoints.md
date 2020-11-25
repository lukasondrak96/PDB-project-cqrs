# Endpoints
## Commands
POST /command/users/new - vytvořit ucet
GET /command/users/{id}/activate
GET /command/users/{id}/deactivate - deaktivovat ucet
PUT /command/users/{id}/edit

POST /command/groups/new - vytvořit skupinu
DELETE /command/groups/{id}/delete
PUT /command/groups/{id}/edit
PUT /command/groups/{id}/change-state
GET /command/groups/{id}/add-user/{id}
GET /command/groups/{id}/remove-user/{id}
GET /command/groups/{id}/change-admin/{id}

POST /command/posts/new
PUT /command/posts/{id}/edit
DELETE /command/posts/{id}/delete

POST /command/comments/new
PUT /command/comments/{id}/edit
DELETE /command/comments/{id}/delete

POST /command/messages/new


## Queries
GET /query/users

GET /query/users/{id}

GET /query/users/{id}/groups/member

GET /query/users/{id}/groups/admin

GET /query/groups - zobrazit ID, jmeno, stav vsech skupin

GET /query/groups/{id}/members

GET /query/groups/{id}/admin

GET /query/groups/{id}/posts

GET /query/posts/{id}/comments

GET /query/users/{id}/conversations

GET /query/users/{id}/conversations/{id}

