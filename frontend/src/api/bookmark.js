import { axiosServiceWithAuth } from '.';

async function getBookmark(userData, callback, errorCallback) {
  await axiosServiceWithAuth
    .get('/bookmark', { params: { projectId: userData.projectId , memberId: userData.memberId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

async function addBookmark(userData, callback, errorCallback) {
  await axiosServiceWithAuth
    .post('/bookmark', userData)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

async function deleteBookmark(userData, callback, errorCallback) {
  await axiosServiceWithAuth
    .put('/bookmark', userData)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

export { getBookmark , addBookmark ,deleteBookmark};