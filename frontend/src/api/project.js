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

export { getProjectList };
