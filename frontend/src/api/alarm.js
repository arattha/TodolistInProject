import { axiosServiceWithAuth } from '@/api/index.js';

function removeAlarm(data, callback, errorCallback) {
  axiosServiceWithAuth
    .put('/alarm/check/', Object.values(data.checkList))
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function removeAllAlarm(memberId, callback, errorCallback) {
  axiosServiceWithAuth
    .put('/alarm/checkAll/' + memberId)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

async function getReviewInMypage(userData, callback, errorCallback) {
  await axiosServiceWithAuth
    .get(
      `/alarm/userAlarmList/${userData.id}?page=${userData.page}&size=${userData.size}&sort=${userData.sort}&sort=rid,desc`
    )
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

async function getAlarmCnt(userData, callback, errorCallback) {
  await axiosServiceWithAuth
    .get(`/alarm/getAlarmCnt?mid=${userData}`)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

export { removeAlarm, removeAllAlarm, getReviewInMypage, getAlarmCnt };
