import { axiosServiceWithAuth } from '@/api/index.js';

function getTeam(projectId, callback, errorCallback) {
  axiosServiceWithAuth
    .get('/team', { params: { projectId: projectId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function getMyTeam(projectId, memberId, callback, errorCallback) {
  axiosServiceWithAuth
    .get('/team/myTeam', { params: { projectId: projectId, memberId: memberId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function getProjectMembers(projectId, callback, errorCallback) {
  axiosServiceWithAuth
    .get('/team/members', { params: { projectId: projectId } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function createTeam(userData, callback, errorCallback) {
  axiosServiceWithAuth
    .post('/team', userData)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function modifyTeam(userData, callback, errorCallback) {
  axiosServiceWithAuth
    .put('/team', userData)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function teamNameCheck(userData, callback, errorCallback) {
  axiosServiceWithAuth
    .get('/team/ncheck', { params: { projectId: userData.projectId, teamName: userData.teamName } })
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

function modifyProjectTeamName(data, callback, errorCallback) {
  axiosServiceWithAuth
    .patch('/team', data)
    .then((res) => {
      callback(res.data);
    })
    .catch((err) => {
      errorCallback(err);
    });
}

export { getTeam, getMyTeam, getProjectMembers, createTeam, teamNameCheck, modifyTeam, modifyProjectTeamName };
