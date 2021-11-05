import { axiosService } from '@/api/index.js';

function getTodoContent(todoId, callback, errorCallback) {
  axiosService
    .get('/todo_content', { params: { id: todoId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function deleteTodoContent(todoId, callback, errorCallback) {
  axiosService
    .delete('/todo_content', { params: { id: todoId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function modifyTodoContent(todo_content, callback, errorCallback) {
  axiosService
    .put('/todo_content', { id: todo_content.id, contents: todo_content.contents })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function createTodoContent(todo_content, callback, errorCallback) {
  axiosService
    .put('/todo_content', {
      todoId: todo_content.todoId,
      memberId: todo_content.memberId,
      contents: todo_content.contents,
    })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function getTodoUrls(todoId, callback, errorCallback) {
  axiosService
    .get('/todo_content/url', { params: { id: todoId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function getTodoRecords(todoId, callback, errorCallback) {
  axiosService
    .get('/todo_content/record', { params: { id: todoId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

export {
  getTodoContent,
  deleteTodoContent,
  createTodoContent,
  modifyTodoContent,
  getTodoUrls,
  getTodoRecords,
};