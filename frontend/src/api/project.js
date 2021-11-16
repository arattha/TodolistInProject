import { axiosServiceWithAuth } from '@/api/index.js';

function getProjectList(isDone, memberId, callback, errorCallback) {
  axiosServiceWithAuth
    .get("/project/getList", { params: { isDone: isDone, memberId: memberId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function addProject(data, memberId, callback, errorCallback) {
  axiosServiceWithAuth
    .post("/project/addProject/" + memberId, data)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function changeDoneProject(projectId, callback, errorCallback) {
  axiosServiceWithAuth
    .put("/project/changeDone/" , projectId )
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

export { getProjectList, addProject, changeDoneProject };
