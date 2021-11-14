import { axiosServiceWithAuth } from '@/api/index.js';

function getProfile(memberId, callback, errorCallback) {
  axiosServiceWithAuth
    .get('/mypage', { params: { memberId: memberId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function updateProfile(data, callback, errorCallback) {
  axiosServiceWithAuth
    .put('/mypage', data)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

export { getProfile, updateProfile };
