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

export { getTeam };
